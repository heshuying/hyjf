package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ContentQualify;
import com.hyjf.mybatis.model.auto.ContentQualifyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ContentQualifyMapper {
    int countByExample(ContentQualifyExample example);

    int deleteByExample(ContentQualifyExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ContentQualify record);

    int insertSelective(ContentQualify record);

    List<ContentQualify> selectByExample(ContentQualifyExample example);

    ContentQualify selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ContentQualify record, @Param("example") ContentQualifyExample example);

    int updateByExample(@Param("record") ContentQualify record, @Param("example") ContentQualifyExample example);

    int updateByPrimaryKeySelective(ContentQualify record);

    int updateByPrimaryKey(ContentQualify record);
}