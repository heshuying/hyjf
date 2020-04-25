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

package com.hyjf.activity.mgm10.prizedraw;

import com.hyjf.base.bean.BaseDefine;

public class PrizeDrawDefine extends BaseDefine {

    /** 用户抽奖 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/prizedraw";
    /** 获取奖品列表 @RequestMapping值*/
    public static final String GET_PRIZELIST = "/getPrizeDrawList";
    /** 兑奖  @RequestMapping值*/
    public static final String DO_PRIZE_DRAW = "/doPrizeDraw";
}
