package com.hyjf.admin.manager.config.bankrecharge;

import java.util.List;

import com.hyjf.mybatis.model.auto.BankRechargeLimitConfig;

public interface BankRechargeService {

    /**
     * 获取手续费列表列表
     * 
     * @return
     */
    public List<BankRechargeLimitConfig> getRecordList(BankRechargeLimitConfig BankRechargeLimitConfig, int limitStart, int limitEnd);

    /**
     * 获取单个手续费列表维护
     * 
     * @return
     */
    public BankRechargeLimitConfig getRecord(Integer record);

    /**
     * 根据主键判断手续费列表中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(BankRechargeLimitConfig record);

    /**
     * 手续费列表插入
     * 
     * @param record
     */
    public void insertRecord(BankRechargeLimitConfig record);

    /**
     * 手续费列表更新
     * 
     * @param record
     */
    public void updateRecord(BankRechargeLimitConfig record);
    
    /**
     * 配置删除
     * 
     * @param record
     */
    public void deleteRecord(List<Integer> recordList);
    
    
    /**
     * 导出报表
     * 
     * @return
     */
    public List<BankRechargeLimitConfig> exportRecordList(BankRechargeLimitConfig bankRechargeLimitConfig);


	/**
	 * 判断银行卡是否重复
	 * 
	 * @return
	 */
	public int bankIsExists(Integer bankId, Integer recordId);
	
	/**
	 * 更新快捷充值限额信息
	 * @param bankCode
	 * @param singleTransQuota
	 * @param cardDailyTransQuota
	 */
	public void updateBankRechargeConfig(String bankCode,String singleTransQuota,String cardDailyTransQuota);

}