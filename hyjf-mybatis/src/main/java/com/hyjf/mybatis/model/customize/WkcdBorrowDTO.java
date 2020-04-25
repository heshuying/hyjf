package com.hyjf.mybatis.model.customize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WkcdBorrowDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String bid;
	
	private String wkcd_id;
	
	private Integer recoverLastTime;
	
	private List<WkcdBorrowCustomize> plans = new ArrayList<>();

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public List<WkcdBorrowCustomize> getPlans() {
		return plans;
	}

	public void setPlans(List<WkcdBorrowCustomize> plans) {
		this.plans = plans;
	}

	public String getWkcd_id() {
		return wkcd_id;
	}

	public void setWkcd_id(String wkcd_id) {
		this.wkcd_id = wkcd_id;
	}

	public Integer getRecoverLastTime() {
		return recoverLastTime;
	}

	public void setRecoverLastTime(Integer recoverLastTime) {
		this.recoverLastTime = recoverLastTime;
	}

	

}
