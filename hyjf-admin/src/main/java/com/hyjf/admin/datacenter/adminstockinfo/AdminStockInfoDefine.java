package com.hyjf.admin.datacenter.adminstockinfo;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class AdminStockInfoDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/datacenter/adminstockinfo";

	/** 列表画面  */
	public static final String ADMINSTOCKINFO_LIST = "adminStockInfoAction";
	
    /** 新增修改画面  */
    public static final String ADMINSTOCKINFO_INFO_LIST = "adminStockInfoInfoAction";
    
    /** 新增修改信息 */
    public static final String ADMINSTOCKINFO_ADD_OR_SAVE = "addOrSaveAdminStockInfoAction";
    
    /** 删除信息  */
    public static final String ADMINSTOCKINFO_DELETE = "adminStockInfoDeleteAction";
    
	/** 新增修改画面 路径 */
	public static final String INFO_PATH = "/datacenter/adminstockinfo/adminStockInfoInfo";
    
    /** 列表画面 路径 */
    public static final String LIST_PATH = "/datacenter/adminstockinfo/adminStockInfoList";
	
	/** FORM */
	public static final String ADMINSTOCKINFO_FORM = "AdminStockInfoForm";

    /** 查看权限 */
    public static final String PERMISSIONS = "DATACENTERSTOCKINFO";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 检索权限 */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;
}
