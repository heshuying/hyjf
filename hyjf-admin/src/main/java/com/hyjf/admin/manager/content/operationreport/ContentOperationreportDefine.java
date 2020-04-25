package com.hyjf.admin.manager.content.operationreport;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class ContentOperationreportDefine extends BaseDefine {

    /**
     * 权限关键字
     */
    public static final String PERMISSIONS = "operationreport";
    /**
     * 运营报告 @RequestMapping值
     */
    public static final String REQUEST_MAPPING = "/manager/content/operationreport";

    /**
     * 列表画面 @RequestMapping值
     */
    public static final String INIT = "init";
    
    /**
     * 月度度列表画面 @RequestMapping值
     */
    public static final String MONTH_INIT = "monthinit";
    
    /**
     * 季度列表画面 @RequestMapping值halfyearinit
     */
    public static final String QUARTER_INIT = "quarterinit";
    /**
     * 上半年列表画面 @RequestMapping值halfyearinit
     */
    public static final String HALFYEAR_INIT = "halfyearinit";
    /**
     * 年度列表画面 @RequestMapping值halfyearinit
     */
    public static final String YEAR_INIT = "yearinit";

    /**
     * 条件查询数据 @RequestMapping值
     */
    public static final String SEARCH_ACTION = "searchAction";

    /**
     * FROM
     */
    public static final String FORM = "contentoperationreportForm";
    /**
     * 列表画面 路径
     */
    public static final String LIST_PATH = "manager/content/contentoperationreport/operationreport";
 
    /**
     * 季度列表画面 路径
     */
    public static final String QUARTER_LIST_PATH = "manager/content/contentoperationreport/quarteroperationreport";
    /**
     * 上半年度列表画面 路径
     */
    public static final String HALFYEAR_LIST_PATH = "manager/content/contentoperationreport/halfyearoperationreport";
    /**
     * 年度列表画面 路径
     */
    public static final String YEAR_LIST_PATH = "manager/content/contentoperationreport/yearoperationreport";
    
    /**
     * 月度列表画面 路径
     */
    public static final String MONTH_LIST_PATH = "manager/content/contentoperationreport/monoperationreport";

    /** 从定向 路径 */
    public static final String RE_LIST_PATH = "redirect:" + REQUEST_MAPPING + "/" + INIT;
    /**
     * 插入数据 @RequestMapping值
     */
    public static final String ADD_ACTION = "addAction";

    /**
     * 迁移到详细画面 @RequestMapping值
     */
    public static final String INFO_ACTION = "infoAction";
    /**
     * 插入数据 @RequestMapping值
     */
    public static final String PUBLISH_ACTION = "publishAction";

    public static final String EXPORT_EXCEL_ACTION = "exportExcelAction";


    /** 删除数据的 @RequestMapping值 */
    public static final String DELETE_ACTION = "deleteAction";
    /**
     * 列表画面 路径
     */
    public static final String ADD_PATH = "manager/content/contentoperationreport/operationreportinfo";

    /**
     * 详细画面的路径
     */
    public static final String INFO_PATH = "manager/content/contentoperationreport/monthoperationreport";

    /**
     * 查看权限
     */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /**
     * 检索权限
     */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

    /**
     * 修改权限
     */
    public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

    /**
     * 删除权限
     */
    public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

    /**
     * 添加权限
     */
    public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

    /**
     * 导出权限
     */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

    /**
     * 详细权限
     */
    public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;
    
    //monthOperationReportAction
    /**
     * 月度运营报告新增 @RequestMapping值
     */
 	public static final String MONTHACTION = "monthOperationReportAction";

	 /**
    * 季度运营报告新增 @RequestMapping值
    */
	public static final String QUARTERACTION = "quarterOperationReportSaveAction";

 /**
    * 季度运营报告新增 @RequestMapping值
    */
	public static final String YEARACTION = "yearOperationReportSaveAction";
	/**
	 * 上半年度运营报告新增 @RequestMapping值
	 */
	public static final String HALFYEARACTION = "halfYearOperationReportSaveAction";
	 /** 月度报告增加或修改保存 @RequestMapping值*/
	public static final String INSERTACTION_SAVE = "insertAction";
	//operationreportCommonForm
	 /** FROM */
    public static final String FORM_COOMMON = "operationreportCommonForm";
 
    //modifyMonthAction
    /** 月度报告修改页面跳转 @RequestMapping值*/
	public static final String UPDATEACTION = "updateAction";

//    /** 添加数据异常 */
//    public static final Integer ERRIR_STATUS = 0;
//    /** 运营报告类型异常 */
//    public static final Integer ERRIR_OPERATIONREPORT_TYPE = -1;
    
    public static final String UPLOAD_FILE = "uploadFileAction";
    /**
     * 月度运营报告预览
     */
    public static final String PREVIEW = "previewAction";
    public static final String YEAR_PREVIEW = "yearPreviewAction";

    /**
     * 半年度运营报告预览
     */
    public static final String PREVIEW_HALF = "previewHalfAction";

    /**
     * 季度运营报告预览
     */
    public static final String PREVIEWQUARTERACTION = "previewQuarterAction";

}