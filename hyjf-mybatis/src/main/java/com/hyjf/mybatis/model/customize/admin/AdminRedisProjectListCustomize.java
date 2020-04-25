/**
 * Description:用户开户列表前端显示查询所用po
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by :
 */

package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;

/**
 * @author 王坤
 */

public class AdminRedisProjectListCustomize implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 5748630051215873837L;
	// 项目id
	private String borrowNid;
	// 项目状态
	private String status;
	// 剩余可投金额
	private String accountWait;

	/**
	 * 构造方法
	 */

	public AdminRedisProjectListCustomize() {
		super();
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAccountWait() {
		return accountWait;
	}

	public void setAccountWait(String accountWait) {
		this.accountWait = accountWait;
	}

}
