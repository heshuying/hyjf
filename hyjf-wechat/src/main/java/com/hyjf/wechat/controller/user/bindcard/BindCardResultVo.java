package com.hyjf.wechat.controller.user.bindcard;

import com.hyjf.wechat.base.BaseResultBean;

public class BindCardResultVo extends BaseResultBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String username;
	private String trueUsername;
	private String cardNo;
	private String trueCardNo;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * username
	 * @return the username
	 */
	
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * trueUsername
	 * @return the trueUsername
	 */
	
	public String getTrueUsername() {
		return trueUsername;
	}
	/**
	 * @param trueUsername the trueUsername to set
	 */
	
	public void setTrueUsername(String trueUsername) {
		this.trueUsername = trueUsername;
	}
	/**
	 * cardNo
	 * @return the cardNo
	 */
	
	public String getCardNo() {
		return cardNo;
	}
	/**
	 * @param cardNo the cardNo to set
	 */
	
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	/**
	 * trueCardNo
	 * @return the trueCardNo
	 */
	
	public String getTrueCardNo() {
		return trueCardNo;
	}
	/**
	 * @param trueCardNo the trueCardNo to set
	 */
	
	public void setTrueCardNo(String trueCardNo) {
		this.trueCardNo = trueCardNo;
	}

}
