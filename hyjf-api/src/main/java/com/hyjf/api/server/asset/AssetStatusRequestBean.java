package com.hyjf.api.server.asset;

import java.io.Serializable;

import com.hyjf.base.bean.BaseBean;

/**
 * 资产查询请求参数
 */
public class AssetStatusRequestBean extends BaseBean implements Serializable{
	
	private static final long serialVersionUID = 32143440657771846L;
	
    private String assetId;
    
	/**
	 * assetId
	 * @return the assetId
	 */
	
	public String getAssetId() {
		return assetId;
	}

	/**
	 * @param assetId the assetId to set
	 */
	
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

}

