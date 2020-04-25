package com.hyjf.app.interceptor;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hyjf.app.BaseResultBean;
import com.hyjf.app.util.SignValue;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;

/**
 * 
 * Token拦截器 对用户登录之后才有权查看的资源请求进行拦截，验证Token信息，验证通过放行，验证失败返回失败信息
 * 
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年2月18日
 * @see 上午8:14:29
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {
	Logger _log = LoggerFactory.getLogger(TokenInterceptor.class);
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
	}

	/**
	 * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
	 * 
	 * 如果返回true 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链,
	 * 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion()
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		BaseResultBean resultBean = new BaseResultBean(CustomConstants.TOKEN_INTERCEPTOR);
		String sign = request.getParameter("sign");
		String token = request.getParameter("token");
		String platform = request.getParameter("platform");
		String redirect = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + CustomConstants.ERROR_PAGE;
		if (!StringUtils.isBlank(token)) {
			if (CustomConstants.GET.equals(request.getMethod()) && CustomConstants.CLIENT_IOS.equals(platform)) {// 如果是ios
				// get请求，则需要对token进行解码
				token = URLDecoder.decode(token, "utf-8");
			}
			if (StringUtils.isBlank(sign)) {// 如果sign是空
				if (CustomConstants.GET.equals(request.getMethod())) {// 如果是get请求，则重定向到异常页面
					response.sendRedirect(redirect);// 重定向到异常页面
				} else {// 如果是其他请求，则将错误信息返回给app前端
					resultBean.setStatus("707");
					resultBean.setStatusDesc("sign属性异常");
					JSON.writeJSONStringTo(resultBean, response.getWriter(), SerializerFeature.WriteMapNullValue);
				}
			} else {
				String value = RedisUtils.get(sign);
				SignValue signValue = JSON.parseObject(value, SignValue.class);
				if (token.equals(signValue.getToken())) {// 如果token是空
					return true;
				} else {// 如果是其他请求，则将错误信息返回给app前端
					if (CustomConstants.GET.equals(request.getMethod())) {// 如果是get请求，则重定向到异常页面
						response.sendRedirect(redirect);// 重定向到异常页面
					} else {// 如果是其他请求，则将错误信息返回给app前端
						resultBean.setStatus("708");
						resultBean.setStatusDesc("token属性异常请重新登陆");
						JSON.writeJSONStringTo(resultBean, response.getWriter(), SerializerFeature.WriteMapNullValue);
					}
				}
			}
		} else {
			if (CustomConstants.GET.equals(request.getMethod())) {
				response.sendRedirect(redirect);// 重定向到异常页面
			} else {
				resultBean.setStatus("708");
				resultBean.setStatusDesc("token属性异常请重新登陆");
				JSON.writeJSONStringTo(resultBean, response.getWriter(), SerializerFeature.WriteMapNullValue);
			}
		}
		return false;
	}

}
