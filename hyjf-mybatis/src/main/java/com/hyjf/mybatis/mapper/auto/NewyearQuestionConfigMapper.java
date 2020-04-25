package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.NewyearQuestionConfig;
import com.hyjf.mybatis.model.auto.NewyearQuestionConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NewyearQuestionConfigMapper {
    int countByExample(NewyearQuestionConfigExample example);

    int deleteByExample(NewyearQuestionConfigExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(NewyearQuestionConfig record);

    int insertSelective(NewyearQuestionConfig record);

    List<NewyearQuestionConfig> selectByExample(NewyearQuestionConfigExample example);

    NewyearQuestionConfig selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NewyearQuestionConfig record, @Param("example") NewyearQuestionConfigExample example);

    int updateByExample(@Param("record") NewyearQuestionConfig record, @Param("example") NewyearQuestionConfigExample example);

    int updateByPrimaryKeySelective(NewyearQuestionConfig record);

    int updateByPrimaryKey(NewyearQuestionConfig record);
}