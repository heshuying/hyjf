package com.hyjf.mybatis.model.customize.batch;

import java.io.Serializable;

/**
 * @author Michael
 */

public class BatchChannelStatisticsOldCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * 渠道ID
	 */
	private String sourceId;
	/**
	 * 渠道
	 */
	private String channelName;


	/**
	 * 累计出借
	 */
	private String cumulativeInvest;

	/**
	 * 汇直投出借金额
	 */
	private String hztInvestSum;

	/**
	 * 汇消费出借金额
	 */
	private String hxfInvestSum;

	/**
	 * 汇天利出借金额
	 */
	private String htlInvestSum;

	/**
	 * 汇添金出借金额
	 */
	private String htjInvestSum;

	/**
	 * 汇金理财出借金额
	 */
	private String rtbInvestSum;

	/**
	 * 汇转让出借金额
	 */
	private String hzrInvestSum;

	/**
	 * 检索条件 时间开始
	 */
	private String timeStartSrch;

	/**
	 * 检索条件 时间结束
	 */
	private String timeEndSrch;

	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;

	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}


	public String getCumulativeInvest() {
		return cumulativeInvest;
	}

	public void setCumulativeInvest(String cumulativeInvest) {
		this.cumulativeInvest = cumulativeInvest;
	}

	public String getHztInvestSum() {
		return hztInvestSum;
	}

	public void setHztInvestSum(String hztInvestSum) {
		this.hztInvestSum = hztInvestSum;
	}

	public String getHxfInvestSum() {
		return hxfInvestSum;
	}

	public void setHxfInvestSum(String hxfInvestSum) {
		this.hxfInvestSum = hxfInvestSum;
	}

	public String getHtlInvestSum() {
		return htlInvestSum;
	}

	public void setHtlInvestSum(String htlInvestSum) {
		this.htlInvestSum = htlInvestSum;
	}


	/**
	 * timeStartSrch
	 * 
	 * @return the timeStartSrch
	 */

	public String getTimeStartSrch() {
		return timeStartSrch;
	}

	/**
	 * @param timeStartSrch
	 *            the timeStartSrch to set
	 */

	public void setTimeStartSrch(String timeStartSrch) {
		this.timeStartSrch = timeStartSrch;
	}

	/**
	 * timeEndSrch
	 * 
	 * @return the timeEndSrch
	 */

	public String getTimeEndSrch() {
		return timeEndSrch;
	}

	/**
	 * @param timeEndSrch
	 *            the timeEndSrch to set
	 */

	public void setTimeEndSrch(String timeEndSrch) {
		this.timeEndSrch = timeEndSrch;
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


	public String getHtjInvestSum() {
		return htjInvestSum;
	}

	public void setHtjInvestSum(String htjInvestSum) {
		this.htjInvestSum = htjInvestSum;
	}

	public String getRtbInvestSum() {
		return rtbInvestSum;
	}

	public void setRtbInvestSum(String rtbInvestSum) {
		this.rtbInvestSum = rtbInvestSum;
	}

	public String getHzrInvestSum() {
		return hzrInvestSum;
	}

	public void setHzrInvestSum(String hzrInvestSum) {
		this.hzrInvestSum = hzrInvestSum;
	}


	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

}
