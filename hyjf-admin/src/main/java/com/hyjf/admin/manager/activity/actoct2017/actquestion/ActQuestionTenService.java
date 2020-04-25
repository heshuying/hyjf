package com.hyjf.admin.manager.activity.actoct2017.actquestion;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.ActQuestions;
import com.hyjf.mybatis.model.auto.ActQuestionsAnswerlist;

public interface ActQuestionTenService extends BaseService {

	public Integer selectRecordCount(ActQuestionTenBean form);

	public List<ActQuestionsAnswerlist> getRecordList(ActQuestionTenBean form, int limitStart, int limitEnd);

    public List<ActQuestions> getAllQuestions();

}
