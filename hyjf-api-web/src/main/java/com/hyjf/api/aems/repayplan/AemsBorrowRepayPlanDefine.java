/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.api.aems.repayplan;

import com.hyjf.api.aems.register.AemsUserRegisterServer;
import com.hyjf.base.bean.BaseDefine;

/**
 * AEMS系统:查询还款计划Define
 *
 * @author liuyang
 * @version AemsBorrowRepayPlanDefine, v0.1 2018/10/16 17:28
 */
public class AemsBorrowRepayPlanDefine extends BaseDefine {

    /** 外部服务接口:查询还款计划 @RequestMapping */
    public static final String REQUEST_MAPPING = "/aems/repayplan";

    /** 类名 @RequestMapping */
    public static final String THIS_CLASS = AemsBorrowRepayPlanServer.class.getName();

    /** 查询还款计划 @RequestMapping */
    public static final String GET_REPAY_PLAN_ACTION = "/getRepayPlan";

    /** 查询还款计划详情 @RequestMapping */
    public static final String GET_REPAY_PLAN_DETAIL_ACTION = "/getRepayPlanDetail";
}
