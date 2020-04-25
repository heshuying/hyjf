package com.hyjf.callcenter.undertake;

import java.util.List;

import com.hyjf.mybatis.model.customize.callcenter.CallCenterBorrowCreditCustomize;

/**
 * Description:按照用户名/手机号查询承接债权信息
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: LIBIN
 * @version: 1.0
 *           Created at: 2017年7月15日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */
public interface SrchUndertakeInfoService {
	/**
	 * 获取详细列表
	 * 
	 * @param borrowCreditCustomize
	 * @return
	 */
	public List<CallCenterBorrowCreditCustomize> selectBorrowCreditTenderList(CallCenterBorrowCreditCustomize callCenterBorrowCreditCustomize);

}
