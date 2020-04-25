package com.hyjf.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.VipInfo;
import com.hyjf.web.common.HyjfSession;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.coupon.CouponService;
import com.hyjf.web.util.WebUtils;
import com.hyjf.web.vip.apply.ApplyService;

/**
 * 
 * 用户登录拦截器
 * 如果用户没有登录则需要跳转登录页面或者弹出登录窗口
 * 
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年3月29日
 * @see 上午9:26:13
 */
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private CouponService couponService;

    /**
     * 在DispatcherServlet完全处理完请求后被调用
     * 
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
     */
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
        throws Exception {

    }

    /**
     * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
        WebViewUser user = WebUtils.getUser(request);
        // 非ajax请求
        if (modelAndView != null) {
            if (user != null) {
                // 登录用户
                UsersInfo usersInfo = applyService.getUsersInfoByUserId(user.getUserId());
                modelAndView.addObject("currentUsersInfo", usersInfo);
                boolean isVip = usersInfo.getVipId() != null ? true : false;
                modelAndView.addObject("isVip", isVip);
                if (isVip) {
                    VipInfo vipInfo = applyService.getVipInfo(usersInfo.getVipId());
                    modelAndView.addObject("vipName", vipInfo.getVipName());
                }

                Integer validCount = couponService.selectCouponValidCount(user.getUserId());
                modelAndView.addObject("couponValidCount", validCount);
            }
        }
        response.addHeader("Cache-Control", "no-store");
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
        // 判断用户是否已经登录
        String sessionId = WebUtils.getCookie(request, "sessionId");
        if (StringUtils.isNotBlank(sessionId)) {
            String result = RedisUtils.get(sessionId);// 使用sessionId从缓存中获取session对象
            if (StringUtils.isNotBlank(result)) {// 如果session对象不为空
                HyjfSession session = JSON.parseObject(result, HyjfSession.class);
                WebViewUser user = session.getUser();
                if (null == user) {// 说明用户没有登录
                    WebUtils.redirectTargetPage(response, xmlHttpRequest, "/user/login/init.do", "707");
                } else {// 说明用户已经登录
                    RedisUtils.expire(sessionId, CustomConstants.SESSION_EXPIRE);
                    String hyjfUsername = user.getUsername();
                    String iconurl = user.getIconurl();
                    String sex = user.getSex().toString();
                    String roleId = user.getRoleId();
                    WebUtils.addCookie(request, response, "hyjfUsername", hyjfUsername, CustomConstants.SESSION_EXPIRE,
                            InterceptorDefine.HYJF_WEB_DOMAIN_NAMES_LIST.get(0));
                    WebUtils.addCookie(request, response, "sex", sex, CustomConstants.SESSION_EXPIRE,
                            InterceptorDefine.HYJF_WEB_DOMAIN_NAMES_LIST.get(0));
                    if (StringUtils.isNotBlank(iconurl)) {
                        WebUtils.addCookie(request, response, "iconurl", iconurl, CustomConstants.SESSION_EXPIRE,
                                InterceptorDefine.HYJF_WEB_DOMAIN_NAMES_LIST.get(0));
                    }
                    if (StringUtils.isNotBlank(roleId)) {
                        WebUtils.addCookie(request, response, "roleId", roleId, CustomConstants.SESSION_EXPIRE,
                                InterceptorDefine.HYJF_WEB_DOMAIN_NAMES_LIST.get(0));
                    }
                    return true;
                }
            } else {
                WebUtils.redirectTargetPage(response, xmlHttpRequest, "/user/login/init.do", "707");
            }
        } else {
            // 生成sessionId并写入cookie
            sessionId = WebUtils.createSessionId();
            WebUtils.addCookie(request, response, CustomConstants.SESSIONID, sessionId, null,
                    InterceptorDefine.HYJF_WEB_DOMAIN_NAMES_LIST.get(0));
            WebUtils.redirectTargetPage(response, xmlHttpRequest, "/user/login/init.do", "707");
        }
        return false;
    }

}
