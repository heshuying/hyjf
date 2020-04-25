package com.hyjf.mybatis.model.customize;

import java.io.Serializable;

import com.hyjf.mybatis.model.auto.BorrowFinmanNewCharge;

public class BorrowFinmanNewChargeCustomize extends BorrowFinmanNewCharge implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -6164613091621602935L;

    /** 项目名称 */
    private String projectName;

    /** 资产来源 */
    private String instName;
    
    /** 产品类型名称 */
    private String assetTypeName;
    
    /** 资产来源 */
    private String instCodeSrch;
	
	/** 产品类型 */
    private String assetTypeSrch;
    
    /** 类型 */
    private String manChargeTypeSear;

    /** 期限 */
    private String manChargeTimeSear;

    /** 项目类型 */
    private String projectTypeSear;

    /** 状态 */
    private String statusSear;

    /**
     * 检索条件 limitStart
     */
    private int limitStart = -1;

    /**
     * 检索条件 limitEnd
     */
    private int limitEnd = -1;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getLimitStart() {
        return limitStart;
    }

    public void setLimitStart(int limitStart) {
        this.limitStart = limitStart;
    }

    public int getLimitEnd() {
        return limitEnd;
    }

    public void setLimitEnd(int limitEnd) {
        this.limitEnd = limitEnd;
    }

    public String getManChargeTypeSear() {
        return manChargeTypeSear;
    }

    public void setManChargeTypeSear(String manChargeTypeSear) {
        this.manChargeTypeSear = manChargeTypeSear;
    }

    public String getManChargeTimeSear() {
        return manChargeTimeSear;
    }

    public void setManChargeTimeSear(String manChargeTimeSear) {
        this.manChargeTimeSear = manChargeTimeSear;
    }

    public String getProjectTypeSear() {
        return projectTypeSear;
    }

    public void setProjectTypeSear(String projectTypeSear) {
        this.projectTypeSear = projectTypeSear;
    }

    public String getStatusSear() {
        return statusSear;
    }

    public void setStatusSear(String statusSear) {
        this.statusSear = statusSear;
    }

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}
	
	public String getAssetTypeName() {
		return assetTypeName;
	}

	public void setAssetTypeName(String assetTypeName) {
		this.assetTypeName = assetTypeName;
	}

	public String getInstCodeSrch() {
		return instCodeSrch;
	}

	public void setInstCodeSrch(String instCodeSrch) {
		this.instCodeSrch = instCodeSrch;
	}

	public String getAssetTypeSrch() {
		return assetTypeSrch;
	}
	
	public void setAssetTypeSrch(String assetTypeSrch) {
		this.assetTypeSrch = assetTypeSrch;
	}

}
