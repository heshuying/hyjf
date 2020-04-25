/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.api.aems.register;

import com.hyjf.base.bean.BaseDefine;

/**
 * Aems系统用户注册
 *
 * @author liuyang
 * @version AemsUserRegisterDefine, v0.1 2018/9/4 9:39
 */
public class AemsUserRegisterDefine extends BaseDefine {

    /** 外部服务接口:用户注册 @RequestMapping */
    public static final String REQUEST_MAPPING = "/aems/register";

    /** 类名 @RequestMapping */
    public static final String THIS_CLASS = AemsUserRegisterServer.class.getName();

    /** 注册 @RequestMapping */
    public static final String REGISTER_ACTION = "/register";
}
