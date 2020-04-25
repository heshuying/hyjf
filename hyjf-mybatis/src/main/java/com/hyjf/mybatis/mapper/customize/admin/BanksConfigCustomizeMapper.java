package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;

import com.hyjf.mybatis.model.auto.BanksConfig;
import com.hyjf.mybatis.model.auto.BanksConfigExample;

public interface BanksConfigCustomizeMapper {


    List<BanksConfig> selectByExample(BanksConfigExample example);
    
    List<BanksConfig> selectByQuickPayment(Integer quickPayment);
}