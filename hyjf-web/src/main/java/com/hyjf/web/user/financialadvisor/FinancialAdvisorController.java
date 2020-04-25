package com.hyjf.web.user.financialadvisor;
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

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.common.util.GetCilentIP;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.StringUtil;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.login.LoginController;
import com.hyjf.web.user.login.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.customize.EvalationCustomize;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.customize.QuestionCustomize;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.coupon.util.CouponCheckUtil;
import com.hyjf.web.util.WebUtils;

/**
 *
 * 风险测评
 * @author pcc
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年3月24日
 * @see 10:23:33
 */
@Controller
@RequestMapping(value = FinancialAdvisorDefine.REQUEST_MAPPING)
public class FinancialAdvisorController extends BaseController {
    Logger _log = LoggerFactory.getLogger(FinancialAdvisorController.class);
    @Autowired
    private FinancialAdvisorService financialAdvisorService;
    @Autowired
    private CouponCheckUtil couponCheckUtil;
    @Autowired
    private LoginService loginService;


    /**
     *
     * 网站改版风险测评页面初始化
     * @author pcc
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value=FinancialAdvisorDefine.FINANCIAL_ADVISOR_INIT_ACTION , produces = "text/html;charset=UTF-8")
    public ModelAndView financialAdvisorInit(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(FinancialAdvisorController.class.toString(), FinancialAdvisorDefine.FINANCIAL_ADVISOR_INIT_ACTION);
        ModelAndView modelAndView = new ModelAndView(FinancialAdvisorDefine.FINANCIAL_ADVISOR_LIST_PATH);
        // 用户ID

        Integer userId = WebUtils.getUserId(request);
        if(userId == null || userId == 0){
            //userError 用户未登录
            modelAndView.addObject(FinancialAdvisorDefine.JSON_USER_LOGIN_ERROR_KEY,FinancialAdvisorDefine.JSON_USER_LOGIN_ERROR_VLUES);
            return modelAndView;
        }
        _log.info("开始测评====");
        WebViewUser user = WebUtils.getUser(request);
        UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
        userOperationLogEntity.setOperationType(12);
        userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
        userOperationLogEntity.setPlatform(0);
        userOperationLogEntity.setRemark("");
        userOperationLogEntity.setOperationTime(new Date());
        userOperationLogEntity.setUserName(user.getUsername());
        userOperationLogEntity.setUserRole(user.getRoleId());
        loginService.sendUserLogMQ(userOperationLogEntity);
        _log.info("结束测评====");
        //测评结果
        UserEvalationResultCustomize userEvalationResultCustomize =financialAdvisorService.selectUserEvalationResultByUserId(userId);
        //已测评
        if(userEvalationResultCustomize != null && userEvalationResultCustomize.getId() != 0){
            // 获取用户信息
            //获取评测时间加一年的毫秒数18.2.2评测 19.2.2
            Long lCreate = user.getEvaluationExpiredTime().getTime();
            //获取当前时间加一天的毫秒数 19.2.1以后需要再评测19.2.2
            Long lNow = System.currentTimeMillis();
			if (lCreate <= lNow) {
				//已过期需要重新评测
	            //测评问题
	            List<QuestionCustomize> list=financialAdvisorService.getNewQuestionList();
	            modelAndView.addObject("questionList",list);
	            //评分标准
	            List<EvalationCustomize> evalationCustomizeList = financialAdvisorService.getEvalationRecord();
	            modelAndView.addObject("evalationList", evalationCustomizeList);
			} else {
	            modelAndView = new ModelAndView(FinancialAdvisorDefine.FINANCIAL_ADVISOR_RESULT_PATH); //已测评到结果页
	            //测评结果
	            modelAndView.addObject("userEvalationResult", userEvalationResultCustomize);
			}
        }else{
            //测评问题
            List<QuestionCustomize> list=financialAdvisorService.getNewQuestionList();
            modelAndView.addObject("questionList",list);
            //评分标准
            List<EvalationCustomize> evalationCustomizeList = financialAdvisorService.getEvalationRecord();
            modelAndView.addObject("evalationList", evalationCustomizeList);
        }
        LogUtil.endLog(FinancialAdvisorController.class.toString(), FinancialAdvisorDefine.FINANCIAL_ADVISOR_INIT_ACTION);
        return modelAndView;
    }
    
    
    
    /**
     * 用户测评结果
     * @author pcc
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = FinancialAdvisorDefine.EVALUATION_RESULT_ACTION,method = RequestMethod.POST)
    public ModelAndView evaluationResult(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(FinancialAdvisorController.class.toString(), FinancialAdvisorDefine.REQUEST_MAPPING);
        ModelAndView modelAndView = new ModelAndView(FinancialAdvisorDefine.FINANCIAL_ADVISOR_RESULT_PATH);
        // 用户ID
        Integer userId = WebUtils.getUserId(request);
        //用户答案
        String userAnswer = request.getParameter("userAnswer");
        Map<String,Object> returnMap = this.answerAnalysisAndCoupon(userAnswer, userId);
        //优惠券发放
        if(returnMap.get("sendCount") != null){
            int sendCount = (int)returnMap.get("sendCount");
            modelAndView.addObject("sendCount", sendCount);
        }

        //返回测评结果增加上限金额
        //if(returnMap.get("revaluation_money") != null){
        //    int money = (int)returnMap.get("revaluation_money");
        //    String revalMonry = StringUtil.getTenThousandOfANumber(money);
        //    modelAndView.addObject("revaluation_money", revalMonry);
        //}
        UserEvalationResultCustomize userEvalationResultCustomize = (UserEvalationResultCustomize)returnMap.get("userEvalationResult");
        // userEvalationResult 测评结果
        modelAndView.addObject("userEvalationResult", userEvalationResultCustomize);
        WebViewUser webUser = loginService.getWebViewUserByUserId(userId);
        WebUtils.sessionLogin(request, response, webUser);
        LogUtil.endLog(FinancialAdvisorController.class.toString(), FinancialAdvisorDefine.EVALUATION_RESULT_ACTION);
        return modelAndView;

    }
    
    
    /**
     * 
     * 再测一次
     * @author pcc
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(FinancialAdvisorDefine.QUESTIONNAIRE_INIT_ACTION)
    public ModelAndView questionnaireInit(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(FinancialAdvisorController.class.toString(), FinancialAdvisorDefine.REQUEST_MAPPING);
        ModelAndView modelAndView = new ModelAndView(FinancialAdvisorDefine.FINANCIAL_ADVISOR_LIST_PATH);
        WebViewUser user = WebUtils.getUser(request);
        UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
        userOperationLogEntity.setOperationType(12);
        userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
        userOperationLogEntity.setPlatform(0);
        userOperationLogEntity.setRemark("");
        userOperationLogEntity.setOperationTime(new Date());
        userOperationLogEntity.setUserName(user.getUsername());
        userOperationLogEntity.setUserRole(user.getRoleId());
        loginService.sendUserLogMQ(userOperationLogEntity);
        //测评问题
        List<QuestionCustomize> list=financialAdvisorService.getNewQuestionList();
        modelAndView.addObject("questionList",list);
        modelAndView.addObject("listSize",list.size());
        //评分标准
        List<EvalationCustomize> evalationCustomizeList = financialAdvisorService.getEvalationRecord();
        modelAndView.addObject("evalationList", evalationCustomizeList);
		LogUtil.endLog(FinancialAdvisorController.class.toString(), FinancialAdvisorDefine.QUESTIONNAIRE_INIT_ACTION);
		return modelAndView;
	}
  
    
	/**
	 * 插入评测数据并发券
	 * @param userAnswer
	 * @param userId
	 * @return
	 */
    public synchronized Map<String,Object> answerAnalysisAndCoupon(String userAnswer,Integer userId){
    	Map<String,Object> returnMap  = new HashMap<String,Object>();
    	// add by zhangjp 发放优惠券 start
  		UserEvalationResultCustomize ueResult = financialAdvisorService.selectUserEvalationResultByUserId(userId);
  		// 是否已经参加过测评（true：已测评过，false：测评）
  		boolean isAdvisor = ueResult != null ? true : false;
      	// add by zhangjp 发放优惠券 end
        // 1_1,2_8
        UserEvalationResultCustomize userEvalationResultCustomize = answerAnalysis(userAnswer, new Integer(userId));
        //查询redis中的类型和返回增加金额上限
        if(userEvalationResultCustomize != null){
            //从redis中获取测评类型和上限金额
            String revaluation_money = null;
            String eval_type = userEvalationResultCustomize.getType();
            switch (eval_type){
                case "保守型":
                    revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE);
                    break;
                case "稳健型":
                    revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS);
                    break;
                case "成长型":
                    revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_GROWTH) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_GROWTH);
                    break;
                case "进取型":
                    revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE);
                    break;
                default:
                    revaluation_money = "0";
            }
            returnMap.put("revaluationMoney", StringUtil.getTenThousandOfANumber(Double.valueOf(revaluation_money).intValue()));
            userEvalationResultCustomize.setRevaluationMoney(StringUtil.getTenThousandOfANumber(Double.valueOf(revaluation_money).intValue()));
        }
        // add by zhangjp 发放优惠券 start
  		if(!isAdvisor){
  			String platform = CustomConstants.CLIENT_PC;
  			// 发放优惠券
  			String result = this.sendCoupon(userId, platform);
  			if(StringUtils.isNotEmpty(result)){
  				JSONObject resultObj = JSONObject.parseObject(result);
  	  			if(resultObj.getIntValue("status") == 0 && resultObj.getIntValue("couponCount") > 0){
  					int sendCount = resultObj.getIntValue("couponCount");
  					returnMap.put("sendCount", sendCount);
  				}
  			} 			
  		}
  		returnMap.put("userEvalationResult", userEvalationResultCustomize);
  		return returnMap;
      	// add by zhangjp 发放优惠券 end
    }
    
    /**
     * 发放优惠券
     * @param userId
     * @param platform
     * @return
     */
    private String sendCoupon(int userId,String platform){
        String activityId = CustomConstants.ACTIVITY_ID;
        // 活动有效期校验
        String resultActivity = couponCheckUtil.checkActivityIfAvailable(activityId);
        // 终端平台校验
        String resultPlatform = couponCheckUtil.checkActivityPlatform(activityId, platform);
        // String
        String result = StringUtils.EMPTY;
        if(StringUtils.isEmpty(resultActivity)&&StringUtils.isEmpty(resultPlatform)){
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

	/**
	 * 计算用户测评分数
	 * @param userAnswer
	 * @param userId
	 * @return
	 */
    public UserEvalationResultCustomize answerAnalysis(String userAnswer, Integer userId) {
        UserEvalationResultCustomize oldUserEvalationResultCustomize =financialAdvisorService.selectUserEvalationResultByUserId(userId);
        financialAdvisorService.deleteUserEvalationResultByUserId(userId);

        String[] answer = userAnswer.split(",");
        List<String> answerList = new ArrayList<String>();
        List<String> questionList = new ArrayList<String>();
        for (String string : answer) {
            if(string.split("_").length==2){
                questionList.add(string.split("_")[0]);
                answerList.add(string.split("_")[1]);
            }
        }
        int countScore = financialAdvisorService.countScore(answerList);
        EvalationCustomize evalationCustomize = financialAdvisorService.getEvalationByCountScore(countScore);
        UserEvalationResultCustomize userEvalationResultCustomize = financialAdvisorService.insertUserEvalationResult(answerList,
                questionList, evalationCustomize, countScore, userId, oldUserEvalationResultCustomize);
        return userEvalationResultCustomize;
    }
    
}
