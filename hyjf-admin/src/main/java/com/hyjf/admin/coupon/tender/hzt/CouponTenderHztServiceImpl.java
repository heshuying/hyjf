package com.hyjf.admin.coupon.tender.hzt;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderCustomize;

@Service
public class CouponTenderHztServiceImpl extends BaseServiceImpl implements CouponTenderHztService {

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	@Override
	public List<CouponTenderCustomize> getRecordList(CouponTenderCustomize couponTenderCustomize) {
		return couponTenderCustomizeMapper.selectCouponTenderList(couponTenderCustomize);
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
		return couponTenderCustomizeMapper.countCouponTender(couponTenderCustomize);
	}

    @Override
    public List<CouponTenderCustomize> exoportRecordList(CouponTenderCustomize couponTenderCustomize) {
        return couponTenderCustomizeMapper.exportCouponTenderList(couponTenderCustomize);
    }
    /**
     * 
     * 计算汇直投优惠券使用列表真实出借金额总额
     * @author pcc
     * @param couponTenderCustomize
     * @return
     * @see com.hyjf.admin.coupon.tender.hzt.CouponTenderHztService#queryInvestTotalHzt(com.hyjf.mybatis.model.customize.coupon.CouponTenderCustomize)
     */
    @Override
    public String queryInvestTotalHzt(CouponTenderCustomize couponTenderCustomize) {
        return couponTenderCustomizeMapper.queryInvestTotalHzt(couponTenderCustomize);
    }
	


}
