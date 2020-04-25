package com.hyjf.api.aems.directrecharge;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.server.user.accountopenpage.OpenAccountPageDefine;
import com.hyjf.api.server.user.accountopenpage.OpenAccountPageResultBean;
import com.hyjf.api.server.user.directrecharge.UserDirectRechargeRequestBean;
import com.hyjf.api.server.user.trusteepay.TrusteePayResultBean;
import com.hyjf.api.server.util.ErrorCodeConstant;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.user.directrecharge.DirectRechargeServer;
import com.hyjf.bank.service.user.directrecharge.UserDirectRechargeBean;
import com.hyjf.bank.service.user.recharge.RechargeService;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.vip.apply.ApplyDefine;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 外部服务接口:3.3.充值页面
 *
 */
@Controller
@RequestMapping(AemsUserDirectRechargeDefine.REQUEST_MAPPING)
public class AemsUserDirectRechargeServer extends BaseController {

    Logger _log = LoggerFactory.getLogger(AemsUserDirectRechargeServer.class);

    @Autowired
    private DirectRechargeServer directRechargeServer ;
    
    @Autowired
    private RechargeService userRechargeService;

    /**
     * 充值页面
     *
     * @param userRechargeRequestBean
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(AemsUserDirectRechargeDefine.RECHARGE_ACTION)
    public ModelAndView recharge(@RequestBody UserDirectRechargeRequestBean userRechargeRequestBean, HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(AemsUserDirectRechargeServer.class.getName(), AemsUserDirectRechargeDefine.RECHARGE_ACTION);
        ModelAndView modelAndView = new ModelAndView(OpenAccountPageDefine.PATH_OPEN_ACCOUNT_PAGE_ERROR);
        _log.info("-----------充值页面-----------");
        AemsUserDirectRechargeResultBean resultBean = new AemsUserDirectRechargeResultBean();
        try {
            if (checkIsNull(userRechargeRequestBean)) {
                getErrorMV(userRechargeRequestBean, modelAndView, ErrorCodeConstant.STATUS_CE000001, "请求参数异常");
                _log.info("请求参数异常[" + JSONObject.toJSONString(userRechargeRequestBean, true) + "]");
                return modelAndView;
            }
            // 加签字段     时间戳  电子帐户号   手机号  idno   cardNo  txamount   name  
            if (!this.AEMSVerifyRequestSign(userRechargeRequestBean, AemsUserDirectRechargeDefine.REQUEST_MAPPING+AemsUserDirectRechargeDefine.RECHARGE_ACTION)) {
                _log.info("----验签失败----");
                getErrorMV(userRechargeRequestBean, modelAndView, ErrorCodeConstant.STATUS_CE000002, "验签失败");
                _log.info("验签失败[" + JSONObject.toJSONString(userRechargeRequestBean, true) + "]");
                return modelAndView;
            }
            // 根据用户电子账户号查询用户信息
            BankOpenAccount bankOpenAccount = this.directRechargeServer.getBankOpenAccount(userRechargeRequestBean.getAccountId());
            if (bankOpenAccount == null) {
                _log.info("查询用户开户信息失败,用户电子账户号:[" + userRechargeRequestBean.getAccountId() + "]");
                getErrorMV(userRechargeRequestBean, modelAndView, ErrorCodeConstant.STATUS_CE000006, "查询用户开户信息失败");
                return modelAndView;
            }
            // 用户ID
            Integer userId = bankOpenAccount.getUserId();
            // 根据用户ID查询用户信息
            Users user = this.directRechargeServer.getUsersByUserId(userId);
            if (user == null) {
                getErrorMV(userRechargeRequestBean, modelAndView, ErrorCodeConstant.STATUS_CE000004, "查询用户开户信息失败");
                return modelAndView;
            }
            // 根据用户ID查询用户详情
            UsersInfo userInfo = this.directRechargeServer.getUsersInfoByUserId(userId);
            if (userInfo == null) {
                getErrorMV(userRechargeRequestBean, modelAndView, ErrorCodeConstant.STATUS_CE000004, "查询用户开户信息失败");
                return modelAndView;
            }
            // 根据用户ID查询用户平台银行卡信息
            BankCard bankCard = this.directRechargeServer.getBankCardByUserId(userId);
            if (bankCard == null) {
                _log.info("根据用户ID查询用户银行卡信息失败,用户电子账户号:[" + userRechargeRequestBean.getAccountId() + "],用户ID:[" + userId + "].");
                getErrorMV(userRechargeRequestBean, modelAndView, ErrorCodeConstant.STATUS_BC000002, "查询用户银行卡信息失败");
                return modelAndView;
            }
            
            // 用户汇盈平台的银行卡卡号
            String localCardNo = bankCard.getCardNo() == null ? "" : bankCard.getCardNo();
            if (!userRechargeRequestBean.getCardNo().equals(localCardNo)) {
                _log.info("用户银行卡信息不一致,用户电子账户号:[" + userRechargeRequestBean.getAccountId() + "],请求银行卡号:[" + userRechargeRequestBean.getCardNo() + "],平台保存的银行卡号:[" + localCardNo + "].");
                getErrorMV(userRechargeRequestBean, modelAndView, ErrorCodeConstant.STATUS_NC000002, "用户银行卡信息不一致");
                return modelAndView;
            }
            
            // 检查是否设置交易密码
            Integer passwordFlag = user.getIsSetPassword();
            if (passwordFlag != 1) {// 未设置交易密码
                getErrorMV(userRechargeRequestBean, modelAndView, ErrorCodeConstant.STATUS_TP000002, "未设置交易密码！");
                return modelAndView;
            }
            // 缴费授权
            /*if (user.getPaymentAuthStatus() !=1) {
                _log.info("用户未进行缴费授权,用户电子账户号:[" + userRechargeRequestBean.getAccountId() + "],用户ID:[" + userId + "].");
                getErrorMV(userRechargeRequestBean, modelAndView, ErrorCodeConstant.STATUS_CE000011, "用户未进行缴费授权！");
                return modelAndView;
            }*/
            
