package com.hyjf.callcenter.capital;

import java.util.List;

import com.hyjf.mybatis.model.customize.callcenter.CallCenterAccountDetailCustomize;

/**
 * Description:按照用户名/手机号查询资金明细
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: LIBIN
 * @version: 1.0
 *           Created at: 2017年7月15日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */
public interface SrchCapitalInfoService {
	
	/**
	 * 资金明细（列表）
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public List<CallCenterAccountDetailCustomize> queryAccountDetails(CallCenterAccountDetailCustomize callCenterAccountDetailCustomize);

}
