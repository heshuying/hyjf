package com.hyjf.admin.maintenance.paramname;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.ParamName;

public interface ParamNameService extends BaseService {

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<ParamName> getRecordList(ParamNameBean paramNameBean, int limitStart, int limitEnd);

	/**
	 * 获取列表size
	 * 
	 * @return
	 */
	public Integer getRecordListSize(ParamNameBean paramNameBean);

	/**
	 * 获取单个
	 * 
	 * @return
	 */
	public ParamName getRecord(ParamName record);

	/**
	 * 插入
	 * 
	 * @param record
	 */
	public void insertRecord(ParamNameBean record);

	/**
	 * 更新
	 * 
	 * @param record
	 */
	public void updateRecord(ParamNameBean record);

	/**
	 * 删除
	 * 
	 * @param record
	 */
	public void deleteRecord(ParamName record);

	/**
	 * 是否重复
	 * 
	 * @param isExists
	 */
	public boolean isExists(ParamNameBean record);

}