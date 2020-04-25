package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.NewyearQuestionUser;
import com.hyjf.mybatis.model.auto.NewyearQuestionUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NewyearQuestionUserMapper {
    int countByExample(NewyearQuestionUserExample example);

    int deleteByExample(NewyearQuestionUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(NewyearQuestionUser record);

    int insertSelective(NewyearQuestionUser record);

    List<NewyearQuestionUser> selectByExample(NewyearQuestionUserExample example);

    NewyearQuestionUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NewyearQuestionUser record, @Param("example") NewyearQuestionUserExample example);

    int updateByExample(@Param("record") NewyearQuestionUser record, @Param("example") NewyearQuestionUserExample example);

    int updateByPrimaryKeySelective(NewyearQuestionUser record);

    int updateByPrimaryKey(NewyearQuestionUser record);
}