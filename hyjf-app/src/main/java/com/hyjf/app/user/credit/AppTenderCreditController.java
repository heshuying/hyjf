package com.hyjf.app.user.credit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseMapBean;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.bank.user.auto.AutoDefine;
import com.hyjf.app.bank.user.openaccount.OpenAccountDefine;
import com.hyjf.app.project.ProjectDefine;
import com.hyjf.app.project.ProjectService;
import com.hyjf.app.project.RepayPlanBean;
import com.hyjf.app.user.invest.InvestDefine;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.mybatis.model.customize.app.*;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

/**
 * 
 * app债权转让
 * 
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月28日
 * @see 13:59:50
 */
@Controller
@RequestMapping(value = AppTenderCreditDefine.REQUEST_MAPPING)
public class AppTenderCreditController extends BaseController {

	@Autowired
	private AppTenderCreditService appTenderCreditService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	/** 发布地址 */
	private static String HOST_URL = PropUtils.getSystem("hyjf.web.host");

	private static DecimalFormat DF_FOR_VIEW = new DecimalFormat("#,##0.00");

	Logger _log = LoggerFactory.getLogger(AppTenderCreditController.class);
	/**
	 * 
	 * 获取债转出借详情
	 * 
	 * @author liuyang
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = AppTenderCreditDefine.PROJECT_DETAIL_ACTION)
	public ModelAndView searchTenderCreditDetail(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.PROJECT_DETAIL_ACTION);
		ModelAndView modelAndView = new ModelAndView(AppTenderCreditDefine.PROJECT_DETAIL_PTAH);
		String creditNid = request.getParameter("creditNid");
		String platform = request.getParameter("realPlatform");
		String token = request.getParameter("token");
		String sign = request.getParameter("sign");
		String randomString = request.getParameter("randomString");
		String order = request.getParameter("order");
		String version = request.getParameter("version");

		if (StringUtils.isBlank(platform)) {
			platform = request.getParameter("platform");
		}
		modelAndView.addObject("platform", platform);
		if (StringUtils.isNotEmpty(token)) {
			modelAndView.addObject("token", strEncode(token));
		}
		modelAndView.addObject("sign", sign);
		modelAndView.addObject("randomString", randomString);
		modelAndView.addObject("order", strEncode(order));
		// 根据债转标号查询相应的项目信息
		AppTenderCreditDetailBean tenderCreditDetail = new AppTenderCreditDetailBean();

		AppTenderToCreditDetailCustomize tenderCredit = this.appTenderCreditService.selectCreditTenderDetail(creditNid);
		// 用户未登录
		if (StringUtils.isEmpty(token)) {
			tenderCreditDetail.setInvestUrl(GetJumpCommand.getLinkJumpPrefix(request, version) + AppTenderCreditDefine.USER_LOGIN_URL);
			modelAndView.addObject("loginFlag", 0);
		} else {// 用户已经登陆
			modelAndView.addObject("loginFlag", 1);
			Integer userId = SecretUtil.getUserId(sign);
			Users user = this.projectService.searchLoginUser(userId);
			if (user.getBankOpenAccount() == 0) {
				// 用户未开户
				String mobile = user.getMobile();
				// 开户url
				String url = HOST_URL + AppTenderCreditDefine.REQUEST_HOME + OpenAccountDefine.REQUEST_MAPPING + OpenAccountDefine.BANKOPEN_OPEN_ACTION;
				modelAndView.addObject("openAccountUrl", url);
				modelAndView.addObject("mobile", mobile);
				tenderCreditDetail.setInvestUrl(GetJumpCommand.getLinkJumpPrefix(request, version) + AppTenderCreditDefine.USER_OPEN_URL);
			} else {
				tenderCreditDetail.setInvestUrl(GetJumpCommand.getLinkJumpPrefix(request, version) + AppTenderCreditDefine.USER_INVEST_URL);
			}
		}
		// tab信息设置
		tenderCreditDetail.setTabOneName(AppTenderCreditDefine.PROJECT_DETAIL_CONSTANT_TABNAME_PROJECTINFO);
		tenderCreditDetail.setTabOneUrl(HOST_URL + AppTenderCreditDefine.REQUEST_HOME + AppTenderCreditDefine.REQUEST_MAPPING + AppTenderCreditDefine.PROJECT_INFO_ACTION);
		tenderCreditDetail.setTabTwoName(AppTenderCreditDefine.PROJECT_DETAIL_CONSTANT_TABNAME_INVEST);
		tenderCreditDetail.setTabTwoUrl(HOST_URL + AppTenderCreditDefine.REQUEST_HOME + AppTenderCreditDefine.REQUEST_MAPPING + AppTenderCreditDefine.PROJECT_INVEST_ACTION);

		if (tenderCredit == null) {
			modelAndView = new ModelAndView(AppTenderCreditDefine.ERROR_PTAH);
			return modelAndView;
		} else {
			BeanUtils.copyProperties(tenderCredit, tenderCreditDetail);
			// 点击原项目,跳转原项目详情url
			if (token == null) {
				// 用户没有登录
				tenderCreditDetail.setBorrowInfoUrl(GetJumpCommand.getLinkJumpPrefix(request, version) + AppTenderCreditDefine.USER_OPEN_URL + "/?{\'url\':\'" + HOST_URL
						+ AppTenderCreditDefine.REQUEST_HOME + ProjectDefine.REQUEST_MAPPING + ProjectDefine.PROJECT_DETAIL_ACTION + "?borrowNid=" + tenderCreditDetail.getBidNid() + "&platform="
						+ platform + "&realPlatform=" + platform + "&order=" + order + "&randomString=" + randomString + "&sign=" + sign + "\'}");
			} else {
				// 用户登录
				tenderCreditDetail.setBorrowInfoUrl(GetJumpCommand.getLinkJumpPrefix(request, version) + AppTenderCreditDefine.USER_OPEN_URL + "/?{\'url\':\'" + HOST_URL
						+ AppTenderCreditDefine.REQUEST_HOME + ProjectDefine.REQUEST_MAPPING + ProjectDefine.PROJECT_DETAIL_ACTION + "?borrowNid=" + tenderCreditDetail.getBidNid() + "&platform="
						+ platform + "&realPlatform=" + platform + "&order=" + order + "&randomString=" + randomString + "&sign=" + sign + "&token=" + token + "\'}");
			}
			System.out.println("BorrowInfoUrl:" + tenderCreditDetail.getBorrowInfoUrl());
			// 项目剩余 format
			tenderCreditDetail.setInvestAccount(DF_FOR_VIEW.format(new BigDecimal(tenderCreditDetail.getInvestAccount())));
			// 债权本金
			tenderCreditDetail.setCreditCapital(DF_FOR_VIEW.format(new BigDecimal(tenderCreditDetail.getCreditCapital())));
			// 添加相应的项目详情信息
			modelAndView.addObject("projectDeatil", tenderCreditDetail);
			// 查询相应的还款计划
			List<RepayPlanBean> repayPlan = this.appTenderCreditService.getRepayPlan(tenderCredit.getBidNid());
			modelAndView.addObject("repayPlanList", repayPlan);
		}
		// 获取债转详细的参数
		LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.PROJECT_DETAIL_ACTION);
		return modelAndView;
	}

	/**
	 * 查询相应的项目的出借记录列表
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppTenderCreditDefine.PROJECT_INVEST_ACTION, produces = "application/json; charset=utf-8")
	public String searchProjectInvestList(@ModelAttribute AppTenderCreditInvestBean form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.PROJECT_INVEST_ACTION);
		JSONObject info = new JSONObject();
		// form.setCreditNid("1608010001");
		this.createCreditTenderInvestPage(info, form);
		info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
		info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
		LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.PROJECT_INVEST_ACTION);
		return JSONObject.toJSONString(info, true);
	}

	/**
	 * 创建相应的项目的用户出借分页信息
	 * 
	 * @param info
	 * @param form
	 */
	private void createCreditTenderInvestPage(JSONObject info, AppTenderCreditInvestBean form) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("creditNid", form.getCreditNid());
		int recordTotal = this.appTenderCreditService.countTenderCreditInvestRecordTotal(params);
		if (recordTotal > 0) { // 查询相应的汇直投列表数据
			int limit = form.getPageSize();
			int page = form.getPage();
			int offSet = (page - 1) * limit;
			if (offSet == 0 || offSet > 0) {
				params.put("limitStart", offSet);
			}
			if (limit > 0) {
				params.put("limitEnd", limit);
			}
			List<AppTenderCreditInvestListCustomize> recordList = this.appTenderCreditService.searchTenderCreditInvestList(params);
			info.put("investList", recordList);
			info.put("investListTotal", String.valueOf(recordTotal));
		} else {
			info.put("investList", new ArrayList<AppTenderCreditInvestListCustomize>());
			info.put("investListTotal", "0");
		}
	}

	/**
	 * 
	 * 我的债权--检索可转让列表
	 * 
	 * @author liuyang
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppTenderCreditDefine.TENDER_TO_CREDIT_LIST, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public JSONObject searchTenderToCreditList(@ModelAttribute AppTenderCreditBean form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_LIST);
		JSONObject ret = new JSONObject();

		Map<String, Object> params = new HashMap<String, Object>();
		// 立即转让URL
		params.put("host", HOST_URL + AppTenderCreditDefine.REQUEST_HOME + AppTenderCreditDefine.REQUEST_MAPPING + AppTenderCreditDefine.TENDER_TO_CREDIT_DETAIL);

		String sign = request.getParameter("sign");

		String platform = request.getParameter("realPlatform");

		if (StringUtils.isBlank(platform)) {
			platform = request.getParameter("platform");
		}
		// 获取用户id
		Integer userId = SecretUtil.getUserId(sign);
		// Integer userId = 22400080;
		ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
		ret.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
		ret.put(CustomConstants.APP_REQUEST, AppTenderCreditDefine.REQUEST_HOME + AppTenderCreditDefine.REQUEST_MAPPING + AppTenderCreditDefine.TENDER_TO_CREDIT_LIST);

		if (userId != null && userId.intValue() != 0) {
			Integer nowTime = GetDate.getNowTime10();// 获取当前时间
			// 查询可转让列表的总数
			int recordTotal = this.appTenderCreditService.countTenderToCredit(userId, nowTime);


			//modify by xiashuqing 20171120 判断用户所处的渠道是否支持债转 start
			//if (recordTotal > 0)
			//modify by xiashuqing 20171120 判断用户所处的渠道是否支持债转 end
			if (recordTotal > 0 && appTenderCreditService.isAllowChannelAttorn(userId)) {
				int limit = form.getPageSize();
				int page = form.getPage();
				int offSet = (page - 1) * limit;
				if (offSet == 0 || offSet > 0) {
					params.put("limitStart", offSet);
				}
				if (limit > 0) {
					params.put("limitEnd", limit);
				}
				// 用户id
				params.put("userId", userId);
				// 当前时间
				params.put("nowTime", nowTime);
				// platform
				params.put("platform", platform);
				// sign
				params.put("sign", sign);
				// 查询汇转让列表数据
				List<AppTenderToCreditListCustomize> recordList = this.appTenderCreditService.selectTenderToCreditList(params);
				if (recordList != null && recordList.size() != 0) {
					for (int i = 0; i < recordList.size(); i++) {
						AppTenderToCreditListCustomize temp = recordList.get(i);
						int lastdays = 0;

						String borrowNid = recordList.get(i).getBidNid();
						// 根据borrowNid判断是否分期
						Borrow borrow = this.appTenderCreditService.selectBorrowByBorrowNid(borrowNid);
						String borrowStyle = borrow.getBorrowStyle();
						if (borrowStyle.equals(CalculatesUtil.STYLE_END) || borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
							try {
								lastdays = GetDate.daysBetween(GetDate.getDateTimeMyTimeInMillis(nowTime), GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(temp.getRecoverTime())));
							} catch (NumberFormatException | ParseException e) {
								e.printStackTrace();
							}
						}
						// 等额本息和等额本金和先息后本
						if (borrowStyle.equals(CalculatesUtil.STYLE_MONTH) || borrowStyle.equals(CalculatesUtil.STYLE_PRINCIPAL) || borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {
							List<BorrowRepayPlan> list = this.appTenderCreditService.searchBorrowRepayPlanList(borrowNid, borrow.getBorrowPeriod());
							if (list != null && list.size() > 0) {
								try {
									lastdays = GetDate.daysBetween(GetDate.getDateTimeMyTimeInMillis(nowTime), GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(list.get(0).getRepayTime())));
								} catch (NumberFormatException | ParseException e) {
									e.printStackTrace();
								}
							}
						}
						temp.setLastDays("剩余" + lastdays + "天");
					}
					// 可转让列表
					ret.put("tenderToCreditList", recordList);
					ret.put("tenderToCreditListTotal", String.valueOf(recordTotal));
				} else {
					System.out.println("------获取可债转列表为空-------  userId=" + userId);
					ret.put("tenderToCreditList", new ArrayList<AppTenderToCreditListCustomize>());
					ret.put("tenderToCreditListTotal", "0");
				}
			} else {
				// 检索可转让列表为0件的情况
				ret.put("tenderToCreditList", new ArrayList<AppTenderToCreditListCustomize>());
				ret.put("tenderToCreditListTotal", String.valueOf(recordTotal));
			}
		}

		LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_LIST);
		return ret;
	}

	/**
	 * 
	 * 转让设置画面
	 * 
	 * @author liuyang
	 * @param request
	 * @param response
	 * @param appTenderCreditCustomize
	 * @return
	 */
	@RequestMapping(value = AppTenderCreditDefine.TENDER_TO_CREDIT_DETAIL)
	public ModelAndView searchTenderToCreditDetail(HttpServletRequest request, HttpServletResponse response, @ModelAttribute AppTenderCreditCustomize appTenderCreditCustomize) {
		LogUtil.startLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_DETAIL);
		ModelAndView modelAndView = new ModelAndView(AppTenderCreditDefine.TENDER_TO_CREDIT_DETAIL_PATH);
		CreditResultBean creditResultBean = new CreditResultBean();
		String sign = request.getParameter("sign");
		String platform = request.getParameter("platform");
		try {
			// 用户id
			Integer userId = SecretUtil.getUserId(sign);
			if (userId != null && userId.intValue() != 0) {
				// 获取当前时间
				Integer nowTime = GetDate.getNowTime10();
				// 获取债转详情页面
				List<AppTenderCreditCustomize> appTenderToCreditDetail = this.appTenderCreditService.selectTenderToCreditDetail(userId, nowTime, appTenderCreditCustomize.getBorrowNid(),
						appTenderCreditCustomize.getTenderNid());
				//
				if (appTenderToCreditDetail != null && appTenderToCreditDetail.size() > 0) {
					int lastdays = 0;
					String borrowNid = appTenderToCreditDetail.get(0).getBorrowNid();
					// 根据borrowNid判断是否分期
					Borrow borrow = this.appTenderCreditService.selectBorrowByBorrowNid(borrowNid);
					String borrowStyle = borrow.getBorrowStyle();
					if (borrowStyle.equals(CalculatesUtil.STYLE_END) || borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
						lastdays = GetDate
								.daysBetween(GetDate.getDateTimeMyTimeInMillis(nowTime), GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(appTenderToCreditDetail.get(0).getRecoverTime())));
					}
					// 等额本息和等额本金和先息后本
					if (borrowStyle.equals(CalculatesUtil.STYLE_MONTH) || borrowStyle.equals(CalculatesUtil.STYLE_PRINCIPAL) || borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {
						List<BorrowRepayPlan> list = this.appTenderCreditService.searchBorrowRepayPlanList(borrowNid, borrow.getBorrowPeriod());
						if (list != null && list.size() > 0) {
							lastdays = GetDate.daysBetween(GetDate.getDateTimeMyTimeInMillis(nowTime), GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(list.get(0).getRepayTime())));
						}
					}
					appTenderToCreditDetail.get(0).setLastDays(lastdays + "");
					creditResultBean.setResultFlag(CustomConstants.RESULT_SUCCESS);
					creditResultBean.setMsg("");
					creditResultBean.setData(appTenderToCreditDetail.get(0));

					Map<String, BigDecimal> creditCreateMap = this.appTenderCreditService.selectassignInterestForBigDecimal(appTenderCreditCustomize.getBorrowNid(),
							appTenderCreditCustomize.getTenderNid(), "0.0", nowTime.intValue());
					if (creditCreateMap != null) {
						modelAndView.addObject("assignInterest", creditCreateMap.get("assignInterestAdvance"));
					}
					modelAndView.addObject("userId", userId);
					modelAndView.addObject("platform", platform);
				} else {
					System.out.println("获取债转数据失败");
					modelAndView.setViewName(AppTenderCreditDefine.TENDER_TO_CREDIT_ERROR_PATH);
					creditResultBean.setResultFlag(CustomConstants.RESULT_FAIL);
					creditResultBean.setMsg("获取债转数据失败");
					creditResultBean.setData(null);
					modelAndView.addObject("msg", "获取债转数据失败!");
					modelAndView.addObject("msg1", "请刷新列表重试!");
					return modelAndView;
				}
			} else {
				LogUtil.infoLog(this.getClass().getName(), "searchTenderToCreditDetail", "用户未登录");
				creditResultBean.setResultFlag(CustomConstants.RESULT_FAIL);
				creditResultBean.setMsg("用户未登录");
				creditResultBean.setData(null);
			}
		} catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), "searchTenderToCreditDetail", "系统异常");
			modelAndView.setViewName(AppTenderCreditDefine.TENDER_TO_CREDIT_ERROR_PATH);
			creditResultBean.setResultFlag(CustomConstants.RESULT_FAIL);
			creditResultBean.setMsg("系统异常");
			creditResultBean.setData(null);
			modelAndView.addObject("msg", "系统异常");
			return modelAndView;
		}

		LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_DETAIL);
		modelAndView.addObject("creditResult", creditResultBean);
		return modelAndView;
	}

	/**
	 * 发送短信验证码（ajax请求） 短信验证码数据保存
	 */
	@ResponseBody
	@RequestMapping(value = AppTenderCreditDefine.TENDER_TO_CREDIT_SEND_CODE, method = RequestMethod.POST)
	public String sendCode(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_SEND_CODE);
		String userId = request.getParameter("userId");
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		JSONObject jo = new JSONObject();
		UserInfoCustomize userInfo = this.appTenderCreditService.getUserInfoByUserId(userId);
		if (userInfo != null) {
			// 手机号码(必须,数字,最大长度)
			String mobile = userInfo.getMobile();
			SmsConfig smsConfig = appTenderCreditService.getSmsConfig();
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
						appTenderCreditService.sendSms(mobile, "IP访问次数超限:" + ip);
					} catch (Exception e) {
						LogUtil.errorLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_SEND_CODE, e);
					}
					RedisUtils.set(ip + ":MaxIpCount", (Integer.valueOf(ipCount) + 1) + "", 24 * 60 * 60);
				}
				try {
					appTenderCreditService.sendEmail(mobile, "IP访问次数超限" + ip);
				} catch (Exception e) {
					LogUtil.errorLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_SEND_CODE, e);
				}
				jo.put(AppTenderCreditDefine.CODE_ERROR, AppTenderCreditDefine.ERROR_MAXCOUNT_OTHERS);
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
				ret.put(CustomConstants.DATA, jo);
				ret.put(CustomConstants.MSG, "IP访问次数超限");
				LogUtil.errorLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_SEND_CODE, "短信验证码发送失败", null);
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
						appTenderCreditService.sendSms(mobile, "手机发送次数超限");
					} catch (Exception e) {
						LogUtil.errorLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_SEND_CODE, e);
					}
					RedisUtils.set(mobile + ":MaxPhoneCount", (Integer.valueOf(count) + 1) + "", 24 * 60 * 60);
				}
				try {
					appTenderCreditService.sendEmail(mobile, "手机发送次数超限");
				} catch (Exception e) {
					LogUtil.errorLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_SEND_CODE, e);
				}
				jo.put(AppTenderCreditDefine.CODE_ERROR, AppTenderCreditDefine.ERROR_MAXCOUNT_OTHERS);
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
				ret.put(CustomConstants.DATA, jo);
				ret.put(CustomConstants.MSG, "手机发送次数超限");
				LogUtil.errorLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_SEND_CODE, "短信验证码发送失败", null);
				return JSONObject.toJSONString(ret);
			}
			// 判断发送间隔时间
			String intervalTime = RedisUtils.get(mobile + ":IntervalTime");
			System.out.println(mobile + "----------IntervalTime-----------" + intervalTime);
			if (!StringUtils.isEmpty(intervalTime)) {
				jo.put(AppTenderCreditDefine.CODE_ERROR, AppTenderCreditDefine.ERROR_INTERVAL_TIME_OTHERS);
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
				ret.put(CustomConstants.DATA, jo);
				ret.put(CustomConstants.MSG, "发送时间间隔太短");
				LogUtil.errorLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_SEND_CODE, "短信验证码发送失败", null);
				return JSONObject.toJSONString(ret);
			}
			if (!ValidatorCheckUtil.validateRequired(info, null, null, mobile)) {
				jo.put(AppTenderCreditDefine.MOBILE_ERROR, AppTenderCreditDefine.ERROR_TYPE_REQUIRED);
			} else if (!ValidatorCheckUtil.validateMobile(info, null, null, mobile, 11, false)) {
				jo.put(AppTenderCreditDefine.MOBILE_ERROR, AppTenderCreditDefine.ERROR_TYPE_OTHERS);
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
				this.appTenderCreditService.saveSmsCode(mobile, checkCode);
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
				ret.put(CustomConstants.MSG, "手机验证码已经发送完成");
				LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_SEND_CODE);
				return JSONObject.toJSONString(ret);
			} else {
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
				ret.put(CustomConstants.DATA, jo);
				ret.put(CustomConstants.MSG, "系统错误");
				LogUtil.errorLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_SEND_CODE, "短信验证码发送失败", null);
				return JSONObject.toJSONString(ret);
			}
		} else {
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
			ret.put(CustomConstants.DATA, "");
			ret.put(CustomConstants.MSG, "用户未登录");
			LogUtil.errorLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_SEND_CODE, "短信验证码发送失败", null);
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
	@RequestMapping(AppTenderCreditDefine.TENDER_TO_CREDIT_CHECK_CODE)
	public boolean checkCode(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_CHECK_CODE);
		String userId = request.getParameter("userId");
		UserInfoCustomize userInfo = this.appTenderCreditService.getUserInfoByUserId(userId);
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		JSONObject jo = new JSONObject();
		if (userInfo != null) {
			// 手机号码(必须,数字,最大长度)
			String mobile = userInfo.getMobile();
			if (!ValidatorCheckUtil.validateRequired(info, null, null, mobile)) {
				jo.put(AppTenderCreditDefine.MOBILE_ERROR, AppTenderCreditDefine.ERROR_TYPE_REQUIRED);
			} else if (!ValidatorCheckUtil.validateMobile(info, null, null, mobile, 11, false)) {
				jo.put(AppTenderCreditDefine.MOBILE_ERROR, AppTenderCreditDefine.ERROR_TYPE_OTHERS);
			}
			// 短信验证码
			String code = request.getParameter("code");
			if (!ValidatorCheckUtil.validateRequired(info, null, null, code)) {
				jo.put(AppTenderCreditDefine.CODE_ERROR, AppTenderCreditDefine.ERROR_TYPE_REQUIRED);
			}
			if (jo == null || jo.isEmpty()) {
				int cnt = this.appTenderCreditService.checkMobileCode(mobile, code);
				if (cnt > 0) {
					ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
					ret.put(CustomConstants.DATA, jo);
					ret.put(CustomConstants.MSG, "");
					return true;
				} else {
					jo.put(AppTenderCreditDefine.CODE_ERROR, AppTenderCreditDefine.ERROR_TYPE_OTHERS);
					LogUtil.errorLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_CHECK_CODE, "验证码验证失败", null);
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
		LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_CHECK_CODE);
		return false;
	}

	/**
	 * 
	 * 债转提交保存
	 * 
	 * @author liuyang
	 * @param request
	 * @param response
	 * @param appTenderBorrowCreditCustomize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppTenderCreditDefine.TENDER_TO_CREDIT_SAVE, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String saveTenderToCredit(HttpServletRequest request, HttpServletResponse response, @ModelAttribute AppTenderBorrowCreditCustomize appTenderBorrowCreditCustomize) {
		LogUtil.startLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_LIST);
		// 返回的JSON对象
		JSONObject ret = new JSONObject();
		// 用户id
		String userId = request.getParameter("userId");
		String platform = request.getParameter("platform");
		try {
			if (StringUtils.isNotEmpty(userId) && Integer.valueOf(userId) != 0) {
				UserInfoCustomize userInfo = this.appTenderCreditService.getUserInfoByUserId(userId);
				if (userInfo != null) {
					Integer nowTime = GetDate.getNowTime10();// 获取当前时间
					// 获取当前时间的日期
					String nowDate = (GetDate.yyyyMMdd.format(new Date()) != null && !"".equals(GetDate.yyyyMMdd.format(new Date()))) ? GetDate.yyyyMMdd.format(new Date()) : "0";
					Integer creditedNum = this.appTenderCreditService.tenderAbleToCredit(Integer.parseInt(userId), Integer.parseInt(nowDate));
					if (creditedNum != null && creditedNum >= 3) {
						ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
						ret.put("resultCode", "TenderCredit-CreditMore");
						ret.put(CustomConstants.DATA, null);
						ret.put(CustomConstants.MSG, "今天的转让次数已满3次,请明天再试");
						LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_SAVE);
						return JSONObject.toJSONString(ret, true);
					}
					// 验证BorrowNid和TenderNid
					if (StringUtils.isEmpty(appTenderBorrowCreditCustomize.getBorrowNid()) || StringUtils.isEmpty(appTenderBorrowCreditCustomize.getTenderNid())) {
						ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
						ret.put(CustomConstants.DATA, null);
						ret.put(CustomConstants.MSG, "无法获取债转借款编号和出借编号");
						LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_SAVE);
						return JSONObject.toJSONString(ret, true);
					}
					// 根据BorrowNid和TenderNid判断,用户是否已经发起债转
					BorrowCredit borrowCredit = this.appTenderCreditService.selectBorrowCreditByBorrowNid(appTenderBorrowCreditCustomize.getBorrowNid(), appTenderBorrowCreditCustomize.getTenderNid(),
							userId);
					if (borrowCredit != null) {
						ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
						ret.put(CustomConstants.DATA, null);
						ret.put(CustomConstants.MSG, "债权转让中，请勿重复提交申请");
						LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_SAVE);
						return JSONObject.toJSONString(ret, true);
					}
					// 验证折价率
					if (StringUtils.isEmpty(appTenderBorrowCreditCustomize.getCreditDiscount())) {
						ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
						ret.put(CustomConstants.DATA, null);
						ret.put(CustomConstants.MSG, "折让率不能为空");
						LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_SAVE);
						return JSONObject.toJSONString(ret, true);
					} else {
						if (appTenderBorrowCreditCustomize.getCreditDiscount().matches("\\d{1}\\.\\d{1}")) {
							float creditDiscount = Float.parseFloat(appTenderBorrowCreditCustomize.getCreditDiscount());
							if (creditDiscount > 2.0 || creditDiscount < 0.5) {
								ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
								ret.put(CustomConstants.DATA, null);
								ret.put(CustomConstants.MSG, "折让率范围错误");
								LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_SAVE);
								return JSONObject.toJSONString(ret, true);
							}
						} else {
							ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
							ret.put(CustomConstants.DATA, null);
							ret.put(CustomConstants.MSG, "折让率格式错误");
							LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_SAVE);
							return JSONObject.toJSONString(ret, true);
						}
					}
					// 验证手机验证码
					if (StringUtils.isEmpty(appTenderBorrowCreditCustomize.getTelcode())) {
						ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
						ret.put(CustomConstants.DATA, null);
						ret.put(CustomConstants.MSG, "请输入手机验证码");
						LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_SAVE);
						return JSONObject.toJSONString(ret, true);
					} else {
						// 验证手机验证码
						int cnt = this.appTenderCreditService.checkMobileCode(userInfo.getMobile(), appTenderBorrowCreditCustomize.getTelcode());
						if (cnt <= 0) {
							ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
							ret.put(CustomConstants.DATA, null);
							ret.put(CustomConstants.MSG, "手机验证码错误");
							LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_SAVE);
							return JSONObject.toJSONString(ret, true);
						}
					}
					// 债转保存
					Integer tenderCredit = appTenderCreditService.insertTenderToCredit(Integer.parseInt(userId), nowTime, appTenderBorrowCreditCustomize, platform);
					if (tenderCredit != null && tenderCredit > 0) {
						ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
						ret.put(CustomConstants.DATA, tenderCredit);
						ret.put(CustomConstants.MSG, "保存成功");
					} else {
						ret.put(CustomConstants.RESULT_FLAG, AppTenderCreditDefine.RESULT_ERROR);
						ret.put(CustomConstants.DATA, null);
						ret.put(CustomConstants.MSG, "保存时出现异常,请联系客服");
					}
				} else {
					LogUtil.infoLog(this.getClass().getName(), "saveTenderToCredit", "用户未登录");
					ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
					ret.put(CustomConstants.DATA, null);
					ret.put(CustomConstants.MSG, "用户未登录");
				}
			} else {
				LogUtil.infoLog(this.getClass().getName(), "saveTenderToCredit", "用户未登录");
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
				ret.put(CustomConstants.DATA, null);
				ret.put(CustomConstants.MSG, "用户未登录");
			}
		} catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), "saveTenderToCredit", "系统异常");
			ret.put(CustomConstants.RESULT_FLAG, AppTenderCreditDefine.RESULT_ERROR);
			ret.put(CustomConstants.DATA, null);
			ret.put(CustomConstants.MSG, "系统异常");
		}
		LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_LIST);
		return JSONObject.toJSONString(ret, true);
	}

	/**
	 * 
	 * 提交债转后页面跳转
	 * 
	 * @author liuyang
	 * @param request
	 * @param response
	 * @param creditNid
	 * @return
	 */
	@RequestMapping(value = AppTenderCreditDefine.TENDER_TO_CREDIT_RESULT)
	public ModelAndView tenderToCreditResult(HttpServletRequest request, HttpServletResponse response, @RequestParam String creditNid) {
		LogUtil.startLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_RESULT);
		ModelAndView modelAndView = new ModelAndView();
		String resultFlag = request.getParameter("resultFlag");
		String msg = request.getParameter("msg");
		if (StringUtils.isEmpty(resultFlag) && StringUtils.isEmpty(msg)) {
			if (StringUtils.isNotEmpty(creditNid)) {
				List<BorrowCredit> borrowCreditList = appTenderCreditService.selectBorrowCreditByNid(creditNid);
				if (borrowCreditList != null && borrowCreditList.size() > 0) {
					modelAndView.setViewName(AppTenderCreditDefine.TENDER_TO_CREDIT_RESULT_PATH);
					BorrowCredit creditTender = borrowCreditList.get(0);
					modelAndView.addObject("borrowCredit", creditTender);
				} else {
					// modelAndView.setViewName(AppAppTenderCreditDefine.CREDIT_500_PATH);
					modelAndView.setViewName(AppTenderCreditDefine.TENDER_TO_CREDIT_ERROR_PATH);
					modelAndView.addObject("msg", "获取债转信息失败");
				}
			}
		} else {
			modelAndView.setViewName(AppTenderCreditDefine.TENDER_TO_CREDIT_ERROR_PATH);
			modelAndView.addObject("msg", msg);
		}
		LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.TENDER_TO_CREDIT_RESULT);
		return modelAndView;
	}

	/**
	 * 
	 * 查询已承接债转列表
	 * 
	 * @author liuyang
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppTenderCreditDefine.CREDIT_ASSIGN_LIST, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public JSONObject searchCreditAssignList(@ModelAttribute AppTenderCreditBean form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.CREDIT_ASSIGN_LIST);
		JSONObject ret = new JSONObject();
		Map<String, Object> params = new HashMap<String, Object>();
		// 承接详情URL
		params.put("host", HOST_URL + AppTenderCreditDefine.REQUEST_HOME + AppTenderCreditDefine.REQUEST_MAPPING + AppTenderCreditDefine.CREDIT_ASSIGN_DETAIL);
		// 债转协议URL
		params.put("creditContractUrl", HOST_URL + AppTenderCreditDefine.REQUEST_HOME + AppTenderCreditDefine.REQUEST_MAPPING + AppTenderCreditDefine.CREDIT_CONTRACT);
		ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
		ret.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
		ret.put(CustomConstants.APP_REQUEST, AppTenderCreditDefine.REQUEST_HOME + AppTenderCreditDefine.REQUEST_MAPPING + AppTenderCreditDefine.CREDIT_ASSIGN_LIST);
		String sign = request.getParameter("sign");
		// 获取用户id
		Integer userId = SecretUtil.getUserId(sign);
		// Integer userId = 22400081;
		if (userId != null && userId.intValue() != 0) {
			params.put("userId", userId);
			params.put("sign", sign);
			// 查询用户承接债转的总数
			int recordTotal = this.appTenderCreditService.countCreditAssigned(params);

			if (recordTotal > 0) {
				int limit = form.getPageSize();
				int page = form.getPage();
				int offSet = (page - 1) * limit;
				if (offSet == 0 || offSet > 0) {
					params.put("limitStart", offSet);
				}
				if (limit > 0) {
					params.put("limitEnd", limit);
				}
				// 查询已承接汇转让列表数据
				long timestamp = System.currentTimeMillis() / 1000;
				params.put("timestamp", String.valueOf(timestamp));
				List<AppTenderCreditAssignedListCustomize> resultList = this.appTenderCreditService.selectCreditAssigned(params);
				if (resultList != null && resultList.size() > 0) {
					// 已承接列表信息
					ret.put("creditAssignList", resultList);
					ret.put("creditAssignListTotal", String.valueOf(recordTotal));
				} else {
					// 已承接列表信息
					ret.put("creditAssignList", new ArrayList<AppTenderCreditAssignedListCustomize>());
					ret.put("creditAssignListTotal", 0);
				}
			} else {
				// 检索已承接列表为0件的情况
				ret.put("creditAssignList", new ArrayList<AppTenderCreditAssignedListCustomize>());
				ret.put("creditAssignListTotal", String.valueOf(recordTotal));
			}
		}
		LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.CREDIT_ASSIGN_LIST);
		return ret;
	}

	/**
	 * 
	 * 承接债转详情
	 * 
	 * @author liuyang
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = AppTenderCreditDefine.CREDIT_ASSIGN_DETAIL)
	public ModelAndView getCreditAssignDetail(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.CREDIT_ASSIGN_DETAIL);
		ModelAndView modelAndView = new ModelAndView(AppTenderCreditDefine.CREDIT_ASSIGN_DETAIL_PATH);
		Map<String, Object> params = new HashMap<String, Object>();
		// 债转投标单号
		String assignNid = request.getParameter("assignNid");
		String userId = request.getParameter("userId");
		params.put("userId", userId);
		params.put("assignNid", assignNid);
		// 承接详情
		AppTenderCreditAssignedDetailCustomize tenderCreditAssignedDetail = this.appTenderCreditService.getCreditAssignDetail(params);
		if (tenderCreditAssignedDetail != null) {
			modelAndView.addObject("tenderCreditAssignedDetail", tenderCreditAssignedDetail);
		} else {
			System.out.println("------获取债转承接详情失败-------:userId=" + userId + " assignNid=" + assignNid);
			modelAndView = new ModelAndView(AppTenderCreditDefine.ERROR_PTAH);
			return modelAndView;
		}
		LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.CREDIT_ASSIGN_DETAIL);
		return modelAndView;
	}

	/**
	 * 
	 * 获取转让记录列表
	 * 
	 * @author liuyang
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppTenderCreditDefine.CREDIT_RECORD_LIST, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public JSONObject searchCreditRecordList(@ModelAttribute AppTenderCreditBean form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.CREDIT_ASSIGN_LIST);
		JSONObject ret = new JSONObject();
		Map<String, Object> params = new HashMap<String, Object>();
		String status = request.getParameter("status");
		form.setStatus(status);
		// 转让详情URL
		params.put("host", HOST_URL + AppTenderCreditDefine.REQUEST_HOME + AppTenderCreditDefine.REQUEST_MAPPING + AppTenderCreditDefine.CREDIT_RECORD_DETAIL);
		ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
		ret.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
		ret.put(CustomConstants.APP_REQUEST, AppTenderCreditDefine.REQUEST_HOME + AppTenderCreditDefine.REQUEST_MAPPING + AppTenderCreditDefine.CREDIT_RECORD_LIST);
		String sign = request.getParameter("sign");

		// 获取用户id
		Integer userId = SecretUtil.getUserId(sign);

		if (userId != null && userId.intValue() != 0) {
			// 用户id
			params.put("userId", userId);
			params.put("sign", sign);
			// 转让记录状态:0:转让中,1:转让成功,2:全部
			if ("0".equals(form.getStatus())) {
				params.put("status", "0");
			} else if ("1".equals(form.getStatus())) {
				params.put("successStatus", "0");
				// 已认购金额不为0
				params.put("creditCapitalAssigned", "0");
			}

			params.put("countStatus", form.getStatus());
			// 查询债转记录件数
			int recordTotal = this.appTenderCreditService.countCreditRecord(params);

			if (recordTotal > 0) {
				int limit = form.getPageSize();
				int page = form.getPage();
				int offSet = (page - 1) * limit;
				if (offSet == 0 || offSet > 0) {
					params.put("limitStart", offSet);
				}
				if (limit > 0) {
					params.put("limitEnd", limit);
				}
				// 检索转让记录列表
				List<AppTenderCreditRecordListCustomize> recordList = this.appTenderCreditService.searchCreditRecordList(params);
				if (recordList != null && recordList.size() > 0) {
					// 转让记录列表
					ret.put("creditRecordListStatus", status);
					ret.put("creditRecordList", recordList);
					ret.put("creditRecordListTotal", String.valueOf(recordTotal));
				} else {
					// 检索转让记录列表为0件的情况
					ret.put("creditRecordListStatus", status);
					ret.put("creditRecordList", new ArrayList<AppTenderCreditRecordListCustomize>());
					ret.put("creditRecordListTotal", 0);
				}
			} else {
				// 检索转让记录列表为0件的情况
				ret.put("creditRecordListStatus", status);
				ret.put("creditRecordList", new ArrayList<AppTenderCreditRecordListCustomize>());
				ret.put("creditRecordListTotal", String.valueOf(recordTotal));
			}

		}
		return ret;
	}

	/**
	 * 
	 * 获取债转转让详情
	 * 
	 * @author liuyang
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = AppTenderCreditDefine.CREDIT_RECORD_DETAIL)
	public ModelAndView getCreditRecordDetail(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.CREDIT_RECORD_DETAIL);
		ModelAndView modelAndView = new ModelAndView(AppTenderCreditDefine.CREDIT_RECORD_DETAIL_PATH);

		Map<String, Object> params = new HashMap<String, Object>();
		// 1.获取债转编号
		String creditNid = request.getParameter("creditNid");

		String sign = request.getParameter("sign");

		String version = request.getParameter("version");

		// 获取用户id
		Integer userId = SecretUtil.getUserId(sign);

		// 用户id
		params.put("userId", userId);
		// 债转编号
		params.put("creditNid", creditNid);
		// 获取转让记录详情
		AppTenderCreditRecordDetailCustomize tenderCreditRecordInfo = this.appTenderCreditService.selectTenderCreditRecordDetail(params);
		if (tenderCreditRecordInfo == null) {
			System.out.println("---------获取转让记录详情失败--------userId=" + userId + " creditNid=" + creditNid);
			modelAndView = new ModelAndView(AppTenderCreditDefine.ERROR_PTAH);
			return modelAndView;
		} else {
			tenderCreditRecordInfo
					.setTransferDetailUrl(GetJumpCommand.getLinkJumpPrefix(request, version) + "://jumpTransferDetailList/?{'creditNid':'" + tenderCreditRecordInfo.getCreditNid() + "'}");
			// 添加相应的项目详情信息
			modelAndView.addObject("tenderCreditRecordInfo", tenderCreditRecordInfo);
		}
		LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.CREDIT_RECORD_DETAIL);
		return modelAndView;
	}

	/**
	 * 获取债转转让明细列表
	 * 
	 * @author liuyang
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppTenderCreditDefine.CREDIT_RECORD_DETAIL_LIST, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public JSONObject getCreditRecordDetailList(@ModelAttribute AppTenderCreditBean form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.CREDIT_RECORD_DETAIL_LIST);
		JSONObject ret = new JSONObject();
		Map<String, Object> params = new HashMap<String, Object>();
		// 拼接债转协议URL
		params.put("host", HOST_URL + AppTenderCreditDefine.REQUEST_HOME + AppTenderCreditDefine.REQUEST_MAPPING + AppTenderCreditDefine.CREDIT_CONTRACT);
		ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
		ret.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
		ret.put(CustomConstants.APP_REQUEST, AppTenderCreditDefine.REQUEST_HOME + AppTenderCreditDefine.REQUEST_MAPPING + AppTenderCreditDefine.CREDIT_RECORD_DETAIL_LIST);
		// 债转编号
		String creditNid = request.getParameter("creditNid");
		if (StringUtils.isNotEmpty(creditNid)) {
			params.put("creditNid", creditNid);
		}
		String sign = request.getParameter("sign");
		// 获取用户id
		Integer userId = SecretUtil.getUserId(sign);
		params.put("userId", userId);
		params.put("sign", sign);
		// 获取债权转让明细列表件数
		int recordTotal = this.appTenderCreditService.countCreditRecordDetailList(params);
		if (recordTotal > 0) {
			int limit = form.getPageSize();
			int page = form.getPage();
			int offSet = (page - 1) * limit;
			if (offSet == 0 || offSet > 0) {
				params.put("limitStart", offSet);
			}
			if (limit > 0) {
				params.put("limitEnd", limit);
			}
			// 获取转让记录明细列表
			List<AppTenderCreditRecordDetailListCustomize> recordList = this.appTenderCreditService.getCreditRecordDetailList(params);
			if (recordList != null && recordList.size() > 0) {
				ret.put("creditRecordDetailList", recordList);
				ret.put("creditRecordDetailListCount", String.valueOf(recordTotal));
			} else {
				System.out.println("----------获取转让记录明细列表失败-----------creditNid=" + creditNid + " userId = " + userId);
				ret.put("creditRecordDetailList", new ArrayList<AppTenderCreditRecordDetailListCustomize>());
				ret.put("creditRecordDetailListCount", 0);
			}
		} else {
			// 检索转让记录列表为0件的情况
			ret.put("creditRecordDetailList", new ArrayList<AppTenderCreditRecordListCustomize>());
			ret.put("creditRecordDetailListCount", String.valueOf(recordTotal));
		}
		LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.CREDIT_RECORD_DETAIL_LIST);
		return ret;
	}

	/**
	 * 
	 * 获取用户债权转让协议
	 * 
	 * @author liuyang
	 * @param request
	 * @param response
	 * @param appTenderCreditAssignedBean
	 * @return
	 */
	@RequestMapping(value = AppTenderCreditDefine.CREDIT_CONTRACT)
	public ModelAndView userCreditContract(HttpServletRequest request, HttpServletResponse response, @ModelAttribute AppTenderCreditAssignedBean appTenderCreditAssignedBean) {
		LogUtil.startLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.CREDIT_CONTRACT);
		ModelAndView modelAndView = new ModelAndView(AppTenderCreditDefine.CREDIT_CONTRACT_PATH);
		CreditResultBean result = new CreditResultBean();

		String sign = request.getParameter("sign");
		// 获取用户id
		Integer userId = SecretUtil.getUserId(sign);
		try {
			if (userId != null && userId.intValue() != 0) {
				if (StringUtils.isEmpty(appTenderCreditAssignedBean.getBidNid()) || StringUtils.isEmpty(appTenderCreditAssignedBean.getCreditNid())
						|| StringUtils.isEmpty(appTenderCreditAssignedBean.getCreditTenderNid()) || StringUtils.isEmpty(appTenderCreditAssignedBean.getAssignNid())) {
					result.setResultFlag(CustomConstants.RESULT_FAIL);
					result.setMsg("查看失败,参数不符");
					result.setData(null);
					LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.CREDIT_CONTRACT);

					modelAndView.addObject("creditResult", result);
					return modelAndView;
				}
				Map<String, Object> creditContract = this.appTenderCreditService.selectUserCreditContract(appTenderCreditAssignedBean);
				result.setResultFlag(CustomConstants.RESULT_SUCCESS);
				result.setMsg("");
				result.setData(creditContract);
			} else {
				LogUtil.infoLog(this.getClass().getName(), "userCreditContract", "用户未登录");

				result.setResultFlag(CustomConstants.RESULT_FAIL);
				result.setMsg("用户未登录");
				result.setData(null);
			}
		} catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), "userCreditContract", "系统异常");
			result.setResultFlag(CustomConstants.RESULT_FAIL);
			result.setMsg("系统异常");
			result.setData(null);
		}
		modelAndView.addObject("creditResult", result);
		LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.CREDIT_CONTRACT);
		return modelAndView;
	}

	/**
	 * 债转交易成功后回调处理
	 *
	 * @param request
	 * @param bean
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(AppTenderCreditDefine.RETURN_MAPPING)
	public synchronized ModelAndView tenderCreditReturn(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
		ModelAndView modelAndView = null;
		bean.convert();
		String message = "";
		Integer userId = Integer.parseInt(bean.getLogUserId());// 用户ID
		String logOrderId = bean.getLogOrderId() == null ? "" : bean.getLogOrderId();
		_log.info("债转出借收到回调,承接订单号:[" + logOrderId + "],承接用户ID:[" + userId + "]");
		// 更新相应的债转承接log表
		boolean updateFlag = this.appTenderCreditService.updateCreditTenderLog(logOrderId, userId);
		// 查询相应的债转承接记录
        CreditTenderLog creditTenderLog = this.appTenderCreditService.selectCreditTenderLogByOrderId(logOrderId);
		// 如果更新成功(异步没有回调成功)
		if (updateFlag) {
			// 调用相应的查询接口查询此笔承接的相应的承接状态
			BankCallBean tenderQueryBean = this.appTenderCreditService.creditInvestQuery(logOrderId, userId);
			// 判断返回结果是不是空
			if (Validator.isNotNull(tenderQueryBean)) {
				// bean实体转化
				tenderQueryBean.convert();
				// 获取债转查询返回码
				String retCode = StringUtils.isNotBlank(tenderQueryBean.getRetCode()) ? tenderQueryBean.getRetCode() : "";
				String isSuccess = request.getParameter("isSuccess");
				_log.info("出借人购买债券查询接口:银行返回码:[" + retCode + "].");
				_log.info("出借人购买债券查询接口:isSuccess:[" + isSuccess + "].");
				// 承接成功
				if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)||!"1".equals(isSuccess)) {
					_log.info("承接失败,跳转失败页......失败原因: {}", JSONObject.toJSONString(tenderQueryBean));
					// 直接返回查询银行债转状态查询失败

					message = this.appTenderCreditService.getBankRetMsg(tenderQueryBean.getRetCode());

					if(StringUtils.isBlank(message)){
						message = "交易失败";
					}
					modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
	                BaseMapBean baseMapBean=new BaseMapBean();
	                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
	                baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
	                baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_ERROR_PATH.replace("{transferId}", creditTenderLog.getCreditNid()));
	                modelAndView.addObject("callBackForm", baseMapBean);
					_log.info("债转出借结果baseMapBean: {}",JSONObject.toJSONString(baseMapBean));
					return modelAndView;
				}
				// 如果已经查询到相应的债转承接log表
				if (Validator.isNotNull(creditTenderLog)) {
					// 债转编号
					String creditNid = creditTenderLog.getCreditNid();
					// 银行承接金额
					String amount = tenderQueryBean.getTsfAmount();
					try {
						// 此次查询的授权码
						String authCode = tenderQueryBean.getAuthCode();
						_log.info("债转承接后,调用出借人购买债权查询接口,银行返回授权码:["+authCode+"].");
						if (StringUtils.isNotBlank(authCode)) {
							// 更新债转汇付交易成功后的相关信息
							boolean tenderFlag = this.appTenderCreditService.updateTenderCreditInfo(logOrderId, userId, authCode);
							if (tenderFlag) {
								_log.info("银行债转承接成功后,更新本地数据成功,承接订单号:["+logOrderId+"].承接用户ID:["+userId+"].");
								// 查询相应的承接记录，如果相应的承接记录存在，则承接成功
								CreditTender creditTender = appTenderCreditService.creditTenderByAssignNid(logOrderId, userId);
								if (creditTender != null) {
									message = "恭喜您，汇转让出借成功！";
									// add by liuyang 20180305 发送法大大PDF处理MQ start
									this.appTenderCreditService.sendPdfMQ(userId, creditTender.getBidNid(), creditTender.getAssignNid(), creditTender.getCreditNid(), creditTender.getCreditTenderNid());
									// add by liuyang 20180305 发送法大大PDF处理MQ end
									modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
				                    BaseMapBean baseMapBean=new BaseMapBean();
				                    baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
				                    baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);

				                    // 出借本金
									baseMapBean.set("amount", CommonUtils.formatAmount(creditTender.getAssignCapital()));
									// 历史回报
									baseMapBean.set("income", String.valueOf(creditTender.getAssignInterest()));

				                    baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_SUCCESS_PATH.replace("{transferId}", creditTenderLog.getCreditNid()));
				                    modelAndView.addObject("callBackForm", baseMapBean);
									return modelAndView;
								} else {
									message = "交易正在处理中，请稍后债转管理内查看";
									modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
				                    BaseMapBean baseMapBean=new BaseMapBean();
				                    baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
				                    baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
				                    baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_HANDLING_PATH.replace("{transferId}", creditTenderLog.getCreditNid()));
				                    modelAndView.addObject("callBackForm", baseMapBean);
									return modelAndView;
								}
							} else {
								message = "交易正在处理中，请稍后债转管理内查看";
								modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
			                    BaseMapBean baseMapBean=new BaseMapBean();
			                    baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			                    baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
			                    baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_HANDLING_PATH.replace("{transferId}", creditTenderLog.getCreditNid()));
			                    modelAndView.addObject("callBackForm", baseMapBean);
								return modelAndView;
							}
						} else {
							message = "交易正在处理中，请稍后债转管理内查看";
							modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
		                    BaseMapBean baseMapBean=new BaseMapBean();
		                    baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
		                    baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
		                    baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_HANDLING_PATH.replace("{transferId}", creditTenderLog.getCreditNid()));
		                    modelAndView.addObject("callBackForm", baseMapBean);
							return modelAndView;
						}
					} catch (Exception e) {
						message = "交易正在处理中，请稍后债转管理内查看";
						modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
	                    BaseMapBean baseMapBean=new BaseMapBean();
	                    baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
	                    baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
	                    baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_HANDLING_PATH.replace("{transferId}", creditTenderLog.getCreditNid()));
	                    modelAndView.addObject("callBackForm", baseMapBean);
						return modelAndView;
					}
				} else {
					_log.info("未查询到债转日志，直接查询承接记录....");
					// 查询相应的承接记录，如果相应的承接记录存在，则承接成功
					CreditTender creditTender = appTenderCreditService.creditTenderByAssignNid(logOrderId, userId);
					if (creditTender != null) {
						message = "恭喜您，汇转让出借成功！";
						// add by liuyang 20180305 发送法大大PDF处理MQ start
						this.appTenderCreditService.sendPdfMQ(userId, creditTender.getBidNid(), creditTender.getAssignNid(), creditTender.getCreditNid(), creditTender.getCreditTenderNid());
						// add by liuyang 20180305 发送法大大PDF处理MQ end
						modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
                        BaseMapBean baseMapBean=new BaseMapBean();
                        baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                        baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
						// 出借本金
						baseMapBean.set("amount", CommonUtils.formatAmount(creditTender.getAssignCapital()));
						// 历史回报
						baseMapBean.set("income", String.valueOf(creditTender.getAssignInterest().add(creditTender.getAssignCapital())));
                        baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_SUCCESS_PATH.replace("{transferId}", creditTenderLog.getCreditNid()));
                        modelAndView.addObject("callBackForm", baseMapBean);
						return modelAndView;
					} else {
						message = "交易正在处理中，请稍后债转管理内查看";
						modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
                        BaseMapBean baseMapBean=new BaseMapBean();
                        baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                        baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
                        baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_HANDLING_PATH.replace("{transferId}", creditTenderLog.getCreditNid()));
                        modelAndView.addObject("callBackForm", baseMapBean);
						return modelAndView;
					}
				}
			} else {
				// 直接返回查询银行债转状态查询失败
				message = "调用银行接口失败！";
				modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
                BaseMapBean baseMapBean=new BaseMapBean();
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
                baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_HANDLING_PATH.replace("{transferId}", creditTenderLog.getCreditNid()));
                modelAndView.addObject("callBackForm", baseMapBean);
				return modelAndView;
			}
		} else {
			// 查询相应的承接记录，如果相应的承接记录存在，则承接成功
			CreditTender creditTender = appTenderCreditService.creditTenderByAssignNid(logOrderId, userId);
			if (creditTender != null) {
				message = "恭喜您，汇转让出借成功！";
				/*modelAndView = new ModelAndView(InvestDefine.INVEST_SUCCESS_PATH);
				DecimalFormat df = CustomConstants.DF_FOR_VIEW;
				// 出借金额 出借本金
				modelAndView.addObject("account", df.format(creditTender.getAssignCapital()));
				// 债转利息 历史回报
				modelAndView.addObject("interest", df.format(creditTender.getAssignInterest()));
				// 真实支付金额
				modelAndView.addObject("assignpay", df.format(creditTender.getAssignPay()));
				modelAndView.addObject("error", 0);
				creditTender.setAddip(GetDate.timestamptoStrYYYYMMDDHHMMSS(creditTender.getAssignRepayEndTime()));
				modelAndView.addObject("creditTender", creditTender);*/
				// add by liuyang 20180305 发送法大大PDF处理MQ start
				this.appTenderCreditService.sendPdfMQ(userId, creditTender.getBidNid(), creditTender.getAssignNid(), creditTender.getCreditNid(), creditTender.getCreditTenderNid());
				// add by liuyang 20180305 发送法大大PDF处理MQ end
				modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
                BaseMapBean baseMapBean=new BaseMapBean();
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
				// 出借本金
				baseMapBean.set("amount", CommonUtils.formatAmount(creditTender.getAssignCapital()));
				// 预期收益
				baseMapBean.set("income", String.valueOf(creditTender.getAssignInterest()));
                JSONObject info = new JSONObject();
                info.put("account", creditTender.getAssignCapital());
                info.put("interest", creditTender.getAssignInterest());
                info.put("assignpay", creditTender.getAssignPay());
                creditTender.setAddip(GetDate.timestamptoStrYYYYMMDDHHMMSS(creditTender.getAssignRepayEndTime()));
                info.put("creditTender", creditTender);
                baseMapBean.set("successInfo", info.toJSONString());
                baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_SUCCESS_PATH.replace("{transferId}", creditTender.getCreditNid()));
                modelAndView.addObject("callBackForm", baseMapBean);
				return modelAndView;
			} else {
				message = "交易正在处理中，请稍后债转管理内查看";
				modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
				BaseMapBean baseMapBean=new BaseMapBean();
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
				baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
				baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_HANDLING_PATH.replace("{transferId}", creditTenderLog.getCreditNid()));
				modelAndView.addObject("callBackForm", baseMapBean);
				return modelAndView;
			}
		}
	}

	/**
	 * 债转交易成功后异步回调处理
	 *
	 * @param request
	 * @param bean
	 * @return
	 * @throws InterruptedException
	 */
	@ResponseBody
	@RequestMapping(AppTenderCreditDefine.CALLBACK_MAPPING)
	public String tenderCreditCallback(HttpServletRequest request, @ModelAttribute BankCallBean bean) throws InterruptedException {
		bean.convert();
		BankCallResult resultBean = new BankCallResult();
		String message = "";
		Integer userId = Integer.parseInt(bean.getLogUserId());// 用户ID
		String logOrderId = bean.getLogOrderId() == null ? "" : bean.getLogOrderId();
		_log.info("债转承接后,收到异步回调,承接订单号:[" + logOrderId + "],承接用户ID:[" + userId + "].");
		// 更新相应的债转承接log表
		boolean updateFlag = this.appTenderCreditService.updateCreditTenderLog(logOrderId, userId);
		// 如果更新成功(异步没有回调成功)
		if (updateFlag) {
			// 获取债转查询返回码
			String retCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
			_log.info("异步回调,银行返回结果:["+retCode+"].");
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
				// 直接返回查询银行债转状态查询失败
				message = "承接失败，";
				resultBean.setMessage(message);
				resultBean.setStatus(true);
				return JSONObject.toJSONString(resultBean, true);
			}
			// 调用相应的查询接口查询此笔承接的相应的承接状态
			BankCallBean tenderQueryBean = this.appTenderCreditService.creditInvestQuery(logOrderId, userId);
			// 判断返回结果是不是空
			if (Validator.isNotNull(tenderQueryBean)) {
				// bean实体转化
				tenderQueryBean.convert();
				// 获取债转查询返回码
				String queryRetCode = StringUtils.isNotBlank(tenderQueryBean.getRetCode()) ? tenderQueryBean.getRetCode() : "";
				_log.info("收到回调后,查询出借人购买债权接口查询,银行返回结果:[" + queryRetCode + "].");
				// 承接成功
				if (!BankCallConstant.RESPCODE_SUCCESS.equals(queryRetCode)) {
					// 直接返回查询银行债转状态查询失败
					message = "承接失败，";
					resultBean.setMessage(message);
					resultBean.setStatus(true);
					return JSONObject.toJSONString(resultBean, true);
				}
				// 查询相应的债转承接记录
				CreditTenderLog creditTenderLog = this.appTenderCreditService.selectCreditTenderLogByOrderId(logOrderId);
				// 如果已经查询到相应的债转承接log表
				if (Validator.isNotNull(creditTenderLog)) {
					try {
						// 此次查询的授权码
						String authCode = tenderQueryBean.getAuthCode();
						if (StringUtils.isNotBlank(authCode)) {
							// 更新债转汇付交易成功后的相关信息
							boolean tenderFlag = this.appTenderCreditService.updateTenderCreditInfo(logOrderId, userId, authCode);
							if (tenderFlag) {
								// 查询相应的承接记录，如果相应的承接记录存在，则承接成功
								// add by liuyang 20180305 发送法大大PDF处理MQ start
								CreditTender creditTender = appTenderCreditService.creditTenderByAssignNid(logOrderId, userId);
								this.appTenderCreditService.sendPdfMQ(userId, creditTender.getBidNid(), creditTender.getAssignNid(), creditTender.getCreditNid(), creditTender.getCreditTenderNid());
								// add by liuyang 20180305 发送法大大PDF处理MQ end
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
					CreditTender creditTender = appTenderCreditService.creditTenderByAssignNid(logOrderId, userId);
					if (creditTender != null) {
						// add by liuyang 20180305 发送法大大PDF处理MQ start
						this.appTenderCreditService.sendPdfMQ(userId, creditTender.getBidNid(), creditTender.getAssignNid(), creditTender.getCreditNid(), creditTender.getCreditTenderNid());
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
			CreditTender creditTender = appTenderCreditService.creditTenderByAssignNid(logOrderId, userId);
			if (creditTender != null) {
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
		}
	}
}
