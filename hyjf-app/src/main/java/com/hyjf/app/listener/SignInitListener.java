/**
 * 移动端网络延迟出现拿不到服务端sign的情况
 * 对此的解决方案是在服务启动时初始化一个通用sign存到redis中
 * 当移动端拿不到sign时，会将通用sign传递到服务端，进行常规验证。
 */
package com.hyjf.app.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.alibaba.fastjson.JSON;
import com.hyjf.app.util.SignValue;
import com.hyjf.common.cache.RedisUtils;

public class SignInitListener implements ServletContextListener {
	// 通用sign值
	private static final String sign = "c916f191-f7c2-4e43-9a7d-0494173f31b97oMb316";
	
	public void contextInitialized(ServletContextEvent event) {
        // 保存到Redis中
        SignValue signValue = new SignValue();
        RedisUtils.set(sign, JSON.toJSONString(signValue));
	}

	public void contextDestroyed(ServletContextEvent event) {
	}

}