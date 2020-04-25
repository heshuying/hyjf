package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;

/**
 * 债权结束Customize
 * 
 * @author liuyang
 *
 */
public class AdminBankDebtEndCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6735716740778822593L;
	// 项目编号(展示)
	private String borrowNid;
	// 出借人用户Id(展示)
	private String userId;
	// 用户名
	private String userName;
	// 手机号
	private String mobile;
	// 出借订单号
	private String tenderNid;
	// 出借授权码(展示)
	private String authCode;
	// 出借本金
	private String recoverCapital;
	// 已债转金额
	private String creditAmount;
	// 出借订单号(检索用)
	private String tenderNidSrch;
	// 出借授权码(检索用)
	private String authCodeSrch;
	// 用户名(检索用)
	private String userNameSrch;
	// 手机号(检索用)
	private String mobileSrch;
	// 项目编号(检索用)
	private String borrowNidSrch;
	// 放款时间
	private String addTime;
	
	/** * 结束债权(新) 共用form  start */
	// 出借人用户Id(展示)
	private String tenderUserId;
	// 批次号(展示)
	private String batchNo;
	// 本批次交易流水号 (展示)
	private String seqNo;
	// 订单号 (展示)
	private String orderId;
	// 结束债权类型（1:还款，2:散标债转，3:计划债转） (展示)
    private String creditEndType;
    // 银行接受结果 (展示)
    private String received;
    // 银行交易状态（S-成功;F-失败;A-待处理;D-正在处理;C-撤销;）
    private String state;
    // 失败描述 (展示)
    private String failmsg;
	// 本批次交易日期(展示)
	private String txDate;
	// 本批次交易时间(展示)
	private String txTime;
	// 交易状态  0 初始 1 处理中 2 成功 3 处理失败
	private String status;
	// 总交易笔数
	private String txCounts;
	
	// 跟新时间
	private Integer updateTime;
	
	/**
	 * 融资用户ID(检索用)
	 */
	private Integer userIdSrch;
	/**
	 * 出借资用户ID(检索用)
	 */
	private Integer tenderUserIdSrch;
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
	private Integer statusSrch;
	/** * 结束债权(新) end */
	
	/**
	 * 检索条件 limitStart
	 */
	private int limitStart;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd;

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTenderNid() {
		return tenderNid;
	}

	public void setTenderNid(String tenderNid) {
		this.tenderNid = tenderNid;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getRecoverCapital() {
		return recoverCapital;
	}

	public void setRecoverCapital(String recoverCapital) {
		this.recoverCapital = recoverCapital;
	}

	public String getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(String creditAmount) {
		this.creditAmount = creditAmount;
	}

	public String getTenderNidSrch() {
		return tenderNidSrch;
	}

	public void setTenderNidSrch(String tenderNidSrch) {
		this.tenderNidSrch = tenderNidSrch;
	}

	public String getAuthCodeSrch() {
		return authCodeSrch;
	}

	public void setAuthCodeSrch(String authCodeSrch) {
		this.authCodeSrch = authCodeSrch;
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

	public String getBorrowNidSrch() {
		return borrowNidSrch;
	}

	public void setBorrowNidSrch(String borrowNidSrch) {
		this.borrowNidSrch = borrowNidSrch;
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

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTenderUserId() {
		return tenderUserId;
	}

	public void setTenderUserId(String tenderUserId) {
		this.tenderUserId = tenderUserId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCreditEndType() {
		return creditEndType;
	}

	public void setCreditEndType(String creditEndType) {
		this.creditEndType = creditEndType;
	}

	public String getReceived() {
		return received;
	}

	public void setReceived(String received) {
		this.received = received;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getFailmsg() {
		return failmsg;
	}

	public void setFailmsg(String failmsg) {
		this.failmsg = failmsg;
	}

	public String getTxDate() {
		return txDate;
	}

	public void setTxDate(String txDate) {
		this.txDate = txDate;
	}

	public String getTxTime() {
		return txTime;
	}

	public void setTxTime(String txTime) {
		this.txTime = txTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getUserIdSrch() {
		return userIdSrch;
	}

	public void setUserIdSrch(Integer userIdSrch) {
		this.userIdSrch = userIdSrch;
	}

	public Integer getTenderUserIdSrch() {
		return tenderUserIdSrch;
	}

	public void setTenderUserIdSrch(Integer tenderUserIdSrch) {
		this.tenderUserIdSrch = tenderUserIdSrch;
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

	public Integer getStatusSrch() {
		return statusSrch;
	}

	public void setStatusSrch(Integer statusSrch) {
		this.statusSrch = statusSrch;
	}

	public String getTxCounts() {
		return txCounts;
	}

	public void setTxCounts(String txCounts) {
		this.txCounts = txCounts;
	}

	public Integer getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Integer updateTime) {
		this.updateTime = updateTime;
	}
	
}
