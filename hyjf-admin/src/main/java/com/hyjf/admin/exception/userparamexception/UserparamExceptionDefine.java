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
package com.hyjf.admin.exception.userparamexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class UserparamExceptionDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/userparamexception";

	/** 用户列表 */
	public static final String USERS_LIST_PATH = "exception/userparamexception/userparamexception";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT_ACTION = "initAction";

	/** 条件查询数据 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 获取会员管理列表 @RequestMapping值 */
	public static final String USERS_LIST_ACTION = "userslist";

	/** 更新用户属性Attribute @RequestMapping值 */
	public static final String UPDATE_USERPARAM_ACTION = "updateUserParam";

	/** 更新全部用户属性Attribute @RequestMapping值 */
	public static final String UPDATE_ALLUSERPARAM_ACTION = "updateAllUserParam";
	
	/** 修复出借错误数据初始化    @RequestMapping值 */
	public static final String INIT_TENDER_REPAIR_ACTION = "initTenderRepairAction";

	/** 修复出借错误数据 @RequestMapping值 */
	public static final String TENDER_REPAIR_ACTION = "tenderRepairAction";
	
	/** 修复出借错误数据初始化    @RequestMapping值 */
	public static final String INIT_TENDER_REPAIR_PATH = "exception/userparamexception/repairTender";

	/** 查看权限 */
	public static final String PERMISSIONS = "userparamexception";

	/** 查看权限 */
	public static final String PERMISSIONS_LIST = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_LIST;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 修改推荐人权限 */
	public static final String PERMISSIONS_MODIFYRE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFYRE;

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 全部修改权限 */
	public static final String PERMISSIONS_MODIFYALL = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFYALL;

	/** 详情查看权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;

	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	/** 文件导出权限 */
	public static final String PERMISSIONS_UPDATEINVITE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_UPDATEINVITE;

	/** 更新用户出借数据权限 */
	public static final String PERMISSION_REPAIR_TENDER = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_REPAIR_TENDER;
	
	/** 会员管理列表FROM */
	public static final String USERS_LIST_FORM = "usersListForm";

	/** 会员详情FROM */
	public static final String USERS_DETAIL_FORM = "usersDetailForm";

	/** 会员信息修改FROM */
	public static final String USERS_UPDATE_FORM = "usersUpdateForm";

	/** 推荐人修改FROM */
	public static final String MODIFY_RE__FORM = "modifyReForm";

	/** 类名 */
	public static final String THIS_CLASS = UserparamExceptionController.class.getName();

	/** 状态(不可用) */
	public static final String FLG_DISABLE = "1";

	/** 状态(可用) */
	public static final String FLG_AVTIVE = "0";

}
