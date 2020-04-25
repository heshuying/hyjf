/**
 * Description:获取指定的项目类型的项目列表
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.plan.borrow;

import com.hyjf.common.calculate.*;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.mybatis.model.customize.web.*;
import com.hyjf.web.BaseController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.coupon.CouponService;
import com.hyjf.web.home.HomePageDefine;
import com.hyjf.web.util.WebUtils;
import com.hyjf.web.vip.apply.ApplyDefine;
import com.hyjf.web.vip.manage.VIPManageDefine;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

@Controller
@RequestMapping(value = PlanBorrowDefine.REQUEST_MAPPING)
public class PlanBorrowController extends BaseController {

	@Autowired
	private PlanBorrowService projectService;
	@Autowired
	private CouponService couponService;
	/** 发布地址 */
	private static String HOST_URL = PropUtils.getSystem("hyjf.web.host").trim();

	/**
	 * 初始化项目列表画面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = PlanBorrowDefine.INIT_PROJECT_LIST_ACTION)
	public ModelAndView initHztList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView(PlanBorrowDefine.PROJECT_LIST_PTAH);
		String projectType = request.getParameter("projectType");
		modelAndView.addObject("projectType", projectType);
		return modelAndView;
	}

	/**
	 * 获取指定类型的项目的列表
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = PlanBorrowDefine.PROJECT_LIST_ACTION, produces = "application/json; charset=utf-8")
	public PlanBorrowListAJaxBean searchProjectList(@ModelAttribute PlanBorrowBean form, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(PlanBorrowDefine.THIS_CLASS, PlanBorrowDefine.PROJECT_LIST_ACTION);
		PlanBorrowListAJaxBean result = new PlanBorrowListAJaxBean();
		this.createProjectListPage(result, form);
		result.success();
		result.setHost(HomePageDefine.HOST);
		LogUtil.endLog(PlanBorrowDefine.THIS_CLASS, PlanBorrowDefine.PROJECT_LIST_ACTION);
		return result;
	}

	/**
	 * 查询相应的项目分页列表
	 * 
	 * @param info
	 * @param form
	 */
	private void createProjectListPage(PlanBorrowListAJaxBean result, PlanBorrowBean form) {

		Map<String, Object> params = new HashMap<String, Object>();
		if (form.getProjectType().equals("XSH")) {
			params.put("projectType", "HZT");
			params.put("type", "4");
		} else if (form.getProjectType().equals("ZXH")) {
			params.put("projectType", "HZT");
			params.put("type", "11");
		} else {
			params.put("projectType", form.getProjectType());
		}
		// 统计相应的汇直投的数据记录数
		int projecTotal = this.projectService.countProjectListRecordTotal(params);
		if (projecTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), projecTotal, form.getPageSize());
			// 查询相应的汇直投列表数据
			int limit = paginator.getLimit();
			int offSet = paginator.getOffset();
			if (offSet == 0 || offSet > 0) {
				params.put("limitStart", offSet);
			}
			if (limit > 0) {
				params.put("limitEnd", limit);
			}
			List<WebProjectListCustomize> projectList = projectService.searchProjectList(params);
			result.setProjectList(projectList);
			result.setPaginator(paginator);
			int nowTime = GetDate.getNowTime10();
			result.setNowTime(nowTime);
		} else {
			result.setProjectList(new ArrayList<WebProjectListCustomize>());
			result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
		}
	}

	/**
	 * 查询相应的项目详情
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = PlanBorrowDefine.PROJECT_DETAIL_ACTION)
	public ModelAndView searchProjectDetail(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(PlanBorrowDefine.THIS_CLASS, PlanBorrowDefine.PROJECT_DETAIL_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanBorrowDefine.HZT_PROJECT_DETAIL_PTAH);
		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		// 1.获取项目标号
		String borrowNid = request.getParameter("borrowNid");
		// 1.获取项目标号
		String couponId = request.getParameter("couponGrantId");
		// 2.根据项目标号获取相应的项目信息
		WebProjectDetailCustomize borrow = this.projectService.selectProjectDetail(borrowNid);
		if (borrow == null) {
			modelAndView = new ModelAndView(PlanBorrowDefine.ERROR_PTAH);
			return modelAndView;
		} else {
			// 预约相关
			// 预约等待金额
			if (borrow.getBorrowAccountWaitAppoint() != null) {
				modelAndView.addObject("borrowAccountWaitAppoint", new BigDecimal(borrow.getBorrowAccountWaitAppoint()).intValue());
			}
			// 预约时间还剩多长
			if (borrow.getBookingEndTime() != null) {
				int endMins = borrow.getBookingEndTime() - GetDate.getNowTime10();
				modelAndView.addObject("endMins", endMins < 0 ? 0 : endMins * 1000);
			}
			WebViewUser loginUser = WebUtils.getUser(request);

			/** 计算最优优惠券开始 pccvip isThereCoupon 1是有最优优惠券，0无最有优惠券 */
			UserCouponConfigCustomize couponConfig = null;
			couponConfig = new UserCouponConfigCustomize();
			if (couponId == null || "".equals(couponId) || couponId.length() == 0) {
				// 获取用户最优优惠券
				couponConfig = getBestCoupon(borrowNid, loginUser.getUserId(), null, "0");
			} else {
				couponConfig = getBestCouponById(couponId);
			}
			BigDecimal couponInterest = BigDecimal.ZERO;
			if (couponConfig != null) {
				modelAndView.addObject("isThereCoupon", 1);
				BigDecimal borrowApr = new BigDecimal(borrow.getBorrowApr());
				String borrowStyle = borrow.getBorrowStyle();
				couponInterest = getInterest(borrowStyle, couponConfig.getCouponType(), borrowApr, couponConfig.getCouponQuota(), "0", new Integer(borrow.getBorrowPeriod() == null ? "0" : borrow.getBorrowPeriod()));
				couponConfig.setCouponInterest(df.format(couponInterest));
			} else {
				modelAndView.addObject("isThereCoupon", 0);
			}

			modelAndView.addObject("couponConfig", couponConfig);
			/** 计算最优优惠券结束 */
			if (borrow.getInvestAccount() != null) {
				modelAndView.addObject("InvestAccountInt", new BigDecimal(borrow.getInvestAccount()).intValue());
			}
			String nowTime = String.valueOf(GetDate.getNowTime10());
			modelAndView.addObject("nowTime", nowTime);
			String borrowStyle = borrow.getBorrowStyle();
			BigDecimal borrowInterest = new BigDecimal(0);

			/** 叠加收益率开始 pccvip */
			// 收益率
			BigDecimal borrowApr = new BigDecimal(borrow.getBorrowApr());
			/** 可用优惠券张数开始 pccvip */
			String couponAvailableCount = couponService.getUserCouponAvailableCount(borrowNid, loginUser.getUserId(), "0", "0");
			modelAndView.addObject("couponAvailableCount", couponAvailableCount);
			/** 可用优惠券张数结束 pccvip */

			/** 获取用户是否是vip 开始 pccvip 1是vip 0不是vip */
			UsersInfo usersInfo = projectService.getUsersInfoByUserId(loginUser.getUserId());
			if (usersInfo.getVipId() != null && usersInfo.getVipId() != 0) {
				modelAndView.addObject("ifVip", 1);
				String returl = HOST_URL + VIPManageDefine.REQUEST_MAPPING + "/" + VIPManageDefine.INIT_ACTION + ".do";
				modelAndView.addObject("returl", returl);
			} else {
				modelAndView.addObject("ifVip", 0);
				String returl = HOST_URL + ApplyDefine.REQUEST_MAPPING + ApplyDefine.INIT + ".do";
				modelAndView.addObject("returl", returl);

			}
			/** 获取用户优惠券总张数开始 pccvip */
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("usedFlag", 0);
			paraMap.put("userId", loginUser.getUserId());
			Integer recordTotal = couponService.countCouponUsers(paraMap);
			modelAndView.addObject("recordTotal", recordTotal);
			switch (borrowStyle) {
			case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“
				// 计算历史回报
				borrowInterest = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod())).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“
				borrowInterest = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod())).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：；
				borrowInterest = BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod()), Integer.parseInt(borrow.getBorrowPeriod())).divide(new BigDecimal("1"), 2,
						BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：；
				borrowInterest = AverageCapitalPlusInterestUtils.getInterestCount(new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod())).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
				borrowInterest = AverageCapitalUtils.getInterestCount(new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod())).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			default:
				break;
			}

			if (couponConfig != null && couponConfig.getCouponType() == 3) {
				modelAndView.addObject("capitalInterest", df.format(borrowInterest.add(couponConfig.getCouponQuota()).subtract(couponInterest)));
			} else {
				modelAndView.addObject("capitalInterest", df.format(borrowInterest.subtract(couponInterest)));
			}

			borrow.setBorrowInterest(borrowInterest.toString());
			if (borrow.getType().equals("9")) {// 如果项目为汇资产项目
				// 添加相应的项目详情信息
				modelAndView.addObject("projectDeatil", borrow);
				// 4查询相应的汇资产的首页信息
				WebHzcProjectDetailCustomize borrowInfo = this.projectService.searchHzcProjectDetail(borrowNid);
				modelAndView.addObject("borrowInfo", borrowInfo);
				// 处置预案
				WebHzcDisposalPlanCustomize disposalPlan = this.projectService.searchDisposalPlan(borrowNid);
				modelAndView.addObject("disposalPlan", disposalPlan);
				// 5查询相应的还款计划
				List<PlanBorrowRepayPlanBean> repayPlanList = this.projectService.getRepayPlan(borrowNid);
				modelAndView.addObject("repayPlanList", repayPlanList);
				// 相关文件
				List<PlanBorrowFileBean> files = this.projectService.searchProjectFiles(borrowNid, HOST_URL);
				modelAndView.addObject("fileList", files);
			} else {// 项目为非汇资产项目
				// 添加相应的项目详情信息
				modelAndView.addObject("projectDeatil", borrow);
				// 4查询非汇资产项目的项目信息
				if (StringUtils.isNotEmpty(borrow.getComOrPer()) && borrow.getComOrPer().equals("1")) {
					// 查询相应的企业项目详情
					WebProjectCompanyDetailCustomize borrowInfo = projectService.searchProjectCompanyDetail(borrowNid);
					modelAndView.addObject("borrowInfo", borrowInfo);
				} else if (StringUtils.isNotEmpty(borrow.getComOrPer()) && borrow.getComOrPer().equals("2")) {
					// 查询相应的汇直投个人项目详情
					WebProjectPersonDetailCustomize borrowInfo = projectService.searchProjectPersonDetail(borrowNid);
					modelAndView.addObject("borrowInfo", borrowInfo);
				}
				// 风控信息
				WebRiskControlCustomize riskControl = this.projectService.selectRiskControl(borrowNid);
				riskControl.setControlMeasures(riskControl.getControlMeasures()==null?"":riskControl.getControlMeasures().replace("\r\n", ""));
				riskControl.setControlMort(riskControl.getControlMort()==null?"":riskControl.getControlMort().replace("\r\n", ""));
				// 添加风控信息
				modelAndView.addObject("riskControl", riskControl);
				List<WebMortgageCustomize> mortgageList = this.projectService.selectMortgageList(borrowNid);
				// 添加相应的房产信息
				modelAndView.addObject("mortgageList", mortgageList);
				List<WebVehiclePledgeCustomize> vehiclePledgeList = this.projectService.selectVehiclePledgeList(borrowNid);
				// 添加相应的汽车抵押信息
				modelAndView.addObject("vehiclePledgeList", vehiclePledgeList);
				// 5查询相应的认证信息
				List<WebProjectAuthenInfoCustomize> authenList = projectService.searchProjectAuthenInfos(borrowNid);
				modelAndView.addObject("authenList", authenList);
				// 6查询相应的还款计划
				List<PlanBorrowRepayPlanBean> repayPlanList = this.projectService.getRepayPlan(borrowNid);
				modelAndView.addObject("repayPlanList", repayPlanList);
				// 7 相关文件
				List<PlanBorrowFileBean> files = this.projectService.searchProjectFiles(borrowNid, HOST_URL);
				modelAndView.addObject("fileList", files);

			}
			String loginFlag = "1";
			String investFlag = "0";
			Integer userId = loginUser.getUserId();
			// 用户是否出借项目
			int count = this.projectService.countUserInvest(loginUser.getUserId(), borrowNid);
			if (count > 0) {
				investFlag = "1";
			}
			if (loginUser.isOpenAccount()) {
				modelAndView.addObject("openFlag", 1);
			} else {
				modelAndView.addObject("openFlag", 0);
			}
			modelAndView.addObject("investFlag", investFlag);
			Account account = this.projectService.getAccount(userId);
			String userBalance = account.getBalance().toString();
			modelAndView.addObject("loginFlag", loginFlag);
			modelAndView.addObject("userBalance", userBalance);

			LogUtil.endLog(PlanBorrowDefine.THIS_CLASS, PlanBorrowDefine.PROJECT_DETAIL_ACTION);
			return modelAndView;
		}

	}

	private BigDecimal getInterest(String borrowStyle, Integer couponType, BigDecimal borrowApr, BigDecimal couponQuota, String money, Integer borrowPeriod) {
		BigDecimal earnings = new BigDecimal("0");

		// 出借金额
		BigDecimal accountDecimal = null;
		if (couponType == 1) {
			// 体验金 出借资金=体验金面值
			accountDecimal = couponQuota;
		} else if (couponType == 2) {
			// 加息券 出借资金=真实出借资金
			accountDecimal = new BigDecimal(money);
			borrowApr = couponQuota;
		} else if (couponType == 3) {
			// 代金券 出借资金=体验金面值
			accountDecimal = couponQuota;
		}
		switch (borrowStyle) {
		case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
			// 计算历史回报
			earnings = DuePrincipalAndInterestUtils.getMonthInterest(accountDecimal, borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷360*天数；
			// 计算历史回报
			earnings = DuePrincipalAndInterestUtils.getDayInterest(accountDecimal, borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：历史回报=出借金额*年化收益÷12*月数；
			// 计算历史回报
			earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(accountDecimal, borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：历史回报=出借金额*年化收益÷12*月数；
			// 计算历史回报
			earnings = AverageCapitalPlusInterestUtils.getInterestCount(accountDecimal, borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
			break;
		default:
			break;
		}
		if (couponType == 3) {
			earnings = earnings.add(couponQuota);
		}
		return earnings;
	}

	/**
	 * 项目详细信息预览
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = PlanBorrowDefine.PROJECT_PREVIEW_ACTION)
	public ModelAndView getProjectPreview(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(PlanBorrowDefine.THIS_CLASS, PlanBorrowDefine.PROJECT_PREVIEW_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanBorrowDefine.HZT_PROJECT_PREVIEW_PTAH);
		// 1.获取项目标号
		String borrowNid = request.getParameter("borrowNid");
		// 2.根据项目标号获取相应的项目信息
		WebProjectDetailCustomize borrow = this.projectService.selectProjectPreview(borrowNid);
		if (borrow == null) {
			modelAndView = new ModelAndView(PlanBorrowDefine.ERROR_PTAH);
			return modelAndView;
		} else {
			String nowTime = String.valueOf(GetDate.getNowTime10());
			modelAndView.addObject("nowTime", nowTime);
			String borrowStyle = borrow.getBorrowStyle();
			BigDecimal borrowInterest = new BigDecimal(0);
			switch (borrowStyle) {
			case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“
				// 计算历史回报
				borrowInterest = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(borrow.getBorrowAccount()), new BigDecimal(borrow.getBorrowApr()).divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod())).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“
				borrowInterest = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(borrow.getBorrowAccount()), new BigDecimal(borrow.getBorrowApr()).divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod())).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：；
				borrowInterest = BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(borrow.getBorrowAccount()), new BigDecimal(borrow.getBorrowApr()).divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod()), Integer.parseInt(borrow.getBorrowPeriod()))
						.divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：；
				borrowInterest = AverageCapitalPlusInterestUtils.getInterestCount(new BigDecimal(borrow.getBorrowAccount()), new BigDecimal(borrow.getBorrowApr()).divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod())).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
				borrowInterest = AverageCapitalUtils.getInterestCount(new BigDecimal(borrow.getBorrowAccount()), new BigDecimal(borrow.getBorrowApr()).divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod())).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			default:
				break;
			}
			borrow.setBorrowInterest(borrowInterest.toString());

			// 添加相应的项目详情信息
			modelAndView.addObject("projectDeatil", borrow);
			// 风控信息
			WebRiskControlCustomize riskControl = this.projectService.selectRiskControl(borrowNid);
			riskControl.setControlMeasures(riskControl.getControlMeasures()==null?"":riskControl.getControlMeasures().replace("\r\n", ""));
			riskControl.setControlMort(riskControl.getControlMort()==null?"":riskControl.getControlMort().replace("\r\n", ""));
			// 添加风控信息
			modelAndView.addObject("riskControl", riskControl);
			List<WebMortgageCustomize> mortgageList = this.projectService.selectMortgageList(borrowNid);
			// 添加相应的房产信息
			modelAndView.addObject("mortgageList", mortgageList);
			List<WebVehiclePledgeCustomize> vehiclePledgeList = this.projectService.selectVehiclePledgeList(borrowNid);
			// 添加相应的汽车抵押信息
			modelAndView.addObject("vehiclePledgeList", vehiclePledgeList);
			// 5查询相应的认证信息
			List<WebProjectAuthenInfoCustomize> authenList = projectService.searchProjectAuthenInfos(borrowNid);
			modelAndView.addObject("authenList", authenList);
			// 7 相关文件
			List<PlanBorrowFileBean> files = this.projectService.searchProjectFiles(borrowNid, HOST_URL);
			modelAndView.addObject("fileList", files);

			// 处置预案
			WebHzcDisposalPlanCustomize disposalPlan = this.projectService.searchDisposalPlan(borrowNid);
			modelAndView.addObject("disposalPlan", disposalPlan);
			if (borrow.getType().equals("9")) {// 如果项目为汇资产项目
				// 4查询相应的汇资产的首页信息
				WebHzcProjectDetailCustomize borrowInfo = this.projectService.searchHzcProjectDetail(borrowNid);
				modelAndView.addObject("borrowInfo", borrowInfo);
			} else {// 项目为非汇资产项目
				// 4查询非汇资产项目的项目信息
				if (StringUtils.isNotEmpty(borrow.getComOrPer()) && borrow.getComOrPer().equals("1")) {
					// 查询相应的企业项目详情
					WebProjectCompanyDetailCustomize borrowInfo = projectService.searchProjectCompanyDetail(borrowNid);
					modelAndView.addObject("borrowInfo", borrowInfo);
				} else if (StringUtils.isNotEmpty(borrow.getComOrPer()) && borrow.getComOrPer().equals("2")) {
					// 查询相应的汇直投个人项目详情
					WebProjectPersonDetailCustomize borrowInfo = projectService.searchProjectPersonDetail(borrowNid);
					modelAndView.addObject("borrowInfo", borrowInfo);
				}
			}
			LogUtil.endLog(PlanBorrowDefine.THIS_CLASS, PlanBorrowDefine.PROJECT_PREVIEW_ACTION);
			return modelAndView;
		}

	}

	/***
	 * 查询相应的汇消费项目的打包数据
	 * 
	 * @param hztInvest
	 * @param request
	 * @param response
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = PlanBorrowDefine.PROJECT_CONSUME_ACTION, produces = "application/json; charset=utf-8")
	public PlanBorrowConsumeListAjaxBean searchProjectConsumeList(@ModelAttribute PlanBorrowConsumeBean form, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(PlanBorrowDefine.THIS_CLASS, PlanBorrowDefine.PROJECT_CONSUME_ACTION);
		PlanBorrowConsumeListAjaxBean result = new PlanBorrowConsumeListAjaxBean();
		this.createProjectConsumePage(result, form);
		result.success();
		result.setHost(HomePageDefine.HOST);
		LogUtil.endLog(PlanBorrowDefine.THIS_CLASS, PlanBorrowDefine.PROJECT_CONSUME_ACTION);
		return result;
	}

	/**
	 * 创建相应的汇消费项目的打包信息
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */

	private void createProjectConsumePage(PlanBorrowConsumeListAjaxBean result, PlanBorrowConsumeBean form) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", form.getBorrowNid());
		int recordTotal = this.projectService.countProjectConsumeRecordTotal(params);
		if (recordTotal > 0) {
			// 查询相应的汇直投列表数据
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
			// 查询相应的汇直投列表数据
			int limit = paginator.getLimit();
			int offSet = paginator.getOffset();
			;
			if (offSet == 0 || offSet > 0) {
				params.put("limitStart", offSet);
			}
			if (limit > 0) {
				params.put("limitEnd", limit);
			}
			List<WebProjectConsumeCustomize> recordList = projectService.searchProjectConsumeList(params);
			result.setProjectConsumeList(recordList);
			result.setPaginator(paginator);
		} else {
			// 添加相应的汇消费债券信息
			result.setProjectConsumeList(new ArrayList<WebProjectConsumeCustomize>());
			result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
		}
	}

	/**
	 * 查询相应的项目的出借列表
	 * 
	 * @param hztInvest
	 * @param request
	 * @param response
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = PlanBorrowDefine.PROJECT_INVEST_ACTION, produces = "application/json; charset=utf-8")
	public PlanBorrowInvestListAjaxBean searchProjectInvestList(@ModelAttribute PlanBorrowInvestBean form, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(PlanBorrowDefine.THIS_CLASS, PlanBorrowDefine.PROJECT_INVEST_ACTION);
		PlanBorrowInvestListAjaxBean result = new PlanBorrowInvestListAjaxBean();
		this.createProjectInvestPage(result, form);

		result.success();
		result.setHost(HomePageDefine.HOST);
		LogUtil.endLog(PlanBorrowDefine.THIS_CLASS, PlanBorrowDefine.PROJECT_INVEST_ACTION);
		return result;
	}

	/**
	 * 创建用户出借记录分页查询
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createProjectInvestPage(PlanBorrowInvestListAjaxBean result, PlanBorrowInvestBean form) {

		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		Borrow borrow = this.projectService.selectBorrowByNid(form.getBorrowNid());
		if (borrow != null) {
			result.setInvestTotal(df.format(borrow.getBorrowAccountYes()));
			result.setInvestTimes(String.valueOf(borrow.getTenderTimes()));
		} else {
			result.setInvestTotal(df.format(new BigDecimal("0")));
			result.setInvestTimes("0");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", form.getBorrowNid());
		int recordTotal = this.projectService.countProjectInvestRecordTotal(params);
		if (recordTotal > 0) {
			// 查询相应的汇直投列表数据
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
			int limit = paginator.getLimit();
			int offSet = paginator.getOffset();
			if (offSet == 0 || offSet > 0) {
				params.put("limitStart", offSet);
			}
			if (limit > 0) {
				params.put("limitEnd", limit);
			}
			List<WebProjectInvestListCustomize> recordList = projectService.searchProjectInvestList(params);
			result.setProjectInvestList(recordList);
			result.setPaginator(paginator);
		} else {
			result.setProjectInvestList(new ArrayList<WebProjectInvestListCustomize>());
			result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
		}
	}

	/**
	 * 定时发标的状态查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = PlanBorrowDefine.PROJECT_STATUS_ACTION)
	public PlanBorrowStatusAjaxBean searchProjectStatus(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(PlanBorrowDefine.THIS_CLASS, PlanBorrowDefine.PROJECT_STATUS_ACTION);
		// 1.获取项目标号
		String borrowNid = request.getParameter("borrowNid");
		// 2.根据项目标号获取相应的项目信息
		WebProjectDetailCustomize borrow = this.projectService.selectProjectDetail(borrowNid);
		PlanBorrowStatusAjaxBean projectStatus = new PlanBorrowStatusAjaxBean();
		if (borrow == null) {
			projectStatus.setStatus(false);
			projectStatus.setMessage("标不存在");
		} else {
			projectStatus.setProjectStatus(borrow.getStatus());
			projectStatus.setStatus(true);
			projectStatus.setMessage("标的状态查询成功");
		}
		LogUtil.endLog(PlanBorrowDefine.THIS_CLASS, PlanBorrowDefine.PROJECT_STATUS_ACTION);
		return projectStatus;

	}

	private UserCouponConfigCustomize getBestCoupon(String borrowNid, Integer userId, String money, String platform) {
		/* Integer userId = SecretUtil.getUserId(sign); */
		// Integer userId = 20000125;
		if (money == null || "".equals(money) || money.length() == 0) {
			money = "0";
		}
		return projectService.getBestCoupon(borrowNid, userId, money, platform);
	}

	private UserCouponConfigCustomize getBestCouponById(String couponId) {
		return projectService.getBestCouponById(couponId);
	}

	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		System.out.println(new Date().getTime());
		System.out.println(GetDate.getNowTime10());
	}
}
