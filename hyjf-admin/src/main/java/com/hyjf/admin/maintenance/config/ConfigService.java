package com.hyjf.admin.maintenance.config;

import java.util.List;

import com.hyjf.mybatis.model.auto.Config;

public interface ConfigService {

	/**
	 * 获取配置列表
	 * 
	 * @return
	 */
	public List<Config> getRecordList(Config borrowConfig, int limitStart, int limitEnd);

	/**
	 * 获取单个配置维护
	 * 
	 * @return
	 */
	public Config getRecord(Config record);

	/**
	 * 根据主键判断配置中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(Config record);

	/**
	 * 配置插入
	 * 
	 * @param record
	 */
	public void insertRecord(Config record);

	/**
	 * 配置更新
	 * 
	 * @param record
	 */
	public void updateRecord(Config record);

	/**
	 * 配置删除
	 * 
	 * @param record
	 */
	public void deleteRecord(List<String> recordList);
}
