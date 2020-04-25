package com.hyjf.invest;

import java.io.Serializable;
import java.util.List;

import com.hyjf.base.bean.BaseResultBean;

public class AvailableCouponResultBean extends BaseResultBean implements Serializable  {


	private static final long serialVersionUID = 2569482809922162226L;
	//可用优惠券列表
	private List<CouponBean> availableCouponList;
	//不可用优惠券列表
	private List<CouponBean> notAvailableCouponList;
	//可用优惠券数量
	private Integer availableCouponListCount;
	//不可用优惠券数量
	private Integer notAvailableCouponListCount;
    public List<CouponBean> getAvailableCouponList() {
        return availableCouponList;
    }
    public void setAvailableCouponList(List<CouponBean> availableCouponList) {
        this.availableCouponList = availableCouponList;
    }
    public List<CouponBean> getNotAvailableCouponList() {
        return notAvailableCouponList;
    }
    public void setNotAvailableCouponList(List<CouponBean> notAvailableCouponList) {
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
