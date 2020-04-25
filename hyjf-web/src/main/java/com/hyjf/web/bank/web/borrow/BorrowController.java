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
package com.hyjf.web.bank.web.borrow;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.borrow.BorrowFileCustomBean;
import com.hyjf.bank.service.borrow.BorrowRepayPlanCustomBean;
import com.hyjf.bank.service.borrow.BorrowService;
import com.hyjf.bank.service.user.auth.AuthService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.*;
import com.hyjf.common.enums.utils.ProtocolEnum;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.mybatis.model.customize.web.*;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.web.BaseController;
import com.hyjf.web.agreement.AgreementService;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.coupon.CouponService;
import com.hyjf.web.util.WebUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

@Controller(BorrowDefine.CONTROLLER_NAME)
@RequestMapping(value = BorrowDefine.REQUEST_MAPPING)
public class BorrowController extends BaseController {
	 Logger _log = LoggerFactory.getLogger(BorrowController.class);
	@Autowired
	private BorrowService borrowService;
	@Autowired
	private CouponService couponService;
	@Autowired
    private AuthService authService;

	@Autowired
	private AgreementService agreementService;
	/**
	 * 初始化债权列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = BorrowDefine.INIT_BORROW_LIST_ACTION)
	public ModelAndView initBorrowList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView(BorrowDefine.BORROW_LIST_PTAH);
		modelAndView.addObject("projectType", "HZT");
		modelAndView.addObject("borrowClass", "");
		return modelAndView;	
	}

	/**
	 * 初始化新手汇列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = BorrowDefine.NEW_BORROW_LIST_ACTION)
	public ModelAndView initNewBorrowList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView(BorrowDefine.NEW_BORROW_LIST_PTAH);
		modelAndView.addObject("projectType", "HZT");
		modelAndView.addObject("borrowClass", "NEW");
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
	@RequestMapping(value = BorrowDefine.BORROW_LIST_ACTION, produces = "application/json; charset=utf-8")
	public BorrowListAJaxBean searchBorrowList(@ModelAttribute BorrowBean form, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(BorrowDefine.THIS_CLASS, BorrowDefine.BORROW_LIST_ACTION);
		BorrowListAJaxBean result = new BorrowListAJaxBean();
		this.createBorrowListPage(result, form);
		result.success();
		result.setHost(BorrowDefine.HOST);
		LogUtil.endLog(BorrowDefine.THIS_CLASS, BorrowDefine.BORROW_LIST_ACTION);
		return result;
	}

	/**
	 * 查询相应的项目分页列表
	 * 
	 * @param result
	 * @param form
	 */
	private void createBorrowListPage(BorrowListAJaxBean result, BorrowBean form) {

		List<BorrowProjectType> borrowTypes = this.borrowService.getProjectTypeList();
		String projectType = form.getProjectType();// 项目类型
		String borrowClass = form.getBorrowClass();// 项目子类型
		// 校验相应的项目类型
		if (borrowTypes != null && borrowTypes.size() > 0) {
			boolean typeFlag = false;
			boolean classFlag = false;
			if (StringUtils.isNotBlank(projectType)) {
				for (BorrowProjectType borrowType : borrowTypes) {
					String type = borrowType.getBorrowProjectType();
					if (type.equals(projectType)) {
						typeFlag = true;
					}
					if (StringUtils.isNotBlank(borrowClass)) {
						String classType = borrowType.getBorrowClass();
						if (classType.equals(borrowClass)) {
							classFlag = true;
						}
					} else {
						classFlag = true;
					}
				}
			} else {
				result.setProjectList(new ArrayList<WebProjectListCustomize>());
				result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
			}
			if (typeFlag && classFlag) {
				Map<String, Object> paramsCnt = new HashMap<String, Object>();
				paramsCnt.put("projectType", projectType);
				paramsCnt.put("borrowClass", borrowClass);

				// 汇盈金服pc官网列表定向标过滤
				paramsCnt.put("publishInstCode", CustomConstants.HYJF_INST_CODE);

				// 统计相应的汇直投的数据记录数
				int projecTotal = this.borrowService.countProjectListRecordTotalNew(paramsCnt);
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.putAll(paramsCnt);
				if (projecTotal > 0) {
					
					//add by cwyang 项目列表显示2页
					int pageNum = 2;
					if(projecTotal > form.getPageSize() * pageNum){
						projecTotal = form.getPageSize() * pageNum;
					}
					
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
					List<WebProjectListCustomize> projectList = borrowService.searchProjectListNew(params);
					// add by nxl 判断是否为产品加息 start
					if(null!=projectList&&projectList.size()>0){
						for(WebProjectListCustomize webProjectListCustomize:projectList){
							int intFlg = Integer.parseInt(StringUtils.isNotBlank(webProjectListCustomize.getIncreaseInterestFlag())?webProjectListCustomize.getIncreaseInterestFlag():"0");
							BigDecimal dbYield = new BigDecimal(StringUtils.isNotBlank(webProjectListCustomize.getBorrowExtraYield())?webProjectListCustomize.getBorrowExtraYield():"0");
							boolean booleanVal = Validator.isIncrease(intFlg,dbYield);
							webProjectListCustomize.setIsIncrease(String.valueOf(booleanVal));
						}
					}
					// add by nxl 判断是否为产品加息 end
					result.setProjectList(projectList);
					result.setPaginator(paginator);
					int nowTime = GetDate.getNowTime10();
					result.setNowTime(nowTime);
				} else {
					result.setProjectList(new ArrayList<WebProjectListCustomize>());
					result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
				}
			} else {
				result.setProjectList(new ArrayList<WebProjectListCustomize>());
				result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
			}
		} else {
			result.setProjectList(new ArrayList<WebProjectListCustomize>());
			result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
		}
	}


