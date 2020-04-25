package com.hyjf.admin.manager.plan.release;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanExample;
import com.hyjf.mybatis.model.auto.DebtPlanWithBLOBs;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.DebtPlanBorrowCustomize;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;

/**
 * 计划发布的Service
 * 
 * @ClassName PlanReleaseServiceImpl
 * @author liuyang
 * @date 2016年9月23日 下午1:53:58
 */
@Service
public class PlanReleaseServiceImpl extends BaseServiceImpl implements PlanReleaseService {

	/**
	 * 检索计划发布列表件数
	 * 
	 * @Title countPlanRelease
	 * @param planCommonCustomize
	 * @return
	 */
	@Override
	public int countPlanRelease(PlanCommonCustomize planCommonCustomize) {
		int ret = 0;
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		// 计划编号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanNidSrch())) {
			cra.andDebtPlanNidLike("%" + planCommonCustomize.getPlanNidSrch() + "%");
		}
		// 计划名称
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanNameSrch())) {
			cra.andDebtPlanNameLike("%" + planCommonCustomize.getPlanNameSrch() + "%");
		}
		// 计划类型
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanTypeSrch())) {
			cra.andDebtPlanTypeEqualTo(Integer.parseInt(planCommonCustomize.getPlanTypeSrch()));
		}
		// 状态
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanStatusSrch())) {
			cra.andDebtPlanStatusEqualTo(Integer.valueOf(planCommonCustomize.getPlanStatusSrch()));
		}
		// 发起时间
		if (StringUtils.isNotEmpty(planCommonCustomize.getTimeStartSrch())) {
			cra.andCreateTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getTimeStartSrch())));
		}
		if (StringUtils.isNotEmpty(planCommonCustomize.getTimeEndSrch())) {
			cra.andCreateTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayEnd(planCommonCustomize.getTimeEndSrch())));
		}
		
		List<Integer> debtPlanStatus = new ArrayList<>();
		debtPlanStatus.add(CustomConstants.DEBT_PLAN_STATUS_0);
		debtPlanStatus.add(CustomConstants.DEBT_PLAN_STATUS_1);
		debtPlanStatus.add(CustomConstants.DEBT_PLAN_STATUS_2);
		cra.andDebtPlanStatusIn(debtPlanStatus);
		if (planCommonCustomize.getLimitStart() >= 0) {
			example.setLimitStart(planCommonCustomize.getLimitStart());
			example.setLimitEnd(planCommonCustomize.getLimitEnd());
		}
		ret = this.debtPlanMapper.countByExample(example);
		return ret;
	}

	/**
	 * 检索计划发布列表
	 * 
	 * @Title selectPlanReleaseList
	 * @param planCommonCustomize
	 * @return
	 */
	public List<DebtPlan> selectPlanReleaseList(PlanCommonCustomize planCommonCustomize) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		// 计划编号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanNidSrch())) {
			cra.andDebtPlanNidLike("%" + planCommonCustomize.getPlanNidSrch() + "%");
		}
		// 计划名称
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanNameSrch())) {
			cra.andDebtPlanNameLike("%" + planCommonCustomize.getPlanNameSrch() + "%");
		}
		// 计划类型
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanTypeSrch())) {
			cra.andDebtPlanTypeEqualTo(Integer.parseInt(planCommonCustomize.getPlanTypeSrch()));
		}
		// 状态
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanStatusSrch())) {
			cra.andDebtPlanStatusEqualTo(Integer.valueOf(planCommonCustomize.getPlanStatusSrch()));
		}
		// 发起时间
		if (StringUtils.isNotEmpty(planCommonCustomize.getTimeStartSrch())) {
			cra.andCreateTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getTimeStartSrch())));
		}
		if (StringUtils.isNotEmpty(planCommonCustomize.getTimeEndSrch())) {
			cra.andCreateTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayEnd(planCommonCustomize.getTimeEndSrch())));
		}
		List<Integer> debtPlanStatus = new ArrayList<>();
		debtPlanStatus.add(CustomConstants.DEBT_PLAN_STATUS_0);
		debtPlanStatus.add(CustomConstants.DEBT_PLAN_STATUS_1);
		debtPlanStatus.add(CustomConstants.DEBT_PLAN_STATUS_2);
		cra.andDebtPlanStatusIn(debtPlanStatus);
		// 排序
		example.setOrderByClause("create_time Desc");
		if (planCommonCustomize.getLimitStart() >= 0) {
			example.setLimitStart(planCommonCustomize.getLimitStart());
			example.setLimitEnd(planCommonCustomize.getLimitEnd());
		}
		List<DebtPlan> list = this.debtPlanMapper.selectByExample(example);
		return list;
	}

	/**
	 * 根据计划编号检索计划详情
	 * 
	 * @Title selectPlanReleaseInfoByDebtPlanNid
	 * @param debtPlanNid
	 * @return
	 */
	@Override
	public DebtPlan selectPlanReleaseInfoByDebtPlanNid(String debtPlanNid) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(debtPlanNid)) {
			cra.andDebtPlanNidEqualTo(debtPlanNid);
		}
		List<DebtPlan> list = this.debtPlanMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 根据计划编号获取计划详情
	 * 
	 * @Title selectPlanInfoWithBLOBsByDebtPlanNid
	 * @param debtPlanNid
	 * @return
	 */
	@Override
	public DebtPlanWithBLOBs selectPlanInfoWithBLOBsByDebtPlanNid(String debtPlanNid) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		// 计划编号
		if (StringUtils.isNotEmpty(debtPlanNid)) {
			cra.andDebtPlanNidEqualTo(debtPlanNid);
		}
		List<DebtPlanWithBLOBs> list = this.debtPlanMapper.selectByExampleWithBLOBs(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 确认提审,更新计划状态
	 * 
	 * @Title updatePlanRecord
	 * @param deptPlanNid
	 */
	@Override
	public void updatePlanRecord(String deptPlanNid) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(deptPlanNid)) {
			cra.andDebtPlanNidEqualTo(deptPlanNid);
		}
		List<DebtPlanWithBLOBs> list = this.debtPlanMapper.selectByExampleWithBLOBs(example);
		if (list != null && list.size() > 0) {
			DebtPlanWithBLOBs plan = list.get(0);
			if (plan != null) {
				// 计划状态:待审核
				plan.setDebtPlanStatus(1);
				AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
				// 修改人ID
				plan.setUpdateUserId(Integer.parseInt(adminSystem.getId()));
				// 修改时间
				plan.setUpdateTime(GetDate.getMyTimeInMillis());
				// 修改人用户名
				plan.setUpdateUserName(adminSystem.getUsername());
				this.debtPlanMapper.updateByPrimaryKeySelective(plan);
			}
		}
	}

	/**
	 * 根据计划编号检索计划关联的资产配置
	 * 
	 * @Title selectDebtPlanBorrowListByDebtPlanNid
	 * @param param
	 * @return
	 */
	@Override
	public List<DebtPlanBorrowCustomize> selectDebtPlanBorrowListByDebtPlanNid(Map<String, Object> param) {
		return this.planCustomizeMapper.selectDebtPlanBorrowListByDebtPlanNid(param);
	}

	/**
	 * 获取计划编号检索计划关联的资产配置件数
	 * 
	 * @Title countDebtPlanBorrowListByDebtPlanNid
	 * @param param
	 * @return
	 */
	public int countDebtPlanBorrowListByDebtPlanNid(Map<String, Object> param) {
		return this.planCustomizeMapper.countDebtPlanBorrowListByDebtPlanNid(param);
	}

	/**
	 * 审核后,更新计划信息
	 * 
	 * @Title updatePlanInfo
	 * @param form
	 */
	@Override
	public void updatePlanInfo(PlanReleaseBean form) {
		// 计划编号
		String debtPlanNid = form.getDebtPlanNid();
		if (StringUtils.isNotEmpty(debtPlanNid)) {
			// 根据计划编号获取计划详情
			DebtPlanWithBLOBs plan = this.selectPlanInfoWithBLOBsByDebtPlanNid(debtPlanNid);
			if (plan != null) {
				// 审核通过
				if (CustomConstants.PLAN_ISAUDITS_YES.equals(form.getIsAudits())) {
					if (plan.getBuyBeginTime() > GetDate.getMyTimeInMillis()) {
						plan.setDebtPlanStatus(CustomConstants.DEBT_PLAN_STATUS_3);
					} else {
						plan.setDebtPlanStatus(CustomConstants.DEBT_PLAN_STATUS_4);
						// 计划的redits的设置
						RedisUtils.set(plan.getDebtPlanNid(), String.valueOf(plan.getDebtPlanMoney()));
					}
					plan.setDebtPlanMoneyWait(plan.getDebtPlanMoney());
				} else {
					// 审核不通过
					plan.setDebtPlanStatus(CustomConstants.DEBT_PLAN_STATUS_2);
				}
				// 审核时间
				plan.setAuditTime(GetDate.getMyTimeInMillis());
				AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
				// 审核人ID
				plan.setAuditUserId(Integer.valueOf(adminSystem.getId()));
				// 审核备注
				plan.setAuditRemark(StringUtils.isEmpty(form.getRemark()) ? StringUtils.EMPTY : form.getRemark());
				// 更新时间
				plan.setUpdateTime(GetDate.getMyTimeInMillis());
				plan.setUpdateUserId(Integer.valueOf(adminSystem.getId()));
				plan.setUpdateUserName(adminSystem.getUsername());

				// 更新
				this.debtPlanMapper.updateByPrimaryKeySelective(plan);
			}
		}
	}

	/**
	 * 检索已关联资产总计
	 * 
	 * @Title countDebtPlanBorrowListAmount
	 * @param param
	 * @return
	 */
	@Override
	public Map<String, Object> countDebtPlanBorrowListAmount(Map<String, Object> param) {
		return this.planCustomizeMapper.countDebtPlanBorrowListAmount(param);
	}

}
