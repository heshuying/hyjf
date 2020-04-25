package com.hyjf.admin.manager.borrow.borrowrepaymentinfo.infolist;

import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;
/**
 * 
 * @author 孙亮
 * @since 2015年12月18日 下午4:23:24
 */
public class BorrowRepaymentInfoListDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/borrowrepaymentinfo/infolist";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/borrow/borrowrepaymentinfo/infolist/borrowrepaymentinfolist";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 检索数据 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 转发路径 */
	public static final String FORWORD_LIST_PATH = "forward:"+REQUEST_MAPPING+"/"+INIT;
	
	/** 重定向路径 */
	public static final String RE_LIST_PATH = "redirect:"+REQUEST_MAPPING+"/"+INIT;

	/** 导出数据 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";

	/** FROM */
	public static final String REPAYMENTINFOLIST_FORM = "borrowRepaymentInfoListForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "borrowrepaymentlist";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	/** 还款权限 */
	public static final String PERMISSIONS_REPAY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_REPAY;

	/** 延期权限 */
	public static final String PERMISSIONS_AFTER_REPAY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_AFTER_REPAY;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

	
	/** 用此值判断是从哪个界面跳过来的 */
	public static final String ACTFROM = "actfrom";
	/** 用此值判断是从哪个界面跳过来的 */
	public static final String ACTFROMPLAN = "0";
	/** 从Info跳过来的 */
	public static final String ACTFROMINFO = "1";

}

	