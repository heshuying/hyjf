package com.hyjf.web.hjhdetail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.bank.service.evalation.EvaluationService;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.AccountDetailCustomize;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.web.bank.web.user.tender.TenderController;
import com.hyjf.web.bank.web.user.tender.TenderDefine;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hyjf.bank.service.borrow.BorrowFileCustomBean;
import com.hyjf.bank.service.borrow.BorrowRepayPlanCustomBean;
import com.hyjf.bank.service.borrow.BorrowService;
import com.hyjf.bank.service.evalation.EvaluationService;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.bank.service.user.auth.AuthService;
import com.hyjf.bank.service.user.tender.TenderService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.enums.utils.ProtocolEnum;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.mybatis.model.customize.web.*;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanAccedeCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanDetailCustomize;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.WebBaseAjaxResultBean;
import com.hyjf.web.agreement.AgreementService;
import com.hyjf.web.bank.web.borrow.*;
import com.hyjf.web.bank.web.user.tender.TenderController;
import com.hyjf.web.bank.web.user.tender.TenderDefine;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.plan.*;
import com.hyjf.web.user.invest.InvestInfoAjaxBean;
import com.hyjf.web.user.invest.InvestServiceImpl;
import com.hyjf.web.util.WebUtils;
import com.hyjf.web.vip.apply.ApplyDefine;
import com.hyjf.web.vip.manage.VIPManageDefine;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;


@Controller
@RequestMapping(value = HjhDetailDefine.REQUEST_MAPPING)
public class HjhDetailController extends BaseController {
	Logger _log = LoggerFactory.getLogger(HjhDetailController.class);

	@Autowired
	private HjhDetailService hjhDetailService;
	@Autowired
	private PlanService planService;
	@Autowired
	private BorrowService borrowService;
	@Autowired
	private AuthService authService;
	@Autowired
	private TenderService tenderService;
	@Autowired
	private EvaluationService evaluationService;
	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private AgreementService agreementService;

	/** Jedis  */
	public static JedisPool pool = RedisUtils.getPool();
	/** 发布地址 */
	private static String HOST_URL = PropUtils.getSystem("hyjf.web.host").trim();

	/**
	 * 查询相应的项目详情
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = HjhDetailDefine.PLAN_DETAIL_ACTION)
	public ModelAndView searchPlanDetail(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(HjhDetailDefine.THIS_CLASS, HjhDetailDefine.PLAN_DETAIL_ACTION);
		ModelAndView modelAndView = new ModelAndView(HjhDetailDefine.PLAN_DETAIL_PTAH);

		// 获取计划编号(列表画面传递-计划编号)
		String planNid = request.getParameter("planNid");
		// 获取优惠券编号(???)
		String couponId = request.getParameter("couponGrantId");
		// 获取相应的登陆用户
		WebViewUser loginUser = WebUtils.getUser(request);
		// 阀值
		Integer threshold = 1000 ;
		modelAndView.addObject("threshold", threshold);
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

		//加入总人数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("planNid", planNid);
		int joinPeopleNum = this.hjhDetailService.countPlanAccedeRecordTotal(params);
		modelAndView.addObject("joinPeopleNum", String.valueOf(joinPeopleNum));

		// 根据项目标号获取相应的计划信息
		DebtPlanDetailCustomize planDetail = hjhDetailService.selectDebtPlanDetail(planNid);

		// 线上异常处理 如果为空的话直接返回
		if(planDetail==null){
			logger.info("未查询到对应的计划，计划编号为:"+planNid);
			modelAndView = new ModelAndView("/error/404");
			return modelAndView;

		}
		// 最小出借金额(起投金额)-->计算最后一笔出借
		if (planDetail.getDebtMinInvestment() != null) {
			modelAndView.addObject("debtMinInvestment", new BigDecimal(planDetail.getDebtMinInvestment()).intValue());
		}
		// 开放额度剩余金额(取小数两位)
		if (planDetail.getAvailableInvestAccount() != null) {

			// 开放额度< 0 显示 0.00
			if(new BigDecimal(planDetail.getAvailableInvestAccount()).compareTo(BigDecimal.ZERO) == -1){
				// 画面有地方用 planDetail.availableInvestAccount
				planDetail.setAvailableInvestAccount("0.00");
				// 画面也有地方有 availableInvestAccount
				modelAndView.addObject("availableInvestAccount",new BigDecimal(0.00));
			} else {
				modelAndView.addObject("availableInvestAccount", new BigDecimal(planDetail.getAvailableInvestAccount()));
			}
		}
		if (Validator.isNotNull(planDetail)) {
			//系统当前时间戳
			modelAndView.addObject("nowTime", GetDate.getNowTime10());

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
			modelAndView.addObject("interest", BigDecimal.ZERO);
			if (couponConfig != null) {
				modelAndView.addObject("isThereCoupon", 1);

				couponInterest = planService.getCouponInterest(couponConfig.getUserCouponId(), planNid, "0");
				couponConfig.setCouponInterest(df.format(couponInterest));
				if(couponConfig!=null && couponConfig.getCouponType()==3){
					modelAndView.addObject("interest", df.format(couponInterest.subtract(couponConfig.getCouponQuota())));
				}else{
					modelAndView.addObject("interest", df.format(couponInterest));
				}

			} else {
				modelAndView.addObject("isThereCoupon", 0);
			}

			modelAndView.addObject("couponConfig", couponConfig);
			/** 计算最优优惠券结束 */
			/** 汇添金优惠券使用结束 pcc */

