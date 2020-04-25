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
package com.hyjf.web.user.mytender;

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

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.pandect.AssetBean;
import com.hyjf.bank.service.user.pandect.PandectService;
import com.hyjf.common.calculate.AverageCapitalPlusInterestUtils;
import com.hyjf.common.calculate.AverageCapitalUtils;
import com.hyjf.common.calculate.BeforeInterestAfterPrincipalUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import com.hyjf.mybatis.model.customize.web.WebPandectBorrowRecoverCustomize;
import com.hyjf.mybatis.model.customize.web.WebPandectCreditTenderCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectRepayListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserProjectListCustomize;
import com.hyjf.web.BaseController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.project.ProjectService;
import com.hyjf.web.user.planinfo.PlanInfoService;
import com.hyjf.web.util.WebUtils;

@Controller
@RequestMapping(value = MyTenderDefine.REQUEST_MAPPING)
public class MyTenderController extends BaseController {
	@Autowired
	PandectService pandectService;
	@Autowired
	private MyTenderService mytenderService;
	@Autowired
	private PlanInfoService planInfoService;
	@Autowired
	private ProjectService projectService;

	/**
	 * 进入‘我的出借’页面
	 * 
	 * @param project
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = MyTenderDefine.TO_MYTENDER_ACTION)
	public ModelAndView toMytenderPage(ProjectListBean project, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(MyTenderDefine.THIS_CLASS, MyTenderDefine.PROJECT_LIST_ACTION);
		ModelAndView modelAndView = new ModelAndView(MyTenderDefine.PROJECT_LIST_PATH);
		WebViewUser wuser = WebUtils.getUser(request);
		if (wuser == null) {
			modelAndView.addObject("message", "用户信息失效，请您重新登录。");
			return new ModelAndView("error/systemerror");
		}
		Integer userId = wuser.getUserId();
		Account account = pandectService.getAccount(userId);
		modelAndView.addObject("account", account);
		// 债转统计
		WebPandectCreditTenderCustomize creditTender = pandectService.queryCreditInfo(userId);
		if (creditTender == null) {
			creditTender = new WebPandectCreditTenderCustomize();
		}
		// 去掉已债转
		WebPandectBorrowRecoverCustomize recoverYesInfo = pandectService.queryRecoverInfo(userId, 1);
		if (recoverYesInfo == null) {
			recoverYesInfo = new WebPandectBorrowRecoverCustomize();
		}
		// 去掉待收已债转
		WebPandectBorrowRecoverCustomize recoverWaitInfo = pandectService.queryRecoverInfo(userId, 0);
		if (recoverWaitInfo == null) {
			recoverWaitInfo = new WebPandectBorrowRecoverCustomize();
		}
		// 总资产
		AssetBean asset = pandectService.queryAsset(userId);
		// 总出借额 (累计出借)= 总出借额+ 支付金额 -已债转总利息（含垫付）-已债转金额 -待收已债转 -待收已债转利息
		/*
		 * BigDecimal investTotal =
		 * asset.getInvestTotal().add(creditTender.getCreditAssign())
		 * .subtract(recoverYesInfo
		 * .getCreditInterestAmount()).subtract(recoverYesInfo
		 * .getCreditAmount())
		 * .subtract(recoverWaitInfo.getCreditAmount()).subtract
		 * (recoverWaitInfo.getCreditInterestAmount());
		 */
		BigDecimal investTotal = asset.getInvestTotal().add(creditTender.getCreditCapital())
				.add(account.getPlanAccedeTotal());
		modelAndView.addObject("investTotal", investTotal);

		// 汇天利总收益
		BigDecimal interestall = pandectService.queryHtlSumInterest(userId);
		if (interestall == null) {
			interestall = new BigDecimal(0);
		}

		// 优惠券总收益 add by hesy 优惠券相关 start
		BigDecimal couponInterestTotalDec = BigDecimal.ZERO;
		String couponInterestTotal = mytenderService.queryCouponInterestTotal(userId);
		LogUtil.infoLog(this.getClass().getName(), "getMyAsset", "优惠券已得收益：" + couponInterestTotal);
		if (org.apache.commons.lang3.StringUtils.isNotEmpty(couponInterestTotal)) {
			couponInterestTotalDec = new BigDecimal(couponInterestTotal);
		}
		// add by hesy 优惠券相关 end
		BigDecimal planInterestYes = BigDecimal.ZERO;
		if (account.getPlanRepayInterest() != null) {
			planInterestYes = account.getPlanRepayInterest();
		}
		// 已回收的利息 (累计收益)
		BigDecimal recoverInterest = asset.getRecoverInterest().add(interestall)
				.add(creditTender.getCreditInterestYes()).subtract(recoverYesInfo.getCreditInterestAmount())
				.add(couponInterestTotalDec).add(planInterestYes);
		modelAndView.addObject("recoverInterest", recoverInterest);

