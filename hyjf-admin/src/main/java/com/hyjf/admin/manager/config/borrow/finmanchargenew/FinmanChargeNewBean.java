package com.hyjf.admin.manager.config.borrow.finmanchargenew;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.BorrowFinmanNewCharge;
import com.hyjf.mybatis.model.customize.BorrowFinmanNewChargeCustomize;

public class FinmanChargeNewBean extends BorrowFinmanNewCharge implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 8853785949572253967L;

    /** 管理费列表list */
    private List<BorrowFinmanNewChargeCustomize> recordList;

    /** 类型 */
    private String manChargeTypeSear;
    
    /** 资产来源 */
	String instCodeSrch;
	
	/** 产品类型 */
	String assetTypeSrch;
	
    /** 期限 */
    private String manChargeTimeSear;

    /** 项目类型 */
    private String projectTypeSear;

    /** 状态 */
    private String statusSear;

    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;

    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;

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

    public List<BorrowFinmanNewChargeCustomize> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<BorrowFinmanNewChargeCustomize> recordList) {
        this.recordList = recordList;
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
