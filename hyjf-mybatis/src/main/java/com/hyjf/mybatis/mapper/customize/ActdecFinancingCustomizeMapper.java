package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.auto.ActdecFinancing;
import com.hyjf.mybatis.model.auto.ActdecFinancingExample;
import com.hyjf.mybatis.model.customize.ActdecFinancingCustomizeExample;

import java.util.List;

public interface ActdecFinancingCustomizeMapper {

    List<ActdecFinancing> selectByExample(ActdecFinancingCustomizeExample example);


}