package com.hyjf.batch.managefee;

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
import com.hyjf.mybatis.model.auto.Users;

@Controller
@RequestMapping(value = "/manageFee")
public class ManageFeeController {
	private static final Logger LOG = LoggerFactory.getLogger(ManageFeeController.class);

	@Autowired
	private ManageFeeService manageFeeService;

	@ResponseBody
	@RequestMapping(value = "/update")
	public JSONObject updateBorrowUserManageFee(HttpServletRequest request, HttpServletResponse response) {
		LOG.info("更新借款人账户信息开始...");
		JSONObject ret = new JSONObject();
		// 检索借款人列表
		List<Users> borrowUserList = manageFeeService.searchBorrowUsersList();
		if (borrowUserList != null && borrowUserList.size() > 0) {
			for (Users users : borrowUserList) {
				try {
					this.manageFeeService.updateBorrowUserAccount(users);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		ret.put("status", true);
		ret.put("error", "数据更新完成");
		LOG.info("更新借款人账户信息结束...");
		return ret;
	}
}
