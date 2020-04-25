package com.hyjf.app.activity.activity68;

import com.hyjf.app.BaseDefine;

/**
 * 
 * 
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月25日
 * @see 下午2:40:11
 */
public class Activity68Define extends BaseDefine {

    /***/
    public static final String REQUEST_MAPPING = "/activity/activity68";
    /** 用户登录请求 */
    public static final String LOGIN_REQUEST_MAPPING = "/user/login/init.do";
    /** 用户出借请求 */
    public static final String PROJECT_REQUEST_MAPPING = "/project/initProjectList.do?projectType=HZT";
    /** 用户新手汇请求 */
    public static final String PROJECT_XSH_REQUEST_MAPPING = "/project/initProjectList.do?projectType=XSH";
    public static final String USER_COUPON_REQUEST_MAPPING = "/coupon/getUserCoupons.do";
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = Activity68Controller.class.getName();

    public static final String GET_USER_STATUS_ACTION = "getUserStatus";
    
    public static final String ACTIVITY68_PATH = "/act/activity68/reg-reward";
}
