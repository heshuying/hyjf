package com.hyjf.batch.exception.bankrecharge;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.mybatis.model.auto.AccountRecharge;

/**
 * 江西银行充值掉单异常处理定时任务
 * 
 * @author liuyang
 *
 */
public class BankRechargeExceptionTask {
	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	private BankRechargeExceptionService bankRechargeExceptionService;

	public void run() {
		recharge();
	}

	private boolean recharge() {
		if (isRun == 0) {
			isRun = 1;
			try {
				// 检索处理中的充值订单
				List<AccountRecharge> rechargeList = this.bankRechargeExceptionService.selectBankRechargeList();
				if (rechargeList != null && rechargeList.size() > 0) {
					// 循环处理中的充值订单
					for (AccountRecharge accountRecharge : rechargeList) {
						this.bankRechargeExceptionService.updateRecharge(accountRecharge);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
			}
		}
		return true;
	}
}
