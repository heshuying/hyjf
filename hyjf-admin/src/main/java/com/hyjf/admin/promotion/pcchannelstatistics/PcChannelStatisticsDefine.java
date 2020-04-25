package com.hyjf.admin.promotion.pcchannelstatistics;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class PcChannelStatisticsDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/promotion/pcchannelstatistics";
	/** 画面初始化 @RequestMapping */
	public static final String INIT = "init";
	/** 画面检索 @RequestMapping */
	public static final String SEARCH_ACTION = "searchAction";
	/** 导出数据的 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";
	/** 列表画面 */
	public static final String LIST_PATH = "/promotion/pcchannelstatistics/pcchannelstatisticslist";
	/** FORM */
	public static final String FORM = "pcChannelStatisticsForm";
	/** 权限 */
	public static final String PERMISSIONS = " pcChannelStatistics";
	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;
	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
