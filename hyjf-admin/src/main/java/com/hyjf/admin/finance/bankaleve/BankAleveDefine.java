package com.hyjf.admin.finance.bankaleve;

import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * Created by cuigq on 2018/1/22.
 */
public class BankAleveDefine {

    public static final String REQUEST_MAPPING = "/finance/bankaleve";

    public static final String INIT = "init";

    /**
     * 列表画面 路径
     */
    public static final String LIST_PATH = "finance/bankaleve/bankaleve_list";

    public static final String BANKALEVE_LIST = "bankaleve_list";

    /**
     * 导出资金明细列表 @RequestMapping值
     */
    public static final String EXPORT_BANKALEVE_ACTION = "exportBankAleveExcel";

    /**
     * FROM
     */
    public static final String BANKALEVE_FORM = "bankAleveForm";

    /**
     * 查看权限
     */
    public static final String PERMISSIONS = "bankaleve";

    /**
     * 查看权限
     */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /**
     * 文件导出权限
     */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
    public static final int DEFAULT_PAGE_LIMIT = 50;
}
