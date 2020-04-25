/**
 * 首页实体
 * @package com.hyjf.app.home
 * @author 王坤
 * @date 2016/02/05 10:30
 * @version V1.0  
 */
package com.hyjf.app.user.coupon;

import java.io.Serializable;

public class CouponBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3803722754627032581L;
	//用户优惠券id
    private String userCouponId;
	//优惠券类型
	private String couponType;
	//项目类型
    private String projectType;
	//操作平台
	private String operationPlatform;
	//优惠券额度
    private String couponQuota;
    //出借金额
    private String investQuota;
    //优惠券有效期
    private String time;
    //出借期限
    private String investTime;
    //备注
    private String remarks;
	public String getUserCouponId() {
		return userCouponId;
	}
	public void setUserCouponId(String userCouponId) {
		this.userCouponId = userCouponId;
	}
	public String getCouponType() {
		return couponType;
	}
	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public String getOperationPlatform() {
		return operationPlatform;
	}
	public void setOperationPlatform(String operationPlatform) {
		this.operationPlatform = operationPlatform;
	}
	public String getCouponQuota() {
		return couponQuota;
	}
	public void setCouponQuota(String couponQuota) {
		this.couponQuota = couponQuota;
	}
	public String getInvestQuota() {
		return investQuota;
	}
	public void setInvestQuota(String investQuota) {
		this.investQuota = investQuota;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getInvestTime() {
		return investTime;
	}
	public void setInvestTime(String investTime) {
		this.investTime = investTime;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
    
    
    
}
