package com.hyjf.api.server.user.tenderauth;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.surong.user.cashauth.suCashAuthService;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.util.ApiSignUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.vip.apply.ApplyDefine;

@Controller(TenderAuthDefine.CONTROLLER_NAME)
@RequestMapping(value = TenderAuthDefine.REQUEST_MAPPING)
public class TenderAuthServer extends BaseController {
    
    Logger _log = LoggerFactory.getLogger(TenderAuthServer.class);

    @Autowired
    private suCashAuthService suuserCashAuthService;
    

    
    @RequestMapping(value = TenderAuthDefine.TENDER_AUTH+"Test")
    public ModelAndView cashWithdrawalByFrom(HttpServletRequest request, HttpServletResponse response){
        //参数校验
        String channel = request.getParameter("channel");//渠道
        String accountId = request.getParameter("accountId");//电子账户accountId
        String bitMap = request.getParameter("bitMap");//维护标识
        String autoBid = request.getParameter("autoBid");//维护标识
        String autoTransfer = request.getParameter("autoTransfer");//维护标识
        String agreeWithdraw = request.getParameter("agreeWithdraw");//维护标识
        String directConsume = request.getParameter("directConsume");//维护标识
        String returnUrl = request.getParameter("retUrl");//同步通知地址
        String retnotifyUrl = request.getParameter("notifyUrl");//异步通知地址
        String transactionUrl = request.getParameter("transactionUrl");//返回交易页面链接
        String forgotPwdUrl = request.getParameter("forgotPwdUrl");//返回交易页面链接
        String instCode = request.getParameter("instCode");//返回交易页面链接
        
        
        TenderAuthRequestBean cashAuthRequestBean = new TenderAuthRequestBean();
            cashAuthRequestBean.setChannel(channel);//渠道
            cashAuthRequestBean.setAccountId(accountId);//电子账户accountId
            cashAuthRequestBean.setBitMap(bitMap);//维护标识
            cashAuthRequestBean.setAutoBid(autoBid);//维护标识
            cashAuthRequestBean.setAutoTransfer(autoTransfer);//维护标识
            cashAuthRequestBean.setAgreeWithdraw(agreeWithdraw);//维护标识
            cashAuthRequestBean.setDirectConsume(directConsume);//维护标识
            cashAuthRequestBean.setRetUrl(returnUrl);//同步通知地址
            cashAuthRequestBean.setNotifyUrl(retnotifyUrl);//异步通知地址
            cashAuthRequestBean.setTransactionUrl(transactionUrl);//返回交易页面链接
            cashAuthRequestBean.setForgotPwdUrl(forgotPwdUrl);
            cashAuthRequestBean.setInstCode(instCode);
            TenderAuthRequestBean bean = cashAuthRequestBean;
            String		sign =  bean.getAccountId() +(bean.getAcqRes()==null?"":bean.getAcqRes()) + (bean.getAgreeWithdraw() == null ?"": bean.getAgreeWithdraw()) + (bean.getAutoBid()==null?"":bean.getAutoBid()) + (bean.getAutoTransfer() ==null ? "":bean.getAutoTransfer()) + bean.getBitMap() 
                    + bean.getChannel() + (bean.getDirectConsume() == null ? "" : bean.getDirectConsume()) + bean.getNotifyUrl() 
                    + bean.getRetUrl() + bean.getTransactionUrl();

            String chkValue = ApiSignUtil.encryptByRSAForRequest(instCode, sign);
            cashAuthRequestBean.setChkValue(chkValue);
        return cashWithdrawal(request,response,cashAuthRequestBean);
    }
    
