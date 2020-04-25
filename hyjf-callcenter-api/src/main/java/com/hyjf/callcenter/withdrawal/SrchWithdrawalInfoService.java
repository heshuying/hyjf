package com.hyjf.callcenter.withdrawal;

import java.util.List;

import com.hyjf.mybatis.model.customize.callcenter.CallcenterWithdrawCustomize;

/**
 * Description:按照用户名/手机号查询提现明细
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: LIBIN
 * @version: 1.0
 *           Created at: 2017年7月15日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */
public interface SrchWithdrawalInfoService {
	
	/**
	 * 获取提现列表
	 *
	 * @param form
	 * @return
	 */
	public List<CallcenterWithdrawCustomize> getWithdrawRecordList(CallcenterWithdrawCustomize callcenterWithdrawCustomize);
	
}
