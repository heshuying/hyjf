package com.hyjf.batch.htj.planActualApr;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanExample;

/**
 * 自动扣款(放款服务)
 *
 * @author Administrator
 *
 */
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class PlanActualAprServiceImpl extends BaseServiceImpl implements PlanActualAprService {

	@Override
	public List<DebtPlan> selectPlanAllInLock() {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cri = example.createCriteria();
		// 计划状态 0 发起中；1 待审核；2审核不通过；3待开放；4募集中；5锁定中；6清算中；7清算完成，8未还款，9还款中，10还款完成
		List<Integer> params = new ArrayList<Integer>();
		params.add(4);
		params.add(5);
		params.add(6);
		params.add(7);
		params.add(8);
		params.add(9);
		params.add(10);
		cri.andDebtPlanStatusIn(params);
		return debtPlanMapper.selectByExample(example);
	}

	@Override
	public Long updatePlanActualApr(String planNid) {
		return planLockCustomizeMapper.updatePlanActualApr(planNid);
	}

}
