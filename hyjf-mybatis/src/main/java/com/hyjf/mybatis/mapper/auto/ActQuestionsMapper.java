package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActQuestions;
import com.hyjf.mybatis.model.auto.ActQuestionsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActQuestionsMapper {
    int countByExample(ActQuestionsExample example);

    int deleteByExample(ActQuestionsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActQuestions record);

    int insertSelective(ActQuestions record);

    List<ActQuestions> selectByExample(ActQuestionsExample example);

    ActQuestions selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActQuestions record, @Param("example") ActQuestionsExample example);

    int updateByExample(@Param("record") ActQuestions record, @Param("example") ActQuestionsExample example);

    int updateByPrimaryKeySelective(ActQuestions record);

    int updateByPrimaryKey(ActQuestions record);
}