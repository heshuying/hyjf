package com.hyjf.batch.bank.borrow.autoreqrepay;


public class RepayDefine {

	/** 汇天利数据统计 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/web/user/repay";

	/** 用户还款页面 @RequestMapping值 */
	public static final String GET_AGREEMENT_ACTION = "/getAgreement";

	/** 用户还款页面 @RequestMapping值 */
	public static final String CHECK_PASSWORD_ACTION = "/checkPassword";

	/** 用户还款页面 @RequestMapping值 */
	public static final String USER_REPAY_PAGE_ACTION = "/userRepayPage";
	
	/** 担保用户批量还款页面 @RequestMapping值 */
	public static final String ORG_USER_BATCH_REPAY_PAGE_ACTION = "/orgUserBatchRepayPage";
	
	/** 担保用户批量还款处理中页面 @RequestMapping值 */
	public static final String USER_ORG_BATCH_REPAY_PAGE = "/bank/user/repay/loan-borrower-batch";
	
	/** 担保用户批量还款处理中跳转 @RequestMapping值 */
	public static final String ORG_USER_BATCH_REPAY_ACTION = "/orgUserStartBatchRepay";
	
	/**担保机构开始批量还款*/
	public static final String ORG_USER_START_BATCH_REPAY_ACTION = "/orgUserStartBatchRepayAction";
	
	/**担保机构开始校验批量还款*/
	public static final String ORG_USER_START_BATCH_REPAY_CHECK_ACTION = "/orgUserStartBatchRepayCheckAction";
	
	public static final String ORG_USER_BATCH_REPAY_SERCH_ACTION = "/orgUserBatchRepaySerch";

	/** 借款人下载pdf协议 @RequestMapping值 */
	public static final String DOWNLOAD_BORROWER_PDF_ACTION = "/downloadBorrowerPdf";

	/** 统计 @RequestMapping值 */
	public static final String REPAY_LIST_ACTION = "/repaylist";

	/** 垫付机构已垫付项目列表 @RequestMapping */
	public static final String ORG_REPAY_LIST_ACTION = "/repayOrglist";

	/** 项目详情企业或者个人 @RequestMapping值 */
	public static final String INVEST_LIST_ACTION = "/investlist";

	/** 项目详情企业或者个人 @RequestMapping值 */
	public static final String REPAY_DETAIL_ACTION = "/repaydetail";

	/** 项目详情企业或者个人 @RequestMapping值 */
	public static final String REPAY_ACTION = "/repay";

	/** 还款时的校验信息 */
	public static final String REPAY_ERROR = "WEBPAY0001";

	/** 还款时的校验信息 */
	public static final String REPAY_ERROR1 = "EWEBPAY0001";
	/** 还款时的校验信息 */
	public static final String REPAY_ERROR2 = "EWEBPAY0002";
	/** 还款时的校验信息 */
	public static final String REPAY_ERROR3 = "EWEBPAY0003";
	/** 还款时的校验信息 */
	public static final String REPAY_ERROR4 = "EWEBPAY0004";
	/** 还款时的校验信息 */
	public static final String REPAY_ERROR5 = "EWEBPAY0005";
	/** 还款时的校验信息 */
	public static final String REPAY_ERROR6 = "EWEBPAY0006";
	/** 还款时的校验信息 */
	public static final String REPAY_ERROR7 = "EWEBPAY0007";
	/** 还款时的校验信息 */
	public static final String REPAY_ERROR8 = "EWEBPAY0008";
	/** 还款时的校验信息 */
	public static final String REPAY_ERROR9 = "EWEBPAY0009";
	/** 还款时的余额 */
	public static final String REPAY_ERROR10 = "EWEBPAY0010";
	/** 还款时期数错误 */
	public static final String REPAY_ERROR11 = "EWEBPAY0011";

	/** 借款管理页面 */
	public static final String USER_REPAY_PAGE = "/bank/user/repay/user_repay";

	/** 借款详情页面 */
	public static final String REPAY_DETAIL_PAGE = "/bank/user/repay/user_repay_detail";

	/** 还款错误页面 */
	public static final String REPAY_ERROR_PAGE = "/bank/user/repay/user_repay_fail";

	/** 还款成功页面 */
	public static final String REPAY_SUCCESS_PAGE = "/bank/user/repay/user_repay_success";

	/** 汇消费协议页面 */
	public static final String AGREEMENT_CONTRACT_HXF_PAGE = "/bank/user/repay/agreement_contract_hxf";

	/** 协议页面 */
	public static final String AGREEMENT_CONTRACT_PAGE = "/bank/user/repay/agreement_contract";

	/** 收到报文后对合法性检查后的异步回调 */
	public static final String REPAY_VERIFY_RETURN_ACTION = "/repayVerifyReturn";

	/** 业务处理结果的异步回调 */
	public static final String REPAY_RESULT_RETURN_ACTION = "/repayResultReturn";

}
