/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2015年11月20日 下午5:24:10
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.model.customize.billion;

import java.io.Serializable;

/**
 * @author Administrator
 */

public class ActivityBillionThirdCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;
	
	//用户优惠券id
	private Integer id;
	//优惠券面值
    private String couponQuota;
    //优惠券类型
    private String couponType;
    //开抢时间
    private String secKillTime;
    //用户名
    private String prizeName;
    //优惠券编号
    private String couponCode;
    //金额限制
    private String tenderQuota;
    //剩余数量
    private String remainingNum;
    //启用状态
    private String status;
    //开抢状态
    private String killStatus;
	/**
	 * 检索条件 时间开始
	 */
	private String timeStartSrch;

	/**
	 * 检索条件 时间结束
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCouponQuota() {
        return couponQuota==null?"0":couponQuota.substring(0, couponQuota.indexOf("."));
    }

    public void setCouponQuota(String couponQuota) {
        this.couponQuota = couponQuota;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getSecKillTime() {
        return secKillTime;
    }

    public void setSecKillTime(String secKillTime) {
        this.secKillTime = secKillTime;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getTenderQuota() {
        return tenderQuota;
    }

    public void setTenderQuota(String tenderQuota) {
        this.tenderQuota = tenderQuota;
    }

    public String getRemainingNum() {
        return remainingNum;
    }

    public void setRemainingNum(String remainingNum) {
        this.remainingNum = remainingNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getKillStatus() {
        return killStatus;
    }

    public void setKillStatus(String killStatus) {
        this.killStatus = killStatus;
    }

   
   
    
}
