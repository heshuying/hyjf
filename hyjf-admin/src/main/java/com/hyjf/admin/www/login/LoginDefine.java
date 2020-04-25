/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2015年11月18日 下午12:34:08
 * Modification History:
 * Modified by : 
 */

package com.hyjf.admin.www.login;

import com.hyjf.admin.BaseDefine;

/**
 * @author Administrator
 */

public class LoginDefine extends BaseDefine{

	/** CONTROLLOR @value值 */
	public static final String CONTROLLOR_CLASS_NAME = "LoginController";

	/** CONTROLLOR @RequestMapping值 */
	public static final String CONTROLLOR_REQUEST_MAPPING = "/login";

	/** 路径 */
	public static final String INIT_PATH = "www/login/login";
	
	/** 路径 修改密码页面 */
	public static final String UPDATEOPASSWORD_PATH = "www/updatepassword/updatepassword";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 列表画面 @RequestMapping值 */
	public static final String LOGIN = "login";

	/** 列表画面 @RequestMapping值 */
	public static final String LOGIN_OUT = "loginOut";
	
	/** 列表画面 @RequestMapping值 */
	public static final String TO_UPDATEPASSWORD = "toUpdatePassword";
	
	/** 列表画面 @RequestMapping值 */
	public static final String UPDATEPASSWORD = "updatePasswordAction";
	/** 列表画面 @RequestMapping值 */
	public static final String CHECKPASSWORD = "checkPasswordAction";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String MOVE_TO_INFO_ACTION = "moveToInfoAction";

	/** 更新或者插入数据的 @RequestMapping值 */
	public static final String INSERT_OR_UPDATE_ACTION = "insertOrUpdateAction";

	/** 删除数据的 @RequestMapping值 */
	public static final String DELETE_RECORD_ACTION = "deleteRecordAction";
	
	/** */
	public static final String CHECKPWDISOK = "checkPasswordIsOk";

}
