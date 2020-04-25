package com.hyjf.api.aems.asset;

import com.hyjf.api.server.asset.AssetSearchService;
import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.AssetDetailCustomize;

@Service
public class AemsAssetSearchServiceImpl extends BaseServiceImpl implements AemsAssetSearchService {

	/**
	 * 查询资产状态
	 * @param assetId
	 * @return
	 * @author LiuBin
	 */
	@Override
	public AssetDetailCustomize selectStatusById(String assetId, String instCode) {
		return assetListCustomizeMapper.selectAssetStatus(assetId, instCode);
	}

}
