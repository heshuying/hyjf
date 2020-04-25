package com.hyjf.admin.manager.activity.billion.third;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.ActivityBillionThird;

/**
 * 
 * @author Michael
 */
public class BillionThirdBean extends ActivityBillionThird implements Serializable {

	/**
	 * serialVersionUID:
	 */
	private static final long serialVersionUID = 1L;
	
	
	private List<ActivityBillionThird> recordList;
	
	/**
	 * 活动时间 检索条件
	 */
	private String timeStartSrch;
	/**
	 * 活动时间 检索条件
	 */
	private String timeEndSrch;
	/**
	 * 用户名 检索条件
	 */
	private String userNameSrch;
	/**
	 * 姓名 检索条件
	 */
	private String trueNameSrch;
	/**
	 * 手机号 检索条件
	 */
	private String mobileSrch;
	/**
	 * 发放状态 检索条件
	 */
	private String couponCodeSrch;
	
	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

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

	/**
	 * limitStart
	 * 
	 * @return the limitStart
	 */
	public int getLimitStart() {
		return limitStart;
	}

	/**
	 * @param limitStart
	 *            the limitStart to set
	 */
	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	/**
	 * limitEnd
	 * 
	 * @return the limitEnd
	 */
	public int getLimitEnd() {
		return limitEnd;
	}

	/**
	 * @param limitEnd
	 *            the limitEnd to set
	 */
	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}

	public List<ActivityBillionThird> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<ActivityBillionThird> recordList) {
		this.recordList = recordList;
	}

	public String getUserNameSrch() {
		return userNameSrch;
	}

	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
	}

	public String getTrueNameSrch() {
		return trueNameSrch;
	}

	public void setTrueNameSrch(String trueNameSrch) {
		this.trueNameSrch = trueNameSrch;
	}

	public String getMobileSrch() {
		return mobileSrch;
	}

	public void setMobileSrch(String mobileSrch) {
		this.mobileSrch = mobileSrch;
	}

	public String getCouponCodeSrch() {
		return couponCodeSrch;
	}

	public void setCouponCodeSrch(String couponCodeSrch) {
		this.couponCodeSrch = couponCodeSrch;
	}

}
