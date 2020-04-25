package com.hyjf.bank.service.user.transpassword;

import com.hyjf.bank.service.BaseAjaxResultBean;

public class TransPasswordAjaxBean extends BaseAjaxResultBean {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = -8830215900809194471L;
	//业务授权码
	private String info;
	/** 最大时间 */
	private Integer maxValidTime;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getMaxValidTime() {
        return maxValidTime;
    }

    public void setMaxValidTime(Integer maxValidTime) {
        this.maxValidTime = maxValidTime;
    }
	
	
}
