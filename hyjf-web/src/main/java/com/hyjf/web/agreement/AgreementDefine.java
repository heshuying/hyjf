/**
 * 设置交易密码
 */
package com.hyjf.web.agreement;

import com.hyjf.web.BaseDefine;

public class AgreementDefine extends BaseDefine {

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/agreement";
	

	/** 汇盈金服出借咨询与管理服务协议@RequestMapping值 */
	public static final String INVESTMENT_ADVISORY_AND_MANAGEMENT_SERVICES_ACTION = "/investmentAdvisoryAndManagementServices";
	/** 散标风险揭示书@RequestMapping值 */
    public static final String CONFIRMATION_OF_INVESTMENT_RISK_ACTION = "/confirmationOfInvestmentRisk";
    
    /** 散标-转让-平台债权转让协议请求@RequestMapping值 */
    public static final String TRANSFER_OF_CREDITOR_RIGHT_ACTION = "/transferOfCreditorRight";
    
    /** 汇添金出借计划服务协议@RequestMapping值 */
    public static final String HTJ_INVESTMENT_PLANNING_SERVICES_ACTION = "/htjInvestmentPlanningServices";
    
    /** 出借协议@RequestMapping值 */
    public static final String DIARY_SERVICES_ACTION = "/diaryServices";
    /** 汇盈金服互联网金融服务平台居间服务协议@RequestMapping值 */
    public static final String INTERMEDIARY_SERVICES_ACTION = "/intermediaryServices";
    
    /** 汇盈金服互联网金融服务平台居间服务协议--s@RequestMapping值 */
    public static final String INITBORROW_INTERMEDIARY_SERVICES_ACTION = "/initBorrowIntermediaryServices";
    /** 江西银行网络交易资金账户服务三方协议@RequestMapping值 */
    public static final String JX_BANK_SERVICE_ACTION = "/jxBankService";
    /** 汇盈金服互联网金融服务平台隐私条款@RequestMapping值 */
    public static final String PRIVACY_CLAUSE_ACTION = "/privacyClause";
    /** 汇盈金服注册协议@RequestMapping值 */
    public static final String REGISTRATION_PROTOCOL_ACTION = "/registrationProtocol";
	/** 汇盈金服出借咨询与管理服务协议页面*/
	public static final String INVESTMENT_ADVISORY_AND_MANAGEMENT_SERVICES_PATH = "agreement/type-consultant";
	/** 散标风险揭示书页面*/
    public static final String CONFIRMATION_OF_INVESTMENT_RISK_PATH = "agreement/type-contract";
    /** 汇盈金服互联网金融服务平台债权转让协议页面*/
    public static final String TRANSFER_OF_CREDITOR_RIGHT_PATH = "agreement/type-creditcontract";
    /** 汇添金出借计划服务协议页面*/
    public static final String HTJ_INVESTMENT_PLANNING_SERVICES_PATH = "agreement/type-htjxieyi";
    /** 汇盈金服互联网金融服务平台居间服务协议页面*/
    public static final String INTERMEDIARY_SERVICES_PATH = "agreement/type-invest";
    
