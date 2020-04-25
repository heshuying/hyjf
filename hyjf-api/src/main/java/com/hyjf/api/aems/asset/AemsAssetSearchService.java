package com.hyjf.api.aems.asset;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.customize.AssetDetailCustomize;

public interface AemsAssetSearchService extends BaseService {

	/**
	 * 查询资产状态
	 *
	 * @param instCode
	 * @return
	 */
	AssetDetailCustomize selectStatusById(String assetId, String instCode);
}
