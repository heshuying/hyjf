package com.hyjf.app.agreement;

import com.hyjf.app.BaseDefine;

public class AgreementDefine extends BaseDefine {
	
	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/hjhagreement";
	/** 当前controller名称 */
	public static final String THIS_CLASS = AgreementController.class.getName();
    /** 新汇计划-计划协议@RequestMapping值 */
    public static final String HJH_INFO_AGREEMENT = "/hjhInfo";
    /** 出借协议《借款协议》--查询有填充值@RequestMapping值 */
    public static final String HJH_DIARY_AGREEMENT = "/hjhDiaryAgreement";
    
    /** 协议列表查询有填充值@RequestMapping值 */
    public static final String HJH_LIST_AGREEMENT = "/hjhListAgreement";
    /** 汇计划居间协议@RequestMapping值 */
    public static final String HJH_MEDIACY_CONTRACT = "/mediacyContract";
    /** 新汇计划-计划协议页面*/
    public static final String HJH_INFO_AGREEMENT_PATH = "hjhagreement/type-new-hjhxieyi";
    /** 借款协议页面*/
    public static final String DIARY_SERVICES_PATH = "hjhagreement/type-jiekuan";
    /** 列表协议页面*/
    public static final String LIST_SERVICES_PATH = "hjhagreement/type-agreement-list";
    /** 汇盈金服互联网金融服务平台居间服务协议@RequestMapping值 */
    public static final String INTERMEDIARY_SERVICES_ACTION = "/intermediaryServices";
    /** 汇盈金服互联网金融服务平台居间服务协议页面*/
    public static final String INTERMEDIARY_SERVICES_PATH = "hjhagreement/type-invest";
    /** 散标风险揭示书@RequestMapping值 */
    public static final String CONFIRMATION_OF_INVESTMENT_RISK_ACTION = "/confirmationOfInvestmentRisk";
    /** 散标风险揭示书页面*/
    public static final String CONFIRMATION_OF_INVESTMENT_RISK_PATH = "hjhagreement/app_contract";
    //融通宝相关的地址
    public static final String RONG_TONG_BAO_JIA = "/rcontract_jia";
    public static final String RONG_TONG_BAO_ZHONG = "/rcontract_zhong";

    // 汇计划债转协议
    public static final String PLAN_CREDIT_CONTRACT = "/userPlanCreditContract";
    // 汇计划债转协议路径
    public static final String PLAN_CREDIT_CONTRACT_PATH = "/hzr/planusercreditcontract";

}
