package com.hyjf.admin.manager.plan.statis;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class StatisDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/htj/statis";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "htj/statis/statis";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	
	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH = "searchAction";
	
	/** 列表画面 @RequestMapping值 */
	public static final String COUNT_ACTION = "countAction";
	
	/** FROM */
	public static final String HTLSTATIS_FORM = "htlstatisForm";
	
	/** 查看权限 */
	public static final String HTILSTATIS = "htjstatis";

	/** 查看权限 */
	public static final String STATIS_VIEW = HTILSTATIS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;



}
