package com.hyjf.api.aems.aemsunbindcardpage;


/**
 * 解绑银行卡(页面调用)合规四期
 */


public class AemsUnbindCardPageDefine {

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/aems/unbindCardPage";
	/** @RequestMapping值 */
    public static final String DELETE_CARD = "/deleteCardPage";

    /**错误页面*/
    public static final String PATH_OPEN_ACCOUNT_PAGE_ERROR = "/bank/user/trusteePay/error";

    /** 同步回调地址 @RequestMapping值 */
    public static final String RETURL_SYN_ACTION = "/return";
    /** 异步回调地址 @RequestMapping值 */
    public static final String RETURL_ASY_ACTION = "/notifyReturn";

}



