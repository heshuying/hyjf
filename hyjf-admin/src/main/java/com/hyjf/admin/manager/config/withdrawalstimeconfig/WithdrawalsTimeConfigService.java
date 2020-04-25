package com.hyjf.admin.manager.config.withdrawalstimeconfig;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.WithdrawalsTimeConfig;

public interface WithdrawalsTimeConfigService extends BaseService {

    /**
     * 获取手续费列表列表
     * 
     * @return
     */
    public List<WithdrawalsTimeConfig> getRecordList(WithdrawalsTimeConfig withdrawalsTimeConfig, int limitStart, int limitEnd);

    /**
     * 获取单个手续费列表维护
     * 
     * @return
     */
    public WithdrawalsTimeConfig getRecord(Integer record);



    /**
     * 手续费列表插入
     * 
     * @param record
     */
    public void insertRecord(WithdrawalsTimeConfigBean record);

    /**
     * 手续费列表更新
     * 
     * @param record
     */
    public void updateRecord(WithdrawalsTimeConfigBean record);
    



}