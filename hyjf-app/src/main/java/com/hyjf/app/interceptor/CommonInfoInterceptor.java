package com.hyjf.app.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.hyjf.app.user.invest.InvestService;
import com.hyjf.app.util.SignValue;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetJumpCommand;

/**
 *
 * 网站共通信息收集（用户信息及菜单信息等）可用来升级网站的动态菜单
 *
 * @author zhangjp
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年6月14日
 * @see 上午9:34:23
 */
@Component
public class CommonInfoInterceptor extends HandlerInterceptorAdapter {

	Logger _log = LoggerFactory.getLogger(CommonInfoInterceptor.class);
	
	@Autowired
	private InvestService investService;

	/**
	 * 在DispatcherServlet完全处理完请求后被调用
	 *
	 * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
		String jumpcommend = "";
		// 非ajax请求
		String version = request.getParameter("version");
		String sign = request.getParameter("sign");
		if (StringUtils.isBlank(version)) {
			if (StringUtils.isNotBlank(sign)) {
				String value = RedisUtils.get(sign);
				if(StringUtils.isNotEmpty(value)){
					SignValue signValue = JSON.parseObject(value, SignValue.class);
					version = signValue.getVersion();
				}
			} else {
				String usrCustId = request.getParameter("UsrCustId");
				if (StringUtils.isNotBlank(usrCustId)) {
					Integer userId = investService.getUserIdByUsrcustId(usrCustId);
					if (userId != null) {
						String ver = RedisUtils.get("version_" + userId);
						if (StringUtils.isNotBlank(ver)) {
							version = ver;
						}
					}
				}
			}
		} else {
			if (StringUtils.isNotBlank(sign)) {
				String value = RedisUtils.get(sign);
				if(StringUtils.isNotEmpty(value)){
					SignValue signValue = JSON.parseObject(value, SignValue.class);
					signValue.setVersion(version);
					RedisUtils.set(sign, JSON.toJSONString(signValue), RedisUtils.signExpireTime);
				}
			}
		}
//      System.out.println("version: " + version + " url: " + request.getRequestURL());
		jumpcommend = getLinkJumpPrefix(version);
		request.setAttribute("jumpcommend", jumpcommend);
		request.setAttribute("version", StringUtils.isBlank(version) ? "" : version);

		response.setHeader("jumpcommend", jumpcommend);
		response.setHeader("version", StringUtils.isBlank(version) ? "" : version);
	}

	/**
	 * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		String jumpcommend = "";
		// 非ajax请求
		String version = request.getParameter("version");
		String sign = request.getParameter("sign");
		if (StringUtils.isBlank(version)) {
			if (StringUtils.isNotBlank(sign)) {
				String value = RedisUtils.get(sign);
				if(StringUtils.isNotEmpty(value)){
					SignValue signValue = JSON.parseObject(value, SignValue.class);
					version = signValue.getVersion();
				}
			} else {
				String usrCustId = request.getParameter("UsrCustId");
				if (StringUtils.isNotBlank(usrCustId)) {
					Integer userId = investService.getUserIdByUsrcustId(usrCustId);
					if (userId != null) {
						String ver = RedisUtils.get("version_" + userId);
						if (StringUtils.isNotBlank(ver)) {
							version = ver;
						}
					}
				}
			}
		} else {
			if (StringUtils.isNotBlank(sign)) {
				String value = RedisUtils.get(sign);
				if(StringUtils.isNotEmpty(value)){
					SignValue signValue = JSON.parseObject(value, SignValue.class);
					signValue.setVersion(version);
					RedisUtils.set(sign, JSON.toJSONString(signValue), RedisUtils.signExpireTime);
				}
			}
		}
//		System.out.println("version: " + version + " url: " + request.getRequestURL());
		jumpcommend = getLinkJumpPrefix(version);
		request.setAttribute("jumpcommend", jumpcommend);
		request.setAttribute("version", StringUtils.isBlank(version) ? "" : version);

		response.setHeader("jumpcommend", jumpcommend);
		response.setHeader("version", StringUtils.isBlank(version) ? "" : version);
	}

	/**
	 * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
	 *
	 * 如果返回true 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链,
	 * 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion()
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String jumpcommend = "";
		// 非ajax请求
		String version = request.getParameter("version");
		String sign = request.getParameter("sign");
		if(StringUtils.isEmpty(version)){
			if(StringUtils.isNotEmpty(sign)){
				String value = RedisUtils.get(sign);
				SignValue signValue = JSON.parseObject(value, SignValue.class);
				version = signValue.getVersion();
			}else {
				String usrCustId = request.getParameter("UsrCustId");
				if(StringUtils.isNotEmpty(usrCustId)){
					Integer userId = investService.getUserIdByUsrcustId(usrCustId);
					if(userId != null){
						String ver = RedisUtils.get("version_" + userId);
						if(StringUtils.isNotEmpty(ver)){
							version = ver;
						}
					}
				}
			}
		}else{
			if(StringUtils.isNotEmpty(sign)){
				String value = RedisUtils.get(sign);
				if(StringUtils.isNotEmpty(value)){
					SignValue signValue = JSON.parseObject(value, SignValue.class);
					signValue.setVersion(version);
					RedisUtils.set(sign, JSON.toJSONString(signValue), RedisUtils.signExpireTime);
				}
			}
		}
		_log.info("version: " + version + " url: " + request.getRequestURL());
		jumpcommend = GetJumpCommand.getLinkJumpPrefix(request, version);
		request.setAttribute("jumpcommend", jumpcommend);
		request.setAttribute("version", StringUtils.isEmpty(version)? "" : version);

		response.setHeader("jumpcommend", jumpcommend);
		response.setHeader("version", StringUtils.isBlank(version) ? "" : version);
		_log.info("在业务处理器处理请求之前的拦截器：version: " + version + " url: " + request.getRequestURL());
		return true;
	}

	public String getLinkJumpPrefix(String version) {
		if (StringUtils.isBlank(version)) {
			return "hyjf";
		}

		String pcode = "";
		String vers[] = version.split("\\."); // 取渠道号
		if (vers != null && vers.length > 3) {
			pcode = vers[3];
		}

		if (StringUtils.isBlank(pcode)) {
			return "hyjf";
		}

		if (pcode.equals(CustomConstants.MSG_PUSH_PACKAGE_CODE_39)) {
			return "hyjf";

		} else if (pcode.equals(CustomConstants.MSG_PUSH_PACKAGE_CODE_YXB)) {
			return "hyjfYXB";

		} else if (pcode.equals(CustomConstants.MSG_PUSH_PACKAGE_CODE_ZNB)) {
			return "hyjfZNB";

		} else if (pcode.equals(CustomConstants.MSG_PUSH_PACKAGE_CODE_ZYB)) {
			return "hyjfZYB";

		} else if (pcode.equals(CustomConstants.MSG_PUSH_PACKAGE_CODE_ZZB)) {
			return "hyjfZZB";

		} else if (pcode.equals(CustomConstants.MSG_PUSH_PACKAGE_CODE_TEST)) {
			return "hyjfTEST";

		} else {
			return "hyjf";
		}

	}

}
