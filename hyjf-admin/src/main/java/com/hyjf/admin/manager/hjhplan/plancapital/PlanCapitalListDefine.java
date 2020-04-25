package com.hyjf.admin.manager.hjhplan.plancapital;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 友情连接type为1
 *
 * @author qingbing
 */
public class PlanCapitalListDefine extends BaseDefine {

    /**
     * 活动列表 CONTROLLOR @RequestMapping值
     */
    public static final String REQUEST_MAPPING = "manager/hjhplan/plancapitallist";

    /**
     * 列表画面 路径
     */
    public static final String LIST_PATH = "manager/plancapital/plancapitallist";

    /**
     * 从定向 路径
     */
    public static final String RE_LIST_PATH = "redirect:/manager/plancapital/plancapitallist/init";

    /** 跳转到复投详细画面的Action @RequestMapping值 */
    public static final String REINVEST_DETAIL_ACITON = "redirect:/manager/hjhreinvestdetail/init";

    /** 跳转到债转详细画面的Action @RequestMapping值 */
    public static final String CREDIT_DETAIL_ACITON = "redirect:/manager/hjhplan/daycreditdetail/init";

    /**
     * 迁移 路径
     */
    public static final String INFO_PATH = "manager/plancapital/plancapitallistinfo";

    /**
     * 列表画面 @RequestMapping值
     */
    public static final String INIT = "init";

    /**
     * 条件查询数据 @RequestMapping值
     */
    public static final String SEARCH_ACTION = "searchAction";

    /**
     * 迁移到复投详细画面 @RequestMapping值
     */
    public static final String REINVEST_INFO_ACTION = "reinvestInfoAction";

    /**
     * 迁移到复投详细画面 @RequestMapping值
     */
    public static final String CREDIT_INFO_ACTION = "creditInfoAction";

    /** 导出数据的 @RequestMapping值 */
    public static final String EXPORT_ACTION = "exportAction";

    /**
     * 二次提交后跳转的画面
     */
    public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

    /**
     * FROM
     */
    public static final String PLANCAPITALLIST_FORM = "planCapitalListForm";

    /**
     * 权限关键字
     */
    public static final String PERMISSIONS = "plancapitallist";

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
