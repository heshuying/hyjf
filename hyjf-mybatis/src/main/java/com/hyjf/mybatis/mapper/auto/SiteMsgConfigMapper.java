package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.SiteMsgConfig;
import com.hyjf.mybatis.model.auto.SiteMsgConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SiteMsgConfigMapper {
    int countByExample(SiteMsgConfigExample example);

    int deleteByExample(SiteMsgConfigExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SiteMsgConfig record);

    int insertSelective(SiteMsgConfig record);

    List<SiteMsgConfig> selectByExample(SiteMsgConfigExample example);

    SiteMsgConfig selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SiteMsgConfig record, @Param("example") SiteMsgConfigExample example);

    int updateByExample(@Param("record") SiteMsgConfig record, @Param("example") SiteMsgConfigExample example);

    int updateByPrimaryKeySelective(SiteMsgConfig record);

    int updateByPrimaryKey(SiteMsgConfig record);
}