package com.hyjf.admin.manager.config.protocolog;

import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * Created by DELL on 2018/6/4.
 */
public class ProtocolTemplateLogDefine {
    /**
     * 协议模板 @RequestMapping值
     */
    public static final String REQUEST_MAPPING = "/manager/config/protocolog";

    /**
     * 列表画面 @RequestMapping值
     */
    public static final String INIT = "init";

    /**
     * 权限
     */
    public static final String PERMISSIONS = "protocolLogView";

    /**
     * 查看权限protocolView
     */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /**
     * 列表画面 路径
     */
    public static final String LIST_PATH = "manager/config/protocolog/protocoltemplatelog";
    /**
     * FROM
     */
    public static final String FORM = "protocolLogForm";



}
