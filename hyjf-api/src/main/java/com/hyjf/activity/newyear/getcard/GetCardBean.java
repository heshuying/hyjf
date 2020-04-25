package com.hyjf.activity.newyear.getcard;

import com.hyjf.base.bean.BaseBean;


public class GetCardBean extends BaseBean {
	
	private String tenderNid;
	
	private Integer userId;
	
	private String inviteUserId;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getInviteUserId() {
        return inviteUserId;
    }

    public void setInviteUserId(String inviteUserId) {
        this.inviteUserId = inviteUserId;
    }

    public String getTenderNid() {
		return tenderNid;
	}

	public void setTenderNid(String tenderNid) {
		this.tenderNid = tenderNid;
	}

}
