package com.hyjf.admin.coupon.tender.hjh;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.customize.coupon.CouponRecoverCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderDetailCustomize;

public interface CouponTenderHjhService extends BaseService {

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<CouponTenderCustomize> getRecordList(CouponTenderCustomize couponTenderCustomize);

	/**
	 * 获得记录数
	 * @param CouponConfigCustomize
	 * @return
	 */
	public Integer countRecord(CouponTenderCustomize couponTenderCustomize);
	
	/**
	 * 获取单表
	 * 
	 * @return
	 */
	public CouponConfig getRecord(String id);

	/**
     * 导出列表
     * @param couponTenderCustomize
     * @return
     * @author Michael
     */
    public List<CouponTenderCustomize> exoportRecordList(CouponTenderCustomize couponTenderCustomize);

    /**
     * 
     * 计算汇直投优惠券使用列表真实出借金额总额
     * @author pcc
     * @param couponTenderCustomize
     * @return
     */
    public String queryInvestTotalHzt(CouponTenderCustomize couponTenderCustomize);

	List<CouponRecoverCustomize> getCouponRecoverHjhCustomize(Map<String, Object> paramMap);

	CouponTenderDetailCustomize getCouponTenderDetailHjhCustomize(Map<String, Object> paramMap);

}