package com.hyjf.server.module.wkcd.user.openAccount;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.server.BaseService;

public interface WkcdOpenAccountService  extends BaseService {
	/**
	 * 保存用户开户信息
	 * 
	 * @param bean
	 * @return
	 */
	public boolean userOpenAccount(ChinapnrBean bean);


	/**
	 * 根据手机号统计用户的数量
	 * @param userId 
	 * 
	 * @param mobile
	 * @return
	 */
	public JSONObject selectUserByMobile(int userId, String mobile);
	
}



