package com.hyjf.app.user.trade;

import java.io.Serializable;

import com.hyjf.mybatis.model.customize.app.AppTradeListCustomize;

public class TradeDetailListBean implements Serializable {

	/**
	 * 
	 */
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
	private int page = 1;

	/**
	 * 翻页功能所用分页大小
	 */
	private int pageSize = 10;

	public TradeDetailListBean() {
		super();
	}

	public int getPage() {
		if (page == 0) {
			page = 1;
		}
		return page;
	}

	public void setPage(int page) {
		this.page = page;
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
