/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2018
 * Company: HYJF Corporation
 * @author: PC-LIUSHOUYI
 * @version: 1.0
 * Created at: 2018年1月31日 上午11:24:34
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.mybatis.model.customize;

import java.io.Serializable;

/**
 * @author PC-LIUSHOUYI
 */

public class ActdecListedTwoCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */
		
	private static final long serialVersionUID = 1L;
	
	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;

	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;
	
	/**
	 * 用户名
	 * 
	 */
	private String userName;
	
	/**
	 * 姓名
	 * 
	 */
	private String trueName;
	
	/**
	 * 用户id
	 * 
	 */
	private String userId;

	/**
	 * 手机号
	 * 
	 */
	private String mobile;

	/**
	 * 操作(0领奖、1充值、2出借、3提现)
	 * 
	 */
	private String trade;
	
	/**
	 * 领奖时增投金额
	 * 
	 */
	private int investedAmount;

	/**
	 * 领奖金额
	 * 
	 */
	private String acceptPrize;

	/**
	 * 领取时间
	 * 
	 */
	private int acceptTime;

	/**
	 * 操作金额
	 * 
	 */
	private int amount;
	
	/**
	 * 检索条件 用户名
	 * 
	 */
	private String userNameSrch;

	/**
	 * 当前新增充值金额
	 * 
	 */
	private int cumulativeCharge;
	
	/**
	 * 当前新增冲投金额
	 * 
	 */
	private int cumulativeInvest;
	
	/**
	 * 创建人
	 * 
	 */
	private String createUser;
	
	/**
	 * 创建时间
	 * 
	 */
	private int createTime; 
	
	/**
	 * 修改人
	 * 
	 */
	private String updateUser;
	
	/**
	 * 修改时间
	 * 
	 */
	private int updateTime;
	
	/**
	 * 是否删除
	 * 
	 */
	private int delFlg;
	
	/**
	 * 检索条件 姓名
	 * 
	 */
	private String trueNameSrch;

	/**
	 * 检索条件 手机号
	 * 
	 */
	private String mobileSrch;

	/**
	 * 检索条件 领取奖励
	 * 
	 */
	private String acceptPrizeSrch;

	/**
	 * 检索条件 领取时间
	 * 
	 */
	private String acceptTimeStartSrch; 

	/**
	 * 检索条件 领取时间
	 * 
	 */
	private String acceptTimeEndSrch;

	/**
	 * 检索条件 操作(0领奖、1充值、2出借、3提现)
	 * 
	 */
	private String tradeSrch;
	
	/**
	 * limitStart
	 * @return the limitStart
	 */
	
	public int getLimitStart() {
		return limitStart;
	}

	/**
	 * @param limitStart the limitStart to set
	 */
	
	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	/**
	 * limitEnd
	 * @return the limitEnd
	 */
	
	public int getLimitEnd() {
		return limitEnd;
	}

	/**
	 * @param limitEnd the limitEnd to set
	 */
	
	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}

	/**
	 * userName
	 * @return the userName
	 */
	
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * trueName
	 * @return the trueName
	 */
	
	public String getTrueName() {
		return trueName;
	}

	/**
	 * @param trueName the trueName to set
	 */
	
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	/**
	 * mobile
	 * @return the mobile
	 */
	
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile the mobile to set
	 */
	
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * trade
	 * @return the trade
	 */
	
	public String getTrade() {
		return trade;
	}

	/**
	 * @param trade the trade to set
	 */
	
	public void setTrade(String trade) {
		this.trade = trade;
	}

	/**
	 * investedAmount
	 * @return the investedAmount
	 */
	
	public int getInvestedAmount() {
		return investedAmount;
	}

	/**
	 * @param investedAmount the investedAmount to set
	 */
	
	public void setInvestedAmount(int investedAmount) {
		this.investedAmount = investedAmount;
	}

	/**
	 * acceptPrize
	 * @return the acceptPrize
	 */
	
	public String getAcceptPrize() {
		return acceptPrize;
	}

	/**
	 * @param acceptPrize the acceptPrize to set
	 */
	
	public void setAcceptPrize(String acceptPrize) {
		this.acceptPrize = acceptPrize;
	}

	/**
	 * acceptTime
	 * @return the acceptTime
	 */
	
	public int getAcceptTime() {
		return acceptTime;
	}

	/**
	 * @param acceptTime the acceptTime to set
	 */
	
	public void setAcceptTime(int acceptTime) {
		this.acceptTime = acceptTime;
	}

	/**
	 * amount
	 * @return the amount
	 */
	
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * userNameSrch
	 * @return the userNameSrch
	 */
	
	public String getUserNameSrch() {
		return userNameSrch;
	}

	/**
	 * @param userNameSrch the userNameSrch to set
	 */
	
	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
	}

	/**
	 * cumulativeCharge
	 * @return the cumulativeCharge
	 */
	
	public int getCumulativeCharge() {
		return cumulativeCharge;
	}

	/**
	 * @param cumulativeCharge the cumulativeCharge to set
	 */
	
	public void setCumulativeCharge(int cumulativeCharge) {
		this.cumulativeCharge = cumulativeCharge;
	}

	/**
	 * cumulativeInvest
	 * @return the cumulativeInvest
	 */
	
	public int getCumulativeInvest() {
		return cumulativeInvest;
	}

	/**
	 * @param cumulativeInvest the cumulativeInvest to set
	 */
	
	public void setCumulativeInvest(int cumulativeInvest) {
		this.cumulativeInvest = cumulativeInvest;
	}

	/**
	 * trueNameSrch
	 * @return the trueNameSrch
	 */
	
	public String getTrueNameSrch() {
		return trueNameSrch;
	}

	/**
	 * @param trueNameSrch the trueNameSrch to set
	 */
	
	public void setTrueNameSrch(String trueNameSrch) {
		this.trueNameSrch = trueNameSrch;
	}

	/**
	 * mobileSrch
	 * @return the mobileSrch
	 */
	
	public String getMobileSrch() {
		return mobileSrch;
	}

	/**
	 * @param mobileSrch the mobileSrch to set
	 */
	
	public void setMobileSrch(String mobileSrch) {
		this.mobileSrch = mobileSrch;
	}

	/**
	 * acceptPrizeSrch
	 * @return the acceptPrizeSrch
	 */
	
	public String getAcceptPrizeSrch() {
		return acceptPrizeSrch;
	}

	/**
	 * @param acceptPrizeSrch the acceptPrizeSrch to set
	 */
	
	public void setAcceptPrizeSrch(String acceptPrizeSrch) {
		this.acceptPrizeSrch = acceptPrizeSrch;
	}

	/**
	 * acceptTimeStartSrch
	 * @return the acceptTimeStartSrch
	 */
	
	public String getAcceptTimeStartSrch() {
		return acceptTimeStartSrch;
	}

	/**
	 * @param acceptTimeStartSrch the acceptTimeStartSrch to set
	 */
	
	public void setAcceptTimeStartSrch(String acceptTimeStartSrch) {
		this.acceptTimeStartSrch = acceptTimeStartSrch;
	}

	/**
	 * acceptTimeEndSrch
	 * @return the acceptTimeEndSrch
	 */
	
	public String getAcceptTimeEndSrch() {
		return acceptTimeEndSrch;
	}

	/**
	 * @param acceptTimeEndSrch the acceptTimeEndSrch to set
	 */
	
	public void setAcceptTimeEndSrch(String acceptTimeEndSrch) {
		this.acceptTimeEndSrch = acceptTimeEndSrch;
	}

	/**
	 * createUser
	 * @return the createUser
	 */
	
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * userId
	 * @return the userId
	 */
	
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @param createUser the createUser to set
	 */
	
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	/**
	 * createTime
	 * @return the createTime
	 */
	
	public int getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	
	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

	/**
	 * updateUser
	 * @return the updateUser
	 */
	
	public String getUpdateUser() {
		return updateUser;
	}

	/**
	 * @param updateUser the updateUser to set
	 */
	
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	/**
	 * updateTime
	 * @return the updateTime
	 */
	
	public int getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	
	public void setUpdateTime(int updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * delFlg
	 * @return the delFlg
	 */
	
	public int getDelFlg() {
		return delFlg;
	}

	/**
	 * @param delFlg the delFlg to set
	 */
	
	public void setDelFlg(int delFlg) {
		this.delFlg = delFlg;
	}

	/**
	 * tradeSrch
	 * @return the tradeSrch
	 */
		
	public String getTradeSrch() {
		return tradeSrch;
			
	}

	/**
	 * @param tradeSrch the tradeSrch to set
	 */
		
	public void setTradeSrch(String tradeSrch) {
		this.tradeSrch = tradeSrch;
			
	} 
	
}

	