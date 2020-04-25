package com.hyjf.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.web.common.HyjfSession;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.util.WebUtils;

/**
 * 
 * 判断用户是否在汇付开户
 * 
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月18日
 * @see 下午12:52:16
 */
public class BankIsSetPasswordInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 在DispatcherServlet完全处理完请求后被调用
	 * 
	 * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
	 */
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {

	}

	/**
	 * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}

	/**
	 * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
	 * 
	 * 如果返回true 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链,
	 * 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion()
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String xmlHttpRequest = request.getHeader("x-requested-with");
		String sessionId = WebUtils.getCookie(request, CustomConstants.SESSIONID);
		if (StringUtils.isBlank(sessionId)) {
			sessionId = WebUtils.createSessionId();
			WebUtils.addCookie(request, response, CustomConstants.SESSIONID, sessionId, null, InterceptorDefine.HYJF_WEB_DOMAIN_NAMES_LIST.get(0));
		}
		// 获取登录的用户
		String result = RedisUtils.get(sessionId);// 使用sessionId从缓存中获取session对象
		if (StringUtils.isBlank(result)) {// 如果用户没有登录
			WebUtils.redirectTargetPage(response, xmlHttpRequest, "/user/login/init.do", "707");
		} else {
			HyjfSession session = JSON.parseObject(result, HyjfSession.class);
			WebViewUser webViewUser = session.getUser();
			if (null == webViewUser) {// 如果用户没有登录
				WebUtils.redirectTargetPage(response, xmlHttpRequest, "/user/login/init.do", "707");
				WebUtils.redirectTargetPage(response, xmlHttpRequest, "/user/login/init.do", "707");
			} else {
				// 判断该用户是否设置江西银行交易密码
				if (webViewUser.getIsSetPassword()==1) {
					return true;
				} else {
				    // 重定向到设置交易密码页面
					WebUtils.redirectTargetPage(response, xmlHttpRequest, "/bank/user/transpassword/setPassword.do", "709");
				}
			}
		}
		return false;
	}
}
