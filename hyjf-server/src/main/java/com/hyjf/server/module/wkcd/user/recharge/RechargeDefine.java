/**
 * Description:获取指定的项目类型的项目常量定义
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年11月11日 下午2:17:31
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.server.module.wkcd.user.recharge;

import com.hyjf.server.BaseDefine;

public class RechargeDefine extends BaseDefine {

    /** 指定类型的项目 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/user/recharge";

    /** @RequestMapping值 */
    public static final String GET_QP_RECHARGE_INFO_ACTION = "/getQpRechargeInfo";

    /** @RequestMapping值 */
    public static final String GET_RECHARGE_URL_ACTION = "/getRechargeUrl";

    /** @RequestMapping值 */
    public static final String USER_RECHARGE_ACTION = "/rechargeAction";


    public static final String FEE = "充值手续费：";

    public static final String BALANCE = "实际到账：";

    public static final String RECHARGE_INFO_SUFFIX = "元；";

    /** JSP 回调画面 */
    public static final String JSP_CHINAPNR_SEND = "/chinapnr/chinapnr_send";

    public static final String RETURN_MAPPING = "/return";

    public static final String CALLBACK_MAPPING = "/callBack";

    /** 返回信息 */
    public static final String MESSAGE = "message";
    
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

    /** 充值返回页面 */
    public static final String RECHARGE_JSP = "/server/serverReturn";

}
