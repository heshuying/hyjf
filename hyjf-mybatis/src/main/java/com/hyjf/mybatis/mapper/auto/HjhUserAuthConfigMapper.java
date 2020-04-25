package com.hyjf.mybatis.mapper.auto;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.auto.HjhUserAuthConfig;
import com.hyjf.mybatis.model.auto.HjhUserAuthConfigExample;

public interface HjhUserAuthConfigMapper {
	
    int countByExample(HjhUserAuthConfigExample example);

    int deleteByExample(HjhUserAuthConfigExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HjhUserAuthConfig record);

    int insertSelective(HjhUserAuthConfig record);

    List<HjhUserAuthConfig> selectByExample(HjhUserAuthConfigExample example);

    HjhUserAuthConfig selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") HjhUserAuthConfig record, @Param("example") HjhUserAuthConfigExample example);

    int updateByExample(@Param("record") HjhUserAuthConfig record, @Param("example") HjhUserAuthConfigExample example);

    int updateByPrimaryKeySelective(HjhUserAuthConfig record);

    int updateByPrimaryKey(HjhUserAuthConfig record);

}