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

package com.hyjf.web.user.invest;

import com.hyjf.web.BaseDefine;

public class InvestDefine extends BaseDefine {

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/user/invest";
	/** @RequestMapping值 */
	public static final String INVEST_ACTION = "/invest";
	/** @RequestMapping值 */
	public static final String APPOINTMENT_ACTION = "/appointment";
	/** @RequestMapping值 */
	public static final String APPOINT_CONTRACT_ACTION = "/contract";
	/** @RequestMapping值 */
	public static final String APPOINT_CONTRACTPDF_ACTION = "/contractPdf";
	/** @RequestMapping值 */
	public static final String INVEST_INFO_ACTION="/investInfo";
	/** @RequestMapping值 */
	public static final String INVEST_CHECK_ACTION = "/investCheck";
	/** @RequestMapping值 */
	public static final String APPOINT_CHECK_ACTION = "/appointCheck";
	/** 出借后同步回调 @RequestMapping值 */
	public static final String RETURL_SYN_ACTION = "/returnUrl";
	/** 出借后异步回调 @RequestMapping值 */
	public static final String RETURL_ASY_ACTION = "/bgreturnUrl";
	/** 出借成功@RequestMapping值 */
	public static final String INVEST_SUCCESS_PATH = "/user/invest/investSuccess";
	/** 出借失败@RequestMapping值 */
	public static final String INVEST_ERROR_PATH = "/user/invest/investError";

	/** chinapnrForm值 */
	public static final String CHINAPNR_FORM = "chinapnrForm";
	
	/** @RequestMapping值 */
	public static final String INVEST_URL_ACTION = "/tenderUrl";
	
	/** @RequestMapping值 */
	public static final String GET_INVEST_INFO_MAPPING = "/getInvestInfo";

	/** JSP 汇付天下跳转画面 */
	public static final String JSP_CHINAPNR_RESULT = "/chinapnr/chinapnr_result";
	/** JSP 回调画面 */
	public static final String JSP_CHINAPNR_RECEIVE = "/chinapnr/chinapnr_receive";
	/** JSP 回调画面 */
	public static final String JSP_CHINAPNR_SEND = "/chinapnr/chinapnr_send";

	/** @RequestMapping值 */
	public static final String APP_REQUEST_MAPPING = "/appInvest";
	/** @RequestMapping值 */
	public static final String WECHAT_REQUEST_MAPPING = "/weChatInvest";
	/** @RequestMapping值 */
	public static final String WECHAT_INVEST_ACTION = "/weChatTender";
	/** @RequestMapping值 */
	public static final String WECHAT_INVEST_URL_ACTION = "/weChatTenderUrl";
	
	public static final String DOU_HAO = ",";

}
