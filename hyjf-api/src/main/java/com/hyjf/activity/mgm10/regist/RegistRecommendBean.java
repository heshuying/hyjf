package com.hyjf.activity.mgm10.regist;

import java.io.Serializable;

import com.hyjf.base.bean.BaseBean;

public class RegistRecommendBean extends BaseBean implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2569482809922162226L;
	
	private Integer inviteUser;
	
	private Integer inviteByUser;

	public Integer getInviteUser() {
		return inviteUser;
	}

	public void setInviteUser(Integer inviteUser) {
		this.inviteUser = inviteUser;
	}

	public Integer getInviteByUser() {
		return inviteByUser;
	}

	public void setInviteByUser(Integer inviteByUser) {
		this.inviteByUser = inviteByUser;
	}
	
}
