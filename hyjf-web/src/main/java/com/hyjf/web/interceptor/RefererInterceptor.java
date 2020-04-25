package com.hyjf.web.interceptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hyjf.common.util.CustomConstants;

/**
 * 
 * referer拦截
 * 判断用户的请求来源是否是hyjf.com的域名,如果不是则跳回主站首页
 * 
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年3月29日
 * @see 上午9:34:23
 */
public class RefererInterceptor extends HandlerInterceptorAdapter {

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
    }

    /**
     * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
     * 
     * 如果返回true 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链,
     * 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion()
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String referer = request.getHeader("referer");
        if (StringUtils.isBlank(referer)) {
            System.err.println("没有获取到referer，请联系任星臣+++++++++++++++++++++++++++++++++++++++");
            System.err.println(request.getRequestURI());
            response.sendRedirect(CustomConstants.HOST);
        } else {
            // 获取请求的域名
            String domainName = getDnFromReferer(referer);
            if (InterceptorDefine.HYJF_WEB_DOMAIN_NAMES_LIST.contains(domainName)) {
                // 如果是hyjf.com域名的访问
                return true;
            } else {
                // 如果不是hyjf.com域名的访问 重定向到首页
                response.sendRedirect(CustomConstants.HOST);
            }
        }
        return false;
    }

    private String getDnFromReferer(String referer) {
        String result = null;
        try {
            Pattern p =
                    Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)",
                            Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(referer);
            if (matcher.find()) {
                result = matcher.group();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
}
