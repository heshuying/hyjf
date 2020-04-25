/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.app.activity.activity2018qixi;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @author fq
 * @version Activity2018QixiController, v0.1 2018/7/23 14:28
 */
@RestController
@RequestMapping(Activity2018QixiDefine.REQUEST_MAPPING)
public class Activity2018QixiController extends BaseController {
    private static final int startTime = GetDate.strYYYYMMDDHHMMSS2Timestamp2("2018-08-10 00:00:00");
    private static final int endTime = GetDate.strYYYYMMDDHHMMSS2Timestamp2("2018-08-24 23:59:59");
	@Autowired
	private Activity2018QixiService service;

	/**
	 * 跳转散标列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(Activity2018QixiDefine.INVEST_LIST_REQUEST_MAPPING)
	public JSONObject returnInvestlist(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
        // 获取登陆用户userId
        String sign = request.getParameter("sign");

        if (Validator.isNull(sign)) {
            jsonObject.put("status", "999");
            jsonObject.put("statusDesc", "用户未登录，请先登录！");
            return jsonObject;
        }
		Integer userId = null; // 用户ID
		try {
			userId = SecretUtil.getUserId(sign);
		} catch (Exception e) {
			jsonObject.put("status", "999");
			jsonObject.put("statusDesc", "未登录");
			return jsonObject;
		}
		// 获取用户id
		if (userId != null) {
			try {
                if (userId != null && System.currentTimeMillis() >= (long) startTime * 1000
                        && System.currentTimeMillis() <= (long) endTime * 1000) {
					service.updateClickCount(userId);
				}
				jsonObject.put("status", "000");
				jsonObject.put("statusDesc", "成功");
				return jsonObject;
			} catch (Exception e) {
				logger.error("更新七夕活动点击数失败......", e);
				jsonObject.put("status", "99");
				jsonObject.put("statusDesc", "失败");
				return jsonObject;
			}
		} else {
			jsonObject.put("status", "999");
			jsonObject.put("statusDesc", "未登录");
			return jsonObject;
		}

	}

	/**
	 * 查询活动期间内出借总额
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(Activity2018QixiDefine.INVEST_SUM)
	public JSONObject selectInvestSum(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		// 获取登陆用户userId
		String sign = request.getParameter("sign");

		if (Validator.isNull(sign)) {
			// 未登录
			jsonObject.put("isLogin", 1);
		}
		Integer userId = null; // 用户ID
		try {
			userId = SecretUtil.getUserId(sign);
		} catch (Exception e) {
			// 未登录
			jsonObject.put("isLogin", 1);
		}
		if (userId != null) {
			try {
				JSONObject params = new JSONObject();
				BigDecimal sum = service.selectInvestSum(userId, startTime, endTime);
				// 累计出借金额
				params.put("totalMoney", sum == null ? 0 : sum.setScale(0, BigDecimal.ROUND_DOWN));
				params.put("startTime", (long) startTime * 1000);
				params.put("endTime", (long) endTime * 1000);
				params.put("nowTime", System.currentTimeMillis());
				// 查找获奖类型
				Integer awardType = service.selectAwartType(userId);
				params.put("awardType", awardType == null ? 0 : awardType);
				jsonObject.put("status", "000");
				jsonObject.put("statusDesc", "成功");
				jsonObject.put("data", params);
				jsonObject.put("isLogin", 0);
				return jsonObject;
			} catch (Exception e) {
				logger.error("查询活动期间内出借总额失败......", e);
				jsonObject.put("status", "99");
				jsonObject.put("statusDesc", "失败");
				jsonObject.put("data", null);
				jsonObject.put("isLogin", 0);
				return jsonObject;
			}
		} else {
			JSONObject params = new JSONObject();
			params.put("totalMoney", 0);
			params.put("startTime", (long) startTime * 1000);
			params.put("endTime", (long) endTime * 1000);
			params.put("nowTime", System.currentTimeMillis());
			params.put("awardType", 0);
			jsonObject.put("status", "000");
			jsonObject.put("statusDesc", "成功");
			jsonObject.put("data", params);
			jsonObject.put("isLogin", 1);
			return jsonObject;
		}
	}
}
