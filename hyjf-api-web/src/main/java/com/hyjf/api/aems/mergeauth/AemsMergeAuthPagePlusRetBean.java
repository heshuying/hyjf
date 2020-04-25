package com.hyjf.api.aems.mergeauth;

import com.hyjf.base.bean.BaseMapBean;

public class AemsMergeAuthPagePlusRetBean extends BaseMapBean {


    private String callBackAction;
    
    private String acqRes;
    
    public String getCallBackAction() {
        return callBackAction;
    }

    public void setCallBackAction(String callBackAction) {
        this.callBackAction = callBackAction;
    }

	public String getAcqRes() {
		return acqRes;
	}

	public void setAcqRes(String acqRes) {
		this.acqRes = acqRes;
	}
    
    
}
