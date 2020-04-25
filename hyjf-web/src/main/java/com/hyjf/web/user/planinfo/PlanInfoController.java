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
package com.hyjf.web.user.planinfo;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.bank.service.user.pandect.PandectService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.pdf.PdfGenerator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.PlanInvestCustomize;
import com.hyjf.mybatis.model.customize.PlanLockCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectRepayListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;
import com.hyjf.web.BaseController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.mytender.MyTenderDefine;
import com.hyjf.web.user.mytender.ProjectRepayListBean;
import com.hyjf.web.user.mytender.UserInvestListBean;
import com.hyjf.web.util.WebUtils;

@Controller
@RequestMapping(value = PlanInfoDefine.REQUEST_MAPPING)
public class PlanInfoController extends BaseController {
	@Autowired	
	PandectService pandectService;
	@Autowired
	private PlanInfoService planInfoService;

	/**
	 * 进入我的计划页面
	 * 
	 * @param project
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = PlanInfoDefine.TO_MYPlAN_ACTION)
	public ModelAndView toMyPlanPage(PlanListBean project, HttpServletRequest request,
			HttpServletResponse response) {
		LogUtil.startLog(PlanInfoDefine.THIS_CLASS, PlanInfoDefine.TO_MYPlAN_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanInfoDefine.PROJECT_LIST_PATH);
		WebViewUser wuser = WebUtils.getUser(request);
		if (wuser == null) {
			modelAndView.addObject("message", "用户信息失效，请您重新登录。");
			return new ModelAndView("error/systemerror");
		}
		Integer userId = wuser.getUserId();
		Account account = pandectService.getAccount(userId);
		//TODO account没有放0
		modelAndView.addObject("account", account);
		LogUtil.endLog(PlanInfoDefine.THIS_CLASS, PlanInfoDefine.TO_MYPlAN_ACTION);
		return modelAndView;
	}

	/**
	 * 查询用户出借的汇直投项目和汇消费项目
	 * 
	 * @param project
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = PlanInfoDefine.PLAN_LIST_ACTION, method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public PlanInfoListAjaxBean searchUserProjectList(@ModelAttribute PlanListBean project, HttpServletRequest request,
			HttpServletResponse response) {

		LogUtil.startLog(PlanInfoDefine.THIS_CLASS, PlanInfoDefine.PLAN_LIST_ACTION);
		// 用户ID
		Integer userId = WebUtils.getUserId(request);
		project.setUserId(userId.toString());
		PlanInfoListAjaxBean result = new PlanInfoListAjaxBean();
		if (StringUtils.isBlank(project.getPlanStatus())) {
			project.setPlanStatus("4"); // 4 申购中； 5(包含5锁定中；6清算中；7清算完成，8未还款，9还款中，)锁定中；10已退出
			result.setPlanStatus("4");
		}else{
			result.setPlanStatus(project.getPlanStatus());
		}
		this.createProjectListPage(result, project);
		result.success();
		LogUtil.endLog(PlanInfoDefine.THIS_CLASS, PlanInfoDefine.PLAN_LIST_ACTION);
		return result;
	}

	/**
	 * 创建用户出借列表分页数据
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createProjectListPage(PlanInfoListAjaxBean result, PlanListBean form) {

		Map<String ,Object> params=new HashMap<String ,Object>();
		String status = StringUtils.isNotEmpty(form.getPlanStatus())?form.getPlanStatus():null;
		String userId = StringUtils.isNotEmpty(form.getUserId())?form.getUserId():null;
		params.put("status", status);
		params.put("userId", userId);
		// 统计相应的用户出借项目总数
		Long recordTotal = this.planInfoService.countUserProjectRecordTotal(params);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal.intValue(),form.getPageSize());
			// 查询相应的汇直投列表数据
			int limit = paginator.getLimit();
			int offSet = paginator.getOffset();
			if (offSet == 0 || offSet > 0) {
				params.put("limitStart", offSet);
			}
			if (limit > 0) {
				params.put("limitEnd", limit);
			}
			// 查询相应的用户出借项目列表
			List<PlanLockCustomize> recordList = planInfoService.selectUserProjectList(params);
			result.setPaginator(paginator);
			result.setProjectlist(recordList);
		} else {
			result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
			result.setProjectlist(new ArrayList<PlanLockCustomize>());
		}
	}
	
	/**
	 * 进入我的计划详情页面
	 * 
	 * @param project
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = PlanInfoDefine.PROJECT_DETAIL_ACTION)
	public ModelAndView toMyPlanInfoDetailPage( HttpServletRequest request,
			HttpServletResponse response,PlanListBean form ) {
		LogUtil.startLog(PlanInfoDefine.THIS_CLASS, PlanInfoDefine.PROJECT_DETAIL_ACTION);
	
		ModelAndView modelAndView = new ModelAndView();
	
		WebViewUser wuser = WebUtils.getUser(request);
		if (wuser == null) {
			modelAndView.addObject("message", "用户信息失效，请您重新登录。");
			return new ModelAndView("error/systemerror");
		}
		Integer userId = wuser.getUserId();
		//1基本信息
		Map<String ,Object> params=new HashMap<String ,Object>();
		params.put("accedeOrderId", form.getAccedeOrderId());
		params.put("userId", userId);
		List<PlanLockCustomize> recordList = planInfoService.selectUserProjectListCapital(params);
		if (recordList!=null&&recordList.size()>0) {
			PlanLockCustomize planinfo=recordList.get(0);
			modelAndView.addObject("planinfo", planinfo);
			BigDecimal accedeAccount=new BigDecimal(planinfo.getAccedeAccount());
			BigDecimal lockPeriod=new BigDecimal(planinfo.getDebtLockPeriod());
			BigDecimal expectApr=new BigDecimal(planinfo.getExpectApr()).divide(new BigDecimal("100"));
			BigDecimal repayAccountYes=new BigDecimal(planinfo.getRepayAccountYes());
			//2资产统计
			HashMap<String , Object> map=planInfoService.selectPlanInfoSum( form.getAccedeOrderId());
			BigDecimal investSum=BigDecimal.ZERO;
			if (map!=null) {
				//当前持有资产总计
				investSum=new BigDecimal(map.get("investSum")+"");
				modelAndView.addObject("investSum",investSum);	
			}
			//预计到期收益 加入计划金额*计划期限*计划收益率/12；
			BigDecimal expectIntrest=accedeAccount.multiply(lockPeriod).multiply(expectApr).divide(new BigDecimal("12"),2, BigDecimal.ROUND_DOWN);
			modelAndView.addObject("expectIntrest",expectIntrest);	
			//回款总金额
			modelAndView.addObject("repayAccountYes",repayAccountYes);	
			modelAndView.addObject("factIntrest",planinfo.getRepayInterestYes());
			Map<String, Object> params1 =new HashMap<String, Object>();
			params1.put("planOrderId", form.getAccedeOrderId());
			//params1.put("type", 1);
			//3持有项目列表
			if (form.getType()!=null&&form.getType().equals("1")) {
				//锁定中 
				//TODO  不要分页  查两次 合并
				List<PlanInvestCustomize> debtInvestList= planInfoService.selectInvestCreditList(params1);
				List<PlanInvestCustomize> debtCreditList= planInfoService.selectCreditCreditList(params1);
				List<PlanInvestCustomize> tmpList=new ArrayList<PlanInvestCustomize>();
				if (debtInvestList!=null) {
					tmpList.addAll(debtInvestList);
				}
				if (debtCreditList!=null) {
					tmpList.addAll(debtCreditList);
				}
				modelAndView.addObject("debtInvestList", tmpList);
				modelAndView.setViewName(PlanInfoDefine.PROJECT_DETAIL_LOCK_PATH);
				/*	Long count=planInfoService.countDebtInvestListNew( params1);
				if (count > 0) {
					//债权总额
					Paginator paginator = new Paginator(form.getPaginatorPage(), count.intValue());
					params1.put("limitStart", paginator.getOffset());
					params1.put("limitEnd", paginator.getLimit());
					List<PlanInvestCustomize> debtInvestList= planInfoService.selectPlanInvestListNew(params1);
					form.setPaginator(paginator);
					modelAndView.addObject("debtInvestList", debtInvestList);
				}	*/
			}else if (form.getType()!=null&&form.getType().equals("2")) {
				params1.put("status", "11");
				//已退出
				modelAndView.setViewName(PlanInfoDefine.PROJECT_DETAIL_EXIT_PATH);
				List<PlanInvestCustomize> debtInvestList= planInfoService.selectInvestCreditList(params1);
				List<PlanInvestCustomize> debtCreditList= planInfoService.selectCreditCreditList(params1);
				List<PlanInvestCustomize> tmpList=new ArrayList<PlanInvestCustomize>();
				if (debtInvestList!=null) {
					tmpList.addAll(debtInvestList);
				}
				if (debtCreditList!=null) {
					tmpList.addAll(debtCreditList);
				}
				modelAndView.addObject("debtInvestList", tmpList);
				/*Long count=planInfoService.countDebtInvestListNew( params1);
				if (count > 0) {
					//债权总额
					Paginator paginator = new Paginator(form.getPaginatorPage(), count.intValue());
					params1.put("limitStart", paginator.getOffset());
					params1.put("limitEnd", paginator.getLimit());
					List<PlanInvestCustomize> debtInvestList= planInfoService.selectPlanInvestListNew(params1);
					form.setPaginator(paginator);
					modelAndView.addObject("debtInvestList", debtInvestList);
				}	*/
			}else {
				//申购中
				modelAndView.setViewName(PlanInfoDefine.PROJECT_DETAIL_PATH);
			}
		}
		LogUtil.endLog(PlanInfoDefine.THIS_CLASS, PlanInfoDefine.TO_MYPlAN_ACTION);
		return modelAndView;
	}
	
}
