package com.hyjf.mybatis.model.customize.web;

import java.io.Serializable;

import com.hyjf.mybatis.model.auto.StockInfo;

public class StockInfoCustomize extends StockInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7072466137528603230L;
	/**
	 * 1、时分图；2、5日图；3、月图；4、年图
	 */
	private String queryType;
	/**
	 * 时间起始
	 */
	private Integer timeBegin;
	/**
	 * 时间结束
	 */
	private Integer timeEnd;
	/**
	 * 时间间隔
	 */
	private Integer timeBetween;
	
	
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	public Integer getTimeBegin() {
		return timeBegin;
	}
	public void setTimeBegin(Integer timeBegin) {
		this.timeBegin = timeBegin;
	}
	public Integer getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(Integer timeEnd) {
		this.timeEnd = timeEnd;
	}
	public Integer getTimeBetween() {
		return timeBetween;
	}
	public void setTimeBetween(Integer timeBetween) {
		this.timeBetween = timeBetween;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
