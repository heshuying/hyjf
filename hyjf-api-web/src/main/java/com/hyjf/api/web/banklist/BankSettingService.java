package com.hyjf.api.web.banklist;

import java.util.List;

import com.hyjf.mybatis.model.auto.BanksConfig;

public interface BankSettingService {

    /**
     * 获取银行列表（快捷卡）
     * 
     * @return
     */
    public List<BanksConfig> getBankRecordList(Integer quickPayment);

    
}