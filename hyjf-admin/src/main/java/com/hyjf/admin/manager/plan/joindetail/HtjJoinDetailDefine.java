package com.hyjf.admin.manager.plan.joindetail;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 汇添金加入明细Define
 * 
 * @ClassName HtjJoinDetailDefine
 * @author liuyang
 * @date 2016年9月29日 上午9:46:11
 */
public class HtjJoinDetailDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/htj/joindetail";

	/** 加入明细列表 @RequestMapping */
	public static final String LIST_PATH = "/htj/joindetail/joindetailList";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 检索画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** exeportExecl */
	public static final String EXPORTEXECL = "exportExcel";
	
	/** FORM */
	public static final String JOINDETAIL_FORM = "joindetailForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "joindetail";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
