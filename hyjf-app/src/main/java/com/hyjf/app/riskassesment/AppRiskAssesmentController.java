package com.hyjf.app.riskassesment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hyjf.app.user.manage.AppUserService;
import com.hyjf.bank.service.mq.MqService;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.bank.service.evalation.EvaluationService;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.user.coupon.util.CouponCheckUtil;
import com.hyjf.app.user.financialadvisor.FinancialAdvisorService;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.customize.EvalationCustomize;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.app.NewAppQuestionCustomize;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;

/**
 * @author xiasq
 * @version RiskAssesmentController, v0.1 2017/12/3 10:38 app用户风险测评信息
 */

@RestController
@RequestMapping(RiskAssesmentDefine.REQUEST_MAPPING)
public class AppRiskAssesmentController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(AppRiskAssesmentController.class);
	private final String TOKEN_ISINVALID_STATUS = "Token失效，请重新登录";
	@Autowired
	private FinancialAdvisorService financialAdvisorService;
	@Autowired
	private CouponCheckUtil couponCheckUtil;
	@Autowired
	private AppUserService appUserService;
	@Autowired
	EvaluationService evaluationService;
	@Autowired
	private MqService mqService;
	/**
	 * 风险测评
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = RiskAssesmentDefine.USER_RISKTEST, method = RequestMethod.GET)
	public RiskAssesmentResponse getQuestionList(HttpServletRequest request) {

		RiskAssesmentResponse response = new RiskAssesmentResponse();

		String sign = request.getParameter("sign");
		if (sign == null) {
			response.setStatus(RiskAssesmentResponse.FAIL);
			response.setStatusDesc("参数非法");
			return response;
		}
		String platform = request.getParameter("platform");
		Integer userId = null;
		try {
			userId = SecretUtil.getUserId(sign);
		} catch (Exception e) { // token失效
			response.setStatus(CustomConstants.APP_STATUS_FAIL);
			response.setStatusDesc(TOKEN_ISINVALID_STATUS);
			return response;
		}
		if (userId == null || userId == 0) {
			response.setStatus(RiskAssesmentResponse.FAIL);
			response.setStatusDesc("用户未登录");
			return response;
		}
		Users users = financialAdvisorService.getUsers(userId);
		UsersInfo usersInfo = financialAdvisorService.getUsersInfoByUserId(userId);
		UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
		userOperationLogEntity.setOperationType(12);
		userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
		userOperationLogEntity.setPlatform(request.getParameter("realPlatform")==null?Integer.valueOf(platform):Integer.valueOf(request.getParameter("realPlatform")));
		userOperationLogEntity.setRemark("");
		userOperationLogEntity.setOperationTime(new Date());
		userOperationLogEntity.setUserName(users.getUsername());
		userOperationLogEntity.setUserRole(String.valueOf(usersInfo.getRoleId()));
		appUserService.sendUserLogMQ(userOperationLogEntity);
		UserEvalationResultCustomize ueResult = financialAdvisorService.selectUserEvalationResultByUserId(userId);
		if (ueResult == null) { // 未测评
			String activityId = CustomConstants.ACTIVITY_ID;
			// 活动有效期校验
			String resultActivity = couponCheckUtil.checkActivityIfAvailable(activityId);
			// 终端平台校验
			String resultPlatform = couponCheckUtil.checkActivityPlatform(activityId, platform);
			String couponResult = StringUtils.EMPTY;
			if (StringUtils.isEmpty(resultActivity) && StringUtils.isEmpty(resultPlatform)) {
				couponResult = "首次完成评测，可以获得1张加息券";
			}
			response.setCouponResult(couponResult);
			response.setResultStatus("0");
		} else {
			Users user = financialAdvisorService.getUsersByUserId(userId);
			//获取评测时间加一年的毫秒数18.2.2评测 19.2.2
			Long lCreate = user.getEvaluationExpiredTime().getTime();
			//获取当前时间加一天的毫秒数 19.2.1以后需要再评测19.2.2
			Long lNow = System.currentTimeMillis();
			if (lCreate <= lNow) {
				//已过期需要重新评测
				response.setResultStatus("2");
			}
			// 已测评
			response.setResultStatus("1");
			response.setResultType(ueResult.getType());
			//response.setResultText(ueResult.getSummary());
			// 测评金额上限增加（获取评分标准对应的上限金额并拼接）
			switch (ueResult.getType()){
				case "保守型":
					response.setRevaluationMoney(StringUtil.getTenThousandOfANumber(
							Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE)).intValue() == 0.0D ? 0 :
									Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE)).intValue()));
					break;
				case "稳健型":
					response.setRevaluationMoney(StringUtil.getTenThousandOfANumber(
							Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS)).intValue() == 0.0D ? 0:
									Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS)).intValue()));
					break;
				case "成长型":
					response.setRevaluationMoney(StringUtil.getTenThousandOfANumber(
							Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_GROWTH)).intValue() == 0.0D ? 0:
									Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_GROWTH)).intValue()));
					break;
				case "进取型":
					response.setRevaluationMoney(StringUtil.getTenThousandOfANumber(
							Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE)).intValue() == 0.0D ? 0:
									Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE)).intValue()));
					break;
				default:
					response.setRevaluationMoney("0");
			}
			response.setResultText(ueResult.getSummary());
		}
		// 返回题目
		List<NewAppQuestionCustomize> list = financialAdvisorService.getNewAppQuestionList();
		for (NewAppQuestionCustomize newAppQuestionCustomize : list) {
			String title = newAppQuestionCustomize.getTitle().substring(newAppQuestionCustomize.getTitle().indexOf(".")+1);
			newAppQuestionCustomize.setTitle(title);
		}
		response.setQuestionList(list);
		response.setStatus(RiskAssesmentResponse.SUCCESS);
		response.setStatusDesc(RiskAssesmentResponse.SUCCESS_MSG);

		return response;
	}

	/**
	 * 风险测评提交
	 */
	@RequestMapping(value = RiskAssesmentDefine.USER_RISKTEST, method = RequestMethod.POST)
	public RiskAssesmentResponse evaluationResult(HttpServletRequest request,
			@RequestBody UserAnswerRequestBean userAnswerRequestBean) {
		RiskAssesmentResponse response = new RiskAssesmentResponse();
		// 检查参数
		String sign = request.getParameter("sign");
		if (sign == null) {
			response.setStatus(RiskAssesmentResponse.FAIL);
			response.setStatusDesc("参数非法");
			return response;
		}
		// 1_1,2_8
		logger.info("userAnswer:{}", JSONObject.toJSONString(userAnswerRequestBean));
		// 用户ID
		Integer userId = SecretUtil.getUserId(sign);
		if (userId == null || userId == 0) {
			response.setStatus(RiskAssesmentResponse.FAIL);
			response.setStatusDesc(RiskAssesmentResponse.FAIL_MSG);
			return response;
		}
		//UserEvalationResult ueResult = financialAdvisorService.selectUserEvalationResultByUserId(userId);
		// 是否已经参加过测评（true：已测评过，false：测评）
		//boolean isAdvisor = ueResult != null ? true : false;
		financialAdvisorService.deleteUserEvalationResultByUserId(userId);
		UserEvalationResultCustomize userEvalationResultCustomize = answerAnalysis(userAnswerRequestBean.getUserAnswer(), userId);
		if(userEvalationResultCustomize == null){
		    response.setStatus(RiskAssesmentResponse.FAIL);
	        response.setStatusDesc("操作失败，请重试！");
	        return response;
		}
		// add by zhangjp 发放优惠券 start
//		if (!isAdvisor) {
//			String platform = request.getParameter("platform");
//			// 发放优惠券
//			String result = this.sendCoupon(userId, platform);
//			if (StringUtils.isNotEmpty(result)) {
//				JSONObject resultObj = JSONObject.parseObject(result);
//				if (resultObj.getIntValue("status") == 0 && resultObj.getIntValue("couponCount") > 0) {
//					String sendResult = "恭喜您获得" + resultObj.getIntValue("couponCount")
//							+ "张加息券，体验出借流程，获取高额收益，可在我的账户-优惠券中查看";
//					response.setSendResult(sendResult);
//				}
//			}
//
//		}
		// add by zhangjp 发放优惠券 end

		// ifEvaluation是否已经调查表示 1是已调查，0是未调查
		response.setResultStatus("1");
		// userEvalationResult 测评结果
		response.setResultType(userEvalationResultCustomize.getType());
		// response.setResultText(userEvalationResult.getSummary());
		// 测评金额上限增加（获取评分标准对应的上限金额并拼接）
		switch (userEvalationResultCustomize.getType()){
			case "保守型":
				response.setRevaluationMoney(StringUtil.getTenThousandOfANumber(
						Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE)).intValue() == 0.0D ? 0 :
								Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE)).intValue()));
				break;
			case "稳健型":
				response.setRevaluationMoney(StringUtil.getTenThousandOfANumber(
						Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS)).intValue() == 0.0D ? 0:
								Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS)).intValue()));
				break;
			case "成长型":
				response.setRevaluationMoney(StringUtil.getTenThousandOfANumber(
						Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_GROWTH)).intValue() == 0.0D ? 0:
								Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_GROWTH)).intValue()));
				break;
			case "进取型":
				response.setRevaluationMoney(StringUtil.getTenThousandOfANumber(
						Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE)).intValue() == 0.0D ? 0:
								Double.valueOf(RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE)).intValue()));
				break;
			default:
				response.setRevaluationMoney("0");
		}
		response.setResultText(userEvalationResultCustomize.getSummary());
		this.updateUsers(userId);

		response.setStatus(RiskAssesmentResponse.SUCCESS);
		response.setStatusDesc(RiskAssesmentResponse.SUCCESS_MSG);
		return response;
	}

	/**
	 * 风险空测评提交  -- 跳过测评
	 */
	@RequestMapping(value = RiskAssesmentDefine.USER_RISKTESTNONE, method = RequestMethod.POST)
	public RiskAssesmentResponse skipEvaluate(HttpServletRequest request) {

		RiskAssesmentResponse response = new RiskAssesmentResponse();
		// 检查参数
		String sign = request.getParameter("sign");
		if (sign == null) {
			response.setStatus(RiskAssesmentResponse.FAIL);
			response.setStatusDesc("参数非法");
			return response;
		}
		// 用户ID
		Integer userId = SecretUtil.getUserId(sign);
		if (userId == null || userId == 0) {
			response.setStatus(RiskAssesmentResponse.FAIL);
			response.setStatusDesc(RiskAssesmentResponse.FAIL_MSG);
			return response;
		}
		//UserEvalationResult ueResult = financialAdvisorService.selectUserEvalationResultByUserId(userId);
		// 是否已经参加过测评（true：已测评过，false：测评）
		//boolean isAdvisor = ueResult != null ? true : false;

		// add by zhangjp 发放优惠券 start  bu
//		if (!isAdvisor) {
//			String platform = request.getParameter("platform");
//			// 发放优惠券
//			String result = this.sendCoupon(userId, platform);
//			if (StringUtils.isNotEmpty(result)) {
//				JSONObject resultObj = JSONObject.parseObject(result);
//				if (resultObj.getIntValue("status") == 0 && resultObj.getIntValue("couponCount") > 0) {
//					String sendResult = "恭喜您获得" + resultObj.getIntValue("couponCount")
//							+ "张加息券，体验出借流程，获取高额收益，可在我的账户-优惠券中查看";
//					response.setSendResult(sendResult);
//				}
//			}
//
//		}
		// add by zhangjp 发放优惠券 end
		UserEvalationResultCustomize oldUserEvalationResultCustomize = financialAdvisorService.selectUserEvalationResultByUserId(userId);
		financialAdvisorService.deleteUserEvalationResultByUserId(userId);
		int countScore = 0;
		EvalationCustomize evalationCustomize = financialAdvisorService.getEvalationByCountScore(countScore);
		UserEvalationResultCustomize userEvalationResultCustomize = financialAdvisorService.insertEvalationResult(evalationCustomize, countScore,
				userId, oldUserEvalationResultCustomize);

		if (userEvalationResultCustomize == null) {
			response.setStatus(RiskAssesmentResponse.FAIL);
			response.setStatusDesc(RiskAssesmentResponse.FAIL_MSG);
			return response;
		}
		// userEvalationResult 测评结果
		response.setResultType(userEvalationResultCustomize.getType());
		response.setResultText(userEvalationResultCustomize.getSummary());


		this.updateUsers(userId);

		// add 合规数据上报 埋点 liubin 20181122 start
		// 推送数据到MQ 用户信息修改（风险测评）
		JSONObject params = new JSONObject();
		params.put("userId", userId);
		this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.USERINFO_CHANGE_DELAY_KEY, JSONObject.toJSONString(params));
		// add 合规数据上报 埋点 liubin 20181122 end

		response.setStatus(RiskAssesmentResponse.SUCCESS);
		response.setStatusDesc(RiskAssesmentResponse.SUCCESS_MSG);
		return response;
	}


	/**
	 * 修改user表的风险测评标志
	 * @param userId
	 */
	private void updateUsers(int userId){
		
		//修改users表标志位
		Users users = financialAdvisorService.getUsers(userId);
		if (users != null) {
			users.setIsEvaluationFlag(1);
			// 获取测评到期日期
			Date evaluationExpiredTime = evaluationService.selectEvaluationExpiredTime(new Date());
			users.setEvaluationExpiredTime(evaluationExpiredTime);
			financialAdvisorService.updateUsers(users);
		}
	}

	/**
	 * 发放优惠券
	 *
	 * @param userId
	 * @param platform
	 * @return
	 */
	private String sendCoupon(int userId, String platform) {
		String activityId = CustomConstants.ACTIVITY_ID;
		// 活动有效期校验
		String resultActivity = couponCheckUtil.checkActivityIfAvailable(activityId);
		// 终端平台校验
		String resultPlatform = couponCheckUtil.checkActivityPlatform(activityId, platform);
		// String
		String result = StringUtils.EMPTY;
		if (StringUtils.isEmpty(resultActivity) && StringUtils.isEmpty(resultPlatform)) {
			CommonParamBean couponParamBean = new CommonParamBean();
			// 用户编号
			couponParamBean.setUserId(String.valueOf(userId));
			// 评测送加息券
			couponParamBean.setSendFlg(1);
			// 发放优惠券（1张加息券）
			result = CommonSoaUtils.sendUserCoupon(couponParamBean);
		}
		return result;
	}

	public UserEvalationResultCustomize answerAnalysis(String userAnswer, Integer userId) {
		UserEvalationResultCustomize oldUserEvalationResultCustomize = financialAdvisorService.selectUserEvalationResultByUserId(userId);
		financialAdvisorService.deleteUserEvalationResultByUserId(userId);
		String[] answer = userAnswer.split(",");
		List<String> answerList = new ArrayList<String>();
		List<String> questionList = new ArrayList<String>();
		for (String string : answer) {
		    try {
		        questionList.add(string.split("_")[0]);
	            answerList.add(string.split("_")[1]);
            } catch (Exception e) {
               return null;
            }
		}
		int countScore = financialAdvisorService.countScore(answerList);
		EvalationCustomize evalationCustomize = financialAdvisorService.getEvalationByCountScore(countScore);
		UserEvalationResultCustomize userEvalationResultCustomize = financialAdvisorService.insertUserEvalationResult(answerList,
				questionList, evalationCustomize, countScore, userId, oldUserEvalationResultCustomize);
		return userEvalationResultCustomize;
	}
}
