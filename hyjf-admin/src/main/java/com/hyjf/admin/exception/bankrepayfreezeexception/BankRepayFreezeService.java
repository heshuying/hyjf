package com.hyjf.admin.exception.bankrepayfreezeexception;


import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BankRepayFreezeLog;

public interface BankRepayFreezeService extends BaseService {


	/**
	 * 统计相应的还款冻结记录数量
	 * @return
	 */
	public Integer selectCountRepayFreezeList();

	/**
	 * 查询相应的冻结记录
	 * @param form 
	 * @return
	 */
	public List<BankRepayFreezeLog> selectBankFreezeList(BankRepayFreezeBean form);

	/**
	 * 根据原始订单号查询 用户的冻结日志
	 * @param orderId
	 * @return
	 */
	public BankRepayFreezeLog selectBankFreezeLog(String orderId);

	/**
	 * 调用银行订单解冻用户的还款冻结金额
	 * @param repayFreezeFlog
	 * @return
	 */
	public boolean repayUnfreeze(BankRepayFreezeLog repayFreezeFlog);

	/**
	 * 删除用户的冻结记录
	 * @param repayFreezeFlog
	 * @return
	 */
	public boolean updateBankRepayFreeze(BankRepayFreezeLog repayFreezeFlog); 

}
