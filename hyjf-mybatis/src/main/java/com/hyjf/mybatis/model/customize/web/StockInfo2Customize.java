package com.hyjf.mybatis.model.customize.web;

import java.io.Serializable;
import java.math.BigDecimal;


public class StockInfo2Customize  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7072466137528603230L;
	
    private BigDecimal nowPrice;

    private String date;
    
    private Integer time;

	public BigDecimal getNowPrice() {
		return nowPrice;
	}

	public void setNowPrice(BigDecimal nowPrice) {
		this.nowPrice = nowPrice;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}


    
    
}
