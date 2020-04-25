package com.hyjf.web.htl.invest;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hyjf.common.paginator.Paginator;

/**
 * HtlCommonBean
 * 汇天利通用bean
 * @author  michael
 *
 */
public class HtlCommonBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3803722754627032581L;
	/**
	 * 汇天利利率
	 */
	private String htlRate;

	/**
	 * 汇天利单户上限
	 */
	private String userPupper;	
	
	/**
	 * 汇天利累计交易金额
	 */
	private String htlTotalSum;	
	/**
	 * 用户可用余额
	 */
	private String userBalance;
	
	/**
	 * 用户可申购金额
	 */
	private String userAvaPurchase;
	
	/**
	 * 汇天利可申购金额
	 */
	private String avaPurchase;
	
	/**
	 * 用户汇天利本金
	 */
	private String userPrincipal;
	/**
	 * 计息日期
	 */
	private String interestDate;
	/**
	 * 已提取收益
	 */
	private String extractInterest;
	
	/**
	 * 未提取收益
	 */
	private String notExtractInterest;	
	
	/**
	 * 用户可用余额(数字，用户前端校验)
	 */
	private BigDecimal userBalanceNumber;
	
	/**
	 * 用户可申购金额(数字，用户前端校验)
	 */
	private BigDecimal userAvaPurchaseNumber;
	
	/**
	 * 汇天利可申购金额(数字，用户前端校验)
	 */
	private BigDecimal avaPurchaseNumber;
	
	/**
	 * 用户汇天利本金(数字，用户前端校验)
	 */
	private BigDecimal userPrincipalNumber;
	
	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;
	private int pageSize = 10;

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

	public int getPageSize() {
		if (pageSize == 0) {
			pageSize = 10;
		}
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
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

	public String getHtlRate() {
		return htlRate;
	}

	public void setHtlRate(String htlRate) {
		this.htlRate = htlRate;
	}

	public String getAvaPurchase() {
		return avaPurchase;
	}

	public void setAvaPurchase(String avaPurchase) {
		this.avaPurchase = avaPurchase;
	}

	public String getUserPupper() {
		return userPupper;
	}

	public void setUserPupper(String userPupper) {
		this.userPupper = userPupper;
	}

	public String getHtlTotalSum() {
		return htlTotalSum;
	}

	public void setHtlTotalSum(String htlTotalSum) {
		this.htlTotalSum = htlTotalSum;
	}

	public String getUserBalance() {
		return userBalance;
	}

	public void setUserBalance(String userBalance) {
		this.userBalance = userBalance;
	}

	public String getUserAvaPurchase() {
		return userAvaPurchase;
	}

	public void setUserAvaPurchase(String userAvaPurchase) {
		this.userAvaPurchase = userAvaPurchase;
	}

	public String getUserPrincipal() {
		return userPrincipal;
	}

	public void setUserPrincipal(String userPrincipal) {
		this.userPrincipal = userPrincipal;
	}

	public String getInterestDate() {
		return interestDate;
	}

	public void setInterestDate(String interestDate) {
		this.interestDate = interestDate;
	}

	public BigDecimal getUserBalanceNumber() {
		return userBalanceNumber;
	}

	public void setUserBalanceNumber(BigDecimal userBalanceNumber) {
		this.userBalanceNumber = userBalanceNumber;
	}

	public BigDecimal getUserAvaPurchaseNumber() {
		return userAvaPurchaseNumber;
	}

	public void setUserAvaPurchaseNumber(BigDecimal userAvaPurchaseNumber) {
		this.userAvaPurchaseNumber = userAvaPurchaseNumber;
	}

	public BigDecimal getAvaPurchaseNumber() {
		return avaPurchaseNumber;
	}

	public void setAvaPurchaseNumber(BigDecimal avaPurchaseNumber) {
		this.avaPurchaseNumber = avaPurchaseNumber;
	}

	public String getExtractInterest() {
		return extractInterest;
	}

	public void setExtractInterest(String extractInterest) {
		this.extractInterest = extractInterest;
	}

	public String getNotExtractInterest() {
		return notExtractInterest;
	}

	public void setNotExtractInterest(String notExtractInterest) {
		this.notExtractInterest = notExtractInterest;
	}

	public BigDecimal getUserPrincipalNumber() {
		return userPrincipalNumber;
	}

	public void setUserPrincipalNumber(BigDecimal userPrincipalNumber) {
		this.userPrincipalNumber = userPrincipalNumber;
	}

	
}
