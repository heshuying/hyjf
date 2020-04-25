package com.hyjf.admin.coupon.tender.htj;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderCustomize;

@Service
public class CouponTenderHtjServiceImpl extends BaseServiceImpl implements CouponTenderHtjService {

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	@Override
	public List<CouponTenderCustomize> getRecordList(CouponTenderCustomize couponTenderCustomize) {
		return couponTenderCustomizeMapper.selectCouponTenderHtjList(couponTenderCustomize);
	}

	/**
	 * 获取单个维护
	 * 
	 * @return
	 */
	@Override
	public CouponConfig getRecord(String record) {
		CouponConfig coupon = couponConfigMapper.selectByPrimaryKey(Integer.parseInt(record));
		return coupon;
	}

	/**
	 * 获得记录数
	 * @param CouponConfigCustomize
	 * @return
	 */
	@Override
	public Integer countRecord(CouponTenderCustomize couponTenderCustomize){
		return couponTenderCustomizeMapper.countCouponTenderHtj(couponTenderCustomize);
	}

    @Override
    public List<CouponTenderCustomize> exoportRecordList(CouponTenderCustomize couponTenderCustomize) {
        return couponTenderCustomizeMapper.exportCouponTenderHtjList(couponTenderCustomize);
    }

    @Override
    public String queryRecoverAccountTotalHtj(CouponTenderCustomize couponTenderCustomize) {
        return couponTenderCustomizeMapper.queryRecoverAccountTotalHtj(couponTenderCustomize);
    }
	


}
