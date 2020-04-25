package com.hyjf.activity.actdoubleeleven.bargain;

import com.hyjf.base.bean.BaseBean;

public class BargainRequestBean extends BaseBean{

	private Integer prizeId;
	private String wechatId;
	private String wechatNickName;
	private String wechatIdHelp;
	private String wechatNickNameHelp;
	
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
	@Override
	public String toString() {
		return super.toString() + "BargainRequestBean [prizeId=" + prizeId + ", wechatId=" + wechatId + ", wechatNickName="
				+ wechatNickName + ", wechatIdHelp=" + wechatIdHelp + ", wechatNickNameHelp=" + wechatNickNameHelp
				+ "]";
	}
    
}
