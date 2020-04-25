package com.hyjf.admin.coupon.config;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomize;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Michael
 */
public class CouponConfigBean extends CouponConfig implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 387630498860089653L;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;
	/**
	 * token
	 */
	private String pageToken;
	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;
	/**
	 * 列表
	 */
	private List<CouponConfigCustomize> recordList;
	
	/**
	 * 实体转换
	 */
	private String expirationDateStr;
	
	/**
	 * 优惠券名称查询
	 */
	private String couponNameSrch;
	/**
	 * 优惠券编号查询
	 */
	private String couponCodeSrch;
	/**
	 * 优惠券类型查询
	 */
	private String couponTypeSrch;

	/**
	 * 检索条件 时间开始
	 */
	private String timeStartSrch;

	/**
	 * 检索条件 时间结束
	 */
	private String timeEndSrch;
	
	/**
	 * 审核人
	 */
	private String auditUserRealname;
	
	/**
	 * 审核时间（年月日时分）
	 */
	private String auditTimeDisplay;
	
	//全部平台标识
	private String couponSystemAll;
	//全部项目标识
	private String projectTypeAll;


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

	/**
	 * timeStartSrch
	 * 
	 * @return the timeStartSrch
	 */

	public String getTimeStartSrch() {
		return timeStartSrch;
	}

	/**
	 * @param timeStartSrch
	 *            the timeStartSrch to set
	 */

	public void setTimeStartSrch(String timeStartSrch) {
		this.timeStartSrch = timeStartSrch;
	}

	/**
	 * timeEndSrch
	 * 
	 * @return the timeEndSrch
	 */

	public String getTimeEndSrch() {
		return timeEndSrch;
	}

	/**
	 * @param timeEndSrch
	 *            the timeEndSrch to set
	 */

	public void setTimeEndSrch(String timeEndSrch) {
		this.timeEndSrch = timeEndSrch;
	}

	public String getCouponNameSrch() {
		return couponNameSrch;
	}

	public void setCouponNameSrch(String couponNameSrch) {
		this.couponNameSrch = couponNameSrch;
	}

	public String getCouponTypeSrch() {
		return couponTypeSrch;
	}

	public void setCouponTypeSrch(String couponTypeSrch) {
		this.couponTypeSrch = couponTypeSrch;
	}

	public List<CouponConfigCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<CouponConfigCustomize> recordList) {
		this.recordList = recordList;
	}

	public String getExpirationDateStr() {
		return expirationDateStr;
	}

	public void setExpirationDateStr(String expirationDateStr) {
		this.expirationDateStr = expirationDateStr;
	}

	public String getAuditUserRealname() {
		return auditUserRealname;
	}

	public void setAuditUserRealname(String auditUserRealname) {
		this.auditUserRealname = auditUserRealname;
	}

	public String getAuditTimeDisplay() {
		return auditTimeDisplay;
	}

	public void setAuditTimeDisplay(String auditTimeDisplay) {
		this.auditTimeDisplay = auditTimeDisplay;
	}

    public String getCouponSystemAll() {
        return couponSystemAll;
    }

    public void setCouponSystemAll(String couponSystemAll) {
        this.couponSystemAll = couponSystemAll;
    }

    public String getProjectTypeAll() {
        return projectTypeAll;
    }

    public void setProjectTypeAll(String projectTypeAll) {
        this.projectTypeAll = projectTypeAll;
    }

	public String getCouponCodeSrch() {
		return couponCodeSrch;
	}

	public void setCouponCodeSrch(String couponCodeSrch) {
		this.couponCodeSrch = couponCodeSrch;
	}

	public String getPageToken() {
		return pageToken;
	}

	public void setPageToken(String pageToken) {
		this.pageToken = pageToken;
	}
}
