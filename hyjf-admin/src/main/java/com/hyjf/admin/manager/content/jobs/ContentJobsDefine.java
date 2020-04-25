package com.hyjf.admin.manager.content.jobs;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class ContentJobsDefine extends BaseDefine {

    /** 活动列表 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/content/contentjobs";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "manager/content/contentjobs/contentjobs";

    /** 从定向 路径 */
    public static final String RE_LIST_PATH = "redirect:/manager/content/contentjobs/init";

    /** 迁移 路径 */
    public static final String INFO_PATH = "manager/content/contentjobs/contentjobsInfo";

    /** 删除后 路径 */
    public static final String DELETE_AFTER_PATH = "manager/content/contentjobs/contentjobs";

    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";

    /** 迁移到详细画面 @RequestMapping值 */
    public static final String INFO_ACTION = "infoAction";

    /** 条件查询数据 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";

    /** 插入数据 @RequestMapping值 */
    public static final String INSERT_ACTION = "insertAction";

    /** 插入数据 @RequestMapping值 */
    public static final String STATUS_ACTION = "statusAction";

    /** 更新数据 @RequestMapping值 */
    public static final String UPDATE_ACTION = "updateAction";

    /** 删除数据的 @RequestMapping值 */
    public static final String DELETE_ACTION = "deleteAction";

    /** FROM */
    public static final String CONTENTJOBS_FORM = "contentjobsForm";

	/** 权限关键字 */
	public static final String PERMISSIONS = "contentjobs";
	
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
