package com.hyjf.callcenter.balance;

import java.util.List;

import com.hyjf.mybatis.model.customize.callcenter.CallCenterAccountManageCustomize;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterBankAccountManageCustomize;

/**
 * Description:按照用户名/手机号查询账户余额
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: LIBIN
 * @version: 1.0
 *           Created at: 2017年7月15日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */
public interface SrchBalanceInfoService {

	/**
	 * 银行账户管理 （列表）
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public List<CallCenterBankAccountManageCustomize> queryBankAccountInfos(CallCenterBankAccountManageCustomize callCenterBankAccountManageCustomize);
	
	/**
	 * 汇付账户管理 （列表）
	 * @param accountManageBean
	 * @returns
	 */
	public List<CallCenterAccountManageCustomize> queryAccountInfos(CallCenterAccountManageCustomize callCenterAccountManageCustomize) ;

}
