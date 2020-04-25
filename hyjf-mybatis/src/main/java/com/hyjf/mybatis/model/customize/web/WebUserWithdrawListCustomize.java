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

package com.hyjf.mybatis.model.customize.web;

/**
 * @author 王坤
 */

public class WebUserWithdrawListCustomize {

	// 主键
	public String id;
	// 取现时间
	public String time;
	// 取现金额
	public String money;
	// 取现费用
	public String fee;
	// 到账金额
	public String balance;
	// 取现状态
	public String status;
	// 银行存管提现标志位 1为银行存管   0汇付天下
	public String bankFlag;

	/**
	 * 构造方法
	 */

	public WebUserWithdrawListCustomize() {
		super();
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