		LogUtil.endLog(MyTenderDefine.THIS_CLASS, MyTenderDefine.PROJECT_LIST_ACTION);
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
	@RequestMapping(value = MyTenderDefine.PROJECT_LIST_ACTION, method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public ProjectListAjaxBean searchUserProjectList(@ModelAttribute ProjectListBean project,
			HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(MyTenderDefine.THIS_CLASS, MyTenderDefine.PROJECT_LIST_ACTION);
		// 用户ID
		Integer userId = WebUtils.getUserId(request);
		project.setUserId(userId.toString());
		ProjectListAjaxBean result = new ProjectListAjaxBean();
		if (StringUtils.isBlank(project.getProjectStatus())) {
			project.setProjectStatus("13"); // 12 冻结中； 13还款中；14已还款
			result.setProjectStatus("13");
		} else {
			result.setProjectStatus(project.getProjectStatus());
		}
		this.createProjectListPage(result, project);
		result.success();
		LogUtil.endLog(MyTenderDefine.THIS_CLASS, MyTenderDefine.PROJECT_LIST_ACTION);
		return result;
	}

	public static void main(String[] args) {
		String text = "12345678901234";
		if (text.length() > 13) {
			text = text.substring(0, 12) + "…";
		}
		System.out.println("…".length());
		System.out.println(text);
	}

	/**
	 * 创建用户出借列表分页数据
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createProjectListPage(ProjectListAjaxBean result, ProjectListBean form) {

		Map<String, Object> params = new HashMap<String, Object>();
		String status = StringUtils.isNotEmpty(form.getProjectStatus()) ? form.getProjectStatus() : null;
		String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
		params.put("status", status);
		params.put("userId", userId);
		params.put("startDate", form.getStartDate());
		params.put("endDate", form.getEndDate());
		// 统计相应的用户出借项目总数
		int recordTotal = this.mytenderService.countUserProjectRecordTotal(params);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
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
			List<WebUserProjectListCustomize> recordList = mytenderService.selectUserProjectList(params);
			result.setPaginator(paginator);
			result.setProjectlist(recordList);
		} else {
			result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
			result.setProjectlist(new ArrayList<WebUserProjectListCustomize>());
		}
	}

	/**
	 * 查询相应的用户出借的用户列表（查看协议）
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = MyTenderDefine.PROJECT_USER_INVEST_LIST_ACTION, method = RequestMethod.GET)
	public ModelAndView searchUserInvestList(@ModelAttribute UserInvestListBean form, HttpServletRequest request,
			HttpServletResponse response) {

		LogUtil.startLog(MyTenderDefine.THIS_CLASS, MyTenderDefine.PROJECT_USER_INVEST_LIST_ACTION);
		ModelAndView modelAndView = new ModelAndView(MyTenderDefine.XY_PATH);
		if (form.getBorrowNid() == null || "".equals(form.getBorrowNid().trim())) {
			modelAndView = new ModelAndView("error/systemerror");
			modelAndView.addObject("message", "标的信息不存在，请重新查证。");
			return modelAndView;
		}
		// 用户ID
		Integer userId = WebUtils.getUserId(request);

		// 融通宝展示不同协议
		if (form.getProjectType().equals("13")) {
			WebProjectDetailCustomize borrow = this.projectService.selectProjectDetail(form.getBorrowNid());
			if (borrow != null && borrow.getBorrowPublisher() != null && borrow.getBorrowPublisher().equals("中商储")) {
				modelAndView = new ModelAndView(MyTenderDefine.RTB_XY_PATH_ZSC);
			} else {
				modelAndView = new ModelAndView(MyTenderDefine.RTB_XY_PATH);
			}
			UsersInfo userinfo = mytenderService.getUsersInfoByUserId(userId);
			form.setUserId(userId + "");
			List<WebUserInvestListCustomize> invest = mytenderService.selectUserInvestList(form, 0, 10);
			if (invest != null && invest.size() > 0) {
				modelAndView.addObject("investDeatil", invest.get(0));
			}
			modelAndView.addObject("requestBean", form);
			modelAndView.addObject("projectDeatil", borrow);
			modelAndView.addObject("truename", userinfo.getTruename());
			modelAndView.addObject("idcard", userinfo.getIdcard());
			modelAndView.addObject("borrowNid", form.getBorrowNid());// 标的号
			modelAndView.addObject("assetNumber", form.getAssetNumber());// 资产编号
			modelAndView.addObject("projectType", form.getProjectType());// 项目类型
			return modelAndView;
		}

		// 查询借款人用户名
		BorrowCommonCustomize borrowCommonCustomize = new BorrowCommonCustomize();
		// 借款编码
		borrowCommonCustomize.setBorrowNidSrch(form.getBorrowNid());
		List<BorrowCustomize> recordList = mytenderService.selectBorrowList(borrowCommonCustomize);
		if (recordList.size() != 1) {
			modelAndView = new ModelAndView("error/systemerror");
			modelAndView.addObject("message", "标的信息异常，请重新查证。");
			return modelAndView;
		}
		modelAndView.addObject("borrowNid", form.getBorrowNid());// 标的号
		modelAndView.addObject("nid", form.getNid());// 出借标示
		modelAndView.addObject("jkrUsername", recordList.get(0).getUsername());// 借款人用户名
		modelAndView.addObject("recoverLastDay", recordList.get(0).getRecoverLastDay());// 最后一笔的放款完成时间
																						// (协议签订日期)

		// 如果是分期还款，查询分期信息
		String borrowStyle = recordList.get(0).getBorrowStyle();// 还款模式
		if (borrowStyle != null) {
			if ("month".equals(borrowStyle) || "principal".equals(borrowStyle) || "endmonth".equals(borrowStyle)) {
				ProjectRepayListBean bean = new ProjectRepayListBean();
				bean.setUserId(WebUtils.getUserId(request).toString());
				bean.setBorrowNid(form.getBorrowNid());
				bean.setNid(form.getNid());
				int recordTotal = this.mytenderService.countProjectRepayPlanRecordTotal(bean);
				if (recordTotal > 0) {
					Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
					List<WebProjectRepayListCustomize> fqList = mytenderService.selectProjectRepayPlanList(bean,
							paginator.getOffset(), paginator.getLimit());
					modelAndView.addObject("paginator", paginator);
					modelAndView.addObject("fqList", fqList);
				} else {
					Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
					modelAndView.addObject("paginator", paginator);
					modelAndView.addObject("fqList", "");
				}
			}
		}
		form.setUserId(userId.toString());
		createUserInvestPage(request, modelAndView, form);
		modelAndView.addObject("requestBean", form);

		modelAndView.addObject("phpHost", PropUtils.getSystem("hyjf.web.host.php"));// php路径，用于用php接口导出pdf
		LogUtil.endLog(MyTenderDefine.THIS_CLASS, MyTenderDefine.PROJECT_USER_INVEST_LIST_ACTION);
		return modelAndView;
	}

	/**
	 * 创建用户出借分页信息
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createUserInvestPage(HttpServletRequest request, ModelAndView modelAndView, UserInvestListBean form) {

		int recordTotal = this.mytenderService.countUserInvestRecordTotal(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			List<WebUserInvestListCustomize> recordList = mytenderService.selectUserInvestList(form,
					paginator.getOffset(), paginator.getLimit());
			modelAndView.addObject("paginator", paginator);
			modelAndView.addObject("userinvestlist", recordList);
		} else {
			int recordTotal1 = this.planInfoService.countUserInvestRecordTotal(form);
			if (recordTotal1 > 0) {
				Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal1);
				List<WebUserInvestListCustomize> recordList = planInfoService.selectUserInvestList(form,
						paginator.getOffset(), paginator.getLimit());
				modelAndView.addObject("paginator", paginator);
				modelAndView.addObject("userinvestlist", recordList);
			} else {
				Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal1);
				modelAndView.addObject("paginator", paginator);
				modelAndView.addObject("userinvestlist", "");
			}

		}
	}

	/**
	 * 分期项目查看相应的还款信息 （还款计划）
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = MyTenderDefine.PROJECT_REPAY_LIST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String searchProjectRepayList(@ModelAttribute ProjectRepayListBean form, HttpServletRequest request,
			HttpServletResponse response) {

		LogUtil.startLog(MyTenderDefine.THIS_CLASS, MyTenderDefine.PROJECT_REPAY_LIST_ACTION);
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		// 用户ID
		Integer userId = WebUtils.getUserId(request);
		form.setUserId(userId.toString());
		createProjectRepayPage(request, info, form);
		ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
		ret.put(CustomConstants.DATA, info);
		ret.put(CustomConstants.MSG, "");
		LogUtil.endLog(MyTenderDefine.THIS_CLASS, MyTenderDefine.PROJECT_REPAY_LIST_ACTION);
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

		int recordTotal = this.mytenderService.countProjectRepayPlanRecordTotal(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			List<WebProjectRepayListCustomize> recordList = mytenderService.selectProjectRepayPlanList(form,
					paginator.getOffset(), paginator.getLimit());
			info.put("paginator", paginator);
			info.put("userrepaylist", recordList);
		} else {

			int recordTotal1 = this.planInfoService.countProjectRepayPlanRecordTotal(form);
			if (recordTotal1 > 0) {
				Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal1);
				List<WebProjectRepayListCustomize> recordList = planInfoService.selectProjectRepayPlanList(form,
						paginator.getOffset(), paginator.getLimit());
				info.put("paginator", paginator);
				info.put("userrepaylist", recordList);
			} else {
				Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal1);
				info.put("paginator", paginator);
				info.put("userrepaylist", "");
			}

		}
	}

	/**
	 * 分期项目查看相应的还款信息 （优惠券还款计划）
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = MyTenderDefine.PROJECT_COUPON_REPAY_LIST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String searchCouponProjectRepayList(@ModelAttribute ProjectRepayListBean form, HttpServletRequest request,
			HttpServletResponse response) {

		LogUtil.startLog(MyTenderDefine.THIS_CLASS, MyTenderDefine.PROJECT_COUPON_REPAY_LIST_ACTION);
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		// 用户ID
		Integer userId = WebUtils.getUserId(request);
		form.setUserId(userId.toString());
		createCouponProjectRepayPage(request, info, form);
		ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
		ret.put(CustomConstants.DATA, info);
		ret.put(CustomConstants.MSG, "");
		LogUtil.endLog(MyTenderDefine.THIS_CLASS, MyTenderDefine.PROJECT_REPAY_LIST_ACTION);
		return JSONObject.toJSONString(ret, true);
	}

	/**
	 * 获取融通宝加息收益（临时）
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = MyTenderDefine.PROJECT_RTB_EXTRA_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String getRTBExtraEarn(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(MyTenderDefine.THIS_CLASS, MyTenderDefine.PROJECT_RTB_EXTRA_ACTION);
		JSONObject ret = new JSONObject();
		// 项目编号
		String borrowNid = request.getParameter("borrowNid");
		// 待收金额
		String accountWait = request.getParameter("accountWait");
		// 出借金额
		String accountMoney = request.getParameter("accountMoney");

		// 项目编号为空
		if (StringUtils.isEmpty(borrowNid)) {
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
			ret.put(CustomConstants.DATA, accountWait);
			return JSONObject.toJSONString(ret, true);
		}
		// 出借金额为空
		if (StringUtils.isEmpty(accountMoney)) {
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
			ret.put(CustomConstants.DATA, accountWait);
			return JSONObject.toJSONString(ret, true);
		}
		Borrow borrow = mytenderService.getBorrowByNid(borrowNid);
		if (borrow == null) {
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
			ret.put(CustomConstants.DATA, accountWait);
			return JSONObject.toJSONString(ret, true);
		}
		// 不是融通宝项目
		if (borrow.getProjectType() != 13) {
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
			ret.put(CustomConstants.DATA, accountWait);
			return JSONObject.toJSONString(ret, true);
		}
		// 额外加息率为0
		if (borrow.getBorrowExtraYield().compareTo(BigDecimal.ZERO) == 0) {
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
			ret.put(CustomConstants.DATA, accountWait);
			return JSONObject.toJSONString(ret, true);
		}
		// 借款类型
		String borrowStyle = borrow.getBorrowStyle();
		// 额外利率
		BigDecimal borrowApr = borrow.getBorrowExtraYield();
		// 出借金额
		BigDecimal money = new BigDecimal(accountMoney);
		// 周期
		Integer borrowPeriod = borrow.getBorrowPeriod();
		BigDecimal earnings = new BigDecimal("0");
		switch (borrowStyle) {
		case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
			// 计算历史回报
			earnings = DuePrincipalAndInterestUtils.getMonthInterest(money, borrowApr.divide(new BigDecimal("100")),
					borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷360*天数；
			earnings = DuePrincipalAndInterestUtils.getDayInterest(money, borrowApr.divide(new BigDecimal("100")),
					borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：历史回报=出借金额*年化收益÷12*月数；
			earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(money,
					borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2,
					BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：历史回报=出借金额*年化收益÷12*月数；
			earnings = AverageCapitalPlusInterestUtils.getInterestCount(money, borrowApr.divide(new BigDecimal("100")),
					borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
			earnings = AverageCapitalUtils.getInterestCount(money, borrowApr.divide(new BigDecimal("100")),
					borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
			break;
		default:
			break;
		}
		ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
		ret.put(CustomConstants.DATA, new BigDecimal(accountWait).add(earnings));

		LogUtil.endLog(MyTenderDefine.THIS_CLASS, MyTenderDefine.PROJECT_RTB_EXTRA_ACTION);
		return JSONObject.toJSONString(ret, true);
	}

	/**
	 * 创建相应的还款信息分页
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createCouponProjectRepayPage(HttpServletRequest request, JSONObject info, ProjectRepayListBean form) {

		int recordTotal = this.mytenderService.countCouponProjectRepayPlanRecordTotal(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			List<WebProjectRepayListCustomize> recordList = mytenderService.selectCouponProjectRepayPlanList(form,
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
