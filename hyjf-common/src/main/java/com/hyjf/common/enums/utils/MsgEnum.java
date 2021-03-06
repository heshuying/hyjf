/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: lb
 * @version: 1.0
 * Created at: 2017年10月12日 上午9:13:24
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.common.enums.utils;

/**
 * 信息代码和信息本体的枚举
 * @author liubin
 */

public enum MsgEnum {
	
	
	// ----------通用警告信息------------
	WARN_BUILING("WTY000001","接口访问量过大"),
	
	// ----------通用错误信息------------
	ERR_OBJECT_REQUIRED("ETY000001","{0}不能为空"),
	ERR_OBJECT_DIGIT("ETY000002","{0}不能存在非数字"),
	ERR_OBJECT_DATE("ETY000003","{0}非日期格式"),
	ERR_OBJECT_MAIL("ETY000004","{0}非法邮件地址"),
	ERR_OBJECT_VALUE("ETY000005","传入参数{0}的值非法"),
	ERR_SIGN("ETY000101","签名验证失败"),
	ERR_PARAM_TYPE("ETY000102","传入参数类型错误"),
	ERR_JSON("ETY000103","传入JSON错误"),
	ERR_SYSTEM("ETY000199","接口调用发生异常，请联系服务方"),
	ERR_PAGE_MAX("ETY000202","单次检索记录数不能超过{0}条"),
	ERR_DATA_MAX("ETY000202","单次检索记录时间不能超过{0}天"),
	ERR_OBJECT_UNMATCH("ETY000104","{0}不符合接口要求，请重新传入"),
	// ----------共通机能用错误信息------------
	STATUS_CE000001("CE000001","请求参数异常"),
	STATUS_CE000002("CE000002","系统验签失败"),
	STATUS_CE000003("CE000003","根据手机号查询用户信息失败"),
	STATUS_CE000004("CE000004","根据电子账户号查询用户信息失败"),
	STATUS_CE000005("CE000005","银行处理中，请稍后查看"),
	STATUS_CE000006("CE000006","没有用户信息"),
	STATUS_CE000007("CE000007","没有用户开户信息"),
	STATUS_CE000008("CE000008","请求日期格式错误"),
	STATUS_CE000009("CE000009","请求开始日期大于结束日期"),
	STATUS_CE000010("CE000010","请求手机号格式错误"),
	STATUS_CE000011("CE000011","请求手机号不存在"),
	STATUS_CE000012("CE000012","请求用户电子账号不存在"),
	STATUS_CE000013("CE000013","请求项目编号不存在"),
	STATUS_CE999999("CE999999","系统异常"),
	// ----------注册机能用错误信息------------
	STATUS_ZC000001("ZC000001","手机号不能为空"),
	STATUS_ZC000002("ZC000002","机构编号不能为空"),
	STATUS_ZC000003("ZC000003","请输入您的真实手机号码"),
	STATUS_ZC000004("ZC000004","机构编号错误"),
	STATUS_ZC000005("ZC000005","手机号已在平台注册"),
	STATUS_ZC000006("ZC000006","渠道不能为空"),
	STATUS_ZC000007("ZC000007","姓名不能为空"),
	STATUS_ZC000008("ZC000008","身份证号不能为空"),
	STATUS_ZC000009("ZC000009","银行卡号不能为空"),
	STATUS_ZC000010("ZC000010","手机验证码不能为空"),
	STATUS_ZC000011("ZC000011","短信发送订单号为空"),
	STATUS_ZC000012("ZC000012","真实姓名不能包含空格"),
	STATUS_ZC000013("ZC000013","真实姓名不能超过10位"),
	STATUS_ZC000014("ZC000014","身份证已存在"),
	STATUS_ZC000015("ZC000015","验证码错误"),
	STATUS_ZC000016("ZC000016","银行卡与姓名不符"),
	STATUS_ZC000017("ZC000017","银行卡与证件不符"),
	
    // 还款信息接口项目编号不能为空
    STATUS_ZC000018("ZC000018","资产编号不能为空"),
	// ----------资产机能用错误信息------------
	STATUS_ZT000001("ZT000001","没有用户信息"),
	STATUS_ZT000002("ZT000002","没有用户开户信息"),
	STATUS_ZT000003("ZT000003","用户不是借款人"),
	STATUS_ZT000004("ZT000004","系统异常,资产未进库"),
	STATUS_ZT000005("ZT000005","不支持这种还款方式"),
	STATUS_ZT000006("ZT000006","资产金额超过一百万"),
	STATUS_ZT000007("ZT000007","资产信息不正确"),
	STATUS_ZT000008("ZT000008","资产已入库"),
	STATUS_ZT000009("ZT000009","资产不存在"),
	// ----------免密提现机能用错误信息------------
	STATUS_NC000001("NC000001","免密提现-用户暂未开通该服务"),
	STATUS_NC000002("NC000002","免密提现-用户银行卡信息不一致"),
	STATUS_NC000003("NC000003","免密提现-查询用户失败"),
	STATUS_NC000004("NC000004","免密提现-查询用户详细信息失败"),
	STATUS_NC000005("NC000005","免密提现-查询电子帐号失败"),
	STATUS_NC000006("NC000006","免密提现-提现金额不能小于一元"),
	STATUS_NC000007("NC000007","免密提现-提现失败"),
	STATUS_NC000008("NC000008","免密提现-查询用户银行卡信息失败"),
	// ----------绑定银行卡机能用错误信息------------
	STATUS_BC000001("BC000001","用户已绑定银行卡,请先解除绑定,然后重新操作！"),
	STATUS_BC000002("BC000002","获取用户银行卡信息失败"),
	STATUS_BC000003("BC000003","银行处理中，请稍后查看"),
	STATUS_BC000004("BC000004","解绑失败，余额大于0元是不能解绑银行卡"),
	// ----------交易密码机能用错误信息------------
	STATUS_TP000001("TP000001","已设置交易密码"),
	STATUS_TP000002("TP000002","未设置过交易密码，请先设置交易密码"),
	// ----------用户测评错误信息------------
	STATUS_EV000001("EV000001","未找到对应测评结果"),
	// ----------通用信息------------
	INFO_BUILING("ITY000001","系统处理中，请稍后");

	
	private String msg;
	private String code;

	private MsgEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getMsg() {
		return this.msg;
	}

	public String getCode() {
		return this.code;
	}
}

	