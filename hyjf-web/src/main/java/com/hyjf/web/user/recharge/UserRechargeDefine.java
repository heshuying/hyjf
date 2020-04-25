/**
 * Description:出借常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 *           Created at: 2015年12月4日 下午2:33:39
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.web.user.recharge;

import com.hyjf.web.BaseDefine;

public class UserRechargeDefine extends BaseDefine {
    
    /** @RequestMapping值 */
    public static final String RECHARGEINFO_MAPPING = "/rechargeInfo";
    
    /** @RequestMapping值 */
    public static final String RECHARGEPAGE_MAPPING = "/rechargePage";

    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/recharge";

    /** 获取银行卡充值手续费 */
    public static final String GET_FEE = "/getFee";

    /** @RequestMapping值 */
    public static final String CHECK_MAPPING = "/check";

    /** @RequestMapping值 */
    public static final String RECHARGE_MAPPING = "/recharge";

    public static final String NETSAVE_MAPPING = "/netsave";

    /** @RequestMapping值 */
    public static final String RETURN_MAPPING = "/return";

    /** @RequestMapping值 */
    public static final String CALLBACK_MAPPING = "/callback";

    /** chinapnrForm值 */
    public static final String CHINAPNR_FORM = "chinapnrForm";

    /** JSP 汇付天下跳转画面 */
    public static final String JSP_CHINAPNR_RESULT = "/chinapnr/chinapnr_result";

    /** JSP 回调画面 */
    public static final String JSP_CHINAPNR_RECEIVE = "/chinapnr/chinapnr_receive";

    /** JSP 回调画面 */
    public static final String JSP_CHINAPNR_SEND = "/chinapnr/chinapnr_send";
    
    /** JSP 回调画面 */
    public static final String JSP_RECHARGE_PAGE = "/user/recharge/recharge";

    /** ceshiURL */
    public static final String PROP_URL = "hyjf.chinapnr.url";

    public static final String RECHARGETYPE_0 = "0";// 个人网银充值

    public static final String RECHARGETYPE_1 = "1";// 企业网银充值

    public static final String RECHARGETYPE_2 = "2";// 快捷充值

    // 充值状态
    /** 失败 */
    public static final Integer STATUS_FAIL = 0;

    /** 成功 */
    public static final Integer STATUS_SUCCESS = 1;

    /** 充值中 */
    public static final Integer STATUS_RUNNING = 2;

    /** 充值成功页面 */
    public static final String RECHARGE_SUCCESS = "/user/recharge/recharge_success";

    /** 充值中页面 */
    public static final String RECHARGE_PROCESSING = null;

    /**  */
    public static final String MESSAGE = "message";

    /** 充值失败页面 */
    public static final String RECHARGE_ERROR = "/user/recharge/recharge_error";


}
