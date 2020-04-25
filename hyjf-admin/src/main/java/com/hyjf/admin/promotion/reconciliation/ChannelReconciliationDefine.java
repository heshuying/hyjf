package com.hyjf.admin.promotion.reconciliation;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class ChannelReconciliationDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/promotion/channelreconciliation";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "promotion/reconciliation/channelreconciliation";
	
	/** 列表画面 路径_计划 */
    public static final String HJH_LIST_PATH = "promotion/reconciliation/channelreconciliationHjh";

	/** 列表画面——散标 @RequestMapping值 */
	public static final String INIT = "init";
	
	/** 列表画面——计划 @RequestMapping值 */
    public static final String HJH_INIT = "hjhinit";

	/** 列表画面——散标 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";
	
	/** 列表画面——计划 @RequestMapping值 */
	public static final String HJH_SEARCH_ACTION = "hjhSearchAction";

	/** 导出数据的——散标 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";
	
	/** 导出数据的——计划 @RequestMapping值 */
    public static final String HJH_EXPORT_ACTION = "exportHjhAction";

	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

	/** FROM */
	public static final String FORM = "channelreconciliationForm";

	/** FORM*/
	public static final String HJH_FORM = "channelreconciliationHjhForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "pcchannelrecon";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
}
