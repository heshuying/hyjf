/**
 * Description:汇转让WEB接口Controller
 * Copyright: Copyright (HYJF Corporation) 2016
 * Company: HYJF Corporation
 * @author: 朱晓东
 * @version: 1.0
 * Created at: 2015年03月24日 下午18:35:00
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.bank.web.user.credit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.bank.service.mq.MqService;
import com.hyjf.common.util.*;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.web.user.login.LoginService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hyjf.bank.service.borrow.BorrowFileCustomBean;
import com.hyjf.bank.service.borrow.BorrowRepayPlanCustomBean;
import com.hyjf.bank.service.borrow.BorrowService;
import com.hyjf.bank.service.paginator.WebPaginator;
import com.hyjf.bank.service.user.auth.AuthService;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.bank.service.user.credit.*;
import com.hyjf.bank.service.user.credit.CanCreditListAJaxBean;
import com.hyjf.bank.service.user.credit.CreditAssignedBean;
import com.hyjf.bank.service.user.credit.CreditListAJaxBean;
import com.hyjf.bank.service.user.credit.CreditResultBean;
import com.hyjf.bank.service.user.credit.CreditService;
import com.hyjf.bank.service.user.credit.DebtConfigService;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.enums.utils.ProtocolEnum;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.customize.web.CreditTenderListCustomize;
import com.hyjf.mybatis.model.customize.web.TenderBorrowCreditCustomize;
import com.hyjf.mybatis.model.customize.web.TenderCreditAssignedCustomize;
import com.hyjf.mybatis.model.customize.web.TenderCreditAssignedStatisticCustomize;
import com.hyjf.mybatis.model.customize.web.TenderCreditCustomize;
import com.hyjf.mybatis.model.customize.web.TenderCreditDetailCustomize;
import com.hyjf.mybatis.model.customize.web.TenderCreditRepayPlanCustomize;
import com.hyjf.mybatis.model.customize.web.TenderToCreditAssignCustomize;
import com.hyjf.mybatis.model.customize.web.TenderToCreditDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebHzcDisposalPlanCustomize;
import com.hyjf.mybatis.model.customize.web.WebHzcProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectCompanyDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectPersonDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebRiskControlCustomize;
import com.hyjf.mybatis.model.customize.web.WebVehiclePledgeCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.WebBaseAjaxResultBean;
import com.hyjf.web.agreement.AgreementService;
import com.hyjf.web.bank.web.borrow.BorrowDefine;
import com.hyjf.web.bank.web.borrow.BorrowDetailBean;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.recharge.UserRechargeDefine;
import com.hyjf.web.util.WebUtils;
import com.hyjf.web.vip.apply.ApplyDefine;
import com.hyjf.web.vip.manage.VIPManageDefine;

@Controller("CreditController")
@RequestMapping(value = CreditDefine.REQUEST_MAPPING)
public class CreditController extends BaseController {
	@Autowired
	private MqService mqService;
	@Autowired
	private CreditService tenderCreditService;
	@Autowired
	private DebtConfigService debtConfigService;
	@Autowired
	private LoginService loginService;
	@Autowired
	private AgreementService agreementService;

	private static DecimalFormat DF_COM_VIEW = new DecimalFormat("######0.00");
	private final String FORMAT_DEFAULT_AMOUNT = "0.00";

	@Autowired
	private BorrowService projectService;
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
    private AuthService authService;

	Logger _log = LoggerFactory.getLogger(CreditController.class);
	/**
	 * 用户中心查询出借可债转列表资源 获取可债转数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	// 页面跳转
	@RequestMapping(value = CreditDefine.USER_CREDIT_LIST)
	public ModelAndView userCreditList(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.USER_CREDIT_LIST);
		ModelAndView modelAndView = new ModelAndView(CreditDefine.USER_CREDIT_LIST_PATH);
		String tab = request.getParameter("tab");
		if (tab != null && !"".equals(tab)) {
			modelAndView.addObject("tab", tab);
		}
		LogUtil.endLog(CreditController.class.toString(), CreditDefine.USER_CREDIT_LIST);
		return modelAndView;
	}



	/**
	 * 我要债转列表页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = CreditDefine.USER_CAN_CREDIT_LIST)
	public ModelAndView canCreditList(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.USER_CAN_CREDIT_LIST);
		ModelAndView modelAndView = new ModelAndView(CreditDefine.USER_CAN_CREDIT_LIST_PATH);
		Integer nowTime = GetDate.getNowTime10();// 获取当前时间
		Integer userId = WebUtils.getUserId(request);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("nowTime", nowTime);

		//modify by xiashuqing 20171120 判断用户所处的渠道如果不允许债转，可债转金额为0  start
		if(!tenderCreditService.isAllowChannelAttorn(userId)){
			_log.info("判断用户所处渠道不允许债转,可债转金额0....userId is:{}", userId);
			modelAndView.addObject("canCreditMoney", FORMAT_DEFAULT_AMOUNT);
		} else {
			_log.info("用户可债转列表....userId is:{}", userId);
			// 可债转金额
			modelAndView.addObject("canCreditMoney", tenderCreditService.selectCanCreditMoneyTotal(params));
		}
		//modify by xiashuqing 20171120 判断用户所处的渠道如果不允许债转，可债转金额为0  end

		// 转让中本金
		BigDecimal inCreditMoney = tenderCreditService.selectInCreditMoneyTotal(params);
		// 累计已转让本金
		BigDecimal creditSuccessMoney = tenderCreditService.selectCreditSuccessMoneyTotal(params);
		Account account = tenderCreditService.getAccount(userId);

		modelAndView.addObject("inCreditMoney", inCreditMoney);
		modelAndView.addObject("creditSuccessMoney", creditSuccessMoney);
		modelAndView.addObject("holdMoneyTotal", account.getBankAwaitCapital());

		LogUtil.endLog(CreditController.class.toString(), CreditDefine.USER_CAN_CREDIT_LIST);
		return modelAndView;
	}

	/**
	 * 我要债转列表页数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = CreditDefine.USER_CAN_CREDIT_LIST_DATA, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public CanCreditListAJaxBean getCanCreditListData(@ModelAttribute CreditListBean form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.USER_CAN_CREDIT_LIST_DATA);
		CanCreditListAJaxBean resultBean = new CanCreditListAJaxBean();
		Integer userId = null;
		try {
			userId = WebUtils.getUserId(request); // 用户ID
			if (userId != null && userId.intValue() != 0) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("userId", userId);
				Integer nowTime = GetDate.getNowTime10();// 获取当前时间

				String tenderTimeSort = request.getParameter("tenderTimeSort");
				String creditAccountSort = request.getParameter("creditAccountSort");
				String tenderPeriodSort = request.getParameter("tenderPeriodSort");
				String remainDaysSort = request.getParameter("remainDaysSort");

				params.put("nowTime", nowTime);
				params.put("tenderTimeSort", tenderTimeSort);
				params.put("creditAccountSort", creditAccountSort);
				params.put("tenderPeriodSort", tenderPeriodSort);
				params.put("remainDaysSort", remainDaysSort);

				//add by xiashuqing 20171120 判断用户所处的渠道如果不允许债转，则返回空对象  start
				if(!tenderCreditService.isAllowChannelAttorn(userId)){
					_log.info("判断用户所处渠道不允许债转，没有可转让列表....userId is:{}", userId);
					return resultBean;
				}
				_log.info("用户可债转列表....userId is:{}", userId);
				//add by xiashuqing 20171120 判断用户所处的渠道如果不允许债转，则返回空对象  end

				// 查询相应的汇消费列表的总数
				int recordTotal = this.tenderCreditService.countTenderToCredit(userId, nowTime);
				if (recordTotal > 0) {
					Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
					params.put("limitStart", paginator.getOffset());
					params.put("limitEnd", paginator.getLimit());
					// 查询可债转列表数据
					List<TenderCreditCustomize> recordList = tenderCreditService.selectTenderToCreditList(params);
					resultBean.setPaginator(paginator);
					resultBean.setRecordList(recordList);
				} else {
					resultBean.setPaginator(new Paginator(form.getPaginatorPage(), 0));
					resultBean.setRecordList(new ArrayList<TenderCreditCustomize>());
				}
				resultBean.success();
			}
		} catch (Exception e) {
			LogUtil.errorLog(this.getClass().getName(), "searchTenderToCreditList", "系统异常", e);
			resultBean.setPaginator(new Paginator(1, 0));
			resultBean.setRecordList(new ArrayList<TenderCreditCustomize>());
		}

		LogUtil.endLog(CreditController.class.toString(), CreditDefine.USER_CAN_CREDIT_LIST_DATA);
		return resultBean;
	}

	@ResponseBody
	@RequestMapping(value = CreditDefine.TENDER_TO_CREDIT_LIST, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String searchTenderToCreditList(@ModelAttribute CreditListBean form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_LIST);
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		Integer userId = null;
		try {
			userId = WebUtils.getUserId(request); // 用户ID
			if (userId != null && userId.intValue() != 0) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("userId", userId);

				Integer nowTime = GetDate.getNowTime10();// 获取当前时间

				String tenderTimeSort = request.getParameter("tenderTimeSort");
				String creditAccountSort = request.getParameter("creditAccountSort");
				String tenderPeriodSort = request.getParameter("tenderPeriodSort");
				String remainDaysSort = request.getParameter("remainDaysSort");

				params.put("nowTime", nowTime);
				params.put("tenderTimeSort", tenderTimeSort);
				params.put("creditAccountSort", creditAccountSort);
				params.put("tenderPeriodSort", tenderPeriodSort);
				params.put("remainDaysSort", remainDaysSort);

				// 查询相应的汇消费列表的总数
				int recordTotal = this.tenderCreditService.countTenderToCredit(userId, nowTime);
				if (recordTotal > 0) {
					// WebPaginator paginator = new WebPaginator(currPage,
					// recordTotal, limitPage, "tenderToCreditPage", null);
					Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
					params.put("limitStart", paginator.getOffset());
					params.put("limitEnd", paginator.getLimit());
					// 查询可债转列表数据
					List<TenderCreditCustomize> recordList = tenderCreditService.selectTenderToCreditList(params);
					info.put("paginator", paginator);
					info.put("recordList", recordList);
				} else {
					// WebPaginator paginator = new WebPaginator(currPage,
					// recordTotal);
					info.put("paginator", new Paginator(form.getPaginatorPage(), 0));
					info.put("recordList", "");
				}
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
				ret.put(CustomConstants.DATA, info);
				ret.put(CustomConstants.MSG, "");
			} else {
				LogUtil.infoLog(this.getClass().getName(), "searchTenderToCreditList", "用户未登录");
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
				ret.put("resultCode", "");
				ret.put(CustomConstants.DATA, null);
				ret.put(CustomConstants.MSG, "用户未登录");
			}
		} catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), "searchTenderToCreditList", "系统异常");
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
			ret.put("resultCode", "");
			ret.put(CustomConstants.DATA, null);
			ret.put(CustomConstants.MSG, "系统异常");
		}
		LogUtil.endLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_LIST);
		return JSONObject.toJSONString(ret, true);
	}

	/**
	 * 用户中心验证出借人当天是否可以债转
	 * @param request
	 * @param response
	 * @param tenderCreditCustomize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = CreditDefine.TENDER_ABLE_TO_CREDIT, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String tenderAbleToCredit(HttpServletRequest request, HttpServletResponse response, @ModelAttribute TenderCreditCustomize tenderCreditCustomize) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.TENDER_ABLE_TO_CREDIT);
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		Integer userId = null;
		try {
			userId = WebUtils.getUserId(request); // 用户ID
			if (userId != null && userId.intValue() != 0) {
				if (StringUtils.isEmpty(tenderCreditCustomize.getBorrowNid()) || StringUtils.isEmpty(tenderCreditCustomize.getTenderNid())) {
					ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
					ret.put(CustomConstants.DATA, null);
					ret.put(CustomConstants.MSG, "转让失败,无法获取借款和出借编号");
					LogUtil.endLog(CreditController.class.toString(), CreditDefine.TENDER_ABLE_TO_CREDIT);
					return JSONObject.toJSONString(ret, true);
				}
				// 获取当前时间
				Integer nowTime = GetDate.getNowTime10();
				// 获取当前时间的日期
				String nowDate = (GetDate.yyyyMMdd.format(new Date()) != null && !"".equals(GetDate.yyyyMMdd.format(new Date()))) ? GetDate.yyyyMMdd.format(new Date()) : "0";
				Integer creditedNum = tenderCreditService.tenderAbleToCredit(userId, Integer.parseInt(nowDate));
				List<TenderCreditCustomize> tenderToCreditDetail = tenderCreditService.selectTenderToCreditDetail(userId, nowTime, tenderCreditCustomize.getBorrowNid(),
						tenderCreditCustomize.getTenderNid());
				if (tenderToCreditDetail != null && tenderToCreditDetail.size() > 0) {
					info.put("tenderToCredit", tenderToCreditDetail.get(0));
				}
				info.put("creditedNum", creditedNum);
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
				ret.put(CustomConstants.DATA, info);
				ret.put(CustomConstants.MSG, "");
			} else {
				LogUtil.infoLog(this.getClass().getName(), "tenderAbleToCredit", "用户未登录");
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
				ret.put("resultCode", "");
				ret.put(CustomConstants.DATA, null);
				ret.put(CustomConstants.MSG, "用户未登录");
			}
		} catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), "tenderAbleToCredit", "系统异常");
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
			ret.put("resultCode", "");
			ret.put(CustomConstants.DATA, null);
			ret.put(CustomConstants.MSG, "系统异常");
		}
		LogUtil.endLog(CreditController.class.toString(), CreditDefine.TENDER_ABLE_TO_CREDIT);
		return JSONObject.toJSONString(ret, true);
	}

	/**
	 * 用户中心查询出借可债转详细
	 * @param request
	 * @param response
	 * @param tenderCreditCustomize
	 * @return
	 */
	@RequestMapping(value = CreditDefine.TENDER_TO_CREDIT_DETAIL)
	public ModelAndView searchTenderToCreditDetail(HttpServletRequest request, HttpServletResponse response, @ModelAttribute TenderCreditCustomize tenderCreditCustomize) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_DETAIL);
		ModelAndView modelAndView = new ModelAndView(CreditDefine.TENDER_TO_CREDIT_DETAIL_PATH);
		CreditResultBean creditResultBean = new CreditResultBean();
		Integer userId = null;
		try {
			userId = WebUtils.getUserId(request); // 用户ID
			if (userId != null && userId.intValue() != 0) {
			    // 用户是否完成自动授权标识
                HjhUserAuth hjhUserAuth = this.authService.getHjhUserAuthByUserId(userId);
                // 合规三期
                // 是否开启服务费授权 0未开启  1已开启
                modelAndView.addObject("paymentAuthStatus", hjhUserAuth==null?"":hjhUserAuth.getAutoPaymentStatus());
                modelAndView.addObject("paymentAuthOn", CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus());
                modelAndView.addObject("isCheckUserRole",PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN));
                WebViewUser loginUser = WebUtils.getUser(request);
                modelAndView.addObject("roleId", loginUser==null?"0":loginUser.getRoleId());

				Users user = tenderCreditService.getUsersByUserId(userId);
				List<DebtConfig> config = debtConfigService.selectDebtConfigList();
				if(!CollectionUtils.isEmpty(config)){
					modelAndView.addObject("attornRate",config.get(0).getAttornRate().setScale(2, BigDecimal.ROUND_DOWN));
					modelAndView.addObject("concessionRateUp",config.get(0).getConcessionRateUp().setScale(1, BigDecimal.ROUND_DOWN));
					modelAndView.addObject("concessionRateDown",config.get(0).getConcessionRateDown().setScale(1, BigDecimal.ROUND_DOWN));
					modelAndView.addObject("toggle",config.get(0).getToggle());
					modelAndView.addObject("closeDes",config.get(0).getCloseDes());
				}else{
                    LogUtil.infoLog(this.getClass().getName(), "searchTenderToCreditDetail", "配置表无数据请配置");
                    creditResultBean.setResultFlag(CustomConstants.RESULT_FAIL);
                    creditResultBean.setMsg("配置表无数据请配置");
                    creditResultBean.setData(null);
                    modelAndView.addObject("creditResult", creditResultBean);
                    return modelAndView;
                }
				creditResultBean.setMobile(user.getMobile());

				Integer nowTime = GetDate.getNowTime10();// 获取当前时间
				// 获取当前时间的日期
				if (StringUtils.isEmpty(tenderCreditCustomize.getBorrowNid()) || StringUtils.isEmpty(tenderCreditCustomize.getTenderNid())) {
					creditResultBean.setResultFlag(CustomConstants.RESULT_FAIL);
					creditResultBean.setMsg("转让失败,无法获取借款和出借编号");
					creditResultBean.setData(null);
					LogUtil.endLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_DETAIL);
					modelAndView.addObject("creditResult", creditResultBean);
					return modelAndView;
				}
				List<TenderCreditCustomize> tenderToCreditDetail = tenderCreditService.selectTenderToCreditDetail(userId, nowTime, tenderCreditCustomize.getBorrowNid(),
						tenderCreditCustomize.getTenderNid());
				if (tenderToCreditDetail != null && tenderToCreditDetail.size() > 0) {
					int lastdays = 0;
					String borrowNid = tenderToCreditDetail.get(0).getBorrowNid();
					// 根据borrowNid判断是否分期
					Borrow borrow = this.tenderCreditService.seachBorrowInfo(borrowNid);

					String borrowStyle = borrow.getBorrowStyle();
					if (borrowStyle.equals(CalculatesUtil.STYLE_END) || borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
						try {
							lastdays = GetDate.daysBetween(GetDate.getDateTimeMyTimeInMillis(nowTime),
									GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(tenderToCreditDetail.get(0).getRecoverTime())));
						} catch (NumberFormatException | ParseException e) {
							e.printStackTrace();
						}
					}
					// 等额本息和等额本金和先息后本
					if (borrowStyle.equals(CalculatesUtil.STYLE_MONTH) || borrowStyle.equals(CalculatesUtil.STYLE_PRINCIPAL) || borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {
						List<BorrowRepayPlan> list = this.tenderCreditService.searchBorrowRepayPlanList(borrowNid, borrow.getBorrowPeriod());
						if (list != null && list.size() > 0) {
							try {
								lastdays = GetDate.daysBetween(GetDate.getDateTimeMyTimeInMillis(nowTime), GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(list.get(0).getRepayTime())));
							} catch (NumberFormatException | ParseException e) {
								e.printStackTrace();
							}
						}
					}

					Map<String, Object> resultMap = tenderCreditService.selectExpectCreditFee(tenderCreditCustomize.getBorrowNid(), tenderCreditCustomize.getTenderNid(), String.valueOf(config.get(0).getConcessionRateDown()), nowTime);
					tenderToCreditDetail.get(0).setLastDays(lastdays + "");
					creditResultBean.setResultFlag(CustomConstants.RESULT_SUCCESS);
					creditResultBean.setMsg("");
					creditResultBean.setData(tenderToCreditDetail.get(0));
					creditResultBean.setCalData(resultMap);
				} else {
					creditResultBean.setResultFlag(CustomConstants.RESULT_FAIL);
					creditResultBean.setMsg("获取债转数据为空");
					creditResultBean.setData(null);
				}
			} else {
				LogUtil.infoLog(this.getClass().getName(), "searchTenderToCreditDetail", "用户未登录");
				creditResultBean.setResultFlag(CustomConstants.RESULT_FAIL);
				creditResultBean.setMsg("用户未登录");
				creditResultBean.setData(null);
			}
		} catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), "searchTenderToCreditDetail", "系统异常");
			creditResultBean.setResultFlag(CustomConstants.RESULT_FAIL);
			creditResultBean.setMsg("系统异常");
			creditResultBean.setData(null);
		}
		LogUtil.endLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_DETAIL);
		modelAndView.addObject("creditResult", creditResultBean);
		return modelAndView;
	}

	/**
	 * 用户中心查询 债转详细预计服务费计算
	 * @param request
	 * @param response
	 * @param tenderCreditCustomize
	 * @param creditDiscount
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = CreditDefine.EXCEPT_CREDIT_FEE, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String getExpectCreditFee(HttpServletRequest request, HttpServletResponse response, @ModelAttribute TenderCreditCustomize tenderCreditCustomize, @RequestParam String creditDiscount) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.EXCEPT_CREDIT_FEE);
		JSONObject ret = new JSONObject();
		Integer userId = null;
		try {
			userId = WebUtils.getUserId(request); // 用户ID
			if (userId != null && userId.intValue() != 0) {
				if (StringUtils.isEmpty(tenderCreditCustomize.getBorrowNid()) || StringUtils.isEmpty(tenderCreditCustomize.getTenderNid())) {
					ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
					ret.put(CustomConstants.DATA, null);
					ret.put(CustomConstants.MSG, "无法获取借款和出借编号");
					LogUtil.endLog(CreditController.class.toString(), CreditDefine.EXCEPT_CREDIT_FEE);
					return JSONObject.toJSONString(ret, true);
				}
				if (StringUtils.isEmpty(creditDiscount)) {
					ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
					ret.put(CustomConstants.DATA, null);
					ret.put(CustomConstants.MSG, "无法获取折价率");
					LogUtil.endLog(CreditController.class.toString(), CreditDefine.EXCEPT_CREDIT_FEE);
					return JSONObject.toJSONString(ret, true);
				}
				Integer nowTime = GetDate.getNowTime10();// 获取当前时间
				Map<String, Object> resultMap = tenderCreditService.selectExpectCreditFee(tenderCreditCustomize.getBorrowNid(), tenderCreditCustomize.getTenderNid(), creditDiscount, nowTime);
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
				ret.put(CustomConstants.DATA, resultMap);
				ret.put(CustomConstants.MSG, "");
			} else {
				LogUtil.infoLog(this.getClass().getName(), "getExpectCreditFee", "用户未登录");
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
				ret.put(CustomConstants.DATA, null);
				ret.put(CustomConstants.MSG, "用户未登录");
			}
		} catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), "getExpectCreditFee", "系统异常");
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
			ret.put(CustomConstants.DATA, null);
			ret.put(CustomConstants.MSG, "系统异常");
		}
		LogUtil.endLog(CreditController.class.toString(), CreditDefine.EXCEPT_CREDIT_FEE);
		return JSONObject.toJSONString(ret, true);
	}

	/**
	 * 检查是否可债转
	 * @param request
	 * @param response
	 * @param tenderCreditCustomize
	 * @return
	 */
    @ResponseBody
    @RequestMapping(value = CreditDefine.CHECK_CAN_CREDIT, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public String checkCanCredit(HttpServletRequest request, HttpServletResponse response, @ModelAttribute TenderCreditCustomize tenderCreditCustomize) {
        LogUtil.startLog(CreditController.class.toString(), CreditDefine.CHECK_CAN_CREDIT);
        JSONObject ret = new JSONObject();
        Integer userId = null;
        try {
            userId = WebUtils.getUserId(request); // 用户ID
            if (userId != null && userId.intValue() != 0) {
                // 获取当前时间的日期
                String nowDate = (GetDate.yyyyMMdd.format(new Date()) != null && !"".equals(GetDate.yyyyMMdd.format(new Date()))) ? GetDate.yyyyMMdd.format(new Date()) : "0";
                Integer creditedNum = tenderCreditService.tenderAbleToCredit(userId, Integer.parseInt(nowDate));
                if (creditedNum != null && creditedNum >= 3) {
                    ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
                    ret.put(CustomConstants.DATA, null);
                    ret.put(CustomConstants.MSG, "今天的转让次数已满3次,请明天再试");
                    LogUtil.endLog(CreditController.class.toString(), CreditDefine.CHECK_CAN_CREDIT);
                    return JSONObject.toJSONString(ret, true);
                }
                if (StringUtils.isEmpty(tenderCreditCustomize.getBorrowNid()) || StringUtils.isEmpty(tenderCreditCustomize.getTenderNid())) {
                    ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
                    ret.put(CustomConstants.DATA, null);
                    ret.put(CustomConstants.MSG, "无法获取借款和出借编号");
                    LogUtil.endLog(CreditController.class.toString(), CreditDefine.CHECK_CAN_CREDIT);
                    return JSONObject.toJSONString(ret, true);
                }
                ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
                ret.put(CustomConstants.DATA, null);
                ret.put(CustomConstants.MSG, "");
            } else {
                LogUtil.infoLog(this.getClass().getName(), CreditDefine.CHECK_CAN_CREDIT, "用户未登录");
                ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
                ret.put(CustomConstants.DATA, null);
                ret.put(CustomConstants.MSG, "用户未登录");
            }
        } catch (Exception e) {
            LogUtil.infoLog(this.getClass().getName(), CreditDefine.CHECK_CAN_CREDIT, "系统异常");
            ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
            ret.put(CustomConstants.DATA, null);
            ret.put(CustomConstants.MSG, "系统异常");
        }
        LogUtil.endLog(CreditController.class.toString(), CreditDefine.CHECK_CAN_CREDIT);
        return JSONObject.toJSONString(ret, true);
    }

	/**
	 * 债转提交保存
	 * @param request
	 * @param response
	 * @param tenderBorrowCreditCustomize
	 * @return
	 */
	@RequestMapping(value = CreditDefine.TENDER_TO_CREDIT_SAVE, method = RequestMethod.POST)
	public ModelAndView saveTenderToCredit(HttpServletRequest request, HttpServletResponse response, @ModelAttribute TenderBorrowCreditCustomize tenderBorrowCreditCustomize) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_LIST);
		ModelAndView modelAndView = new ModelAndView();
		Integer userId = null;
		WebViewUser user = null;
		// 神策数据统计 add by liuyang 20180726 start
		String presetProps = request.getParameter("presetProps");
		// 神策数据统计 add by liuyang 20180726 end
		try {
			userId = WebUtils.getUserId(request); // 用户ID
			_log.info("用户发生转让....userId is:{}", userId);
			user = WebUtils.getUser(request);
			if (user != null) {
				Borrow borrow = this.tenderCreditService.seachBorrowInfo(tenderBorrowCreditCustomize.getBorrowNid());
				boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrow.getBorrowStyle()) || CustomConstants.BORROW_STYLE_MONTH.equals(borrow.getBorrowStyle())
						|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrow.getBorrowStyle())|| CustomConstants.BORROW_STYLE_END.equals(borrow.getBorrowStyle());

				String dayOrMonth ="";
				String lockPeriod = String.valueOf(borrow.getBorrowPeriod());
				if(isMonth){//月标
					dayOrMonth = lockPeriod + "个月散标";
				}else{
					dayOrMonth = lockPeriod + "天散标";
				}
				UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
				userOperationLogEntity.setOperationType(5);
				userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
				userOperationLogEntity.setPlatform(0);
				userOperationLogEntity.setRemark(dayOrMonth);
				userOperationLogEntity.setOperationTime(new Date());
				userOperationLogEntity.setUserName(user.getUsername());
				userOperationLogEntity.setUserRole(user.getRoleId());
				loginService.sendUserLogMQ(userOperationLogEntity);
				// 获取当前时间的日期
				String nowDate = (GetDate.yyyyMMdd.format(new Date()) != null && !"".equals(GetDate.yyyyMMdd.format(new Date()))) ? GetDate.yyyyMMdd.format(new Date()) : "0";
				Integer creditedNum = tenderCreditService.tenderAbleToCredit(userId, Integer.parseInt(nowDate));
				if (creditedNum != null && creditedNum >= 3) {
					modelAndView = new ModelAndView(CreditDefine.CREDIT_ERROR_PATH);
					modelAndView.addObject("errorMessage", "今天的转让次数已满3次,请明天再试");
					LogUtil.endLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SAVE);
					return modelAndView;
				}
				// 验证BorrowNid和TenderNid
				if (StringUtils.isEmpty(tenderBorrowCreditCustomize.getBorrowNid()) || StringUtils.isEmpty(tenderBorrowCreditCustomize.getTenderNid())) {
					modelAndView = new ModelAndView(CreditDefine.CREDIT_ERROR_PATH);
					modelAndView.addObject("errorMessage", "无法获取债转借款编号和出借编号");
					LogUtil.endLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SAVE);
					return modelAndView;
				}
				// 验证折让率
				//新增配置表校验
				List<DebtConfig> config = debtConfigService.selectDebtConfigList();
				if (StringUtils.isEmpty(tenderBorrowCreditCustomize.getCreditDiscount())||CollectionUtils.isEmpty(config)) {
					modelAndView = new ModelAndView(CreditDefine.CREDIT_ERROR_PATH);
					modelAndView.addObject("errorMessage", "折让率不能为空");
					LogUtil.endLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SAVE);
					return modelAndView;
				} else {
					_log.info("creditDiscount:"+ tenderBorrowCreditCustomize.getCreditDiscount());
					if (tenderBorrowCreditCustomize.getCreditDiscount().matches("\\d{1,2}\\.\\d{1}")) {
						float creditDiscount = Float.parseFloat(tenderBorrowCreditCustomize.getCreditDiscount());
						DebtConfig debtConfig = config.get(0);
						if (creditDiscount > debtConfig.getConcessionRateUp().floatValue() || creditDiscount < debtConfig.getConcessionRateDown().floatValue()) {
							modelAndView = new ModelAndView(CreditDefine.CREDIT_ERROR_PATH);
							modelAndView.addObject("errorMessage", "折让率范围错误");
							LogUtil.endLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SAVE);
							return modelAndView;
						}
					} else {
						modelAndView = new ModelAndView(CreditDefine.CREDIT_ERROR_PATH);
						modelAndView.addObject("errorMessage", "折让率格式错误");
						LogUtil.endLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SAVE);
						return modelAndView;
					}
				}
				// 验证手机验证码
				if (StringUtils.isEmpty(tenderBorrowCreditCustomize.getTelcode())) {
					modelAndView = new ModelAndView(CreditDefine.CREDIT_ERROR_PATH);
					modelAndView.addObject("errorMessage", "请输入手机验证码");
					LogUtil.endLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SAVE);
					return modelAndView;
				} else {
					int cnt = this.tenderCreditService.checkMobileCode(user.getMobile(), tenderBorrowCreditCustomize.getTelcode());
					if (cnt <= 0) {
						modelAndView = new ModelAndView(CreditDefine.CREDIT_ERROR_PATH);
						modelAndView.addObject("errorMessage", "手机验证码错误");
						LogUtil.endLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SAVE);
						return modelAndView;
					}
				}
				// 服务费授权校验
				if (!authService.checkPaymentAuthStatus(userId)) {
				    modelAndView = new ModelAndView(CreditDefine.CREDIT_ERROR_PATH);
                    modelAndView.addObject("errorMessage", "该产品需开通服务费授权功能");
                    LogUtil.endLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SAVE);
                    return modelAndView;
                }
				// 债转保存
				Integer tenderCredit = tenderCreditService.insertTenderToCredit(userId, tenderBorrowCreditCustomize, "0");
				if (tenderCredit != null && tenderCredit > 0) {
					// add 合规数据上报 埋点 liubin 20181122 start
					// 推送数据到MQ 转让 散
					JSONObject params = new JSONObject();
					params.put("creditNid", tenderCredit+"");
					params.put("flag", "1"); //1（散）2（智投）
					params.put("status", "0"); //0转让
					this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.TRANSFER_SUCCESS_DELAY_KEY, JSONObject.toJSONString(params));
					// add 合规数据上报 埋点 liubin 20181122 end

					List<BorrowCredit> borrowCreditList = tenderCreditService.selectBorrowCreditByNid(String.valueOf(tenderCredit));
					if (borrowCreditList != null && borrowCreditList.size() > 0) {
						modelAndView.setViewName(CreditDefine.TENDER_TO_CREDIT_RESULT_PATH);
						BorrowCredit creditTender = borrowCreditList.get(0);
						modelAndView.addObject("creditEndTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(creditTender.getAddTime() + 3 * 24 * 3600));
						modelAndView.addObject("borrowCredit", creditTender);

						// add by liuyang 神策数据统计 20180726 start
						if (StringUtils.isNotBlank(presetProps)) {
							_log.info("神策预置属性:" + presetProps);
							SensorsDataBean sensorsDataBean = new SensorsDataBean();
							// 将json串转换成Bean
							try {
								Map<String, Object> sensorsDataMap = JSONObject.parseObject(presetProps, new TypeReference<Map<String, Object>>() {
								});
								sensorsDataBean.setPresetProps(sensorsDataMap);
								sensorsDataBean.setUserId(userId);
								sensorsDataBean.setEventCode("submit_credit_assign");
								sensorsDataBean.setCreditNid(String.valueOf(tenderCredit));
								// 发送神策数据统计MQ
								this.tenderCreditService.sendSensorsDataMQ(sensorsDataBean);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						// add by liuyang 神策数据统计  20180726 end
						return modelAndView;
					}
				} else {
					modelAndView = new ModelAndView(CreditDefine.CREDIT_ERROR_PATH);
					modelAndView.addObject("errorMessage", "保存时出现异常");
					LogUtil.endLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SAVE);
					return modelAndView;
				}
			}
		} catch (Exception e) {
			modelAndView = new ModelAndView(CreditDefine.CREDIT_ERROR_PATH);
			modelAndView.addObject("errorMessage", "系统异常,请稍后再试");
			LogUtil.endLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SAVE);
			return modelAndView;
		}
		LogUtil.endLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SAVE);
		return modelAndView;
	}

	/**
	 * 用户中心查询转让中债转列表资源
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = CreditDefine.CREDIT_INPROGRESS_LIST, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String searchCreditInProgressList(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.CREDIT_INPROGRESS_LIST);
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		Integer userId = null;
		try {
			userId = WebUtils.getUserId(request); // 用户ID
			if (userId != null && userId.intValue() != 0) {
				// 获取当前页码
				Integer currPage = (request.getParameter("currPage") != null && Integer.parseInt(request.getParameter("currPage")) > 0) ? Integer.parseInt(request.getParameter("currPage")) : 1;
				// 获取分页条数
				Integer limitPage = (request.getParameter("limitPage") != null && Integer.parseInt(request.getParameter("limitPage")) > 0) ? Integer.parseInt(request.getParameter("limitPage")) : 10;
				// 查询相应的汇消费列表的总数
				int recordTotal = this.tenderCreditService.countCreditInProgress(userId);
				if (recordTotal > 0) {
					WebPaginator paginator = new WebPaginator(currPage, recordTotal, limitPage, "creditInprogressPage", null);
					// 查询汇消费列表数据
					List<TenderCreditDetailCustomize> recordList = tenderCreditService.selectCreditInProgress(userId, paginator.getOffset(), paginator.getLimit());
					info.put("paginator", paginator);
					info.put("recordList", recordList);
				} else {
					WebPaginator paginator = new WebPaginator(currPage, recordTotal);
					info.put("paginator", paginator);
					info.put("recordList", "");
				}
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
				ret.put(CustomConstants.DATA, info);
				ret.put(CustomConstants.MSG, "");
			} else {
				LogUtil.infoLog(this.getClass().getName(), "searchCreditInProgressList", "用户未登录");
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
				ret.put("resultCode", "");
				ret.put(CustomConstants.DATA, null);
				ret.put(CustomConstants.MSG, "用户未登录");
			}
		} catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), "searchCreditInProgressList", "系统异常");
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
			ret.put("resultCode", "");
			ret.put(CustomConstants.DATA, null);
			ret.put(CustomConstants.MSG, "系统异常");
		}
		LogUtil.endLog(CreditController.class.toString(), CreditDefine.CREDIT_INPROGRESS_LIST);
		return JSONObject.toJSONString(ret, true);
	}

	/**
	 * 用户中心查询转让记录
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = CreditDefine.CREDIT_RECORD_LIST, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String searchCreditRecordList(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.CREDIT_RECORD_LIST);
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		Integer userId = null;
		try {
			userId = WebUtils.getUserId(request); // 用户ID
			if (userId != null && userId.intValue() != 0) {
				// 获取当前页码
				Integer currPage = (request.getParameter("currPage") != null && Integer.parseInt(request.getParameter("currPage")) > 0) ? Integer.parseInt(request.getParameter("currPage")) : 1;
				// 获取分页条数
				Integer limitPage = (request.getParameter("limitPage") != null && Integer.parseInt(request.getParameter("limitPage")) > 0) ? Integer.parseInt(request.getParameter("limitPage")) : 10;
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("userId", userId);

				// 查询相应的汇消费列表的总数
				int recordTotal = this.tenderCreditService.countCreditRecordTotal(params);
				if (recordTotal > 0) {
					WebPaginator paginator = new WebPaginator(currPage, recordTotal, limitPage, "creditInprogressPage", null);
					// 查询汇消费列表数据
					params.put("limitStart", paginator.getOffset());
					params.put("limitEnd", paginator.getLimit());
					List<TenderCreditDetailCustomize> recordList = tenderCreditService.selectCreditRecordList(params);
					info.put("paginator", paginator);
					info.put("recordList", recordList);
				} else {
					WebPaginator paginator = new WebPaginator(currPage, recordTotal);
					info.put("paginator", paginator);
					info.put("recordList", "");
				}
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
				ret.put(CustomConstants.DATA, info);
				ret.put(CustomConstants.MSG, "");
			} else {
				LogUtil.infoLog(this.getClass().getName(), "searchCreditInProgressList", "用户未登录");
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
				ret.put("resultCode", "");
				ret.put(CustomConstants.DATA, null);
				ret.put(CustomConstants.MSG, "用户未登录");
			}
		} catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), "searchCreditInProgressList", "系统异常");
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
			ret.put("resultCode", "");
			ret.put(CustomConstants.DATA, null);
			ret.put(CustomConstants.MSG, "系统异常");
		}
		LogUtil.endLog(CreditController.class.toString(), CreditDefine.CREDIT_RECORD_LIST);
		return JSONObject.toJSONString(ret, true);
	}

	/***
	 * 用户中心查询已完成债转列表资源
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = CreditDefine.CREDIT_STOP_LIST, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String searchCreditStopList(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.CREDIT_STOP_LIST);
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		Integer userId = null;
		try {
			userId = WebUtils.getUserId(request); // 用户ID
			if (userId != null && userId.intValue() != 0) {
				// 获取当前页码
				Integer currPage = (request.getParameter("currPage") != null && Integer.parseInt(request.getParameter("currPage")) > 0) ? Integer.parseInt(request.getParameter("currPage")) : 1;
				// 获取分页条数
				Integer limitPage = (request.getParameter("limitPage") != null && Integer.parseInt(request.getParameter("limitPage")) > 0) ? Integer.parseInt(request.getParameter("limitPage")) : 10;
				// 查询相应的汇消费列表的总数
				int recordTotal = this.tenderCreditService.countCreditStop(userId);
				if (recordTotal > 0) {
					WebPaginator paginator = new WebPaginator(currPage, recordTotal, limitPage, "creditStopPage", null);
					// 查询汇消费列表数据
					List<TenderCreditDetailCustomize> recordList = tenderCreditService.selectCreditStop(userId, paginator.getOffset(), paginator.getLimit());
					info.put("paginator", paginator);
					info.put("recordList", recordList);
				} else {
					WebPaginator paginator = new WebPaginator(currPage, recordTotal);
					info.put("paginator", paginator);
					info.put("recordList", "");
				}
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
				ret.put(CustomConstants.DATA, info);
				ret.put(CustomConstants.MSG, "");
			} else {
				LogUtil.infoLog(this.getClass().getName(), "searchCreditStopList", "用户未登录");
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
				ret.put("resultCode", "");
				ret.put(CustomConstants.DATA, null);
				ret.put(CustomConstants.MSG, "用户未登录");
			}
		} catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), "searchCreditStopList", "系统异常");
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
			ret.put("resultCode", "");
			ret.put(CustomConstants.DATA, null);
			ret.put(CustomConstants.MSG, "系统异常");
		}
		LogUtil.endLog(CreditController.class.toString(), CreditDefine.CREDIT_STOP_LIST);
		return JSONObject.toJSONString(ret, true);
	}

	/**
	 * 用户中心查询已承接债转列表资源
	 * @param request
	 * @param response
	 * @param tenderCreditAssignedBean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = CreditDefine.CREDIT_ASSIGN_LIST, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String searchCreditAssignList(HttpServletRequest request, HttpServletResponse response, CreditAssignedBean tenderCreditAssignedBean) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.CREDIT_ASSIGN_LIST);
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		Map<String, Object> params = new HashMap<String, Object>();
		Integer userId = null;
		try {
			userId = WebUtils.getUserId(request); // 用户ID
			if (userId != null && userId.intValue() != 0) {
				LogUtil.infoLog(this.getClass().getName(), "searchWebCreditTender", "汇转让我的账户,用户已登录");
				// 获取当前页码
				Integer currPage = (request.getParameter("currPage") != null && Integer.parseInt(request.getParameter("currPage")) > 0) ? Integer.parseInt(request.getParameter("currPage")) : 1;
				// 获取分页条数
				Integer limitPage = (request.getParameter("limitPage") != null && Integer.parseInt(request.getParameter("limitPage")) > 0) ? Integer.parseInt(request.getParameter("limitPage")) : 10;
				params.put("userId", userId);
				// 年化收益排序
				String bidAprSort = "DESC";
				if ("DESC".equals(request.getParameter("bidAprSort")) || "ASC".equals(request.getParameter("bidAprSort"))) {
					bidAprSort = String.valueOf(request.getParameter("bidAprSort"));
					params.put("bidAprSort", bidAprSort);
				}
				// 期限排序
				String creditTermSort = "DESC";
				if ("DESC".equals(request.getParameter("creditTermSort")) || "ASC".equals(request.getParameter("creditTermSort"))) {
					creditTermSort = String.valueOf(request.getParameter("creditTermSort"));
					params.put("capitalSort", creditTermSort);
				}
				// 查询相应的汇消费列表的总数
				int recordTotal = this.tenderCreditService.countCreditAssigned(params);
				if (recordTotal > 0) {
					WebPaginator paginator = new WebPaginator(currPage, recordTotal, limitPage, "creditAssignPage", null);
					params.put("limitStart", paginator.getOffset());
					params.put("limitEnd", paginator.getLimit());
					// 查询汇消费列表数据
					long timestamp = System.currentTimeMillis() / 1000;
					params.put("timestamp", String.valueOf(timestamp));
					List<TenderCreditAssignedCustomize> recordList = tenderCreditService.selectCreditAssigned(params);
					info.put("phpWebHost", PropUtils.getSystem("hyjf.web.host.php"));
					info.put("timestamp", timestamp);
					info.put("paginator", paginator);
					info.put("recordList", recordList);
				} else {
					WebPaginator paginator = new WebPaginator(currPage, recordTotal);
					info.put("paginator", paginator);
					info.put("recordList", "");
				}
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
				ret.put(CustomConstants.DATA, info);
				ret.put(CustomConstants.MSG, "");
				LogUtil.endLog(CreditController.class.toString(), CreditDefine.CREDIT_ASSIGN_LIST);
			} else {
				LogUtil.infoLog(this.getClass().getName(), "searchCreditAssignList", "用户未登录");
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
				ret.put("resultCode", "");
				ret.put(CustomConstants.DATA, info);
				ret.put(CustomConstants.MSG, "用户未登录");
			}
		} catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), "searchCreditAssignList", "系统异常");
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
			ret.put("resultCode", "");
			ret.put(CustomConstants.DATA, info);
			ret.put(CustomConstants.MSG, "系统异常");
		}
		return JSONObject.toJSONString(ret, true);
	}

	/**
	 * 用户中心查询 已承接债转的 出借详情列表资源
	 * @param request
	 * @param response
	 * @param tenderCreditAssignedBean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = CreditDefine.CREDIT_ASSIGN_TENDER_LIST, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String searchCreditTenderAssignList(HttpServletRequest request, HttpServletResponse response, CreditAssignedBean tenderCreditAssignedBean) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.CREDIT_ASSIGN_TENDER_LIST);
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		Map<String, Object> params = new HashMap<String, Object>();
		Integer userId = null;
		try {
			userId = WebUtils.getUserId(request); // 用户ID
			if (userId != null && userId.intValue() != 0) {
				LogUtil.infoLog(this.getClass().getName(), "searchCreditTenderAssignList", "汇转让我的账户,用户已登录");
				// params.put("creditUserId", userId);
				// creditNid
				if (StringUtils.isEmpty(tenderCreditAssignedBean.getCreditNid())) {
					ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
					ret.put(CustomConstants.DATA, null);
					ret.put(CustomConstants.MSG, "无法获取债转编号");
					LogUtil.endLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SAVE);
					return JSONObject.toJSONString(ret, true);
				} else {
					params.put("creditNid", tenderCreditAssignedBean.getCreditNid());
				}
				// 查询相应的汇消费列表的总数
				int recordTotal = this.tenderCreditService.countCreditAssigned(params);
				if (recordTotal > 0) {
					// 查询汇消费列表数据
					long timestamp = System.currentTimeMillis() / 1000;
					params.put("timestamp", String.valueOf(timestamp));
					List<TenderCreditAssignedCustomize> recordList = tenderCreditService.selectCreditAssigned(params);
					info.put("phpWebHost", PropUtils.getSystem("hyjf.web.host.php"));
					info.put("timestamp", timestamp);
					info.put("recordList", recordList);
				} else {
					info.put("recordList", "");
				}

				// 债转承接记录统计
				List<TenderCreditAssignedStatisticCustomize> statisticList = tenderCreditService.selectCreditTenderAssignedStatistic(params);
				if (statisticList != null & !statisticList.isEmpty()) {
					info.put("statisticRecord", statisticList.get(0));
				} else {
					info.put("statisticRecord", "");
				}
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
				ret.put(CustomConstants.DATA, info);
				ret.put(CustomConstants.MSG, "");
				LogUtil.endLog(CreditController.class.toString(), CreditDefine.CREDIT_ASSIGN_TENDER_LIST);
			} else {
				LogUtil.infoLog(this.getClass().getName(), "searchCreditAssignList", "用户未登录");
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
				ret.put(CustomConstants.DATA, null);
				ret.put(CustomConstants.MSG, "用户未登录");
			}
		} catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), "searchCreditTenderAssignList", "系统异常");
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
			ret.put(CustomConstants.DATA, null);
			ret.put(CustomConstants.MSG, "系统异常");
		}
		return JSONObject.toJSONString(ret, true);
	}

	/**
	 * 用户中心查询 已承接债转的 还款计划列表资源
	 * @param request
	 * @param response
	 * @param tenderCreditAssignedBean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = CreditDefine.CREDIT_REPAY_PLAN_LIST, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String searchCreditRepayPlanList(HttpServletRequest request, HttpServletResponse response, CreditAssignedBean tenderCreditAssignedBean) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.CREDIT_REPAY_PLAN_LIST);
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		Map<String, Object> params = new HashMap<String, Object>();
		Integer userId = null;
		try {
			userId = WebUtils.getUserId(request); // 用户ID
			if (userId != null && userId.intValue() != 0) {
				LogUtil.infoLog(this.getClass().getName(), "searchCreditTenderAssignList", "汇转让我的账户,用户已登录");
				if (StringUtils.isEmpty(tenderCreditAssignedBean.getCreditNid()) || StringUtils.isEmpty(tenderCreditAssignedBean.getAssignNid())) {
					ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
					ret.put(CustomConstants.DATA, null);
					ret.put(CustomConstants.MSG, "无法获取债转编号与承接编号");
					LogUtil.endLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SAVE);
					return JSONObject.toJSONString(ret, true);
				} else {
					params.put("creditNid", tenderCreditAssignedBean.getCreditNid());
					params.put("assignNid", tenderCreditAssignedBean.getAssignNid());
				}
				// 查询已承接还款计划列表数据
				List<TenderCreditRepayPlanCustomize> recordList = tenderCreditService.selectCreditRepayPlanList(params);
				if (recordList != null && recordList.size() > 0) {
					info.put("recordList", recordList);
				} else {
					info.put("recordList", "");
				}
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
				ret.put(CustomConstants.DATA, info);
				ret.put(CustomConstants.MSG, "");
				LogUtil.endLog(CreditController.class.toString(), CreditDefine.CREDIT_REPAY_PLAN_LIST);
			} else {
				LogUtil.infoLog(this.getClass().getName(), "searchCreditAssignList", "用户未登录");
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
				ret.put(CustomConstants.DATA, null);
				ret.put(CustomConstants.MSG, "用户未登录");
			}
		} catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), "searchCreditTenderAssignList", "系统异常");
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
			ret.put(CustomConstants.DATA, null);
			ret.put(CustomConstants.MSG, "系统异常");
		}
		return JSONObject.toJSONString(ret, true);
	}



	/**
	 * 
	 * 债券转让初始页面
	 * 
	 * @author hsy
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = CreditDefine.WEB_CREDIT_PAGE_INIT)
	public ModelAndView initWebCreditPage(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.WEB_CREDIT_PAGE_INIT);
		ModelAndView modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_LIST_PATH);
		LogUtil.endLog(CreditController.class.toString(), CreditDefine.WEB_CREDIT_PAGE_INIT);
		return modelAndView;
	}

	/**
	 * 前端Web页面查询汇转让数据列表(包含查询条件)
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = CreditDefine.WEB_CREDIT_LIST, produces = "application/json; charset=utf-8")
	public CreditListAJaxBean searchWebCreditList(@ModelAttribute CreditListBean form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.WEB_CREDIT_LIST);
		CreditListAJaxBean resultBean = new CreditListAJaxBean();
		Map<String, Object> params = new HashMap<String, Object>();
		// 获取项目期限
		params.put("borrowPeriodMin", 0);
		params.put("borrowPeriodMax", 100);
		// 获取项目收益
		params.put("borrowAprMin", 0);
		params.put("borrowAprMax", 100);
		// 获取折价比例排序
		params.put("discountSort", "DESC");
		// 获取期限排序
		params.put("termSort", "DESC");
		// 获取金额排序
		params.put("capitalSort", "DESC");
		// 进度排序
		params.put("inProgressSort", "DESC");
		// 查询汇转让数据总数
		int recordTotal = this.tenderCreditService.countWebCredit(params);
		if (recordTotal > 0) {
			// add by cwyang 2017-5-26 项目列表默认显示为2页
			// add by cwyang 项目列表显示2页
			// modify by liuyang 期限债转列表只显示两页限制 松超提成 start
//			int pageNum = 2;
//			if (recordTotal > form.getPageSize() * pageNum) {
//				recordTotal = form.getPageSize() * pageNum;
//			}
			// end
			// modify by liuyang 期限债转列表只显示两页限制 松超提成 end
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
			// 查询汇转让列表数据
			List<TenderCreditDetailCustomize> recordList = tenderCreditService.selectWebCreditList(params, paginator.getOffset(), paginator.getLimit());
			resultBean.setPaginator(paginator);
			resultBean.setProjectList(recordList);
			resultBean.success();
		} else {
			Paginator paginator = new Paginator(form.getPaginatorPage(), 0);
			resultBean.setPaginator(paginator);
			resultBean.setProjectList(new ArrayList<TenderCreditDetailCustomize>());
			resultBean.success();
		}
		LogUtil.endLog(CreditController.class.toString(), CreditDefine.WEB_CREDIT_LIST);
		return resultBean;
	}

	/**
	 * 前端Web页面出借可债转出借(包含查询条件)
	 * @param request
	 * @param response
	 * @param creditNid
	 * @return
	 */
	@RequestMapping(value = CreditDefine.WEB_CREDIT_TENDER)
	public ModelAndView searchWebCreditTender(HttpServletRequest request, HttpServletResponse response, @RequestParam String creditNid) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.WEB_CREDIT_TENDER);
		ModelAndView modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_DETAIL_OLD_PATH);
		// 数据校验
		if (StringUtils.isBlank(creditNid)) {
			modelAndView = new ModelAndView(BorrowDefine.ERROR_PTAH);
			return modelAndView;
		}
		// 获取债转的详细参数
		TenderToCreditDetailCustomize creditDetail = this.tenderCreditService.selectWebCreditDetail(creditNid);
		if (Validator.isNull(creditDetail)) {
			modelAndView = new ModelAndView(BorrowDefine.ERROR_PTAH);
			return modelAndView;
		}
		// 获取相应的标的详情
		String borrowNid = creditDetail.getBorrowNid();
		BorrowWithBLOBs borrow = this.tenderCreditService.getBorrowByNid(borrowNid);
		if (Validator.isNull(borrow)) {
			modelAndView = new ModelAndView(BorrowDefine.ERROR_PTAH);
			return modelAndView;
		}
		//modelAndView.addObject("investLevel", borrow.getInvestLevel());
		creditDetail.setInvestLevel( borrow.getInvestLevel());
		// 项目类型
		int projectType = borrow.getProjectType();
		// 企业标的用户标的区分
		String comOrPer = StringUtils.isBlank(borrow.getCompanyOrPersonal()) ? "0" : borrow.getCompanyOrPersonal();
		if (borrow.getIsNew() == 0) {
			modelAndView.addObject("creditDetail", creditDetail);
			WebProjectDetailCustomize projectDetail = projectService.selectProjectDetail(borrowNid);
			modelAndView.addObject("projectDeatil", projectDetail);
			// 如果项目为汇资产项目
			if (projectType == 9) {
				// 4查询相应的汇资产的首页信息
				WebHzcProjectDetailCustomize borrowInfo = this.projectService.searchHzcProjectDetail(borrowNid);
				modelAndView.addObject("borrowInfo", borrowInfo);
				// 处置预案
				WebHzcDisposalPlanCustomize disposalPlan = this.projectService.searchDisposalPlan(borrowNid);
				modelAndView.addObject("disposalPlan", disposalPlan);
			}
			// 项目为非汇资产项目
			else {
				// 4查询非汇资产项目的项目信息
				if (comOrPer.equals("1")) {
					// 查询相应的企业项目详情
					WebProjectCompanyDetailCustomize borrowInfo = projectService.searchProjectCompanyDetail(borrowNid);
					modelAndView.addObject("borrowInfo", borrowInfo);
				} else if (comOrPer.equals("2")) {
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
				List<WebVehiclePledgeCustomize> vehiclePledgeList = this.projectService.selectVehiclePledgeList(borrowNid);
				// 添加相应的汽车抵押信息
				modelAndView.addObject("vehiclePledgeList", vehiclePledgeList);
			}
			// 获取图片信息
			List<BorrowFileCustomBean> projectFileList = projectService.searchProjectFiles(borrowNid, CustomConstants.HOST);
			modelAndView.addObject("borrowFiles", projectFileList);
			// 还款计划
			List<BorrowRepayPlanCustomBean> repayPlanList = this.projectService.getRepayPlan(borrowNid);
			modelAndView.addObject("repayPlanList", repayPlanList);
		} else {
			modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_DETAIL_NEW_PATH);
			modelAndView.addObject("creditDetail", creditDetail);
			WebProjectDetailCustomize projectDetail = projectService.selectProjectDetail(borrowNid);
			modelAndView.addObject("projectDeatil", projectDetail);
			/**
			 * 借款类型 1、企业借款 2、借款人 3、汇资产
			 */
			modelAndView.addObject("borrowType", comOrPer);
			// 借款人企业信息
			BorrowUsers borrowUsers = projectService.getBorrowUsersByNid(borrowNid);
			// 借款人信息
			BorrowManinfo borrowManinfo = projectService.getBorrowManinfoByNid(borrowNid);
			// 房产抵押信息
			List<BorrowHouses> borrowHousesList = projectService.getBorrowHousesByNid(borrowNid);
			// 车辆抵押信息
			List<BorrowCarinfo> borrowCarinfoList = projectService.getBorrowCarinfoByNid(borrowNid);
			// 还款计划
			List<BorrowRepayPlanCustomBean> repayPlanList = this.projectService.getRepayPlan(borrowNid);
			modelAndView.addObject("repayPlanList", repayPlanList);
			// 相关文件
			List<BorrowFileCustomBean> files = this.projectService.searchProjectFiles(borrowNid, CustomConstants.HOST);
			modelAndView.addObject("fileList", files);
			// 还款信息
			BorrowRepay borrowRepay = projectService.getBorrowRepay(borrowNid);
			
			// 资产列表
			JSONArray json = new JSONArray();
			// 基础信息
			String baseTableData = "";
			// 资产信息
			String assetsTableData = "";
			// 项目介绍
			String intrTableData = "";
			// 信用状况
			String credTableData = "";
			// 审核信息
			String reviewTableData = "";
			//其他信息
			String otherTableData = "";
			// 借款类型
			int borrowType = Integer.parseInt(comOrPer);
			if (borrowType == 1 && borrowUsers != null) {
				// 基础信息
				baseTableData = JSONObject.toJSONString(packDetail(borrowUsers, 1, borrowType, projectDetail.getBorrowLevel()));
				// 信用状况
				credTableData = JSONObject.toJSONString(packDetail(borrowUsers, 4, borrowType, projectDetail.getBorrowLevel()));
				// 审核信息
				reviewTableData = JSONObject.toJSONString(packDetail(borrowUsers, 5, borrowType, projectDetail.getBorrowLevel()));
				//其他信息
				otherTableData =  JSONObject.toJSONString(packDetail(borrowUsers, 6, borrowType, borrow.getBorrowLevel()));
			} else {
				if (borrowManinfo != null) {
					// 基础信息
					baseTableData = JSONObject.toJSONString(packDetail(borrowManinfo, 1, borrowType, projectDetail.getBorrowLevel()));
					// 信用状况
					credTableData = JSONObject.toJSONString(packDetail(borrowManinfo, 4, borrowType, projectDetail.getBorrowLevel()));
					// 审核信息
					reviewTableData = JSONObject.toJSONString(packDetail(borrowManinfo, 5, borrowType, projectDetail.getBorrowLevel()));
					//其他信息
					otherTableData =  JSONObject.toJSONString(packDetail(borrowManinfo, 6, borrowType, borrow.getBorrowLevel()));
				}
			}
			// 资产信息
			if (borrowHousesList != null && borrowHousesList.size() > 0) {
				for (BorrowHouses borrowHouses : borrowHousesList) {
					json.add(packDetail(borrowHouses, 2, borrowType, projectDetail.getBorrowLevel()));
				}
			}
			if (borrowCarinfoList != null && borrowCarinfoList.size() > 0) {
				for (BorrowCarinfo borrowCarinfo : borrowCarinfoList) {
					json.add(packDetail(borrowCarinfo, 2, borrowType, projectDetail.getBorrowLevel()));
				}
			}
			assetsTableData = json.toString();
			// 项目介绍
			intrTableData = JSONObject.toJSONString(packDetail(projectDetail, 3, borrowType, projectDetail.getBorrowLevel()));
			// 基础信息
			modelAndView.addObject("baseTableData", baseTableData);
			// 资产信息
			modelAndView.addObject("assetsTableData", assetsTableData);
			// 项目介绍
			modelAndView.addObject("intrTableData", intrTableData);
			// 信用状况
			modelAndView.addObject("credTableData", credTableData);
			// 审核信息
			modelAndView.addObject("reviewTableData", reviewTableData);
			// 信批需求新增(放款后才显示)
			if(borrow.getStatus()>=4 && borrowRepay != null){
				//其他信息
				modelAndView.addObject("otherTableData", otherTableData);
				modelAndView.addObject("updateTime", getUpdateTime(Integer.parseInt(borrowRepay.getAddtime()), StringUtils.isBlank(borrowRepay.getRepayYestime())?null:Integer.parseInt(borrowRepay.getRepayYestime())));
			}else{
				//其他信息
				modelAndView.addObject("otherTableData", JSONObject.toJSONString(new ArrayList<BorrowDetailBean>()));
			}
		}
		// 剩余可承接金额
		BigDecimal creditAssignCapital = new BigDecimal(creditDetail.getCreditAssignCapital());
		if (creditAssignCapital.compareTo(new BigDecimal("100")) <= 0) {
			modelAndView.addObject("isLast", "1");// 是否是最后一笔
		} else {
			modelAndView.addObject("isLast", "0");
		}
		// 登陆用户
		WebViewUser user = WebUtils.getUser(request);
		try {
			if (Validator.isNotNull(user)) {
				Integer userId = user.getUserId(); // 用户ID
				Users users = tenderCreditService.getUsersByUserId(userId);
				modelAndView.addObject("loginFlag", "1");// 判断是否登录
				modelAndView.addObject("openFlag", user.isBankOpenAccount() ? "1" : "0");// 判断是否开户
				modelAndView.addObject("setPwdFlag", users.getIsSetPassword()); // 是否设置交易密码
				modelAndView.addObject("isUserValid", users.getStatus());
				// 合规三期   服务费授权开关和状态
				modelAndView.addObject("paymentAuthOn", CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus());
				modelAndView.addObject("paymentAuthStatus", users.getPaymentAuthStatus()); // 缴费授权
				modelAndView.addObject("isCheckUserRole",PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN));
				modelAndView.addObject("roleId", user.getRoleId()); // 借款人和垫付机构不能出借
				UsersInfo usersInfo = tenderCreditService.getUsersInfoByUserId(userId);
				if (usersInfo.getVipId() != null && usersInfo.getVipId() != 0) {
					modelAndView.addObject("ifVip", 1);
					String returl = CustomConstants.HOST + VIPManageDefine.REQUEST_MAPPING + "/" + VIPManageDefine.INIT_ACTION + ".do";
					modelAndView.addObject("returl", returl);
				} else {
					modelAndView.addObject("ifVip", 0);
					String returl = CustomConstants.HOST + ApplyDefine.REQUEST_MAPPING + ApplyDefine.INIT + ".do";
					modelAndView.addObject("returl", returl);
				}
				// 获取用户信息
				Account account = tenderCreditService.getAccount(userId);
				// 可用余额
				modelAndView.addObject("balance", account.getBankBalance().toString());
				// 风险测评改造 mod by liuyang 20180111 start
				// 风险测评标识
				// JSONObject jsonObject = CommonSoaUtils.getUserEvalationResultByUserId(userId + "");
//				modelAndView.addObject("riskFlag", String.valueOf(user.getIsEvaluationFlag()));
				try {
					// 5. 用户是否完成风险测评标识：0未测评 1已测评
					// modify by liuyang 20180411 用户是否完成风险测评标识 start
//					JSONObject jsonObject = CommonSoaUtils.getUserEvalationResultByUserId(userId + "");
//					logger.info("风险测评结果： {}", jsonObject.toJSONString());
//					if ((Integer)jsonObject.get("userEvaluationResultFlag") == 1) {
//						userValidation.setRiskTested(Boolean.TRUE);
//					} else {
//						userValidation.setRiskTested(Boolean.FALSE);
//					}
					if(user.getIsEvaluationFlag()==1 && null != user.getEvaluationExpiredTime()){
						//测评到期日
						Long lCreate = user.getEvaluationExpiredTime().getTime();
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
				LogUtil.infoLog(this.getClass().getName(), "searchWebCreditTender", "汇转让购买,用户已登录" + userId);
			} else {
				modelAndView.addObject("loginFlag", "0");// 判断是否登录
				modelAndView.addObject("openFlag", "0");
				modelAndView.addObject("loginFlag", "0");
				modelAndView.addObject("setPwdFlag", "0");
				modelAndView.addObject("isUserValid", "0");
				modelAndView.addObject("riskFlag", "0");// 是否进行过风险测评 0未测评 1已测评
				LogUtil.infoLog(this.getClass().getName(), "searchWebCreditTender", "汇转让购买,用户未登录");
			}
		} catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), "searchWebCreditTender", "汇转让购买,用户未登录");
		}
		//协议名称 动态获得
		List<ProtocolTemplate> list = agreementService.getdisplayNameDynamic();
		if(org.apache.commons.collections.CollectionUtils.isNotEmpty(list)){
			//是否在枚举中有定义
			for (ProtocolTemplate p : list) {
				String protocolType = p.getProtocolType();
				String alia = ProtocolEnum.getAlias(protocolType);
				if (alia != null){
					modelAndView.addObject(alia, p.getDisplayName());
				}
			}
		}
		LogUtil.endLog(CreditController.class.toString(), CreditDefine.WEB_CREDIT_TENDER);
		return modelAndView;
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
	
	private List<BorrowDetailBean> packDetail(Object objBean, int type, int borrowType, String borrowLevel) {
		List<BorrowDetailBean> detailBeanList = new ArrayList<BorrowDetailBean>();
		// 得到对象
		Class c = objBean.getClass();
		String currencyName ="元";
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
						// 封装bean
						BorrowDetailBean detailBean = new BorrowDetailBean();
						detailBean.setId(fName);
						detailBean.setVal(result.toString());
						if (type == 1) {
							if (borrowType == 2) {// 个人借款
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
									if ("1".equals(result.toString())) {
										detailBean.setVal("男");
									} else {
										detailBean.setVal("女");
									}
									detailBeanList.add(detailBean);
									break;
								case "old":
									detailBean.setKey("年龄");
									detailBeanList.add(detailBean);
									break;
								case "merry":
									detailBean.setKey("婚姻状况");
									if ("1".equals(result.toString())) {
										detailBean.setVal("已婚");
									} else {
										detailBean.setVal("未婚");
									}
									detailBeanList.add(detailBean);
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
							} else {// 企业借款
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
										detailBean.setVal(CustomConstants.DF_FOR_VIEW.format(detailBean.getVal()) + currencyName);
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
						} else if (type == 2) {
							switch (fName) {
							case "housesType":
								detailBean.setKey("资产类型");
								String houseType = this.projectService.getParamName("HOUSES_TYPE", detailBean.getVal());
								if(houseType != null){
									 detailBean.setVal(houseType);
								}else{
									detailBean.setVal("住宅");
								}
								detailBeanList.add(detailBean);
								break;
							case "housesArea":
								detailBean.setKey("资产面积");
								detailBeanList.add(detailBean);
								break;
							case "housesCnt":
								detailBean.setKey("资产数量");
								detailBeanList.add(detailBean);
								break;
							case "housesToprice":
								detailBean.setKey("评估价值");
								detailBeanList.add(detailBean);
								break;
							case "housesBelong":
								detailBean.setKey("资产所属");
								detailBeanList.add(detailBean);
								break;
							// 车辆
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
								detailBeanList.add(detailBean);
								break;
							case "toprice":
								detailBean.setKey("评估价值");
								detailBeanList.add(detailBean);
								break;
							case "number":
								detailBean.setKey("车牌号");
								detailBeanList.add(detailBean);
								break;
							case "registration":
								detailBean.setKey("车辆登记地");
								detailBeanList.add(detailBean);
								break;
							case "vin":
								detailBean.setKey("车架号");
								detailBeanList.add(detailBean);
								break;
							default:
								break;
							}

						} else if (type == 3) {
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
							case "secondPayment":// 还没有
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
						} else if (type == 4) {
							switch (fName) {
							case "overdueTimes":
								detailBean.setKey("在平台逾期次数");
								detailBeanList.add(detailBean);
								break;
							case "overdueAmount":
								detailBean.setKey("在平台逾期金额");
								detailBeanList.add(detailBean);
								break;
							case "litigation":
								detailBean.setKey("涉诉情况");
								detailBeanList.add(detailBean);
								break;
							default:
								break;
							}
						} else if (type == 5) {
							if (borrowType == 2) {
								switch (fName) {
								case "isCard":
									detailBean.setKey("身份证");
									if ("1".equals(result.toString())) {
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;
								case "isIncome":
									detailBean.setKey("收入状况");
									if ("1".equals(result.toString())) {
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;
								case "isCredit":
									detailBean.setKey("信用状况");
									if ("1".equals(result.toString())) {
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;
								case "isAsset":
									detailBean.setKey("资产状况");
									if ("1".equals(result.toString())) {
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;
								case "isVehicle":
									detailBean.setKey("车辆状况");
									if ("1".equals(result.toString())) {
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;
								case "isDrivingLicense":
									detailBean.setKey("行驶证");
									if ("1".equals(result.toString())) {
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;
								case "isVehicleRegistration":
									detailBean.setKey("车辆登记证");
									if ("1".equals(result.toString())) {
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;
								case "isMerry":
									detailBean.setKey("婚姻状况");
									if ("1".equals(result.toString())) {
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;
								case "isWork":
									detailBean.setKey("工作状况");
									if ("1".equals(result.toString())) {
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;
								case "isAccountBook":
									detailBean.setKey("户口本");
									if ("1".equals(result.toString())) {
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;
								default:
									break;
								}
							} else {
								switch (fName) {
								case "isCertificate":
									detailBean.setKey("企业证件");
									if ("1".equals(result.toString())) {
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;
								case "isOperation":
									detailBean.setKey("经营状况");
									if ("1".equals(result.toString())) {
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;
								case "isFinance":
									detailBean.setKey("财务状况");
									if ("1".equals(result.toString())) {
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;
								case "isEnterpriseCreidt":
									detailBean.setKey("企业信用");
									if ("1".equals(result.toString())) {
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;
								case "isLegalPerson":
									detailBean.setKey("法人信息");
									if ("1".equals(result.toString())) {
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;
								case "isAsset":
									detailBean.setKey("资产状况");
									if ("1".equals(result.toString())) {
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;
								case "isPurchaseContract":
									detailBean.setKey("购销合同");
									if ("1".equals(result.toString())) {
										detailBean.setVal("已审核");
										detailBeanList.add(detailBean);
									}
									break;
								case "isSupplyContract":
									detailBean.setKey("供销合同");
									if ("1".equals(result.toString())) {
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
		if (type == 1 || type == 4) {
			// 信用评级单独封装
			BorrowDetailBean detailBean = new BorrowDetailBean();
			detailBean.setId("borrowLevel");
			detailBean.setKey("信用评级");
			detailBean.setVal(borrowLevel);
			detailBeanList.add(detailBean);
		}
		return detailBeanList;
	}

	/** 还款计划分页请求 */
	@ResponseBody
	@RequestMapping(value = CreditDefine.WEB_CREDIT_REPAY_PAGE)
	public String searchWebCreditRepayPage(HttpServletRequest request, HttpServletResponse response, @RequestParam String borrowNid, @RequestParam Integer currPage, @RequestParam Integer limitPage) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.WEB_CREDIT_REPAY_PAGE);
		JSONObject ret = new JSONObject();
		Integer userId = null;
		try {
			userId = WebUtils.getUserId(request); // 用户ID
			if (userId != null) {
				LogUtil.infoLog(this.getClass().getName(), "searchWebCreditRepayPage", "汇转让购买-还款计划分页,用户已登录");
				if (StringUtils.isEmpty(borrowNid) || StringUtils.isEmpty(borrowNid)) {
					ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
					ret.put(CustomConstants.DATA, null);
					ret.put(CustomConstants.MSG, "借款编号不能为空");
					LogUtil.endLog(CreditController.class.toString(), CreditDefine.WEB_CREDIT_REPAY_PAGE);
					return JSONObject.toJSONString(ret, true);
				}
				// 获取当前页码
				currPage = (currPage != null && currPage > 0) ? currPage : 1;
				// 获取分页条数
				limitPage = (limitPage != null && limitPage > 0) ? limitPage : 8;
				// 获取债转的详细参数
				Map<String, Object> repayMap = tenderCreditService.selectWebCreditRepayList(borrowNid, currPage, limitPage);
				if (repayMap.get("repayPlan") != null) {
					ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
					ret.put(CustomConstants.DATA, repayMap);
					ret.put(CustomConstants.MSG, "查询完成");
				} else {
					ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
					ret.put(CustomConstants.DATA, null);
					ret.put(CustomConstants.MSG, "查询不到还款计划的还款信息");
				}
				LogUtil.endLog(CreditController.class.toString(), CreditDefine.WEB_CREDIT_REPAY_PAGE);
			} else {
				LogUtil.infoLog(this.getClass().getName(), "searchWebCreditRepayPage", "汇转让购买,用户未登录");
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
				ret.put("resultCode", "");
				ret.put(CustomConstants.DATA, null);
				ret.put(CustomConstants.MSG, "用户未登录");
			}
		} catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), "searchWebCreditRepayPage", "汇转让购买,用户未登录");
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
			ret.put("resultCode", "");
			ret.put(CustomConstants.DATA, null);
			ret.put(CustomConstants.MSG, "用户未登录");
		}
		return JSONObject.toJSONString(ret, true);
	}

	/** 出借记录分页请求 */
	@ResponseBody
	@RequestMapping(value = CreditDefine.WEB_CREDIT_TENDER_PAGE, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String searchWebCreditTenderPage(HttpServletRequest request, HttpServletResponse response, @RequestParam String creditNid, @RequestParam Integer currPage, @RequestParam Integer limitPage) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.WEB_CREDIT_TENDER_PAGE);
		JSONObject ret = new JSONObject();
		Integer userId = null;
		try {
			userId = WebUtils.getUserId(request); // 用户ID
			if (userId != null) {
				LogUtil.infoLog(this.getClass().getName(), "searchWebCreditTenderPage", "汇转让购买-出借记录分页,用户已登录");
				if (StringUtils.isEmpty(creditNid) || StringUtils.isEmpty(creditNid)) {
					ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
					ret.put(CustomConstants.DATA, null);
					ret.put(CustomConstants.MSG, "债转编号不能为空");
					LogUtil.endLog(CreditController.class.toString(), CreditDefine.WEB_CREDIT_TENDER_PAGE);
					return JSONObject.toJSONString(ret, true);
				}
				// 获取当前页码
				currPage = (currPage != null && currPage > 0) ? currPage : 1;
				// 获取分页条数
				limitPage = (limitPage != null && limitPage > 0) ? limitPage : 8;
				// 获取债转的详细参数
				Map<String, Object> tenderMap = tenderCreditService.selectWebCreditTenderList(creditNid, currPage, limitPage);
				if (tenderMap.get("tenders") != null) {
					ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
					ret.put(CustomConstants.DATA, tenderMap);
					ret.put(CustomConstants.MSG, "查询完成");
				} else {
					ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
					ret.put(CustomConstants.DATA, null);
					ret.put(CustomConstants.MSG, "查询不到出借记录的还款信息");
				}
				LogUtil.endLog(CreditController.class.toString(), CreditDefine.WEB_CREDIT_TENDER_PAGE);
			} else {
				LogUtil.infoLog(this.getClass().getName(), "searchWebCreditTenderPage", "汇转让购买,用户未登录");
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
				ret.put(CustomConstants.DATA, null);
				ret.put(CustomConstants.MSG, "用户未登录");
			}
		} catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), "searchWebCreditTenderPage", "汇转让购买,用户未登录");
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
			ret.put(CustomConstants.DATA, null);
			ret.put(CustomConstants.MSG, "用户未登录");
		}
		return JSONObject.toJSONString(ret, true);
	}

	/**
	 * 前端Web页面出借可债转输入出借金额后收益提示(包含查询条件)
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = CreditDefine.WEB_CHECK_CREDIT_TENDER_ASSIGN, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public WebBaseAjaxResultBean webCheckCreditTenderAssign(HttpServletRequest request, HttpServletResponse response) {
		
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.WEB_CHECK_CREDIT_TENDER_ASSIGN);
		WebBaseAjaxResultBean result = new WebBaseAjaxResultBean();
		String assignCapital = request.getParameter("assignCapital");
		String creditNid = request.getParameter("creditNid");
		Integer userId = WebUtils.getUserId(request);// 用户ID
		System.out.println("tenderCheck ShiroUtil.getLoginUserId--------------------" + userId);
		// 获取相应的登陆用户
		// WebViewUser loginUser = WebUtils.getUser(request);
		// 测评到期日
		// Long lCreate = loginUser.getEvaluationExpiredTime().getTime();
		JSONObject info = tenderCreditService.checkParam(creditNid, assignCapital, userId, "0"/*,lCreate*/);
		if (info.getString("error").equals("1") || info.getString("error").equals("2")) {
			result.setStatus(false);
			result.setMessage(info.getString("data"));
			if(StringUtils.isNotBlank(info.getString("errorCode"))){
				result.setErrorCode(info.getString("errorCode"));
			}
			return result;
		}
		/*if(info.getString("error").equals("714")){
			result.setStatus(false);
			//返回错误码
			result.setErrorCode(info.getString("error"));
			return result;
		}*/
		if(info.getString("error").equals("719") || info.getString("error").equals("716") || info.getString("error").equals("717")){
			//返回类型和限额
			result.setInvestLevel(info.getString("InvestLevel"));
			if(info.getString("error").equals("716")){
				result.setEvalFlagType(info.getString("evalFlagType"));
			}else if(info.getString("error").equals("719")){
				result.setRevaluationMoney(info.getString("revaluation_money"));
			}else if(info.getString("error").equals("717")){
				result.setRevaluationMoneyPrincipal(info.getString("revaluation_money_principal"));
			}
			result.setStatus(false);
			//返回错误码
			result.setErrorCode(info.getString("error"));
			return result;
		}
		LogUtil.endLog(CreditController.class.toString(), CreditDefine.WEB_CHECK_CREDIT_TENDER_ASSIGN);
		result.setStatus(true);
		result.setMessage("校验成功");
		return result;
	}

	/**
	 * 前端Web页面出借可债转输入出借金额后获取收益
	 * @param request
	 * @param response
	 * @param creditNid
	 * @param assignCapital
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = CreditDefine.WEB_CREDIT_TENDER_INTEREST, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String getInterestInfo(HttpServletRequest request, HttpServletResponse response, @RequestParam String creditNid, @RequestParam String assignCapital) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.WEB_CHECK_CREDIT_TENDER_ASSIGN);
		JSONObject ret = new JSONObject();
		Integer userId = WebUtils.getUserId(request);// 用户ID
		try {
			// 获取债转的详细参数
			TenderToCreditAssignCustomize creditAssign = tenderCreditService.getInterestInfo(creditNid, assignCapital,userId);
			if (Validator.isNotNull(creditAssign)) {
				ret.put("creditAssign", creditAssign);
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
			} else {
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
				ret.put(CustomConstants.MSG, "系统异常,请稍后再试!");
			}
		} catch (Exception e) {
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
			ret.put(CustomConstants.MSG, "系统异常,请稍后再试!");
		}
		return JSONObject.toJSONString(ret, true);
	}

	/**
	 * 前端Web页面出借确定认购提交,必须是FORM表单提交
	 * @param request
	 * @param response
	 * @param creditNid
	 * @param assignCapital
	 * @return
	 */
	@RequestMapping(value = CreditDefine.WEB_SUBMIT_CREDIT_TENDER_ASSIGN)
	public ModelAndView webSubmitCreditTenderAssign(HttpServletRequest request, HttpServletResponse response, @RequestParam String creditNid, @RequestParam String assignCapital) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.WEB_SUBMIT_CREDIT_TENDER_ASSIGN);
		ModelAndView modelAndView = new ModelAndView();
		String message = "";
		Integer userId = WebUtils.getUserId(request); // 用户ID
		// 订单号
		String logOrderId = GetOrderIdUtils.getOrderId2(userId);
		try {
			if (userId != null) {
				LogUtil.infoLog(this.getClass().getName(), "searchWebCreditTender", "汇转让购买,用户已登录");
				Users user = this.tenderCreditService.getUsers(userId);
				TenderToCreditAssignCustomize creditAssign = this.tenderCreditService.getInterestInfo(creditNid, assignCapital,userId);
				Borrow borrow = this.tenderCreditService.seachBorrowInfo(creditAssign.getBorrowNid());
				// 是否月标(true:月标, false:天标)
				WebViewUser sessionUser = WebUtils.getUser(request);
				// 是否月标(true:月标, false:天标)
				boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrow.getBorrowStyle()) || CustomConstants.BORROW_STYLE_MONTH.equals(borrow.getBorrowStyle())
						|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrow.getBorrowStyle())|| CustomConstants.BORROW_STYLE_END.equals(borrow.getBorrowStyle());

				String dayOrMonth ="";
				String lockPeriod = String.valueOf(borrow.getBorrowPeriod());
				if(isMonth){//月标
					dayOrMonth = lockPeriod + "个月债转";
				}else{
					dayOrMonth = lockPeriod + "天债转";
				}
				UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
				userOperationLogEntity.setOperationType(4);
				userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
				userOperationLogEntity.setPlatform(0);
				userOperationLogEntity.setRemark(dayOrMonth);
				userOperationLogEntity.setOperationTime(new Date());
				userOperationLogEntity.setUserName(sessionUser.getUsername());
				userOperationLogEntity.setUserRole(sessionUser.getRoleId());
				loginService.sendUserLogMQ(userOperationLogEntity);
				if (user != null) {
					if (user.getStatus() == 1) {
						message = "用户已被禁用";
						modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_TENDER_FAILED_PATH);
						modelAndView.addObject("message", message);
						return modelAndView;
					}
				} else {
					message = "用户不存在";
					modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_TENDER_FAILED_PATH);
					modelAndView.addObject("message", message);
					return modelAndView;
				}

				// 合规三期检查服务费授权
				if (!authService.checkPaymentAuthStatus(userId)) {
				    message = "该产品需开通服务费授权功能，马上开通？";
                    modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_TENDER_FAILED_PATH);
                    modelAndView.addObject("message", message);
                    modelAndView.addObject("paymentAuthOn", CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus());
                    return modelAndView;
                }
				UsersInfo usersInfo = this.tenderCreditService.getUsersInfoByUserId(userId);
				if (null != usersInfo) {
					String roleIsOpen = PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN);
					if(StringUtils.isNotBlank(roleIsOpen) && roleIsOpen.equals("true")){
						if (usersInfo.getRoleId() != 1) {
							message = "仅限出借人进行出借";
							modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_TENDER_FAILED_PATH);
							modelAndView.addObject("message", message);
							return modelAndView;
						}
					}

				} else {
					message = "账户信息异常";
					modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_TENDER_FAILED_PATH);
					modelAndView.addObject("message", message);
					return modelAndView;
				}
				if (StringUtils.isEmpty(creditNid) || StringUtils.isEmpty(assignCapital)) {
					message = "债转编号和承接本金不能为空";
					modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_TENDER_FAILED_PATH);
					modelAndView.addObject("message", message);
					return modelAndView;
				} else {
					if (!assignCapital.matches("^[-+]?(([0-9]+)(([0-9]+))?|(([0-9]+))?)$") || !Validator.isNumber(creditNid)) {
						message = "债转编号和承接本金必须是数字格式";
						modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_TENDER_FAILED_PATH);
						modelAndView.addObject("message", message);
						return modelAndView;
					}
				}
				// 验证用户余额是否可以债转
				Account account = this.tenderCreditService.getAccount(userId);
				//TenderToCreditAssignCustomize creditAssign = this.tenderCreditService.getInterestInfo(creditNid, assignCapital,userId);
				String assignPay = creditAssign.getAssignTotal();
				if (account.getBankBalance() != null && account.getBankBalance().compareTo(BigDecimal.ONE) >= 0) {
					if (account.getBankBalance().compareTo(new BigDecimal(assignPay)) < 0) {
						message = "余额不足";
						modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_TENDER_FAILED_PATH);
						modelAndView.addObject("message", message);
						return modelAndView;
					}
				} else {
					message = "余额不足";
					modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_TENDER_FAILED_PATH);
					modelAndView.addObject("message", message);
					return modelAndView;
				}
				String txDate = GetOrderIdUtils.getTxDate();
				String txTime = GetOrderIdUtils.getTxTime();
				String seqNo = GetOrderIdUtils.getSeqNo(6);
				// 获取债转的详细参数
				Map<String, Object> creditDetailMap = tenderCreditService.saveCreditTenderAssign(userId, creditNid, assignCapital, request, "0", logOrderId, txDate, txTime, seqNo);
				if (creditDetailMap.get("creditTenderLog") != null) {
					CreditTenderLog creditTenderLog = (CreditTenderLog) creditDetailMap.get("creditTenderLog");
					if (account.getBankBalance().compareTo(creditTenderLog.getAssignPay()) < 0) {
						message = "垫付利息可用余额不足，请充值";
						modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_TENDER_FAILED_PATH);
						modelAndView.addObject("message", message);
						return modelAndView;
					}
					BankOpenAccount accountChinapnrTender = tenderCreditService.getBankOpenAccount(userId);
					// 出让人的用户Id
					Integer creditUserId = (Integer) creditDetailMap.get("creditUserId");
					// 出让人开户信息
					BankOpenAccount accountChinapnrCrediter = tenderCreditService.getBankOpenAccount(creditUserId);
					if (accountChinapnrCrediter == null || Validator.isNull(accountChinapnrCrediter.getAccount())) {
						message = "出让人未开户!";
						modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_TENDER_FAILED_PATH);
						modelAndView.addObject("message", message);
						return modelAndView;
					}
					// 调用汇付接口
					String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + CreditDefine.REQUEST_MAPPING + "/" + CreditDefine.RETURN_MAPPING + ".do";
					String bgRetUrl = PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) + CreditDefine.REQUEST_MAPPING + "/" + CreditDefine.CALLBACK_MAPPING + ".do";// 支付工程路径
					BankCallBean bean = new BankCallBean();
					bean.setLogOrderId(logOrderId);
					bean.setLogUserId(String.valueOf(userId));
					bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_MOBILE_CREDITINVEST);
					bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
					bean.setTxCode(BankCallMethodConstant.TXCODE_CREDITINVEST);
					bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
					bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
					bean.setTxDate(txDate);// 交易日期
					bean.setTxTime(txTime);// 交易时间
					bean.setSeqNo(seqNo);// 交易流水号6位
					bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
					bean.setAccountId(accountChinapnrTender.getAccount());// 存管平台分配的账号
					bean.setOrderId(logOrderId);
					bean.setTxAmount(DF_COM_VIEW.format(creditTenderLog.getAssignPay()));
					bean.setTxFee(creditTenderLog.getCreditFee() != null ? DF_COM_VIEW.format(creditTenderLog.getCreditFee()) : "0.01");
					bean.setTsfAmount(DF_COM_VIEW.format(creditTenderLog.getAssignCapital()));
					bean.setForAccountId(accountChinapnrCrediter.getAccount());// 对手电子账号:卖出方账号
					bean.setOrgOrderId(creditTenderLog.getCreditTenderNid());
					bean.setOrgTxAmount(DF_COM_VIEW.format(creditDetailMap.get("tenderMoney")));
					bean.setProductId(creditTenderLog.getBidNid().toString());
					bean.setForgotPwdUrl(PropUtils.getSystem(CustomConstants.FORGET_PASSWORD_URL));// 忘记密码的跳转URL
					bean.setRetUrl(retUrl);// 商户前台台应答地址(必须)
					bean.setNotifyUrl(bgRetUrl); // 商户后台应答地址(必须)
					bean.setSuccessfulUrl(retUrl+"?isSuccess=1");// 调用成功后跳转地址
					System.out.println("债转前台回调函数：\n" + bean.getRetUrl());
					System.out.println("债转后台回调函数：\n" + bean.getNotifyUrl());
					LogAcqResBean logAcqResBean = new LogAcqResBean();
					logAcqResBean.setUserId(userId);
					bean.setLogAcqResBean(logAcqResBean);// 请求方保留
					// 跳转到银行画面
					try {
						modelAndView = BankCallUtils.callApi(bean);
					} catch (Exception e) {
						e.printStackTrace();
						// 此处更改CreditTenderLog数据中的status为1,代表此订单失效
						tenderCreditService.updateCreditTenderLogToFail(creditTenderLog);
						message = "交易失败请重试";
						modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_TENDER_FAILED_PATH);
						modelAndView.addObject("message", message);
						return modelAndView;
					}
				} else {
					message = String.valueOf(creditDetailMap.get("msg"));
					modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_TENDER_FAILED_PATH);
					modelAndView.addObject("message", message);
					return modelAndView;
				}
				LogUtil.endLog(CreditController.class.toString(), CreditDefine.WEB_SUBMIT_CREDIT_TENDER_ASSIGN);
			} else {
				LogUtil.infoLog(this.getClass().getName(), "searchWebCreditTender", "汇转让购买,用户未登录");
				message = "用户未登录";
				modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_TENDER_FAILED_PATH);
				modelAndView.addObject("message", message);
				return modelAndView;
			}
		} catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), "searchWebCreditTender", "汇转让购买,用户未登录");
			message = "用户未登录";
			modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_TENDER_FAILED_PATH);
			modelAndView.addObject("message", message);
			return modelAndView;
		}
		return modelAndView;
	}

	/**
	 * 债转汇付交易成功后回调处理
	 * @param request
	 * @param bean
	 * @return
	 */
	@RequestMapping(CreditDefine.RETURN_MAPPING)
	public ModelAndView tenderCreditReturn(HttpServletRequest request, @ModelAttribute BankCallBean bean) {

		ModelAndView modelAndView = null;
		bean.convert();
		String message = "";
		Integer userId = WebUtils.getUserId(request); // 用户ID
		String isSuccess = request.getParameter("isSuccess");
		// 订单号
		String logOrderId = bean.getLogOrderId()==null?"":bean.getLogOrderId();
		_log.info("债转承接同步回调,承接定单号:[" + logOrderId + "],承接人用户ID:[" + userId + "].retCode:"+bean.getRetCode());
		// 更新相应的债转承接log表
		boolean updateFlag = this.tenderCreditService.updateCreditTenderLog(logOrderId, userId);
        if (isSuccess == null || !isSuccess.equals("1")) {
            message = "交易失败，请联系客服";
            modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_TENDER_FAILED_PATH);
            modelAndView.addObject("message", message);
            return modelAndView;
        }
		// 如果更新成功(异步没有回调成功)
		if (updateFlag) {
			// 调用相应的查询接口查询此笔承接的相应的承接状态
			BankCallBean tenderQueryBean = this.tenderCreditService.creditInvestQuery(logOrderId, userId);
			// 判断返回结果是不是空
			if (Validator.isNotNull(tenderQueryBean)) {
				// bean实体转化
				tenderQueryBean.convert();
				// 获取债转查询返回码
				String retCode = StringUtils.isNotBlank(tenderQueryBean.getRetCode()) ? tenderQueryBean.getRetCode() : "";
				_log.info("债转承接后,调用出借人购买债权查询接口,银行返回码:[" + retCode + "].");
				// 承接成功
				if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
					// 直接返回查询银行债转状态查询失败
					message = "交易正在进行中";
					modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_INFO);
					modelAndView.addObject("INFO", message);
					return modelAndView;
				}
				// 查询相应的债转承接记录
				CreditTenderLog creditTenderLog = this.tenderCreditService.selectCreditTenderLogByOrderId(logOrderId);
				// 如果已经查询到相应的债转承接log表
				if (Validator.isNotNull(creditTenderLog)) {
					try {
						// 此次查询的授权码
						String authCode = tenderQueryBean.getAuthCode();
						_log.info("债转承接后,调用出借人购买债权查询接口,银行返回码:[" + retCode + "],银行返回授权码:[" + authCode + "].");
						if (StringUtils.isNotBlank(authCode)) {
							// 更新债转汇付交易成功后的相关信息
							boolean tenderFlag = this.tenderCreditService.updateTenderCreditInfo(logOrderId, userId, authCode);
							if (tenderFlag) {
								// 查询相应的承接记录，如果相应的承接记录存在，则承接成功
								CreditTender creditTender = tenderCreditService.creditTenderByAssignNid(logOrderId, userId);
								if (creditTender != null) {
									// add by liuyang 20180305 发送法大大PDF处理MQ start
									this.tenderCreditService.sendPdfMQ(userId, creditTender.getBidNid(), creditTender.getAssignNid(), creditTender.getCreditNid(), creditTender.getCreditTenderNid());
									// add by liuyang 20180305 发送法大大PDF处理MQ end
									message = "恭喜您，汇转让出借成功！";
									modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_TENDER_SUCCESS_PATH);
									modelAndView.addObject("message", message);
									modelAndView.addObject("error", 0);
									modelAndView.addObject("creditTender", creditTender);
									return modelAndView;
								} else {
									message = "交易正在进行中";
									modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_INFO);
									modelAndView.addObject("INFO", message);
									return modelAndView;
								}
							} else {
								message = "交易正在进行中";
								modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_INFO);
								modelAndView.addObject("INFO", message);
								return modelAndView;
							}
						} else {
							message = "交易正在进行中";
							modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_INFO);
							modelAndView.addObject("INFO", message);
							return modelAndView;
						}
					} catch (Exception e) {
						message = "交易正在进行中";
						modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_INFO);
						modelAndView.addObject("INFO", message);
						return modelAndView;
					}
				} else {
					// 查询相应的承接记录，如果相应的承接记录存在，则承接成功
					CreditTender creditTender = tenderCreditService.creditTenderByAssignNid(logOrderId, userId);
					if (creditTender != null) {
						// add by liuyang 20180305 发送法大大PDF处理MQ start
						this.tenderCreditService.sendPdfMQ(userId, creditTender.getBidNid(), creditTender.getAssignNid(), creditTender.getCreditNid(), creditTender.getCreditTenderNid());
						// add by liuyang 20180305 发送法大大PDF处理MQ end
						message = "恭喜您，汇转让出借成功！";
						modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_TENDER_SUCCESS_PATH);
						modelAndView.addObject("message", message);
						modelAndView.addObject("error", 0);
						modelAndView.addObject("creditTender", creditTender);
						return modelAndView;
					} else {
						message = "交易正在进行中";
						modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_INFO);
						modelAndView.addObject("INFO", message);
						return modelAndView;
					}
				}
			} else {
				// 直接返回查询银行债转状态查询失败
				message = "调用银行接口失败！";
				modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_TENDER_FAILED_PATH);
				modelAndView.addObject("INFO", message);
				return modelAndView;
			}
		} else {
			// 查询相应的承接记录，如果相应的承接记录存在，则承接成功
			CreditTender creditTender = tenderCreditService.creditTenderByAssignNid(logOrderId, userId);
			if (creditTender != null) {
				message = "恭喜您，汇转让出借成功！";
				modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_TENDER_SUCCESS_PATH);
				modelAndView.addObject("message", message);
				modelAndView.addObject("error", 0);
				modelAndView.addObject("creditTender", creditTender);
				// add by liuyang 20180305 发送法大大PDF处理MQ start
				this.tenderCreditService.sendPdfMQ(userId, creditTender.getBidNid(), creditTender.getAssignNid(), creditTender.getCreditNid(), creditTender.getCreditTenderNid());
				// add by liuyang 20180305 发送法大大PDF处理MQ end
				return modelAndView;
			} else {
				message = "交易正在进行中";
				modelAndView = new ModelAndView(CreditDefine.WEB_CREDIT_INFO);
				modelAndView.addObject("INFO", message);
				return modelAndView;
			}
		}
	}

	/**
	 * 债转汇付交易成功后异步回调处理
	 * @param request
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(CreditDefine.CALLBACK_MAPPING)
	public String tenderCreditCallback(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
		bean.convert();
		BankCallResult resultBean = new BankCallResult();
		String message = "";
		// 用户ID
		Integer userId = Integer.parseInt(bean.getLogUserId());
		String logOrderId = bean.getLogOrderId() == null ? "" : bean.getLogOrderId();
		_log.info("承接债权异步回调:承接订单号:[" + logOrderId + "],承接用户ID:[" + userId + "].");
		// 更新相应的债转承接log表
		boolean updateFlag = this.tenderCreditService.updateCreditTenderLog(logOrderId, userId);
		// 如果更新成功(异步没有回调成功)
		if (updateFlag) {
			// 获取债转查询返回码
			String retCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
			_log.info("承接债权异步回调银行返回码:[" + retCode + "]");
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
				// 直接返回查询银行债转状态查询失败
				message = "承接失败，";
				resultBean.setMessage(message);
				resultBean.setStatus(true);
				return JSONObject.toJSONString(resultBean, true);
			}
			// 调用相应的查询接口查询此笔承接的相应的承接状态
			BankCallBean tenderQueryBean = this.tenderCreditService.creditInvestQuery(logOrderId, userId);
			// 判断返回结果是不是空
			if (Validator.isNotNull(tenderQueryBean)) {
				// bean实体转化
				tenderQueryBean.convert();
				// 获取债转查询返回码
				String queryRetCode = StringUtils.isNotBlank(tenderQueryBean.getRetCode()) ? tenderQueryBean.getRetCode() : "";
				_log.info("承接债权异步回调,调用出借人购买债权查询接口,银行返回码:[" + queryRetCode + "].");
				// 承接成功
				if (!BankCallConstant.RESPCODE_SUCCESS.equals(queryRetCode)) {
					// 直接返回查询银行债转状态查询失败
					message = "承接失败，";
					resultBean.setMessage(message);
					resultBean.setStatus(true);
					return JSONObject.toJSONString(resultBean, true);
				}
				// 查询相应的债转承接记录
				CreditTenderLog creditTenderLog = this.tenderCreditService.selectCreditTenderLogByOrderId(logOrderId);
				// 如果已经查询到相应的债转承接log表
				if (Validator.isNotNull(creditTenderLog)) {
					try {
						// 此次查询的授权码
						String authCode = tenderQueryBean.getAuthCode();
						_log.info("承接债权异步回调,调用出借人购买债权查询接口,银行返回码:[" + queryRetCode + "],银行授权码:[" + authCode + "].");
						if (StringUtils.isNotBlank(authCode)) {
							// 更新债转汇付交易成功后的相关信息
							boolean tenderFlag = this.tenderCreditService.updateTenderCreditInfo(logOrderId, userId, authCode);
							if (tenderFlag) {
								// add by liuyang 20180305 发送法大大PDF处理MQ start
								CreditTender creditTender = tenderCreditService.creditTenderByAssignNid(logOrderId, userId);
								this.tenderCreditService.sendPdfMQ(userId, creditTender.getBidNid(), creditTender.getAssignNid(), creditTender.getCreditNid(), creditTender.getCreditTenderNid());
								// add by liuyang 20180305 发送法大大PDF处理MQ end
								// 查询相应的承接记录，如果相应的承接记录存在，则承接成功
								message = "恭喜您，汇转让出借成功！";
								resultBean.setMessage(message);
								resultBean.setStatus(true);
								return JSONObject.toJSONString(resultBean, true);
							} else {
								message = "交易正在进行中";
								resultBean.setMessage(message);
								resultBean.setStatus(true);
								return JSONObject.toJSONString(resultBean, true);
							}
						} else {
							message = "交易正在进行中";
							resultBean.setMessage(message);
							resultBean.setStatus(false);
							return JSONObject.toJSONString(resultBean, true);
						}
					} catch (Exception e) {
						message = "交易正在进行中";
						resultBean.setMessage(message);
						resultBean.setStatus(true);
						return JSONObject.toJSONString(resultBean, true);
					}
				} else {
					// 查询相应的承接记录，如果相应的承接记录存在，则承接成功
					CreditTender creditTender = tenderCreditService.creditTenderByAssignNid(logOrderId, userId);
					if (creditTender != null) {
						// add by liuyang 20180305 发送法大大PDF处理MQ start
						this.tenderCreditService.sendPdfMQ(userId, creditTender.getBidNid(), creditTender.getAssignNid(), creditTender.getCreditNid(), creditTender.getCreditTenderNid());
						// add by liuyang 20180305 发送法大大PDF处理MQ end
						message = "恭喜您，汇转让出借成功！";
						resultBean.setMessage(message);
						resultBean.setStatus(true);
						return JSONObject.toJSONString(resultBean, true);
					} else {
						resultBean.setMessage(message);
						resultBean.setStatus(true);
						return JSONObject.toJSONString(resultBean, true);
					}
				}
			} else {
				// 直接返回查询银行债转状态查询失败
				message = "调用银行接口失败！";
				resultBean.setMessage(message);
				resultBean.setStatus(true);
				return JSONObject.toJSONString(resultBean, true);
			}
		} else {
			// 查询相应的承接记录，如果相应的承接记录存在，则承接成功
			CreditTender creditTender = tenderCreditService.creditTenderByAssignNid(logOrderId, userId);
			if (creditTender != null) {
				message = "恭喜您，汇转让出借成功！";
				// add by liuyang 20180305 发送法大大PDF处理MQ start
				this.tenderCreditService.sendPdfMQ(userId, creditTender.getBidNid(), creditTender.getAssignNid(), creditTender.getCreditNid(), creditTender.getCreditTenderNid());
				// add by liuyang 20180305 发送法大大PDF处理MQ end
				resultBean.setMessage(message);
				resultBean.setStatus(true);
				return JSONObject.toJSONString(resultBean, true);
			} else {
				message = "交易正在进行中";
				resultBean.setMessage(message);
				resultBean.setStatus(true);
				return JSONObject.toJSONString(resultBean, true);
			}
		}
	}


	/**
	 * 发送短信验证码（ajax请求） 短信验证码数据保存
	 */
	@ResponseBody
	@RequestMapping(value = CreditDefine.TENDER_TO_CREDIT_SEND_CODE)
	public String sendCode(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SEND_CODE);
		WebViewUser user = WebUtils.getUser(request); // 获取用户对象

		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		JSONObject jo = new JSONObject();
		if (user != null) {
			// 手机号码(必须,数字,最大长度)
			String mobile = user.getMobile();
			SmsConfig smsConfig = tenderCreditService.getSmsConfig();
			System.out.println("smsConfig---------------------------------------" + JSON.toJSONString(smsConfig));
			String ip = GetCilentIP.getIpAddr(request);
			String ipCount = RedisUtils.get(ip + ":MaxIpCount");
			if (StringUtils.isEmpty(ipCount)) {
				ipCount = "0";
				RedisUtils.set(ip + ":MaxIpCount", "0");
			}
			System.out.println(mobile + "------ip---" + ip + "----------MaxIpCount-----------" + ipCount);
			if (Integer.valueOf(ipCount) >= smsConfig.getMaxIpCount()) {
				if (Integer.valueOf(ipCount) == smsConfig.getMaxIpCount()) {
					try {
						tenderCreditService.sendSms(mobile, "IP访问次数超限:" + ip);
					} catch (Exception e) {
						LogUtil.errorLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SEND_CODE, e);
					}

					RedisUtils.set(ip + ":MaxIpCount", (Integer.valueOf(ipCount) + 1) + "", 24 * 60 * 60);

				}
				try {
					tenderCreditService.sendEmail(mobile, "IP访问次数超限" + ip);
				} catch (Exception e) {
					LogUtil.errorLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SEND_CODE, e);
				}
				jo.put(CreditDefine.CODE_ERROR, CreditDefine.ERROR_MAXCOUNT_OTHERS);
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
				ret.put(CustomConstants.DATA, jo);
				ret.put(CustomConstants.MSG, "IP访问次数超限");
				LogUtil.errorLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SEND_CODE, "IP访问次数超限", null);
				return JSONObject.toJSONString(ret);
			}

			// 判断最大发送数max_phone_count
			String count = RedisUtils.get(mobile + ":MaxPhoneCount");
			if (StringUtils.isEmpty(count)) {
				count = "0";
				RedisUtils.set(mobile + ":MaxPhoneCount", "0");
			}
			System.out.println(mobile + "----------MaxPhoneCount-----------" + count);
			if (Integer.valueOf(count) >= smsConfig.getMaxPhoneCount()) {
				if (Integer.valueOf(count) == smsConfig.getMaxPhoneCount()) {
					try {
						tenderCreditService.sendSms(mobile, "手机发送次数超限");
					} catch (Exception e) {
						LogUtil.errorLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SEND_CODE, e);
					}

					RedisUtils.set(mobile + ":MaxPhoneCount", (Integer.valueOf(count) + 1) + "", 24 * 60 * 60);
				}
				try {
					tenderCreditService.sendEmail(mobile, "手机发送次数超限");
				} catch (Exception e) {
					LogUtil.errorLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SEND_CODE, e);
				}
				jo.put(CreditDefine.CODE_ERROR, CreditDefine.ERROR_MAXCOUNT_OTHERS);
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
				ret.put(CustomConstants.DATA, jo);
				ret.put(CustomConstants.MSG, "手机发送次数超限");
				LogUtil.errorLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SEND_CODE, "手机发送次数超限", null);
				return JSONObject.toJSONString(ret);
			}
			// 判断发送间隔时间
			String intervalTime = RedisUtils.get(mobile + ":IntervalTime");
			System.out.println(mobile + "----------IntervalTime-----------" + intervalTime);
			if (!StringUtils.isEmpty(intervalTime)) {
				jo.put(CreditDefine.CODE_ERROR, CreditDefine.ERROR_INTERVAL_TIME_OTHERS);
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
				ret.put(CustomConstants.DATA, jo);
				ret.put(CustomConstants.MSG, "发送时间间隔太短");
				LogUtil.errorLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SEND_CODE, "发送时间间隔太短", null);
				return JSONObject.toJSONString(ret);
			}

			if (!ValidatorCheckUtil.validateRequired(info, null, null, mobile)) {
				jo.put(CreditDefine.MOBILE_ERROR, CreditDefine.ERROR_TYPE_REQUIRED);
			} else if (!ValidatorCheckUtil.validateMobile(info, null, null, mobile, 11, false)) {
				jo.put(CreditDefine.MOBILE_ERROR, CreditDefine.ERROR_TYPE_OTHERS);
			}

		
			// 手机验证码
			if (jo == null || jo.isEmpty()) {
				// 生成验证码
				String checkCode = GetCode.getRandomSMSCode(6);
				Map<String, String> param = new HashMap<String, String>();
				param.put("val_code", checkCode);
				// 发送短信验证码
				SmsMessage smsMessage = new SmsMessage(null, param, mobile, null, MessageDefine.SMSSENDFORMOBILE, null, CustomConstants.PARAM_TPL_ZHUCE, CustomConstants.CHANNEL_TYPE_NORMAL);
				Integer result = smsProcesser.gather(smsMessage);
				// checkCode过期时间，默认120秒
				RedisUtils.set(mobile + ":MaxValidTime", checkCode, smsConfig.getMaxValidTime() == null ? 120 : smsConfig.getMaxValidTime() * 60);
				// 发送checkCode最大时间间隔，默认60秒
				RedisUtils.set(mobile + ":IntervalTime", mobile, smsConfig.getMaxIntervalTime() == null ? 60 : smsConfig.getMaxIntervalTime());

				// 短信发送成功后处理
				if (result != null && result == 1) {
					// 累计IP次数
					String currentMaxIpCount = RedisUtils.get(ip + ":MaxIpCount");
					if (StringUtils.isEmpty(currentMaxIpCount)) {
						currentMaxIpCount = "0";
					}
					// 累加手机次数
					String currentMaxPhoneCount = RedisUtils.get(mobile + ":MaxPhoneCount");
					if (StringUtils.isEmpty(currentMaxPhoneCount)) {
						currentMaxPhoneCount = "0";
					}
					RedisUtils.set(ip + ":MaxIpCount", (Integer.valueOf(currentMaxIpCount) + 1) + "", 24 * 60 * 60);
					RedisUtils.set(mobile + ":MaxPhoneCount", (Integer.valueOf(currentMaxPhoneCount) + 1) + "", 24 * 60 * 60);
				}

				// 保存短信验证码
				this.tenderCreditService.saveSmsCode(mobile, checkCode);
				jo.put("status", 0);
				// jo.put("checkCode", checkCode);
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
				ret.put(CustomConstants.DATA, jo);
				String maxValidTime = "60";
				Integer time = smsConfig.getMaxIntervalTime();
				if (time != null) {
					maxValidTime = time + "";
				}
				ret.put(CustomConstants.MAX_VALIDTIME, maxValidTime);
				ret.put(CustomConstants.MSG, "");
				LogUtil.endLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SEND_CODE);
				return JSONObject.toJSONString(ret);
			} else {
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
				ret.put(CustomConstants.DATA, jo);
				ret.put(CustomConstants.MSG, "系统错误");
				LogUtil.errorLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SEND_CODE, "短信验证码发送失败", null);
				return JSONObject.toJSONString(ret);
			}
		} else {
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
			ret.put(CustomConstants.DATA, "");
			ret.put(CustomConstants.MSG, "用户未登录");
			LogUtil.errorLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SEND_CODE, "短信验证码发送失败", null);
			return JSONObject.toJSONString(ret);
		}
	}

	/**
	 * 短信验证码校验
	 * 
	 * 用户注册数据提交（获取session数据并保存） 1.校验验证码
	 * 2.若验证码正确，则获取session数据，并将相应的注册数据写入数据库（三张表），跳转相应的注册成功界面
	 */
	@ResponseBody
	@RequestMapping(CreditDefine.TENDER_TO_CREDIT_CHECK_CODE)
	public boolean checkCode(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_CHECK_CODE);
		WebViewUser user = WebUtils.getUser(request); // 获取用户对象
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		JSONObject jo = new JSONObject();
		if (user != null) {
			// 手机号码(必须,数字,最大长度)
			String mobile = user.getMobile();
			if (!ValidatorCheckUtil.validateRequired(info, null, null, mobile)) {
				jo.put(CreditDefine.MOBILE_ERROR, CreditDefine.ERROR_TYPE_REQUIRED);
			} else if (!ValidatorCheckUtil.validateMobile(info, null, null, mobile, 11, false)) {
				jo.put(CreditDefine.MOBILE_ERROR, CreditDefine.ERROR_TYPE_OTHERS);
			}

			// 短信验证码
			String code = request.getParameter("code");
			if (!ValidatorCheckUtil.validateRequired(info, null, null, code)) {
				jo.put(CreditDefine.CODE_ERROR, CreditDefine.ERROR_TYPE_REQUIRED);
			}

			if (jo == null || jo.isEmpty()) {
				int cnt = this.tenderCreditService.checkMobileCode(mobile, code);
				if (cnt > 0) {
					ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
					ret.put(CustomConstants.DATA, jo);
					ret.put(CustomConstants.MSG, "");
					return true;
				} else {
					jo.put(CreditDefine.CODE_ERROR, CreditDefine.ERROR_TYPE_OTHERS);
					LogUtil.errorLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_CHECK_CODE, "验证码验证失败", null);
				}
			} else {
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
				ret.put(CustomConstants.DATA, "");
				ret.put(CustomConstants.MSG, jo);
			}
		} else {
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
			ret.put(CustomConstants.DATA, jo);
			ret.put(CustomConstants.MSG, "用户未登录");
		}
		LogUtil.endLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_CHECK_CODE);
		return false;
	}

	/**
	 * 债转汇付掉单数据手动恢复
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "tenderCreditFix", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String tenderCreditFix(HttpServletRequest request) {
		LogUtil.startLog(CreditController.class.toString(), "tenderCreditFix");
		JSONObject ret = null;
		String assignNid = request.getParameter("assignNid");
		try {
			if (StringUtils.isEmpty(assignNid)) {
				return "非法访问";
			}
			LogUtil.startLog(this.getClass().getName(), UserRechargeDefine.RETURN_MAPPING, "[债转交易失败与汇付不同步时,手动调整债转出借]");
			ret = this.tenderCreditService.updateTenderCreditInfoHandle(assignNid);
		} catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), "return", "汇转让购买,系统异常");

		}
		return JSONObject.toJSONString(ret, true);
	}

	/**
	 * 用户中心查询 已承接债转的 出借详情列表资源
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = CreditDefine.WEB_CREDIT_TENDER_LIST, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String searchCreditTenderList(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(CreditController.class.toString(), CreditDefine.WEB_CREDIT_TENDER_LIST);
		JSONObject ret = new JSONObject();
		String creditNid = request.getParameter("creditNid");
		String paginatorPageStr = request.getParameter("paginatorPage");
		String pageSizeStr = request.getParameter("pageSize");
		int paginatorPage = StringUtils.isBlank(paginatorPageStr) ? 1 : Integer.parseInt(paginatorPageStr);
		int pageSize = StringUtils.isBlank(pageSizeStr) ? 1 : Integer.parseInt(pageSizeStr);
		if (StringUtils.isEmpty(creditNid)) {
			ret.put(CreditDefine.STATUS, false);
			ret.put(CreditDefine.MESSAGE, "系统错误,请稍后再试");
			LogUtil.endLog(CreditController.class.toString(), CreditDefine.TENDER_TO_CREDIT_SAVE);
			return JSONObject.toJSONString(ret, true);
		}
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("creditNid", creditNid);
			// 查询相应的汇消费列表的总数
			int recordTotal = this.tenderCreditService.countWebCreditTenderList(params);
			if (recordTotal > 0) {
				Paginator paginator = new Paginator(paginatorPage, recordTotal, pageSize);
				params.put("limitStart", paginator.getOffset());
				params.put("limitEnd", paginator.getLimit());
				List<CreditTenderListCustomize> recordList = tenderCreditService.selectWebCreditTenderList(params);
				ret.put("recordList", recordList);
				ret.put("paginator", paginator);
				List<BorrowCredit> creditList = this.tenderCreditService.selectBorrowCreditByNid(creditNid);
				if (creditList != null && creditList.size() == 1) {
					BorrowCredit credit = creditList.get(0);
					ret.put("assignTotal", credit.getCreditCapitalAssigned());
					ret.put("assignTimes", credit.getAssignNum());
				}
			} else {
				ret.put("paginator", new Paginator(paginatorPage, recordTotal, pageSize));
				ret.put("recordList", new ArrayList<BorrowCredit>());
				ret.put("assignTotal", 0);
				ret.put("assignTimes", 0);
			}
			ret.put(CreditDefine.STATUS, true);
			LogUtil.endLog(CreditController.class.toString(), CreditDefine.WEB_CREDIT_TENDER_LIST);
		} catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), "searchCreditTenderList", "系统异常");
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
			ret.put(CustomConstants.MSG, "系统异常");
		}
		return JSONObject.toJSONString(ret, true);
	}

}
