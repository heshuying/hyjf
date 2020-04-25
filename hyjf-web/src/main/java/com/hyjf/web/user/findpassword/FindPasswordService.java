package com.hyjf.web.user.findpassword;

import com.hyjf.web.BaseService;

public interface FindPasswordService extends BaseService {
	/**
	 * 判断是否存在手机号
	 * 
	 * @param phone
	 * @return
	 */
	public Boolean existPhone(String phone);

	/**
	 * 
	 * 修改某手机的密码,传递密码为未加密的密码<br/>
	 * 返回-1:没有查到用户,-2:存在重复的电话号码,0代表未做任何操作,1代表成功
	 * 
	 * @param phone
	 * @param password
	 * @return
	 */
	public Integer updatePassword(String phone, String password);
}
