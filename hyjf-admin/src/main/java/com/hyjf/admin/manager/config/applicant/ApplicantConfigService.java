package com.hyjf.admin.manager.config.applicant;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.ConfigApplicant;

public interface ApplicantConfigService extends BaseService{

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<ConfigApplicant> getRecordList(ApplicantConfigBean device, int limitStart, int limitEnd);

	/**
	 * 获得记录条数
	 */
	public Integer countRecord(ApplicantConfigBean record);
	

	/**
	 * 获取单个
	 * 
	 * @return
	 */
	public ConfigApplicant getRecord(Integer id);

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
	public void insertRecord(ApplicantConfigBean form);

	/**
	 * 更新
	 * 
	 * @param record
	 */
	public void updateRecord(ApplicantConfigBean form);

	/**
	 * 删除
	 * 
	 * @param ids
	 */
	public void deleteRecord(List<Integer> ids);
    
    
}
