package com.hyjf.batch.increaseinterest;

import java.util.Date;
import java.util.List;

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
import com.hyjf.mybatis.model.auto.IncreaseInterestLoan;

@Controller
@RequestMapping(value = "/increaseInterest")
public class IncreaseInterestController {
	private static final Logger LOG = LoggerFactory.getLogger(IncreaseInterestController.class);
	@Autowired
	private IncreaseInterestService increaseInterestService;

	@ResponseBody
	@RequestMapping(value = "/update", produces = "application/json; charset=utf-8")
	public JSONObject IncreaseInterest(HttpServletRequest request, HttpServletResponse response) {
		LOG.info("加息还款部分更新用户的账户信息开始...");
		JSONObject ret = new JSONObject();
		String startTime = GetDate.dateToString(new Date());
		System.out.println("处理开始时间:" + startTime);
		// 检索未还款的加息还款
		List<IncreaseInterestLoan> increaseInterestList = this.increaseInterestService.searchIncreaseInterestLoanList();
		
		this.increaseInterestService.updateTenderUserAccount(increaseInterestList);
		
		// 处理结束时间
		String endTime = GetDate.dateToString(new Date());
		// 处理用时
		String consumeTime = GetDate.countTime(GetDate.stringToDate(startTime), GetDate.stringToDate(endTime));
		System.out.println("处理结束时间:" + endTime);
		System.out.println("处理用时:" + startTime + "减去" + endTime + "等于" + consumeTime);
		ret.put("status", true);
		ret.put("error", "数据更新完成");
		LOG.info("加息还款部分更新用户的账户信息结束...");
		return ret;
	}
}
