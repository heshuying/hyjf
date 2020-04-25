package com.hyjf.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.web.common.HyjfSession;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.login.LoginService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.web.util.WebUtils;

import java.util.Date;

/**
 * 
 * Session拦截器
 * 判断当前请求中是否携带了sessionId,如果没有则生成sessionId编号并写入cookie
 * 判断是否是登出请求,如果是登出请求则直接注销用户的session,并将sessionId从cookie中删除
 * 
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年3月29日
 * @see 上午9:21:03
 */
public class SessionInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    LoginService loginService;
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
        // 判断是否是登录退出请求
        String url = request.getRequestURI().substring(request.getRequestURI().lastIndexOf('/'));
        String sessionId = WebUtils.getCookie(request, CustomConstants.SESSIONID);
        if ("/logout.do".equals(url)) {
            if (StringUtils.isNotBlank(sessionId)) {
                String result = RedisUtils.get(sessionId);// 使用sessionId从缓存中获取session对象
                if (StringUtils.isNotBlank(result)) {// 如果session对象不为空
                    HyjfSession session = JSON.parseObject(result, HyjfSession.class);
                    WebViewUser user = session.getUser();
                    if(user!=null){
                        UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
                        userOperationLogEntity.setOperationType(2);
                        userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
                        userOperationLogEntity.setPlatform(0);
                        userOperationLogEntity.setRemark("");
                        userOperationLogEntity.setOperationTime(new Date());
                        userOperationLogEntity.setUserName(user.getUsername());
                        userOperationLogEntity.setUserRole(user.getRoleId());
                        loginService.sendUserLogMQ(userOperationLogEntity);
                    }
                }
                // 删除session
                RedisUtils.del(sessionId);
                // 清除cookie
                WebUtils.removeCookie(request, response, sessionId);
                WebUtils.removeCookie(request, response, "hyjfUsername");
                WebUtils.removeCookie(request, response, "sex");
                WebUtils.removeCookie(request, response, "iconurl");
                WebUtils.removeCookie(request, response, "roleId");
                // 推广渠道追加  liuyang 20170605 start
                WebUtils.removeCookie(request, response, "utm_id");
                // 推广渠道追加  liuyang 20170605 end
                // 重定向到登录首页
                response.sendRedirect(CustomConstants.HOST + "/homepage/home.do");
                return false;
            }
        } else {
            if (StringUtils.isNotBlank(sessionId)) {
                return true;
            } else {
                // 生成sessionId并写入cookie
                sessionId = WebUtils.createSessionId();
                WebUtils.addCookie(request, response, CustomConstants.SESSIONID, sessionId, null,
                        InterceptorDefine.HYJF_WEB_DOMAIN_NAMES_LIST.get(0));
                return true;
            }
        }
        return false;
    }

}
