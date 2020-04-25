package com.hyjf.mybatis.mapper.customize.callcenter;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountTrade;
import com.hyjf.mybatis.model.customize.AccountDetailCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminAccountDetailDataRepairCustomize;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterAccountDetailCustomize;

public interface CallCenterAccountDetailCustomizeMapper {
	/**
	 * 资金明细 （列表）
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public List<CallCenterAccountDetailCustomize> queryAccountDetails(CallCenterAccountDetailCustomize callCenterAccountDetailCustomize);

}
