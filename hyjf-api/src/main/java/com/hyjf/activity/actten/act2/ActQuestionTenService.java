package com.hyjf.activity.actten.act2;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.ActQuestions;
import com.hyjf.mybatis.model.auto.ActQuestionsAnswerlist;

public interface ActQuestionTenService extends BaseService {

    /**
     * 根据当前时间查询问题
     * @author sunss
     * @param resultBean
     */
    ActQuestions getQuestionByNowDate();

    /**
     * 根据用户ID查询用户这道题是不是答过了
     * @author sunss
     * @param questionId
     * @param userId 
     * @return
     */
    ActQuestionsAnswerlist getActQuestionsAnswerListByUserId(Integer questionId, Integer userId);

    /**
     * 根据Id查询问题答案
     * @author sunss
     * @param questionId
     * @return
     */
    ActQuestions getQuestionById(Integer questionId);

    /**
     * 记录答题成功日志
     * @author sunss
     * @param requestBean
     */
    void insertActRewardlist(ActQuestionTenRequestBean requestBean);


}
