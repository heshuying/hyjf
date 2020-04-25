package com.hyjf.api.server.user.auth.mergeauth;

import com.hyjf.base.bean.BaseMapBean;

public class MergeAuthPagePlusRetBean  extends BaseMapBean   {


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
