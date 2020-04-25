package com.hyjf.admin.manager.plan.raise;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanExample;

/**
 * 计划募集Service实现类
 * 
 * @ClassName PlanRaiseServiceImpl
 * @author liuyang
 * @date 2016年9月26日 下午3:02:28
 */
@Service
public class PlanRaiseServiceImpl extends BaseServiceImpl implements PlanRaiseService {

	/**
	 * 检索募集中的计划的数量
	 * 
	 * @Title countPlanRaise
	 * @param form
	 * @return
	 */
	@Override
	public int countPlanRaise(PlanRaiseBean form) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		List<Integer> status = new ArrayList<Integer>();
		status.add(CustomConstants.DEBT_PLAN_STATUS_3);
		status.add(CustomConstants.DEBT_PLAN_STATUS_4);
		cra.andDebtPlanStatusIn(status);
		return this.debtPlanMapper.countByExample(example);
	}

	/**
	 * 检索募集中计划列表
	 * 
	 * @Title selectPlanRaiseList
	 * @param form
	 * @return
	 */
	@Override
	public List<DebtPlan> selectPlanRaiseList(PlanRaiseBean form) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		List<Integer> status = new ArrayList<Integer>();
		status.add(CustomConstants.DEBT_PLAN_STATUS_3);
		status.add(CustomConstants.DEBT_PLAN_STATUS_4);
		cra.andDebtPlanStatusIn(status);

		if (form.getLimitStart() > 0) {
			example.setLimitStart(form.getLimitStart());
		}
		if (form.getLimitEnd() > 0) {
			example.setLimitEnd(form.getLimitEnd());
		}
		example.setOrderByClause("buy_begin_time ASC");
		return this.debtPlanMapper.selectByExample(example);
	}

}
