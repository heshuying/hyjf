package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ContentEnvironment;
import com.hyjf.mybatis.model.auto.ContentEnvironmentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ContentEnvironmentMapper {
    int countByExample(ContentEnvironmentExample example);

    int deleteByExample(ContentEnvironmentExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ContentEnvironment record);

    int insertSelective(ContentEnvironment record);

    List<ContentEnvironment> selectByExample(ContentEnvironmentExample example);

    ContentEnvironment selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ContentEnvironment record, @Param("example") ContentEnvironmentExample example);

    int updateByExample(@Param("record") ContentEnvironment record, @Param("example") ContentEnvironmentExample example);

    int updateByPrimaryKeySelective(ContentEnvironment record);

    int updateByPrimaryKey(ContentEnvironment record);
}