            // 拼装参数  调用江西银行
            // 同步调用路径
            String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
                    + AemsUserDirectRechargeDefine.REQUEST_MAPPING + AemsUserDirectRechargeDefine.RETURL_SYN_ACTION + ".do?acqRes="
                    + userRechargeRequestBean.getAcqRes() + StringPool.AMPERSAND + "callback="
                    + userRechargeRequestBean.getRetUrl().replace("#", "*-*-*");
            // 异步调用路
            String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
                    + AemsUserDirectRechargeDefine.REQUEST_MAPPING + AemsUserDirectRechargeDefine.RETURL_ASY_ACTION + ".do?acqRes="
                    + userRechargeRequestBean.getAcqRes() + "&phone="+userRechargeRequestBean.getMobile()+"&callback=" + userRechargeRequestBean.getBgRetUrl().replace("#", "*-*-*");
            
            // 用户ID
            UserDirectRechargeBean directRechargeBean = new UserDirectRechargeBean();
            PropertyUtils.copyProperties(directRechargeBean, userRechargeRequestBean);
            directRechargeBean.setUserId(userId);
            directRechargeBean.setIp(CustomUtil.getIpAddr(request));
            directRechargeBean.setCardNo(localCardNo);
            directRechargeBean.setUserName(user.getUsername());
            // 同步 异步
            directRechargeBean.setRetUrl(retUrl);
            directRechargeBean.setNotifyUrl(bgRetUrl);
            directRechargeBean.setForgotPwdUrl(userRechargeRequestBean.getForgotPwdUrl());
            modelAndView = directRechargeServer.insertGetMV(directRechargeBean);
            
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            _log.info("充值发生异常,错误信息:[" + e.getMessage() + "]");
            // 充值失败
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("充值失败");
            return modelAndView;
        }
    }

    /**
     * 
     * 同步
     * @author sunss
     * @param request
     * @param response
     * @param bean
     * @return
     */
    @RequestMapping(AemsUserDirectRechargeDefine.RETURL_SYN_ACTION)
    public ModelAndView pageReturn(HttpServletRequest request, HttpServletResponse response, BankCallBean bean) {
        _log.info("页面充值同步回调start,请求参数为：【" + JSONObject.toJSONString(bean, true) + "】");
        ModelAndView modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRUSTEEPAY_VIEW);
        TrusteePayResultBean repwdResult = new TrusteePayResultBean();
        repwdResult.setCallBackAction(request.getParameter("callback").replace("*-*-*", "#"));
        String isSuccess = request.getParameter("isSuccess");
        if (bean == null) {
            _log.info("调用江西银行页面充值接口,银行返回空");
            modelAndView.addObject("statusDesc", "页面充值,调用银行接口失败！");
            BaseResultBean resultBean = new BaseResultBean();
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
            repwdResult.set("acqRes", request.getParameter("acqRes"));
            modelAndView.addObject("callBackForm", repwdResult);
            return modelAndView;
        }
        if (isSuccess != null && "1".equals(isSuccess)) {
            _log.info("页面充值成功,跳转到成功链接用户ID:[" + bean.getLogUserId() + "]");
            // 成功
            BaseResultBean resultBean = new BaseResultBean();
            modelAndView.addObject("statusDesc", "充值成功");
            resultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
            repwdResult.set("acqRes", request.getParameter("acqRes"));
            modelAndView.addObject("callBackForm", repwdResult);
            _log.info("页面充值同步第三方返回参数："+JSONObject.toJSONString(repwdResult));
            return modelAndView;
        }
        // 银行返回响应代码
        String retCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
        
        if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
            // 根据银行相应代码,查询错误信息
            String retMsg = directRechargeServer.getBankRetMsg(retCode);
            _log.info("页面充值失败,银行返回响应代码:[" + retCode + "],retMsg:[" + retMsg + "]");

            BaseResultBean resultBean = new BaseResultBean();
            modelAndView.addObject("statusDesc", "充值失败,调用银行接口失败");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
            repwdResult.set("acqRes", request.getParameter("acqRes"));
            modelAndView.addObject("callBackForm", repwdResult);
            _log.info("页面充值同步第三方返回参数："+JSONObject.toJSONString(repwdResult));
            return modelAndView;
        }else{
            _log.info("页面充值成功,用户ID:[" + bean.getLogUserId() + "]");
            // 成功
            BaseResultBean resultBean = new BaseResultBean();
            modelAndView.addObject("statusDesc", "充值成功");
            resultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
            repwdResult.set("acqRes", request.getParameter("acqRes"));
            modelAndView.addObject("callBackForm", repwdResult);
            _log.info("页面充值同步第三方返回参数："+JSONObject.toJSONString(repwdResult));
            return modelAndView;
        }
    }

    /**
     * 异步回调
     */
    @ResponseBody
    @RequestMapping(AemsUserDirectRechargeDefine.RETURL_ASY_ACTION)
    public BankCallResult bgreturn(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute BankCallBean bean) {
        _log.info("页面充值异步回调start");
        BankCallResult result = new BankCallResult();
        BaseResultBean resultBean = new BaseResultBean();
        Map<String, String> params = new HashMap<String, String>();

        if (bean == null) {
            _log.info("调用江西银行充值接口,银行异步返回空");
            params = setResult(resultBean,result,ErrorCodeConstant.STATUS_CE999999,"充值失败,调用银行接口失败",request.getParameter("acqRes"),true);
            CommonSoaUtils.noRetPostThree(request.getParameter("callback").replace("*-*-*", "#"), params);
            return result;
        }

        String phone = request.getParameter("phone");
        bean.setMobile(phone);
        bean.convert();
        int userId = Integer.parseInt(bean.getLogUserId());
        // 银行返回响应代码
        String retCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
        JSONObject msg = this.userRechargeService.handleRechargeOnlineInfo(bean, params);
        if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
            // 根据银行相应代码,查询错误信息
            String retMsg = directRechargeServer.getBankRetMsg(retCode);
            _log.info("充值失败,银行返回响应代码:[" + retCode + "],retMsg:[" + retMsg + "],订单号:[" + bean.getLogOrderId() + "].");
            params = setResult(resultBean,result,ErrorCodeConstant.STATUS_CE999999,"充值失败,调用银行接口失败",request.getParameter("acqRes"),true);
            _log.info("页面充值异步第三方返回参数："+JSONObject.toJSONString(params));
            CommonSoaUtils.noRetPostThree(request.getParameter("callback").replace("*-*-*", "#"), params);
            return result;
        }else{
            // 充值成功
            if (msg != null && msg.get("error").equals("0")) {
                _log.info("充值成功,手机号:[" + bean.getMobile() + "],用户ID:[" + userId + "],充值金额:[" + bean.getTxAmount() + "]");
                params = setResult(resultBean,result,ErrorCodeConstant.SUCCESS,"充值成功",request.getParameter("acqRes"),true);
                params.put("ip", bean.getUserIP());
                params.put("mobile",bean.getMobile());
                params.put("txAmount", bean.getTxAmount());
                params.put("orderId", bean.getLogOrderId());
                _log.info("页面充值异步第三方返回参数："+JSONObject.toJSONString(params));
                CommonSoaUtils.noRetPostThree(request.getParameter("callback").replace("*-*-*", "#"), params);
                return result;
            } else {
                _log.info("充值失败,手机号:[" + bean.getMobile() + "],用户ID:[" + userId + "],冲值金额:[" + bean.getTxAmount() + "],失败原因:[" + msg.get("data") + "].");
                params = setResult(resultBean,result,ErrorCodeConstant.STATUS_CE999999,"充值失败",request.getParameter("acqRes"),false);
                params.put("ip", bean.getUserIP());
                params.put("mobile",bean.getMobile());
                _log.info("页面充值异步第三方返回参数："+JSONObject.toJSONString(params));
                CommonSoaUtils.noRetPostThree(request.getParameter("callback").replace("*-*-*", "#"), params);
                return result;
            }
        }
    }
    
    // 拼装返回值
    private Map<String, String> setResult(BaseResultBean resultBean, BankCallResult result, String status, String msg,
        String acqRes, boolean success) {
        Map<String, String> params = new HashMap<String, String>();
        resultBean.setStatus(status);
        resultBean.setStatusDesc(msg);
        resultBean.setStatusForResponse(status);
        params.put("status", status);
        params.put("statusDesc", msg);
        params.put("chkValue", resultBean.getChkValue());
        params.put("acqRes", acqRes);
        result.setMessage(msg);
        result.setStatus(success);
        return params;
    }
    
    private ModelAndView getErrorMV(UserDirectRechargeRequestBean userOpenAccountRequestBean, ModelAndView modelAndView,
        String status, String des) {
        OpenAccountPageResultBean repwdResult = new OpenAccountPageResultBean();
        BaseResultBean resultBean = new BaseResultBean();
        resultBean.setStatusForResponse(status);
        repwdResult.setCallBackAction(userOpenAccountRequestBean.getRetUrl());
        repwdResult.set("chkValue", resultBean.getChkValue());
        repwdResult.set("status", resultBean.getStatus());
        repwdResult.setAcqRes(userOpenAccountRequestBean.getAcqRes());
        modelAndView.addObject("callBackForm", repwdResult);
        return modelAndView;
    }
    
    // 检查参数是否为空
    private boolean checkIsNull(UserDirectRechargeRequestBean requestBean) {
        if (Validator.isNull(requestBean.getInstCode())) {
            return true;
        }
        // 手机号
        if (Validator.isNull(requestBean.getMobile())) {
            return true;
        }
        if (Validator.isNull(requestBean.getRetUrl())) {
            return true;
        }
        if (Validator.isNull(requestBean.getBgRetUrl())) {
            return true;
        }
        // 身份证号
        if (Validator.isNull(requestBean.getIdNo())) {
            return true;
        }
        // 银行卡号
        if (Validator.isNull(requestBean.getCardNo())) {
            return true;
        }
        // 渠道
        if (Validator.isNull(requestBean.getChannel())) {
            return true;
        }
        // 充值金额
        if (Validator.isNull(requestBean.getTxAmount())) {
            return true;
        }
        if (Validator.isNull(requestBean.getName())) {
            return true;
        }
        // 充值金额校验
        if (!requestBean.getTxAmount().matches("-?[0-9]+.*[0-9]*")) {
            _log.info("充值金额格式错误,充值金额:[" + requestBean.getTxAmount() + "]");
            return true;
        }
        
        //判断小数位数
        if(requestBean.getTxAmount().indexOf(".")>=0){
            String l = requestBean.getTxAmount().substring(requestBean.getTxAmount().indexOf(".")+1,requestBean.getTxAmount().length());
            if(l.length()>2){
                _log.info("充值金额不能大于两位小数,充值金额:[" + requestBean.getTxAmount() + "]");
                return true;
            }
        }
        return false;
    }
}
