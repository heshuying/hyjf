package com.hyjf.api.surong.user.recharge;

public class RechargeDefine {
	/** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/surong/recharge";
    
    /** 充值接口 */
    public static final String USER_RECHARGE_ACTION = "/userRecharge";
    /** 充值异步回调接口 */
    public static final String CALLBACK_MAPPING = "/callBack";
    /** 充值同步回调接口 */
    public static final String RETURN_MAPPING = "/return";
    
    public static final String SURONG_REQUEST_HOME = "/hyjf-api-web";
    // 充值状态
    
    /** 失败 */
    public static final Integer STATUS_FAIL = 0;

    /** 成功 */
    public static final Integer STATUS_SUCCESS = 1;

    /** 充值中 */
    public static final Integer STATUS_RUNNING = 2;
    
    /** 返回信息 */
    public static final String MESSAGE = "message";
    
    //页面
    
    /** 充值异常 */
    public static final String RECHARGE_ERROR = "/recharge_fail";

    /** 充值成功 */
    public static final String RECHARGE_SUCCESS = "/recharge_success";
    
    /** JSP 回调画面 */
    public static final String JSP_CHINAPNR_SEND = "/chinapnr/chinapnr_send";
    
    /** 充值中 */
    public static final String RECHARGE_PROCESSING = "/recharge_processing";
    
    /** 返回结果@RequestMapping值 */
	public static final String STATUS = "status";
	/** 成功结果@RequestMapping值 */
	public static final Boolean STATUS_TRUE = true;
} 
