/**
 * 10月份活动，获取推荐星实体类
 */	
package com.hyjf.mybatis.model.customize.admin;

import org.codehaus.plexus.util.StringUtils;

/**
 * 
 * @author zhangjinpeng
 *
 */

public class GetRecommendCustomize {
	
	/********************页面表示用START**********************/
	/**
	 * 用户编号
	 */
	private Integer userId;
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 姓名
	 */
	private String trueName;
	
	/**
	 * 手机号
	 */
	private String mobile;
	
	/**
	 * 所属部门
	 */
	private String departmentName;
	
	/**
	 * 推荐星来源
	 */
	private String recommendSource;
	
	/**
	 * 推荐星数量
	 */
	private Integer recommendCount;
	
	/**
	 * 当前累计推荐星数量
	 */
	private Integer recommendCurrentTotal;
	
	/**
	 * 被邀请用户编号
	 */
	private Integer inviteByUserId ;
	
	/**
	 * 被邀请用户名
	 */
	private String inviteByUserName ;
	
	/**
	 * 被邀请用户名
	 */
	private String inviteByTrueName;
	
	/**
	 * 被邀请用户手机号
	 */
	private String inviteByMobile ;
	
	/**
	 * 获得时间
	 */
	private String sendTime ;
	
	/**
	 * 推荐星是否发放
	 */
	private String sendFlag ;
	
	/********************页面表示用END**********************/
	
	
	/********************检索用字段START**********************/
	
	/**
	 * 推荐星类别（检索用）
	 */
	private Integer recommendSourceSrch;
	
	/**
	 * 推荐星是否发放（检索用）
	 */
	private Integer sendFlagSrch;
	
	/**
     * 检索条件 时间开始
     */
    private String timeStartSrch;

    /**
     * 检索条件 时间结束
     */
    private String timeEndSrch;
    
	/** 
	 * 部门 （检索用）
	 * 
	 */
    private String combotreeSrch;
    /** 
     * 部门（检索用）
     */
    private String[] combotreeListSrch;
	
	/********************检索用字段END**********************/
	
	/** 翻页开始 */
    protected int limitStart = -1;
    /** 翻页结束 */
    protected int limitEnd = -1;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = StringUtils.trim(userName);
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = StringUtils.trim(mobile);
	}
	public String getRecommendSource() {
		return recommendSource;
	}

	public void setRecommendSource(String recommendSource) {
		this.recommendSource = recommendSource;
	}

	public Integer getRecommendCount() {
		return recommendCount;
	}

	public void setRecommendCount(Integer recommendCount) {
		this.recommendCount = recommendCount;
	}

	public Integer getRecommendCurrentTotal() {
		return recommendCurrentTotal;
	}

	public void setRecommendCurrentTotal(Integer recommendCurrentTotal) {
		this.recommendCurrentTotal = recommendCurrentTotal;
	}

	public Integer getInviteByUserId() {
		return inviteByUserId;
	}

	public void setInviteByUserId(Integer inviteByUserId) {
		this.inviteByUserId = inviteByUserId;
	}

	public String getInviteByUserName() {
		return inviteByUserName;
	}

	public void setInviteByUserName(String inviteByUserName) {
		this.inviteByUserName = StringUtils.trim(inviteByUserName);
	}

	public String getInviteByMobile() {
		return inviteByMobile;
	}

	public void setInviteByMobile(String inviteByMobile) {
		this.inviteByMobile = StringUtils.trim(inviteByMobile);
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public Integer getRecommendSourceSrch() {
		return recommendSourceSrch;
	}

	public void setRecommendSourceSrch(Integer recommendSourceSrch) {
		this.recommendSourceSrch = recommendSourceSrch;
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

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
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

	public String getSendFlag() {
		return sendFlag;
	}

	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}

	public Integer getSendFlagSrch() {
		return sendFlagSrch;
	}

	public void setSendFlagSrch(Integer sendFlagSrch) {
		this.sendFlagSrch = sendFlagSrch;
	}

	public String getCombotreeSrch() {
		return combotreeSrch;
	}

	public void setCombotreeSrch(String combotreeSrch) {
		this.combotreeSrch = combotreeSrch;
	}

	public String[] getCombotreeListSrch() {
		return combotreeListSrch;
	}

	public void setCombotreeListSrch(String[] combotreeListSrch) {
		this.combotreeListSrch = combotreeListSrch;
	}

    public String getTrueName() {
        return trueName;
    }

    public String getInviteByTrueName() {
        return inviteByTrueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public void setInviteByTrueName(String inviteByTrueName) {
        this.inviteByTrueName = inviteByTrueName;
    }

}

	