			// 计划详情头部(结束)
			modelAndView.addObject("planDetail", planDetail);
			// 获取计划介绍
			String planIntroduce = planDetail.getPlanConcept();
			if (Validator.isNotNull(planIntroduce)) {
				modelAndView.addObject("planIntroduce", planIntroduce);
			}
			// 获取计划原理
			String planPrinciple = planDetail.getPlanPrinciple();
			if (Validator.isNotNull(planPrinciple)) {
				modelAndView.addObject("planPrinciple", planPrinciple);

			}
			// 获取风控保障措施
			String safeguardMeasures = planDetail.getSafeguardMeasures();
			if (Validator.isNotNull(safeguardMeasures)) {
				modelAndView.addObject("safeguardMeasures", safeguardMeasures);

			}
			// 获取风险保证金措施
			String marginMeasures = planDetail.getMarginMeasures();
			if (Validator.isNotNull(marginMeasures)) {
				modelAndView.addObject("marginMeasures", marginMeasures);

			}
			// 获取常见问题
			String normalQuestions = planDetail.getNormalQuestions();
			if (Validator.isNotNull(normalQuestions)) {
				modelAndView.addObject("normalQuestions", normalQuestions);

			}
			// 获取各种标志位
			String investFlag = "0";
			if (Validator.isNotNull(loginUser)) {
				Integer userId = loginUser.getUserId();
				// 用户是否加入过项目
				int count = this.hjhDetailService.countUserAccede(planNid, userId);
				if (count > 0) {
					investFlag = "1";
				}else{
					investFlag = "0";//是否出借过该项目 0未出借 1已出借
				}
				modelAndView.addObject("investFlag", investFlag);
				// 用户是否开户
				if (loginUser.isBankOpenAccount()) {
					modelAndView.addObject("openFlag", 1);
				} else {
					modelAndView.addObject("openFlag", 0);
				}
				// 用户是否设置交易密码
				Users user=hjhDetailService.getUsers(loginUser.getUserId());
				if(user.getIsSetPassword() == 1){
					modelAndView.addObject("setPwdFlag", "1");
				}else{
					modelAndView.addObject("setPwdFlag", "0");
				}
				// 用户是否被禁用：0 未禁用 1禁用
				if(user.getStatus() == 1){
					modelAndView.addObject("forbiddenFlag", "1");
				}else{
					modelAndView.addObject("forbiddenFlag", "0");
				}
				// 用户是否完成风险测评标识
				//JSONObject jsonObject = CommonSoaUtils.getUserEvalationResultByUserId(userId + "");
//	            modelAndView.addObject("riskFlag", loginUser.getIsEvaluationFlag());
				try {
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

				modelAndView.addObject("loginFlag", 1);
				// 用户角色
				modelAndView.addObject("roleId", loginUser.getRoleId());

				// 获取用户账户余额
				Account account = this.planService.getAccount(userId);
				if (Validator.isNotNull(account)) {
					String userBalance = account.getBankBalance().toString();
					modelAndView.addObject("userBalance", userBalance);
				}
				// 用户是否完成自动授权标识
				HjhUserAuth hjhUserAuth = this.authService.getHjhUserAuthByUserId(userId);
				if (Validator.isNotNull(hjhUserAuth)) {
					String autoInvesFlag = hjhUserAuth.getAutoInvesStatus().toString();
					modelAndView.addObject("autoInvesFlag", autoInvesFlag);
				} else {
					modelAndView.addObject("autoInvesFlag", "0");//自动投标授权状态 0: 未授权    1:已授权
				}
				// 合规三期
				// 是否开启服务费授权 0未开启  1已开启
				modelAndView.addObject("paymentAuthStatus", hjhUserAuth==null?"":hjhUserAuth.getAutoPaymentStatus());
				modelAndView.addObject("paymentAuthOn", CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus());
				modelAndView.addObject("isCheckUserRole",PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN));

			} else {
				//状态位用于判断tab的是否可见
				modelAndView.addObject("paymentAuthStatus", "0");
				modelAndView.addObject("paymentAuthOn", CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus());
				modelAndView.addObject("loginFlag", "0");//登录状态 0未登陆 1已登录
				modelAndView.addObject("openFlag", "0"); //开户状态 0未开户 1已开户
				modelAndView.addObject("investFlag", "0");//是否出借过该项目 0未出借 1已出借
				modelAndView.addObject("riskFlag", "0");//是否进行过风险测评 0未测评 1已测评
				modelAndView.addObject("setPwdFlag", "0");//是否设置过交易密码 0未设置 1已设置
				modelAndView.addObject("forbiddenFlag", "0");//是否禁用 0未禁用 1已禁用
			}
			return modelAndView;
		}
		return modelAndView;
	}

	/**
	 * 查询相应的汇计划债权列表
	 *
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = HjhDetailDefine.PLAN_BORROW_ACTION, produces = "application/json; charset=utf-8")
	public PlanBorrowListAjaxBean searchPlanBorrow(@ModelAttribute PlanBorrowBean form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(HjhDetailDefine.THIS_CLASS, HjhDetailDefine.PLAN_BORROW_ACTION);
		PlanBorrowListAjaxBean result = new PlanBorrowListAjaxBean();
		this.createPlanBorrowPage(request, result, form);
		result.success();
		LogUtil.endLog(HjhDetailDefine.THIS_CLASS, HjhDetailDefine.PLAN_BORROW_ACTION);
		return result;
	}

	/**
	 * 创建用户出借记录分页查询
	 * @param request
	 * @param result
	 * @param form
	 */
	private void createPlanBorrowPage(HttpServletRequest request, PlanBorrowListAjaxBean result, PlanBorrowBean form) {
		String planNid = form.getPlanNid();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("planNid", planNid);
		Integer userId = WebUtils.getUserId(request);
		params.put("userId", userId);
		Date date = GetDate.getDate();
		int dayStart10 = GetDate.getDayStart10(date);
		int dayEnd10 = GetDate.getDayEnd10(date);
		params.put("startTime", dayStart10);
		params.put("endTime", dayEnd10);
		int recordTotal = this.hjhDetailService.countPlanBorrowRecordTotal(params);
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
			List<DebtPlanBorrowCustomize> recordList = hjhDetailService.selectPlanBorrowList(params);
			for(DebtPlanBorrowCustomize object : recordList){
				if("".equalsIgnoreCase(object.getBorrowPurpose())){
					object.setBorrowPurpose("个人资金周转");
				}
			}
			result.setPlanBorrowList(recordList);
			result.setPaginator(paginator);
		} else {
			result.setPlanBorrowList(new ArrayList<DebtPlanBorrowCustomize>());
			result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
		}
	}

	/**
	 * 查询相应的汇计划的加入明细
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = HjhDetailDefine.PLAN_ACCEDE_ACTION, produces = "application/json; charset=utf-8")
	public PlanAccedeListAjaxBean searchPlanAccede(@ModelAttribute PlanAccedeBean form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(HjhDetailDefine.THIS_CLASS, HjhDetailDefine.PLAN_ACCEDE_ACTION);
		PlanAccedeListAjaxBean result = new PlanAccedeListAjaxBean();
		this.createPlanAccedePage(result, form);
		result.success();
		LogUtil.endLog(HjhDetailDefine.THIS_CLASS, HjhDetailDefine.PLAN_ACCEDE_ACTION);
		return result;
	}

	/**
	 * 创建汇计划的加入明细分页查询
	 *
	 * @param result
	 * @param form
	 */
	private void createPlanAccedePage(PlanAccedeListAjaxBean result, PlanAccedeBean form) {
		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		String planNid = form.getPlanNid();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("planNid", planNid);
		int recordTotal = this.hjhDetailService.countPlanAccedeRecordTotal(params);
		//加入总次数
		result.setAccedeTimes(recordTotal + "");
		if (recordTotal > 0) {
			//加入总金额
			Long sum = hjhDetailService.selectPlanAccedeSum(params);
			result.setAccedeTotal(df.format(sum) + "");
			// 查询相应的列表数据
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
			int limit = paginator.getLimit();
			int offSet = paginator.getOffset();
			if (offSet == 0 || offSet > 0) {
				params.put("limitStart", offSet);
			}
			if (limit > 0) {
				params.put("limitEnd", limit);
			}
			List<DebtPlanAccedeCustomize> recordList = hjhDetailService.selectPlanAccedeList(params);
			result.setPlanAccedeList(recordList);
			result.setPaginator(paginator);
		} else {
			result.setPlanAccedeList(new ArrayList<DebtPlanAccedeCustomize>());
			result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
		}
	}

	/**
	 * 加入汇计划请求
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(HjhDetailDefine.PLAN_INVEST_ACTION)
	public ModelAndView joinPlan(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(PlanController.class.toString(), HjhDetailDefine.PLAN_INVEST_ACTION);
		Integer userId = WebUtils.getUserId(request);
		ModelAndView modelAndView = new ModelAndView();
		String planNid = request.getParameter("nid");
		// 神策数据统计 add by liuyang 20180726 start
		String presetProps = request.getParameter("presetProps");
		// 神策数据统计 add by liuyang 20180726 end
		@SuppressWarnings("unused")
		String redisKey = "";
		if(StringUtils.isNotEmpty(planNid)){
			redisKey = RedisConstants.HJH_PLAN + planNid;
		}
		// 出借金额
		String accountStr = request.getParameter("money");
		if (accountStr == null || "".equals(accountStr)) {
			accountStr = "0";
		}
		modelAndView.addObject("plan", "1");
		modelAndView.addObject("planNid", planNid);

		HjhPlan plan = hjhDetailService.getPlanByNid(planNid);
		String lockPeriod = plan.getLockPeriod().toString();
		String dayOrMonth = plan.getIsMonth().equals(0)?"天":"个月";
		WebViewUser user = WebUtils.getUser(request);
		UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
		userOperationLogEntity.setOperationType(4);
		userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
		userOperationLogEntity.setPlatform(0);
		userOperationLogEntity.setRemark(lockPeriod+dayOrMonth+"智投");
		userOperationLogEntity.setOperationTime(new Date());
		userOperationLogEntity.setUserName(user.getUsername());
		userOperationLogEntity.setUserRole(user.getRoleId());
		hjhDetailService.sendUserLogMQ(userOperationLogEntity);
		_log.info("joinPlan ShiroUtil.getLoginUserId--------------------" + userId);// 如果没有本金出借且有优惠券出借
		BigDecimal decimalAccount = StringUtils.isNotEmpty(accountStr) ? new BigDecimal(accountStr) : BigDecimal.ZERO;

		// add by pcc 汇添金无本金出借是使用优惠券出借 start
		// 优惠券用户id （coupon_user的id）
		String couponGrantId = request.getParameter("couponGrantId");

		_log.info("出借开始，userId:" + userId + ", borrowNid:" + planNid + ", tenderMoney:" + accountStr + ", couponGrantId:" + couponGrantId);

		// 优惠券收益
		modelAndView.addObject("couponInterest", "0");
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
			_log.info("cuc.getCouponType():" + cuc.getCouponType());
			this.couponTender(modelAndView, request, cuc, userId, couponOldTime);
			return modelAndView;
		}
		// add by pcc 汇添金无本金出借是使用优惠券出借 end
		JSONObject result = hjhDetailService.checkParamPlan(planNid, accountStr, userId, couponGrantId, redisKey, CustomConstants.TENDER_THRESHOLD);

		/** redis 锁 */
		boolean reslut = RedisUtils.tranactionSet("HjhInvestUser" + userId, 10);
		if(!reslut){
			modelAndView.setViewName(HjhDetailDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("investDesc", "您正在出借，请稍后再试...");
			return modelAndView;
		}

		if (result == null) {
			LogUtil.infoLog(HjhDetailDefine.THIS_CLASS, "校验异常");
			modelAndView.setViewName(HjhDetailDefine.INVEST_ERROR_PATH);
			// mod by nxl 智投服务 修改显示
//			modelAndView.addObject("investDesc", "抱歉，出借失败，请重试！");
			modelAndView.addObject("investDesc","授权服务失败，请重试！");
			return modelAndView;
		} else if (result.get("error") != null && result.get("error").equals("1")) {
			LogUtil.infoLog(HjhDetailDefine.THIS_CLASS, result.get("data") + "");
			modelAndView.setViewName(HjhDetailDefine.INVEST_ERROR_PATH);
			// mod by nxl 智投服务 修改显示
//			modelAndView.addObject("investDesc", "抱歉，出借失败，请重试！");
			modelAndView.addObject("investDesc","授权服务失败，请重试！");
			return modelAndView;
		}
		String tenderUsrcustid = result.getString("tenderUsrcustid");
		// 生成冻结订单
		String frzzeOrderId = GetOrderIdUtils.getOrderId0(Integer.valueOf(userId));
		String frzzeOrderDate = GetOrderIdUtils.getOrderDate();
		// 冻结 加入 相应金额 明细
		Jedis jedis = pool.getResource();
			// 操作redis
			while ("OK".equals(jedis.watch(redisKey))) {
				String balance = RedisUtils.get(redisKey);
				//balance = RedisUtils.get(redisKey);
				if (StringUtils.isNotBlank(balance)) {
					LogUtil.infoLog(HjhDetailDefine.THIS_CLASS, "PC用户:" + userId + "***********************************加入计划冻结前可投金额：" + balance);
					if (new BigDecimal(balance).compareTo(BigDecimal.ZERO) == 0) {
						modelAndView.setViewName(HjhDetailDefine.INVEST_ERROR_PATH);
						LogUtil.infoLog(HjhDetailDefine.THIS_CLASS, "您来晚了，下次再来抢吧");
						modelAndView.setViewName(HjhDetailDefine.INVEST_ERROR_PATH);
						// mod by nxl 智投服务 修改显示
//						modelAndView.addObject("investDesc", "抱歉，出借失败，请重试！");
						modelAndView.addObject("investDesc","授权服务失败，请重试！");
						return modelAndView;
					} else {
						if (new BigDecimal(balance).compareTo(decimalAccount) < 0) {
							LogUtil.infoLog(HjhDetailDefine.THIS_CLASS, "可加入剩余金额为" + balance + "元");
							modelAndView.setViewName(HjhDetailDefine.INVEST_ERROR_PATH);
							// mod by nxl 智投服务 修改显示
//							modelAndView.addObject("investDesc", "抱歉，出借失败，请重试！");
							modelAndView.addObject("investDesc","授权服务失败，请重试！");
							return modelAndView;
						} else {
							Transaction tx = jedis.multi();
							// 事务：计划当前可用额度 = 计划未投前可用余额 - 用户出借额度
							BigDecimal lastAccount = new BigDecimal(balance).subtract(decimalAccount);
							tx.set(redisKey, lastAccount + "");
							List<Object> result1 = tx.exec();
							if (result1 == null || result1.isEmpty()) {
								jedis.unwatch();
								LogUtil.infoLog(HjhDetailDefine.THIS_CLASS, "redis可加入剩余金额为" + balance + "元");
								//modelAndView.setViewName(HjhDetailDefine.INVEST_ERROR_PATH);
								// mod by nxl 智投服务 修改显示
//								modelAndView.addObject("investDesc", "抱歉，出借失败，请重试！");
								//modelAndView.addObject("investDesc","授权服务失败，请重试！");
								//return modelAndView;
							} else {
								_log.info("PC用户:" + userId + "***********************************计划未投前可用余额redis：" + decimalAccount);
								// 写队列
								break;
							}
						}
					}
				} else {
					LogUtil.infoLog(HjhDetailDefine.THIS_CLASS, "您来晚了，下次再来抢吧");
					modelAndView.setViewName(HjhDetailDefine.INVEST_ERROR_PATH);
					// mod by nxl 智投服务 修改显示
//					modelAndView.addObject("investDesc", "抱歉，出借失败，请重试！");
					modelAndView.addObject("investDesc","授权服务失败，请重试！");
					return modelAndView;
				}
			}

		boolean afterDealFlag = false;
		String couponInterest="0";
		// 写入加入计划表
		try {
			// 生成加入订单
			String planOrderId = GetOrderIdUtils.getOrderId0(Integer.valueOf(userId));
			// update by pcc 汇添金有本金出借是使用优惠券出借修改updateAfterPlanRedis接口

			// (原汇添金)添加couponGrantId、modelAndView参数 start
			//afterDealFlag = planService.updateAfterPlanRedis(planNid, frzzeOrderId, userId, accountStr, tenderUsrcustid, 0, GetCilentIP.getIpAddr(request), "", frzzeOrderDate, planOrderId, couponGrantId, modelAndView,couponInterest);
			afterDealFlag = hjhDetailService.updateAfterPlanRedis(planNid, frzzeOrderId, userId, accountStr, tenderUsrcustid, 0, GetCilentIP.getIpAddr(request), "", frzzeOrderDate, planOrderId,
					couponGrantId, modelAndView,couponInterest);
			// update by pcc 汇添金有本金出借是使用优惠券出借修改updateAfterPlanRedis接口
			// 添加couponGrantId、modelAndView参数 end
			if (afterDealFlag) {
				modelAndView.setViewName(HjhDetailDefine.INVEST_SUCCESS_PATH);
				LogUtil.endLog(HjhDetailDefine.class.toString(), HjhDetailDefine.PLAN_INVEST_ACTION, "[交易完成后,回调结束]");
				DecimalFormat df = CustomConstants.DF_FOR_VIEW;
				String interest = null;
				if (StringUtils.isBlank(interest)) {
					// 根据项目编号获取相应的项目
					//DebtPlan debtPlan = planService.getPlanByNid(planNid);
					HjhPlan debtPlan = hjhDetailService.getPlanByNid(planNid);
					BigDecimal planApr = debtPlan.getExpectApr();
					// 周期
					Integer planPeriod = debtPlan.getLockPeriod();
					BigDecimal earnings = new BigDecimal("0");
					df.setRoundingMode(RoundingMode.FLOOR);
					String borrowStyle = plan.getBorrowStyle();
					if (StringUtils.isNotEmpty(borrowStyle)) {
						// 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷365*锁定期；
						if (StringUtils.equals("endday", borrowStyle)){
							// 计算历史回报
							earnings = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(accountStr), planApr.divide(new BigDecimal("100")), planPeriod);
						}
						// 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
						else {
							earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(accountStr), planApr.divide(new BigDecimal("100")), planPeriod);
						}
					}
					CommonSoaUtils.listedTwoInvestment(Integer.valueOf(userId), new BigDecimal(accountStr));
					CommonSoaUtils.listInvestment(Integer.valueOf(userId), new BigDecimal(accountStr), borrowStyle,planPeriod);
					modelAndView.addObject("earnings", earnings);
					interest = df.format(earnings.add(new BigDecimal(couponInterest)));
				}
				if (StringUtils.isNotBlank(interest)) {
					modelAndView.addObject("interest", interest);
				}
				modelAndView.addObject("account", df.format(new BigDecimal(accountStr)));
				// mod by nxl 智投服务 修改显示
//				modelAndView.addObject("investDesc", "恭喜您出借成功!");
				modelAndView.addObject("investDesc", "恭喜您授权服务成功!");
				_log.info("PC用户:" + userId + "***********************************加入计划成功：" + accountStr);
				logger.info("PC加入计划用户:" + userId +",planOrderId:"+ planOrderId+"发送mq");
				//MQ  加入汇计划，出借触发奖励
				sendMQActivity(userId,planOrderId,new BigDecimal(accountStr),3);
				sendRrturnCashActivity(userId,planOrderId,new BigDecimal(accountStr),3);
				_log.info("presetProps:" + presetProps);
				// add by liuyang 神策数据统计  20180726 start
				if (StringUtils.isNotBlank(presetProps)){
					SensorsDataBean sensorsDataBean = new SensorsDataBean();
					// 将json串转换成Bean
					try {
						Map<String, Object> sensorsDataMap = JSONObject.parseObject(presetProps, new TypeReference<Map<String, Object>>() {
						});
						sensorsDataBean.setPresetProps(sensorsDataMap);
						sensorsDataBean.setUserId(userId);
						sensorsDataBean.setEventCode("submit_intelligent_invest");
						sensorsDataBean.setOrderId(planOrderId);
						// 发送神策数据统计MQ
						this.hjhDetailService.sendSensorsDataMQ(sensorsDataBean);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// add by liuyang 神策数据统计 20180726 end

				LogUtil.endLog(HjhDetailController.class.toString(), HjhDetailDefine.PLAN_INVEST_ACTION);

				//end
				return modelAndView;
			} else {
				_log.info("PC用户:" + userId + "***********************************预约成功后处理失败：" + accountStr);
				// 恢复redis
				hjhDetailService.recoverRedis(planNid, userId, accountStr, lockPeriod,redisKey);
				LogUtil.infoLog(HjhDetailDefine.THIS_CLASS, "系统异常");
				modelAndView.setViewName(HjhDetailDefine.INVEST_ERROR_PATH);
				// mod by nxl 智投服务 修改显示
//				modelAndView.addObject("investDesc", "抱歉，出借失败，请重试！");
				modelAndView.addObject("investDesc","授权服务失败，请重试！");
				return modelAndView;
			}
		} catch (Exception e) {
			// 恢复redis
			hjhDetailService.recoverRedis(planNid, userId, accountStr, lockPeriod,redisKey);
			modelAndView.setViewName(HjhDetailDefine.INVEST_ERROR_PATH);
			// mod by nxl 智投服务 修改显示
//			modelAndView.addObject("investDesc", "抱歉，出借失败，请重试！");
			modelAndView.addObject("investDesc","授权服务失败，请重试！");
			LogUtil.errorLog(PlanController.class.getName(), HjhDetailDefine.PLAN_INVEST_ACTION, e);
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
			modelAndView.setViewName(HjhDetailDefine.INVEST_ERROR_PATH);
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
			modelAndView.setViewName(HjhDetailDefine.INVEST_SUCCESS_PATH);
			return modelAndView;
		} else {
			LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender", "优惠券出借结束。。。。。。");
			modelAndView.setViewName(HjhDetailDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("investDesc", jsonObject.getString("statusDesc"));
			modelAndView.addObject("plan", 1);
			return modelAndView;
		}

	}

	/**
	 * 优惠券出借校验
	 *
	 * @param userId
	 *
	 * @param account
	 * @param couponGrantId
	 * @param planNid
	 * @param platform
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
	 * 发放活动奖励
	 * @param userId
	 * @param order
	 * @param investMoney
	 * @param projectType 项目类型
	 */
	private void sendMQActivity(Integer userId,String order,BigDecimal investMoney,int projectType){
		// 加入到消息队列
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("orderId", order);
		params.put("investMoney", investMoney.toString());
		//来源,1=新手标，2=散标，3=汇计划
		params.put("productType", projectType);
		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.MDIAU_ACTIVITY, JSONObject.toJSONString(params));
	}
	/**
	 * 纳觅返现活动
	 * @param userId
	 * @param order
	 */
	private void sendRrturnCashActivity(Integer userId,String order,BigDecimal investMoney,int projectType){
		// 加入到消息队列
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("orderId", order);
		params.put("investMoney", investMoney.toString());
		//来源,1=新手标，2=散标，3=汇计划
		params.put("productType", projectType);
		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.RETURN_CASH_ACTIVITY, JSONObject.toJSONString(params));
	}
	/**
	 * pc加入计划验证
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = HjhDetailDefine.PLAN_CHECK_ACTION, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public WebBaseAjaxResultBean appointCheck(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(HjhDetailDefine.THIS_CLASS, HjhDetailDefine.PLAN_CHECK_ACTION);
		// 计划编号
		String planNid = request.getParameter("nid");

		//阀值
		String threshold = "1000" ;

		@SuppressWarnings("unused")
		String redisKey = "";
		if(StringUtils.isNotEmpty(planNid)){
			redisKey = RedisConstants.HJH_PLAN + planNid;
		}
		// 出借金额
		String money = request.getParameter("money");
		// 优惠券ID
		String couponGrantId = request.getParameter("couponGrantId");
		// 是否是最后一笔
		//String isLast = request.getParameter("isLast");

		Integer userId = WebUtils.getUserId(request);
		if (money == null || "".equals(money)) {
			money = "0";
		}
		/*--------------upd by liushouyi 线上空指针异常 Start----------------*/
		//System.out.println("tenderCheck ShiroUtil.getLoginUserId--------------------" + userId);
		logger.info("tenderCheck ShiroUtil.getLoginUserId--------------------" + userId);
		/*--------------upd by liushouyi 线上空指针异常 End----------------*/

		//改为现在汇计划校验
		JSONObject info = hjhDetailService.checkParamPlan(planNid, money, userId, couponGrantId, redisKey, threshold);
		// 参数验证成功，则返回url，否则不返回
		WebBaseAjaxResultBean result = new WebBaseAjaxResultBean();
		if (info == null) {
			result.setStatus(false);
			result.setMessage("出借失败");
		} else if (info.get("error").equals("0")) {

			if (StringUtils.isNotEmpty(couponGrantId)) {
				try {
					// 校验优惠券
					JSONObject jsonObject = CommonSoaUtils.planCheckCoupon(userId + "", planNid, money,CustomConstants.CLIENT_PC, couponGrantId);
					int couponStatus = jsonObject.getIntValue("status");
					String couponDesc = jsonObject.getString("statusDesc");
					_log.info("updateCouponTender" + "优惠券出借校验结果：" + couponDesc);
					if (couponStatus == 0) {
						result.setStatus(true);
						result.setMessage("操作成功");
					} else {
						result.setStatus(false);
						result.setMessage(couponDesc);
					}
				} catch (Exception e) {
					_log.info("=============出借优惠券校验异常!");
				}
			}else{

				result.setStatus(true);
				result.setMessage("操作成功");
			}

		} else {
			result.setStatus(false);
			result.setMessage(info.getString("data"));
		}
		// 获得错误码
		String errorCode = tenderService.checkHjhErrCode(userId);
		/*--------------upd by liushouyi 线上空指针异常 Start----------------*/
		//System.out.println("返回errorCode ： " + errorCode);
		//System.out.println("返回info：" + info);
		logger.info("返回errorCode ： " + errorCode);
		logger.info("返回info：" + info);
		/*--------------upd by liushouyi 线上空指针异常 End----------------*/
		//用户测评
		//从user中获取客户类型，ht_user_evalation_result（用户测评总结表）
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
					revaluation_money = RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_CONSERVATIVE) == null ? "0": RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_CONSERVATIVE);
					//如果reids不存在或者为零尝试获取数据库（单笔）
					if("0".equals(revaluation_money)){
						revaluation_money = evalConfig.getConservativeEvaluationSingleMoney() == null ? "0": String.valueOf(evalConfig.getConservativeEvaluationSingleMoney());
					}
					//从redis获取金额（代收本金）
					revaluation_money_principal = RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_CONSERVATIVE_PRINCIPAL) == null ? "0": RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_CONSERVATIVE_PRINCIPAL);
					//如果reids不存在或者为零尝试获取数据库（代收本金）
					if("0".equals(revaluation_money_principal)){
						revaluation_money_principal = evalConfig.getConservativeEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getConservativeEvaluationPrincipalMoney());
					}
					break;
				case "稳健型":
					//从redis获取金额（单笔）
					revaluation_money = RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_ROBUSTNESS) == null ? "0": RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_ROBUSTNESS);
					//如果reids不存在或者为零尝试获取数据库（单笔）
					if("0".equals(revaluation_money)){
						revaluation_money = evalConfig.getSteadyEvaluationSingleMoney() == null ? "0": String.valueOf(evalConfig.getSteadyEvaluationSingleMoney());
					}
					//从redis获取金额（代收本金）
					revaluation_money_principal = RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_ROBUSTNESS_PRINCIPAL) == null ? "0": RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_ROBUSTNESS_PRINCIPAL);
					//如果reids不存在或者为零尝试获取数据库（代收本金）
					if("0".equals(revaluation_money_principal)){
						revaluation_money_principal = evalConfig.getSteadyEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getSteadyEvaluationPrincipalMoney());
					}
					break;
				case "成长型":
					//从redis获取金额（单笔）
					revaluation_money = RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_GROWTH) == null ? "0": RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_GROWTH);
					//如果reids不存在或者为零尝试获取数据库（单笔）
					if("0".equals(revaluation_money)){
						revaluation_money = evalConfig.getGrowupEvaluationSingleMoney() == null ? "0": String.valueOf(evalConfig.getGrowupEvaluationSingleMoney());
					}
					//从redis获取金额（代收本金）
					revaluation_money_principal = RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_GROWTH_PRINCIPAL) == null ? "0": RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_GROWTH_PRINCIPAL);
					//如果reids不存在或者为零尝试获取数据库（代收本金）
					if("0".equals(revaluation_money_principal)){
						revaluation_money_principal = evalConfig.getGrowupEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getGrowupEvaluationPrincipalMoney());
					}
					break;
				case "进取型":
					//从redis获取金额（单笔）
					revaluation_money = RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_AGGRESSIVE) == null ? "0": RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_AGGRESSIVE);
					//如果reids不存在或者为零尝试获取数据库（单笔）
					if("0".equals(revaluation_money)){
						revaluation_money = evalConfig.getEnterprisingEvaluationSinglMoney() == null ? "0": String.valueOf(evalConfig.getEnterprisingEvaluationSinglMoney());
					}
					//从redis获取金额（代收本金）
					revaluation_money_principal = RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_AGGRESSIVE_PRINCIPAL) == null ? "0": RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_AGGRESSIVE_PRINCIPAL);
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
					result.setMessage("根据监管要求，投资前必须进行风险测评。");
					LogUtil.endLog(TenderController.class.toString(), TenderDefine.INVEST_ACTION);
					return result;
				}
				if(CustomConstants.EVALUATION_CHECK.equals(intellectualEveluationTypeCheck)){
					//计划类判断用户类型为稳健型以上才可以出借
					HjhPlan plan = hjhDetailService.getPlanByNid(planNid);
					if(plan != null){
						//获取计划中配置的评级类型
						if(!CommonUtils.checkStandardInvestment(eval_type,"HJHPLAN",plan.getInvestLevel())){
							//返回错误码
							result.setErrorCode(CustomConstants.BANK_TENDER_RETURN_CUSTOMER_STANDARD_FAIL);
							result.setStatus(false);
							//返回类型
							result.setInvestLevel(eval_type);
							result.setEvalFlagType(plan.getInvestLevel());
							result.setMessage("您的风险等级为 #"+eval_type+"# \\n达到 #"+plan.getInvestLevel()+"# 及以上才可以出借此项目");
							LogUtil.endLog(TenderController.class.toString(), TenderDefine.INVEST_ACTION);
							return result;
						}
					}
				}
				if(revaluation_money == null){
					_log.info("=============从redis中获取测评类型和上限金额异常!(没有获取到对应类型的限额数据) eval_type="+eval_type);
				}else {
					if(CustomConstants.EVALUATION_CHECK.equals(intellectualEvaluationMoneyCheck)){
						//金额对比判断（校验金额 大于 设置测评金额）
						if (new BigDecimal(money).compareTo(new BigDecimal(revaluation_money)) > 0) {
							//返回错误码
							result.setErrorCode(CustomConstants.BANK_TENDER_RETURN_LIMIT_EXCESS);
							result.setStatus(false);
							//返回类型和限额
							result.setInvestLevel(eval_type);
							result.setRevaluationMoney(StringUtil.getTenThousandOfANumber(Double.valueOf(revaluation_money).intValue()));
							LogUtil.endLog(TenderController.class.toString(), TenderDefine.INVEST_ACTION);
							return result;
						}
					}
				}
				if(revaluation_money_principal == null){
					_log.info("=============从redis中获取测评类型和上限金额异常!(没有获取到对应类型的限额数据) eval_type="+eval_type);
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
								result.setInvestLevel(eval_type);
								result.setRevaluationMoneyPrincipal(StringUtil.getTenThousandOfANumber(Double.valueOf(revaluation_money_principal).intValue()));
								LogUtil.endLog(TenderController.class.toString(), TenderDefine.INVEST_ACTION);
								return result;
							}
						}
					}
				}
			}
		}else{
			_log.info("=============该用户测评总结数据为空! userId="+userId);
		}

		// 加入返回结果
		result.setErrorCode(errorCode);
		if (StringUtils.isNotEmpty(errorCode)) {
			result.setStatus(false);
		}
		LogUtil.endLog(HjhDetailDefine.THIS_CLASS, HjhDetailDefine.PLAN_CHECK_ACTION);
		return result;
	}


	/**
	 * 查询相应的项目详情
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = HjhDetailDefine.BORROW_DETAIL_ACTION)
	public ModelAndView searchProjectDetail(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(HjhDetailDefine.THIS_CLASS, HjhDetailDefine.BORROW_DETAIL_ACTION);
		ModelAndView modelAndView = new ModelAndView(HjhDetailDefine.BORROW_DETAIL_PTAH);
		// 1.获取项目标号
		String borrowNid = request.getParameter("borrowNid");
		// 用户信息
		WebViewUser loginUser = WebUtils.getUser(request);
		// 2.根据项目标号获取相应的项目信息
		WebProjectDetailCustomize borrow = this.borrowService.selectProjectDetail(borrowNid);
		//没有标的信息
		if (borrow == null) {
			modelAndView = new ModelAndView(HjhDetailDefine.ERROR_PTAH);
			return modelAndView;
		}
		//用户id
		Integer userId = null;
		//没有登录信息
		if(loginUser != null){
			userId = loginUser.getUserId();
		}
		getProjectDetailNew(modelAndView, borrow,userId);
		/*Add 协议模版 By dangzw ---start---*/
		//协议名称 动态获得
		List<ProtocolTemplate> listT = agreementService.getdisplayNameDynamic();
		if(CollectionUtils.isNotEmpty(listT)){
			//是否在枚举中有定义
			for (ProtocolTemplate p : listT) {
				String protocolType = p.getProtocolType();
				String alia = ProtocolEnum.getAlias(protocolType);
				if (alia != null){
					modelAndView.addObject(alia, p.getDisplayName());
				}
			}
		}
		/*Add 协议模版 By dangzw ---end---*/
		LogUtil.endLog(BorrowDefine.THIS_CLASS, HjhDetailDefine.BORROW_DETAIL_ACTION);
		return modelAndView;

	}

	/**
	 * 获取新标的相关信息
	 * @param modelAndView
	 * @param borrow
	 */
	private void getProjectDetailNew(ModelAndView modelAndView,WebProjectDetailCustomize borrow,Integer userId){
		//标的号
		String borrowNid = borrow.getBorrowNid();

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
			// 还款信息
			BorrowRepay borrowRepay = borrowService.getBorrowRepay(borrowNid);
			modelAndView.addObject("repayPlanList", repayPlanList);
			//相关文件
			List<BorrowFileCustomBean> files = this.borrowService.searchProjectFiles(borrowNid, CustomConstants.HOST);
			modelAndView.addObject("fileList", files);
			//资产列表
			JSONArray json = new JSONArray();
			//基础信息
			String baseTableData = "[]";
			//资产信息
			String assetsTableData = "[]";
			//项目介绍
			String intrTableData = "[]";
			//信用状况
			String credTableData = "[]";
			//审核信息
			String reviewTableData = "[]";
			//其他信息
			String otherTableData = "[]";
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
			modelAndView.addObject("viewableFlag", "0");// add by nxl 未登录不可见
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
			// 如何为空判断
			int count = 0;
			if(userId!=null) {
				count = this.borrowService.countUserInvest(userId, borrowNid);
			}
			if (count > 0) {
				modelAndView.addObject("investFlag", "1");//是否出借过该项目 0未出借 1已出借
			}else{
				modelAndView.addObject("investFlag", "0");//是否出借过该项目 0未出借 1已出借
			}
			// add 汇计划二期前端优化  针对区分原始标与债转标 nxl 20180424 start
			/**
			 * 查看标的详情
			 * 原始标：复审中、还款中、已还款状态下 如果当前用户是否投过此标，是：可看，否则不可见
			 * 债转标的：未被完全承接时，项目详情都可看；被完全承接时，只有出借者和承接者可查看
			 */
			String viewableFlag ="0";
			int intCount = 0;
			Map<String, Object> mapObj = new HashMap<String, Object>();
			mapObj.put("borrowNid", borrowNid);
			List<HjhDebtCredit> listHjhDebtCredit = this.hjhDetailService.selectHjhDebtCreditList(mapObj);
			// add by nxl 是否发生过债转
			Boolean isDebt = false;
			if(null!=listHjhDebtCredit&&listHjhDebtCredit.size()>0) {
				//部分承接
				List<Integer> valuesIn = new ArrayList<>();
				valuesIn.add(0);
				valuesIn.add(1);
				mapObj.put("inStatus",valuesIn);
				List<HjhDebtCredit> listHjhDebtCreditOnePlace = this.hjhDetailService.selectHjhDebtCreditList(mapObj);
				if(null!=listHjhDebtCreditOnePlace&&listHjhDebtCreditOnePlace.size()>0) {
					//部分债转
					viewableFlag ="1";
				}else {
					// 完全债转
					for(HjhDebtCredit deptcredit:listHjhDebtCredit) {
						//待承接本金 = 0
						if(null!=deptcredit.getCreditCapitalWait()&&deptcredit.getCreditCapitalWait().compareTo(BigDecimal.ZERO) == 0) {
							Map<String,Object> mapParam = new HashMap<String,Object>();
							mapParam.put("userId", userId);
							mapParam.put("borrowNid", borrowNid);
							intCount = borrowService.countCreditTender(mapParam);
							if(intCount>0||count>0) {
								viewableFlag = "1";
							}
						}
					}
				}
				isDebt = true;
			}else {
				//原始标
				//复审中，还款中和已还款状态出借者(可看)
				if(borrow.getStatusOrginal()==3||borrow.getStatusOrginal()==4||borrow.getStatusOrginal()==5) {
					if(count>0) {
						//可以查看标的详情
						viewableFlag ="1";
					}else {
						//提示仅出借者可看
						viewableFlag ="0";
					}
				}else {
					viewableFlag="1";
				}
			}
			modelAndView.addObject("viewableFlag", viewableFlag);
			modelAndView.addObject("isDebt", isDebt);
			// add 汇计划二期前端优化  针对区分原始标与债转标  nxl 20180424 end
			//是否设置交易密码
			if(user.getIsSetPassword() == 1){
				modelAndView.addObject("setPwdFlag", "1");
			}else{
				modelAndView.addObject("setPwdFlag", "0");
			}
			// 风险测评标识
			// modify by liuyang 20180411 用户是否完成风险测评标识 start
			// JSONObject jsonObject = CommonSoaUtils.getUserEvalationResultByUserId(userId + "");
			// modelAndView.addObject("riskFlag", jsonObject.get("userEvaluationResultFlag"));
//			modelAndView.addObject("riskFlag",user.getIsEvaluationFlag());
			try {
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
			// modify by liuyang 20180411 用户是否完成风险测评标识 end
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



	/**
	 * 查询相应的项目的出借列表
	 *
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = HjhDetailDefine.BORROW_INVEST_ACTION, produces = "application/json; charset=utf-8")
	public BorrowInvestListAjaxBean searchProjectInvestList(@ModelAttribute BorrowInvestBean form, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(HjhDetailDefine.THIS_CLASS, HjhDetailDefine.BORROW_INVEST_ACTION);
		BorrowInvestListAjaxBean result = new BorrowInvestListAjaxBean();
		this.createProjectInvestPage(result, form);

		result.success();
		result.setHost(HjhDetailDefine.HOST);
		LogUtil.endLog(HjhDetailDefine.THIS_CLASS, HjhDetailDefine.BORROW_INVEST_ACTION);
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
			result.setProjectInvestList(recordList);
			result.setPaginator(paginator);
		} else {
			result.setProjectInvestList(new ArrayList<WebProjectInvestListCustomize>());
			result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
		}
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

		// 得到对象
		Class c = objBean.getClass();
		String currencyName = "元";
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
							}else{
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
	 *
	 * 根据出借项目id历史回报
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = HjhDetailDefine.INVEST_INFO_ACTION, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public InvestInfoAjaxBean getInvestInfo(HttpServletRequest request, HttpServletResponse response) {

		String planNid = request.getParameter("nid");
		String accountStr = request.getParameter("money");
		// 获取优惠券编号
		String couponId = request.getParameter("couponGrantId");
		InvestInfoAjaxBean investInfo = new InvestInfoAjaxBean();
		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		df.setRoundingMode(RoundingMode.FLOOR);
		HjhPlan plan = hjhDetailService.getPlanByNid(planNid);
		//DebtPlan plan = planService.getPlanByNid(planNid);
		UserCouponConfigCustomize couponConfig = null;
		if (null != plan) {
			BigDecimal couponInterest = BigDecimal.ZERO;
			// 获取相应的登陆用户
			WebViewUser loginUser = WebUtils.getUser(request);
			/** 汇添金优惠券使用开始 pcc start */
			if (!"0".equals(plan.getCouponConfig())&&loginUser!=null) {

				/** 计算最优优惠券开始 pccvip isThereCoupon 1是有最优优惠券，0无最有优惠券 */

				couponConfig = planService.getUserOptimalCoupon(couponId, planNid, loginUser.getUserId(), accountStr, "0");

				if (couponConfig != null) {
					investInfo.setIsThereCoupon(1);

					couponInterest = planService.getCouponInterest(couponConfig.getUserCouponId(), planNid, accountStr);
					couponConfig.setCouponInterest(df.format(couponInterest));
				} else {
					investInfo.setIsThereCoupon(0);
				}
				investInfo.setCouponConfig(couponConfig);
				/** 计算最优优惠券结束 */

				/** 可用优惠券张数开始 pccvip */
				int availableCouponListCount = planService.getUserCouponAvailableCount(planNid, loginUser.getUserId(), accountStr, "0");
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
			if (!StringUtils.isBlank(accountStr) && new BigDecimal(accountStr).compareTo(BigDecimal.ZERO) == 1 ||!(StringUtils.isEmpty(accountStr) && couponConfig != null && couponConfig.getCouponType() == 1 && couponConfig.getAddFlg() == 1)) {
				// 收益率
				BigDecimal borrowApr = plan.getExpectApr();
				// 周期
				Integer borrowPeriod = plan.getLockPeriod();
				// 还款方式
				String borrowStyle = plan.getBorrowStyle();//endday

				if (StringUtils.isNotEmpty(borrowStyle)) {

					if (StringUtils.equals("endday", borrowStyle)){
						// 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷365*锁定期；
						earnings = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(accountStr), borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
						investInfo.setEarnings(df.format(earnings));
						investInfo.setStatus(true);
						investInfo.setMessage("历史回报计算完成");
					} else {
						// 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
						earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(accountStr), borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
						investInfo.setEarnings(df.format(earnings));
						investInfo.setStatus(true);
						investInfo.setMessage("历史回报计算完成");
					}
				}
			} else {
				investInfo.setStatus(false);
				investInfo.setMessage("请填写正确的出借金额");
			}
			if(couponConfig!=null && couponConfig.getCouponType()==3){
				investInfo.setCapitalInterest(df.format(earnings.add(couponInterest).subtract(couponConfig.getCouponQuota())));
			}else{
				investInfo.setCapitalInterest(df.format(earnings.add(couponInterest)));
			}

		} else {
			investInfo.setStatus(false);
			investInfo.setMessage("请填写正确的金额");
		}
		return investInfo;
	}

	/**
	 * add by nxl 新加承接记录列表显示 20180511 查询相应的项目的出借列表
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = HjhDetailDefine.BORROW_UNDERTAKE_ACTION, produces = "application/json; charset=utf-8")
	public BorrowUndertakeListAjaxBean searchBorrowUndertakeList(@ModelAttribute BorrowInvestBean form,
																 HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(HjhDetailDefine.THIS_CLASS, HjhDetailDefine.BORROW_UNDERTAKE_ACTION);
		BorrowUndertakeListAjaxBean result = new BorrowUndertakeListAjaxBean();

		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		// Borrow borrow = this.borrowService.getBorrowByNid(form.getBorrowNid());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", form.getBorrowNid());
		// 获取承接记录列表
		int undertRecordCount = this.borrowService.countCreditTenderByBorrowNid(form.getBorrowNid());
		String strUndertakeAccount = df.format(new BigDecimal("0"));
		if (undertRecordCount > 0) {
			// 查询承接记录列表
			Paginator paginator = new Paginator(form.getPaginatorPage(), undertRecordCount, form.getPageSize());
			int limit = paginator.getLimit();
			int offSet = paginator.getOffset();
			if (offSet == 0 || offSet > 0) {
				params.put("limitStart", offSet);
			}
			if (limit > 0) {
				params.put("limitEnd", limit);
			}
			List<WebProjectUndertakeListCustomize> undertRecordList = this.borrowService.selectProjectUndertakeList(params);
			// 总金额
			String strAccount = this.borrowService.sumUndertakeAccount(form.getBorrowNid());
			if (StringUtils.isNotEmpty(strAccount)) {
				BigDecimal bdAccount = new BigDecimal(strAccount);
				strUndertakeAccount = df.format(bdAccount);
			}
			result.setUndertRecordTotle(String.valueOf(undertRecordCount));
			result.setSumUndertakeAccount(strUndertakeAccount);
			result.setProjectUndertakeList(undertRecordList);
			result.setPaginator(paginator);
		} else {
			result.setUndertRecordTotle("0");
			result.setSumUndertakeAccount(strUndertakeAccount);
			result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
			result.setProjectUndertakeList(new ArrayList<WebProjectUndertakeListCustomize>());
		}
		result.success();
		result.setHost(HjhDetailDefine.HOST);
		LogUtil.endLog(HjhDetailDefine.THIS_CLASS, HjhDetailDefine.BORROW_UNDERTAKE_ACTION);
		return result;
	}

}
