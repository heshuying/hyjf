/**
 * 10月份活动，用户使用推荐星实体类
 */	
package com.hyjf.mybatis.model.customize.admin;

import org.codehaus.plexus.util.StringUtils;

/**
 * 
 * @author zhangjinpeng
 *
 */

public class UsedRecommendCustomize {
	
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
	 * 参与活动
	 */
	private String prizeKindName;
	
	/**
	 * 兑换数量
	 */
	private Integer prizeCount;
	
	/**
	 * 使用推荐星数量
	 */
	private Integer usedRecommendCount;
	
	/**
	 * 获得奖品名称
	 */
	private String prizeName;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 参与时间
	 */
	private String addTime;
	
	/**
	 * 线上奖品是否发放
	 */
	private String prizeSendFlag;
	
	
	/********************页面表示用END**********************/
	
	
	/********************检索用字段START**********************/
	
	/**
	 * 活动类别（检索用）
	 */
	private Integer prizeKindCd;
	
	/**
	 * 线上奖品是否发放（检索用）
	 */
	private Integer prizeSendFlagSrch;
	
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

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getTimeStartSrch() {
		return timeStartSrch;
	}

	public void setTimeStartSrch(String timeStartSrch) {
		this.timeStartSrch = StringUtils.trim(timeStartSrch);
	}

	public String getTimeEndSrch() {
		return timeEndSrch;
	}

	public void setTimeEndSrch(String timeEndSrch) {
		this.timeEndSrch = StringUtils.trim(timeEndSrch);
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = StringUtils.trim(departmentName);
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

	public String getPrizeKindName() {
		return prizeKindName;
	}

	public void setPrizeKindName(String prizeKindName) {
		this.prizeKindName = StringUtils.trim(prizeKindName);
	}

	public Integer getPrizeCount() {
		return prizeCount;
	}

	public void setPrizeCount(Integer prizeCount) {
		this.prizeCount = prizeCount;
	}

	public Integer getUsedRecommendCount() {
		return usedRecommendCount;
	}

	public void setUsedRecommendCount(Integer usedRecommendCount) {
		this.usedRecommendCount = usedRecommendCount;
	}

	public String getPrizeName() {
		return prizeName;
	}

	public void setPrizeName(String prizeName) {
		this.prizeName = StringUtils.trim(prizeName);
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = StringUtils.trim(remark);
	}

	public Integer getPrizeKindCd() {
		return prizeKindCd;
	}

	public void setPrizeKindCd(Integer prizeKindCd) {
		this.prizeKindCd = prizeKindCd;
	}

	public String getPrizeSendFlag() {
		return prizeSendFlag;
	}

	public void setPrizeSendFlag(String prizeSendFlag) {
		this.prizeSendFlag = prizeSendFlag;
	}

	public Integer getPrizeSendFlagSrch() {
		return prizeSendFlagSrch;
	}

	public void setPrizeSendFlagSrch(Integer prizeSendFlagSrch) {
		this.prizeSendFlagSrch = prizeSendFlagSrch;
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

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

}

	