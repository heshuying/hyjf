package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.SiteMsgLog;
import com.hyjf.mybatis.model.auto.SiteMsgLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SiteMsgLogMapper {
    int countByExample(SiteMsgLogExample example);

    int deleteByExample(SiteMsgLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SiteMsgLog record);

    int insertSelective(SiteMsgLog record);

    List<SiteMsgLog> selectByExample(SiteMsgLogExample example);

    SiteMsgLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SiteMsgLog record, @Param("example") SiteMsgLogExample example);

    int updateByExample(@Param("record") SiteMsgLog record, @Param("example") SiteMsgLogExample example);

    int updateByPrimaryKeySelective(SiteMsgLog record);

    int updateByPrimaryKey(SiteMsgLog record);
}