package com.hyjf.callcenter.recharge;

import java.util.List;

import com.hyjf.mybatis.model.customize.callcenter.CallCenterRechargeCustomize;

/**
 * Description:按照用户名/手机号查询充值明细
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: LIBIN
 * @version: 1.0
 *           Created at: 2017年7月15日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */
public interface SrchRechargeInfoService {
	
	/**
	 * 充值管理 （列表）
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public List<CallCenterRechargeCustomize> queryRechargeList(CallCenterRechargeCustomize callCenterRechargeCustomize);

}
