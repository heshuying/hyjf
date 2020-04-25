package com.hyjf.admin.coupon.config;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigExoportCustomize;

public interface CouponConfigService extends BaseService {

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<CouponConfigCustomize> getRecordList(CouponConfigCustomize couponConfigCustomize);

	/**
	 * 获得记录数
	 * @param CouponConfigCustomize
	 * @return
	 */
	public Integer countRecord(CouponConfigCustomize couponConfigCustomize);
	
	/**
	 * 获取单表
	 * 
	 * @return
	 */
	public CouponConfig getRecord(String id);

	/**
	 * 插入
	 * 
	 * @param record
	 */
	public void insertRecord(CouponConfigBean record);

	/**
	 * 更新
	 * 
	 * @param record
	 */
	public void updateRecord(CouponConfigBean record);

	/**
	 * 删除
	 * 
	 * @param record
	 */
	public void deleteRecord(String recordId);
	
	/**
	 * 获取项目类别
	 * @return
	 */
	public List<BorrowProjectType> getProjectTypeList();
	
	/**
	 * 获取已发放数量
	 * @param couponCode
	 * @return
	 */
	public Integer getIssueNumber(String couponCode);
	
	
	/**
	 * 导出列表
	 * 
	 * @return
	 */
	public List<CouponConfigExoportCustomize> exoportRecordList(CouponConfigCustomize couponConfigCustomize);

	void auditRecord(CouponConfig record);
	
	int getCouponUsedCount(int couponId);

	/**
	 * 
	 * 校验优惠券发行数量是否已用完
	 * @author hsy
	 * @param couponCode
	 * @param amount 
	 * @return
	 */
    public abstract boolean checkSendNum(String couponCode, String amount);
    
    
    public List<BorrowProjectType> getCouponProjectTypeList();


}