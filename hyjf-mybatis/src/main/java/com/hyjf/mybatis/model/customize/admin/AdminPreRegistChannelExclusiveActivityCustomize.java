/**
 * Description:预注册渠道专属活动
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 朱晓东
 * @version: 1.0
 * Created at: 2016年06月23日 下午2:17:31
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.model.customize.admin;

/**
 * @author 朱晓东
 */

public class AdminPreRegistChannelExclusiveActivityCustomize {
    
    //主键
    public String id;
	//用户ID
	public String userId;
	//用户名
	public String userName;
	//手机号
    public String mobile;
	//推荐人ID
	public String referrer;
	//推荐人用户名
	public String referrerUserName;
	//预注册时间
	public String preRegistTime;
	//注册时间
	public String registTime;
	//关键字ID
	public String utmId;
	//关键词
	public String utmTerm;
	//渠道平台ID
    public String sourceId;
	//渠道平台名称
    public String utmSource;
    //活动期内累计出借额
    public String tenderTotal;
    //活动期内最高单笔出借额
    public String tenderSingle;
    //奖励
    public String reward;
    //备注
    public String remark;
    //创建时间
    public String createTime;
    //更新时间
    public String updateTime;

	/**
	 * 构造方法不带参数
	 */
	public AdminPreRegistChannelExclusiveActivityCustomize() {
		super();
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getReferrerUserName() {
        return referrerUserName;
    }

    public void setReferrerUserName(String referrerUserName) {
        this.referrerUserName = referrerUserName;
    }

    public String getPreRegistTime() {
        return preRegistTime;
    }

    public void setPreRegistTime(String preRegistTime) {
        this.preRegistTime = preRegistTime;
    }

    public String getRegistTime() {
        return registTime;
    }

    public void setRegistTime(String registTime) {
        this.registTime = registTime;
    }

    public String getUtmId() {
        return utmId;
    }

    public void setUtmId(String utmId) {
        this.utmId = utmId;
    }

    public String getUtmTerm() {
        return utmTerm;
    }

    public void setUtmTerm(String utmTerm) {
        this.utmTerm = utmTerm;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getUtmSource() {
        return utmSource;
    }

    public void setUtmSource(String utmSource) {
        this.utmSource = utmSource;
    }

    public String getTenderTotal() {
        return tenderTotal;
    }

    public void setTenderTotal(String tenderTotal) {
        this.tenderTotal = tenderTotal;
    }

    public String getTenderSingle() {
        return tenderSingle;
    }

    public void setTenderSingle(String tenderSingle) {
        this.tenderSingle = tenderSingle;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
