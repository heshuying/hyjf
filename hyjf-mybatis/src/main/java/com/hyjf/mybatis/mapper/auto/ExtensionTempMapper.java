package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ExtensionTemp;
import com.hyjf.mybatis.model.auto.ExtensionTempExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ExtensionTempMapper {
    int countByExample(ExtensionTempExample example);

    int deleteByExample(ExtensionTempExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ExtensionTemp record);

    int insertSelective(ExtensionTemp record);

    List<ExtensionTemp> selectByExample(ExtensionTempExample example);

    ExtensionTemp selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ExtensionTemp record, @Param("example") ExtensionTempExample example);

    int updateByExample(@Param("record") ExtensionTemp record, @Param("example") ExtensionTempExample example);

    int updateByPrimaryKeySelective(ExtensionTemp record);

    int updateByPrimaryKey(ExtensionTemp record);
}