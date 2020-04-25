package com.hyjf.admin.manager.config.evaluationconfig.evaluationchecklog;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class EvaluationCheckLogDefine extends BaseDefine {

	/** 开关配置 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/config/evaluationchecklog";

	/** 从定向 路径 */
	public static final String RE_CHECK_PATH = "redirect:/manager/config/evaluationchecklog/init";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 条件查询数据 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/config/evaluationconfig/evaluationcheck/evaluationcheckloglist";



	/** 开关FROM */
	public static final String EVALUATION_FORM = "evaluationchecklogForm";

	/** 权限关键字 */
	public static final String PERMISSIONS = "evaluationchecklog";
	
	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
	public static final String PERMISSION_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;


}
