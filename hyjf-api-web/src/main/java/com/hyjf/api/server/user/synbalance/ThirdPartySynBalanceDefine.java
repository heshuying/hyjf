/**
 * Description:出借常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年12月4日 下午2:33:39
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.api.server.user.synbalance;

import com.hyjf.base.bean.BaseDefine;

public class ThirdPartySynBalanceDefine extends BaseDefine {

    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/server/synbalance";
    /** 同步余额 @RequestMapping值*/
    public static final String SYNBALANCE_ACTION = "/synbalance";
    /** 错误页面 @Path值 */
    public static final String ERROR_PTAH = "error";
}