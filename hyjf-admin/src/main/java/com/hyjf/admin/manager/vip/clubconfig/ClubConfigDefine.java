package com.hyjf.admin.manager.vip.clubconfig;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class ClubConfigDefine extends BaseDefine {

	/** 会员CLUB通到配置 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/vip/clubconfig";


	/** 列表画面 @RequestMapping值 */
	public static final String DISPLAY_VIP = "displayvip";

	/** 权限关键字 */
    public static final String PERMISSIONS_USER = "userslist";
	
	/** 修改权限 */
    public static final String PERMISSIONS_UPDATE = PERMISSIONS_USER + StringPool.COLON + ShiroConstants.PERMISSION_UPDATE;

}
