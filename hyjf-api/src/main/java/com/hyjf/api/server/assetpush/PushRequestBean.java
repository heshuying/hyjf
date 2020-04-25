package com.hyjf.api.server.assetpush;

import java.util.List;

import com.hyjf.api.server.assetriskinfo.InfoBean;
import com.hyjf.base.bean.BaseBean;

/**
 * 资产推送请求参数
 */
public class PushRequestBean extends BaseBean {
	

    private Integer assetType;
	
	private List<PushBean> reqData;
	
	private List<InfoBean> riskInfo;

	public List<PushBean> getReqData() {
		return reqData;
	}

	public void setReqData(List<PushBean>  reqData) {
		this.reqData = reqData;
	}

	public Integer getAssetType() {
		return assetType;
	}

	public void setAssetType(Integer assetType) {
		this.assetType = assetType;
	}

	/**
	 * riskInfo
	 * @return the riskInfo
	 */
		
	public List<InfoBean> getRiskInfo() {
		return riskInfo;
			
	}

	/**
	 * @param riskInfo the riskInfo to set
	 */
		
	public void setRiskInfo(List<InfoBean> riskInfo) {
		this.riskInfo = riskInfo;
			
	}
    
	
	
}
