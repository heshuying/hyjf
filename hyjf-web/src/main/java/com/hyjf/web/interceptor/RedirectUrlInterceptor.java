package com.hyjf.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.web.util.WebUtils;

/**
 * 
 * 用户登录后跳转回登录前的页面
 * 
 * @author zhangjp
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年6月14日
 * @see 上午9:34:23
 */
public class RedirectUrlInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 在DispatcherServlet完全处理完请求后被调用
	 * 
	 * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
	 */
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {

	}

	/**
	 * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
	        ModelAndView modelAndView) throws Exception {
		
		// 非ajax请求
		if (modelAndView != null) {
			String sessionId = WebUtils.getCookie(request, CustomConstants.SESSIONID);
			
			if (StringUtils.isEmpty(sessionId)) {
				return;
			}
            
            String requestUrl = request.getRequestURI();
			if(StringUtils.isNotEmpty(request.getQueryString())){
				// 如果有get请求的参数则拼接到url
				requestUrl = requestUrl + "?" + request.getQueryString();
			}
			// 从注册相关的页面跳转到登录页面的请求置空，固定跳转到账户中心
			if(StringUtils.contains(requestUrl, "user/regist")){
				;
			}else if(!StringUtils.contains(requestUrl, "/user/login")){
				// 登录相关的页面url不放入session,否则会出现登录到登录的循环场景
				WebUtils.addCookie(request, response, "redirectUrl", requestUrl, null,
		                InterceptorDefine.HYJF_WEB_DOMAIN_NAMES_LIST.get(0));// 将登陆重定向url写入cookie
			}
			
		}

	}

	/**
	 * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
	 * 
	 * 如果返回true 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链,
	 * 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion()
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		return true;
	}

}
