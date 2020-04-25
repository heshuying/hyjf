package com.hyjf.web.bank.web.borrow.batchloan;

import com.hyjf.web.BaseDefine;

public class BatchLoanDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/web/borrow/loan";

	
	/** 放款校验回调 @RequestMapping值 */
	public static final String LOAN_VERIFY_RETURN_ACTION = "/loanVerifyReturn";

	/** 放款结果回调 @RequestMapping值 */
	public static final String LOAN_RESULT_RETURN_ACTION = "/loanResultReturn";
	
	/** 放款状态:放款校验成功 */
	public static final Integer STATUS_VERIFY_SUCCESS = 3;

	/** 放款状态:放款校验失败 */
	public static final Integer STATUS_VERIFY_FAIL = 4;

	/** 放款状态:放款失败 */
	public static final Integer STATUS_LOAN_FAIL = 5;

	/** 放款状态:放款成功 */
	public static final Integer STATUS_LOAN_SUCCESS = 6;

    public static final String FDD_CALL_API_SIGNNODIFY = "/callApiSignNodify";
}
