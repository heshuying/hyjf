package com.hyjf.admin.coupon.tender.hjh;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.customize.coupon.CouponRecoverCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderDetailCustomize;

@Service
public class CouponTenderHjhServiceImpl extends BaseServiceImpl implements CouponTenderHjhService {

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	@Override
	public List<CouponTenderCustomize> getRecordList(CouponTenderCustomize couponTenderCustomize) {
		return couponTenderCustomizeMapper.selectCouponTenderHjhList(couponTenderCustomize);
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
		return couponTenderCustomizeMapper.countCouponTenderHjh(couponTenderCustomize);
	}

    @Override
    public List<CouponTenderCustomize> exoportRecordList(CouponTenderCustomize couponTenderCustomize) {
        return couponTenderCustomizeMapper.exportCouponTenderHjhList(couponTenderCustomize);
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
        return couponTenderCustomizeMapper.queryInvestTotalHjh(couponTenderCustomize);
    }
    
	/**
	 * 
	 * 根据条件查询优惠券使用详情
	 * 
	 * @author pcc
	 * @param detail
	 * @return
	 */
	@Override
	public CouponTenderDetailCustomize getCouponTenderDetailHjhCustomize(Map<String, Object> paramMap) {
		return couponTenderCustomizeMapper.selectCouponTenderDetailHjhCustomize(paramMap);
	}

	@Override
	public List<CouponRecoverCustomize> getCouponRecoverHjhCustomize(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return couponTenderCustomizeMapper.getCouponRecoverCustomizeHjh(paramMap);
	}
	


}
