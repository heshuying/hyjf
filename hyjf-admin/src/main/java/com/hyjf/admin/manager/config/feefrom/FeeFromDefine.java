package com.hyjf.admin.manager.config.feefrom;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class FeeFromDefine extends BaseDefine {

	/** 活动列表 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/config/feefrom";
	// ========================action========================
	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	// ========================jsppath========================
	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/config/feefrom/feefrom";

	/** 列表画面 路径 */
	public static final String RE_LIST_PATH = "redirect:/manager/config/feefrom/init";

	// ========================form========================
	/** FROM */
	public static final String FEEFROM_FORM = "feeFromForm";
	// ========================shiro========================
	/** 权限关键字 */
	public static final String PERMISSIONS = "feefrom";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

}
