package com.hyjf.batch.htj.liquidatesfinish;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.DebtPlan;

/**
 * 汇添金定时自动出借
 * 
 * @author wangkun
 */
public class AutoFinishTask {

	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	AutoFinishService autoFinshService;

	/**
	 * 汇添金自动出借
	 */
	public void run() {
		autoFinish();
	}

	/**
	 * 汇添金主动出借逻辑
	 * 
	 * @return
	 */
	private boolean autoFinish() {

		// 调用自动投标接口
		if (isRun == 0) {
			isRun = 1;
			System.out.println("汇添金自动清算  AutoFinishTask.run ... ");
			try {
				// a.进行债权承接
				// （1）查询相应的已清算的汇添金计划（根据liquidate_fact_time升序排列），
				List<DebtPlan> debtPlanLiquidateses = this.autoFinshService.selectDebtPlanLiquidates();
				// 判断相应的清算债权数据是否为空
				if (Validator.isNotNull(debtPlanLiquidateses) && debtPlanLiquidateses.size() > 0) {
					for (DebtPlan debtPlanLiquidates : debtPlanLiquidateses) {
						// 获取已清算的计划nid
						String liquidatesPlanNid = debtPlanLiquidates.getDebtPlanNid();
						// 查询相应的清算债权数据
						int count = this.autoFinshService.countDebtCreditsAll(liquidatesPlanNid);
						if (count == 0) {
							try {
								boolean debtPlanFlag = this.autoFinshService.updateDebtPlan(debtPlanLiquidates);
								if (debtPlanFlag) {
									this.autoFinshService.sendSms(debtPlanLiquidates);
									this.autoFinshService.sendEmail(debtPlanLiquidates);
									continue;
								} else {
									throw new Exception("更新计划为承接完成失败,计划编号：" + debtPlanLiquidates.getDebtPlanNid());
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							continue;
						}
					}
					return true;
				} else {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
				System.out.println("汇添金清算 AutoFinishTask.end ... ");
			}
		}
		return false;
	}
}
