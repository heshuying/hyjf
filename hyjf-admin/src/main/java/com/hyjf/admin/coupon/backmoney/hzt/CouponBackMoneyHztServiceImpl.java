package com.hyjf.admin.coupon.backmoney.hzt;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.coupon.CouponBackMoneyCustomize;

@Service
public class CouponBackMoneyHztServiceImpl extends BaseServiceImpl implements CouponBackMoneyHztService {

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	@Override
	public List<CouponBackMoneyCustomize> getRecordListTY(CouponBackMoneyCustomize couponBackMoneyCustomize) {
		return couponBackMoneyCustomizeMapper.selectCouponBackMoneyTYList(couponBackMoneyCustomize);
	}


	/**
	 * 获得记录数
	 * @param CouponConfigCustomize
	 * @return
	 */
	@Override
	public Integer countRecordTY(CouponBackMoneyCustomize couponBackMoneyCustomize){
		return couponBackMoneyCustomizeMapper.countCouponBackMoneyTY(couponBackMoneyCustomize);
	}
	
	/**
     * 获取列表
     * 
     * @return
     */
    @Override
    public List<CouponBackMoneyCustomize> getRecordListJX(CouponBackMoneyCustomize couponBackMoneyCustomize) {
        return couponBackMoneyCustomizeMapper.selectCouponBackMoneyJXList(couponBackMoneyCustomize);
    }


    /**
     * 获得记录数
     * @param CouponConfigCustomize
     * @return
     */
    @Override
    public Integer countRecordJX(CouponBackMoneyCustomize couponBackMoneyCustomize){
        return couponBackMoneyCustomizeMapper.countCouponBackMoneyJX(couponBackMoneyCustomize);
    }

    /**
     * 
     * 导出体验金回款列表
     * @author pcc
     * @param couponBackMoneyCustomize
     * @return
     * @see com.hyjf.admin.coupon.backmoney.hzt.CouponBackMoneyHztService#exoportTYRecordList(com.hyjf.mybatis.model.customize.coupon.CouponBackMoneyCustomize)
     */
    @Override
    public List<CouponBackMoneyCustomize> exoportTYRecordList(CouponBackMoneyCustomize couponBackMoneyCustomize) {
        return couponBackMoneyCustomizeMapper.exportCouponBackMoneyTYList(couponBackMoneyCustomize);
    }

    /**
     * 
     * 导出加息券回款列表
     * @author pcc
     * @param couponBackMoneyCustomize
     * @return
     */
    @Override
    public List<CouponBackMoneyCustomize> exoportJXRecordList(CouponBackMoneyCustomize couponBackMoneyCustomize) {
        return couponBackMoneyCustomizeMapper.exportCouponBackMoneyJXList(couponBackMoneyCustomize);
    }


    @Override
    public Integer countRecordDJ(CouponBackMoneyCustomize couponBackMoneyCustomize) {
        return couponBackMoneyCustomizeMapper.countCouponBackMoneyDJ(couponBackMoneyCustomize);
    }


    @Override
    public List<CouponBackMoneyCustomize> getRecordListDJ(CouponBackMoneyCustomize couponBackMoneyCustomize) {
        return couponBackMoneyCustomizeMapper.selectCouponBackMoneyDJList(couponBackMoneyCustomize);
    }


    @Override
    public List<CouponBackMoneyCustomize> exoportDJRecordList(CouponBackMoneyCustomize couponBackMoneyCustomize) {
        return couponBackMoneyCustomizeMapper.exportCouponBackMoneyDJList(couponBackMoneyCustomize);
    }


    @Override
    public String queryRecoverInterestTotle(CouponBackMoneyCustomize couponBackMoneyCustomize) {
        return couponBackMoneyCustomizeMapper.queryRecoverInterestTotle(couponBackMoneyCustomize);
    }

    /**
     * 
     * 计算汇添金优惠券回款应回款金额
     * @author pcc
     * @param couponBackMoneyCustomize
     * @return
     */
    @Override
    public String queryInvestTotal(CouponBackMoneyCustomize couponBackMoneyCustomize) {
        return couponBackMoneyCustomizeMapper.queryInvestTotal(couponBackMoneyCustomize);
    }
}