    /**
     * 代提现授权接口
     */
    @RequestMapping(value = TenderAuthDefine.TENDER_AUTH)
    public ModelAndView cashWithdrawal(HttpServletRequest request, HttpServletResponse response,@RequestBody TenderAuthRequestBean cashAuthRequestBean){
        BaseResultBean baseBean = new BaseResultBean();
        ModelAndView modelAndView = new ModelAndView();
       //参数校验
        String channel = cashAuthRequestBean.getChannel();//渠道
        String accountId = cashAuthRequestBean.getAccountId();//电子账户accountId
        String bitMap = cashAuthRequestBean.getBitMap();//维护标识
        String autoBid = cashAuthRequestBean.getAutoBid();//维护标识
        String autoTransfer = cashAuthRequestBean.getAutoTransfer();//维护标识
        String agreeWithdraw = cashAuthRequestBean.getAgreeWithdraw();//维护标识
        String directConsume = cashAuthRequestBean.getDirectConsume();//维护标识
        String returnUrl = cashAuthRequestBean.getRetUrl();//同步通知地址
        String retnotifyUrl = cashAuthRequestBean.getNotifyUrl();//异步通知地址
        String transactionUrl = cashAuthRequestBean.getTransactionUrl();//返回交易页面链接
        String forgotPwdUrl = cashAuthRequestBean.getForgotPwdUrl();//返回交易页面链接
        String acqRes = cashAuthRequestBean.getAcqRes();//保留域
        if(acqRes == null){
            acqRes = "";
        }
        _log.info("请求参数" + JSONObject.toJSONString(cashAuthRequestBean, true) + "]");
        
        if(checkParams(cashAuthRequestBean,modelAndView,baseBean) !=null ){
            return checkParams(cashAuthRequestBean,modelAndView,baseBean);
        }

        String verifyOrderUrl = "";
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+TenderAuthDefine.PROJECT_NAME+TenderAuthDefine.REQUEST_MAPPING+TenderAuthDefine.USER_AUTH_INVES_RETURN_ACTION+"?acqRes="+acqRes+"&callback="+returnUrl.replace("#", "*-*-*");
        String notifyUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+TenderAuthDefine.PROJECT_NAME+TenderAuthDefine.REQUEST_MAPPING+TenderAuthDefine.USER_AUTH_INVES_BGRETURN_ACTION+"?acqRes="+acqRes+"&callback="+retnotifyUrl.replace("#", "*-*-*");
        
        // 验证签名
        if(StringUtils.isEmpty(cashAuthRequestBean.getChkValue())||!this.verifyRequestSign(cashAuthRequestBean, TenderAuthDefine.REQUEST_MAPPING+TenderAuthDefine.TENDER_AUTH)){
            
            return sendErrorMsg("CE000002","验签失败",retnotifyUrl,returnUrl,baseBean,modelAndView,cashAuthRequestBean);
        }

       // 根据电子账户号查询用户ID
        BankOpenAccount bankOpenAccount = this.suuserCashAuthService.selectBankOpenAccountByAccountId(accountId);
        if (bankOpenAccount == null) {
            _log.info("查询用户开户信息失败,用户电子账户号:[" + accountId + "]");
            return sendErrorMsg("CE000001","查询用户开户信息失败",retnotifyUrl,returnUrl,baseBean,modelAndView,cashAuthRequestBean);
        }       
        // 用户ID
        Integer userId = bankOpenAccount.getUserId();
        Users user = this.suuserCashAuthService.getUsers(bankOpenAccount.getUserId());
        if (user == null) {
            _log.info("-------------------用户不存在汇盈金服账户！--------------------");
            return sendErrorMsg("CE999999","用户不存在汇盈金服账户",retnotifyUrl,returnUrl,baseBean,modelAndView,cashAuthRequestBean);
        }
        if(StringUtils.isEmpty(user.getIsSetPassword().toString())||!user.getIsSetPassword().toString().equals("1")){
            _log.info("-------------------用户未设置交易密码！--------------------");
            return sendErrorMsg("CE999999","用户未设置交易密码",retnotifyUrl,returnUrl,baseBean,modelAndView,cashAuthRequestBean);
        }
        //根据用户ID查询开通业务类型
        Map<String,Object> userAuth = this.suuserCashAuthService.selectUserAuth(userId);
        
        //根据维护标识来设置返回数据
        
        if("0".equals(bitMap)){
            modelAndView = new ModelAndView(TenderAuthDefine.AUTH_ERROR_PATH);
            if(!userAuth.isEmpty() && userAuth !=null){
                modelAndView.addObject("statusDesc", "查询成功");
                modelAndView.addObject("status","000");
                modelAndView.addObject("autoBid", StringUtils.isEmpty(userAuth.get("autocStatus").toString())?"0":"1");
                modelAndView.addObject("autoTransfer", StringUtils.isEmpty(userAuth.get("autoiStatus").toString())?"0":"1");
                modelAndView.addObject("agreeWithdraw",StringUtils.isEmpty(userAuth.get("autowStatus").toString())?"0":"1");
                modelAndView.addObject("directConsume", StringUtils.isEmpty(userAuth.get("autocoSataus").toString())?"0":"1");
                return modelAndView;
            }else{
                return sendErrorMsg("CE000004","查询失败或暂未授权",retnotifyUrl,returnUrl,baseBean,modelAndView,cashAuthRequestBean);
            }
        }else if("1".equals(bitMap)){
            BankCallBean cashBean = new BankCallBean();
            String orderId = GetOrderIdUtils.getOrderId2(userId);
            cashBean.setLogOrderId(orderId);// 日志订单号
            cashBean.setLogUserId(String.valueOf(userId));// 请求用户名
            cashBean.setLogBankDetailUrl(BankCallConstant.BANK_URL_AUTH_MODIFY);
            cashBean.setTxCode(TenderAuthDefine.TXCODE_AUTH);
            cashBean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
            cashBean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
            cashBean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
            cashBean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
            cashBean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
            cashBean.setOrderId(orderId);// 订单号
            cashBean.setChannel(channel);// 交易渠道
//            System.out.println("notifyUrl"+notifyUrl);
//            System.out.println("retUrl"+retUrl);

            cashBean.setNotifyUrl(notifyUrl);//异步通知地址
            cashBean.setRetUrl(retUrl);//同步通知地址
            cashBean.setForgotPwdUrl(forgotPwdUrl);//忘记密码通知地址
            cashBean.setVerifyOrderUrl(verifyOrderUrl);//订单有效性连接
            cashBean.setTransactionUrl(transactionUrl);
            cashBean.setAccountId(accountId);
            //设置维护值
            String [] user_Auth = getUserAuth(userAuth, autoBid, autoTransfer, agreeWithdraw, directConsume);
            cashBean.setAutoBid(user_Auth[0]); //开通自动投标功能标志
            cashBean.setAutoTransfer(user_Auth[1]); //开通自动债转功能标志
            cashBean.setAgreeWithdraw(user_Auth[2]); //开通预约取现功能标志
            cashBean.setDirectConsume(user_Auth[3]); //开通无密消费功能标识   
            
            boolean insAuthLog = this.suuserCashAuthService.insertUserAuthLog(userId, cashBean,"1");
            if(!insAuthLog){
                _log.info("插入日志表失败,电子账户号:[" + accountId + "].");
            }
            try {
                modelAndView = BankCallUtils.callApi(cashBean);
            }catch(Exception e){
                e.getMessage();
                return sendErrorMsg("CE999999","开通失败,调用银行接口失败",retnotifyUrl,returnUrl,baseBean,modelAndView,cashAuthRequestBean);
            }
            return modelAndView;
            
        }else{
            return sendErrorMsg("CE000001","维护标识不正确",retnotifyUrl,returnUrl,baseBean,modelAndView,cashAuthRequestBean);
        }
    }
    
    
    
