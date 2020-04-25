package com.hyjf.admin.manager.borrow.borrowrepayment;

import java.io.Serializable;

import com.hyjf.mybatis.model.customize.BorrowRepaymentCustomize;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class RepayCustomizeBean extends BorrowRepaymentCustomize implements Serializable {

	/**
	 * serialVersionUID:TODO 这个变量是干什么的
	 */

	private static final long serialVersionUID = 1L;

	private String password;

	private String nid;

	/**
	 * nid
	 * 
	 * @return the nid
	 */

	public String getNid() {
		return nid;
	}

	/**
	 * @param nid
	 *            the nid to set
	 */

	public void setNid(String nid) {
		this.nid = nid;
	}

	public RepayCustomizeBean() {
		super();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
