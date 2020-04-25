package com.hyjf.admin.maintenance.log;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.StringPool;

public class MaintenanceLogDefine extends BaseDefine{
	/** CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/maintenance/log";

	/** 路径 */
	public static final String LIST_PATH = "maintenance/log/log_list";
	/** 列表画面 @RequestMapping值,所有员工列表 */
	public static final String INIT = "/init";
	/** FROM */
	public static final String MAINTENANCELOG_FORM = "maintenanceLogForm";
	
	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;
	
	
	/** ACTION*/
	/** 查询数据 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";
    
	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_ACTION = "insertAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	/** 删除数据的 @RequestMapping值 */
	public static final String DELETE_ACTION = "deleteAction";

    /** 检查是否唯一 @RequestMapping值 */
    public static final String CHECK_ACTION = "checkAction";


}
