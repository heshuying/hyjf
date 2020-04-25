package com.hyjf.web.user.repay;

import com.hyjf.web.BaseDefine;

public class UserRepayDefine extends BaseDefine {
    
    /** 用户还款页面  @RequestMapping值 */
    public static final String GET_AGREEMENT_ACTION = "/getAgreement";
    
    /** 用户还款页面  @RequestMapping值 */
    public static final String CHECK_PASSWORD_ACTION = "/checkPassword";
    
    /** 用户还款页面  @RequestMapping值 */
    public static final String USER_REPAY_PAGE_ACTION = "/userRepayPage";

	/** 汇天利数据统计 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/user/repay";
	
	/** 借款人下载pdf协议 @RequestMapping值 */
	public static final String DOWNLOAD_BORROWER_PDF_ACTION = "/downloadBorrowerPdf";

	/** 统计 @RequestMapping值 */
	public static final String REPAY_LIST_ACTION = "repaylist";
	
	/** 项目详情企业或者个人 @RequestMapping值 */
	public static final String INVEST_LIST_ACTION = "investlist";
	
	/** 项目详情企业或者个人 @RequestMapping值 */
	public static final String REPAY_DETAIL_ACTION = "repaydetail";
	
	/** 项目详情企业或者个人 @RequestMapping值 */
	public static final String REPAY_ACTION = "repay";
	
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
	/** 还款时期数错误*/
	public static final String REPAY_ERROR11 = "EWEBPAY0011";
	
	/** 统计类名 */
	public static final String THIS_CLASS= UserRepayController.class.getName();

	/** 借款管理页面 */
    public static final String USER_REPAY_PAGE = "/user/repay/user_repay";

    /** 借款详情页面 */
    public static final String REPAY_DETAIL_PAGE = "/user/repay/user_repay_detail";

    /** 还款错误页面 */
    public static final String REPAY_ERROR_PAGE = "/user/repay/user_repay_fail";

    /** 还款成功页面 */
    public static final String REPAY_SUCCESS_PAGE = "/user/repay/user_repay_success";

    /** 汇消费协议页面 */
    public static final String AGREEMENT_CONTRACT_HXF_PAGE = "/user/repay/agreement_contract_hxf";

    /** 协议页面 */
    public static final String AGREEMENT_CONTRACT_PAGE = "/user/repay/agreement_contract";

    


}
