package com.hyjf.app.user.financialadvisor;


import java.util.List;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.customize.EvalationCustomize;
import com.hyjf.mybatis.model.auto.UserEvalationBehavior;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.QuestionCustomize;
import com.hyjf.mybatis.model.customize.app.NewAppQuestionCustomize;

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
     * 
     * 此创建用户行为
     * @author pcc
     * @param userId
     * @param clientStr 
     * @return
     */
    UserEvalationBehavior insertUserEvalationBehavior(Integer userId, String behavior);
    /**
     * 
     * 修改用户行为
     * @author pcc
     * @param userEvalationBehavior
     */
    void updateUserEvalationBehavior(UserEvalationBehavior userEvalationBehavior);
    /**
     * 
     * 返回用户测评标示
     * @author pcc
     * @param userId
     * @return
     */
    Integer getUserEvalationResultByUserId(int userId);

    /**
     * app改版获取问题列表
     * @return
     */
    List<NewAppQuestionCustomize> getNewAppQuestionList();

    UserEvalationResultCustomize insertEvalationResult(EvalationCustomize evalationCustomize, int countScore, Integer userId, UserEvalationResultCustomize oldUserEvalationResultCustomize);

    void updateUsers(Users users);
}
