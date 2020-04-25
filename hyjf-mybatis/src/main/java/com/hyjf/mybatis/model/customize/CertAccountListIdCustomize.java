package com.hyjf.mybatis.model.customize;


/**
 * 
 * @author pcc
 */
public class CertAccountListIdCustomize {
	/**
	 * 
	 */
	/**
	 * 
	 */
	private Integer limitMinId;
	private Integer sumCount;
	private Integer limitMaxId;
	private Integer maxId;
	public Integer getLimitMinId() {
		return limitMinId;
	}
	public void setLimitMinId(Integer limitMinId) {
		this.limitMinId = limitMinId;
	}
	public Integer getLimitMaxId() {
		return limitMaxId;
	}
	public void setLimitMaxId(Integer limitMaxId) {
		this.limitMaxId = limitMaxId;
	}
	public Integer getMaxId() {
		return maxId;
	}
	public void setMaxId(Integer maxId) {
		this.maxId = maxId;
	}
	public Integer getSumCount() {
		return sumCount;
	}
	public void setSumCount(Integer sumCount) {
		this.sumCount = sumCount;
	}
	
}
