package com.hyjf.batch.wkcd;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.http.HttpRequest;
import com.hyjf.common.security.utils.AESUtil;
import com.hyjf.common.security.utils.BASE64;
import com.hyjf.common.security.utils.RSAUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;

/**
 * 微可车贷Token定时获取
 * 
 * @author 朱晓东
 */
public class WkcdTokenTask {
	/** 运行状态 */
	private static int isRun = 0;

	/**
	 * 微可车贷Token定时获取
	 */
	public void run(){
		onTimeWkcdToken();
	}

	/**
	 * 调用微可车贷Token定时获取
	 *
	 * @return
	 */
	private boolean onTimeWkcdToken() {
		if (isRun == 0) {
			System.out.println("微可车贷Token定时获取 WkcdTokenTask.run ... "+new Date());
			isRun = 1;
			try {
			    JSONObject ret = new JSONObject();
			    ret.put("version", PropUtils.getSystem("wkcd.version"));
			    ret.put("requestTime", GetDate.yyyymmddhhmmss.format(new Date()));
			    ret.put("partnerId", PropUtils.getSystem("wkcd.partnerId"));
			    RSAUtil rsa = new RSAUtil();
			    Map<String, Object> params = new HashMap<String, Object>();
		        //params.put("_request_body", BASE64.encode(rsa.encrypt(RSAUtil.getPublicKey(PropUtils.getSystem("wkcd.rsa.publicKey")),ret.toString().getBytes())));
		        params.put("_request_body", URLEncoder.encode(BASE64.encode(rsa.encryptByPublicKey(ret.toString().getBytes("UTF-8"),PropUtils.getSystem("wkcd.rsa.publicKey")))));
		        String result = HttpRequest.sendPost(PropUtils.getSystem("wkcd.host")+"/thirdparty/getToken", params);
		        JSONObject retResult = JSONObject.parseObject(result);
		        if(String.valueOf(retResult.get("code")).equals("10000")){
		            String response = String.valueOf(retResult.get("response"));
		            if(response!=null && !"".equals(response)){
		                response = AESUtil.decryptAES(response, PropUtils.getSystem("wkcd.aes.key"));
		                JSONObject responseJson = JSONObject.parseObject(response);
		                if(responseJson.get("token")!=null){
		                    String token = String.valueOf(responseJson.get("token"));
		                    RedisUtils.set("Third-Party-WKCD-Token", token);
		                }
		            }
		        }else{
		            throw new Exception(String.valueOf(retResult.get("info")));
		        }
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
			}
			System.out.println("微可车贷Token定时获取 WkcdTokenTask.end ... "+new Date());
		}
		return false;
	}
}
