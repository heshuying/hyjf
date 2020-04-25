package com.hyjf.wechat.controller.user.directrecharge;

import com.hyjf.wechat.base.BaseDefine;

public class WxUserDirectRechargeDefine extends BaseDefine{

    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/wx/user";
    
    public static final String DIRECT_RECHARGE_ACCOUNT_ACTION = "/direct_recharge";

    /** 充值成功画面路径 */
    public static final String DIRECT_RECHARGE_SUCCESS_PATH = "/user/bank/recharge/result/success";
    /** 充值失败画面路径*/
    public static final String DIRECT_RECHARGE_ERROR_PATH = "/user/bank/recharge/result/failed";

    public static final String SIGN = "sign";
    public static final String TOKEN = "token";

    /** 同步@RequestMapping值 */
    public static final String RETURL_SYN_ACTION = "/directRechargeReturn";
    /** 异步@RequestMapping值 */
    public static final String RETURL_ASY_ACTION = "/directRechargeBgreturn";
    // 跳转
	public static final String JUMP_HTML = "/jumpHTML";
}
