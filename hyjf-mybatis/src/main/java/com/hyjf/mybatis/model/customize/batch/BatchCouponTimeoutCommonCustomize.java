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

package com.hyjf.mybatis.model.customize.batch;

import java.io.Serializable;

/**
 * @author Administrator
 */

public class BatchCouponTimeoutCommonCustomize implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6302534038180678827L;
	
	/**
	 * 用户编号
	 */
	private Integer userId;
	
	/**
	 * 手机
	 */
	private String mobile;
	
	/**
	 * 代金券面值总计
	 */
	private Integer couponQuota;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getCouponQuota() {
		return couponQuota;
	}

	public void setCouponQuota(Integer couponQuota) {
		this.couponQuota = couponQuota;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	

}
