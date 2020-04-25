package com.hyjf.wechat.controller.user.bankwithdraw;

import com.hyjf.wechat.base.BaseDefine;

/**
 * Created by cuigq on 2018/2/13.
 */
public class WxBankWIthdrawDefine extends BaseDefine{

    public static final String REQUEST_MAPPING = "/wx/bank/withdraw/";

    //提现银行信息准备（可提现金额、手续费、能否提现等）
    public static final String QUERY_WITHDRAW_INFO = "queryWithdrawInfo";

    //提现操作
    public static final String WITHDRAW_ACTION = "withdraw";

    // 提现成功页面返回
    public static final String WITHDRAW_SUCCESS = "/user/withdraw/result/success";

    // 提现失败页面返回
    public static final String WITHDRAW_FALSE = "/user/withdraw/result/failed";

    // 提现失败页面返回
    public static final String WITHDRAW_HANDING = "/user/withdraw/result/handing";
    // 结果页跳转处理页面
    public static final String JUMP_HTML = "/jumpHTML";

    // 返回前端对象标识
    public static final String RESULTBEAN = "callBackForm";

    public static final String RETURN_MAPPING = "return";
    public static final String CALLBACK_MAPPING = "callback";
    //发送短信验证码请求
    public static final String SEND_VERIFICATIONCODE_ACTION = "/sendVerificationCodeAction";
    /**提现短信模版类型*/
    public static final String TPL_SMS_WITHDRAW = "TPL_SMS_WITHDRAW";
    /** 更换手机号-绑定新手机号 */
	public static final String PARAM_TPL_BDYSJH = "TPL_BDYSJH";
	/** 短信验证码状态,新验证码 */
	public static final Integer CKCODE_NEW = 0;
	/** 短信验证码状态,已验 */
	public static final Integer CKCODE_YIYAN = 8;
	/** 验证验证码 */
	public static final String VALIDATE_VERIFICATIONCODE_ACTION = "/validateVerificationCodeAction";
	
    /** 发送验证码 */
	public static final String SEND_VERIFICATIONCODE_REQUEST = REQUEST_HOME + REQUEST_MAPPING
			+ SEND_VERIFICATIONCODE_ACTION;
	
	/** 验证验证码 */
	public static final String VALIDATE_VERIFICATIONCODE_REQUEST = REQUEST_HOME + REQUEST_MAPPING
			+ VALIDATE_VERIFICATIONCODE_ACTION;
	
}
