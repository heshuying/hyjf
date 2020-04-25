/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.batch.recharge.fee;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.RechargeFeeReconciliation;
import com.hyjf.mybatis.model.auto.UserTransfer;
import com.hyjf.mybatis.model.customize.RechargeFeeCustomize;

/**
 * 充值手续费垫付对账
 * @author 李深强
 */
public interface RechargeFeeService extends BaseService{
    /**
     * 查询借款人充值垫付手续费
     * @return
     */
	public List<RechargeFeeCustomize> selectRechargeFeeReconciliationList(RechargeFeeCustomize rechargeFee);
	/**
	 * 插入数据
	 * @param rechargeFee
	 */
	public void insertRechargeFeeReconciliation(RechargeFeeCustomize rechargeFee);
	
	/**
	 * 获取未付款数据
	 */
	public List<RechargeFeeReconciliation> selectFeeListDelay();
	
	/**
	 * 更新数据表
	 * @param rechargeFeeReconciliation
	 */
	public void updateRechargeFeeReconciliation(RechargeFeeReconciliation rechargeFeeReconciliation);
	
	
	/**
	 * 获取转账记录（转账中）
	 */
	public List<UserTransfer> selectTransferingRecord();
	
	/**
	 * 超过4小时更新为已过期
	 * @param userTransfer
	 */
	public void updateTransferRecord(UserTransfer userTransfer);
	
}
