package com.hyjf.wechat.service.financialadvisor;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.hyjf.bank.service.evalation.EvaluationService;
import com.hyjf.bank.service.mq.MqService;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.common.util.StringUtil;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.*;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.wechat.base.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by cuigq on 2018/2/9.
 */
@Service
public class WxFinanciaAdvisorServiceImpl extends BaseServiceImpl implements WxFinancialAdvisorService {

    @Autowired
    private MqService mqService;
    @Autowired
    EvaluationService evaluationService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public UserEvalationResultCustomize selectUserEvalationResultByUserId(String userId) {
        UserEvalationResultExampleCustomize example = new UserEvalationResultExampleCustomize();
        example.createCriteria().andUserIdEqualTo(Integer.parseInt(userId));
        List<UserEvalationResultCustomize> userEvalationResultCustomize = userEvalationResultCustomizeMapper.selectByExample(example);
        if (userEvalationResultCustomize != null && userEvalationResultCustomize.size() > 0) {
            return userEvalationResultCustomize.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<QuestionCustomize> getQuestionList() {
        List<QuestionCustomize> lstQuestion = questionCustomizeMapper.getQuestionList();

        for (QuestionCustomize question : lstQuestion) {
            Collections.sort(question.getAnswers(), new Comparator<AnswerCustomize>() {
                @Override
                public int compare(AnswerCustomize o1, AnswerCustomize o2) {
                    return o1.getAnswer().compareTo(o2.getAnswer());
                }
            });
        }

        return lstQuestion;
    }

    /**
     * 提交答案
     *
     * @param userId
     * @param userAnswer
     * @return
     */
    @Override
    public UserEvalationResultCustomize insertResult(String userId, String userAnswer) {
        UserEvalationResultCustomize userEvalationResultCustomize = this.selectUserEvalationResultByUserId(userId);
        if (userEvalationResultCustomize == null) {
            //初次评测，發優惠卷
            String sendCouponResult = sendCoupon(userId, CustomConstants.CLIENT_WECHAT);
            if (!Strings.isNullOrEmpty(sendCouponResult)) {
                JSONObject resultObj = JSONObject.parseObject(sendCouponResult);
                if (resultObj.getIntValue("status") == 0 && resultObj.getIntValue("couponCount") > 0) {
                    logger.info("为用户【{}】发放优惠卷成功！", userId);
                } else {
                    logger.info("为用户【{}】发放优惠卷失败，返回值【{}】！", userId, sendCouponResult);
                    //TODO 要不要异常抛出？
                }
            } else {
                logger.info("为用户【{}】发放优惠卷返回值为空！", userId);
            }
        } else {
            //清理评测结果
            UserEvalationExample userEvalationExample = new UserEvalationExample();
            userEvalationExample.createCriteria().andErIdEqualTo(userEvalationResultCustomize.getId());
            userEvalationMapper.deleteByExample(userEvalationExample);
            userEvalationResultCustomizeMapper.deleteByPrimaryKey(userEvalationResultCustomize.getId());
        }


        int countScore = 0;
        if (!Strings.isNullOrEmpty(userAnswer)) {
        	logger.info("为用户"+userId+",测评结果"+userAnswer);
            Map<String, String> mapQuestionAnswer = Splitter.on(',').trimResults().withKeyValueSeparator("_").split(userAnswer);

            //计算得分
            List<String> lstChosen = new ArrayList(mapQuestionAnswer.values());
            countScore = questionCustomizeMapper.countScore(lstChosen);
        }


        //根据分数查询用户出借类型"稳健/进取。。。。"
        EvalationExampleCustomize example = new EvalationExampleCustomize();
        example.createCriteria().andScoreUpLessThanOrEqualTo(countScore).andScoreDownGreaterThanOrEqualTo(countScore).andStatusEqualTo(0);
        List<EvalationCustomize> lstEvalationCustomize = evalationCustomizeMapper.selectByExample(example);
        Preconditions.checkArgument(lstEvalationCustomize.size() == 1);
        EvalationCustomize evalationCustomize = lstEvalationCustomize.get(0);

        //構造評測結果對象
        UserEvalationResultCustomize userEvaluationResult = new UserEvalationResultCustomize();
        userEvaluationResult.setUserId(Integer.parseInt(userId));
        userEvaluationResult.setScoreCount(countScore);
        userEvaluationResult.setType(evalationCustomize.getType());
        userEvaluationResult.setSummary(evalationCustomize.getSummary());
        userEvaluationResult.setCreatetime(new Date());
        int i = userEvalationResultCustomizeMapper.insertSelective(userEvaluationResult);

        //返回测评金额上限
        switch (evalationCustomize.getType()){
            case "保守型":
                userEvaluationResult.setRevaluationMoney(StringUtil.getTenThousandOfANumber(
                        Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE)).intValue() == 0.0D ? 0 :
                                Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE)).intValue()));
                break;
            case "稳健型":
                userEvaluationResult.setRevaluationMoney(StringUtil.getTenThousandOfANumber(
                        Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS)).intValue() == 0.0D ? 0:
                                Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS)).intValue()));
                break;
            case "成长型":
                userEvaluationResult.setRevaluationMoney(StringUtil.getTenThousandOfANumber(
                        Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_GROWTH)).intValue() == 0.0D ? 0:
                                Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_GROWTH)).intValue()));
                break;
            case "进取型":
                userEvaluationResult.setRevaluationMoney(StringUtil.getTenThousandOfANumber(
                        Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE)).intValue() == 0.0D ? 0:
                                Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE)).intValue()));
                break;
            default:
                userEvaluationResult.setRevaluationMoney("");
        }

        if (i > 0) {
            // 更新用户信息
            Users user = this.getUsers(Integer.parseInt(userId));
            if (user != null) {
                // 已测评
                user.setIsEvaluationFlag(1);
                // 获取测评到期日期
                Date evaluationExpiredTime = evaluationService.selectEvaluationExpiredTime(new Date());
                user.setEvaluationExpiredTime(evaluationExpiredTime);
                this.usersMapper.updateByPrimaryKey(user);
            }

            if(!Strings.isNullOrEmpty(userAnswer)){
                Map<String, String> mapQuestionAnswer = Splitter.on(',').trimResults().withKeyValueSeparator("_").split(userAnswer);
                int sortIndex = 0;
                for (Map.Entry<String, String> entry : mapQuestionAnswer.entrySet()) {
                    UserEvalation userEvalation = new UserEvalation();
                    userEvalation.setErId(userEvaluationResult.getId());
                    userEvalation.setQuestionId(new Integer(entry.getKey()));
                    userEvalation.setAnswerId(new Integer(entry.getValue()));
                    userEvalation.setSort(sortIndex);
                    userEvalationMapper.insertSelective(userEvalation);
                    sortIndex++;
                }
            }

            // add 合规数据上报 埋点 liubin 20181122 start
            // 推送数据到MQ 用户信息修改（风险测评）
            JSONObject params = new JSONObject();
            params.put("userId", userId);
            this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.USERINFO_CHANGE_DELAY_KEY, JSONObject.toJSONString(params));
            // add 合规数据上报 埋点 liubin 20181122 end

        }
        return userEvaluationResult;
    }

    /**
     * 发放优惠券
     *
     * @param userId
     * @param platform
     * @return
     */
    private String sendCoupon(String userId, String platform) {
        String activityId = CustomConstants.ACTIVITY_ID;
        // 活动有效期校验
        String resultActivity = checkActivityIfAvailable(activityId);
        // 终端平台校验
        String resultPlatform = checkActivityPlatform(activityId, platform);
        logger.info("为用户"+userId+",活动有效期校验"+resultActivity);
        logger.info("为用户"+userId+",终端平台校验"+resultPlatform);
        // String
        String result = StringUtils.EMPTY;
        if (StringUtils.isEmpty(resultActivity) && StringUtils.isEmpty(resultPlatform)) {
            CommonParamBean couponParamBean = new CommonParamBean();
            // 用户编号
            couponParamBean.setUserId(userId);
            // 评测送加息券
            couponParamBean.setSendFlg(1);
            // 发放优惠券（1张加息券）
            result = CommonSoaUtils.sendUserCoupon(couponParamBean);
        }
        return result;
    }

    //继承自WEB、APP评测发放优惠卷逻辑
    private String checkActivityIfAvailable(String activityId) {
        if (activityId == null) {
            return CouponCheckUtilDefine.ACTIVITYID_IS_NULL;
        }
        ActivityList activityList = activityListMapper.selectByPrimaryKey(new Integer(activityId));
        if (activityList == null) {
            return CouponCheckUtilDefine.ACTIVITY_ISNULL;
        }
        if (activityList.getTimeStart() > GetDate.getNowTime10()) {
            return CouponCheckUtilDefine.ACTIVITY_TIME_NOT_START;
        }
        if (activityList.getTimeEnd() < GetDate.getNowTime10()) {
            return CouponCheckUtilDefine.ACTIVITY_TIME_END;
        }
        return "";
    }

    private String checkActivityPlatform(String activityId, String platform) {
        if (activityId == null) {
            return CouponCheckUtilDefine.ACTIVITYID_IS_NULL;
        }
        ActivityList activityList = activityListMapper.selectByPrimaryKey(new Integer(activityId));
        if (activityList.getPlatform().indexOf(platform) == -1) {

            // 操作平台
            List<ParamName> clients = this.getParamNameList("CLIENT");
            // 被选中操作平台
            String clientSed[] = StringUtils.split(activityList.getPlatform(), ",");
            StringBuffer selectedClientDisplayBuffer = new StringBuffer();
            for (String client : clientSed) {
                // 被选中的平台编号
                for (ParamName pn : clients) {
                    if (StringUtils.equals(pn.getNameCd(), client)) {
                        // 被选中的平台名称 表示用
                        selectedClientDisplayBuffer.append(pn.getName());
                        selectedClientDisplayBuffer.append("/");
                    }
                }

            }
            return CouponCheckUtilDefine.PLATFORM_LIMIT.replace("***", selectedClientDisplayBuffer.toString());
        }
        return "";
    }

    /**
     * 获取评分标准列表
     * @return
     * @author Michael
     */
    @Override
    public List<EvalationCustomize> getEvalationRecord() {
        EvalationExampleCustomize example = new EvalationExampleCustomize();
        example.createCriteria().andStatusEqualTo(0);
        // 获取评分标准对应的上限金额并拼接
        List<EvalationCustomize> eval = evalationCustomizeMapper.selectByExample(example);
        for(EvalationCustomize evalStr : eval){
            switch (evalStr.getType()){
                case "保守型":
                    evalStr.setRevaluationMoney(StringUtil.getTenThousandOfANumber(
                            Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE)).intValue() == 0.0D ? 0 :
                                    Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE)).intValue()));
                    break;
                case "稳健型":
                    evalStr.setRevaluationMoney(StringUtil.getTenThousandOfANumber(
                            Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS)).intValue() == 0.0D ? 0:
                                    Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS)).intValue()));
                    break;
                case "成长型":
                    evalStr.setRevaluationMoney(StringUtil.getTenThousandOfANumber(
                            Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_GROWTH)).intValue() == 0.0D ? 0:
                                    Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_GROWTH)).intValue()));
                    break;
                case "进取型":
                    evalStr.setRevaluationMoney(StringUtil.getTenThousandOfANumber(
                            Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE)).intValue() == 0.0D ? 0:
                                    Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE)).intValue()));
                    break;
                default:
                    evalStr.setRevaluationMoney("0");
            }
        }
        return eval;
    }
}
