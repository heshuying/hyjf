package com.hyjf.app.newagreement;

import com.hyjf.app.BaseDefine;

public class NewAgreementDefine extends BaseDefine {
	
	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/new/agreement";
	/** 当前controller名称 */
	public static final String THIS_CLASS = NewAgreementController.class.getName();
	/** （一）居间服务借款协议@RequestMapping值 */
    public static final String INTER_SERVICE_LOAN_AGREEMENT_ACTION = "/interServiceLoanAgreement";
    /** （二）汇计划智投服务协议@RequestMapping值 */
    public static final String HJH_INFO_AGREEMENT = "/hjhInfo";
    /** （三）债权转让协议 @RequestMapping值 */
    public static final String CREDIT_CONTRACT = "/userCreditContract";
    /** （四）融通宝（嘉诺）协议@RequestMapping值*/
    public static final String RONG_TONG_BAO_JIA = "/rcontract_jia";
    /** （五）融通宝（中商储）协议@RequestMapping值*/
    public static final String RONG_TONG_BAO_ZHONG = "/rcontract_zhong";
    /** （六）开户协议*/
    public static final String OPEN_AGREEMENT = "/openAgreement";
    
    
    // 汇计划债转协议列表
    public static final String PLAN_CREDIT_CONTRACT_LIST = "/userCreditContractList";
    
    
    /**散标风险揭示书前端地址*/
    public static final String CONFIRM_INVEST_RISK_AGREEMENT_PATH = "/agreement/ConfirmInvestRiskAgreement";
    /**协议列表前端地址*/
    public static final String AGREEMENT_LIST_PATH = "/agreement/AgreementList";
    /**居间服务借款协议前端地址*/
    public static final String SERVICE_LOAN_AGREEMENT_PATH = "/agreement/ServiceLoanAgreement";
    /**债权转让协议前端地址*/
    public static final String TRANS_FER_AGREEMENT_PATH = "/agreement/TransferAgreement";
    /**（汇计划）智投服务协议前端地址*/
    public static final String HJH_INVEST_SERVICE_AGREEMENT_PATH = "/agreement/HJHInvestServiceAgreement";
    /**融通宝（嘉诺）协议前端地址*/
    public static final String RTB_JN_AGREEMENT_PATH = "/agreement/RTBJNAgreement";
    /**融通宝（中商储）协议前端地址*/
    public static final String RTB_ZSC_AGREEMENT_PATH = "/agreement/RTBZSCAgreement";
    /**注册协议前端地址*/
    public static final String REGISTER_RULE_AGREEMENT_PATH = "/agreement/RegisterRuleAgreement";
    /**开户协议前端地址*/
    public static final String OPEN_AGREEMENT_PATH = "/agreement/OpenAgreement";
    /**用户授权协议前端地址*/
    public static final String USER_AUTHORIZATION_AGREEMENT_PATH = "/agreement/UserAuthorizationAgreement";
    /** 获得所有的协议模板ID*/
    public static final String GETAGREEMENT_PROTOCOLID = "getAgreementProtocolId";
    /** 获得所有的协议模板*/
    public static final String GETAGREEMENT_PROTOCOL_LIST = "getAgreementProtocolList";
    /** IOS 使用pdf路径， android 使用 img 路径*/
    public static final String GOTAGREEMENTPDF_OR_IMG = "gotAgreementPdfOrImg";
    /**协议名称 动态获得 */
    public static final String GET_DISPLAY_NAME_DYNAMIC = "/getdisplayNameDynamic";
}
