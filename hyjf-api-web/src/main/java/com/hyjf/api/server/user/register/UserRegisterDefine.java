package com.hyjf.api.server.user.register;

import com.hyjf.base.bean.BaseDefine;

/**
 * 外部服务接口:用户注册
 * 
 * @author liuyang
 *
 */
public class UserRegisterDefine extends BaseDefine {

	/** 外部服务接口:用户注册 @RequestMapping */
	public static final String REQUEST_MAPPING = "/server/user/register";

	/** 类名 @RequestMapping */
	public static final String THIS_CLASS = UserRegisterServer.class.getName();

	/** 注册 @RequestMapping */
	public static final String REGISTER_ACTION = "/register";
}
