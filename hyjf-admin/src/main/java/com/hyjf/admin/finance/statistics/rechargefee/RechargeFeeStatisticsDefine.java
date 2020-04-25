package com.hyjf.admin.finance.statistics.rechargefee;

import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class RechargeFeeStatisticsDefine {

    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/finance/statistics/rechargefee";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "finance/statistics/rechargefee/rechargefee_statistics_list";
    
    /** 统计图表画面 路径 */
    public static final String STATIS_PATH = "finance/statistics/rechargefee/rechargefee_statistics";
    
    /** FROM */
    public static final String RECHARGE_FEE_STATISTICS_FORM = "rechargeFeeStatisticsForm";
    /** 充值手续费统计 列表    */
    public static final String INIT = "init";
    
    /** 充值手续费统计 页面    */
    public static final String STATISTICS = "statistics";
    
    /** 充值手续费统计 列表 c查询条件    */
    public static final String SEARCH_ACTION= "searchAction";
    /** 导出数据 @RequestMapping值 */
    public static final String EXPORT_ACTION = "exportAction";
    
    /** 充值手续费统计 图表    */
    public static final String STATISTICS_SEARCH_ACTION= "statisSearchAction"; 
    
    /** 查看权限 */
    public static final String PERMISSIONS = "refeestatistic";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 文件导出权限 */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

    public static final String STATUS_SUCCESS = "1";

}
