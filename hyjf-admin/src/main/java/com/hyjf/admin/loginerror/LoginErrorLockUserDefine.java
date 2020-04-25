/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.loginerror;

import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * @author cui
 * @version LoginErrorLockUserDefine, v0.1 2018/7/13 16:36
 */
public class LoginErrorLockUserDefine {

    public static final String REQUEST_MAPPING = "/loginerrorlockuser";

    //前台列表INIT ACTION
    public static final String FRONT_INIT_ACTION = "frontinit";
    //前台解锁
    public static final String FRONT_UNLOCK_ACTION = "frontunlock";

    //后台列表INIT ACTION
    public static final String BACK_INIT_ACTION = "backinit";
    //后台解锁
    public static final String BACK_UNLOCK_ACTION = "backunlock";

    /**
     * 权限
     */
    public static final String PERMISSIONS = "lockUserPermission";
    /**
     * 查看权限
     */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    //解锁权限
    public static final String PERMISSIONS_UNLOCK = PERMISSIONS + StringPool.COLON+ShiroConstants.PERMISSION_UNLOCK;

    //前台锁定用户列表页
    public static final String FRONT_LOCK_USER_LIST_PATH = "loginerror/frontlockuserlist";

    //后台锁定用户列表页
    public static final String BACK_LOCK_USER_LIST_PATH = "loginerror/backlockuserlist";

    public static final String LOCKUSER_LIST_FORM = "lockUserForm";

    //后台登录失败配置
    public static final String BACK_LOGINERRORCONFIG_ACTION = "backLoginErrorCfg";
    public static final String BACK_LOGINERRORCFG_PATH="loginerror/backlockuserconfig";

    //前台登录失败配置
    public static final String FRONT_LOGINERRORCONFIG_ACTION = "frontLoginErrorCfg";
    public static final String FRONT_LOGINERRORCFG_PATH="loginerror/frontlockuserconfig";
}
