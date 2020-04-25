/**
 * Description:出借常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年12月4日 下午2:33:39
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.app.user.invest;

import com.hyjf.app.BaseDefine;

import java.math.BigDecimal;

public class InvestDefine extends BaseDefine {

    /** 用户出借 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/user/invest";

    /** 出借 @RequestMapping值 */
    public static final String INVEST_ACTION = "/tender";
    
    /** @RequestMapping值 */
    public static final String TENDER_URL_ACTION = "/getTenderUrl";

    /** 出借校验 @RequestMapping值 */
    public static final String INVEST_CHECK_ACTION = "/check";

    /** 出借后同步回调 @RequestMapping值 */
    public static final String RETURL_SYN_ACTION = "/returl";

    /** 出借后异步回调 @RequestMapping值 */
    public static final String RETURL_ASY_ACTION = "/bgreturl";
    
    /** JSP 汇付天下跳转画面 */
    public static final String JSP_CHINAPNR_SEND = "/chinapnr/chinapnr_send";

    /** 用户开户记录 */
    public static final String INVEST_SUCCESS_PATH = "/invest/invest_success";

    /** 更新开户信息 */
    public static final String INVEST_ERROR_PATH = "/invest/invest_error";

    /** log日志controller 类名 */
    public static final String THIS_CLASS = InvestController.class.getName();

    /** 出借 @RequestMapping值 */
    public static final String GET_INVEST_INFO_MAPPING = "/getInvestInfo";

    /** 获取协议 @RequestMapping值 */
    public static final String GODETAIL_MAPPING = "/goDetail";

    /** 获取出借信息 @Request值 */
    public static final String GET_INVEST_INFO = "/hyjf-app/user/invest/getInvestInfo";
    
    /** 获取协议 @RequestMapping值 */
    public static final String GODETAIL = "/hyjf-app/user/invest/goDetail";
    
    public static final BigDecimal MONTH = new BigDecimal("12");

    public static final BigDecimal DAY = new BigDecimal("360");
	
    public static final BigDecimal HUNDRED = new BigDecimal("100");
    
    public static final String DOU_HAO = ",";

    /** 历史回报文字描述常量 */
    public static final String PROSPECTIVE_EARNINGS = "历史回报 ";
    
    // add by zhangjp 优惠券出借 start
    /** 优惠券用户编号 */
    public static final String COUPON_GRANT_ID = "couponGrantId";
    /** 优惠券用户更新时间 排他校验用 */
    public static final String COUPON_UPDATE_TIME = "couponUpdateTime";
    /** 优惠券截止时间 */
    public static final String COUPON_END_TIME = "couponEndTime";
    // add by zhangjp 优惠券出借 start

    /** 汇计划 出借成功 失败 跳转 */
    public static final String HJH_TENDER_URL_ACTION = "/showHJHResult";
    
    
    /** 债转出借成功结果页*/
    public static final String JUMP_HTML_SUCCESS_PATH = "/transfer/{transferId}/result/success";
    /** 债转出借失败结果页*/
    public static final String JUMP_HTML_ERROR_PATH = "/transfer/{transferId}/result/failed";
    /** 债转出借中间结果页*/
    public static final String JUMP_HTML_HANDLING_PATH = "/transfer/{transferId}/result/handing";
    /** 汇计划出借失败结果页*/
    public static final String JUMP_HTML_HJH_FAILED_PATH = "/plan/{planId}/result/failed";
    /** 汇计划出借成功结果页*/
    public static final String JUMP_HTML_HJH_SUCCESS_PATH = "/plan/{planId}/result/success";
    /** app出借失败结果页*/
    public static final String JUMP_HTML_APP_FAILED_PATH = "/borrow/{borrowId}/result/failed";
    /** app出借成功结果页*/
    public static final String JUMP_HTML_APP_SUCCESS_PATH = "/borrow/{borrowId}/result/success";
    
    /** 汇计划出借处理中结果页*/
    public static final String JUMP_HTML_HJH_HANDLING_PATH = "/plan/{planId}/result/handing";

}
