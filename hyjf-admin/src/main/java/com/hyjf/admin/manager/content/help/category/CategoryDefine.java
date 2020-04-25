package com.hyjf.admin.manager.content.help.category;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class CategoryDefine extends BaseDefine {
	
	public static final String GROUP="help";

    /** 待返现列表画面 @RequestMapping值 */
    public static final String INIT = "init";
    
	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String HELP = "/manager/content/help";
	
	/** 问题分类路径 CONTROLLOR @RequestMapping值 */
	public static final String CATEGORY_PATH = "/help/category";
	
	/** 问题分类页面路径 CONTROLLOR @RequestMapping值 */
	public static final String  CATEGORY_LIST_VIEW="/manager/content/help/category";
	
	/** 查询form CONTROLLOR @RequestMapping值 */
	public static final String  QUERY_FORM="listForm";

	/** 跳转到添加分类的页面 */
	public static final String INFOTYPE_ACTION = "infoTypeAction";

	/** 跳转到添加子分类的页面 */
	public static final String INFOSUBTYPE_ACTION = "infoSubTypeAction";
	
	/** 详情页面路径 */
	public static final String INFO_PATH = "/manager/content/help/categoryInfo";
	
	/** 详情页数据CONTROLLOR @RequestMapping值 */
	public static final String INFO_FORM="infoForm";
	
	/** 新增分类 CONTROLLOR @RequestMapping值 */
	public static final String ADD_ACTION="addAction";
	
	/** 修改分类 CONTROLLOR @RequestMapping值 */
	public static final String UPDATE_ACTION="updateAction";
	
	/** 关闭分类 CONTROLLOR @RequestMapping值 */
	public static final String CLOSE_ACTION="closeAction";
	
	/** 启用分类CONTROLLOR @RequestMapping值 */
	public static final String OPEN_ACTION="openAction";
	
	/** 删除前的验证Action */
	public static final String BEFORE_DEL_INFO_ACTION ="beforeDelInfoAction";
	
	/** 删除分类页面入口CONTROLLOR @RequestMapping值 */
	public static final String DEL_INFO_ACTION ="delInfoAction";
	
	/** 二级菜单联动Action*/
	public static final String CHANGE_SUBTYPE_ACTION ="changeSubTypeAction";
	
	/** 二级菜单联动Action*/
	public static final String CHANGE_SUBTYPE_ACTION2 ="changeSubTypeAction2";
	
	/** 删除页面路径 */
	public static final String DEL_INFO_PATH = "/manager/content/help/delCategoryInfo";
	
	/** 删除页面问题列表 */
	public static final String DEL_CATEGORY_LIST = "catelist";
	
	/** 删除分类CONTROLLOR @RequestMapping值 */
	public static final String DEL_ACTION ="delAction";
	
	//	以下为问题列表所需字段
	
	/** 问题列表数据CONTROLLOR @RequestMapping值 */
	public static final String CONTENT_HELP_LIST="listHelp";
	
    /** 待返现列表画面 @RequestMapping值 */
    public static final String HELP_INIT = "helpInit";
    
	/** 问题列表页面路径 CONTROLLOR @RequestMapping值 */
	public static final String  HELP_LIST_VIEW="/manager/content/help/helplist";
	
	/** 问题列表页面数据 CONTROLLOR @RequestMapping值 */
	public static final String  HELP_FORM="helpForm";
	
	/** 问题列表分类下拉框数据 CONTROLLOR @RequestMapping值 */
	public static final String  CATE_LIST="catelist";
	
	/** 问题列表切换到详情页 CONTROLLOR @RequestMapping值 */
	public static final String HELP_INFO_ACTION="helpInfoAction";
	
	/** 问题详情页面路径 */
	public static final String HELP_INFO_PATH = "/manager/content/help/helpInfo";
	
	/** 详情页数据CONTROLLOR @RequestMapping值 */
	public static final String HELP_INFO_FORM="helpInfoForm";
	
	/** 新增问题 CONTROLLOR @RequestMapping值 */
	public static final String HELP_ADD_ACTION="helpAddAction";
	
	/** 修改问题 CONTROLLOR @RequestMapping值 */
	public static final String HELP_UPDATE_ACTION="helpUpdateAction";
	
	/** 关闭问题 CONTROLLOR @RequestMapping值 */
	public static final String HELP_CLOSE_ACTION="helpCloseAction";
	
	/** 启用问题CONTROLLOR @RequestMapping值 */
	public static final String HELP_OPEN_ACTION="helpOpenAction";
	
	/** 删除问题CONTROLLOR @RequestMapping值 */
	public static final String HELP_DEL_ACTION="helpDelAction";
	
	/** 添加到常见问题CONTROLLOR @RequestMapping值 */
	public static final String MOVE_OFTEN_ACTION="moveOftenAction";
	/** 添加到智齿常见问题CONTROLLOR @RequestMapping值 */
	public static final String MOVE_ZHICHI_ACTION="moveZhiChiAction";

	//以下是常见问题所需字段
	
	/** 常见问题列表画面 @RequestMapping值 */
	public static final String OFTEN_INIT="oftenInit";

	/** 智齿客服常见问题列表画面 @RequestMapping值 */
	public static final String ZHICHI_INIT="zhiChiInit";
	
	/** 常见问题列表页面路径 CONTROLLOR @RequestMapping值 */
	public static final String  OFTEN_LIST_VIEW="/manager/content/help/oftenlist";
	/** 智齿客服常见问题列表页面路径 CONTROLLOR @RequestMapping值 */
	public static final String  ZHICHI_LIST_VIEW="/manager/content/help/zhichilist";
	/** 常见问题列表页面数据 CONTROLLOR @RequestMapping值 */
	public static final String  OFTEN_FORM="oftenForm";
	
	/** 常见问题列表数据CONTROLLOR @RequestMapping值 */
	public static final String OFTEN_LIST="oftenlist";
	
	/** 移除常见数据CONTROLLOR @RequestMapping值 */
	public static final String MOVE_ACTION="oftenMoveAction";

	/** 移除智齿客服常见数据ZHICHI_MOVE_ACTION @RequestMapping值 */

	public static final String ZHICHI_MOVE_ACTION="zhiChiMoveAction";

	/** 权限关键字 */
	public static final String PERMISSIONS = "help";
	
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
