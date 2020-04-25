package com.hyjf.admin.coupon.backmoney.hzt;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.coupon.CouponBackMoneyCustomize;

public interface CouponBackMoneyHztService extends BaseService {

	/**
	 * 获取体验金回款列表
	 * 
	 * @return
	 */
	public List<CouponBackMoneyCustomize> getRecordListTY(CouponBackMoneyCustomize couponBackMoneyCustomize);

	/**
	 * 获得体验金回款记录数
	 * @param CouponConfigCustomize
	 * @return
	 */
	public Integer countRecordTY(CouponBackMoneyCustomize couponBackMoneyCustomize);

	/**
     * 获得加息券回款记录数
     * @param CouponConfigCustomize
     * @return
     */
    public Integer countRecordJX(CouponBackMoneyCustomize couponBackMoneyCustomize);
    
    /**
     * 获取加息券回款列表
     * 
     * @return
     */
    public List<CouponBackMoneyCustomize> getRecordListJX(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 导出体验金列表
     * @param couponTenderCustomize
     * @return
     * @author Michael
     */
    public List<CouponBackMoneyCustomize> exoportTYRecordList(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 导出加息券列表
     * @param couponTenderCustomize
     * @return
     * @author Michael
     */
    public List<CouponBackMoneyCustomize> exoportJXRecordList(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 获得代金券回款记录数
     * @param CouponConfigCustomize
     * @return
     */
    public Integer countRecordDJ(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 获取代金券回款列表
     * 
     * @return
     */
    public List<CouponBackMoneyCustomize> getRecordListDJ(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 导出代金券列表
     * @param couponTenderCustomize
     * @return
     * @author Michael
     */
    public List<CouponBackMoneyCustomize> exoportDJRecordList(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 
     * 计算汇直投优惠券回款应回款金额
     * @author pcc
     * @param couponBackMoneyCustomize
     * @return
     */
    public String queryRecoverInterestTotle(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 
     * 计算汇直投优惠券回款出借金额
     * @author pcc
     * @param couponBackMoneyCustomize
     * @return
     */
    public String queryInvestTotal(CouponBackMoneyCustomize couponBackMoneyCustomize);

	

}