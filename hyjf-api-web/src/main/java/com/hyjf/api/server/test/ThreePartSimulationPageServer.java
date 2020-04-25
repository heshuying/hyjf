/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: lb
 * @version: 1.0
 * Created at: 2017年9月22日 上午11:51:41
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.api.server.test;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hyjf.api.server.asset.AssetSearchDefine;
import com.hyjf.common.http.HttpDeal;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;

/**
 * @author lb
 */
@Controller
@RequestMapping("/test")
public class ThreePartSimulationPageServer {
	/**
	 * 模拟
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/threepart")
	public String ThreePart(HttpServletRequest request, HttpServletResponse response, hjsBean bean) {
		String result = null;
		try {
			String url = PropUtils.getSystem(CustomConstants.HYJF_SEAL_URL);
			String str = JSON.toJSONString(bean);
			result = HttpDeal.postJson(bean.getUrl(), str);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}

class hjsBean {
	String bindUniqueIdScy;
	String timestamp;
	String chkValue;
	String url;
	/**
	 * bindUniqueIdScy
	 * @return the bindUniqueIdScy
	 */
	
	public String getBindUniqueIdScy() {
		return bindUniqueIdScy;
	}
	/**
	 * @param bindUniqueIdScy the bindUniqueIdScy to set
	 */
	
	public void setBindUniqueIdScy(String bindUniqueIdScy) {
		this.bindUniqueIdScy = bindUniqueIdScy;
	}
	/**
	 * timestamp
	 * @return the timestamp
	 */
	
	public String getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * chkValue
	 * @return the chkValue
	 */
	
	public String getChkValue() {
		return chkValue;
	}
	/**
	 * @param chkValue the chkValue to set
	 */
	
	public void setChkValue(String chkValue) {
		this.chkValue = chkValue;
	}
	/**
	 * url
	 * @return the url
	 */
	
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	
	public void setUrl(String url) {
		this.url = url;
	}
	
}

	