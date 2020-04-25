package com.hyjf.admin.vip.vipupgrade;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class VIPUPGradeListDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/vip/vipupgrade";

	/** VIP列表 */
	public static final String VIP_UPGRADE_PATH = "vip/vipdetail/vipupgrade";

	/** 获取VIP管理列表 @RequestMapping值 */
	public static final String VIP_UPGRADE_ACTION = "init";

	/** 查看权限 */
	public static final String PERMISSIONS = "vipupgrade";

	/** 查看权限 */
	public static final String PERMISSIONS_LIST = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_LIST;

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 详情查看权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;

	/** 会员管理列表FROM */
	public static final String VIP_UPGRADE_FORM = "vipUPGradeForm";

}
