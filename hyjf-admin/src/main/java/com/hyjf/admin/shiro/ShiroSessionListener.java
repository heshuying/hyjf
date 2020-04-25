package com.hyjf.admin.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

public class ShiroSessionListener implements SessionListener {

	/**
	 * 会话创建时触发
	 * 
	 * @param session
	 * @author Administrator
	 */

	@Override
	public void onStart(Session session) {
		// System.out.println("会话创建时长：" + session.getId() + ", " +
		// session.getTimeout() + ", " + session.getStartTimestamp());
	}

	/**
	 * 退出/会话过期时触发
	 * 
	 * @param session
	 * @author Administrator
	 */

	@Override
	public void onStop(Session session) {
		// System.out.println("会话过期：" + session.getId());
	}

	/**
	 * 会话过期时触发
	 * 
	 * @param session
	 * @author Administrator
	 */

	@Override
	public void onExpiration(Session session) {
		// System.out.println("会话停止：" + session.getId());
	}
}
