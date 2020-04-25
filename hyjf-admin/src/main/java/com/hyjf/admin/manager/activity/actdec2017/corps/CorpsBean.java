package com.hyjf.admin.manager.activity.actdec2017.corps;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

/**
 * 
 * 十二月份活动
 * @author dddzs
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月26日
 * @see 上午9:56:26
 */
public class CorpsBean implements Serializable {

	/**
	 * serialVersionUID:
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户id
	 */
	private Integer userId;

	/**
	 * 用户名 检索条件
	 */
	private String usernameSrch;
	
	/**
	 * 手机号
	 */
	private String mobileSrch;
	/**
	 * 微信昵称
	 */
	private String wxNameSrch;
	
	/**
	 * 奖励名称
	 */
	private String couponNameSrch;
	/**
	 * 奖励发放
	 */
	private String typeSrch;
	
	/**
	 * 出借时间 检索条件
	 */
	private String timeStartSrch;
	/**
	 * 出借时间 检索条件
	 */
	private String timeEndSrch;
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
	

	public String getWxNameSrch() {
		return wxNameSrch;
	}

	public void setWxNameSrch(String wxNameSrch) {
		this.wxNameSrch = wxNameSrch;
	}

	public String getTypeSrch() {
		return typeSrch;
	}

	public void setTypeSrch(String typeSrch) {
		this.typeSrch = typeSrch;
	}

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
	 * usernameSrch
	 * 
	 * @return the usernameSrch
	 */

	public String getUsernameSrch() {
		return usernameSrch;
	}

	/**
	 * @param usernameSrch
	 *            the usernameSrch to set
	 */

	public void setUsernameSrch(String usernameSrch) {
		this.usernameSrch = usernameSrch;
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

    public String getMobileSrch() {
        return mobileSrch;
    }

    public void setMobileSrch(String mobileSrch) {
        this.mobileSrch = mobileSrch;
    }

    public String getCouponNameSrch() {
        return couponNameSrch;
    }

    public void setCouponNameSrch(String couponNameSrch) {
        this.couponNameSrch = couponNameSrch;
    }

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
