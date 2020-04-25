package com.hyjf.coupon.util;

import com.hyjf.base.bean.BaseDefine;

public class CouponCheckUtilDefine extends BaseDefine {
    
    
    //-------------------------------通用---------------------------------
    //用户id不能为空
    public final static String USER_ID_IS_NULL="用户id不能为空";
    
    //用户不存在
    public final static String USER_IS_NULL="用户不存在";
    //用户不存在
    public final static String OLD_USER_MESSAGE="老用户不能参与该活动";
    
    //-------------------------------活动---------------------------------
    //活动编号不能为空
    public final static String ACTIVITYID_IS_NULL="活动编号不能为空";
    //该活动不存在
    public final static String ACTIVITY_ISNULL="该活动不存在";
    //该活动不存在
    public final static String ACTIVITY_TIME_NOT_START="该活动还未开始";
    //该活动已结束
    public final static String ACTIVITY_TIME_END="您来晚了，活动已过期~~";
    //该活动已结束
    public final static String PLATFORM_LIMIT="本活动限***端参与";
    //该活动用户已参与
    public final static String ACTIVITY_LIMIT="该活动用户已参与";
    
    //-------------------------------优惠券---------------------------------
    //用户优惠券id不能为空
    public final static String COUPONUSERID_IS_NULL="用户优惠券id不能为空"; 
    //用户优惠券不存在
    public final static String COUPONUSER_IS_NULL="用户优惠券不存在"; 
    //用户优惠券不存在
    public final static String COUPONUCONFIG_IS_NULL="优惠券信息不存在"; 
    //用户优惠券已过期
    public final static String COUPONUSER_TIME_END="用户优惠券已过期"; 
    //用户优惠券已失效
    public final static String COUPONUSER_EFFECT_FLAG="用户优惠券已失效";
    //用户优惠券已使用
    public final static String COUPONUSER_USED_FLAG="用户优惠券已适用";
    //用户优惠券不能使用
    public final static String COUPONUSER_NOT_USED ="用户优惠券不能使用";
    //-------------------------------出借标的---------------------------------
    //出借项目类型Code不能为空
    public final static String PROJECTTYPECODE_IS_NULL="出借项目类型Code不能为空"; 
    //出借项目类型不能为空
    public final static String PROJECTTYPE_IS_NULL="出借项目类型不能为空"; 
    //出借标的编号不能为空
    public final static String BORROWNID_IS_NULL="出借标的编号不能为空"; 
    //出借标的信息不存在
    public final static String BORROW_IS_NULL="出借标的信息不存在";
    
    
    
    
}
