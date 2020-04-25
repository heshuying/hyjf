package com.hyjf.web.user.financialadvisor;


import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.customize.EvalationCustomize;
import com.hyjf.mybatis.model.auto.UserEvalationBehavior;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.customize.QuestionCustomize;
import com.hyjf.web.BaseService;

public interface FinancialAdvisorService extends BaseService {
    /**
     * 
     * 查询调查问卷列表
     * @author pcc
     * @return
     */
    List<QuestionCustomize> getQuestionList();
    /**
     * 
     * 根据用户的id查询问卷结果
     * @author pcc
     * @param userId
     * @return
     */
    UserEvalationResultCustomize selectUserEvalationResultByUserId(Integer userId);
    
    /**
     * 
     * 计算测评分数
     * @author pcc
     * @param answerList
     * @return
     */
    int countScore(List<String> answerList);
    /**
     * 
     * 根据测评分数返回测评结果
     * @author pcc
     * @param countScore
     * @return
     */
    EvalationCustomize getEvalationByCountScore(int countScore);
    
    
    /**
     * 
     * 获取评分标准列表
     * @author pcc
     * @return
     */
    List<EvalationCustomize> getEvalationRecord();
    
    /**
     * 
     * 记录用户测评信息
     * @author pcc
     * @param answerList
     * @param questionList
     * @param evalationCustomize
     * @param countScore
     * @param userId 
     * @param oldUserEvalationResultCustomize
     * @return
     */
    UserEvalationResultCustomize insertUserEvalationResult(List<String> answerList, List<String> questionList, EvalationCustomize evalationCustomize,
                                                           int countScore, Integer userId, UserEvalationResultCustomize oldUserEvalationResultCustomize);
    /**
     * 
     * 返回所有问题类型
     * @author pcc
     * @return
     */
    List<String> getTypeList();
    /**
     * 
     * 删除用户测评结果
     * @author pcc
     * @param userId
     */
    void deleteUserEvalationResultByUserId(Integer userId);
    /**
     * 创建记录用户行为 
     * @author pcc
     * @param userId
     * @param behavior
     * @return
     */
    UserEvalationBehavior insertUserEvalationBehavior(Integer userId, String behavior);
    /**
     * 
     * 记录用户
     * @author pcc
     * @param userEvalationBehavior
     */
    void updateUserEvalationBehavior(UserEvalationBehavior userEvalationBehavior);
    /**
     * 
     * 网站改版获取调查问卷列表
     * @author pcc
     * @return
     */
    List<QuestionCustomize> getNewQuestionList();
    /**
     * 
     *  返回用户是否测评标识
     * @author pcc
     * @param userId
     * @param ret
     */
    void getUserEvalationResultByUserId(Integer userId, JSONObject ret);

    
}
