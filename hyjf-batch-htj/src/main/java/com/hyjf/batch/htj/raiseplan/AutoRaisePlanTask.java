package com.hyjf.batch.htj.raiseplan;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.mybatis.model.auto.DebtPlanWithBLOBs;
import com.hyjf.soa.apiweb.CommonSoaUtils;

public class AutoRaisePlanTask {

	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

	@Autowired
	private AutoRaisePlanService autoRaisePlanService;

	public void run() {
		if (isOver) {
			System.out.println("-----------------募集中的计划变更定时开始---------------");
			isOver = false;
			try {
				// 获取满标的计划列表
				List<DebtPlanWithBLOBs> fullPlanList = this.autoRaisePlanService.selectFullPlanList();

				if (fullPlanList != null && fullPlanList.size() > 0) {
					for (int i = 0; i < fullPlanList.size(); i++) {
						DebtPlanWithBLOBs plan = fullPlanList.get(i);
						int updateCount = this.autoRaisePlanService.updatePlanStatus(plan);
						if (updateCount == 0) {
							throw new Exception("更新失败,更新件数为0件。[计划编号：" + plan.getDebtPlanNid() + "]");
						}
						this.autoRaisePlanService.sendMsg(plan, true);
					}
				}
				// 获取未满标的计划列表
				List<DebtPlanWithBLOBs> notFullPlanList = this.autoRaisePlanService.selectNotFullPlanList();
				if (notFullPlanList != null && notFullPlanList.size() > 0) {
					for (int i = 0; i < notFullPlanList.size(); i++) {
						DebtPlanWithBLOBs plan = notFullPlanList.get(i);
						int updateCount = this.autoRaisePlanService.updatePlanStatusAndLiquidateTime(plan);
						if (updateCount == 0) {
							throw new Exception("更新失败,更新件数为0件。[计划编号：" + plan.getDebtPlanNid() + "]");
						}
						this.autoRaisePlanService.sendMsg(plan, false);
						// add by pangchengchao 计算汇添金优惠券的还款时间 start
						CommonSoaUtils.planCouponRecover(plan.getDebtPlanNid());
						// add by pangchengchao 计算汇添金优惠券的还款时间 end
					}
				}

				// 获取没有加入金额,购买结束时间到期的计划
				List<DebtPlanWithBLOBs> emptyPlanList = this.autoRaisePlanService.selectEmptyPlanList();

				if (emptyPlanList != null && emptyPlanList.size() > 0) {
					for (int i = 0; i < emptyPlanList.size(); i++) {
						DebtPlanWithBLOBs plan = emptyPlanList.get(i);
						int updateCount = this.autoRaisePlanService.updateEmptyPlanStatus(plan);
						if (updateCount == 0) {
							throw new Exception("更新失败,更新件数为0件。[计划编号：" + plan.getDebtPlanNid() + "]");
						}
					}
				}
				System.out.println("-----------------募集中的计划变更定时结束---------------");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isOver = true;
			}
		}
	}
}
