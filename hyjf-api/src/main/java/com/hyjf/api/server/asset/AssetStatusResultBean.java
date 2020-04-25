package com.hyjf.api.server.asset;

import com.hyjf.base.bean.BaseResultBean;


/**
 * 资产接口返回参数
 * @author xiaojohn
 *
 */
public class AssetStatusResultBean extends BaseResultBean {

	private static final long serialVersionUID = 1L;

	private String assetStatus;

	private String borrowNid;

	/**
	 * assetStatus
	 * @return the assetStatus
	 */
	
	public String getAssetStatus() {
		return assetStatus;
	}

	/**
	 * @param assetStatus the assetStatus to set
	 */
	
	public void setAssetStatus(String assetStatus) {
		this.assetStatus = assetStatus;
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}
	
	
}
