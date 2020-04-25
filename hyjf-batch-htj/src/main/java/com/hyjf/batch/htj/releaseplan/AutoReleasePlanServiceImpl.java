package com.hyjf.batch.htj.releaseplan;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanExample;
import com.hyjf.mybatis.model.auto.DebtPlanWithBLOBs;

/**
 * 自动发布计划Service实现类
 * 
 * @ClassName AutoReleasePlanServiceImpl
 * @author liuyang
 * @date 2016年10月9日 下午5:45:14
 */
@Service
public class AutoReleasePlanServiceImpl extends BaseServiceImpl implements AutoReleasePlanService {

	/**
	 * 检索待开发计划列表
	 * 
	 * @Title selectNeedReleasePlanList
	 * @return
	 */
	@Override
	public List<DebtPlan> selectNeedReleasePlanList() {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		// 计划状态:待开放
		cra.andDebtPlanStatusEqualTo(CustomConstants.DEBT_PLAN_STATUS_3);
		return this.debtPlanMapper.selectByExample(example);
	}

	/**
	 * 更新计划的计划状态
	 * 
	 * @Title updateReleasePlan
	 * @param plan
	 */
	@Override
	public int updateReleasePlan(DebtPlan plan) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		cra.andDebtPlanNidEqualTo(plan.getDebtPlanNid());
		List<DebtPlanWithBLOBs> planList = this.debtPlanMapper.selectByExampleWithBLOBs(example);
		DebtPlanWithBLOBs updateplan = null;
		if (planList != null && planList.size() > 0) {
			updateplan = planList.get(0);
			// 计划状态变为募集中
			updateplan.setDebtPlanStatus(CustomConstants.DEBT_PLAN_STATUS_4);
			// 计划余额更新成计划金额
			updateplan.setDebtPlanMoneyWait(updateplan.getDebtPlanMoney());

			return this.debtPlanMapper.updateByPrimaryKeySelective(updateplan);
		}
		return 0;
	}
}
