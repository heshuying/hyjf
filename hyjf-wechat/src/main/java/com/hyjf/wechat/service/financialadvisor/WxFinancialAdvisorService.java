package com.hyjf.wechat.service.financialadvisor;

import com.hyjf.mybatis.model.customize.EvalationCustomize;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.customize.QuestionCustomize;
import com.hyjf.wechat.base.BaseService;

import java.util.List;

/**
 * Created by cuigq on 2018/2/9.
 */
public interface WxFinancialAdvisorService extends BaseService {

    UserEvalationResultCustomize selectUserEvalationResultByUserId(String userId);

    List<QuestionCustomize> getQuestionList();

    UserEvalationResultCustomize insertResult(String userId, String userAnswer);

    List<EvalationCustomize> getEvalationRecord();
}
