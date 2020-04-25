package com.hyjf.admin.finance.bankjournal;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * Created by cui on 2018/1/18.
 */
public class BankJournalDefine extends BaseDefine {

    public static final String REQUEST_MAPPING = "/finance/bankjournal";

    public static final String INIT="init";

    /**
     * 列表画面 路径
     */
    public static final String LIST_PATH = "finance/bankjournal/bankjournal_list";

    public static final String BANKJOURNAL_LIST = "bankjournal_list";

    /**
     * 导出资金明细列表 @RequestMapping值
     */
    public static final String EXPORT_BANKJOURNAL_ACTION = "exportBankJournalExcel";

    /**
     * FROM
     */
    public static final String BANKJOURNAL_FORM = "bankJournalForm";

    /**
     * 查看权限
     */
    public static final String PERMISSIONS = "bankjournal";

    /**
     * 查看权限
     */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /**
     * 文件导出权限
     */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

    //每页显示记录条数
    public static final Integer DEFAULT_PAGE_LIMIT = 50;

}
