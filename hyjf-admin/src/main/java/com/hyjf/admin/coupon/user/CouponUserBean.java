package com.hyjf.admin.coupon.user;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.coupon.CouponUserCustomize;

/**
 * 
 * @author Michael
 */
public class CouponUserBean extends CouponUserCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	
	/**
     * 检索条件 审批状态
     */
    private String auditStatus;
    
    /**
     * 审核口令
     */
    private String couponAuditPwd;
    /**
     * 检索条件 审批备注
     */ 
    private String description;
    
    private Integer amount;
    
	/**
     * 检索条件 有效时间开始
     */
    private String timeStartSrch;

    /**
     * 检索条件 有效时间结束
     */
    private String timeEndSrch;
    
    /**
     * 检索条件 添加开始时间
     */
    private String timeStartAddSrch;
    
    /**
     * 检索条件 添加结束时间
     */
    private String timeEndAddSrch;
    
	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;
	/**
	 * 列表
	 */
	private List<CouponUserCustomize> recordList;
	
	
	public int getPaginatorPage() {
		if (paginatorPage == 0) {
			paginatorPage = 1;
		}
		return paginatorPage;
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

	public List<CouponUserCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<CouponUserCustomize> recordList) {
		this.recordList = recordList;
	}

    public String getTimeStartSrch() {
        return timeStartSrch;
    }

    public void setTimeStartSrch(String timeStartSrch) {
        this.timeStartSrch = timeStartSrch;
    }

    public String getTimeEndSrch() {
        return timeEndSrch;
    }

    public void setTimeEndSrch(String timeEndSrch) {
        this.timeEndSrch = timeEndSrch;
    }

    public String getTimeStartAddSrch() {
        return timeStartAddSrch;
    }

    public void setTimeStartAddSrch(String timeStartAddSrch) {
        this.timeStartAddSrch = timeStartAddSrch;
    }

    public String getTimeEndAddSrch() {
        return timeEndAddSrch;
    }

    public void setTimeEndAddSrch(String timeEndAddSrch) {
        this.timeEndAddSrch = timeEndAddSrch;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

    public String getCouponAuditPwd() {
        return couponAuditPwd;
    }

    public void setCouponAuditPwd(String couponAuditPwd) {
        this.couponAuditPwd = couponAuditPwd;
    }

    
}
