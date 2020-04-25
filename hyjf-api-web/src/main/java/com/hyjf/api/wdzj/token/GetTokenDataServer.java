package com.hyjf.api.wdzj.token;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 网贷之家token获取
 * @author hesy
 *
 */
@Controller
@RequestMapping(value = "/wdzj/token")
public class GetTokenDataServer extends BaseController{

	Logger _log = LoggerFactory.getLogger(GetTokenDataServer.class);
    
    /**
     * token获取接口
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getToken")
    public JSONObject getToken(HttpServletRequest request, HttpServletResponse response) {
    	JSONObject result = new JSONObject();
    	String usernameRight = PropUtils.getSystem("hyjf.wdzj.username");
    	String passwordRight = PropUtils.getSystem("hyjf.wdzj.password");
    	
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        _log.info("网贷之家 getToken接口请求参数：username=" + username + " password=" + password);
        
        if (Validator.isNull(username) || Validator.isNull(password)) {
        	result = getFailMsg("1", "用户名或密码为空");
            _log.info("网贷之家 getToken接口返回：" + result.toJSONString());
            return result;
        }
        if(!username.equals(usernameRight)){
        	result = getFailMsg("1", "用户名不存在");
            _log.info("网贷之家 getToken接口返回：" + result.toJSONString());
            return result;
        }
        if(!password.equals(passwordRight)){
        	result = getFailMsg("1", "密码不正确");
            _log.info("网贷之家 getToken接口返回：" + result.toJSONString());
            return result;
        }
        
        String token = getTokenInRedis("token_wdzj_" + username);
        result = getSuccMsg("0", "成功", token);
        _log.info("网贷之家 getToken接口返回：" + result.toJSONString());
        
        return result;
    }
    
    /**
     * redis中获取token值
     * @param key
     * @return
     */
    private String getTokenInRedis(String key){
    	if(RedisUtils.exists(key)){
    		return RedisUtils.get(key);
    	}else{
    		RedisUtils.set(key, UUID.randomUUID().toString(), 60*60*3);
    		return RedisUtils.get(key);
    	}
    }

    
	private JSONObject getFailMsg(String status, String desc) {
		JSONObject result = new JSONObject();
		JSONObject token = new JSONObject();
		token.put("token", "");
		
		result.put("status", status);
		result.put("statusDesc", desc);
		result.put("data", token);
		return result;
	}
	
	private JSONObject getSuccMsg(String status, String desc, String tokenString) {
		JSONObject result = new JSONObject();
		JSONObject token = new JSONObject();
		token.put("token", tokenString);

		result.put("status", status);
		result.put("statusDesc", desc);
		result.put("data", token);
		return result;
	}
    
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString());
	}
    
    
}
