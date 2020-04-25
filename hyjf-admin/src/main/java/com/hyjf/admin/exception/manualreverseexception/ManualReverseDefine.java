package com.hyjf.admin.exception.manualreverseexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * @author PC-LIUSHOUYI
 */

public class ManualReverseDefine extends BaseDefine{

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/manualreverse";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 列表路径 @RequestMapping值 */
	public static final String LIST_PATH = "/exception/manualreverseexception/manualreverseexception";
	
	/** 插入列表路径 @RequestMapping值 */
	public static final String INFO_PATH = "/exception/manualreverseexception/manualreverseinfo";

    /** 迁移到详细画面 @RequestMapping值 */
    public static final String INFO_ACTION = "infoAction";
    
	/** FORM */
	public static final String FORM = "manualreverseForm";
	
	/** 列表检索 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_ACTION = "insertAction";
	
	/** 获取用户开户账户 @RequestMapping值 */
	public static final String GET_ACCOUNTID_ACTION = "getAccountIdAction";
    
	/** 查看权限 */
	public static final String PERMISSIONS = "manualreverse";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

	/** AJAX @RequestMapping值 */
	public static final String ISEXISTSUSER_ACTION = "isExistsUser";

}

	