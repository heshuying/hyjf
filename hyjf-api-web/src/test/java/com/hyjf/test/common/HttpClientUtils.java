package com.hyjf.test.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;



public class HttpClientUtils {
	private static Logger log = Logger.getLogger(HttpClientUtils.class);  
    
    public static String post(String url, Map<String, String> params) {  
        HttpClient httpclient = HttpClients.createDefault();
        String body = null;  
          
        log.info("create httppost:" + url);  
        HttpPost post = postForm(url, params);  
          
        body = invoke(httpclient, post);  
        // 释放连接
        post.releaseConnection();   
          
        return body;  
    }  
    
    private static String invoke(HttpClient httpclient,  
            HttpUriRequest httpost) {  
          
        HttpResponse response = sendRequest(httpclient, httpost);  
        String body = paseResponse(response);  
          
        return body;  
    }  
  
    @SuppressWarnings("deprecation")
	private static String paseResponse(HttpResponse response) {  
        log.info("get response from http server..");  
        HttpEntity entity = response.getEntity();  
          
        log.info("response status: " + response.getStatusLine());  
        String charset = EntityUtils.getContentCharSet(entity);  
        log.info(charset);  
          
        String body = null;  
        try {  
            body = EntityUtils.toString(entity);  
            log.info(body);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
          
        return body;  
    }  
  
    private static HttpResponse sendRequest(HttpClient httpclient,  
            HttpUriRequest httpost) {  
        log.info("execute post...");  
        HttpResponse response = null;  
          
        try {  
            response = httpclient.execute(httpost);  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return response;  
    }  
  
    @SuppressWarnings("deprecation")
	private static HttpPost postForm(String url, Map<String, String> params){  
          
        HttpPost httpost = new HttpPost(url);  
        List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
          
        Set<String> keySet = params.keySet();  
        for(String key : keySet) {  
            nvps.add(new BasicNameValuePair(key, params.get(key)));  
        }  
          
        try {  
            log.info("set utf-8 form entity to httppost");  
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
          
        return httpost;  
    }  
}
