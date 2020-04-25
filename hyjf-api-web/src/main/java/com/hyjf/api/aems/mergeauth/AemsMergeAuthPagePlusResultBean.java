package com.hyjf.api.aems.mergeauth;

import com.hyjf.base.bean.BaseMapBean;

/**
 * 用户开户结果Bean
 * 
 * @author liuyang
 *
 */
public class AemsMergeAuthPagePlusResultBean extends BaseMapBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -821943657829999082L;
	 // 返回信息
    private String returnMsg;
    
	// 电子账户号
	private String accountId;
	private String callBackAction;
	private String  acqRes;


	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

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
