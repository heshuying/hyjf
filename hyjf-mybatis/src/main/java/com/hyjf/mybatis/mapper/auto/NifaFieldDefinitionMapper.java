package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.NifaFieldDefinition;
import com.hyjf.mybatis.model.auto.NifaFieldDefinitionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NifaFieldDefinitionMapper {
    int countByExample(NifaFieldDefinitionExample example);

    int deleteByExample(NifaFieldDefinitionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(NifaFieldDefinition record);

    int insertSelective(NifaFieldDefinition record);

    List<NifaFieldDefinition> selectByExample(NifaFieldDefinitionExample example);

    NifaFieldDefinition selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NifaFieldDefinition record, @Param("example") NifaFieldDefinitionExample example);

    int updateByExample(@Param("record") NifaFieldDefinition record, @Param("example") NifaFieldDefinitionExample example);

    int updateByPrimaryKeySelective(NifaFieldDefinition record);

    int updateByPrimaryKey(NifaFieldDefinition record);
}