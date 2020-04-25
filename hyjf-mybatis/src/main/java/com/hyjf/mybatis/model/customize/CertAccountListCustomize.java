package com.hyjf.mybatis.model.customize;

import com.hyjf.mybatis.model.auto.AccountList;

/**
 * 
 * @author pcc
 */
public class CertAccountListCustomize extends AccountList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1035131990766081594L;
	private Integer roleId;

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	
}
