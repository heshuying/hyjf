/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.common.cache;

/**
 * redis专用常量
 * @author fp
 * @version RedisConstants, v0.1 2018/3/27 15:43
 */
public class RedisConstants {
    /**
     * 记录密码错误次数Redis前缀web端
     */
    public static final String PASSWORD_ERR_COUNT_APP = "password_err_count_app_";

    /**
     * 记录密码错误次数Redis前缀web端
     */
    public static final String PASSWORD_ERR_COUNT_WEB = "password_err_count_web_";
    /**
     * 记录密码错误次数Redis前缀admin端
     */
    public static final String PASSWORD_ERR_COUNT_ADMIN = "password_err_count_admin_";
    
    /**
     * 记录密码错误次数Redis前缀.web.app三端共享
     */
    public static final String PASSWORD_ERR_COUNT_ALL = "password_err_count_all_";

    /**
     * 用户一秒内的登录次数(ip)
     */
    public static final String LOGIN_ONE_COUNT_WEB = "login_one_count_web_";

    /**
     * 用户一秒内的登录次数(ip)
     */
    public static final String LOGIN_ONE_COUNT_APP = "login_one_count_app_";

    /**
     * 恶意攻击登录ip黑名单
     */
    public static final String LOGIN_BLACK_LIST_WEB = "login_black_list_web";

    /**
     * 恶意攻击登录ip黑名单
     */
    public static final String LOGIN_BLACK_LIST_APP = "login_black_list_app";


    /**
     * 协议模板---协议文件存储Redis前缀
     */
    public static final String PROTOCOL_TEMPLATE_URL = "protocol_template_url_";

    /**
     * 协议模板---协议前台展示名称的别名
     */
    public static final String PROTOCOL_TEMPLATE_ALIAS = "protocol_template_Alias_";

    /**
     * 协议模板二期---动态展示协议前台展示名称
     */
    public static final String PROTOCOL_PARAMS = "params";

    /**
     * 群发短信--剩余短信条数
     */
    public static final String REMAIN_NUMBER = "remain_number";

    /**
     * 群发短信--短信余额
     */
    public static final String REMAIN_MONEY = "remain_money";
    /**
     * 用户画像评分
     */
    public static final String USERPORTRAIT_SCORE = "userPortrait_score";
    //登录失败配置项
    public static final String LOGIN_LOCK_CONFIG = "login_lock_config";
    /**
     * 测评到期时间和测评结果出借金额上限用
     */
    public static final String REVALUATION = "evaluation:";

    /**
     * 测评到期时间
     */
    public static final String REVALUATION_EXPIRED_DAY = REVALUATION  + "evaluation_expired_day";

    /**
     * 保守型
     */
    public static final String REVALUATION_CONSERVATIVE = REVALUATION  + "conservative";

    /**
     * 稳健型
     */
    public static final String REVALUATION_ROBUSTNESS = REVALUATION  + "robustness";

    /**
     * 成长型
     */
    public static final String REVALUATION_GROWTH = REVALUATION  + "growth";

    /**
     * 进取型
     */
    public static final String REVALUATION_AGGRESSIVE =  REVALUATION  + "aggressive";


    /**
     * 保守型（代收本金）
     */
    public static final String REVALUATION_CONSERVATIVE_PRINCIPAL = REVALUATION  + "principalconservative";

    /**
     * 稳健型（代收本金）
     */
    public static final String REVALUATION_ROBUSTNESS_PRINCIPAL = REVALUATION  + "principalrobustness";

    /**
     * 成长型（代收本金）
     */
    public static final String REVALUATION_GROWTH_PRINCIPAL = REVALUATION  + "principalgrowth";

    /**
     * 进取型（代收本金）
     */
    public static final String REVALUATION_AGGRESSIVE_PRINCIPAL =  REVALUATION  + "principalaggressive";
    /**
     * 逾期原因
     */
    public static final String LATER_REPAY_REASON = "LATER_REPAY_REASON";
    /**
     * 北互金历史上报定时任务开始时间(BifaIndexHistoryDataReportHandleTask)
     */
    public static final String BIFA_HISTORY_START_YYYYMMDD = "bifa_history_start_yyyymmdd";
    /**
     * 北互金历史上报定时任务结束时间(BifaIndexHistoryDataReportHandleTask)
     */
    public static final String BIFA_HISTORY_END_YYYYMMDD = "bifa_history_end_yyyymmdd";
    /**
     * 北互金历史上报定时任务执行开关(0:关闭；1：开启)(BifaIndexHistoryDataReportHandleTask)
     */
    public static final String BIFA_HISTORY_OPEN_FLAG = "bifa_history_open_flag";

}
