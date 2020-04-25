package com.hyjf.app.bank.user.openaccountbg;

import com.hyjf.app.BaseDefine;

/**
 * Created by yaoyong on 2017/12/8.
 */
public class OpenAccountDefine extends BaseDefine{


    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/user/bg/";
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
}
