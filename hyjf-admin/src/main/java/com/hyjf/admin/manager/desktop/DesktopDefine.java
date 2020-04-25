package com.hyjf.admin.manager.desktop;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class DesktopDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/desktop";

	/** 详细画面的路径 */
	public static final String INFO_PATH = "manager/desktop/desktop";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 查看权限 */
	public static final String DESKTOP = "desktop";

	/** 查看权限 */
	public static final String DESKTOP_VIEW = DESKTOP + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

}
