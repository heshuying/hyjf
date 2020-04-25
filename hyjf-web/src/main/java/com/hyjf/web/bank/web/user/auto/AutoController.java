/**
 * 个人设置控制器
 */
package com.hyjf.web.bank.web.user.auto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.login.LoginService;
import com.hyjf.web.util.WebUtils;

/**
 * 
 * 用户授权控制器
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年8月15日
 * @see 下午4:15:36
 */
@Controller

@RequestMapping(value = AutoDefine.REQUEST_MAPPING)
public class AutoController extends BaseController {

    @Autowired
    private AutoService authService;
    
    @Autowired
    private LoginService loginService;
    /**
     * 用户授权自动出借
     *
     * @param request
     * @param form
     * @return
     */
    @Deprecated
    @RequestMapping(AutoDefine.USER_AUTH_INVES_ACTION)
    public ModelAndView setPassword(HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(AutoDefine.THIS_CLASS, AutoDefine.USER_AUTH_INVES_ACTION);
        ModelAndView modelAndView = new ModelAndView();
        // 用户id
        WebViewUser user = WebUtils.getUser(request);
       
        if (user == null) {
            modelAndView = new ModelAndView(AutoDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "登录失效，请重新登陆");
            return modelAndView;
        }
        Users users=authService.getUsers(user.getUserId());

        if (users.getBankOpenAccount()==0) {// 未开户
            modelAndView = new ModelAndView(AutoDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "用户未开户！");
            return modelAndView;
        }
        // 判断用户是否设置过交易密码
        if (users.getIsSetPassword() == 0) {// 未设置交易密码
            modelAndView = new ModelAndView(AutoDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "用户未设置交易密码");
            return modelAndView;
        }
        HjhUserAuth hjhUserAuth=authService.getHjhUserAuthByUserId(user.getUserId());
        
        if(hjhUserAuth!=null&&hjhUserAuth.getAutoInvesStatus()==1){
            modelAndView = new ModelAndView(AutoDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "用户已授权,无需重复授权");
            return modelAndView;
        }
        
        int userId = user.getUserId();
        // 同步调用路径
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + AutoDefine.REQUEST_MAPPING
                + AutoDefine.USER_AUTH_INVES_RETURN_ACTION + ".do";
        // 异步调用路
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + AutoDefine.REQUEST_MAPPING
                + AutoDefine.USER_AUTH_INVES_BGRETURN_ACTION + ".do";
        String orderId=GetOrderIdUtils.getOrderId2(userId);
        String transactionUrl=PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+"/hjhplan/initPlanList.do";
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
        bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
        bean.setNotifyUrl(bgRetUrl);//异步通知地址
        bean.setRetUrl(retUrl);//同步通知地址
        bean.setForgotPwdUrl(CustomConstants.FORGET_PASSWORD_URL);//忘记密码通知地址
        bean.setVerifyOrderUrl(verifyOrderUrl);//订单有效性连接
        bean.setTransactionUrl(transactionUrl);
        bean.setAccountId(user.getBankAccount());// 电子账号
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
        
        // 商户私有域，存放开户平台,用户userId
        /*LogAcqResBean acqRes = new LogAcqResBean();
        acqRes.setUserId(userId);
        bean.setLogAcqResBean(acqRes);*/
        
        
        bean.setLogRemark("出借人自动投标签约");
        this.authService.insertUserAuthLog(userId, bean,0,BankCallConstant.QUERY_TYPE_1);
        // 跳转到汇付天下画面
        try {
            modelAndView = BankCallUtils.callApi(bean);
            LogUtil.endLog(AutoController.class.toString(), AutoDefine.USER_AUTH_INVES_ACTION);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView = new ModelAndView(AutoDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "调用银行接口失败！");
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
    @Deprecated
    @RequestMapping(AutoDefine.USER_AUTH_INVES_RETURN_ACTION)
    public ModelAndView userAuthInvesReturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {

        LogUtil.startLog(AutoDefine.THIS_CLASS, AutoDefine.USER_AUTH_INVES_RETURN_ACTION, "[用户授权自动出借同步回调开始]");
        ModelAndView modelAndView = new ModelAndView();
        bean.convert();
        Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
        HjhUserAuth hjhUserAuth = this.authService.getHjhUserAuthByUserId(userId);
        // 返回失败
        if (bean.getRetCode()!=null&&!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
            modelAndView = new ModelAndView(AutoDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "用户授权自动出借失败,失败原因：" + authService.getBankRetMsg(bean.getRetCode()));
            return modelAndView;
        }
        // 判断用户授权自动出借是否已成功
        if (hjhUserAuth!=null&&hjhUserAuth.getAutoInvesStatus()==1) {
            modelAndView = new ModelAndView(AutoDefine.USER_AUTH_SUCCESS_PATH);
            modelAndView.addObject("message", "用户授权自动出借成功");
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
                modelAndView = new ModelAndView(AutoDefine.USER_AUTH_SUCCESS_PATH);
                modelAndView.addObject("message", "用户授权自动出借成功");
                return modelAndView;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.errorLog(AutoDefine.THIS_CLASS, AutoDefine.USER_AUTH_INVES_RETURN_ACTION, e);
        }

        modelAndView = new ModelAndView(AutoDefine.USER_AUTH_ERROR_PATH);
        modelAndView.addObject("message", "用户授权自动出借失败");
        LogUtil.endLog(AutoDefine.THIS_CLASS, AutoDefine.USER_AUTH_INVES_RETURN_ACTION, "[用户授权自动出借完成后,回调结束]");
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
    @Deprecated
    @RequestMapping(AutoDefine.USER_AUTH_INVES_BGRETURN_ACTION)
    public String userAuthInvesBgreturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {
        BankCallResult result = new BankCallResult();
        LogUtil.startLog(AutoDefine.THIS_CLASS, AutoDefine.USER_AUTH_INVES_BGRETURN_ACTION, "[用户授权自动出借异步回调开始]");
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

}
