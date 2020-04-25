package com.hyjf.admin.coupon.backmoney.htj;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.coupon.CouponBackMoneyCustomize;

@Service
public class CouponBackMoneyHtjServiceImpl extends BaseServiceImpl implements CouponBackMoneyHtjService {

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	@Override
	public List<CouponBackMoneyCustomize> getRecordListTY(CouponBackMoneyCustomize couponBackMoneyCustomize) {
		return couponBackMoneyCustomizeMapper.selectCouponBackMoneyTYHtjList(couponBackMoneyCustomize);
	}


	/**
	 * 获得记录数
	 * @param CouponConfigCustomize
	 * @return
	 */
	@Override
	public Integer countRecordTY(CouponBackMoneyCustomize couponBackMoneyCustomize){
		return couponBackMoneyCustomizeMapper.countCouponBackMoneyTYHtj(couponBackMoneyCustomize);
	}
	
	/**
     * 获取列表
     * 
     * @return
     */
    @Override
    public List<CouponBackMoneyCustomize> getRecordListJX(CouponBackMoneyCustomize couponBackMoneyCustomize) {
        return couponBackMoneyCustomizeMapper.selectCouponBackMoneyJXHtjList(couponBackMoneyCustomize);
    }


    /**
     * 获得记录数
     * @param CouponConfigCustomize
     * @return
     */
    @Override
    public Integer countRecordJX(CouponBackMoneyCustomize couponBackMoneyCustomize){
        return couponBackMoneyCustomizeMapper.countCouponBackMoneyJXHtj(couponBackMoneyCustomize);
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
        return couponBackMoneyCustomizeMapper.exportCouponBackMoneyTYHtjList(couponBackMoneyCustomize);
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
        return couponBackMoneyCustomizeMapper.exportCouponBackMoneyJXHtjList(couponBackMoneyCustomize);
    }


    @Override
    public Integer countRecordDJ(CouponBackMoneyCustomize couponBackMoneyCustomize) {
        return couponBackMoneyCustomizeMapper.countCouponBackMoneyDJHtj(couponBackMoneyCustomize);
    }


    @Override
    public List<CouponBackMoneyCustomize> getRecordListDJ(CouponBackMoneyCustomize couponBackMoneyCustomize) {
        return couponBackMoneyCustomizeMapper.selectCouponBackMoneyDJHtjList(couponBackMoneyCustomize);
    }


    @Override
    public List<CouponBackMoneyCustomize> exoportDJRecordList(CouponBackMoneyCustomize couponBackMoneyCustomize) {
        return couponBackMoneyCustomizeMapper.exportCouponBackMoneyDJHtjList(couponBackMoneyCustomize);
    }


    @Override
    public String queryRecoverInterestTotle(CouponBackMoneyCustomize couponBackMoneyCustomize) {
        return couponBackMoneyCustomizeMapper.queryRecoverInterestTotleHtj(couponBackMoneyCustomize);
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
        return couponBackMoneyCustomizeMapper.queryInvestTotalHtj(couponBackMoneyCustomize);
    }
}
