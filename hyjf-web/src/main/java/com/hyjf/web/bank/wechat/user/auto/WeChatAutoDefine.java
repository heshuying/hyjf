/**
 * 设置交易密码
 */
package com.hyjf.web.bank.wechat.user.auto;

import com.hyjf.web.BaseDefine;

public class WeChatAutoDefine extends BaseDefine {
    

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/wechat/user/auto";
	/** 用户授权自动出借 */
    public static final String USER_AUTH_INVES_ACTION = "/userAuthInves";
    /** 用户授权自动出借状态查询 */
    public static final String GET_USER_AUTO_STATUS_BY_USERID_ACTION = "/getUserAutoStatusByUserId";
    
    
    /** 用户授权自动出借同步回调 */
    public static final String USER_AUTH_INVES_RETURN_ACTION = "/userAuthInvesReturn";
    /** 用户授权自动出借异步回调 */
    public static final String USER_AUTH_INVES_BGRETURN_ACTION = "/userAuthInvesBgreturn";
    
}
