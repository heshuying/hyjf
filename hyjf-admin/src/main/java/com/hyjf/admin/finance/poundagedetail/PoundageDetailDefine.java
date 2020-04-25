package com.hyjf.admin.finance.poundagedetail;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class PoundageDetailDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/poundagedetail";

	/** 列表画面  */
	public static final String POUNDAGE_DETAIL_LIST = "searchAction";
	
    /** 新增修改画面  */
    public static final String POUNDAGE_DETAIL_INFO_LIST = "infoAction";
    
    /** 新增修改信息 */
    public static final String POUNDAGE_DETAIL_ADD_OR_SAVE = "addOrSaveAction";
    
    /** 删除信息  */
    public static final String POUNDAGE_DETAIL_DELETE = "deleteAction";
    
    /** 导出信息  */
    public static final String POUNDAGE_DETAIL_EXPORT = "exportAction";
    
    
    /** 列表画面 路径 */
    public static final String LIST_PATH = "finance/poundagedetail/poundageDetailList";
	
	/** FORM */
	public static final String POUNDAGE_DETAIL_FORM = "poundageDetailForm";
	

    
    /** 查看权限 */
    public static final String PERMISSIONS = "poundagedetail";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 查询权限 */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

    /** 修改权限 */
    public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

    /** 删除权限 */
    public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;
   
    /** 添加权限 */
    public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;
   
    /** 导出权限 */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
}
