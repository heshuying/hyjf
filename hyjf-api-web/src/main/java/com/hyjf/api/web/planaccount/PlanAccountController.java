/**
 * Description:我的出借控制器
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.api.web.planaccount;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatPlanAccountCustomize;

@Controller
@RequestMapping(value = PlanAccountDefine.REQUEST_MAPPING)
public class PlanAccountController extends BaseController {
	@Autowired
	private PlanAccountService planAccountService;

	/**
	 * 查询用户出借的汇添金
	 * 
	 * @param project
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = PlanAccountDefine.PLAN_LIST_ACTION, method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public JSONObject searchUserProjectList(@ModelAttribute PlanAccountBean form, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(PlanAccountDefine.THIS_CLASS, PlanAccountDefine.PLAN_LIST_ACTION);
		JSONObject info = new JSONObject();
		// 用户ID
		Integer userId = form.getUserId();
		// 计划状态
		String planStatus = form.getPlanStatus();

		if (!this.checkSign(form, BaseDefine.METHOD_SEARCH_USER_PROJECT_LIST)) {
			info.put("error", BaseResultBean.STATUS_FAIL);
			info.put("", "验签失败！");
			LogUtil.errorLog(this.getClass().getName(), "searchUserProjectList", "验签失败！", null);
			return info;
		}
		if (userId == null || userId == 0) {
			info.put("error", "1");
			info.put("errorDesc", "用户ID不可为空");
			return info;
		}
		if (StringUtils.isEmpty(planStatus)) {
			info.put("error", "1");
			info.put("errorDesc", "计划状态不能为空");
			return info;
		}
		// 分页信息
		Integer page = form.getPage();
		Integer pageSize = form.getPageSize();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		param.put("planStatus", planStatus);
		int totalSize = 0;
		try {
			param.put("limitStart", pageSize * (page - 1));
			param.put("limitEnd", pageSize);
			// 查询相应的用户出借项目列表
			totalSize = this.planAccountService.countUserProjectRecordTotal(param);
			List<WeChatPlanAccountCustomize> recordList = planAccountService.selectUserProjectList(param);
			info.put("totalSize", totalSize);
			info.put("recordList", recordList);
		} catch (Exception e) {
			info.put("error", "1");
			info.put("errorDesc", "获取我的汇添金列表失败");
		}
		LogUtil.endLog(PlanAccountDefine.THIS_CLASS, PlanAccountDefine.PLAN_LIST_ACTION);
		return info;
	}

}
