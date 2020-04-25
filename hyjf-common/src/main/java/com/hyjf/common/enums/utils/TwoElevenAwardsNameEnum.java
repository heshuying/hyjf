/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.common.enums.utils;

import com.hyjf.common.util.ActivityDateUtil;

/**
 * 双十一活动奖励名称
 * @author yinhui
 * @version TwoElevenAwardsNameEnum, v0.1 2018/10/11 17:59
 */
public enum  TwoElevenAwardsNameEnum {

    JX_ONE(ActivityDateUtil.TWOELEVEN_ACTIVITY_JX_ONE,"0.8"),
    JX_TWO(ActivityDateUtil.TWOELEVEN_ACTIVITY_JX_TWO,"0.9"),
    JX_THREE(ActivityDateUtil.TWOELEVEN_ACTIVITY_JX_THREE,"1.0"),
    JX_FOUR(ActivityDateUtil.TWOELEVEN_ACTIVITY_JX_FOUR,"0.8"),
    JX_FIVE(ActivityDateUtil.TWOELEVEN_ACTIVITY_JX_FIVE,"0.9"),
    JXT_SIX(ActivityDateUtil.TWOELEVEN_ACTIVITY_JX_SIX,"1.0"),
    JX_SEVEN(ActivityDateUtil.TWOELEVEN_ACTIVITY_JX_SEVEN,"0.8"),
    JX_EIGHT(ActivityDateUtil.TWOELEVEN_ACTIVITY_JX_EIGHT,"0.9"),
    JX_NINE(ActivityDateUtil.TWOELEVEN_ACTIVITY_JX_NINE,"1.0"),

    DJ_ONE(ActivityDateUtil.TWOELEVEN_ACTIVITY_DJ_ONE,"11"),
    DJ_TWO(ActivityDateUtil.TWOELEVEN_ACTIVITY_DJ_TWO,"88"),
    DJ_THREE(ActivityDateUtil.TWOELEVEN_ACTIVITY_DJ_THREE,"111"),
    DJ_FOUR(ActivityDateUtil.TWOELEVEN_ACTIVITY_DJ_FOUR,"11"),
    DJ_FIVE(ActivityDateUtil.TWOELEVEN_ACTIVITY_DJ_FIVE,"88"),
    DJT_SIX(ActivityDateUtil.TWOELEVEN_ACTIVITY_DJ_SIX,"111"),
    DJ_SEVEN(ActivityDateUtil.TWOELEVEN_ACTIVITY_DJ_SEVEN,"11"),
    DJ_EIGHT(ActivityDateUtil.TWOELEVEN_ACTIVITY_DJ_EIGHT,"88"),
    DJ_NINE(ActivityDateUtil.TWOELEVEN_ACTIVITY_DJ_NINE,"111");


    private String key;//值
    private String value;//名称

    private TwoElevenAwardsNameEnum(String key,String value){
        this.key = key;
        this.value = value;
    }

    public static String getValue(String key){

        for (TwoElevenAwardsNameEnum e:TwoElevenAwardsNameEnum.values()){

            if(key.equals(e.key)){
                return e.value;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
