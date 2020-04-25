/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2015年11月18日 下午12:34:08
 * Modification History:
 * Modified by : 
 */

package com.hyjf.web.wisdomtooth;

import com.hyjf.web.BaseDefine;

/**
 * @author Administrator
 */

public class WisdomToothDefine extends BaseDefine {


    public static final String CONTROLLOR_REQUEST_MAPPING = "/wisdomtooth";

    public static final String MODIFY_CODE = "/modifyCode";

    public static final String MODIFY_TEL_PHONE= "/modifyTelPhone";

    public static final String MODIFY_BUSINESS_CODE  = "/modifyBusinessCode";
    public static final String MIANPAGE = "/mainPage";
    public static final String CONTENT_HELPS = "/contentHelps";
    public static final String HELP = "/help";
    public static final String FIND_PASSWORD = "/findPassword";
    public static final String OFFLINE_RECHARGE = "/offlineRecharge";
    /* 智齿客服綫下充值*/
    public static final String  OFFLINE_RECHARGE_PAGE="/help/recharge-flow-path";
    /* 智齿客服修改绑定银行卡*/
    public static final String MODIFY_BANK_CIRD= "/modifyBankCard";
    public static final String  SERVICE_SOBOT="/help/service-sobot";
    /* 智齿客服修改密码跳转*/
    public static final String USER_SAFE_MODIFYCODE = "/user/safe/modifyCode.do";
    /* 智齿客服修改手机号跳转*/
    public static final String BANK_USER_TRANSPASSWORD_INITMOBILE = "/bank/user/transpassword/initMobile.do";
    /* 智齿客服找回密码跳转*/
    public static final String USER_FINDPASSWORD_INIT = "/user/findpassword/init.do";
    /* 智齿客服修改交易密码跳转*/
    public static final String BANK_USER_TRANSPASSWORD_RESETPASSWORD = "/bank/user/transpassword/resetPassword.do";

}
