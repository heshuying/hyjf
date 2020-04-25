package com.hyjf.web.user.preregist;

import com.hyjf.web.BaseDefine;

public class UserPreRegistDefine extends BaseDefine {
    /** CONTROLLOR @value值 */
    public static final String CONTROLLOR_CLASS_NAME = "UserRegistDefine";
    
    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/user/preregist";

    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";
    
    /** 列表画面 @RequestMapping值 */
    public static final String HOME = "home";
    
    /** 路径 */
    public static final String INIT_PATH = "user/preregist/preregistIndex";
    /** 路径 */
    public static final String INIT_HOME = "user/preregist/preregistHome";
    
    /** 预注册信息保存 */
    public static final String PREREGIST_SAVE_PATH = "preregistSubmit";
    

	
	public static final String THIS_CLASS = UserPreRegistController.class.getName();
}
