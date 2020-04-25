package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.HtlUserInfoCustomize;

public interface HtlUserInfoCustomizeMapper {

	/**
	 * 获取汇天利账户信息
	 * @param htlUserInfo
	 * @return
	 */
	List<HtlUserInfoCustomize> selectHtlUserInfos(HtlUserInfoCustomize htlUserInfo);


	/**
	 * 获得列表数
	 * 
	 * @param ProductInterestCustomize
	 * @return
	 */
	Integer countHtlUserInfoRecord(HtlUserInfoCustomize htlUserInfoCustomize);

	/**
	 * 获取汇天利用户
	 * @param htlUserInfoCustomize
	 * @return
	 */
	HtlUserInfoCustomize selectHtlUserPrincipal(HtlUserInfoCustomize htlUserInfoCustomize);
	
}