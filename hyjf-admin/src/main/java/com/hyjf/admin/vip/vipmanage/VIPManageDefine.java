/**
 * Description:会员管理所用常量
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:01:57
 * Modification History:
 * Modified by : 
 * */
package com.hyjf.admin.vip.vipmanage;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class VIPManageDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/vip/vipmanage";

	/** VIP列表 */
	public static final String VIP_MANAGE_PATH = "vip/vipmanage/vipManage";

	/** 获取VIP管理列表 @RequestMapping值 */
	public static final String VIP_MANAGE_ACTION = "init";

	/** 导出VIP管理列表 @RequestMapping值 */
	public static final String EXPORT_VIP_ACTION = "exportvips";

	/** 查看权限 */
	public static final String PERMISSIONS = "vipmanage";

	/** 查看权限 */
	public static final String PERMISSIONS_LIST = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_LIST;

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 详情查看权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;

	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	/** 会员管理列表FROM */
	public static final String VIP_MANAGE_FORM = "vipManageForm";

}
