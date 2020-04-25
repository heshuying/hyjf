package com.hyjf.server.module.wkcd.user.withdraw;

import com.hyjf.server.BaseDefine;

public class UserWithdrawDefine  extends BaseDefine {
    
    /** 统计类名 */
    public static final String THIS_CLASS = UserWithdrawController.class.getName();
    
    /** REQUEST_MAPPING */
    public static final String REQUEST_MAPPING = "/user/withdraw";
    
    /** 用户提现  */
    public static final String CASH_ACTION = "/cashAction";
    
    /** 可取现金额查询  */
    public static final String CASH_BALANCE = "/cashBalance";
    
    /** 银行卡信息查询接口  */
    public static final String GETACCOUNTBANKCARD = "/getAccountBankCard";
    
    /** @RequestMapping值 */
    public static final String RETURN_MAPPING = "/return";
    /** @RequestMapping值 */
    public static final String CALLBACK_MAPPING = "/callback";
    /** chinapnrForm值 */
    public static final String CHINAPNR_FORM = "chinapnrForm";
    
    /** @RequestMapping值 */
    public static final String RETURN_REQUEST= REQUEST_HOME + REQUEST_MAPPING + CASH_ACTION;
    
    /** 提现返回  路径 */
    public static final String WITHDRAW_JSP = "/server/serverReturn";
}
