package com.hyjf.api.server.tradelist;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hyjf.base.bean.BaseBean;

/**
 * 用户交易明细
 */
public class TradeListBean extends BaseBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8624578044153859497L;
	
	/**
	 * 交易开始时间(必传)
	 */
	public String startDate;//暂定 2017-10-01(不带时分秒) 
	
	/**
	 * 交易结束时间(必传)
	 */
	public String endDate;// 暂定2017-10-07
	
	/**
	 * 手机号(必传)
	 */
	public String phone;//校验手机号会否合法
	
	/**
	 * 电子账号(必传)
	 */
	private String accountId;//通过手机号验重
	
	/**
	 * 订单号(选传)
	 */
	private String nid;
	
	/**
	 * 用户名(通过必传手机号查询)
	 */
	private String username;
	
	/**
	 * 资金托管平台(默认)
	 */
	private String isBank;//默认为1 江西银行

	/**
	 * 交易状态(选传)
	 */
	private String tradeStatus;//0失败，1成功 2:冲正是否是数字验证

	/**
	 * 收支类型(选传)
	 */
	private String typeSearch;//1收入2支出3冻结4解冻
	
	/**
	 * 交易类型ID(选传)
	 */
	private String tradeTypeSearch;//选几个值固定给宽策略 是否是数字验证
	

	/*------------------------------------以上为请求参数-------------------------------*/
	/*------------------------------------以下为返回参数-------------------------------*/
	/**
	 * 部门名称
	 */
	private String departmentName;
	
	//private int userId; base已有
	
	/**
	 * 推荐组
	 */
	private String referrerGroup;
	
	/**
	 * 操作类型
	 */
	private String type;
	
	/**
	 * 交易类型
	 */
	private String tradeType;
	
	/**
	 * 操作金额
	 */
	private BigDecimal amount;
	
	/**
	 * 可用余额
	 */
	private BigDecimal balance;
	
	/**
	 * 冻结金额
	 */
	private BigDecimal frost;
	
	/**
	 * 智投服务可用余额
	 */
	private BigDecimal planBalance;

	/**
	 * 汇添金冻结金额
	 */
	private BigDecimal planFrost;
	
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 交易时间
	 */
	private String createTime;
	
	/**
	 * 银行总资产
	 */
	private String bankTotal;

	/**
	 * 银行存管余额
	 */
	private BigDecimal bankBalance;

	/**
	 * 银行存管冻结金额
	 */
	private BigDecimal bankFrost;
	
	/**
	 * account_list 主键
	 */
	private int id;
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getTypeSearch() {
		return typeSearch;
	}

	public void setTypeSearch(String typeSearch) {
		this.typeSearch = typeSearch;
	}

	public String getTradeTypeSearch() {
		return tradeTypeSearch;
	}

	public void setTradeTypeSearch(String tradeTypeSearch) {
		this.tradeTypeSearch = tradeTypeSearch;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getReferrerGroup() {
		return referrerGroup;
	}

	public void setReferrerGroup(String referrerGroup) {
		this.referrerGroup = referrerGroup;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getFrost() {
		return frost;
	}

	public void setFrost(BigDecimal frost) {
		this.frost = frost;
	}

	public BigDecimal getPlanBalance() {
		return planBalance;
	}

	public void setPlanBalance(BigDecimal planBalance) {
		this.planBalance = planBalance;
	}

	public BigDecimal getPlanFrost() {
		return planFrost;
	}

	public void setPlanFrost(BigDecimal planFrost) {
		this.planFrost = planFrost;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getBankTotal() {
		return bankTotal;
	}

	public void setBankTotal(String bankTotal) {
		this.bankTotal = bankTotal;
	}

	public BigDecimal getBankBalance() {
		return bankBalance;
	}

	public void setBankBalance(BigDecimal bankBalance) {
		this.bankBalance = bankBalance;
	}

	public BigDecimal getBankFrost() {
		return bankFrost;
	}

	public void setBankFrost(BigDecimal bankFrost) {
		this.bankFrost = bankFrost;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
