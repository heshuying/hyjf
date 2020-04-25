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

package com.hyjf.activity.mgm10.prizechange;

import com.hyjf.base.bean.BaseDefine;

public class PrizeChangeDefine extends BaseDefine {

    /** 用户兑奖 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/prizechange";
    /** 获取奖品列表 @RequestMapping值*/
    public static final String GET_PRIZELIST = "/getPrizeChangeList";
    /** 兑奖  @RequestMapping值*/
    public static final String DO_PRIZE_CHANGE = "/doPrizeChange";
    /** 兑奖条件校验  @RequestMapping值*/
    public static final String PRIZE_CHANGE_CHECK = "/prizeChangeCheck";
}
