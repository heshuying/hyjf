package com.hyjf.admin.promotion.message;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class MessageDefine extends BaseDefine {

    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/message";

    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";

    /** 查看权限 */
    public static final String PERMISSIONS = "smsMailMessage";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

}
