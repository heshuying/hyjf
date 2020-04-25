package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.auto.ProductInfo;
import com.hyjf.mybatis.model.customize.ProductInfoCustomize;
import com.hyjf.mybatis.model.customize.ProductSearchForPage;

public interface HtlCommonCustomizeMapper {

	/**
	 * 获取查询出汇天利每日报表
	 * @param htlUserInfo
	 * @return
	 */
	//购买表读取 in_count ,in_amount 
	ProductInfo getCreateProductInfo_productlist(ProductInfoCustomize productInfo);
	//赎回表读取out_count,out_amount
	ProductInfo getCreateProductInfo_productredeem(ProductInfoCustomize productInfo);
	//资金表读取loan_balance
	ProductInfo getCreateProductInfo_account(ProductInfoCustomize productInfo);
	//收益表读取out_interest
	ProductInfo getCreateProductInfo_productinterest(ProductInfoCustomize productInfo);
	//购买表读取invest_amount
	ProductInfo getCreateProductInfo_restamount(ProductInfoCustomize productInfo);
	/**
	 * 获取赎回记录信息（页面接口）
	 * @param productSearchForPage
	 * @return
	 */
	List<ProductSearchForPage>  selectRedeemRecordPage(ProductSearchForPage productSearchForPage);
	/**
	 *  获取收益记录信息（页面接口）
	 * @return
	 */
	List<ProductSearchForPage> selectInterestRecordPage(ProductSearchForPage productSearchForPage);
	/**
	 *  获取购买记录信息（页面接口）
	 * @return
	 */
	List<ProductSearchForPage> selectBuyRecordPage(ProductSearchForPage productSearchForPage);
	/**
	 *  获取用户已提取收益
	 * @return
	 */
	ProductSearchForPage selectUserAlreadyInterest(ProductSearchForPage productSearchForPage);
	/**
	 *  获取用户本金
	 * @return
	 */
	ProductSearchForPage selectUserPrincipal(ProductSearchForPage productSearchForPage);
	/**
	 * 获取未赎回记录
	 * @param productSearchForPage
	 * @return
	 */
	List<ProductSearchForPage> selectUserNotRedeemRecord(ProductSearchForPage productSearchForPage);
	
	/**
	 * 获取赎回记录数
	 * @param productSearchForPage
	 * @return
	 */
	Integer countRedeemRecordPage(ProductSearchForPage productSearchForPage);
	/**
	 * 获取利息记录数
	 * @param productSearchForPage
	 * @return
	 */
	Integer countInterestRecordPage(ProductSearchForPage productSearchForPage);
	/**
	 * 获取购买记录数
	 * @param productSearchForPage
	 * @return
	 */
	Integer countBuyRecordPage(ProductSearchForPage productSearchForPage);
	
}