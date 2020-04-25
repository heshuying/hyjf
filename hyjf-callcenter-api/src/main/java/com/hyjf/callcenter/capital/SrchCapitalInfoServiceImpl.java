package com.hyjf.callcenter.capital;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.customize.CustomizeMapper;
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
@Service
public class SrchCapitalInfoServiceImpl extends CustomizeMapper implements SrchCapitalInfoService {
    /**
     * 按照用户名/手机号查询资金明细
     * @param user
     * @return List<CallCenterAccountDetailCustomize>
     * @author LIBIN
     */
	@Override
	public List<CallCenterAccountDetailCustomize> queryAccountDetails(CallCenterAccountDetailCustomize callCenterAccountDetailCustomize) {
		List<CallCenterAccountDetailCustomize> accountInfos = this.callCenterAccountDetailCustomizeMapper
				.queryAccountDetails(callCenterAccountDetailCustomize);
		return accountInfos;
	}
	
}
