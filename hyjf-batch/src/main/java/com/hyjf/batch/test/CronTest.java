/**
 * Description:corn表达式测试类
 * Copyright: Copyright (HYJF Corporation)2016
 * Company: HYJF Corporation
 * @author: Michael
 * @version: 1.0
 * Created at: 2016年6月20日 下午2:58:31
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.batch.test;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.http.HttpClientUtils;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.GetDate;
  
public class CronTest {  
  
	public static void main(String[] args) {
      borrowCouponInvest();
  	  //planCouponInvest();
  }

	private static void planCouponInvest() {
		Map<String, String> params = new HashMap<String, String>();
      final String SOA_INTERFACE_KEY = "c430totb012s";
      String userId = "31580";
      String planNid = "HJH201801260004";
      String money = "484000";
      String platform = "3";
      String couponGrantId = "2394756";
      String ordId = "25372386555388015368";
      String ip = "10.10.3.80";
      String couponOldTime = "1537148434";
   // 用户编号
      params.put("userId", userId);
      // 项目编号
      params.put("planNid", planNid);
      // 出借金额
      params.put("money", money);
      // 平台标识
      params.put("platform", platform);
      // 优惠券id
      params.put("couponGrantId", couponGrantId);
      // 订单号
      params.put("ordId", ordId);
      // ip
      params.put("ip", ip);
      // 排他check
      params.put("couponOldTime", couponOldTime);
      String timestamp = GetDate.getNowTime10() + "";
      // 时间戳
      params.put("timestamp", timestamp);
      // 发放优惠券url
      String requestUrl = "http://www.hyjf.com/hyjf-api-web/plan/coupon/couponTender.json";
      String sign = StringUtils.lowerCase(MD5.toMD5Code(SOA_INTERFACE_KEY + userId + planNid + money + platform
              + couponGrantId + ip + ordId + couponOldTime + timestamp + SOA_INTERFACE_KEY));
      params.put("chkValue", sign);

      String result = HttpClientUtils.post(requestUrl, params);
      JSONObject status = JSONObject.parseObject(result);
      System.out.println(status.toJSONString());
		
	}

	private static void borrowCouponInvest() {
		Map<String, String> params = new HashMap<String, String>();
      final String SOA_INTERFACE_KEY = "c430totb012s";
      String userId = "124784";
      String borrowNid = "HDD18080213";
      String money = "122000";
      String platform = "3";
      String couponGrantId = "2404618";
      String ordId = "15339691016908447213";
      String ip = "10.10.3.80";
      String couponOldTime = "1537252417";
      // 用户编号
      params.put("userId", userId);
      // 项目编号
      params.put("borrowNid", borrowNid);
      // 出借金额
      params.put("money", money);
      // 平台标识
      params.put("platform", platform);
      // 优惠券id
      params.put("couponGrantId", couponGrantId);
      // 订单号
      params.put("ordId", ordId);
      // ip
      params.put("ip", ip);
      // 排他check
      params.put("couponOldTime", couponOldTime);
      String timestamp = GetDate.getNowTime10() + "";
      // 时间戳
      params.put("timestamp", timestamp);
      // 发放优惠券url
      String requestUrl = "http://www.hyjf.com/hyjf-api-web/invest/couponTender.json";
      String sign = StringUtils.lowerCase(MD5.toMD5Code(SOA_INTERFACE_KEY + userId + borrowNid + money + platform
              + couponGrantId + ip + ordId + couponOldTime + timestamp + SOA_INTERFACE_KEY));
      params.put("chkValue", sign);

      String result = HttpClientUtils.post(requestUrl, params);
      JSONObject status = JSONObject.parseObject(result);
      System.out.println(status.toJSONString());
	}
  
}  