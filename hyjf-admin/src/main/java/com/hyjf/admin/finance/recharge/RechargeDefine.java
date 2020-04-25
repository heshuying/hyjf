package com.hyjf.admin.finance.recharge;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class RechargeDefine extends BaseDefine {

    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/finance/recharge";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "finance/recharge/recharge_list";
    /** 列表画面 路径 */
    public static final String LISTPT_PATH = "finance/recharge/rechargePT_list";
    /** 手动充值页    */
    public static final String RECHARGE_HAND_PATH = "finance/recharge/handrecharge";
    /** 详细画面的路径 */
//  public static final String INFO_PATH = "finance/recharge/recharge_info";

    /** FROM */
    public static final String RECHARGE_FORM = "rechargeForm";
    /** 账户管理 列表    */
    public static final String RECHARGE_LIST = "recharge_list";
    /** 账户管理 列表 c查询条件    */
    public static final String RECHARGE_LIST_WITHQ = "searchAction";
    /** 账户管理 列表    */
    public static final String RECHARGEPT_LIST = "rechargePT_list";
    /** 账户管理 列表 c查询条件    */
    public static final String RECHARGEPT_LIST_WITHQ = "searchPTAction";
    
	/** 用户转账条件校验 @RequestMapping值 */
	public static final String CHECK_TRANSFER_ACTION = "checkTransfer";
    /** FIX    */
    public static final String RECHARGE_FIX ="rechargeFixAction";
    /** 导出数据 @RequestMapping值 */
    public static final String EXPORT_ACTION = "exportAction";

    /** 导出列表 @RequestMapping值 具有 组织机构查看权限*/
    public static final String ENHANCE_EXPORT_ACTION = "enhanceExportAction";

    /** 导出数据 @RequestMapping值 */
    public static final String EXPORTPT_ACTION = "exportPTAction";
    /** 转到手动充值页面    */
    public static final String TO_HANDRECHARGE = "tohandrechargeAction";
    /** 手动充值   */
    public static final String HANDRECHARGE = "handrechargeAction";
    /** 修改充值状态  @RequestMapping 值*/
    public static final String MODIFY_ACTION = "modifyAction";
    
    /** 查看权限 */
    public static final String PERMISSIONS = "recharge";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 修改权限 */
    public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

    /** 删除权限 */
    public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

    /** 添加权限 */
    public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

    /** 充值确认权限 */
    public static final String PERMISSIONS_FIX = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_CONFIRM;

    /** 文件导出权限 */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

    public static final String ENHANCE_PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.ORGANIZATION_VIEW;

    /** 转到手动充值权限 */
    public static final String PERMISSIONS_TOHANDRECHARGE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

    /** 数据脱敏权限*/
	public static final String PERMISSION_HIDE_SHOW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_HIDDEN_SHOW;
	
    public static final String STATUS_SUCCESS = "1";

}
