/**
 * Description:用户出借服务
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:45:13
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.financialadvisor;

import java.util.Date;
import java.util.List;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.customize.EvalationCustomize;
import com.hyjf.mybatis.model.auto.UserEvalationBehavior;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.QuestionCustomize;

public interface FinancialAdvisorService extends BaseService {

    UserEvalationResultCustomize getUserEvalationResultByUserId(Integer userId);

    List<QuestionCustomize> getNewQuestionList();

    void updateUserEvalationBehavior(UserEvalationBehavior userEvalationBehavior);

    UserEvalationBehavior insertUserEvalationBehavior(Integer userId, String string);

    UserEvalationResultCustomize selectUserEvalationResultByUserId(Integer userId);

    void deleteUserEvalationResultByUserId(Integer userId);

    int countScore(List<String> answerList);

    EvalationCustomize getEvalationByCountScore(int countScore);

    UserEvalationResultCustomize insertUserEvalationResult(List<String> answerList, List<String> questionList,
                                                           EvalationCustomize evalationCustomize, int countScore, Integer userId, UserEvalationResultCustomize oldUserEvalationResultCustomize, Date evaluationExpiredTime);
    
    int ThirdPartySaveUserEvaluationResults(Users user, Integer evalationScoreCount, EvalationCustomize evalationCustomize, String instCode, String channel, Date evaluationExpiredTime);

    EvalationCustomize getEvalationByEvalationType(String evalationType);


}
