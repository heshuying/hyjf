package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ProtocolLog;
import com.hyjf.mybatis.model.auto.ProtocolLogExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProtocolLogMapper {
    int countByExample(ProtocolLogExample example);

    int deleteByExample(ProtocolLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProtocolLog record);

    int insertSelective(ProtocolLog record);

    List<ProtocolLog> selectByExample(ProtocolLogExample example);

    ProtocolLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProtocolLog record, @Param("example") ProtocolLogExample example);

    int updateByExample(@Param("record") ProtocolLog record, @Param("example") ProtocolLogExample example);

    int updateByPrimaryKeySelective(ProtocolLog record);

    int updateByPrimaryKey(ProtocolLog record);
}