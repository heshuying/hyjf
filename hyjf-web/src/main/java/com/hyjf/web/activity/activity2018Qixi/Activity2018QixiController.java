/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.web.activity.activity2018Qixi;

import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.web.BaseController;
import com.hyjf.web.bank.web.borrow.BorrowDefine;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @author fq
 * @version Activity2018QixiController, v0.1 2018/7/23 14:13
 */
@RequestMapping(Activity2018QixiDefine.REQUEST_MAPPING)
@Controller
public class Activity2018QixiController extends BaseController {
	private static final int startTime = GetDate.strYYYYMMDDHHMMSS2Timestamp2("2018-08-10 00:00:00");
	private static final int endTime = GetDate.strYYYYMMDDHHMMSS2Timestamp2("2018-08-24 23:59:59");
	@Autowired
	private Activity2018QixiService service;

	/**
	 * 跳转散表列表
	 * @param request
	 * @return
	 */
	@RequestMapping(Activity2018QixiDefine.INVEST_LIST_REQUEST_MAPPING)
	public ModelAndView returnInvestlist(HttpServletRequest request) {
		WebViewUser user = WebUtils.getUser(request);
		if (user != null && user.getUserId() != null) {
			// 更新点击次数
			if (System.currentTimeMillis() >= (long) startTime * 1000
					&& System.currentTimeMillis() <= (long) endTime * 1000) {
				service.updateClickCount(user.getUserId());
			}
			ModelAndView modelAndView = new ModelAndView(BorrowDefine.BORROW_LIST_PTAH);
			modelAndView.addObject("projectType", "HZT");
			modelAndView.addObject("borrowClass", "");
			return modelAndView;
		} else {
			return new ModelAndView("/user/login/login");
		}

	}

	/**
	 * 跳转活动页面
	 */
	@RequestMapping(Activity2018QixiDefine.ACTIVITY_QIXI)
	public ModelAndView returnActivityView(HttpServletRequest request) {
		WebViewUser user = WebUtils.getUser(request);
		ModelAndView modelAndView = new ModelAndView("/activity/active-qixi");

		// 获取用户累计出借总额
		// 查询活动期间内出借总额
		modelAndView.addObject("startTime", (long)startTime * 1000);
		modelAndView.addObject("endTime", (long)endTime * 1000);
		modelAndView.addObject("nowTime", System.currentTimeMillis());
		if (user != null) {
			BigDecimal sum = service.selectInvestSum(user.getUserId(), startTime, endTime);
			modelAndView.addObject("money", sum == null ? "0.00" : CommonUtils.formatAmount(sum).toString());
			// 已登录
			modelAndView.addObject("isLogin", 0);
			// 查找获奖类型
			Integer awardType = service.selectAwartType(user.getUserId());
			if (awardType != null) {
				modelAndView.addObject("awartType", awardType);
			}
		} else {
			// 未登录
			modelAndView.addObject("isLogin", 1);
			modelAndView.addObject("awartType", 0);
		}

		return modelAndView;
	}
}
