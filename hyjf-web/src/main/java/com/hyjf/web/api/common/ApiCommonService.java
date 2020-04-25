/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: lb
 * @version: 1.0
 * Created at: 2017年9月15日 上午9:43:49
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.web.api.common;

import com.hyjf.web.api.base.ApiBaseService;
import com.hyjf.web.api.user.ApiUserPostBean;
import com.hyjf.web.api.user.NmcfPostBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liubin
 */
public interface ApiCommonService extends ApiBaseService {
	void checkPostBeanOfInfo(ApiUserPostBean bean);
	void checkPostBeanOfWeb(ApiUserPostBean bean);
	void checkNmcfPostBean(NmcfPostBean bean);
	void checkSign(ApiUserPostBean bean);
	String decrypt(String str);
	Long RSAdecrypt(NmcfPostBean bean);
	Long decrypt(ApiUserPostBean bean);
	String encode(ApiUserPostBean bean);
	Integer getUserIdByBind(Long bindUniqueId, int bindPlatformId);
	boolean checkLoginUser(HttpServletRequest request,HttpServletResponse response);
	boolean removeCookie(HttpServletRequest request,HttpServletResponse response, String sessionId);

}

	