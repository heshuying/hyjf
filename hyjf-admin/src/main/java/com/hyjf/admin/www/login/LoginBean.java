package com.hyjf.admin.www.login;

import java.io.Serializable;

public class LoginBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1319285566828004025L;

	private String username;

	private String password;

	private String remember;

	private String validateCode;//新加验证码

	/**
	 * remember
	 * 
	 * @return the remember
	 */

	public String getRemember() {
		return remember;
	}

	/**
	 * @param remember
	 *            the remember to set
	 */

	public void setRemember(String remember) {
		this.remember = remember;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
}
