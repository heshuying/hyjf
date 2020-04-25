package com.hyjf.wechat.controller.user.financialadvisor;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.hyjf.mybatis.model.customize.EvalationCustomize;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.model.auto.Evalation;
import com.hyjf.mybatis.model.auto.UserEvalationResult;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.QuestionCustomize;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.base.SimpleResultBean;
import com.hyjf.wechat.service.financialadvisor.WxFinancialAdvisorService;
import com.hyjf.wechat.service.login.LoginService;
import com.hyjf.wechat.util.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.List;

/**
 * Created by cuigq on 2018/2/9.
 */
@Controller
@RequestMapping(WxFinancialAdvisorDefine.REQUEST_MAPPING)
public class WxFinancialAdvisorController extends BaseController {

    @Autowired
    private WxFinancialAdvisorService wxFinancialAdvisorService;
    @Autowired
    private LoginService loginService;

    @SignValidate
    @RequestMapping(value = WxFinancialAdvisorDefine.QUERY_QUESTION_ACTION, method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean queryQuestions(HttpServletRequest request) {
        SimpleResultBean<List<QuestionCustomize>> resultBean = new SimpleResultBean<>();
        String userId = String.valueOf(requestUtil.getRequestUserId(request));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(userId));
        Users users = loginService.getUsers(requestUtil.getRequestUserId(request));
        UsersInfo usersInfo = loginService.getUsersInfoByUserId(requestUtil.getRequestUserId(request));
        UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
        userOperationLogEntity.setOperationType(12);
        userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
        userOperationLogEntity.setPlatform(1);
        userOperationLogEntity.setRemark("");
        userOperationLogEntity.setOperationTime(new Date());
        userOperationLogEntity.setUserName(users.getUsername());
        userOperationLogEntity.setUserRole(String.valueOf(usersInfo.getRoleId()));
        loginService.sendUserLogMQ(userOperationLogEntity);
        UserEvalationResultCustomize userEvalationResultCustomize = wxFinancialAdvisorService.selectUserEvalationResultByUserId(userId);
        if (userEvalationResultCustomize != null) {
            // 获取用户信息
            Users user = this.wxFinancialAdvisorService.getUsersByUserId(Integer.valueOf(userId));
            // 获取评测时间加一年的毫秒数18.2.2评测 19.2.2
            Long lCreate = user.getEvaluationExpiredTime().getTime();
            // 获取当前时间加一天的毫秒数 19.2.1以后需要再评测19.2.2
            Long lNow = System.currentTimeMillis();
			if (lCreate <= lNow) {
				// 已过期需要重新评测
                resultBean.setResultStatus("2");
	            // List<QuestionCustomize> vo = Lists.newArrayList();
	            // List<QuestionCustomize> lstQuestion = wxFinancialAdvisorService.getQuestionList();
	            // vo.addAll(lstQuestion);
	            // resultBean.setObject(vo);
	            // return resultBean;
			} else {
	            // 已测评
	            // 已测评
                resultBean.setResultStatus("1");
	            // resultBean.setEnum(ResultEnum.USER_ERROR_1001);
	            // return resultBean;
			}
        } else {
            // List<QuestionCustomize> vo = Lists.newArrayList();
            // List<QuestionCustomize> lstQuestion = wxFinancialAdvisorService.getQuestionList();
            // vo.addAll(lstQuestion);
            resultBean.setResultStatus("0");
            // resultBean.setObject(vo);
            // return resultBean;
        }
        List<QuestionCustomize> vo = Lists.newArrayList();
        List<QuestionCustomize> lstQuestion = wxFinancialAdvisorService.getQuestionList();
        vo.addAll(lstQuestion);
        resultBean.setObject(vo);
        return resultBean;
    }

    @SignValidate
    @RequestMapping(value = WxFinancialAdvisorDefine.QUERY_EVALUATION_RESULT, method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean queryEvaluationResult(HttpServletRequest request) {
        SimpleResultBean<UserEvalationResultCustomize> resultBean = new SimpleResultBean<>();
        String userId = String.valueOf(requestUtil.getRequestUserId(request));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(userId));

        UserEvalationResultCustomize userEvalationResultCustomize = wxFinancialAdvisorService.selectUserEvalationResultByUserId(userId);
        if (userEvalationResultCustomize == null) {
            //已测评
            resultBean.setEnum(ResultEnum.USER_ERROR_1002);
            return resultBean;
        } else {
            // 获取用户信息
            Users user = wxFinancialAdvisorService.getUsersByUserId(Integer.valueOf(userId));
            //测评到期日
            Long lCreate = user.getEvaluationExpiredTime().getTime();
            //当前日期
            Long lNow = System.currentTimeMillis();
			if (lCreate <= lNow) {
				//已过期需要重新评测
	            resultBean.setEnum(ResultEnum.USER_ERROR_1012);
	            return resultBean;
			} else {
				resultBean.setObject(userEvalationResultCustomize);
            	return resultBean;
			}
        }
    }

    @SignValidate
    @ResponseBody
    @RequestMapping(value = WxFinancialAdvisorDefine.RESULT_SUMIT, method = RequestMethod.POST)
    public BaseResultBean sumitResult(HttpServletRequest request, @RequestBody FinancialadvisorSumitQO qo) {
        SimpleResultBean<UserEvalationResultCustomize> resultBean = new SimpleResultBean();
        String userId = String.valueOf(requestUtil.getRequestUserId(request));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(userId));

        //答案 "13_48,14_52"
        String userAnswer = qo.getUserAnswer();
        UserEvalationResultCustomize userEvalationResultCustomize = wxFinancialAdvisorService.insertResult(userId, userAnswer);

        resultBean.setObject(userEvalationResultCustomize);

        return resultBean;
    }

    /**
     * 评分标准接口
     *
     * */

    @SignValidate
    @ResponseBody
    @RequestMapping(value = WxFinancialAdvisorDefine.RESULT_STANDARD, method = RequestMethod.POST)
    public BaseResultBean gradingStandardResult(HttpServletRequest request) {
        SimpleResultBean< List<EvalationCustomize>> resultBean = new SimpleResultBean();
        String userId = String.valueOf(requestUtil.getRequestUserId(request));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(userId));
        //评分标准
        List<EvalationCustomize> evalationCustomizeList = wxFinancialAdvisorService.getEvalationRecord();
        resultBean.setObject(evalationCustomizeList);
        return resultBean;
    }

}
