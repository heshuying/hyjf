/**
 * Description:会员管理所用常量
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:01:57
 * Modification History:
 * Modified by : 
 * */
package com.hyjf.admin.manager.user.evaluation;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class EvaluationDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/evaluation";


	/** 获取用户测评列表 @RequestMapping值 */
	public static final String EVALUATION_LIST_ACTION = "evaluationList";

	
	/** 获取用户测评列表 @RequestMapping值 */
    public static final String EVALUATION_DETAIL_ACTION = "evaluationDetail";
    /** 数据的 @RequestMapping值 */
    public static final String EXPORT_ACTION = "exportAction";
	/** 用户测评列表 */
    public static final String EVALUATION_LIST_PATH = "manager/evaluation/evaluationList";
    
    /** 用户测评详情 */
    public static final String EVALUATION_DETAIL_PATH = "manager/evaluation/evaluationDetail";

	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + EVALUATION_LIST_ACTION;

	/** 查看权限 */
	public static final String PERMISSIONS = "evaluationlist";
	
	/** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查看权限 */
	public static final String PERMISSIONS_LIST = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_LIST;
	/** 导出权限 */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
    
    /** 数据脱敏权限*/
	public static final String PERMISSION_HIDE_SHOW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_HIDDEN_SHOW;
	
	/** 会员管理列表FROM */
	public static final String USERS_LIST_FORM = "usersListForm";

	/** 类名 */
	public static final String THIS_CLASS = EvaluationController.class.getName();

	/** 状态(不可用) */
	public static final String FLG_DISABLE = "0";

	/** 状态(可用) */
	public static final String FLG_AVTIVE = "1";
	
	
	 /** ValidateForm请求返回值 */
    public static final String JSON_IF_EVALUATION_KEY = "ifEvaluation";
    
    /** ValidateForm请求返回值*/
    public static final String JSON_USER_EVALATION_RESULT_KEY = "userEvalationResult";

}
