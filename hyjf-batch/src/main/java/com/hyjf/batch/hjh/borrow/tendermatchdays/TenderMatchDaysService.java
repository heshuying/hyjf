package com.hyjf.batch.hjh.borrow.tendermatchdays;

import com.hyjf.batch.BaseService;

/**
 * 计算自动出借的匹配期(每日)
 * @author liubin
 * 汇计划三期
 */
public interface TenderMatchDaysService extends BaseService {
	/**
	 * 新未进入锁定期的计划订单的匹配期hjhaccede
	 * @return
	 */
	Boolean updateMatchDays();
}