	/**
	 * 项目详情预览
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = BorrowDefine.BORROW_DETAIL_PREVIEW_ACTION)
	public ModelAndView getBorrowDetailPreview(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(BorrowDefine.THIS_CLASS, BorrowDefine.BORROW_DETAIL_PREVIEW_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowDefine.BORROW_DETAIL_PREVIEW_OLD_PTAH);
		// 1.获取项目标号
		String borrowNid = request.getParameter("borrowNid");
		// 用户信息
		WebViewUser loginUser = WebUtils.getUser(request);
		// 2.根据项目标号获取相应的项目信息
		WebProjectDetailCustomize borrow = this.borrowService.selectProjectPreview(borrowNid);
		//没有标的信息
		if (borrow == null) {
			modelAndView = new ModelAndView(BorrowDefine.ERROR_PTAH);
			return modelAndView;
		}
		//用户id
		Integer userId = null;
		//没有登录信息
		if(loginUser != null){
			userId = loginUser.getUserId();
		}
		//判断新标还是老标，老标走原来逻辑原来页面，新标走新方法 0为老标 1为新标
		if(borrow.getIsNew() == 0 || borrow.getType().equals("13")){ 
			getProjectDetail(modelAndView, borrow,userId);
		}else{ 
			modelAndView = new ModelAndView(BorrowDefine.BORROW_DETAIL_PREVIEW_NEW_PTAH);
			getProjectDetailNew(modelAndView, borrow,userId);
		}
		LogUtil.endLog(BorrowDefine.THIS_CLASS, BorrowDefine.BORROW_DETAIL_PREVIEW_ACTION);
		return modelAndView;

	}

	
	/**
	 * 查询相应的项目详情
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = BorrowDefine.BORROW_DETAIL_ACTION)
	public ModelAndView searchProjectDetail(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(BorrowDefine.THIS_CLASS, BorrowDefine.BORROW_DETAIL_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowDefine.BORROW_DETAIL_PTAH);
		// 1.获取项目标号
		String borrowNid = request.getParameter("borrowNid");
		// 用户信息
		WebViewUser loginUser = WebUtils.getUser(request);
		// 2.根据项目标号获取相应的项目信息(新手与散标共用的详情查询)
		WebProjectDetailCustomize borrow = this.borrowService.selectProjectDetail(borrowNid);
		//没有标的信息
		if (borrow == null) {
			modelAndView = new ModelAndView(BorrowDefine.ERROR_PTAH);
			return modelAndView;
		}
		// add by nxl 判断是否为产品加息 start
		if (null != borrow) {
			int intFlg = Integer.parseInt(StringUtils.isNotBlank(borrow.getIncreaseInterestFlag())?borrow.getIncreaseInterestFlag():"0");
			BigDecimal dbYield = new BigDecimal(StringUtils.isNotBlank(borrow.getBorrowExtraYield())?borrow.getBorrowExtraYield():"0");
			boolean booleanVal = Validator.isIncrease(intFlg, dbYield);
			borrow.setIsIncrease(String .valueOf(booleanVal));
		}
		// add by nxl 判断是否为产品加息 end
		// 看标的是否关联计划 ，防止别有用心的guy从散标列表出借汇计划标的-->展示取消判断
		if(borrow.getIsShow() != null && borrow.getIsShow().intValue() ==1){
//			if(borrow){
//				modelAndView = new ModelAndView(BorrowDefine.ERROR_PTAH);
//				return modelAndView;
//			}
		}
		
		//用户id
		Integer userId = null;
		//没有登录信息
		if(loginUser != null){
			userId = loginUser.getUserId();
			try {
				if(loginUser.getIsEvaluationFlag()==1 && null != loginUser.getEvaluationExpiredTime()){
					//测评到期日
					Long lCreate = loginUser.getEvaluationExpiredTime().getTime();
					//当前日期
					Long lNow = System.currentTimeMillis();
					if (lCreate <= lNow) {
						//已过期需要重新评测
						modelAndView.addObject("riskFlag", "2");
					} else {
						//未到一年有效期
						modelAndView.addObject("riskFlag", "1");
					}
				}else{
					modelAndView.addObject("riskFlag", "0");
				}
				// modify by liuyang 20180411 用户是否完成风险测评标识 end
			} catch (Exception e) {
				logger.error("查询用户是否完成风险测评标识出错....", e);
				modelAndView.addObject("riskFlag", "0");
			}
		}
		
		//判断新标还是老标，老标走原来逻辑原来页面，新标走新方法 0为老标 1为新标(融通宝走原来页面)
		if(borrow.getIsNew() == 0 || borrow.getType().equals("13")){ 
			getProjectDetail(modelAndView, borrow,userId);
		}else{ 
			modelAndView = new ModelAndView(BorrowDefine.BORROW_DETAIL_NEW_PTAH);
			getProjectDetailNew (modelAndView, borrow,userId);
		}
		// 缴费授权
//		modelAndView.addObject("paymentAuthStatus", loginUser==null?"0":loginUser.getPaymentAuthStatus());
		//update by jijun 2018/04/09 合规接口改造一期
		//协议名称 动态获得
		List<ProtocolTemplate> list = agreementService.getdisplayNameDynamic();
		if(CollectionUtils.isNotEmpty(list)){
			//是否在枚举中有定义
			for (ProtocolTemplate p : list) {
				String protocolType = p.getProtocolType();
				String alia = ProtocolEnum.getAlias(protocolType);
				if (alia != null){
					modelAndView.addObject(alia, p.getDisplayName());
				}
			}
		}
		modelAndView.addObject("paymentAuthStatus", "");

        if (userId != null) {
            HjhUserAuth hjhUserAuth = this.authService.getHjhUserAuthByUserId(userId);
            // 服务费授权状态
            modelAndView.addObject("paymentAuthStatus", hjhUserAuth == null ? "" : hjhUserAuth.getAutoPaymentStatus());
            // 是否开启服务费授权 0未开启 1已开启
            modelAndView.addObject("paymentAuthOn",
                    CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus());
            modelAndView.addObject("isCheckUserRole",PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN));
        }
		// 借款人和垫付机构不能出借
        modelAndView.addObject("roleId", loginUser==null?"0":loginUser.getRoleId());

		LogUtil.endLog(BorrowDefine.THIS_CLASS, BorrowDefine.BORROW_DETAIL_ACTION);
		return modelAndView;

	}
	/**
	 * 获取老标的相关信息（不变）
	 * @param modelAndView
	 * @param borrow
	 * @param userId
	 */
	private void getProjectDetail(ModelAndView modelAndView,WebProjectDetailCustomize borrow,Integer userId){
		//标的号
		String borrowNid = borrow.getBorrowNid();
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
		//可投金额
		if (borrow.getInvestAccount() != null) {
			modelAndView.addObject("InvestAccountInt", new BigDecimal(borrow.getInvestAccount()).intValue());
		}
		// 当前系统时间
		String nowTime = String.valueOf(GetDate.getNowTime10());
		modelAndView.addObject("nowTime", nowTime);
		/* 项目还款方式 */
		String borrowStyle = borrow.getBorrowStyle();
		// 收益率
		BigDecimal borrowApr = new BigDecimal(borrow.getBorrowApr());
		
		/** 计算最优优惠券开始 pccvip isThereCoupon 1是有最优优惠券，0无最有优惠券 */
		UserCouponConfigCustomize couponConfig = new UserCouponConfigCustomize();
		BigDecimal couponInterest = BigDecimal.ZERO;
		//用户未登陆,不取优惠券
		if(userId == null){
			modelAndView.addObject("isThereCoupon", 0);//无最优优惠券
			modelAndView.addObject("couponConfig", null);//优惠券配置信息
			modelAndView.addObject("couponAvailableCount", 0);//可用优惠券张数
			couponConfig = null;
		}else{
			// 获取用户最优优惠券
			couponConfig = getBestCoupon(borrowNid, userId, null, CustomConstants.CLIENT_PC); 
			if (couponConfig != null) {
				modelAndView.addObject("isThereCoupon", 1);
				if (couponConfig.getCouponType() == 1) {
					couponInterest = getInterestDj(couponConfig.getCouponQuota(), couponConfig.getCouponProfitTime(), borrowApr);
				} else {
					couponInterest = getInterest(borrowStyle, couponConfig.getCouponType(), borrowApr, couponConfig.getCouponQuota(), "0",
							new Integer(borrow.getBorrowPeriod() == null ? "0" : borrow.getBorrowPeriod()));
				}
				couponConfig.setCouponInterest(CustomConstants.DF_FOR_VIEW.format(couponInterest));
			} else {
				modelAndView.addObject("isThereCoupon", 0);
			}
			modelAndView.addObject("couponConfig", couponConfig);
			
			/** 可用优惠券张数开始 pccvip */
			String couponAvailableCount = couponService.getUserCouponAvailableCount(borrowNid, userId, "0", "0");
			modelAndView.addObject("couponAvailableCount", couponAvailableCount);
			/** 可用优惠券张数结束 pccvip */
		}
		/** 计算最优优惠券结束 */
		
		//收益
		BigDecimal borrowInterest = BigDecimal.ZERO;

		/**
		 * 融通宝收益叠加
		 */
		// mod by nxl 设置项目加息收益 Start
		/*if (borrow.getType().equals("13")) {
			borrowApr = borrowApr.add(new BigDecimal(borrow.getBorrowExtraYield()));
		}*/
		BigDecimal borrowExtraYield = new BigDecimal(StringUtils.isNotBlank(borrow.getBorrowExtraYield())?borrow.getBorrowExtraYield():"0");
		int intFlg = Integer.parseInt(StringUtils.isNotBlank(borrow.getIncreaseInterestFlag())?borrow.getIncreaseInterestFlag():"0");
		boolean isIncrease = Validator.isIncrease(intFlg,borrowExtraYield);
		if(isIncrease){
			borrowApr = borrowApr.add(new BigDecimal(borrow.getBorrowExtraYield()));
			borrow.setIncreaseInterestFlag(String.valueOf(isIncrease));
		}
		// mod by nxl 设置项目加息收益 End
		/** 获取用户优惠券总张数开始 pccvip */
//		Map<String, Object> paraMap = new HashMap<String, Object>();
//		paraMap.put("usedFlag", 0);
//		paraMap.put("userId", userId);
//		Integer recordTotal = couponService.countCouponUsers(paraMap);
//		modelAndView.addObject("recordTotal", recordTotal);
		/** 获取用户优惠券总张数结束 pccvip */

		switch (borrowStyle) {
		case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“
			// 计算历史回报
			borrowInterest = DuePrincipalAndInterestUtils
					.getMonthInterest(new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod()))
					.divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“
			borrowInterest = DuePrincipalAndInterestUtils
					.getDayInterest(new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod()))
					.divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：；
			borrowInterest = BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")),
					Integer.parseInt(borrow.getBorrowPeriod()), Integer.parseInt(borrow.getBorrowPeriod())).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：；
			borrowInterest = AverageCapitalPlusInterestUtils
					.getInterestCount(new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod()))
					.divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
			borrowInterest = AverageCapitalUtils.getInterestCount(new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod()))
					.divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
			break;
		default:
			break;
		}

		if (couponConfig != null && couponConfig.getCouponType() == 3) {
			modelAndView.addObject("capitalInterest", CustomConstants.DF_FOR_VIEW.format(borrowInterest.add(couponConfig.getCouponQuota()).subtract(couponInterest)));
		} else if (couponConfig != null && couponConfig.getCouponType() == 1) {
			modelAndView.addObject("capitalInterest", borrowInterest.add(couponInterest));
		} else {
			modelAndView.addObject("capitalInterest", CustomConstants.DF_FOR_VIEW.format(borrowInterest.subtract(couponInterest)));
		}

		borrow.setBorrowInterest(borrowInterest.toString());
		if (borrow.getType().equals("9")) {// 如果项目为汇资产项目
			// 添加相应的项目详情信息
			modelAndView.addObject("projectDeatil", borrow);
			// 4查询相应的汇资产的首页信息
			WebHzcProjectDetailCustomize borrowInfo = this.borrowService.searchHzcProjectDetail(borrowNid);
			modelAndView.addObject("borrowInfo", borrowInfo);
			// 处置预案
			WebHzcDisposalPlanCustomize disposalPlan = this.borrowService.searchDisposalPlan(borrowNid);
			modelAndView.addObject("disposalPlan", disposalPlan);
			// 5查询相应的还款计划
			List<BorrowRepayPlanCustomBean> repayPlanList = this.borrowService.getRepayPlan(borrowNid);
			modelAndView.addObject("repayPlanList", repayPlanList);
			// 相关文件
			List<BorrowFileCustomBean> files = this.borrowService.searchProjectFiles(borrowNid, CustomConstants.HOST);
			modelAndView.addObject("fileList", files);
		} else {// 项目为非汇资产项目
			// 添加相应的项目详情信息
			modelAndView.addObject("projectDeatil", borrow);
			// 4查询非汇资产项目的项目信息
			if (StringUtils.isNotEmpty(borrow.getComOrPer()) && borrow.getComOrPer().equals("1")) {
				// 查询相应的企业项目详情
				WebProjectCompanyDetailCustomize borrowInfo = borrowService.searchProjectCompanyDetail(borrowNid);
				modelAndView.addObject("borrowInfo", borrowInfo);
			} else if (StringUtils.isNotEmpty(borrow.getComOrPer()) && borrow.getComOrPer().equals("2")) {
				// 查询相应的汇直投个人项目详情
				WebProjectPersonDetailCustomize borrowInfo = borrowService.searchProjectPersonDetail(borrowNid);
				modelAndView.addObject("borrowInfo", borrowInfo);
			}
			// 风控信息
			WebRiskControlCustomize riskControl = this.borrowService.selectRiskControl(borrowNid);
			riskControl.setControlMeasures(riskControl.getControlMeasures()==null?"":riskControl.getControlMeasures().replace("\r\n", ""));
			riskControl.setControlMort(riskControl.getControlMort()==null?"":riskControl.getControlMort().replace("\r\n", ""));
			// 添加风控信息
			modelAndView.addObject("riskControl", riskControl);
			List<WebMortgageCustomize> mortgageList = this.borrowService.selectMortgageList(borrowNid);
			// 添加相应的房产信息
			modelAndView.addObject("mortgageList", mortgageList);
			List<WebVehiclePledgeCustomize> vehiclePledgeList = this.borrowService.selectVehiclePledgeList(borrowNid);
			// 添加相应的汽车抵押信息
			modelAndView.addObject("vehiclePledgeList", vehiclePledgeList);
			// 5查询相应的认证信息
			List<WebProjectAuthenInfoCustomize> authenList = borrowService.searchProjectAuthenInfos(borrowNid);
			modelAndView.addObject("authenList", authenList);
			// 6查询相应的还款计划
			List<BorrowRepayPlanCustomBean> repayPlanList = this.borrowService.getRepayPlan(borrowNid);
			modelAndView.addObject("repayPlanList", repayPlanList);
			// 7 相关文件
			List<BorrowFileCustomBean> files = this.borrowService.searchProjectFiles(borrowNid, CustomConstants.HOST);
			modelAndView.addObject("fileList", files);

		}
		//用户未登陆（默认都是否状态）
		if(userId == null){
			modelAndView.addObject("loginFlag", "0");//登录状态 0未登陆 1已登录
			modelAndView.addObject("openFlag", "0"); //开户状态 0未开户 1已开户
			modelAndView.addObject("investFlag", "0");//是否出借过该项目 0未出借 1已出借
			modelAndView.addObject("riskFlag", "0");//是否进行过风险测评 0未测评 1已测评
			modelAndView.addObject("setPwdFlag", "0");//是否设置过交易密码 0未设置 1已设置
		}else{
			modelAndView.addObject("loginFlag", "1");//登录状态 0未登陆 1已登录
			//用户信息
			Users user = this.borrowService.getUsers(userId);
			if (user.getBankOpenAccount() == 1) {
				modelAndView.addObject("openFlag", "1");
			} else {
				modelAndView.addObject("openFlag", "0");
			}
			// 用户是否出借项目
			int count = this.borrowService.countUserInvest(userId, borrowNid);
			if (count > 0) {
				modelAndView.addObject("investFlag", "1");//是否出借过该项目 0未出借 1已出借
			}else{
				modelAndView.addObject("investFlag", "0");//是否出借过该项目 0未出借 1已出借
			}
			//是否设置交易密码
			if(user.getIsSetPassword() == 1){
				modelAndView.addObject("setPwdFlag", "1");
			}else{
				modelAndView.addObject("setPwdFlag", "0");
			}
			// 出借时风险测评改造 del by liuyang 20180111 start
			// 风险测评标识
			// JSONObject jsonObject = CommonSoaUtils.getUserEvalationResultByUserId(userId + "");
			// modelAndView.addObject("riskFlag", jsonObject.get("userEvaluationResultFlag"));
			// 出借时风险测评改造 del by liuyang 20180111 end
			//账户信息
			Account account = this.borrowService.getAccount(userId);
			String userBalance = account.getBankBalance().toString();
			modelAndView.addObject("userBalance", userBalance);
		}
	}
	/**
	 * 获取新标的相关信息
	 * @param modelAndView
	 * @param borrow
	 */
	private void getProjectDetailNew(ModelAndView modelAndView,WebProjectDetailCustomize borrow,Integer userId){
		//标的号
		String borrowNid = borrow.getBorrowNid();

		/* 项目还款方式 */
		String borrowStyle = borrow.getBorrowStyle();
		// 收益率
		BigDecimal borrowApr = new BigDecimal(borrow.getBorrowApr());
		//可投金额
		if (borrow.getInvestAccount() != null) {
			modelAndView.addObject("InvestAccountInt", new BigDecimal(borrow.getInvestAccount()).intValue());
		}
		// 系统当前时间
		String nowTime = String.valueOf(GetDate.getNowTime10());
		modelAndView.addObject("nowTime", nowTime);
		
		/** 计算最优优惠券开始 pccvip isThereCoupon 1是有最优优惠券，0无最有优惠券 */
		UserCouponConfigCustomize couponConfig = new UserCouponConfigCustomize();
		BigDecimal couponInterest = BigDecimal.ZERO;
		modelAndView.addObject("interest", BigDecimal.ZERO);
		//用户未登陆,不取优惠券
		if(userId == null){
			couponConfig = null;
			modelAndView.addObject("isThereCoupon", 0);//无最优优惠券
			modelAndView.addObject("couponConfig", couponConfig);//优惠券配置信息
			modelAndView.addObject("couponAvailableCount", 0);//可用优惠券张数
		}else{
			// 获取用户最优优惠券
			couponConfig = getBestCoupon(borrowNid, userId, null, CustomConstants.CLIENT_PC); 
			if (couponConfig != null) {
				modelAndView.addObject("isThereCoupon", 1);
				if (couponConfig.getCouponType() == 1) {
					couponInterest = getInterestDj(couponConfig.getCouponQuota(), couponConfig.getCouponProfitTime(), borrowApr);
				} else {
					couponInterest = getInterest(borrowStyle, couponConfig.getCouponType(), borrowApr, couponConfig.getCouponQuota(), "0",
							new Integer(borrow.getBorrowPeriod() == null ? "0" : borrow.getBorrowPeriod()));
				}
				couponConfig.setCouponInterest(CustomConstants.DF_FOR_VIEW.format(couponInterest));
				if(couponConfig!=null && couponConfig.getCouponType()==3){
                    modelAndView.addObject("interest", CustomConstants.DF_FOR_VIEW.format(couponInterest.subtract(couponConfig.getCouponQuota())));
                }else{
                    modelAndView.addObject("interest", CustomConstants.DF_FOR_VIEW.format(couponInterest));
                }
			} else {
				modelAndView.addObject("isThereCoupon", 0);
			}
			modelAndView.addObject("couponConfig", couponConfig);
			/** 可用优惠券张数开始 pccvip */
			String couponAvailableCount = couponService.getUserCouponAvailableCount(borrowNid, userId, "0", "0");
			modelAndView.addObject("couponAvailableCount", couponAvailableCount);
			modelAndView.addObject("borrowMeasuresMea",borrow.getBorrowMeasuresMea());
			/** 可用优惠券张数结束 pccvip */
		}
		/** 计算最优优惠券结束 */

		//收益
		BigDecimal borrowInterest = BigDecimal.ZERO;

		/**
		 * 融通宝收益叠加
		 */
		// mod by nxl 设置项目加息收益 Start
		/*if (borrow.getType().equals("13")) {
			borrowApr = borrowApr.add(new BigDecimal(borrow.getBorrowExtraYield()));
		}*/
		// add by nxl 设置项目加息收益
		BigDecimal borrowExtraYield = new BigDecimal(StringUtils.isNotBlank(borrow.getBorrowExtraYield())?borrow.getBorrowExtraYield():"0");
		int intFlg = Integer.parseInt(StringUtils.isNotBlank(borrow.getIncreaseInterestFlag())?borrow.getIncreaseInterestFlag():"0");
		boolean isIncrease = Validator.isIncrease(intFlg,borrowExtraYield);
		if(isIncrease){
			borrowApr = borrowApr.add(new BigDecimal(borrow.getBorrowExtraYield()));
			borrow.setIncreaseInterestFlag(String.valueOf(isIncrease));
		}
		// mod by nxl 设置项目加息收益 End
		switch (borrowStyle) {
		case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“
			// 计算历史回报
			borrowInterest = DuePrincipalAndInterestUtils
					.getMonthInterest(new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod()))
					.divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“
			borrowInterest = DuePrincipalAndInterestUtils
					.getDayInterest(new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod()))
					.divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：；
			borrowInterest = BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")),
					Integer.parseInt(borrow.getBorrowPeriod()), Integer.parseInt(borrow.getBorrowPeriod())).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：；
			borrowInterest = AverageCapitalPlusInterestUtils
					.getInterestCount(new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod()))
					.divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
			break;
		case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
			borrowInterest = AverageCapitalUtils.getInterestCount(new BigDecimal(borrow.getBorrowAccount()), borrowApr.divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod()))
					.divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
			break;
		default:
			break;
		}

		if (couponConfig != null && couponConfig.getCouponType() == 3) {
			modelAndView.addObject("capitalInterest", CustomConstants.DF_FOR_VIEW.format(borrowInterest.add(couponConfig.getCouponQuota()).subtract(couponInterest)));
		} else if (couponConfig != null && couponConfig.getCouponType() == 1) {
			modelAndView.addObject("capitalInterest", borrowInterest.add(couponInterest));
		} else {
			modelAndView.addObject("capitalInterest", CustomConstants.DF_FOR_VIEW.format(borrowInterest.subtract(couponInterest)));
		}
		
		borrow.setBorrowInterest(borrowInterest.toString());

		if (borrow.getType().equals("9")) {// 如果项目为汇资产项目
			// 添加相应的项目详情信息
			modelAndView.addObject("projectDeatil", borrow);
			// 4查询相应的汇资产的首页信息
			WebHzcProjectDetailCustomize borrowInfo = this.borrowService.searchHzcProjectDetail(borrowNid);
			modelAndView.addObject("borrowInfo", borrowInfo);
			// 处置预案
			WebHzcDisposalPlanCustomize disposalPlan = this.borrowService.searchDisposalPlan(borrowNid);
			modelAndView.addObject("disposalPlan", disposalPlan);
			// 5查询相应的还款计划
			List<BorrowRepayPlanCustomBean> repayPlanList = this.borrowService.getRepayPlan(borrowNid);
			modelAndView.addObject("repayPlanList", repayPlanList);
			// 相关文件
			List<BorrowFileCustomBean> files = this.borrowService.searchProjectFiles(borrowNid, CustomConstants.HOST);
			modelAndView.addObject("fileList", files);
			/**
			 * 借款类型  1、企业借款 2、借款人  3、汇资产
			 */
			modelAndView.addObject("borrowType", "3");
			
		} else {// 项目为非汇资产项目
			// 添加相应的项目详情信息
			modelAndView.addObject("projectDeatil", borrow);
			/**
			 * 借款类型  1、企业借款 2、借款人  3、汇资产
			 */
			modelAndView.addObject("borrowType", borrow.getComOrPer());
			//借款人企业信息
			BorrowUsers borrowUsers = borrowService.getBorrowUsersByNid(borrowNid);
			//借款人信息
			BorrowManinfo borrowManinfo = borrowService.getBorrowManinfoByNid(borrowNid);
			//房产抵押信息
			List<BorrowHouses> borrowHousesList = borrowService.getBorrowHousesByNid(borrowNid);
			//车辆抵押信息
			List<BorrowCarinfo> borrowCarinfoList = borrowService.getBorrowCarinfoByNid(borrowNid);
			//还款计划
			List<BorrowRepayPlanCustomBean> repayPlanList = this.borrowService.getRepayPlan(borrowNid);
			modelAndView.addObject("repayPlanList", repayPlanList);
			//相关文件
			List<BorrowFileCustomBean> files = this.borrowService.searchProjectFiles(borrowNid, CustomConstants.HOST);
			modelAndView.addObject("fileList", files);
			// 还款信息
			BorrowRepay borrowRepay = borrowService.getBorrowRepay(borrowNid);
			//资产列表
			JSONArray json = new JSONArray();
			//基础信息
			String baseTableData = "";
			//资产信息
			String assetsTableData = "";
			//项目介绍
			String intrTableData = "";
			//信用状况
			String credTableData = "";
			//审核信息
			String reviewTableData = "";
			//其他信息
			String otherTableData = "";
			//借款类型
			int borrowType = Integer.parseInt(borrow.getComOrPer());

			if(borrowType == 1 && borrowUsers != null){
				//基础信息
				baseTableData = JSONObject.toJSONString(packDetail(borrowUsers, 1, borrowType, borrow.getBorrowLevel()));
				//信用状况
				credTableData = JSONObject.toJSONString(packDetail(borrowUsers, 4, borrowType, borrow.getBorrowLevel()));
				//审核信息
				reviewTableData =  JSONObject.toJSONString(packDetail(borrowUsers, 5, borrowType, borrow.getBorrowLevel()));
				//其他信息
				otherTableData =  JSONObject.toJSONString(packDetail(borrowUsers, 6, borrowType, borrow.getBorrowLevel()));
			}else{
				if(borrowManinfo != null){
					//基础信息
					baseTableData = JSONObject.toJSONString(packDetail(borrowManinfo, 1, borrowType, borrow.getBorrowLevel()));
					//信用状况
					credTableData = JSONObject.toJSONString(packDetail(borrowManinfo, 4, borrowType, borrow.getBorrowLevel()));
					//审核信息
					reviewTableData = JSONObject.toJSONString(packDetail(borrowManinfo, 5, borrowType, borrow.getBorrowLevel()));
					
					//其他信息
					otherTableData =  JSONObject.toJSONString(packDetail(borrowManinfo, 6, borrowType, borrow.getBorrowLevel()));
				}
			}
			//资产信息
			if(borrowHousesList != null && borrowHousesList.size() > 0){
			    for (BorrowHouses  borrowHouses: borrowHousesList) {
			        json.add(packDetail(borrowHouses, 2, borrowType, borrow.getBorrowLevel())); 
                }
			}
			if(borrowCarinfoList != null && borrowCarinfoList.size() > 0){
				for (BorrowCarinfo borrowCarinfo : borrowCarinfoList) {
					json.add(packDetail(borrowCarinfo, 2, borrowType, borrow.getBorrowLevel()));
				}
			}
			assetsTableData = json.toString();
			//项目介绍
			intrTableData = JSONObject.toJSONString(packDetail(borrow, 3, borrowType, borrow.getBorrowLevel()));

			//基础信息
			modelAndView.addObject("baseTableData", baseTableData);
			//资产信息
			modelAndView.addObject("assetsTableData", assetsTableData);
			//项目介绍
			modelAndView.addObject("intrTableData", intrTableData);
			//信用状况
			modelAndView.addObject("credTableData", credTableData);
			//审核信息
			modelAndView.addObject("reviewTableData", reviewTableData);
			// 信批需求新增(放款后才显示)
			if(borrow.getStatusOrginal()>=4 && borrowRepay != null){
				//其他信息
				modelAndView.addObject("otherTableData", otherTableData);
				modelAndView.addObject("updateTime", getUpdateTime(Integer.parseInt(borrowRepay.getAddtime()), StringUtils.isBlank(borrowRepay.getRepayYestime())?null:Integer.parseInt(borrowRepay.getRepayYestime())));
			}else{
				//其他信息
				modelAndView.addObject("otherTableData", JSONObject.toJSONString(new ArrayList<BorrowDetailBean>()));
			}
			
		}
		//用户未登陆（默认都是否状态）
		if(userId == null){
			modelAndView.addObject("loginFlag", "0");//登录状态 0未登陆 1已登录
			modelAndView.addObject("openFlag", "0"); //开户状态 0未开户 1已开户
			modelAndView.addObject("investFlag", "0");//是否出借过该项目 0未出借 1已出借
			modelAndView.addObject("riskFlag", "0");//是否进行过风险测评 0未测评 1已测评
			modelAndView.addObject("setPwdFlag", "0");//是否设置过交易密码 0未设置 1已设置
		}else{
			modelAndView.addObject("loginFlag", "1");//登录状态 0未登陆 1已登录
			//用户信息
			Users user = this.borrowService.getUsers(userId);
			if (user.getBankOpenAccount() == 1) {
				modelAndView.addObject("openFlag", "1");
			} else {
				modelAndView.addObject("openFlag", "0");
			}
			// 用户是否出借项目
			int count = this.borrowService.countUserInvest(userId, borrowNid);
			if (count > 0) {
				modelAndView.addObject("investFlag", "1");//是否出借过该项目 0未出借 1已出借
			}else{
				modelAndView.addObject("investFlag", "0");//是否出借过该项目 0未出借 1已出借
			}
			//是否设置交易密码
			if(user.getIsSetPassword() == 1){
				modelAndView.addObject("setPwdFlag", "1");
			}else{
				modelAndView.addObject("setPwdFlag", "0");
			}
			// 出借时风险测评改造 del by liuyang 20180111 start
			// 风险测评标识
			// JSONObject jsonObject = CommonSoaUtils.getUserEvalationResultByUserId(userId + "");
			// modelAndView.addObject("riskFlag", jsonObject.get("userEvaluationResultFlag"));
			// 出借时风险测评改造 del by liuyang 20180111 end
			//账户信息
			Account account = this.borrowService.getAccount(userId);
			String userBalance = account.getBankBalance().toString();
			modelAndView.addObject("userBalance", userBalance);
		}
	}
	
	/**
	 * 计算更新时间
	 * @param timeLoan
	 * @param timeRepay
	 * @return
	 */
	public static String getUpdateTime(Integer timeLoan, Integer timeRepay){
		if(timeLoan == null){
			return "";
		}
		
		Integer timeCurr = GetDate.getNowTime10();
		if(timeRepay != null && timeCurr > timeRepay){
			timeCurr = timeRepay;
		}
		
		Integer timeDiff = timeCurr - timeLoan;
		Integer timeDiffMonth = timeDiff/(60*60*24*31);
		
		Calendar timeLoanCal = Calendar.getInstance();
		timeLoanCal.setTimeInMillis(timeLoan * 1000L);
		
		if(timeDiffMonth >= 1){
			timeLoanCal.add(Calendar.MONTH, timeDiffMonth);
		}
		
		return GetDate.formatDate(timeLoanCal);
	}

	public static void main(String[] args) {
		System.out.println(getUpdateTime(1515136012, 1519833600));
	}
	
	private BigDecimal getInterestDj(BigDecimal couponQuota, Integer couponProfitTime, BigDecimal borrowApr) {
		BigDecimal earnings = new BigDecimal("0");

		earnings = couponQuota.multiply(borrowApr.divide(new BigDecimal(100), 6, BigDecimal.ROUND_HALF_UP)).divide(new BigDecimal(365), 6, BigDecimal.ROUND_HALF_UP)
				.multiply(new BigDecimal(couponProfitTime)).setScale(2, BigDecimal.ROUND_DOWN);

		return earnings;

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
		case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
			earnings = AverageCapitalUtils.getInterestCount(accountDecimal, borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
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
	 * 查询相应的项目的出借列表
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = BorrowDefine.BORROW_INVEST_ACTION, produces = "application/json; charset=utf-8")
	public BorrowInvestListAjaxBean searchProjectInvestList(@ModelAttribute BorrowInvestBean form, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(BorrowDefine.THIS_CLASS, BorrowDefine.BORROW_INVEST_ACTION);
		BorrowInvestListAjaxBean result = new BorrowInvestListAjaxBean();
		this.createProjectInvestPage(result, form);

		result.success();
		result.setHost(BorrowDefine.HOST);
		LogUtil.endLog(BorrowDefine.THIS_CLASS, BorrowDefine.BORROW_INVEST_ACTION);
		return result;
	}

	/**
	 * 创建用户出借记录分页查询
	 * 
	 * @param result
	 * @param form
	 */
	private void createProjectInvestPage(BorrowInvestListAjaxBean result, BorrowInvestBean form) {

		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		Borrow borrow = this.borrowService.getBorrowByNid(form.getBorrowNid());
		if (borrow != null) {
			result.setInvestTotal(df.format(borrow.getBorrowAccountYes()));
			result.setInvestTimes(String.valueOf(borrow.getTenderTimes()));
		} else {
			result.setInvestTotal(df.format(new BigDecimal("0")));
			result.setInvestTimes("0");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", form.getBorrowNid());
		int recordTotal = this.borrowService.countProjectInvestRecordTotal(params);
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
			List<WebProjectInvestListCustomize> recordList = borrowService.searchProjectInvestList(params);
			List<ActdecListedOne> alo = this.borrowService.getActdecList(form.getBorrowNid());
			if(!alo.isEmpty()) {
				if(form.getPaginatorPage() == 1) {
					WebProjectInvestListCustomize all=recordList.get(0);
					all.setRedbag("1");
					recordList.set(0, all);
				}
				if((form.getPaginatorPage() * limit) > recordTotal){
					WebProjectInvestListCustomize all=recordList.get(recordList.size()-1);
					all.setRedbag("1");
					recordList.set(recordList.size()-1, all);
				}

			}
			result.setProjectInvestList(recordList);
			result.setPaginator(paginator);
		} else {
			result.setProjectInvestList(new ArrayList<WebProjectInvestListCustomize>());
			result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
		}
	}
	/***
	 * 查询相应的汇消费项目的打包数据
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = BorrowDefine.BORROW_CONSUME_ACTION, produces = "application/json; charset=utf-8")
	public BorrowConsumeListAjaxBean searchProjectConsumeList(@ModelAttribute BorrowConsumeBean form,
			HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(BorrowDefine.THIS_CLASS, BorrowDefine.BORROW_CONSUME_ACTION);
		BorrowConsumeListAjaxBean result = new BorrowConsumeListAjaxBean();
		this.createProjectConsumePage(result, form);
		result.success();
		result.setHost(BorrowDefine.HOST);
		LogUtil.endLog(BorrowDefine.THIS_CLASS, BorrowDefine.BORROW_CONSUME_ACTION);
		return result;
	}

	/**
	 * 创建相应的汇消费项目的打包信息
	 * 
	 * @param result
	 * @param form
	 */

	private void createProjectConsumePage(BorrowConsumeListAjaxBean result, BorrowConsumeBean form) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", form.getBorrowNid());
		int recordTotal = this.borrowService.countProjectConsumeRecordTotal(params);
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
			List<WebProjectConsumeCustomize> recordList = borrowService.searchProjectConsumeList(params);
			result.setProjectConsumeList(recordList);
			result.setPaginator(paginator);
		} else {
			// 添加相应的汇消费债券信息
			result.setProjectConsumeList(new ArrayList<WebProjectConsumeCustomize>());
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
	@RequestMapping(value = BorrowDefine.BORROW_STATUS_ACTION)
	public BorrowStatusAjaxBean searchProjectStatus(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(BorrowDefine.THIS_CLASS, BorrowDefine.BORROW_STATUS_ACTION);
		// 1.获取项目标号
		String borrowNid = request.getParameter("borrowNid");
		// 2.根据项目标号获取相应的项目信息
		WebProjectDetailCustomize borrow = this.borrowService.selectProjectDetail(borrowNid);
		BorrowStatusAjaxBean projectStatus = new BorrowStatusAjaxBean();
		if (borrow == null) {
			projectStatus.setStatus(false);
			projectStatus.setMessage("标不存在");
		} else {
			projectStatus.setProjectStatus(borrow.getStatus());
			projectStatus.setStatus(true);
			projectStatus.setMessage("标的状态查询成功");
		}
		LogUtil.endLog(BorrowDefine.THIS_CLASS, BorrowDefine.BORROW_STATUS_ACTION);
		return projectStatus;

	}

	private UserCouponConfigCustomize getBestCoupon(String borrowNid, Integer userId, String money, String platform) {
		if(StringUtils.isBlank(money)){
			money = "0";
		}
		return borrowService.getBestCoupon(borrowNid, userId, money, platform);
	}

	/**
	 * 融通宝介绍页跳转
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping(value = BorrowDefine.RTB_INTR_ACTION)
	public ModelAndView rtbIntr(@ModelAttribute BorrowInvestBean form, HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView(BorrowDefine.BORROW_RTBINIT_PTAH);
	}
	
	/**
	 * 封装项目详情页
	 * @param objBean
	 * @param type  1 基础信息 2资产信息 3项目介绍 4信用状况 5审核状况
	 * @param borrowType  1借款人 2企业借款
	 * @param borrowLevel  信用评级
	 * @return
	 */
	private List<BorrowDetailBean> packDetail(Object objBean,int type,int borrowType, String borrowLevel){
		List<BorrowDetailBean> detailBeanList = new ArrayList<BorrowDetailBean>();
		String currencyName = "元";
		// 得到对象
		Class c = objBean.getClass();
		// 得到方法
		Field fieldlist[] = c.getDeclaredFields();
		for (int i = 0; i < fieldlist.length; i++) {
			// 获取类属性
			Field f = fieldlist[i];
			// 得到方法名
			String fName = f.getName();
			try {
				// 参数方法获取
				String paramName = fName.substring(0, 1).toUpperCase() + fName.substring(1, fName.length());
				// 取得结果
				Method getMethod = c.getMethod(BankCallConstant.GET + paramName);
				if (getMethod != null) {
					Object result = getMethod.invoke(objBean);
					// 结果不为空时
					if (Validator.isNotNull(result)) {
						//封装bean
						BorrowDetailBean detailBean = new BorrowDetailBean();
						detailBean.setId(fName);
						detailBean.setVal(result.toString());
						if(type == 1){
							if(borrowType == 2){//个人借款
								switch (fName) {
								case "name":
									detailBean.setKey("姓名");
									//数据脱敏
									detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), 1, 2));
									detailBeanList.add(detailBean);
									break;
								case "cardNo":
									detailBean.setKey("身份证号");
									//数据脱敏
									detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), 4, 10));
									detailBeanList.add(detailBean);
									break;
								case "sex":
									detailBean.setKey("性别");
									if("1".equals(result.toString())){
										detailBean.setVal("男");
									}else{
										detailBean.setVal("女");
									}
									detailBeanList.add(detailBean);
									break;
								case "old":
									if(!"0".equals(detailBean.getVal())){
										detailBean.setKey("年龄");
										detailBeanList.add(detailBean);
									}
									break;
                                case "merry":
                                    if(!("0".equals(result.toString()) || result.toString()==null)){
                                        detailBean.setKey("婚姻状况");
                                        if("1".equals(result.toString())){
                                            detailBean.setVal("已婚");
                                        }else if("2".equals(result.toString())) {
                                            detailBean.setVal("未婚");
                                        }else if("3".equals(result.toString())) {
                                            detailBean.setVal("离异");
                                        }else if("4".equals(result.toString())) {
                                            detailBean.setVal("丧偶");
                                        }
                                        detailBeanList.add(detailBean);
                                    }
                                    break; 		
								case "city":
									detailBean.setKey("工作城市");
									detailBeanList.add(detailBean);
									break;						
								case "domicile":
									detailBean.setKey("户籍地");
									detailBeanList.add(detailBean);
									break;	
								case "position":
									detailBean.setKey("岗位职业");
									detailBeanList.add(detailBean);
									break;
								case "annualIncome":
									detailBean.setKey("年收入");
									detailBeanList.add(detailBean);
									break;
								case "overdueReport":
									detailBean.setKey("征信报告逾期情况");
									detailBeanList.add(detailBean);
									break;
								case "debtSituation":
									detailBean.setKey("重大负债状况");
									detailBeanList.add(detailBean);
									break;
								case "otherBorrowed":
									detailBean.setKey("其他平台借款情况");
									detailBeanList.add(detailBean);
									break;
								default:
									break;
								}
							}else{//企业借款
							    
								switch (fName) {
								case "currencyName":
								    currencyName = detailBean.getVal();
                                    break;
								case "username":
									detailBean.setKey("借款主体");
									detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(),detailBean.getVal().length()-2));
									detailBeanList.add(detailBean);
									break;
								case "city":
									detailBean.setKey("注册地区");
									detailBeanList.add(detailBean);
									break;
								case "regCaptial":
									detailBean.setKey("注册资本");
									if(StringUtils.isNotBlank(detailBean.getVal())){
										detailBean.setVal(CustomConstants.DF_FOR_VIEW.format(new BigDecimal(detailBean.getVal())) + currencyName);
									}
									detailBeanList.add(detailBean);
									break;
								case "comRegTime":
									detailBean.setKey("注册时间");
									detailBeanList.add(detailBean);
									break;						
								case "socialCreditCode":
									detailBean.setKey("统一社会信用代码");
									//数据脱敏
									detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), 4, 10));
									detailBeanList.add(detailBean);
									break;	
								case "registCode":
									detailBean.setKey("注册号");
									//数据脱敏
									detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), 4, 10));
									detailBeanList.add(detailBean);
									break;						
								case "legalPerson":
									detailBean.setKey("法定代表人");
									//数据脱敏
									detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), 1, 2));
									detailBeanList.add(detailBean);
									break;	
								case "industry":
									detailBean.setKey("所属行业");
									detailBeanList.add(detailBean);
									break;							
								case "mainBusiness":
									detailBean.setKey("主营业务");
									detailBeanList.add(detailBean);
									break;				
								case "overdueReport":
									detailBean.setKey("征信报告逾期情况");
									detailBeanList.add(detailBean);
									break;
								case "debtSituation":
									detailBean.setKey("重大负债状况");
									detailBeanList.add(detailBean);
									break;
								case "otherBorrowed":
									detailBean.setKey("其他平台借款情况");
									detailBeanList.add(detailBean);
									break;
								default:
									break;
								}
							}
						}else if(type == 2){
							switch (fName) {
							case "housesType":
								detailBean.setKey("资产类型");
								String houseType = this.borrowService.getParamName("HOUSES_TYPE", detailBean.getVal());
								if(houseType != null){
									 detailBean.setVal(houseType);
								}else{
									detailBean.setVal("住宅");
								}
								detailBeanList.add(detailBean);
								break;
							case "housesArea":
								detailBean.setKey("资产面积");
								detailBean.setVal(detailBean.getVal() + "m<sup>2</sup>");
								detailBeanList.add(detailBean);
								break;
							case "housesCnt":
								detailBean.setKey("资产数量");
								detailBeanList.add(detailBean);
								break;
							case "housesToprice":
								detailBean.setKey("评估价值");
								if(StringUtils.isNotBlank(detailBean.getVal())){
									detailBean.setVal(CustomConstants.DF_FOR_VIEW.format(new BigDecimal(detailBean.getVal())) + "元");
								}
								detailBeanList.add(detailBean);
								break;						
							case "housesBelong":
								detailBean.setKey("资产所属");
								detailBeanList.add(detailBean);
								break;	
							//车辆
							case "brand":
								BorrowDetailBean carBean = new BorrowDetailBean();
								carBean.setId("carType");
								carBean.setKey("资产类型");
								carBean.setVal("车辆");
								detailBeanList.add(carBean);
								detailBean.setKey("品牌");
								detailBeanList.add(detailBean);
								break;	
	
							case "model":
								detailBean.setKey("型号");
								detailBeanList.add(detailBean);
								break;	
							case "place":
								detailBean.setKey("产地");
								detailBeanList.add(detailBean);
								break;	
							case "price":
								detailBean.setKey("购买价格");
								if(StringUtils.isNotBlank(detailBean.getVal())){
									detailBean.setVal(CustomConstants.DF_FOR_VIEW.format(new BigDecimal(detailBean.getVal())) + "元");
								}
								detailBeanList.add(detailBean);
								break;	
							case "toprice":
								detailBean.setKey("评估价值");
								if(StringUtils.isNotBlank(detailBean.getVal())){
									detailBean.setVal(CustomConstants.DF_FOR_VIEW.format(new BigDecimal(detailBean.getVal())) + "元");
								}
								detailBeanList.add(detailBean);
								break;								
							case "number":
								detailBean.setKey("车牌号");
								//数据脱敏
								detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), 2, 4));
								detailBeanList.add(detailBean);
								break;								
							case "registration":
								detailBean.setKey("车辆登记地");
								detailBeanList.add(detailBean);
								break;								
							case "vin":
								detailBean.setKey("车架号");
								//数据脱敏
								detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), 4, 5));
								detailBeanList.add(detailBean);
								break;								
							default:
								break;
							}
							
						}else if(type == 3){
							switch (fName) {
							case "borrowContents": 
								detailBean.setKey("项目信息");
								detailBeanList.add(detailBean);
								break;
							case "fianceCondition":
								detailBean.setKey("财务状况 ");
								detailBeanList.add(detailBean);
								break;
							case "financePurpose":
								detailBean.setKey("借款用途");
								detailBeanList.add(detailBean);
								break;
							case "monthlyIncome":
								detailBean.setKey("月薪收入");
								if(StringUtils.isNotBlank(detailBean.getVal())){
									detailBean.setVal(detailBean.getVal());
								}
								detailBeanList.add(detailBean);
								break;						
							case "payment":
								detailBean.setKey("还款来源");
								detailBeanList.add(detailBean);
								break;	
							case "firstPayment":
								detailBean.setKey("第一还款来源");
								detailBeanList.add(detailBean);
								break;						
							case "secondPayment"://还没有
								detailBean.setKey("第二还款来源");
								detailBeanList.add(detailBean);
								break;	
							case "costIntrodution":
								detailBean.setKey("费用说明");
								detailBeanList.add(detailBean);
								break;	
							default:
								break;
							}
						}else if(type == 4){
							switch (fName) {
							case "overdueTimes":
								detailBean.setKey("在平台逾期次数");
								detailBeanList.add(detailBean);
								break;
							case "overdueAmount":
								detailBean.setKey("在平台逾期金额");
								if(StringUtils.isNotBlank(detailBean.getVal())){
									detailBean.setVal(CustomConstants.DF_FOR_VIEW.format(new BigDecimal(detailBean.getVal())) + "元");
								}
								detailBeanList.add(detailBean);
								break;
							case "litigation":
								detailBean.setKey("涉诉情况");
								detailBeanList.add(detailBean);
								break;
							default:
								break;
							}
						}else if(type == 5){
							if(borrowType == 2){
								switch (fName) {
								case "isCard":
									detailBean.setKey("身份证");
									if("1".equals(result.toString())){
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;
								case "isIncome":
									detailBean.setKey("收入状况");
									if("1".equals(result.toString())){
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;
								case "isCredit":
									detailBean.setKey("信用状况");
									if("1".equals(result.toString())){
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;
								case "isAsset":
									detailBean.setKey("资产状况");
									if("1".equals(result.toString())){
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;
								case "isVehicle":
									detailBean.setKey("车辆状况");
									if("1".equals(result.toString())){
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;								
								case "isDrivingLicense":
									detailBean.setKey("行驶证");
									if("1".equals(result.toString())){
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;								
								case "isVehicleRegistration":
									detailBean.setKey("车辆登记证");
									if("1".equals(result.toString())){
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;								
								case "isMerry":
									detailBean.setKey("婚姻状况");
									if("1".equals(result.toString())){
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;								
								case "isWork":
									detailBean.setKey("工作状况");
									if("1".equals(result.toString())){
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;	
								case "isAccountBook":
									detailBean.setKey("户口本");
									if("1".equals(result.toString())){
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;									
								default:
									break;
								}
							} else{
									switch (fName) {
									case "isCertificate":
										detailBean.setKey("企业证件");
										if("1".equals(result.toString())){
											detailBean.setVal("已审核");
											detailBeanList.add(detailBean);
										}
										break;
									case "isOperation":
										detailBean.setKey("经营状况");
										if("1".equals(result.toString())){
											detailBean.setVal("已审核");
											detailBeanList.add(detailBean);
										}
										break;
									case "isFinance":
										detailBean.setKey("财务状况");
										if("1".equals(result.toString())){
											detailBean.setVal("已审核");
											detailBeanList.add(detailBean);
										}
										break;
									case "isEnterpriseCreidt":
										detailBean.setKey("企业信用");
										if("1".equals(result.toString())){
											detailBean.setVal("已审核");
											detailBeanList.add(detailBean);
										}
										break;
									case "isLegalPerson":
										detailBean.setKey("法人信息");
										if("1".equals(result.toString())){
											detailBean.setVal("已审核");
											detailBeanList.add(detailBean);
										}
										break;								
									case "isAsset":
										detailBean.setKey("资产状况");
										if("1".equals(result.toString())){
											detailBean.setVal("已审核");
											detailBeanList.add(detailBean);
										}
										break;								
									case "isPurchaseContract":
										detailBean.setKey("购销合同");
										if("1".equals(result.toString())){
											detailBean.setVal("已审核");
											detailBeanList.add(detailBean);
										}
										break;								
									case "isSupplyContract":
										detailBean.setKey("供销合同");
										if("1".equals(result.toString())){
											detailBean.setVal("已审核");
											detailBeanList.add(detailBean);
										}
										break;								
									default:
										break;
								}
							}
						}else if(type == 6){
							switch (fName) {
							case "isFunds":
								detailBean.setKey("借款资金运用情况");
								detailBeanList.add(detailBean);
								break;
							case "isManaged":
								detailBean.setKey("借款人经营状况及财务状况");
								detailBeanList.add(detailBean);
								break;
							case "isAbility":
								detailBean.setKey("借款人还款能力变化情况");
								detailBeanList.add(detailBean);
								break;
							case "isOverdue":
								detailBean.setKey("借款人逾期情况");
								detailBeanList.add(detailBean);
								break;
							case "isComplaint":
								detailBean.setKey("借款人涉诉情况");
								detailBeanList.add(detailBean);
								break;								
							case "isPunished":
								detailBean.setKey("借款人受行政处罚情况");
								detailBeanList.add(detailBean);
								break;								
							default:
								break;
							}
						}
					}
				}

			} catch (Exception e) {
				continue;
			}
		}
		if(type == 1 || type == 4){
			//信用评级单独封装
			BorrowDetailBean detailBean = new BorrowDetailBean();
			detailBean.setId("borrowLevel");
			detailBean.setKey("信用评级");
			detailBean.setVal(borrowLevel);
			detailBeanList.add(detailBean);
		}
		return detailBeanList;
	}

	/**
	 * 查询相应的项目详情
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = BorrowDefine.BORROW_ONTIME_ACTION)
	@ResponseBody
	public OntimeCheckBean ontimeBorrow(HttpServletRequest request, HttpServletResponse response, @PathVariable("borrowNid") String borrowNid) {
		_log.info("查询定时发标详情开始。。。");
		LogUtil.startLog(BorrowDefine.THIS_CLASS, BorrowDefine.BORROW_ONTIME_ACTION);
		OntimeCheckBean ontimeCheckBean = new OntimeCheckBean();
		
		//定时时间为空，定时锁不为空，redis的标的定时状态 = 0,开标成功
//		if (ontime == null && StringUtils.isNotEmpty(lock) && status.equals("0")) {
		String status = RedisUtils.get(borrowNid+CustomConstants.UNDERLINE+
				CustomConstants.REDIS_KEY_ONTIME_STATUS);
		if (status != null && status.equals("0")) {
			ontimeCheckBean.setStatus(0);
			return ontimeCheckBean;
		}
		
		//redis的标的定时时间
		Integer ontime;
		if (StringUtils.isEmpty(RedisUtils.get(borrowNid+CustomConstants.UNDERLINE+
				CustomConstants.REDIS_KEY_ONTIME))){
			ontime = null;
		}else{
			ontime = Integer.valueOf(RedisUtils.get(borrowNid+CustomConstants.UNDERLINE+
					CustomConstants.REDIS_KEY_ONTIME));
		}

		//Redis中取得定时时间,时间check
		if (ontime != null){
			//取得服务器时间
			Integer nowtime = GetDate.getNowTime10();
			//未到发标时间时，失败返回发标时间
			if (nowtime < ontime) {
				ontimeCheckBean.setStatus(-1);
				ontimeCheckBean.setStatusInfo("未到发标时间！");
				ontimeCheckBean.setOntime(ontime);
				ontimeCheckBean.setNowtime(GetDate.getNowTime10());
				return ontimeCheckBean;
			}else{
				//到时删除Redis中的定时时间
				RedisUtils.del(borrowNid+CustomConstants.UNDERLINE+
						CustomConstants.REDIS_KEY_ONTIME);
			}
		}

		try {
			//修改标的状态被占用(有效期10秒)
			if (!RedisUtils.tranactionSet(borrowNid+CustomConstants.UNDERLINE+
					CustomConstants.REDIS_KEY_ONTIME_LOCK, 10)){
				ontimeCheckBean.setStatus(-2);
				ontimeCheckBean.setStatusInfo("锁等待中！");
				ontimeCheckBean.setOntime(ontime);
				ontimeCheckBean.setNowtime(GetDate.getNowTime10());
				return ontimeCheckBean;
			}
			
			//设定 redis的标的定时状态 为 1 锁定更改中(有效期同batch执行周期，5分钟)
			RedisUtils.set(borrowNid+CustomConstants.UNDERLINE+
					CustomConstants.REDIS_KEY_ONTIME_STATUS, "1", 300);
			
			//该标的非自动发标标的或者未到发标时间(DB验证)
			Integer id = this.borrowService.getOntimeIdByNid(borrowNid, GetDate.getNowTime10());
			if (id == null) {
				//删除 redis的标的定时独占锁
				RedisUtils.del(borrowNid+CustomConstants.UNDERLINE+
						CustomConstants.REDIS_KEY_ONTIME_LOCK);
				ontimeCheckBean.setStatus(-3);
				ontimeCheckBean.setStatusInfo("该标的非自动发标标的或者未到发标时间！");
				ontimeCheckBean.setOntime(ontime);
				ontimeCheckBean.setNowtime(GetDate.getNowTime10());
				return ontimeCheckBean;
			}
			
            // Redis的出借余额校验
            if (RedisUtils.get(borrowNid) != null) {
                _log.error(borrowNid + " 定时发标异常：标的编号在redis已经存在");
				ontimeCheckBean.setStatus(-5);
				ontimeCheckBean.setStatusInfo("定时标的状态异常");
				ontimeCheckBean.setOntime(ontime);
				ontimeCheckBean.setNowtime(GetDate.getNowTime10());
				return ontimeCheckBean;
            }
			
			//修改标的状态发标
			_log.info("修改数据库中标的发标状态");
			boolean flag = this.borrowService.updateOntimeSendBorrow(id);
			if (!flag) {
				_log.error(BorrowDefine.THIS_CLASS, BorrowDefine.BORROW_ONTIME_ACTION, 
						"标的自动发标失败！（页面触发）" + "[借款编号：" + borrowNid + "]");
				//删除 redis的标的定时独占锁
				RedisUtils.del(borrowNid+CustomConstants.UNDERLINE+
						CustomConstants.REDIS_KEY_ONTIME_LOCK);
				ontimeCheckBean.setStatus(-4);
				ontimeCheckBean.setStatusInfo("定时标的状态修改失败！");
				ontimeCheckBean.setOntime(ontime);
				ontimeCheckBean.setNowtime(GetDate.getNowTime10());
				return ontimeCheckBean;
			}
			_log.info("定时标的【" + borrowNid + "】发标完成。（web）");
			
			//设定  redis的标的定时状态 为 0 标的状态修改成功开标(有效期同batch执行周期，5分钟)
			RedisUtils.set(borrowNid+CustomConstants.UNDERLINE+
					CustomConstants.REDIS_KEY_ONTIME_STATUS,"0",300);
			
			//删除 redis的标的定时独占锁
			RedisUtils.del(borrowNid+CustomConstants.UNDERLINE+
					CustomConstants.REDIS_KEY_ONTIME_LOCK);
			
			ontimeCheckBean.setStatus(0);
			LogUtil.endLog(BorrowDefine.THIS_CLASS, BorrowDefine.BORROW_ONTIME_ACTION);
		} catch (Exception e) {
			//删除 redis的标的定时独占锁
			RedisUtils.del(borrowNid+CustomConstants.UNDERLINE+
					CustomConstants.REDIS_KEY_ONTIME_LOCK);
			_log.error(BorrowDefine.THIS_CLASS, BorrowDefine.BORROW_ONTIME_ACTION, e);
			ontimeCheckBean.setStatus(-5);
			ontimeCheckBean.setStatusInfo("未知异常！");
			ontimeCheckBean.setOntime(ontime);
			ontimeCheckBean.setNowtime(GetDate.getNowTime10());
			LogUtil.infoLog(BorrowDefine.THIS_CLASS, BorrowDefine.BORROW_ONTIME_ACTION, 
					"未知异常！（页面触发）" + "[借款编号：" + borrowNid + "]");
			return ontimeCheckBean;
		}
		return ontimeCheckBean;
	}
}
