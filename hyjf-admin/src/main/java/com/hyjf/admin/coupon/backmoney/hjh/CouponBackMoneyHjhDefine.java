package com.hyjf.admin.coupon.backmoney.hjh;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class CouponBackMoneyHjhDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String TY_REQUEST_MAPPING = "/coupon/backmoneyty/hjh";
	
	/** 权限 CONTROLLOR @RequestMapping值 */
    public static final String JX_REQUEST_MAPPING = "/coupon/backmoneyjx/hjh";

    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String DJ_REQUEST_MAPPING = "/coupon/backmoneydj/hjh";
	/** 列表画面 路径 */
	public static final String BACK_MONEY_JX_LIST_PATH = "coupon/backmoney/hjh/backmoneyjx";
	
	/** 列表画面 路径 */
    public static final String BACK_MONEY_TY_LIST_PATH = "coupon/backmoney/hjh/backmoneyty";

    /** 列表画面 路径 */
    public static final String BACK_MONEY_DJ_LIST_PATH = "coupon/backmoney/hjh/backmoneydj";
	/** 列表画面 @RequestMapping值 */
	public static final String BACK_MONEY_TY_INIT = "init";
	
	/** 列表画面 @RequestMapping值 */
    public static final String BACK_MONEY_JX_INIT = "init";
    
    /** 列表画面 @RequestMapping值 */
    public static final String BACK_MONEY_DJ_INIT = "init";

	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_TY_ACTION = "searchAction";
	/** 列表画面 @RequestMapping值 */
    public static final String SEARCH_JX_ACTION = "searchAction";
    
    /** 列表画面 @RequestMapping值 */
    public static final String SEARCH_DJ_ACTION = "searchAction";
    /** 数据的 @RequestMapping值 */
    public static final String EXPORT_TY_ACTION = "exportTYAction";

    /** 数据的 @RequestMapping值 */
    public static final String EXPORT_JX_ACTION = "exportJXAction";
    /** 数据的 @RequestMapping值 */
    public static final String EXPORT_DJ_ACTION = "exportDJAction";
	/** FROM */
	public static final String FORM = "couponBackMoneyForm";

	/** 查看权限 */
    public static final String PERMISSIONSTY = "HJHBACKMONEYTY";
    /** 查看权限 */
    public static final String PERMISSIONSJX = "HJHBACKMONEYJX";
    /** 查看权限 */
    public static final String PERMISSIONSDJ = "HJHBACKMONEYDJ";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW_TY = PERMISSIONSTY + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH_TY = PERMISSIONSTY + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;
	/** 查看权限 */
    public static final String PERMISSIONS_VIEW_JX = PERMISSIONSJX + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 检索权限 */
    public static final String PERMISSIONS_SEARCH_JX = PERMISSIONSJX + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;
    /** 查看权限 */
    public static final String PERMISSIONS_VIEW_DJ = PERMISSIONSDJ + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 检索权限 */
    public static final String PERMISSIONS_SEARCH_DJ = PERMISSIONSDJ + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;
	
	/** 导出权限 */
    public static final String PERMISSIONS_EXPORT_TY = PERMISSIONSTY + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
    
    /** 导出权限 */
    public static final String PERMISSIONS_EXPORT_JX = PERMISSIONSJX + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
    
    /** 导出权限 */
    public static final String PERMISSIONS_EXPORT_DJ = PERMISSIONSDJ + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
