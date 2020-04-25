package com.hyjf.mybatis.mapper.customize.callcenter;

import java.util.List;

import com.hyjf.mybatis.model.customize.callcenter.CallCenterAccountManageCustomize;

public interface CallCenterAccountManageCustomizeMapper {
	/**
	 * 账户管理 （列表）
	 * @param accountManageBean
	 * @return
	 */
	public List<CallCenterAccountManageCustomize> queryAccountInfos(CallCenterAccountManageCustomize callCenterAccountManageCustomize) ;         
	
	
}

	