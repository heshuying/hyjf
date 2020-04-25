package com.hyjf.batch.htj.releaseplan;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.DebtPlan;

/**
 * 自动发布计划
 * 
 * @ClassName AutoReleasePlanTask
 * @author liuyang
 * @date 2016年10月9日 下午5:43:02
 */
public class AutoReleasePlanTask {
	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

	@Autowired
	private AutoReleasePlanService autoReleasePlanService;

	public void run() {
		if (isOver) {
			System.out.println("-----------------自动发布计划定时开始---------------");
			isOver = false;
			try {
				// 检索待开放的计划列表
				List<DebtPlan> releasePlanList = this.autoReleasePlanService.selectNeedReleasePlanList();
				// 待开放列表不为空
				if (releasePlanList != null && releasePlanList.size() > 0) {
					for (int i = 0; i < releasePlanList.size(); i++) {
						DebtPlan plan = releasePlanList.get(i);
						// 判断计划的申购开始时间跟当前时间比较:申购开始时间小于当前时间
						if (plan.getBuyBeginTime() < GetDate.getMyTimeInMillis()) {
							// 更新计划状态,计划
							int updateCount = this.autoReleasePlanService.updateReleasePlan(plan);

							if (updateCount == 0) {
								throw new Exception("更新失败,更新件数为0件。[计划编号：" + plan.getDebtPlanNid() + "]");
							}

							// 计划的redits的设置
							RedisUtils.set(plan.getDebtPlanNid(), String.valueOf(plan.getDebtPlanMoney()));
						}
					}
				}
				System.out.println("-----------------自动发布计划定时结束---------------");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isOver = true;
			}
		}
	}
}
