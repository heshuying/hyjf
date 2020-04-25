package com.hyjf.batch.htj.liquidatewill;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.auto.DebtPlanAccedeExample;
import com.hyjf.mybatis.model.auto.DebtPlanExample;

/**
 * 平台数据
 *
 * @author Administrator
 *
 */
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class LiquidateWillServiceImpl extends BaseServiceImpl implements LiquidateWillService {

	@Override
	public List<DebtPlan> selectLiquidateWill() {
		Integer startDate=GetDate.getSearchStartTime(GetDate.getSomeDayBeforeOrAfter(new Date(), 1));
		Integer endDate=GetDate.getSearchEndTime(GetDate.getSomeDayBeforeOrAfter(new Date(), 1));
		DebtPlanExample example=new DebtPlanExample();
		DebtPlanExample.Criteria cri=example.createCriteria();
		cri.andDebtPlanStatusEqualTo(5);
		cri.andLiquidateShouldTimeBetween(startDate, endDate);
		return	debtPlanMapper.selectByExample(example);
		
	}

	@Override
	public List<DebtPlanAccede> selectAllExFireValue(String debtPlanNid) {
		DebtPlanAccedeExample example=new DebtPlanAccedeExample();
		DebtPlanAccedeExample.Criteria cri=example.createCriteria();
		cri.andPlanNidEqualTo(debtPlanNid);
		return	debtPlanAccedeMapper.selectByExample(example);
	}
}
