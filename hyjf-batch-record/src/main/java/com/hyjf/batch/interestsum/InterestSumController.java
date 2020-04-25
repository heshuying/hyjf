package com.hyjf.batch.interestsum;

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
import com.hyjf.mybatis.model.auto.Users;

@Controller
@RequestMapping(value = "/interestSum")
public class InterestSumController {

	private static final Logger LOG = LoggerFactory.getLogger(InterestSumController.class);
	
	private static boolean readFlg = false;

	@Autowired
	private InterestSumService interestSumService;

	/**
	 * 更新用户的累计收益
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/update")
	public JSONObject updateBankInterestSum(HttpServletRequest request, HttpServletResponse response) {
		if(readFlg){
			LOG.info("重复请求，不作处理。");
			return null;
		}
		readFlg = true;
		LOG.info("更新出借人账户累计累计收益开始...");
		JSONObject ret = new JSONObject();
		String startTime = GetDate.dateToString(new Date());
		LOG.info("处理开始...");
		// 检索已开户总数
		//Integer userTotal = this.interestSumService.countOpenAccountUsers();
		/*// 页码定义
		int pageSize = 1000;
		//int stopNum = 90000;
		// 更新成功的
		Integer successCount = 0;
		// 页码总数
		Integer pageTotal = (userTotal / pageSize) + 1;
		for (int j = 1; j <= pageTotal; j++) {
			Paginator paginator = new Paginator(j, userTotal,pageSize);
			List<Users> openAccountList = this.interestSumService.selectOpenAccountUsers(paginator.getOffset(), pageSize);
			if (openAccountList != null && openAccountList.size() > 0) {
				try {
					successCount += this.interestSumService.updateInterestSum(openAccountList);
				} catch (Exception e) {
					e.printStackTrace();
					LOG.info("第"+j+"页数据更新失败");
				}
			}
			if (successCount >= stopNum) {
				break;
			}
		}*/
		List<Users> openAccountList = this.interestSumService.selectOpenAccountUsers(123, 123);
		if (openAccountList != null && openAccountList.size() > 0) {
			try {
				this.interestSumService.updateInterestSum(openAccountList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 处理结束时间
		String endTime = GetDate.dateToString(new Date());
		// 处理用时
		String consumeTime = GetDate.countTime(GetDate.stringToDate(startTime), GetDate.stringToDate(endTime));
		System.out.println("处理结束时间:" + endTime);
		System.out.println("处理用时:" + startTime + "减去" + endTime + "等于" + consumeTime);
		ret.put("status", true);
		ret.put("error", "数据更新完成");
		LOG.info("更新出借人账户累计累计收益结束...");
		return ret;
	}
}
