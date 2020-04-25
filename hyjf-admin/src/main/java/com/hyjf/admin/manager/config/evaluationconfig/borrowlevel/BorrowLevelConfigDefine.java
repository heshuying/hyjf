/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.config.evaluationconfig.borrowlevel;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 风险测评配置- 信用等级配置Define
 *
 * @author liuyang
 * @version BorrowLevelConfigDefine, v0.1 2018/11/28 17:17
 */
public class BorrowLevelConfigDefine extends BaseDefine {

    /**
     * 风险测评配置- 信用等级配置 CONTROLLOR @RequestMapping值
     */
    public static final String REQUEST_MAPPING = "/manager/config/borrowlevelconfig";
    /**
     * 列表画面 @RequestMapping值
     */
    public static final String INIT = "init";
    /**
     * 详情画面 路径
     */
    public static final String INFO_PATH = "manager/config/evaluationconfig/borrowlevelconfig/borrowlevelconfig";
    /**
     * 列表画面 路径
     */
    public static final String LIST_PATH = "manager/config/evaluationconfig/borrowlevelconfig/borrowlevelconfiglist";

    /**
     * 更新数据 @RequestMapping值
     */
    public static final String UPDATE_ACTION = "updateAction";

    /**
     * 详情页 @RequestMapping值
     *
     */
    public static final String INFO_ACTION ="infoAction";

    /**
     * FORM
     */
    public static final String BORROW_LEVEL_CONFIG_FORM = "borrowLevelCofigForm";

    /**
     * 权限关键字
     */
    public static final String PERMISSIONS = "borrowLevelConfig";

    /**
     * 查看权限
     */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /**
     * 修改权限
     */
    public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

    /**
     * 详细权限
     */
    public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;

}
