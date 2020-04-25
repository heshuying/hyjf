/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.common.enums.utils;

/**
 * 神策数据埋点入口枚举
 * @author liuyang
 * @version SensorsDataEntranceEnum, v0.1 2018/7/12 16:16
 */
public enum SensorsDataEntranceEnum {

    WEB_TGY("推广页","0"),
    WEB_SY("首页","1"),
    WEB_DLY("登录页","2"),
    WEB_ZCY("注册页","3"),
    WEB_ZHSZ("账户设置","4"),
    WEB_TZQR("出借确认页","5"),
    WEB_ZHZL("账户总览","6"),
    WEB_HDY("活动落地页","7"),
    WEB_JHZQ("智投专区","8"),
    WEB_XSZQ("新手专区","9"),
    WEB_SBZQ("散标专区","11"),

    APP_MY("APP我的","12"),
    APP_SY("APP首页","13"),
    APP_TZY("APP出借页","14"),
    APP_XQY("APP详情页","15"),
    APP_TZQR("出借确认页","16"),





    ;

    private String entranceName;//前台界面显示名称
    private String entranceKey;//别名

    SensorsDataEntranceEnum(String entranceName, String entranceKey) {
        this.entranceName = entranceName;
        this.entranceKey = entranceKey;
    }
}
