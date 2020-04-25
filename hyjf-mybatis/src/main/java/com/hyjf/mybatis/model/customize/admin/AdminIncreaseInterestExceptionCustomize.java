package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;

/**
 * 融通宝加息异常处理
 * 
 * @ClassName AdminIncreaseInterestExceptionCustomize
 * @author liuyang
 * @date 2017年1月6日 上午9:29:53
 */
public class AdminIncreaseInterestExceptionCustomize implements Serializable {

	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = -1454363224648552693L;

	private String id;
	/** 项目编号 */
	private String borrowNid;

	/** 借款方式 */
	private String borrowStyle;
	/** 借款方式名称 */
	private String borrowStyleName;

	/** 转账状态 */
	private String repayStatus;

	/** 应还时间 */
	private String repayTime;

	/** 产品加息收益率 */
	private String borrowExtraYield;

	/** 产品加息收益 */
	private String repayInterest;

	/** 失败原因 */
	private String data;

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	public String getBorrowStyle() {
		return borrowStyle;
	}

	public void setBorrowStyle(String borrowStyle) {
		this.borrowStyle = borrowStyle;
	}

	public String getBorrowStyleName() {
		return borrowStyleName;
	}

	public void setBorrowStyleName(String borrowStyleName) {
		this.borrowStyleName = borrowStyleName;
	}

	public String getRepayStatus() {
		return repayStatus;
	}

	public void setRepayStatus(String repayStatus) {
		this.repayStatus = repayStatus;
	}

	public String getRepayTime() {
		return repayTime;
	}

	public void setRepayTime(String repayTime) {
		this.repayTime = repayTime;
	}

	public String getBorrowExtraYield() {
		return borrowExtraYield;
	}

	public void setBorrowExtraYield(String borrowExtraYield) {
		this.borrowExtraYield = borrowExtraYield;
	}

	public String getRepayInterest() {
		return repayInterest;
	}

	public void setRepayInterest(String repayInterest) {
		this.repayInterest = repayInterest;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
