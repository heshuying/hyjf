package com.hyjf.admin.manager.activity.actoct2017.actquestion;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ActQuestions;
import com.hyjf.mybatis.model.auto.ActQuestionsAnswerlist;
import com.hyjf.mybatis.model.auto.ActQuestionsAnswerlistExample;
import com.hyjf.mybatis.model.auto.ActQuestionsExample;

@Service
public class ActQuestionTenServiceImpl extends BaseServiceImpl implements ActQuestionTenService {
	
	@Override
	public Integer selectRecordCount(ActQuestionTenBean form) {
	    ActQuestionsAnswerlistExample example = getSeachExample(form);
		return actQuestionsAnswerlistMapper.countByExample(example);
	}
	
    @Override
    public List<ActQuestionsAnswerlist> getRecordList(ActQuestionTenBean form, int limitStart, int limitEnd) {
        ActQuestionsAnswerlistExample example = getSeachExample(form);
        example.setOrderByClause(" id desc ");
        example.setLimitStart(limitStart);
        example.setLimitEnd(limitEnd);
        return actQuestionsAnswerlistMapper.selectByExample(example);
    }

    /**
     * 条件查询参数
     * @author sunss
     * @param form
     * @return
     */
    private ActQuestionsAnswerlistExample getSeachExample(ActQuestionTenBean form) {
        ActQuestionsAnswerlistExample example = new ActQuestionsAnswerlistExample();
        ActQuestionsAnswerlistExample.Criteria cra = example.createCriteria();
        if(!Validator.isNull(form.getUserName())){
            cra.andUserNameEqualTo(form.getUserName());
        }
        if(!Validator.isNull(form.getMobile())){
            cra.andMobileEqualTo(form.getMobile());
        }
        if(!Validator.isNull(form.getQuestionId())){
            cra.andQuestionIdEqualTo(form.getQuestionId());
        }
        return example;
    }

    /**
     * 查询所有的问题列表
     * @author sunss
     * @return
     * @see com.hyjf.admin.manager.activity.actoct2017.actquestion.ActQuestionTenService#getAllQuestions()
     */
    @Override
    public List<ActQuestions> getAllQuestions() {
        return actQuestionsMapper.selectByExample(new ActQuestionsExample());
    }
}
