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

public class WebUserRechargeListCustomize {

	// 主键
	public String id;
	// 交易日期
	public String time;
	// 费用
	public String fee;
	// 交易金额
	public String money;
	// 到账金额
	public String balance;
	// 状态 
	public String status;
	
	//汇付天下 或者 江西银行
	public String isBank;

	/**
	 * 构造方法
	 */

	public WebUserRechargeListCustomize() {
		super();
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
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

	public String getIsBank() {
		return isBank;
	}

	public void setIsBank(String isBank) {
		this.isBank = isBank;
	}
	
}
