package com.hyjf.admin.manager.activity.actnov2017.draw;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ActJanAnswerList;
import com.hyjf.mybatis.model.auto.ActJanAnswerListExample;
import com.hyjf.mybatis.model.auto.ActJanQuestions;
import com.hyjf.mybatis.model.auto.ActJanQuestionsExample;

@Service
public class ActDrawGuessServiceImpl extends BaseServiceImpl implements ActDrawGuessService {
	
	@Override
	public Integer selectRecordCount(ActDrawGuessBean form) {
	    ActJanAnswerListExample example = getSeachExample(form);
		return actJanAnswerListMapper.countByExample(example);
	}
	
    @Override
    public List<ActJanAnswerList> getRecordList(ActDrawGuessBean form, int limitStart, int limitEnd) {
        ActJanAnswerListExample example = getSeachExample(form);
        example.setOrderByClause(" id desc ");
        example.setLimitStart(limitStart);
        example.setLimitEnd(limitEnd);
        return actJanAnswerListMapper.selectByExample(example);
    }

    /**
     * 条件查询参数
     * @author sunss
     * @param form
     * @return
     */
    private ActJanAnswerListExample getSeachExample(ActDrawGuessBean form) {
        ActJanAnswerListExample example = new ActJanAnswerListExample();
        ActJanAnswerListExample.Criteria cra = example.createCriteria();
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
    public List<ActJanQuestions> getAllQuestions() {
        return actJanQuestionsMapper.selectByExample(new ActJanQuestionsExample());
    }
}
