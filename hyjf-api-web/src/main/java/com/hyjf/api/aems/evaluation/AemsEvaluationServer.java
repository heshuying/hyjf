    package com.hyjf.api.aems.evaluation;

    import com.alibaba.fastjson.JSONObject;
    import com.hyjf.api.web.BaseController;
    import com.hyjf.bank.service.evalation.EvaluationService;
    import com.hyjf.base.bean.BaseDefine;
    import com.hyjf.common.enums.utils.MsgEnum;
    import com.hyjf.common.result.ResultApiBean;
    import com.hyjf.common.validator.CheckUtil;
    import com.hyjf.common.validator.Validator;
    import com.hyjf.financialadvisor.FinancialAdvisorService;
    import com.hyjf.mybatis.model.auto.Evalation;
    import com.hyjf.mybatis.model.auto.Users;
    import com.hyjf.mybatis.model.customize.EvalationCustomize;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.ResponseBody;

    import java.util.Date;

    @Controller
@RequestMapping(AemsEvaluationDefine.REQUEST_MAPPING)
public class AemsEvaluationServer extends BaseController{

	Logger _log = LoggerFactory.getLogger("Evaluation");
	@Autowired
    private FinancialAdvisorService financialAdvisorService;

	@Autowired
    EvaluationService evaluationService;
	/**
     * 用户风险测评
     * @param thirdPartyFinancialadvisorRequestBean
     * @return
     */
    @ResponseBody
    @RequestMapping(AemsEvaluationDefine.SAVE_USER_EVALUATION_RESULTS)
    public ResultApiBean<AemsEvaluationResultBean> saveUserEvaluationResults(@RequestBody AemsEvaluationRequestBean thirdPartyFinancialadvisorRequestBean) {
        AemsEvaluationResultBean resultBean=new AemsEvaluationResultBean();
        _log.info(thirdPartyFinancialadvisorRequestBean.getAccountId()+"用户风险测评开始-----------------------------");
        _log.info("第三方请求参数："+JSONObject.toJSONString(thirdPartyFinancialadvisorRequestBean));
        //验证请求参数
        // 验证
        CheckUtil.check(Validator.isNotNull(thirdPartyFinancialadvisorRequestBean.getTimestamp()), MsgEnum.STATUS_CE000001);
        CheckUtil.check(Validator.isNotNull(thirdPartyFinancialadvisorRequestBean.getInstCode()), MsgEnum.STATUS_CE000001);
        CheckUtil.check(Validator.isNotNull(thirdPartyFinancialadvisorRequestBean.getChkValue()), MsgEnum.STATUS_CE000001);
        CheckUtil.check(Validator.isNotNull(thirdPartyFinancialadvisorRequestBean.getMobile()), MsgEnum.STATUS_CE000001);
        CheckUtil.check(Validator.isNotNull(thirdPartyFinancialadvisorRequestBean.getPlatform()), MsgEnum.STATUS_CE000001);
        CheckUtil.check(Validator.isNotNull(thirdPartyFinancialadvisorRequestBean.getEvalationType()), MsgEnum.STATUS_CE000001);
        // 验签
        CheckUtil.check(this.AEMSVerifyRequestSign(thirdPartyFinancialadvisorRequestBean, AemsEvaluationDefine.REQUEST_MAPPING+AemsEvaluationDefine.SAVE_USER_EVALUATION_RESULTS),
              MsgEnum.ERR_SIGN);
        //根据userId查询用户信息
        Users user = financialAdvisorService.getUsersByMobile(thirdPartyFinancialadvisorRequestBean.getMobile());//用户ID
        if (user == null) {
            CheckUtil.check(false, MsgEnum.STATUS_CE000006);
        }
        //根据平常类型查询平常信息
        EvalationCustomize evalation = financialAdvisorService.getEvalationByEvalationType(thirdPartyFinancialadvisorRequestBean.getEvalationType());
        if(evalation==null){
            CheckUtil.check(false, MsgEnum.STATUS_EV000001);
        }
        // hyjf-api中调用不到bankService的方法、只能从api-web调用处理后传入
        Date evaluationExpiredTime = evaluationService.selectEvaluationExpiredTime(new Date());
        int flag = financialAdvisorService.ThirdPartySaveUserEvaluationResults(user,evalation.getScoreDown()-2,evalation,
                thirdPartyFinancialadvisorRequestBean.getInstCode(),thirdPartyFinancialadvisorRequestBean.getChannel(),evaluationExpiredTime);
        resultBean.setAccountId(thirdPartyFinancialadvisorRequestBean.getAccountId());
        if(flag==0){
            CheckUtil.check(false, MsgEnum.STATUS_CE999999);
        }
        
        return new ResultApiBean<AemsEvaluationResultBean>(resultBean);
    }
	
}
