package com.hyjf.app.user.financialadvisor;
/**
 * Description:出借控制器
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 * Created at: 2015年12月4日 下午2:32:36
 * Modification History:
 * Modified by :
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.mybatis.model.auto.Users;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.user.coupon.util.CouponCheckUtil;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.customize.EvalationCustomize;
import com.hyjf.mybatis.model.auto.UserEvalationBehavior;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.customize.QuestionCustomize;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;

/**
 * 
 * 风险测评
 * @author pcc
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年3月24日
 * @see 上午10:23:33
 */
@Controller
@RequestMapping(value = FinancialAdvisorDefine.REQUEST_MAPPING)
public class FinancialAdvisorController extends BaseController {

	@Autowired
	private FinancialAdvisorService financialAdvisorService;
	@Autowired
    private CouponCheckUtil couponCheckUtil;
    
	@RequestMapping(FinancialAdvisorDefine.INIT_ACTION)
	// @RequiresRoles(ShiroConstants.ROLE_NORMAL_USER)
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(FinancialAdvisorDefine.class.toString(), FinancialAdvisorDefine.INIT_ACTION);
		ModelAndView modelAndView = new ModelAndView(FinancialAdvisorDefine.INIT_PATH);
		String sign = request.getParameter("sign");
		modelAndView.addObject("sign", sign);
		String platform = request.getParameter("platform");
		modelAndView.addObject("platform", platform);
		
		// add by zhangjp 发放优惠券 start
		Integer userId = SecretUtil.getUserId(sign);
		UserEvalationResultCustomize ueResult = financialAdvisorService.selectUserEvalationResultByUserId(userId);
		if(ueResult==null){
			String activityId = CustomConstants.ACTIVITY_ID;
			// 活动有效期校验
			String resultActivity = couponCheckUtil.checkActivityIfAvailable(activityId);
			// 终端平台校验
			String resultPlatform = couponCheckUtil.checkActivityPlatform(activityId, platform);
			// String 
			String couponResult = StringUtils.EMPTY;
			if(StringUtils.isEmpty(resultActivity)&&StringUtils.isEmpty(resultPlatform)){
				couponResult = "首次完成评测，可以获得1张加息券";
			}
			modelAndView.addObject("couponResult", couponResult);
		} else {
			// 获取用户信息
			Users user = this.financialAdvisorService.getUsersByUserId(Integer.valueOf(userId));
			//获取评测时间加一年的毫秒数18.2.2评测 19.2.2
			Long lCreate = user.getEvaluationExpiredTime().getTime();
			//获取当前时间加一天的毫秒数 19.2.1以后需要再评测19.2.2
			Long lNow = System.currentTimeMillis();
			if (lCreate <= lNow) {
				//已过期需要重新评测
				modelAndView.addObject("couponResult", "已过期需要重新评测");
			}
		}
		// add by zhangjp 发放优惠券 end
		
