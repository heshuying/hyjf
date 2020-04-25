package com.hyjf.admin.manager.config.borrow.finmancharge;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BorrowFinmanCharge;

public interface FinmanChargeService extends BaseService {

	/**
	 * 获取手续费列表列表
	 * 
	 * @return
	 */
	public List<BorrowFinmanCharge> getRecordList(FinmanChargeBean finmanChargeBean, int limitStart, int limitEnd);

	/**
	 * 获取单个手续费列表维护
	 * 
	 * @return
	 */
	public BorrowFinmanCharge getRecord(String record);

	/**
	 * 手续费列表插入
	 * 
	 * @param record
	 */
	public void insertRecord(FinmanChargeBean record);

	/**
	 * 手续费列表更新
	 * 
	 * @param record
	 */
	public void updateRecord(FinmanChargeBean record);

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

}