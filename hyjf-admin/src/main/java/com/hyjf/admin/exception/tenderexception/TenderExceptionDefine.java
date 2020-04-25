package com.hyjf.admin.exception.tenderexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class TenderExceptionDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/tenderexception";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "exception/tenderexception/tenderexception";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 检索数据 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 检索数据 @RequestMapping值 */
	public static final String TENDER_BACK_ACTION = "tenderBackAction";

	/** FROM */
	public static final String FORM = "form";

	/** 查看权限 */
	public static final String PERMISSIONS = "tenderexception";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查找权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

}
