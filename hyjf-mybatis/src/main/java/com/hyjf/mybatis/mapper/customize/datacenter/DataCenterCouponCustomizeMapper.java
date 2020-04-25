package com.hyjf.mybatis.mapper.customize.datacenter;

import java.util.List;

import com.hyjf.mybatis.model.customize.datacenter.DataCenterCouponCustomize;

public interface DataCenterCouponCustomizeMapper {

	/**
	 * 获取体验金列表
	 * 
	 * @param CouponConfigCustomize
	 * @return
	 */
	List<DataCenterCouponCustomize> selectDataCenterCouponTYList(DataCenterCouponCustomize dataCenterCouponCustomize);
	/**
	 * 获取体验金列表记录数
	 * 
	 * @param CouponConfigCustomize
	 * @return
	 */
	Integer countDataCenterCouponTY(DataCenterCouponCustomize dataCenterCouponCustomize);
	
	/**
     * 获取加息券列表
     * 
     * @param CouponConfigCustomize
     * @return
     */
    List<DataCenterCouponCustomize> selectDataCenterCouponJXList(DataCenterCouponCustomize dataCenterCouponCustomize);
    /**
     * 获取加息券列表记录数
     * 
     * @param CouponConfigCustomize
     * @return
     */
    Integer countDataCenterCouponJX(DataCenterCouponCustomize dataCenterCouponCustomize);
    
    /**
     * 获取代金券列表记录数
     * 
     * @param CouponConfigCustomize
     * @return
     */
    Integer countDataCenterCouponDJ(DataCenterCouponCustomize dataCenterCouponCustomize);
    
    /**
     * 获取代金券列表
     * 
     * @param CouponConfigCustomize
     * @return
     */
    List<DataCenterCouponCustomize> selectDataCenterCouponDJList(DataCenterCouponCustomize dataCenterCouponCustomize);
    

}