		LogUtil.endLog(FinancialAdvisorDefine.class.toString(), FinancialAdvisorDefine.INIT_ACTION);
	/*	// 用户ID
        //Integer userId = SecretUtil.getUserId(sign);
        Integer userId =2;
        if(platform!=null&&platform.length()!=0){
            String platformString="";
            if(CustomConstants.CLIENT_ANDROID.equals(platform)){
                platformString="安卓";
            }
            if(CustomConstants.CLIENT_IOS.equals(platform)){
                platformString="IOS";
            }
            UserEvalationBehavior userEvalationBehavior=financialAdvisorService.insertUserEvalationBehavior(userId,platformString); 
            modelAndView.addObject("behaviorId",userEvalationBehavior.getId());
        }*/
		return modelAndView;
	}
	/**
	 * 
	 * 返回测评问卷
	 * @author pcc
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value=FinancialAdvisorDefine.QUESTIONNAIRE_INIT_ACTION, produces = "text/html;charset=UTF-8",method = RequestMethod.POST)
	// @RequiresRoles(ShiroConstants.ROLE_NORMAL_USER)
	public String questionnaireInit(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(FinancialAdvisorController.class.toString(), FinancialAdvisorDefine.REQUEST_MAPPING);
//		ModelAndView modelAndView = new ModelAndView(FinancialAdvisorDefine.QUESTIONNAIRE_PATH);
		JSONObject ret=new JSONObject();
		// 检查参数

		// 唯一标识
	    String sign = request.getParameter("sign");
	    
	    ///String platform = request.getParameter("platform");
        // 用户ID
		Integer userId = SecretUtil.getUserId(sign);
//		Integer userId =2;
        if(userId==null||userId==0){
            //ifEvaluation是否已经调查表示  1是已调查，0是未调查
            ret.put(FinancialAdvisorDefine.JSON_IF_EVALUATION_KEY,0);
            //userError 用户未登录
            ret.put(FinancialAdvisorDefine.JSON_USER_LOGIN_ERROR_KEY,FinancialAdvisorDefine.JSON_USER_LOGIN_ERROR_VLUES);
            return JSONObject.toJSONString(ret);
        }
		
		UserEvalationResultCustomize userEvalationResultCustomize =financialAdvisorService.selectUserEvalationResultByUserId(new Integer(userId));
		if(userEvalationResultCustomize !=null&& userEvalationResultCustomize.getId()!=0){
			// 获取用户信息
			Users user = this.financialAdvisorService.getUsersByUserId(Integer.valueOf(userId));
			//获取评测时间加一年的毫秒数18.2.2评测 19.2.2
			Long lCreate = user.getEvaluationExpiredTime().getTime();
			//获取当前时间加一天的毫秒数 19.2.1以后需要再评测19.2.2
			Long lNow = System.currentTimeMillis();
			if (lCreate <= lNow) {
				//已过期需要重新评测
			    ret.put(FinancialAdvisorDefine.JSON_IF_EVALUATION_KEY,2);
			} else {
			    //ifEvaluation是否已经调查表示  1是已调查，0是未调查
			    ret.put(FinancialAdvisorDefine.JSON_IF_EVALUATION_KEY,1);
			    //userEvalationResult 测评结果
			    ret.put(FinancialAdvisorDefine.JSON_USER_EVALATION_RESULT_KEY, userEvalationResultCustomize);
			}
		}else{
		    ret.put(FinancialAdvisorDefine.JSON_IF_EVALUATION_KEY,0);
		}
		
		
		
		List<QuestionCustomize> list=financialAdvisorService.getQuestionList();
		//List<String> typeList=financialAdvisorService.getTypeList();
		ret.put(FinancialAdvisorDefine.JSON_QUESRION_LIST_KEY,list);
		
		LogUtil.endLog(FinancialAdvisorController.class.toString(), FinancialAdvisorDefine.QUESTIONNAIRE_INIT_ACTION);
		return JSONObject.toJSONString(ret);
	}
	
	
	
	/**
	 * 
	 * 统计用户测评结果
	 * @author pcc
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value=FinancialAdvisorDefine.EVALUATION_RESULT_ACTION, produces = "text/html;charset=UTF-8",method = RequestMethod.POST)
    // @RequiresRoles(ShiroConstants.ROLE_NORMAL_USER)
    public String evaluationResult(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(FinancialAdvisorController.class.toString(), FinancialAdvisorDefine.REQUEST_MAPPING);
       /* ModelAndView modelAndView = new ModelAndView(FinancialAdvisorDefine.QUESTIONNAIRE_PATH);*/
        JSONObject ret=new JSONObject();
        // 检查参数
        String sign = request.getParameter("sign");
        // 用户ID
        Integer userId = SecretUtil.getUserId(sign);
        // 统计ID
        String behaviorId = request.getParameter("behaviorId");