    /** 借款协议页面*/
    public static final String DIARY_SERVICES_PATH = "agreement/type-tz-invest";
    /** 汇盈金服互联网金融服务平台居间服务协议页面--散标*/
    public static final String INTERMEDIARY_BORROW_SERVICES_PATH = "agreement/type-hjh-borrow-invest";
    /** 江西银行网络交易资金账户服务三方协议页面*/
    public static final String JX_BANK_SERVICE_PATH = "agreement/type-jxbank-contract";
    /** 汇盈金服互联网金融服务平台隐私条款页面*/
    public static final String PRIVACY_CLAUSE_PATH = "agreement/type-protection";
    /** 汇盈金服注册协议页面*/
    public static final String REGISTRATION_PROTOCOL_PATH = "agreement/type-registration";
	/** 当前controller名称 */
	public static final String THIS_CLASS = AgreementController.class.getName();
    /**  (融通宝)温州金融资产交易中心股份有限公司个人会员服务协议协议@RequestMapping值 */
    public static final String INDIVIDUAL_MEMBER_SERVICES_ACTION = "/RTBProductAgreement";
    /** (融通宝)汇盈金服平台服务协议协议@RequestMapping值 */
    public static final String PLATFORM_SERVICE_AGREEMENT_ACTION = "/RTBPlatformAgreement";
	/** 我的计划画面 路径 */
	public static final String PROJECT_DETAIL_LOCK_PATH = "user/planinfo/planinfolockdetail";
	/** 我的计划画面 路径 */
	public static final String PROJECT_DETAIL_EXIT_PATH = "user/planinfo/planinfoexitdetail";
	/** 我的计划画面 路径 */
	public static final String PROJECT_DETAIL_PATH = "user/planinfo/planinfodetail";
    /** 用户授权协议@RequestMapping值 */
    public static final String USER_LICENSE_AGREEMENT = "/userLicenseAgreement";
    /** 用户授权协议页面*/
    public static final String USER_LICENSE_AGREEMENT_PATH = "agreement/type-usershouquan";
    /** 汇计划-计划协议@RequestMapping值 */
    public static final String PLAN_INFO_AGREEMENT = "/planinfo";
    /** 汇计划-计划协议页面*/
    public static final String PLAN_INFO_AGREEMENT_PATH = "agreement/type-htjxieyi";
    /** 资产管理-计划-转让记录-持有项目列表-出借协议@RequestMapping值 */
    public static final String PLAN_CONTENT_DETAIL_AGREEMENT = "/goConDetail";
    /** 资产管理-计划-转让记录-持有项目列表-出借协议页面*/
    public static final String PLAN_CONTENT_DETAIL_AGREEMENT_PATH = "agreement/type-invest";
    /** 用户中心 计划转让中与已转让协议 */
    public static final String PLAN_CREDIT_CONTRACT = "planusercreditcontract";
    /** 汇盈金服互联网金融服务平台债权转让协议页面(债转被出借的协议)*/
    public static final String TRANSFER_OF_CREDITOR_PATH = "agreement/type-credit-contract";
    
    /** 新汇计划-计划协议@RequestMapping值 */
    public static final String HJH_INFO_AGREEMENT = "/hjhInfo";
    /** 新汇计划-计划协议页面*/
    public static final String HJH_INFO_AGREEMENT_PATH = "agreement/type-new-hjhxieyi";
    
    /** 资产管理-计划-持有项目列表-出借协议@RequestMapping值 */
    public static final String HJH_BORROW_AGREEMENT = "/hjhBorrowAgreement";
    /** 出借协议《借款协议》--查询有填充值@RequestMapping值 */
    public static final String HJH_DIARY_AGREEMENT = "/hjhDiaryAgreement";
    /** 资产管理-计划-转让记录-持有项目列表-出借协议页面*/
    public static final String HJH_BORROW_AGREEMENT_PATH = "agreement/type-hjh-borrow-invest";
    
    
    /** 汇盈金服授权协议@RequestMapping值 */
    public static final String HYJF_AUTH_ACTION = "/authagreement";
	/** 散标风险揭示书页面*/
    public static final String HYJF_AUTH_ACTION_PATH = "agreement/licensing-agreement";
    
    
    /** 汇盈金服互联网金融服务平台债权转让协议(汇计划债转)@RequestMapping值 */
    public static final String TRANSFER_OF_HJH_CREDITOR_RIGHT_ACTION = "/transferOfHJHCreditorRight";
    
    /** 汇盈金服互联网金融服务平台债权转让协议页面(汇计划债转)*/
    public static final String TRANSFER_OF_HJH_CREDITOR_RIGHT_PATH = "agreement/type-hjh-creditcontract";
    /**获得协议pdf */
    public static final String GOAGREEMENT_PDF = "/goAgreementPdf";
    /**协议名称 动态获得 */
    public static final String GET_DISPLAY_NAME_DYNAMIC = "/getdisplayNameDynamic";
       
}
