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

/**
 * @author Administrator
 */

public class AppChannelStatisticsCustomize implements Serializable {

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
	 * 访问数
	 */
	private String visitCount;

	/**
	 * 注册数
	 */
	private String registerCount;
	/**
	 * 无主单注册数
	 */
	private String registerAttrCount;

	/**
	 * 开户数
	 */
	private String openAccountCount;

	/**
	 * 出借人数
	 */
	private String investNumber;

	/**
	 * 累计充值
	 */
	private String cumulativeCharge;

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
	 * 开户数（ios）
	 */
	private String accountNumberIos;
	/**
	 * 开户数（pc）
	 */
	private String accountNumberPc;
	/**
	 * 开户数（wechat）
	 */
	private String accountNumberWechat;

	/**
	 * 开户数（android）
	 */
	private String accountNumberAndroid;

	/**
	 * 出借人数（android）
	 */
	private String tenderNumberAndroid;
	/**
	 * 出借人数（pc）
	 */
	private String tenderNumberPc;

	/**
	 * 出借ren数（wechat）
	 */
	private String tenderNumberWechat;

	/**
	 * 出借人数（ios）
	 */
	private String tenderNumberIos;

	/**
	 * 累计充值（无主单）
	 */
	private String cumulativeAttrCharge;

	/**
	 * 开户数（无主单）
	 */
	private String openAccountAttrCount;
	/**
	 * 出借人数（无主单）
	 */
	private String investAttrNumber;
	/**
	 * 累计出借（无主单）
	 */
	private String cumulativeAttrInvest;
	/**
	 * 检索条件 渠道类别
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
	 * 检索条件 sourceId
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

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(String visitCount) {
		this.visitCount = visitCount;
	}

	public String getRegisterCount() {
		return registerCount;
	}

	public void setRegisterCount(String registerCount) {
		this.registerCount = registerCount;
	}

	public String getOpenAccountCount() {
		return openAccountCount;
	}

	public void setOpenAccountCount(String openAccountCount) {
		this.openAccountCount = openAccountCount;
	}

	public String getInvestNumber() {
		return investNumber;
	}

	public void setInvestNumber(String investNumber) {
		this.investNumber = investNumber;
	}

	public String getCumulativeCharge() {
		return cumulativeCharge;
	}

	public void setCumulativeCharge(String cumulativeCharge) {
		this.cumulativeCharge = cumulativeCharge;
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

	public String getRegisterAttrCount() {
		return registerAttrCount;
	}

	public void setRegisterAttrCount(String registerAttrCount) {
		this.registerAttrCount = registerAttrCount;
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

	public String getAccountNumberIos() {
		return accountNumberIos;
	}

	public void setAccountNumberIos(String accountNumberIos) {
		this.accountNumberIos = accountNumberIos;
	}

	public String getAccountNumberPc() {
		return accountNumberPc;
	}

	public void setAccountNumberPc(String accountNumberPc) {
		this.accountNumberPc = accountNumberPc;
	}

	public String getAccountNumberWechat() {
		return accountNumberWechat;
	}

	public void setAccountNumberWechat(String accountNumberWechat) {
		this.accountNumberWechat = accountNumberWechat;
	}

	public String getAccountNumberAndroid() {
		return accountNumberAndroid;
	}

	public void setAccountNumberAndroid(String accountNumberAndroid) {
		this.accountNumberAndroid = accountNumberAndroid;
	}

	public String getTenderNumberAndroid() {
		return tenderNumberAndroid;
	}

	public void setTenderNumberAndroid(String tenderNumberAndroid) {
		this.tenderNumberAndroid = tenderNumberAndroid;
	}

	public String getTenderNumberPc() {
		return tenderNumberPc;
	}

	public void setTenderNumberPc(String tenderNumberPc) {
		this.tenderNumberPc = tenderNumberPc;
	}

	public String getTenderNumberWechat() {
		return tenderNumberWechat;
	}

	public void setTenderNumberWechat(String tenderNumberWechat) {
		this.tenderNumberWechat = tenderNumberWechat;
	}

	public String getTenderNumberIos() {
		return tenderNumberIos;
	}

	public void setTenderNumberIos(String tenderNumberIos) {
		this.tenderNumberIos = tenderNumberIos;
	}

	public String getCumulativeAttrCharge() {
		return cumulativeAttrCharge;
	}

	public void setCumulativeAttrCharge(String cumulativeAttrCharge) {
		this.cumulativeAttrCharge = cumulativeAttrCharge;
	}

	public String getOpenAccountAttrCount() {
		return openAccountAttrCount;
	}

	public void setOpenAccountAttrCount(String openAccountAttrCount) {
		this.openAccountAttrCount = openAccountAttrCount;
	}

	public String getInvestAttrNumber() {
		return investAttrNumber;
	}

	public void setInvestAttrNumber(String investAttrNumber) {
		this.investAttrNumber = investAttrNumber;
	}

	public String getCumulativeAttrInvest() {
		return cumulativeAttrInvest;
	}

	public void setCumulativeAttrInvest(String cumulativeAttrInvest) {
		this.cumulativeAttrInvest = cumulativeAttrInvest;
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

	public String getUtmIds() {
		return utmIds;
	}

	public void setUtmIds(String utmIds) {
		this.utmIds = utmIds;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String[] getUtmIdsSrch() {
		return utmIdsSrch;
	}

	public void setUtmIdsSrch(String[] utmIdsSrch) {
		this.utmIdsSrch = utmIdsSrch;
	}

	public String getSourceIdSrch() {
		return sourceIdSrch;
	}

	public void setSourceIdSrch(String sourceIdSrch) {
		this.sourceIdSrch = sourceIdSrch;
	}

}
