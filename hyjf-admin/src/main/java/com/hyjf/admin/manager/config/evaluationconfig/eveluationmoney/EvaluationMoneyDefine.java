package com.hyjf.admin.manager.config.evaluationconfig.eveluationmoney;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class EvaluationMoneyDefine extends BaseDefine {

	/** 限额配置 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/config/evaluationmoney";

	/** 从定向 路径 */
	public static final String RE_CHECK_PATH = "redirect:/manager/config/evaluationmoney/init";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 限额画面 路径 */
	public static final String INFO_PATH = "manager/config/evaluationconfig/evaluationmoney/evaluationmoney";
	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/config/evaluationconfig/evaluationmoney/evaluationmoneylist";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";


	/** 限额FROM */
	public static final String EVALUATION_FORM = "evaluationmoneyForm";

	/** 权限关键字 */
	public static final String PERMISSIONS = "evaluationmoney";
	
	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 详细权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;

}
