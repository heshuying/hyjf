package com.hyjf.mybatis.mapper.customize.act;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.auto.ActdecCorps;
import com.hyjf.mybatis.model.auto.ActdecCorpsExample;


public interface ActdecCorpsCustomizeMapper {


    int updateByExampleSelective(@Param("record") ActdecCorps record, @Param("example") ActdecCorpsExample example);
}