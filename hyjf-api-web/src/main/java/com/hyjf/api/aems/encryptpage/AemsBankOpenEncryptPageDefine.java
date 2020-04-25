package com.hyjf.api.aems.encryptpage;

import com.hyjf.base.bean.BaseDefine;

public class AemsBankOpenEncryptPageDefine extends BaseDefine {
	/** 外部服务接口:用户注册 @RequestMapping */
	public static final String REQUEST_MAPPING = "/aems/encryptpage";
	/** 用户开户 @RequestMapping */
	public static final String OPEN_ACCOUNT_ACTION = "/open";
	/** 同步回调  */
    public static final String RETURL_SYN_ACTION = "/openaccountReturn";
    /** 异步回调  */
    public static final String RETURL_ASY_ACTION = "/openaccountBgreturn";
	
	/** 类名 */
	public static final String THIS_CLASS = AemsBankOpenEncryptPageServer.class.getName();
	
	/**错误页面*/
    public static final String PATH_OPEN_ACCOUNT_PAGE_ERROR = "/bank/user/trusteePay/error";

}
