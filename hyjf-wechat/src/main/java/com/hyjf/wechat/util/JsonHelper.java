package com.hyjf.wechat.util;

import com.alibaba.fastjson.JSONObject;

public class JsonHelper {

    /**
     * 
     * 未登录
     * @author sunss
     * @return
     */
    public static JSONObject getNotLogin(){
        JSONObject jso = new JSONObject();
        jso.put("status", "998");
        jso.put("StatusDesc", "请先登录!");
        return jso;
    }
    
    /**
     * 
     * 需要重新登录
     * @author sunss
     * @return
     */
    public static JSONObject getLoginInvalid(){
        JSONObject jso = new JSONObject();
        jso.put("status", "998");
        jso.put("StatusDesc", "登录过期,请重新登录!");
        return jso;
    }
}
