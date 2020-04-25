package com.hyjf.admin.manager.config.planpushmoneyconfig;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.mybatis.model.auto.DebtCommissionConfig;
import com.hyjf.mybatis.model.auto.DebtCommissionConfigExample;
import com.hyjf.mybatis.model.customize.AdminSystem;

@Service
public class PlanPushMoneyConfigServiceImpl extends BaseServiceImpl implements PlanPushMoneyConfigService {

	/**
	 * 检索提成配置列表
	 * 
	 * @Title selectCommissionList
	 * @param form
	 * @return
	 */
	@Override
	public List<DebtCommissionConfig> selectCommissionList(PlanPushMoneyConfigBean form) {
		DebtCommissionConfigExample example = new DebtCommissionConfigExample();
		DebtCommissionConfigExample.Criteria cra = example.createCriteria();
		cra.andCommissionTypeEqualTo(0);
		if (form.getLimitStart() >= 0) {
			example.setLimitStart(form.getLimitStart());
			example.setLimitEnd(form.getLimitEnd());
		}
		example.setOrderByClause(" create_time desc");
		return this.debtCommissionConfigMapper.selectByExample(example);
	}

	/**
	 * 提成列表配置件数
	 * 
	 * @Title countCommissionList
	 * @param form
	 * @return
	 */
	@Override
	public int countCommissionList(PlanPushMoneyConfigBean form) {
		DebtCommissionConfigExample example = new DebtCommissionConfigExample();
		DebtCommissionConfigExample.Criteria cra = example.createCriteria();
		cra.andCommissionTypeEqualTo(0);
		if (form.getLimitStart() >= 0) {
			example.setLimitStart(form.getLimitStart());
			example.setLimitEnd(form.getLimitEnd());
		}
		return this.debtCommissionConfigMapper.countByExample(example);
	}

	/**
	 * 根据ID获取提成配置详情
	 * 
	 * @Title getDebtCommissionConfigById
	 * @param id
	 * @return
	 */
	@Override
	public DebtCommissionConfig getDebtCommissionConfigById(String id) {
		DebtCommissionConfigExample example = new DebtCommissionConfigExample();
		DebtCommissionConfigExample.Criteria cra = example.createCriteria();
		cra.andCommissionTypeEqualTo(0);
		cra.andIdEqualTo(Integer.parseInt(id));
		List<DebtCommissionConfig> list = this.debtCommissionConfigMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 汇添金提成配置信息更新
	 * 
	 * @Title updateRecord
	 * @param form
	 */
	@Override
	public void updateRecord(PlanPushMoneyConfigBean form) {
		DebtCommissionConfig debtPlanConfigInfo = this.getDebtCommissionConfigById(form.getIds());
		BeanUtils.copyProperties(form, debtPlanConfigInfo);
		debtPlanConfigInfo.setId(Integer.parseInt(form.getIds()));
		this.debtCommissionConfigMapper.updateByPrimaryKeySelective(debtPlanConfigInfo);
	}

	/**
	 * 汇添金提成配置信息新增
	 * 
	 * @Title insertRecord
	 * @param form
	 */
	@Override
	public void insertRecord(PlanPushMoneyConfigBean form) {
		DebtCommissionConfig debtPlanConfig = new DebtCommissionConfig();
		BeanUtils.copyProperties(form, debtPlanConfig);
		debtPlanConfig.setDelFlag(0);
		AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
		debtPlanConfig.setCreateUserId(Integer.parseInt(adminSystem.getId()));
		debtPlanConfig.setCreateTime(GetDate.getMyTimeInMillis());
		debtPlanConfig.setCreateUserName(adminSystem.getUsername());
		// 提成类型:0:普通提成,1:超额提成
		debtPlanConfig.setCommissionType(0);
		debtPlanConfig.setUserType(0);
		this.debtCommissionConfigMapper.insertSelective(debtPlanConfig);
	}

}
