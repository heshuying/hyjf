package com.hyjf.batch.htj.planweakasset;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.mybatis.model.auto.DebtPlan;

/**
 * 汇添金定时自动出借
 * @author wangkun
 */
public class WeakAssetTask {
	
	/** 运行状态 */
	private static int isRun = 0;
	
	@Autowired
	WeakAssetService weakAssetService;

	/**
	 * 汇添金自动出借
	 */
	public void run() {
		weakAsset();
	}

	/**
	 * 汇添金主动出借逻辑
	 * @return
	 */
	private boolean weakAsset() {
		if (isRun == 0) {
			isRun = 1;
			System.out.println("汇添金资产不足  WeakAssetTask.run ... ");
			try {
				//1.查询相应的出借中计划
				List<DebtPlan> debtPlanInvests = this.weakAssetService.selectDebtPlanInvest();
				if(debtPlanInvests!=null&&debtPlanInvests.size()>0){
					for(DebtPlan debtPlan : debtPlanInvests){
						//计划编号
						String planNid = debtPlan.getDebtPlanNid();
						//计划设置规则出借遍历次数
						int cycleTimes = debtPlan.getCycleTimes();
						//计划设置无规则出借遍历次数
						int unableCycleTimes = debtPlan.getUnableCycleTimes();
						//计划最小余额
						BigDecimal minSurplusInvestAccount = debtPlan.getMinSurplusInvestAccount();
						//2.查询该计划对应的计划加入记录，取出可用余额，取出拆分最大金额，拆分最小金额，承接次数（未完成记录），遍历次数小于最大遍历次数
						int count = this.weakAssetService.countDebtPlanAccede(planNid,minSurplusInvestAccount,cycleTimes+unableCycleTimes);
						if(count>0){
							this.weakAssetService.sendSms(planNid,count,cycleTimes+unableCycleTimes);
							this.weakAssetService.sendEmail(planNid,count,cycleTimes+unableCycleTimes);
						}
					}
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
				System.out.println("汇添金资产不足  WeakAssetTask.end ... ");
			}
		}
		return false;
	}
}
