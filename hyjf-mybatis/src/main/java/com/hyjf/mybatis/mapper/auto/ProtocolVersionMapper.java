package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ProtocolVersion;
import com.hyjf.mybatis.model.auto.ProtocolVersionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProtocolVersionMapper {
    int countByExample(ProtocolVersionExample example);

    int deleteByExample(ProtocolVersionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProtocolVersion record);

    int insertSelective(ProtocolVersion record);

    List<ProtocolVersion> selectByExample(ProtocolVersionExample example);

    ProtocolVersion selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProtocolVersion record, @Param("example") ProtocolVersionExample example);

    int updateByExample(@Param("record") ProtocolVersion record, @Param("example") ProtocolVersionExample example);

    int updateByPrimaryKeySelective(ProtocolVersion record);

    int updateByPrimaryKey(ProtocolVersion record);
}