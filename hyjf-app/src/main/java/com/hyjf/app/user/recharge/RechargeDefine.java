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
package com.hyjf.app.user.recharge;

import com.hyjf.app.BaseDefine;

@Deprecated
public class RechargeDefine extends BaseDefine {

    /** 指定类型的项目 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/recharge";

    /** @RequestMapping值 */
    public static final String GET_QP_RECHARGE_INFO_ACTION = "/getQpRechargeInfo";

    /** @RequestMapping值 */
    public static final String GET_RECHARGE_URL_ACTION = "/getRechargeUrl";

    /** @RequestMapping值 */
    public static final String USER_RECHARGE_ACTION = "/userRecharge";

    /** 充值规则映射@RequestMapping值 */
    public static final String RECHARGE_RULE_MAPPING = "/rechargeRule";

    /** @RequestMapping值 */
    public static final String GET_QP_RECHARGE_INFO = "/hyjf-app/recharge/getQpRechargeInfo";

    /** @RequestMapping值 */
    public static final String GET_RECHARGE_URL = "/hyjf-app/recharge/getRechargeUrl";
    
    /** @RequestMapping值 */
    public static final String RECHARGE_RULE_URL = "/hyjf-app/recharge/rechargeRule";

    public static final String FEE = "充值手续费：";

    public static final String BALANCE = "实际到账：";

    public static final String RECHARGE_INFO_SUFFIX = "元；";

    /** JSP 回调画面 */
    public static final String JSP_CHINAPNR_SEND = "/chinapnr/chinapnr_send";

    public static final String RETURN_MAPPING = "/return";

    public static final String CALLBACK_MAPPING = "/callBack";

    /** 返回信息 */
    public static final String MESSAGE = "message";

    // 充值状态
    /** 失败 */
    public static final Integer STATUS_FAIL = 0;

    /** 成功 */
    public static final Integer STATUS_SUCCESS = 1;

    /** 充值中 */
    public static final Integer STATUS_RUNNING = 2;

    /** 充值异常 */
    public static final String RECHARGE_ERROR = "/recharge_fail";

    /** 充值成功 */
    public static final String RECHARGE_SUCCESS = "/recharge_success";

    /** 充值规则页面 */
    public static final String RECHARGE_RULE = "/recharge";

    /** 充值中 */
    public static final String RECHARGE_PROCESSING = "/recharge_processing";

 

}
