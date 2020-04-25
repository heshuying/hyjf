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

package com.hyjf.mybatis.model.customize.batch;

import java.io.Serializable;

/**
 * @author 王坤
 */

public class BatchDebtPlanBorrowCustomize implements Serializable {

	/**
	 *序列化id
	 */
	private static final long serialVersionUID = -4418452245247248461L;
	// 项目id
	private String borrowNid;
	// 项目标题
	private String borrowName;
	// 项目年华收益率
	private String borrowApr;
	// 项目金额
	private String borrowAccount;
	// 项目金额
	private String borrowAccountWait;
	// 项目进度
	private String borrowSchedule;
	// 项目状态
	private String status;
    
	/**
	 * 构造方法
	 */

	public BatchDebtPlanBorrowCustomize() {
		super();
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	public String getBorrowName() {
		return borrowName;
	}

	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}

	public String getBorrowApr() {
		return borrowApr;
	}

	public void setBorrowApr(String borrowApr) {
		this.borrowApr = borrowApr;
	}

	public String getBorrowAccount() {
		return borrowAccount;
	}

	public void setBorrowAccount(String borrowAccount) {
		this.borrowAccount = borrowAccount;
	}

	public String getBorrowAccountWait() {
		return borrowAccountWait;
	}

	public void setBorrowAccountWait(String borrowAccountWait) {
		this.borrowAccountWait = borrowAccountWait;
	}

	public String getBorrowSchedule() {
		return borrowSchedule;
	}

	public void setBorrowSchedule(String borrowSchedule) {
		this.borrowSchedule = borrowSchedule;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
