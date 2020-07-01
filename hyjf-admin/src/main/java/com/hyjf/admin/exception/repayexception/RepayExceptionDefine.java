package com.hyjf.admin.exception.repayexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class RepayExceptionDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/repayexception";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "/exception/repayexception/repayexception";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 检索数据 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

    /** 重新还款 @RequestMapping值 */
    public static final String RESTART_REPAY_ACTION = "restartRepayAction";

	/** 转发路径 */
	public static final String FORWORD_LIST_PATH = "forward:" + REQUEST_MAPPING + "/" + INIT;

	/** 重定向路径 */
	public static final String RE_LIST_PATH = "redirect:" + REQUEST_MAPPING + "/" + INIT;
	
	/** 重新进行单笔还款 @RequestMapping值 */
	public static final String INIT_REPAY_ACTION = "initRepayAction";
	
	/** 进行单笔还款 @RequestMapping值 */
	public static final String ADD_REPAY_ACTION = "addRepayAction";
	
	/** 列表画面 路径 */
	public static final String INIT_REPAY_PATH = "/exception/repayexception/addRepay";

	/** 导出数据 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";

	/** 跳转到详情页面的Action @RequestMapping值 */
	public static final String TO_RECOVER_ACTION = "toRecoverAction";

	/** FROM */
	public static final String REPAYMENT_FORM = "repayexceptionForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "repayexception";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	/** 还款权限 */
	public static final String PERMISSIONS_REPAY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_REPAY;

	/** 详细权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;
	
	/** 还款权限 */
	public static final String PERMISSIONS_ADD_REPAY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;
	
	/** 详细权限 */
	public static final String THIS_CLASS = RepayExceptionController.class.getName();
}