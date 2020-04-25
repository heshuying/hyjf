package com.hyjf.admin.shiro;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.admin.maintenance.admin.AdminDefine;
import com.hyjf.admin.maintenance.adminsystem.AdminSystemService;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.AdminSystem;

@Aspect
public class AuthorizingHYJFRealm extends AuthorizingRealm {

    private static Logger logger = LoggerFactory.getLogger(AuthorizingHYJFRealm.class);

    @Autowired
    private AdminSystemService adminSystemService;

    public AuthorizingHYJFRealm() {
        super();
    }

    /**
     * 认证回调函数, 登录时调用.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        logger.debug("认证回调函数, 登录时被调用");

        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String username = token.getUsername();
        String password = String.valueOf(token.getPassword());

        if (Validator.isNull(password)) {
            throw new AuthenticationException();
        }

        AdminSystem adminSystem = new AdminSystem();
        adminSystem.setUsername(username);
        adminSystem.setPassword(password);
        adminSystem.setState("NOT CHECK");
        adminSystem = this.adminSystemService.getUserInfo(adminSystem);

        if (adminSystem != null) {
            if (AdminDefine.FLG_DISABLE.equals(adminSystem.getState())) {
                throw new AuthenticationException("user_disabled");
            }

            String userName = (String) authcToken.getPrincipal();
            //处理session
            DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
            DefaultWebSessionManager sessionManager = (DefaultWebSessionManager) securityManager.getSessionManager();
            Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();//获取当前已登录的用户session列表
            for (Session session : sessions) {
                //清除该用户以前登录时保存的session
                if (userName.equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
                    sessionManager.getSessionDAO().delete(session);
                }
            }

            return new SimpleAuthenticationInfo(username, password, getName());
        } else {
            throw new AuthenticationException();
        }
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.在配有缓存的情况下，只加载一次.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        logger.debug("授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.");
        //AdminSystem adminSystem = (AdminSystem) principals.getPrimaryPrincipal();
        String username = (String) principals.getPrimaryPrincipal();
        AdminSystem adminSystem = new AdminSystem();
        adminSystem.setUsername(username);
        adminSystem = this.adminSystemService.getUserInfo(adminSystem);

        // 角色
        Set<String> roles = new HashSet<String>();
        List<Integer> groupList = this.adminSystemService.selectAdminGroup(adminSystem);
        if (groupList != null && groupList.size() > 0) {
            for (Integer groupId : groupList) {
                roles.add(String.valueOf(groupId));
            }
        }

        // 权限
        Set<String> permissions = new HashSet<String>();
        List<AdminSystem> permissionsList = this.adminSystemService.getUserPermission(adminSystem);
        if (permissionsList != null && permissionsList.size() > 0) {
            for (AdminSystem menuPermissions : permissionsList) {
                if (menuPermissions != null) {
                    permissions.add(menuPermissions.getMenuCtrl() + StringPool.COLON + menuPermissions.getPermission());
                }
            }
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(roles);
        info.setStringPermissions(permissions);

        AdminSystem menuParam = new AdminSystem();
        menuParam.setId(adminSystem.getId());
        menuParam.setPermission(ShiroConstants.PERMISSION_VIEW);

        List<AdminSystem> leftMenuTree = this.adminSystemService.selectLeftMenuTree(menuParam);
        SessionUtils.setSession(CustomConstants.MAIN_MENU_TREE, leftMenuTree);
        SessionUtils.setSession(CustomConstants.LOGIN_USER_INFO, adminSystem);

        return info;
    }

    /**
     * 更新用户授权信息缓存.
     */
    public void clearCachedAuthorizationInfo(String principal) {
        logger.debug("更新用户授权信息缓存.");
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
        clearCachedAuthorizationInfo(principals);
    }

    /**
     * 清除所有用户授权信息缓存.
     */
    public void clearAllCachedAuthorizationInfo() {
        logger.debug("清除所有用户授权信息缓存.");
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache != null) {
            for (Object key : cache.keys()) {
                cache.remove(key);
            }
        }
    }
}
