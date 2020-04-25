package com.hyjf.admin.manager.config.repaystyle;

import java.util.List;

import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.BorrowStyleWithBLOBs;

public interface RepayStyleService {

	/**
	 * 获取权限列表
	 * 
	 * @return
	 */
	public List<BorrowStyleWithBLOBs> getRecordList(RepayStyleBean borrowstyle, int limitStart, int limitEnd);

	/**
	 * 获取单个权限维护
	 * 
	 * @return
	 */
	public BorrowStyle getRecord(BorrowStyle record);
	/**
	 * 根据ID获取单个还款方式
	 * @param recordID
	 * @return
	 */
	public BorrowStyleWithBLOBs getRecordById(Integer recordID);

	/**
	 * 根据主键判断权限维护中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(BorrowStyle record);

	/**
	 * 根据主键判断权限维护中权限是否存在
	 * 
	 * @return
	 */
	public boolean isExistsPermission(BorrowStyle record);

	/**
	 * 权限维护插入
	 * 
	 * @param record
	 */
	public void insertRecord(BorrowStyleWithBLOBs record);

	/**
	 * 权限维护更新
	 * 
	 * @param record
	 */
	public void updateRecord(BorrowStyleWithBLOBs record);

	/**
	 * 权限维护删除
	 * 
	 * @param record
	 */
	public void deleteRecord(List<String> recordList);

	/**
	 * @param form
	 * @return
	 */
		
	public int countRecordTotal(RepayStyleBean form);
}
