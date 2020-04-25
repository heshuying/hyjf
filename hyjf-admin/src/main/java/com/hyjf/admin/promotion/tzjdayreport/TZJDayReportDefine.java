package com.hyjf.admin.promotion.tzjdayreport;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class TZJDayReportDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/promotion/tzj/dayreport";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "promotion/tzjdayreport/dayreport";
	
    /** 列表画面 路径 */
    public static final String CHART_PATH = "promotion/tzjdayreport/reportchart";
	
	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	
	/** 统计图表画面 @RequestMapping值 */
    public static final String CHART = "chart";
    
	/** 导出数据 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";
	
	/** 检索数据 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";
    
    /** 获取统计图表数据*/
    public static final String STATISTICS_CHART_DATA_ACTION = "chartData";
	
	/** FROM */
	public static final String TZJ_DAYREPORT_FORM = "tzjDayReportForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "tzjdayreport";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查找权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
