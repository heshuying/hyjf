/**
 */
package com.hyjf.web.user.invite;

import com.hyjf.web.BaseDefine;

public class InviteDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/user/invite";

	/** 显示邀请信息页面  @RequestMapping值 */
	public static final String TO_INVITE_ACTION = "toInvite";
	/** 显示邀请信息@RequestMapping值 */
	public static final String PROJECT_LIST_ACTION = "logs";

	/** 我的钱包画面 路径 */
    public static final String TO_REWARD_PATH = "user/invite/reward";
	/**下载二维码*/
	public static final String DOWNLOAD_ACTION = "download";

	/** 表单 */
	public static final String REGTIST_USER_FORM = "inviteForm";

	/** 统计类名 */
	public static final String THIS_CLASS= InviteController.class.getName();
}
