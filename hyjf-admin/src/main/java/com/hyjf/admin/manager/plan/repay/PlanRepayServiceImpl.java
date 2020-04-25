package com.hyjf.admin.manager.plan.repay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.DebtLoan;
import com.hyjf.mybatis.model.auto.DebtLoanExample;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.auto.DebtPlanAccedeExample;
import com.hyjf.mybatis.model.auto.DebtPlanExample;
import com.hyjf.mybatis.model.auto.DebtPlanWithBLOBs;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;
import com.hyjf.mybatis.model.customize.PlanInvestCustomize;
import com.hyjf.mybatis.model.customize.PlanLockCustomize;

import redis.clients.jedis.JedisPool;

@Service
public class PlanRepayServiceImpl extends BaseServiceImpl implements PlanRepayService {
	
	public static JedisPool pool = RedisUtils.getPool();

	/**
	 * 
	 * @method: countPlan
	 * @description: 计划数量查询
	 * @return
	 * @return: List<DebtPlanType>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月12日 下午5:57:15
	 */
	@Override
	public int countPlan(PlanCommonCustomize planCommonCustomize) {
		int ret = 0;
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();

		// 计划状态 0 发起中；1 待审核；2审核不通过；3待开放；4募集中；5锁定中；6清算中；7清算完成，8未还款，9还款中，10还款完成
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanStatusSrch())) {
			cra.andDebtPlanStatusEqualTo(Integer.parseInt(planCommonCustomize.getPlanStatusSrch()));
		} else {
			List<Integer> statusList = new ArrayList<Integer>();
			statusList.add(CustomConstants.DEBT_PLAN_STATUS_8);
			statusList.add(CustomConstants.DEBT_PLAN_STATUS_9);
			statusList.add(CustomConstants.DEBT_PLAN_STATUS_10);
			statusList.add(CustomConstants.DEBT_PLAN_STATUS_11);
			cra.andDebtPlanStatusIn(statusList);
		}
		// 计划编号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanNidSrch())) {
			cra.andDebtPlanNidLike("%" + planCommonCustomize.getPlanNidSrch() + "%");
		}
		// 最迟还款日
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeLastStart())) {
			cra.andRepayTimeLastGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getRepayTimeLastStart())));
		}
		// 最迟还款日end
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeLastEnd())) {
			planCommonCustomize.setRepayTimeLastEnd(planCommonCustomize.getRepayTimeLastEnd() + " 23:59:59");
			cra.andRepayTimeLastLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getRepayTimeLastEnd())));
		}
		// 实际还款日期
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeStart())) {
			cra.andRepayTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getRepayTimeStart())));
		}

		// 实际还款日期end
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeEnd())) {
			planCommonCustomize.setRepayTimeEnd(planCommonCustomize.getRepayTimeEnd() + " 23:59:59");
			cra.andRepayTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getRepayTimeEnd())));
		}

		ret = this.debtPlanMapper.countByExample(example);

		return ret;
	}

	/**
	 * 
	 * @method: selectPlanList
	 * @description: 计划列表查询
	 * @return
	 * @return: List<DebtPlanType>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月12日 下午5:57:15
	 */
	@Override
	public List<DebtPlan> selectPlanList(PlanCommonCustomize planCommonCustomize) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();

		// 计划状态 0 发起中；1 待审核；2审核不通过；3待开放；4募集中；5锁定中；6清算中；7清算完成，8未还款，9还款中，10还款完成
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanStatusSrch()) && !planCommonCustomize.getPlanStatusSrch().equals("8")) {
			cra.andDebtPlanStatusEqualTo(Integer.parseInt(planCommonCustomize.getPlanStatusSrch()));
		} else if (StringUtils.isNotEmpty(planCommonCustomize.getPlanStatusSrch()) && planCommonCustomize.getPlanStatusSrch().equals("8")) {
			List<Integer> statusList = new ArrayList<Integer>();
			statusList.add(CustomConstants.DEBT_PLAN_STATUS_8);
			statusList.add(CustomConstants.DEBT_PLAN_STATUS_9);
			cra.andDebtPlanStatusIn(statusList);
		} else {
			List<Integer> statusList = new ArrayList<Integer>();
			statusList.add(CustomConstants.DEBT_PLAN_STATUS_8);
			statusList.add(CustomConstants.DEBT_PLAN_STATUS_9);
			statusList.add(CustomConstants.DEBT_PLAN_STATUS_10);
			statusList.add(CustomConstants.DEBT_PLAN_STATUS_11);
			cra.andDebtPlanStatusIn(statusList);
		}
		// 计划编号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanNidSrch())) {
			cra.andDebtPlanNidLike("%" + planCommonCustomize.getPlanNidSrch() + "%");
		}
		// 最迟还款日
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeLastStart())) {
			cra.andRepayTimeLastGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getRepayTimeLastStart())));
		}
		// 最迟还款日end
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeLastEnd())) {
			planCommonCustomize.setRepayTimeLastEnd(planCommonCustomize.getRepayTimeLastEnd() + " 23:59:59");
			cra.andRepayTimeLastLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getRepayTimeLastEnd())));
		}
		// 实际还款日期
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeStart())) {
			cra.andRepayTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getRepayTimeStart())));
		}

		// 实际还款日期end
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeEnd())) {
			planCommonCustomize.setRepayTimeEnd(planCommonCustomize.getRepayTimeEnd() + " 23:59:59");
			cra.andRepayTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getRepayTimeEnd())));
		}
		// 排序
		example.setOrderByClause("debt_plan_status=8,debt_plan_status=9,repay_time_last DESC");
		example.setLimitStart(planCommonCustomize.getLimitStart());
		example.setLimitEnd(planCommonCustomize.getLimitEnd());
		List<DebtPlan> result = this.debtPlanMapper.selectByExample(example);

		return result;
	}

	@Override
	public List<DebtPlan> exportPlanList(PlanCommonCustomize planCommonCustomize) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		// 计划状态 0 发起中；1 待审核；2审核不通过；3待开放；4募集中；5锁定中；6清算中；7清算完成，8未还款，9还款中，10还款完成
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanStatusSrch())) {
			cra.andDebtPlanStatusEqualTo(Integer.parseInt(planCommonCustomize.getPlanStatusSrch()));
		} else {
			List<Integer> statusList = new ArrayList<Integer>();
			statusList.add(CustomConstants.DEBT_PLAN_STATUS_8);
			statusList.add(CustomConstants.DEBT_PLAN_STATUS_9);
			statusList.add(CustomConstants.DEBT_PLAN_STATUS_10);
			statusList.add(CustomConstants.DEBT_PLAN_STATUS_11);
			cra.andDebtPlanStatusIn(statusList);
		}
		// 计划编号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanNidSrch())) {
			cra.andDebtPlanNidLike("%" + planCommonCustomize.getPlanNidSrch() + "%");
		}
		// 最迟还款日
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeLastStart())) {
			cra.andRepayTimeLastGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getRepayTimeLastStart())));
		}
		// 最迟还款日end
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeLastEnd())) {
			planCommonCustomize.setRepayTimeLastEnd(planCommonCustomize.getRepayTimeLastEnd() + " 23:59:59");
			cra.andRepayTimeLastLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getRepayTimeLastEnd())));
		}
		// 实际还款日期
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeStart())) {
			cra.andRepayTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getRepayTimeStart())));
		}

		// 实际还款日期end
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeEnd())) {
			planCommonCustomize.setRepayTimeEnd(planCommonCustomize.getRepayTimeEnd() + " 23:59:59");
			cra.andRepayTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getRepayTimeEnd())));
		}
		// 排序
		example.setOrderByClause("debt_plan_status=8,debt_plan_status=9,repay_time_last Desc");
		List<DebtPlan> result = this.debtPlanMapper.selectByExample(example);
		return result;
	}

	@Override
	public Long countPlanAccede(PlanCommonCustomize planCommonCustomize) {
		Long ret = planLockCustomizeMapper.countPlanAccedeForAdmin(planCommonCustomize);
		return ret;
	}

	@Override
	public List<PlanLockCustomize> selectPlanAccedeList(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.selectPlanAccedeListForAdmin(planCommonCustomize);
	}

	@Override
	public HashMap<String, Object> selectPlanCountMap(String planNidSrch) {
		return planLockCustomizeMapper.selectPlanCountMap(planNidSrch);
	}

	@Override
	public HashMap<String, Object> planLockSumMap(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.planLockSumMap(planCommonCustomize);
	}

	@Override
	public int updateCycleTimesZero(String accedeorderId) {
		return planLockCustomizeMapper.updateCycleTimesZero(accedeorderId);
	}

	@Override
	public List<PlanInvestCustomize> selectPlanInvestList(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.selectPlanInvestList(planCommonCustomize);
	}

	@Override
	public Long countPlanInvest(PlanCommonCustomize planCommonCustomize) {

		return planLockCustomizeMapper.countPlanInvest(planCommonCustomize);

	}

	@Override
	public HashMap<String, Object> planInvestSumMap(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.planInvestSumMap(planCommonCustomize);
	}

	@Override
	public int countDebtLoan(PlanCommonCustomize planCommonCustomize) {

		int ret = 0;
		DebtLoanExample example = new DebtLoanExample();
		DebtLoanExample.Criteria cra = example.createCriteria();
		// 计划编号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanNidSrch())) {
			cra.andPlanNidEqualTo(planCommonCustomize.getPlanNidSrch());
		}
		// 计划订单号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanOrderId())) {
			cra.andPlanOrderIdLike("%" + planCommonCustomize.getPlanOrderId() + "%");
		}
		// 用户名
		if (StringUtils.isNotEmpty(planCommonCustomize.getUserName())) {
			cra.andUserNameLike("%" + planCommonCustomize.getUserName() + "%");
		}
		// 项目编号
		if (StringUtils.isNotEmpty(planCommonCustomize.getBorrowNid())) {
			cra.andBorrowNidLike("%" + planCommonCustomize.getBorrowNid() + "%");
		}
		// 回款状态
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayStatus())) {
			cra.andRepayStatusEqualTo(Integer.parseInt(planCommonCustomize.getRepayStatus()));
		}
		// 应还款时间开始
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeStart())) {
			cra.andRepayTimeGreaterThan(GetDate.getDayStart(planCommonCustomize.getRepayTimeStart()));
		}
		// 应还款时间结束
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeEnd())) {
			cra.andRepayTimeGreaterThanOrEqualTo(GetDate.getDayEnd(planCommonCustomize.getRepayTimeEnd()));
		}
		ret = this.debtLoanMapper.countByExample(example);
		return ret;
	}

	@Override
	public List<DebtLoan> selectDebtLoanList(PlanCommonCustomize planCommonCustomize) {

		DebtLoanExample example = new DebtLoanExample();
		DebtLoanExample.Criteria cra = example.createCriteria();
		// 计划订单号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanNidSrch())) {
			cra.andPlanNidEqualTo(planCommonCustomize.getPlanNidSrch());
		}
		// 计划订单号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanOrderId())) {
			cra.andPlanOrderIdLike("%" + planCommonCustomize.getPlanOrderId() + "%");
		}
		// 用户名
		if (StringUtils.isNotEmpty(planCommonCustomize.getUserName())) {
			cra.andUserNameLike("%" + planCommonCustomize.getUserName() + "%");
		}
		// 项目编号
		if (StringUtils.isNotEmpty(planCommonCustomize.getBorrowNid())) {
			cra.andBorrowNidLike("%" + planCommonCustomize.getBorrowNid() + "%");
		}
		// 回款状态
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayStatus())) {
			cra.andRepayStatusEqualTo(Integer.parseInt(planCommonCustomize.getRepayStatus()));
		}
		// 应还款时间开始
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeStart())) {
			cra.andRepayTimeGreaterThan(GetDate.getDayStart(planCommonCustomize.getRepayTimeStart()));
		}
		// 应还款时间结束
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeEnd())) {
			cra.andRepayTimeGreaterThanOrEqualTo(GetDate.getDayEnd(planCommonCustomize.getRepayTimeEnd()));
		}
		// 排序
		example.setOrderByClause("repay_time Desc");
		example.setLimitStart(planCommonCustomize.getLimitStart());
		example.setLimitEnd(planCommonCustomize.getLimitEnd());
		return debtLoanMapper.selectByExample(example);
	}

	@Override
	public HashMap<String, Object> DebtLoanSumMap(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.DebtLoanSumMap(planCommonCustomize);
	}

	@Override
	public List<DebtPlanAccede> getDebtPlanAccedes(String accedeOrderId) {
		DebtPlanAccedeExample example = new DebtPlanAccedeExample();
		DebtPlanAccedeExample.Criteria cra = example.createCriteria();
		cra.andAccedeOrderIdEqualTo(accedeOrderId);
		return debtPlanAccedeMapper.selectByExample(example);

	}

	@Override
	public Long countLoanDetail(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.countLoanDetail(planCommonCustomize);
	}

	@Override
	public List<Map<String, Object>> selectLoanDetailList(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.selectLoanDetailList(planCommonCustomize);
	}

	@Override
	public HashMap<String, Object> LoanDeailSumMap(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.LoanDeailSumMap(planCommonCustomize);
	}

	@Override
	public Long countLoanDetailNew(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.countLoanDetailNew(planCommonCustomize);
	}

	@Override
	public HashMap<String, Object> LoanDeailSumMapNew(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.LoanDeailSumMapNew(planCommonCustomize);
	}

	@Override
	public List<Map<String, Object>> selectLoanDetailListNew(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.selectLoanDetailListNew(planCommonCustomize);
	}

	@Override
	public boolean repayNow(String plannid) {
		// 看是否 可以还款
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cri = example.createCriteria();
		cri.andDebtPlanNidEqualTo(plannid);

		List<DebtPlan> list = debtPlanMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			if (list.get(0).getDebtPlanStatus() == 8) {
				DebtPlanWithBLOBs debtPlanWithBLOBs = new DebtPlanWithBLOBs();
				debtPlanWithBLOBs.setDebtPlanStatus(9);// 改为未还款
				boolean updateFlag = debtPlanMapper.updateByExampleSelective(debtPlanWithBLOBs, example) > 0 ? true : false;
				return updateFlag;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	@Override
	public HashMap<String, Object> selectPlanCreditCountMap(String planNidSrch) {
		return planLockCustomizeMapper.selectPlanCreditCountMap(planNidSrch);
	}

}
