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
import com.hyjf.web.util.WebUtils;

/**
 * 
 * 为需要生成表单令牌的请求创建令牌
 * 
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年3月29日
 * @see 上午9:35:10
 */
public class TokenGrantInterceptor extends HandlerInterceptorAdapter {

    /**
     * 在DispatcherServlet完全处理完请求后被调用
     * 
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
     */
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
        throws Exception {

    }

    /**
     * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
    	// 非ajax请求
    	if (modelAndView != null) {
    		String sessionId = WebUtils.getCookie(request, CustomConstants.SESSIONID);
            // 生成令牌追加到modelview中
            String token = WebUtils.createToken();
            HyjfSession session = WebUtils.getHyjfSession(request, response, true);
            if (StringUtils.isNotBlank(sessionId) && session.addToken(token)) {
                RedisUtils.set(sessionId, JSON.toJSONString(session), CustomConstants.SESSION_EXPIRE);
                modelAndView.addObject("tokenGrant", token);
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
