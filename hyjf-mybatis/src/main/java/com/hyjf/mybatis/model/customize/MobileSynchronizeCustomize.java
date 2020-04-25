package com.hyjf.mybatis.model.customize;

import java.io.Serializable;

/**
 * 同步手机号Mapper
 * 
 * @author liuyang
 *
 */
public class MobileSynchronizeCustomize implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6455442854066228801L;

	/** 用户ID */
	private String userId;
	/** 用户名 */
	private String userName;
	/** 用户电子账户号 */
	private String accountId;
	/** 手机号 */
	private String mobile;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
