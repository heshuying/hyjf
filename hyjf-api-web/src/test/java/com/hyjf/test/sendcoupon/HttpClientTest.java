package com.hyjf.test.sendcoupon;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.test.common.HttpClientUtils;

public class HttpClientTest {

	public static void main(String[] args) {
		//HttpClientTest.testRegist();
		HttpClientTest.testNonCash();

		//HttpClientTest.borrowRepayForCoupon();
	    HttpClientTest.dati();
	}
	
	public static void borrowRepayForCoupon(){
		Map<String, String> params = new HashMap<String, String>();  
		/*String from = "40";
		String username = "zhangtest";
		String mobile = "18653296345";
		String email = "zhangjinpeng@hyjf12345.com";
		String timestamp = "1436254211";
		
		String sign = MD5.toMD5Code(accessKey+email+from+mobile+timestamp+username+accessKey);*/
		
		String timestamp = GetDate.getNowTime10()+"";
		String borrowNid = "HDD17050543";
		String periodNow = "1";
		String accessKey = "c430totb012s";
		String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + borrowNid + 
                periodNow + timestamp + accessKey));
		
		params.put("timestamp", timestamp);  
		params.put("borrowNid", borrowNid);
		params.put("periodNow", periodNow);
		params.put("chkValue", sign);
		System.out.println(sign);
		
		String loginUrl = "http://www.hyjf.com/hyjf-api-web/coupon/repay/borrowRepayForCoupon.json";
		      
		String xml = HttpClientUtils.post(loginUrl, params);  
		
		
		System.out.println(xml);
	}
	
	public static void testBorrowTender(){
		Map<String, String> params = new HashMap<String, String>();  
		String from = "40";
		String username = "zhangtest";
		String usernamep = "liuli006";
		String timestamp = "1436254211";
		String starttime = "1439007132";
		String endtime = "1502165532";
		String accessKey = PropUtils.getSystem("test.tanliuliu.accesskey");
		String sign = MD5.toMD5Code(accessKey+from+timestamp+username+usernamep+accessKey);
		
		params.put("from", from);  
		params.put("username", username);
		params.put("usernamep", usernamep);
		params.put("timestamp", timestamp);
		params.put("starttime", starttime);
		params.put("endtime", endtime);
		params.put("sign", sign);
		
		String loginUrl = "http://localhost:8080/hyjf-api-web/userBorrowTenderServer/userBorrowTender.json";
		      
		String xml = HttpClientUtils.post(loginUrl, params);  
		System.out.println(xml);
	}
	
	public static void testNonCash(){
        Map<String, String> params = new HashMap<String, String>();  
      /*  String accessKey = PropUtils.getSystem("test.tanliuliu.accesskey");
        String sign = MD5.toMD5Code(accessKey+from+timestamp+username+usernamep+accessKey);*/
        
        params.put("channel", "000001");  
        params.put("timestamp", "20170907");
        params.put("accountId", "6212461890000150418");
        params.put("cardNo", "6222021602020873719");
        params.put("platform", "1");
        params.put("account", "10");
        params.put("sign", "sign");
        
        String loginUrl = "http://test.hyjf.com:8080/hyjf-api-web/server/user/nonCashWithdraw/cash.json";
              
        String xml = HttpClientUtils.post(loginUrl, params);  
        System.out.println("返回值：" + xml);
    }
	
	
	public static void dati(){
        Map<String, String> params = new HashMap<String, String>();  
        
        String timestamp = GetDate.getNowTime10()+"";
        String accessKey = "c430totb012s";
        String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + timestamp + accessKey));
        params.put("userId", "");  
        params.put("timestamp", timestamp);  
        params.put("chkValue", sign);
        System.out.println(params.toString());
        
        String loginUrl = "https://newweb3.hyjf.com/hyjf-api-web/inviteten/getUserQuestion.do";
              
        String xml = HttpClientUtils.post(loginUrl, params);  
        
        
        System.out.println(xml);
    }

}
