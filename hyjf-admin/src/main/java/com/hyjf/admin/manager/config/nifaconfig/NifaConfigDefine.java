/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.config.nifaconfig;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * @author nixiaoling
 * @version NifaConfigBean, v0.1 2018/7/4 11:46
 */
public class NifaConfigDefine extends BaseDefine{


    /** 互金配置 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/config/nifaconfig";

    /** 互金配置转发 */
    public static final String RE_LIST_PATH = "redirect:/manager/config/nifaconfig/init";
    public static final String RE_CONTRACT_PATH = "redirect:/manager/config/nifaconfig/initContract";

    /** 列表画面 路径 */
    public static final String LIST_PATH_FIELD= "manager/config/nifaconfig/fielddefinition";
    public static final String LIST_PATH_CONTRACT = "manager/config/nifaconfig/contracttemplate";


    /** 添加和修改画面的路径 */
    public static final String INFO_PATH_FIELD = "manager/config/nifaconfig/fielddefinitionInfo";
    /** 查看画面的路径 */
    public static final String DETAIL_PATH_FIELD = "manager/config/nifaconfig/fielddefinitionDetail";
    //
    public static final String INFO_PATH_CONTRACT = "manager/config/nifaconfig/contracttemplateInfo";

    /** 删除后 路径 */
    public static final String DELETE_AFTER_PATH = "manager/config/nifaconfig/nifaconfig";

    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";
    public static final String INITCONTRACT = "initContract";

    /** 迁移到详细画面 @RequestMapping值 */
    public static final String INFO_ACTION_FIELD = "/fieldInfoAction";
    public static final String DETAIL_ACTION_FIELD = "/detailAction";
    public static final String INFO_ACTION_CONTRACT = "/contractInfoAction";

    /** 插入数据 @RequestMapping值 */
    public static final String INSERT_ACTION_FIELD = "fieldInsertAction";
    public static final String INSERT_ACTION_CONTRACT = "contractInsertAction";

    /** 更新数据 @RequestMapping值 */
    public static final String UPDATE_ACTION_FIELD = "fieldUpdateAction";
    public static final String UPDATE_ACTION_CONTRACT = "contractUpdateAction";

    /** 删除数据的 @RequestMapping值 */
    public static final String DELETE_ACTION_CONTRACT = "contractDeleteAction";

    /** 数据的 @RequestMapping值 */
    public static final String CHECK_ACTION = "checkAction";

    /** 二次提交后跳转的画面 */
    public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

    /** FROM */
    public static final String HZRCONFIG_FORM = "nifaconfigForm";

    /** 查看权限 */
    public static final String PERMISSIONS = "nifaconfig";


    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 修改权限 */
    public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

    /** 删除权限 */
    public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

    /** 添加权限 */
    public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

    public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;

}
