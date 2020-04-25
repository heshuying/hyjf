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
package com.hyjf.web.user.project;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.customize.web.WebProjectRepayListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserProjectListCustomize;
import com.hyjf.web.BaseController;
import com.hyjf.web.shiro.ShiroUtil;

@Controller("userStatisticsController")
@RequestMapping(value = InvestProjectDefine.REQUEST_MAPPING)
public class InvestProjectController extends BaseController {

	@Autowired
	private InvestProjectService userStatisticsService;

	/**
	 * 查询用户出借的汇直投项目和汇消费项目
	 * 
	 * @param project
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = InvestProjectDefine.PROJECT_LIST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String searchUserProjectList(@ModelAttribute ProjectListBean project, HttpServletRequest request,
			HttpServletResponse response) {

		LogUtil.startLog(InvestProjectDefine.THIS_CLASS, InvestProjectDefine.PROJECT_LIST_ACTION);
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		// 用户ID
		Integer userId = ShiroUtil.getLoginUserId(request);
		project.setUserId(userId.toString());
		this.createProjectListPage(request, info, project);
		ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
		ret.put(CustomConstants.DATA, info);
		ret.put(CustomConstants.MSG, "");
		LogUtil.endLog(InvestProjectDefine.THIS_CLASS, InvestProjectDefine.PROJECT_LIST_ACTION);
		return JSONObject.toJSONString(ret, true);
	}

	/**
	 * 创建用户出借列表分页数据
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createProjectListPage(HttpServletRequest request, JSONObject info, ProjectListBean form) {

		// 统计相应的用户出借项目总数
		int recordTotal = this.userStatisticsService.countUserProjectRecordTotal(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			// 查询相应的用户出借项目列表
			List<WebUserProjectListCustomize> recordList = userStatisticsService.selectUserProjectList(form,
					paginator.getOffset(), paginator.getLimit());
			info.put("paginator", paginator);
			info.put("userProjectlist", recordList);
		} else {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			info.put("paginator", paginator);
			info.put("userProjectlist", "");
		}
	}

	/**
	 * 查询相应的用户出借的用户列表（用户协议）
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = InvestProjectDefine.PROJECT_USER_INVEST_LIST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String searchUserInvestList(@ModelAttribute UserInvestListBean form, HttpServletRequest request,
			HttpServletResponse response) {

		LogUtil.startLog(InvestProjectDefine.THIS_CLASS, InvestProjectDefine.PROJECT_USER_INVEST_LIST_ACTION);
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		// 用户ID
		Integer userId = ShiroUtil.getLoginUserId(request);
		form.setUserId(userId.toString());
		createUserInvestPage(request, info, form);
		ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
		ret.put(CustomConstants.DATA, info);
		ret.put(CustomConstants.MSG, "");
		LogUtil.endLog(InvestProjectDefine.THIS_CLASS, InvestProjectDefine.PROJECT_USER_INVEST_LIST_ACTION);
		return JSONObject.toJSONString(ret, true);
	}

	/**
	 * 创建用户出借分页信息
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createUserInvestPage(HttpServletRequest request, JSONObject info, UserInvestListBean form) {

		int recordTotal = this.userStatisticsService.countUserInvestRecordTotal(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			List<WebUserInvestListCustomize> recordList = userStatisticsService.selectUserInvestList(form,
					paginator.getOffset(), paginator.getLimit());
			info.put("paginator", paginator);
			info.put("userinvestlist", recordList);
		} else {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			info.put("paginator", paginator);
			info.put("userinvestlist", "");
		}
	}

	/**
	 * 分期项目查看相应的还款信息
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = InvestProjectDefine.PROJECT_REPAY_LIST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String searchProjectRepayList(@ModelAttribute ProjectRepayListBean form, HttpServletRequest request,
			HttpServletResponse response) {

		LogUtil.startLog(InvestProjectDefine.THIS_CLASS, InvestProjectDefine.PROJECT_REPAY_LIST_ACTION);
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		// 用户ID
		Integer userId = ShiroUtil.getLoginUserId(request);
		form.setUserId(userId.toString());
		createProjectRepayPage(request, info, form);
		ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
		ret.put(CustomConstants.DATA, info);
		ret.put(CustomConstants.MSG, "");
		LogUtil.endLog(InvestProjectDefine.THIS_CLASS, InvestProjectDefine.PROJECT_REPAY_LIST_ACTION);
		return JSONObject.toJSONString(ret, true);
	}

	/**
	 * 创建相应的还款信息分页
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createProjectRepayPage(HttpServletRequest request, JSONObject info, ProjectRepayListBean form) {

		int recordTotal = this.userStatisticsService.countProjectRepayRecordTotal(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			List<WebProjectRepayListCustomize> recordList = userStatisticsService.selectProjectRepayList(form,
					paginator.getOffset(), paginator.getLimit());
			info.put("paginator", paginator);
			info.put("userrepaylist", recordList);
		} else {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			info.put("paginator", paginator);
			info.put("userrepaylist", "");
		}
	}
}
