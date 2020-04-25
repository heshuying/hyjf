package com.hyjf.activity.actdoubleeleven.fightluck;

import com.hyjf.base.bean.BaseDefine;

public class FightLuckDefine extends BaseDefine {

    /** 拼手气活动 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/fightluck";
    /** 初始化接口 @RequestMapping值 */
    public static final String INIT_ACTION = "/init";
    /** 抢优惠券接口 @RequestMapping值 */
    public static final String GRAB_COUPONS_ACTION = "/grabCoupons";
    /** 返回拼手气获奖名单 @RequestMapping值 */
    public static final String GET_FIGHT_LUCK_WINNERS_LIST_ACTION = "/getFightLuckWinnersList";
   
    /** 活动类型  */
    public static final Integer ACT_TYPE = 4;
}
