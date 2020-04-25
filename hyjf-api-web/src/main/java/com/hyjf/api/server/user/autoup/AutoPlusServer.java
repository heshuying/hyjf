package com.hyjf.api.server.user.autoup;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.server.util.ErrorCodeConstant;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.user.autoup.AutoPlusService;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.HjhUserAuthConfig;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.vip.apply.ApplyDefine;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Controller(AutoPlusDefine.CONTROLLER_NAME)
@RequestMapping(value = AutoPlusDefine.REQUEST_MAPPING)
public class AutoPlusServer extends BaseController {
    
    Logger _log = LoggerFactory.getLogger("AutoPlusServer ");

    @Autowired
    private AutoPlusService autoPlusService;
    
    /**
     * 
     * 前导发送短信验证码
     * @author sss
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = AutoPlusDefine.SENDCODE_ACTION, produces = "application/json; charset=utf-8")
    public AutoPlusResultBean sendCode(@RequestBody AutoPlusRequestBean autoPlusRequestBean, HttpServletRequest request, HttpServletResponse response) {

        _log.info("授权申请发送短信验证码第三方请求参数：" + JSONObject.toJSONString(autoPlusRequestBean));
        
        AutoPlusResultBean resultBean = new AutoPlusResultBean();
        // 手机号
        String mobile = autoPlusRequestBean.getMobile();
        // 渠道
        String channel = autoPlusRequestBean.getChannel();
        // 机构编号
        String instCode = autoPlusRequestBean.getInstCode();
        // 短信类型  1为自动投标授权  2为自动债转授权
        String sendType = autoPlusRequestBean.getSendType();
        // 电子账户号
        String accountId = autoPlusRequestBean.getAccountId();
        
        String txcode = "";
        
        // 验证请求参数
        // 机构编号
        if (Validator.isNull(instCode) || Validator.isNull(sendType)) {
            _log.info("请求参数非法");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        // 手机号
        if (Validator.isNull(mobile)) {
            _log.info("手机号不能为空");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000001);
            resultBean.setStatusDesc("手机号不能为空");
            return resultBean;
        }
        // 渠道
        if (Validator.isNull(channel)) {
            _log.info("渠道不能为空");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000006);
            resultBean.setStatusDesc("渠道不能为空");
            return resultBean;
        }
        // 验签
       if (!this.verifyRequestSign(autoPlusRequestBean, AutoPlusDefine.REQUEST_MAPPING+ AutoPlusDefine.SENDCODE_ACTION)) {
            _log.info("----验签失败----");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000002);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        // 手机号合法性校验
        if (!Validator.isMobile(mobile)) {
            _log.info("请输入您的真实手机号码,手机号:[" + mobile + "].");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000003);
            resultBean.setStatusDesc("请输入您的真实手机号码");
            return resultBean;
        }
        // 根据手机号查询用户信息
        Users user = this.autoPlusService.selectUserByMobile(mobile);
        if (user == null) {
            _log.info("根据手机号查询用户信息失败,手机号:[" + mobile + "].");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000003);
            resultBean.setStatusDesc("根据手机号查询用户信息失败");
            return resultBean;
        }
       
        // 用户ID
        Integer userId = user.getUserId();
        // 根据电子账户号查询用户ID
        BankOpenAccount bankOpenAccount = this.autoPlusService.selectBankOpenAccountByAccountId(accountId);
        if (bankOpenAccount == null) {
            _log.info("查询用户开户信息失败,用户电子账户号:[" + accountId + "]");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000004);
            resultBean.setStatusDesc("根据电子账户号查询用户信息失败");
            return resultBean;
        }
        
        if (user.getBankOpenAccount().intValue() != 1) {// 未开户
            _log.info("-------------------用户未开户！"+autoPlusRequestBean.getAccountId()+"！--------------------");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000006);
            resultBean.setStatusDesc("用户未开户");
            return resultBean;
        }
        
        Integer passwordFlag = user.getIsSetPassword();
        if (passwordFlag != 1) {// 未设置交易密码
            _log.info("-------------------未设置交易密码！"+autoPlusRequestBean.getAccountId()+"！--------------------status"+user.getIsSetPassword());
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_TP000002);
            resultBean.setStatusDesc("未设置交易密码");
            return resultBean;
        }
        
        Users loginUser = autoPlusService.getUsers(bankOpenAccount.getUserId());//用户ID
        //手机号是否正确
        if(!mobile.equals(loginUser.getMobile())){
            _log.info("手机号不正确");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000003);
            resultBean.setStatusDesc("手机号非注册手机号");
            return resultBean;
        }
        
        // 查询是否已经授权过
        HjhUserAuth hjhUserAuth=autoPlusService.getHjhUserAuthByUserId(user.getUserId());
        
        if("1".equals(sendType)){
            txcode = BankCallConstant.TXCODE_AUTO_BID_AUTH_PLUS;
            if(hjhUserAuth!=null&&hjhUserAuth.getAutoInvesStatus()==1){
                _log.info("已经授权过");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000009);
                resultBean.setStatusDesc("已授权,请勿重复授权");
                return resultBean;
            }
        }else if("2".equals(sendType)){
            txcode = BankCallConstant.TXCODE_AUTO_CREDIT_INVEST_AUTH_PLUSS;
            if(hjhUserAuth!=null&&hjhUserAuth.getAutoCreditStatus()==1){
                _log.info("已经授权过");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000009);
                resultBean.setStatusDesc("已授权,请勿重复授权");
                return resultBean;
            }
        }else {
            _log.info("请求参数非法");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        
        _log.info("===================== "+txcode+"开始获取手机验证码,userID = " + userId);
        // 调用短信发送接口
        BankCallBean mobileBean = this.autoPlusService.sendSms(userId, txcode,
                mobile, channel);
        if (Validator.isNull(mobileBean)) {
            _log.info("短信验证码发送失败，请稍后再试！");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            resultBean.setStatusDesc("短信验证码发送失败，请稍后再试！");
            return resultBean;
        }
        _log.info("====================="+txcode+"获取手机验证码完毕,userID = " + userId);
        // 短信发送返回结果码
        String retCode = mobileBean.getRetCode();
        if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)
                && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
            _log.info("短信验证码发送失败，请稍后再试！");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            resultBean.setStatusDesc("短信验证码发送失败，请稍后再试！");
            return resultBean;
        }
        if (Validator.isNull(mobileBean.getSrvAuthCode()) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
            _log.info("短信验证码发送失败，请稍后再试！");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            resultBean.setStatusDesc("短信验证码发送失败，请稍后再试！");
            return resultBean;
        }
        _log.info("发送短信验证码成功,手机号:[" + mobile + "],前导业务授权码:[" + mobileBean.getSrvAuthCode() + "]");
        resultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
        resultBean.setStatusDesc("发送短信验证码成功");
        return resultBean;
    }
    
    /**
     * 
     * 自动出借授权
     * @author sss
     * @param request
     * @param response
     * @param payRequestBean
     * @return
     */
    @RequestMapping(value = AutoPlusDefine.USER_AUTH_INVES_ACTION)
    @ResponseBody
    public ModelAndView userAuthInves(HttpServletRequest request, HttpServletResponse response, @RequestBody AutoPlusRequestBean payRequestBean){
        ModelAndView modelAndView = new ModelAndView(AutoPlusDefine.PATH_TRUSTEE_PAY_ERROR);
        _log.info("自动出借授权请求参数" + JSONObject.toJSONString(payRequestBean, true) + "]");
        // 检查参数是否为空
        if(payRequestBean.checkParmIsNull(modelAndView)){
            getErrorMV(payRequestBean, modelAndView, ErrorCodeConstant.STATUS_CE000001);
            payRequestBean.doNotify(payRequestBean.getErrorMap(ErrorCodeConstant.STATUS_CE000001, "请求参数异常"));
            _log.info("请求参数异常" + JSONObject.toJSONString(payRequestBean, true) + "]");
            return modelAndView;
        }
        
        // 验签  暂时去掉验签
       if (!this.verifyRequestSign(payRequestBean, AutoPlusDefine.REQUEST_MAPPING+ AutoPlusDefine.USER_AUTH_INVES_ACTION)) {
            getErrorMV(payRequestBean, modelAndView, ErrorCodeConstant.STATUS_CE000002);
            payRequestBean.doNotify(payRequestBean.getErrorMap(ErrorCodeConstant.STATUS_CE000002, "验签失败"));
            _log.info("请求参数异常" + JSONObject.toJSONString(payRequestBean, true) + "]");
            return modelAndView;
        }
        
        // 根据电子账户号查询用户ID
        BankOpenAccount bankOpenAccount = this.autoPlusService.selectBankOpenAccountByAccountId(payRequestBean.getAccountId());
        if(bankOpenAccount == null){
            _log.info("-------------------没有根据电子银行卡找到用户"+payRequestBean.getAccountId()+"！--------------------");
            Map<String, String> params = payRequestBean.getErrorMap(ErrorCodeConstant.STATUS_CE000004,"没有根据电子银行卡找到用户");
            payRequestBean.doNotify(params);
            getErrorMV(payRequestBean, modelAndView, ErrorCodeConstant.STATUS_CE000004);
            return modelAndView;
        }
        
        // 检查用户是否存在
        Users user = autoPlusService.getUsers(bankOpenAccount.getUserId());//用户ID
        if (user == null) {
            _log.info("-------------------用户不存在汇盈金服账户！"+payRequestBean.getAccountId()+"！--------------------");
            Map<String, String> params = payRequestBean.getErrorMap(ErrorCodeConstant.STATUS_CE000007,"用户不存在汇盈金服账户！");
            payRequestBean.doNotify(params);
            getErrorMV(payRequestBean, modelAndView, ErrorCodeConstant.STATUS_CE000007);
            return modelAndView;
        }
        
        Integer userId = user.getUserId();
        if (user.getBankOpenAccount().intValue() != 1) {// 未开户
            _log.info("-------------------用户未开户！"+payRequestBean.getAccountId()+"！--------------------");
            Map<String, String> params = payRequestBean.getErrorMap(ErrorCodeConstant.STATUS_CE000006,"用户未开户！");
            payRequestBean.doNotify(params);
            getErrorMV(payRequestBean, modelAndView, ErrorCodeConstant.STATUS_CE000006);
            return modelAndView;
        }
        
        // 检查是否设置交易密码
        Integer passwordFlag = user.getIsSetPassword();
        if (passwordFlag != 1) {// 未设置交易密码
            _log.info("-------------------未设置交易密码！"+payRequestBean.getAccountId()+"！--------------------status"+user.getIsSetPassword());
            Map<String, String> params = payRequestBean.getErrorMap(ErrorCodeConstant.STATUS_TP000002,"未设置交易密码！");
            payRequestBean.doNotify(params);
            getErrorMV(payRequestBean, modelAndView, ErrorCodeConstant.STATUS_TP000002);
            return modelAndView;
        }
        
        String smsSeq = this.autoPlusService.selectBankSmsSeq(userId, BankCallConstant.TXCODE_AUTO_BID_AUTH_PLUS);
        if (StringUtils.isBlank(smsSeq)) {
            _log.info("-------------------授权码为空！"+payRequestBean.getAccountId()+"！--------------------status"+user.getIsSetPassword());
            Map<String, String> params = payRequestBean.getErrorMap(ErrorCodeConstant.STATUS_CE000008,"未查询到短信授权码！");
            payRequestBean.doNotify(params);
            getErrorMV(payRequestBean, modelAndView, ErrorCodeConstant.STATUS_CE000008);
            return modelAndView;
        }
        _log.info("-------------------授权码为！"+smsSeq+"电子账户号"+payRequestBean.getAccountId()+"！--------------------status"+user.getIsSetPassword());
        
        // 查询是否已经授权过
        HjhUserAuth hjhUserAuth=autoPlusService.getHjhUserAuthByUserId(user.getUserId());
        if(hjhUserAuth!=null&&hjhUserAuth.getAutoInvesStatus()==1){
            _log.info("-------------------已经授权过！"+payRequestBean.getAccountId());
            Map<String, String> params = payRequestBean.getErrorMap(ErrorCodeConstant.STATUS_CE000009,"已授权,请勿重复授权！");
            payRequestBean.doNotify(params);
            getErrorMV(payRequestBean, modelAndView, ErrorCodeConstant.STATUS_CE000009);
            return modelAndView;
        }
        
        // 同步调用路径
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
                + AutoPlusDefine.REQUEST_MAPPING + AutoPlusDefine.USER_AUTH_INVES_RETURN_ACTION + ".do?acqRes="
                + payRequestBean.getAcqRes() + "&callback=" + payRequestBean.getRetUrl().replace("#", "*-*-*");
        // 异步调用路
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
                + AutoPlusDefine.REQUEST_MAPPING + AutoPlusDefine.USER_AUTH_INVES_BGRETURN_ACTION + ".do?acqRes="
                + payRequestBean.getAcqRes() + "&callback=" + payRequestBean.getNotifyUrl().replace("#", "*-*-*");
        
        // 组装发往江西银行参数
        BankCallBean bean = getCommonBankCallBean(payRequestBean.getAccountId(),userId,1,payRequestBean.getChannel(),smsSeq,payRequestBean.getSmsCode());
        bean.setRetUrl(retUrl);//同步通知地址
        bean.setNotifyUrl(bgRetUrl);//异步通知地址
        // 插入日志
        this.autoPlusService.insertUserAuthLog(user.getUserId(), bean,1, BankCallConstant.QUERY_TYPE_1);
        // 跳转到汇付天下画面
        try {
            modelAndView = BankCallUtils.callApi(bean);
        } catch (Exception e) {
            e.printStackTrace();
            _log.info("调用银行接口失败！"+e.getMessage());
            Map<String, String> params = payRequestBean.getErrorMap(ErrorCodeConstant.STATUS_CE999999,"系统异常！");
            payRequestBean.doNotify(params);
            getErrorMV(payRequestBean, modelAndView, ErrorCodeConstant.STATUS_CE999999);
            return modelAndView;
            
        }
        _log.info("自动出借授权申请end");
        return modelAndView;
    }
    
