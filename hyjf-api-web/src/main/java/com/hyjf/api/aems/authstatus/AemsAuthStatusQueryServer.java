package com.hyjf.api.aems.authstatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.api.aems.authstatus.AemsAuthStatusQueryDefine;
import com.hyjf.api.aems.authstatus.AemsAuthStatusQueryRequestBean;
import com.hyjf.api.aems.authstatus.AemsAuthStatusQueryResultBean;
import com.hyjf.api.aems.invest.AemsUserInvestServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.server.util.ErrorCodeConstant;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.user.autoup.AutoPlusService;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
/***
 *  授权状态查询
* @author Zha Daojian
* @date 2018/10/10 11:56
* @param
* @return
**/
@Controller
@RequestMapping(value = AemsAuthStatusQueryDefine.REQUEST_MAPPING)
public class AemsAuthStatusQueryServer extends BaseController {
    
    Logger logger = LoggerFactory.getLogger(AemsAuthStatusQueryServer.class);

    @Autowired
    private AutoPlusService autoPlusService;
    
    /**
     * 
     * 授权状态查询
     * @author sunss
     * @param autoStateQuery
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = AemsAuthStatusQueryDefine.AUTH_STATUS_QUERY_ACTION, produces = "application/json; charset=utf-8")
    public AemsAuthStatusQueryResultBean sendCode(@RequestBody AemsAuthStatusQueryRequestBean autoStateQuery, HttpServletRequest request, HttpServletResponse response) {

        logger.info("授权状态查询第三方请求参数：" + JSONObject.toJSONString(autoStateQuery));
        
        AemsAuthStatusQueryResultBean resultBean = new AemsAuthStatusQueryResultBean();
        String channel = BankCallConstant.CHANNEL_PC;
        // 电子账户号
        String accountId = autoStateQuery.getAccountId();
        
        // 验证请求参数
        // 机构编号
        if (autoStateQuery.checkParmIsNull()) {
            logger.info("请求参数非法");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
       
        // 验签  accountId
       if (!this.AEMSVerifyRequestSign(autoStateQuery, AemsAuthStatusQueryDefine.REQUEST_MAPPING+AemsAuthStatusQueryDefine.AUTH_STATUS_QUERY_ACTION)) {
            logger.info("----验签失败----");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000002);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        // 用户ID
        // 根据电子账户号查询用户ID
        BankOpenAccount bankOpenAccount = this.autoPlusService.selectBankOpenAccountByAccountId(accountId);
        if (bankOpenAccount == null) {
            logger.info("查询用户开户信息失败,用户电子账户号:[" + accountId + "]");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000004);
            resultBean.setStatusDesc("根据电子账户号查询用户信息失败");
            return resultBean;
        }
        Integer userId = bankOpenAccount.getUserId();
        Users user = this.autoPlusService.getUsersByUserId(userId);
        if (user == null) {
            logger.info("查询用户失败:[" + userId + "].");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000007);
            resultBean.setStatusDesc("查询用户失败");
            return resultBean;
        }
        Integer passwordFlag = user.getIsSetPassword();
        if (passwordFlag != 1) {// 未设置交易密码
            logger.info("-------------------未设置交易密码！"+autoStateQuery.getAccountId()+"！--------------------status"+user.getIsSetPassword());
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_TP000002);
            resultBean.setStatusDesc("未设置交易密码");
            return resultBean;
        }
        BankCallBean retBean=autoPlusService.getTermsAuthQuery(userId,channel);
        logger.info("调用江西银行授权状态查询接口:"+(retBean==null?"空":retBean.getPaymentAuth()));
        if(retBean==null){
            logger.info("银行返回为空,accountId:["+accountId+"]");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            resultBean.setStatusDesc("授权状态查询接口失败！");
            return resultBean;  
        }
        String retCode = retBean.getRetCode();
        if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
            logger.info("授权状态查询接口失败,accountId:["+accountId+"]返回码["+retCode+"]！");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            resultBean.setStatusDesc("授权状态查询接口失败！");
            return resultBean;
        }
        resultBean = getResultJosn(resultBean,retBean);
        logger.info("授权状态查询第三方返回参数："+JSONObject.toJSONString(resultBean));
        resultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
        resultBean.setStatusDesc("授权状态查询成功");
        return resultBean;
    }

    // 拼接返回参数
    private AemsAuthStatusQueryResultBean getResultJosn(AemsAuthStatusQueryResultBean resultBean, BankCallBean retBean) {
        resultBean.setAccountId(retBean.getAccountId());
        resultBean.setAgreeWithdrawStatus(retBean.getAgreeWithdraw());
        resultBean.setAutoBidDeadline(retBean.getAutoBidDeadline());
        resultBean.setAutoBidStatus(retBean.getAutoBid());
        resultBean.setAutoTransferStatus(retBean.getAutoTransfer());
        resultBean.setPaymentAuthStatus(retBean.getPaymentAuth());
        resultBean.setPaymentDeadline(retBean.getPaymentDeadline());
        resultBean.setRepayAuthStatus(retBean.getRepayAuth());
        resultBean.setRepayDeadline(retBean.getRepayDeadline());
        return resultBean;
    }
}
