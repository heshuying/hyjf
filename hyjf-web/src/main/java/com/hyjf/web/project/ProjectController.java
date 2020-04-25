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
package com.hyjf.web.project;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.common.calculate.AverageCapitalPlusInterestUtils;
import com.hyjf.common.calculate.AverageCapitalUtils;
import com.hyjf.common.calculate.BeforeInterestAfterPrincipalUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.DebtBorrow;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.mybatis.model.customize.web.WebHzcDisposalPlanCustomize;
import com.hyjf.mybatis.model.customize.web.WebHzcProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebMortgageCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectAuthenInfoCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectCompanyDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectConsumeCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectInvestListCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectListCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectPersonDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebRiskControlCustomize;
import com.hyjf.mybatis.model.customize.web.WebVehiclePledgeCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowAuthenInfoCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowCompanyDetailCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowDetailCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowHzcDetailCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowHzcDisposalPlanCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowMortgageCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowPersonDetailCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowRiskControlCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowVehiclePledgeCustomize;
import com.hyjf.web.BaseController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.coupon.CouponService;
import com.hyjf.web.home.HomePageDefine;
import com.hyjf.web.util.WebUtils;
import com.hyjf.web.vip.apply.ApplyDefine;
import com.hyjf.web.vip.manage.VIPManageDefine;

@Controller
@RequestMapping(value = ProjectDefine.REQUEST_MAPPING)
public class ProjectController extends BaseController {

