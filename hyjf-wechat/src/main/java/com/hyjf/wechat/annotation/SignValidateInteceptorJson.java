package com.hyjf.wechat.annotation;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.util.AppUserToken;
import com.hyjf.wechat.util.ResultEnum;
import com.hyjf.wechat.util.SecretUtil;

@Component
public class SignValidateInteceptorJson extends HandlerInterceptorAdapter {

	public SignValidateInteceptorJson() {
	}

	private List<String> allowUrls;

	public List<String> getAllowUrls() {
		return allowUrls;
	}

	public void setAllowUrls(List<String> allowUrls) {
		this.allowUrls = allowUrls;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		response.setCharacterEncoding("UTF-8");
		HandlerMethod methodHandler = (HandlerMethod) handler;
		SignValidate signValidate = methodHandler.getMethodAnnotation(SignValidate.class);
		// 如果方法中添加了@SignValidate 这里的signValidate不为null
		// 需要检查sign
		String sign = request.getParameter("sign");
		Integer userId = null;
		String accountId = null;
		if (StringUtils.isBlank(sign)) {
			sign = (String) request.getAttribute("sign");
		}
//		System.out.println("sign:" + sign);
		if (StringUtils.isNotBlank(sign)) {
			// 获取用户ID
			AppUserToken token = SecretUtil.getUserId(sign);
			if (token != null) {
				userId = token.getUserId();
				accountId = token.getAccountId();
			}
			if (userId != null && userId - 0 > 0) {
				// 需要刷新 sign
				SecretUtil.refreshSign(sign);
			}
			request.setAttribute("userId", userId);
			request.setAttribute("accountId", accountId);
		}
		if (signValidate != null) {
			BaseResultBean result = new BaseResultBean();
			response.setContentType("application/json; charset=utf-8");
			PrintWriter out = null;
			if (StringUtils.isNotBlank(sign)) {
				// 如果查询不到 证明过期了 需要重新登录
				if (userId == null || userId - 0 == 0) {
					out = response.getWriter();
					out.append(JSONObject.toJSONString(result.setEnum(ResultEnum.LOGININVALID)));
					return false;
				}
			} else {
				out = response.getWriter();
				out.append(JSONObject.toJSONString(result.setEnum(ResultEnum.NOTLOGIN)));
				return false;
			}
		}
		return true;
	}
}
