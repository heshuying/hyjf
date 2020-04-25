package com.hyjf.batch.exception.bankrecharge;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.AccountRecharge;

/**
 * 江西银行充值掉单异常处理
 * 
 * @author liuyang
 *
 */
public interface BankRechargeExceptionService extends BaseService {

	/**
	 * 检索充值中的充值记录
	 * 
	 * @return
	 */
	public List<AccountRecharge> selectBankRechargeList();

	/**
	 * 更新处理中的充值记录
	 * 
	 * @param accountRecharge
	 */
	public void updateRecharge(AccountRecharge accountRecharge);

}
