/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.batch.recharge.config;

import com.hyjf.batch.BaseService;

/**
 * 更新快捷充值限额信息
 * @author 李深强
 */
public interface RechargeConfigService extends BaseService{
	/**
	 * 更新快捷充值限额信息
	 * @param bankCode
	 * @param singleTransQuota
	 * @param cardDailyTransQuota
	 */
	public void updateBankRechargeConfig(String bankCode,String singleTransQuota,String cardDailyTransQuota);

}
