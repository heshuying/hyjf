package com.hyjf.admin.manager.activity.actnov2017.bargain;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

/**
 * 
 * 七月份活动
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月26日
 * @see 上午9:56:26
 */
public class ActBargainBean implements Serializable {

	/**
	 * serialVersionUID:
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户名 检索条件
	 */
	private String wechatNameSrch;
	
	/**
	 * 手机号
	 */
	private String wechatNickNameSrch;
	
	/**
	 * 奖励名称
	 */
	private String wechatNameHelpSrch;
	
	/**
	 * 手机号
	 */
	private String wechatNickNameHelpSrch;
	
	/**
	 * 奖励名称
	 */
	private String mobileSrch;
	
	/**
	 * 奖励名称
	 */
	private String bargainMoneySrch;
	
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

    public String getMobileSrch() {
        return mobileSrch;
    }

    public void setMobileSrch(String mobileSrch) {
        this.mobileSrch = mobileSrch;
    }

	public String getWechatNameSrch() {
		return wechatNameSrch;
	}

	public void setWechatNameSrch(String wechatNameSrch) {
		this.wechatNameSrch = wechatNameSrch;
	}

	public String getWechatNickNameSrch() {
		return wechatNickNameSrch;
	}

	public void setWechatNickNameSrch(String wechatNickNameSrch) {
		this.wechatNickNameSrch = wechatNickNameSrch;
	}

	public String getWechatNameHelpSrch() {
		return wechatNameHelpSrch;
	}

	public void setWechatNameHelpSrch(String wechatNameHelpSrch) {
		this.wechatNameHelpSrch = wechatNameHelpSrch;
	}

	public String getWechatNickNameHelpSrch() {
		return wechatNickNameHelpSrch;
	}

	public void setWechatNickNameHelpSrch(String wechatNickNameHelpSrch) {
		this.wechatNickNameHelpSrch = wechatNickNameHelpSrch;
	}

	public String getBargainMoneySrch() {
		return bargainMoneySrch;
	}

	public void setBargainMoneySrch(String bargainMoneySrch) {
		this.bargainMoneySrch = bargainMoneySrch;
	}

}
