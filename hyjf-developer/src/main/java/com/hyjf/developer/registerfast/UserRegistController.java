package com.hyjf.developer.registerfast;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.developer.BaseController;
import com.hyjf.developer.utils.RandomNumber;

@Controller("userRegistController")
@RequestMapping(value = UserRegistDefine.REQUEST_MAPPING)
public class UserRegistController extends BaseController {
    
    public static final String DEFALT_PWD = "qq1234";

	@Autowired
	private UserRegistService registService;
	
	/**
	 * 用户注册初始化画面数据保存（保存到session）
	 */
	@ResponseBody
	@RequestMapping(value = UserRegistDefine.USER_REGIST_ACTION, produces = "application/json; charset=utf-8")
	public JSONObject regist(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.USER_REGIST_ACTION);

		JSONObject ret = new JSONObject();
		
		// 密码
		String password = request.getParameter("pwd");
		// 手机号
		String mobile = request.getParameter("phone");
		// 推荐人
		String reffer = request.getParameter("reffer");

		if(StringUtils.isEmpty(mobile)){
		    mobile = RandomNumber.getPhoneNum();
		    if (registService.existUser(mobile)) {
		        mobile = RandomNumber.getPhoneNum();
		        if (registService.existUser(mobile)) {
	                mobile = RandomNumber.getPhoneNum();
	                if (registService.existUser(mobile)) {
	                    mobile = RandomNumber.getPhoneNum();
	                }
	            }
            }
		}
		
		if(StringUtils.isEmpty(password)){
		    password = DEFALT_PWD;
		}
		
		// 登录IP
		String loginIp = GetCilentIP.getIpAddr(request);
		// ------以下是通过着陆页注册 补充字段-------------
		String utm_id = request.getParameter("utm_id");// 链接唯一id
		if (request.getSession().getAttribute("utm_id") == null && StringUtils.isNotEmpty(utm_id)) {
			request.getSession().setAttribute("utm_id", utm_id);
		}
		String utm_source = request.getParameter("utm_source");// 推广渠道
		String utm_medium = request.getParameter("utm_medium");// 推广方式
		String utm_content = request.getParameter("utm_content");// 推广单元
		
        if (registService.existUser(mobile)) {
            ret.put("status", "注册失败,手机号已存在");
            return ret;
        }

		// 注册
		int userid = registService.insertUserActionNew(mobile, password, "", reffer, loginIp,
				CustomConstants.CLIENT_PC, utm_id, utm_source, utm_medium, utm_content, request, response);

		if(userid > 0){
		    ret.put("status", "注册成功");
		    ret.put("phone", mobile);
		    ret.put("pwd", password);
		    ret.put("reffer", reffer);
		}else {
		    ret.put("status", "注册失败");
		}
		return ret;
	}
	
}
