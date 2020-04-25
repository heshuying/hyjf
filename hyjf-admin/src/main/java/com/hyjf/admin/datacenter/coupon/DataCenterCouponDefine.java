package com.hyjf.admin.datacenter.coupon;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class DataCenterCouponDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_TY_MAPPING = "/datacenter/couponTY";
	
	/** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_JX_MAPPING = "/datacenter/couponJX";

    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_DJ_MAPPING = "/datacenter/couponDJ";
	/** 列表画面 路径 */
	public static final String DATA_CENTER_COUPON_JX_LIST_PATH = "datacenter/coupon/couponjx";
	
	/** 列表画面 路径 */
    public static final String DATA_CENTER_COUPON_TY_LIST_PATH = "datacenter/coupon/couponty";
    /** 列表画面 路径 */
    public static final String DATA_CENTER_COUPON_DJ_LIST_PATH = "datacenter/coupon/coupondj";

	/** 列表画面 @RequestMapping值 */
	public static final String DATA_CENTER_COUPON_TY_INIT = "init";
	
	/** 列表画面 @RequestMapping值 */
    public static final String DATA_CENTER_COUPON_JX_INIT = "init";
    /** 列表画面 @RequestMapping值 */
    public static final String DATA_CENTER_COUPON_DJ_INIT = "init";
    
    /** 数据的 @RequestMapping值 */
    public static final String EXPORT_TY_ACTION = "exportTYAction";

    /** 数据的 @RequestMapping值 */
    public static final String EXPORT_JX_ACTION = "exportJXAction";
    
    /** 数据的 @RequestMapping值 */
    public static final String EXPORT_DJ_ACTION = "exportDJAction";

	/** FROM */
	public static final String FORM = "dataCenterCouponForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "DATACENTERCOUPON";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;
	
	/** 导出权限 */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
