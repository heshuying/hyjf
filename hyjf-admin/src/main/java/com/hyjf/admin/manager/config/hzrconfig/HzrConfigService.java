package com.hyjf.admin.manager.config.hzrconfig;

import java.util.List;

import com.hyjf.mybatis.model.auto.HzrConfig;

public interface HzrConfigService {

	/**
	 * 获取汇转让配置列表
	 * 
	 * @return
	 */
	public List<HzrConfig> getRecordList(HzrConfigBean hzrconfig, int limitStart, int limitEnd);

	/**
	 * 获取单个取汇转让配置
	 * 
	 * @return
	 */
	public HzrConfig getRecord(Integer record);

	/**
	 * 根据主键判断取汇转让配置中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(HzrConfigBean record);

	/**
	 * 取汇转让配置插入
	 * 
	 * @param record
	 */
	public void insertRecord(HzrConfigBean record);

	/**
	 * 取汇转让配置更新
	 * 
	 * @param record
	 */
	public void updateRecord(HzrConfigBean record);

	/**
	 * 汇转让配置删除
	 * 
	 * @param record
	 */
	public void deleteRecord(List<Integer> recordList);

	/**
	 * 编号是否已经存在（存在：true）
	 * 
	 * @param record
	 * @return
	 */
	public boolean isExistsCode(HzrConfigBean record);
}
