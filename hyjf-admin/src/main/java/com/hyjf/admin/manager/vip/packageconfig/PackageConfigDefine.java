package com.hyjf.admin.manager.vip.packageconfig;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class PackageConfigDefine extends BaseDefine {

	/** vip等级配置列表 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/vip/packageconfig";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/vip/packageconfig/packageconfig";
	
	
	/** 迁移 路径 */
	public static final String INFO_PATH = "manager/vip/packageconfig/packageconfigInfo";


	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";
	/** 迁移到详细画面 @RequestMapping值 */
    public static final String SELECT_COUPON_LIST_ACTION = "selectCouponListAction";

	/** 条件查询数据 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";
	
	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_ACTION = "insertAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	/** 删除数据的 @RequestMapping值 */
	public static final String DELETE_ACTION = "deleteAction";

	/** 保存之前的去重验证 @RequestMapping值 */
	public static final String VALIDATEBEFORE = "validateBeforeAction";
	
	/** 加载优惠券配置的@RequestMapping值  */
    public static final String LOAD_COUPONCONFIG_ACTION = "loadCouponConfigAction";

	/** FROM */
	public static final String CONFIGFEE_FORM = "packageconfigForm";

	/** 权限关键字 */
	public static final String PERMISSIONS = "PACKAGECONFIG";
	
	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	/** 详细权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;

}
