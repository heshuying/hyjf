package com.hyjf.api.server.asset;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.customize.AssetDetailCustomize;

public interface AssetSearchService extends BaseService {

	/**
	 * 查询资产状态
	 *
	 * @param bean
	 * @return
	 */
	AssetDetailCustomize selectStatusById(String assetId, String instCode);
}
