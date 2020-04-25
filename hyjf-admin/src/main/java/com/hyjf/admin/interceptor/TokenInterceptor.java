package com.hyjf.admin.interceptor;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hyjf.admin.annotate.Token;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.SessionUtils;

public class TokenInterceptor extends HandlerInterceptorAdapter {

	public static final String RESUBMIT_TOKEN = "RESUBMIT_TOKEN";
	public static final String JSPPAGE_HIDDEN_TOKEN = "pageToken";

	public String[] allowUrls;// 还没发现可以直接配置不拦截的资源，所以在代码里面来排除

	public void setAllowUrls(String[] allowUrls) {
		this.allowUrls = allowUrls;
	}

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
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

		if (handler instanceof HandlerMethod) {
			Subject currentUser = SecurityUtils.getSubject();
			Session session = currentUser.getSession();
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			// 获取自定义的注解
			Token annotation = method.getAnnotation(Token.class);
			// 如果 方法中有注解
			if (annotation != null) {
				boolean needSaveSession = annotation.save();
				if (needSaveSession) {
					// 生成UUID放到session中
					session.setAttribute(RESUBMIT_TOKEN, UUID.randomUUID().toString());
				}

				boolean checkSession = annotation.check();
				if (checkSession) {
					Map<String, Object> map = modelAndView.getModel();
					if (map.get(CustomConstants.FORM_HAS_ERROR) == null) {
						// 生成UUID放到session中
						session.setAttribute(RESUBMIT_TOKEN, UUID.randomUUID().toString());
					}
				}
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
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		String requestUrl = request.getRequestURI().replace(request.getContextPath(), "");
		//System.out.println(requestUrl);
		if (null != allowUrls && allowUrls.length >= 1) {
			for (String url : allowUrls) {
				if (requestUrl.contains(url)) {
					return true;
				}
			}
		}

		String fid = request.getParameter(CustomConstants.FUNCTION_ID);
		if (StringUtils.isNotEmpty(fid)) {
			SessionUtils.setSession(CustomConstants.FUNCTION_ID, fid);
		}

		// java 中的 instanceof 运算符是用来在运行时指出对象是否是特定类的一个实例。
		// instanceof 通过返回一个布尔值来指出，这个对象是否是这个特定类或者是它的子类的一个实例。
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			// 获取自定义的注解
			Token annotation = method.getAnnotation(Token.class);
			// 如果 方法中有注解
			if (annotation != null) {
				// 获取 注解时赋予的值。 在执行某个方法时生成token令牌，以便登录数据时验证。
				boolean checkSession = annotation.check();
				if (checkSession) {
					// 获取 注解时赋予的值。 在执行某个方法时删除调token令牌，以便防止重复登录。
					if (isRepeatSubmit(request)) {
						// 获取 注解时赋予的值。 如果是重复提交的情况下，往哪个画面迁移。
						String forward = annotation.forward();
						response.sendRedirect(request.getContextPath() + "/" + forward);
						return false;
					}
				}
			}
			return true;
		} else {
			return super.preHandle(request, response, handler);
		}
	}

	/**
	 * 是否是重复提交
	 * 
	 * @param request
	 * @return
	 */
	private boolean isRepeatSubmit(HttpServletRequest request) {
		if (request.getSession(false) == null) {
			return true;
		}

		// 获取session中的token令牌值
		String serverToken = (String) request.getSession(false).getAttribute(RESUBMIT_TOKEN);
		if (serverToken == null) {
			return true;
		}

		// 获取jsp画面中中的token令牌值
		String clinetToken = request.getParameter(JSPPAGE_HIDDEN_TOKEN);
		if (clinetToken == null) {
			return true;
		}

		// 判断是否相等
		if (!serverToken.equals(clinetToken)) {
			return true;
		}

		return false;
	}

}
