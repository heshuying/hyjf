package com.hyjf.admin.manager.config.versionconfig;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.Version;

public interface VersionConfigService extends BaseService{

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<Version> getRecordList(VersionConfigBean device, int limitStart, int limitEnd);

	/**
	 * 获得记录条数
	 */
	public Integer countRecord(VersionConfigBean record);
	

	/**
	 * 获取单个
	 * 
	 * @return
	 */
	public Version getRecord(Integer id);

	/**
	 * 根据主键判断数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(Integer id);

	/**
	 * 插入
	 * 
	 * @param record
	 */
	public void insertRecord(VersionConfigBean form);

	/**
	 * 更新
	 * 
	 * @param record
	 */
	public void updateRecord(VersionConfigBean form);

	/**
	 * 删除
	 * 
	 * @param ids
	 */
	public void deleteRecord(List<Integer> ids);
    /**
     * 校验版本号是否唯一
     * @return
     */
	public Version getVersionByCode(Integer vid,Integer type,String version);
}
