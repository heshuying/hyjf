package com.hyjf.mybatis.mapper.customize.coupon;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.customize.coupon.CouponRecoverCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderDetailCustomize;

public interface CouponTenderCustomizeMapper {

	/**
	 * 获取优惠券列表
	 * 
	 * @param CouponConfigCustomize
	 * @return
	 */
	List<CouponTenderCustomize> selectCouponTenderList(CouponTenderCustomize couponTenderCustomize);
	/**
	 * 获取优惠券列表记录数
	 * 
	 * @param CouponConfigCustomize
	 * @return
	 */
	Integer countCouponTender(CouponTenderCustomize couponTenderCustomize);
	/**
     * 
     * 根据条件查询优惠券使用详情
     * @author pcc
     * @param detail
     * @return
     */
    CouponTenderDetailCustomize selectCouponTenderDetailCustomize(Map<String,Object> paramMap);
    /**
     * 
     * 返回回款列表
     * @author pcc
     * @param detail
     * @return
     */
    List<CouponRecoverCustomize> getCouponRecoverCustomize(Map<String,Object> paramMap);
    /**
     * 导出优惠券使用列表
     * 
     * @param CouponConfigCustomize
     * @return
     */
    List<CouponTenderCustomize> exportCouponTenderList(CouponTenderCustomize couponTenderCustomize);
    
    /**
     * 根据优惠券出借编号取得优惠券信息
     * @param tenderNid
     * @return
     */
    CouponConfig getCouponConfig(String tenderNid);
    
    /**
     * 根据出借编号取得使用的优惠券编号
     * @param tenderNid
     * @return
     */
    String getCouponUserCode(String tenderNid);
    
    /**
     * 
     * 计算汇直投优惠券使用列表真实出借金额总额
     * @author pcc
     * @param couponTenderCustomize
     * @return
     */
    String queryInvestTotalHzt(CouponTenderCustomize couponTenderCustomize);
    
    
    
    /***************************************************汇添金优惠券出借*************************************************/
    
    
    /**
     * 获取优惠券列表
     * 
     * @param CouponConfigCustomize
     * @return
     */
    List<CouponTenderCustomize> selectCouponTenderHtjList(CouponTenderCustomize couponTenderCustomize);
    /**
     * 获取优惠券列表记录数
     * 
     * @param CouponConfigCustomize
     * @return
     */
    Integer countCouponTenderHtj(CouponTenderCustomize couponTenderCustomize);
    /**
     * 
     * 根据条件查询优惠券使用详情
     * @author pcc
     * @param detail
     * @return
     */
    CouponTenderDetailCustomize selectCouponTenderHtjDetailCustomize(Map<String,Object> paramMap);
    /**
     * 
     * 返回回款列表
     * @author pcc
     * @param detail
     * @return
     */
    List<CouponRecoverCustomize> getCouponRecoverHtjCustomize(Map<String,Object> paramMap);
    /**
     * 导出优惠券使用列表
     * 
     * @param CouponConfigCustomize
     * @return
     */
    List<CouponTenderCustomize> exportCouponTenderHtjList(CouponTenderCustomize couponTenderCustomize);
    
    /**
     * 根据优惠券出借编号取得优惠券信息
     * @param tenderNid
     * @return
     */
    CouponConfig getCouponConfigHtj(String tenderNid);
    
    /**
     * 根据出借编号取得使用的优惠券编号
     * @param tenderNid
     * @return
     */
    String getCouponUserCodeHtj(String tenderNid);

    /**
     * 
     * 计算优惠券使用列表优惠券收益金额总额
     * @author pcc
     * @param couponTenderCustomize
     * @return
     */
    String queryRecoverAccountTotalHtj(CouponTenderCustomize couponTenderCustomize);
    
    
    
    /********************************汇计划相关*********************************/
    
    /**
     * 获取优惠券列表
     * 
     * @param CouponConfigCustomize
     * @return
     */
    List<CouponTenderCustomize> selectCouponTenderHjhList(CouponTenderCustomize couponTenderCustomize);
    /**
     * 获取优惠券列表记录数
     * 
     * @param CouponConfigCustomize
     * @return
     */
    Integer countCouponTenderHjh(CouponTenderCustomize couponTenderCustomize);
    /**
     * 
     * 根据条件查询优惠券使用详情
     * @author pcc
     * @param detail
     * @return
     */
    CouponTenderDetailCustomize selectCouponTenderDetailHjhCustomize(Map<String,Object> paramMap);
    /**
     * 
     * 返回回款列表
     * @author pcc
     * @param detail
     * @return
     */
    List<CouponRecoverCustomize> getCouponRecoverCustomizeHjh(Map<String,Object> paramMap);
    /**
     * 导出优惠券使用列表
     * 
     * @param CouponConfigCustomize
     * @return
     */
    List<CouponTenderCustomize> exportCouponTenderHjhList(CouponTenderCustomize couponTenderCustomize);
    
    /**
     * 根据优惠券出借编号取得优惠券信息
     * @param tenderNid
     * @return
     */
    CouponConfig getCouponConfigHjh(String tenderNid);
    
    /**
     * 根据出借编号取得使用的优惠券编号
     * @param tenderNid
     * @return
     */
    String getCouponUserCodeHjh(String tenderNid);
    
    /**
     * 
     * 计算汇直投优惠券使用列表真实出借金额总额
     * @author pcc
     * @param couponTenderCustomize
     * @return
     */
    String queryInvestTotalHjh(CouponTenderCustomize couponTenderCustomize);

    /**
     * 合规数据上报 CERT add by nxl 根据订单号查询优惠券信息
     * @param couponTenderId
     * @return
     */
    CouponTenderCustomize selectBorrowTenderCpnByOrderId(String couponTenderId);

    /**
     * 合规数据上报 CERT add by nxl 根据订单号查询优惠券利息
     * @param couponTenderId
     * @return
     */
    String sunRecoverInterest(String couponTenderId);
}