//        Integer userId =2;
        if(userId==null||userId==0){
            //ifEvaluation是否已经调查表示  1是已调查，0是未调查
            ret.put(FinancialAdvisorDefine.JSON_IF_EVALUATION_KEY,0);
            //userError 用户未登录
            ret.put(FinancialAdvisorDefine.JSON_USER_LOGIN_ERROR_KEY,FinancialAdvisorDefine.JSON_USER_LOGIN_ERROR_VLUES);
            return JSONObject.toJSONString(ret);
        }
        // add by zhangjp 发放优惠券 start
 		UserEvalationResultCustomize ueResult = financialAdvisorService.selectUserEvalationResultByUserId(userId);
 		// 是否已经参加过测评（true: 已测评过，false: 测评）
 		boolean isAdvisor = ueResult != null ? true : false;
     	// add by zhangjp 发放优惠券 end
        financialAdvisorService.deleteUserEvalationResultByUserId(userId);
        //1_1,2_8
        String userAnswer = request.getParameter("userAnswer");
        UserEvalationResultCustomize userEvalationResultCustomize = answerAnalysis(userAnswer,new Integer(userId));
        if(behaviorId!=null&&behaviorId.length()!=0){
            UserEvalationBehavior userEvalationBehavior=new UserEvalationBehavior();
            userEvalationBehavior.setId(Integer.parseInt(behaviorId));
            userEvalationBehavior.setEndTime(new Date());
            userEvalationBehavior.setBehavior(createNewDate()+"用户完成答题");
            financialAdvisorService.updateUserEvalationBehavior(userEvalationBehavior);
        }
        // add by zhangjp 发放优惠券 start
  		if(!isAdvisor){
  			String platform = request.getParameter("platform");
  			// 发放优惠券
  			String result = this.sendCoupon(userId, platform);
  			if(StringUtils.isNotEmpty(result)){
  				JSONObject resultObj = JSONObject.parseObject(result);
  	  			if(resultObj.getIntValue("status") == 0 && resultObj.getIntValue("couponCount") > 0){
  					String sendResult = "恭喜您获得"+resultObj.getIntValue("couponCount")+"张加息券，体验出借流程，获取高额收益，可在我的账户-优惠券中查看";
  					ret.put("sendResult", sendResult);
  				}
  			}
  			
  		}
      	// add by zhangjp 发放优惠券 end
      		
        //ifEvaluation是否已经调查表示  1是已调查，0是未调查
        ret.put(FinancialAdvisorDefine.JSON_IF_EVALUATION_KEY,1);
        //userEvalationResult 测评结果
        ret.put(FinancialAdvisorDefine.JSON_USER_EVALATION_RESULT_KEY,createUserEvalationResult(userEvalationResultCustomize));
        
        System.out.println(JSONObject.toJSONString(ret));
        LogUtil.endLog(FinancialAdvisorController.class.toString(), FinancialAdvisorDefine.EVALUATION_RESULT_ACTION);
        return JSONObject.toJSONString(ret);
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
    
    public UserEvalationResultCustomize answerAnalysis(String userAnswer, Integer userId){
        UserEvalationResultCustomize oldUserEvalationResultCustomize =financialAdvisorService.selectUserEvalationResultByUserId(userId);
        financialAdvisorService.deleteUserEvalationResultByUserId(userId);
        String[] answer=userAnswer.split(",");
        List<String> answerList=new ArrayList<String>();
        List<String> questionList=new ArrayList<String>();
        for (String string : answer) {
            questionList.add(string.split("_")[0]);
            answerList.add(string.split("_")[1]);
        }
        int countScore=financialAdvisorService.countScore(answerList);
        EvalationCustomize evalationCustomize = financialAdvisorService.getEvalationByCountScore(countScore);
        UserEvalationResultCustomize userEvalationResultCustomize =financialAdvisorService.insertUserEvalationResult(answerList,questionList, evalationCustomize,countScore,userId, oldUserEvalationResultCustomize);
        return userEvalationResultCustomize;
    }
    
    private UserEvalationResultCustomize createUserEvalationResult(UserEvalationResultCustomize userEvalationResult) {
        UserEvalationResultCustomize userEvalationResultCustomize=new UserEvalationResultCustomize();
        userEvalationResultCustomize.setType(userEvalationResult.getType());
        userEvalationResultCustomize.setSummary(userEvalationResult.getSummary());
        userEvalationResultCustomize.setScoreCount(userEvalationResult.getScoreCount());
        return userEvalationResultCustomize;
    }
    
    /**
     * 
     * 开始记录用户行为
     * @author pcc
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value=FinancialAdvisorDefine.START_USER_EVALUATION_BEHAVIOR, produces = "text/html;charset=UTF-8",method = RequestMethod.POST)
    // @RequiresRoles(ShiroConstants.ROLE_NORMAL_USER)
    public String startUserEvaluationBehavior(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(FinancialAdvisorController.class.toString(), FinancialAdvisorDefine.REQUEST_MAPPING);
       /* ModelAndView modelAndView = new ModelAndView(FinancialAdvisorDefine.QUESTIONNAIRE_PATH);*/
        JSONObject ret=new JSONObject();
        String sign = request.getParameter("sign");
        String platform = request.getParameter("platform");
        
        
        LogUtil.endLog(FinancialAdvisorDefine.class.toString(), FinancialAdvisorDefine.INIT_ACTION);
        // 用户ID
        Integer userId = SecretUtil.getUserId(sign);
