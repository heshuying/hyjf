package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActQuestionsAnswerlist;
import com.hyjf.mybatis.model.auto.ActQuestionsAnswerlistExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActQuestionsAnswerlistMapper {
    int countByExample(ActQuestionsAnswerlistExample example);

    int deleteByExample(ActQuestionsAnswerlistExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActQuestionsAnswerlist record);

    int insertSelective(ActQuestionsAnswerlist record);

    List<ActQuestionsAnswerlist> selectByExample(ActQuestionsAnswerlistExample example);

    ActQuestionsAnswerlist selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActQuestionsAnswerlist record, @Param("example") ActQuestionsAnswerlistExample example);

    int updateByExample(@Param("record") ActQuestionsAnswerlist record, @Param("example") ActQuestionsAnswerlistExample example);

    int updateByPrimaryKeySelective(ActQuestionsAnswerlist record);

    int updateByPrimaryKey(ActQuestionsAnswerlist record);
}