    public static ModelAndView sendErrorMsg(String status,String statusDesc , String retnotifyUrl,String returnUrl,BaseResultBean baseBean,ModelAndView modelAndView, TenderAuthRequestBean cashAuthRequestBean){
        Map<String, String> params = new HashMap<String, String>();
        TenderAuthResultBean resultBean = new TenderAuthResultBean();
        String acqRes = cashAuthRequestBean.getAcqRes();
        String accountId = cashAuthRequestBean.getAccountId();
        if(acqRes == null){
            acqRes = "";
        }
        params.put("status", status);
        params.put("statusDesc",statusDesc);
        baseBean.setStatusForResponse(status);
        baseBean.setStatusDesc(statusDesc);
        params.put("chkValue", baseBean.getChkValue());
        params.put("acqRes", acqRes);
        params.put("accountId", accountId);
        if(StringUtils.isNotEmpty(retnotifyUrl)){
            CommonSoaUtils.noRetPostThree(retnotifyUrl, params);
        }
        if(StringUtils.isNotEmpty(returnUrl)){
            resultBean.setCallBackAction(returnUrl);
            modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
            resultBean.set("status", status);
            resultBean.set("statusDesc",statusDesc);
            resultBean.set("chkValue", baseBean.getChkValue());
            resultBean.set("acqRes", acqRes);
            resultBean.set("accountId", accountId);
            modelAndView.addObject("callBackForm", resultBean);
        }else{
            modelAndView = new ModelAndView(TenderAuthDefine.AUTH_ERROR_PATH);
            modelAndView.addObject("status",status);       
            modelAndView.addObject("statusDesc", statusDesc);
        }
        return modelAndView;
    }
    
