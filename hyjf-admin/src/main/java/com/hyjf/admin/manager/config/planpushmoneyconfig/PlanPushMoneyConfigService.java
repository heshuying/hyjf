package com.hyjf.admin.manager.config.planpushmoneyconfig;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.DebtCommissionConfig;

public interface PlanPushMoneyConfigService extends BaseService {

	/**
	 * 获取提成配置
	 * 
	 * @Title selectCommissionList
	 * @param form
	 * @return
	 */
	public List<DebtCommissionConfig> selectCommissionList(PlanPushMoneyConfigBean form);

	/**
	 * 提成列表配置件数
	 * 
	 * @Title countCommissionList
	 * @param form
	 * @return
	 */
	public int countCommissionList(PlanPushMoneyConfigBean form);

	/**
	 * 根据ID获取提成配置信息
	 * 
	 * @Title getDebtCommissionConfigById
	 * @param id
	 * @return
	 */
	public DebtCommissionConfig getDebtCommissionConfigById(String id);
	
	

	/**
	 * 汇添金提成配置信息更新
	 * 
	 * @Title updateRecord
	 * @param form
	 */
	public void updateRecord(PlanPushMoneyConfigBean form);
	
	/**
	 * 汇添金提成配置信息新增
	 * 
	 * @Title insertRecord
	 * @param form
	 */
	public void insertRecord(PlanPushMoneyConfigBean form);
}
