/**
 * 10月份活动，邀请用户信息实体类
 */	
package com.hyjf.mybatis.model.customize.admin;

import org.codehaus.plexus.util.StringUtils;

/**
 * 
 * @author zhangjinpeng
 *
 */

public class InviteUserCustomize {
	
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
	 * 真实用户名
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
	 * 用户属性名
	 */
	private String userAttrName;
	
	/**
	 * 可用推荐星数量
	 */
	private Integer recommendValidCount;
	
	/**
	 * 累计推荐星数量
	 */
	private Integer recommendAllCount;
	
	/**
	 * 使用的推荐星数量
	 */
	private Integer recommendUsedCount;
	
	/********************页面表示用END**********************/
	
	
	/********************检索用字段START**********************/

	/**
	 * 所属部门编号（检索用）
	 */
	private Integer departmentId;
	
	/**
	 * 用户属性编号（检索用）
	 */
	private Integer userAttrCd;
	
	/**
	 * 可用推荐星数量最小值(检索用)
	 */
	private Integer recommendValidCountMin;
	
	/**
	 * 可用推荐星数量最大值(检索用)
	 */
	private Integer recommendValidCountMax;
	
	/**
	 * 累计推荐星数量最小值（检索用）
	 */
	private Integer recommendAllCountMin;
	
	/**
	 * 累计推荐星数量最大值（检索用）
	 */
	private Integer recommendAllCountMax;
	
	/**
	 * 使用的推荐星数量最小值（检索用）
	 */
	private Integer recommendUsedCountMin;
	
	/**
	 * 使用的推荐星数量最大值（检索用）
	 */
	private Integer recommendUsedCountMax;
	
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

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	public String getUserAttrName() {
		return userAttrName;
	}

	public void setUserAttrName(String userAttrName) {
		this.userAttrName = userAttrName;
	}

	public Integer getUserAttrCd() {
		return userAttrCd;
	}

	public void setUserAttrCd(Integer userAttrCd) {
		this.userAttrCd = userAttrCd;
	}

	public Integer getRecommendValidCount() {
		return recommendValidCount;
	}

	public void setRecommendValidCount(Integer recommendValidCount) {
		this.recommendValidCount = recommendValidCount;
	}

	public Integer getRecommendAllCount() {
		return recommendAllCount;
	}

	public void setRecommendAllCount(Integer recommendAllCount) {
		this.recommendAllCount = recommendAllCount;
	}

	public Integer getRecommendUsedCount() {
		return recommendUsedCount;
	}

	public void setRecommendUsedCount(Integer recommendUsedCount) {
		this.recommendUsedCount = recommendUsedCount;
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

	public Integer getRecommendValidCountMin() {
		return recommendValidCountMin;
	}

	public void setRecommendValidCountMin(Integer recommendValidCountMin) {
		this.recommendValidCountMin = recommendValidCountMin;
	}

	public Integer getRecommendValidCountMax() {
		return recommendValidCountMax;
	}

	public void setRecommendValidCountMax(Integer recommendValidCountMax) {
		this.recommendValidCountMax = recommendValidCountMax;
	}

	public Integer getRecommendAllCountMin() {
		return recommendAllCountMin;
	}

	public void setRecommendAllCountMin(Integer recommendAllCountMin) {
		this.recommendAllCountMin = recommendAllCountMin;
	}

	public Integer getRecommendAllCountMax() {
		return recommendAllCountMax;
	}

	public void setRecommendAllCountMax(Integer recommendAllCountMax) {
		this.recommendAllCountMax = recommendAllCountMax;
	}

	public Integer getRecommendUsedCountMin() {
		return recommendUsedCountMin;
	}

	public void setRecommendUsedCountMin(Integer recommendUsedCountMin) {
		this.recommendUsedCountMin = recommendUsedCountMin;
	}

	public Integer getRecommendUsedCountMax() {
		return recommendUsedCountMax;
	}

	public void setRecommendUsedCountMax(Integer recommendUsedCountMax) {
		this.recommendUsedCountMax = recommendUsedCountMax;
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

	