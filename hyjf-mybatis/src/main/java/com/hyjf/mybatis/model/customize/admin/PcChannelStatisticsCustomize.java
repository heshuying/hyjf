package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;

/**
 * PC渠道统计Customize
 * 
 * @author liuyang
 *
 */
public class PcChannelStatisticsCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * 渠道ID
	 */
	private Integer sourceId;

	/**
	 * 渠道名称
	 */
	private String sourceName;

	/**
	 * 访问数
	 */
	private String accessNumber;
	/**
	 * 注册数
	 */
	private String registNumber;

	/**
	 * 开户数
	 */
	private String openAccountNumber;

	/**
	 * 出借数
	 */
	private String tenderNumber;

	/**
	 * 累计充值
	 */
	private String cumulativeRecharge;

	/**
	 * 累计出借
	 */
	private String cumulativeInvestment;

	/**
	 * 汇直投出借金额
	 */
	private String hztTenderPrice;
	/**
	 * 汇消费出借金额
	 */
	private String hxfTenderPrice;

	/**
	 * 汇天利出借金额
	 */
	private String htlTenderPrice;

	/**
	 * 汇添金出借金额
	 */
	private String htjTenderPrice;

	/**
	 * 汇金理财出借金额
	 */
	private String rtbTenderPrice;

	/**
	 * 汇转让出借金额
	 */
	private String hzrTenderPrice;

	/**
	 * 添加时间
	 */
	private String addTime;

	/**
	 * 渠道
	 */
	private String utmIds;

	/**
	 * 渠道
	 */
	private String[] utmIdsSrch;

	/**
	 * 检索条件 时间开始
	 */
	private String timeStartSrch;

	/**
	 * 检索条件 时间结束
	 */
	private String timeEndSrch;

	/**
	 * 检索条件 渠道Id
	 */
	private String sourceIdSrch;

	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;

	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

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

	public String getAccessNumber() {
		return accessNumber;
	}

	public void setAccessNumber(String accessNumber) {
		this.accessNumber = accessNumber;
	}

	public String getRegistNumber() {
		return registNumber;
	}

	public void setRegistNumber(String registNumber) {
		this.registNumber = registNumber;
	}

	public String getOpenAccountNumber() {
		return openAccountNumber;
	}

	public void setOpenAccountNumber(String openAccountNumber) {
		this.openAccountNumber = openAccountNumber;
	}

	public String getTenderNumber() {
		return tenderNumber;
	}

	public void setTenderNumber(String tenderNumber) {
		this.tenderNumber = tenderNumber;
	}

	public String getCumulativeRecharge() {
		return cumulativeRecharge;
	}

	public void setCumulativeRecharge(String cumulativeRecharge) {
		this.cumulativeRecharge = cumulativeRecharge;
	}

	public String getCumulativeInvestment() {
		return cumulativeInvestment;
	}

	public void setCumulativeInvestment(String cumulativeInvestment) {
		this.cumulativeInvestment = cumulativeInvestment;
	}

	public String getHztTenderPrice() {
		return hztTenderPrice;
	}

	public void setHztTenderPrice(String hztTenderPrice) {
		this.hztTenderPrice = hztTenderPrice;
	}

	public String getHxfTenderPrice() {
		return hxfTenderPrice;
	}

	public void setHxfTenderPrice(String hxfTenderPrice) {
		this.hxfTenderPrice = hxfTenderPrice;
	}

	public String getHtlTenderPrice() {
		return htlTenderPrice;
	}

	public void setHtlTenderPrice(String htlTenderPrice) {
		this.htlTenderPrice = htlTenderPrice;
	}

	public String getHtjTenderPrice() {
		return htjTenderPrice;
	}

	public void setHtjTenderPrice(String htjTenderPrice) {
		this.htjTenderPrice = htjTenderPrice;
	}

	public String getRtbTenderPrice() {
		return rtbTenderPrice;
	}

	public void setRtbTenderPrice(String rtbTenderPrice) {
		this.rtbTenderPrice = rtbTenderPrice;
	}

	public String getHzrTenderPrice() {
		return hzrTenderPrice;
	}

	public void setHzrTenderPrice(String hzrTenderPrice) {
		this.hzrTenderPrice = hzrTenderPrice;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String[] getUtmIdsSrch() {
		return utmIdsSrch;
	}

	public void setUtmIdsSrch(String[] utmIdsSrch) {
		this.utmIdsSrch = utmIdsSrch;
	}

	public String getTimeStartSrch() {
		return timeStartSrch;
	}

	public void setTimeStartSrch(String timeStartSrch) {
		this.timeStartSrch = timeStartSrch;
	}

	public String getTimeEndSrch() {
		return timeEndSrch;
	}

	public void setTimeEndSrch(String timeEndSrch) {
		this.timeEndSrch = timeEndSrch;
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

	public String getUtmIds() {
		return utmIds;
	}

	public void setUtmIds(String utmIds) {
		this.utmIds = utmIds;
	}

	public String getSourceIdSrch() {
		return sourceIdSrch;
	}

	public void setSourceIdSrch(String sourceIdSrch) {
		this.sourceIdSrch = sourceIdSrch;
	}

}
