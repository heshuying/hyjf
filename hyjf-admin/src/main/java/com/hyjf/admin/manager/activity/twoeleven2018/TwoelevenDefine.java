package com.hyjf.admin.manager.activity.twoeleven2018;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * @author xiehuili on 2018/10/10.
 */
public class TwoelevenDefine extends BaseDefine {

    /** 查看权限 */
    public static final String PERMISSIONS = "activitylist";

    /** FROM */
    public static final String PRIZECODE_FORM = "twoelevenForm";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/activity/twoeleven2018";

    /** 秒杀明细列表画面 @RequestMapping值 */
    public static final String INIT = "init";
    /** 出借明细列表画面 @RequestMapping值 */
    public static final String INVEST_INIT = "investInit";
    /** 奖励明细列表画面 @RequestMapping值 */
    public static final String REWARD_INIT = "rewardInit";


    /** 迁移到详细画面 @RequestMapping值 */
    public static final String INFO_ACTION = "infoAction";

    /** 奖励明细修改 @RequestMapping值 */
    public static final String UPDATE_ACTION = "updateAction";

    /** 秒杀明细画面 路径 */
    public static final String LIST_PATH = "manager/activity/twoeleven2018/seckill";
    /** 出借明细画面 路径 */
    public static final String INVEST_LIST_PATH = "manager/activity/twoeleven2018/invest";
    /**奖励明细 列表画面 路径 */
    public static final String REWARD_LIST_PATH = "manager/activity/twoeleven2018/reward";
    /**奖励明细 修改页面 路径 */
    public static final String REWARD__INFO_LIST_PATH = "manager/activity/twoeleven2018/rewardInfo";

    /** 从定向 路径 */
    public static final String RE_LIST_PATH = "redirect:" + REWARD_INIT;


    public static final String EXPORT_EXCEL_ACTION = "exportExcelAction";
    public static final String EXPORT_EXCEL_INVEST_ACTION = "exportExcelInvestAction";
    public static final String EXPORT_EXCEL_REWARD_ACTION = "exportExcelRewardAction";

    /**
     * 导出权限
     */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
