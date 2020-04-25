package com.hyjf.app.user.repaycalendar;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseDefine;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.util.CustomConstants;

/**
 * @author xiasq
 * @version RepayCalendarController, v0.1 2018/1/30 16:23
 */

@RestController
@RequestMapping(value = RepayCalendarDefine.REQUEST_MAPPING)
public class RepayCalendarController {
	private Logger logger = LoggerFactory.getLogger(RepayCalendarController.class);

	@Autowired
	private RepayCalendarService repayCalendarService;

	@RequestMapping(value = RepayCalendarDefine.REPAY_CALENDAR_ACTION)
	public JSONObject getRepayCalendar(@RequestParam(required = false) String year,
			@RequestParam(required = false) String month, HttpServletRequest request) {
		logger.info("getRepayCalendar start, year is :{}, month is :{}", year, month);
		JSONObject info = new JSONObject();
		info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
		info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);

		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		logger.info("page is :{}, pageSize is :{}", page, pageSize);
		if (StringUtils.isBlank(page) || StringUtils.isBlank(pageSize)) {
			info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
			info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_FAIL);
			return info;
		}

		// 唯一标识
		String sign = request.getParameter("sign");
		logger.info("sign is :{}", sign);
		if (StringUtils.isBlank(sign)) {
			info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
			info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_FAIL);
			return info;
		}
		// 用户id
		Integer userId = null;
		try{
			userId = SecretUtil.getUserId(sign);
		}catch (Exception e){
			logger.error("解析sign错误...", e);
			info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
			info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_FAIL);
			return info;
		}

		//userId = Integer.parseInt(request.getParameter("userId"));
		logger.info("userId is :{}", userId);

		// 构造查询参数列表
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		if(StringUtils.isNotBlank(year)){
			params.put("tradeYear", Integer.parseInt(year));
		}
		if(StringUtils.isNotBlank(month)){
			params.put("tradeMonth", Integer.parseInt(month));
		}

		this.buildPageCondition(page, pageSize, params);
		this.createRepayCalendar(info, params, year, month);

		info.put(CustomConstants.APP_REQUEST, BaseDefine.REQUEST_HOME + RepayCalendarDefine.REQUEST_MAPPING
				+ RepayCalendarDefine.REPAY_CALENDAR_ACTION);
		return info;

	}

	/***
	 * 创建分页数据
	 * 
	 * @param info
	 * @param params
	 */
	private void createRepayCalendar(JSONObject info, Map<String, Object> params, String year, String month) {

		int recordTotal = repayCalendarService.countRepaymentCalendar(params);
		if (recordTotal > 0) {

			List<ReapyCalendarResult> repayPlanDetail = repayCalendarService.searchRepaymentCalendar(params);

			info.put("beginTime", repayCalendarService.searchNearlyRepaymentTime(params));

			info.put("repayPlanDetail", repayPlanDetail);
			info.put("repayPlanTotal", String.valueOf(recordTotal));
		} else {
			logger.info("未查询到数据,返回月份标题....");
			info.put("repayPlanDetail", buildNoneRepaymentReturn(year, month));
			info.put("repayPlanTotal", "0");
		}
	}

	/**
	 * 构建分页参数
	 * 
	 * @param page
	 * @param pageSize
	 * @param params
	 * @return
	 */
	private Map<String, Object> buildPageCondition(String page, String pageSize, Map<String, Object> params) {

		int limit = Integer.parseInt(pageSize);

		int offSet = (Integer.parseInt(page) - 1) * limit;
		if (offSet == 0 || offSet > 0) {
			params.put("limitStart", offSet);
		}
		if (limit > 0) {
			params.put("limitEnd", limit);
		}

		return params;
	}

	/**
	 * 没有交易明细的返回json
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private List<ReapyCalendarResult> buildNoneRepaymentReturn(String year, String month) {
		List<ReapyCalendarResult> list = new ArrayList<>();
		ReapyCalendarResult customize = new ReapyCalendarResult();
		Calendar cal = Calendar.getInstance();
		int newYear = cal.get(Calendar.YEAR);
		int newMonth = cal.get(Calendar.MONTH) + 1;

		// 没有年月的过滤条件，默认当前
		int paramYear = newYear;
		int paramMonth = newMonth;
		if (StringUtils.isNotBlank(year)) {
			paramYear = Integer.parseInt(year);
		}
		if (StringUtils.isNotBlank(month)) {
			paramMonth = Integer.parseInt(month);
		}

		if (paramYear != newYear) {
			customize.setMonth(year + "年" + month + "月");
		} else {
			if (paramMonth != newMonth) {
				customize.setMonth(month + "月");
			} else {
				customize.setMonth("本月");
			}
		}
		customize.setIsMonth("0");
		list.add(customize);
		return list;
	}
}
