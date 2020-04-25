package com.hyjf.api.server.user.unbindcardpage;

import com.hyjf.api.server.user.encryptpage.BankOpenEncryptPageServer;

/**
 * 解绑银行卡(页面调用)合规四期
 */


public class UnbindCardPageDefine {

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/server/user/unbindCardPage";
	/** @RequestMapping值 */
    public static final String DELETE_CARD = "/deleteCardPage";

    /**错误页面*/
    public static final String PATH_OPEN_ACCOUNT_PAGE_ERROR = "/bank/user/trusteePay/error";

    /** 类名 */
    public static final String THIS_CLASS = BankOpenEncryptPageServer.class.getName();
    /** 同步回调地址 @RequestMapping值 */
    public static final String RETURL_SYN_ACTION = "/return";
    /** 异步回调地址 @RequestMapping值 */
    public static final String RETURL_ASY_ACTION = "/notifyReturn";

}



