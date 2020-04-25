package com.hyjf.server.module.wkcd;

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

public class GetToken {
    /**
     * 测试
     * @param args
     * @throws Exception
     */
     @SuppressWarnings("deprecation")
     public static void main(String[] args) throws Exception {
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
                     System.out.println(token);
                 }
             }
         }else{
             throw new Exception(String.valueOf(retResult.get("info")));
         }
     }
}
