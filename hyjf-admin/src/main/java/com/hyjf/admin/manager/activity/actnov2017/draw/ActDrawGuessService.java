package com.hyjf.admin.manager.activity.actnov2017.draw;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.ActJanAnswerList;
import com.hyjf.mybatis.model.auto.ActJanQuestions;

public interface ActDrawGuessService extends BaseService {

	public Integer selectRecordCount(ActDrawGuessBean form);

	public List<ActJanAnswerList> getRecordList(ActDrawGuessBean form, int limitStart, int limitEnd);

    public List<ActJanQuestions> getAllQuestions();

}
