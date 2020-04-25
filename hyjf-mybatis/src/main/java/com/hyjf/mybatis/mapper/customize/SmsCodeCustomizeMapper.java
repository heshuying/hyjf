package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.SmsCodeCustomize;


public interface SmsCodeCustomizeMapper {

	/**
	 * 获取查询出来的用户手机号码和数量
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	public  List<SmsCodeCustomize> queryUser(SmsCodeCustomize msg);
	
	public  Integer queryUserIdByPhone(String mobile);

}