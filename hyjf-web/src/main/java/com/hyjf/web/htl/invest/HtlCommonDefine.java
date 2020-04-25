/**
 * Description:出借常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 * Created at: 2015年12月4日 下午2:33:39
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.web.htl.invest;

import com.hyjf.web.BaseDefine;

public class HtlCommonDefine  extends BaseDefine{

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/htl";
	
	/** @RequestMapping值 */
	public static final String REQUEST_WECHART_MAPPING = "/htl/wechart";
	
	/** @RequestMapping值 */
	public static final String REQUEST_APP_MAPPING = "/htl/app";
	/** 购买汇天利 */
	public static final String BUY_PRODUCT_MAPPING = "/buyProduct";
	/** 购买汇天利 */
	public static final String BUY_PRODUCT_APP_MAPPING = "/buyProductApp";
	/** 赎回汇天利 */
	public static final String REDEEM_PRODUCT_MAPPING = "/redeemProduct";
	/** 赎回汇天利 */
	public static final String REDEEM_PRODUCT_APP_MAPPING = "/redeemProductApp";
	
	/** 获得汇天利转入记录*/
	public static final String PRODUCT_LIST_RECORD_MAPPING = "/getProductListRecords";

	/** 获得汇天利转出记录 */
	public static final String PRODUCT_REDEEM_RECORD_MAPPING = "/getProductRedeemRecords";
	/** 获得汇天利利息明细*/
	public static final String PRODUCT_INTEREST_RECORD_MAPPING = "/getProductInterestRecords";

	/** 获得汇天利利息明细*/
	public static final String PRODUCT_USER_INFO_MAPPING = "/getUserProdcutInfo";

	/** 获得汇天利利息明细*/
	public static final String PRODUCT_USER_REDEEMINTEREST_MAPPING = "/getUserRedeemInterest";

	/** 汇天利转入转出check */
	public static final String CHECK_MAPPING = "/check";
	/**汇付接口返回地址*/
    public static final String REDEEM_RETURN_MAPPING = "/redeemProductReturn.do";
	/**汇付接口返回地址*/
    public static final String BUY_RETURN_MAPPING = "/buyProductReturn.do";

    /** JSP 汇付天下跳转画面 */
    public static final String JSP_CHINAPNR_RESULT = "/chinapnr/chinapnr_result";
    /** JSP 回调画面 */
    public static final String JSP_CHINAPNR_RECEIVE = "/chinapnr/chinapnr_receive";
    /** JSP 回调画面 */
    public static final String JSP_CHINAPNR_SEND = "/chinapnr/chinapnr_send";
    
    
    /**-------------------------------新增----------------------------------------- */
    
	/** 汇天利页面展示form*/
	public static final String FORM = "htlForm";
	/** 汇天利信息*/
	public static final String HTLINFO_MAPPING = "getHtlInfo";
	/** 汇天利信息页面*/
	public static final String HTLINFO_PAGE = "htl/huitianli";
	/** 汇天利出借*/
	public static final String HTLINVEST_MAPPING = "moveToInvestPage";
	/** 汇天利出借页面*/
	public static final String HTLINVEST_PAGE = "htl/huitianli_invest";
	/** 登陆页面*/
	public static final String LOGIN_PAGE = "user/login/login";
	/** 汇天利出借成功页面*/
	public static final String INVEST_SUCCESS_PAGE = "htl/invest_success";
	/** 汇天利出借失败页面*/
	public static final String INVEST_ERROR_PAGE = "htl/invest_error";
	/** 错误页面*/
	public static final String ERROR_PAGE = "error/systemerror";
	/** 汇天利详细页面*/
	public static final String HTL_USER_DETAIL_PAGE = "htl/huitianli_detail";
	/** 汇天利赎回*/
	public static final String HTLREDEEM_MAPPING = "moveToRedeemPage";
	/** 汇天利赎回页面*/
	public static final String HTLREDEEM_PAGE = "htl/huitianli_redeem";
	/** 汇天利赎回成功页面*/
	public static final String REDEEM_SUCCESS_PAGE = "htl/redeem_success";
	/** 汇天利赎回失败页面*/
	public static final String REDEEM_ERROR_PAGE = "htl/redeem_error";
}

	