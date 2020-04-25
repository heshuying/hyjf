package com.hyjf.admin.exception.increaseinterestexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 融通宝加息异常处理Define
 * 
 * @ClassName IncreaseInterestExceptionDefine
 * @author liuyang
 * @date 2017年1月5日 下午5:35:10
 */
public class IncreaseInterestExceptionDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/increaseinterestexception";

	/** 列表路径 @RequestMapping值 */
	public static final String LIST_PATH = "exception/increaseinterestexception/increaseinterestexception";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 列表检索 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 重新还款 */
	public static final String RESTART_REPAY_ACTION = "restartRepayAction";

	/** FORM */
	public static final String FORM = "repayexceptionForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "repayexception";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 修改权限 */
	public static final String PERMISSIONS_CONFIRM = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_CONFIRM;

}
