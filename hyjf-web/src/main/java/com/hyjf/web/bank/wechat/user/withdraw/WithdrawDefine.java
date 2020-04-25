/**
 * Description:提现常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: gogtz-T
 * @version: 1.0
 * Created at: 2015年12月4日 下午2:33:39
 * Modification History:
 * Modified by :
 */

package com.hyjf.web.bank.wechat.user.withdraw;

import com.hyjf.web.BaseDefine;

public class WithdrawDefine extends BaseDefine {
	
	public static final String CONTROLLER_NAME = "WechatWithdrawController";
    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/wechat/user/withdraw";
    /** @RequestMapping值 */
    public static final String CHECK_MAPPING = "/check";
    /** @RequestMapping值 */
    public static final String CASH_MAPPING = "/cash";
    /** @RequestMapping值 */
    public static final String RETURN_MAPPING = "/return";
    /** @RequestMapping值 */
    public static final String CALLBACK_MAPPING = "/callback";

}
