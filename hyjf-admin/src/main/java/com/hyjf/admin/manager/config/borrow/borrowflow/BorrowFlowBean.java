package com.hyjf.admin.manager.config.borrow.borrowflow;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.customize.HjhAssetBorrowTypeCustomize;

public class BorrowFlowBean extends HjhAssetBorrowType implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 32154339572253967L;

    /** 列表list */
    private List<HjhAssetBorrowTypeCustomize> recordList;

    /** 资产来源 检索条件 */
    private String instCodeSrch;
	
	/** 产品类型 检索条件 */
    private String assetTypeSrch;
    
    /** 项目类型 */
    private String borrowCdSrch;
    
    /** 状态 检索条件 */
    private String statusSrch;

    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;

    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;

	private String manChargeTimeSear;

    public int getPaginatorPage() {
        return paginatorPage == 0 ? 1 : paginatorPage;
    }

    public void setPaginatorPage(int paginatorPage) {
        this.paginatorPage = paginatorPage;
    }

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

	/**
	 * recordList
	 * @return the recordList
	 */
	
	public List<HjhAssetBorrowTypeCustomize> getRecordList() {
		return recordList;
	}

	/**
	 * @param recordList the recordList to set
	 */
	
	public void setRecordList(List<HjhAssetBorrowTypeCustomize> recordList) {
		this.recordList = recordList;
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
	 * manChargeTimeSear
	 * @return the manChargeTimeSear
	 */
	
	public String getManChargeTimeSear() {
		return manChargeTimeSear;
	}

	/**
	 * @param manChargeTimeSear the manChargeTimeSear to set
	 */
	
	public void setManChargeTimeSear(String manChargeTimeSear) {
		this.manChargeTimeSear = manChargeTimeSear;
	}
}