//        Integer userId =2;
        if(platform!=null&&platform.length()!=0){
            String platformString="";
            if(CustomConstants.CLIENT_ANDROID.equals(platform)){
                platformString="安卓";
            }
            if(CustomConstants.CLIENT_IOS.equals(platform)){
                platformString="IOS";
            }
            UserEvalationBehavior userEvalationBehavior=
                    financialAdvisorService.insertUserEvalationBehavior(userId,createNewDate()+"移动端"+platformString+"开始答题"); 
            ret.put("behaviorId",userEvalationBehavior.getId());
        }
        //ifEvaluation是否已经调查表示  1是已调查，0是未调查
        ret.put("success",1);
        LogUtil.endLog(FinancialAdvisorController.class.toString(), FinancialAdvisorDefine.USER_EVALUATION_BEHAVIOR);
        return JSONObject.toJSONString(ret);
    }
    
    
    
    
    /**
     * 
     * 记录用户行为
     * @author pcc
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value=FinancialAdvisorDefine.USER_EVALUATION_BEHAVIOR, produces = "text/html;charset=UTF-8",method = RequestMethod.POST)
    // @RequiresRoles(ShiroConstants.ROLE_NORMAL_USER)
    public String userEvaluationBehavior(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(FinancialAdvisorController.class.toString(), FinancialAdvisorDefine.REQUEST_MAPPING);
       /* ModelAndView modelAndView = new ModelAndView(FinancialAdvisorDefine.QUESTIONNAIRE_PATH);*/
        JSONObject ret=new JSONObject();
        // 统计ID
        String behaviorId = request.getParameter("behaviorId");
        // 统计内容
        String behavior = request.getParameter("behavior");
        if(behaviorId!=null&&behaviorId.length()!=0){
            behavior=createNewDate()+"用户选择问题答案为: "+behavior;
            UserEvalationBehavior userEvalationBehavior=new UserEvalationBehavior();
            userEvalationBehavior.setId(Integer.parseInt(behaviorId));
            userEvalationBehavior.setEndTime(new Date());
            userEvalationBehavior.setBehavior(behavior);
            financialAdvisorService.updateUserEvalationBehavior(userEvalationBehavior);
        }
        //ifEvaluation是否已经调查表示  1是已调查，0是未调查
        ret.put("success",1);
        LogUtil.endLog(FinancialAdvisorController.class.toString(), FinancialAdvisorDefine.USER_EVALUATION_BEHAVIOR);
        return JSONObject.toJSONString(ret);
    }
    private String createNewDate() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd HH:mm:ss");
        return sdf.format(new Date());
    }
    
	
}
