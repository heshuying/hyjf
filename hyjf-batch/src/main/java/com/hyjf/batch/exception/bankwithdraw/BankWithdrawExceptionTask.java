package com.hyjf.batch.exception.bankwithdraw;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.mybatis.model.auto.Accountwithdraw;

/**
* 江西银行提现掉单异常处理定时任务
* @author LiuBin
* @date 2017年8月1日 上午9:58:20
*
*/ 
public class BankWithdrawExceptionTask {
	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	private BankWithdrawExceptionService bankWithdrawExceptionService;

	public void run() {
		withdraw();
	}

	private boolean withdraw() {
		if (isRun == 0) {
			isRun = 1;
			try {
				// 检索处理中的充值订单
				List<Accountwithdraw> withdrawList = this.bankWithdrawExceptionService.selectBankWithdrawList();
				if (withdrawList != null && withdrawList.size() > 0) {
					// 循环处理中的充值订单
					for (Accountwithdraw accountWithdraw : withdrawList) {
						this.bankWithdrawExceptionService.updateWithdraw(accountWithdraw);
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
