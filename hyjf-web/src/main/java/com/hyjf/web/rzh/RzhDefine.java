/**
 * Description:我要融资常量定义
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.rzh;

import com.hyjf.web.BaseDefine;

public class RzhDefine extends BaseDefine {

	/** 发送短信验证码 @RequestMapping值 */
	public static final String TO_LOANPAGE_ACTION = "toLoanPage";
	
	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/rzh/apply";

	/** 用户注册 @RequestMapping值 */
	public static final String LOAN_APPLY_ACTION = "apply";
	/** 跳转到首页@RequestMapping值 */
	public static final String TO_INDEX_ACTION = "toIndexPage";

	/** 发送短信验证码 @RequestMapping值 */
	public static final String LOAN_CHECK_CODE_ACTION = "checkcode";

	/** 发送短信验证码 @RequestMapping值 */
	public static final String LOAN_SEND_CODE_ACTION = "sendcode";
	
	/** 汇借款画面 路径 */
	public static final String HJK_PATH = "rzh/loan";
	
	/** 汇借款申请成功画面 路径 */
	public static final String LOAN_APPLY_SUCCESS_PATH = "rzh/loan_success";
	/** 返回首页 路径 */
	public static final String INDEX_PATH = "index";
	/** 汇借款申请失败画面 路径 */
	public static final String LOAN_APPLY_ERROR_PATH = "rzh/loan_error";

	/** 当前controller名称 */
	public static final String THIS_CLASS = RzhController.class.getName();

	/** 用户注册表单 */
	public static final String REGTIST_USER_FORM = "hjkForm";

	/** 姓名*/
	public static final String USER_NAME = "姓名";

	/** 姓名不能为空 */
	public static final String USER_NAME_ERROR = "EWEB0001";

	/** 手机号码 */
	public static final String USER_MOBILE = "手机号码";

	/** 手机号码不是一个有效的手机号码 */
	public static final String USER_MOBILE_ERROR = "EWEB0002";

	/** 验证码 */
	public static final String USER_CODE = "验证码";

	/** 验证码不能为空 */
	public static final String USER_CODE_ERROR = "EWEB0003";

	/** 性别 */
	public static final String USER_SEX = "性别";

	/** 性别不能为空 */
	public static final String USER_SEX_ERROR = "EWEB0004";

	/** 融资金额 */
	public static final String USER_FINANCE = "融资金额";

	/** 融资金额不能为空 */
	public static final String USER_FINANCE_ERROR = "EWEB0005";

	/** 融资期限 */
	public static final String USER_FINPERIOD = "融资期限";

	/** 融资期限不能为空 */
	public static final String USER_FINPERIOD_ERROR = "EWEB0006";

	/** 融资用途*/
	public static final String USER_FINUSE = "融资用途";

	/** 融资用途不能为空 */
	public static final String USER_FINUSE_ERROR = "EWEB0007";

	/** 质、抵押物 */
	public static final String USER_FINMORTGAGE = "质、抵押物 ";

	/** 质、抵押物不能为空 */
	public static final String USER_FINMORTGAGE_ERROR = "EWEB0008";
	
	/** 省*/
	public static final String USER_PROVINCE = "省";

	/** 省不能为空*/
	public static final String USER_PROVINCE_ERROR = "EWEB0009";

	/** 市 */
	public static final String USER_CITY = "市";

	/** 市不能为空*/
	public static final String USER_CITY_ERROR = "EWEB0010";

	/** 地区 */
	public static final String USER_AREA = "地区  ";

	/** 地区错误编码 */
	public static final String USER_AREA_ERROR = "EWEB0011";

	/** 企业名称 */
	public static final String COMPANY_NAME = "企业名称";

	/** 企业名称 错误编码  */
	public static final String COMPANY_NAME_ERROR = "EWEB0012";

	/** 企业成立时间 */
	public static final String COMPANY_REG_TIME = "企业成立时间";

	/** 企业成立时间错误编码  */
	public static final String COMPANY_REG_TIME_ERROR = "EWEB0013";
	
	/** 企业经营地址 */
	public static final String COMPANY_ADRESS = "企业经营地址 ";

	/** 企业经营地址 错误编码  */
	public static final String COMPANY_ADRESS_ERROR = "EWEB0014";

	/** 所属行业 */
	public static final String COMPANY_INDUSTRY = "所属行业 ";

	/** 所属行业错误编码  */
	public static final String COMPANY_INDUSTRY_ERROR = "EWEB0015";

	/** 主营业务 */
	public static final String COMPANY_BUSINESS = "主营业务 ";

	/** 主营业务错误编码  */
	public static final String COMPANY_BUSINESS_ERROR = "EWEB0016";

	/** 年营业额  */
	public static final String COMPANY_YEAR_TURNOVER = "年营业额 ";

	/** 年营业额错误编码  */
	public static final String COMPANY_YEAR_TURNOVER_ERROR = "EWEB0017";
	
	/** 年利润额 */
	public static final String COMPANY_YEAR_PROFIT = "年利润额 ";

	/** 年利润额错误编码  */
	public static final String COMPANY_YEAR_PROFIT_ERROR = "EWEB0018";

	/** 银行贷款 */
	public static final String COMPANY_BANK_LOANS = "银行贷款  ";

	/** 银行贷款错误编码 */
	public static final String COMPANY_BANK_LOANS_ERROR = "EWEB0019";

}
