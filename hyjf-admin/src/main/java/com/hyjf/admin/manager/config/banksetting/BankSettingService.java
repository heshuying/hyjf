package com.hyjf.admin.manager.config.banksetting;

import java.util.List;

import com.hyjf.mybatis.model.auto.BanksConfig;

public interface BankSettingService {

    /**
     * 获取手续费列表列表
     * 
     * @return
     */
    public List<BanksConfig> getRecordList(BanksConfig bankConfig, int limitStart, int limitEnd);

    /**
     * 获取单个手续费列表维护
     * 
     * @return
     */
    public BanksConfig getRecord(Integer record);

    /**
     * 根据主键判断手续费列表中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(BanksConfig record);

    /**
     * 手续费列表插入
     * 
     * @param record
     */
    public void insertRecord(BanksConfig record);

    /**
     * 手续费列表更新
     * 
     * @param record
     */
    public void updateRecord(BanksConfig record);
    
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
    public List<BanksConfig> getBankRecordList();
    /**
     * 导出报表
     * 
     * @return
     */
    public List<BanksConfig> exportRecordList(BanksConfig record);
    
}