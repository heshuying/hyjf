package com.hyjf.mybatis.model.customize;

import java.math.BigDecimal;
/**
 * 充值管理
 * @author HBZ
 */
public class WebListCustomize {
    private Integer id;
    private int limitStart = -1;
    private int limitEnd = -1;
	/**
     * 订单号
     */
	private String ordid;
	/**
     * 相关借款号
     */
	private String borrowNid;
	/**
     * 操作金额
     */
	private BigDecimal amount;
	/**
     * 类型1收入2支出
     */
	private Integer type;
	/**
     *
     */
	private String trade;
	/**
     * 交易类型
     */
	private String tradeType;
	/**
     * 对方用户ID
     */
	private Integer userId;
	/**
     * 客户号
     */
	private String usrcustid;
	/**
     *
     */
	private String truename;
	/**
     *
     */
	private String regionName;
	/**
     *
     */
	private String branchName;
	/**
     *
     */
	private String departmentName;
	/**
     * 说明
     */
	private String remark;
	/**
     *
     */
	private String note;
	/**
     * 发生时间
     */
	private String createTime;
	/**
     * 操作员
     */
	private String operator;
	/**
     *
     */
	private Integer flag;
	/**
     * 用户名
     */
	private String username;

	/**
     * 类型名词 （汉化type字段）
     */
	private String typeName;
	public String getOrdid() {
		return ordid;
	}
	public void setOrdid(String ordid) {
		this.ordid = ordid;
	}
	public String getBorrowNid() {
		return borrowNid;
	}
	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getTrade() {
		return trade;
	}
	public void setTrade(String trade) {
		this.trade = trade;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUsrcustid() {
		return usrcustid;
	}
	public void setUsrcustid(String usrcustid) {
		this.usrcustid = usrcustid;
	}
	public String getTruename() {
		return truename;
	}
	public void setTruename(String truename) {
		this.truename = truename;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
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
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

}




