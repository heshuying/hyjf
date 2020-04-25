package com.hyjf.api.server.user.directrecharge;

import com.hyjf.base.bean.BaseBean;

/**
 * 外部服务接口:用户充值
 * 
 * @author liuyang
 *
 */
public class UserDirectRechargeRequestBean extends BaseBean {
	private String idNo;
	// 用户电子账户号
	private String accountId;
	// 充值金额
	private String txAmount;
	private String name;
	private String mobile;
	private String  channel;
	private String  cardNo;
    public String getIdNo() {
        return idNo;
    }
    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }
    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    public String getTxAmount() {
        return txAmount;
    }
    public void setTxAmount(String txAmount) {
        this.txAmount = txAmount;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getChannel() {
        return channel;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getCardNo() {
        return cardNo;
    }
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
