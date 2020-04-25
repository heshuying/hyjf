/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 * 
 * _ooOoo_
 * o8888888o
 * 88" . "88
 * (| -_- |)
 * O\ = /O
 * ____/`---'\____
 * .' \\| |// `.
 * / \\||| : |||// \
 * / _||||| -:- |||||- \
 * | | \\\ - /// | |
 * | \_| ''\---/'' | |
 * \ .-\__ `-` ___/-. /
 * ___`. .' /--.--\ `. . __
 * ."" '< `.___\_<|>_/___.' >'"".
 * | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * \ \ `-. \_ __\ /__ _/ .-` / /
 * ======`-.____`-.___\_____/___.-`____.-'======
 * `=---='
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * 佛祖保佑 永无BUG
 */

package com.hyjf.web.plan.repay;

import com.hyjf.web.BaseDefine;

/**
 * 汇添金用户还款定义
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年10月9日
 * @see 上午11:42:30
 */
public class PlanUserRepayDefine extends BaseDefine {

    /** 指定类型的项目 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/plan/userRepay";

    /** 还款 @RequestMapping值 */
    public static final String REPAY_ACTION = "/repay";

    /** 还款详情页面 @RequestMapping值 */
    public static final String REPAY_DETAIL_ACTION = "/repayDetail";

    /** 汇添金专属表还款详情页面 */
    public static final String REPAY_DETAIL_HTJ_PAGE = "/user/repay/user_repay_detail_htj";
}
