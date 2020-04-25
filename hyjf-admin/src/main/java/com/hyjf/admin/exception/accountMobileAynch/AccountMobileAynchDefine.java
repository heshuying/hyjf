package com.hyjf.admin.exception.accountMobileAynch;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * @author 李晟
 * @version AccountMobileAynchDefine, v0.1 2018/5/9 15:16
 */

public class AccountMobileAynchDefine extends BaseDefine {


    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/exception/accountmobilesynch";

    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";

    public static final String MOBILE= "mobilelist";

    public static final String ACCOUTN= "accountlist";
    /** 手机号修改 @RequestMapping值 */
    public static final String MOBILE_LIST = "/exception/accountMobileException/mobileList";

    /** 银行卡修改 @RequestMapping值 */
    public static final String ACCOUNT_LIST = "/exception/accountMobileException/accountList";

    public static final String TO_ADD_ACTION_LIST = "/exception/accountMobileException/loancoverInfo";

    /** 列表检索 @RequestMapping值 */
    public static final String SEARCH_MOBILE_ACTION = "searchMobileAction";

    /** 列表检索 @RequestMapping值 */
    public static final String SEARCH_ACCOUNT_ACTION = "searchAccountAction";



    /** 添加信息 @RequestMapping值 */
    public static final String ADD_ACTION = "addAction";

    /** 删除信息 @RequestMapping值 */
    public static final String DELETE_ACTION = "deleteAction";

    /** 转跳添加界面 @RequestMapping值 */
    public static final String TO_ADD_MOBILE_ACTION = "toAddMobileAction";


    public static final String TO_ADD_ACCOUNT_ACTION = "toAddAccountAction";
    /** FORM */
    public static final String FORM = "mobilesynchronizeForm";

    /** 查看权限 */
    public static final String PERMISSIONS = "accountlist";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 检索权限 */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

    /** 修改权限 */
    public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

    /**手机号*/
    public static final Integer MOBILE_FLAG=1;
    /**银行卡号*/
    public static final Integer ACCOUNT_FLAG=2;
}
