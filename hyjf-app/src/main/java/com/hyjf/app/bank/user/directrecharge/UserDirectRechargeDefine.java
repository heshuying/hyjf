package com.hyjf.app.bank.user.directrecharge;

import com.hyjf.app.BaseDefine;

public class UserDirectRechargeDefine extends BaseDefine{

    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/bank/user";
    /** h5跳转到我们这 */
    public static final String USER_DIRECT_RECHARGE_ACTION = "/userDirectRecharge/recharge";

    /** 同步@RequestMapping值 */
    public static final String RETURL_SYN_ACTION = "/directRechargeReturn";
    /** 异步@RequestMapping值 */
    public static final String RETURL_ASY_ACTION = "/directRechargeBgreturn";
    
    //合规接口改造 start
    /** 充值验证失败*/
    public static final String JUMP_HTML_FAILED_PATH = "/user/bank/recharge/result/failed";
    /** 充值处理中*/
    public static final String JUMP_HTML_HANDLING_PATH = "/user/bank/recharge/result/handling";
    /** 充值成功*/
    public static final String JUMP_HTML_SUCCESS_PATH = "/user/bank/recharge/result/success";

    //合规接口改造 end
}
