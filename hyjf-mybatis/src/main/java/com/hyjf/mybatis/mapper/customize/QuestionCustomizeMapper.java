package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.QuestionCustomize;
import com.hyjf.mybatis.model.customize.app.NewAppQuestionCustomize;

public interface QuestionCustomizeMapper {

    List<QuestionCustomize> getQuestionList();

    int countScore(List<String> answerList);

    List<String> getTypeList();

    List<QuestionCustomize> getNewQuestionList();

    List<NewAppQuestionCustomize> getNewAppQuestionList();
}