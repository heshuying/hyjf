package com.hyjf.app.interceptor;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hyjf.app.BaseResultBean;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;

/**
 * 
 * Order拦截器 验证数据是否是由信任的客户端发出
 * 
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年2月18日
 * @see 上午8:14:29
 */
public class OrderInterceptor extends HandlerInterceptorAdapter {
	private static Logger _log = Logger.getLogger(OrderInterceptor.class);  
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
		BaseResultBean resultBean = new BaseResultBean(CustomConstants.ORDER_INTERCEPTOR);
		String sign = request.getParameter("sign");
		String token = request.getParameter("token");
		String order = request.getParameter("order");
		String platform = request.getParameter("platform");
		String redirect = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + CustomConstants.ERROR_PAGE;
		if (CustomConstants.GET.equals(request.getMethod()) && CustomConstants.CLIENT_IOS.equals(platform)) {// 如果是ios
			// get请求，则需要对token和order进行解码
			if (StringUtils.isNotBlank(token)) {
				token = URLDecoder.decode(token, "utf-8");
			}
			order = URLDecoder.decode(order, "utf-8");
		}
		String randomString = request.getParameter("randomString");
		String key = SecretUtil.getKey(sign);
		if (!StringUtils.isBlank(token)) {
			if (SecretUtil.checkOrder(key, token, randomString, order)) {// token校验
				return true;
			} else {
				if (CustomConstants.GET.equals(request.getMethod())) {
					response.sendRedirect(redirect);// 重定向到异常页面
				} else {
					resultBean.setStatus("403");
					resultBean.setStatusDesc("校验异常");
					JSON.writeJSONStringTo(resultBean, response.getWriter(), SerializerFeature.WriteMapNullValue);
					_log.info("order校验异常");
					_log.info("key:"+key);
					_log.info("token:"+token);
					_log.info("randomString:"+randomString);
					_log.info("order:"+order);
					
				}
			}
		} else {
			if (SecretUtil.checkOrder(key, randomString, order)) {// token校验
				return true;
			} else {
				if (CustomConstants.GET.equals(request.getMethod())) {
					response.sendRedirect(redirect);// 重定向到异常页面
				} else {
					resultBean.setStatus("403");
					resultBean.setStatusDesc("校验异常");
					JSON.writeJSONStringTo(resultBean, response.getWriter(), SerializerFeature.WriteMapNullValue);
					_log.info("order校验异常");
					_log.info("key:"+key);
					_log.info("token:"+token);
					_log.info("randomString:"+randomString);
					_log.info("order:"+order);
				}
			}
		}
		return false;
	}
}
