package com.hyjf.batch.couponinterest;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.GetDate;

@Controller
@RequestMapping(value = "/couponInterest")
public class CouponInterestController {
	private static final Logger LOG = LoggerFactory.getLogger(CouponInterestController.class);
	@Autowired
	private CouponInterestService couponInterestService;

	@ResponseBody
	@RequestMapping(value = "/update", produces = "application/json; charset=utf-8")
	public JSONObject IncreaseInterest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LOG.info("优惠券更新用户的账户信息开始...");
		// 处理开始时间
		String startTime = GetDate.dateToString(new Date());
		System.out.println("处理开始时间:" + startTime);
		JSONObject ret = new JSONObject();
		// 检索未还款的加息还款
		List<Map<String, Object>> dataList = this.couponInterestService.getDataForUpdate();
		
		couponInterestService.updateCouponUserAccount(dataList);
		
		System.out.println("已更新 " + dataList.size() + " 条记录");
		// 处理结束时间
		String endTime = GetDate.dateToString(new Date());
		// 处理用时
		String consumeTime = GetDate.countTime(GetDate.stringToDate(startTime), GetDate.stringToDate(endTime));
		System.out.println("处理结束时间:" + endTime);
		System.out.println("处理用时:" + startTime + "减去" + endTime + "等于" + consumeTime);
		ret.put("status", true);
		ret.put("error", "数据更新完成");
		LOG.info("优惠券更新用户的账户信息结束...");
		return ret;
	}
}
