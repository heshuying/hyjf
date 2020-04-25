package com.hyjf.mybatis.model.customize;

import java.io.Serializable;

import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;

public class HjhAssetBorrowTypeCustomize extends HjhAssetBorrowType implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -6164613081621602935L;

    /** 项目名称 */
    private String projectName;

    /** 资产来源 */
    private String instName;
    
    /** 产品类型名称 */
    private String assetTypeName;

    /** 状态 */
    private String status;
    
    /** 资产来源 检索条件 */
    private String instCodeSrch;
	
	/** 产品类型 检索条件 */
    private String assetTypeSrch;
    
    /** 项目类型 */
    private String borrowCdSrch;
    
    /** 状态 检索条件 */
    private String statusSrch;

    /**
     * 检索条件 limitStart
     */
    private int limitStart = -1;

    /**
     * 检索条件 limitEnd
     */
    private int limitEnd = -1;

	/**
	 * projectName
	 * @return the projectName
	 */
	
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * instName
	 * @return the instName
	 */
	
	public String getInstName() {
		return instName;
	}

	/**
	 * @param instName the instName to set
	 */
	
	public void setInstName(String instName) {
		this.instName = instName;
	}

	/**
	 * assetTypeName
	 * @return the assetTypeName
	 */
	
	public String getAssetTypeName() {
		return assetTypeName;
	}

	/**
	 * @param assetTypeName the assetTypeName to set
	 */
	
	public void setAssetTypeName(String assetTypeName) {
		this.assetTypeName = assetTypeName;
	}

	/**
	 * status
	 * @return the status
	 */
	
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * instCodeSrch
	 * @return the instCodeSrch
	 */
	
	public String getInstCodeSrch() {
		return instCodeSrch;
	}

	/**
	 * @param instCodeSrch the instCodeSrch to set
	 */
	
	public void setInstCodeSrch(String instCodeSrch) {
		this.instCodeSrch = instCodeSrch;
	}

	/**
	 * assetTypeSrch
	 * @return the assetTypeSrch
	 */
	
	public String getAssetTypeSrch() {
		return assetTypeSrch;
	}

	/**
	 * @param assetTypeSrch the assetTypeSrch to set
	 */
	
	public void setAssetTypeSrch(String assetTypeSrch) {
		this.assetTypeSrch = assetTypeSrch;
	}

	/**
	 * borrowCdSrch
	 * @return the borrowCdSrch
	 */
	
	public String getBorrowCdSrch() {
		return borrowCdSrch;
	}

	/**
	 * @param borrowCdSrch the borrowCdSrch to set
	 */
	
	public void setBorrowCdSrch(String borrowCdSrch) {
		this.borrowCdSrch = borrowCdSrch;
	}

	/**
	 * statusSrch
	 * @return the statusSrch
	 */
	
	public String getStatusSrch() {
		return statusSrch;
	}

	/**
	 * @param statusSrch the statusSrch to set
	 */
	
	public void setStatusSrch(String statusSrch) {
		this.statusSrch = statusSrch;
	}

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

}
