package com.hyjf.server.module.wkcd.user.regist;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.server.BaseController;

@Controller
@RequestMapping(value = WkcdRegistDefine.REQUEST_MAPPING)
public class WkcdRegistController extends BaseController {
	@Autowired
	private WkcdRegistService wkcdRegistService;
    
	@RequestMapping(value=WkcdRegistDefine.USER_REGIST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public JSONObject registAction(HttpServletRequest request,HttpServletResponse response){
		JSONObject ret = new JSONObject();
		// 登录IP
        String loginIp = GetCilentIP.getIpAddr(request);
		//获取明文数据
		String requestObjectMingwen = request.getAttribute("requestObject").toString();
		if(StringUtils.isEmpty(requestObjectMingwen)){
			ret.put("status", "1");
	        ret.put("statusDesc", "接口调用失败,参数为空");
	        return ret;
		}
        Map<String, String> map = parseRequestJson(requestObjectMingwen);
        String mobile= map.get("mobile");
        if (Validator.isNull(mobile)) {
        	ret.put("status", "1");
	        ret.put("statusDesc", "手机号码为空");
	        return ret;
        }

        if (!Validator.isMobile(mobile)) {
        	ret.put("status", "1");
	        ret.put("statusDesc", "手机号码不正确");
	        return ret;
        }
        //根据手机号验证用户是否已存在。
        if(wkcdRegistService.existUser(mobile)){
        	ret.put("status", "1");
	        ret.put("statusDesc", "手机号码已经在汇盈金服存在");
	        Users u= wkcdRegistService.getUserByMobile(mobile);
	        if(u!=null){
		        ret.put("userId", u.getUserId());
	        }
	        return ret;
        }
        //密码统一用初始密码   //密码首位必须为字母,必须由数字和字母组成,长度6-16位
        String password= PropUtils.getSystem("hyjf.wkcd.password").trim();
        try{
	        //注册
			Users user = new Users();
			user.setLoginIp(loginIp);
	        int userid= wkcdRegistService.registUser(mobile,password,user);
	        if(userid==1){
				JSONObject responseObject = new JSONObject();
				responseObject.put("userId", user.getUserId());
				ret.put("responseObject", responseObject);
				
	        	ret.put("status", "0");
				ret.put("statusDesc", "注册成功");
	        }else{
	        	ret.put("status", "1");
				ret.put("statusDesc", "注册失败,参数异常");
	        }
        }catch(Exception e){
        	ret.put("status", "1");
			ret.put("statusDesc", "注册发生错误,参数异常");
        }
        LogUtil.endLog(WkcdRegistDefine.THIS_CLASS, WkcdRegistDefine.USER_REGIST_ACTION);
        return ret;
	} 
	
}
