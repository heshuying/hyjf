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

package com.hyjf.web.user.rechargefee;

import com.hyjf.web.BaseDefine;

public class RechargeFeeDefine extends BaseDefine {
    
    /** @RequestMapping */
    public static final String REQUEST_MAPPING = "/rechargeFee";
	
    /** 页面初始化 */
    public static final String PAGE_MAPPING = "/rechargeFeePage";
    
    /** 页面初始化 */
    public static final String RECHARGE_FEE_LIST_ACTION = "/rechargeFeeList";

    /** 校验 */
    public static final String CHECK_ACTION = "/check";
    
    /** 用户付款 */
    public static final String PAY_ACTION = "/rechargeFeePay";
    
    /** 用户付款 异步回调*/
    public static final String PAY_RETURN_BG_ACTION = "/rechargeFeePayReturnBg.do";
    
    /** 用户付款 同步回调*/
    public static final String PAY_RETURN_ACTION = "/rechargeFeePayReturn.do";
    
    /** JSP 画面 */
    public static final String JSP_PAGE = "/user/rechargefee/rechargeFeeList";

    /** 成功页面 */
    public static final String PAGE_SUCCESS = "/user/rechargefee/success";

    /** 失败页面 */
    public static final String PAGE_ERROR = "/user/rechargefee/error";

    /** 展示消息 */
    public static final String MESSAGE = "message";

    /** 导出充值记录*/
    public static final String EXPORT_ACTION = "/exportAction";

}
