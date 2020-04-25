package com.hyjf.admin.exception.bankdebtend;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

public class BankDebtEndBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3810166239044858604L;

	/**
	 * 用户Id
	 */
	private Integer userId;
	
	/**
	 * (融资)用户名(检索用)
	 */
	private String userNameSrch;

	/**
	 * 项目编号
	 */
	private String borrowNid;

	/**
	 * 出借订单号
	 */
	private String tenderNid;
	
	/**
	 * 手机号(检索用)
	 */
	private String mobileSrch;

	/**
	 * 出借授权码(检索用)
	 */
	private String authCodeSrch;
	/**
	 * 出借订单号(检索用)
	 */
	private String tenderNidSrch;

	/**
	 * 项目编号(检索用)
	 */
	private String borrowNidSrch;
	
	/** * 结束债权(新) 共用form  start */
	/**
	 * 总笔数
	 */
	private String txCounts;
	/**
	 * 期数
	 */
	private String txDate;
	/**
	 * 批次
	 */
	private String batchNo;
	/**
	 * 批次状态
	 */
	private String status;
	/**
	 * 融资用户ID(检索用)
	 */
	private String userIdSrch;
	
	/**
	 * 出借资用户ID(检索用)
	 */
	private String tenderUserIdSrch;
	
	/**
	 * 批次号(检索用)
	 */
	private String batchNoSrch;
	/**
	 * 订单号(检索用)
	 */
	private String orderIdSrch;
	
	/**
	 * 状态(检索用)
	 */
	private String statusSrch;
	/**
	 * 订单号
	 */
	private String orderId;
	
	
	/** * 结束债权(新) end */

	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	public int getPaginatorPage() {
		if (paginatorPage == 0) {
			paginatorPage = 1;
		}
		return paginatorPage;
	}

	public void setPaginatorPage(int paginatorPage) {
		this.paginatorPage = paginatorPage;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public int getLimitStart() {
		return limitStart;
	}

	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	public int getLimitEnd() {
		return limitEnd;
	}

	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}

	public String getUserNameSrch() {
		return userNameSrch;
	}

	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
	}

	public String getMobileSrch() {
		return mobileSrch;
	}

	public void setMobileSrch(String mobileSrch) {
		this.mobileSrch = mobileSrch;
	}

	public String getAuthCodeSrch() {
		return authCodeSrch;
	}

	public void setAuthCodeSrch(String authCodeSrch) {
		this.authCodeSrch = authCodeSrch;
	}

	public String getTenderNidSrch() {
		return tenderNidSrch;
	}

	public void setTenderNidSrch(String tenderNidSrch) {
		this.tenderNidSrch = tenderNidSrch;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	public String getTenderNid() {
		return tenderNid;
	}

	public void setTenderNid(String tenderNid) {
		this.tenderNid = tenderNid;
	}

	public String getBorrowNidSrch() {
		return borrowNidSrch;
	}

	public void setBorrowNidSrch(String borrowNidSrch) {
		this.borrowNidSrch = borrowNidSrch;
	}

	public String getBatchNoSrch() {
		return batchNoSrch;
	}

	public void setBatchNoSrch(String batchNoSrch) {
		this.batchNoSrch = batchNoSrch;
	}

	public String getOrderIdSrch() {
		return orderIdSrch;
	}

	public void setOrderIdSrch(String orderIdSrch) {
		this.orderIdSrch = orderIdSrch;
	}

	public String getStatusSrch() {
		return statusSrch;
	}

	public void setStatusSrch(String statusSrch) {
		this.statusSrch = statusSrch;
	}

	public String getUserIdSrch() {
		return userIdSrch;
	}

	public void setUserIdSrch(String userIdSrch) {
		this.userIdSrch = userIdSrch;
	}

	public String getTenderUserIdSrch() {
		return tenderUserIdSrch;
	}

	public void setTenderUserIdSrch(String tenderUserIdSrch) {
		this.tenderUserIdSrch = tenderUserIdSrch;
	}

	public String getTxCounts() {
		return txCounts;
	}

	public void setTxCounts(String txCounts) {
		this.txCounts = txCounts;
	}

	public String getTxDate() {
		return txDate;
	}

	public void setTxDate(String txDate) {
		this.txDate = txDate;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	
}
