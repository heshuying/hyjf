package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.BankAccountManageCustomize;

public interface BankAccountManageCustomizeMapper {
	/**
	 * 账户管理 （账户数量）
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public Integer queryAccountCount(BankAccountManageCustomize accountInfoCustomize);
	
	/**
	 * 账户管理 （账户数量）总数
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public Integer queryAccountCountAll(BankAccountManageCustomize accountInfoCustomize);

	/**
	 * 账户管理 （列表）
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public List<BankAccountManageCustomize> queryAccountInfos(BankAccountManageCustomize accountInfoCustomize);

}
