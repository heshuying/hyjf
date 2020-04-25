package com.hyjf.wechat.controller.user.paymentauthpage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.autoup.AutoPlusService;
import com.hyjf.bank.service.user.paymentauthpage.PaymentAuthPageBean;
import com.hyjf.bank.service.user.paymentauthpage.PaymentAuthService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.wechat.BaseResultBeanFrontEnd;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseMapBean;
import com.hyjf.wechat.util.SignValue;

/**
 * 缴费授权及结果页 
 * @author jun 2018/03/23
 *
 */
@Controller
@RequestMapping(value = PaymentAuthPagDefine.REQUEST_MAPPING)
public class PaymentAuthPagController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(PaymentAuthPagController.class);

    @Autowired
    private PaymentAuthService paymentAuthService;
    
    @Autowired
    private AutoPlusService autoPlusService;

    /**
     * 当前controller名称
     */
    public static final String THIS_CLASS = PaymentAuthPagController.class.getName();

    /**
     * 
     * 服务费授权请求地址
     * @author sunss
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(PaymentAuthPagDefine.PAYMENT_AUTH_PAG_ACTION)
    @SignValidate
    public ModelAndView paymentAuth(HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(THIS_CLASS, PaymentAuthPagDefine.PAYMENT_AUTH_PAG_ACTION);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView = new ModelAndView(PaymentAuthPagDefine.JUMP_HTML);
        BaseMapBean baseMapBean=new BaseMapBean();
        String sign = request.getParameter("sign");
        String platform = "1";
        //判断用户是否登录
        Integer userId = requestUtil.getRequestUserId(request);
        
        if(userId==null||userId<=0){
            return getErrorMV("失败原因：用户未登录！",baseMapBean);
        }
        BankOpenAccount account = this.paymentAuthService.getBankOpenAccount(userId);
        if (account == null) {
            return getErrorMV("失败原因：用户未开户！",baseMapBean); 
        }
        Users user = this.paymentAuthService.getUsers(userId);
        if (user.getIsSetPassword() == 0) {// 未设置交易密码
            return getErrorMV("失败原因：用户未设置交易密码！",baseMapBean); 
        }
        // 判断是否授权过  
        if(autoPlusService.checkIsAuth(user.getUserId(),BankCallConstant.TXCODE_PAYMENT_AUTH_PAGE)){
            return getErrorMV("失败原因：用户已授权,无需重复授权！",baseMapBean); 
        }
        
		// 缴费授权状态
		baseMapBean.set(CustomConstants.PAYMENT_AUTH_STATUS,String.valueOf(user.getPaymentAuthStatus()));
        
        try {
            // 同步调用路径
            String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() +  PaymentAuthPagDefine.REQUEST_MAPPING
                    + PaymentAuthPagDefine.RETURL_SYN_ACTION + ".page";
            // 异步调用路
            String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() +  PaymentAuthPagDefine.REQUEST_MAPPING
                    + PaymentAuthPagDefine.RETURL_ASY_ACTION + ".do";
            PaymentAuthPageBean openBean = new PaymentAuthPageBean();
            openBean.setUserId(user.getUserId());
            openBean.setIp(CustomUtil.getIpAddr(request));
            openBean.setAccountId(account.getAccount());
            // 同步 异步
            openBean.setRetUrl(retUrl);
            openBean.setNotifyUrl(bgRetUrl);
            // 0：PC  1：微官网  2：Android  3：iOS  4：其他
            openBean.setPlatform(platform);
            openBean.setChannel(BankCallConstant.CHANNEL_APP);
            String forgetPassworedUrl = CustomConstants.FORGET_PASSWORD_URL + "?sign=" + sign ;
            openBean.setForgotPwdUrl(forgetPassworedUrl);
            String orderId = GetOrderIdUtils.getOrderId2(openBean.getUserId());
            openBean.setOrderId(orderId);
            modelAndView = paymentAuthService.getCallbankMV(openBean);
            paymentAuthService.insertUserAuthLog(openBean.getUserId(), orderId,Integer.parseInt(openBean.getPlatform()),"5");
            LogUtil.endLog(THIS_CLASS, PaymentAuthPagDefine.PAYMENT_AUTH_PAG_ACTION);
            //缴费授权状态
            modelAndView.addObject(CustomConstants.PAYMENT_AUTH_STATUS,String.valueOf(user.getPaymentAuthStatus()));
            return modelAndView;
            
        } catch (Exception e) {
            logger.error("调用银行接口失败",e);
            LogUtil.errorLog(THIS_CLASS, PaymentAuthPagDefine.PAYMENT_AUTH_PAG_ACTION, e);
            return getErrorMV("失败原因：调用银行接口失败！",baseMapBean); 
        }
    }

    private ModelAndView getErrorMV(String msg, BaseMapBean baseMapBean) {
        ModelAndView modelAndView = new ModelAndView(PaymentAuthPagDefine.JUMP_HTML);
        baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.FAIL);
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, msg);
        baseMapBean.setCallBackAction(CustomConstants.HOST+PaymentAuthPagDefine.JUMP_HTML_FAILED_PATH);
        modelAndView.addObject("callBackForm", baseMapBean);
        LogUtil.endLog(THIS_CLASS, PaymentAuthPagDefine.PAYMENT_AUTH_PAG_ACTION);
        return modelAndView;
    }
    
    private ModelAndView getSuccessMV(String msg, BaseMapBean baseMapBean) {
        ModelAndView modelAndView = new ModelAndView(PaymentAuthPagDefine.JUMP_HTML);
        baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, msg);
        baseMapBean.setCallBackAction(CustomConstants.HOST+PaymentAuthPagDefine.JUMP_HTML_SUCCESS_PATH);
        modelAndView.addObject("callBackForm", baseMapBean);
        LogUtil.endLog(THIS_CLASS, PaymentAuthPagDefine.PAYMENT_AUTH_PAG_ACTION);
        return modelAndView;
    }
    
    
    /**
     * 服务费授权同步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(PaymentAuthPagDefine.RETURL_SYN_ACTION)
    public ModelAndView pageReturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {

        LogUtil.startLog(THIS_CLASS, PaymentAuthPagDefine.RETURL_SYN_ACTION, "[服务费授权同步回调开始]");
        BaseMapBean baseMapBean=new BaseMapBean();
        bean.convert();
        
        String frontParams = request.getParameter("frontParams");
        if(StringUtils.isBlank(bean.getRetCode())&&StringUtils.isNotBlank(frontParams)){
            JSONObject jsonParm = JSONObject.parseObject(frontParams);
            if(jsonParm.containsKey("RETCODE")){
                bean.setRetCode(jsonParm.getString("RETCODE"));
            }
        }
        String retCode = bean.getRetCode();
        if(StringUtils.isNotBlank(retCode)&& !BankCallStatusConstant.RESPCODE_SUCCESS.equals(retCode)){
            return getErrorMV("失败原因:"+autoPlusService.getBankRetMsg(bean.getRetCode()),baseMapBean); 
        }
        // 出借人签约状态查询
        logger.info("服务费授权同步回调调用查询接口查询状态");
        BankCallBean retBean=autoPlusService.getTermsAuthQuery(Integer.parseInt(bean.getLogUserId()),BankCallConstant.CHANNEL_APP);
//        bean=retBean;
        // 返回失败
        if (retBean!=null&& BankCallStatusConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode())) {
            logger.info("服务费授权同步回调调用查询接口查询状态结束  结果为:" + retBean.getRetCode() + "授权状态为：" +retBean.getPaymentAuth());
            // 成功
            if("1".equals(retBean.getPaymentAuth())){
                return getSuccessMV("服务费授权成功！",baseMapBean); 
            }else{
                return getErrorMV("失败原因:"+autoPlusService.getBankRetMsg(retCode),baseMapBean); 
            }
        } else {
            logger.info("服务费授权同步回调调用查询接口查询失败");
            return getErrorMV("失败原因:"+autoPlusService.getBankRetMsg(retCode),baseMapBean); 
        }
    }

    /**
     * 服务费授权异步回调
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(PaymentAuthPagDefine.RETURL_ASY_ACTION)
    public BankCallResult bgreturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {
        BankCallResult result = new BankCallResult();
        LogUtil.startLog(THIS_CLASS, PaymentAuthPagDefine.RETURL_ASY_ACTION, "[服务费授权异步回调开始,userId:"+bean.getLogUserId()+"]");
        bean.convert();
        Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
        // 查询用户开户状态
        Users user = this.autoPlusService.getUsers(userId);
        // 成功
        if (user!=null&&bean != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE)) 
                && "1".equals(bean.getTxState())) {
            try {
                bean.setOrderId(bean.getLogOrderId());
                // 更新签约状态和日志表
                this.autoPlusService.updateUserAuth(Integer.parseInt(bean.getLogUserId()),bean);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.errorLog(THIS_CLASS, PaymentAuthPagDefine.RETURL_ASY_ACTION, e);
            }
        }
        LogUtil.endLog(THIS_CLASS, PaymentAuthPagDefine.RETURL_ASY_ACTION, "[用户服务费授权完成后,回调结束]");
        result.setMessage("服务费授权成功");
        result.setStatus(true);
        return result;
    }
    
}

