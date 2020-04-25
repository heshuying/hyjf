package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.AccountManageCustomize;

public interface AccountManageCustomizeMapper {
	/**
	 * 账户管理 （账户数量）
	 * @param accountManageBean
	 * @return
	 */
	public Integer queryAccountCount(AccountManageCustomize accountInfoCustomize) ;
	/**
	 * 账户管理 （账户数量）总数
	 * @param accountManageBean
	 * @return
	 */
	public Integer queryAccountCountAll(AccountManageCustomize accountInfoCustomize) ;

	/**
	 * 账户管理 （列表）
	 * @param accountManageBean
	 * @return
	 */
	public List<AccountManageCustomize> queryAccountInfos(AccountManageCustomize accountInfoCustomize) ;         
	
	
}

	