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

public class WebUserTradeListCustomize {

	// 主键
	public String id;
	// 交易日期
	public String time;
	// 交易类型
	public String trade;
	// 交易类型 +-
	public String type;
	// 交易金额(交易类型名称)
	public String typeName;
	// 交易金额
	public String money;
	// 可用余额
	public String balance;
	// 备注
	public String remark;
	//收支类型：1收入2支出3冻结
	public String revuAndExpType;
	//状态 :交易状态0: 失败 1：成功 2:冲正
	public String tradeStatus;
	//操作用户  是否是银行的交易记录(0:否,1:是)
	public String isBank;
	
	/** * new added */
	// 用户角色:1出借人2借款人3担保机构
	public String roleId;
	// 标的号
	public String borrowNid;
	
	/**
	 * 构造方法
	 */
	public WebUserTradeListCustomize() {
		super();
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTrade() {
		return trade;
	}

	public void setTrade(String trade) {
		this.trade = trade;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRevuAndExpType() {
		return revuAndExpType;
	}

	public void setRevuAndExpType(String revuAndExpType) {
		this.revuAndExpType = revuAndExpType;
	}

	public String getIsBank() {
		return isBank;
	}

	public void setIsBank(String isBank) {
		this.isBank = isBank;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}
	
	
   
}
