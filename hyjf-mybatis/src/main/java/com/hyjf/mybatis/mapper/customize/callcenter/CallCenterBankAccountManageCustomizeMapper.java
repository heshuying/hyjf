package com.hyjf.mybatis.mapper.customize.callcenter;

import java.util.List;

import com.hyjf.mybatis.model.customize.callcenter.CallCenterBankAccountManageCustomize;

public interface CallCenterBankAccountManageCustomizeMapper {
	/**
	 * 账户管理 （列表）
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public List<CallCenterBankAccountManageCustomize> queryAccountInfos(CallCenterBankAccountManageCustomize accountInfoCustomize);

}
