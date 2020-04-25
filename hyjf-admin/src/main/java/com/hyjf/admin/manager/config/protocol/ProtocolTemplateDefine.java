package com.hyjf.admin.manager.config.protocol;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * Created by DELL on 2018/5/25.
 */
public class ProtocolTemplateDefine extends BaseDefine {
    public static final String COLON = ":";
    /**
     * 协议模板 @RequestMapping值
     */
    public static final String REQUEST_MAPPING = "/manager/config/protocol";

    /**
     * 列表画面 @RequestMapping值
     */
    public static final String INIT = "init";

    /**
     * 列表画面 路径
     */
    public static final String LIST_PATH = "manager/config/protocol/protocoltemplate";


    /** 从定向 路径 */
    public static final String RE_LIST_PATH = "redirect:/manager/config/protocol/init";
    /**
     * 权限
     */
    public static final String PERMISSIONS = "protocolView";

    /**
     * 查看权限protocolView
     */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 添加权限 */
    public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

    /** 修改权限 */
    public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

    /** 删除权限 */
    public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

    /**
     * FROM
     */
    public static final String FORM = "protocoltemplateForm";

    /** 迁移到详细画面 @RequestMapping值 */
    public static final String INFO_ACTION = "infoAction";

    /** 迁移 路径 */
    public static final String INFO_PATH = "manager/config/protocol/protocoltemplateInfo";

    /** 插入数据 @RequestMapping值 */
    public static final String INSERT_ACTION = "insertAction";

    /** 校验字段是否唯一 @RequestMapping值 */
    public static final String VALIDATION_ACTION = "validationProtocolNameAction";

    /** pdf文件上传 @RequestMapping值 */
    public static final String UPLOAD_FILE = "uploadFile";


    /** 修改数据 @RequestMapping值 */
    public static final String UPDATE_ACTION = "updateAction";

    /** 修改数据 @RequestMapping值 */
    public static final String UPDATE_EXIST_ACTION = "updateExistProtocol";

    /** 迁移到查看详细画面 @RequestMapping值 */
    public static final String INFOINFO_ACTION = "infoInfoAction";

    /** 查看页面迁移 路径 */
    public static final String INFOINFO_PATH = "manager/config/protocol/protocoltemplateInfoInfo";

    /** 删除数据的 @RequestMapping值 */
    public static final String DELETE_ACTION = "deleteAction";


}
