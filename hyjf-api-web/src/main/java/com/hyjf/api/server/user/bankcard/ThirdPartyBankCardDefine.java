package com.hyjf.api.server.user.bankcard;
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


public class ThirdPartyBankCardDefine  {

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/server/user/bankcard";
	/** @RequestMapping值 */
    public static final String BIND_CARD_RETURN = "/bindCardReturn";
    /** @RequestMapping值 */
    public static final String BIND_CARD_NOTIFY_RETURN = "/bindCardNotifyReturn";
	  /** chinapnrForm值 */
    public static final String CHINAPNR_FORM = "chinapnrForm";
    
    
    
    
    /** @RequestMapping值 */
	public static final String BIND_CARD = "/userBindCard";
	
	/** @RequestMapping值 */
    public static final String DELETE_CARD = "/userDeleteCard";

    /** JSP 汇付天下跳转画面 */
    public static final String JSP_CHINAPNR_RESULT = "/chinapnr/chinapnr_result";
    /** JSP 回调画面 */
    public static final String JSP_CHINAPNR_RECEIVE = "/chinapnr/chinapnr_receive";
    /** JSP 回调画面 */
    public static final String JSP_CHINAPNR_SEND = "/chinapnr/chinapnr_send";

    /** ceshiURL */
    public static final String PROP_URL = "hyjf.chinapnr.url";
    
    //回调URL
    public static final String BINDCARD_SUCCESS_PATH="/bindcard/bindcard_success";
    public static final String BINDCARD_ERROR_PATH="/bindcard/bindcard_fail";
    
    
    /** 江西银行 发送短信码 */
    public static final String SEND_PLUS_CODE_ACTION = "/sendPlusCode";
    /** 江西银行绑定银行卡增强接口 */
    /** @RequestMapping值 */
    public static final String BIND_CARD_PLUS = "/userBindCardPlus";
}



