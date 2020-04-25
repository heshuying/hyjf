package com.hyjf.api.server.assetriskinfo;


import java.util.List;

import com.hyjf.base.service.BaseService;

public interface RiskInfoService extends BaseService {

	/**
	 * 插入资产表
	 *
	 * @param bean
	 * @return
	 */
	void insertRiskInfo(List<InfoBean> infobeans);
}
