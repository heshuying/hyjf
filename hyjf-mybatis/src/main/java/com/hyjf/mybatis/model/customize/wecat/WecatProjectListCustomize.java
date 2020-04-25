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

package com.hyjf.mybatis.model.customize.wecat;

/**
 * @author 王坤
 */

public class WecatProjectListCustomize {

	// 项目编号
	public String borrowNid;
	// 资产编号（风险缓释金）
	public String borrowAssetNumber;
	// 项目类型
	public String type;
	// 项目名称
	public String name;
	// 项目金额
	public String account;
	// 项目年华收益率
	public String borrow_apr;
	// 项目加息年华收益率
	public String borrowExtraYield;
	// 项目期限
	public String borrow_period_name;
	// 项目进度
	public String borrow_time_name;
	// 项目状态
	public String borrow_status;
	// 项目状态
	public String borrow_account_wait;
	// 时间
	public String borrow_type_name;
	// 时间戳
	public String borrow_account_scale;

	/**
	 * 构造方法
	 */

	public WecatProjectListCustomize() {
		super();
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getBorrow_apr() {
		return borrow_apr;
	}

	public void setBorrow_apr(String borrow_apr) {
		this.borrow_apr = borrow_apr;
	}

	public String getBorrow_period_name() {
		return borrow_period_name;
	}

	public void setBorrow_period_name(String borrow_period_name) {
		this.borrow_period_name = borrow_period_name;
	}

	public String getBorrow_time_name() {
		return borrow_time_name;
	}

	public void setBorrow_time_name(String borrow_time_name) {
		this.borrow_time_name = borrow_time_name;
	}

	public String getBorrow_status() {
		return borrow_status;
	}

	public void setBorrow_status(String borrow_status) {
		this.borrow_status = borrow_status;
	}

	public String getBorrow_account_wait() {
		return borrow_account_wait;
	}

	public void setBorrow_account_wait(String borrow_account_wait) {
		this.borrow_account_wait = borrow_account_wait;
	}

	public String getBorrow_type_name() {
		return borrow_type_name;
	}

	public void setBorrow_type_name(String borrow_type_name) {
		this.borrow_type_name = borrow_type_name;
	}

	public String getBorrow_account_scale() {
		return borrow_account_scale;
	}

	public void setBorrow_account_scale(String borrow_account_scale) {
		this.borrow_account_scale = borrow_account_scale;
	}


	public String getBorrowExtraYield() {
		return borrowExtraYield;
	}

	public void setBorrowExtraYield(String borrowExtraYield) {
		this.borrowExtraYield = borrowExtraYield;
	}

	public String getBorrowAssetNumber() {
		return borrowAssetNumber;
	}

	public void setBorrowAssetNumber(String borrowAssetNumber) {
		this.borrowAssetNumber = borrowAssetNumber;
	}

}
