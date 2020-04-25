package com.hyjf.api.server.util;

/**
 * @author xiasq
 * @version ErrorCode, v0.1 2017/11/30 15:55
 */
public enum ErrorCode {

	INST_CODE_ERROR(ErrorCodeConstant.STATUS_ZC000004, "机构编号非法"),
	PLAT_FORM_ERROR(ErrorCodeConstant.STATUS_CE000001, "开户平台非法"),
	MOBILE_NULL_ERROR(ErrorCodeConstant.STATUS_ZC000001, "手机号不能为空"),
	MOBILE_VALID_ERROR(ErrorCodeConstant.STATUS_CE000001, "手机号非法"),
	TRUENAME_NULL_ERROR(ErrorCodeConstant.STATUS_ZC000007, "姓名不能为空"),
	IDNO_NULL_ERROR(ErrorCodeConstant.STATUS_ZC000008, "身份证号不能为空"),
	IDNO_VALID_ERROR(ErrorCodeConstant.STATUS_ZC000021, "身份证号非法"),
	UTM_NULL_ERROR(ErrorCodeConstant.STATUS_ZC000018, "第三方操作平台"),
	CHANNEL_ERROR(ErrorCodeConstant.STATUS_CE000001, "渠道非法"),
	SEND_SMS_CODE_ERROR(ErrorCodeConstant.STATUS_CE999999, "发送短信验证码失败");


	private String errCode;

	private String msg;

	ErrorCode(String errCode, String msg) {
		this.errCode = errCode;
		this.msg = msg;
	}

	public String getErrCode() {
		return errCode;
	}

	public String getMsg() {
		return msg;
	}
}
