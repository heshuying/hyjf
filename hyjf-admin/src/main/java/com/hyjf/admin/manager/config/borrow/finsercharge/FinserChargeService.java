package com.hyjf.admin.manager.config.borrow.finsercharge;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BorrowFinserCharge;
import com.hyjf.mybatis.model.customize.FinserChargeCustomize;

public interface FinserChargeService extends BaseService {

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<FinserChargeCustomize> getRecordList(FinserChargeBean AdminPermissions, int limitStart, int limitEnd);

	/**
	 * 获取单个维护
	 * 
	 * @return
	 */
	public BorrowFinserCharge getRecord(BorrowFinserCharge record);

	/**
	 * 根据主键判断维护中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(BorrowFinserCharge record);

	/**
	 * 维护插入
	 * 
	 * @param record
	 */
	public void insertRecord(FinserChargeBean record);

	/**
	 * 维护更新
	 * 
	 * @param record
	 */
	public void updateRecord(FinserChargeBean record);

	/**
	 * 根据id删除
	 * 
	 * @param chargeCd
	 */
	public void deleteRecord(String chargeCd);

	/**
	 * 统计个数
	 * 
	 * @param chargeCd
	 */
	public int countRecordTotal(FinserChargeBean form);

	/**
	 * 天标是否是唯一
	 * 
	 */
	public boolean enddayIsExists(String chargeCd,Integer projectType);

	/**
	 * 月数是否是唯一
	 * 
	 */
	public boolean onlyOneMonth(String chargeCd, String chargeTime,Integer projectType);
	
	/**
	 * 相同期限天标唯一
	 * @param chargeCd
	 * @param chargeTime
	 * @param projectType
	 * @return
	 */
	public boolean onlyOneDay(String chargeCd,String chargeTime,Integer projectType);
}
