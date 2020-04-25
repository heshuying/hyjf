package com.hyjf.activity.actdoubleeleven.bargain;

import com.hyjf.base.bean.BaseBean;

public class PrizeBuyRequestBean extends BaseBean{

	private Integer prizeId;
	private String wechatId;
	private String wechatNickName;
	private String bookingName;
	private String bookingMobile;
	private String bookingAddress;
	
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
	public String getBookingName() {
		return bookingName;
	}
	public void setBookingName(String bookingName) {
		this.bookingName = bookingName;
	}
	public String getBookingMobile() {
		return bookingMobile;
	}
	public void setBookingMobile(String bookingMobile) {
		this.bookingMobile = bookingMobile;
	}
	public String getBookingAddress() {
		return bookingAddress;
	}
	public void setBookingAddress(String bookingAddress) {
		this.bookingAddress = bookingAddress;
	}
	@Override
	public String toString() {
		return super.toString() + "PrizeBuyRequestBean [prizeId=" + prizeId + ", wechatId=" + wechatId + ", wechatNickName="
				+ wechatNickName + ", bookingName=" + bookingName + ", bookingMobile=" + bookingMobile
				+ ", bookingAddress=" + bookingAddress + "]";
	}
    
}
