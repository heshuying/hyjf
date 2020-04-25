/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2015年11月20日 下午5:24:10
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.model.customize.app;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Administrator
 */

public class AppChannelStatisticsDetailCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;
	/** 主键id */
	private Long id;
	/** 渠道id */
	private Integer sourceId;
	/** 渠道名称 */
	private String sourceName;
	/** 用户id */
	private Integer userId;
	/** 用户名 */
	private String userName;
	/** 注册时间 */
	private Date registerTime;
	/** 开户时间 */
	private Date openAccountTime;
	/** 首次出借时间 */
	private Integer firstInvestTime;
	/** 累计出借金额 */
	private BigDecimal cumulativeInvest;
	/** 首次出借金额 */
	private BigDecimal investAmount;
	/** 首投项目类型 */
	private String investProjectType;
	/** 首次出借标的的项目期限 */
	private String investProjectPeriod;
	/** 性别 导出用 **/
	private String sex;
	/** 渠道账号 检索 **/
	private String utmIds;
	/** 渠道 */
	private String[] utmIdsSrch;

	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;

	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSourceId() {
		return sourceId;
	}

	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public Date getOpenAccountTime() {
		return openAccountTime;
	}

	public void setOpenAccountTime(Date openAccountTime) {
		this.openAccountTime = openAccountTime;
	}

	public Integer getFirstInvestTime() {
		return firstInvestTime;
	}

	public void setFirstInvestTime(Integer firstInvestTime) {
		this.firstInvestTime = firstInvestTime;
	}

	public BigDecimal getCumulativeInvest() {
		return cumulativeInvest;
	}

	public void setCumulativeInvest(BigDecimal cumulativeInvest) {
		this.cumulativeInvest = cumulativeInvest;
	}

	/**
	 * limitStart
	 * 
	 * @return the limitStart
	 */

	public int getLimitStart() {
		return limitStart;
	}

	/**
	 * @param limitStart
	 *            the limitStart to set
	 */

	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	/**
	 * limitEnd
	 * 
	 * @return the limitEnd
	 */

	public int getLimitEnd() {
		return limitEnd;
	}

	/**
	 * @param limitEnd
	 *            the limitEnd to set
	 */

	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public BigDecimal getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}

	public String getInvestProjectType() {
		return investProjectType;
	}

	public void setInvestProjectType(String investProjectType) {
		this.investProjectType = investProjectType;
	}

	public String getInvestProjectPeriod() {
		return investProjectPeriod;
	}

	public void setInvestProjectPeriod(String investProjectPeriod) {
		this.investProjectPeriod = investProjectPeriod;
	}

	public String getUtmIds() {
		return utmIds;
	}

	public void setUtmIds(String utmIds) {
		this.utmIds = utmIds;
	}

	public String[] getUtmIdsSrch() {
		return utmIdsSrch;
	}

	public void setUtmIdsSrch(String[] utmIdsSrch) {
		this.utmIdsSrch = utmIdsSrch;
	}

}
