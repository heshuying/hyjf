package com.hyjf.activity.actdoubleeleven.bargain;

import com.hyjf.base.bean.BaseBean;

public class BargainDoubleRequestBean extends BaseBean{

	private Integer idBargain;
	private Integer prizeId;
	private String wechatId;
	private String wechatNickName;
	private String wechatIdHelp;
	private String wechatNickNameHelp;
	private String phoneNum;
	private String smsCode;
	
	public Integer getPrizeId() {
		return prizeId;
	}
	public void setPrizeId(Integer prizeId) {
		this.prizeId = prizeId;
	}
	public String getWechatId() {
		return wechatId;
	}
	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}
	public String getWechatNickName() {
		return wechatNickName;
	}
	public void setWechatNickName(String wechatNickName) {
		this.wechatNickName = wechatNickName;
	}
	public String getWechatIdHelp() {
		return wechatIdHelp;
	}
	public void setWechatIdHelp(String wechatIdHelp) {
		this.wechatIdHelp = wechatIdHelp;
	}
	public String getWechatNickNameHelp() {
		return wechatNickNameHelp;
	}
	public void setWechatNickNameHelp(String wechatNickNameHelp) {
		this.wechatNickNameHelp = wechatNickNameHelp;
	}
	public Integer getIdBargain() {
		return idBargain;
	}
	public void setIdBargain(Integer idBargain) {
		this.idBargain = idBargain;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getSmsCode() {
		return smsCode;
	}
	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}
	@Override
	public String toString() {
		return super.toString() + "BargainDoubleRequestBean [idBargain=" + idBargain + ", prizeId=" + prizeId + ", wechatId=" + wechatId
				+ ", wechatNickName=" + wechatNickName + ", wechatIdHelp=" + wechatIdHelp + ", wechatNickNameHelp="
				+ wechatNickNameHelp + ", phoneNum=" + phoneNum + ", smsCode=" + smsCode + "]";
	}
    
}
