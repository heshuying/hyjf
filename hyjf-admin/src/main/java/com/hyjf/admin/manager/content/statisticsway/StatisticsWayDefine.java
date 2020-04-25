package com.hyjf.admin.manager.content.statisticsway;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 运营报告---统计方式配置
 * Created by xiehuili on 2018/6/20.
 */
public class StatisticsWayDefine extends BaseDefine {

    /**
     * 权限关键字
     */
    public static final String PERMISSIONS = "statisticsway";
    /**
     * 统计方式配置 @RequestMapping值
     */
    public static final String REQUEST_MAPPING = "/manager/content/statisticsway";

    /**
     * 列表画面 @RequestMapping值
     */
    public static final String INIT = "init";

    public static final String EXPORT_EXCEL_ACTION = "exportExcelAction";

    /**
     * 迁移到详细画面 @RequestMapping值
     */
    public static final String INFO_ACTION = "infoAction";

    /**
     * 校验字段是否唯一 @RequestMapping值
     */
    public static final String VALIDATE_ACTION = "validationField";

    /**
     * 添加统计方式配置 @RequestMapping值
     */
    public static final String INSERT_ACTION = "insertAction";

    /**
     * 修改统计方式配置 @RequestMapping值
     */
    public static final String UPDATE_ACTION = "updateAction";
    /**
     * 删除统计方式配置 @RequestMapping值
     */
    public static final String DELETE_ACTION = "deleteAction";


    /** 列表画面 路径 */
    public static final String RE_LIST_PATH = "redirect:/manager/content/statisticsway/init";
    /**
     * FROM
     */
    public static final String FORM = "statisticswayForm";

    /**
     * 列表画面 路径
     */
    public static final String LIST_PATH = "manager/content/statisticsway/statisticsway";

    /**
     * 详情画面 路径
     */
    public static final String INFO_PATH = "manager/content/statisticsway/statisticswayinfo";

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

}
