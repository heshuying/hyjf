package com.hyjf.batch.htj.sendagreement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.customize.batch.BatchDebtPlanAccedeCustomize;

/**
 * 汇添金自动发送协议定时
 * 
 * @ClassName AutoSendAgreementTask
 * @author liuyang
 * @date 2016年10月18日 上午9:27:15
 */
public class AutoSendAgreementTask {

	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

	@Autowired
	private AutoSendAgreementService autoSendAgreementService;

	public void sendAgreement() {
		if (isOver) {
			System.out.println("-----------------自动发送协议开始---------------");
			isOver = false;
			try {
				// 检索锁定中的计划
				List<DebtPlan> lockPlanList = this.autoSendAgreementService.selectLockPlanList();

				if (lockPlanList != null && lockPlanList.size() > 0) {
					for (int i = 0; i < lockPlanList.size(); i++) {
						DebtPlan debtPlan = lockPlanList.get(i);
						// 根据计划Nid检索所有加入计划列表
						List<DebtPlanAccede> planAccedeList = this.autoSendAgreementService.selectPlanAccede(debtPlan.getDebtPlanNid());
						if (planAccedeList != null && planAccedeList.size() > 0) {
							for (int j = 0; j < planAccedeList.size(); j++) {
								BatchDebtPlanAccedeCustomize batchDebtPlanAccedeCustomize = this.autoSendAgreementService.selectPlanAccedeInfo(planAccedeList.get(j));
								if (batchDebtPlanAccedeCustomize != null) {
									this.autoSendAgreementService.sendMail(batchDebtPlanAccedeCustomize);
								}
							}
						}
					}
				}
				isOver = true;
				System.out.println("-----------------自动发送协议结束---------------");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isOver = false;
			}
		}
	}
}
