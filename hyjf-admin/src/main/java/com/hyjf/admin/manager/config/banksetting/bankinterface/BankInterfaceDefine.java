package com.hyjf.admin.manager.config.banksetting.bankinterface;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BankInterfaceDefine extends BaseDefine {

	/** 活动列表 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/config/banksetting/bankinterface";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/config/banksetting/bankinterface/bankinterface";
	
	/** 从定向 路径 */
	public static final String RE_LIST_PATH = "redirect:/manager/config/banksetting/bankinterface/init";
	
	/** 迁移 路径 */
	public static final String INFO_PATH = "manager/config/banksetting/bankinterface/bankinterfaceInfo";

	/** 删除后 路径 */
	public static final String DELETE_AFTER_PATH = "manager/config/banksetting/banksetting";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 条件查询数据 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	/** 删除数据的 @RequestMapping值 */
	public static final String DELETE_ACTION = "deleteAction";

	/** 禁用/启用数据的 @RequestMapping值 */
	public static final String USE_ACTION = "useAction";

	/** 保存之前的去重验证 @RequestMapping值 */
	public static final String VALIDATEBEFORE = "validateBeforeAction";

	/** FROM */
	public static final String BANKINTERFACE_FORM = "bankinterfaceForm";

	/** 权限关键字 */
	public static final String PERMISSIONS = "bankinterface";
	
	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

	/** 详细权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;
	   /** 导出数据的 @RequestMapping值 */
    public static final String EXPORT_ACTION = "exportAction";

}
