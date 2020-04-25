/**
 * Description:用户出借控制器
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author:王坤
 * @version: 1.0
 *           Created at: 2015年12月4日 下午2:32:36
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.app.user.invest;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.app.user.manage.AppUserService;
import com.hyjf.app.user.project.InvestProjectService;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.bank.service.evalation.EvaluationService;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.AccountDetailCustomize;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseMapBean;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.agreement.AgreementDefine;
import com.hyjf.app.bank.user.auto.AutoDefine;
import com.hyjf.app.newagreement.NewAgreementBean;
import com.hyjf.app.project.ProjectService;
import com.hyjf.app.riskassesment.RiskAssesmentDefine;
import com.hyjf.app.user.coupon.CouponBean;
import com.hyjf.app.user.coupon.CouponService;
import com.hyjf.app.user.credit.AppTenderCreditDefine;
import com.hyjf.app.user.credit.AppTenderCreditService;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.bank.service.user.auth.AuthService;
import com.hyjf.bank.service.user.tender.IncreaseInterestInvestService;
import com.hyjf.bank.service.user.tender.TenderService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.*;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.app.AppProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderToCreditAssignCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.coupon.PlanCouponResultBean;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.mybatis.model.customize.web.TenderToCreditAssignCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanDetailCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.soa.apiweb.CommonSoaUtils;

@Controller
@RequestMapping(value = InvestDefine.REQUEST_MAPPING)
public class InvestController extends BaseController {
	
	private Logger logger = LoggerFactory.getLogger(InvestController.class);

	@Autowired
	private ProjectService projectService;

	@Autowired
	private InvestService investService;

	@Autowired
	private CouponService couponService;

	@Autowired
	private AppTenderService appTenderService;

	@Autowired
	private AppTenderCreditService appTenderCreditService;

	@Autowired
	private EvaluationService evaluationService;

	@Autowired
    private IncreaseInterestInvestService increaseInterestInvestService;
	
	@Autowired
    private TenderService tenderService;
	@Autowired
    private AuthService authService;
	
	
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private InvestProjectService investProjectService;

	
	@Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;
	@Autowired
    private PlanService planService;
	private static String HOST = PropUtils.getSystem("hyjf.web.host").trim();

	private static DecimalFormat DF_COM_VIEW = new DecimalFormat("######0.00");

	private static DecimalFormat DF_FOR_VIEW = new DecimalFormat("#,##0.00");

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
	@RequestMapping(value = InvestDefine.GET_INVEST_INFO_MAPPING)
	public Object getInvestInfo(HttpServletRequest request, HttpServletResponse response) {
		String borrowNid = request.getParameter("borrowNid");
		String sign = request.getParameter("sign");
		String money = request.getParameter("money");
		String version = request.getParameter("version");
		String platform = request.getParameter("platform");
		String couponId = request.getParameter("couponId");
		String isConfirm = request.getParameter("isConfirm");//是否最后确认

		Integer userId = SecretUtil.getUserId(sign);
		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		//userId=4540;
		logger.info("app获取出借信息,用户:{},使用的优惠券id: {}", userId, couponId);

		// 出借类型
		String investType = borrowNid.substring(0, 3);
		String borrowType = request.getParameter("borrowType"); // 项目类型  HJH传HJH
		if(borrowType!=null&&"HJH".equals(borrowType)){
		    investType = "HJH";
		}
		// 债转编号
		String creditNid = borrowNid.substring(3);
		InvestInfoResultVo resultVo = new InvestInfoResultVo(InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.GET_INVEST_INFO_MAPPING);

		// TODO: 2018/10/13  校验用户测评金额和类型并返回
		resultVo.setRevalJudge(false);
		if (!(money == null || "".equals(money) || (new BigDecimal(money).compareTo(BigDecimal.ZERO) == 0))) {
			//从user中获取客户类型，ht_user_evalation_result（用户测评总结表）
			UserEvalationResultCustomize userEvalationResultCustomize = evaluationService.selectUserEvalationResultByUserId(userId);
			if (userEvalationResultCustomize != null) {
				EvaluationConfig evalConfig = new EvaluationConfig();
				//1.散标／债转出借者测评类型校验
				String debtEvaluationTypeCheck = "0";
				//2.散标／债转单笔投资金额校验
				String deptEvaluationMoneyCheck = "0";
				//3.散标／债转待收本金校验
				String deptCollectionEvaluationCheck = "0";
				//4.智投出借者测评类型校验
				String intellectualEveluationTypeCheck = "0";
				//5.智投单笔投资金额校验
				String intellectualEvaluationMoneyCheck = "0";
				//6.智投待收本金校验
				String intellectualCollectionEvaluationCheck = "0";
				//获取开关信息
				List<EvaluationConfig> evalConfigList = evaluationService.selectEvaluationConfig(evalConfig);
				if (evalConfigList != null && evalConfigList.size() > 0) {
					evalConfig = evalConfigList.get(0);
					//1.散标／债转出借者测评类型校验
					debtEvaluationTypeCheck = evalConfig.getDebtEvaluationTypeCheck() == null ? "0" : String.valueOf(evalConfig.getDebtEvaluationTypeCheck());
					//2.散标／债转单笔投资金额校验
					deptEvaluationMoneyCheck = evalConfig.getDeptEvaluationMoneyCheck() == null ? "0" : String.valueOf(evalConfig.getDeptEvaluationMoneyCheck());
					//3.散标／债转待收本金校验
					deptCollectionEvaluationCheck = evalConfig.getDeptCollectionEvaluationCheck() == null ? "0" : String.valueOf(evalConfig.getDeptCollectionEvaluationCheck());
					//4.智投出借者测评类型校验
					intellectualEveluationTypeCheck = evalConfig.getIntellectualEveluationTypeCheck() == null ? "0" : String.valueOf(evalConfig.getIntellectualEveluationTypeCheck());
					//5.智投单笔投资金额校验
					intellectualEvaluationMoneyCheck = evalConfig.getIntellectualEvaluationMoneyCheck() == null ? "0" : String.valueOf(evalConfig.getIntellectualEvaluationMoneyCheck());
					//6.智投待收本金校验
					intellectualCollectionEvaluationCheck = evalConfig.getIntellectualCollectionEvaluationCheck() == null ? "0" : String.valueOf(evalConfig.getIntellectualCollectionEvaluationCheck());
					//7.投标时校验（二期）(预留二期开发)
				}
				//初始化金额返回值
				String revaluation_money, revaluation_money_principal;
				//根据类型从redis或数据库中获取测评类型和上限金额
				String eval_type = userEvalationResultCustomize.getType();
				switch (eval_type) {
					case "保守型":
						//从redis获取金额（单笔）
						revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE) == null ? "0" : RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE);
						//如果reids不存在或者为零尝试获取数据库（单笔）
						if ("0".equals(revaluation_money)) {
							revaluation_money = evalConfig.getConservativeEvaluationSingleMoney() == null ? "0" : String.valueOf(evalConfig.getConservativeEvaluationSingleMoney());
						}
						//从redis获取金额（代收本金）
						revaluation_money_principal = RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE_PRINCIPAL) == null ? "0" : RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE_PRINCIPAL);
						//如果reids不存在或者为零尝试获取数据库（代收本金）
						if ("0".equals(revaluation_money_principal)) {
							revaluation_money_principal = evalConfig.getConservativeEvaluationPrincipalMoney() == null ? "0" : String.valueOf(evalConfig.getConservativeEvaluationPrincipalMoney());
						}
						break;
					case "稳健型":
						//从redis获取金额（单笔）
						revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS) == null ? "0" : RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS);
						//如果reids不存在或者为零尝试获取数据库（单笔）
						if ("0".equals(revaluation_money)) {
							revaluation_money = evalConfig.getSteadyEvaluationSingleMoney() == null ? "0" : String.valueOf(evalConfig.getSteadyEvaluationSingleMoney());
						}
						//从redis获取金额（代收本金）
						revaluation_money_principal = RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS_PRINCIPAL) == null ? "0" : RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS_PRINCIPAL);
						//如果reids不存在或者为零尝试获取数据库（代收本金）
						if ("0".equals(revaluation_money_principal)) {
							revaluation_money_principal = evalConfig.getSteadyEvaluationPrincipalMoney() == null ? "0" : String.valueOf(evalConfig.getSteadyEvaluationPrincipalMoney());
						}
						break;
					case "成长型":
						//从redis获取金额（单笔）
						revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_GROWTH) == null ? "0" : RedisUtils.get(RedisConstants.REVALUATION_GROWTH);
						//如果reids不存在或者为零尝试获取数据库（单笔）
						if ("0".equals(revaluation_money)) {
							revaluation_money = evalConfig.getGrowupEvaluationSingleMoney() == null ? "0" : String.valueOf(evalConfig.getGrowupEvaluationSingleMoney());
						}
						//从redis获取金额（代收本金）
						revaluation_money_principal = RedisUtils.get(RedisConstants.REVALUATION_GROWTH_PRINCIPAL) == null ? "0" : RedisUtils.get(RedisConstants.REVALUATION_GROWTH_PRINCIPAL);
						//如果reids不存在或者为零尝试获取数据库（代收本金）
						if ("0".equals(revaluation_money_principal)) {
							revaluation_money_principal = evalConfig.getGrowupEvaluationPrincipalMoney() == null ? "0" : String.valueOf(evalConfig.getGrowupEvaluationPrincipalMoney());
						}
						break;
					case "进取型":
						//从redis获取金额（单笔）
						revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE) == null ? "0" : RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE);
						//如果reids不存在或者为零尝试获取数据库（单笔）
						if ("0".equals(revaluation_money)) {
							revaluation_money = evalConfig.getEnterprisingEvaluationSinglMoney() == null ? "0" : String.valueOf(evalConfig.getEnterprisingEvaluationSinglMoney());
						}
						//从redis获取金额（代收本金）
						revaluation_money_principal = RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE_PRINCIPAL) == null ? "0" : RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE_PRINCIPAL);
						//如果reids不存在或者为零尝试获取数据库（代收本金）
						if ("0".equals(revaluation_money_principal)) {
							revaluation_money_principal = evalConfig.getEnterprisingEvaluationPrincipalMoney() == null ? "0" : String.valueOf(evalConfig.getEnterprisingEvaluationPrincipalMoney());
						}
						break;
					default:
						revaluation_money = null;
						revaluation_money_principal = null;
				}
                String checkLeve = null;
                String borrowFlag = null;
                // 2. 转让投资信息
                if ("HZR".equals(investType) && StringUtils.isNotEmpty(creditNid)) {
                    // 根据债转id 获取债转详情
                    List<BorrowCredit> resultList = this.appTenderCreditService.selectBorrowCreditByNid(creditNid);
                    if (resultList != null && resultList.size() > 0) {
                        BorrowCredit borrowCredit = resultList.get(0);
                        // 原标项目编号
                        String bidNid = borrowCredit.getBidNid();
                        // 根据原标标号取借款信息
                        Borrow borrow = this.appTenderCreditService.getBorrowByBorrowNid(bidNid);
                        if (borrow != null) {
                            checkLeve = borrow.getInvestLevel();
                            borrowFlag = "BORROW_ZZ";
                        }
                    }
                }else
                // 3. 汇计划投资
                if ("HJH".equals(investType)) {
                    // 根据项目标号获取相应的计划信息
                    HjhPlan plan = planService.getPlanByNid(borrowNid);
                    if (plan != null) {
                        checkLeve = plan.getInvestLevel();
                        borrowFlag = "BORROW_JH";
                    }
                }else{
                // 1. 散标投资信息
                    // 查询项目信息
                    Borrow borrow = investService.getBorrowByNid(borrowNid);
                    if (borrow != null) {
                        checkLeve = borrow.getInvestLevel();
                        borrowFlag = "BORROW_SB";
                    }
                }
                //风险类型校验
                 if ((CustomConstants.EVALUATION_CHECK.equals(debtEvaluationTypeCheck) && ("BORROW_ZZ".equals(borrowFlag) || "BORROW_SB".equals(borrowFlag)))
                         || (CustomConstants.EVALUATION_CHECK.equals(intellectualEveluationTypeCheck) && "BORROW_JH".equals(borrowFlag))) {
					//计划类判断用户类型为稳健型以上才可以投资
					if (!CommonUtils.checkStandardInvestment(eval_type,borrowFlag,checkLeve)) {
						//返回错误码
						resultVo.setProjectRevalJudge(true);
						resultVo.setEvalType(eval_type);
						resultVo.setProjectRiskLevelDesc(CommonUtils.DESC_PROJECT_RISK_LEVEL_DESC.replace("{0}", userEvalationResultCustomize.getType()).replace("{1}",checkLeve));
					}
				}
				if (revaluation_money_principal == null) {
					logger.info("=============从redis中获取测评类型和上限金额异常!(没有获取到对应类型的限额数据) eval_type=" + eval_type);
				} else {
					//代收本金限额校验
					if ((CustomConstants.EVALUATION_CHECK.equals(deptCollectionEvaluationCheck) && ("BORROW_ZZ".equals(borrowFlag) || "BORROW_SB".equals(borrowFlag)))
                            || (CustomConstants.EVALUATION_CHECK.equals(intellectualCollectionEvaluationCheck) && "BORROW_JH".equals(borrowFlag))) {
						//获取冻结金额和代收本金
						List<AccountDetailCustomize> accountInfos = evaluationService.queryAccountEvalDetail(userId);
						if (accountInfos != null || accountInfos.size() > 0) {
							AccountDetailCustomize accountDetail = accountInfos.get(0);
							BigDecimal planFrost = accountDetail.getPlanFrost();// plan_frost 汇添金计划真实冻结金额
							BigDecimal bankFrost = accountDetail.getBankFrost();// bank_frost 银行冻结金额
							BigDecimal bankAwaitCapital = accountDetail.getBankAwaitCapital();// bank_await_capital 银行待收本金
							BigDecimal account = BigDecimal.ZERO;
							//加法运算
							account = account.add(planFrost).add(bankFrost).add(bankAwaitCapital).add(new BigDecimal(money));
							//金额对比判断（校验金额 大于 设置测评金额）（代收本金）
							if (account.compareTo(new BigDecimal(revaluation_money_principal)) > 0) {
								//是否需要重新测评
								resultVo.setRevalJudge(true);
								//返回类型和限额
								resultVo.setEvalType(eval_type);
								resultVo.setRevaluationMoney(StringUtil.getTenThousandOfANumber(Double.valueOf(revaluation_money_principal).intValue()));
								//resultVo.setRiskLevelDesc("您当前的风险测评类型为 #"+eval_type+"# \n根据监管要求,\n"+eval_type+"用户代收本金最高投资限额 #"
								//		+StringUtil.getTenThousandOfANumber(Double.valueOf(revaluation_money_principal).intValue())+"# 。");
                                resultVo.setRiskLevelDesc("如果您继续出借， ## \n当前累计出借本金将超过 \n您的风险等级 #"+eval_type+"# 对应的限额。");
							}
						}
					}
				}
				if (revaluation_money == null) {
					logger.info("=============从redis中获取测评类型和上限金额异常!(没有获取到对应类型的限额数据) eval_type=" + eval_type);
				} else {
					if ((CustomConstants.EVALUATION_CHECK.equals(deptEvaluationMoneyCheck) && ("BORROW_ZZ".equals(borrowFlag) || "BORROW_SB".equals(borrowFlag)))
							|| (CustomConstants.EVALUATION_CHECK.equals(intellectualEvaluationMoneyCheck) && "BORROW_JH".equals(borrowFlag))) {
						//金额对比判断（校验金额 大于 设置测评金额）
						if (new BigDecimal(money).compareTo(new BigDecimal(revaluation_money)) > 0) {
							//是否需要重新测评
							resultVo.setRevalJudge(true);
							//返回类型和限额
							resultVo.setEvalType(eval_type);
							resultVo.setRevaluationMoney(StringUtil.getTenThousandOfANumber(Double.valueOf(revaluation_money).intValue()));
							resultVo.setRiskLevelDesc("您当前的风险测评类型为 #"+eval_type+"# \n根据监管要求,\n"+eval_type+"用户单笔最高出借限额 #"
									+StringUtil.getTenThousandOfANumber(Double.valueOf(revaluation_money).intValue())+"# 。");
						}
					}
				}
			} else {
				logger.info("=============该用户测评总结数据为空! userId=" + userId);
			}
		}

		//add by cwyang APP3.0.9 确认是否为最后一次确认，如果是最后一次确认则必须进行出借校验
		if(isConfirm != null && "1".equals(isConfirm)){
			JSONObject checkTender = this.checkTender(request);
			logger.info("checkTender:"+JSONObject.toJSONString(checkTender));
			String status = (String) checkTender.get("status");//statusDesc
			if(StringUtils.isNotBlank(status) && !"0".equals(status)){
				resultVo.setStatus((String) checkTender.get("status"));
				resultVo.setStatusDesc((String) checkTender.get("statusDesc"));
				return resultVo;
			}

		}

		resultVo.setBorrowType(borrowType);
		resultVo.setProspectiveEarnings("");
		resultVo.setInterest("");
		resultVo.setUsedCouponDes("未使用");
		resultVo.setIsUsedCoupon("0");

		// 返回出借阈值
		boolean compareToVersion = compareToVersion(version, "3.0.8");//判断是否为 3.0.8以上版本
		resultVo.setStandardValues("0");
		logger.info("money is: {}", money);
		if (money == null || "".equals(money) || (new BigDecimal(money).compareTo(BigDecimal.ZERO) == 0)) {
			money = "0.00";
			resultVo.setRealAmount(money);
			resultVo.setButtonWord("确认");
		} else {
			resultVo.setRealAmount("¥" + CommonUtils.formatAmount(version, money));
			// 散标出借信息
			if ((!("HZR".equals(investType))) && (!("HJH".equals(investType)))) {
				resultVo.setButtonWord("确认出借" + CommonUtils.formatAmount(version, money) + "元");
			}else{
				resultVo.setButtonWord("确认出借" + CommonUtils.formatAmount(version, money) + "元");
			}
		}
		logger.info("=============cwyang 跳转出借详情 creditNid is " + creditNid);
		//if (!money.matches("^[-+]?(([0-9]+)(([0-9]+))?|(([0-9]+))?)$") || (!Validator.isNumber(creditNid)&&!"HJH".equals(borrowType))) {
		if (!CommonUtils.isNumber(money) && !Validator.isNumber(creditNid)&&!"HJH".equals(borrowType)) {
			resultVo.setStatusDesc(CustomConstants.APP_STATUS_DESC_FAIL);
		} else {
			BigDecimal couponInterest = BigDecimal.ZERO;
			BigDecimal borrowInterest = new BigDecimal(0);

			// 1. 散标出借信息
			if ((!("HZR".equals(investType))) && (!("HJH".equals(investType)))) {
				logger.info("散标获取出借数据, borrowNid is " + borrowNid);
				// 查询项目信息
				Borrow borrow = investService.getBorrowByNid(borrowNid);
				if (borrow != null) {
					resultVo.setBorrowApr(borrow.getBorrowApr()+"%");
					resultVo.setPaymentOfInterest("");
					Integer projectType = borrow.getProjectType();
					//根据项目出借唯一标示获取出借类型
					BorrowProjectType ProjectType = investService.getProjectType(projectType);
					if(null != ProjectType.getBorrowName()){
						resultVo.setBorrowType(ProjectType.getBorrowName());
					}else{
						resultVo.setBorrowType("");
					}
					// add by liuyang 神策数据统计 20180820 start
					// 获取标的还款方式
					BorrowStyle projectBorrowStyle = this.appTenderService.getProjectBorrowStyle(borrow.getBorrowStyle());
					if (projectBorrowStyle != null) {
						resultVo.setBorrowStyleName(StringUtils.isBlank(projectBorrowStyle.getName()) ? "" : projectBorrowStyle.getName());
					} else {
						resultVo.setBorrowStyleName("");
					}
					// 项目名称
					resultVo.setProjectName(borrow.getProjectName());
					// 借款期限
					resultVo.setBorrowPeriod(borrow.getBorrowPeriod());
					if ("endday".equals(borrow.getBorrowStyle())) {
						resultVo.setDurationUnit("天");
					} else {
						resultVo.setDurationUnit("月");
					}
					// add by liuyang 神策数据统计 20180820 end
					UserCouponConfigCustomize couponConfig = null;
					// 获取用户所有优惠券列表
					JSONObject userCoupon = couponService.getUserCoupon(borrowNid, userId, money, platform);

					if (couponId == null || "".equals(couponId) || couponId.length() == 0) {
						// 获取用户最优优惠券 不用获取最优优惠券了
						// couponConfig = getBestCoupon(borrowNid, userId, money, platform);
					} else {
						// app选择了优惠券，校验优惠券是否可用
						if (isCouponAvailable(couponId, userCoupon)) {
							couponConfig = getCouponById(couponId);
						}
					}

					if (couponConfig != null) {
						if (couponConfig.getCouponType() == 1) {
							resultVo.setCouponDescribe("体验金: " + couponConfig.getCouponQuota() + "元");
							resultVo.setConfirmCouponDescribe("体验金: " + couponConfig.getCouponQuota() + "元");
							resultVo.setCouponType("体验金");

						}
						if (couponConfig.getCouponType() == 2) {
							resultVo.setCouponDescribe("加息券:  " + couponConfig.getCouponQuota() + "%");
							resultVo.setConfirmCouponDescribe("加息券: " + couponConfig.getCouponQuota() + "%");
							resultVo.setCouponType("加息券");

						}
						if (couponConfig.getCouponType() == 3) {
							resultVo.setCouponDescribe("代金券: " + couponConfig.getCouponQuota() + "元");
							resultVo.setConfirmCouponDescribe("代金券: " + couponConfig.getCouponQuota() + "元");
							resultVo.setCouponType("代金券");
							if(compareToVersion){
								resultVo.setRealAmount("¥" + CommonUtils.formatAmount(version, new BigDecimal(money)));
							}else {
								resultVo.setRealAmount("实际出借 " + CommonUtils.formatAmount(version, new BigDecimal(money).add(couponConfig.getCouponQuota())) + "元");
							}

						}
						resultVo.setCouponName(couponConfig.getCouponName());
						resultVo.setCouponQuota(couponConfig.getCouponQuota().toString());
						resultVo.setEndTime(couponConfig.getCouponAddTime() + "-" + couponConfig.getEndTime());
						resultVo.setIsThereCoupon("1");
						resultVo.setCouponId(couponConfig.getUserCouponId());
						resultVo.setUsedCouponDes(resultVo.getCouponDescribe());

						resultVo.setIsUsedCoupon("1");
						resultVo.setCapitalInterest("历史回报: " + CommonUtils.formatAmount(version, couponInterest) + "元");
					} else {
						resultVo.setIsThereCoupon("0");
						resultVo.setCouponDescribe("暂无可用");
						resultVo.setCouponName("");
						resultVo.setCouponQuota("");
						resultVo.setEndTime("");
						resultVo.setCouponId("-1");

						if(!"0".equals(userCoupon.getString("availableCouponListCount"))){
							resultVo.setIsThereCoupon("1");
							resultVo.setCouponDescribe("请选择");
						}else if ("0".equals(userCoupon.getString("availableCouponListCount")) && !"0".equals(userCoupon.getString("notAvailableCouponListCount"))) {
							resultVo.setIsThereCoupon("1");
							resultVo.setCouponDescribe("暂无可用");
						}else {
							resultVo.setIsThereCoupon("0");
							resultVo.setCouponDescribe("无可用");
						}

						BorrowProjectType borrowProjectType = investService.getProjectTypeByBorrowNid(borrowNid);
						if ("HZT".equals(borrowProjectType.getBorrowProjectType())) {
							if ("ZXH".equals(borrowProjectType.getBorrowClass()) || "NEW".equals(borrowProjectType.getBorrowClass())) {
								resultVo.setConfirmCouponDescribe("不支持使用优惠券");
								resultVo.setCapitalInterest("");
							} else {
								resultVo.setConfirmCouponDescribe("未使用优惠券");
								resultVo.setCapitalInterest("");
							}
						} else {
							resultVo.setConfirmCouponDescribe("不支持使用优惠券");
							resultVo.setCapitalInterest("");
						}

					}

					resultVo.setBorrowNid(borrowNid);
					BigDecimal borrowAccountWait = borrow.getBorrowAccountWait();
					// 去最小值 最大可投和 项目可投
					if (borrow.getTenderAccountMax() != null && borrowAccountWait != null && (borrow.getProjectType() == 4 || borrow.getProjectType() == 11)) {
						BigDecimal TenderAccountMax = new BigDecimal(borrow.getTenderAccountMax());
						if (TenderAccountMax.compareTo(borrowAccountWait) == -1) {
							resultVo.setBorrowAccountWait(CommonUtils.formatAmount(version, TenderAccountMax));
						} else {
							resultVo.setBorrowAccountWait(CommonUtils.formatAmount(version, borrowAccountWait));
						}
					} else {
						resultVo.setBorrowAccountWait(CommonUtils.formatAmount(version, borrowAccountWait));
					}
					String balanceWait = borrow.getBorrowAccountWait() + "";
					if (balanceWait == null || balanceWait.equals("")) {
						balanceWait = "0";
					}
					// 剩余可投小于起投，计算收益按照剩余可投计算
					if ((StringUtils.isBlank(money) || money.equals("0")) && new BigDecimal(balanceWait).compareTo(new BigDecimal(borrow.getTenderAccountMin())) < 0) {
						money = new BigDecimal(balanceWait).intValue() + "";
					}

					// 设置产品加息 显示收益率
			        if (Validator.isIncrease(borrow.getIncreaseInterestFlag(), borrow.getBorrowExtraYield())) {
			            resultVo.setBorrowExtraYield(borrow.getBorrowExtraYield()+"%");
			        }
			        
					BigDecimal earnings = new BigDecimal("0");
					if (!StringUtils.isBlank(money) && Double.parseDouble(money) >= 0) {
						String borrowStyle = borrow.getBorrowStyle();
						// 收益率
						BigDecimal borrowApr = borrow.getBorrowApr();
						// 周期
						Integer borrowPeriod = borrow.getBorrowPeriod();
						// 计算本金出借历史回报
						switch (borrowStyle) {
						case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“: 历史回报=出借金额*年化收益÷12*月数；
							earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(money),
									borrowApr.divide(new BigDecimal("100")), borrowPeriod)
									.setScale(2, BigDecimal.ROUND_DOWN);
							resultVo.setInterest(CommonUtils.formatAmount(version, earnings));
							break;
						case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“: 历史回报=出借金额*年化收益÷360*天数；
							earnings = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(money),
									borrowApr.divide(new BigDecimal("100")), borrowPeriod)
									.setScale(2, BigDecimal.ROUND_DOWN);
							resultVo.setInterest(CommonUtils.formatAmount(version, earnings));
							break;
						case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“: 历史回报=出借金额*年化收益÷12*月数；
							earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(money),
									borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod)
									.setScale(2, BigDecimal.ROUND_DOWN);
							resultVo.setInterest(CommonUtils.formatAmount(version, earnings));
							break;
						case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“: 历史回报=出借金额*年化收益÷12*月数；
							earnings = AverageCapitalPlusInterestUtils.getInterestCount(new BigDecimal(money),
									borrowApr.divide(new BigDecimal("100")), borrowPeriod)
									.setScale(2, BigDecimal.ROUND_DOWN);
							resultVo.setInterest(CommonUtils.formatAmount(version, earnings));
							break;
						case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“: 历史回报=出借金额*年化收益÷12*月数；
							earnings = AverageCapitalUtils.getInterestCount(new BigDecimal(money),
									borrowApr.divide(new BigDecimal("100")), borrowPeriod)
									.setScale(2, BigDecimal.ROUND_DOWN);
							resultVo.setInterest(CommonUtils.formatAmount(version, earnings));
							break;
						default:
							resultVo.setInterest("");
							break;
						}
						logger.info("散标本金历史回报:  {}", earnings);
						borrowInterest = earnings;

						//计算优惠券历史回报
						if (couponConfig != null && couponConfig.getId() > 0) {
							couponInterest = calculateCouponTenderInterest(couponConfig, money, borrow);
							couponConfig.setCouponInterest(CommonUtils.formatAmount(version, couponInterest));
						}
					}

					resultVo.setCapitalInterest("历史回报: 0元");
					resultVo.setConfirmCouponDescribe("加息券:  无");
					resultVo.setCouponType("");

					resultVo.setDesc("历史年回报率: "+borrow.getBorrowApr()+"%      历史回报: " + CommonUtils.formatAmount(version, borrowInterest.add(couponInterest)) + "元");
					/**
					 * 产品加息
					 */
					if (Validator.isIncrease(borrow.getIncreaseInterestFlag(), borrow.getBorrowExtraYield())) {
                        resultVo.setDesc0("历史年回报率: " + borrow.getBorrowApr() + "% + "
                                + borrow.getBorrowExtraYield() + "%");
			        }else{
			            resultVo.setDesc0("历史年回报率: "+borrow.getBorrowApr()+"%");
			        }
					
					// 产品加息预期收益
		            if (Validator.isIncrease(borrow.getIncreaseInterestFlag(), borrow.getBorrowExtraYield())) {
		                BigDecimal incEarnings = tenderService.increaseCalculate(borrow.getBorrowPeriod(), borrow.getBorrowStyle(), money, borrow.getBorrowExtraYield());
		                borrowInterest = incEarnings.add(borrowInterest);
		            }
		            
					resultVo.setDesc1("历史回报: " + CommonUtils.formatAmount(version, borrowInterest.add(couponInterest)) + "元");
					// 安卓的历史回报使用这个字段
					resultVo.setProspectiveEarnings(CommonUtils.formatAmount(version, borrowInterest.add(couponInterest))+ "元");

					resultVo.setConfirmRealAmount("出借金额: " + CommonUtils.formatAmount(version, money) + "元");
					resultVo.setBorrowInterest(CommonUtils.formatAmount(version, borrowInterest) + "元");
					String couponAvailableCount = couponService.getUserCouponAvailableCount(borrowNid, userId, money, platform);
					resultVo.setCouponAvailableCount(couponAvailableCount);

					Account account = investService.getAccount(userId);
					BigDecimal balance = account.getBankBalance();
					resultVo.setBalance(CommonUtils.formatAmount(version, balance));
					resultVo.setStatus(CustomConstants.APP_STATUS_SUCCESS);
					resultVo.setStatusDesc(CustomConstants.APP_STATUS_DESC_SUCCESS);
					resultVo.setInitMoney(borrow.getTenderAccountMin() + "");
					resultVo.setIncreaseMoney(String.valueOf(borrow.getBorrowIncreaseMoney()));
					resultVo.setInvestmentDescription(borrow.getTenderAccountMin() + "元起投," + borrow.getBorrowIncreaseMoney() + "元递增");
					// 可用余额的递增部分
					BigDecimal tmpmoney = balance.subtract(new BigDecimal(borrow.getTenderAccountMin())).divide(new BigDecimal(borrow.getBorrowIncreaseMoney()), 0, BigDecimal.ROUND_DOWN)
							.multiply(new BigDecimal(borrow.getBorrowIncreaseMoney())).add(new BigDecimal(borrow.getTenderAccountMin()));
					if (balance.subtract(new BigDecimal(borrow.getTenderAccountMin())).compareTo(new BigDecimal("0")) < 0) {
						// 可用余额<起投金额 时 investAllMoney 传 -1
						// 全投金额
						resultVo.setInvestAllMoney("-1");
					} else {
						String borrowAccountWaitStr = resultVo.getBorrowAccountWait().replace(",", "");
						if (new BigDecimal(borrow.getTenderAccountMax()).compareTo(new BigDecimal(borrowAccountWaitStr)) < 0) {
							resultVo.setInvestAllMoney(borrow.getTenderAccountMax() + "");
						} else if (tmpmoney.compareTo(new BigDecimal(borrowAccountWaitStr)) < 0) {
							// 全投金额
							resultVo.setInvestAllMoney(tmpmoney + "");
						} else {
							// 全投金额
							resultVo.setInvestAllMoney(resultVo.getBorrowAccountWait() + "");
						}
						
						//计算全投金额 modify by cwyang 2017-8-17
						String result = getMinAmount(borrow.getTenderAccountMax(),tmpmoney,borrowAccountWaitStr);
						if (result != null) {
							resultVo.setInvestAllMoney(result);
						}

					}
					resultVo.setBorrowAccountWait(CommonUtils.formatAmount(borrow.getBorrowAccountWait()) + "");
					resultVo.setAnnotation("");

				} else {
					resultVo.setStatusDesc(CustomConstants.APP_STATUS_DESC_FAIL);
				}
			}

			// 2. 转让出借信息
			if ("HZR".equals(investType) && StringUtils.isNotEmpty(creditNid)) {
				// 出借项目id
				resultVo.setBorrowNid(creditNid);

				// 获取债转的详细参数
				Map<String, Object> creditDetailMap = this.appTenderCreditService.webCheckCreditTenderAssign(userId, creditNid, money);
				if (creditDetailMap.containsKey("resultType") && "1".equals(creditDetailMap.get("resultType"))) {
					resultVo.setStatus("99");
					resultVo.setStatusDesc(
							creditDetailMap.containsKey("msg") ? (String) creditDetailMap.get("msg") : "系统异常");
					return CommonUtils.convertNullToEmptyString(resultVo);
				}
				Borrow borrow = null;
				// 获取债转的详细
				if (creditDetailMap != null) {
					Account account = investService.getAccount(userId);
					BigDecimal balance = account.getBankBalance();
					resultVo.setBalance(CommonUtils.formatAmount(version, balance));
					resultVo.setStatus(CustomConstants.APP_STATUS_SUCCESS);
					resultVo.setStatusDesc(CustomConstants.APP_STATUS_DESC_SUCCESS);
					// resultVo.setInitMoney(borrow.getTenderAccountMin() + "");
					logger.info("tenderToCreditAssign:"+creditDetailMap.get("tenderToCreditAssign"));
					AppTenderToCreditAssignCustomize bean = (AppTenderToCreditAssignCustomize) creditDetailMap.get("tenderToCreditAssign");
					// 待承接垫付利息
					BigDecimal interestAdvanceWait = BigDecimal.ZERO;
					// 待承接金额
					BigDecimal capitalWait = BigDecimal.ZERO;
                    BorrowCredit creditByBorrowNid = appTenderCreditService.selectCreditTenderByCreditNid(creditNid);
                    String borrowAccountWait = String.valueOf(creditByBorrowNid.getCreditCapital().subtract(creditByBorrowNid.getCreditCapitalAssigned()));
                    // 根据债转id 获取债转详情
					List<BorrowCredit> resultList = this.appTenderCreditService.selectBorrowCreditByNid(creditNid);
					if (resultList != null && resultList.size() > 0) {
						BorrowCredit borrowCredit = resultList.get(0);
						interestAdvanceWait = borrowCredit.getCreditInterestAdvance().subtract(borrowCredit.getCreditInterestAdvanceAssigned());
						capitalWait = borrowCredit.getCreditCapital().subtract(borrowCredit.getCreditCapitalAssigned());
						// add by liuyang  神策数据统计 20180820 start
						// 原标项目编号
						String bidNid = borrowCredit.getBidNid();
						// 根据原标标号取借款信息
						borrow = this.appTenderCreditService.getBorrowByBorrowNid(bidNid);
						if (borrow != null) {
							BorrowStyle projectBorrowStyle = this.appTenderService.getProjectBorrowStyle(borrow.getBorrowStyle());
							if (projectBorrowStyle != null) {
								resultVo.setBorrowStyleName(StringUtils.isBlank(projectBorrowStyle.getName()) ? "" : projectBorrowStyle.getName());
							} else {
								resultVo.setBorrowStyleName("");
							}
						}

						// 项目名称
						resultVo.setProjectName(borrow.getProjectName());
						// 借款期限
						resultVo.setBorrowPeriod(borrow.getBorrowPeriod());
						if ("endday".equals(borrow.getBorrowStyle())) {
							resultVo.setDurationUnit("天");
						} else {
							resultVo.setDurationUnit("月");
						}

						// 债转期限
						resultVo.setCreditPeriod(borrowCredit.getCreditTerm());
						// 期限单位
						resultVo.setCreditDurationUnit("天");
						// add by liuyang  神策数据统计 20180820 end
					}
					resultVo.setBorrowAccountWait(CommonUtils.formatAmount(version, borrowAccountWait));
					resultVo.setCouponDescribe("");
					resultVo.setCouponId("");
					resultVo.setCouponQuota("");
					resultVo.setEndTime("");
					resultVo.setInitMoney("");
					resultVo.setIsThereCoupon("0");
					resultVo.setCouponName("");
					resultVo.setCouponType("");
					resultVo.setCouponAvailableCount("");
					resultVo.setAssignPay("");
					resultVo.setBorrowApr(borrow.getBorrowApr()+"%");
					if (StringUtils.isNotEmpty(money) && !"0".equals(money) && !"0.00".equals(money)) {
						// 实际支付金额
						if(compareToVersion){
							resultVo.setRealAmount("¥" + bean.getAssignPay());
						}else {
							resultVo.setRealAmount("实际支付金额:" + bean.getAssignPay());
						}
						// 历史回报
						resultVo.setProspectiveEarnings(bean.getAssignInterest()+"元");
						//BigDecimal assignInterest = new BigDecimal(bean.getAssignInterest()).add(new BigDecimal(money));
						//resultVo.setProspectiveEarnings(assignInterest+"元");
						//备注
						resultVo.setDesc("折让率: "+bean.getCreditDiscount()+"%      历史回报: " + bean.getAssignInterest() +"元");
						//折让率
						resultVo.setDesc0("折让率: "+bean.getCreditDiscount()+"%");
						//历史回报
						resultVo.setDesc1("历史回报: "+bean.getAssignInterest()+"元");
						// 实际支付金额
						resultVo.setAssignPay(bean.getAssignPay());
						// 认购本金
						resultVo.setAssignCapital(DF_FOR_VIEW.format(new BigDecimal(bean.getAssignCapital())) + "元");
						// 垫付利息
						resultVo.setAssignInterestAdvance(bean.getAssignInterestAdvance() + "元");
						// 垫付利息
						resultVo.setPaymentOfInterest(bean.getAssignInterestAdvance() + "元");
						// 实际支付计算式
						resultVo.setAssignPayText(bean.getAssignPayText());
						// 折价率
						resultVo.setCreditDiscount(bean.getCreditDiscount() + "%");
						//按钮上的文字
						resultVo.setButtonWord("实际支付"+bean.getAssignPay()+"元");
					} else {
						resultVo.setCreditDiscount("");
						// 认购本金
						resultVo.setAssignCapital("0.00" + "元");
						// 垫付利息
						resultVo.setAssignInterestAdvance("0.00" + "元");
						// 垫付利息
						resultVo.setPaymentOfInterest("0.00" + "元");
						//备注
						resultVo.setDesc("折让率: "+bean.getCreditDiscount()+"%      历史回报: 0.00元");
						//折让率
						resultVo.setDesc0("折让率: "+bean.getCreditDiscount()+"%");
						//历史回报
						resultVo.setDesc1("历史回报: "+"0.00元");
						// 实际支付计算式
						resultVo.setAssignPayText("");
						// 实际支付金额
						resultVo.setAssignPay("0.00");
						// 历史回报
                        resultVo.setProspectiveEarnings("0.00元");
						resultVo.setButtonWord("确认");
					}
					resultVo.setInvestmentDescription("承接金额应大于1元");
					resultVo.setAnnotation("注: 实际支付金额=认购本金（1-折让率）+垫付利息");
					
					// 折比率
					BigDecimal creditDiscount = new BigDecimal(1).subtract(new BigDecimal(bean.getCreditDiscount()).divide(new BigDecimal(100)));
					BigDecimal sum = capitalWait.multiply(creditDiscount).add(interestAdvanceWait);
					BigDecimal max = new BigDecimal(0); 
					//modify by cwyang 被除数不得为0
					if (sum.compareTo(new BigDecimal(0)) > 0) {
						max = capitalWait.multiply(balance).divide(sum, 8, RoundingMode.DOWN);
					}
					if (max.compareTo(capitalWait) > 0) {
						// 全投金额
						resultVo.setInvestAllMoney((String.valueOf(capitalWait.intValue())));
					} else {
						// 全投金额
						resultVo.setInvestAllMoney(String.valueOf(max.intValue()));
					}
				} else {
					resultVo.setStatusDesc(CustomConstants.APP_STATUS_DESC_FAIL);
					// 全投金额
					resultVo.setInvestAllMoney("0");
				}
			}

			// 3. 汇计划出借
            if ("HJH".equals(investType)) {
				logger.info("================HJH borrowNid is " + borrowNid);
				
				if (StringUtils.isNotBlank(money) && new BigDecimal(money).compareTo(BigDecimal.ZERO) > 0) {
					// mod by nxl 智投服务 修改 确认加入->确认授权
//					resultVo.setButtonWord("确认加入" + CommonUtils.formatAmount(version, money) + "元");
					resultVo.setButtonWord("确认授权" + CommonUtils.formatAmount(version, money) + "元");
				}
                
                // 查询计划信息  传入borrowNid
				resultVo.setStandardValues(CustomConstants.TENDER_THRESHOLD);
                // 根据项目标号获取相应的计划信息
                HjhPlan plan = planService.getPlanByNid(borrowNid);

                // add by liuyang 神策数据统计 20180820 start
				BorrowStyle borrowStyle = this.appTenderService.getProjectBorrowStyle(plan.getBorrowStyle());
				if (borrowStyle!=null){
					resultVo.setBorrowStyleName(StringUtils.isBlank(borrowStyle.getName()) ? "" : borrowStyle.getName());
				}else{
					resultVo.setBorrowStyleName("");
				}

				// 项目名称
				resultVo.setProjectName(plan.getPlanName());
				// 借款期限
				resultVo.setBorrowPeriod(plan.getLockPeriod());
				if (plan.getIsMonth() == 0) {
					resultVo.setDurationUnit("天");
				} else {
					resultVo.setDurationUnit("月");
				}
				// add by liuyang 神策数据统计 20180820 end
                resultVo.setBorrowApr(plan.getExpectApr()+"%");
                DebtPlanDetailCustomize planDetail = planService.selectDebtPlanDetail(borrowNid);
                resultVo.setPaymentOfInterest("0" + "元");
                // 获取用户最优优惠券 
                UserCouponConfigCustomize couponConfig = null;
                if (null != planDetail) {
                    resultVo.setBorrowNid(borrowNid);
                    // -设置  开放额度剩余金额
                    String borrowAccountWait = "0";
                    if (planDetail.getAvailableInvestAccount() != null) {
                        borrowAccountWait =  CommonUtils.formatAmount(version, planDetail.getAvailableInvestAccount());
                    }
                    borrowAccountWait = borrowAccountWait.replaceAll(",", "");
                    resultVo.setBorrowAccountWait(borrowAccountWait); // 开放额度剩余金额
                    String initMoney = "0";
                    // -设置 最小出借金额(起投金额)-->计算最后一笔出借
                    if (planDetail.getDebtMinInvestment() != null) {
                        initMoney = new BigDecimal(planDetail.getDebtMinInvestment()).intValue()+"";
                    }
                    resultVo.setInitMoney(initMoney);
                    // -设置优惠券
                    logger.info("HJH couponId is:{}, borrowNid is :{}", couponId, borrowNid);
                    if (couponId == null || "".equals(couponId) || couponId.length() == 0) {
                        // 不用获取最优优惠券了
                        //couponConfig = planService.getUserOptimalCoupon(couponId, borrowNid, userId, money, platform);
                    } else {
                        // 如果已经有了优惠券  判断优惠券是否可用
						JSONObject userCoupon = couponService.getHJHProjectUserCoupon(borrowNid, userId, money,
								platform);
						if (isHjhCouponAvailable(couponId, userCoupon)) {
								couponConfig = getCouponById(couponId);
						}
                    }
                    logger.info("优惠券信息couponConfig: {}", JSONObject.toJSONString(couponConfig));
                    if("-1".equals(couponId)){
                        couponConfig = null;
                    }
                    
                    // 刚加载页面并且可投小于起投
                    if ((StringUtils.isBlank(money) || money.equals("0")) && new BigDecimal(borrowAccountWait).compareTo(new BigDecimal(planDetail.getDebtMinInvestment())) < 1) {
                        money = new BigDecimal(borrowAccountWait).intValue() + "";
                    }
                    if (money.contains(",")) {
                        money = money.replace(",", "");
                    }
                    
                    BigDecimal earnings = new BigDecimal("0");
                    // 计算收益
                    if ((!StringUtils.isBlank(money) && Double.parseDouble(money) >= 0)) {
                    	// 这里有个坑，如果计划剩余可投小于用户出借金额，那么计算收益需要用计划剩余可投计算，不能使用用户出借金额计算收益
						logger.info("计划剩余可投: {}", borrowAccountWait);
						logger.info("用户出借金额: {}", money);
						//if (new BigDecimal(borrowAccountWait).compareTo(new BigDecimal(money)) < 0) {
						//	logger.info("计划剩余可投小于用户出借金额,收益按照计划剩余可投计算...");
						//	earnings = planService.setProspectiveEarnings(resultVo,couponConfig, borrowNid,userId,platform,borrowAccountWait);
						//} else {
							logger.info("计划剩余可投大于用户出借金额,收益按照用户出借金额计算...");
							earnings = planService.setProspectiveEarnings(resultVo,couponConfig, borrowNid,userId,platform,money);
						//}
                    }
                    logger.info("本金出借计算出的收益是: {}", earnings);
                    
                    // 设置优惠券
                    resultVo.setCapitalInterest("");
                    resultVo.setConfirmCouponDescribe("未使用优惠券");
                    resultVo.setCouponType("");
                    JSONObject counts =  CommonSoaUtils.getUserCouponAvailableCount(borrowNid, userId, money, platform);
                    String couponAvailableCount = "0";
                    if(counts.containsKey("availableCouponListCount")){
                        couponAvailableCount = counts.getString("availableCouponListCount");
                    }
                    if (couponConfig != null) {
                        if (couponConfig != null && couponConfig.getId() > 0 && couponConfig.getCouponType() == 1) {
                            resultVo.setCouponDescribe("体验金: " + couponConfig.getCouponQuota() + "元");
                            resultVo.setConfirmCouponDescribe("体验金: " + couponConfig.getCouponQuota() + "元");
                            resultVo.setCouponType("体验金");
                        }
                        if (couponConfig != null && couponConfig.getId() > 0 && couponConfig.getCouponType() == 2) {
                            resultVo.setCouponDescribe("加息券: " + couponConfig.getCouponQuota() + "%");
                            resultVo.setConfirmCouponDescribe("加息券: " + couponConfig.getCouponQuota() + "%");
                            resultVo.setCouponType("加息券");

                        }
                        if (couponConfig != null && couponConfig.getId() > 0 && couponConfig.getCouponType() == 3) {
                            resultVo.setCouponDescribe("代金券: " + couponConfig.getCouponQuota() + "元");
                            resultVo.setConfirmCouponDescribe("代金券: " + couponConfig.getCouponQuota() + "元");
                            resultVo.setCouponType("代金券");
                            if(compareToVersion){
								resultVo.setRealAmount("¥" + CommonUtils.formatAmount(version, new BigDecimal(money)));
							}else{
								resultVo.setRealAmount("实际出借 " + CommonUtils.formatAmount(version, new BigDecimal(money).add(couponConfig.getCouponQuota())) + "元");
							}

                        }
                        resultVo.setCouponName(couponConfig.getCouponName());
                        resultVo.setCouponQuota(couponConfig.getCouponQuota().toString());
                        resultVo.setEndTime(couponConfig.getCouponAddTime() + "-" + couponConfig.getEndTime());
                        resultVo.setIsThereCoupon("1");
                        resultVo.setCouponId(couponConfig.getUserCouponId());
                        resultVo.setIsUsedCoupon("1");
						resultVo.setUsedCouponDes(resultVo.getCouponDescribe());
						logger.info("开始计算优惠券收益....");
						String calculateIncomeCapital = "";
						if (new BigDecimal(borrowAccountWait).compareTo(new BigDecimal(money)) < 0) {
							logger.info("同样，计划剩余可投小于用户出借金额,收益按照计划剩余可投计算...");
							calculateIncomeCapital = borrowAccountWait;
						} else {
							logger.info("同样，计划剩余可投大于用户出借金额,收益按照用户出借金额计算...");
							calculateIncomeCapital = money;
						}
						logger.info("优惠券金额按照{}计算....", calculateIncomeCapital);
						JSONObject couResult = CommonSoaUtils.getCouponInterest(couponConfig.getUserCouponId() + "",
								borrowNid, calculateIncomeCapital);
                        logger.info("优惠券历史回报计算结果couResult: {}", couResult);

                        resultVo.setCapitalInterest(couResult.getString("couponInterest") + "元");
                        // 优惠券历史回报
                        couponInterest = new BigDecimal(couResult.getString("couponInterest"));

                        // 历史回报
						borrowInterest = earnings.add(couponInterest);

                      	//备注
    					resultVo.setDesc("历史年回报率:  "+plan.getExpectApr()+"%      历史回报:  " + borrowInterest+"元");
						// mod by nxl 智投服务修改显示Start
						/*resultVo.setDesc0("历史年回报率: "+plan.getExpectApr()+"%");
						resultVo.setDesc1("历史回报: " + CommonUtils.formatAmount(version,borrowInterest) + "元");*/
						resultVo.setDesc0("参考年回报率: "+plan.getExpectApr()+"%");
						resultVo.setDesc1("参考回报: " + CommonUtils.formatAmount(version,borrowInterest) + "元");
						// mod by nxl 智投服务修改显示End
                        resultVo.setProspectiveEarnings(CommonUtils.formatAmount(version, borrowInterest) + "元");
                        resultVo.setInterest(CommonUtils.formatAmount(version, borrowInterest));

                    }else{
                        // 没有可用优惠券
                        resultVo.setIsThereCoupon("0");
                        resultVo.setCouponDescribe("暂无可用");
                        resultVo.setCouponName("");
                        resultVo.setCouponQuota("");
                        resultVo.setEndTime("");
                        resultVo.setCouponId("-1");
                      
                        JSONObject userCoupon = couponService.getHJHProjectUserCoupon(borrowNid, userId, money, platform);
						if(!"0".equals(userCoupon.getString("availableCouponListCount"))){
							resultVo.setIsThereCoupon("1");
							resultVo.setCouponDescribe("请选择");
						}else if ("0".equals(userCoupon.getString("availableCouponListCount")) && !"0".equals(userCoupon.getString("notAvailableCouponListCount"))) {
							resultVo.setIsThereCoupon("1");
							resultVo.setCouponDescribe("暂无可用");
						}else {
							resultVo.setIsThereCoupon("0");
							resultVo.setCouponDescribe("无可用");
						}

                        resultVo.setDesc("历史年回报率: "+plan.getExpectApr()+"%      历史回报: " + earnings +"元");
						// mod by nxl 智投服务修改显示Start
						/*resultVo.setDesc0("历史年回报率: "+plan.getExpectApr()+"%");
						resultVo.setDesc1("历史回报: " + CommonUtils.formatAmount(version,earnings) + "元");*/
						resultVo.setDesc0("参考年回报率: "+plan.getExpectApr()+"%");
						resultVo.setDesc1("参考回报: " + CommonUtils.formatAmount(version,earnings) + "元");
						// mod by nxl 智投服务修改显示End
                      	resultVo.setProspectiveEarnings(earnings + "元");
                    }
                    // 可用优惠券数量
                    resultVo.setCouponAvailableCount(couponAvailableCount);
                    resultVo.setConfirmRealAmount("出借金额: " + CommonUtils.formatAmount(version, money) + "元");
                    // -设置 用户余额
                    Account account = investService.getAccount(userId);
                    BigDecimal balance = account.getBankBalance();
                    resultVo.setBalance(CommonUtils.formatAmount(version, balance));
                    resultVo.setStatus(CustomConstants.APP_STATUS_SUCCESS);
                    resultVo.setStatusDesc(CustomConstants.APP_STATUS_DESC_SUCCESS);
                    // 起投金额
                    resultVo.setInitMoney(plan.getMinInvestment().intValue()+"");
                    // 递增金额
                    resultVo.setIncreaseMoney(plan.getInvestmentIncrement().intValue()+"");
                    resultVo.setInvestmentDescription(resultVo.getInitMoney() + "元起投," + resultVo.getIncreaseMoney() + "元递增");
                    resultVo.setBorrowAccountWait(CommonUtils.formatAmount(version, borrowAccountWait)); // 开放额度剩余金额
                    BigDecimal tmpmoney = balance.subtract(plan.getMinInvestment()).divide(plan.getInvestmentIncrement(), 0, BigDecimal.ROUND_DOWN)
                            .multiply(plan.getInvestmentIncrement()).add(plan.getMinInvestment());
                    if (balance.subtract(plan.getMinInvestment()).compareTo(new BigDecimal("0")) < 0) {
                        // 可用余额<起投金额 时 investAllMoney 传 -1
                        // 全投金额
                        resultVo.setInvestAllMoney("-1");
                    } else {
                        String borrowAccountWaitStr = resultVo.getBorrowAccountWait().replace(",", "");
                        if (plan.getMaxInvestment().compareTo(new BigDecimal(borrowAccountWaitStr)) < 0) {
                            if(balance.compareTo(plan.getMaxInvestment())<0){
                                resultVo.setInvestAllMoney(balance + "");
                            }else{
                                resultVo.setInvestAllMoney(plan.getMaxInvestment() + "");
                            }
                        } else if (tmpmoney.compareTo(new BigDecimal(borrowAccountWaitStr)) < 0) {
                            // 全投金额
                            if(balance.compareTo(tmpmoney)<0){
                                resultVo.setInvestAllMoney(balance + "");
                            }else{
                                resultVo.setInvestAllMoney(tmpmoney + "");
                            }
                            resultVo.setInvestAllMoney(tmpmoney + "");
                        } else {
                            // 全投金额
                            resultVo.setInvestAllMoney(resultVo.getBorrowAccountWait() + "");
                        }
                    }
                    resultVo.setAnnotation("");
                } else {
					logger.info("=================HJH borrow is null! =============");
                    resultVo.setStatusDesc(CustomConstants.APP_STATUS_DESC_FAIL);
                }
            }// if end
		}

		// 协议赋值
		this.setProtocolsToResultVO(resultVo, investType, borrowNid);
		return CommonUtils.convertNullToEmptyString(resultVo);
	}

	/**
	 * 返回version 是否大于 基准版本compareVersion
	 * @param version
	 * @param compareVersion
	 * @return
	 */
	private boolean compareToVersion(String version, String compareVersion) {
		if (StringUtils.isEmpty(version) || StringUtils.isEmpty(compareVersion))
			return false;

		if (version.length() >= 5) {
			version = version.substring(0, 5);
		}
		if (version.compareTo(compareVersion) > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 当获取出借信息确定为最后一次确认时，需要进行出借校验
	 * @param request
	 */
	private JSONObject checkTender(HttpServletRequest request) {

		AppTenderVo vo = new AppTenderVo();
		vo.setBorrowNid(request.getParameter("borrowNid"));
		vo.setVersion(request.getParameter("version"));
		vo.setNetStatus(request.getParameter("netStatus"));
		vo.setPlatform(request.getParameter("platform"));
		vo.setToken(request.getParameter("token"));
		vo.setSign(request.getParameter("sign"));
		vo.setRandomString(request.getParameter("randomString"));
		vo.setOrder("0");
		vo.setAccount(request.getParameter("money"));
		request.setAttribute("couponGrantId",request.getParameter("couponId"));
		JSONObject jsonObject = getTenderUrl(vo, request);
		return jsonObject;
	}


	/**
	 * 跳转协议
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = InvestDefine.GODETAIL_MAPPING)
	public ModelAndView goDetail(HttpServletRequest request) {
		LogUtil.startLog(InvestDefine.class.getName(), InvestDefine.GODETAIL_MAPPING);
		ModelAndView modeAndView = null;
		String pageType = request.getParameter("type");
		if (pageType.equals("app_invest")) {
			// app平台居间服务协议H5
			modeAndView = new ModelAndView("invest/app_invest");
		} else if (pageType.equals("app_contract")) {
			// app散标风险揭示书H5
			modeAndView = new ModelAndView("invest/app_contract");
		} else if (pageType.equals("wenzhou_rules")) {
			// 温州协议H5
			modeAndView = new ModelAndView("invest/rtb-contract");
		} else if (pageType.equals("rtb-open-contract")) {
			String borrowNid = request.getParameter("borrowNid");
			// 2.根据项目标号获取相应的项目信息
			AppProjectDetailCustomize borrow = new AppProjectDetailCustomize();
			borrow = projectService.selectProjectDetail(borrowNid);
			if (borrow != null && borrow.getBorrowPublisher() != null && borrow.getBorrowPublisher().equals("中商储")) {
				// 中商储融通宝开户协议
				modeAndView = new ModelAndView("invest/rtb-open-contract-zsc");
			} else {
				// 嘉诺融通宝开户协议
				modeAndView = new ModelAndView("invest/rtb-open-contract");
			}
			if (borrow != null) {
				borrow.setBorrowPeriod(borrow.getBorrowPeriod().substring(0, borrow.getBorrowPeriod().length() - 1));
			}
			modeAndView.addObject("projectDeatil", borrow);
		} else if (pageType.equals("app_hjh_contract")) {
            // hjh散标风险揭示书H5
            modeAndView = new ModelAndView(AgreementDefine.CONFIRMATION_OF_INVESTMENT_RISK_PATH);
        }
		LogUtil.endLog(InvestDefine.class.getName(), InvestDefine.GODETAIL_MAPPING);
		return modeAndView;
	}
	
	/**
	 * 
	 * 获取出借url
	 * 
	 * @author 王坤
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = InvestDefine.TENDER_URL_ACTION)
	public JSONObject getTenderUrl(@ModelAttribute AppTenderVo vo, HttpServletRequest request) {
		JSONObject info = new JSONObject();
		// add by zhangjp 优惠券出借 start
		// 优惠券用户id （coupon_user的id）
		String couponGrantId = request.getParameter("couponGrantId");
		if(couponGrantId==null){
		    couponGrantId = (String)request.getAttribute("couponGrantId");
		}
		// add by zhangjp 优惠券出借 end
		// add by liuyang 债转出借 start
		// 债转编号
		String borrowNid = request.getParameter("borrowNid");
		String investType = borrowNid.substring(0, 3);
		String borrowType = request.getParameter("borrowType"); // 项目类型 HJH传HJH
		// add by liuyang 神策数据统计 20180824 start
		String presetProps = request.getParameter("presetProps");
		// add by liuyang 神策数据统计 20180824 end
		if ("HJH".equals(borrowType)) {
			investType = "HJH";
		}
		String creditNid = borrowNid.substring(3);
		if (vo.getAccount() == null || "".equals(vo.getAccount())) {
			vo.setAccount("0");
		}
		// add by liuyang 债转出借 end
		if ((Validator.isNull(vo.getAccount()) && StringUtils.isEmpty(couponGrantId)) ||!CommonUtils.isNumber(vo.getAccount()) || Validator.isNull(vo.getBorrowNid())) {
			info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
			info.put(CustomConstants.APP_STATUS_DESC, "请求参数非法");
			info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
		} else {// 拼接充值地址并返回
			// 唯一标识
			String sign = request.getParameter("sign");
			// 出借平台
			String platform = request.getParameter("platform");
			// 用户id
			int userId = SecretUtil.getUserId(sign);
			// 校验相应的出借参数
			// modify by zhangjp 优惠券相关 start
			JSONObject result = null;
			int couponOldTime = Integer.MIN_VALUE;
			CouponConfigCustomizeV2 cuc = null;
			//logger.info("couponGrantId is:{}, userId is:{}", couponGrantId, userId);
			logger.info("用户:{}出借开始，使用优惠券:{}", userId, couponGrantId);
			if (StringUtils.isNotEmpty(couponGrantId) && new Integer(couponGrantId) > 0) {
				cuc = investService.getCouponUser(couponGrantId, userId);
				// 排他check用
				couponOldTime = cuc.getUserUpdateTime();
			}
			Users user = this.appTenderCreditService.getUsers(userId);
			// 汇转让出借
			if ("HZR".equals(investType)) {
				if (user != null) {
					if (user.getStatus() == 1) {
						String message = "用户已被禁用";
						info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
						info.put(CustomConstants.APP_STATUS_DESC, message);
						info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
						return info;
					}
					// 服务费授权校验
					if (!authService.checkPaymentAuthStatus(userId)) {
						String message = "用户未进行服务费授权";
						info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
						info.put(CustomConstants.APP_STATUS_DESC, message);
						info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
						return info;
					}
				} else {
					String message = "用户不存在";
					info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
					info.put(CustomConstants.APP_STATUS_DESC, message);
					info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
					return info;
				}
				// 债转出借校验
				UsersInfo usersInfo = this.appTenderCreditService.getUsersInfoByUserId(userId);
				if (null != usersInfo) {
					String roleIsOpen = PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN);
					if(StringUtils.isNotBlank(roleIsOpen) && roleIsOpen.equals("true")){
						if (usersInfo.getRoleId() != 1) {
							String message = "仅限出借人进行出借";
							info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
							info.put(CustomConstants.APP_STATUS_DESC, message);
							info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
							return info;
						}
					}


				} else {
					String message = "账户信息异常";
					info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
					info.put(CustomConstants.APP_STATUS_DESC, message);
					info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
					return info;
				}
				// 取得承接债转的用户在汇付天下的客户号
				BankOpenAccount accountChinapnrCrediter = appTenderCreditService.getBankOpenAccount(userId);
				if (accountChinapnrCrediter == null || Validator.isNull(accountChinapnrCrediter.getAccount())) {
					String message = "您还未开户，请开户后重新操作";
					info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
					info.put(CustomConstants.APP_STATUS_DESC, message);
					info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
					return info;
				}
				/**************************** 风险测评强制测评标示 开始 pcc ***********************************/
				// 新加参数userEvaluationResultFlag 状态码 0已测评 100未测评
				Integer userEvaluationResultFlag = user.getIsEvaluationFlag();
				if (0 == userEvaluationResultFlag) {
					String message = "根据监管要求，出借前必须进行风险测评。";
					info.put(CustomConstants.APP_STATUS, "100");
					info.put(CustomConstants.APP_STATUS_DESC, message);
					info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
					info.put("tenderUrl", CommonUtils.concatReturnUrl(request, PropUtils.getSystem("hyjf.web.host") + RiskAssesmentDefine.REQUEST_MAPPING + RiskAssesmentDefine.USER_RISKTEST));
					return info;
				} else {
					//是否完成风险测评
					if (user.getIsEvaluationFlag()==1 && null != user.getEvaluationExpiredTime()) {
						//测评到期日
						Long lCreate = user.getEvaluationExpiredTime().getTime();
						//当前日期
						Long lNow = System.currentTimeMillis();
						if (lCreate <= lNow) {
							//已过期需要重新评测
							String message = "根据监管要求，出借前必须进行风险测评。";
							info.put(CustomConstants.APP_STATUS, "100");
							info.put(CustomConstants.APP_STATUS_DESC, message);
							info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
							info.put("tenderUrl", CommonUtils.concatReturnUrl(request, PropUtils.getSystem("hyjf.web.host") + RiskAssesmentDefine.REQUEST_MAPPING + RiskAssesmentDefine.USER_RISKTEST));
							return info;
						}
					} else {
						//未获取到评测数据或者评测时间
						String message = "根据监管要求，出借前必须进行风险测评。";
						info.put(CustomConstants.APP_STATUS, "100");
						info.put(CustomConstants.APP_STATUS_DESC, message);
						info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
						info.put("tenderUrl", CommonUtils.concatReturnUrl(request, PropUtils.getSystem("hyjf.web.host") + RiskAssesmentDefine.REQUEST_MAPPING + RiskAssesmentDefine.USER_RISKTEST));
						return info;
					}
				}
				/**************************** 风险测评强制测评标示 结束 pcc ***********************************/
				if (StringUtils.isEmpty(creditNid) || StringUtils.isEmpty(vo.getAccount())) {
					String message = "债转编号和承接本金不能为空";
					info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
					info.put(CustomConstants.APP_STATUS_DESC, message);
					info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
					return info;
				} else {
					if (!vo.getAccount().matches("^[-+]?(([0-9]+)(([0-9]+))?|(([0-9]+))?)$") || !Validator.isNumber(creditNid)) {
						String message = "债转编号和承接本金必须是数字格式";
						info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
						info.put(CustomConstants.APP_STATUS_DESC, message);
						info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
						return info;
					}
				}

				// 验证用户余额是否可以债转
				Account account = this.appTenderCreditService.getAccount(userId);
				if (account.getBankBalance() != null && account.getBankBalance().compareTo(BigDecimal.ZERO) == 1) {
					// 获取债转的详细参数
					TenderToCreditAssignCustomize creditAssign = this.appTenderCreditService.getInterestInfo(creditNid, vo.getAccount(), userId);
					String assignPay = creditAssign.getAssignTotal();
					if (account.getBankBalance().compareTo(new BigDecimal(assignPay))<0) {
						String message = "可用金额不足";
						info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
						info.put(CustomConstants.APP_STATUS_DESC, message);
						info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
						return info;
					}
				} else {
					String message = "可用金额不足";
					info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
					info.put(CustomConstants.APP_STATUS_DESC, message);
					info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
					return info;
				}
				info = this.appTenderCreditService.checkCreditTenderParam(creditNid, vo.getAccount(), String.valueOf(userId), platform, account.getBankBalance());
				if ("1".equals(info.get("status"))) {
					info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
					info.put(CustomConstants.APP_STATUS_DESC, info.get(CustomConstants.APP_STATUS_DESC));
					info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
					return info;
				}

				StringBuffer sb = new StringBuffer(HOST + InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.INVEST_ACTION);
				sb.append("?version=").append(vo.getVersion()).append(CustomConstants.APP_PARM_AND).append("netStatus=").append(vo.getNetStatus()).append(CustomConstants.APP_PARM_AND)
						.append("platform=").append("3".equals(vo.getPlatform())?2:vo.getPlatform()).append(CustomConstants.APP_PARM_AND).append("token=").append(strEncode(vo.getToken())).append(CustomConstants.APP_PARM_AND)
						.append("sign=").append(vo.getSign()).append(CustomConstants.APP_PARM_AND).append("randomString=").append(vo.getRandomString()).append(CustomConstants.APP_PARM_AND)
						.append("order=").append(strEncode(vo.getOrder())).append(CustomConstants.APP_PARM_AND).append("account=").append(vo.getAccount()).append(CustomConstants.APP_PARM_AND)
						.append("investType=").append("HZR").append(CustomConstants.APP_PARM_AND).append("creditNid=").append(creditNid).append(CustomConstants.APP_PARM_AND).append("realPlatform=").append(vo.getPlatform());

				info.put("tenderUrl", sb.toString());
				info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
				info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
				info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
				return info;
			} else if("HJH".equals(investType)){
			    // 2017-11-08 by sunss  汇计划验证判断
			    // 先进行check  然后 进行出借
			    // check start
			    // 取得承接债转的用户在汇付天下的客户号
	            BankOpenAccount accountChinapnrCrediter = appTenderCreditService.getBankOpenAccount(userId);
	            if (accountChinapnrCrediter == null || Validator.isNull(accountChinapnrCrediter.getAccount())) {
	                String message = "您还未开户，请开户后重新操作";
	                info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
	                info.put(CustomConstants.APP_STATUS_DESC, message);
	                info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
	                return info;
	            }
	            /**************************** 风险测评强制测评标示 开始 pcc ***********************************/
	            // 新加参数userEvaluationResultFlag 状态码 0已测评 100未测评
	            Integer userEvaluationResultFlag = user.getIsEvaluationFlag();
	            if (0 == userEvaluationResultFlag) {
	                String message = "根据监管要求，出借前必须进行风险测评。";
	                info.put(CustomConstants.APP_STATUS, "100");
	                info.put(CustomConstants.APP_STATUS_DESC, message);
	                info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
	                info.put("tenderUrl", CommonUtils.concatReturnUrl(request, PropUtils.getSystem("hyjf.web.host") + RiskAssesmentDefine.REQUEST_MAPPING + RiskAssesmentDefine.USER_RISKTEST));
	                return info;
	            } else {
					//是否完成风险测评
					if (user.getIsEvaluationFlag()==1 && null != user.getEvaluationExpiredTime()) {
						//测评到期日
						Long lCreate = user.getEvaluationExpiredTime().getTime();
						//当前日期
						Long lNow = System.currentTimeMillis();
						if (lCreate <= lNow) {
							//已过期需要重新评测
							String message = "根据监管要求，出借前必须进行风险测评。";
							info.put(CustomConstants.APP_STATUS, "100");
							info.put(CustomConstants.APP_STATUS_DESC, message);
							info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
							info.put("tenderUrl", CommonUtils.concatReturnUrl(request, PropUtils.getSystem("hyjf.web.host") + RiskAssesmentDefine.REQUEST_MAPPING + RiskAssesmentDefine.USER_RISKTEST));
							return info;
						}
					} else {
						//未获取到评测数据或者评测时间
						String message = "根据监管要求，出借前必须进行风险测评。";
						info.put(CustomConstants.APP_STATUS, "100");
						info.put(CustomConstants.APP_STATUS_DESC, message);
						info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
						info.put("tenderUrl", CommonUtils.concatReturnUrl(request, PropUtils.getSystem("hyjf.web.host") + RiskAssesmentDefine.REQUEST_MAPPING + RiskAssesmentDefine.USER_RISKTEST));
						return info;
					}
				}

			    result  = planService.checkHJHParam(vo.getBorrowNid(), vo.getAccount(), String.valueOf(userId), platform, cuc);
				logger.info("===================yinhui getTenderURL 返回信息为: " + result);
				if (!result.get(CustomConstants.APP_STATUS).equals("1") && StringUtils.isNotEmpty(couponGrantId) && new Integer(couponGrantId) > 0) {
					// 体验金出借（无本金）
					// 根据项目编号获取相应的项目
					// 校验优惠券
					result = CommonSoaUtils.planCheckCoupon(userId + "", borrowNid, vo.getAccount(),platform, couponGrantId);

				}
			    if(result == null || CustomConstants.APP_STATUS_FAIL.equals(result.getString(CustomConstants.APP_STATUS))){
			        // check 失败
			        result.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
			        return result;
			    }


				// 区别每次请求  先打印看看
			    String tender_token = GetDate.getCalendar().getTimeInMillis() + GetCode.getRandomCode(5);
			    StringBuffer sb = new StringBuffer(HOST + InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.INVEST_ACTION);
                sb.append("?version=").append(vo.getVersion()).append(CustomConstants.APP_PARM_AND).append("netStatus=").append(vo.getNetStatus()).append(CustomConstants.APP_PARM_AND)
                        .append("platform=").append("3".equals(vo.getPlatform())?2:vo.getPlatform()).append(CustomConstants.APP_PARM_AND).append("token=").append(strEncode(vo.getToken())).append(CustomConstants.APP_PARM_AND)
                        .append("sign=").append(vo.getSign()).append(CustomConstants.APP_PARM_AND).append("randomString=").append(vo.getRandomString()).append(CustomConstants.APP_PARM_AND)
                        .append("order=").append(strEncode(vo.getOrder())).append(CustomConstants.APP_PARM_AND).append("account=").append(vo.getAccount()).append(CustomConstants.APP_PARM_AND)
                        .append("investType=").append("HJH").append(CustomConstants.APP_PARM_AND).append("creditNid=").append(creditNid).append(CustomConstants.APP_PARM_AND).append("couponGrantId=").append(couponGrantId).append("&borrowNid=").append(borrowNid)
                        .append(CustomConstants.APP_PARM_AND).append("borrowType=").append("HJH").append(CustomConstants.APP_PARM_AND).append("realPlatform=").append(vo.getPlatform())
                        .append(CustomConstants.APP_PARM_AND).append("tenderToken=").append(tender_token);
				// add by liuyang 神策数据统计 20180824 start
				if (StringUtils.isNotBlank(presetProps)) {
					try {
						presetProps = URLEncoder.encode(presetProps,"UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					sb.append(CustomConstants.APP_PARM_AND).append("presetProps=").append(presetProps);
				}
				logger.info("出借URL:" + sb.toString());
				//  add by liuyang 神策数据统计 20180824 end
                // 如果用户连续点击两次   则返回空
                /*if(RedisUtils.exists("HJHUserTender"+userId)){
                    logger.info("用户连续点击两次加入计划");
                    info.put("tenderUrl", "");
                    info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
                    info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
                    info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
                    return info;
                }
                RedisUtils.set("HJHUserTender"+userId, tender_token , 60);*/
                info.put("tenderUrl", sb.toString());
                info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
                info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
                info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
                return info;
			}
			
			
			result = investService.checkParam(vo.getBorrowNid(), vo.getAccount(), String.valueOf(userId), platform, cuc);
			logger.info("===================cwyang getTenderURL 返回信息为: " + result);
			if (!result.get(CustomConstants.APP_STATUS).equals("1") && StringUtils.isNotEmpty(couponGrantId) && new Integer(couponGrantId) > 0) {
				// 体验金出借（无本金）
				// 根据项目编号获取相应的项目
				Borrow borrow = investService.getBorrowByNid(vo.getBorrowNid());
				result = investService.checkParamForCoupon(borrow, vo, String.valueOf(userId), cuc, couponGrantId);
			}
			// modify by zhangjp 优惠券相关 end

			if (result == null || result.get(CustomConstants.APP_STATUS).equals("1")) {
				info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
				info.put(CustomConstants.APP_STATUS_DESC, result.get(CustomConstants.APP_STATUS_DESC));
				info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
			} else {
				/**************************** 风险测评强制测评标示 开始 pcc ***********************************/
				// 新加参数userEvaluationResultFlag 状态码 0已测评 100未测评
				Integer userEvaluationResultFlag = user.getIsEvaluationFlag();
				if (0 == userEvaluationResultFlag) {
					String message = "根据监管要求，出借前必须进行风险测评。";
					info.put(CustomConstants.APP_STATUS, "100");
					info.put(CustomConstants.APP_STATUS_DESC, message);
					info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
					info.put("tenderUrl", CommonUtils.concatReturnUrl(request, PropUtils.getSystem("hyjf.web.host") + RiskAssesmentDefine.REQUEST_MAPPING + RiskAssesmentDefine.USER_RISKTEST));
					return info;
				} else {
					//是否完成风险测评
					if (user.getIsEvaluationFlag()==1 && null != user.getEvaluationExpiredTime()) {
						//测评到期日
						Long lCreate = user.getEvaluationExpiredTime().getTime();
						//当前日期
						Long lNow = System.currentTimeMillis();
						if (lCreate <= lNow) {
							//已过期需要重新评测
							String message = "根据监管要求，出借前必须进行风险测评。";
							info.put(CustomConstants.APP_STATUS, "100");
							info.put(CustomConstants.APP_STATUS_DESC, message);
							info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
							info.put("tenderUrl", CommonUtils.concatReturnUrl(request, PropUtils.getSystem("hyjf.web.host") + RiskAssesmentDefine.REQUEST_MAPPING + RiskAssesmentDefine.USER_RISKTEST));
							return info;
						}
					} else {
						//未获取到评测数据或者评测时间
						String message = "根据监管要求，出借前必须进行风险测评。";
						info.put(CustomConstants.APP_STATUS, "100");
						info.put(CustomConstants.APP_STATUS_DESC, message);
						info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
						info.put("tenderUrl", CommonUtils.concatReturnUrl(request, PropUtils.getSystem("hyjf.web.host") + RiskAssesmentDefine.REQUEST_MAPPING + RiskAssesmentDefine.USER_RISKTEST));
						return info;
					}
				}
				/**************************** 风险测评强制测评标示 结束 pcc ***********************************/
				StringBuffer sb = new StringBuffer(HOST + InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.INVEST_ACTION);
				sb.append("?version=").append(vo.getVersion()).append(CustomConstants.APP_PARM_AND).append("netStatus=").append(vo.getNetStatus()).append(CustomConstants.APP_PARM_AND)
						.append("platform=").append("3".equals(vo.getPlatform())?2:vo.getPlatform()).append(CustomConstants.APP_PARM_AND).append("token=").append(strEncode(vo.getToken())).append(CustomConstants.APP_PARM_AND)
						.append("sign=").append(vo.getSign()).append(CustomConstants.APP_PARM_AND).append("randomString=").append(vo.getRandomString()).append(CustomConstants.APP_PARM_AND)
						.append("order=").append(strEncode(vo.getOrder())).append(CustomConstants.APP_PARM_AND).append("account=").append(vo.getAccount()).append(CustomConstants.APP_PARM_AND)
						.append("borrowNid=").append(vo.getBorrowNid()).append(CustomConstants.APP_PARM_AND).append("realPlatform=").append(vo.getPlatform());
				// add by zhangjp 优惠券相关 start
				if (StringUtils.isNotEmpty(couponGrantId) && new Integer(couponGrantId) > 0) {
					sb.append(CustomConstants.APP_PARM_AND).append("couponGrantId=").append(couponGrantId);
				}

				if (couponOldTime != Integer.MIN_VALUE) {
					sb.append(CustomConstants.APP_PARM_AND).append("couponOldTime=").append(couponOldTime);
				}
				// add by zhangjp 优惠券相关 end
				info.put("tenderUrl", sb.toString());
				// 输出出借url
				info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
				info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
				info.put(CustomConstants.APP_REQUEST, InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.TENDER_URL_ACTION);
			}
		}
		logger.info("==============cwyang getTenderUREL 返回结果 " + info);
		return info;
	}



	/**
	 * app出借
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = InvestDefine.INVEST_ACTION)
	public ModelAndView tender(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(InvestController.class.toString(), InvestDefine.INVEST_ACTION);
		logger.info("开始调用出借");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
        BaseMapBean baseMapBean=new BaseMapBean();
		// 唯一标识
		String sign = request.getParameter("sign");
		
		String token = request.getParameter("token");
		// 项目id
		String borrowNid = request.getParameter("borrowNid");
		// 出借金额
		String account = request.getParameter("account");
		// 出借平台
		String platform = request.getParameter("realPlatform");
		String message = "出借失败";
		// 用户id
		Integer userId = SecretUtil.getUserId(sign);
		// add by liuyang start
		// 出借类型
		String investType = request.getParameter("investType");
		String borrowType = request.getParameter("borrowType"); // 项目类型  HJH传HJH
        if(borrowType!=null&&"HJH".equals(borrowType)){
            investType = "HJH";
        }
		// 债转编号
		String creditNid = request.getParameter("creditNid");
		// add by zhangjp 优惠券出借 start
		String couponGrantId = request.getParameter("couponGrantId");

		logger.info("根据investType存储备注="+investType);
		String dayOrMonth ="";
		if ("HJH".equals(investType)) {
			logger.info("出借开始，userId:" + userId + ", creditNid:" + creditNid + ", tenderMoney:" + account + ", couponGrantId:" + couponGrantId);
			HjhPlan plan =  planService.getPlanByNid(investType + creditNid);
			logger.info("开始获取查询的数据="+JSON.toJSONString(plan));
			// 是否月标(true:月标, false:天标)
			String lockPeriod = plan.getLockPeriod().toString();
			if (plan.getIsMonth().intValue()!=0) {//月标
				dayOrMonth = lockPeriod + "个月智投";
			} else {
				dayOrMonth = lockPeriod + "天智投";
			}
		}else if ("HZR".equals(investType)){
			logger.info("出借开始，userId:" + userId + ", creditNid:" + creditNid +  "investType:" + investType + ", tenderMoney:" + account + ", couponGrantId:" + couponGrantId);
			BorrowCredit borrowCredit = appTenderCreditService.getBorrowCredit(creditNid);
			String hborrowNid = borrowCredit.getBidNid();
			Borrow borrow = investProjectService.selectBorrowByBorrowNid(hborrowNid);
			logger.info("开始获取查询的数据="+JSON.toJSONString(borrow));
			// 是否月标(true:月标, false:天标)
			boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrow.getBorrowStyle()) || CustomConstants.BORROW_STYLE_MONTH.equals(borrow.getBorrowStyle())
					|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrow.getBorrowStyle())|| CustomConstants.BORROW_STYLE_END.equals(borrow.getBorrowStyle());

			String lockPeriod = String.valueOf(borrow.getBorrowPeriod());
			if (isMonth) {//月标
				dayOrMonth = lockPeriod + "个月债转";
			} else {
				dayOrMonth = lockPeriod + "天债转";
			}
		}else {
			logger.info("出借开始，userId:" + userId + ", borrowNid:" + borrowNid + ", tenderMoney:" + account + ", couponGrantId:" + couponGrantId);
			Borrow borrow = investProjectService.selectBorrowByBorrowNid(borrowNid);
			logger.info("开始获取查询的数据="+JSON.toJSONString(borrow));
			// 是否月标(true:月标, false:天标)
			boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrow.getBorrowStyle()) || CustomConstants.BORROW_STYLE_MONTH.equals(borrow.getBorrowStyle())
					|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrow.getBorrowStyle())|| CustomConstants.BORROW_STYLE_END.equals(borrow.getBorrowStyle());

			String lockPeriod = String.valueOf(borrow.getBorrowPeriod());
			if (isMonth) {//月标
				dayOrMonth = lockPeriod + "个月散标";
			} else {
				dayOrMonth = lockPeriod + "天散标";
			}
		}
		Users users = planService.getUsers(userId);
		UsersInfo usersInfo = planService.getUsersInfoByUserId(userId);
		UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
		userOperationLogEntity.setOperationType(4);
		userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
		userOperationLogEntity.setPlatform(Integer.valueOf(platform));
		userOperationLogEntity.setRemark(dayOrMonth);
		userOperationLogEntity.setOperationTime(new Date());
		userOperationLogEntity.setUserName(users.getUsername());
		userOperationLogEntity.setUserRole(String.valueOf(usersInfo.getRoleId()));
		appUserService.sendUserLogMQ(userOperationLogEntity);
		// 汇转让出借
		if ("HZR".equals(investType)) {
			//zhaizhuan
			modelAndView = this.tenderCredit(request, response, modelAndView, userId, account, creditNid, platform);
			return modelAndView;

		}
		// 汇计划出借
		if ("HJH".equals(investType)) {
		 // check 通过了  就 出借
		    JSONObject result;
		    String tenderToken = request.getParameter("tenderToken");
		    // 先打印个日志
		    logger.info("用户请求加入计划Userid: "+userId+"   tenderToken："+tenderToken);
		    // 先设置上redis的值    假如用户已经发起加入计划了  就返回  操作中
		    // 否则就允许执行
		    if(RedisUtils.exists("HJHUserTender"+userId)){
		        String redisTenderToken = RedisUtils.get("HJHUserTender"+userId);
		        if(!redisTenderToken.equals(tenderToken)){
		            logger.info("用户已经在加入计划了: "+userId+"加入的计划："+borrowNid);
	                // 有了就返回
	                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
	                baseMapBean.set(CustomConstants.APP_STATUS_DESC,"您正在出借，请稍后再试......");
	                baseMapBean.setCallBackAction(
	                        CustomConstants.HOST + InvestDefine.JUMP_HTML_HJH_HANDLING_PATH.replace("{planId}", borrowNid));
	                modelAndView.addObject("callBackForm", baseMapBean);
	                return modelAndView;
		        }
		    }
		    String value = GetDate.getCalendar().getTimeInMillis() + GetCode.getRandomCode(5);
            // 设置过期时间五分钟
            RedisUtils.set("HJHUserTender"+userId, value , 300);
            /**
		    
			/**
			 * 这个方法目前设计有问题，有闲心的可以改（要么通过result,要么通过modelAndView，两个都给，还分场景，要闹哪样），逻辑太多， 具体实现如下:  看这段代码要小心
			 * 1. 如果只有优惠券出借，result和modelAndView 返回的数据都是正确的，
			 * 	  格式:  result: {"account":"0","accountDecimal":"500.00","couponGrantId":"10694","couponInterest":"0.61","couponQuota":"500.00","couponType":"代金券","earnings":"0.61","interest":"0.61","investType":"3","status":"0","statusDesc":"出借成功！"}
			 * 	  		modelAndView: {"plan":1,"planNid":"HJH201712200001","couponInterest":"0.61","interest":"0.61","account":"0","earnings":"0","couponType":"3","couponQuota":"500.00"}
			 * 2. 如果只有本金出借:
			 * 		result: {"account":"1,000.00","earnings":1.22,"status":"0","statusDesc":"恭喜您出借成功!"}
			 * 		modelAndView: {"plan":0,"planNid":"HJH201712200001","couponInterest":"0","earnings":1.22,"couponType":"0","account":"1,000.00"}
			 * 3. 本金出借和优惠券出借都有的情况
			 * 本金的出借结果通过result返回， 格式
			 * 	result: {"account":"1,000.00","earnings":1.66,"status":"0","statusDesc":"恭喜您出借成功!"}
			 * modelAndView返回了优惠券出借和真实资金出借的信息 格式
			 * modelMap: {"plan":0,"planNid":"HJH201712199","couponInterest":"0.38","couponType":2,"couponQuota":"2.00","investDesc":"出借成功！","earnings":1.66,"account":"1,000.00"}
			 */
		    try {
				result = planService.updateInvestInfo(modelAndView,borrowNid, account, String.valueOf(userId), platform,GetCilentIP.getIpAddr(request),couponGrantId);
		    } catch (Exception e) {
		    	result = new JSONObject();
		        result.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
		        result.put(CustomConstants.APP_STATUS_DESC, "抱歉，出借失败，请重试！");
		    } finally {
		        RedisUtils.del("HJHUserTender"+userId);
            }
			ModelMap map = modelAndView.getModelMap();
			logger.info("请求出借返回结果result: "+result);
			logger.info("请求出借返回结果modelAndView: "+ JSONObject.toJSONString(map));
			if (result != null && result.get(CustomConstants.APP_STATUS).equals(CustomConstants.APP_STATUS_SUCCESS)) {
				//MQ  加入汇计划，出借触发奖励
				logger.info("加入计划用户:" + userId +",planOrderId:"+ result.get("planOrderId")+"发送mq");
				if (!"null".equals(String.valueOf(result.get("planOrderId")))){
					sendMQActivity(userId,String.valueOf(result.get("planOrderId")),new BigDecimal(account),3);
					sendRrturnCashActivity(userId,String.valueOf(result.get("planOrderId")),new BigDecimal(account),3);
				}

				// 成功
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
				baseMapBean.set(CustomConstants.APP_STATUS_DESC, "出借成功！");
				baseMapBean.set("amount", CommonUtils.formatAmount(account));

				String couponType = (map == null ? "0" : String.valueOf(map.get("couponType")));
				baseMapBean.set("couponType", couponType);

				String couponQuota = (map == null ? "" : String.valueOf(map.get("couponQuota")));

				// 优惠券利息
				String couponInterestStr = (map == null ? "" : String.valueOf(map.get("couponInterest")));
				BigDecimal couponInterest = new BigDecimal(couponInterestStr);
				// 本金出借利息
				String earningsStr = (map == null ? "" : String.valueOf(map.get("earnings")));
				BigDecimal earnings = new BigDecimal(earningsStr);

				// 历史回报
				BigDecimal capitalInterest = couponInterest.add(earnings);
				// 代金券需要加上券的面值
				if ("3".equals(couponType)) {
					logger.info("代金券加上券的面值....");
					capitalInterest = capitalInterest.add(new BigDecimal(couponQuota));
				}

				// 加息券增加%
				if ("2".equals(couponType)) {
					try {
						couponQuota = URLEncoder.encode(couponQuota.concat("%"), "utf-8");
					} catch (UnsupportedEncodingException e) {
						logger.error("URLEncoder编码错误...." ,e);
					}
					logger.info("加息券面额....{}", couponQuota);
				}
				baseMapBean.set("couponValue", couponQuota);

				//capitalInterest.add(new BigDecimal(account));
				logger.info("计算出的历史回报, {}", String.valueOf(capitalInterest));
				baseMapBean.set("income", String.valueOf(capitalInterest));
				baseMapBean.setCallBackAction(
						CustomConstants.HOST + InvestDefine.JUMP_HTML_HJH_SUCCESS_PATH.replace("{planId}", borrowNid));
				logger.info("baseMapBean: {}", JSONObject.toJSONString(baseMapBean));
				modelAndView.addObject("callBackForm", baseMapBean);

				// add by liuyang 神策数据统计 20180824 start
				// 加入计划成功后,获取前端预置属性
				String presetProps = request.getParameter("presetProps");
				logger.info("预置属性presetProps为:[" + presetProps + "].");
				// 如果不为空
				if (StringUtils.isNotBlank(presetProps)) {
					try {
						presetProps = URLDecoder.decode(presetProps,"UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					SensorsDataBean sensorsDataBean = new SensorsDataBean();
					// 将json串转换成Bean
					try {
						Map<String, Object> sensorsDataMap = JSONObject.parseObject(presetProps, new TypeReference<Map<String, Object>>() {
						});
						sensorsDataBean.setPresetProps(sensorsDataMap);
						sensorsDataBean.setUserId(userId);
						sensorsDataBean.setEventCode("submit_intelligent_invest");
						sensorsDataBean.setOrderId(String.valueOf(result.get("accedeOrderId")));
						// 发送神策数据统计MQ
						this.planService.sendSensorsDataMQ(sensorsDataBean);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// add by liuyang 神策数据统计 20180824 end
			} else {
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
				baseMapBean.set(CustomConstants.APP_STATUS_DESC,
						result.get(CustomConstants.APP_STATUS_DESC).toString());
				baseMapBean.setCallBackAction(
						CustomConstants.HOST + InvestDefine.JUMP_HTML_HJH_FAILED_PATH.replace("{planId}", borrowNid));
				modelAndView.addObject("callBackForm", baseMapBean);
				return modelAndView;
			}
		    return modelAndView;
        }
		// add by liuyang start
		
		// 优惠券用户id （coupon_user的id）
		// 更新时间 排他check用
		int couponOldTime = StringUtils.isEmpty(request.getParameter("couponOldTime")) ? Integer.MIN_VALUE : Integer.valueOf(request.getParameter("couponOldTime"));
		CouponConfigCustomizeV2 cuc = null;
		// 如果有优惠券出借
		if (StringUtils.isNotEmpty(couponGrantId) && new Integer(couponGrantId) > 0) {
			// 优惠券出借
			cuc = investService.getCouponUser(couponGrantId, userId);
		}
		// 如果没有本金出借且有优惠券出借
		BigDecimal decimalAccount = StringUtils.isNotEmpty(account) ? new BigDecimal(account) : BigDecimal.ZERO;
		if (decimalAccount.compareTo(BigDecimal.ZERO) != 1 && cuc != null && (cuc.getCouponType() == 3 || cuc.getCouponType() == 1)) {
			this.couponTender(modelAndView, request, cuc, userId, couponOldTime);
			return modelAndView;
		}
		modelAndView.setViewName(InvestDefine.JSP_CHINAPNR_SEND);
		// add by zhangjp 优惠券出借 end
		if (userId == null) {
			message = "用户未登录,请登录！";
			// 回调url（h5错误页面）
//			modelAndView = new ModelAndView(InvestDefine.INVEST_ERROR_PATH);
//			modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
			modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未登录,请登录！");
            baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_APP_FAILED_PATH.replace("{borrowId}", borrowNid));
            modelAndView.addObject("callBackForm", baseMapBean);
			return modelAndView;
		} else {
			logger.info("tender出借用户id: " + userId + "~~~~~~~~~~标号: " + borrowNid);
			// 校验相应的出借参数
			JSONObject result = investService.checkParam(borrowNid, account, String.valueOf(userId.intValue()), platform, cuc);
			logger.info("=========cwyang 出借校验返回结果: " + result);
			if (result == null || result.get(CustomConstants.APP_STATUS).equals("1")) {
				// 回调url（h5错误页面）
//			    modelAndView = new ModelAndView(InvestDefine.INVEST_ERROR_PATH);
//				modelAndView.addObject(CustomConstants.APP_STATUS_DESC, result.get(CustomConstants.APP_STATUS));
				
				modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
	            baseMapBean.set(CustomConstants.APP_STATUS_DESC, (result.get(CustomConstants.APP_STATUS) == null?"":result.get(CustomConstants.APP_STATUS).toString()));
	            baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_APP_FAILED_PATH.replace("{borrowId}", borrowNid));
	            modelAndView.addObject("callBackForm", baseMapBean);
				return modelAndView;
			}
			Map<String, String> map = new HashMap<String, String>();
			// 出借用户汇付客户号
			String tenderUsrcustid = result.getString("tenderUsrcustid");
			String tenderUserName = result.getString("tenderUserName");
			// 借款人汇付客户号
			// String borrowerUsrcustid = result.getString("borrowerUsrcustid");
			// 生成订单id
			String orderId = GetOrderIdUtils.getOrderId2(Integer.valueOf(userId));
			// 标的号
			// String borrowId = result.getString("borrowId");
			// 写日志
			Boolean flag = investService.updateBeforeChinaPnR(borrowNid, orderId, Integer.valueOf(userId), account, GetCilentIP.getIpAddr(request),couponGrantId,tenderUserName);
			// 日志插入成功后调用出借接口
			if (flag) {
				// 获取共同参数
				String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
				String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
				// 同步回调路径
				String returl = HOST + InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.RETURL_SYN_ACTION + "?userId=" + userId + "&platform=" + platform + "&couponGrantId="
						+ couponGrantId + "&couponUpdateTime=" + couponOldTime;
				// 异步回调路径
				String bgRetUrl = HOST + InvestDefine.REQUEST_HOME + InvestDefine.REQUEST_MAPPING + InvestDefine.RETURL_ASY_ACTION + "?userId=" + userId + "&platform=" + platform + "&couponGrantId="
						+ couponGrantId + "&couponUpdateTime=" + couponOldTime;
				//忘记密码url
				String forgetPassworedUrl = CustomConstants.FORGET_PASSWORD_URL + "?sign=" + sign + "&token=" + token+"&platform="+platform;

				BankCallBean bean = new BankCallBean();
				bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
				bean.setTxCode(BankCallConstant.TXCODE_BID_APPLY);// 交易代码
				bean.setInstCode(instCode);
				bean.setBankCode(bankCode);
				bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
				bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
				bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
				bean.setChannel(BankCallConstant.CHANNEL_WEI);// 交易渠道
				bean.setAccountId(tenderUsrcustid);// 电子账号
				bean.setOrderId(orderId);// 订单号
				bean.setTxAmount(CustomUtil.formatAmount(account));// 交易金额
				bean.setProductId(borrowNid);// 标的号
				bean.setFrzFlag(BankCallConstant.DEBT_FRZFLAG_UNFREEZE);// 是否冻结金额  modify by cwyang 2017-10-25 实时放款出借不冻结
				bean.setForgotPwdUrl(forgetPassworedUrl);
				bean.setSuccessfulUrl(returl+"&isSuccess=1");// 银行同步返回地址
				bean.setRetUrl(returl);// 银行同步返回地址
				bean.setNotifyUrl(bgRetUrl);// 银行异步返回地址
				bean.setLogOrderId(orderId);// 订单号
				bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单日期
				bean.setLogIp(GetCilentIP.getIpAddr(request));// 客户IP
				bean.setLogUserId(String.valueOf(userId));// 出借用户
				bean.setLogUserName(tenderUserName);// 出借用户名
				bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_MOBILE_BIDAPPLY);// 银行请求详细url
				try {
					modelAndView = BankCallUtils.callApi(bean);
					return modelAndView;
				} catch (Exception e) {
					e.printStackTrace();
					map.put("error", "1");
					map.put("message", "出借失败");
					String url = JSON.toJSONString(map);
					try {
						url = URLEncoder.encode(url, "UTF-8");
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
					message = "江西银行接口调用失败";
					LogUtil.errorLog(InvestController.class.getName(), message, e);
//					modelAndView = new ModelAndView(InvestDefine.INVEST_ERROR_PATH);
//					modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
					
					modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
					baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
		            baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
		            baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_APP_FAILED_PATH.replace("{borrowId}", borrowNid));
		            modelAndView.addObject("callBackForm", baseMapBean);
					return modelAndView;
				}

			} else {
				message = "写入出借日志失败";
				LogUtil.errorLog(InvestController.class.getName(), message, null);
//				modelAndView = new ModelAndView(InvestDefine.INVEST_ERROR_PATH);
//				modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
				
				modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
	            baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
	            baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_APP_FAILED_PATH.replace("{borrowId}", borrowNid));
	            modelAndView.addObject("callBackForm", baseMapBean);
				return modelAndView;
			}
		}
	}

	/**
	 * 散标出借同步回调
	 * @param request
	 * @param bean
	 * @return
	 */
	@RequestMapping(InvestDefine.RETURL_SYN_ACTION)
	public ModelAndView tenderRetUrlSyn(HttpServletRequest request, @ModelAttribute BankCallBean bean) {

		ModelAndView modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
        BaseMapBean baseMapBean=new BaseMapBean();
		String logOrderId = StringUtils.isBlank(bean.getLogOrderId()) ? "" : bean.getLogOrderId();// 订单号
		int userId = StringUtils.isBlank(bean.getLogUserId()) ? 0 : Integer.parseInt(bean.getLogUserId());// 用户Userid
		String ip = bean.getUserIP();// 用户操作ip
		String couponGrantId = request.getParameter("couponGrantId");// 优惠券用户id
		String couponOldTime = StringUtils.isNotEmpty(request.getParameter("couponOldTime")) ? request.getParameter("couponOldTime") : "0";
		String respCode = bean.getRetCode();// 出借结果返回码
		String platForm = request.getParameter("platform");

		String accountId = StringUtils.isBlank(bean.getAccountId()) ? "" : bean.getAccountId();// 电子账号
		String isSuccess = request.getParameter("isSuccess");// 优惠券用户id
		// 打印返回码
		logger.info("散标出借同步回调, 用户: {} , 出借接口结果码: {}", userId, respCode);
		logger.info("出借订单号: {}， 优惠券id: {}", logOrderId, couponGrantId);
		logger.info("电子账号: {}", accountId);
		logger.info("isSuccess: {}", isSuccess);
		String message = "出借失败！";// 错误信息

		if (StringUtils.isBlank(respCode)||!"1".equals(isSuccess)) {
			// 测试有出现过retCode是空的情况，判断一下，增强代码健壮性
			// BorrowTender borrowTender = this.investService.getTenderByNid(logOrderId);
			// 有出借记录，则返回出借成功
			if (!isTenderSuccess(userId, accountId, logOrderId)) {
				String borrowNid = bean.getProductId();
				if(StringUtils.isNotBlank(borrowNid)){
					baseMapBean.setCallBackAction(CustomConstants.HOST
							+ InvestDefine.JUMP_HTML_APP_FAILED_PATH.replace("{borrowId}", borrowNid));
				} else {
					baseMapBean.setCallBackAction(CustomConstants.HOST+ InvestDefine.JUMP_HTML_APP_FAILED_PATH);
				}
				baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
				modelAndView.addObject("callBackForm", baseMapBean);
				return modelAndView;
			} else {
				respCode = BankCallConstant.RESPCODE_SUCCESS;
			}
		}
		
		if (!BankCallConstant.RESPCODE_SUCCESS.equals(respCode)&&!"1".equals(isSuccess)) {

            // 返回码提示余额不足
			if (BankCallConstant.RETCODE_BIDAPPLY_YUE_FAIL.equals(respCode)) {
				logger.info("用户:" + userId + "**出借接口调用失败，余额不足，错误码: " + respCode);
				message = "出借失败，可用余额不足！请联系客服.";
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
	            baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
	            baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_APP_FAILED_PATH.replace("{borrowId}", bean.getProductId()));
	            modelAndView.addObject("callBackForm", baseMapBean);
				return modelAndView;
			} else {
				message = bean.getRetMsg();
				logger.info("用户:" + userId + "**出借接口调用失败,系统订单号: " + logOrderId + "**接口返回错误码" + respCode);
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
	            baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
	            baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_APP_FAILED_PATH.replace("{borrowId}", bean.getProductId()));
	            modelAndView.addObject("callBackForm", baseMapBean);
				return modelAndView;
			}
		}

		bean.convert();
		String borrowNid = bean.getProductId();// 借款Id
		logger.info("=====cwyang borrowid 为 " + borrowNid);
		String account = bean.getTxAmount();// 借款金额
		logger.info("=====cwyang account 为 " + account);
		String orderId = bean.getOrderId();// 订单id
		logger.info("=====cwyang orderId 为 " + orderId);
		logger.info("=====cwyang platFrom 为 " + platForm);
		if (StringUtils.isNotEmpty(platForm)) {
			bean.setLogClient(Integer.parseInt(platForm));// add by cwyang 将出借平台放到bean中传输
		}
		// 根据借款Id检索标的信息
		BorrowWithBLOBs borrow = this.appTenderService.getBorrowByNid(borrowNid);

		logger.info("标号:" + borrowNid);
		if (StringUtils.isBlank(borrowNid)) {
			message = "回调时,borrowNid为空";
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
            baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_APP_FAILED_PATH.replace("{borrowId}", bean.getProductId()));
            modelAndView.addObject("callBackForm", baseMapBean);
			return modelAndView;
		}

		// 优惠券出借结果
		JSONObject couponTender = null;
		// 出借状态
		String status = BankCallConstant.STATUS_FAIL;

		try {
			// 进行出借 tendertmp锁住
			JSONObject tenderResult = this.appTenderService.userTender(borrow, bean);
			logger.info("出借结果: {}", tenderResult == null ? "" : tenderResult.toJSONString());
			// 出借成功
			if (tenderResult.getString("status").equals("1")) {
				logger.info("用户:" + userId + "***出借成功: " + account);
				message = "恭喜您出借成功!";
				status = BankCallConstant.STATUS_SUCCESS;
				if (StringUtils.isNotEmpty(couponGrantId)) {
					// 优惠券出借校验
					try {
						// 优惠券出借校验
						JSONObject couponCheckResult = CommonSoaUtils.CheckCoupon(userId + "", borrowNid, account,
								CustomConstants.CLIENT_PC, couponGrantId);
						logger.info("优惠券出借校验结果: {}", couponCheckResult == null ? "" : couponCheckResult.toJSONString());
						
						if (couponCheckResult != null) {
							int couponStatus = couponCheckResult.getIntValue("status");
							if (couponStatus == 0) {
								// 优惠券出借
								// couponTender = CommonSoaUtils.CouponInvestForPC(userId + "", borrowNid,
								// account, CustomConstants.CLIENT_PC, couponGrantId, orderId, ip,
								// couponOldTime);
								// update by pcc 放入汇直投优惠券使用mq队列 start

								Map<String, String> params = new HashMap<String, String>();
								params.put("mqMsgId", GetCode.getRandomCode(10));
								// 真实出借金额
								params.put("money", account);
								// 借款项目编号
								params.put("borrowNid", borrowNid);
								// 平台
								params.put("platform", platForm);
								// 优惠券id
								params.put("couponGrantId", couponGrantId);
								// ip
								params.put("ip", ip);
								// 真实出借订单号
								params.put("ordId", orderId);
								// 优惠券修改时间
								params.put("couponOldTime", couponOldTime + "");
								// 用户编号
								params.put("userId", userId + "");
								logger.info("优惠券出借任务push到redis queue, param is :{}", JSONObject.toJSONString(params));
								rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON,
										RabbitMQConstants.ROUTINGKEY_HZT_COUPON_TENDER,
										JSONObject.toJSONString(params));
								// update by pcc 放入汇直投优惠券使用mq队列 end
							}
						}
						
					} catch (Exception e) {
						logger.error("优惠券出借失败...", e);
					}
				}
			}
			// 出借失败 回滚redis
			else {
				// 更新tendertmp
				boolean updateFlag = appTenderService.updateBorrowTenderTmp(userId, borrowNid, orderId);
				// 更新失败，出借失败
				if (updateFlag) {
					if (tenderResult.getString("status").equals("-1")) {// 同步/异步 优先执行完毕
						// add by cwyang 增加redis防重校验 2017-08-02
						boolean checkTender = RedisUtils.tranactionSet("tendersuccess_orderid" + orderId, 20);
						if (!checkTender) {// 同步/异步 优先执行完毕
							message = "恭喜您出借成功!";
							status = BankCallConstant.STATUS_SUCCESS;
						} else {
							message = "投标失败,请联系客服人员!";
						}
					} else {
						// 出借失败,出借撤销
						try {
							boolean flag = appTenderService.bidCancel(userId, borrowNid, orderId, account);
							if (!flag) {
								message = "投标失败,请联系客服人员!";
							}
						} catch (Exception e) {
							logger.error("投标申请撤销异常...", e);
							message = "投标申请撤销失败,请联系客服人员!";
						}
					}
				} else {
					message = "恭喜您出借成功!";
					status = BankCallConstant.STATUS_SUCCESS;
				}
			}
		} catch (Exception e) {
			logger.error("出借异常...", e);
			// 更新tendertmp
			boolean updateFlag = appTenderService.updateBorrowTenderTmp(userId, borrowNid, orderId);
			// 更新成功，出借失败
			if (updateFlag) {
				// 出借失败,出借撤销
				try {
					boolean flag = appTenderService.bidCancel(userId, borrowNid, orderId, account);
					if (!flag) {
						message = "投标失败,请联系客服人员!";
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					message = "投标申请撤销失败,请联系客服人员!";
				}
			} else {
				message = "恭喜您出借成功!";
				status = BankCallConstant.STATUS_SUCCESS;
			}
		}
		
		if (status.equals(BankCallConstant.STATUS_FAIL)) {
            // 返回标的暂无可投信息
            String borrowAccountWait = projectService.getBorrowAccountWait(borrowNid);
            if (borrowAccountWait.equals("0.00")) {
                message = "该标的可投金额不足, 请选择其他标的";
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
                baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_APP_FAILED_PATH.replace("{borrowId}", bean.getProductId()));
                modelAndView.addObject("callBackForm", baseMapBean);
                return modelAndView;
            }

			modelAndView.addObject("investDesc", message);
			modelAndView.addObject("projectType", borrow.getProjectType());
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.FAIL);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
            baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_APP_FAILED_PATH.replace("{borrowId}", bean.getProductId()));
            modelAndView.addObject("callBackForm", baseMapBean);
		} else {
            logger.info("出借成功逻辑分支开始执行....");
            DecimalFormat df = CustomConstants.DF_FOR_VIEW;

			// 判断一下优惠券是否已被使用(由于异步调用可能在同步调用之前执行,导致无法获得优惠券的使用情况,所以这里需要重新获取一下)并且同步调用未进行优惠券出借
			if (StringUtils.isNotEmpty(couponGrantId)) {
			    int index=0;
			    do {
	                try {
	                    Thread.sleep(500);
	                    logger.info("==================cwyang 异步调用优先执行,重新获取优惠券信息.============");
	                    couponTender = appTenderService.getCouponIsUsed(orderId, couponGrantId, userId);
	                    if (!CustomConstants.RESULT_SUCCESS.equals(couponTender.getString("isSuccess"))) {
	                        logger.error("====================cwyang 获取优惠券信息失败!==================");
	                        couponTender = null;
	                        index++;
	                    }
	                } catch (Exception e) {
	                    logger.error("获取优惠券信息异常!", e);
	                }  
	            } while (couponTender==null&&index<3);
			}

			
			logger.info("开始获取结果页展示数据....");
			String interest = null;
			String borrowStyle = borrow.getBorrowStyle();// 项目还款方式
			Integer borrowPeriod = borrow.getBorrowPeriod();// 周期
			BigDecimal borrowApr = borrow.getBorrowApr();// 項目预期年化收益率
			BigDecimal earnings = BigDecimal.ZERO;
			// 计算历史回报
			switch (borrowStyle) {
				case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“: 历史回报=出借金额*年化收益÷12*月数；
					earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(account), borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					interest = df.format(earnings);
					break;
				case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“: 历史回报=出借金额*年化收益÷360*天数；
					earnings = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(account), borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					interest = df.format(earnings);
					break;
				case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“: 历史回报=出借金额*年化收益÷12*月数；
					earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(account), borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2,
							BigDecimal.ROUND_DOWN);
					interest = df.format(earnings);
					break;
				case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“: 历史回报=出借金额*年化收益÷12*月数；
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

			//历史回报
			BigDecimal income = BigDecimal.ZERO;
			if (StringUtils.isNotBlank(interest)) {
				// 本金的收益
				income = earnings.add(income);
			}
			// 历史回报
			baseMapBean.set("income", income.toString());
			logger.info("本金收益: {}", income);

			if (Validator.isNotNull(couponTender)) {
				logger.info("优惠券出借结果: {}", couponTender.toJSONString());
				int couponStatus = couponTender.getIntValue("status");
				if (couponStatus == 0) {
					// 优惠券面值
					String couponQuota = couponTender.getString("couponQuota");
					// 优惠券收益率
					// 优惠券类别
					Integer couponType = couponTender.getIntValue("couponTypeInt");
					// 加息券收益
					String couponInterest = couponTender.getString("couponInterest");
					if (StringUtils.isNotEmpty(couponInterest)) {
						// 代金券需要加上券的面值
						if (couponType == 3) {
							logger.info("代金券加上券的面值....");
							income = income.add(new BigDecimal(couponQuota));
						}
						income = income.add(new BigDecimal(couponInterest));
						// 历史回报
						baseMapBean.set("income", CommonUtils.formatAmount(income));
					}
					if (StringUtils.isNotEmpty(couponQuota)) {
						// 加息券增加%
						if (couponType == 2) {
							try {
								couponQuota = URLEncoder.encode(couponQuota.concat("%"), "utf-8");
							} catch (UnsupportedEncodingException e) {
								logger.error("URLEncoder编码错误....", e);
							}
							logger.info("加息券面额....{}", couponQuota);
						}
						// 优惠券面值
						baseMapBean.set("couponValue", couponQuota);
					}
					if (couponType != Integer.MIN_VALUE) {
						// 优惠券类别
						baseMapBean.set("couponType", String.valueOf(couponType));
					}
				}
			}
			
			// 产品加息预期收益
            if (Validator.isIncrease(borrow.getIncreaseInterestFlag(), borrow.getBorrowExtraYield())) {
                BigDecimal incEarnings = tenderService.increaseCalculate(borrowPeriod, borrowStyle, account, borrow.getBorrowExtraYield());
                income = income.add(incEarnings);
                baseMapBean.set("income", CommonUtils.formatAmount(income));
            }
            
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
			baseMapBean.set("amount", CommonUtils.formatAmount(account));
			baseMapBean.setCallBackAction(CustomConstants.HOST + InvestDefine.JUMP_HTML_APP_SUCCESS_PATH.replace("{borrowId}", bean.getProductId()));
			logger.info("出借结果信息展示: {}",JSONObject.toJSONString(baseMapBean));
			modelAndView.addObject("callBackForm", baseMapBean);
		}
		return modelAndView;

	}

	/**
	 * 散标出借异步回调结果
	 * 
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(InvestDefine.RETURL_ASY_ACTION)
	public String tenderRetUrlAsy(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BankCallBean bean) {

		logger.info("开始调用出借异步方法");
		int userId = StringUtils.isBlank(bean.getLogUserId()) ? 0 : Integer.parseInt(bean.getLogUserId());// 用户Userid
		String ip = bean.getUserIP();// 用户操作ip
		String couponGrantId = request.getParameter("couponGrantId");// 优惠券用户id
		String couponOldTime = StringUtils.isNotEmpty(request.getParameter("couponOldTime")) ? request.getParameter("couponOldTime") : "0";
		String respCode = bean.getRetCode();// 出借结果返回码
		String platForm = request.getParameter("platform");
		BankCallResult result = new BankCallResult();
		String message = "出借失败！";// 错误信息
		if (StringUtils.isBlank(respCode)) {
			result.setMessage(message);
			return JSONObject.toJSONString(result, true);
		}
		if (!BankCallConstant.RESPCODE_SUCCESS.equals(respCode)) {
			// 返回码提示余额不足，不结冻
			if (BankCallConstant.RETCODE_BIDAPPLY_YUE_FAIL.equals(respCode)) {
				logger.info("PC用户:" + userId + "**出借接口调用失败，余额不足，错误码: " + respCode);
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
		logger.info("=====cwyang platFrom 为 " + platForm);
		if (StringUtils.isNotEmpty(platForm)) {
			bean.setLogClient(Integer.parseInt(platForm));// add by cwyang
															// 将出借平台放到bean中传输
		}
		// 根据借款Id检索标的信息
		BorrowWithBLOBs borrow = this.appTenderService.getBorrowByNid(borrowId);
		String borrowNid = borrowId == null ? "" : borrow.getBorrowNid();
		logger.info("出借异步回调: " + bean.getAllParams().toString());
		logger.info("标号:" + borrowNid);
		if (StringUtils.isBlank(borrowNid)) {
			message = "回调时,borrowNid为空";
			result.setMessage(message);
			return JSONObject.toJSONString(result, true);
		}
		try {
			// 进行出借, tendertmp锁住
			JSONObject tenderResult = this.appTenderService.userTender(borrow, bean);
			// 出借成功
			if (tenderResult.getString("status").equals("1")) {
				logger.info("PC用户:" + userId + "***出借成功: " + account);
				message = "恭喜您出借成功!";
				logger.info("异步调用优惠劵ID:" + couponGrantId );
				if (StringUtils.isNotEmpty(couponGrantId)) {
					// 优惠券出借校验
					try {
						// 优惠券出借校验
						logger.info("异步调用优惠劵ID:" + couponGrantId );
						JSONObject couponCheckResult = CommonSoaUtils.CheckCoupon(userId + "", borrowNid, account, CustomConstants.CLIENT_PC, couponGrantId);
						int couponStatus = couponCheckResult.getIntValue("status");
						logger.info("优惠券出借校验结果: {}" + couponCheckResult == null ? "" : couponCheckResult.toJSONString());
						if (couponStatus == 0) {
							// 优惠券出借
							//CommonSoaUtils.CouponInvestForPC(userId + "", borrowNid, account, CustomConstants.CLIENT_PC, couponGrantId, orderId, ip, couponOldTime);
							// update by pcc 放入汇直投优惠券使用mq队列 start
                            LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender", "同步方法放入汇直投优惠券使用mq队列: ");

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("mqMsgId", GetCode.getRandomCode(10));
                            // 真实出借金额
                            params.put("money", account);
                            // 借款项目编号
                            params.put("borrowNid", borrowNid);
                            // 平台
                            params.put("platform", platForm);
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
							logger.info("优惠券出借任务push到redis queue, param is :{}", JSONObject.toJSONString(params));
                            rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_HZT_COUPON_TENDER, JSONObject.toJSONString(params));
                            // update by pcc 放入汇直投优惠券使用mq队列 end
						}
					} catch (Exception e) {
						LogUtil.infoLog(InvestController.class.getName(), "tenderRetUrl", "优惠券出借失败");
					}
				}
			}
			// 出借失败 回滚redis
			else {
				
				// 更新tendertmp
				boolean updateFlag = appTenderService.updateBorrowTenderTmp(userId, borrowNid, orderId);
				// 更新失败，出借失败
				if (updateFlag) {
					if(tenderResult.getString("status").equals("-1")){// 同步/异步 优先执行完毕
						message = "恭喜您出借成功!";
						//add by cwyang 增加redis防重校验 2017-08-02
						boolean checkTender = RedisUtils.tranactionSet("tendersuccess_orderid" + orderId, 20);
						if(!checkTender){//同步/异步 优先执行成功!
							boolean updateTenderFlag = this.appTenderService.updateTender(userId, borrowNid, orderId, bean);
							if (!updateTenderFlag) {
								message = "投标出現错误,请联系客服人员!";
							}
						}else{
							message = "投标失败,请联系客服人员!";
						}
						//end
					}else{
						// 出借失败,出借撤销
						try {
							boolean flag = appTenderService.bidCancel(userId, borrowId, orderId, account);
							if (!flag) {
								message = "投标失败,请联系客服人员!";
							}
						} catch (Exception e) {
							e.printStackTrace();
							message = "投标申请撤销失败,请联系客服人员!";
						}
					}
				} else {
					message = "恭喜您出借成功!";
					boolean updateTenderFlag = this.appTenderService.updateTender(userId, borrowNid, orderId, bean);
					if (!updateTenderFlag) {
						message = "投标出現错误,请联系客服人员!";
					}
				}
			}
		} catch (Exception e) {
			// 更新tendertmp
			boolean updateFlag = appTenderService.updateBorrowTenderTmp(userId, borrowNid, orderId);
			// 更新失败，出借失败
			if (updateFlag) {
				// 出借失败,出借撤销
				try {
					boolean flag = appTenderService.bidCancel(userId, borrowId, orderId, account);
					if (!flag) {
						message = "投标失败,请联系客服人员!";
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					message = "投标申请撤销失败,请联系客服人员!";
				}
			} else {
				message = "恭喜您出借成功!";
				boolean updateTenderFlag = this.appTenderService.updateTender(userId, borrowNid, orderId, bean);
				if (!updateTenderFlag) {
					message = "投标出現错误,请联系客服人员!";
				}
			}
		}
		result.setStatus(true);
		return JSONObject.toJSONString(result, true);
	}

	/**
	 *
	 * 债转出借
	 *
	 * @author liuyang
	 * @param modelAndView
	 * @param userId
	 * @param account
	 * @param creditNid
	 */
	private ModelAndView tenderCredit(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView, Integer userId, String account, String creditNid, String platform) {
		String message = null;
		// 用户未登录
		if (userId == null) {
			message = "用户未登录,请登录！";
			// 回调url（h5错误页面）

			modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
			BaseMapBean baseMapBean=new BaseMapBean();
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
			baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_ERROR_PATH.replace("{transferId}", creditNid));
			modelAndView.addObject("callBackForm", baseMapBean);

			return modelAndView;
		} else {
			logger.info("债转出借用户id:" + userId + "债转标号creditNid:" + creditNid + "出借金额:" + account);
			// 检查用户角色是否能出借  合规接口改造之后需要判断
	        UsersInfo userInfo = investService.getUsersInfoByUserId(userId);
	        if (null != userInfo) {
				String roleIsOpen = PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN);
				if(StringUtils.isNotBlank(roleIsOpen) && roleIsOpen.equals("true")){
					if (userInfo.getRoleId() != 1) {// 担保机构用户
						message = "仅限出借人进行出借";
						// 回调url（h5错误页面）
						modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
						BaseMapBean baseMapBean=new BaseMapBean();
						baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
						baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
						baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_ERROR_PATH.replace("{transferId}", creditNid));
						modelAndView.addObject("callBackForm", baseMapBean);
						return modelAndView;
					}
				}

	        } else {
	            message = "账户信息异常";
                // 回调url（h5错误页面）
                modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
                BaseMapBean baseMapBean=new BaseMapBean();
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
                baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_ERROR_PATH.replace("{transferId}", creditNid));
                modelAndView.addObject("callBackForm", baseMapBean);
                return modelAndView;
	        }
			// 出借校验相应的出借参数
			// 债转出借校验
			// 取得承接债转的用户在汇付天下的客户号
			BankOpenAccount accountChinapnrCrediter = appTenderCreditService.getBankOpenAccount(userId);
			if (accountChinapnrCrediter == null || Validator.isNull(accountChinapnrCrediter.getAccount())) {
				message = "您还未开户，请开户后重新操作";
				// 回调url（h5错误页面）

				modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
				BaseMapBean baseMapBean=new BaseMapBean();
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
				baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
				baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_ERROR_PATH.replace("{transferId}", creditNid));
				modelAndView.addObject("callBackForm", baseMapBean);
				return modelAndView;
			}
			if (StringUtils.isEmpty(creditNid) || StringUtils.isEmpty(account)) {
				message = "债转编号和承接本金不能为空";
				// 回调url（h5错误页面）

				modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
				BaseMapBean baseMapBean=new BaseMapBean();
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
				baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
				baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_ERROR_PATH.replace("{transferId}", creditNid));
				modelAndView.addObject("callBackForm", baseMapBean);
				return modelAndView;
			} else {
				if (!account.matches("^[-+]?(([0-9]+)(([0-9]+))?|(([0-9]+))?)$") || !Validator.isNumber(creditNid)) {
					message = "债转编号和承接本金必须是数字格式";
					// 回调url（h5错误页面）

					modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
					BaseMapBean baseMapBean=new BaseMapBean();
					baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
					baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
					baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_ERROR_PATH.replace("{transferId}", creditNid));
					modelAndView.addObject("callBackForm", baseMapBean);
					return modelAndView;
				}
			}
			// 验证用户余额是否可以债转
			Account userAccount = this.appTenderCreditService.getAccount(userId);
			if (userAccount.getBankBalance() != null && userAccount.getBankBalance().compareTo(BigDecimal.ZERO) == 1) {
				TenderToCreditAssignCustomize creditAssign = this.appTenderCreditService.getInterestInfo(creditNid, account, userId);
				String assignPay = creditAssign.getAssignTotal();
				if (userAccount.getBankBalance().compareTo(new BigDecimal(assignPay))<0) {
					message = "余额不足";
					// 回调url（h5错误页面）

					modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
					BaseMapBean baseMapBean=new BaseMapBean();
					baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
					baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
					baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_ERROR_PATH.replace("{transferId}", creditNid));
					modelAndView.addObject("callBackForm", baseMapBean);
					return modelAndView;
				}
			} else {
				message = "余额不足";
				// 回调url（h5错误页面）

				modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
				BaseMapBean baseMapBean=new BaseMapBean();
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
				baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
				baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_ERROR_PATH.replace("{transferId}", creditNid));
				modelAndView.addObject("callBackForm", baseMapBean);
				return modelAndView;
			}
			// 订单号
			String logOrderId = GetOrderIdUtils.getOrderId2(userId);
			String txDate = GetOrderIdUtils.getTxDate();
			String txTime = GetOrderIdUtils.getTxTime();
			String seqNo = GetOrderIdUtils.getSeqNo(6);
			String sign = request.getParameter("sign");// add by cwyang 用于忘记交易密码传参
			String token = request.getParameter("token");// add by cwyang 用于忘记交易密码传参
			// 获取债转的详细参数
			Map<String, Object> creditDetailMap = appTenderCreditService.saveCreditTenderAssign(userId, creditNid, account, request, platform, logOrderId, txDate, txTime, seqNo);
			if (creditDetailMap.get("creditTenderLog") != null) {
				CreditTenderLog creditTenderLog = (CreditTenderLog) creditDetailMap.get("creditTenderLog");
				if (userAccount.getBankBalance().compareTo(creditTenderLog.getAssignPay()) <0) {
					message = "垫付利息可用余额不足，请充值";
					// 回调url（h5错误页面）

					modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
					BaseMapBean baseMapBean=new BaseMapBean();
					baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
					baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
					baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_ERROR_PATH.replace("{transferId}", creditNid));
					modelAndView.addObject("callBackForm", baseMapBean);
					return modelAndView;
				}
				// 取得债权出让人的用户在汇付天下的客户号
				BankOpenAccount accountChinapnrTender = appTenderCreditService.getBankOpenAccount(creditTenderLog.getCreditUserId());
				// 同步回调路径
				String returl = HOST + AppTenderCreditDefine.REQUEST_HOME + AppTenderCreditDefine.REQUEST_MAPPING + "/" + AppTenderCreditDefine.RETURN_MAPPING + ".do?a=1";
				String bgRetUrl = HOST + AppTenderCreditDefine.REQUEST_HOME + AppTenderCreditDefine.REQUEST_MAPPING + "/" + AppTenderCreditDefine.CALLBACK_MAPPING + ".do";// 支付工程路径
				String forgetPwdUrl = CustomConstants.FORGET_PASSWORD_URL+ "?sign=" + sign + "&token=" + token+"&platform="+platform;
				// 调用接口
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
				bean.setChannel(BankCallConstant.CHANNEL_APP);// 交易渠道
				bean.setAccountId(accountChinapnrCrediter.getAccount());// 存管平台分配的账号
				bean.setOrderId(logOrderId);
				bean.setTxAmount(String.valueOf(creditTenderLog.getAssignPay()));
				bean.setTxFee(creditTenderLog.getCreditFee() != null ? DF_COM_VIEW.format(creditTenderLog.getCreditFee()) : "0.01");
				bean.setTsfAmount(String.valueOf(creditTenderLog.getAssignCapital()));
				bean.setForAccountId(String.valueOf(accountChinapnrTender.getAccount()));
				bean.setOrgOrderId(creditTenderLog.getCreditTenderNid());
				bean.setOrgTxAmount(String.valueOf(creditDetailMap.get("tenderMoney")));
				bean.setProductId(creditTenderLog.getBidNid().toString());
				bean.setForgotPwdUrl(forgetPwdUrl);// 忘记密码的跳转URL

				bean.setRetUrl(returl);// 商户前台台应答地址(必须)
				bean.setSuccessfulUrl(returl+"&isSuccess=1");// 商户前台台应答地址(必须)
				bean.setNotifyUrl(bgRetUrl); // 商户后台应答地址(必须)
				logger.info("债转前台回调函数: \n" + bean.getRetUrl());
				logger.info("债转后台回调函数: \n" + bean.getNotifyUrl());
				// 跳转到江西银行画面
				try {
					modelAndView = BankCallUtils.callApi(bean);
					LogUtil.endLog(InvestController.class.toString(), InvestDefine.INVEST_ACTION);
					return modelAndView;
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.errorLog(InvestController.class.getName(), message, null);
					// 此处更改CreditTenderLog数据中的status为1,代表此订单失效
					appTenderCreditService.updateCreditTenderLogToFail(creditTenderLog);
					message = "交易失败请重试";
					modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
					BaseMapBean baseMapBean=new BaseMapBean();
					baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
					baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
					baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_ERROR_PATH.replace("{transferId}", creditNid));
					modelAndView.addObject("callBackForm", baseMapBean);
					return modelAndView;
				}
			} else {
				message = String.valueOf(creditDetailMap.get("msg"));
				modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
				BaseMapBean baseMapBean=new BaseMapBean();
				baseMapBean.set("message",message);
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
				baseMapBean.set(CustomConstants.APP_STATUS_DESC, BaseResultBeanFrontEnd.SUCCESS_MSG);
				baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_ERROR_PATH.replace("{transferId}", creditNid));
				modelAndView.addObject("callBackForm", baseMapBean);
				return modelAndView;
			}

		}
	}


	/**
	 * 优惠券出借
	 *
	 * @param modelAndView
	 * @param request
	 * @param cuc
	 * @param userId
	 * @return
	 */
	private void couponTender(ModelAndView modelAndView, HttpServletRequest request, CouponConfigCustomizeV2 cuc, int userId, int couponOldTime) {
		String platform = request.getParameter("platform");
		// 金额
		String account = request.getParameter("money");
		String ip = GetCilentIP.getIpAddr(request);
		String couponGrantId = request.getParameter("couponGrantId");
		String borrowNid = request.getParameter("borrowNid");
		if (account == null || "".equals(account)) {
			account = "0";
		}
		BaseMapBean baseMapBean=new BaseMapBean();

		// 根据项目编号获取相应的项目
		Borrow borrow = investService.getBorrowByNid(borrowNid);
		// 优惠券出借校验
		Map<String, String> validateMap = this.validateCoupon(account, couponGrantId, borrow, platform, cuc, String.valueOf(userId));
		logger.info("=====================cwyang优惠券出借校验结果:" + validateMap);
		if (validateMap.containsKey(CustomConstants.APP_STATUS_DESC)) {

			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, validateMap.get(CustomConstants.APP_STATUS_DESC));
			baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_APP_FAILED_PATH.replace("{borrowId}", borrowNid));
			modelAndView.addObject("callBackForm", baseMapBean);
		}
		JSONObject jsonObject = CommonSoaUtils.CouponInvestForPC(userId + "", borrowNid, account, CustomConstants.CLIENT_PC, couponGrantId, "", ip, couponOldTime + "");
		logger.info("仅有优惠券出借返回结果: {}", jsonObject);
		if (jsonObject.getIntValue("status") == 0) {
			// 优惠券类别
			String couponType = jsonObject.getString("couponTypeInt") ==null?"":jsonObject.getString("couponTypeInt").toString();
			baseMapBean.set("couponType", couponType);
			// 优惠券额度
			String couponQuota = jsonObject.getString("couponQuota") == null ? ""
					: jsonObject.getString("couponQuota").toString();
			if ("2".equals(couponType)) {
				try {
					couponQuota = URLEncoder.encode(couponQuota.concat("%"), "utf-8");
				} catch (UnsupportedEncodingException e) {
					logger.error("URLEncoder编码错误...." ,e);
				}
			}
			baseMapBean.set("couponValue", couponQuota);

			String income = jsonObject.getString("couponInterest") == null ? ""
					: jsonObject.getString("couponInterest").toString();
			if ("3".equals(couponType)) {
				logger.info("代金券计算历史回报需要加上券的面值...");
				income = String
						.valueOf(new BigDecimal(income).add(new BigDecimal(jsonObject.getString("couponQuota") == null
								? "" : jsonObject.getString("couponQuota").toString())));
			}
			// 优惠券收益
			baseMapBean.set("income", income);

			// 金额
			baseMapBean.set("amount", account);
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, validateMap.get(CustomConstants.APP_STATUS_DESC));
			baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_APP_SUCCESS_PATH.replace("{borrowId}", borrowNid));

			//logger.info("出借成功跳转baseMapBean is :{}", JSONObject.toJSONString(baseMapBean));
			modelAndView.addObject("callBackForm", baseMapBean);
		} else {
			LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender", "优惠券出借结束。。。。。。");

			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, jsonObject.getString("statusDesc"));
			baseMapBean.setCallBackAction(CustomConstants.HOST+InvestDefine.JUMP_HTML_APP_FAILED_PATH.replace("{borrowId}", borrowNid));
			modelAndView.addObject("callBackForm", baseMapBean);
		}

	}

	/**
	 * 优惠券出借校验
	 *
	 * @param account
	 * @param couponGrantId
	 * @param couponGrantId
	 * @return
	 */
	private Map<String, String> validateCoupon(String account, String couponGrantId, Borrow borrow, String platform, CouponConfigCustomizeV2 couponConfig, String userId) {

		JSONObject jsonObject = CommonSoaUtils.CheckCoupon(userId, borrow.getBorrowNid(), account, platform, couponGrantId);
		int status = jsonObject.getIntValue("status");
		String statusDesc = jsonObject.getString("statusDesc");

		Map<String, String> paramMap = new HashMap<String, String>();
		if (status == 1) {
			paramMap.put(CustomConstants.APP_STATUS_DESC, statusDesc);
		}

		return paramMap;

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
		return investService.getBestCoupon(borrowNid, userId, money, platform);
	}

	/**
	 * 获取最优优惠券
	 * 
	 * @param couponId
	 * @return
	 */
	private UserCouponConfigCustomize getCouponById(String couponId) {
		return investService.getCouponById(couponId);
	}

	/**
	 * 判断优惠券是否可用
	 * @param userCouponId
	 * @param result
	 * @return
	 */
	private boolean isCouponAvailable(String userCouponId, JSONObject result) {
		String obj = JSONObject.toJSONString(result.get("availableCouponList"));
		List<CouponBean> coupons = JSONArray.parseArray(obj, CouponBean.class);
		if (!CollectionUtils.isEmpty(coupons)) {
			for (CouponBean couponBean : coupons) {
				if (userCouponId.equals(couponBean.getUserCouponId())) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 优惠券判断优惠券是否可用
	 * @param userCouponId
	 * @param result
	 * @return
	 */
	private boolean isHjhCouponAvailable(String userCouponId, JSONObject result) {
		String obj = JSONObject.toJSONString(result.get("availableCouponList"));
		List<PlanCouponResultBean> coupons = JSONArray.parseArray(obj, PlanCouponResultBean.class);
		if (!CollectionUtils.isEmpty(coupons)) {
			for (PlanCouponResultBean couponBean : coupons) {
				if (userCouponId.equals(couponBean.getUserCouponId())) {
					return true;
				}
			}
		}
		return false;
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
		logger.info("hjh send mq activity userId="+userId+" orderId="+order);
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
		logger.info("hjh send mq activity userId="+userId+" orderId="+order);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("orderId", order);
		params.put("investMoney", investMoney.toString());
		//来源,1=新手标，2=散标，3=汇计划
		params.put("productType", projectType);
		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.RETURN_CASH_ACTIVITY, JSONObject.toJSONString(params));
	}
	/**
	 * 计算优惠券历史回报
	 * @param couponConfig
	 * @param couponConfig
	 * @param couponConfig
	 * @param money  出借本金
	 * @param borrow
	 * @return
	 */
	private BigDecimal calculateCouponTenderInterest(UserCouponConfigCustomize couponConfig, String money, Borrow borrow) {
		//计算优惠券历史回报
		BigDecimal couponInterest = BigDecimal.ZERO;
		BigDecimal borrowApr = borrow.getBorrowApr();
		String borrowStyle = borrow.getBorrowStyle();

		if (couponConfig != null) {
			Integer couponType = couponConfig.getCouponType();
			BigDecimal couponQuota = couponConfig.getCouponQuota();
			Integer couponProfitTime = couponConfig.getCouponProfitTime();

			if (couponType == 1) {
				couponInterest = getInterestDj(couponQuota, couponProfitTime, borrowApr);
			} else {
			    couponInterest = getInterest(borrowStyle, couponType, borrowApr, couponQuota, money, borrow.getBorrowPeriod());
			}
		}
		logger.info("优惠券的收益: {} ", couponInterest);
		return couponInterest;
	}

	/**
	 * 出借协议列表
	 * @param resultVo
	 * @param investType
	 * @param borrowNid
	 */
	private void setProtocolsToResultVO(InvestInfoResultVo resultVo, String investType, String borrowNid){
		/*if ("HJH".equals(investType)) {borrowPublisher
			resultVo.setProtocols(NewAgreementConfig.getAgreementMap().get(NewAgreementConfig.agreementType_HJH));
			resultVo.setProtocolUrlDesc("协议列表");
		}else if ("HZR".equals(investType)) {
			resultVo.setProtocols(NewAgreementConfig.getAgreementMap().get(NewAgreementConfig.agreementType_HZR));
			resultVo.setProtocolUrlDesc("协议列表");
		}else{
			resultVo.setProtocols(NewAgreementConfig.getAgreementMap().get(NewAgreementConfig.agreementType_Common));
			resultVo.setProtocolUrlDesc("协议列表");
		}*/
	    if("RTB".equals(investType)){
	        Borrow borrow = investService.getBorrowByNid(borrowNid);
	        if("嘉诺".equals(borrow.getBorrowPublisher())){
	            investType=investType+"JN";
	        } else if("中商储".equals(borrow.getBorrowPublisher())){
	            investType=investType+"ZSC";
	        }
	    }
	    List<NewAgreementBean> list=new ArrayList<NewAgreementBean>();
	    NewAgreementBean newAgreementBean=new NewAgreementBean("出借协议",  PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+"/agreement/AgreementViewList?borrowType="+investType);
	    list.add(newAgreementBean);
	    resultVo.setProtocols(list);
        resultVo.setProtocolUrlDesc("协议列表");
	}

	private String getMinAmount(Integer tenderAccountMax, BigDecimal tmpmoney, String borrowAccountWait) {
		if (tenderAccountMax == null || tmpmoney == null || StringUtils.isBlank(borrowAccountWait)) {
			return null;
		}else{
			ArrayList<BigDecimal> amountList =  new ArrayList<BigDecimal>();
			amountList.add(new BigDecimal(tenderAccountMax));
			amountList.add(tmpmoney);
			amountList.add(new BigDecimal(borrowAccountWait));
			for (int i = 0; i < amountList.size(); i++) {
				for (int j = i+1; j < amountList.size(); j++) {
					if (amountList.get(i).compareTo(amountList.get(j)) > 0) {
						BigDecimal temp = null;
						temp = amountList.get(i);
						amountList.set(i, amountList.get(j));
						amountList.set(j, temp);
					}
				}
			}
			return amountList.get(0).toString();
		}
	}


	/**
	 * 体验金收益计算
	 * @param couponQuota
	 * @param couponProfitTime
	 * @param borrowApr
	 * @return
	 */
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
			case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“: 历史回报=出借金额*年化收益÷12*月数；
				// 计算历史回报
				earnings = DuePrincipalAndInterestUtils.getMonthInterest(accountDecimal, borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“: 历史回报=出借金额*年化收益÷360*天数；
				// 计算历史回报
				earnings = DuePrincipalAndInterestUtils.getDayInterest(accountDecimal, borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“: 历史回报=出借金额*年化收益÷12*月数；
				// 计算历史回报
				earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(accountDecimal, borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“: 历史回报=出借金额*年化收益÷12*月数；
				// 计算历史回报
				earnings = AverageCapitalPlusInterestUtils.getInterestCount(accountDecimal,borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2,BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_PRINCIPAL: //还款方式为“等额本金” 历史回报=出借金额*年化收益÷12*月数；
				// 计算历史回报
				earnings = AverageCapitalUtils.getInterestCount(accountDecimal,borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
				break;
			default:
				break;
		}
		if (couponType == 3) {
			earnings = earnings.add(couponQuota);
		}
		return earnings;
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


}
