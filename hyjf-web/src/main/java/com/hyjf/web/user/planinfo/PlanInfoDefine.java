/**
 * Description:我的出借常量定义
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.user.planinfo;

import com.hyjf.web.BaseDefine;

public class PlanInfoDefine extends BaseDefine {

	/** 我的计划数据统计 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/user/planinfo";

	/** 我的计划项目页面@RequestMapping值 */
	public static final String TO_MYPlAN_ACTION = "init";
	/** 我的计划项目列表@RequestMapping值 */
	public static final String PLAN_LIST_ACTION = "planlist";
	/** 我的计划画面 路径 */
	public static final String PROJECT_LIST_PATH = "user/planinfo/planinfolist";
	/** 我的计划画面 路径 */
	public static final String PROJECT_DETAIL_LOCK_PATH = "user/planinfo/planinfolockdetail";
	/** 我的计划画面 路径 */
	public static final String PROJECT_DETAIL_EXIT_PATH = "user/planinfo/planinfoexitdetail";
	/** 我的计划画面 路径 */
	public static final String PROJECT_DETAIL_PATH = "user/planinfo/planinfodetail";
	/** 我的出借项目页面@RequestMapping值 */
	public static final String 	PDF_EXPORT_ACTION = "createAgreementPDF";
	/** 我的出借项目详情企业或者个人 @RequestMapping值 */
	public static final String PROJECT_DETAIL_ACTION = "getPlanInfoDetail";
	/** 我的出借项目出借列表@RequestMapping值 */
	public static final String PROJECT_INVEST_LIST_ACTION = "investlist";
	/** 我的出借汇消费项目列表 @RequestMapping值 */
	public static final String PROJECT_CONSUME_LIST_ACTION = "consumelist";
	/** 我的出借项目还款信息 @RequestMapping值 */
	public static final String PROJECT_REPAY_LIST_ACTION = "repaylist";
	/** 我的优惠券出借项目还款信息 @RequestMapping值 */
    public static final String PROJECT_COUPON_REPAY_LIST_ACTION = "couponrepaylist";
    /** 跳转居间协议 @RequestMapping值 */
    public static final String CON_DETAIL = "/goConDetail";
	/** 统计类名 */
	public static final String THIS_CLASS= PlanInfoController.class.getName();
	
}
