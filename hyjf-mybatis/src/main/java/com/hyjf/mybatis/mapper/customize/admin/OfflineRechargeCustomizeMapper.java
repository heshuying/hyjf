package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.OfflineRechargeCustomize;

public interface OfflineRechargeCustomizeMapper {
	/**
	 * 取得需要查询的线下充值的用户信息
	 * @param paramMap
	 * @return
	 */
	List<OfflineRechargeCustomize> selectUserAccount(Map<String,Object> paramMap);
}