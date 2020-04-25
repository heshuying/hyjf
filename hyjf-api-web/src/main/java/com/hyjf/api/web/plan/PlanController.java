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
package com.hyjf.api.web.plan;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.HtmlUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanAccedeCustomize;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanBorrowCustomize;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanCustomize;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanDetailCustomize;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanIntroduceCustomize;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanInvestInfoCustomize;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanQuestionCustomize;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanRiskControlCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

@Controller
@RequestMapping(value = PlanDefine.REQUEST_MAPPING)
public class PlanController extends BaseController {

	@Autowired
	private PlanService planService;

	public static JedisPool pool = RedisUtils.getPool();

	private static String HOST = PropUtils.getSystem("http.hyjf.web.host").trim();

	/**
	 * 获取汇添金计划列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = PlanDefine.PLAN_LIST_ACTION, produces = "application/json; charset=utf-8")
	public JSONObject searchPlanList(@ModelAttribute PlanBean form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(PlanDefine.THIS_CLASS, PlanDefine.PLAN_LIST_ACTION);
		JSONObject info = new JSONObject();
		Map<String, Object> params = new HashMap<String, Object>();
		// 分页信息
		Integer page = form.getPage();
		Integer pageSize = form.getPageSize();
		if (!this.checkSign(form, BaseDefine.METHOD_SEARCH_PLAN_LIST)) {
			info.put("error", BaseResultBean.STATUS_FAIL);
			info.put("", "验签失败！");
			LogUtil.errorLog(this.getClass().getName(), "searchPlanList", "验签失败！", null);
			return info;
		}
		int totalSize = 0;
		try {
			params.put("limitStart", pageSize * (page - 1));
			params.put("limitEnd", pageSize);
			// 统计相应的汇直投的数据记录数
			totalSize = this.planService.queryDebtPlanRecordTotal(params);
			List<WeChatDebtPlanCustomize> planList = planService.searchDebtPlanList(params);
			info.put("planList", planList);
			info.put("totalSize", totalSize);
			info.put("error", "0");// 成功标识位
		} catch (Exception e) {
			info.put("error", "1");
			info.put("errorDesc", "获取列表异常");
		}
		LogUtil.endLog(PlanDefine.THIS_CLASS, PlanDefine.PLAN_LIST_ACTION);
		return info;
	}

	/**
	 * 查询汇添金计划详情
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = PlanDefine.PLAN_DETAIL_ACTION, produces = "application/json; charset=utf-8")
	public JSONObject searchPlanDetail(@ModelAttribute PlanBean form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(PlanDefine.THIS_CLASS, PlanDefine.PLAN_DETAIL_ACTION);
		JSONObject info = new JSONObject();
		// 获取计划编号
		String planNid = form.getPlanNid();
		if (StringUtils.isEmpty(planNid)) {
			info.put("error", "1");
			info.put("errorDesc", "计划编号不可为空");
			return info;
		}
		if (!this.checkSign(form, BaseDefine.METHOD_SEARCH_PLAN_DETAIL)) {
			info.put("error", BaseResultBean.STATUS_FAIL);
			info.put("", "验签失败！");
			LogUtil.errorLog(this.getClass().getName(), "searchPlanDetail", "验签失败！", null);
			return info;
		}
		info.put("error", "0");
		// 2.根据项目标号获取相应的计划信息
		WeChatDebtPlanDetailCustomize planDetail = this.planService.selectDebtPlanDetail(planNid);
		if (Validator.isNotNull(planDetail)) {
			// 计划详情头
			info.put("planDetail", planDetail);
			// 获取计划介绍
			WeChatDebtPlanIntroduceCustomize planIntroduce = this.planService.selectDebtPlanIntroduce(planNid);
			if (Validator.isNotNull(planIntroduce)) {
				// 计划概念html转text
				planIntroduce.setPlanConcept(HtmlUtil.getTextFromHtml(planIntroduce.getPlanConcept()));
				// 计划原理html转text
				planIntroduce.setPlanTheory(HtmlUtil.getTextFromHtml(planIntroduce.getPlanTheory()));
				// 计划介绍
				info.put("planIntroduce", planIntroduce);
			}
			// 获取计划安全保障
			WeChatDebtPlanRiskControlCustomize planRiskControl = this.planService.selectDebtPlanRiskControl(planNid);
			// 计划常见问题
			WeChatDebtPlanQuestionCustomize planQuestion = this.planService.selectDebtPlanQuestion(planNid);

			if (Validator.isNotNull(planRiskControl)) {
				if (Validator.isNotNull(planQuestion)) {
					// 常见问题
					planRiskControl.setQuestion(planQuestion.getQuestion());
				}
				// 计划安全保障
				info.put("planRiskControl", planRiskControl);
			}
		}
		return info;
	}

	/**
	 * 查询相应的汇添金计划的加入记录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = PlanDefine.PLAN_ACCEDE_ACTION, produces = "application/json; charset=utf-8")
	public JSONObject searchPlanAccede(@ModelAttribute PlanBean form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(PlanDefine.THIS_CLASS, PlanDefine.PLAN_ACCEDE_ACTION);
		JSONObject info = new JSONObject();
		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		// 计划编号
		String planNid = form.getPlanNid();
		if (StringUtils.isEmpty(planNid)) {
			info.put("error", "1");
			info.put("errorDesc", "计划编号不可为空");
			return info;
		}
		if (!this.checkSign(form, BaseDefine.METHOD_SEARCH_PLAN_ACCEDE)) {
			info.put("error", BaseResultBean.STATUS_FAIL);
			info.put("", "验签失败！");
			LogUtil.errorLog(this.getClass().getName(), "searchPlanAccede", "验签失败！", null);
			return info;
		}
		// 分页信息
		Integer page = form.getPage();
		Integer pageSize = form.getPageSize();
		DebtPlan debtPlan = this.planService.selectDebtPlanByNid(planNid);
		if (Validator.isNotNull(debtPlan)) {
			// 加入金额
			info.put("accedeTotal", df.format(debtPlan.getDebtPlanMoneyYes()));
			// 加入次数
			info.put("accedeTimes", String.valueOf(debtPlan.getAccedeTimes()));
		} else {
			info.put("accedeTotal", df.format(new BigDecimal("0")));
			info.put("accedeTimes", "0");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("planNid", planNid);
		int totalSize = 0;
		try {
			params.put("limitStart", pageSize * (page - 1));
			params.put("limitEnd", pageSize);
			// 统计相应的汇直投的数据记录数
			totalSize = this.planService.countPlanAccedeRecordTotal(params);
			List<WeChatDebtPlanAccedeCustomize> recordList = planService.selectPlanAccedeList(params);
			// 加入记录
			info.put("accedeList", recordList);
			info.put("totalSize", totalSize);
		} catch (Exception e) {
			info.put("errorDesc", "获取列表异常");
			info.put("error", "1");
			return info;
		}
		info.put("error", "0");
		LogUtil.endLog(PlanDefine.THIS_CLASS, PlanDefine.PLAN_ACCEDE_ACTION);
		return info;
	}

	/**
	 * 查询相应的汇添金债权列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = PlanDefine.PLAN_BORROW_ACTION, produces = "application/json; charset=utf-8")
	public JSONObject searchPlanBorrow(@ModelAttribute PlanBean form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(PlanDefine.THIS_CLASS, PlanDefine.PLAN_ACCEDE_ACTION);
		JSONObject info = new JSONObject();
		// 先直投标 再转让， 最大五十条， 利率》=计划预期利率 ，根据利率排序
		// 获取计划编号
		String planNid = form.getPlanNid();
		if (StringUtils.isEmpty(planNid)) {
			info.put("error", "1");
			info.put("errorDesc", "计划编号不可为空");
			return info;
		}
		if (!this.checkSign(form, BaseDefine.METHOD_SEARCH_PLAN_BORROW)) {
			info.put("error", BaseResultBean.STATUS_FAIL);
			info.put("", "验签失败！");
			LogUtil.errorLog(this.getClass().getName(), "searchPlanBorrow", "验签失败！", null);
			return info;
		}
		// 分页信息
		Integer page = form.getPage();
		Integer pageSize = form.getPageSize();
		Map<String, Object> params = new HashMap<String, Object>();
		Integer TopCount = 50;// 最大五十条
		params.put("planNid", planNid);
		DebtPlan debtPlan = planService.getPlanByNid(planNid);
		int recordTotal = this.planService.countPlanBorrowRecordTotal(params);
		if (recordTotal >= TopCount) {
			// 只有专属标，不考虑上诉条件 只是利率倒叙
			if (recordTotal > 0) {
				// 查询相应的汇直投列表数据
				params.put("limitStart", pageSize * (page - 1));
				params.put("limitEnd", pageSize);
				List<WeChatDebtPlanBorrowCustomize> recordList = planService.selectPlanBorrowList(params);
				info.put("planBorrowList", recordList);
				info.put("totalSize", recordTotal);
				info.put("error", "0");// 成功标识位
			} else {
				info.put("planBorrowList", "");
				info.put("totalSize", 0);
				info.put("error", "0");// 成功标识位
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
				params.put("limitStart", pageSize * (page - 1));
				params.put("limitEnd", pageSize);
				List<WeChatDebtPlanBorrowCustomize> recordList = planService.selectPlanBorrowListCredit(params);
				info.put("planBorrowList", recordList);
				info.put("totalSize", recordTotal);
				info.put("error", "0");// 成功标识位
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
						params.put("limitStart", pageSize * (page - 1));
						params.put("limitEnd", pageSize);
						List<WeChatDebtPlanBorrowCustomize> recordList = planService.selectPlanBorrowListLast(params);
						info.put("planBorrowList", recordList);
						info.put("totalSize", recordTotal);
						info.put("error", "0");// 成功标识位
					} else {
						info.put("planBorrowList", "");
						info.put("totalSize", 0);
						info.put("error", "0");// 成功标识位
					}
				} else {
					info.put("planBorrowList", "");
					info.put("totalSize", 0);
					info.put("error", "0");// 成功标识位
				}
			}
		}
		LogUtil.endLog(PlanDefine.THIS_CLASS, PlanDefine.PLAN_ACCEDE_ACTION);
		return info;
	}

	/**
	 * 
	 * 跳转出借页面（获取出借信息）
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = PlanDefine.INVEST_INFO_ACTION, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public JSONObject getPlanInvestInfo(@ModelAttribute PlanBean form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(PlanDefine.THIS_CLASS, PlanDefine.INVEST_INFO_ACTION);
		JSONObject info = new JSONObject();

		// 计划编号
		String planNid = form.getPlanNid();
		// 用户id
		Integer userId = form.getUserId();
		if (StringUtils.isEmpty(planNid)) {
			info.put("error", "1");
			info.put("errorDesc", "计划编号不可为空");
			return info;
		}
		if (userId == null || userId == 0) {
			info.put("error", "1");
			info.put("errorDesc", "您未登陆，请先登录");
			return info;
		}
		info = this.planService.checkUser(userId);
		if (!info.get("error").equals("0")) {
			info.put("errorDesc", info.getString("data"));
			return info;
		}
		if (!this.checkSign(form, BaseDefine.METHOD_GET_PLAN_INVEST_INFO)) {
			info.put("error", BaseResultBean.STATUS_FAIL);
			info.put("", "验签失败！");
			LogUtil.errorLog(this.getClass().getName(), "getPlanInvestInfo", "验签失败！", null);
			return info;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("planNid", planNid);
		params.put("userId", userId);
		WeChatDebtPlanInvestInfoCustomize planInfo = this.planService.selectPlanInvestInfo(params);
		if (Validator.isNull(planInfo)) {
			info.put("error", "1");
			info.put("errorDesc", "查询计划不存在");
			return info;
		}
		info.put("balance", planInfo.getBalance());
		info.put("planAccountWait", planInfo.getPlanAccountWait());
		info.put("debtMinInvestment", planInfo.getDebtMinInvestment());
		info.put("debtInvestmentIncrement", planInfo.getDebtInvestmentIncrement());
		return info;
	}

	/**
	 * 
	 * 计算预期收益
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = PlanDefine.INVEST_EARNINGS_ACTION, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public JSONObject getPlanInvestEarnings(@ModelAttribute PlanBean form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(PlanDefine.THIS_CLASS, PlanDefine.INVEST_EARNINGS_ACTION);
		JSONObject info = new JSONObject();
		// 计划编号
		String planNid = form.getPlanNid();
		// 传递金额
		String account = form.getAccount();
		if (StringUtils.isEmpty(planNid)) {
			info.put("error", "1");
			info.put("errorDesc", "计划编号不可为空");
			return info;
		}
		if (StringUtils.isEmpty(account)) {
			info.put("error", "1");
			info.put("errorDesc", "出借金额不可为空");
			return info;
		}
		if (!this.checkSign(form, BaseDefine.METHOD_GET_PLAN_INVEST_EARNINGS)) {
			info.put("error", BaseResultBean.STATUS_FAIL);
			info.put("", "验签失败！");
			LogUtil.errorLog(this.getClass().getName(), "getPlanInvestEarnings", "验签失败！", null);
			return info;
		}
		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		df.setRoundingMode(RoundingMode.FLOOR);
		// 查询项目信息
		String money = account;
		DebtPlan plan = planService.getPlanByNid(planNid);
		if (plan == null) {
			info.put("error", "1");
			info.put("errorDesc", "获取计划信息失败");
			return info;
		}
		// 如果出借金额不为空
		if (!StringUtils.isBlank(money) && Long.parseLong(money) > 0) {
			// 收益率
			BigDecimal borrowApr = plan.getExpectApr();
			// 周期
			Integer borrowPeriod = plan.getDebtLockPeriod();
			BigDecimal earnings = new BigDecimal("0");
			// 还款方式为”按月计息，到期还本还息“：预期收益=出借金额*年化收益÷12*月数；
			// 计算预期收益
			earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(money), borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
			info.put("earnings", df.format(earnings));
			info.put("error", "0");// 成功标识位
		} else {
			info.put("error", "1");
			info.put("errorDesc", "请填写正确的金额");
			return info;
		}
		return info;
	}

	/**
	 * 出借二次确认
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = PlanDefine.PLAN_CHECK_ACTION, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public JSONObject appointCheck(@ModelAttribute PlanBean form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(PlanController.class.toString(), PlanDefine.PLAN_CHECK_ACTION);
		JSONObject info = new JSONObject();
		// 项目编号
		String planNid = form.getPlanNid();
		// 出借金额
		String money = form.getAccount();

		Integer userId = form.getUserId();

		if (userId == null || userId == 0) {
			info.put("error", "1");
			info.put("errorDesc", "用户id不可为空");
			return info;
		}
		if (!this.checkSign(form, BaseDefine.METHOD_APPOINT_CHECK)) {
			info.put("error", BaseResultBean.STATUS_FAIL);
			info.put("", "验签失败！");
			LogUtil.errorLog(this.getClass().getName(), "appointCheck", "验签失败！", null);
			return info;
		}
		// 校验
		info = planService.checkParamPlan(planNid, money, userId);
		if (info.get("error").equals("0")) {
			DebtPlan plan = planService.getPlanByNid(planNid);
			// 收益率
			BigDecimal borrowApr = plan.getExpectApr();
			// 周期
			Integer borrowPeriod = plan.getDebtLockPeriod();
			BigDecimal earnings = new BigDecimal("0");
			// 还款方式为”按月计息，到期还本还息“：预期收益=出借金额*年化收益÷12*月数；
			// 计算预期收益
			earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(money), borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
			info.put("earnings", earnings);
			info.put("account", money);
			info.put("error", "0");// 成功标识位
		} else {
			info.put("error", "1");
			info.put("errorDesc", info.getString("data"));
		}
		LogUtil.endLog(PlanController.class.toString(), PlanDefine.PLAN_CHECK_ACTION);
		return info;
	}

	/**
	 * 加入计划
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = PlanDefine.PLAN_INVEST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public JSONObject joinPlan(@ModelAttribute PlanBean form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(PlanController.class.toString(), PlanDefine.PLAN_INVEST_ACTION);
		JSONObject info = new JSONObject();
		// 项目编号
		String planNid = form.getPlanNid();
		// 出借金额
		String account = form.getAccount();
		// 用户id
		Integer userId = form.getUserId();

		if (!this.checkSign(form, BaseDefine.METHOD_JOIN_PLAN)) {
			info.put("error", BaseResultBean.STATUS_FAIL);
			info.put("", "验签失败！");
			LogUtil.errorLog(this.getClass().getName(), "joinPlan", "验签失败！", null);
			return info;
		}
		JSONObject result = planService.checkParamPlan(planNid, account, userId);
		// 返回状态 status 0成功1失败
		if (result == null) {
			result = new JSONObject();
			result.put("error", "1");
			result.put("errorDesc", "出借失败！");
			return result;
		} else if (result.get("error") != null && result.get("error").equals("1")) {
			result.put("error", "1");
			result.put("errorDesc", result.get("data") + "");
			return result;
		}
		// 校验成功
		info.put("error", "0");
		// 项目编号
		info.put("planNid", planNid);
		BigDecimal decimalAccount = StringUtils.isNotEmpty(account) ? new BigDecimal(account) : BigDecimal.ZERO;
		// 汇付客户号
		String tenderUsrcustid = result.getString("tenderUsrcustid");
		// 生成冻结订单
		String frzzeOrderId = GetOrderIdUtils.getOrderId0(Integer.valueOf(userId));
		String frzzeOrderDate = GetOrderIdUtils.getOrderDate();
		// TODO 冻结 加入 相应金额 明细
		// 写日志
		Boolean flag = planService.updateBeforeChinaPnR(planNid, frzzeOrderId, userId, account, tenderUsrcustid, frzzeOrderDate);
		if (!flag) {
			result.put("error", "1");
			result.put("errorDesc", "出借失败！");
			return result;
		}
		Jedis jedis = pool.getResource();
		String balance = RedisUtils.get(planNid);
		if (StringUtils.isBlank(balance)) {
			result.put("error", "1");
			result.put("errorDesc", "您来晚了，下次再来抢吧");
			return result;
		}
		// 操作redis
		while ("OK".equals(jedis.watch(planNid))) {
			balance = RedisUtils.get(planNid);
			if (StringUtils.isBlank(balance)) {
				result.put("error", "1");
				result.put("errorDesc", "您来晚了，下次再来抢吧");
				return result;
			}
			System.out.println("微信用户:" + userId + "***********************************加入计划冻结前可投金额：" + balance);
			if (new BigDecimal(balance).compareTo(BigDecimal.ZERO) == 0) {
				result.put("error", "1");
				result.put("errorDesc", "您来晚了，下次再来抢吧");
				return result;
			}
			if (new BigDecimal(balance).compareTo(decimalAccount) < 0) {
				result.put("error", "1");
				result.put("errorDesc", "可加入剩余金额为" + balance + "元");
				return result;
			}
			Transaction tx = jedis.multi();
			BigDecimal lastAccount = new BigDecimal(balance).subtract(decimalAccount);
			// 写队列
			tx.set(planNid, lastAccount + "");
			List<Object> result1 = tx.exec();
			if (result1 == null || result1.isEmpty()) {
				jedis.unwatch();
				result.put("error", "1");
				result.put("errorDesc", "可加入剩余金额为" + balance + "元");
				return result;
			} else {
				System.out.println("微信用户:" + userId + "***********************************加入前减redis：" + decimalAccount);
				break;
			}
		}
		// 调用冻结接口开始冻结
		FreezeResult freezeResult = planService.freeze(userId, account, tenderUsrcustid, frzzeOrderId, frzzeOrderDate);
		// 冻结标识
		boolean freezeFlag = false;
		String freezeTrxId = null;
		if (freezeResult == null) {
			result.put("error", "1");
			result.put("errorDesc", "加入计划冻结失败！请联系客服。");
			return result;
		}
		freezeTrxId = freezeResult.getFreezeTrxId();
		freezeFlag = freezeResult.isFreezeFlag();

		if (!freezeFlag) {
			System.out.println("微信用户加入计划:" + userId + "***********************************冻结失败额度：" + account);
			// 恢复redis
			planService.recoverRedis(planNid, userId, account);
			LogUtil.errorLog(PlanController.class.getName(), "plan", "freeze error freezeDefine is null", null);
			result.put("error", "1");
			result.put("errorDesc", "加入计划冻结失败！请联系客服。");
			return result;
		}
		boolean afterDealFlag = false;
		// 写入加入计划表
		try {
			// 生成加入订单
			String planOrderId = GetOrderIdUtils.getOrderId0(Integer.valueOf(userId));
			afterDealFlag = planService.updateAfterPlanRedis(planNid, frzzeOrderId, userId, account, tenderUsrcustid, 1, GetCilentIP.getIpAddr(request), freezeTrxId, frzzeOrderDate, planOrderId);

			if (afterDealFlag) {
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
					// 计算预期收益
					earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(account), planApr.divide(new BigDecimal("100")), planPeriod);
					interest = df.format(earnings);
				}
				if (StringUtils.isNotBlank(interest)) {
					info.put("interest", interest);
				}
				info.put("account", df.format(new BigDecimal(account)));
				info.put("errorDesc", "恭喜您加入计划成功!");
				LogUtil.endLog(PlanController.class.toString(), PlanDefine.PLAN_INVEST_ACTION);
			} else {
				System.out.println("微信用户:" + userId + "***********************************预约成功后处理失败：" + account);
				// 恢复redis
				planService.recoverRedis(planNid, userId, account);
				result.put("error", "1");
				result.put("errorDesc", "系统异常");
				return result;
			}
		} catch (Exception e) {
			// 恢复redis
			planService.recoverRedis(planNid, userId, account);
			result.put("error", "1");
			result.put("errorDesc", "系统异常");
			System.out.println("微信用户:" + userId + "***********************************加入计划成功后处理失败：" + account);
			LogUtil.errorLog(PlanController.class.getName(), PlanDefine.PLAN_INVEST_ACTION, e);
			return result;
		}
		LogUtil.endLog(PlanController.class.toString(), PlanDefine.PLAN_INVEST_ACTION);
		return info;
	}

	/**
	 * @method: appointmentCheck
	 * @description: 预约授权检验
	 * @param request
	 * @return
	 * @mender: zhouxiaoshuai
	 * @date: 2016年7月26日 下午1:28:11
	 */
	@ResponseBody
	@RequestMapping(value = PlanDefine.APPOINTMENT_CHECK_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public JSONObject appointmentCheck(@ModelAttribute PlanBean form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(PlanController.class.toString(), PlanDefine.APPOINTMENT_CHECK_ACTION);
		JSONObject info = new JSONObject();
		// 用户id
		Integer userId = form.getUserId();
		// 授权参数(1授权 0取消授权)
		String appointment = form.getAppointment();
		if (userId == null || userId == 0) {
			info.put("error", "1");
			info.put("errorDesc", "用户id不可为空");
			return info;
		}
		if (StringUtils.isEmpty(appointment)) {
			info.put("error", "1");
			info.put("errorDesc", "请求参数错误");
			return info;
		}

		if (!appointment.equals("1") && !appointment.equals("0")) {
			info.put("error", "1");
			info.put("errorDesc", "请求参数错误");
			return info;
		}
		if (!this.checkSign(form, BaseDefine.METHOD_CHECK_APPOINTMENT)) {
			info.put("error", BaseResultBean.STATUS_FAIL);
			info.put("", "验签失败！");
			LogUtil.errorLog(this.getClass().getName(), "appointment", "验签失败！", null);
			return info;
		}
		// 获取用户信息
		AccountChinapnr accountChinapnrTender = planService.getAccountChinapnr(userId);
		// 用户未在平台开户
		if (accountChinapnrTender == null) {
			info.put("error", "1");
			info.put("errorDesc", "用户开户信息不存在");
			return info;
		}
		// 判断借款人开户信息是否存在
		if (accountChinapnrTender.getChinapnrUsrcustid() == null) {
			info.put("error", "1");
			info.put("errorDesc", "用户汇付客户号不存在");
			return info;
		}
		// 预约接口查询
		Map<String, Object> appointmentMap = planService.checkAppointmentStatus(userId, appointment);
		boolean appointmentFlag = (boolean) appointmentMap.get("appointmentFlag");
		String isError = appointmentMap.get("isError") + "";
		if (!appointmentFlag) {
			if (isError.equals("2")) {
				// 用户还有汇添金计划
				info.put("error", "3");
				info.put("errorDesc", "您当前有申购中/锁定中的汇添金计划，暂时不能取消授权");
				return info;
			}
		}
		info.put("error", "0");
		info.put("errorDesc", "校验通过");
		return info;
	}

	/**
	 * @method: appointment
	 * @description: 预约授权
	 * @param request
	 * @return
	 * @mender: zhouxiaoshuai
	 * @date: 2016年7月26日 下午1:28:11
	 */
	@RequestMapping(PlanDefine.APPOINTMENT_ACTION)
	public ModelAndView appointment(@ModelAttribute PlanBean form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(PlanController.class.toString(), PlanDefine.APPOINTMENT_ACTION);
		// 用户id
		Integer userId = form.getUserId();
		// 授权参数(1授权 0取消授权)
		String appointment = form.getAppointment();
		// 返回PHP路径
		String callback = form.getCallback();
		// 返回错误页面
		ModelAndView info = new ModelAndView();

		String error = "";
		String errorDesc = "";
		if (userId == null || userId == 0) {
			error = "1";
			errorDesc = "用户id不可为空";
			info = new ModelAndView(returnUrl(error, errorDesc, appointment, callback));
			return info;
		}
		if (StringUtils.isEmpty(appointment)) {
			error = "1";
			errorDesc = "请求参数错误";
			info = new ModelAndView(returnUrl(error, errorDesc, appointment, callback));
			return info;
		}
		if (!appointment.equals("1") && !appointment.equals("0")) {
			error = "1";
			errorDesc = "请求参数错误";
			info = new ModelAndView(returnUrl(error, errorDesc, appointment, callback));
			return info;
		}
		if (!this.checkSign(form, BaseDefine.METHOD_APPOINTMENT)) {
			error = "1";
			errorDesc = "验签失败！";
			info = new ModelAndView(returnUrl(error, errorDesc, appointment, callback));
			LogUtil.errorLog(this.getClass().getName(), "appointment", "验签失败！", null);
			return info;
		}
		// 获取用户信息
		AccountChinapnr accountChinapnrTender = planService.getAccountChinapnr(userId);
		// 用户未在平台开户
		if (accountChinapnrTender == null) {
			error = "1";
			errorDesc = "用户开户信息不存在";
			info = new ModelAndView(returnUrl(error, errorDesc, appointment, callback));
			return info;
		}
		// 判断借款人开户信息是否存在
		if (accountChinapnrTender.getChinapnrUsrcustid() == null) {
			error = "1";
			errorDesc = "用户汇付客户号不存在";
			info = new ModelAndView(returnUrl(error, errorDesc, appointment, callback));
			return info;
		}
		// 预约接口查询
		Map<String, Object> appointmentMap = planService.checkAppointmentStatus(userId, appointment);
		boolean appointmentFlag = (boolean) appointmentMap.get("appointmentFlag");
		String isError = appointmentMap.get("isError") + "";
		if (appointmentFlag) {
			// 回调路径
			String returl = HOST + PlanDefine.REQUEST_MAPPING + PlanDefine.RETURL_SYN_ACTION + ".do?userId=" + userId + "&appointment=" + appointment + "&callback=" + callback;

			Long tenderUsrcustid = accountChinapnrTender.getChinapnrUsrcustid();
			ChinapnrBean chinapnrBean = new ChinapnrBean();
			// 接口版本号
			chinapnrBean.setVersion(ChinaPnrConstant.VERSION_10);// 2.0
			if (appointment.equals("1")) {
				// 消息类型(自动投标计划)
				chinapnrBean.setCmdId(ChinaPnrConstant.CMDID_AUTO_TENDER_PLAN);
				chinapnrBean.setTenderPlanType("W");// p部分授权 w完全授权
			} else {
				// 消息类型(自动投标计划关闭)
				chinapnrBean.setCmdId(ChinaPnrConstant.CMDID_AUTO_TENDER_PLAN_CLOSE);
			}
			// 商户客户号
			chinapnrBean.setMerCustId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID));
			// 用户客户号
			chinapnrBean.setUsrCustId(tenderUsrcustid + "");
			chinapnrBean.setRetUrl(returl); // 页面返回
			try {
				info = ChinapnrUtil.callApi(chinapnrBean);// 跳转汇付页面
				return info;
			} catch (Exception e) {
				error = "1";
				errorDesc = "自动出借授权操作失败";
				info = new ModelAndView(returnUrl(error, errorDesc, appointment, callback));
				return info;
			}
		} else {
			Users user = planService.getUsers(userId);
			Integer AuthStatus = 0;
			String AuthType = "W";
			if (user.getAuthType() != null && user.getAuthType() == 1) {
				AuthType = "P";
			}
			if (user.getAuthStatus() != null) {
				AuthStatus = user.getAuthStatus();
			}
			if (isError.equals("0")) {
				if (appointment.equals("1") && AuthStatus == 0) {
					// 开启授权操作
					planService.updateUserAuthStatus(AuthType, appointment, userId + "");
					error = "0";
					errorDesc = "恭喜你自动出借授权成功";
				} else if (appointment.equals("1") && AuthStatus == 1) {
					// 已经开启授权
					error = "0";
					errorDesc = "恭喜你自动出借授权成功";
				} else if (appointment.equals("0") && AuthStatus == 1) {
					// 关闭授权操作
					planService.updateUserAuthStatus(AuthType, appointment, userId + "");
					error = "0";
					errorDesc = "您的自动出借授权已取消";
				} else if (appointment.equals("0") && AuthStatus == 0) {
					// 已经授权
					error = "0";
					errorDesc = "您的自动出借授权已取消";
				}
			} else if (isError.equals("2")) {
				// 用户还有汇添金计划
				error = "3";
				errorDesc = "您当前有申购中/锁定中的汇添金计划，暂时不能取消授权";
			} else {
				if (appointment.equals("1")) {
					info.addObject("error", "1");
					info.addObject("errorDesc", "自动出借授权失败");
				} else {
					info.addObject("error", "1");
					info.addObject("errorDesc", "关闭自动出借授权失败");
				}
			}
			info = new ModelAndView(returnUrl(error, errorDesc, appointment, callback));
		}
		return info;
	}

	/**
	 * 
	 * @method: appointmentRetUrl
	 * @description: 预约授权同步回调
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 * @return: ModelAndView
	 * @mender: zhouxiaoshuai
	 * @date: 2016年7月26日 下午1:35:42
	 */
	// pay模块回调此方法
	@RequestMapping(PlanDefine.RETURL_SYN_ACTION)
	public String appointmentRetUrl(@ModelAttribute PlanBean form, HttpServletRequest request, HttpServletResponse response, @ModelAttribute ChinapnrBean bean) {

		String error = "0"; // 成功标识
		String errorDesc = "";// 错误信息
		String userId = String.valueOf(form.getUserId());
		String appointment = form.getAppointment();
		String callback = form.getCallback();

		// 判断汇付返回参数
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(appointment) || StringUtils.isEmpty(callback)) {
			error = "1";
			errorDesc = "授权失败，回调参数为空";
			return returnUrl(error, errorDesc, appointment, callback);
		}
		if (!appointment.equals("1") && !appointment.equals("0")) {
			error = "1";
			errorDesc = "授权参数错误!";
			return returnUrl(error, errorDesc, appointment, callback);
		}

		bean.convert();
		// 接口返回码
		String respCode = bean.getRespCode();
		// 返回成功
		if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE)) || ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
			// 授权
			boolean authFlag = planService.updateUserAuthStatus(bean.getTenderPlanType(), appointment, userId);
			if (authFlag) {
				if (appointment.equals("1")) {
					errorDesc = "恭喜你自动出借授权成功!";
				} else {
					errorDesc = "您的自动出借授权已取消!";
				}
			} else {
				if (appointment.equals("1")) {
					error = "1";
					errorDesc = "自动出借授权失败!";
				} else {
					error = "1";
					errorDesc = "关闭自动出借授权失败!";
				}
			}
			return returnUrl(error, errorDesc, appointment, callback);
		} else {
			error = "1";
			errorDesc = "授权失败，接口返回码：" + respCode;
			return returnUrl(error, errorDesc, appointment, callback);
		}
	}

	/**
	 * 拼接返回结果
	 * 
	 * @param error
	 * @param errorDesc
	 * @param callback
	 * @param appointment
	 * @return
	 */
	public String returnUrl(String error, String errorDesc, String appointment, String callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("error", error);
		map.put("errorDesc", errorDesc);
		map.put("appointment", appointment);
		String data = JSON.toJSONString(map);
		try {
			data = URLEncoder.encode(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String url = callback + "backinfo/" + data;
		return "redirect:" + url;
	}

}
