package com.hyjf.admin.finance.couponrepaymonitor;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class CouponRepayDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/finance/couponrepaymonitor";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "finance/couponrepay/repaymonitor";
	
    /** 列表画面 路径 */
    public static final String CHART_PATH = "finance/couponrepay/repaymonitorchart";
	
	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	
	/** 统计图表画面 @RequestMapping值 */
    public static final String CHART = "chart";
    
    /** 统计图表画面 @RequestMapping值 */
    public static final String REPAY_STATISTIC_ACTION = "repayStatisticAction";

	/** 导出数据 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";
	
	/** 检索数据 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";
	
	/** FROM */
	public static final String COUPON_FORM = "couponRepayMonitorForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "couponrepaymonitor";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查找权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
