package com.hyjf.api.aems.asset;

import com.hyjf.base.bean.BaseResultBean;


/**
 * 资产接口返回参数
 * @author xiaojohn
 *
 */
public class AemsAssetStatusResultBean extends BaseResultBean {

	private static final long serialVersionUID = 1L;

	private String assetStatus;

	private String statusDesc;

	private String borrowNid;

	private String nid;

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

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

	@Override
	public String getStatusDesc() {
		return statusDesc;
	}

	@Override
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
}
