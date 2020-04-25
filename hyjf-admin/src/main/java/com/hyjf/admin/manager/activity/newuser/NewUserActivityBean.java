package com.hyjf.admin.manager.activity.newuser;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

/**
 * 
 * 兑奖码列表
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月26日
 * @see 上午9:56:26
 */
public class NewUserActivityBean implements Serializable {

	/**
	 * serialVersionUID:
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户名 检索条件
	 */
	private String usernameSrch;
	
	/**
	 * 手机号
	 */
	private String mobileSrch;
	
	/**
	 * 推荐人用户名
	 */
	private String recommendNameSrch;
	
	/**
	 * 活动类型 0 68元代金券 1 1.5%加息券
	 */
	private String activitySrch;
	
	/**
	 * 发送状态 0 未发放  1 已发放
	 */
	private String sendStatusSrch;
	
	/**
	 * 注册平台
	 */
	private String registPlatSrch;
	
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

    public String getRecommendNameSrch() {
        return recommendNameSrch;
    }

    public void setRecommendNameSrch(String recommendNameSrch) {
        this.recommendNameSrch = recommendNameSrch;
    }

    public String getActivitySrch() {
        return activitySrch;
    }

    public void setActivitySrch(String activitySrch) {
        this.activitySrch = activitySrch;
    }

    public String getSendStatusSrch() {
        return sendStatusSrch;
    }

    public void setSendStatusSrch(String sendStatusSrch) {
        this.sendStatusSrch = sendStatusSrch;
    }

    public String getRegistPlatSrch() {
        return registPlatSrch;
    }

    public void setRegistPlatSrch(String registPlatSrch) {
        this.registPlatSrch = registPlatSrch;
    }

}
