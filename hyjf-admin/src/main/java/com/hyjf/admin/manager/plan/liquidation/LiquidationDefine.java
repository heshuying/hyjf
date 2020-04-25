/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 * 
 * _ooOoo_
 * o8888888o
 * 88" . "88
 * (| -_- |)
 * O\ = /O
 * ____/`---'\____
 * .' \\| |// `.
 * / \\||| : |||// \
 * / _||||| -:- |||||- \
 * | | \\\ - /// | |
 * | \_| ''\---/'' | |
 * \ .-\__ `-` ___/-. /
 * ___`. .' /--.--\ `. . __
 * ."" '< `.___\_<|>_/___.' >'"".
 * | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * \ \ `-. \_ __\ /__ _/ .-` / /
 * ======`-.____`-.___\_____/___.-`____.-'======
 * `=---='
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * 佛祖保佑 永无BUG
 */

package com.hyjf.admin.manager.plan.liquidation;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 汇添金清算常量定义
 * 
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年11月1日
 * @see 下午2:36:50
 */
public class LiquidationDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/liquidation";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "infoSearchAction";

	/** 导出列表 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";

	/** 导出列表 @RequestMapping值 */
	public static final String INFO_EXPORT_ACTION = "infoExportAction";

	/** 计划详情 @RequestMapping值 */
	public static final String PLAN_LIQUIDATION_DETAIL = "detailAction";

	/** 清算 @RequestMapping值 */
	public static final String PLAN_LIQUIDATION = "liquidationAction";

	/** 查看权限 */
	public static final String PERMISSIONS = "liquidationManager";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查询权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	/** 清算权限 */
	public static final String PERMISSIONS_LIQUIDATION = PERMISSIONS + StringPool.COLON
			+ ShiroConstants.PERMISSION_LIQUIDATION;

	/** 详情权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;

	/** 计划列表页面 */
	public static final String LIST_PATH = "manager/borrow/liquidation/planLiquidationList";

	/** 计划详情页面 */
	public static final String DETAIL_PATH = "manager/borrow/liquidation/planLiquidationDetail";

	/** 清算出来的债权表单 **/
	public static final String CREDIT_FORM = "creditForm";

}
