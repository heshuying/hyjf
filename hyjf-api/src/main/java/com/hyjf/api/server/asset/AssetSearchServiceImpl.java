package com.hyjf.api.server.asset;

import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.AssetDetailCustomize;

@Service
public class AssetSearchServiceImpl extends BaseServiceImpl implements AssetSearchService {

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
