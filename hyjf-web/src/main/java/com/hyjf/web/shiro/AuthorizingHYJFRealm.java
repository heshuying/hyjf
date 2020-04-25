package com.hyjf.web.shiro;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
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
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.DigitalUtils;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.web.user.login.LoginService;

@Aspect
public class AuthorizingHYJFRealm extends AuthorizingRealm {

	private static Logger logger = LoggerFactory.getLogger(AuthorizingHYJFRealm.class);

	@Autowired
	private LoginService loginService;
	
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
		if (StringUtils.isBlank(username)) {
			throw new AuthenticationException();
		}
		if (!DigitalUtils.isInteger(username)) {
			throw new AuthenticationException();
		}
		Users users = loginService.getUsers(Integer.valueOf(username));

		if (users != null) {
			return new SimpleAuthenticationInfo(users, password, getName());
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
		Users users = (Users) principals.getPrimaryPrincipal();

		// 角色
		Set<String> roles = new HashSet<String>();
		roles.add(ShiroConstants.ROLE_NORMAL_USER);
		
		// 权限
		Set<String> permissions = new HashSet<String>();

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(roles);
        info.setStringPermissions(permissions);
        
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		session.setAttribute(CustomConstants.LOGIN_USER_INFO, users);

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
