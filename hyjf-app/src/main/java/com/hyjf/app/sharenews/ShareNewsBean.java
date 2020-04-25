package com.hyjf.app.sharenews;

import java.io.Serializable;

public class ShareNewsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


    private String title;//标题
    private String content;//内容
    private String img;//图片url
    private String linkUrl;//链接地址
    
    
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    

}