    public static ModelAndView checkParams (TenderAuthRequestBean cashAuthRequestBean,ModelAndView modelAndView,BaseResultBean baseBean){
        String channel = cashAuthRequestBean.getChannel();//渠道
        String accountId = cashAuthRequestBean.getAccountId();//电子账户accountId
        String bitMap = cashAuthRequestBean.getBitMap();//维护标识
        String autoBid = cashAuthRequestBean.getAutoBid();//维护标识
        String autoTransfer = cashAuthRequestBean.getAutoTransfer();//维护标识
        String agreeWithdraw = cashAuthRequestBean.getAgreeWithdraw();//维护标识
        String directConsume = cashAuthRequestBean.getDirectConsume();//维护标识
        String returnUrl = cashAuthRequestBean.getRetUrl();//同步通知地址
        String retnotifyUrl = cashAuthRequestBean.getNotifyUrl();//异步通知地址
        String transactionUrl = cashAuthRequestBean.getTransactionUrl();//返回交易页面链接
        String acqRes = cashAuthRequestBean.getAcqRes();//保留域
        String forgotPwdUrl = cashAuthRequestBean.getForgotPwdUrl();//保留域

        String status = "CE000001";
        
        if (Validator.isNull(returnUrl)) {
            return sendErrorMsg(status,"同步通知地址不能为空",retnotifyUrl,returnUrl,baseBean,modelAndView,cashAuthRequestBean);
        }
        
        if (Validator.isNull(retnotifyUrl)) {
            return sendErrorMsg(status,"异步通知地址不能为空",retnotifyUrl,returnUrl,baseBean,modelAndView,cashAuthRequestBean);
        }
        
        if (Validator.isNull(transactionUrl)) {
            return sendErrorMsg(status,"返回交易页面链接不能为空",retnotifyUrl,returnUrl,baseBean,modelAndView,cashAuthRequestBean);
        }
        
        if (Validator.isNull(forgotPwdUrl)) {
            return sendErrorMsg(status,"忘记交易密码链接不能为空",retnotifyUrl,returnUrl,baseBean,modelAndView,cashAuthRequestBean);
        }
        
        
        if (Validator.isNull(channel)) {
            return sendErrorMsg(status,"交易渠道不能为空",retnotifyUrl,returnUrl,baseBean,modelAndView,cashAuthRequestBean);
        }
        
        if (Validator.isNull(accountId)) {
            return sendErrorMsg(status,"电子账户号不能为空",retnotifyUrl,returnUrl,baseBean,modelAndView,cashAuthRequestBean);
        }
        if (Validator.isNull(bitMap)) {
            return sendErrorMsg(status,"维护标识不能为空",retnotifyUrl,returnUrl,baseBean,modelAndView,cashAuthRequestBean);
        }
        
        if (Validator.isNull(autoBid)&&Validator.isNull(autoTransfer)&&Validator.isNull(agreeWithdraw)&&Validator.isNull(directConsume)) {
            return sendErrorMsg(status,"开通功能标志不能为空",retnotifyUrl,returnUrl,baseBean,modelAndView,cashAuthRequestBean);
        }
        return null;
    }
    
