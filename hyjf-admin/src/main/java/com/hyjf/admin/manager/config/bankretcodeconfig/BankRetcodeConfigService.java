package com.hyjf.admin.manager.config.bankretcodeconfig;

import java.util.List;

import com.hyjf.mybatis.model.auto.BankReturnCodeConfig;

public interface BankRetcodeConfigService {

	/**
	 * 获取返回码列表列表
	 * 
	 * @return
	 */
	public List<BankReturnCodeConfig> getRecordList(BankReturnCodeConfig bankreturncodeconfig, int limitStart, int limitEnd);

	/**
	 * 获取单个返回码列表维护
	 * 
	 * @return
	 */
	public BankReturnCodeConfig getRecord(BankReturnCodeConfig record);

	/**
	 * 根据参数判断返回码列表中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(BankReturnCodeConfig record);

	/**
	 * 返回码列表插入
	 * 
	 * @param record
	 */
	public void insertRecord(BankReturnCodeConfig record);

	/**
	 * 返回码列表更新
	 * 
	 * @param record
	 */
	public void updateRecord(BankReturnCodeConfig record);

	/**
	 * 配置删除
	 * 
	 * @param record
	 */
	public void deleteRecord(List<Integer> recordList);

	/**
	 * 计算列表总数
	 * 
	 * @param
	 * @return
	 */
	public int countRecord(BankRetcodeConfigBean form);

	/**
	 * 根据主键判断维护中数据是否存在
	 * 
	 * @return
	 */
	boolean isExistsReturnCode(BankReturnCodeConfig record);

}
