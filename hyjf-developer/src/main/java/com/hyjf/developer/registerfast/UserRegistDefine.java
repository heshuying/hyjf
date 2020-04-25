package com.hyjf.developer.registerfast;

import com.hyjf.developer.BaseDefine;

public class UserRegistDefine extends BaseDefine {
    /** CONTROLLOR @value值 */
    public static final String CONTROLLOR_CLASS_NAME = "UserRegistDefine";

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/user/regist";

	/** 用户注册 @RequestMapping值 */
	public static final String USER_REGIST_ACTION = "regist";
	
	/** 当前controller名称 */
	public static final String THIS_CLASS = UserRegistController.class.getName();

}
