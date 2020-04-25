/**
 * 网站协议下载
 */
package com.hyjf.web.agreement;

import com.hyjf.web.BaseDefine;

public class CreateAgreementDefine extends BaseDefine {
	
	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/createAgreementPDF";
	
	/** 个人中心-散标-当前持有-成绩债转下载(债权转让协议PDF下载 )请求*/
	public static final String CREDIT_TRANSFER_AGREEMENT_PDF = "/creditTransferAgreementPDF";
	/** 用户中心 转让中与已转让协议 */
	public static final String CREDIT_CONTRACT = "/usercreditcontract";
	/** 用户中心出借可债转资源详细 */
	public static final String TENDER_TO_CREDIT_DETAIL = "/tendertocreditdetail";
	/** 债权还款计划 */
	public static final String CREDIT_DIARY = "/creditDiary";
	
	/** 债权还款计划 --散标*/
    public static final String CREDIT_PAYMENT_PLAN = "/creditPaymentPlan";
    public static final String CREDIT_PAYMENT_PLAN_REPAY = "/createAgreementPDFFileRepay";
    /** 查看法大大协议 */
    public static final String SELECT_FDD_PAYMENT_PLAN = "/selectFaddPDFFile";
    /** 下载法大大协议 直接下载打包，返回成功状态 */
    public static final String CREDIT_FDD_PAYMENT_PLAN = "/createFaddPDFFile";
    public static final String CREDIT_FDD_PAYMENT_PLAN_IMG_ONE = "/createFaddPDFImgFileOne";
    public static final String CREDIT_FDD_PAYMENT_PLAN_IMG = "/createFaddPDFImgFile";
	/** 汇计划还款计划 */
	public static final String PLAN_PAYMENT_PLAN = "/planPaymentPlan";
	/** 汇添金出借计划服务协议PDF下载 */
	public static final String HTJ_INVEST_PLAN_AGREEMENT_PDF = "/htjInvestPDF";
	/** 新汇计划出借计划服务协议PDF下载 */
    public static final String NEW_HJH_INVEST_PLAN_AGREEMENT_PDF = "/newHjhInvestPDF";
	/**账户中心-债权管理-订单详情 债转被出借的协议 下载*/
	public static final String PLAN_CREDIT_CONTRACT_ASSIGN_SEAL_PDF = "planusercreditcontractsealpdf";
	
	/**账户中心-资产管理-当前持有-- 出借协议(实际为散标居间协议)下载*/
	public static final String INTERMEDIARY_SERVICE_AGREEMENT_PDF = "/intermediaryAgreementPDF";
	
	/** 债权转让协议PDF下载(汇计划) */
	public static final String CREDIT_HJH_TRANSFER_AGREEMENT_PDF = "/creditHJHTransferAgreementPDF";
	
}
