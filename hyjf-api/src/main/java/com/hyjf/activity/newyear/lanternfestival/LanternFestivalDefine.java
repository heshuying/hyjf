package com.hyjf.activity.newyear.lanternfestival;

import com.hyjf.base.bean.BaseDefine;

public class LanternFestivalDefine extends BaseDefine{
    
    
//    /hyjf-api-web/lanternfestival/getPresentRiddles
    public static final String REQUEST_MAPPING = "/lanternfestival";
    /**
     * 获取当天谜题信息
     */
    public static final String GET_PRESENT_RIDDLES = "/getPresentRiddles";
    
    /**
     * 返回今天用户是否答题表示
     */
    public static final String GET_TODAY_USER_ANSWER_FLAG = "/getTodayUserAnswerFlag";
    
    
    /**
     * 获取用户累计获得优惠券信息
     */
    public static final String GET_USER_PRESENT_CUMULATIVE_COUPON = "/getUserPresentCumulativeCoupon";
    
    /**
     * 获取用户灯笼点亮列表
     */
    public static final String GET_USER_LANTERN_ILLUMINE_LIST = "/getUserLanternIllumineList";
    
    /**
     * 修改用户答题信息
     */
    public static final String UPDATE_USER_ANSWER_RECORD = "/updateUserAnswerRecord";
    
    /**
     * 记录用户答题信息
     */
    public static final String INSERT_USER_ANSWER_RECORD_INIT = "/insertUserAnswerRecordInit";
    
    
    /**
     * 校验用户答题
     */
    public static final String CHECK = "/check";
    
    
}
