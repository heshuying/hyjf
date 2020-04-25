package com.hyjf.financialadvisor;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mq.ApiMqService;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("financialAdvisorService")
public class FinancialAdvisorServiceImpl extends BaseServiceImpl implements FinancialAdvisorService {
    @Autowired
    private ApiMqService apiMqService;

    @Override
    public UserEvalationResultCustomize getUserEvalationResultByUserId(Integer userId) {
        UserEvalationResultExampleCustomize example = new UserEvalationResultExampleCustomize();
        example.createCriteria().andUserIdEqualTo(userId);
        List<UserEvalationResultCustomize> userEvalationResultCustomize = userEvalationResultCustomizeMapper.selectByExample(example);
        if (userEvalationResultCustomize != null && userEvalationResultCustomize.size() > 0) {
            return userEvalationResultCustomize.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<QuestionCustomize> getNewQuestionList() {
        List<QuestionCustomize> customizes = questionCustomizeMapper.getNewQuestionList();
        return customizes;
    }

    @Override
    public void updateUserEvalationBehavior(UserEvalationBehavior userEvalationBehavior) {
        UserEvalationBehavior behavior=userEvalationBehaviorMapper.selectByPrimaryKey(userEvalationBehavior.getId());
        userEvalationBehavior.setBehavior(behavior.getBehavior()+","+userEvalationBehavior.getBehavior());
        userEvalationBehaviorMapper.updateByPrimaryKeySelective(userEvalationBehavior);
    }

    @Override
    public UserEvalationBehavior insertUserEvalationBehavior(Integer userId, String behavior) {
        UserEvalationBehavior userEvalationBehavior=new UserEvalationBehavior();
        userEvalationBehavior.setUserId(userId);
        userEvalationBehavior.setStatrTime(new Date());
        userEvalationBehavior.setBehavior(behavior);
        userEvalationBehaviorMapper.insertSelective(userEvalationBehavior);
        return userEvalationBehavior;
    }

    @Override
    public UserEvalationResultCustomize selectUserEvalationResultByUserId(Integer userId) {

        UserEvalationResultExampleCustomize example = new UserEvalationResultExampleCustomize();
        example.createCriteria().andUserIdEqualTo(userId);
        List<UserEvalationResultCustomize> userEvalationResultCustomize = userEvalationResultCustomizeMapper.selectByExample(example);
        if (userEvalationResultCustomize != null && userEvalationResultCustomize.size() > 0) {
            return userEvalationResultCustomize.get(0);
        } else {
            return null;
        }
    }
    @Override
    public void deleteUserEvalationResultByUserId(Integer userId) {
        UserEvalationResultExampleCustomize userEvalationResultExampleCustomize =new UserEvalationResultExampleCustomize();
        userEvalationResultExampleCustomize.createCriteria().andUserIdEqualTo(userId);
        List<UserEvalationResultCustomize> userEvalationResultCustomizes = userEvalationResultCustomizeMapper.selectByExample(userEvalationResultExampleCustomize);
        UserEvalationResultCustomize userEvalationResultCustomize =null;
        if(userEvalationResultCustomizes !=null&& userEvalationResultCustomizes.size()!=0){
            userEvalationResultCustomize = userEvalationResultCustomizes.get(0);
        }else{
            return;
        }
        UserEvalationExample userEvalationExample=new UserEvalationExample();
        userEvalationExample.createCriteria().andErIdEqualTo(userEvalationResultCustomize.getId());
        userEvalationMapper.deleteByExample(userEvalationExample);
        userEvalationResultCustomizeMapper.deleteByPrimaryKey(userEvalationResultCustomize.getId());
        
        
    }
    
    @Override
    public int countScore(List<String> answerList) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", answerList);
        int countScore = questionCustomizeMapper.countScore(answerList);
        return countScore;
    }
    
    @Override
    public EvalationCustomize getEvalationByCountScore(int countScore) {
        EvalationExampleCustomize example = new EvalationExampleCustomize();
        example.createCriteria().andScoreUpLessThanOrEqualTo(countScore).andScoreDownGreaterThanOrEqualTo(countScore).andStatusEqualTo(0);
        List<EvalationCustomize> list=evalationCustomizeMapper.selectByExample(example);
        if(list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }
    @Override
    public UserEvalationResultCustomize insertUserEvalationResult(List<String> answerList, List<String> questionList,
                                                                  EvalationCustomize evalationCustomize, int countScore, Integer userId, UserEvalationResultCustomize oldUserEvalationResultCustomize, Date evaluationExpiredTime) {
        UserEvalationResultCustomize userEvalationResultCustomize =new UserEvalationResultCustomize();
        userEvalationResultCustomize.setUserId(userId);
        userEvalationResultCustomize.setScoreCount(countScore);
        userEvalationResultCustomize.setType(evalationCustomize.getType());
        userEvalationResultCustomize.setSummary(evalationCustomize.getSummary());
        userEvalationResultCustomize.setCreatetime(new Date());
        if(oldUserEvalationResultCustomize !=null){
            userEvalationResultCustomize.setLasttime(oldUserEvalationResultCustomize.getCreatetime());
        }
        
        int i= userEvalationResultCustomizeMapper.insertSelective(userEvalationResultCustomize);
        if(i>0){
            // 更新用户信息
            Users user = this.getUsersByUserId(userId);
            if (user != null){
                user.setIsEvaluationFlag(1);// 已测评
                user.setEvaluationExpiredTime(evaluationExpiredTime);
                // 更新用户是否测评标志位
                this.usersMapper.updateByPrimaryKey(user);
            }
            for (int j = 0; j < answerList.size(); j++) {
                UserEvalation userEvalation=new UserEvalation();
                userEvalation.setErId(userEvalationResultCustomize.getId());
                userEvalation.setQuestionId(new Integer(questionList.get(j)));
                userEvalation.setAnswerId(new Integer(answerList.get(j)));
                userEvalation.setSort(j);
                userEvalationMapper.insertSelective(userEvalation);
            }

            // add 合规数据上报 埋点 liubin 20181122 start
            // 推送数据到MQ 用户信息修改（风险测评）
            JSONObject params = new JSONObject();
            params.put("userId", userId);
            this.apiMqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.USERINFO_CHANGE_DELAY_KEY, JSONObject.toJSONString(params));
            // add 合规数据上报 埋点 liubin 20181122 end
        }
        return userEvalationResultCustomize;
    }
    
    @Override
    public int ThirdPartySaveUserEvaluationResults(Users user, Integer evalationScoreCount,
                                                   EvalationCustomize evalationCustomize, String instCode, String channel, Date evaluationExpiredTime) {
            UserEvalationResultCustomize oldUserEvalationResultCustomize =this.selectUserEvalationResultByUserId(user.getUserId());
            
            deleteUserEvalationResultByUserId(user.getUserId());
            UserEvalationResultCustomize userEvalationResultCustomize =new UserEvalationResultCustomize();
            userEvalationResultCustomize.setUserId(user.getUserId());
            userEvalationResultCustomize.setScoreCount(evalationScoreCount);
            userEvalationResultCustomize.setType(evalationCustomize.getType());
            userEvalationResultCustomize.setSummary(evalationCustomize.getSummary());
            userEvalationResultCustomize.setCreatetime(new Date());
            HjhInstConfig hjhInstConfig=this.selectInstConfigByInstCode(instCode);
            if(hjhInstConfig!=null){
                userEvalationResultCustomize.setInstCode(hjhInstConfig.getInstCode());
                userEvalationResultCustomize.setInstName(hjhInstConfig.getInstName());
            }
            if(oldUserEvalationResultCustomize !=null){
                userEvalationResultCustomize.setLasttime(oldUserEvalationResultCustomize.getCreatetime());
            }
            int insertCount =  userEvalationResultCustomizeMapper.insertSelective(userEvalationResultCustomize);
            if (insertCount > 0){
                Users users = this.getUsersByUserId(user.getUserId());
                if(users != null){
                    users.setIsEvaluationFlag(1);
                    // 获取测评到期日期
                    user.setEvaluationExpiredTime(evaluationExpiredTime);
                    this.usersMapper.updateByPrimaryKey(users);
                }

                // add 合规数据上报 埋点 liubin 20181122 start
                // 推送数据到MQ 用户信息修改（风险测评）
                JSONObject params = new JSONObject();
                params.put("userId", user.getUserId());
                this.apiMqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.USERINFO_CHANGE_DELAY_KEY, JSONObject.toJSONString(params));
                // add 合规数据上报 埋点 liubin 20181122 end
            }
            return insertCount;
    }

    @Override
    public EvalationCustomize getEvalationByEvalationType(String evalationType) {
        EvalationExampleCustomize example = new EvalationExampleCustomize();
        example.createCriteria().andTypeEqualTo(evalationType).andStatusEqualTo(0);
        List<EvalationCustomize> list=evalationCustomizeMapper.selectByExample(example);
        if(list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }

}
