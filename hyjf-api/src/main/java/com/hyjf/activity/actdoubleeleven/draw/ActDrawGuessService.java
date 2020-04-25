package com.hyjf.activity.actdoubleeleven.draw;

import java.util.List;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.ActJanAnswerList;
import com.hyjf.mybatis.model.auto.ActJanQuestions;
import com.hyjf.mybatis.model.auto.ActRewardList;

public interface ActDrawGuessService extends BaseService {

    /**
     * 根据当前时间查询问题
     * @author sunss
     * @param resultBean
     */
    ActJanQuestions getQuestionByNowDate();

    /**
     * 根据用户ID查询用户这道题是不是答过了
     * @author sunss
     * @param questionId
     * @param userId 
     * @return
     */
    ActJanAnswerList getActQuestionsAnswerListByUserId(Integer questionId, Integer userId);

    /**
     * 根据Id查询问题答案
     * @author sunss
     * @param questionId
     * @return
     */
    ActJanQuestions getQuestionById(Integer questionId);

    /**
     * 记录答题成功日志
     * @author sunss
     * @param requestBean
     */
    void insertActRewardlist(ActDrawGuessRequestBean requestBean);

    /**
     * 
     * 获得此人已经中了哪个优惠券
     * @author sss
     * @param userId
     * @return
     */
    List<ActRewardList> getHasCoupArrByUserId(Integer userId);


}
