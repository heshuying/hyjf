/**
 * Description:提现常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: gogtz-T
 * @version: 1.0
 * Created at: 2015年12月4日 下午2:33:39
 * Modification History:
 * Modified by :
 */

package com.hyjf.vip.apply;

import com.hyjf.base.bean.BaseDefine;



public class ApplyDefine extends BaseDefine {

    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/vip/apply";
    /** 用户登录请求 */
    public static final String LOGIN_REQUEST_MAPPING = "/user/login/init.do";
    /** 用户开户请求 */
    public static final String OPEN_ACCOUNT_REQUEST_MAPPING = "/user/openaccount/init.do";
    /** 用户充值请求 */
    public static final String RECHARGE_REQUEST_MAPPING = "/recharge/rechargePage.do";
    /** vip申请请求 跳转汇付url */
    public static final String VIP_APPLY_ACTION_MAPPING = "/vip/apply/vipApply.do";
	/** 画面初始 */
    public static final String INIT = "/init";
    /** 购买前校验 */
    public static final String APPLY_CHECK = "/applyCheck";
    /** VIP申请 */
    public static final String VIP_APPLY_ACTION = "/vipApply";
    /** VIP申请回调 */
    public static final String VIP_APPLY_RETURN_ACTION = "/vipApplyReturn";
    /** VIP申请异步回调 */
    public static final String VIP_APPLY_RETURN_ASYN_ACTION = "/vipApplyReturnAsyn";
    
    /** 画面跳转url */
    /** vip请求页面url */
    public static final String APPLY = "vip/vipApply/apply";
    /** 支付失败url */
    public static final String APPLY_FAIL = "vip/vipApply/applyFail";
    /** 支付成功url */
    public static final String APPLY_SUCCESS = "vip/vipApply/applySuccess";
    /** V值 */
    public static final Integer INT_ZERO = 0;
    /** VIP1 */
    public static final Integer INT_ONE = 1;
}
