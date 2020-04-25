package com.hyjf.admin.manager.plan;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanConfig;
import com.hyjf.mybatis.model.auto.DebtPlanConfigExample;
import com.hyjf.mybatis.model.auto.DebtPlanExample;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;

import cn.jpush.api.utils.StringUtils;

@Service
public class PlanServiceImpl extends BaseServiceImpl implements PlanService {

	/**
	 * 
	 * @method: getPlanTypeList
	 * @description: 计划类型查询
	 * @return
	 * @return: List<DebtPlanType>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月12日 下午5:57:15
	 */
	@Override
	public List<DebtPlanConfig> getPlanTypeList() {

		DebtPlanConfigExample example = new DebtPlanConfigExample();
		return this.debtPlanConfigMapper.selectByExample(example);
	}

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
			cra.andDebtPlanTypeEqualTo(Integer.valueOf(planCommonCustomize.getPlanTypeSrch()));
		}
		// 计划状态
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
		// 排序
		example.setOrderByClause("create_time Desc");

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
			cra.andDebtPlanTypeEqualTo(Integer.valueOf(planCommonCustomize.getPlanTypeSrch()));
		}
		// 计划状态
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
		// 排序
		example.setOrderByClause("create_time Desc");
		// 分页
		if (planCommonCustomize.getLimitStart() >= 0) {
			example.setLimitStart(planCommonCustomize.getLimitStart());
			example.setLimitEnd(planCommonCustomize.getLimitEnd());
		}

		List<DebtPlan> result = this.debtPlanMapper.selectByExample(example);

		return result;
	}

	/**
	 * 
	 * @method: exportPlanList
	 * @description: 计划列表查询
	 * @return: List<DebtPlan>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月12日 下午5:57:15
	 */
	@Override
	public List<DebtPlan> exportPlanList(PlanCommonCustomize planCommonCustomize) {
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
			cra.andDebtPlanTypeEqualTo(Integer.valueOf(planCommonCustomize.getPlanTypeSrch()));
		}
		// 计划状态
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
		List<DebtPlan> result = this.debtPlanMapper.selectByExample(example);
		return result;
	}

	/**
	 * 获取计划金额总计
	 * 
	 * @Title getPlanAccountTotal
	 * @param planCommonCustomize
	 * @return
	 */
	@Override
	public Map<String, Object> getPlanAccountTotal(PlanCommonCustomize planCommonCustomize) {
		return this.planCustomizeMapper.getPlanAccountTotal(planCommonCustomize);
	}
}
