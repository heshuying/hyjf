package com.hyjf.wechat.annotation;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.wechat.base.BaseMapBean;
import com.hyjf.wechat.util.RequestUtil;

@Component
public class ViewValidateInteceptor extends HandlerInterceptorAdapter {
	Logger _log = LoggerFactory.getLogger(ViewValidateInteceptor.class);
	
	@Autowired
    public RequestUtil requestUtil;

	public ViewValidateInteceptor() {
	}

	
	@Override
	public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView modelAndView)
            throws Exception {
		// 当前登录用户id
 		Integer userId = requestUtil.getRequestUserId(request);
		String from = RedisUtils.get("loginFrom"+userId);
        // 你自己的业务逻辑
		StringBuffer url = request.getRequestURL();  
		String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
		_log.info("用户userId："+userId+"请求来源tempContextUrl:"+tempContextUrl);
		//判断是否为风车理财的出借
		if (modelAndView != null) {
			Map<String, Object> map = modelAndView.getModelMap();
			Object obj = map.get("callBackForm");
			BaseMapBean baseMapBean = null;
			if(obj instanceof BaseMapBean){
				baseMapBean = (BaseMapBean)map.get("callBackForm");
			}
			if(baseMapBean != null && StringUtils.isNotBlank(baseMapBean.getCallBackAction())){
				String action = baseMapBean.getCallBackAction();
				String arg[] = action.split("hyjf.com");
				if (StringUtils.isNotBlank(from)) {
		            // 输出登录来源
	        		_log.info("用户userId："+userId+"，loginFrom:"+from);
					if ("2".equals(from)) {
						//风车理财的请求，对ModelAndView中跳转页面的域名变更
						String host = CustomConstants.WECHAT_HOST;
//						System.out.println("action:"+action);
						baseMapBean.setCallBackAction(host+arg[1]);
						_log.info("用户userId："+userId+"，host:"+host+",arg[1]："+arg[1]);
						_log.info("用户userId："+userId+"，loginFrom:"+from+",替换后的url："+baseMapBean.getCallBackAction());
						modelAndView.addObject(baseMapBean);
						_log.info("modelAndView:"+modelAndView);
					}
				}
			}
			
		}
		
        super.postHandle(request, response, handler, modelAndView);
    }
}
