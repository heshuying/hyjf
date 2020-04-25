/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
/**
 * 
 */
package com.hyjf.admin.exception.bidapplyquery;

import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * @author libin
 * 出借人投标申请查询Define
 * @version BidApplyQueryDefine.java, v0.1 2018年8月16日 上午9:56:54
 */
public class BidApplyQueryDefine {
	
	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/bidapplyquery";
	
	/** 列表画面 路径 */
	public static final String LIST_PATH = "exception/bidapplyquery/bidapplyquery";
	
	/** 列表画面 @RequestMapping值 */
	public static final String INIT_ACTION = "init";
	
	/** 列表画面查询 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";
	
	/** FROM */
	public static final String BID_APPLY_QUERY_FORM = "bidApplyQueryForm";
	
	/** 查看权限 */
	public static final String PERMISSIONS = "bidapplyquery";
	
	/** 接口中文名（信息用） */
	public static final String INTERFACE_NAME_CN = "出借人投标申请查询";
	
	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;
}
