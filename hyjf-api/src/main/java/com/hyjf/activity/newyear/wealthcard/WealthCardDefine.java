package com.hyjf.activity.newyear.wealthcard;

import com.hyjf.base.bean.BaseDefine;

public class WealthCardDefine extends BaseDefine{
    
    // 通知短信模板
    public static final String TPL_SMS_NEWYEAR_CAIFUKA = "newyear_caishenka";
    
    // 新年活动A
    public static final int ACTIVITY_FLG_NEWYEAR_A = 1;
    // 新年活动B
    public static final int ACTIVITY_FLG_NEWYEAR_B = 2;
    
    // 财神卡类别 1：金，2：鸡，3：纳，4：福，5：金鸡纳福
    public static final int CARD_TYPE_JIN = 1;
    public static final int CARD_TYPE_JI = 2;
    public static final int CARD_TYPE_NA = 3;
    public static final int CARD_TYPE_FU = 4;
    public static final int CARD_TYPE_JJNF = 5;
    
    // 卡片来源,1：邀请好友，2：赠送好友，3：活动抽奖，4：出借，5：好友赠送, 6：注册开户
    public static final int CARD_SOURCE_INVITE = 1;
    public static final int CARD_SOURCE_GIVE = 2;
    public static final int CARD_SOURCE_PRIZEDRAW = 3;
    public static final int CARD_SOURCE_INVEST = 4;
    public static final int CARD_SOURCE_GET = 5;
    public static final int CARD_SOURCE_REGIST = 6;
    
    // 操作类别,1：获取，2：使用
    public static final int OPERATE_TYPE_GET = 1;
    public static final int OPERATE_TYPE_USE = 2;

    public static final String REQUEST_MAPPING = "wealthcard";
    
    /**
     * 获取用户财富卡个数
     */
    public static final String GET_USERCARD_COUNT = "getUserCardCount";
    
    /**
     * 抽奖
     */
    public static final String DO_PRIZE_DRAW = "doPrizeDraw";
    
    /**
     * 手机号码校验
     */
    public static final String DO_PHONENUM_CHECK = "doPhoneNumCheck";
    
    /**
     * 发送财富卡
     */
    public static final String DO_CARD_SEND = "doCardSend";
}
