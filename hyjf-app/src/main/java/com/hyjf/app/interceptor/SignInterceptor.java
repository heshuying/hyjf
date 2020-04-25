package com.hyjf.app.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hyjf.app.BaseResultBean;
import com.hyjf.app.server.ServerDefine;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;

/**
 * 
 * Sign拦截器 对所有请求进行拦截，验证Sign信息，验证通过放行，验证失败返回失败信息
 * 
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年2月18日
 * @see 上午8:14:14
 */
public class SignInterceptor extends HandlerInterceptorAdapter {

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
		// 请求资源路径
		BaseResultBean resultBean = new BaseResultBean(CustomConstants.SIGN_INTERCEPTOR);
		String uri = request.getRequestURI();
		String platForm = request.getParameter("platform");
		String redirect = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + CustomConstants.ERROR_PAGE;
		if (CustomConstants.CLIENT_ANDROID.equals(platForm) || CustomConstants.CLIENT_IOS.equals(platForm)) {// 如果是android或者ios平台
			String url = uri.substring(uri.lastIndexOf('/'));
			String sign;
			if (ServerDefine.GET_BEST_SERVER_MAPPING.equals(url)) {
				return true;
			}
			sign = request.getParameter("sign");
			if (StringUtils.isBlank(sign)) {
				if (CustomConstants.GET.equals(request.getMethod())) {// 如果是get请求重定向到异常页面
					response.sendRedirect(redirect);// 重定向到异常页面
				} else {// 其他请求则将数据返回给app客户端
					resultBean.setStatus("707");
					resultBean.setStatusDesc("sign属性异常");
					JSON.writeJSONStringTo(resultBean, response.getWriter(), SerializerFeature.WriteMapNullValue);
				}
			} else {
				// 获取sign缓存
				String value = RedisUtils.get(sign);
				if (null != value) {
					return true;
				} else {
					if (CustomConstants.GET.equals(request.getMethod())) {// 如果是get请求重定向到异常页面
						response.sendRedirect(redirect);// 重定向到异常页面
					} else {// 其他请求则将数据返回给app客户端
						resultBean.setStatus("707");
						resultBean.setStatusDesc("sign属性异常");
						JSON.writeJSONStringTo(resultBean, response.getWriter(), SerializerFeature.WriteMapNullValue);
					}
				}
			}
		} else {// 如果非android或者ios平台
			response.sendRedirect(redirect);// 重定向到异常页面
		}
		return false;
	}

}
