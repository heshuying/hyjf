package com.hyjf.admin.exception.singletradeinfo;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
* 接口：单笔资金类业务交易查询  常量类
* @author LiuBin
* @date 2017年7月31日 上午9:31:11
* 
*/
public class SingleTradeInfoDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/singletradeinfo";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "exception/singletradeinfo/singletradeinfo";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT_ACTION = "init";
	
	/** 列表画面查询 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";
	
	/** FROM */
	public static final String SINGLE_TRADE_INFO_FORM = "singletradeinfoForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "singletradeinfo";
	
	/** 接口中文名（信息用） */
	public static final String INTERFACE_NAME_CN = "单笔资金类业务交易查询";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;


}
