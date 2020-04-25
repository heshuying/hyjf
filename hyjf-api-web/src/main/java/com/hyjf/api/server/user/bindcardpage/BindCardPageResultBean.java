package com.hyjf.api.server.user.bindcardpage;

import com.hyjf.base.bean.BaseMapBean;

public class BindCardPageResultBean extends BaseMapBean {
	
	/**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 3709370958884607483L;
    
    private String callBackAction;

    public String getCallBackAction() {
        return callBackAction;
    }

    public void setCallBackAction(String callBackAction) {
        this.callBackAction = callBackAction;
    }
    
	
}
