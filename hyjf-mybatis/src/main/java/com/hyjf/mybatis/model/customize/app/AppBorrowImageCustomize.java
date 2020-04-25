package com.hyjf.mybatis.model.customize.app;

import java.io.Serializable;

public class AppBorrowImageCustomize implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 7226765771325156353L;

	private String typeType;

	private String typeName;

	private String typeLogo;
	
	private String notes;
	private String typeUrl;
	private String pageType;
	private String jumpName;


    public AppBorrowImageCustomize() {
		super();
	}

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

	public String getTypeType() {
		return typeType;
	}

	public void setTypeType(String typeType) {
		this.typeType = typeType;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeLogo() {
		return typeLogo;
	}

	public void setTypeLogo(String typeLogo) {
		this.typeLogo = typeLogo;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

    public String getTypeUrl() {
        return typeUrl;
    }

    public void setTypeUrl(String typeUrl) {
        this.typeUrl = typeUrl;
    }

    public String getJumpName() {
        return jumpName;
    }

    public void setJumpName(String jumpName) {
        this.jumpName = jumpName;
    }
	

}