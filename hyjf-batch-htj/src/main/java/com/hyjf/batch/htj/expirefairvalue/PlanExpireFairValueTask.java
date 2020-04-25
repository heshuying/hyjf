package com.hyjf.batch.htj.expirefairvalue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.mybatis.model.auto.DebtPlan;

/**
 * 计算到期公允价值
 * 
 * @ClassName PlanExpireFairValueTask
 * @author liuyang
 * @date 2016年11月22日 下午1:56:13
 */
public class PlanExpireFairValueTask {

	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	private PlanExpireFairValueService planExpireFairValueService;

	public void run() {
		calculation();
	}

	/**
	 * 计算公允价值
	 * 
	 * @Title calculation
	 */
	private void calculation() {

		if (isRun == 0) {
			try {
				isRun = 1;
				// 检索锁定中的计划列表
				List<DebtPlan> planList = this.planExpireFairValueService.selectLockPlanList();
				if (planList != null && planList.size() > 0) {
					// 循环锁定中的计划列表
					for (DebtPlan debtPlan : planList) {
						this.planExpireFairValueService.calculation(debtPlan);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
			}
		}
	}

}
