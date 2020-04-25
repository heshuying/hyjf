package com.hyjf.plan.coupon;

import java.io.Serializable;
import java.util.List;

import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.mybatis.model.customize.coupon.PlanCouponResultBean;

public class PlanAvailableCouponResultBean extends BaseResultBean implements Serializable  {


	private static final long serialVersionUID = 2569482809922162226L;
	//可用优惠券列表
	private List<PlanCouponResultBean> availableCouponList;
	//不可用优惠券列表
	private List<PlanCouponResultBean> notAvailableCouponList;
	//可用优惠券数量
	private Integer availableCouponListCount;
	//不可用优惠券数量
	private Integer notAvailableCouponListCount;
    public List<PlanCouponResultBean> getAvailableCouponList() {
        return availableCouponList;
    }
    public void setAvailableCouponList(List<PlanCouponResultBean> availableCouponList) {
        this.availableCouponList = availableCouponList;
    }
    public List<PlanCouponResultBean> getNotAvailableCouponList() {
        return notAvailableCouponList;
    }
    public void setNotAvailableCouponList(List<PlanCouponResultBean> notAvailableCouponList) {
        this.notAvailableCouponList = notAvailableCouponList;
    }
    public Integer getAvailableCouponListCount() {
        return availableCouponListCount;
    }
    public void setAvailableCouponListCount(Integer availableCouponListCount) {
        this.availableCouponListCount = availableCouponListCount;
    }
    public Integer getNotAvailableCouponListCount() {
        return notAvailableCouponListCount;
    }
    public void setNotAvailableCouponListCount(Integer notAvailableCouponListCount) {
        this.notAvailableCouponListCount = notAvailableCouponListCount;
    }
	
	
}
