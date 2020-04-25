package com.hyjf.admin.manager.user.protocols;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 友情连接type为1
 *
 * @author qingbing
 */
public class ProtocolsDefine extends BaseDefine {

    /**
     * 活动列表 CONTROLLOR @RequestMapping值
     */
    public static final String REQUEST_MAPPING = "/manager/config/protocols";

    /**
     * 列表画面 路径
     */
    public static final String LIST_PATH = "manager/config/protocols/protocols";

    /**
     * 从定向 路径
     */
    public static final String RE_LIST_PATH = "redirect:/manager/config/protocols/init";

    /**
     * 删除后 路径
     */
    public static final String DELETE_AFTER_PATH = "manager/config/protocols/protocols";

    /**
     * 迁移 路径
     */
    public static final String INFO_PATH = "manager/config/protocols/protocolsInfo";

    /**
     * 列表画面 @RequestMapping值
     */
    public static final String INIT = "init";

    /**
     * 取得新规协议模板编号 @RequestMapping值
     */
    public static final String PROTOCOLTYPE_ACTION = "protocolTypeAction";

    /**
     * 条件查询数据 @RequestMapping值
     */
    public static final String SEARCH_ACTION = "searchAction";

    /**
     * 迁移到详细画面 @RequestMapping值
     */
    public static final String INFO_ACTION = "infoAction";

    /** 导出数据的 @RequestMapping值 */
    public static final String EXPORT_ACTION = "exportAction";

    /**
     * 插入数据 @RequestMapping值
     */
    public static final String INSERT_ACTION = "insertAction";

    /**
     * 更新数据 @RequestMapping值
     */
    public static final String UPDATE_ACTION = "updateAction";

    /**
     * 删除数据的 @RequestMapping值
     */
    public static final String DELETE_ACTION = "deleteAction";

    /**
     * 更新数据 @RequestMapping值
     */
    public static final String STATUS_ACTION = "statusAction";

    public static final String UPLOAD_FILE = "uploadFile";

    /**
     * 二次提交后跳转的画面
     */
    public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

    /**
     * FROM
     */
    public static final String PROTOCOLS_FORM = "protocolsForm";

    /**
     * 权限关键字
     */
    public static final String PERMISSIONS = "protocols";

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
