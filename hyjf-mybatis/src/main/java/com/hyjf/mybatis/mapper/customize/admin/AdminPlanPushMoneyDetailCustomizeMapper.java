package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.AdminPlanPushMoneyDetailCustomize;

public interface AdminPlanPushMoneyDetailCustomizeMapper {

	/**
	 * 检索提成明细列表
	 * 
	 * @Title selectCommissionList
	 * @param param
	 * @return
	 */
	public List<AdminPlanPushMoneyDetailCustomize> selectCommissionList(Map<String, Object> param);

	/**
	 * 检索提成列表件数
	 * 
	 * @Title countRecordTotal
	 * @param param
	 * @return
	 */
	public int countRecordTotal(Map<String, Object> param);
}
