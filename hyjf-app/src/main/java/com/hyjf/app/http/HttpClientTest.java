package com.hyjf.app.http;


import java.util.HashMap;
import java.util.Map;

import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.PropUtils;

public class HttpClientTest {

	public static void main(String[] args) {
		//HttpClientTest.testRegist();
		try {
			HttpClientTest.testBorrowTender();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};

	}
	
	public static void testRegist() throws Exception{
		Map<String, String> params = new HashMap<String, String>();  
		String from = "40";
		String username = "mahongf908g";
		String mobile = "18653296909";
		String email = "";
		String timestamp = "1436254211";
		String accessKey = PropUtils.getSystem("release.tanliuliu.accesskey");
		String sign = MD5.toMD5Code(accessKey+email+from+mobile+timestamp+username+accessKey);
		
		params.put("from", from);  
		params.put("username", username);
		params.put("mobile", mobile);
		params.put("email", email);
		params.put("timestamp", timestamp);
		params.put("sign", sign);
		
		String loginUrl = "http://www.hyjf.com/hyjf-api-web/userRegistServer/userRegist.json";
		      
		String xml = HttpClientUtils.post(loginUrl, params);  
		System.out.println(xml);
	}
	
	public static void testBorrowTender() throws Exception{
		Map<String, String> params = new HashMap<String, String>();  
//		String from = "40";
//		String username = "zhangtest";
//		String usernamep = "liuli006";
//		String timestamp = "1436254211";
//		String starttime = "1439007132";
//		String endtime = "1502165532";
//		String accessKey = PropUtils.getSystem("release.tanliuliu.accesskey");
//		String sign = MD5.toMD5Code(accessKey+from+timestamp+username+usernamep+accessKey);
		
//		params.put("from", from);  
//		params.put("username", username);
//		params.put("usernamep", usernamep);
//		params.put("timestamp", timestamp);
//		params.put("starttime", starttime);
//		params.put("endtime", endtime);
//		params.put("sign", sign);
		String test = "MeVReFtZ4bs%3D";
		params.put("access_key", test);
		
		String loginUrl = "https://new.hyjf.com/account_syn?access_key=MeVReFtZ4bs%3D";
		      
		String xml = HttpClientUtils.get(loginUrl, params);  
		System.out.println(xml);
	}

}
