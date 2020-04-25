package com.hyjf.admin.exception.singletradeinfo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**
* 接口：单笔资金类业务交易查询 画面实体类
* @author LiuBin
* @date 2017年7月31日 上午9:31:11
* 
*/ 
public class SingleTradeInfoBean implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 3427543263747796904L;
	/**
	 * 检索条件:用户ID
	 */
	private Integer userId;
	/**
	 * 检索条件:电子账号
	 */
	private String accountId;
	/**
	 * 检索条件:原交易日期
	 */
	private String orgTxDate;
	/**
	 * 检索条件:原交易时间
	 */
	private String orgTxTime;
	/**
	 * 检索条件:原交易流水号
	 */
	private String orgSeqNo;
	/**
	 * 结果
	 */
	private JSONObject result;
	/**
	 * accountId
	 * @return the accountId
	 */
	/**
	 * userId
	 * @return the userId
	 */
	
	public Integer getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * accountId
	 * @return the accountId
	 */
	
	public String getAccountId() {
		return accountId;
	}
	/**
	 * @param accountId the accountId to set
	 */
	
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	/**
	 * orgTxDate
	 * @return the orgTxDate
	 */
	
	public String getOrgTxDate() {
		return orgTxDate;
	}
	/**
	 * @param orgTxDate the orgTxDate to set
	 */
	
	public void setOrgTxDate(String orgTxDate) {
		this.orgTxDate = orgTxDate;
	}
	/**
	 * orgTxTime
	 * @return the orgTxTime
	 */
	
	public String getOrgTxTime() {
		return orgTxTime;
	}
	/**
	 * @param orgTxTime the orgTxTime to set
	 */
	
	public void setOrgTxTime(String orgTxTime) {
		this.orgTxTime = orgTxTime;
	}
	/**
	 * orgSeqNo
	 * @return the orgSeqNo
	 */
	
	public String getOrgSeqNo() {
		return orgSeqNo;
	}
	/**
	 * @param orgSeqNo the orgSeqNo to set
	 */
	
	public void setOrgSeqNo(String orgSeqNo) {
		this.orgSeqNo = orgSeqNo;
	}
	/**
	 * result
	 * @return the result
	 */
	
	public JSONObject getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	
	public void setResult(JSONObject result) {
		this.result = result;
	}

}
