/**
 * Description:出借控制器
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 *           Created at: 2015年12月4日 下午2:32:36
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.web.bank.web.user.tender;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.common.util.*;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.bank.service.evalation.EvaluationService;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.AccountDetailCustomize;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.customize.UserVO;
import org.apache.commons.lang3.StringUtils;
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

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.tender.TenderInfoAjaxBean;
import com.hyjf.bank.service.user.tender.TenderService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.AverageCapitalPlusInterestUtils;
import com.hyjf.common.calculate.AverageCapitalUtils;
import com.hyjf.common.calculate.BeforeInterestAfterPrincipalUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.WebBaseAjaxResultBean;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.coupon.CouponService;
import com.hyjf.web.user.invest.InvestServiceImpl;
import com.hyjf.web.util.WebUtils;

import redis.clients.jedis.JedisPool;

/**
 * @package com.hyjf.web.bank.web.user.tender
 * @author wangkun
 * @date 2015/12/04 14:00
 * @version V1.0  
 */
@Controller(TenderDefine.CONTROLLER_NAME)
@RequestMapping(value = TenderDefine.REQUEST_MAPPING)
public class TenderController extends BaseController {
	
	Logger _log = LoggerFactory.getLogger(TenderController.class);

	@Autowired
	private TenderService tenderService;

	@Autowired
	private CouponService couponService;

    @Autowired
    private EvaluationService evaluationService;

	@Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;
	public static JedisPool pool = RedisUtils.getPool();

