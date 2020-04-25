package com.hyjf.wechat.controller.landingpage;

import com.hyjf.wechat.base.BaseDefine;

/**
 * 着陆页
 * @Author : huanghui
 * @Version : hyjf 1.0
 * @Date : 2018年05月24日
 */
public class LandingPageDefine extends BaseDefine {

    /** 统计类名 */
    public static final String THIS_CLASS = LandingPageController.class.getName();

    /** REQUEST_MAPPING */
    public static final String REQUEST_MAPPING = "/wx/landingPage";

    /** 注册着陆页用户数据 */
    public static final String LANDINGPAGE_USER_DATA = "/userData";

    /** 用户渠道信息 */
    public static final String SELECT_USER_UTM_INFO = "/getUserUtmInfo";
}
