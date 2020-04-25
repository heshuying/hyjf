package com.hyjf.api.aems.assetriskinfo;


import com.hyjf.base.service.BaseService;

import java.util.List;

public interface AemsRiskInfoService extends BaseService {

	/**
	 * 插入资产表
	 *
	 * @param bean
	 * @return
	 */
	void insertRiskInfo(List<AemsInfoBean> infobeans);
}
