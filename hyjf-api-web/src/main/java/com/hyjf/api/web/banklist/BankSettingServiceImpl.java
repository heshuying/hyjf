package com.hyjf.api.web.banklist;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.api.web.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.BanksConfig;

@Service
public class BankSettingServiceImpl extends BaseServiceImpl implements BankSettingService {
    /**
     * 获取列表列表
     * 
     * @return
     */
    @Override
    public List<BanksConfig> getBankRecordList(Integer quickPayment) {

       return this.banksConfigCustomizeMapper.selectByQuickPayment(quickPayment); 
    }
   





}
