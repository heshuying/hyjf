package com.hyjf.app.msgpush;

import java.io.Serializable;

public class MsgPushTagInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/******************消息标签返回*********************/
	//消息图片url
	private String iconUrl;
	//消息类型名称
	private String tagName;
	//消息类型(id,可根据类型id获取消息列表)
	private String tagId;
	//消息内容
	private String introduction;
	//消息时间
	private String time;
	//消息红点标识(0显示，1不显示)
	private String redFlag;
	
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getTagId() {
		return tagId;
	}
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getRedFlag() {
		return redFlag;
	}
	public void setRedFlag(String redFlag) {
		this.redFlag = redFlag;
	}
	
}
