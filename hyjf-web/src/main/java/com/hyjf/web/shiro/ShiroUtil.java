package com.hyjf.web.shiro;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.security.utils.ThreeDESUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.Users;

public class ShiroUtil {
	private static String KEY = PropUtils.getSystem("hyjf.3des.key").trim();

	/**
	 * 取得登录用户ID
	 * 
	 * @param key
	 * @param value
	 */
	public static Integer getLoginUserId(HttpServletRequest request) {
		Users as = getLoginUserInfo(request);
		return as == null ? null : as.getUserId();
	}

	/**
	 * 取得登录用户名
	 * 
	 * @param key
	 * @param value
	 */
	public static String getLoginUsername(HttpServletRequest request) {
		Users as = getLoginUserInfo(request);
		return as == null ? "" : as.getUsername();
	}

	/**
	 * 取得登录用户信息
	 * 
	 * @param key
	 * @param value
	 */
	public static Users getLoginUserInfo(HttpServletRequest request) {
		String jsessionid = request.getParameter("jsessionid");
		String jtimestamp = request.getParameter("jtimestamp");
		String kkey = KEY + jtimestamp;

		if (request.getMethod().equalsIgnoreCase("post")) {
			try {
				jsessionid = URLDecoder.decode(jsessionid, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
				return null;
			}

		}
		
		if (jsessionid.contains("%")) {
			try {
				jsessionid = URLDecoder.decode(jsessionid, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				return null;
			}
		}

		try {
			String result = ThreeDESUtils.Decrypt3DES(kkey, jsessionid);
			JSONObject objcet = JSONObject.parseObject(result);
			jsessionid = objcet.getString("sessionId");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if (StringUtils.isBlank(jsessionid)) {
			return null;
		}
		String userInfo = RedisUtils.get(jsessionid);
		if (StringUtils.isBlank(userInfo)) {
			return null;
		}
		JSONObject object = JSONObject.parseObject(userInfo);
		Users user = new Users();
		user.setUserId(object.getInteger("userId"));
		user.setUsername(object.getString("username"));
		return user;
	}

}
