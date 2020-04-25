package com.hyjf.web.user.findpassword;

import com.hyjf.web.BaseDefine;

public class FindPasswordDefine extends BaseDefine {
	
    /** CONTROLLOR @value值 */
    public static final String CONTROLLOR_CLASS_NAME = "findPasswordController";

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/user/findpassword";
	//controller
    /** 初始化界面  @RequestMapping值 */
    public static final String INIT = "init";
    /** 校验手机号 */
    public static final String CHECKPHONE="checkPhone";
    /** 校验验证码 */
    public static final String CHECKCODE="checkCode";
    /** 第二步界面  @RequestMapping值 */
    public static final String TO_SENCOND_PAGE = "sencodPage";
    
    /** 校验密码*/
    public static final String CHECKPWD="checkPwd";
    /** 跳转成功页 */
    public static final String TO_SUCCESS_PAGE="successPage";
    //jsp
    /** 初始化界面  @RequestMapping值 */
    public static final String INIT_PATH = "user/findpassword/findPwd";
    /** 第二步界面  @RequestMapping值 */
    public static final String SECOND_PATH = "user/findpassword/findPwdSecond";
    /** 成功界面  @RequestMapping值 */
    public static final String SUCCESS_PATH = "user/findpassword/findPwdSuccess";
  
  
}
