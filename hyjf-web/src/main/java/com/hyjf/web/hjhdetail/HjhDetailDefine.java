package com.hyjf.web.hjhdetail;

import com.hyjf.web.BaseDefine;

public class HjhDetailDefine extends BaseDefine {
	
	/** 指定类型的项目 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/hjhdetail";
	
	/** 计划详情 @RequestMapping值 */
	public static final String PLAN_DETAIL_ACTION = "/getPlanDetail";
	
	/** 类名 */
	public static final String THIS_CLASS = HjhDetailController.class.getName();
	
	/** 计划详情 @Path值 */
	public static final String PLAN_DETAIL_PTAH = "hjhplan/product-detail";
	
	/** 计划债权 @Path值 */
	public static final String PLAN_BORROW_ACTION = "/getPlanBorrowList";
	
	/** 计划加入记录 @RequestMapping值 */
	public static final String PLAN_ACCEDE_ACTION = "/getPlanAccedeList";
	
	/** @RequestMapping值 */
	public static final String PLAN_INVEST_ACTION = "/planInvest";
	
	/** @RequestMapping值 */
	public static final String PLAN_CHECK_ACTION = "/planCheck";

	/** 项目详情 @RequestMapping值 */
    public static final String BORROW_DETAIL_ACTION = "/getBorrowDetail";
    
    /** 项目详情错误页 @Path值 */
    public static final String ERROR_PTAH = "hjhplan/error";
    
    /** 汇直投项目详情 @Path值 */
    public static final String BORROW_DETAIL_PTAH = "hjhplan/hjh-borrow-detail";
    
    /** 出借记录 @RequestMapping值 */
    public static final String BORROW_INVEST_ACTION = "/getBorrowInvest";
    
	/** 计算历史回报@RequestMapping值 */
	public static final String INVEST_INFO_ACTION="/investInfo";
	
	
	/** 出借成功@RequestMapping值 */
    public static final String INVEST_SUCCESS_PATH = "/user/invest/investSuccess";
    /** 出借失败@RequestMapping值 */
    public static final String INVEST_ERROR_PATH = "/user/invest/investError";
    
    // add by nxl  汇计划二期前端优化  新加承接记录列表显示
    /** 出借记录 @RequestMapping值 */
    public static final String BORROW_UNDERTAKE_ACTION = "/getBorrowUndertake";

}
