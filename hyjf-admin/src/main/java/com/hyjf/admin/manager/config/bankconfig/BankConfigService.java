package com.hyjf.admin.manager.config.bankconfig;

import java.util.List;

import com.hyjf.mybatis.model.auto.BankConfig;

public interface BankConfigService {

    /**
     * 获取手续费列表列表
     * 
     * @return
     */
    public List<BankConfig> getRecordList(BankConfig bankConfig, int limitStart, int limitEnd);

    /**
     * 获取单个手续费列表维护
     * 
     * @return
     */
    public BankConfig getRecord(Integer record);

    /**
     * 根据主键判断手续费列表中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(BankConfig record);

    /**
     * 手续费列表插入
     * 
     * @param record
     */
    public void insertRecord(BankConfig record);

    /**
     * 手续费列表更新
     * 
     * @param record
     */
    public void updateRecord(BankConfig record);
    
    /**
     * 配置删除
     * 
     * @param record
     */
    public void deleteRecord(List<Integer> recordList);


    /**
     * 获取银行列表（快捷卡）
     * 
     * @return
     */
    public List<BankConfig> getBankRecordList();

    
}