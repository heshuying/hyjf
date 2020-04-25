package com.hyjf.admin.message;

import java.text.ParseException;
import java.util.List;

import com.hyjf.mybatis.model.customize.SmsCodeCustomize;

public interface SmsCodeService {

	/**
	 * 按条件查询用户数量和手机号码
	 * 
	 * @param msb
	 * @return
	 */
	public List<SmsCodeCustomize> queryUser(SmsCodeBean msb);

	/**
	 * 按照手机号码查询用户ID
	 * 
	 * @param msb
	 * @return
	 */
	public Integer queryUserIdByPhone(String mobile);

	public boolean sendSmsOntime(SmsCodeBean form) throws NumberFormatException, ParseException;

	public boolean getUserByMobile(String mobile);

}
