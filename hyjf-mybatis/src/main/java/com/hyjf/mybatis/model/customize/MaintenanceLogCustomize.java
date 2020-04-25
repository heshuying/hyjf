package com.hyjf.mybatis.model.customize;

import java.io.Serializable;

public class MaintenanceLogCustomize implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//oa_logs表
	private int id; //添加者ID
	private String type; //操作类型,用于区分操作的模块.
	private String operatorname; //操作者的姓名
	private String beoperatorname; //被操作者姓名
	private String method; //操作方法
	private String status; //是否成功
	private String ipaddress; //IP地址
	private String times; //当前时间
	
	//分页限制
	private int limitStart = -1;
	private int limitEnd = -1;
	
	//查询所用变量
	private String operatornameSrch;
	private String beoperatornameSrch;
	private String timesStartSrch;
	private String timesEndSrch;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOperatorname() {
		return operatorname;
	}
	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname;
	}
	public String getBeoperatorname() {
		return beoperatorname;
	}
	public void setBeoperatorname(String beoperatorname) {
		this.beoperatorname = beoperatorname;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getOperatornameSrch() {
		return operatornameSrch;
	}
	public void setOperatornameSrch(String operatornameSrch) {
		this.operatornameSrch = operatornameSrch;
	}
	public String getBeoperatornameSrch() {
		return beoperatornameSrch;
	}
	public void setBeoperatornameSrch(String beoperatornameSrch) {
		this.beoperatornameSrch = beoperatornameSrch;
	}
	public String getTimesStartSrch() {
		return timesStartSrch;
	}
	public void setTimesStartSrch(String timesStartSrch) {
		this.timesStartSrch = timesStartSrch;
	}
	public String getTimesEndSrch() {
		return timesEndSrch;
	}
	public void setTimesEndSrch(String timesEndSrch) {
		this.timesEndSrch = timesEndSrch;
	}
	
	
	
	

}
