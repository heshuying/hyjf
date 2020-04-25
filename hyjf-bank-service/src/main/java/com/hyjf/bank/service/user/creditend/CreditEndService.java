package com.hyjf.bank.service.user.creditend;


import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.BankCreditEnd;

public interface CreditEndService extends BaseService {
	/**
	 * 跟据OrderId查询记录
	 * @param orderId
	 * @return
	 */
	public BankCreditEnd selectByOrderId(String orderId);
	/**
	 * 生成批次，调用银行结束债权
	 * @return
	 */
	public Boolean updateBatchCreditEnd();

	/**
	 * 调用批次查询接口查询批次返回结果并更新
	 * @param bankCreditEnd
	 * @return
	 */
	public boolean batchDetailsQuery(BankCreditEnd bankCreditEnd);

	/**
	 * 更新结束债权的状态
	 * @param bankCreditEnd
	 * @param status
	 * @return
	 */
	public int updateCreditEndForStatus(BankCreditEnd bankCreditEnd, int status);

	/**
	 * 更新确认后结束债权的状态为初始
	 * @param bankCreditEnd
	 * @param i
	 * @return
	 */
	public int updateCreditEndForInitial(BankCreditEnd bankCreditEnd);
}
