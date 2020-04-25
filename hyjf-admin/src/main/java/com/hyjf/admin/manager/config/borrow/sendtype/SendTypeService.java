package com.hyjf.admin.manager.config.borrow.sendtype;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BorrowSendType;

public interface SendTypeService extends BaseService {

	/**
	 * 获取手续费列表列表
	 * 
	 * @return
	 */
	public List<BorrowSendType> getRecordList(SendTypeBean sendTypeBean, int limitStart, int limitEnd);

	/**
	 * 获取单个手续费列表维护
	 * 
	 * @return
	 */
	public BorrowSendType getRecord(String record);

	/**
	 * 手续费列表插入
	 * 
	 * @param record
	 */
	public void insertRecord(SendTypeBean record);

	/**
	 * 手续费列表更新
	 * 
	 * @param record
	 */
	public void updateRecord(SendTypeBean record);

	/**
	 * 配置删除
	 * 
	 * @param record
	 */
	public void deleteRecord(String sendCd);

}