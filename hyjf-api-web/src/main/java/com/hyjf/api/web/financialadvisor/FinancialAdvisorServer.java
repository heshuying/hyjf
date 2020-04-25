package com.hyjf.api.web.financialadvisor;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.evalation.EvaluationService;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.coupon.util.CouponCheckUtil;
import com.hyjf.financialadvisor.FinancialAdvisorBean;
import com.hyjf.financialadvisor.FinancialAdvisorDefine;
import com.hyjf.financialadvisor.FinancialAdvisorResultBean;
import com.hyjf.financialadvisor.FinancialAdvisorService;
import com.hyjf.mybatis.model.customize.EvalationCustomize;
import com.hyjf.mybatis.model.auto.UserEvalationBehavior;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.QuestionCustomize;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = FinancialAdvisorDefine.REQUEST_MAPPING)
public class FinancialAdvisorServer extends BaseController {

    @Autowired
    private FinancialAdvisorService financialAdvisorService;
    @Autowired
    private CouponCheckUtil couponCheckUtil;
    @Autowired
    EvaluationService evaluationService;
    /**
     *
     * 返回用户是否测评标识
     * @author pcc
     * @param financialAdvisorBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = FinancialAdvisorDefine.GET_USEREVALATIONRESULT_BY_USERID)
    public FinancialAdvisorResultBean getUserEvalationResultByUserId(@ModelAttribute FinancialAdvisorBean financialAdvisorBean, HttpServletRequest request, HttpServletResponse response) {
    	LogUtil.startLog(this.getClass().getName(), FinancialAdvisorDefine.GET_USEREVALATIONRESULT_BY_USERID);
    	FinancialAdvisorResultBean resultBean = new FinancialAdvisorResultBean();

        //验证请求参数
        if (Validator.isNull(financialAdvisorBean.getUserId()) ) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }

        //验签
        if(!this.checkSign(financialAdvisorBean, BaseDefine.METHOD_GET_USEREVALATIONRESULT_BY_USERID)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        UserEvalationResultCustomize userEvalationResultCustomize =financialAdvisorService.getUserEvalationResultByUserId(financialAdvisorBean.getUserId());
        if(userEvalationResultCustomize !=null){
            // 获取用户信息
            Users user = this.financialAdvisorService.getUsersByUserId(financialAdvisorBean.getUserId());
			//获取评测时间加一年的毫秒数18.2.2评测 19.2.2
			Long lCreate = user.getEvaluationExpiredTime().getTime();
			//获取当前时间加一天的毫秒数 19.2.1以后需要再评测19.2.2
			Long lNow = System.currentTimeMillis();
			if (lCreate <= lNow) {
				//已过期需要重新评测
	            resultBean.setUserEvaluationResultFlag(2);
			} else {
				//未到有效期
	            resultBean.setUserEvaluationResultFlag(1);
	            resultBean.setUserEvalationResultJson(JSONObject.toJSONString(userEvalationResultCustomize));
			}
        }else{
            resultBean.setUserEvaluationResultFlag(0);
        }
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);

        LogUtil.endLog(this.getClass().getName(), FinancialAdvisorDefine.GET_USEREVALATIONRESULT_BY_USERID);
        return resultBean;
    }
    
    
    /**
     * 
     * 返回问题答案列表
     * @author pcc
     * @param financialAdvisorBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = FinancialAdvisorDefine.GET_QUESTION_LIST)
    public FinancialAdvisorResultBean getQuestionList(@ModelAttribute FinancialAdvisorBean financialAdvisorBean, HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(this.getClass().getName(), FinancialAdvisorDefine.GET_QUESTION_LIST);
        FinancialAdvisorResultBean resultBean = new FinancialAdvisorResultBean();
        
        //验证请求参数
        if (Validator.isNull(financialAdvisorBean.getBehaviorId())||
                Validator.isNull(financialAdvisorBean.getBehavior())) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        
        //验签
        if(!this.checkSign(financialAdvisorBean, BaseDefine.METHOD_GET_QUESTION_LIST)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        List<QuestionCustomize> list=financialAdvisorService.getNewQuestionList();
        resultBean.setQuestionList(list);
        // 统计ID
        String behaviorId = financialAdvisorBean.getBehaviorId();
        // 记录用户按钮点击
        String behavior = financialAdvisorBean.getBehavior();
        if(behaviorId!=null&&behaviorId.length()!=0){
            if("0".equals(behavior)){
                behavior="现在测评";
            }else if("1".equals(behavior)){
                behavior="再测一次";
            }else{
                behavior="其他按钮";
            }
            behavior=createNewDate()+"用户点击按钮"+behavior;
            UserEvalationBehavior userEvalationBehavior=new UserEvalationBehavior();
            userEvalationBehavior.setId(Integer.parseInt(behaviorId));
            userEvalationBehavior.setEndTime(new Date());
            userEvalationBehavior.setBehavior(behavior);
            financialAdvisorService.updateUserEvalationBehavior(userEvalationBehavior);
        }
        
        resultBean.setBehaviorId(behaviorId);

        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), FinancialAdvisorDefine.GET_QUESTION_LIST);
        return resultBean;
    }
    
    /**
     * 
     * 用户测评结束
     * @author pcc
     * @param financialAdvisorBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = FinancialAdvisorDefine.USER_EVALATION_END)
    public FinancialAdvisorResultBean userEvalationEnd(@ModelAttribute FinancialAdvisorBean financialAdvisorBean, HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(this.getClass().getName(), FinancialAdvisorDefine.USER_EVALATION_END);
        FinancialAdvisorResultBean resultBean = new FinancialAdvisorResultBean();
        
        //验证请求参数
        if (Validator.isNull(financialAdvisorBean.getUserId())||
                Validator.isNull(financialAdvisorBean.getUserAnswer())||
                Validator.isNull(financialAdvisorBean.getBehaviorId())) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        
        //验签
        if(!this.checkSign(financialAdvisorBean, BaseDefine.METHOD_USER_EVALATION_END)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        Integer userId = financialAdvisorBean.getUserId(); // 用户ID
//      Integer userId = 2;

        String userAnswer = financialAdvisorBean.getUserAnswer();
        Map<String,Object> returnMap = this.answerAnalysisAndCoupon(userAnswer, userId);
        if(returnMap.get("sendCount") != null){
            int sendCount = (int)returnMap.get("sendCount");
            resultBean.setSendCount(sendCount);
            resultBean.setSendResult((String)returnMap.get("sendResult"));
        }
        UserEvalationResultCustomize userEvalationResultCustomize = (UserEvalationResultCustomize)returnMap.get("userEvalationResult");
        resultBean.setUserEvaluationResultFlag(0);
        resultBean.setUserEvalationResultJson(JSONObject.toJSONString(userEvalationResultCustomize));
      
        // 统计ID
        String behaviorId = financialAdvisorBean.getBehaviorId();
        // 记录用户按钮点击
        String behavior = "";
        if(behaviorId!=null&&behaviorId.length()!=0){
            
            behavior=createNewDate()+"用户点击按钮完成问卷";
            UserEvalationBehavior userEvalationBehavior=new UserEvalationBehavior();
            userEvalationBehavior.setId(Integer.parseInt(behaviorId));
            userEvalationBehavior.setEndTime(new Date());
            userEvalationBehavior.setBehavior(behavior);
            financialAdvisorService.updateUserEvalationBehavior(userEvalationBehavior);
        }

        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), FinancialAdvisorDefine.USER_EVALATION_END);
        return resultBean;
    }
    
    /**
     * 
     * 用户测评行为记录
     * @author pcc
     * @param financialAdvisorBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = FinancialAdvisorDefine.USER_EVALUATION_BEHAVIOR)
    public FinancialAdvisorResultBean userEvaluationBehavior(@ModelAttribute FinancialAdvisorBean financialAdvisorBean, HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(this.getClass().getName(), FinancialAdvisorDefine.USER_EVALUATION_BEHAVIOR);
        FinancialAdvisorResultBean resultBean = new FinancialAdvisorResultBean();
        
        //验证请求参数
        if (Validator.isNull(financialAdvisorBean.getBehaviorId())||Validator.isNull(financialAdvisorBean.getBehavior()) ) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        
        //验签
        if(!this.checkSign(financialAdvisorBean, BaseDefine.METHOD_USER_EVALUATION_BEHAVIOR)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        // 统计ID
        String behaviorId = financialAdvisorBean.getBehaviorId();
        // 统计内容
        String behavior = financialAdvisorBean.getBehavior();
        if(behaviorId!=null&&behaviorId.length()!=0){
            behavior=createNewDate()+"用户选择问题答案为："+behavior;
            UserEvalationBehavior userEvalationBehavior=new UserEvalationBehavior();
            userEvalationBehavior.setId(Integer.parseInt(behaviorId));
            userEvalationBehavior.setEndTime(new Date());
            userEvalationBehavior.setBehavior(behavior);
            financialAdvisorService.updateUserEvalationBehavior(userEvalationBehavior);
        }
        

        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), FinancialAdvisorDefine.USER_EVALUATION_BEHAVIOR);
        return resultBean;
    }
    
    /**
     * 
     * 用户调查问卷行为记录开始
     * @author pcc
     * @param financialAdvisorBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = FinancialAdvisorDefine.USER_EVALUATION_BEHAVIOR_STATUS)
    public FinancialAdvisorResultBean userEvaluationBehaviorStatus(@ModelAttribute FinancialAdvisorBean financialAdvisorBean, HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(this.getClass().getName(), FinancialAdvisorDefine.USER_EVALUATION_BEHAVIOR_STATUS);
        FinancialAdvisorResultBean resultBean = new FinancialAdvisorResultBean();
        
        //验证请求参数
        if (Validator.isNull(financialAdvisorBean.getUserId()) ) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        
        //验签
        if(!this.checkSign(financialAdvisorBean, BaseDefine.METHOD_USER_EVALUATION_BEHAVIOR_STATUS)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }

        UserEvalationBehavior userEvalationBehavior=
                financialAdvisorService.insertUserEvalationBehavior(financialAdvisorBean.getUserId(),"微官网端用户进入测评页面"); 
        
        resultBean.setBehaviorId(userEvalationBehavior.getId()+"");
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), FinancialAdvisorDefine.USER_EVALUATION_BEHAVIOR_STATUS);
        return resultBean;
    }
    
    
    
    // 插入评测数据并发券
    public synchronized Map<String,Object> answerAnalysisAndCoupon(String userAnswer,Integer userId){
        Map<String,Object> returnMap  = new HashMap<String,Object>();
        UserEvalationResultCustomize ueResult = financialAdvisorService.selectUserEvalationResultByUserId(userId);
        // 是否已经参加过测评（true：已测评过，false：测评）
        boolean isAdvisor = ueResult != null ? true : false;
        // 1_1,2_8
        UserEvalationResultCustomize userEvalationResultCustomize = answerAnalysis(userAnswer, new Integer(userId));

        if(!isAdvisor){
            String platform = CustomConstants.CLIENT_PC;
            // 发放优惠券
            String result = this.sendCoupon(userId, platform);
            if(StringUtils.isNotEmpty(result)){
                JSONObject resultObj = JSONObject.parseObject(result);
                if(resultObj.getIntValue("status") == 0 && resultObj.getIntValue("couponCount") > 0){
                    int sendCount = resultObj.getIntValue("couponCount");
                    returnMap.put("sendCount", sendCount);
                    String sendResult = "恭喜您获得"+resultObj.getIntValue("couponCount")+"张加息券，体验出借流程，获取高额收益，可在我的账户-优惠券中查看";
                    returnMap.put("sendResult", sendResult);
                }
            }
            
        }
        returnMap.put("userEvalationResult", userEvalationResultCustomize);
        return returnMap;

    }
    
    /**
     * 发放优惠券
     * @param userId
     * @param platform
     * @return
     */
    private String sendCoupon(int userId,String platform){
        String activityId = CustomConstants.ACTIVITY_ID;
        // 活动有效期校验
        String resultActivity = couponCheckUtil.checkActivityIfAvailable(activityId);
        // 终端平台校验
        String resultPlatform = couponCheckUtil.checkActivityPlatform(activityId, platform);
        // String 
        String result = StringUtils.EMPTY;
        if(StringUtils.isEmpty(resultActivity)&&StringUtils.isEmpty(resultPlatform)){
            CommonParamBean couponParamBean = new CommonParamBean();
            // 用户编号
            couponParamBean.setUserId(String.valueOf(userId));
            // 评测送加息券
            couponParamBean.setSendFlg(1);
            // 发放优惠券（1张加息券）
            result = CommonSoaUtils.sendUserCoupon(couponParamBean);
        }
        return result;
    }
    public UserEvalationResultCustomize answerAnalysis(String userAnswer, Integer userId) {
        UserEvalationResultCustomize oldUserEvalationResultCustomize =financialAdvisorService.selectUserEvalationResultByUserId(userId);
        financialAdvisorService.deleteUserEvalationResultByUserId(userId);
        
        String[] answer = userAnswer.split(",");
        List<String> answerList = new ArrayList<String>();
        List<String> questionList = new ArrayList<String>();
        for (String string : answer) {
            if(string.split("_").length==2){
                questionList.add(string.split("_")[0]);
                answerList.add(string.split("_")[1]);
            }
            
        }
        int countScore = financialAdvisorService.countScore(answerList);
        EvalationCustomize evalationCustomize = financialAdvisorService.getEvalationByCountScore(countScore);
        // hyjf-api中调用不到bankService的方法、只能从api-web调用处理后传入
        Date evaluationExpiredTime = evaluationService.selectEvaluationExpiredTime(new Date());
        UserEvalationResultCustomize userEvalationResultCustomize = financialAdvisorService.insertUserEvalationResult(answerList,
                questionList, evalationCustomize, countScore, userId, oldUserEvalationResultCustomize,evaluationExpiredTime);
        return userEvalationResultCustomize;
    }
    
    private String createNewDate() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
