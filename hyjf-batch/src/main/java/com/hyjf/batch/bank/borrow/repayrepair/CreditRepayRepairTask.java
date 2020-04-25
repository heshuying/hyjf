package com.hyjf.batch.bank.borrow.repayrepair;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.mybatis.model.auto.CreditRepay;

public class CreditRepayRepairTask {
	/** 运行状态 */
	private static int isRun = 0;
	@Autowired
	CreditRepayRepairService creditRepayRepairService;

	/**
	 * 修复债转还款
	 */
	public void run() {
		creditRepayRepair();
	}

	/**
	 * 债转还款修复
	 * 
	 * @return
	 */
	private boolean creditRepayRepair() {
		try {
			if (isRun == 0) {
				isRun = 1;
				List<CreditRepay> creditRepayList = this.creditRepayRepairService.selectCreditRepayList();
				if (creditRepayList != null && creditRepayList.size() > 0) {
					for (CreditRepay creditRepay : creditRepayList) {
						this.creditRepayRepairService.creditRepayRepair(creditRepay);
					}
				}
				isRun = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			isRun = 0;
		}
		return true;
	}
}
