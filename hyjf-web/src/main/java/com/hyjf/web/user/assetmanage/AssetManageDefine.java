/**
 * Description:出借常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 * Created at: 2015年12月4日 下午2:33:39
 * Modification History:
 * Modified by :
 */

package com.hyjf.web.user.assetmanage;

import com.hyjf.web.BaseDefine;

public class AssetManageDefine extends BaseDefine {

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/user/assetmanage";
	/** 资产管理页面初始化 */
    public static final String INIT_ACTION = "/init";
    /** 当前持有债权 */
    public static final String GET_CURRENT_HOLD_OBLIGATORY_RIGHT_ACTION="/getCurrentHoldObligatoryRight";
    /** 已回款债权 */
    public static final String GET_REPAY_MENT_ACTION="/getRepayMent";
    /** 债权转让记录 */
    public static final String GET_CREDIT_RECORD_LIST_ACTION="/getMyCreditRecordList";
    /** 债权转让承接明细 */
    public static final String GET_MYCREDIT_ASSIGN_ACTION = "/getMyCreditAssignDetail";
    /** 当前持有计划 */
    public static final String GET_CURRENT_HOLD_PLAN_ACTION="/getCurrentHoldPlan";
    
    
    /** 已回款计划 */
    public static final String GET_REPAY_MENT_PLAN_ACTION="/getRepayMentPlan";
    
    /** 当前持有计划 */
    public static final String GET_REPAYMENT_PLAN_ACTION="/getRepaymentPlan";
    
    /** 资产管理页面 */
    public static final String ASSET_MANAGE_INIT_PATH = "/user/assetmanage/rights-manage";
    
    /** 我的出借项目详情企业或者个人 @RequestMapping值 */
    public static final String TO_MY_PLAN_INFO_DETAIL_PAGE_ACTION = "toMyPlanInfoDetailPage";
    
    /** 汇计划出借项目详情企业或者个人 @RequestMapping值 */
    public static final String TO_MY_HJH_PLAN_INFO_DETAIL_PAGE_ACTION = "toMyHjhPlanInfoDetailPage";
    
    /** 我的计划画面 路径 */
    public static final String TO_MY_PLAN_INFO_DETAIL_PAGE_PATH = "user/assetmanage/planinfodetail";
    
    
    /** 我的计划画面 路径 */
    public static final String TO_MY_HJH_PLAN_INFO_DETAIL_PAGE_PATH = "user/assetmanage/hjh-invest-detail";
    
    /** 我的计划项目列表 @RequestMapping值 */
    public static final String BORROW_LIST_ACTION = "/getBorrowList";
}
