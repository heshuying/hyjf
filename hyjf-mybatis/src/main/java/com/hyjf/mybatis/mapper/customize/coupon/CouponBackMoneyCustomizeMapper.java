package com.hyjf.mybatis.mapper.customize.coupon;

import java.util.List;

import com.hyjf.mybatis.model.customize.coupon.CouponBackMoneyCustomize;

public interface CouponBackMoneyCustomizeMapper {

	/**
	 * 获取体验金列表
	 * 
	 * @param CouponConfigCustomize
	 * @return
	 */
	List<CouponBackMoneyCustomize> selectCouponBackMoneyTYList(CouponBackMoneyCustomize couponBackMoneyCustomize);
	/**
	 * 获取体验金列表记录数
	 * 
	 * @param CouponConfigCustomize
	 * @return
	 */
	Integer countCouponBackMoneyTY(CouponBackMoneyCustomize couponBackMoneyCustomize);
	
	/**
     * 获取加息券列表
     * 
     * @param CouponConfigCustomize
     * @return
     */
    List<CouponBackMoneyCustomize> selectCouponBackMoneyJXList(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 获取加息券列表记录数
     * 
     * @param CouponConfigCustomize
     * @return
     */
    Integer countCouponBackMoneyJX(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 
     * 导出体验金回款列表
     * @author pcc
     * @param couponBackMoneyCustomize
     * @return
     */
    List<CouponBackMoneyCustomize> exportCouponBackMoneyTYList(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 
     * 导出加息券回款列表
     * @author pcc
     * @param couponBackMoneyCustomize
     * @return
     */
    List<CouponBackMoneyCustomize> exportCouponBackMoneyJXList(CouponBackMoneyCustomize couponBackMoneyCustomize);
    
    /**
     * 获取代金券列表记录数
     * 
     * @param CouponConfigCustomize
     * @return
     */
    Integer countCouponBackMoneyDJ(CouponBackMoneyCustomize couponBackMoneyCustomize);
    
    /**
     * 获取代金券列表
     * 
     * @param CouponConfigCustomize
     * @return
     */
    List<CouponBackMoneyCustomize> selectCouponBackMoneyDJList(CouponBackMoneyCustomize couponBackMoneyCustomize);
    
    /**
     * 
     * 导出代金券回款列表
     * @author pcc
     * @param couponBackMoneyCustomize
     * @return
     */
    List<CouponBackMoneyCustomize> exportCouponBackMoneyDJList(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 
     * 加息券回款总收益
     * @author pcc
     * @param couponBackMoneyCustomize
     * @return
     */
    String queryRecoverInterestTotle(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 
     * 计算汇直投优惠券回款应回款金额
     * @author pcc
     * @param couponBackMoneyCustomize
     * @return
     */
    String queryInvestTotal(CouponBackMoneyCustomize couponBackMoneyCustomize);
    
    
    
    
    /***************************************************汇添金优惠券回款*****************************************************************/
    
    
    /**
     * 获取体验金列表
     * 
     * @param CouponConfigCustomize
     * @return
     */
    List<CouponBackMoneyCustomize> selectCouponBackMoneyTYHtjList(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 获取体验金列表记录数
     * 
     * @param CouponConfigCustomize
     * @return
     */
    Integer countCouponBackMoneyTYHtj(CouponBackMoneyCustomize couponBackMoneyCustomize);
    
    /**
     * 获取加息券列表
     * 
     * @param CouponConfigCustomize
     * @return
     */
    List<CouponBackMoneyCustomize> selectCouponBackMoneyJXHtjList(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 获取加息券列表记录数
     * 
     * @param CouponConfigCustomize
     * @return
     */
    Integer countCouponBackMoneyJXHtj(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 
     * 导出体验金回款列表
     * @author pcc
     * @param couponBackMoneyCustomize
     * @return
     */
    List<CouponBackMoneyCustomize> exportCouponBackMoneyTYHtjList(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 
     * 导出加息券回款列表
     * @author pcc
     * @param couponBackMoneyCustomize
     * @return
     */
    List<CouponBackMoneyCustomize> exportCouponBackMoneyJXHtjList(CouponBackMoneyCustomize couponBackMoneyCustomize);
    
    /**
     * 获取代金券列表记录数
     * 
     * @param CouponConfigCustomize
     * @return
     */
    Integer countCouponBackMoneyDJHtj(CouponBackMoneyCustomize couponBackMoneyCustomize);
    
    /**
     * 获取代金券列表
     * 
     * @param CouponConfigCustomize
     * @return
     */
    List<CouponBackMoneyCustomize> selectCouponBackMoneyDJHtjList(CouponBackMoneyCustomize couponBackMoneyCustomize);
    
    /**
     * 
     * 导出代金券回款列表
     * @author pcc
     * @param couponBackMoneyCustomize
     * @return
     */
    List<CouponBackMoneyCustomize> exportCouponBackMoneyDJHtjList(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 
     * 加息券回款总收益
     * @author pcc
     * @param couponBackMoneyCustomize
     * @return
     */
    String queryRecoverInterestTotleHtj(CouponBackMoneyCustomize couponBackMoneyCustomize);
    String queryInvestTotalHtj(CouponBackMoneyCustomize couponBackMoneyCustomize);
    
    /***************************************汇计划优惠券还款*********************************************/
    /**
     * 获取体验金列表
     * 
     * @param CouponConfigCustomize
     * @return
     */
    List<CouponBackMoneyCustomize> selectCouponBackMoneyTYHjhList(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 获取体验金列表记录数
     * 
     * @param CouponConfigCustomize
     * @return
     */
    Integer countCouponBackMoneyTYHjh(CouponBackMoneyCustomize couponBackMoneyCustomize);
    
    /**
     * 获取加息券列表
     * 
     * @param CouponConfigCustomize
     * @return
     */
    List<CouponBackMoneyCustomize> selectCouponBackMoneyJXHjhList(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 获取加息券列表记录数
     * 
     * @param CouponConfigCustomize
     * @return
     */
    Integer countCouponBackMoneyJXHjh(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 
     * 导出体验金回款列表
     * @author pcc
     * @param couponBackMoneyCustomize
     * @return
     */
    List<CouponBackMoneyCustomize> exportCouponBackMoneyTYHjhList(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 
     * 导出加息券回款列表
     * @author pcc
     * @param couponBackMoneyCustomize
     * @return
     */
    List<CouponBackMoneyCustomize> exportCouponBackMoneyJXHjhList(CouponBackMoneyCustomize couponBackMoneyCustomize);
    
    /**
     * 获取代金券列表记录数
     * 
     * @param CouponConfigCustomize
     * @return
     */
    Integer countCouponBackMoneyDJHjh(CouponBackMoneyCustomize couponBackMoneyCustomize);
    
    /**
     * 获取代金券列表
     * 
     * @param CouponConfigCustomize
     * @return
     */
    List<CouponBackMoneyCustomize> selectCouponBackMoneyDJHjhList(CouponBackMoneyCustomize couponBackMoneyCustomize);
    
    /**
     * 
     * 导出代金券回款列表
     * @author pcc
     * @param couponBackMoneyCustomize
     * @return
     */
    List<CouponBackMoneyCustomize> exportCouponBackMoneyDJHjhList(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 
     * 加息券回款总收益
     * @author pcc
     * @param couponBackMoneyCustomize
     * @return
     */
    String queryRecoverInterestTotleHjh(CouponBackMoneyCustomize couponBackMoneyCustomize);
    /**
     * 
     * 计算汇直投优惠券回款应回款金额
     * @author pcc
     * @param couponBackMoneyCustomize
     * @return
     */
    String queryInvestTotalHjh(CouponBackMoneyCustomize couponBackMoneyCustomize);

}