package com.hyjf.admin.exception.bankaccountcheck;

import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;
/**
 * 
 * @author cwyang
 * add by 17-4-7
 * 银行对账常量类
 */
public class BankAccountCheckDefine {

	/** 银行对账 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/bankaccountcheck";
	/** 银行对账查看 CONTROLLOR @RequestMapping值 */
	public static final String INIT_ACTION = "init";
	/**银行对账权限名称*/
	public static final String PERMISSIONS = "bankaccountcheck";
	/**银行对账页面访问权限*/
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
	
	/**银行对账页面路径*/
	public static final String BANKACCOUNTCHECK_PATH= "exception/bankaccountcheck/bankaccountcheck_list";
	
	/**
	 * 类名
	 */
	public static final String THIS_CLASS = BankAccountCheckController.class.getName();
	/**
	 * 页面结果
	 */
	public static final String ACCOUNTCHECK_FORM = "accountcheckForm";
	/**
	 * 数据字典对应对账状态calss_name
	 */
	public static final String ACCOUNT_CHECK = "ACCOUNT_CHECK";
	/**
	 * 数据字典对应交易状态calss_name
	 */
	public static final String ACCOUNT_CHECK_TRADE = "ACCOUNT_CHECK_TRADE";
	
}