	@Autowired
	private ProjectService projectService;
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
	@RequestMapping(value = ProjectDefine.INIT_PROJECT_LIST_ACTION)
	public ModelAndView initHztList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView(ProjectDefine.PROJECT_LIST_PTAH);
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
	@RequestMapping(value = ProjectDefine.PROJECT_LIST_ACTION, produces = "application/json; charset=utf-8")
	public ProjectListAJaxBean searchProjectList(@ModelAttribute ProjectBean form, HttpServletRequest request,
			HttpServletResponse response) {

		LogUtil.startLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_LIST_ACTION);
		ProjectListAJaxBean result = new ProjectListAJaxBean();
		this.createProjectListPage(result, form);
		result.success();
		result.setHost(HomePageDefine.HOST);
		LogUtil.endLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_LIST_ACTION);
		return result;
	}

	/**
	 * 查询相应的项目分页列表
	 * 
	 * @param info
	 * @param form
	 */
	private void createProjectListPage(ProjectListAJaxBean result, ProjectBean form) {

		Map<String, Object> params = new HashMap<String, Object>();
		if (form.getProjectType().equals("XSH")) {
			params.put("projectType", "HZT");
			params.put("type", "4");
		} else if (form.getProjectType().equals("ZXH")) {
			params.put("projectType", "HZT");
			params.put("type", "11");
		} else if (form.getProjectType().equals("RTB")) {
			params.put("projectType", "HZT");
			params.put("type", "13");
		} else {
			params.put("projectType", form.getProjectType());
		}
		// 统计相应的汇直投的数据记录数
		int projecTotal = this.projectService.countProjectListRecordTotal(params);
		// 李深强修改 by明举 所有产品列表只查2页之内的数据
		int pageNum = 2;
		if(projecTotal > form.getPageSize() * pageNum){
			projecTotal = form.getPageSize() * pageNum;
		}
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
	
	public static void main(String[] args) {
		System.out.println(11/10);
	}

	/**
	 * 查询相应的项目详情
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = ProjectDefine.PROJECT_DETAIL_ACTION)
	public ModelAndView searchProjectDetail(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_DETAIL_ACTION);
		ModelAndView modelAndView = new ModelAndView(ProjectDefine.HZT_PROJECT_DETAIL_PTAH);
		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		// 1.获取项目标号
		String borrowNid = request.getParameter("borrowNid");
		// System.out.println("~~~~~~~~~~出借页面加载获得标编号~~~~~~~~~~~~~borrowNid:"+borrowNid);
		// 1.获取项目标号
		String couponId = request.getParameter("couponGrantId");
		// 2.根据项目标号获取相应的项目信息
		WebProjectDetailCustomize borrow = this.projectService.selectProjectDetail(borrowNid);
		if (borrow == null) {
			modelAndView = new ModelAndView(ProjectDefine.ERROR_PTAH);
			return modelAndView;
		} else {
			// 预约相关
			// 预约等待金额
			if (borrow.getBorrowAccountWaitAppoint() != null) {
				modelAndView.addObject("borrowAccountWaitAppoint",
						new BigDecimal(borrow.getBorrowAccountWaitAppoint()).intValue());
			}
			// 预约时间还剩多长
			if (borrow.getBookingEndTime() != null) {
				int endMins = borrow.getBookingEndTime() - GetDate.getNowTime10();
				modelAndView.addObject("endMins", endMins < 0 ? 0 : endMins * 1000);
			}
			/*
			 * //预约时间是否开始 if (borrow.getBookingBeginTime()!=null) { int
			 * isBegin=GetDate.getNowTime10()-borrow.getBookingBeginTime();
			 * modelAndView.addObject("isBegin",isBegin>=0?0:1); } //预约时间是否结束 if
			 * (borrow.getBookingEndTime()!=null) { int
			 * isEnd=GetDate.getNowTime10()-borrow.getBookingEndTime();
			 * modelAndView.addObject("isEnd",isEnd>0?1:0); }
			 */
			WebViewUser loginUser = WebUtils.getUser(request);

			/** 计算最优优惠券开始 pccvip isThereCoupon 1是有最优优惠券，0无最有优惠券 */
			UserCouponConfigCustomize couponConfig = null;
			couponConfig = new UserCouponConfigCustomize();
			if (couponId == null || "".equals(couponId) || couponId.length() == 0) {
				// 获取用户最优优惠券
				couponConfig = getBestCoupon(borrowNid, loginUser.getUserId(), null, "0");
				// couponConfig=null;
			} else {
				couponConfig = getBestCouponById(couponId);
			}
			BigDecimal couponInterest = BigDecimal.ZERO;
			if (couponConfig != null) {
				modelAndView.addObject("isThereCoupon", 1);
				BigDecimal borrowApr = new BigDecimal(borrow.getBorrowApr());
				String borrowStyle = borrow.getBorrowStyle();
				if (couponConfig.getCouponType() == 1) {
					couponInterest = getInterestDj(couponConfig.getCouponQuota(), couponConfig.getCouponProfitTime(),
							borrowApr);
				} else {
					couponInterest = getInterest(borrowStyle, couponConfig.getCouponType(), borrowApr,
							couponConfig.getCouponQuota(), "0", new Integer(borrow.getBorrowPeriod() == null ? "0"
									: borrow.getBorrowPeriod()));
				}

				couponConfig.setCouponInterest(df.format(couponInterest));
			} else {
				modelAndView.addObject("isThereCoupon", 0);
			}

			modelAndView.addObject("couponConfig", couponConfig);
			/** 计算最优优惠券结束 */
			if (borrow.getInvestAccount() != null) {
				modelAndView.addObject("InvestAccountInt", new BigDecimal(borrow.getInvestAccount()).intValue());
			}
			// 去最大出借金额和可投的最小值
			/*
			 * if
			 * (borrow.getTenderAccountMax()!=null&&borrow.getInvestAccount()!=
			 * null) { borrow.setInvestAccount(( new
			 * BigDecimal(borrow.getTenderAccountMax()).intValue()< new
			 * BigDecimal(borrow.getInvestAccount()).intValue()? new
			 * BigDecimal(borrow.getTenderAccountMax()).intValue(): new
			 * BigDecimal(borrow.getInvestAccount()).intValue())+""); }
			 */
			String nowTime = String.valueOf(GetDate.getNowTime10());
			modelAndView.addObject("nowTime", nowTime);
			String borrowStyle = borrow.getBorrowStyle();
			BigDecimal borrowInterest = new BigDecimal(0);

			/** 叠加收益率开始 pccvip */
			// 收益率
			BigDecimal borrowApr = new BigDecimal(borrow.getBorrowApr());

			/*
			 * if(couponConfig!=null){ if(couponConfig.getCouponType()==1){
			 * BigDecimal
			 * tempMoney=StringUtils.isEmpty(money)?BigDecimal.ZERO:new
			 * BigDecimal(money);
			 * money=tempMoney.add(couponConfig.getCouponQuota()).toString(); }
			 * if(couponConfig.getCouponType()==2){
			 * borrowApr=borrowApr.add(couponConfig.getCouponQuota()); }
			 * 
			 * }
			 */
			/** 叠加收益率结束 */
			/**
			 * 融通宝收益叠加
			 */
			if (borrow.getType().equals("13")) {
				borrowApr = borrowApr.add(new BigDecimal(borrow.getBorrowExtraYield()));
			}
			/** 可用优惠券张数开始 pccvip */
			String couponAvailableCount = couponService.getUserCouponAvailableCount(borrowNid, loginUser.getUserId(),
					"0", "0");
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
			/** 获取用户是否是vip 结束 pccvip */

			/** 获取用户优惠券总张数开始 pccvip */
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("usedFlag", 0);
			paraMap.put("userId", loginUser.getUserId());
			Integer recordTotal = couponService.countCouponUsers(paraMap);
			modelAndView.addObject("recordTotal", recordTotal);
			/** 获取用户优惠券总张数结束 pccvip */

			switch (borrowStyle) {
			case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“
				// 计算历史回报
				borrowInterest = DuePrincipalAndInterestUtils.getMonthInterest(
						new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")),
						Integer.parseInt(borrow.getBorrowPeriod())).divide(new BigDecimal("1"), 2,
						BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“
				borrowInterest = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(borrow.getBorrowAccount()),
						borrowApr.divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod())).divide(
						new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：；
				borrowInterest = BeforeInterestAfterPrincipalUtils.getInterestCount(
						new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")),
						Integer.parseInt(borrow.getBorrowPeriod()), Integer.parseInt(borrow.getBorrowPeriod())).divide(
						new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：；
				borrowInterest = AverageCapitalPlusInterestUtils.getInterestCount(
						new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")),
						Integer.parseInt(borrow.getBorrowPeriod())).divide(new BigDecimal("1"), 2,
						BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
				borrowInterest = AverageCapitalUtils.getInterestCount(new BigDecimal(borrow.getBorrowAccount()),
						borrowApr.divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod())).divide(
						new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			default:
				break;
			}

			if (couponConfig != null && couponConfig.getCouponType() == 3) {
				modelAndView.addObject("capitalInterest",
						df.format(borrowInterest.add(couponConfig.getCouponQuota()).subtract(couponInterest)));
			} else if (couponConfig != null && couponConfig.getCouponType() == 1) {
				modelAndView.addObject("capitalInterest", borrowInterest.add(couponInterest));
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
				List<RepayPlanBean> repayPlanList = this.projectService.getRepayPlan(borrowNid);
				modelAndView.addObject("repayPlanList", repayPlanList);
				// 相关文件
				List<ProjectFileBean> files = this.projectService.searchProjectFiles(borrowNid, HOST_URL);
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
				List<WebVehiclePledgeCustomize> vehiclePledgeList = this.projectService
						.selectVehiclePledgeList(borrowNid);
				// 添加相应的汽车抵押信息
				modelAndView.addObject("vehiclePledgeList", vehiclePledgeList);
				// 5查询相应的认证信息
				List<WebProjectAuthenInfoCustomize> authenList = projectService.searchProjectAuthenInfos(borrowNid);
				modelAndView.addObject("authenList", authenList);
				// 6查询相应的还款计划
				List<RepayPlanBean> repayPlanList = this.projectService.getRepayPlan(borrowNid);
				modelAndView.addObject("repayPlanList", repayPlanList);
				// 7 相关文件
				List<ProjectFileBean> files = this.projectService.searchProjectFiles(borrowNid, HOST_URL);
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

			LogUtil.endLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_DETAIL_ACTION);
			return modelAndView;
		}

	}

	private BigDecimal getInterestDj(BigDecimal couponQuota, Integer couponProfitTime, BigDecimal borrowApr) {
		BigDecimal earnings = new BigDecimal("0");

		earnings = couponQuota.multiply(borrowApr.divide(new BigDecimal(100), 6, BigDecimal.ROUND_HALF_UP))
				.divide(new BigDecimal(365), 6, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(couponProfitTime))
				.setScale(2, BigDecimal.ROUND_DOWN);

		return earnings;

	}

	private BigDecimal getInterest(String borrowStyle, Integer couponType, BigDecimal borrowApr,
			BigDecimal couponQuota, String money, Integer borrowPeriod) {
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
			earnings = DuePrincipalAndInterestUtils.getMonthInterest(accountDecimal,
					borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷360*天数；
			// 计算历史回报
			earnings = DuePrincipalAndInterestUtils.getDayInterest(accountDecimal,
					borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：历史回报=出借金额*年化收益÷12*月数；
			// 计算历史回报
			earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(accountDecimal,
					borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2,
					BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：历史回报=出借金额*年化收益÷12*月数；
			// 计算历史回报
			earnings = AverageCapitalPlusInterestUtils.getInterestCount(accountDecimal,
					borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
			earnings = AverageCapitalUtils.getInterestCount(accountDecimal, borrowApr.divide(new BigDecimal("100")),
					borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
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
	@RequestMapping(value = ProjectDefine.PROJECT_PREVIEW_ACTION)
	public ModelAndView getProjectPreview(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_PREVIEW_ACTION);
		ModelAndView modelAndView = new ModelAndView(ProjectDefine.HZT_PROJECT_PREVIEW_PTAH);
		// 1.获取项目标号
		String borrowNid = request.getParameter("borrowNid");
		// 2.根据项目标号获取相应的项目信息
		WebProjectDetailCustomize borrow = this.projectService.selectProjectPreview(borrowNid);
		if (borrow == null) {
			modelAndView = new ModelAndView(ProjectDefine.ERROR_PTAH);
			return modelAndView;
		} else {
			String nowTime = String.valueOf(GetDate.getNowTime10());
			modelAndView.addObject("nowTime", nowTime);
			String borrowStyle = borrow.getBorrowStyle();
			BigDecimal borrowInterest = new BigDecimal(0);
			BigDecimal borrowApr = new BigDecimal(borrow.getBorrowApr());
			/**
			 * 融通宝收益叠加
			 */
			if (borrow.getType().equals("13")) {
				borrowApr = borrowApr.add(new BigDecimal(borrow.getBorrowExtraYield()));
			}

			switch (borrowStyle) {
			case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“
				// 计算历史回报
				borrowInterest = DuePrincipalAndInterestUtils.getMonthInterest(
						new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")),
						Integer.parseInt(borrow.getBorrowPeriod())).divide(new BigDecimal("1"), 2,
						BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“
				borrowInterest = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(borrow.getBorrowAccount()),
						borrowApr.divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod())).divide(
						new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：；
				borrowInterest = BeforeInterestAfterPrincipalUtils.getInterestCount(
						new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")),
						Integer.parseInt(borrow.getBorrowPeriod()), Integer.parseInt(borrow.getBorrowPeriod())).divide(
						new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：；
				borrowInterest = AverageCapitalPlusInterestUtils.getInterestCount(
						new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")),
						Integer.parseInt(borrow.getBorrowPeriod())).divide(new BigDecimal("1"), 2,
						BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
				borrowInterest = AverageCapitalUtils.getInterestCount(new BigDecimal(borrow.getBorrowAccount()),
						borrowApr.divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod())).divide(
						new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
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
			List<ProjectFileBean> files = this.projectService.searchProjectFiles(borrowNid, HOST_URL);
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
			LogUtil.endLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_PREVIEW_ACTION);
			return modelAndView;
		}

	}

	/**
	 * 汇添金专属项目详细信息预览
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = ProjectDefine.HTJ_PROJECT_PREVIEW_ACTION)
	public ModelAndView getHTJProjectPreview(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(ProjectDefine.THIS_CLASS, ProjectDefine.HTJ_PROJECT_PREVIEW_ACTION);
		ModelAndView modelAndView = new ModelAndView(ProjectDefine.HZT_PROJECT_PREVIEW_PTAH);
		// 1.获取项目标号
		String borrowNid = request.getParameter("borrowNid");
		// 2.根据项目标号获取相应的项目信息
		WebProjectDetailCustomize borrow = this.projectService.selectHTJProjectPreview(borrowNid);
		if (borrow == null) {
			modelAndView = new ModelAndView(ProjectDefine.ERROR_PTAH);
			return modelAndView;
		} else {
			String nowTime = String.valueOf(GetDate.getNowTime10());
			modelAndView.addObject("nowTime", nowTime);
			String borrowStyle = borrow.getBorrowStyle();
			BigDecimal borrowInterest = new BigDecimal(0);
			switch (borrowStyle) {
			case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“
				// 计算历史回报
				borrowInterest = DuePrincipalAndInterestUtils.getMonthInterest(
						new BigDecimal(borrow.getBorrowAccount()),
						new BigDecimal(borrow.getBorrowApr()).divide(new BigDecimal("100")),
						Integer.parseInt(borrow.getBorrowPeriod())).divide(new BigDecimal("1"), 2,
						BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“
				borrowInterest = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(borrow.getBorrowAccount()),
						new BigDecimal(borrow.getBorrowApr()).divide(new BigDecimal("100")),
						Integer.parseInt(borrow.getBorrowPeriod())).divide(new BigDecimal("1"), 2,
						BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：；
				borrowInterest = BeforeInterestAfterPrincipalUtils.getInterestCount(
						new BigDecimal(borrow.getBorrowAccount()),
						new BigDecimal(borrow.getBorrowApr()).divide(new BigDecimal("100")),
						Integer.parseInt(borrow.getBorrowPeriod()), Integer.parseInt(borrow.getBorrowPeriod())).divide(
						new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：；
				borrowInterest = AverageCapitalPlusInterestUtils.getInterestCount(
						new BigDecimal(borrow.getBorrowAccount()),
						new BigDecimal(borrow.getBorrowApr()).divide(new BigDecimal("100")),
						Integer.parseInt(borrow.getBorrowPeriod())).divide(new BigDecimal("1"), 2,
						BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
				borrowInterest = AverageCapitalUtils.getInterestCount(new BigDecimal(borrow.getBorrowAccount()),
						new BigDecimal(borrow.getBorrowApr()).divide(new BigDecimal("100")),
						Integer.parseInt(borrow.getBorrowPeriod())).divide(new BigDecimal("1"), 2,
						BigDecimal.ROUND_DOWN);
				break;
			default:
				break;
			}
			borrow.setBorrowInterest(borrowInterest.toString());

			// 添加相应的项目详情信息
			modelAndView.addObject("projectDeatil", borrow);
			// 风控信息
			WebRiskControlCustomize riskControl = this.projectService.selectHTJRiskControl(borrowNid);
			riskControl.setControlMeasures(riskControl.getControlMeasures()==null?"":riskControl.getControlMeasures().replace("\r\n", ""));
			riskControl.setControlMort(riskControl.getControlMort()==null?"":riskControl.getControlMort().replace("\r\n", ""));
			// 添加风控信息
			modelAndView.addObject("riskControl", riskControl);
			List<WebMortgageCustomize> mortgageList = this.projectService.selectHTJMortgageList(borrowNid);
			// 添加相应的房产信息
			modelAndView.addObject("mortgageList", mortgageList);
			List<WebVehiclePledgeCustomize> vehiclePledgeList = this.projectService
					.selectHTJVehiclePledgeList(borrowNid);
			// 添加相应的汽车抵押信息
			modelAndView.addObject("vehiclePledgeList", vehiclePledgeList);
			// 5查询相应的认证信息
			List<WebProjectAuthenInfoCustomize> authenList = projectService.searchHTJProjectAuthenInfos(borrowNid);
			modelAndView.addObject("authenList", authenList);
			// 7 相关文件
			List<ProjectFileBean> files = this.projectService.searchHTJProjectFiles(borrowNid, HOST_URL);
			modelAndView.addObject("fileList", files);

			// 处置预案
			WebHzcDisposalPlanCustomize disposalPlan = this.projectService.searchHTJDisposalPlan(borrowNid);
			modelAndView.addObject("disposalPlan", disposalPlan);
			if (borrow.getType().equals("9")) {// 如果项目为汇资产项目
				// 4查询相应的汇资产的首页信息
				WebHzcProjectDetailCustomize borrowInfo = this.projectService.searchHTJHzcProjectDetail(borrowNid);
				modelAndView.addObject("borrowInfo", borrowInfo);
			} else {// 项目为非汇资产项目
				// 4查询非汇资产项目的项目信息
				if (StringUtils.isNotEmpty(borrow.getComOrPer()) && borrow.getComOrPer().equals("1")) {
					// 查询相应的企业项目详情
					WebProjectCompanyDetailCustomize borrowInfo = projectService
							.searchHTJProjectCompanyDetail(borrowNid);
					modelAndView.addObject("borrowInfo", borrowInfo);
				} else if (StringUtils.isNotEmpty(borrow.getComOrPer()) && borrow.getComOrPer().equals("2")) {
					// 查询相应的汇直投个人项目详情
					WebProjectPersonDetailCustomize borrowInfo = projectService.searchHTJProjectPersonDetail(borrowNid);
					modelAndView.addObject("borrowInfo", borrowInfo);
				}
			}
			LogUtil.endLog(ProjectDefine.THIS_CLASS, ProjectDefine.HTJ_PROJECT_PREVIEW_ACTION);
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
	@RequestMapping(value = ProjectDefine.PROJECT_CONSUME_ACTION, produces = "application/json; charset=utf-8")
	public ProjectConsumeListAjaxBean searchProjectConsumeList(@ModelAttribute ProjectConsumeBean form,
			HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_CONSUME_ACTION);
		ProjectConsumeListAjaxBean result = new ProjectConsumeListAjaxBean();
		this.createProjectConsumePage(result, form);
		result.success();
		result.setHost(HomePageDefine.HOST);
		LogUtil.endLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_CONSUME_ACTION);
		return result;
	}

	/**
	 * 创建相应的汇消费项目的打包信息
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */

	private void createProjectConsumePage(ProjectConsumeListAjaxBean result, ProjectConsumeBean form) {

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
	@RequestMapping(value = ProjectDefine.PROJECT_INVEST_ACTION, produces = "application/json; charset=utf-8")
	public ProjectInvestListAjaxBean searchProjectInvestList(@ModelAttribute ProjectInvestBean form,
			HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_INVEST_ACTION);
		ProjectInvestListAjaxBean result = new ProjectInvestListAjaxBean();
		this.createProjectInvestPage(result, form);

		result.success();
		result.setHost(HomePageDefine.HOST);
		LogUtil.endLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_INVEST_ACTION);
		return result;
	}

	/**
	 * 创建用户出借记录分页查询
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createProjectInvestPage(ProjectInvestListAjaxBean result, ProjectInvestBean form) {

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
	@RequestMapping(value = ProjectDefine.PROJECT_STATUS_ACTION)
	public ProjectStatusAjaxBean searchProjectStatus(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_STATUS_ACTION);
		// 1.获取项目标号
		String borrowNid = request.getParameter("borrowNid");
		// 2.根据项目标号获取相应的项目信息
		WebProjectDetailCustomize borrow = this.projectService.selectProjectDetail(borrowNid);
		ProjectStatusAjaxBean projectStatus = new ProjectStatusAjaxBean();
		if (borrow == null) {
			projectStatus.setStatus(false);
			projectStatus.setMessage("标不存在");
		} else {
			projectStatus.setProjectStatus(borrow.getStatus());
			projectStatus.setStatus(true);
			projectStatus.setMessage("标的状态查询成功");
		}
		LogUtil.endLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_STATUS_ACTION);
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

	/**
	 * 查询专属标相应的项目详情
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = ProjectDefine.HTJ_PROJECT_DETAIL_ACTION)
	public ModelAndView searchHtjProjectDetail(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(ProjectDefine.THIS_CLASS, ProjectDefine.HTJ_PROJECT_DETAIL_ACTION);
		ModelAndView modelAndView = new ModelAndView(ProjectDefine.HTJ_PROJECT_DETAIL_PTAH);
		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		// 1.获取项目标号
		String borrowNid = request.getParameter("borrowNid");
		// System.out.println("~~~~~~~~~~出借页面加载获得标编号~~~~~~~~~~~~~borrowNid:"+borrowNid);
		// 2.根据项目标号获取相应的项目信息
		DebtPlanBorrowDetailCustomize borrow = this.projectService.selectHtjProjectDetail(borrowNid);
		if (borrow == null) {
			modelAndView = new ModelAndView(ProjectDefine.ERROR_PTAH);
			return modelAndView;
		} else {
			WebViewUser loginUser = WebUtils.getUser(request);
			if (borrow.getInvestAccount() != null) {
				modelAndView.addObject("InvestAccountInt", new BigDecimal(borrow.getInvestAccount()).intValue());
			}
			String nowTime = String.valueOf(GetDate.getNowTime10());
			modelAndView.addObject("nowTime", nowTime);
			String borrowStyle = borrow.getBorrowStyle();
			BigDecimal borrowInterest = new BigDecimal(0);

			// 收益率
			BigDecimal borrowApr = new BigDecimal(borrow.getBorrowApr());

			switch (borrowStyle) {
			case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“
				// 计算历史回报
				borrowInterest = DuePrincipalAndInterestUtils.getMonthInterest(
						new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")),
						Integer.parseInt(borrow.getBorrowPeriod())).divide(new BigDecimal("1"), 2,
						BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“
				borrowInterest = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(borrow.getBorrowAccount()),
						borrowApr.divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod())).divide(
						new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：；
				borrowInterest = BeforeInterestAfterPrincipalUtils.getInterestCount(
						new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")),
						Integer.parseInt(borrow.getBorrowPeriod()), Integer.parseInt(borrow.getBorrowPeriod())).divide(
						new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：；
				borrowInterest = AverageCapitalPlusInterestUtils.getInterestCount(
						new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")),
						Integer.parseInt(borrow.getBorrowPeriod())).divide(new BigDecimal("1"), 2,
						BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
				borrowInterest = AverageCapitalUtils.getInterestCount(new BigDecimal(borrow.getBorrowAccount()),
						borrowApr.divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod())).divide(
						new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			default:
				break;
			}
			modelAndView.addObject("capitalInterest", df.format(borrowInterest));
			borrow.setBorrowInterest(borrowInterest.toString());

			if (borrow.getType().equals("9")) {// 如果项目为汇资产项目
				// 添加相应的项目详情信息
				modelAndView.addObject("projectDeatil", borrow);
				// 4查询相应的汇资产的首页信息
				DebtPlanBorrowHzcDetailCustomize borrowInfo = this.projectService.searchHzcPlanProjectDetail(borrowNid);
				modelAndView.addObject("borrowInfo", borrowInfo);
				// 处置预案
				DebtPlanBorrowHzcDisposalPlanCustomize disposalPlan = this.projectService
						.searchDebtPlanBorrowHzcDisposalPlan(borrowNid);
				modelAndView.addObject("disposalPlan", disposalPlan);
				// 5查询相应的还款计划
				List<RepayPlanBean> repayPlanList = this.projectService.getHtjRepayPlan(borrowNid);
				modelAndView.addObject("repayPlanList", repayPlanList);
				// 相关文件
				List<ProjectFileBean> files = this.projectService.searchDebtPlanBorrowFiles(borrowNid, HOST_URL);
				modelAndView.addObject("fileList", files);
			} else {// 项目为非汇资产项目
				// 添加相应的项目详情信息
				modelAndView.addObject("projectDeatil", borrow);
				// 4查询非汇资产项目的项目信息
				if (StringUtils.isNotEmpty(borrow.getComOrPer()) && borrow.getComOrPer().equals("1")) {
					// 查询相应的企业项目详情
					DebtPlanBorrowCompanyDetailCustomize borrowInfo = projectService
							.selectProjectCompanyDetail(borrowNid);
					modelAndView.addObject("borrowInfo", borrowInfo);
				} else if (StringUtils.isNotEmpty(borrow.getComOrPer()) && borrow.getComOrPer().equals("2")) {
					// 查询相应的汇直投个人项目详情
					DebtPlanBorrowPersonDetailCustomize borrowInfo = projectService
							.selectDebtPlanBorrowPersonDetail(borrowNid);
					modelAndView.addObject("borrowInfo", borrowInfo);
				}
				// 风控信息
				DebtPlanBorrowRiskControlCustomize riskControl = this.projectService
						.selectDebtPlanBorrowRiskControl(borrowNid);
				riskControl.setControlMeasures(riskControl.getControlMeasures()==null?"":riskControl.getControlMeasures().replace("\r\n", ""));
				riskControl.setControlMort(riskControl.getControlMort()==null?"":riskControl.getControlMort().replace("\r\n", ""));
				// 添加风控信息
				modelAndView.addObject("riskControl", riskControl);
				List<DebtPlanBorrowMortgageCustomize> mortgageList = this.projectService
						.selectDebtPlanBorrowMortgageList(borrowNid);
				// 添加相应的房产信息
				modelAndView.addObject("mortgageList", mortgageList);
				List<DebtPlanBorrowVehiclePledgeCustomize> vehiclePledgeList = this.projectService
						.selectDebtPlanBorrowVehiclePledgeList(borrowNid);
				// 添加相应的汽车抵押信息
				modelAndView.addObject("vehiclePledgeList", vehiclePledgeList);
				// 5查询相应的认证信息
				List<DebtPlanBorrowAuthenInfoCustomize> authenList = projectService
						.searchDebtPlanBorrowAuthenInfo(borrowNid);
				modelAndView.addObject("authenList", authenList);
				// 6查询相应的还款计划
				List<RepayPlanBean> repayPlanList = this.projectService.getHtjRepayPlan(borrowNid);
				modelAndView.addObject("repayPlanList", repayPlanList);
				// 7 相关文件
				List<ProjectFileBean> files = this.projectService.searchDebtPlanBorrowFiles(borrowNid, HOST_URL);
				modelAndView.addObject("fileList", files);
			}
			String loginFlag = "0";
			String investFlag = "0";
			if (loginUser == null) {
				modelAndView.addObject("loginFlag", loginFlag);
			} else {
				loginFlag = "1";
				Integer userId = loginUser.getUserId();
				// 用户是否出借项目
				int count = this.projectService.countUserDebtInvest(loginUser.getUserId(), borrowNid);
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
			}

			LogUtil.endLog(ProjectDefine.THIS_CLASS, ProjectDefine.HTJ_PROJECT_DETAIL_ACTION);
			return modelAndView;
		}

	}

	/**
	 * 查询相应的汇添金项目的出借列表
	 * 
	 * @param hztInvest
	 * @param request
	 * @param response
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = ProjectDefine.HTJ_PROJECT_INVEST_ACTION, produces = "application/json; charset=utf-8")
	public ProjectInvestListAjaxBean searchHtjProjectInvestList(@ModelAttribute ProjectInvestBean form,
			HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_INVEST_ACTION);
		ProjectInvestListAjaxBean result = new ProjectInvestListAjaxBean();
		this.createHtjProjectInvestPage(result, form);

		result.success();
		result.setHost(HomePageDefine.HOST);
		LogUtil.endLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_INVEST_ACTION);
		return result;
	}

	/**
	 * 创建用户出借记录分页查询
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createHtjProjectInvestPage(ProjectInvestListAjaxBean result, ProjectInvestBean form) {

		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		DebtBorrow borrow = this.projectService.selectDebtBorrowByNid(form.getBorrowNid());
		if (borrow != null) {
			result.setInvestTotal(df.format(borrow.getBorrowAccountYes()));
			result.setInvestTimes(String.valueOf(borrow.getTenderTimes()));
		} else {
			result.setInvestTotal(df.format(new BigDecimal("0")));
			result.setInvestTimes("0");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", form.getBorrowNid());
		int recordTotal = this.projectService.countDebtPlanProjectInvestRecordTotal(params);
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
			List<WebProjectInvestListCustomize> recordList = projectService.searchDeptPlanProjectInvestList(params);
			result.setProjectInvestList(recordList);
			result.setPaginator(paginator);
		} else {
			result.setProjectInvestList(new ArrayList<WebProjectInvestListCustomize>());
			result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
		}
	}

	/**
	 * 融通宝介绍页跳转
	 * 
	 * @param hztInvest
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping(value = ProjectDefine.RTB_INTR_ACTION)
	public ModelAndView rtbIntr(@ModelAttribute ProjectInvestBean form, HttpServletRequest request,
			HttpServletResponse response) {
		return new ModelAndView(ProjectDefine.RTB_INTR_PTAH);
	}
}
