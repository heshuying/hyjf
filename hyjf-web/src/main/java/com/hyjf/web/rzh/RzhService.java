/**
 * Description:我要融资service接口
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.rzh;

import com.hyjf.web.BaseService;

public interface RzhService extends BaseService {

	/**
	 * 保存用户的融资信息
	 * @param ru
	 */
	void saveUserLoan(RzhBean ru);

	/**
	 * 保存短信发送log日志
	 * @param code
	 * @param mobile
	 */
	void saveSmsLog(String code, String mobile);

//	/**
//	 * 查询相应的短信的模版
//	 * @param string
//	 * @return
//	 */
//	SmsTemplate getMessTemplate(String string);

	/**
	 * 校验相应的短信验证码
	 * @param phone
	 * @param code
	 * @return
	 */
	int checkMobileCode(String phone, String code);
}
