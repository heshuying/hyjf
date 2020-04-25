package com.hyjf.batch.banktotal;

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
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.Users;

@Controller
@RequestMapping(value = "/bankTotal")
public class BankTotalController {

	private static final Logger LOG = LoggerFactory.getLogger(BankTotalController.class);

	@Autowired
	private BankTotalService bankTotalService;

	@ResponseBody
	@RequestMapping(value = "/update")
	public JSONObject updateBankTotal(HttpServletRequest request, HttpServletResponse response) {
		LOG.info("更新用户账户总资产开始...");
		JSONObject ret = new JSONObject();
		// 检索已开户的用户总数
		Integer userCount = this.bankTotalService.countOpenAccountUserCount();
		// 页码定义
		int pageSize = 1000;
		int stopNum = 90000;
		Integer successCount = 0;
		// 页码总数
		Integer pageTotal = (userCount / pageSize) + 1;
		for (int j = 1; j <= pageTotal; j++) {
			Paginator paginator = new Paginator(j, userCount);
			List<Users> openAccountList = this.bankTotalService.selectOpenAccountUsers(paginator.getOffset(), pageSize);
			if (openAccountList != null && openAccountList.size() > 0) {
				try {
					successCount += this.bankTotalService.updateUserAccountBankTotal(openAccountList);
					if (successCount >= stopNum) {
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		ret.put("status", true);
		ret.put("error", "数据更新完成");
		LOG.info("更新用户账户总资产结束...");
		return ret;
	}
}
