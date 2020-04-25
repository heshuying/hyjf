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
package com.hyjf.admin.manager.user.manageruser;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class ManageUsersDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/users";

	/** 用户列表 */
	public static final String USERS_LIST_PATH = "manager/users/userlist/userList";

	/** 用户数据修改 */
	public static final String USER_DETAIL_PATH = "manager/users/userlist/userDetail";

	/** 用户推荐人修改 */
	public static final String MODIFY_RE_PATH = "manager/users/userlist/modifyRe";

	/** 用户身份证修改 */
	public static final String MODIFY_IDCARD_PATH = "manager/users/userlist/updateIdCard";

	/** 用户详情 */
	public static final String UPDATE_USER_PATH = "manager/users/userlist/updateUser";

	/** 根据修改履历修改推荐人 */
	public static final String UPDATE_INVITE_PATH = "manager/users/userlist/updateInvite";
	
	/** 企业用户信息补录 */
	public static final String INSERT_COMPANY_PATH = "manager/users/userlist/companyInfo";

	/** 获取会员管理列表 @RequestMapping值 */
	public static final String USERS_LIST_ACTION = "userslist";

	/** 获取会员管理列表 @RequestMapping值 */
	public static final String EXPORT_USERS_ACTION = "exportusers";

	/** 获取会员管理列表 @RequestMapping值 具有 组织机构查看权限*/
	public static final String ENHANCE_EXPORT_USERS_ACTION = "enhanceExportusers";

	/** 用户详情画面 @RequestMapping值 */
	public static final String USER_DETAIL_ACTION = "userdetail";

	/** 获取用户编辑信息画面 @RequestMapping值 */
	public static final String INIT_USER_UPDATE_ACTION = "inituserupdate";

	/** 更新用户信息 @RequestMapping值 */
	public static final String UPDATE_USER_ACTION = "updateuser";

	/** 更新用户属性Attribute @RequestMapping值 */
	public static final String UPDATE_USERPARAM_ACTION = "updateUserParam";
	
	/** 更新全部用户属性Attribute @RequestMapping值 */
	public static final String UPDATE_ALLUSERPARAM_ACTION = "updateAllUserParam";

	/** 检查是否唯一 @RequestMapping值 */
	public static final String CHECK_ACTION = "checkAction";
	
	/** 检查是否唯一 @RequestMapping值 */
	public static final String CHECK_RE_ACTION = "checkReAction";

	/** 获取用户编辑信息画面 @RequestMapping值 */
	public static final String INIT_MODIFY_RE_ACTION = "initmodifyre";
	
	/** 获取用户编辑信息画面 @RequestMapping值 */
	public static final String INIT_MODIFY_IDCARD_ACTION = "initmodifyidcard";

	/** 更新用户信息 @RequestMapping值 */
	public static final String MODIFY_RE_ACTION = "modifyre";
	
	/** 更新用户信息 @RequestMapping值 */
	public static final String MODIFY_IC_ACTION = "modifyic";
	
	/** 根据修改履历修改推荐人 @RequestMapping值 */
	public static final String UPDATE_INVITE_INFO_ACTION = "updateInviteInfoAction";
	
	/** 根据修改履历修改推荐人 @RequestMapping值 */
	public static final String UPDATE_INVITE_ACTION = "updateInviteAction";
	
	/** 补录企业用户信息 @RequestMapping值 */
	public static final String INSERT_COMPANY_ACTION = "insertCompanyInfo";
	
	/** CRM更新推荐人Action @RequestMapping*/
	public static final String UPDATE_REC_ACTION = "updateRec";

	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + USERS_LIST_ACTION;

	/** 查看权限 */
	public static final String PERMISSIONS = "userslist";

	/** 查看权限 */
	public static final String PERMISSIONS_LIST = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_LIST;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 修改推荐人权限 */
	public static final String PERMISSIONS_MODIFYRE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFYRE;
	
	/** 修改身份证权限 */
	public static final String PERMISSIONS_MODIFYIDCARD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFYIDCARD;

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 修改权限 */
	public static final String PERMISSIONS_UPDATE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_UPDATE;

	/** 详情查看权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;

	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	public static final String ENHANCE_PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.ORGANIZATION_VIEW;

	/** 文件导出权限 */
	public static final String PERMISSIONS_UPDATEINVITE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_UPDATEINVITE;
	/**
	 * 补录企业用户信息权限
	 */
	public static final String PERMISSIONS_INSERT_CONPANYINFO= PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_NSERT_CONPANYINFO;

	/** 数据脱敏权限*/
	public static final String PERMISSION_HIDE_SHOW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_HIDDEN_SHOW;
	
	/** 会员管理列表FROM */
	public static final String USERS_LIST_FORM = "usersListForm";

	/** 会员详情FROM */
	public static final String USERS_DETAIL_FORM = "usersDetailForm";

	/** 会员信息修改FROM */
	public static final String USERS_UPDATE_FORM = "usersUpdateForm";
	
	/**用户信息修改FROM*/
	public static final String USERS_CHANGELOG_FORM = "usersChangeLogForm";

	/**身份证信息修改FROM*/
	public static final String IC_CHANGELOG_FORM = "ICChangeLogForm";
	
	/** 推荐人修改FROM */
	public static final String MODIFY_RE__FORM = "modifyReForm";
	
	/** 身份证修改FROM */
	public static final String MODIFY_IDCARD_FORM = "modifyIdCardForm";

	/** 类名 */
	public static final String THIS_CLASS = ManageUsersController.class.getName();

	/** 状态(不可用) */
	public static final String FLG_DISABLE = "1";

	/** 状态(可用) */
	public static final String FLG_AVTIVE = "0";
	
	/**卡号不存在*/
	public static final String RESPCODE_CORPRATION_QUERY_EXIST = "CA000054";
	
	/**平台交易验证未通过*/
	public static final String RESPCODE_CORPRATION_QUERY_CHECEK_ERROR = "CA101207";
	
	/**不是企业用户*/
	public static final String RESPCODE_CORPRATION_QUERY_NOT_CORPRATION = "CA110136";
	
	/**访问频率超限*/
	public static final String RESPCODE_CORPRATION_QUERY_CORPRATION_MORE = "JX900032";

	//合规四期,修改用户信息
	public static final String UPDATE_USERINFOS_ACTION = "initUpdateUserInfos";
	public static final String UPDATE_USERINFOS_PATH = "manager/users/userlist/updateUserInfos";

	//合规四期,添加修改手机号,邮箱,用户角色,修改银行卡权限 add by nxl
	public static final String PERMISSIONS_MODIFYPHONE= PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFYPHONE;
	public static final String PERMISSIONS_MODIFYEMAIL = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFYEMAIL;
	public static final String PERMISSIONS_MODIFYUSERROLE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFYUSERROLE;
	public static final String PERMISSIONS_MODIFYBANKCARD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFYBANKCARD;


}
