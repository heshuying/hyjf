/**
 * Description:汇添金相关的计划列表
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年11月11日 下午2:17:31
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.web.plan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.bank.service.evalation.EvaluationService;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.EvaluationConfig;
import com.hyjf.mybatis.model.customize.AccountDetailCustomize;
import com.hyjf.web.bank.web.user.tender.TenderController;
import com.hyjf.web.bank.web.user.tender.TenderDefine;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanAccedeCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanDetailCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanIntroduceCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanInvestDataCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanQuestionCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanRiskControlCustomize;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.WebBaseAjaxResultBean;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.coupon.CouponDefine;
import com.hyjf.web.user.invest.InvestDefine;
import com.hyjf.web.user.invest.InvestInfoAjaxBean;
import com.hyjf.web.user.invest.InvestServiceImpl;
import com.hyjf.web.util.WebUtils;
import com.hyjf.web.vip.apply.ApplyDefine;
import com.hyjf.web.vip.manage.VIPManageDefine;

@Controller
@RequestMapping(value = PlanDefine.REQUEST_MAPPING)
public class PlanController extends BaseController {

	@Autowired
	private PlanService planService;

	@Autowired
	private EvaluationService evaluationService;

	public static JedisPool pool = RedisUtils.getPool();
	/** 发布地址 */
	private static String HOST_URL = PropUtils.getSystem("hyjf.web.host").trim();

	DecimalFormat df = CustomConstants.DF_FOR_VIEW;

	/**
	 * 初始化项目列表画面
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = PlanDefine.INIT_PLAN_LIST_ACTION)
	public ModelAndView initPlanList(HttpServletRequest request, HttpServletResponse response, @ModelAttribute PlanBean form) {

		LogUtil.startLog(PlanDefine.THIS_CLASS, PlanDefine.INIT_PLAN_LIST_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanDefine.PLAN_LIST_PTAH);
		// 查两天汇添金记录
		List<DebtPlanCustomize> htjRecordList = this.getHtjRecordList();
		modelAndView.addObject("htjRecordList", htjRecordList);
		// 2.汇添金出借数据
		DebtPlanInvestDataCustomize htjInvestData = this.planService.searchInvestData();

		/*金额总数*/
		if (StringUtils.isNotBlank(htjInvestData.getTotalAmount())) {
			BigDecimal totalAmount = new BigDecimal(htjInvestData.getTotalAmount().replace(",", ""));
		    //出借总额(万元)
			htjInvestData.setTotalAmount(totalAmount.divide(new BigDecimal("10000")).setScale(2, BigDecimal.ROUND_DOWN).toString());
		} else {
			htjInvestData.setTotalAmount("0.00");
		}
		/*赚取总额*/
		if (StringUtils.isNotBlank(htjInvestData.getTotalEarnAmount())) {
			BigDecimal totalEarnAmount = new BigDecimal(htjInvestData.getTotalEarnAmount().replace(",", ""));
			//赚取总额(万元)
			htjInvestData.setTotalEarnAmount(totalEarnAmount.divide(new BigDecimal("10000")).setScale(2, BigDecimal.ROUND_DOWN).toString());
		} else {
			htjInvestData.setTotalEarnAmount("0.00");
		}
		modelAndView.addObject("htjInvestData", htjInvestData);
		LogUtil.endLog(PlanDefine.THIS_CLASS, PlanDefine.INIT_PLAN_LIST_ACTION);

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
	@RequestMapping(value = PlanDefine.PLAN_LIST_ACTION, produces = "application/json; charset=utf-8")
	public PlanListAJaxBean searchPlanList(@ModelAttribute PlanBean form, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(PlanDefine.THIS_CLASS, PlanDefine.PLAN_LIST_ACTION);
		PlanListAJaxBean result = new PlanListAJaxBean();
		this.createPlanListPage(result, form);
		result.success();
		LogUtil.endLog(PlanDefine.THIS_CLASS, PlanDefine.PLAN_LIST_ACTION);
		return result;
	}

	/**
	 * 查询相应的计划分页列表
	 *
	 * @param info
	 * @param form
	 */
	private void createPlanListPage(PlanListAJaxBean result, PlanBean form) {

		Map<String, Object> params = new HashMap<String, Object>();
		// 统计相应的汇直投的数据记录数（查出列表里数据库的实际总记录数）
		int projecTotal = this.planService.queryDebtPlanRecordTotal(params);

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
			// 具体内容查询，例如 编号，收益率，金额，进度，状态等等
			List<DebtPlanCustomize> planList = planService.searchDebtPlanList(params);
			/*原AJAX列表展示时间，现在展示倒计时*/
/*			for(DebtPlanCustomize plan : planList){
				if(plan.getBuyBeginTime()!= null && !"".equals(plan.getBuyBeginTime().trim())){
					plan.setBuyBeginTime(GetDate.timestamptoStrYYYYMMDD(Integer.valueOf(plan.getBuyBeginTime())));
				}
			}*/
			result.setDebtPlanList(planList);
			result.setPaginator(paginator);
			//ajax 列表 倒计时使用
			int nowTime = GetDate.getNowTime10();
			result.setNowTime(nowTime);
		} else {
			result.setDebtPlanList(new ArrayList<DebtPlanCustomize>());
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
	@RequestMapping(value = PlanDefine.PLAN_DETAIL_ACTION)
	public ModelAndView searchPlanDetail(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(PlanDefine.THIS_CLASS, PlanDefine.PLAN_DETAIL_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanDefine.PLAN_DETAIL_PTAH);

		// 获取计划编号
		String planNid = request.getParameter("planNid");
		// 1.获取优惠券编号
		String couponId = request.getParameter("couponGrantId");
		// 获取相应的登陆用户
		WebViewUser loginUser = WebUtils.getUser(request);
		// 2.根据项目标号获取相应的计划信息
		DebtPlanDetailCustomize planDetail = this.planService.selectDebtPlanDetail(planNid);
		if (Validator.isNotNull(planDetail)) {
			if (planDetail.getPlanStatus().equals("12")) {
				modelAndView = new ModelAndView(PlanDefine.PLAN_LIST_PTAH);
				return modelAndView;
			}
			//系统当前时间戳
			modelAndView.addObject("nowTime", GetDate.getNowTime10());
			// 计划金额
			BigDecimal planAccount = new BigDecimal(planDetail.getPlanAccount().replace(",", ""));
			// 收益率
			BigDecimal planApr = new BigDecimal(planDetail.getPlanApr().replace(",", ""));
			// 计划期限
			int planPeriod = Integer.parseInt(planDetail.getPlanPeriod());
			BigDecimal planInterest = new BigDecimal(0);
			// 计算历史回报
			planInterest = DuePrincipalAndInterestUtils.getMonthInterest(planAccount, planApr.divide(new BigDecimal("100")), planPeriod).setScale(2, BigDecimal.ROUND_DOWN);
			// 计划历史回报
			planDetail.setPlanInterest(planInterest.toString());

			/** 汇添金优惠券使用开始 pcc */
			DecimalFormat df = null;
			df = CustomConstants.DF_FOR_VIEW;
			/** 计算最优优惠券开始 pccvip isThereCoupon 1是有最优优惠券，0无最有优惠券 */
			UserCouponConfigCustomize couponConfig = null;
			//获取用户优惠券总张数
			int recordTotal=0;
			//可用优惠券张数
			int availableCouponListCount=0;
			if(loginUser!=null){
			    couponConfig = planService.getUserOptimalCoupon(couponId, planNid, loginUser.getUserId(), null, "0");
			    recordTotal = planService.countCouponUsers(0, loginUser.getUserId());
	            availableCouponListCount = planService.getUserCouponAvailableCount(planNid, loginUser.getUserId(), "0", "0");
	            /** 获取用户是否是vip 开始 pccvip 1是vip 0不是vip */
	            UsersInfo usersInfo = planService.getUsersInfoByUserId(loginUser.getUserId());
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
			}

			/** 获取用户优惠券总张数开始 pccvip */
            modelAndView.addObject("recordTotal", recordTotal);
            /** 获取用户优惠券总张数结束 pccvip */
            /** 可用优惠券张数开始 pccvip */
            modelAndView.addObject("couponAvailableCount", availableCouponListCount);
            /** 可用优惠券张数结束 pccvip */
			BigDecimal couponInterest = BigDecimal.ZERO;
			if (couponConfig != null) {
				modelAndView.addObject("isThereCoupon", 1);

				couponInterest = planService.getCouponInterest(couponConfig.getUserCouponId(), planNid, "0");
				couponConfig.setCouponInterest(df.format(couponInterest));
			} else {
				modelAndView.addObject("isThereCoupon", 0);
			}

			modelAndView.addObject("couponConfig", couponConfig);
			/** 计算最优优惠券结束 */

			/** 汇添金优惠券使用结束 pcc */

			// 计划详情头
			modelAndView.addObject("planDetail", planDetail);
			// 获取计划介绍
			DebtPlanIntroduceCustomize planIntroduce = this.planService.selectDebtPlanIntroduce(planNid);
			if (Validator.isNotNull(planIntroduce)) {
				// 计划介绍
				modelAndView.addObject("planIntroduce", planIntroduce);
				// 获取计划安全保障
				DebtPlanRiskControlCustomize planRiskControl = this.planService.selectDebtPlanRiskControl(planNid);
				if (Validator.isNotNull(planRiskControl)) {
					// 计划安全保障
					modelAndView.addObject("planRiskControl", planRiskControl);
					// 计划常见问题
					DebtPlanQuestionCustomize planQuestion = this.planService.selectDebtPlanQuestion(planNid);
					if (Validator.isNotNull(planQuestion)) {
						// 计划介绍
						modelAndView.addObject("planQuestion", planQuestion);
						String investFlag = "0";

						if (Validator.isNotNull(loginUser)) {
							Integer userId = loginUser.getUserId();
							// 用户是加入过项目
							int count = this.planService.countUserAccede(planNid, loginUser.getUserId());
							if (count > 0) {
								investFlag = "1";
							}else{
							    investFlag = "0";//是否出借过该项目 0未出借 1已出借
				            }
							modelAndView.addObject("investFlag", investFlag);
							if (loginUser.isBankOpenAccount()) {
								modelAndView.addObject("openFlag", 1);
							} else {
								modelAndView.addObject("openFlag", 0);
							}
							//是否设置交易密码
				            if(loginUser.getIsSetPassword() == 1){
				                modelAndView.addObject("setPwdFlag", "1");
				            }else{
				                modelAndView.addObject("setPwdFlag", "0");
				            }
				            // 风险测评改造 mod by liuyang 20180111 start
				            // 风险测评标识
				            // JSONObject jsonObject = CommonSoaUtils.getUserEvalationResultByUserId(userId + "");
//				            modelAndView.addObject("riskFlag", String.valueOf(loginUser.getIsEvaluationFlag()));
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
							// 风险测评改造 mod by liuyang 20180111 end
							modelAndView.addObject("loginFlag", 1);
							Account account = this.planService.getAccount(userId);
							if (Validator.isNotNull(account)) {
								String userBalance = account.getBankBalance().toString();
								modelAndView.addObject("userBalance", userBalance);
							}
						} else {
						    modelAndView.addObject("loginFlag", "0");//登录状态 0未登陆 1已登录
				            modelAndView.addObject("openFlag", "0"); //开户状态 0未开户 1已开户
				            modelAndView.addObject("investFlag", "0");//是否出借过该项目 0未出借 1已出借
				            modelAndView.addObject("riskFlag", "0");//是否进行过风险测评 0未测评 1已测评
				            modelAndView.addObject("setPwdFlag", "0");//是否设置过交易密码 0未设置 1已设置
						}
						return modelAndView;
					}
				}
			}
		}
		modelAndView = new ModelAndView(PlanDefine.ERROR_PTAH);
		return modelAndView;
	}

	/**
	 * 查询相应的汇添金计划的加入明细
	 *
	 * @param hztInvest
	 * @param request
	 * @param response
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = PlanDefine.PLAN_ACCEDE_ACTION, produces = "application/json; charset=utf-8")
	public PlanAccedeListAjaxBean searchPlanAccede(@ModelAttribute PlanAccedeBean form, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(PlanDefine.THIS_CLASS, PlanDefine.PLAN_ACCEDE_ACTION);
		PlanAccedeListAjaxBean result = new PlanAccedeListAjaxBean();
		this.createPlanAccedePage(result, form);
		result.success();
		LogUtil.endLog(PlanDefine.THIS_CLASS, PlanDefine.PLAN_ACCEDE_ACTION);
		return result;
	}

	/**
	 * 创建用户出借记录分页查询
	 *
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createPlanAccedePage(PlanAccedeListAjaxBean result, PlanAccedeBean form) {

		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		String planNid = form.getPlanNid();
		DebtPlan debtPlan = this.planService.selectDebtPlanByNid(planNid);
		if (Validator.isNotNull(debtPlan)) {
			result.setAccedeTotal(df.format(debtPlan.getDebtPlanMoneyYes()));
			result.setAccedeTimes(String.valueOf(debtPlan.getAccedeTimes()));
		} else {
			result.setAccedeTotal(df.format(new BigDecimal("0")));
			result.setAccedeTimes("0");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("planNid", planNid);
		int recordTotal = this.planService.countPlanAccedeRecordTotal(params);
		result.setAccedeTimes(recordTotal + "");
		if (recordTotal > 0) {
			Long sum = planService.selectPlanAccedeSum(params);
			result.setAccedeTotal(sum + "");
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
			List<DebtPlanAccedeCustomize> recordList = planService.selectPlanAccedeList(params);
			result.setPlanAccedeList(recordList);
			result.setPaginator(paginator);
		} else {
			result.setPlanAccedeList(new ArrayList<DebtPlanAccedeCustomize>());
			result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
		}
	}

	/**
	 * 查询相应的汇添金标的列表
	 *
	 * @param hztInvest
	 * @param request
	 * @param response
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = PlanDefine.PLAN_BORROW_ACTION, produces = "application/json; charset=utf-8")
	public PlanBorrowListAjaxBean searchPlanBorrow(@ModelAttribute PlanBorrowBean form, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(PlanDefine.THIS_CLASS, PlanDefine.PLAN_ACCEDE_ACTION);
		PlanBorrowListAjaxBean result = new PlanBorrowListAjaxBean();
		this.createPlanBorrowPage(request, result, form);
		result.success();
		LogUtil.endLog(PlanDefine.THIS_CLASS, PlanDefine.PLAN_ACCEDE_ACTION);
		return result;
	}

	/**
	 * 创建用户出借记录分页查询
	 *
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createPlanBorrowPage(HttpServletRequest request, PlanBorrowListAjaxBean result, PlanBorrowBean form) {
		// TODO 先直投标 再转让， 最大五十条， 利率》=计划预期利率 ，根据利率排序
		String planNid = form.getPlanNid();
		Map<String, Object> params = new HashMap<String, Object>();
		Integer TopCount = 50;// 最大五十条
		params.put("planNid", planNid);
		Integer userId = WebUtils.getUserId(request);
		params.put("userId", userId);
		DebtPlan debtPlan = planService.getPlanByNid(planNid);
		int recordTotal = this.planService.countPlanBorrowRecordTotal(params);
		if (recordTotal >= TopCount) {
			// 只有专属标，不考虑上诉条件 只是利率倒叙
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
				List<DebtPlanBorrowCustomize> recordList = planService.selectPlanBorrowList(params);
				result.setPlanBorrowList(recordList);
				result.setPaginator(paginator);
			} else {
				result.setPlanBorrowList(new ArrayList<DebtPlanBorrowCustomize>());
				result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
			}
		} else {

			// 专属标和利率
			params.put("TopCount", TopCount - recordTotal);
			if (debtPlan.getDebtPlanStatus() == 3 || debtPlan.getDebtPlanStatus() == 4) {
				params.put("isShow", 1);
			} else {
				params.put("isShow", 0);
			}
			params.put("apr", debtPlan.getExpectApr());
			int dreditTotal = this.planService.countPlanBorrowRecordTotalCredit(params);
			if (dreditTotal > 0) {
				// 查询相应的汇直投列表数据
				Paginator paginator = new Paginator(form.getPaginatorPage(), dreditTotal, form.getPageSize());
				int limit = paginator.getLimit();
				int offSet = paginator.getOffset();
				if (offSet == 0 || offSet > 0) {
					params.put("limitStart", offSet);
				}
				if (limit > 0) {
					params.put("limitEnd", limit);
				}
				List<DebtPlanBorrowCustomize> recordList = planService.selectPlanBorrowListCredit(params);
				result.setPlanBorrowList(recordList);
				result.setPaginator(paginator);
			} else {
				// 没专属债权和关联标时
				DebtPlan lastPlan = planService.selectLastPlanByTime(debtPlan.getBuyBeginTime());
				if (lastPlan != null) {
					String lastPlanNid = lastPlan.getDebtPlanNid();
					Integer lastPlanLiqTime = lastPlan.getLiquidateShouldTime();
					params.put("planNid", lastPlanNid);
					params.put("lastPlanLiqTime", lastPlanLiqTime);
					int recordTotalLast = this.planService.countPlanBorrowRecordTotalLast(params);
					// 上个计划的专属标
					if (recordTotalLast > 0) {
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
						List<DebtPlanBorrowCustomize> recordList = planService.selectPlanBorrowListLast(params);
						result.setPlanBorrowList(recordList);
						result.setPaginator(paginator);
					} else {
						result.setPlanBorrowList(new ArrayList<DebtPlanBorrowCustomize>());
						result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
					}
				} else {
					result.setPlanBorrowList(new ArrayList<DebtPlanBorrowCustomize>());
					result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
				}
			}
		}

	}

	/**
	 * 查询相应的项目详情
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = PlanDefine.PLAN_PREVIEW_ACTION)
	public ModelAndView getPlanPreview(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(PlanDefine.THIS_CLASS, PlanDefine.PLAN_PREVIEW_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanDefine.PLAN_PREVIEW_PTAH);
		// 获取计划编号
		String planNid = request.getParameter("planNid");
		// 2.根据项目标号获取相应的计划信息
		DebtPlanDetailCustomize planDetail = this.planService.selectDebtPlanDetail(planNid);
		if (Validator.isNotNull(planDetail)) {
			// 计划金额
			BigDecimal planAccount = new BigDecimal(planDetail.getPlanAccount().replace(",", ""));
			// 收益率
			BigDecimal planApr = new BigDecimal(planDetail.getPlanApr().replace(",", ""));
			// 计划期限
			int planPeriod = Integer.parseInt(planDetail.getPlanPeriod());
			BigDecimal planInterest = new BigDecimal(0);
			// 计算历史回报
			planInterest = DuePrincipalAndInterestUtils.getMonthInterest(planAccount, planApr.divide(new BigDecimal("100")), planPeriod).setScale(2, BigDecimal.ROUND_DOWN);
			// TODO 计划历史回报
			planDetail.setPlanInterest(planInterest.toString());
			// 计划详情头
			modelAndView.addObject("planDetail", planDetail);
			// 获取计划介绍
			DebtPlanIntroduceCustomize planIntroduce = this.planService.selectDebtPlanIntroduce(planNid);
			if (Validator.isNotNull(planIntroduce)) {
				// 计划介绍
				modelAndView.addObject("planIntroduce", planIntroduce);
				// 获取计划安全保障
				DebtPlanRiskControlCustomize planRiskControl = this.planService.selectDebtPlanRiskControl(planNid);
				if (Validator.isNotNull(planRiskControl)) {
					// 计划安全保障
					modelAndView.addObject("planRiskControl", planRiskControl);
					// 计划常见问题
					DebtPlanQuestionCustomize planQuestion = this.planService.selectDebtPlanQuestion(planNid);
					if (Validator.isNotNull(planQuestion)) {
						// 计划介绍
						modelAndView.addObject("planQuestion", planQuestion);
						return modelAndView;
					}
				}
			}
		}
		modelAndView = new ModelAndView(PlanDefine.ERROR_PTAH);
		return modelAndView;
	}

	/**
	 * 查询相应的用户出借的用户列表（查看协议）
	 *
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = PlanDefine.XIEYI_ACTION, method = RequestMethod.GET)
	public ModelAndView searchUserInvestList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView(PlanDefine.XY_PATH);
		return modelAndView;
	}

	/**
	 *
	 * 根据出借项目id获取出借信息
	 *
	 * @author 王坤
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = PlanDefine.INVEST_INFO_ACTION, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public InvestInfoAjaxBean getInvestInfo(HttpServletRequest request, HttpServletResponse response) {

		String planNid = request.getParameter("nid");
		String accountStr = request.getParameter("money");
		// 1.获取优惠券编号
		String couponId = request.getParameter("couponGrantId");
		InvestInfoAjaxBean investInfo = new InvestInfoAjaxBean();
		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		df.setRoundingMode(RoundingMode.FLOOR);
		// 查询项目信息
		String money = String.valueOf(Integer.parseInt(accountStr));
		DebtPlan plan = planService.getPlanByNid(planNid);
		if (null != plan) {

			BigDecimal couponInterest = BigDecimal.ZERO;
			// 获取相应的登陆用户
            WebViewUser loginUser = WebUtils.getUser(request);
			/** 汇添金优惠券使用开始 pcc start */
			if (!"0".equals(plan.getCouponConfig())&&loginUser!=null) {

				/** 计算最优优惠券开始 pccvip isThereCoupon 1是有最优优惠券，0无最有优惠券 */
				UserCouponConfigCustomize couponConfig = null;
				couponConfig = planService.getUserOptimalCoupon(couponId, planNid, loginUser.getUserId(), money, "0");

				if (couponConfig != null) {
					investInfo.setIsThereCoupon(1);

					couponInterest = planService.getCouponInterest(couponConfig.getUserCouponId(), planNid, money);
					couponConfig.setCouponInterest(df.format(couponInterest));
				} else {
					investInfo.setIsThereCoupon(0);
				}
				investInfo.setCouponConfig(couponConfig);
				/** 计算最优优惠券结束 */

				/** 可用优惠券张数开始 pccvip */
				int availableCouponListCount = planService.getUserCouponAvailableCount(planNid, loginUser.getUserId(), "0", "0");
				investInfo.setCouponAvailableCount(availableCouponListCount);
				/** 可用优惠券张数结束 pccvip */

				/** 获取用户是否是vip 开始 pccvip 1是vip 0不是vip */
				UsersInfo usersInfo = planService.getUsersInfoByUserId(loginUser.getUserId());
				if (usersInfo.getVipId() != null && usersInfo.getVipId() != 0) {
					investInfo.setIfVip(1);
				} else {
					investInfo.setIfVip(0);
				}
				/** 获取用户是否是vip 结束 pccvip */

				/** 获取用户优惠券总张数开始 pccvip */
				int recordTotal = planService.countCouponUsers(0, loginUser.getUserId());
				investInfo.setRecordTotal(recordTotal);
				/** 获取用户优惠券总张数结束 pccvip */
				investInfo.setCouponCapitalInterest(df.format(couponInterest));
			} else {
				investInfo.setCouponAvailableCount(0);
			}

			/** 汇添金优惠券使用结束 pcc end */

			BigDecimal earnings = new BigDecimal("0");
			// 如果出借金额不为空
			if (!StringUtils.isBlank(money) && Long.parseLong(money) > 0) {
				// 收益率
				BigDecimal borrowApr = plan.getExpectApr();
				// 周期
				Integer borrowPeriod = plan.getDebtLockPeriod();

				// 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
				// 计算历史回报
				earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(money), borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2,
						BigDecimal.ROUND_DOWN);
				investInfo.setEarnings(df.format(earnings));

				investInfo.setStatus(true);
				investInfo.setMessage("历史回报计算完成");
			} else {
				investInfo.setStatus(false);
				investInfo.setMessage("请填写正确的出借金额");
			}
			investInfo.setCapitalInterest(df.format(earnings.add(couponInterest)));
		} else {
			investInfo.setStatus(false);
			investInfo.setMessage("请填写正确的金额");
		}
		return investInfo;
	}

	/**
	 * pc加入计划验证
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = PlanDefine.PLAN_CHECK_ACTION, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public WebBaseAjaxResultBean appointCheck(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(PlanController.class.toString(), PlanDefine.PLAN_CHECK_ACTION);
		String planNid = request.getParameter("nid");
		// 出借金额
		String money = request.getParameter("money");
		// 出借金额
		String couponGrantId = request.getParameter("couponGrantId");

		Integer userId = WebUtils.getUserId(request);
		if (money == null || "".equals(money)) {
			money = "0";
		}
		System.out.println("tenderCheck ShiroUtil.getLoginUserId--------------------" + userId);
		// 参数验证成功，则返回url，否则不返回
		WebBaseAjaxResultBean result = new WebBaseAjaxResultBean();
		JSONObject info = planService.checkParamPlan(planNid, money, userId, couponGrantId);

		//用户测评（问刘阳后说明：汇添金暂时没有用户测评逻辑所以注释）
		//从user中获取客户类型，ht_user_evalation_result（用户测评总结表）

		/*
		UserEvalationResultCustomize userEvalationResultCustomize = evaluationService.selectUserEvalationResultByUserId(userId);
		if(userEvalationResultCustomize != null){
			EvaluationConfig evalConfig = new EvaluationConfig();
			//4.智投出借者测评类型校验
			String intellectualEveluationTypeCheck = "0";
			//5.智投单笔投资金额校验
			String intellectualEvaluationMoneyCheck = "0";
			//6.智投待收本金校验
			String intellectualCollectionEvaluationCheck = "0";
			//获取开关信息
			List<EvaluationConfig> evalConfigList = evaluationService.selectEvaluationConfig(evalConfig);
			if(evalConfigList != null && evalConfigList.size() > 0){
				evalConfig = evalConfigList.get(0);
				//4.智投出借者测评类型校验
				intellectualEveluationTypeCheck = evalConfig.getIntellectualEveluationTypeCheck() == null ? "0" : String.valueOf(evalConfig.getIntellectualEveluationTypeCheck());
				//5.智投单笔投资金额校验
				intellectualEvaluationMoneyCheck = evalConfig.getIntellectualEvaluationMoneyCheck() == null ? "0" : String.valueOf(evalConfig.getIntellectualEvaluationMoneyCheck());
				//6.智投待收本金校验
				intellectualCollectionEvaluationCheck = evalConfig.getIntellectualCollectionEvaluationCheck() == null ? "0" : String.valueOf(evalConfig.getIntellectualCollectionEvaluationCheck());
				//7.投标时校验（二期）(预留二期开发)
			}
			//初始化金额返回值
			String revaluation_money,revaluation_money_principal;
			//根据类型从redis或数据库中获取测评类型和上限金额
			String eval_type = userEvalationResultCustomize.getType();
			switch (eval_type){
				case "保守型":
					//从redis获取金额（单笔）
					revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE);
					//如果reids不存在或者为零尝试获取数据库（单笔）
					if("0".equals(revaluation_money)){
						revaluation_money = evalConfig.getConservativeEvaluationSingleMoney() == null ? "0": String.valueOf(evalConfig.getConservativeEvaluationSingleMoney());
					}
					//从redis获取金额（代收本金）
					revaluation_money_principal = RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE_PRINCIPAL) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE_PRINCIPAL);
					//如果reids不存在或者为零尝试获取数据库（代收本金）
					if("0".equals(revaluation_money_principal)){
						revaluation_money_principal = evalConfig.getConservativeEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getConservativeEvaluationPrincipalMoney());
					}
					break;
				case "稳健型":
					//从redis获取金额（单笔）
					revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS);
					//如果reids不存在或者为零尝试获取数据库（单笔）
					if("0".equals(revaluation_money)){
						revaluation_money = evalConfig.getSteadyEvaluationSingleMoney() == null ? "0": String.valueOf(evalConfig.getSteadyEvaluationSingleMoney());
					}
					//从redis获取金额（代收本金）
					revaluation_money_principal = RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS_PRINCIPAL) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS_PRINCIPAL);
					//如果reids不存在或者为零尝试获取数据库（代收本金）
					if("0".equals(revaluation_money_principal)){
						revaluation_money_principal = evalConfig.getSteadyEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getSteadyEvaluationPrincipalMoney());
					}
					break;
				case "成长型":
					//从redis获取金额（单笔）
					revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_GROWTH) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_GROWTH);
					//如果reids不存在或者为零尝试获取数据库（单笔）
					if("0".equals(revaluation_money)){
						revaluation_money = evalConfig.getGrowupEvaluationSingleMoney() == null ? "0": String.valueOf(evalConfig.getGrowupEvaluationSingleMoney());
					}
					//从redis获取金额（代收本金）
					revaluation_money_principal = RedisUtils.get(RedisConstants.REVALUATION_GROWTH_PRINCIPAL) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_GROWTH_PRINCIPAL);
					//如果reids不存在或者为零尝试获取数据库（代收本金）
					if("0".equals(revaluation_money_principal)){
						revaluation_money_principal = evalConfig.getGrowupEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getGrowupEvaluationPrincipalMoney());
					}
					break;
				case "进取型":
					//从redis获取金额（单笔）
					revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE);
					//如果reids不存在或者为零尝试获取数据库（单笔）
					if("0".equals(revaluation_money)){
						revaluation_money = evalConfig.getEnterprisingEvaluationSinglMoney() == null ? "0": String.valueOf(evalConfig.getEnterprisingEvaluationSinglMoney());
					}
					//从redis获取金额（代收本金）
					revaluation_money_principal = RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE_PRINCIPAL) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE_PRINCIPAL);
					//如果reids不存在或者为零尝试获取数据库（代收本金）
					if("0".equals(revaluation_money_principal)){
						revaluation_money_principal = evalConfig.getEnterprisingEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getEnterprisingEvaluationPrincipalMoney());
					}
					break;
				default:
					revaluation_money = null;
					revaluation_money_principal = null;
			}
			//风险类型校验
			if (info != null && info.get("error").equals("0")) {
				// 获取相应的登陆用户
				WebViewUser loginUser = WebUtils.getUser(request);
				//测评到期日
				Long lCreate = loginUser.getEvaluationExpiredTime().getTime();
				//当前日期
				Long lNow = System.currentTimeMillis();
				if (lCreate <= lNow) {
					//已过期需要重新评测
					result.setErrorCode(CustomConstants.BANK_TENDER_RETURN_ANSWER_EXPIRED);
					result.setStatus(false);
					result.setMessage("根据监管要求，出借前必须进行风险测评。");
					LogUtil.endLog(TenderController.class.toString(), TenderDefine.INVEST_ACTION);
					return result;
				}
				if(CustomConstants.EVALUATION_CHECK.equals(intellectualEveluationTypeCheck)){
					//计划类判断用户类型为稳健型以上才可以出借
					if(!CommonUtils.checkStandardInvestment(eval_type)){
						//返回错误码
						result.setErrorCode(CustomConstants.BANK_TENDER_RETURN_CUSTOMER_STANDARD_FAIL);
						result.setStatus(false);
						//返回类型
						result.setEvalType(eval_type);
						result.setMessage("您的风险等级为 #"+eval_type+"# \\n达到 #稳健型# 及以上才可以出借此项目");
						LogUtil.endLog(TenderController.class.toString(), TenderDefine.INVEST_ACTION);
						return result;
					}
				}
				if(revaluation_money == null){
					System.out.println("=============从redis中获取测评类型和上限金额异常!(没有获取到对应类型的限额数据) eval_type="+eval_type);
				}else {
					if(CustomConstants.EVALUATION_CHECK.equals(intellectualEvaluationMoneyCheck)){
						//金额对比判断（校验金额 大于 设置测评金额）
						if (new BigDecimal(money).compareTo(new BigDecimal(revaluation_money)) > 0) {
							//返回错误码
							result.setErrorCode(CustomConstants.BANK_TENDER_RETURN_LIMIT_EXCESS);
							result.setStatus(false);
							//返回类型和限额
							result.setEvalType(eval_type);
							result.setRevaluationMoney(StringUtil.getTenThousandOfANumber(Double.valueOf(revaluation_money).intValue()));
							LogUtil.endLog(TenderController.class.toString(), TenderDefine.INVEST_ACTION);
							return result;
						}
					}
				}
				if(revaluation_money_principal == null){
					System.out.println("=============从redis中获取测评类型和上限金额异常!(没有获取到对应类型的限额数据) eval_type="+eval_type);
				}else {
					//代收本金限额校验
					if(CustomConstants.EVALUATION_CHECK.equals(intellectualCollectionEvaluationCheck)){
						//获取冻结金额和代收本金
						List<AccountDetailCustomize> accountInfos = evaluationService.queryAccountEvalDetail(userId);
						if(accountInfos!= null || accountInfos.size() >0){
							AccountDetailCustomize accountDetail =  accountInfos.get(0);
							BigDecimal planFrost = accountDetail.getPlanFrost();// plan_frost 汇添金计划真实冻结金额
							BigDecimal bankFrost = accountDetail.getBankFrost();// bank_frost 银行冻结金额
							BigDecimal bankAwaitCapital = accountDetail.getBankAwaitCapital();// bank_await_capital 银行待收本金
							BigDecimal account = BigDecimal.ZERO;
							//加法运算
							account = account.add(planFrost).add(bankFrost).add(bankAwaitCapital).add(new BigDecimal(money));
							//金额对比判断（校验金额 大于 设置测评金额）（代收本金）
							if (account.compareTo(new BigDecimal(revaluation_money_principal)) > 0) {
								//返回错误码
								result.setErrorCode(CustomConstants.BANK_TENDER_RETURN_LIMIT_EXCESS_PRINCIPAL);
								result.setStatus(false);
								//返回类型和限额
								result.setEvalType(eval_type);
								result.setRevaluationMoneyPrincipal(StringUtil.getTenThousandOfANumber(Double.valueOf(revaluation_money_principal).intValue()));
								LogUtil.endLog(TenderController.class.toString(), TenderDefine.INVEST_ACTION);
								return result;
							}
						}
					}
				}
			}
		}else{
			System.out.println("=============该用户测评总结数据为空! userId="+userId);
		}
		*/

		// 加入返回结果
		if (info == null) {
			result.setStatus(false);
			result.setMessage("出借失败");
		} else if (info.get("error").equals("0")) {
			result.setStatus(true);
			result.setMessage("操作成功");
		} else {
			result.setStatus(false);
			result.setMessage(info.getString("data"));
		}
		LogUtil.endLog(PlanController.class.toString(), PlanDefine.PLAN_CHECK_ACTION);
		return result;
	}

	/**
	 * 加入计划
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanDefine.PLAN_INVEST_ACTION)
	public ModelAndView joinPlan(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(PlanController.class.toString(), PlanDefine.PLAN_INVEST_ACTION);
		Integer userId = WebUtils.getUserId(request);
		ModelAndView modelAndView = new ModelAndView();
		String planNid = request.getParameter("nid");
		// 出借金额
		String accountStr = request.getParameter("money");
		if (accountStr == null || "".equals(accountStr)) {
			accountStr = "0";
		}
		modelAndView.addObject("plan", "1");
		modelAndView.addObject("planNid", planNid);
		System.out.println("joinPlan ShiroUtil.getLoginUserId--------------------" + userId);// 如果没有本金出借且有优惠券出借
		BigDecimal decimalAccount = StringUtils.isNotEmpty(accountStr) ? new BigDecimal(accountStr) : BigDecimal.ZERO;

		// add by pcc 汇添金无本金出借是使用优惠券出借 start
		// 优惠券用户id （coupon_user的id）
		String couponGrantId = request.getParameter("couponGrantId");
		// 排他校验时间
		int couponOldTime = Integer.MIN_VALUE;
		CouponConfigCustomizeV2 cuc = null;
		// -1:有可用的优惠券，但是出借时不选择优惠券 空或null：用户没有可用优惠券
		if (StringUtils.isNotEmpty(couponGrantId) && !StringUtils.equals("-1", couponGrantId)) {
			cuc = planService.getCouponUser(couponGrantId, userId);
			// 排他check用
			couponOldTime = cuc.getUserUpdateTime();
		}

		if (decimalAccount.compareTo(BigDecimal.ZERO) != 1 && cuc != null && (cuc.getCouponType() == 3 || cuc.getCouponType() == 1)) {
			System.out.println("cuc.getCouponType():" + cuc.getCouponType());
			this.couponTender(modelAndView, request, cuc, userId, couponOldTime);
			return modelAndView;
		}
		// add by pcc 汇添金无本金出借是使用优惠券出借 end

		JSONObject result = planService.checkParamPlan(planNid, accountStr, userId, couponGrantId);
		if (result == null) {
			modelAndView.setViewName(InvestDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("investDesc", "出借失败！");
			return modelAndView;
		} else if (result.get("error") != null && result.get("error").equals("1")) {
			System.out.println(result.toJSONString());
			modelAndView.setViewName(InvestDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("investDesc", result.get("data") + "");
			return modelAndView;
		}
		String tenderUsrcustid = result.getString("tenderUsrcustid");
		// 生成冻结订单
		String frzzeOrderId = GetOrderIdUtils.getOrderId0(Integer.valueOf(userId));
		String frzzeOrderDate = GetOrderIdUtils.getOrderDate();
		// TODO 冻结 加入 相应金额 明细

		Jedis jedis = pool.getResource();
		String balance = RedisUtils.get(planNid);
		if (StringUtils.isNotBlank(balance)) {
			// 操作redis
			while ("OK".equals(jedis.watch(planNid))) {
				balance = RedisUtils.get(planNid);
				if (StringUtils.isNotBlank(balance)) {
					System.out.println("PC用户:" + userId + "***********************************加入计划冻结前可投金额：" + balance);
					if (new BigDecimal(balance).compareTo(BigDecimal.ZERO) == 0) {
						modelAndView.setViewName(InvestDefine.INVEST_ERROR_PATH);
						modelAndView.addObject("investDesc", "您来晚了，下次再来抢吧");
						return modelAndView;
					} else {
						if (new BigDecimal(balance).compareTo(decimalAccount) < 0) {
							modelAndView.setViewName(InvestDefine.INVEST_ERROR_PATH);
							modelAndView.addObject("investDesc", "可加入剩余金额为" + balance + "元");
							return modelAndView;
						} else {
							Transaction tx = jedis.multi();
							BigDecimal lastAccount = new BigDecimal(balance).subtract(decimalAccount);
							tx.set(planNid, lastAccount + "");
							List<Object> result1 = tx.exec();
							if (result1 == null || result1.isEmpty()) {
								jedis.unwatch();
								modelAndView.setViewName(InvestDefine.INVEST_ERROR_PATH);
								modelAndView.addObject("investDesc", "可加入剩余金额为" + balance + "元");
								return modelAndView;
							} else {
								System.out.println("PC用户:" + userId + "***********************************加入前减redis：" + decimalAccount);
								// 写队列
								break;
							}
						}
					}
				} else {
					modelAndView.setViewName(InvestDefine.INVEST_ERROR_PATH);
					modelAndView.addObject("investDesc", "您来晚了，下次再来抢吧");
					return modelAndView;
				}
			}
		} else {
			modelAndView.setViewName(InvestDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("investDesc", "您来晚了，下次再来抢吧");
			return modelAndView;
		}

		boolean afterDealFlag = false;
		String couponInterest="0";
		// 写入加入计划表
		try {
			// 生成加入订单
			String planOrderId = GetOrderIdUtils.getOrderId0(Integer.valueOf(userId));
			// update by pcc 汇添金有本金出借是使用优惠券出借修改updateAfterPlanRedis接口
			// 添加couponGrantId、modelAndView参数 start
			afterDealFlag = planService.updateAfterPlanRedis(planNid, frzzeOrderId, userId, accountStr, tenderUsrcustid, 0, GetCilentIP.getIpAddr(request), "", frzzeOrderDate, planOrderId,
					couponGrantId, modelAndView,couponInterest);
			// update by pcc 汇添金有本金出借是使用优惠券出借修改updateAfterPlanRedis接口
			// 添加couponGrantId、modelAndView参数 end
			if (afterDealFlag) {
				modelAndView.setViewName(InvestDefine.INVEST_SUCCESS_PATH);
				LogUtil.endLog(PlanDefine.class.toString(), PlanDefine.PLAN_INVEST_ACTION, "[交易完成后,回调结束]");
				DecimalFormat df = CustomConstants.DF_FOR_VIEW;
				String interest = null;
				if (StringUtils.isBlank(interest)) {
					// 根据项目编号获取相应的项目
					DebtPlan debtPlan = planService.getPlanByNid(planNid);
					BigDecimal planApr = debtPlan.getExpectApr();
					// 周期
					Integer planPeriod = debtPlan.getDebtLockPeriod();
					BigDecimal earnings = new BigDecimal("0");
					df.setRoundingMode(RoundingMode.FLOOR);
					// 计算历史回报
					earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(accountStr), planApr.divide(new BigDecimal("100")), planPeriod);
					interest = df.format(earnings.add(new BigDecimal(couponInterest)));
				}
				if (StringUtils.isNotBlank(interest)) {
					modelAndView.addObject("interest", interest);
				}
				modelAndView.addObject("account", df.format(new BigDecimal(accountStr)));
				modelAndView.addObject("investDesc", "恭喜您加入计划成功!");
				System.out.println("PC用户:" + userId + "***********************************加入计划成功：" + accountStr);
				LogUtil.endLog(PlanController.class.toString(), PlanDefine.PLAN_INVEST_ACTION);

				return modelAndView;
			} else {
				System.out.println("PC用户:" + userId + "***********************************预约成功后处理失败：" + accountStr);
				// 恢复redis
				planService.recoverRedis(planNid, userId, accountStr);
				modelAndView.setViewName(InvestDefine.INVEST_ERROR_PATH);
				modelAndView.addObject("investDesc", "系统异常");
				return modelAndView;
			}
		} catch (Exception e) {
			// 恢复redis
			planService.recoverRedis(planNid, userId, accountStr);
			modelAndView.setViewName(InvestDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("investDesc", "系统异常");
			System.out.println("PC用户:" + userId + "***********************************加入计划成功后处理失败：" + accountStr);
			LogUtil.errorLog(PlanController.class.getName(), PlanDefine.PLAN_INVEST_ACTION, e);
			return modelAndView;

		}

	}

	/**
	 * 体验金出借
	 *
	 * @param modelAndView
	 * @param request
	 * @param cuc
	 * @param userId
	 * @return
	 */
	public ModelAndView couponTender(ModelAndView modelAndView, HttpServletRequest request, CouponConfigCustomizeV2 cuc, int userId, int couponOldTime) {
		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		// 金额
		String account = request.getParameter("money");
		String ip = GetCilentIP.getIpAddr(request);
		String couponGrantId = request.getParameter("couponGrantId");
		String planNid = request.getParameter("nid");
		if (account == null || "".equals(account)) {
			account = "0";
		}

		// 优惠券出借校验
		Map<String, String> validateMap = this.validateCoupon(userId, account, couponGrantId, planNid, CustomConstants.CLIENT_PC);
		if (validateMap.containsKey(CustomConstants.APP_STATUS_DESC)) {
			modelAndView.setViewName(InvestDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("investDesc", validateMap.get(CustomConstants.APP_STATUS_DESC));
			return modelAndView;
		}
		JSONObject jsonObject = CommonSoaUtils.planCouponInvest(userId + "", planNid, account, CustomConstants.CLIENT_PC, couponGrantId, "", ip, couponOldTime + "");

		if (jsonObject.getIntValue("status") == 0) {

			/** 修改出借成功页面显示修改开始 */
			modelAndView.addObject("plan", 1);
			modelAndView.addObject("planNid", planNid);

			// 优惠券收益
			modelAndView.addObject("couponInterest", df.format(new BigDecimal(jsonObject.getString("couponInterest"))));
			// 优惠券收益
            modelAndView.addObject("interest", df.format(new BigDecimal(jsonObject.getString("couponInterest"))));
			// 优惠券收益
            modelAndView.addObject("account", "0");
			// 优惠券类别
			modelAndView.addObject("couponType", jsonObject.getString("couponTypeInt"));
			// 优惠券额度
			modelAndView.addObject("couponQuota", jsonObject.getString("couponQuota"));
			modelAndView.addObject("investDesc", "出借成功！");
			// 跳转到成功画面
			modelAndView.setViewName(InvestDefine.INVEST_SUCCESS_PATH);
			return modelAndView;
		} else {
			LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender", "优惠券出借结束。。。。。。");
			modelAndView.setViewName(InvestDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("investDesc", jsonObject.getString("statusDesc"));
			modelAndView.addObject("plan", 1);
			return modelAndView;
		}

	}

	/**
	 *
	 * 根据borrowNid和用户id获取用户可用优惠券和不可用优惠券列表
	 *
	 * @author pcc
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = PlanDefine.GET_PROJECT_AVAILABLE_USER_COUPON_ACTION)
	public JSONObject getProjectAvailableUserCoupon(HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = new JSONObject();
		String planNid = request.getParameter("nid");
		// String sign = request.getParameter("sign");
		// String platform = request.getParameter("platform");
		String money = request.getParameter("money");
		// 检查参数正确性
		if (Validator.isNull(planNid)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		if (money == null || "".equals(money) || money.length() == 0) {
			money = "0";
		}
		Integer userId = WebUtils.getUserId(request);
		planService.getProjectAvailableUserCoupon(CustomConstants.CLIENT_PC, planNid, userId, ret, money);

		ret.put("status", "0");
		ret.put(CustomConstants.APP_REQUEST, CouponDefine.REQUEST_HOME + CouponDefine.REQUEST_MAPPING + CouponDefine.GET_PROJECT_AVAILABLE_USER_COUPON_ACTION);
		ret.put("statusDesc", "成功");
		return ret;
	}

	/**
	 * 优惠券出借校验
	 *
	 * @param userId
	 *
	 * @param account
	 * @param couponType
	 * @param borrowNid
	 * @return
	 */
	private Map<String, String> validateCoupon(Integer userId, String account, String couponGrantId, String planNid, String platform) {

		JSONObject jsonObject = CommonSoaUtils.planCheckCoupon(userId + "", planNid, account, platform, couponGrantId);
		int status = jsonObject.getIntValue("status");
		String statusDesc = jsonObject.getString("statusDesc");
		Map<String, String> paramMap = new HashMap<String, String>();
		if (status == 1) {
			paramMap.put(CustomConstants.APP_STATUS_DESC, statusDesc);
		}

		return paramMap;
	}

	/**
	 * 获得汇添金计划列表
	 *
	 * @param form
	 * @return List
	 */
	public List<DebtPlanCustomize> getHtjRecordList() {

		Map<String, Object> params = new HashMap<String, Object>();
		// 目前样板只显示两个模态框，暂不需要从 Paginator 取值
		int limit = 2;
		int offSet = 0;
		if (offSet == 0 || offSet > 0) {
			params.put("limitStart", offSet);
		}
		if (limit > 0) {
			params.put("limitEnd", limit);
		}
		// 使用旧版（原ajax）的老查询方法，但是改变Limit
		List<DebtPlanCustomize> htjRecordList = planService.searchDebtPlanList(params);
		for (DebtPlanCustomize debtPlanCustomize : htjRecordList) {
			BigDecimal earnings = BigDecimal.ZERO;
			// 计划金额
			if (!StringUtils.isBlank(debtPlanCustomize.getPlanAccount())) {
				// 收益率
				BigDecimal borrowApr = new BigDecimal(debtPlanCustomize.getPlanApr());
				// 周期
				Integer borrowPeriod = Integer.valueOf(debtPlanCustomize.getPlanPeriod());
				// 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
				// 计算历史回报(每万元)
				earnings = (DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal("10000"), borrowApr.divide(new BigDecimal("100")), borrowPeriod)
						.divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN));
				debtPlanCustomize.setIncome(df.format(earnings));
			}
		}
		return htjRecordList;
	}
}