    // 组装发往江西银行参数
    private BankCallBean getCommonBankCallBean(String accountId, Integer userid, int type, String channel, String lastSrvAuthCode, String smsCode) {
        
        String remark = "";
        String txcode = "";
        // 构造函数已经设置
        // 版本号  交易代码  机构代码  银行代码  交易日期  交易时间  交易流水号   交易渠道
        BankCallBean bean = new BankCallBean(BankCallConstant.VERSION_10,txcode,userid,channel);
        if(type==1){// 2.4.4.出借人自动投标签约增强
            remark = "出借人自动投标签约增强";
            bean.setTxCode(BankCallConstant.TXCODE_AUTO_BID_AUTH_PLUS);
            HjhUserAuthConfig config=CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_AUTO_TENDER_AUTH);
            bean.setDeadline(GetDate.date2Str(GetDate.countDate(1,config.getAuthPeriod()),new SimpleDateFormat("yyyyMMdd")));// 签约到期时间
            bean.setTxAmount(config.getPersonalMaxAmount()+"");// 单笔投标金额的上限
            bean.setTotAmount("1000000000");// 自动投标总金额上限（不算已还金额）
        } else if(type==2){// 2.4.8.出借人自动债权转让签约增强
            remark = "出借人自动债权转让签约增强";
            bean.setTxCode(BankCallConstant.TXCODE_AUTO_CREDIT_INVEST_AUTH_PLUSS);
        }
        bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_MOBILE_PLUS);
        bean.setOrderId(bean.getLogOrderId());
        bean.setAccountId(accountId);// 电子账号
        bean.setForgotPwdUrl(CustomConstants.FORGET_PASSWORD_URL);//忘记密码通知地址
        bean.setLastSrvAuthCode(lastSrvAuthCode);// 前导业务授权码
        bean.setSmsCode(smsCode);// 
        bean.setLogRemark(remark);
        
        return bean;
    }

    private ModelAndView getErrorMV(AutoPlusRequestBean payRequestBean, ModelAndView modelAndView, String status) {
        AutoPlusRetBean repwdResult = new AutoPlusRetBean();
        BaseResultBean resultBean = new BaseResultBean();
        resultBean.setStatusForResponse(status);
        repwdResult.setCallBackAction(payRequestBean.getRetUrl());
        repwdResult.set("chkValue", resultBean.getChkValue());
        repwdResult.set("status", resultBean.getStatus());
        repwdResult.setAcqRes(payRequestBean.getAcqRes());
        modelAndView.addObject("callBackForm", repwdResult);
        return modelAndView;
    }
    
    // 自动出借授权同步回调
    @RequestMapping(AutoPlusDefine.USER_AUTH_INVES_RETURN_ACTION)
    public ModelAndView userAuthInvesReturn(HttpServletRequest request, HttpServletResponse response,
         BankCallBean bean) {
        _log.info("自动出借授权同步回调start,请求参数为：【"+JSONObject.toJSONString(bean, true)+"】");
        ModelAndView modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRUSTEEPAY_VIEW);
        AutoPlusRetBean repwdResult = new AutoPlusRetBean();
        repwdResult.setCallBackAction(request.getParameter("callback").replace("*-*-*","#"));
        bean.convert();
        //业务变更，银行不直接返回accountId，需要根据用户Id查询账号
        if (StringUtils.isNotBlank(bean.getLogUserId())) {
        	BankOpenAccount bankOpenAccount = autoPlusService.getBankOpenAccount(Integer.parseInt(bean.getLogUserId()));
        	repwdResult.set("accountId", bankOpenAccount.getAccount());
		}else{
			repwdResult.set("accountId", bean.getAccountId());
		}
        //出借人签约状态查询
        _log.info("自动出借授权同步回调调用查询接口查询状态");
        BankCallBean retBean=autoPlusService.getUserAuthQUery(Integer.parseInt(bean.getLogUserId()), BankCallConstant.QUERY_TYPE_1);
        _log.info("自动出借授权同步回调调用查询接口查询状态结束  结果为:"+(retBean==null?"空":retBean.getRetCode()));
        bean = retBean;        
        if (bean!=null&& BankCallStatusConstant.RESPCODE_SUCCESS.equals(bean.getRetCode()) && "1".equals(bean.getState())) {
            try {
                // 成功
                modelAndView.addObject("statusDesc", "自动出借授权申请成功！");
                BaseResultBean resultBean = new BaseResultBean();
                resultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
                repwdResult.set("chkValue", resultBean.getChkValue());
                repwdResult.set("status", resultBean.getStatus());
                repwdResult.set("deadline", bean.getDeadline());
                
            } catch (Exception e) {
                _log.info("自动出借授权申请同步插入数据库失败，错误原因:"+e.getMessage());
                BaseResultBean resultBean = new BaseResultBean();
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
                repwdResult.set("chkValue", resultBean.getChkValue());
                repwdResult.set("status", resultBean.getStatus());
            }
        } else {
            // 失败
            modelAndView.addObject("statusDesc", "自动出借授权申请失败,失败原因：" + autoPlusService.getBankRetMsg(bean.getRetCode()));
            
            BaseResultBean resultBean = new BaseResultBean();
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
        }
        
        //------------------------------------------------
        repwdResult.setAcqRes(request.getParameter("acqRes"));
        modelAndView.addObject("callBackForm", repwdResult);
        _log.info("自动出借授权申请同步回调end");
        return modelAndView;
    }
    

    // 异步回调
    @ResponseBody
    @RequestMapping(AutoPlusDefine.USER_AUTH_INVES_BGRETURN_ACTION)
    public BankCallResult userAuthInvesBgreturn(HttpServletRequest request, HttpServletResponse response,
                                                @ModelAttribute BankCallBean bean) {
        _log.info("自动出借授权申请异步回调start");
        _log.info("自动出借授权申请异步回调参数[" + JSONObject.toJSONString(bean, true) + "]");
        BankCallResult result = new BankCallResult();
        String message = "";
        String status = "";
        Map<String, String> params = new HashMap<String, String>();
        // 返回值修改 end
        bean.convert();
        int userId = Integer.parseInt(bean.getLogUserId());
        Users user = this.autoPlusService.getUsers(userId);
        
        if (user != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE))) {
            try {
                // 更新签约状态和日志表
                this.autoPlusService.updateUserAuthInves(userId,bean);
                message = "自动出借授权成功";
                status = ErrorCodeConstant.SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                _log.info("自动出借授权出错,userId:【"+userId+"】错误原因："+e.getMessage());
                message = "自动出借授权失败";
                status = ErrorCodeConstant.STATUS_CE999999;
            }
            
        }else{
         // 失败
            message = "自动出借授权失败,失败原因：" + autoPlusService.getBankRetMsg(bean.getRetCode());
            status = ErrorCodeConstant.STATUS_CE999999;
        }
        // 返回值
        if (StringUtils.isNotBlank(bean.getLogUserId())) {
        	BankOpenAccount bankOpenAccount = autoPlusService.getBankOpenAccount(Integer.parseInt(bean.getLogUserId()));
        	params.put("accountId", bankOpenAccount.getAccount()==null?bean.getAccountId():bankOpenAccount.getAccount());
		}else{
			params.put("accountId", bean.getAccountId());
		}
        params.put("status", status);
        params.put("statusDesc",message);
        
        BaseResultBean resultBean = new BaseResultBean();
        resultBean.setStatusForResponse(status);
        params.put("deadline", bean.getBidDeadline());
        params.put("chkValue", resultBean.getChkValue());
        params.put("acqRes",request.getParameter("acqRes"));
        CommonSoaUtils.noRetPostThree(request.getParameter("callback").replace("*-*-*","#"), params);
        _log.info("自动出借授权异步回调end");
        result.setMessage("自动出借授权成功");
        result.setStatus(true);
        return result;
    }
    

    /**
     * 
     * 自动债转授权
     * @author sss
     * @param request
     * @param response
     * @param payRequestBean
     * @return
     */
    @RequestMapping(value = AutoPlusDefine.USER_CREDIT_AUTH_INVES_ACTION)
    @ResponseBody
    public ModelAndView userCreditAuth(HttpServletRequest request, HttpServletResponse response, @RequestBody AutoPlusRequestBean payRequestBean){
        ModelAndView modelAndView = new ModelAndView(AutoPlusDefine.PATH_TRUSTEE_PAY_ERROR);
        _log.info("自动债转授权请求参数" + JSONObject.toJSONString(payRequestBean, true) + "]");
        // 检查参数是否为空
        if(payRequestBean.checkParmIsNull(modelAndView)){
            getErrorMV(payRequestBean, modelAndView, ErrorCodeConstant.STATUS_CE000001);
            payRequestBean.doNotify(payRequestBean.getErrorMap(ErrorCodeConstant.STATUS_CE000001, "请求参数异常"));
            _log.info("请求参数异常" + JSONObject.toJSONString(payRequestBean, true) + "]");
            return modelAndView;
        }
        
        // 验签  暂时去掉验签
       if (!this.verifyRequestSign(payRequestBean, AutoPlusDefine.REQUEST_MAPPING+ AutoPlusDefine.USER_CREDIT_AUTH_INVES_ACTION)) {
            getErrorMV(payRequestBean, modelAndView, ErrorCodeConstant.STATUS_CE000002);
            payRequestBean.doNotify(payRequestBean.getErrorMap(ErrorCodeConstant.STATUS_CE000002, "验签失败"));
            _log.info("请求参数异常" + JSONObject.toJSONString(payRequestBean, true) + "]");
            return modelAndView;
        }

        // 根据电子账户号查询用户ID
        BankOpenAccount bankOpenAccount = this.autoPlusService.selectBankOpenAccountByAccountId(payRequestBean.getAccountId());
        if(bankOpenAccount == null){
            _log.info("-------------------没有根据电子银行卡找到用户"+payRequestBean.getAccountId()+"！--------------------");
            Map<String, String> params = payRequestBean.getErrorMap(ErrorCodeConstant.STATUS_CE000004,"没有根据电子银行卡找到用户");
            payRequestBean.doNotify(params);
            getErrorMV(payRequestBean, modelAndView, ErrorCodeConstant.STATUS_CE000004);
            return modelAndView;
        }
        
        // 检查用户是否存在
        Users user = autoPlusService.getUsers(bankOpenAccount.getUserId());//用户ID
        if (user == null) {
            _log.info("-------------------用户不存在汇盈金服账户！"+payRequestBean.getAccountId()+"！--------------------");
            Map<String, String> params = payRequestBean.getErrorMap(ErrorCodeConstant.STATUS_CE000007,"用户不存在汇盈金服账户！");
            payRequestBean.doNotify(params);
            getErrorMV(payRequestBean, modelAndView, ErrorCodeConstant.STATUS_CE000007);
            return modelAndView;
        }
        
        Integer userId = user.getUserId();
        if (user.getBankOpenAccount().intValue() != 1) {// 未开户
            _log.info("-------------------用户未开户！"+payRequestBean.getAccountId()+"！--------------------");
            Map<String, String> params = payRequestBean.getErrorMap(ErrorCodeConstant.STATUS_CE000006,"用户未开户！");
            payRequestBean.doNotify(params);
            getErrorMV(payRequestBean, modelAndView, ErrorCodeConstant.STATUS_CE000006);
            return modelAndView;
        }
        
        // 检查是否设置交易密码
        Integer passwordFlag = user.getIsSetPassword();
        if (passwordFlag != 1) {// 未设置交易密码
            _log.info("-------------------未设置交易密码！"+payRequestBean.getAccountId()+"！--------------------status"+user.getIsSetPassword());
            Map<String, String> params = payRequestBean.getErrorMap(ErrorCodeConstant.STATUS_TP000002,"未设置交易密码！");
            payRequestBean.doNotify(params);
            getErrorMV(payRequestBean, modelAndView, ErrorCodeConstant.STATUS_TP000002);
            return modelAndView;
        }
        
        // 查询是否已经授权过
        HjhUserAuth hjhUserAuth=autoPlusService.getHjhUserAuthByUserId(user.getUserId());
        if(hjhUserAuth!=null&&hjhUserAuth.getAutoCreditStatus()==1){
            _log.info("-------------------已经授权过！"+payRequestBean.getAccountId());
            Map<String, String> params = payRequestBean.getErrorMap(ErrorCodeConstant.STATUS_CE000009,"已授权,请勿重复授权！");
            payRequestBean.doNotify(params);
            getErrorMV(payRequestBean, modelAndView, ErrorCodeConstant.STATUS_CE000009);
            return modelAndView;
        }
        
        String smsSeq = this.autoPlusService.selectBankSmsSeq(userId, BankCallMethodConstant.TXCODE_AUTO_CREDIT_INVEST_AUTH_PLUSS);
        if (StringUtils.isBlank(smsSeq)) {
            _log.info("-------------------授权码为空！"+payRequestBean.getAccountId()+"！--------------------status"+user.getIsSetPassword());
            Map<String, String> params = payRequestBean.getErrorMap(ErrorCodeConstant.STATUS_CE000008,"未查询到短信授权码！");
            payRequestBean.doNotify(params);
            getErrorMV(payRequestBean, modelAndView, ErrorCodeConstant.STATUS_CE000008);
            return modelAndView;
        }
        
        // 同步调用路径
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
                + AutoPlusDefine.REQUEST_MAPPING + AutoPlusDefine.USER_CREDIT_AUTH_INVES_RETURN_ACTION + ".do?acqRes="
                + payRequestBean.getAcqRes() + "&callback=" + payRequestBean.getRetUrl().replace("#", "*-*-*");
        // 异步调用路
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
                + AutoPlusDefine.REQUEST_MAPPING + AutoPlusDefine.USER_CREDIT_AUTH_INVES_BGRETURN_ACTION + ".do?acqRes="
                + payRequestBean.getAcqRes() + "&callback=" + payRequestBean.getNotifyUrl().replace("#", "*-*-*");
        
        // 组装发往江西银行参数
        BankCallBean bean = getCommonBankCallBean(payRequestBean.getAccountId(),userId,2,payRequestBean.getChannel(),smsSeq,payRequestBean.getSmsCode());
        bean.setRetUrl(retUrl);//同步通知地址
        bean.setNotifyUrl(bgRetUrl);//异步通知地址
        // 插入日志
        this.autoPlusService.insertUserAuthLog(user.getUserId(), bean,1, BankCallConstant.QUERY_TYPE_2);
        // 跳转到汇付天下画面
        try {
            modelAndView = BankCallUtils.callApi(bean);
        } catch (Exception e) {
            e.printStackTrace();
            _log.info("调用银行接口失败！"+e.getMessage());
            Map<String, String> params = payRequestBean.getErrorMap(ErrorCodeConstant.STATUS_CE999999,"系统异常！");
            payRequestBean.doNotify(params);
            getErrorMV(payRequestBean, modelAndView, ErrorCodeConstant.STATUS_CE999999);
            return modelAndView;
            
        }
        _log.info("自动债转授权申请end");
        return modelAndView;
    }

    // 自动债转授权同步回调
    @RequestMapping(AutoPlusDefine.USER_CREDIT_AUTH_INVES_RETURN_ACTION)
    public ModelAndView userCreditAuthInvesReturn(HttpServletRequest request, HttpServletResponse response,
         BankCallBean bean) {
        _log.info("自动债转授权同步回调start,请求参数为：【"+JSONObject.toJSONString(bean, true)+"】");
        ModelAndView modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRUSTEEPAY_VIEW);
        AutoPlusRetBean repwdResult = new AutoPlusRetBean();
        repwdResult.setCallBackAction(request.getParameter("callback").replace("*-*-*","#"));
        bean.convert();
        //出借人签约状态查询
        _log.info("自动债转授权同步回调调用查询接口查询状态");
        BankCallBean retBean=autoPlusService.getUserAuthQUery(Integer.parseInt(bean.getLogUserId()), BankCallConstant.QUERY_TYPE_2);
        _log.info("自动债转授权同步回调调用查询接口查询状态结束  结果为:"+(retBean==null?"空":retBean.getRetCode()));
        bean = retBean;  
        if (bean!=null&& BankCallStatusConstant.RESPCODE_SUCCESS.equals(bean.getRetCode()) && "1".equals(bean.getState())) {
            // 成功
            try {
                BaseResultBean resultBean = new BaseResultBean();
                resultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
                repwdResult.set("chkValue", resultBean.getChkValue());
                repwdResult.set("status", resultBean.getStatus());
            } catch (Exception e) {
                _log.info("自动债转授权同步插入数据库失败，错误原因:"+e.getMessage());
                BaseResultBean resultBean = new BaseResultBean();
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
                repwdResult.set("chkValue", resultBean.getChkValue());
                repwdResult.set("status", resultBean.getStatus());
            }
        } else {
            // 失败
            modelAndView.addObject("statusDesc", "自动债转授权申请失败,失败原因：" + autoPlusService.getBankRetMsg(bean.getRetCode()));
            
            BaseResultBean resultBean = new BaseResultBean();
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
        }
        
        //------------------------------------------------
        repwdResult.setAcqRes(request.getParameter("acqRes"));
        modelAndView.addObject("callBackForm", repwdResult);
        _log.info("自动债转授权授权申请同步回调end");
        return modelAndView;
    }

    // 自动债转授权异步回调
    @ResponseBody
    @RequestMapping(AutoPlusDefine.USER_CREDIT_AUTH_INVES_BGRETURN_ACTION)
    public BankCallResult userCreditAuthInvesBgreturn(HttpServletRequest request, HttpServletResponse response,
                                                      @ModelAttribute BankCallBean bean) {
        _log.info("自动债转授权申请异步回调start");
        BankCallResult result = new BankCallResult();
        String message = "";
        String status = "";
        Map<String, String> params = new HashMap<String, String>();
        // 返回值修改 end
        bean.convert();
        int userId = Integer.parseInt(bean.getLogUserId());
        Users user = this.autoPlusService.getUsers(userId);
        
        if (user != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE))) {
            try {
                // 更新签约状态和日志表
                this.autoPlusService.updateUserAuthInves(userId,bean);
                message = "自动债转授权成功";
                status = ErrorCodeConstant.SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                _log.info("自动债转授权出错,userId:【"+userId+"】错误原因："+e.getMessage());
                message = "自动债转授权失败";
                status = ErrorCodeConstant.STATUS_CE999999;
            }
        }else{
         // 失败
            message = "自动债转授权失败,失败原因：" + autoPlusService.getBankRetMsg(bean.getRetCode());
            status = ErrorCodeConstant.STATUS_CE999999;
        }
        // 返回值
        params.put("accountId", bean.getAccountId());
        params.put("status", status);
        params.put("statusDesc",message);
        BaseResultBean resultBean = new BaseResultBean();
        resultBean.setStatusForResponse(status);
        params.put("chkValue", resultBean.getChkValue());
        params.put("acqRes",request.getParameter("acqRes"));
        CommonSoaUtils.noRetPostThree(request.getParameter("callback").replace("*-*-*","#"), params);
        _log.info("自动债转授权异步回调end");
        result.setMessage("自动债转授权成功");
        result.setStatus(true);
        return result;
    }
    
}
