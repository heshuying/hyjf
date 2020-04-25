package com.hyjf.batch.htj.unableliquidates;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.DebtPlan;

/**
 * 汇添金计划未清算完成
 * 
 * @author wangkun
 */
public class UnableLiquidatesTask {

	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	UnableLiquidatesService unableLiquidatesService;

	/**
	 * 汇添金计划未清算完成
	 */
	public void run() {
		unableLiquidates();
	}

	/**
	 * 汇添金计划未清算完成
	 * 
	 * @return
	 */
	private boolean unableLiquidates() {
		if (isRun == 0) {
			isRun = 1;
			System.out.println("汇添金资产清算未完成  UnableLiquidatesTask.run ... ");
			try {
				// 1.查询相应的出借中计划
				List<DebtPlan> debtPlanLiquidates = this.unableLiquidatesService.selectDebtPlanLiquidates();
				if (debtPlanLiquidates != null && debtPlanLiquidates.size() > 0) {
					for (DebtPlan debtPlan : debtPlanLiquidates) {
						// 计划编号
						String planNid = debtPlan.getDebtPlanNid();
						BigDecimal total = this.unableLiquidatesService.countDebtCreditSum(planNid);
						if (Validator.isNotNull(total) && total.compareTo(BigDecimal.ZERO) > 0) {
							this.unableLiquidatesService.sendSms(planNid, total);
							this.unableLiquidatesService.sendEmail(planNid, total);
						}

					}
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
				System.out.println("汇添金资产清算未完成  UnableLiquidatesTask.end ... ");
			}
		}
		return false;
	}
}
