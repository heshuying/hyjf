package com.hyjf.wechat.controller.user.autoplus;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.autoup.AutoPlusService;
import com.hyjf.bank.service.user.bankopen.BankOpenService;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.HjhUserAuthConfig;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseMapBean;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.util.ResultEnum;

/**
 * 
 * 自动出借债转授权
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月2日
 * @see 上午11:22:44
 */
@Controller(AutoPlusDefine.CONTROLLER_NAME)
@RequestMapping(value = AutoPlusDefine.REQUEST_MAPPING)
public class AutoPlusController extends BaseController {

    private Logger _log = LoggerFactory.getLogger(AutoPlusController.class);

    @Autowired
    private AutoPlusService autoPlusService;

    @Autowired
    private BankOpenService openAccountService;

    /**
     * 
     * 发送授权短信验证码
     * 请求地址: /wx/user/autoplus/sendcode.page
     * 需要参数: 授权类型userAutoType(0 自动投标授权 1 自动债转授权) mobile
     * @author sunss
     * @param request
     * @param userAutoType
     * @param mobile
     * @return
     */
    @SignValidate
    @RequestMapping(value = AutoPlusDefine.USER_AUTH_SENDSMS_ACTION)
    @ResponseBody
    public BaseResultBean sendSmsCode(HttpServletRequest request, @RequestParam String userAutoType, String mobile) {
        Integer userId = requestUtil.getRequestUserId(request);
        _log.info("发送授权短信验证码 接口,手机号为：【" + mobile + "】,授权类型为【" + userAutoType + "】,userid为：【" + userId + "】");
        AutoPlusResultBean result = new AutoPlusResultBean();

        HjhUserAuth userAuth = autoPlusService.getHjhUserAuthByUserId(userId);
        if (userAuth != null) {
            if ("0".equals(userAutoType) && userAuth.getAutoInvesStatus() == 1) {
                // 自动投标检查
                return result.setEnum(ResultEnum.ERROR_005);
            }
            if ("1".equals(userAutoType) && userAuth.getAutoCreditStatus() == 1) {
                // 自动债转检查
                return result.setEnum(ResultEnum.ERROR_006);
            }
        }

        // 银行接口调用需要的业务交易代码
        String srvTxCode;
        if ("0".equals(userAutoType)) {
            srvTxCode = BankCallConstant.TXCODE_AUTOBID_AUTH_PLUS;
        } else if ("1".equals(userAutoType)) {
            srvTxCode = BankCallConstant.TXCODE_AUTO_CREDIT_INVEST_AUTH_PLUS;
        } else {
            return result.setEnum(ResultEnum.PARAM);
        }

        if (StringUtils.isBlank(mobile)) {
            mobile = openAccountService.getUsersMobile(userId);
        }

        // 调用短信发送接口
        BankCallBean mobileBean =
                this.openAccountService.sendSms(userId, srvTxCode, mobile, BankCallConstant.CHANNEL_WEI);
        if (Validator.isNull(mobileBean)) {
            return result.setEnum(ResultEnum.ERROR_007);
        }
        // 短信发送返回结果码
        String retCode = mobileBean.getRetCode();
        if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)
                && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
            return result.setEnum(ResultEnum.ERROR_007);
        }
        if (Validator.isNull(mobileBean.getSrvAuthCode()) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
            return result.setEnum(ResultEnum.ERROR_007);
        }

        result.setEnum(ResultEnum.SUCCESS3);
        // 业务授权码
        String srvAuthCode = mobileBean.getSrvAuthCode();
        result.setSrvAuthCode(srvAuthCode);
        return result;
    }

    /**
     * 
     * 自动出借授权接口
     * 请求地址：/wx/user/autoplus/userAuthInves.page
     * 请求参数：
     * @author sunss
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @SignValidate
    @RequestMapping(AutoPlusDefine.USER_AUTH_INVES_ACTION)
    public ModelAndView userAuthInves(HttpServletRequest request, HttpServletResponse response)
        throws UnsupportedEncodingException {

        String srvAuthCode = request.getParameter("srvAuthCode");
        String code = request.getParameter("code");
        Integer userId = requestUtil.getRequestUserId(request);
        String sign = request.getParameter("sign");
        _log.info("自动出借授权接口,srvAuthCode为：【" + srvAuthCode + "】,code为【" + code + "】,userid为：【" + userId + "】");
        // 判断是否授权过
        HjhUserAuth hjhUserAuth = autoPlusService.getHjhUserAuthByUserId(userId);
        if (StringUtils.isBlank(code) || StringUtils.isBlank(srvAuthCode)) {
            return getErrorModelAndView(ResultEnum.PARAM, sign, "0",hjhUserAuth);
        }

        Users users = autoPlusService.getUsers(userId);
        if (users.getBankOpenAccount() == 0) {// 未开户
        	_log.error("用户未开户...userid:["+users.getUserId()+"]");
            return getErrorModelAndView(ResultEnum.USER_ERROR_200, sign,"0", hjhUserAuth);
        }

        // 判断用户是否设置过交易密码
        if (users.getIsSetPassword() == 0) {// 未设置交易密码
        	_log.error("用户未设置交易密码...userid:["+users.getUserId()+"]");
            return getErrorModelAndView(ResultEnum.USER_ERROR_201, sign,"0", hjhUserAuth);
        }

        // 判断是否授权过
        if (hjhUserAuth != null && hjhUserAuth.getAutoInvesStatus().intValue()==1) {
        	_log.error("用户已经自动出借授权过...userid:["+users.getUserId()+"]");
            ModelAndView mv = getErrorModelAndView(ResultEnum.USER_ERROR_201, sign,"0", hjhUserAuth);
            if (hjhUserAuth.getAutoInvesStatus() == 1) {
                return mv;
            }
        }
        // 组装发往江西银行参数
        BankCallBean bean = getCommonBankCallBean(users, 1, srvAuthCode, code, sign);

        // 插入日志
        this.autoPlusService.insertUserAuthLog(users.getUserId(), bean, 1, BankCallConstant.QUERY_TYPE_1);
        // 跳转到汇付天下画面
        try {
            ModelAndView modelAndView = BankCallUtils.callApi(bean);
            return modelAndView;
        } catch (Exception e) {
            _log.error("调用江西银行接口异常...", e);
            return getErrorModelAndView(ResultEnum.USER_ERROR_204, sign,"0", hjhUserAuth);
        }
    }

    /**
     * 组装跳转错误页面MV
     * @param param
     * @param sign
     * @param type
     * @param hjhUserAuth
     * @return
     */
    private ModelAndView getErrorModelAndView(ResultEnum param, String sign,String type, HjhUserAuth hjhUserAuth) {
        ModelAndView modelAndView = new ModelAndView(AutoPlusDefine.JUMP_HTML);
        BaseMapBean baseMapBean = new BaseMapBean();
        baseMapBean.set(CustomConstants.APP_STATUS, param.getStatus());
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, param.getStatusDesc());
        baseMapBean.set(CustomConstants.APP_SIGN, sign);
        baseMapBean.set("autoInvesStatus", hjhUserAuth==null?"0":hjhUserAuth.getAutoInvesStatus()==null?"0":hjhUserAuth.getAutoInvesStatus()+ "");
        baseMapBean.set("autoCreditStatus", hjhUserAuth==null?"0":hjhUserAuth.getAutoCreditStatus()==null?"0":hjhUserAuth.getAutoCreditStatus() + "");
        baseMapBean.set("userAutoType", type);
        baseMapBean.setCallBackAction(CustomConstants.HOST + AutoPlusDefine.JUMP_HTML_ERROR_PATH);
        modelAndView.addObject("callBackForm", baseMapBean);
        return modelAndView;
    }
    
    /**
     * 组装跳转成功页面MV
     * @param sign
     * @param type
     * @param autoInvesStatus
     * @param autoCreditStatus
     * @return
     */
    private ModelAndView getSuccessModelAndView(String sign, String type, HjhUserAuth hjhUserAuth) {
        ModelAndView modelAndView = new ModelAndView(AutoPlusDefine.JUMP_HTML);
        BaseMapBean baseMapBean = new BaseMapBean();
        baseMapBean.set(CustomConstants.APP_STATUS, ResultEnum.SUCCESS.getStatus());
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, ResultEnum.SUCCESS.getStatusDesc());
        baseMapBean.set(CustomConstants.APP_SIGN, sign);
        baseMapBean.set("autoInvesStatus", hjhUserAuth==null?"0":hjhUserAuth.getAutoInvesStatus()==null?"0":hjhUserAuth.getAutoInvesStatus()+ "");
        baseMapBean.set("autoCreditStatus", hjhUserAuth==null?"0":hjhUserAuth.getAutoCreditStatus()==null?"0":hjhUserAuth.getAutoCreditStatus() + "");
        baseMapBean.set("userAutoType", type);
         
        baseMapBean.setCallBackAction(CustomConstants.HOST + AutoPlusDefine.JUMP_HTML_SUCCESS_PATH);
        modelAndView.addObject("callBackForm", baseMapBean);
        return modelAndView;
    }

    private BankCallBean getCommonBankCallBean(Users users, int type, String lastSrvAuthCode, String smsCode,
        String sign) {

        String remark = "";
        String txcode = "";
        // 同步调用路径
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + AutoPlusDefine.REQUEST_HOME
                + AutoPlusDefine.REQUEST_MAPPING;
        // 异步调用路
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + AutoPlusDefine.REQUEST_HOME
                + AutoPlusDefine.REQUEST_MAPPING;
        String forgetPassworedUrl = CustomConstants.FORGET_PASSWORD_URL + "?sign=" + sign;

        BankCallBean bean = new BankCallBean();
        if (type == 1) {// 2.4.4.出借人自动投标签约增强
            retUrl += AutoPlusDefine.USER_AUTH_INVES_RETURN_ACTION + ".page";
            bgRetUrl += AutoPlusDefine.USER_AUTH_INVES_BGRETURN_ACTION + ".json";
            remark = "出借人自动投标签约增强";
            txcode = BankCallConstant.TXCODE_AUTO_BID_AUTH_PLUS;
            HjhUserAuthConfig config=CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_AUTO_TENDER_AUTH);
            bean.setDeadline(GetDate.date2Str(GetDate.countDate(1,config.getAuthPeriod()),new SimpleDateFormat("yyyyMMdd")));// 签约到期时间
            bean.setTxAmount(config.getPersonalMaxAmount()+"");// 单笔投标金额的上限
            bean.setTotAmount("1000000000");// 自动投标总金额上限（不算已还金额）
        } else if (type == 2) {// 2.4.8.出借人自动债权转让签约增强
            retUrl += AutoPlusDefine.USER_AUTH_CREDIT_RETURN_ACTION + ".page";
            bgRetUrl += AutoPlusDefine.USER_AUTH_CREDIT_BGRETURN_ACTION + ".json";
            remark = "出借人自动债权转让签约增强";
            txcode = BankCallConstant.TXCODE_AUTO_CREDIT_INVEST_AUTH_PLUSS;
        }
        String orderId = GetOrderIdUtils.getOrderId2(users.getUserId());
        // 取得用户在江西银行的客户号
        BankOpenAccount bankOpenAccount = autoPlusService.getBankOpenAccount(users.getUserId());
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_MOBILE_PLUS);
        bean.setTxCode(txcode);
        bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
        bean.setTxDate(GetOrderIdUtils.getTxDate());
        bean.setTxTime(GetOrderIdUtils.getTxTime());
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
        bean.setChannel(BankCallConstant.CHANNEL_WEI);

        bean.setAccountId(bankOpenAccount.getAccount());// 电子账号
        bean.setOrderId(orderId);
        bean.setForgotPwdUrl(forgetPassworedUrl);// 忘记密码通知地址
        bean.setRetUrl(retUrl+"?sign="+sign);// 同步通知地址
        bean.setNotifyUrl(bgRetUrl);// 异步通知地址
        bean.setLastSrvAuthCode(lastSrvAuthCode);// 前导业务授权码
        bean.setSmsCode(smsCode);// 手机验证码

        // 操作者ID
        bean.setLogUserId(String.valueOf(users.getUserId()));
        bean.setLogOrderId(orderId);
        bean.setLogRemark(remark);
        bean.setLogClient(0);
        return bean;
    }

    
    /**
     * 用户授权自动出借同步回调
     *  
     * @param request
     * @param response
     * @return
     */
    @SignValidate
    @RequestMapping(AutoPlusDefine.USER_AUTH_INVES_RETURN_ACTION)
    public ModelAndView userAuthInvesReturn(@ModelAttribute BankCallBean bean, HttpServletRequest request) {
        bean.convert();
        String sign = request.getParameter("sign");
        _log.info("用户授权自动出借同步回调bean: {}", JSONObject.toJSONString(bean));
        Integer userId = requestUtil.getRequestUserId(request);
        HjhUserAuth hjhUserAuth = autoPlusService.getHjhUserAuthByUserId(userId);
        // 返回失败
        if (bean.getRetCode() != null && !BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
            return getErrorModelAndView(ResultEnum.USER_ERROR_204, sign, "0", hjhUserAuth);
        }

        // 出借人签约状态查询
        BankCallBean retBean = autoPlusService.getUserAuthQUery(userId, BankCallConstant.QUERY_TYPE_1);

        if ("1".equals(retBean.getState())) {
            if(hjhUserAuth==null){
                hjhUserAuth = new HjhUserAuth();
            }
            hjhUserAuth.setAutoInvesStatus(1);
            return getSuccessModelAndView(sign, "0", hjhUserAuth);
        }
        return getErrorModelAndView(ResultEnum.USER_ERROR_204, sign, "0", hjhUserAuth);
    }
    
    /**
     * 用户自动出借授权异步
     */
    @ResponseBody
    @RequestMapping(AutoPlusDefine.USER_AUTH_INVES_BGRETURN_ACTION)
    public BankCallResult userAuthInvesBgreturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {
        BankCallResult result = new BankCallResult();
        bean.convert();
        _log.info("用户授权自动出借异步回调bean: {}", JSONObject.toJSONString(bean));
        Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
        // 查询用户开户状态
        Users user = this.autoPlusService.getUsers(userId);

        // 成功
        if (user != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE))) {
            try {
                //出借人签约状态查询
                BankCallBean retBean=autoPlusService.getUserAuthQUery(userId,BankCallConstant.QUERY_TYPE_1);
                if("1".equals(retBean.getState())){
                    // 更新签约状态和日志表
                    this.autoPlusService.updateUserAuthInves(userId,bean);
                }
               
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        result.setMessage("用户授权自动出借成功");
        result.setStatus(true);
        return result;
    }
    
    /**
     * 
     * 用户自动债转授权
     * @author sunss
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @SignValidate
    @RequestMapping(AutoPlusDefine.USER_AUTH_CREDIT_ACTION)
    public ModelAndView userAuthCredit(HttpServletRequest request, HttpServletResponse response)
        throws UnsupportedEncodingException {

        String srvAuthCode = request.getParameter("srvAuthCode");
        String code = request.getParameter("code");
        Integer userId = requestUtil.getRequestUserId(request);
        String sign = request.getParameter("sign");
        _log.info("自动债转授权接口,srvAuthCode为：【" + srvAuthCode + "】,code为【" + code + "】,userid为：【" + userId + "】");
        // 判断是否授权过
        HjhUserAuth hjhUserAuth = autoPlusService.getHjhUserAuthByUserId(userId);
        if (StringUtils.isBlank(code) || StringUtils.isBlank(srvAuthCode)) {
            return getErrorModelAndView(ResultEnum.PARAM, sign, "1", hjhUserAuth);
        }

        Users users = autoPlusService.getUsers(userId);
        if (users.getBankOpenAccount() == 0) {// 未开户
        	_log.error("用户未开户...userid:["+users.getUserId()+"]");
            return getErrorModelAndView(ResultEnum.USER_ERROR_200, sign, "1", hjhUserAuth);
        }

        // 判断用户是否设置过交易密码
        if (users.getIsSetPassword() == 0) {// 未设置交易密码
        	_log.error("用户未设置交易密码...userid:["+users.getUserId()+"]");
            return getErrorModelAndView(ResultEnum.USER_ERROR_201, sign, "1", hjhUserAuth);
        }
 
        if (hjhUserAuth != null && hjhUserAuth.getAutoCreditStatus().intValue()==1) {
        	 _log.error("用户已经自动债转授权过...userid:["+users.getUserId()+"]");
            ModelAndView mv = getErrorModelAndView(ResultEnum.USER_ERROR_203, sign,"1", hjhUserAuth);
            if (hjhUserAuth.getAutoCreditStatus() == 1) {
                return mv;
            }
        }
        // 组装发往江西银行参数
        BankCallBean bean = getCommonBankCallBean(users, 2, srvAuthCode, code, sign);

        // 插入日志
        this.autoPlusService.insertUserAuthLog(users.getUserId(), bean, 1, BankCallConstant.QUERY_TYPE_2);
        // 跳转到汇付天下画面
        try {
            ModelAndView modelAndView = BankCallUtils.callApi(bean);
            return modelAndView;
        } catch (Exception e) {
            _log.error("调用江西银行接口异常...", e);
            return getErrorModelAndView(ResultEnum.USER_ERROR_205, sign, "1", hjhUserAuth);
        }
    }
    
    /**
     * 用户授权自动债转同步回调
     *  
     * @param request
     * @param response
     * @return
     */
    @SignValidate
    @RequestMapping(AutoPlusDefine.USER_AUTH_CREDIT_RETURN_ACTION)
    public ModelAndView userAuthCreditReturn(@ModelAttribute BankCallBean bean, HttpServletRequest request) {
        bean.convert();
        String sign = requestUtil.getRequestSign(request);
        _log.info("用户授权自动债转同步回调bean: {}", JSONObject.toJSONString(bean));
        Integer userId = requestUtil.getRequestUserId(request);
        HjhUserAuth hjhUserAuth = autoPlusService.getHjhUserAuthByUserId(userId);
        // 返回失败
        if (bean.getRetCode() != null && !BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
            return getErrorModelAndView(ResultEnum.USER_ERROR_204, sign,"1", hjhUserAuth);
        }

        // 出借人签约状态查询
        BankCallBean retBean = autoPlusService.getUserAuthQUery(userId, BankCallConstant.QUERY_TYPE_2);
        if(hjhUserAuth==null){
            hjhUserAuth = new HjhUserAuth();
        }
        hjhUserAuth.setAutoCreditStatus(1);
        if ("1".equals(retBean.getState())) {
            return getSuccessModelAndView(sign, "1", hjhUserAuth);
        }
        return getErrorModelAndView(ResultEnum.USER_ERROR_205, sign, "1", hjhUserAuth);
    }
    
    /**
     * 用户自动债转授权异步
     */
    @ResponseBody
    @RequestMapping(AutoPlusDefine.USER_AUTH_CREDIT_BGRETURN_ACTION)
    public BankCallResult userAuthCreditBgreturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {
        BankCallResult result = new BankCallResult();
        bean.convert();
        _log.info("用户授权自动债转异步回调bean: {}", JSONObject.toJSONString(bean));
        Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
        // 查询用户开户状态
        Users user = this.autoPlusService.getUsers(userId);

        // 成功
        if (user != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE))) {
            try {
                //出借人签约状态查询
                BankCallBean retBean=autoPlusService.getUserAuthQUery(userId,BankCallConstant.QUERY_TYPE_2);
                if("1".equals(retBean.getState())){
                    // 更新签约状态和日志表
                    this.autoPlusService.updateUserAuthInves(userId,bean);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        result.setMessage("用户授权自动债转成功");
        result.setStatus(true);
        return result;
    }
    
    
}
