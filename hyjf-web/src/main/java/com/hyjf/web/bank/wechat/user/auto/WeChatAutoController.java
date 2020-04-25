/**
 * 个人设置控制器
 */
package com.hyjf.web.bank.wechat.user.auto;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.auto.AutoService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.bank.web.user.auto.AutoController;
import com.hyjf.web.bank.web.user.auto.AutoDefine;
import com.hyjf.web.user.login.LoginService;

/**
 * 
 * 用户授权控制器
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年8月15日
 * @see 下午4:15:36
 */
@Controller
@RequestMapping(value = WeChatAutoDefine.REQUEST_MAPPING)
public class WeChatAutoController extends BaseController {
    /** THIS_CLASS */
    private static final String THIS_CLASS = WeChatAutoController.class.getName();
    @Autowired
    private AutoService authService;
    
    @Autowired
    private LoginService loginService;
    
    
    
    
    /**
     * 根据用户Id获取用户授权自动出借状态
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(WeChatAutoDefine.GET_USER_AUTO_STATUS_BY_USERID_ACTION)
    public JSONObject getUserAutoStatusByUserId(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, WeChatAutoDefine.GET_USER_AUTO_STATUS_BY_USERID_ACTION);
        JSONObject info = new JSONObject();
        // 用户Id
        String userIdStr = request.getParameter("userId");
        if (StringUtils.isEmpty(userIdStr)) {
            info.put("status", "1");
            info.put("message", "您未登陆，请先登录");
            return info;
        }
        Integer userId = Integer.parseInt(userIdStr);
        try {
            HjhUserAuth hjhUserAuth=authService.getHjhUserAuthByUserId(userId);
            if(hjhUserAuth!=null&&hjhUserAuth.getAutoInvesStatus()==1&&hjhUserAuth.getAutoCreditStatus()==1){
                info.put("userAutoStatus", "1");
                info.put("status", "1");
                info.put("message", "查询用户自动出借授权状态成功");
            }else{
                info.put("serAutoStatus", "0");
                info.put("status", "1");
                info.put("message", "查询用户自动出借授权状态成功");
            }
        } catch (Exception e) {
            info.put("userAutoStatus", "0");
            info.put("status", "0");
            info.put("message", "系统异常，请重新访问");
        }
        
        LogUtil.endLog(THIS_CLASS, WeChatAutoDefine.GET_USER_AUTO_STATUS_BY_USERID_ACTION);
        return info;
    }
    
    
    /**
     * 用户授权自动出借
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value =WeChatAutoDefine.USER_AUTH_INVES_ACTION)
    public ModelAndView userAuthInves(HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(THIS_CLASS, WeChatAutoDefine.USER_AUTH_INVES_ACTION);
        ModelAndView modelAndView = new ModelAndView();
        // 用户id
        String userId = request.getParameter("userId");
        //返回结果url，微信端提供
        String callback = request.getParameter("callback");
        //返回交易页面链接，微信端提供
        String transactionUrl = request.getParameter("transactionUrl");
        
        //返回结果
        String status = "";
        String message = "";
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(callback)) {
            status = "1";
            message = "请求参数错误";
            modelAndView = new ModelAndView(returnUrl(status, message, callback));
            return modelAndView;
        }
        //用户
        Users user = this.authService.getUsers(Integer.valueOf(userId));
        if(user == null){
            status = "1";
            message = "该用户不存在";
            modelAndView = new ModelAndView(returnUrl(status, message, callback));
            return modelAndView;
        }
        
        BankOpenAccount bankOpenAccount = authService.getBankOpenAccount(user.getUserId());
        if(bankOpenAccount == null){
            status = "1";
            message = "开户信息不存在";
            modelAndView = new ModelAndView(returnUrl(status, message, callback));
            return modelAndView;
        }
        if(user.getIsSetPassword() == 0){
            status = "1";
            message = "用户未设置交易密码";
            modelAndView = new ModelAndView(returnUrl(status, message, callback));
            return modelAndView;
        }
        
        HjhUserAuth hjhUserAuth=authService.getHjhUserAuthByUserId(user.getUserId());
        
        if(hjhUserAuth!=null&&hjhUserAuth.getAutoInvesStatus()==1&&hjhUserAuth.getAutoCreditStatus()==1){
            status = "1";
            message = "用户已授权,无需重复授权";
            modelAndView = new ModelAndView(returnUrl(status, message, callback));
            return modelAndView;
        }
        
        // 同步调用路径
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + WeChatAutoDefine.REQUEST_MAPPING
                + WeChatAutoDefine.USER_AUTH_INVES_RETURN_ACTION + ".do?callback="+callback;
        // 异步调用路
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) + WeChatAutoDefine.REQUEST_MAPPING
                + WeChatAutoDefine.USER_AUTH_INVES_BGRETURN_ACTION + ".do?callback="+callback;
        String orderId=GetOrderIdUtils.getOrderId2(Integer.valueOf(userId));
        String verifyOrderUrl = "";
        BankCallBean bean = new BankCallBean();
        bean.setLogOrderId(orderId);
        // 操作者ID
        bean.setLogUserId(String.valueOf(userId));
        bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_AUTH_MODIFY);
        bean.setTxCode(BankCallConstant.TXCODE_USER_AUTH_MODIFY);
        bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
        bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
        bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
        bean.setOrderId(orderId);
        bean.setChannel(BankCallConstant.CHANNEL_WEI);// 交易渠道
        bean.setNotifyUrl(bgRetUrl);//异步通知地址
        bean.setRetUrl(retUrl);//同步通知地址
        bean.setForgotPwdUrl(CustomConstants.FORGET_PASSWORD_URL);//忘记密码通知地址
        bean.setVerifyOrderUrl(verifyOrderUrl);//订单有效性连接
        bean.setTransactionUrl(transactionUrl);
        bean.setAccountId(bankOpenAccount.getAccount());// 电子账号
        bean.setAutoBid("1");
        bean.setAutoTransfer("1");
        if(hjhUserAuth!=null){
            bean.setAgreeWithdraw(hjhUserAuth.getAutoWithdrawStatus()+"");
            bean.setDirectConsume(hjhUserAuth.getAutoConsumeStatus()+"");
        }else{
            bean.setAgreeWithdraw("0");
            bean.setDirectConsume("0");
        }  
        
        
        bean.setRemark("出借人自动投标签约");
        
        bean.setLogRemark("出借人自动投标签约");
        this.authService.insertUserAuthLog(Integer.valueOf(userId), bean,0,BankCallConstant.QUERY_TYPE_1);
        // 跳转到汇付天下画面
        try {
            modelAndView = BankCallUtils.callApi(bean);
            LogUtil.endLog(AutoController.class.toString(), WeChatAutoDefine.USER_AUTH_INVES_ACTION);
        } catch (Exception e) {
            e.printStackTrace();
            status = "0";
            message = "调用银行接口失败！";
            modelAndView = new ModelAndView(returnUrl(status, message, callback));
            LogUtil.errorLog(AutoController.class.toString(), AutoDefine.USER_AUTH_INVES_ACTION, e);
        }
        return modelAndView;
    }

    /**
     * 用户授权自动出借同步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(WeChatAutoDefine.USER_AUTH_INVES_RETURN_ACTION)
    public ModelAndView passwordReturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {

        LogUtil.startLog(THIS_CLASS, WeChatAutoDefine.USER_AUTH_INVES_RETURN_ACTION, "[开户同步回调开始]");
        ModelAndView modelAndView = new ModelAndView();
        //返回地址
        String callback = request.getParameter("callback");
        bean.convert();
        Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
        //返回结果
        String status = "";
        String message = "";
        HjhUserAuth hjhUserAuth = this.authService.getHjhUserAuthByUserId(userId);
        // 返回失败
        if (bean.getRetCode()!=null&&!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
            status = "1";
            message = "用户授权自动出借失败,请联系客服";//失败原因：" + authService.getBankRetMsg(bean.getRetCode());
            modelAndView = new ModelAndView(returnUrl(status, message, callback));
            LogUtil.endLog(THIS_CLASS, WeChatAutoDefine.USER_AUTH_INVES_RETURN_ACTION, "[交易完成后,回调结束]");
            return modelAndView;
        }
        // 判断用户授权自动出借是否已成功
        if (hjhUserAuth!=null&&hjhUserAuth.getAutoInvesStatus()==1) {
            status = "1";
            message = "用户授权自动出借成功";
            modelAndView = new ModelAndView(returnUrl(status, message, callback));
            LogUtil.endLog(THIS_CLASS, WeChatAutoDefine.USER_AUTH_INVES_RETURN_ACTION, "[交易完成后,回调结束]");
            return modelAndView;
        }
        
        BankOpenAccount bankOpenAccount = authService.getBankOpenAccount(userId);
        // 调用查询出借人签约状态查询
        BankCallBean selectbean = new BankCallBean();
        selectbean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        selectbean.setTxCode(BankCallConstant.TXCODE_CREDIT_AUTH_QUERY);
        selectbean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        selectbean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
        selectbean.setTxDate(GetOrderIdUtils.getTxDate());
        selectbean.setTxTime(GetOrderIdUtils.getTxTime());
        selectbean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
        selectbean.setChannel(BankCallConstant.CHANNEL_PC);
        selectbean.setType(BankCallConstant.QUERY_TYPE_1);
        selectbean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));// 电子账号
        
        // 操作者ID
        selectbean.setLogUserId(String.valueOf(userId));
        selectbean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
        selectbean.setLogRemark("用户授权自动出借");
        selectbean.setLogClient(0);
        // 返回参数
        BankCallBean retBean = null;
        // 调用接口
        retBean = BankCallUtils.callApiBg(selectbean);
        LogUtil.endLog(AutoController.class.toString(), AutoDefine.USER_AUTH_INVES_RETURN_ACTION);
        
        try {
            if("1".equals(retBean.getState())){
                // 更新签约状态和日志表
                this.authService.updateUserAuthInves(userId,retBean);
                status = "1";
                message = "用户授权自动出借成功";
                modelAndView = new ModelAndView(returnUrl(status, message, callback));
                LogUtil.endLog(THIS_CLASS, WeChatAutoDefine.USER_AUTH_INVES_RETURN_ACTION, "[交易完成后,回调结束]");
                return modelAndView;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.errorLog(AutoDefine.THIS_CLASS, AutoDefine.USER_AUTH_INVES_RETURN_ACTION, e);
        }
        status = "1";
        message = "用户授权自动出借失败,请联系客服";
        modelAndView = new ModelAndView(returnUrl(status, message, callback));
        LogUtil.endLog(THIS_CLASS, WeChatAutoDefine.USER_AUTH_INVES_RETURN_ACTION, "[交易完成后,回调结束]");
        return modelAndView;
    }

    /**
     * 用户授权自动出借异步回调
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(AutoDefine.USER_AUTH_INVES_BGRETURN_ACTION)
    public String passwordBgreturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {
        BankCallResult result = new BankCallResult();
        LogUtil.startLog(THIS_CLASS, AutoDefine.USER_AUTH_INVES_BGRETURN_ACTION, "[开户异步回调开始]");
        bean.convert();
        Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
        // 查询用户开户状态
        Users user = this.authService.getUsers(userId);

        
        // 成功或审核中
        if (user != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE))) {
            try {
                BankOpenAccount bankOpenAccount = authService.getBankOpenAccount(userId);
                // 调用查询出借人签约状态查询
                BankCallBean selectbean = new BankCallBean();
                selectbean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
                selectbean.setTxCode(BankCallConstant.TXCODE_CREDIT_AUTH_QUERY);
                selectbean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
                selectbean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
                selectbean.setTxDate(GetOrderIdUtils.getTxDate());
                selectbean.setTxTime(GetOrderIdUtils.getTxTime());
                selectbean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
                selectbean.setChannel(BankCallConstant.CHANNEL_PC);
                selectbean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));// 电子账号
                selectbean.setType(BankCallConstant.QUERY_TYPE_1);
                // 操作者ID
                selectbean.setLogUserId(String.valueOf(userId));
                selectbean.setLogOrderId(GetOrderIdUtils.getUsrId(userId));
                selectbean.setLogClient(0);
                // 返回参数
                BankCallBean retBean = null;
                // 调用接口
                retBean = BankCallUtils.callApiBg(selectbean);
                LogUtil.endLog(AutoController.class.toString(), AutoDefine.USER_AUTH_INVES_RETURN_ACTION);
                if("1".equals(retBean.getState())){
                    // 更新签约状态和日志表
                    this.authService.updateUserAuthInves(userId,retBean);

                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.errorLog(AutoDefine.THIS_CLASS, AutoDefine.USER_AUTH_INVES_BGRETURN_ACTION, e);
            }
        }
        LogUtil.endLog(AutoDefine.THIS_CLASS, AutoDefine.USER_AUTH_INVES_BGRETURN_ACTION, "[用户授权自动出借完成后,回调结束]");
        result.setMessage("用户授权自动出借成功");
        result.setStatus(true);
        return JSONObject.toJSONString(result, true);
    }
    
    
    /**
     * 拼接返回结果
     * 
     * @param error
     * @param errorDesc
     * @param callback
     * @return
     */
    public String returnUrl(String error, String errorDesc, String callback) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("error", error);
        map.put("errorDesc", errorDesc);
        String data = JSON.toJSONString(map);
        try {
            data = URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = callback + "backinfo/" + data;
        return "redirect:" + url;
    }
}
