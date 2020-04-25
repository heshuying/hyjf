package com.hyjf.api.aems.withdraw;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.aems.util.AemsErrorCodeConstant;
import com.hyjf.api.server.user.repay.RepayResultBean;
import com.hyjf.api.server.util.ErrorCodeConstant;
import com.hyjf.api.server.withdraw.UserWithdrawService;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.user.auto.AutoService;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.thirdparty.UserWithdrawRecordCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.vip.apply.ApplyDefine;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *外部服务接口:用户提现
 * @author Zha Daojian
 * @date 2018/9/26 15:39
 * @param
 * @return
 **/
@Controller
@RequestMapping(AemsUserWithdrawDefine.REQUEST_MAPPING)
public class AemsUserWithdrawServer extends BaseController {

    Logger _log = LoggerFactory.getLogger(AemsUserWithdrawServer.class);

    @Autowired
    private UserWithdrawService userWithdrawService;
    
    @Autowired
	private AutoService autoService;

    @RequestMapping(value = AemsUserWithdrawDefine.WITHDRAW_ACTION)
    public ModelAndView withdraw(@RequestBody AemsUserWithdrawRequestBean userWithdrawRequestBean, HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(AemsUserWithdrawServer.class.getName(), AemsUserWithdrawDefine.WITHDRAW_ACTION);
        ModelAndView modelAndView = new ModelAndView(AemsUserWithdrawDefine.CALLBACK_VIEW);
        _log.info("外部服务接口用户提现");
        _log.info("用户提现第三方请求参数:" + JSONObject.toJSONString(userWithdrawRequestBean));
        AemsUserWithdrawResultBean userWithdrawResultBean = new AemsUserWithdrawResultBean();
        try {
            // 用户电子账户号
            String accountId = userWithdrawRequestBean.getAccountId();
            // 提现金额
            String account = userWithdrawRequestBean.getAccount();
            // 提现银行卡
            String cardNo = userWithdrawRequestBean.getCardNo();
            // 渠道
            String channel = userWithdrawRequestBean.getChannel();
            // 银联行号
            String payAllianceCode = userWithdrawRequestBean.getPayAllianceCode();
            // 同步回调URL
            String retUrl = userWithdrawRequestBean.getRetUrl();
            // 异步回调URL
            String bgRetUrl = userWithdrawRequestBean.getBgRetUrl();
            // 机构编码
            String instCode = userWithdrawRequestBean.getInstCode();
            // 忘记密码URL
            String forgotPwdUrl = userWithdrawRequestBean.getForgotPwdUrl();
            // 银行卡号
            if (Validator.isNull(cardNo)) {
                // 异步回调URL
                if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    // 发送第三方异步回调
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", userWithdrawRequestBean.getAccountId());
                    params.put("statusDesc", "银行卡号不能为空");
                    params.put("status", resultBean.getStatus());
                    params.put("chkValue", resultBean.getChkValue());
                    CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), params);
                }
                // 同步回调URL不为空
                if (Validator.isNotNull(userWithdrawRequestBean.getRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    // 同步回调
                    modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                    userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                    userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                    userWithdrawResultBean.set("statusDesc", "银行卡号不能为空");
                    userWithdrawResultBean.set("status", resultBean.getStatus());
                    userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                    modelAndView.addObject("callBackForm", userWithdrawResultBean);
                    return modelAndView;
                }
                modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                modelAndView.addObject("message", "银行卡号不能为空");
                return modelAndView;
            }
            // 银行电子账户号
            if (Validator.isNull(accountId)) {
                // 异步回调URL
                if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    // 请求第三方异步回调
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", userWithdrawRequestBean.getAccountId());
                    params.put("statusDesc", "电子账户号不能为空");
                    params.put("status", resultBean.getStatus());
                    params.put("chkValue", resultBean.getChkValue());
                    CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), params);
                }
                // 同步回调URL不为空
                if (Validator.isNotNull(userWithdrawRequestBean.getRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    // 同步回调
                    modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                    userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                    userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                    userWithdrawResultBean.set("statusDesc", "电子账户号不能为空");
                    userWithdrawResultBean.set("status", resultBean.getStatus());
                    userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                    modelAndView.addObject("callBackForm", userWithdrawResultBean);
                    return modelAndView;
                }
                modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                modelAndView.addObject("message", "电子账户号不能为空");
                return modelAndView;
            }
            // 渠道
            if (Validator.isNull(channel)) {
                // 异步回调URL
                if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", userWithdrawRequestBean.getAccountId());
                    params.put("statusDesc", "渠道不能为空");
                    params.put("status", resultBean.getStatus());
                    params.put("chkValue", resultBean.getChkValue());
                    CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), params);
                }
                // 同步回调URL不为空
                if (Validator.isNotNull(userWithdrawRequestBean.getRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    // 同步回调
                    modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                    userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                    userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                    userWithdrawResultBean.set("statusDesc", "渠道不能为空");
                    userWithdrawResultBean.set("status", resultBean.getStatus());
                    userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                    modelAndView.addObject("callBackForm", userWithdrawResultBean);
                    return modelAndView;
                }
                modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                modelAndView.addObject("message", "渠道不能为空");
                return modelAndView;
            }
            // 充值金额
            if (Validator.isNull(account)) {
                // 异步回调URL
                if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", userWithdrawRequestBean.getAccountId());
                    params.put("statusDesc", "提现金额不能为空");
                    params.put("status", resultBean.getStatus());
                    params.put("chkValue", resultBean.getChkValue());
                    CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), params);
                }
                // 同步回调URL不为空
                if (Validator.isNotNull(userWithdrawRequestBean.getRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    // 同步回调
                    modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                    userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                    userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                    userWithdrawResultBean.set("statusDesc", "提现金额不能为空");
                    userWithdrawResultBean.set("status", resultBean.getStatus());
                    userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                    modelAndView.addObject("callBackForm", userWithdrawResultBean);
                    return modelAndView;
                }
                modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                modelAndView.addObject("message", "提现金额不能为空");
                return modelAndView;
            }

            // 同步URL
            if (Validator.isNull(retUrl)) {
                // 异步回调URL不为空
                if (Validator.isNotNull(bgRetUrl)) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", userWithdrawRequestBean.getAccountId());
                    params.put("statusDesc", "同步回调URL不能为空");
                    params.put("status", resultBean.getStatus());
                    params.put("chkValue", resultBean.getChkValue());
                    CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), params);
                }
                modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                modelAndView.addObject("message", "请求参数异常");
                return modelAndView;
            }
            // 异步回调URL
            if (Validator.isNull(bgRetUrl)) {
                // 同步地址不为空
                if (Validator.isNotNull(retUrl)) {
                    _log.info("异步回调URL为空");
                    BaseResultBean resultBean = new RepayResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    // 同步回调
                    modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                    userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                    userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                    userWithdrawResultBean.set("statusDesc", "请求参数异常");
                    userWithdrawResultBean.set("status", resultBean.getStatus());
                    userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                    modelAndView.addObject("callBackForm", userWithdrawResultBean);
                    return modelAndView;
                }
                modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                modelAndView.addObject("message", "请求参数异常");
                return modelAndView;
            }
            // 机构编号
            if (Validator.isNull(instCode)) {
                // 异步回调URL
                if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", userWithdrawRequestBean.getAccountId());
                    params.put("statusDesc", "机构编号不能为空");
                    params.put("status", resultBean.getStatus());
                    params.put("chkValue", resultBean.getChkValue());
                    CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), params);
                }
                // 同步回调URL不为空
                if (Validator.isNotNull(userWithdrawRequestBean.getRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    // 同步回调
                    modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                    userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                    userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                    userWithdrawResultBean.set("statusDesc", "机构编号不能为空");
                    userWithdrawResultBean.set("status", resultBean.getStatus());
                    userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                    modelAndView.addObject("callBackForm", userWithdrawResultBean);
                    return modelAndView;
                }
                modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                modelAndView.addObject("message", "机构编号不能为空");
                return modelAndView;
            }
            // 忘记密码Url
            if (Validator.isNull(forgotPwdUrl)) {
                // 异步回调URL
                if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", userWithdrawRequestBean.getAccountId());
                    params.put("statusDesc", "忘记密码URL不能为空");
                    params.put("status", resultBean.getStatus());
                    params.put("chkValue", resultBean.getChkValue());
                    CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), params);
                }
                // 同步回调URL不为空
                if (Validator.isNotNull(userWithdrawRequestBean.getRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    // 同步回调
                    modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                    userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                    userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                    userWithdrawResultBean.set("statusDesc", "忘记密码URL不能为空");
                    userWithdrawResultBean.set("status", resultBean.getStatus());
                    userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                    modelAndView.addObject("callBackForm", userWithdrawResultBean);
                    return modelAndView;
                }
                modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                modelAndView.addObject("message", "忘记密码URL不能为空");
                return modelAndView;
            }
            // 验签
           if (!this.AEMSVerifyRequestSign(userWithdrawRequestBean, AemsUserWithdrawDefine.REQUEST_MAPPING+AemsUserWithdrawDefine.WITHDRAW_ACTION)) {
                _log.info("-------------------验签失败！--------------------");
                BaseResultBean resultBean = new BaseResultBean();
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000002);
                userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                userWithdrawResultBean.set("acqRes", userWithdrawRequestBean.getAcqRes());
                modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                // 设置交易密码
                modelAndView.addObject("statusDesc", "验签失败！");
                userWithdrawResultBean.set("status", resultBean.getStatus());
                userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                modelAndView.addObject("callBackForm", userWithdrawResultBean);
                //  返回第三方异步回调
                if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", userWithdrawRequestBean.getAccountId());
                    params.put("status", resultBean.getStatus());
                    params.put("statusDesc", "验签失败！");
                    params.put("acqRes", userWithdrawRequestBean.getAcqRes());
                    params.put("chkValue", resultBean.getChkValue());
                    CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), params);
                }
                return modelAndView;
            }

            // 充值金额校验
            if (!account.matches("-?[0-9]+.*[0-9]*")) {
                _log.info("提现金额格式错误,充值金额:[" + account + "]");
                // 异步回调URL
                if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", userWithdrawRequestBean.getAccountId());
                    params.put("statusDesc", "提现金额格式错误");
                    params.put("status", resultBean.getStatus());
                    params.put("chkValue", resultBean.getChkValue());
                    CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), params);
                }
                // 同步回调URL不为空
                if (Validator.isNotNull(userWithdrawRequestBean.getRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    // 同步回调
                    modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                    userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                    userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                    userWithdrawResultBean.set("statusDesc", "提现金额格式错误");
                    userWithdrawResultBean.set("status", resultBean.getStatus());
                    userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                    modelAndView.addObject("callBackForm", userWithdrawResultBean);
                    return modelAndView;
                }
                modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                modelAndView.addObject("message", "提现金额格式错误");
                return modelAndView;
            }

            // 根据机构编号检索机构信息
           HjhInstConfig instConfig = this.userWithdrawService.selectInstConfigByInstCode(instCode);
            // 机构编号
            if (instConfig == null) {
                _log.info("获取机构信息为空,机构编号:[" + instCode + "].");
                // 异步回调URL
                if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000004);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", userWithdrawRequestBean.getAccountId());
                    params.put("statusDesc", "机构编号错误");
                    params.put("status", resultBean.getStatus());
                    params.put("chkValue", resultBean.getChkValue());
                    CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), params);
                }
                // 同步回调URL不为空
                if (Validator.isNotNull(userWithdrawRequestBean.getRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000004);
                    // 同步回调
                    modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                    userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                    userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                    userWithdrawResultBean.set("statusDesc", "机构编号错误");
                    userWithdrawResultBean.set("status", resultBean.getStatus());
                    userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                    modelAndView.addObject("callBackForm", userWithdrawResultBean);
                    return modelAndView;
                }
                modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                modelAndView.addObject("message", "机构编号错误");
                return modelAndView;
            }
            // 大额提现判断银行联行号
            if ((new BigDecimal(account).compareTo(new BigDecimal(50000)) >= 0) && StringUtils.isBlank(payAllianceCode)) {
                _log.info("大额提现时,银行联行号不能为空");
                // 异步回调URL
                if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_NC000012);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", userWithdrawRequestBean.getAccountId());
                    params.put("statusDesc", "大额提现时,银行联行号不能为空");
                    params.put("status", resultBean.getStatus());
                    params.put("chkValue", resultBean.getChkValue());
                    CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), params);
                }
                // 同步回调URL不为空
                if (Validator.isNotNull(userWithdrawRequestBean.getRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_NC000012);
                    // 同步回调
                    modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                    userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                    userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                    userWithdrawResultBean.set("statusDesc", "大额提现时,银行联行号不能为空");
                    userWithdrawResultBean.set("status", resultBean.getStatus());
                    userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                    modelAndView.addObject("callBackForm", userWithdrawResultBean);
                    return modelAndView;
                }
                modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                modelAndView.addObject("message", "大额提现时,银行联行号不能为空");
                return modelAndView;
            }
            // 根据电子账户号查询用户ID
            BankOpenAccount bankOpenAccount = this.userWithdrawService.selectBankOpenAccountByAccountId(accountId);
            if (bankOpenAccount == null) {
                _log.info("查询用户开户信息失败,用户电子账户号:[" + accountId + "]");
                // 异步回调URL
                if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", userWithdrawRequestBean.getAccountId());
                    params.put("statusDesc", "提现失败");
                    params.put("status", resultBean.getStatus());
                    params.put("chkValue", resultBean.getChkValue());
                    CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), params);
                }
                // 同步回调URL不为空
                if (Validator.isNotNull(userWithdrawRequestBean.getRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    // 同步回调
                    modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                    userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                    userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                    userWithdrawResultBean.set("statusDesc", "提现失败");
                    userWithdrawResultBean.set("status", resultBean.getStatus());
                    userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                    modelAndView.addObject("callBackForm", userWithdrawResultBean);
                    return modelAndView;
                }
                modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                modelAndView.addObject("message", "提现失败");
                return modelAndView;
            }
            // 用户ID
            Integer userId = bankOpenAccount.getUserId();
            // 根据用户ID查询用户信息
            Users user = this.userWithdrawService.getUsersByUserId(userId);
            if (user == null) {
                _log.info("根据用户ID查询用户信息失败,用户电子账户号:[" + accountId + "],用户ID:[" + userId + "].");
                // 异步回调URL
                if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", userWithdrawRequestBean.getAccountId());
                    params.put("statusDesc", "提现失败");
                    params.put("status", resultBean.getStatus());
                    params.put("chkValue", resultBean.getChkValue());
                    CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), params);
                }
                // 同步回调URL不为空
                if (Validator.isNotNull(userWithdrawRequestBean.getRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    // 同步回调
                    modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                    userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                    userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                    userWithdrawResultBean.set("statusDesc", "提现失败");
                    userWithdrawResultBean.set("status", resultBean.getStatus());
                    userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                    modelAndView.addObject("callBackForm", userWithdrawResultBean);
                    return modelAndView;
                }
                modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                modelAndView.addObject("message", "提现失败");
                return modelAndView;
            }
            
            // 检查是否设置交易密码
            Integer passwordFlag = user.getIsSetPassword();
            if (passwordFlag != 1) {// 未设置交易密码
                // 异步回调URL
                if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_TP000002);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", userWithdrawRequestBean.getAccountId());
                    params.put("statusDesc", "未设置交易密码！");
                    params.put("status", resultBean.getStatus());
                    params.put("chkValue", resultBean.getChkValue());
                    CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), params);
                }
                // 同步回调URL不为空
                if (Validator.isNotNull(userWithdrawRequestBean.getRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_TP000002);
                    // 同步回调
                    modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                    userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                    userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                    userWithdrawResultBean.set("statusDesc", "未设置交易密码！");
                    userWithdrawResultBean.set("status", resultBean.getStatus());
                    userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                    modelAndView.addObject("callBackForm", userWithdrawResultBean);
                    return modelAndView;
                }
                modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                modelAndView.addObject("message", "提现失败");
                return modelAndView;
            }
            
            // 服务费授权状态和开关
            if (!autoService.checkPaymentAuthStatus(bankOpenAccount.getUserId())) {
                _log.info("用户未进行缴费授权,用户电子账户号:[" + accountId + "],用户ID:[" + userId + "].");
                // 异步回调URL
                if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000011);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", userWithdrawRequestBean.getAccountId());
                    params.put("statusDesc", "用户未进行缴费授权");
                    params.put("status", resultBean.getStatus());
                    params.put("chkValue", resultBean.getChkValue());
                    CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), params);
                }
                // 同步回调URL不为空
                if (Validator.isNotNull(userWithdrawRequestBean.getRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000011);
                    // 同步回调
                    modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                    userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                    userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                    userWithdrawResultBean.set("statusDesc", "用户未进行缴费授权");
                    userWithdrawResultBean.set("status", resultBean.getStatus());
                    userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                    modelAndView.addObject("callBackForm", userWithdrawResultBean);
                    return modelAndView;
                }
                modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                modelAndView.addObject("message", "提现失败");
                return modelAndView;
            }
            // 根据用户ID查询用户详情
            UsersInfo userInfo = this.userWithdrawService.getUsersInfoByUserId(userId);
            if (userInfo == null) {
                _log.info("根据用户ID查询用户详情失败,用户电子账户号:[" + accountId + ",用户ID:[" + userId + "]");
                // 异步回调URL
                if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", userWithdrawRequestBean.getAccountId());
                    params.put("statusDesc", "提现失败");
                    params.put("status", resultBean.getStatus());
                    params.put("chkValue", resultBean.getChkValue());
                    CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), params);
                }
                // 同步回调URL不为空
                if (Validator.isNotNull(userWithdrawRequestBean.getRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    // 同步回调
                    modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                    userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                    userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                    userWithdrawResultBean.set("statusDesc", "提现失败");
                    userWithdrawResultBean.set("status", resultBean.getStatus());
                    userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                    modelAndView.addObject("callBackForm", userWithdrawResultBean);
                    return modelAndView;
                }
                modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                modelAndView.addObject("message", "提现失败");
                return modelAndView;
            }
            // 身份证号
            String idNo = userInfo.getIdcard();
            // 姓名
            String trueName = userInfo.getTruename();
            // 用户手机号
            String mobile = user.getMobile();
            // 提现用户名
            String userName = user.getUsername();
            // 根据用户ID查询用户平台银行卡信息
            BankCard bankCard = this.userWithdrawService.getBankCardByUserId(userId);
            if (bankCard == null) {
                _log.info("根据用户ID查询用户银行卡信息失败,用户电子账户号:[" + accountId + "],用户ID:[" + userId + "].");
                // 异步回调URL
                if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", userWithdrawRequestBean.getAccountId());
                    params.put("statusDesc", "提现失败");
                    params.put("status", resultBean.getStatus());
                    params.put("chkValue", resultBean.getChkValue());
                    CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), params);
                }
                // 同步回调URL不为空
                if (Validator.isNotNull(userWithdrawRequestBean.getRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    // 同步回调
                    modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                    userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                    userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                    userWithdrawResultBean.set("statusDesc", "提现失败");
                    userWithdrawResultBean.set("status", resultBean.getStatus());
                    userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                    modelAndView.addObject("callBackForm", userWithdrawResultBean);
                    return modelAndView;
                }
                modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                modelAndView.addObject("message", "提现失败");
                return modelAndView;
            }
            // 用户汇盈平台的银行卡卡号
            String localCardNo = bankCard.getCardNo() == null ? "" : bankCard.getCardNo();
            // 如果两边银行卡号不一致
            if (!cardNo.equals(localCardNo)) {
                _log.info("用户银行卡信息不一致,用户电子账户号:[" + accountId + "],请求银行卡号:[" + cardNo + "],平台保存的银行卡号:[" + localCardNo + "].");
                // 异步回调URL
                if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", userWithdrawRequestBean.getAccountId());
                    params.put("statusDesc", "提现失败");
                    params.put("status", resultBean.getStatus());
                    params.put("chkValue", resultBean.getChkValue());
                    CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), params);
                }
                // 同步回调URL不为空
                if (Validator.isNotNull(userWithdrawRequestBean.getRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    // 同步回调
                    modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                    userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                    userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                    userWithdrawResultBean.set("statusDesc", "提现失败");
                    userWithdrawResultBean.set("status", resultBean.getStatus());
                    userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                    modelAndView.addObject("callBackForm", userWithdrawResultBean);
                    return modelAndView;
                }
                modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                modelAndView.addObject("message", "提现失败");
                return modelAndView;
            }
            
         // 根据用户ID查询用户账户信息
            Account hyAccount = this.userWithdrawService.getAccount(userId);
            // 取得账户为空
            if (hyAccount == null) {
                _log.info("根据用户ID查询用户账户信息失败,用户ID:[" + userId + "],电子账户号:[" + accountId + "].");
                // 异步回调URL
                if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_NC000009);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", userWithdrawRequestBean.getAccountId());
                    params.put("statusDesc", "查询用户账户信息失败");
                    params.put("status", resultBean.getStatus());
                    params.put("chkValue", resultBean.getChkValue());
                    CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), params);
                }
                // 同步回调URL不为空
                if (Validator.isNotNull(userWithdrawRequestBean.getRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_NC000009);
                    // 同步回调
                    modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                    userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                    userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                    userWithdrawResultBean.set("statusDesc", "查询用户账户信息失败");
                    userWithdrawResultBean.set("status", resultBean.getStatus());
                    userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                    modelAndView.addObject("callBackForm", userWithdrawResultBean);
                    return modelAndView;
                }
                modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                modelAndView.addObject("message", "查询用户账户信息失败");
                return modelAndView;
            }
            // 提现金额大于汇盈账户余额
            if (new BigDecimal(account).compareTo(hyAccount.getBankBalance()) > 0) {
                _log.info("提现金额大于汇盈账户可用余额,用户ID:[" + userId + "],电子账户号:[" + accountId + "],提现金额:[" + account + "],账户可用余额:[" + hyAccount.getBankBalance() + "].");
                // 异步回调URL
                if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_NC000010);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", userWithdrawRequestBean.getAccountId());
                    params.put("statusDesc", "用户账户余额不足");
                    params.put("status", resultBean.getStatus());
                    params.put("chkValue", resultBean.getChkValue());
                    CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), params);
                }
                // 同步回调URL不为空
                if (Validator.isNotNull(userWithdrawRequestBean.getRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_NC000010);
                    // 同步回调
                    modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                    userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                    userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                    userWithdrawResultBean.set("statusDesc", "用户账户余额不足");
                    userWithdrawResultBean.set("status", resultBean.getStatus());
                    userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                    modelAndView.addObject("callBackForm", userWithdrawResultBean);
                    return modelAndView;
                }
                modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                modelAndView.addObject("message", "用户账户余额不足");
                return modelAndView;
            }
            //可用金额
            BigDecimal total2 = hyAccount.getBankBalance();
            //可提现金额
            BigDecimal availableCash = null;
            // 获取用户角色
            UsersInfo usersInfo = userWithdrawService.getUsersInfoByUserId(userId);
            if (usersInfo != null && usersInfo.getRoleId() == 1) {
                int tenderRecord = userWithdrawService.getTenderRecord(userId);
                if (tenderRecord <= 0) {
                    List<AccountRecharge> accountRecharges = userWithdrawService.getRechargeMoney(userId);
                    // 当天充值，提现金额为当前余额减去当日充值金额
                    if (!CollectionUtils.isEmpty(accountRecharges)) {
                        for (AccountRecharge accountRecharge : accountRecharges) {
                            total2 = total2.subtract(accountRecharge.getBalance());
                            availableCash = total2;
                        }
                        if (org.apache.commons.lang.StringUtils.isNotBlank(account) && new BigDecimal(account).compareTo(availableCash) > 0) {
                            // 异步回调URL
                            if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                                BaseResultBean resultBean = new BaseResultBean();
                                resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_NC000011);
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("accountId", userWithdrawRequestBean.getAccountId());
                                params.put("statusDesc", "当天充值资金当天无法提现，请调整取现金额。");
                                params.put("status", resultBean.getStatus());
                                params.put("chkValue", resultBean.getChkValue());
                                CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), params);
                            }
                            // 同步回调URL不为空
                            if (Validator.isNotNull(userWithdrawRequestBean.getRetUrl())) {
                                BaseResultBean resultBean = new BaseResultBean();
                                resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_NC000011);
                                // 同步回调
                                modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                                userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                                userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                                userWithdrawResultBean.set("statusDesc", "当天充值资金当天无法提现，请调整取现金额。");
                                userWithdrawResultBean.set("status", resultBean.getStatus());
                                userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                                modelAndView.addObject("callBackForm", userWithdrawResultBean);
                                return modelAndView;
                            }

                            modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                            modelAndView.addObject("message", "当天充值资金当天无法提现，请调整取现金额。");
                            return modelAndView;
                        }
                    }
                }
            }

            // 取得手续费 默认1
            // 11-23  改为从数据库中读取配置的手续费
            String fee = userWithdrawService.getUserFee(instCode);
            //String fee = this.userWithdrawService.getWithdrawFee(userId, cardNo, new BigDecimal(account));
            // 实际取现金额
            // 去掉一块钱手续费
           
            
            if (!(new BigDecimal(account).compareTo(new BigDecimal(fee)) > 0)) {

                // 异步回调URL
                if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", userWithdrawRequestBean.getAccountId());
                    params.put("statusDesc", "提现金额不能小于手续费");
                    params.put("status", resultBean.getStatus());
                    params.put("chkValue", resultBean.getChkValue());
                    CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), params);
                }
                // 同步回调URL不为空
                if (Validator.isNotNull(userWithdrawRequestBean.getRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    // 同步回调
                    modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                    userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                    userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                    userWithdrawResultBean.set("statusDesc", "提现金额不能小于手续费");
                    userWithdrawResultBean.set("status", resultBean.getStatus());
                    userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                    modelAndView.addObject("callBackForm", userWithdrawResultBean);
                    return modelAndView;
                }

                modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                modelAndView.addObject("message", "提现金额不能小于手续费");
                return modelAndView;
            
            }
            
            account = new BigDecimal(account).subtract(new BigDecimal(Validator.isNull(fee) ? "0" : fee)).toString();
            // 调用江西银行提现接口
            // 调用汇付接口(提现)
            String bankRetUrl = PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) + AemsUserWithdrawDefine.REQUEST_MAPPING + AemsUserWithdrawDefine.RETURN_MAPPING + ".do?callback=" + retUrl.replace("#", "*-*-*");
            // 支付工程路径
            String bankBgRetUrl = PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) + AemsUserWithdrawDefine.REQUEST_MAPPING + AemsUserWithdrawDefine.CALLBACK_MAPPING + ".do?callback=" + bgRetUrl.replace("#", "*-*-*");// 支付工程路径
            // 路由代码
            String routeCode = "";
            // 调用汇付接口(4.2.2 用户绑卡接口)
            BankCallBean bean = new BankCallBean();
            bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
            bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间
            bean.setLogUserId(String.valueOf(userId));
            bean.setLogRemark("第三方服务接口:用户提现");
            bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_MOBILE_WITHDRAW);
            bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
            bean.setTxCode(BankCallMethodConstant.TXCODE_WITHDRAW);
            bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
            bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
            bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
            bean.setChannel(channel);// 交易渠道
            bean.setAccountId(accountId);// 存管平台分配的账号
            bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);// 证件类型01身份证
            bean.setIdNo(idNo);// 证件号
            bean.setName(trueName);// 姓名
            bean.setMobile(mobile);// 手机号
            bean.setCardNo(bankCard.getCardNo());// 银行卡号
            bean.setTxAmount(CustomUtil.formatAmount(account));
            bean.setTxFee(fee);

            // 扣除手续费
            if ((new BigDecimal(account).compareTo(new BigDecimal(50000)) > 0) && StringUtils.isNotBlank(payAllianceCode)) {
                routeCode = "2";// 路由代码
                bean.setCardBankCnaps(payAllianceCode);// 绑定银行联行号
            }
            if (routeCode.equals("2")) {
                bean.setRouteCode(routeCode);
                LogAcqResBean logAcq = new LogAcqResBean();
                logAcq.setPayAllianceCode(payAllianceCode);
                bean.setLogAcqResBean(logAcq);
            }
            // 企业用户提现
            if (user.getUserType() == 1) { // 企业用户 传组织机构代码
                CorpOpenAccountRecord record = userWithdrawService.getCorpOpenAccountRecord(userId);
                bean.setIdType(record.getCardType() != null ? String.valueOf(record.getCardType()) : BankCallConstant.ID_TYPE_COMCODE);// 证件类型
                bean.setIdNo(record.getBusiCode());
                bean.setName(record.getBusiName());
                bean.setRouteCode("2");
                bean.setCardBankCnaps(StringUtils.isEmpty(payAllianceCode) ? bankCard.getPayAllianceCode() : payAllianceCode);
            }
            // TODO忘记密码URL
            //bean.setForgotPwdUrl(CustomConstants.FORGET_PASSWORD_URL);
            bean.setForgotPwdUrl(userWithdrawRequestBean.getForgotPwdUrl());
            bean.setRetUrl(bankRetUrl);// 商户前台台应答地址(必须)
            bean.setNotifyUrl(bankBgRetUrl); // 商户后台应答地址(必须)
            _log.info("提现同步回调URL:[" + bean.getRetUrl() + "],异步回调URL:[" + bean.getNotifyUrl() + "].");
            // 插值用参数
            Map<String, String> params = new HashMap<String, String>();
            params.put("userId", String.valueOf(userId));
            params.put("userName", userName);
            params.put("ip", CustomUtil.getIpAddr(request));
            params.put("cardNo", cardNo);
            params.put("fee", CustomUtil.formatAmount(fee));
            // 提现平台
            params.put("client", userWithdrawRequestBean.getPlatform()==null?"0":userWithdrawRequestBean.getPlatform());
            // 用户提现前处理
            int cnt = this.userWithdrawService.updateBeforeCash(bean, params);
            if (cnt > 0) {
                // 跳转到调用江西银行
                modelAndView = BankCallUtils.callApi(bean);
            } else {
                _log.info("提现前,插入提现记录失败");
                // 异步回调URL
                if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    Map<String, String> retParams = new HashMap<String, String>();
                    retParams.put("accountId", userWithdrawRequestBean.getAccountId());
                    retParams.put("statusDesc", "提现异常");
                    retParams.put("status", resultBean.getStatus());
                    retParams.put("chkValue", resultBean.getChkValue());
                    CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), retParams);
                }
                // 同步回调URL不为空
                if (Validator.isNotNull(userWithdrawRequestBean.getRetUrl())) {
                    BaseResultBean resultBean = new BaseResultBean();
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                    // 同步回调
                    modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                    userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                    userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                    userWithdrawResultBean.set("statusDesc", "提现异常");
                    userWithdrawResultBean.set("status", resultBean.getStatus());
                    userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                    modelAndView.addObject("callBackForm", userWithdrawResultBean);
                    return modelAndView;
                }
                modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
                modelAndView.addObject("message", "提现异常");
                return modelAndView;
            }
        } catch (Exception e) {
            _log.info("提现发生异常,异常信息:[" + e.getMessage() + "].");
            // 异步回调URL
            if (Validator.isNotNull(userWithdrawRequestBean.getBgRetUrl())) {
                BaseResultBean resultBean = new BaseResultBean();
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                Map<String, String> retParams = new HashMap<String, String>();
                retParams.put("accountId", userWithdrawRequestBean.getAccountId());
                retParams.put("statusDesc", "提现异常");
                retParams.put("status", resultBean.getStatus());
                retParams.put("chkValue", resultBean.getChkValue());
                CommonSoaUtils.noRetPostThree(userWithdrawRequestBean.getBgRetUrl(), retParams);
            }
            // 同步回调URL不为空
            if (Validator.isNotNull(userWithdrawRequestBean.getRetUrl())) {
                BaseResultBean resultBean = new BaseResultBean();
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                // 同步回调
                modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                userWithdrawResultBean.setCallBackAction(userWithdrawRequestBean.getRetUrl());
                userWithdrawResultBean.set("accountId", userWithdrawRequestBean.getAccountId());
                userWithdrawResultBean.set("statusDesc", "提现异常");
                userWithdrawResultBean.set("status", resultBean.getStatus());
                userWithdrawResultBean.set("chkValue", resultBean.getChkValue());
                modelAndView.addObject("callBackForm", userWithdrawResultBean);
                return modelAndView;
            }
            modelAndView = new ModelAndView(AemsUserWithdrawDefine.WITHDRAW_ERROR_PATH);
            modelAndView.addObject("message", "提现异常");
            return modelAndView;
        }
        return modelAndView;
    }

    /**
     * 用户提现后处理
     *
     * @param request
     * @param bean
     * @return
     */
    @RequestMapping(AemsUserWithdrawDefine.RETURN_MAPPING)
    public ModelAndView cashReturn(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
        LogUtil.startLog(AemsUserWithdrawServer.class.getName(), AemsUserWithdrawDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
        _log.info("用户提现后,同步处理");
        ModelAndView modelAndView = new ModelAndView(AemsUserWithdrawDefine.CALLBACK_VIEW);
        bean.convert();
        String logOrderId = bean.getLogOrderId() == null ? "" : bean.getLogOrderId();
        // 提现订单号
        _log.info("提现订单号:[" + logOrderId + "].");
        AemsUserWithdrawResultBean resultBean = new AemsUserWithdrawResultBean();
        resultBean.setCallBackAction(request.getParameter("callback").replace("*-*-*", "#"));
        Accountwithdraw accountwithdraw = userWithdrawService.getAccountWithdrawByOrdId(logOrderId);
        // 提现成功
        if (accountwithdraw != null) {
            _log.info("提现成功,提现订单号:[" + logOrderId + "]");
            resultBean.setAmt(String.valueOf(accountwithdraw.getTotal()));// 交易金额
            resultBean.setArrivalAmount(String.valueOf(accountwithdraw.getCredited()));// 到账金额
            resultBean.setFee(accountwithdraw.getFee());// 提现手续费
            modelAndView.addObject("statusDesc", "提现成功");
            BaseResultBean baseResultBean = new BaseResultBean();
            baseResultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
            resultBean.set("chkValue", baseResultBean.getChkValue());
            resultBean.set("status", baseResultBean.getStatus());
            resultBean.set("amt", String.valueOf(accountwithdraw.getTotal()));// 交易金额
            resultBean.set("arrivalAmount", String.valueOf(accountwithdraw.getCredited()));// 到账金额
            resultBean.set("fee", accountwithdraw.getFee());// 提现手续费
            resultBean.set("orderId", accountwithdraw.getNid());// 提现订单号
        } else {
            _log.info("银行处理中,请稍后查询交易明细");
            modelAndView.addObject("statusDesc", "银行处理中,请稍后查询交易明细");
            BaseResultBean baseResultBean = new BaseResultBean();
            baseResultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000005);
            resultBean.set("chkValue", baseResultBean.getChkValue());
            resultBean.set("status", baseResultBean.getStatus());
//            resultBean.setStatus("2");
//            resultBean.setStatusDesc("银行处理中,请稍后查询交易明细");
        }
        modelAndView.addObject("callBackForm", resultBean);
        return modelAndView;
    }

    /**
     * 用户提现异步回调处理
     *
     * @param request
     * @param bean
     * @return
     */
    @ResponseBody
    @RequestMapping(AemsUserWithdrawDefine.CALLBACK_MAPPING)
    public BankCallResult withdrawBgReturn(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
        AemsUserWithdrawResultBean resultBean = new AemsUserWithdrawResultBean();
        String logOrderId = bean.getLogOrderId() == null ? "" : bean.getLogOrderId();
        bean.convert();
        BankCallResult result = new BankCallResult();
        try {
            String status ="";
            String message = "";
            Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
            resultBean.setCallBackAction(request.getParameter("callback"));
            // 插值用参数
            Map<String, String> params = new HashMap<String, String>();
            params.put("userId", String.valueOf(userId));
            params.put("ip", CustomUtil.getIpAddr(request));
            // 执行提现后处理
            this.userWithdrawService.handlerAfterCash(bean, params);
            Accountwithdraw accountwithdraw = userWithdrawService.getAccountWithdrawByOrdId(logOrderId);
            // 提现成功
            if (accountwithdraw != null) {
                _log.info("提现成功,提现订单号:[" + logOrderId + "]");
                status = ErrorCodeConstant.SUCCESS;
                resultBean.setAmt(String.valueOf(accountwithdraw.getTotal()));// 交易金额
                resultBean.setArrivalAmount(String.valueOf(accountwithdraw.getCredited()));// 到账金额
                resultBean.setFee(accountwithdraw.getFee());// 提现手续费
                message="提现成功";
            } else {
                _log.info("银行处理中,请稍后查询交易明细");
//                resultBean.setStatus("2");
//                resultBean.setStatusDesc("银行处理中,请稍后查询交易明细");
                message = "银行处理中,请稍后查询交易明细";
                status = ErrorCodeConstant.STATUS_CE000005;
            }
            _log.info("用户提现异步回调end");

            params.put("amt",String.valueOf(accountwithdraw.getTotal()));
            params.put("arrivalAmount",String.valueOf(accountwithdraw.getCredited()));
            params.put("fee",accountwithdraw.getFee());

            params.put("accountId",bean.getAccountId());
            params.put("orderId",logOrderId);
            params.put("status", status);
            params.put("statusDesc",message);
            BaseResultBean baseResultBean = new BaseResultBean();
            baseResultBean.setStatusForResponse(status);
            params.put("chkValue", baseResultBean.getChkValue());
            CommonSoaUtils.noRetPostThree(request.getParameter("callback").replace("*-*-*","#"), params);
        } catch (Exception e) {
            _log.info("提现失败,用户提现发生异常,错误信息:[" + e.getMessage() + "]");
//            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
//            resultBean.setStatusDesc("提现失败");
        }
        result.setStatus(true);
        return result;
    }
    
    
    /**
     * 获取用户提现记录
     * @param userWithdrawRequestBean
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AemsUserWithdrawDefine.GET_USER_WITHDRAW_RECORD_ACTION)
    public BaseResultBean getUserWithdrawRecord(@RequestBody AemsUserWithdrawRequestBean userWithdrawRequestBean,
        HttpServletRequest request, HttpServletResponse response){
        _log.info("userWithdrawRequestBean:"+JSONObject.toJSONString(userWithdrawRequestBean));
        AemsUserWithdrawRecordResultBean result=new AemsUserWithdrawRecordResultBean();
        if (Validator.isNull(userWithdrawRequestBean.getAccountId())||
                Validator.isNull(userWithdrawRequestBean.getLimitEnd())||
                Validator.isNull(userWithdrawRequestBean.getLimitStart())||
                Validator.isNull(userWithdrawRequestBean.getInstCode())) {
            
            _log.info("-------------------请求参数非法--------------------");
            result.setStatus(BaseResultBean.STATUS_FAIL);
            result.setStatusDesc("请求参数非法");
            return result;
        }
        
        //验签
        if(!this.AEMSVerifyRequestSign(userWithdrawRequestBean, AemsUserWithdrawDefine.REQUEST_MAPPING+AemsUserWithdrawDefine.GET_USER_WITHDRAW_RECORD_ACTION)){
            _log.info("-------------------验签失败！--------------------");
            result.setStatus(BaseResultBean.STATUS_FAIL);
            result.setStatusDesc("验签失败");
            return result;
        }
        
        //根据账号找出用户ID
        BankOpenAccount bankOpenAccount = userWithdrawService.selectBankOpenAccountByAccountId(userWithdrawRequestBean.getAccountId());
        if(bankOpenAccount == null){
            _log.info("-------------------没有根据电子银行卡找到用户"+userWithdrawRequestBean.getAccountId()+"！--------------------");
            result.setStatus(BaseResultBean.STATUS_FAIL);
            result.setStatusDesc("没有根据电子银行卡找到用户");
            return result;
        }
        Users user = userWithdrawService.getUsers(bankOpenAccount.getUserId());//用户ID
        if(user == null){
            _log.info("---用户不存在汇盈金服账户---");
            result.setStatus(BaseResultBean.STATUS_FAIL);
            result.setStatusDesc("用户不存在汇盈金服账户");
            return result;
        }
        if (user.getBankOpenAccount()==0) {
            _log.info("-------------------没有根据电子银行卡找到用户"+userWithdrawRequestBean.getAccountId()+"！--------------------");
            result.setStatus(BaseResultBean.STATUS_FAIL);
            result.setStatusDesc("没有根据电子银行卡找到用户");
            return result;
        }
        
        if (userWithdrawRequestBean.getInstCode()==null||!userWithdrawRequestBean.getInstCode().equals(user.getInstCode())) {
            _log.info("-------------------该电子账户号非本平台注册不能查询"+userWithdrawRequestBean.getAccountId()+"！--------------------");
            result.setStatus(BaseResultBean.STATUS_FAIL);
            result.setStatusDesc("该电子账户号非本平台注册不能查询");
            return result;
        }
        
        Map<String,Object> param = new HashMap<String, Object>();
        param.put("userId", user.getUserId());
        param.put("status", userWithdrawRequestBean.getStatus());
        param.put("limitStart", userWithdrawRequestBean.getLimitStart());
        param.put("limitEnd", userWithdrawRequestBean.getLimitEnd());
        List<UserWithdrawRecordCustomize> recordList = userWithdrawService.getThirdPartyUserWithdrawRecord(param);
        result.setRecordList(recordList);
        result.setStatus(BaseResultBean.STATUS_SUCCESS);
        result.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        // 返回查询结果
        return result;
    }
    
}
