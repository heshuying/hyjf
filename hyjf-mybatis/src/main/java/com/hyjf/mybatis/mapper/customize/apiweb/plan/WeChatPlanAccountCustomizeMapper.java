package com.hyjf.mybatis.mapper.customize.apiweb.plan;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatPlanAccountCustomize;

public interface WeChatPlanAccountCustomizeMapper {
	/**
	 * 
	 * @method: getPlanTypeList
	 * @description: 计划数量查询
	 * @return
	 * @return: List<DebtPlanType>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月12日 下午5:57:15
	 */
	int countPlanAccede(Map<String, Object> param);

	/**
	 * 
	 * @method: selectPlanList
	 * @description: 计划列表查询
	 * @return
	 * @return: List<DebtPlanType>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月12日 下午5:57:15
	 */
	List<WeChatPlanAccountCustomize> selectPlanAccedeList(Map<String, Object> param);

}