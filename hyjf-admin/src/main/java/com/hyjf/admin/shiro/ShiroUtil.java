package com.hyjf.admin.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.SpringContextHolder;
import com.hyjf.mybatis.model.customize.AdminSystem;

public class ShiroUtil {

    /**
     * 取得登录用户ID
     * 
     * @param key
     * @param value
     */
    public static String getLoginUserId() {
        AdminSystem as = getLoginUserInfo();
        return as == null ? "" : as.getId();
    }

    /**
     * 取得登录用户名
     * 
     * @param key
     * @param value
     */
    public static String getLoginUsername() {
        AdminSystem as = getLoginUserInfo();
        return as == null ? "" : as.getUsername();
    }

    /**
     * 取得登录用户角色名
     * 
     * @param key
     * @param value
     */
    public static Integer getLoginUserRoleId() {
        AdminSystem as = getLoginUserInfo();
        return as == null ? 0 : as.getRoleId();
    }

    /**
     * 取得登录用户信息
     * 
     * @param key
     * @param value
     */
    public static AdminSystem getLoginUserInfo() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        if (session.getAttribute(CustomConstants.LOGIN_USER_INFO) != null) {
            return (AdminSystem) session.getAttribute(CustomConstants.LOGIN_USER_INFO);
        }
        return null;
    }
    
    /**
     * 更新权限
     */
    public static void updateAuth() {
        AuthorizingHYJFRealm authorizingHYJFRealm = SpringContextHolder.getBean(AuthorizingHYJFRealm.class);
        authorizingHYJFRealm.clearCachedAuthorizationInfo(getLoginUsername());// 清除权限缓存
        authorizingHYJFRealm.isPermitted(SecurityUtils.getSubject().getPrincipals(), String.valueOf(System.currentTimeMillis()));
    }
}
