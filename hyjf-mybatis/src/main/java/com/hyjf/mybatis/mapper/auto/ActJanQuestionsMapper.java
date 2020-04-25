package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActJanQuestions;
import com.hyjf.mybatis.model.auto.ActJanQuestionsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActJanQuestionsMapper {
    int countByExample(ActJanQuestionsExample example);

    int deleteByExample(ActJanQuestionsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActJanQuestions record);

    int insertSelective(ActJanQuestions record);

    List<ActJanQuestions> selectByExample(ActJanQuestionsExample example);

    ActJanQuestions selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActJanQuestions record, @Param("example") ActJanQuestionsExample example);

    int updateByExample(@Param("record") ActJanQuestions record, @Param("example") ActJanQuestionsExample example);

    int updateByPrimaryKeySelective(ActJanQuestions record);

    int updateByPrimaryKey(ActJanQuestions record);
}