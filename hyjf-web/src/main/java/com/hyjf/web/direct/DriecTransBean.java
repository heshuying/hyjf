package com.hyjf.web.direct;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HtlCommonBean
 * 汇天利通用bean
 * @author  michael
 *
 */
public class DriecTransBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3803722754627032581L;
	/**
	 * 转出用户id
	 */
	private Integer outUserId;
	/**
	 * 转出用户名
	 */
	private String outUserName;	
	
	/**
	 * 转出用户姓名
	 */
	private String outTureName;	
	/**
	 * 转出用户手机号
	 */
	private String outMobile;
	
	/**
	 * 转出用户汇付客户号
	 */
	private Long outChinapnrUsrcustid;
	
	/**
	 * 转出用户可用金额
	 */
	private BigDecimal outUserBalance;
	
	
	/**
	 * 转入用户id
	 */
	private Integer inUserId;

	/**
	 * 转入用户名
	 */
	private String inUserName;	
	
	/**
	 * 转入用户姓名
	 */
	private String inTureName;	
	/**
	 * 转入用户手机号
	 */
	private String inMobile;
	/**
	 * 转入用户汇付客户号
	 */
	private Long inChinapnrUsrcustid;

	/**
	 *  转入用户可用金额
	 */
	private BigDecimal inUserBalance;
	
	/**
	 * 交易金额
	 */
	private BigDecimal transAmt;

	
	
	public Integer getOutUserId() {
		return outUserId;
	}

	public void setOutUserId(Integer outUserId) {
		this.outUserId = outUserId;
	}

	public String getOutUserName() {
		return outUserName;
	}

	public void setOutUserName(String outUserName) {
		this.outUserName = outUserName;
	}

	public String getOutTureName() {
		return outTureName;
	}

	public void setOutTureName(String outTureName) {
		this.outTureName = outTureName;
	}

	public String getOutMobile() {
		return outMobile;
	}

	public void setOutMobile(String outMobile) {
		this.outMobile = outMobile;
	}

	public Integer getInUserId() {
		return inUserId;
	}

	public void setInUserId(Integer inUserId) {
		this.inUserId = inUserId;
	}

	public String getInUserName() {
		return inUserName;
	}

	public void setInUserName(String inUserName) {
		this.inUserName = inUserName;
	}

	public String getInTureName() {
		return inTureName;
	}

	public void setInTureName(String inTureName) {
		this.inTureName = inTureName;
	}

	public String getInMobile() {
		return inMobile;
	}

	public void setInMobile(String inMobile) {
		this.inMobile = inMobile;
	}

	public BigDecimal getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(BigDecimal transAmt) {
		this.transAmt = transAmt;
	}

	public Long getOutChinapnrUsrcustid() {
		return outChinapnrUsrcustid;
	}

	public void setOutChinapnrUsrcustid(Long outChinapnrUsrcustid) {
		this.outChinapnrUsrcustid = outChinapnrUsrcustid;
	}

	public Long getInChinapnrUsrcustid() {
		return inChinapnrUsrcustid;
	}

	public void setInChinapnrUsrcustid(Long inChinapnrUsrcustid) {
		this.inChinapnrUsrcustid = inChinapnrUsrcustid;
	}

	public BigDecimal getOutUserBalance() {
		return outUserBalance;
	}

	public void setOutUserBalance(BigDecimal outUserBalance) {
		this.outUserBalance = outUserBalance;
	}

	public BigDecimal getInUserBalance() {
		return inUserBalance;
	}

	public void setInUserBalance(BigDecimal inUserBalance) {
		this.inUserBalance = inUserBalance;
	}
	
}
