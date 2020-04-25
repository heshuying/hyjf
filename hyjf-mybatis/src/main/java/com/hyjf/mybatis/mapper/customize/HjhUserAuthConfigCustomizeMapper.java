package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.auto.HjhUserAuthConfigCustomize;

public interface HjhUserAuthConfigCustomizeMapper {

    List<HjhUserAuthConfigCustomize> selectCustomizeAuthConfigList(@Param("limitStart") int limitStart, @Param("limitEnd") int limitEnd);
}