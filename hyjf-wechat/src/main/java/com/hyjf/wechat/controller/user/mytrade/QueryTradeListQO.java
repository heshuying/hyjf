package com.hyjf.wechat.controller.user.mytrade;

import java.io.Serializable;

/**
 * cuigq 和hyjf-app一样
 * 查询交易明細請求對象
 */
public class QueryTradeListQO implements Serializable {

	private static final long serialVersionUID = 3458197418404401541L;
	/** 用户id */
	private String userId;
	/** 交易类型 */
	private String tradeType;
	/** 交易年 */
	private String year;
	/** 交易月 */
	private String month;
	/**
	 * 翻页机能用的隐藏变量
	 */
	private int currentPage = 1;

	/**
	 * 翻页功能所用分页大小
	 */
	private int pageSize = 10;

	public QueryTradeListQO() {
		super();
	}

	public int getCurrentPage() {
		if (currentPage == 0) {
			currentPage = 1;
		}
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
}
