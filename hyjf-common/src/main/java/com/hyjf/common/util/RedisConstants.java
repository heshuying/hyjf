package com.hyjf.common.util;

/**
 * <p>
 * 常量文件
 * </p>
 *
 * @author gogtz
 * @version 1.0.0
 */
public class RedisConstants {
    /** 直投类放款任务名称 */
	public static final String ZHITOU_LOAN_TASK = "zhitouLoan";
	/**计划类实时放款 add by cwyang 2017-10-23*/
	public static final String PLAN_REALTIME_LOAN_TASK = "planRealtimeLoan";
	
	/**直投类实时放款 add by cwyang 2017-10-23*/
	public static final String ZHITOU_REALTIME_LOAN_TASK = "zhitouRealtimeLoan";
	/** 计划类放款任务名称 */
	public static final String PLAN_LOAN_TASK = "planLoan";
	/** 放款请求任务名称 */
	public static final String LOAN_REQUEST_TASK = "loanRequest";
	
	/** 直投类还款任务名称 */
	public static final String ZHITOU_REPAY_TASK = "zhitouRepay";
	/** 计划类还款任务名称 */
	public static final String PLAN_REPAY_TASK = "planRepay";
	/** 还款请求任务名称 */
	public static final String REPAY_REQUEST_TASK = "repayRequest";

    // 风险保证金前缀
    public static final String CAPITAL_TOPLIMIT_ = "CAPITAL_TOPLIMIT_";

    // 汇计划队列前缀
    public static final String HJH_PLAN_LIST = "QUEUE_";

    // 汇计划可投余额前缀
    public static final String HJH_PLAN = "HJHBAL_";

    // 汇计划标的队列标识 债转（债转标的）
    public static final String HJH_BORROW_CREDIT = "CREDIT";
    // 汇计划标的队列标识 出借（原始标的）
    public static final String HJH_BORROW_INVEST = "INVEST";
    
    // 汇计划分割线
    public static final String HJH_SLASH = "_";

    // 汇计划发标redis key
    public static final String GEN_HJH_BORROW_NID = "gen_hjh_borrow_nid";
    
    // 汇计划自动债转中的标志 redis key
    public static final String HJH_DEBT_SWAPING = "DebtSwaping";

    // 汇计划进入锁定期处理中队列
    public static final String HJH_LOCK_REPEAT = "hjh_lockisrepeat";

    // add 汇计划三期 汇计划自动出借(分散出借) liubin 20180515 start
    // _tmp
    public static final String HJH_SLASH_TMP = "_tmp";
    // add 汇计划三期 汇计划自动出借(分散出借) liubin 20180515 end

    //线下充值类型
    public static final String UNDER_LINE_RECHARGE_TYPE = "UNDER_LINE_RECHARGE_TYPE";

    //某计划连续开放额度不同次数
    public static final String CONT_WARN_OF_HJH_ACCOUNT = "contWarnOfHjhAccount";

    /**
     * 日推标上限额度前缀
     */
    public static final String DAY_MARK_LINE = "DAY_MARK_LINE_";

    /**
     * 月推标上限额度前缀
     */
    public static final String MONTH_MARK_LINE = "MONTH_MARK_LINE_";

    /**
     * 日推标累计额度前缀
     */
    public static final String DAY_MARK_ACCUMULATE = "DAY_MARK_ACCUMULATE_";

    /**
     * 日发标已用
     */
    public static final String DAY_USED = "DAY_USED_";

    /**
     * 月发标已用
     */
    public static final String MONTH_USED = "MONTH_USED_";

    /**
     * 中秋国庆活动
     */
    public static final String MIDAU_ACTIVITY = "MIDAU_ACTIVITY_";

    /**
     * 纳觅返现活动
     */
    public static final String RETURN_CASH_ACTIVITY = "RETURN_CASH_ACTIVITY_";
    /**
     * 纳觅返现活动
     */
    public static final String RETURN_CASH_ACTIVITY_ORDERID = "RETURN_CASH_ACTIVITY_ORDERID_";
    /**
     * 中秋国庆活动的用户
     */
    public static final String MIDAU_ACTIVITY_USER_ = "MIDAU_ACTIVITY_USER_";
    /**
     * 中秋国庆
     */
    public static final String MIDAU_ACTIVITY_ORDERID_ = "MIDAU_ACTIVITY_ORDERID_";

    /** 双十一 每个优惠券的数量 **/
    public static final String TWO_ELEVEN_COUPON_NUMBER_ = "TWO_ELEVEN_COUPON_NUMBER_";

    /**
     * 双十一 每个优惠券的发出数量
     */
    public static final String TWO_ELEVEN_COUPON_SENT_NUMBER_ = "TWO_ELEVEN_COUPON_SENT_NUMBER_";

    /**
     * 双十一 防刷token
     */
    public static final String TWO_ELEVEN_ACTIVITY_TOKEN = "TWO_ELEVEN_ACTIVITY_TOKEN";

    /**
     * 双十一 优惠券竞争Redis锁KEY
     */
    public static final String TWO_ELEVEN_COUPON_COMPETE_ = "TWO_ELEVEN_COUPON_COMPETE_";

    /**
     * 优惠券发放
     */
    public static final String SEND_COUPON_ACTIVITY_ = "SEND_COUPON_ACTIVITY_";

    /**
     * 互金历史数据处理天数
     */
    public static final String NIFA_DUAL_DAYS = "NIFA_DUAL_DAYS";

    /**
     * 互金历史数据处理天数
     */
    public static final String NIFA_START_DATE = "NIFA_START_DATE";
    
    /**
     * 互金历史数据处理结束日期
     */
    public static final String NIFA_END_DATE = "NIFA_END_DATE";

    /**合规数据上报 CERT 批次号自增 序列号*/
    public static final String CERT_BATCH_NUMBER_SEQ_ID = "CERT_BATCH_NUMBER_SEQ_ID";
    /**合规数据上报 CERT 是否初始化标的  没有 就是没初始化*/
    public static final String CERT_BATCH_IS_INIT_BORROW= "CERT_BATCH_IS_INIT_BORROW";

    /**合规数据上报 CERT 是否开始实时上报数据  0不上报  1上报*/
    public static final String CERT_CAN_RUN= "CERT_CAN_RUN";
}