	/** 当前controller名称 */
	public static final String THIS_CLASS = TenderController.class.getName();

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
	@RequestMapping(value = TenderDefine.INVEST_INFO_ACTION, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public TenderInfoAjaxBean getInvestInfo(HttpServletRequest request, HttpServletResponse response) {
		String borrowNid = request.getParameter("nid");
		String accountStr = request.getParameter("money");
		String couponId = request.getParameter("couponGrantId");
		TenderInfoAjaxBean investInfo = new TenderInfoAjaxBean();
		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		df.setRoundingMode(RoundingMode.FLOOR);
		// 查询项目信息
		String money = accountStr;
		Borrow borrow = tenderService.getBorrowByNid(borrowNid);
		if (null == borrow) {
			investInfo.setStatus(false);
			investInfo.setMessage("请刷新页面重试");
			return investInfo;
		}
		/** 计算最优优惠券开始 pccvip */
		WebViewUser loginUser = WebUtils.getUser(request);
		UserCouponConfigCustomize couponConfig = new UserCouponConfigCustomize();
		// 未登录，不计算优惠券
		if (loginUser != null) {
			if (couponId == null || "".equals(couponId) || couponId.length() == 0) {
				// 获取用户最优优惠券
				couponConfig = getBestCoupon(borrowNid, loginUser.getUserId(), accountStr, "0");
			} else {
				couponConfig = getBestCouponById(couponId);
			}
			if (couponConfig != null) {
				investInfo.setIsThereCoupon(1);

			} else {
				investInfo.setIsThereCoupon(0);
			}
			/** 可用优惠券张数开始 pccvip */
			String couponAvailableCount = couponService.getUserCouponAvailableCount(borrowNid, loginUser.getUserId(), accountStr, "0");
			investInfo.setCouponAvailableCount(new Integer(couponAvailableCount));
			/** 可用优惠券张数结束 pccvip */

			/** 获取用户是否是vip 开始 pccvip */
			UsersInfo usersInfo = tenderService.getUsersInfoByUserId(loginUser.getUserId());
			if (usersInfo.getVipId() != null && usersInfo.getVipId() != 0) {
				investInfo.setIfVip(1);
			} else {
				investInfo.setIfVip(0);
			}
			/** 获取用户是否是vip 结束 pccvip */

			/** 获取用户优惠券总张数开始 pccvip */
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("usedFlag", 0);
			paraMap.put("userId", loginUser.getUserId());
			Integer recordTotal = couponService.countCouponUsers(paraMap);
			investInfo.setRecordTotal(recordTotal);
			/** 获取用户优惠券总张数结束 pccvip */
		} else {
			couponConfig = null;
			// 是否有优惠券
			investInfo.setIsThereCoupon(0);
			// 优惠券总张数
			investInfo.setRecordTotal(0);
			// 优惠券可用张数
			investInfo.setCouponAvailableCount(0);
		}

		// 设置产品加息 显示收益率
		if (Validator.isIncrease(borrow.getIncreaseInterestFlag(), borrow.getBorrowExtraYield())) {
		    investInfo.setBorrowExtraYield(df.format(borrow.getBorrowExtraYield()));
        }
		
		// 如果出借金额不为空
		if ((!StringUtils.isBlank(money) && Long.parseLong(money) > 0) || (couponConfig != null && (couponConfig.getCouponType() == 3 || couponConfig.getCouponType() == 1))) {
			String borrowStyle = borrow.getBorrowStyle();
			// 收益率
			BigDecimal borrowApr = borrow.getBorrowApr();
			
			BigDecimal couponInterest = BigDecimal.ZERO;
			/** 叠加收益率开始 pccvip */
			if (couponConfig != null) {
				if (couponConfig.getCouponType() == 1) {
					couponInterest = getInterestDj(couponConfig.getCouponQuota(), couponConfig.getCouponProfitTime(), borrowApr);
				} else {
					couponInterest = getInterest(borrowStyle, couponConfig.getCouponType(), borrowApr, couponConfig.getCouponQuota(), money, borrow.getBorrowPeriod());
				}

				couponConfig.setCouponInterest(df.format(couponInterest));
				if (couponConfig.getCouponType() == 2) {
					borrowApr = borrowApr.add(couponConfig.getCouponQuota());
				}
				if (couponConfig.getCouponType() == 3) {
                    money = new BigDecimal(money).add(couponConfig.getCouponQuota()).toString();
				}
			}
			/** 叠加收益率结束 */
			// 周期
			Integer borrowPeriod = borrow.getBorrowPeriod();
			BigDecimal earnings = new BigDecimal("0");
			switch (borrowStyle) {
			case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
				// 计算历史回报
				earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(money), borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2,
						BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷360*天数；
				earnings = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(money), borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：历史回报=出借金额*年化收益÷12*月数；
				earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(money), borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2,
						BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：历史回报=出借金额*年化收益÷12*月数；
				earnings = AverageCapitalPlusInterestUtils.getInterestCount(new BigDecimal(money), borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
				earnings = AverageCapitalUtils.getInterestCount(new BigDecimal(money), borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
				break;
			default:
				break;
			}
			_log.info("本金收益："+earnings);
			if (couponConfig != null && couponConfig.getCouponType() == 3) {
                investInfo.setCapitalInterest(df.format(earnings.add(couponConfig.getCouponQuota()).subtract(couponInterest)));
			} else if (couponConfig != null && couponConfig.getCouponType() == 1) {
			    earnings = earnings.add(couponInterest);
				investInfo.setCapitalInterest(df.format(earnings));
			} else {
				investInfo.setCapitalInterest(df.format(earnings.subtract(couponInterest)));
			}
			_log.info("本金+优惠券收益："+earnings);
			// 产品加息预期收益
			if (Validator.isIncrease(borrow.getIncreaseInterestFlag(), borrow.getBorrowExtraYield())) {
			    if (couponConfig != null && couponConfig.getCouponType() == 3){
			        money = new BigDecimal(money).subtract(couponConfig.getCouponQuota()).toString();
			    }
			    BigDecimal incEarnings = tenderService.increaseCalculate(borrowPeriod, borrowStyle, money, borrow.getBorrowExtraYield());
			    earnings = earnings.add(incEarnings);
			}
			_log.info("本金+优惠券+加息收益："+earnings);
			investInfo.setEarnings(df.format(earnings));
			investInfo.setCouponConfig(couponConfig);
			investInfo.setStatus(true);
			investInfo.setMessage("历史回报计算完成");
		} else {
			investInfo.setStatus(false);
			investInfo.setMessage("请填写正确的出借金额");
		}
		return investInfo;
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

	private BigDecimal getInterestDj(BigDecimal couponQuota, Integer couponProfitTime, BigDecimal borrowApr) {
		BigDecimal earnings = new BigDecimal("0");

		earnings = couponQuota.multiply(borrowApr.divide(new BigDecimal(100), 6, BigDecimal.ROUND_HALF_UP)).divide(new BigDecimal(365), 6, BigDecimal.ROUND_HALF_UP)
				.multiply(new BigDecimal(couponProfitTime)).setScale(2, BigDecimal.ROUND_DOWN);

		return earnings;

	}

	/**
	 * pc出借验证
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = TenderDefine.INVEST_CHECK_ACTION, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public WebBaseAjaxResultBean tenderCheck(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(TenderController.class.toString(), TenderDefine.INVEST_ACTION);
		String borrowNid = request.getParameter("nid");
		String couponGrantId = request.getParameter("couponGrantId");
		// 出借金额
		String money = request.getParameter("money");
		if (money == null || "".equals(money)) {
			money = "0";
		}
		Integer userId = WebUtils.getUserId(request);
		_log.info("tenderCheck ShiroUtil.getLoginUserId--------------------" + userId);
		JSONObject info = tenderService.checkParam(borrowNid, money, userId, "0", couponGrantId);
		// 参数验证成功，则返回url，否则不返回
		WebBaseAjaxResultBean result = new WebBaseAjaxResultBean();
        // TODO: 2018/10/13  出借开始
		if (info == null) {
			result.setStatus(false);
			result.setMessage("出借失败");
		} else if (info.get("error").equals("0")) {
			if (StringUtils.isNotEmpty(couponGrantId)) {
				try {
					// 校验优惠券
					JSONObject couponCheckResult = CommonSoaUtils.CheckCoupon(userId + "", borrowNid, money, CustomConstants.CLIENT_PC, couponGrantId);
					int couponStatus = couponCheckResult.getIntValue("status");
					String couponDesc = couponCheckResult.getString("statusDesc");
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
			} else {
				result.setStatus(true);
				result.setMessage("操作成功");
			}
		} else {
			result.setStatus(false);
			result.setMessage(info.getString("data"));
		}
		// 获得错误码
		String errorCode = tenderService.checkErrCode(userId);
		_log.info("===========cwyang 返回errorCode = " + errorCode);
		_log.info("==============cwyang info is" + info);
		//从user中获取客户类型，ht_user_evalation_result（用户测评总结表）
		UserEvalationResultCustomize userEvalationResultCustomize = evaluationService.selectUserEvalationResultByUserId(userId);
		if(userEvalationResultCustomize != null){
			EvaluationConfig evalConfig = new EvaluationConfig();
			//1.散标／债转出借者测评类型校验
			String debtEvaluationTypeCheck = "0";
			//2.散标／债转单笔投资金额校验
			String deptEvaluationMoneyCheck = "0";
			//3.散标／债转待收本金校验
			String deptCollectionEvaluationCheck = "0";
			//4.智投出借者测评类型校验
			// String intellectualEveluationTypeCheck = "0";
			//5.智投单笔投资金额校验
			// String intellectualEvaluationMoneyCheck = "0";
			//6.智投待收本金校验
			// String intellectualCollectionEvaluationCheck = "0";
			//获取开关信息
			List<EvaluationConfig> evalConfigList = evaluationService.selectEvaluationConfig(evalConfig);
			if(evalConfigList != null && evalConfigList.size() > 0){
				evalConfig = evalConfigList.get(0);
				//1.散标／债转出借者测评类型校验
				debtEvaluationTypeCheck = evalConfig.getDebtEvaluationTypeCheck() == null ? "0" : String.valueOf(evalConfig.getDebtEvaluationTypeCheck());
				//2.散标／债转单笔投资金额校验
				deptEvaluationMoneyCheck = evalConfig.getDeptEvaluationMoneyCheck() == null ? "0" : String.valueOf(evalConfig.getDeptEvaluationMoneyCheck());
				//3.散标／债转待收本金校验
				deptCollectionEvaluationCheck = evalConfig.getDeptCollectionEvaluationCheck() == null ? "0" : String.valueOf(evalConfig.getDeptCollectionEvaluationCheck());
				//4.智投出借者测评类型校验
				// intellectualEveluationTypeCheck = evalConfig.getIntellectualEveluationTypeCheck() == null ? "0" : String.valueOf(evalConfig.getIntellectualEveluationTypeCheck());
				//5.智投单笔投资金额校验
				// intellectualEvaluationMoneyCheck = evalConfig.getIntellectualEvaluationMoneyCheck() == null ? "0" : String.valueOf(evalConfig.getIntellectualEvaluationMoneyCheck());
				//6.智投待收本金校验
				// intellectualCollectionEvaluationCheck = evalConfig.getIntellectualCollectionEvaluationCheck() == null ? "0" : String.valueOf(evalConfig.getIntellectualCollectionEvaluationCheck());
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
				if("".equals(errorCode)){
					if(CustomConstants.EVALUATION_CHECK.equals(debtEvaluationTypeCheck)){
						//计划类判断用户类型为稳健型以上才可以投资
						Borrow borrow = tenderService.getBorrowByNid(borrowNid);
						if(borrow != null){
							if(!CommonUtils.checkStandardInvestment(eval_type,"BORROW_SB",borrow.getInvestLevel())){
								//返回错误码
								result.setErrorCode(CustomConstants.BANK_TENDER_RETURN_CUSTOMER_STANDARD_FAIL);
								result.setStatus(false);
								//返回类型（用户类型）
								result.setInvestLevel(eval_type);
                                //返回类型（配置类型）
                                result.setEvalFlagType(borrow.getInvestLevel());
								result.setMessage("您的风险等级为 #"+eval_type+"# \\n达到 #"+borrow.getInvestLevel()+"# 及以上才可以出借此项目");
								LogUtil.endLog(TenderController.class.toString(), TenderDefine.INVEST_ACTION);
								return result;
							}
						}
					}
					if(revaluation_money == null){
						_log.info("=============从redis中获取测评类型和上限金额异常!(没有获取到对应类型的限额数据) eval_type="+eval_type);
					}else {
						if(CustomConstants.EVALUATION_CHECK.equals(deptEvaluationMoneyCheck)){
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
						if(CustomConstants.EVALUATION_CHECK.equals(deptCollectionEvaluationCheck)){
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
			}
		}else{
			_log.info("=============该用户测评总结数据为空! userId="+userId);
		}

		result.setErrorCode(errorCode);
		if (StringUtils.isNotEmpty(errorCode)) {
			result.setStatus(false);
		}
		LogUtil.endLog(TenderController.class.toString(), TenderDefine.INVEST_ACTION);
		return result;
	}

	/**
	 * 出借
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(TenderDefine.INVEST_ACTION)
	public ModelAndView tender(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(TenderController.class.toString(), TenderDefine.INVEST_ACTION);
		// add by zhangjp 优惠券出借 start
		// 优惠券用户id （coupon_user的id）
		String couponGrantId = request.getParameter("couponGrantId");
		if (couponGrantId == null) {
			couponGrantId = "";
		}
		Integer userId = WebUtils.getUserId(request);
		// 排他校验时间
		int couponOldTime = Integer.MIN_VALUE;
		CouponConfigCustomizeV2 cuc = null;
		// -1:有可用的优惠券，但是出借时不选择优惠券 空或null：用户没有可用优惠券
		if (StringUtils.isNotEmpty(couponGrantId) && !StringUtils.equals("-1", couponGrantId)) {
			cuc = tenderService.getCouponUser(couponGrantId, userId);
			// 排他check用
			couponOldTime = cuc.getUserUpdateTime();
		}
		// add by zhangjp 优惠券出借 end

		ModelAndView modelAndView = new ModelAndView();
		// 借款borrowNid,如HBD120700101
		String borrowNid = request.getParameter("nid");
		Borrow borrow = tenderService.getBorrowByNid(borrowNid);
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrow.getBorrowStyle()) || CustomConstants.BORROW_STYLE_MONTH.equals(borrow.getBorrowStyle())
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrow.getBorrowStyle())|| CustomConstants.BORROW_STYLE_END.equals(borrow.getBorrowStyle());

		String lockPeriod = String.valueOf(borrow.getBorrowPeriod());
		String dayOrMonth ="";
		if(isMonth){//月标
			dayOrMonth = lockPeriod + "个月散标";
		}else{
			dayOrMonth = lockPeriod + "天散标";
		}
		WebViewUser user = WebUtils.getUser(request);
		UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
		userOperationLogEntity.setOperationType(4);
		userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
		userOperationLogEntity.setPlatform(0);
		userOperationLogEntity.setRemark(dayOrMonth);
		userOperationLogEntity.setOperationTime(new Date());
		userOperationLogEntity.setUserName(user.getUsername());
		userOperationLogEntity.setUserRole(user.getRoleId());
		couponService.sendUserLogMQ(userOperationLogEntity);
		// 出借金额
		String accountStr = request.getParameter("money");
		if (accountStr == null || "".equals(accountStr)) {
			accountStr = "0";
		}
		
		_log.info("出借开始，userId:" + userId + ", borrowNid:" + borrowNid + ", tenderMoney:" + accountStr + ", couponGrantId:" + couponGrantId);
		
		BigDecimal decimalAccount = StringUtils.isNotEmpty(accountStr) ? new BigDecimal(accountStr) : BigDecimal.ZERO;
		if (decimalAccount.compareTo(BigDecimal.ZERO) != 1 && cuc != null && (cuc.getCouponType() == 3 || cuc.getCouponType() == 1)) {
			_log.info("cuc.getCouponType():" + cuc.getCouponType());
			this.couponTender(modelAndView, request, cuc, userId, couponOldTime);
			return modelAndView;
		}
		JSONObject result = tenderService.checkParam(borrowNid, accountStr, userId, "0", couponGrantId);
		if (result == null) {
			modelAndView = new ModelAndView(TenderDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("investDesc", "出借失败！");
			modelAndView.addObject("borrowNid",borrowNid);
			return modelAndView;
		} else if (result.get("error") != null && result.get("error").equals("1")) {
			_log.info(result.toJSONString());
			modelAndView = new ModelAndView(TenderDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("investDesc", result.get("data") + "");
			modelAndView.addObject("borrowNid",borrowNid);
			return modelAndView;
		}
		String account = accountStr;
		String tenderUsrcustid = result.getString("tenderUsrcustid");
		String tenderUserName = result.getString("tenderUserName");
		// 生成订单
		String orderId = GetOrderIdUtils.getOrderId2(userId);
		// 写日志
		Boolean flag = false;
		try {
			flag = tenderService.updateTenderLog(borrowNid, orderId, userId, account, GetCilentIP.getIpAddr(request), couponGrantId, tenderUserName);
		} catch (Exception e1) {
			_log.info("===========写入日志失败!=================");
		}
		// 成功后调用出借接口
		if (flag) {
			// 获取共同参数
			String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
			String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
			// 回调路径
			String retUrl = PropUtils.getSystem("hyjf.web.host").trim() + TenderDefine.REQUEST_MAPPING + TenderDefine.RETURL_SYN_ACTION + ".do?couponGrantId=" + couponGrantId + "&couponOldTime="
					+ couponOldTime;
			// 商户后台应答地址(必须)
			String notifyUrl = PropUtils.getSystem("http.hyjf.web.host").trim() + TenderDefine.REQUEST_MAPPING + TenderDefine.RETURL_ASY_ACTION + ".do?couponGrantId=" + couponGrantId
					+ "&couponOldTime=" + couponOldTime;
			BankCallBean bean = new BankCallBean();
			bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
			bean.setTxCode(BankCallConstant.TXCODE_BID_APPLY);// 交易代码
			bean.setInstCode(instCode);
			bean.setBankCode(bankCode);
			bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
			bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
			bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
			bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
			bean.setAccountId(tenderUsrcustid);// 电子账号
			bean.setOrderId(orderId);// 订单号
			bean.setTxAmount(CustomUtil.formatAmount(account));// 交易金额
			bean.setProductId(borrowNid);// 标的号
			bean.setFrzFlag(BankCallConstant.DEBT_FRZFLAG_UNFREEZE);// 是否冻结金额  modify by cwyang 2017-10-25 实时放款出借不冻结
			bean.setForgotPwdUrl(CustomConstants.FORGET_PASSWORD_URL);
			bean.setSuccessfulUrl(retUrl+"&isSuccess=1");// 银行同步返回地址
			bean.setRetUrl(retUrl);// 银行同步返回地址
			bean.setNotifyUrl(notifyUrl);// 银行异步返回地址
			bean.setLogOrderId(orderId);// 订单号
			bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单日期
			bean.setLogIp(GetCilentIP.getIpAddr(request));// 客户IP
			bean.setLogUserId(String.valueOf(userId));// 出借用户
			bean.setLogUserName(tenderUserName);// 出借用户名
			bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_MOBILE_BIDAPPLY);// 银行请求详细url
			try {
				modelAndView = BankCallUtils.callApi(bean);
			} catch (Exception e) {
				_log.info("==========调用出借银行接口异常.");
				modelAndView = new ModelAndView(TenderDefine.INVEST_ERROR_PATH);
				modelAndView.addObject("investDesc", "出借失败！");
				modelAndView.addObject("borrowNid",borrowNid);
			}
		} else {
			modelAndView = new ModelAndView(TenderDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("investDesc", "出借失败！");
			modelAndView.addObject("borrowNid",borrowNid);
		}
		LogUtil.endLog(TenderController.class.toString(), TenderDefine.INVEST_ACTION);
		return modelAndView;
	}

	// 出借同步回调
	@RequestMapping(TenderDefine.RETURL_SYN_ACTION)
	public ModelAndView tenderRetUrl(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BankCallBean bean) {
		_log.info("开始调用出借同步方法");
		ModelAndView modelAndView = new ModelAndView();
		String logOrderId = StringUtils.isBlank(bean.getLogOrderId()) ? "" : bean.getLogOrderId();// 订单号
		int userId = StringUtils.isBlank(bean.getLogUserId()) ? 0 : Integer.parseInt(bean.getLogUserId());// 用户Userid
		String ip = bean.getUserIP();// 用户操作ip
		String couponGrantId = request.getParameter("couponGrantId");// 优惠券用户id
		String couponOldTime = StringUtils.isNotEmpty(request.getParameter("couponOldTime")) ? request.getParameter("couponOldTime") : "0";
		String respCode = bean.getRetCode();// 出借结果返回码
		String isSuccess = request.getParameter("isSuccess");
		String accountId = StringUtils.isBlank(bean.getAccountId()) ? "" : bean.getAccountId();// 电子账号
		_log.info("PC用户:" + userId + "**isSuccess：" + isSuccess);
		// 打印返回码
		_log.info("PC用户:" + userId + "***出借接口结果码：" + respCode);
		String message = "出借失败！";// 错误信息
		if (StringUtils.isBlank(respCode)||!"1".equals(isSuccess)) {
			    //测试有出现过retCode是空的情况，判断一下，增强代码健壮性
				// BorrowTender borrowTender = this.investService.getTenderByNid(logOrderId);
				// 有出借记录，则返回出借成功
				if (!isTenderSuccess(userId, accountId, logOrderId)) {
					_log.info("PC用户:" + userId + "*出借失败！");
						modelAndView = new ModelAndView(TenderDefine.INVEST_ERROR_PATH);
						modelAndView.addObject("investDesc", "出借失败！");
						return modelAndView;
					
				} else {
					_log.info("PC用户:" + userId + "*出借成功！");
					respCode = BankCallConstant.RESPCODE_SUCCESS;
				}
		}
		if (!BankCallConstant.RESPCODE_SUCCESS.equals(respCode)&&!"1".equals(isSuccess)) {
			// 返回码提示余额不足
			if (BankCallConstant.RETCODE_BIDAPPLY_YUE_FAIL.equals(respCode)) {
				_log.info("PC用户:" + userId + "**出借接口调用失败，余额不足，错误码：" + respCode);
				message = "出借失败，可用余额不足！请联系客服.";
				modelAndView = new ModelAndView(TenderDefine.INVEST_ERROR_PATH);
				modelAndView.addObject("investDesc", message);
				return modelAndView;
			} else {
				message = bean.getRetMsg();
				_log.info("PC用户:" + userId + "**出借接口调用失败,系统订单号：" + logOrderId + "**调用解冻接口**汇付接口返回错误码" + respCode);
				modelAndView = new ModelAndView(TenderDefine.INVEST_ERROR_PATH);
				modelAndView.addObject("investDesc", message);
				return modelAndView;
			}
		}
		bean.convert();
		String borrowId = bean.getProductId();// 借款Id
		String account = bean.getTxAmount();// 借款金额
		String orderId = bean.getOrderId();// 订单id
		// 根据借款Id检索标的信息
		// modify by cwyang borrowid 换成borrowNid
		BorrowWithBLOBs borrow = this.tenderService.selectBorrowByNid(borrowId);
		String borrowNid = borrowId == null ? "" : borrow.getBorrowNid();// 项目编号
		_log.info("出借同步回调" + bean.getAllParams().toString());
		_log.info("标号:" + borrowNid);
		if (StringUtils.isBlank(borrowNid)) {
			modelAndView = new ModelAndView(TenderDefine.INVEST_ERROR_PATH);
			message = "回调时,borrowNid为空";
			modelAndView.addObject("investDesc", message);
			return modelAndView;
		}
		// 优惠券出借结果
		JSONObject couponTender = null;
		// 出借状态
		String status = BankCallConstant.STATUS_FAIL;
		// 增加操作标示,用来区分同步是否进行过优惠券出借 add by cwyang 2017-5-12
		try {
			// 进行出借 tendertmp锁住
			JSONObject tenderResult = this.tenderService.userTender(borrow, bean);
			// 出借成功
			if (tenderResult.getString("status").equals("1")) {
				_log.info("PC用户:" + userId + "***同步出借成功：" + account);
				message = "恭喜您出借成功!";
				status = BankCallConstant.STATUS_SUCCESS;
				if (StringUtils.isNotEmpty(couponGrantId)) {
					// 优惠券出借校验
					try {
						// 优惠券出借校验
						JSONObject couponCheckResult = CommonSoaUtils.CheckCoupon(userId + "", borrowNid, account, CustomConstants.CLIENT_PC, couponGrantId);
						int couponStatus = couponCheckResult.getIntValue("status");
						String statusDesc = couponCheckResult.getString("statusDesc");
						_log.info("updateCouponTender" + "优惠券出借校验结果：" + statusDesc);
						if (couponStatus == 0) {
							// 优惠券出借
							//couponTender = CommonSoaUtils.CouponInvestForPC(userId + "", borrowNid, account, CustomConstants.CLIENT_PC, couponGrantId, orderId, ip, couponOldTime);
						    // update by pcc 放入汇直投优惠券使用mq队列 start
							 LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender", "异步方法放入汇直投优惠券使用mq队列：");

                             Map<String, String> params = new HashMap<String, String>();
                             params.put("mqMsgId", GetCode.getRandomCode(10));
                             // 真实出借金额
                             params.put("money", account);
                             // 借款项目编号
                             params.put("borrowNid", borrowNid);
                             // 平台
                             params.put("platform", CustomConstants.CLIENT_PC);
                             // 优惠券id
                             params.put("couponGrantId", couponGrantId);
                             // ip
                             params.put("ip", ip);
                             // 真实出借订单号
                             params.put("ordId", orderId);
                             // 优惠券修改时间
                             params.put("couponOldTime", couponOldTime+"");
                             // 用户编号
                             params.put("userId", userId+"");
                             rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_HZT_COUPON_TENDER, JSONObject.toJSONString(params));
                             // update by pcc 放入汇直投优惠券使用mq队列 end
						}
					} catch (Exception e) {
						LogUtil.infoLog(TenderController.class.getName(), "tenderRetUrl", "优惠券出借失败");
					}
				}
			}
			// 出借失败 回滚redis
			else {
				// 更新tendertmp
				boolean updateFlag = tenderService.updateBorrowTenderTmp(userId, borrowNid, orderId);
				// 更新失败，出借失败
				if (updateFlag) {
					if(tenderResult.getString("status").equals("-1")){//同步/异步 优先执行完毕
						_log.info("PC用户:" + userId + "***同步代码中异步优先出借成功：" + account + "，标的号：" + borrowNid);
						//add by cwyang 增加redis防重校验 2017-08-02
						boolean checkTender = RedisUtils.tranactionSet("tendersuccess_orderid" + orderId, 20);
						_log.info("同步PC用户:checkTender" + checkTender);
						if(!checkTender){//同步/异步 优先执行完毕
							_log.info("PC用户:" + userId + "***同步代码中异步优先出借成功后，成功Redis标识置为成功！：" + account + "，标的号：" + borrowNid);
							message = "恭喜您出借成功!";
							status = BankCallConstant.STATUS_SUCCESS;
						}else{
							_log.info("PC用户:" + userId + "***同步代码中异步优先出借成功后，成功Redis标识未置为成功！：" + account + "，标的号：" + borrowNid);
							message = "投标失败,请联系客服人员!";
						}
						//end
					}else{
						// 出借失败,出借撤销
						try {
							boolean flag = tenderService.bidCancel(userId, borrowId, orderId, account);
							if (!flag) {
								message = "投标失败,请联系客服人员!";
							}
						} catch (Exception e) {
							_log.info("==========投标申请撤销异常,orderId is " + orderId);
							message = "投标申请撤销失败,请联系客服人员!";
						}
					}
					//end
				} else {
					//异步优先执行，并且出借失败会执行撤销操作，并删除temp表导致走到此分支，所以需要通过成功的Redis来判断是否出借成功 add by cwyang
					boolean checkTender = RedisUtils.tranactionSet("tendersuccess_orderid" + orderId, 20);
					if(!checkTender){//同步/异步 优先执行完毕
						message = "恭喜您出借成功!";
						status = BankCallConstant.STATUS_SUCCESS;
					}else{
						message = "投标失败,请联系客服人员!";
					}
				}
			}
		} catch (Exception e) {
			// 更新tendertmp
			boolean updateFlag = tenderService.updateBorrowTenderTmp(userId, borrowNid, orderId);
			// 更新成功，出借失败
			if (updateFlag) {
				// 出借失败,出借撤销
				try {
					boolean flag = tenderService.bidCancel(userId, borrowId, orderId, account);
					if (!flag) {
						message = "投标失败,请联系客服人员!";
					}
				} catch (Exception e1) {
					_log.info("========投标申请撤销异常!");
					message = "投标申请撤销失败,请联系客服人员!";
				}
			} else {
				message = "恭喜您出借成功!";
				status = BankCallConstant.STATUS_SUCCESS;
			}
		}

		if (status.equals(BankCallConstant.STATUS_FAIL)) {
			modelAndView = new ModelAndView(TenderDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("investDesc", message);
			modelAndView.addObject("projectType", borrow.getProjectType());
			modelAndView.addObject("borrowNid",borrowNid);
		} else {
			modelAndView = new ModelAndView(TenderDefine.INVEST_SUCCESS_PATH);
			LogUtil.endLog(TenderDefine.class.toString(), TenderDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
			DecimalFormat df = CustomConstants.DF_FOR_VIEW;
			// 取得项目类别
			BorrowProjectType borrowProjectType = tenderService.getBorrowProjectType(borrow.getProjectType().toString());
			/** 修改出借成功页面显示修改开始 */
			String projectTypeName = getProjectTypeName(borrowProjectType);
			// 判断一下优惠券是否已被使用(由于异步调用可能在同步调用之前执行,导致无法获得优惠券的使用情况,所以这里需要重新获取一下)并且同步调用未进行优惠券出借
			if (StringUtils.isNotEmpty(couponGrantId)) {
				_log.info("==================cwyang 异步调用优先执行,重新获取优惠券信息.============");
				int index=0;
                do {
                    try {
                        Thread.sleep(500);
                        couponTender = tenderService.getCouponIsUsed(orderId,couponGrantId, userId);
                        if (!CustomConstants.RESULT_SUCCESS.equals(couponTender.getString("isSuccess"))) {
                            _log.info("====================cwyang 获取优惠券信息失败!==================");
                            couponTender = null;
                            index++;
                        }
                    } catch (Exception e) {
                        logger.error("获取优惠券信息异常!", e);
                    }  
                } while (couponTender==null&&index<3);
			}
			if (Validator.isNotNull(couponTender)) {
				int couponStatus = Integer.parseInt(couponTender.getString("status"));
				if (couponStatus == 0) {
					// 优惠券面值
					String couponQuota = couponTender.getString("couponQuota");
					// 优惠券收益率
					// 优惠券类别
					int couponType = couponTender.getIntValue("couponTypeInt");
					// 加息券收益
					String couponInterest = couponTender.getString("couponInterest");
					if (StringUtils.isNotEmpty(couponInterest)) {
						// 历史回报
						modelAndView.addObject("couponInterest", couponInterest);
					}
					if (StringUtils.isNotEmpty(couponQuota)) {
						// 优惠券面值
						modelAndView.addObject("couponQuota", couponQuota);
					}
					if (couponType != Integer.MIN_VALUE) {
						// 优惠券类别
						modelAndView.addObject("couponType", couponType);
					}
				}
			}
			String interest = null;
			String borrowStyle = borrow.getBorrowStyle();// 项目还款方式
			Integer borrowPeriod = borrow.getBorrowPeriod();// 周期
			BigDecimal borrowApr = borrow.getBorrowApr();// 項目预期年化收益率
			// mod by nxl Start设置项目加息收益 end
			BigDecimal earnings = new BigDecimal("0");
			// 计算历史回报
			switch (borrowStyle) {
			case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
				earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(account), borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
				interest = df.format(earnings);
				break;
			case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷360*天数；
				earnings = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(account), borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
				interest = df.format(earnings);
				break;
			case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：历史回报=出借金额*年化收益÷12*月数；
				earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(account), borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2,
						BigDecimal.ROUND_DOWN);
				interest = df.format(earnings);
				break;
			case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：历史回报=出借金额*年化收益÷12*月数；
				earnings = AverageCapitalPlusInterestUtils.getInterestCount(new BigDecimal(account), borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
				interest = df.format(earnings);
				break;
			case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
				earnings = AverageCapitalUtils.getInterestCount(new BigDecimal(account), borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
				interest = df.format(earnings);
				break;
			default:
				break;
			}
			// 产品加息预期收益
            if (Validator.isIncrease(borrow.getIncreaseInterestFlag(), borrow.getBorrowExtraYield())) {
                BigDecimal incEarnings = tenderService.increaseCalculate(borrowPeriod, borrowStyle, account, borrow.getBorrowExtraYield());
                earnings = incEarnings.add(earnings);
            }
			if (StringUtils.isNotBlank(interest)) {
			    if (Validator.isNotNull(couponTender)) {
			        modelAndView.addObject("interest", df.format(earnings.add(new BigDecimal(couponTender.getString("couponEarnings")))));
			    }else{
			        modelAndView.addObject("interest", df.format(earnings));
			    }
			}
			modelAndView.addObject("borrowNid", borrow.getBorrowNid());
			modelAndView.addObject("projectTypeName", projectTypeName);
			modelAndView.addObject("account", df.format(new BigDecimal(account)));
			modelAndView.addObject("investDesc", message);
			modelAndView.addObject("projectType", borrow.getProjectType());
			if (StringUtils.isNotEmpty(borrow.getBorrowAssetNumber())) {
				modelAndView.addObject("borrowAssetNumber", borrow.getBorrowAssetNumber());
			} else {
				modelAndView.addObject("borrowAssetNumber", "");
			}
		}
		return modelAndView;

	}
	
	private boolean isTenderSuccess(int userId, String accountId, String orderId) {
		// 获取共同参数
		String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
		String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
		String channel = BankCallConstant.CHANNEL_APP;
		// 查询相应的债权状态
		try {
			String logOrderId = GetOrderIdUtils.getOrderId2(userId);
			String orderDate = GetOrderIdUtils.getOrderDate();
			String txDate = GetOrderIdUtils.getTxDate();
			String txTime = GetOrderIdUtils.getTxTime();
			String seqNo = GetOrderIdUtils.getSeqNo(6);
			// 调用投标申请查询接口
			BankCallBean bankCallBean = new BankCallBean();
			bankCallBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
			bankCallBean.setTxCode(BankCallConstant.TXCODE_BID_APPLY_QUERY);// 消息类型(批量还款)
			bankCallBean.setInstCode(instCode);// 机构代码
			bankCallBean.setBankCode(bankCode);
			bankCallBean.setTxDate(txDate);
			bankCallBean.setTxTime(txTime);
			bankCallBean.setSeqNo(seqNo);
			bankCallBean.setChannel(channel);
			bankCallBean.setAccountId(accountId);
			bankCallBean.setOrgOrderId(orderId);
			bankCallBean.setLogUserId(String.valueOf(userId));
			bankCallBean.setLogOrderId(logOrderId);
			bankCallBean.setLogOrderDate(orderDate);
			bankCallBean.setLogRemark("查询债权状态请求");
			bankCallBean.setLogClient(0);
			BankCallBean statusResult = BankCallUtils.callApiBg(bankCallBean);
			_log.info("用户："+userId + "调用出借结果statusResult"+statusResult.toString());
			if (Validator.isNotNull(statusResult)) {
				String retCode = StringUtils.isNotBlank(statusResult.getRetCode()) ? statusResult.getRetCode() : "";
				if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
					if(StringUtils.isNotBlank(statusResult.getState()) && !"9".equals(statusResult.getState())){
						return true;
					}
				}
			}
		} catch (Exception e) {
			logger.error("查询债权异常...", e);
		}
		return false;
	}

	// 出借异步回调
	@ResponseBody
	@RequestMapping(TenderDefine.RETURL_ASY_ACTION)
	public String tenderBigRetUrl(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BankCallBean bean) {
		_log.info("开始调用出借异步方法");
		int userId = StringUtils.isBlank(bean.getLogUserId()) ? 0 : Integer.parseInt(bean.getLogUserId());// 用户Userid
		String ip = bean.getUserIP();// 用户操作ip
		String couponGrantId = request.getParameter("couponGrantId");// 优惠券用户id
		String couponOldTime = StringUtils.isNotEmpty(request.getParameter("couponOldTime")) ? request.getParameter("couponOldTime") : "0";
		String respCode = bean.getRetCode();// 出借结果返回码
		BankCallResult result = new BankCallResult();
		String message = "出借失败！";// 错误信息
		if (StringUtils.isBlank(respCode)) {
			result.setMessage(message);
			return JSONObject.toJSONString(result, true);
		}
		if (!BankCallConstant.RESPCODE_SUCCESS.equals(respCode)) {
			// 返回码提示余额不足，不结冻
			if (BankCallConstant.RETCODE_BIDAPPLY_YUE_FAIL.equals(respCode)) {
				_log.info("PC用户:" + userId + "**出借接口调用失败，余额不足，错误码：" + respCode);
				message = "出借失败，可用余额不足！请联系客服.";
				result.setMessage(message);
				return JSONObject.toJSONString(result, true);
			} else {
				message = bean.getRetMsg();
				result.setMessage(message);
				return JSONObject.toJSONString(result, true);
			}
		}
		bean.convert();
		String borrowId = bean.getProductId();// 借款Id
		String account = bean.getTxAmount();// 借款金额
		String orderId = bean.getOrderId();// 订单id
		// 根据借款Id检索标的信息
		BorrowWithBLOBs borrow = this.tenderService.getBorrowByNid(borrowId);
		String borrowNid = borrowId == null ? "" : borrow.getBorrowNid();
		_log.info("出借异步回调" + bean.getAllParams().toString());
		_log.info("标号:" + borrowNid);
		if (StringUtils.isBlank(borrowNid)) {
			message = "回调时,borrowNid为空";
			result.setMessage(message);
			return JSONObject.toJSONString(result, true);
		}
		try {
			// 进行出借, tendertmp锁住
			JSONObject tenderResult = this.tenderService.userTender(borrow, bean);
			// 出借成功
			if (tenderResult.getString("status").equals("1")) {
				_log.info("PC用户:" + userId + "***异步出借成功：" + account + "，标的号：" + borrowNid);
				message = "恭喜您出借成功!";
				if (StringUtils.isNotEmpty(couponGrantId)) {
					// 优惠券出借校验
					try {
						// 优惠券出借校验
						JSONObject couponCheckResult = CommonSoaUtils.CheckCoupon(userId + "", borrowNid, account, CustomConstants.CLIENT_PC, couponGrantId);
						int couponStatus = couponCheckResult.getIntValue("status");
						String statusDesc = couponCheckResult.getString("statusDesc");
						_log.info("updateCouponTender" + "优惠券出借校验结果：" + statusDesc);
						if (couponStatus == 0) {
							// 优惠券出借
							//CommonSoaUtils.CouponInvestForPC(userId + "", borrowNid, account, CustomConstants.CLIENT_PC, couponGrantId, orderId, ip, couponOldTime);
							// update by pcc 放入汇直投优惠券使用mq队列 start
                            LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender", "异步方法放入汇直投优惠券使用mq队列：");

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("mqMsgId", GetCode.getRandomCode(10));
                            // 真实出借金额
                            params.put("money", account);
                            // 借款项目编号
                            params.put("borrowNid", borrowNid);
                            // 平台
                            params.put("platform", CustomConstants.CLIENT_PC);
                            // 优惠券id
                            params.put("couponGrantId", couponGrantId);
                            // ip
                            params.put("ip", ip);
                            // 真实出借订单号
                            params.put("ordId", orderId);
                            // 优惠券修改时间
                            params.put("couponOldTime", couponOldTime+"");
                            // 用户编号
                            params.put("userId", userId+"");
                            rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_HZT_COUPON_TENDER, JSONObject.toJSONString(params));
                            // update by pcc 放入汇直投优惠券使用mq队列 end
						}
					} catch (Exception e) {
						LogUtil.infoLog(TenderController.class.getName(), "tenderRetUrl", "优惠券出借失败");
					}
				}
			}
			// 出借失败 回滚redis
			else {
				// 更新tendertmp
				boolean updateFlag = tenderService.updateBorrowTenderTmp(userId, borrowNid, orderId);
				_log.info("PC用户:updateFlag:" + updateFlag);
				// 更新失败，出借失败
				if (updateFlag) {
					if(tenderResult.getString("status").equals("-1")){//同步/异步 优先执行完毕
						_log.info("PC用户:" + userId + "***异步代码中同步优先出借成功：" + account + "，标的号：" + borrowNid);
						message = "恭喜您出借成功!";
						//add by cwyang 增加redis防重校验 2017-08-02
						boolean checkTender = RedisUtils.tranactionSet("tendersuccess_orderid" + orderId, 20);
						_log.info("异步PC用户:checkTender" + checkTender);
						if(!checkTender){//同步/异步 优先执行成功!
							boolean updateTenderFlag = this.tenderService.updateTender(userId, borrowNid, orderId, bean);
							if (!updateTenderFlag) {
								message = "投标出現错误,请联系客服人员!";
							}
						}else{
							_log.info("PC用户:" + userId + "***异步代码中同步优先出借成功,redis标识未设置成功！" + account + "，标的号：" + borrowNid);
							message = "投标失败,请联系客服人员!";
						}
						//end
					}else{
						// 出借失败,出借撤销
						try {
							boolean flag = tenderService.bidCancel(userId, borrowId, orderId, account);
							if (!flag) {
								message = "投标失败,请联系客服人员!";
							}
						} catch (Exception e) {
							_log.info("==========投标申请撤销异常==========orderid is " + orderId);
							message = "投标申请撤销失败,请联系客服人员!";
						}
					}
				} else {
					message = "恭喜您出借成功!";
					boolean updateTenderFlag = this.tenderService.updateTender(userId, borrowNid, orderId, bean);
					if (!updateTenderFlag) {
						message = "投标出現错误,请联系客服人员!";
					}
				}
			}
		} catch (Exception e) {
			// 更新tendertmp
			boolean updateFlag = tenderService.updateBorrowTenderTmp(userId, borrowNid, orderId);
			// 更新失败，出借失败
			if (updateFlag) {
				// 出借失败,出借撤销
				try {
					boolean flag = tenderService.bidCancel(userId, borrowId, orderId, account);
					if (!flag) {
						message = "投标失败,请联系客服人员!";
					}
				} catch (Exception e1) {
					_log.info("=================投标申请撤销异常!=====");
					message = "投标申请撤销失败,请联系客服人员!";
				}
			} else {
				message = "恭喜您出借成功!";
				boolean updateTenderFlag = this.tenderService.updateTender(userId, borrowNid, orderId, bean);
				if (!updateTenderFlag) {
					message = "投标出現错误,请联系客服人员!";
				}
			}
		}
		result.setStatus(true);
		return JSONObject.toJSONString(result, true);
	}

	/**
	 * 获取相应的项目名称
	 * 
	 * @param borrowProjectType
	 * @return
	 */
	private String getProjectTypeName(BorrowProjectType borrowProjectType) {
		// 项目类型
		return tenderService.getProjectTypeName(borrowProjectType);
	}

	/**
	 * 获取最优优惠券
	 * 
	 * @param borrowNid
	 * @param userId
	 * @param money
	 * @param platform
	 * @return
	 */
	private UserCouponConfigCustomize getBestCoupon(String borrowNid, Integer userId, String money, String platform) {
		if (money == null || "".equals(money) || money.length() == 0) {
			money = "0";
		}
		return tenderService.getBestCoupon(borrowNid, userId, money, platform);
	}

	/**
	 * 获取最优的优惠券
	 * 
	 * @param couponId
	 * @return
	 */
	private UserCouponConfigCustomize getBestCouponById(String couponId) {
		return tenderService.getBestCouponById(couponId);
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

		// 金额
		String account = request.getParameter("money");
		String ip = GetCilentIP.getIpAddr(request);
		String couponGrantId = request.getParameter("couponGrantId");
		String borrowNid = request.getParameter("nid");
		if (account == null || "".equals(account)) {
			account = "0";
		}
		// 根据项目编号获取相应的项目
		Borrow borrow = tenderService.getBorrowByNid(borrowNid);

		// 优惠券出借校验
		JSONObject couponCheckResult = CommonSoaUtils.CheckCoupon(userId + "", borrowNid, account, CustomConstants.CLIENT_PC, couponGrantId);
		int status = couponCheckResult.getIntValue("status");
		String statusDesc = couponCheckResult.getString("statusDesc");
		BorrowProjectType borrowProjectType = tenderService.getBorrowProjectType(borrow.getProjectType().toString());
		// add by cwyang 2017-5-17 代金券无本金出借时,增加新手出借的校验
		// 取得项目类别
		if (borrowProjectType != null && borrowProjectType.getInvestUserType().equals("1")) {
			boolean newUser = tenderService.checkIsNewUserCanInvest(userId);
			if (!newUser) {
				modelAndView.setViewName(TenderDefine.INVEST_ERROR_PATH);
				modelAndView.addObject("investDesc", "该项目只能新手出借");
				return modelAndView;
			}
		}
		if (status == 1) {
			modelAndView.setViewName(TenderDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("investDesc", statusDesc);
			return modelAndView;
		}
		JSONObject jsonObject = CommonSoaUtils.CouponInvest(userId + "", borrowNid, account, CustomConstants.CLIENT_PC, couponGrantId, "", ip, couponOldTime + "");

		if (jsonObject.getIntValue("status") == 0) {

			/** 修改出借成功页面显示修改开始 */
			String projectTypeName = getProjectTypeName(borrowProjectType);
			modelAndView.addObject("borrowNid", borrowNid);
			modelAndView.addObject("projectTypeName", projectTypeName);
			// 优惠券收益
			modelAndView.addObject("couponInterest", jsonObject.getString("couponInterest"));
			// 优惠券类别
			modelAndView.addObject("couponType", jsonObject.getString("couponTypeInt"));
			// 优惠券额度
			modelAndView.addObject("couponQuota", jsonObject.getString("couponQuota"));
			modelAndView.addObject("investDesc", "出借成功！");
			
			// 优惠券收益
            modelAndView.addObject("interest", jsonObject.getString("couponInterest"));
            // 优惠券收益
            modelAndView.addObject("account", "0");
			// 跳转到成功画面
			modelAndView.setViewName(TenderDefine.INVEST_SUCCESS_PATH);
			return modelAndView;
		} else {
			LogUtil.infoLog(THIS_CLASS, "updateCouponTender", "优惠券出借结束。。。。。。");
			modelAndView.setViewName(TenderDefine.INVEST_ERROR_PATH);
			modelAndView.addObject("investDesc", jsonObject.getString("statusDesc"));
			modelAndView.addObject("projectType", borrow.getProjectType());
			return modelAndView;
		}

	}

}
