package com.hyjf.admin.finance.subcommission;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 商户分佣Define
 *
 * @author liuyang
 */
public class SubCommissionDefine extends BaseDefine {

	/** 类名 */
	public static final String THIS_CLASS = SubCommissionController.class.getName();
	/** CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/finance/subcommission";
	/** 画面初始化 @RequestMapping */
	public static final String INIT_ACTION = "init";
	/** 检索列表 @RequstMapping */
	public static final String SEARCH_ACTION = "searchAction";
	/** 账户分佣 @RequestMapping */
	public static final String TRANSFER_ACTION = "transferAction";
	/** 添加详情 @RequestMapping */
	public static final String DETAIL_ACTION = "detailAction";
	/** 数据导出 @RequestMapping */
	public static final String EXPORT_ACTION = "exportAction";
	/** 根据用户id查询用户信息 */
    public static final String USERNAME = "userNameAction";
	/** 列表画面 路径 */
	public static final String LIST_PATH = "/finance/subcommission/subcommissionlist";
	/** 详情画面 路径 */
	public static final String DETAIL_PATH = "/finance/subcommission/subcommissiondetail";
	/** FROM */
	public static final String FORM = "subCommissionForm";
	/** 权限 */
	public static final String PERMISSIONS = "subCommission";
	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;
	/** 查询权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;
	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
