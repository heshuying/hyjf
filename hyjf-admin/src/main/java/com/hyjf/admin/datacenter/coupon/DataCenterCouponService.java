package com.hyjf.admin.datacenter.coupon;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.datacenter.DataCenterCouponCustomize;

public interface DataCenterCouponService extends BaseService {

	/**
	 * 获取体验金回款列表
	 * 
	 * @return
	 */
	public List<DataCenterCouponCustomize> getRecordListTY(DataCenterCouponCustomize dataCenterCouponCustomize);

	/**
	 * 获得体验金回款记录数
	 * @param CouponConfigCustomize
	 * @return
	 */
	public Integer countRecordTY(DataCenterCouponCustomize dataCenterCouponCustomize);

	/**
     * 获得加息券回款记录数
     * @param CouponConfigCustomize
     * @return
     */
    public Integer countRecordJX(DataCenterCouponCustomize dataCenterCouponCustomize);
    
    /**
     * 获取加息券回款列表
     * 
     * @return
     */
    public List<DataCenterCouponCustomize> getRecordListJX(DataCenterCouponCustomize dataCenterCouponCustomize);

    /**
     * 获得代金券回款记录数
     * @param CouponConfigCustomize
     * @return
     */
    public Integer countRecordDJ(DataCenterCouponCustomize dataCenterCouponCustomize);
    
    /**
     * 获取代金券回款列表
     * 
     * @return
     */
    public List<DataCenterCouponCustomize> getRecordListDJ(DataCenterCouponCustomize dataCenterCouponCustomize);

	

}