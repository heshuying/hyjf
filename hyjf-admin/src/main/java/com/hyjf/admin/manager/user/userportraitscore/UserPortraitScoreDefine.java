/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.user.userportraitscore;

import com.hyjf.admin.BaseDefine;

/**
 * @author yaoyong
 * @version UserPortraitScoreDefine, v0.1 2018/7/9 17:25
 */
public class UserPortraitScoreDefine extends BaseDefine {
    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/userPortrait";

    /** 列表画面 @RequestMapping值 */
    public static final String USERPORTRAITSCORE_LIST_ACTION = "userPortraitScore";

    /** 列表FORM */
    public static final String USERPORTRAITSCORE_LIST_FORM = "userPortraitScoreForm";

    /** 用户授权明细 */
    public static final String USER_PORTRAITSCORE_LIST_PATH = "manager/users/userportrait/userportraitscore";

    /** 用户编辑 */
    public static final String USERPORTRAITSCORE_UPDATE_ACTION = "updateuserportrait";

    /** 编辑保存 */
    public static final String USERPORTRAIT_SAVE_ACTION = "saveuserportrait";

    /** 用户画像编辑页面 */
    public static final String USERPORTRAIT_UPDATE_PATH = "manager/users/userportrait/userportraitupdate";

    /** 用户画像表格导出 */
    public static final String EXPORT_USERPORTRAIT_ACTION = "exportuserportrait";
}
