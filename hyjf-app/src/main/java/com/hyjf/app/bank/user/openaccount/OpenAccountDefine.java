package com.hyjf.app.bank.user.openaccount;

import com.hyjf.app.BaseDefine;

/**
 * Created by yaoyong on 2017/12/8.
 */
public class OpenAccountDefine extends BaseDefine{

    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/user";
    /** @RequestMapping值 */
    public static final String BANKOPEN_SENDCODE_ACTION = "/open/smscode";
    /** 跳转开户页 */
    public static final String BANKOPEN_OPEN_ACTION = "/open";

    public static final String BANKOPEN_OPEN_SUCESS_ACTION = "/openSuccess";

    /** 开户页面请求地址*/
    public static final String BANKOPEN_OPEN_ACCOUNT_ACTION = "/open";

    /** 开户协议 */
    public static final String JX_BANK_SERVICE_ACTION = "/bankopen/jxBankService";
    public static final String JX_BANK_SERVICE_PATH = "/open/open_contract";

    /** 开户成功画面路径 */
    public static final String BANKOPEN_OPEN_SUCCESS_PATH = "/open/open_success";
    /** 开户失败画面路径*/
    public static final String BANKOPEN_OPEN_ERROR_PATH = "/open/open_error";

    public static final String STATUSDESC = "statusDesc";


    public static final String MOBILE = "phone";
    public static final String SIGN = "sign";
    public static final String TOKEN = "token";

    /** 开户成功跳转设置密码 */
    public static final String BANK_PASSWORD_URL = "/bank/user/transpassword/setPassword";
    
    /** 同步@RequestMapping值 */
    public static final String RETURL_SYN_ACTION = "/bankOpenReturn";
    /** 异步@RequestMapping值 */
    public static final String RETURL_ASY_ACTION = "/bankOpenBgreturn";
    
    //合规接口改造 start
    /** 页面开户验证失败*/
    public static final String JUMP_HTML_FAILED_PATH = "/user/open/result/failed";
    /** 页面开户处理中*/
    public static final String JUMP_HTML_HANDLING_PATH = "/user/open/result/handling";
    /** 页面开户成功*/
    public static final String JUMP_HTML_SUCCESS_PATH = "/user/open/result/success";
    /**跳转到设置叫*/
    public static final String SET_PAWWWORD_URL = "setPwdUrl";

    /** 企业用户开户 */
    public static final String ENTERPRISEGUIDE = "/enterpriseguide";
    //合规接口改造 end
}
