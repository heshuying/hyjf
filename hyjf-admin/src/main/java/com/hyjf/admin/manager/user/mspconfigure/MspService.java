package com.hyjf.admin.manager.user.mspconfigure;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.MspConfigure;

public interface MspService extends BaseService {

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<MspConfigure> getRecordList(Map<String, Object> conditionMap,  int limitStart, int limitEnd);
	
	/**
	 * 获取记录数
	 * @param form
	 * @return
	 */
	public Integer getRecordCount(Map<String, Object> conditionMap);

	/**
	 * 获取单表
	 * 
	 * @return
	 */
	public MspConfigure getRecord(String id);

	/**
	 * 插入
	 * 
	 * @param record
	 */
	public void insertRecord(MspConfigure record);

	/**
	 * 是否重复
	 * 
	 * @return
	 */
	public int sourceIdIsExists(String record);

	/**
	 * 是否重复
	 * 
	 * @return
	 */
	public int sourceNameIsExists(MspConfigure mspConfigure);

	/**
	 * 更新
	 * 
	 * @param record
	 */
	public void updateRecord(MspConfigure mspConfigure);

	/**
	 * 删除
	 * 
	 * @param record
	 */
	public void deleteRecord(String sendCd);

}