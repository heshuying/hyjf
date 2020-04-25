package com.hyjf.web.bank.web.user.repayauto;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.autoup.AutoPlusService;
import com.hyjf.bank.service.user.repayauth.RepayAuthService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.HjhUserAuthConfig;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.util.WebUtils;


/**
 * 
 * 还款授权
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年12月20日
 * @see 上午10:16:01
 */
@Controller
@RequestMapping(value = RepayAutoDefine.REQUEST_MAPPING)
public class RepayAutoController extends BaseController {

    Logger _log = LoggerFactory.getLogger(RepayAutoController.class);
    
    @Autowired
    private RepayAuthService repayAuthService;
    
    @Autowired
    private AutoPlusService autoPlusService;

    
    
    /**
     * 用户还款授权
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(RepayAutoDefine.USER_AUTH_INVES_ACTION)
    public ModelAndView userAuthInves(HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(RepayAutoDefine.THIS_CLASS, RepayAutoDefine.USER_AUTH_INVES_ACTION);
        ModelAndView modelAndView = new ModelAndView();
        
        
        // 用户id
        WebViewUser user = WebUtils.getUser(request);
        if (user == null) {
            modelAndView = new ModelAndView(RepayAutoDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "登录失效，请重新登陆");
            return modelAndView;
        }
        Users users=autoPlusService.getUsers(user.getUserId());
        if (users.getBankOpenAccount()==0) {// 未开户
            modelAndView = new ModelAndView(RepayAutoDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "用户未开户！");
            return modelAndView;
        }
        
        // 判断用户是否设置过交易密码
        if (users.getIsSetPassword() == 0) {// 未设置交易密码
            modelAndView = new ModelAndView(RepayAutoDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "用户未设置交易密码");
            return modelAndView;
        }
        
        // 判断是否授权过  todo
        if(autoPlusService.checkIsAuth(user.getUserId(),BankCallConstant.TXCODE_REPAY_AUTH_PAGE)){
            modelAndView = new ModelAndView(RepayAutoDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "用户已授权,无需重复授权");
            return modelAndView;
        }
       
        // 组装发往江西银行参数
        BankCallBean bean = getCommonBankCallBean(user);
        // 插入日志
        this.repayAuthService.insertUserAuthLog(user.getUserId(), bean,0);
        // 跳转到汇付天下画面
        try {
            modelAndView = BankCallUtils.callApi(bean);
            LogUtil.endLog(RepayAutoController.class.toString(), RepayAutoDefine.USER_AUTH_INVES_ACTION);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView = new ModelAndView(RepayAutoDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "调用银行接口失败！");

            LogUtil.errorLog(RepayAutoController.class.toString(), RepayAutoDefine.USER_AUTH_INVES_ACTION, e);
        }

        return modelAndView;
    }

    // 组装发往江西银行参数
    private BankCallBean getCommonBankCallBean(WebViewUser users) {
        
        String txcode = "";
        // 同步调用路径
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + 
                RepayAutoDefine.REQUEST_MAPPING + RepayAutoDefine.USER_AUTH_INVES_RETURN_ACTION + ".do";
        // 异步调用路
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + 
                RepayAutoDefine.REQUEST_MAPPING + RepayAutoDefine.USER_AUTH_INVES_BGRETURN_ACTION + ".do";
        // 构造函数已经设置
        // 版本号  交易代码  机构代码  银行代码  交易日期  交易时间  交易流水号   交易渠道
        BankCallBean bean = new BankCallBean(BankCallConstant.VERSION_10,txcode,users.getUserId(),"000002");
        
        HjhUserAuthConfig repayCconfig = CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_REPAYMENT_AUTH);
        bean.setTxCode(BankCallConstant.TXCODE_REPAY_AUTH_PAGE);
        bean.setAccountId(users.getBankAccount());// 电子账号
        bean.setMaxAmt(repayCconfig.getEnterpriseMaxAmount()+"");
        bean.setDeadline(GetDate.date2Str(GetDate.countDate(1,repayCconfig.getAuthPeriod()),new SimpleDateFormat("yyyyMMdd")));
        bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_REPAY_AUTH_PAGE);
        bean.setLogRemark("还款授权");
        
        bean.setForgotPwdUrl(CustomConstants.FORGET_PASSWORD_URL);//忘记密码通知地址
        bean.setRetUrl(retUrl);//同步通知地址
        bean.setNotifyUrl(bgRetUrl);//异步通知地址 
        bean.setLogRemark("用户还款授权");
        
        return bean;
    }

    /**
     * 用户还款授权同步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(RepayAutoDefine.USER_AUTH_INVES_RETURN_ACTION)
    public ModelAndView userAuthInvesReturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {

        LogUtil.startLog(RepayAutoDefine.THIS_CLASS, RepayAutoDefine.USER_AUTH_INVES_RETURN_ACTION, "[还款授权同步回调开始]");
        ModelAndView modelAndView = new ModelAndView();
        bean.convert();
        // 返回失败
        // 出借人签约状态查询
        _log.info("还款授权同步回调调用查询接口查询状态");
        BankCallBean retBean =
                autoPlusService.getTermsAuthQuery(Integer.parseInt(bean.getLogUserId()), BankCallConstant.CHANNEL_PC);
        _log.info("还款授权同步回调调用查询接口查询状态结束  结果为:" + (retBean == null ? "空" : retBean.getRetCode()));
        retBean.setOrderId(bean.getLogOrderId());
        bean=retBean;
        // 返回失败
        if (bean.getRetCode() != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())
                && "1".equals(bean.getRepayAuth())) {
            // 成功
            try {
                this.autoPlusService.updateUserAuth(Integer.parseInt(bean.getLogUserId()),retBean);
            } catch (Exception e) {
                // 判断是否授权过  todo
                if(autoPlusService.checkIsAuth(Integer.parseInt(bean.getLogUserId()),BankCallConstant.TXCODE_REPAY_AUTH_PAGE)){
                    modelAndView = new ModelAndView(RepayAutoDefine.USER_AUTH_SUCCESS_PATH);
                    modelAndView.addObject("message", "还款授权成功");
                    LogUtil.endLog(RepayAutoDefine.THIS_CLASS, RepayAutoDefine.USER_AUTH_INVES_RETURN_ACTION, "[还款授权完成后,回调结束]");
                    modelAndView.addObject("typeURL", "0");
                    return modelAndView;
                }
                _log.info("还款授权同步插入数据库失败，错误原因:" + e.getMessage());
                modelAndView = new ModelAndView(RepayAutoDefine.USER_AUTH_ERROR_PATH);
                modelAndView.addObject("message", "请联系客服!");
                return modelAndView;
            }

        } else {
            modelAndView = new ModelAndView(RepayAutoDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "请联系客服!");
            return modelAndView;
        }
        modelAndView = new ModelAndView(RepayAutoDefine.USER_AUTH_SUCCESS_PATH);
        modelAndView.addObject("message", "还款授权成功");
        LogUtil.endLog(RepayAutoDefine.THIS_CLASS, RepayAutoDefine.USER_AUTH_INVES_RETURN_ACTION, "[还款授权完成后,回调结束]");
        modelAndView.addObject("typeURL", "0");
        return modelAndView;
    }


    
    /**
     * 还款授权异步回调
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(RepayAutoDefine.USER_AUTH_INVES_BGRETURN_ACTION)
    public String userAuthInvesBgreturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {
        BankCallResult result = new BankCallResult();
        LogUtil.startLog(RepayAutoDefine.THIS_CLASS, RepayAutoDefine.USER_AUTH_INVES_BGRETURN_ACTION, "[还款授权异步回调开始]");
        bean.convert();
        Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
        // 查询用户开户状态
        Users user = this.autoPlusService.getUsers(userId);
        // 成功
        if (user != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE))) {
            try {
                bean.setOrderId(bean.getLogOrderId());
                // 更新签约状态和日志表
                this.autoPlusService.updateUserAuth(Integer.parseInt(bean.getLogUserId()),bean);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.errorLog(RepayAutoDefine.THIS_CLASS, RepayAutoDefine.USER_AUTH_INVES_BGRETURN_ACTION, e);
            }
        }
        LogUtil.endLog(RepayAutoDefine.THIS_CLASS, RepayAutoDefine.USER_AUTH_INVES_BGRETURN_ACTION, "[用户还款授权完成后,回调结束]");
        result.setMessage("还款授权成功");
        result.setStatus(true);
        return JSONObject.toJSONString(result, true);
    }
    
    
    
    
}