    public static String[] getUserAuth(Map<String,Object> map ,String autoBid,String autoTransfer,String agreeWithdraw,String directConsume){

        if(StringUtils.isEmpty(autoBid)){
            autoBid = "0";
        }
        if(StringUtils.isEmpty(autoTransfer)){
            autoTransfer = "0";            
        }
        if(StringUtils.isEmpty(agreeWithdraw)){
            autoTransfer = "0";
        }
        if(StringUtils.isEmpty(directConsume)){
            directConsume = "0";
        }
        if(map==null || map.isEmpty()){
            
        }else{
            if((boolean)map.get("autoiStatus")&&StringUtils.isEmpty(autoBid)){
                autoBid = "1";
            }
            if((boolean)map.get("autocStatus")&&(StringUtils.isEmpty(autoTransfer))){
                autoTransfer = "1";
            }
            if((boolean)map.get("autowStatus")&&StringUtils.isEmpty(agreeWithdraw)){
                agreeWithdraw = "1";
            }
            if((boolean)map.get("autocoSataus")&&StringUtils.isEmpty(directConsume)){
                directConsume = "1";
            }
        }
        String [] str =new  String[] {autoBid,autoTransfer,agreeWithdraw,directConsume};
        return str;
    }
    
    /**
     * 用户授权同步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(TenderAuthDefine.USER_AUTH_INVES_RETURN_ACTION)
    public ModelAndView userAuthInvesReturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {
        _log.info("授权同步回调start");
        ModelAndView modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
        
        TenderAuthResultBean resultBean = new TenderAuthResultBean();
        System.out.println(request.getAttribute("accountId"));
        resultBean.setCallBackAction(request.getParameter("callback").replace("*-*-*","#"));
        _log.info("同步回调--------------------"+request.getParameter("callback"));
        _log.info("同步参数"+bean.getLogUserId());

        int userId = Integer.parseInt(bean.getLogUserId());
        BankOpenAccount bankOpenAccount = this.suuserCashAuthService.getBankOpenAccount(userId);
        
        //根据用户ID查询开通业务类型
        if(bean.getRetCode()!=null&&!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())){
            resultBean.set("status", "CE999999");
            resultBean.set("acqRes", request.getParameter("acqRes"));
            resultBean.set("accountId",bankOpenAccount.getAccount());
            resultBean.set("statusDesc",bean.getRetMsg());
            modelAndView.addObject("callBackForm", resultBean);
            return modelAndView;
        }else{
              String statusDesc = "银行处理中";
              String status = "000";
                  
              BaseResultBean baseBean = new BaseResultBean();
              baseBean.setStatusForResponse(status);
              resultBean.set("accountId",bankOpenAccount.getAccount());
              resultBean.set("status", status);
              resultBean.set("acqRes", request.getParameter("acqRes"));
              resultBean.set("statusDesc",statusDesc);
              resultBean.set("chkValue", baseBean.getChkValue());
              modelAndView.addObject("callBackForm", resultBean);
              return modelAndView;
        }
        
    }
    
    /**
     * 用户授权异步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(TenderAuthDefine.USER_AUTH_INVES_BGRETURN_ACTION)
    @ResponseBody
    public BankCallResult userAuthInvesBgreturn(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute BankCallBean bean) {
        _log.info("授权异步回调start");
        BankCallResult result = new BankCallResult();
        bean.convert();
        Map<String, String> params = new HashMap<String, String>();
        String statusDesc = "";
        String status = "";
        System.out.println("异步回调--------------------"+request.getParameter("callback"));
        BankOpenAccount bankOpenAccount = this.suuserCashAuthService.selectBankOpenAccountByAccountId(bean.getAccountId());
        int userId = bankOpenAccount.getUserId();
        if(BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())){
                Map<String,Object> userAuth = this.suuserCashAuthService.selectUserAuth(userId);
                //将授权标识与订单号插入用户授权表
                _log.info("userAuth"+ userAuth);
                _log.info("授权异步回调上游返回参数"+ JSONObject.toJSONString(bean, true));
                boolean insAuth;
                if(StringUtils.isEmpty(bean.getAutoBid())){
                    if(userAuth!=null&&userAuth.containsKey("autoiStatus")){
                        bean.setAutoBid((boolean)userAuth.get("autoiStatus")?"1":"0");
                    }else{
                        bean.setAutoBid("0");
                    }
                }
                if(StringUtils.isEmpty(bean.getAutoTransfer())){
                   if(userAuth!=null&&userAuth.containsKey("autocStatus")){
                       bean.setAutoTransfer((boolean)userAuth.get("autocStatus")?"1":"0");               
                   }else{
                       bean.setAutoTransfer("0");
                   }
                }
                if(StringUtils.isEmpty(bean.getAgreeWithdraw())){
                    if(userAuth!=null&&userAuth.containsKey("autowStatus")){
                        bean.setAgreeWithdraw((boolean)userAuth.get("autowStatus")?"1":"0");
                    }else{
                        bean.setAgreeWithdraw("0");
                    }
                }
                if(StringUtils.isEmpty(bean.getDirectConsume())){
                    if(userAuth!=null&&userAuth.containsKey("autocoSataus")){
                        bean.setDirectConsume((boolean)userAuth.get("autocoSataus")?"1":"0");
                    }else{
                        bean.setDirectConsume("0");
                    }
                }

                if(userAuth ==null || userAuth.isEmpty()){
                    insAuth = this.suuserCashAuthService.insertUserAuthInves(userId,bean);
                }else{
                     insAuth = this.suuserCashAuthService.updateUserAuthInves(userId,bean);
                }
                
                System.out.println(bean.getOrderId());      
                
                boolean updAuthLog = this.suuserCashAuthService.updateUserAuthLog(userId, bean.getLogOrderId());
                
                if(!updAuthLog){
                    _log.info("更新日志表失败,电子账户号:[" + bean.getAccountId() + "].");
                }
                
                if(insAuth){
                    statusDesc = "授权成功";
                    status = "000";
                }else{
                    statusDesc = "授权失败";
                    status = "CE999999";
                }
                params.put("status", status);
                params.put("statusDesc",statusDesc);
                BaseResultBean baseBean = new BaseResultBean();
                baseBean.setStatusForResponse(status);
                baseBean.setStatusDesc(statusDesc);
                params.put("acqRes", request.getParameter("acqRes"));
                params.put("accountId",bean.getAccountId());
                params.put("chkValue", baseBean.getChkValue());
                CommonSoaUtils.noRetPostThree(request.getParameter("callback").replace("*-*-*","#"), params);
                result.setMessage("用户授权修改成功");
                result.setStatus(true);
                return result;
        }else{
            status = "CE999999";
            statusDesc = "授权失败"+bean.getRetMsg();
            params.put("status", status);
            params.put("acqRes", request.getParameter("acqRes"));
            params.put("statusDesc",statusDesc);
            params.put("accountId",bean.getAccountId());
            BaseResultBean baseBean = new BaseResultBean();
            baseBean.setStatusForResponse(status);
            baseBean.setStatusDesc(statusDesc);
            params.put("chkValue", baseBean.getChkValue());
            CommonSoaUtils.noRetPostThree(request.getParameter("callback"), params);
            result.setMessage("用户授权修改失败");
            result.setStatus(false);
            return result;
        }
    }
}
