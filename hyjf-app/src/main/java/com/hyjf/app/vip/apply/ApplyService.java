/**
 * Description:用户充值
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: GOGTZ-T
 * @version: 1.0
 * Created at: 2015年12月4日 下午1:45:13
 * Modification History:
 * Modified by :
 */
package com.hyjf.app.vip.apply;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.VipInfo;

public interface ApplyService extends BaseService {
	/**
	 * 获取用户的客户信息
	 */
	AccountChinapnr getAccountChinapnr(Integer userId);
	
	/**
	 * 校验
	 * @param account
	 * @param userIdInt
	 * @return
	 */
	JSONObject checkParam(String account, Integer userIdInt) throws Exception;
	
	JSONObject updateUserVip(Integer userId,BigDecimal transAmt,HttpServletRequest request,String ordId,String usrCustId,String merCustId, String platform) throws Exception;
	
	void updateGiftStatus(int userId,int upgradeId) throws Exception;
	
	VipInfo getVipInfo(int vipId);
    
}
