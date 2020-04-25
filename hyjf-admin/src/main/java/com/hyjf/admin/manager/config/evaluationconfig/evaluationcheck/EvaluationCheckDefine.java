package com.hyjf.admin.manager.config.evaluationconfig.evaluationcheck;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class EvaluationCheckDefine extends BaseDefine {

	/** 开关配置 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/config/evaluationcheck";

	/** 从定向 路径 */
	public static final String RE_CHECK_PATH = "redirect:/manager/config/evaluationcheck/init";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 开关画面 路径 */
	public static final String INFO_PATH = "manager/config/evaluationconfig/evaluationcheck/evaluationcheck";
	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/config/evaluationconfig/evaluationcheck/evaluationchecklist";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";


	/** 开关FROM */
	public static final String EVALUATION_FORM = "evaluationcheckForm";

	/** 权限关键字 */
	public static final String PERMISSIONS = "evaluationcheck";
	
	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 详细权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;

}
