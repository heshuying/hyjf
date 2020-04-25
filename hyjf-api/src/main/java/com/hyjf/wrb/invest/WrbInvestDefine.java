package com.hyjf.wrb.invest;

import com.hyjf.base.bean.BaseDefine;

public class WrbInvestDefine extends BaseDefine {

    /**  REQUEST_MAPPING */
    public static final String REQUEST_MAPPING = "/wrb_interface";

    /** 获取某天出借情况 */
    public static final String DAY_INVEST_LIST = "/day_invest_list";

    /** 标的查询接口 */
    public static final String BID_INVEST_LIST = "/bid_invest_list";

    /** 查询每日汇总数据 */
    public static final String GET_DAY_SUM = "/ws_sum";
  
    /** 查询用户账户信息 */
    public static final String GET_ACCOUNT_INFO = "/acc_info";

    /** 查询标的情况 */
    public static final String BID_BORROW_LIST = "/borrow_list";

    /** 出借记录查询接口 */
    public static final String INVEST_RECORD = "/acc_invest";

    /** 出借记录回款计划 */
    public static final String RECOVER_PLAN = "/acc_back_list";
}
