package com.hyjf.mybatis.model.customize;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 
 * @author ZXS
 */
public class AppointmentRecodLogCustomize  implements Serializable {


	private int limitStart=0;

	private int limitEnd=999999999;
	/**
     *  主键
     */
	private int id;
	/**
     * 用户id
     */
	private int userId;	
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 汇付客户号
	 */
	private String userCustId;
	/**
	 * 累计积分
	 */
	private String recodTotal;
	/**
	 * 积分清空时间
	 */
	private String recodTruncateTime;
	/**
	 * 违约分值
	 */
	private int recod;
	/**
	 * 违约标号
	 */
	private String recodNid;
	/**
	 * 违约金额
	 */
	private BigDecimal recodMoney;
	/**
	 * 违约类型
	 */
	private int recodType;
	/**
	 * 订单号
	 */
	private String apointOrderId;
	/**
	 * 备注
	 */
	private String recodRemark;
	/**
	 * 操作时间
	 */
	private String addTime;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserCustId() {
		return userCustId;
	}
	public void setUserCustId(String userCustId) {
		this.userCustId = userCustId;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getRecodTotal() {
		return recodTotal;
	}
	public void setRecodTotal(String recodTotal) {
		this.recodTotal = recodTotal;
	}
	public String getRecodTruncateTime() {
		return recodTruncateTime;
	}
	public void setRecodTruncateTime(String recodTruncateTime) {
		this.recodTruncateTime = recodTruncateTime;
	}
	public int getRecod() {
		return recod;
	}
	public void setRecod(int recod) {
		this.recod = recod;
	}
	public String getRecodNid() {
		return recodNid;
	}
	public void setRecodNid(String recodNid) {
		this.recodNid = recodNid;
	}
	public BigDecimal getRecodMoney() {
		return recodMoney;
	}
	public void setRecodMoney(BigDecimal recodMoney) {
		this.recodMoney = recodMoney;
	}
	public int getRecodType() {
		return recodType;
	}
	public void setRecodType(int recodType) {
		this.recodType = recodType;
	}
	public String getApointOrderId() {
		return apointOrderId;
	}
	public void setApointOrderId(String apointOrderId) {
		this.apointOrderId = apointOrderId;
	}
	public String getRecodRemark() {
		return recodRemark;
	}
	public void setRecodRemark(String recodRemark) {
		this.recodRemark = recodRemark;
	}
	
}




	