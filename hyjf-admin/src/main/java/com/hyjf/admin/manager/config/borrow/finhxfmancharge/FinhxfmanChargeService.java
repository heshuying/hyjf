package com.hyjf.admin.manager.config.borrow.finhxfmancharge;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BorrowFinhxfmanCharge;

public interface FinhxfmanChargeService extends BaseService {

	/**
	 * 获取手续费列表列表
	 * 
	 * @return
	 */
	public List<BorrowFinhxfmanCharge> getRecordList(FinhxfmanChargeBean finmanChargeBean, int limitStart, int limitEnd);

	/**
	 * 获取单个手续费列表维护
	 * 
	 * @return
	 */
	public BorrowFinhxfmanCharge getRecord(String record);

	/**
	 * 手续费列表插入
	 * 
	 * @param record
	 */
	public void insertRecord(FinhxfmanChargeBean record);

	/**
	 * 手续费列表更新
	 * 
	 * @param record
	 */
	public void updateRecord(FinhxfmanChargeBean record);

	/**
	 * 配置删除
	 * 
	 * @param record
	 */
	public void deleteRecord(String manchargeCd);

	/**
	 * 类型是否重复
	 * 
	 * @param isExists
	 */
	public boolean isManChargeTypeExists(String chargeTimeType);

	/**
	 * 月数是否是唯一
	 * 
	 */
	public boolean onlyOneMonth(String chargeCd, String chargeTime);

}