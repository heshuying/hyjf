package com.hyjf.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.VipInfo;
import com.hyjf.web.coupon.CouponService;
import com.hyjf.web.util.WebUtils;
import com.hyjf.web.vip.apply.ApplyService;

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

	@Autowired
	private ApplyService applyService;
	@Autowired
	private CouponService couponService;

	/**
	 * 在DispatcherServlet完全处理完请求后被调用
	 * 
	 * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
	 */
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {

	}

	/**
	 * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		Integer userId = WebUtils.getUserId(request);
		// 非ajax请求
		if (modelAndView != null) {
			if (userId != null) {
				// 登录用户
				UsersInfo usersInfo = applyService.getUsersInfoByUserId(userId);
				modelAndView.addObject("currentUsersInfo", usersInfo);
				boolean isVip = usersInfo.getVipId() != null ? true : false;
				modelAndView.addObject("isVip", isVip);
				if (isVip) {
					VipInfo vipInfo = applyService.getVipInfo(usersInfo.getVipId());
					modelAndView.addObject("vipName", vipInfo.getVipName());
				}
				
				Integer validCount = couponService.selectCouponValidCount(userId);
				modelAndView.addObject("couponValidCount", validCount);
			}
		}
        // modify by libin 修正之前onlineType取值错误 20190109
		request.setAttribute("onlineType", CustomConstants.HYJF_ONLINE_TYPE);
		// modify by libin 修正之前onlineType取值错误 20190109
		// add by liuyang 20180917 神策数据统计 start
		String sensorsDataUrl = PropUtils.getSystem("sensors.data.url.path");
		// 如果取的为空,默认设置测试环境地址
		if (StringUtils.isBlank(sensorsDataUrl)) {
			sensorsDataUrl = "https://sa.hyjf.com:8106/sa?project=default";
		}
		request.setAttribute("sensorsDataUrl", sensorsDataUrl);
		// add by liuyang 20180917 神策数据统计 end
	}

	/**
	 * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
	 * 
	 * 如果返回true 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链,
	 * 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion()
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		return true;
	}

}
