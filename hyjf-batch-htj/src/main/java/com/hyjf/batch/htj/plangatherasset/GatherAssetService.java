package com.hyjf.batch.htj.plangatherasset;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.customize.batch.BatchDebtPlanBorrowCustomize;

public interface GatherAssetService extends BaseService {

	/**
	 * 查询相应的出借了一笔没有满标的标的
	 * @param nowTime 
	 * @return
	 */
		
	List<BatchDebtPlanBorrowCustomize> selectUnFullDebtBorrow(int num);

	/**
	 * @param borrowNid
	 */
		
	void sendSms(String borrowNid);

	/**
	 * @param borrowNid
	 */
		
	void sendEmail(String borrowNid);
	
}
