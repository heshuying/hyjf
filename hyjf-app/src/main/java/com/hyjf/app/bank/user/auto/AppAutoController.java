/**
 * 个人设置控制器
 */
package com.hyjf.app.bank.user.auto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseMapBean;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.bank.service.user.auto.AutoService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

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
public class AppAutoController extends BaseController {

    @Autowired
    private AutoService authService;
    
    /**
     * 用户授权自动出借
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AutoDefine.USER_AUTH_INVES_ACTION)
    public ModelAndView setPassword(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            LogUtil.startLog(AutoDefine.THIS_CLASS, AutoDefine.USER_AUTH_INVES_ACTION);
         // 检查参数
            JSONObject checkResult = checkParam(request);
            if (checkResult != null) {
                modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
                BaseMapBean baseMapBean=new BaseMapBean();
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, "非法参数！");
                baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
                modelAndView.addObject("callBackForm", baseMapBean);
                return modelAndView;
            }
            // 唯一标识
            String sign = request.getParameter("sign");
            // token
            String token = request.getParameter("token");
            Integer userId = SecretUtil.getUserId(sign);
            if (userId == null || userId == 0) {
                modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
                BaseMapBean baseMapBean=new BaseMapBean();
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未登录");
                baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
                modelAndView.addObject("callBackForm", baseMapBean);
                return modelAndView;
            }
            // 取得用户在江西银行的客户号
            BankOpenAccount accountChinapnrTender = authService.getBankOpenAccount(userId);
            if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getAccount())) {
                modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
                BaseMapBean baseMapBean=new BaseMapBean();
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未开户");
                baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
                modelAndView.addObject("callBackForm", baseMapBean);
                return modelAndView;
            }
            Users users = authService.getUsers(userId);
            // 判断用户是否设置过交易密码
            if (users.getIsSetPassword() == 0) {// 未设置交易密码
                modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
                BaseMapBean baseMapBean=new BaseMapBean();
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未设置交易密码");
                baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
                modelAndView.addObject("callBackForm", baseMapBean);
                return modelAndView;
            }
            HjhUserAuth hjhUserAuth=authService.getHjhUserAuthByUserId(userId);
            
            if(hjhUserAuth!=null&&hjhUserAuth.getAutoInvesStatus()==1&hjhUserAuth.getAutoCreditStatus()==1){
                modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
                BaseMapBean baseMapBean=new BaseMapBean();
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户已授权,无需重复授权");
                baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
                modelAndView.addObject("callBackForm", baseMapBean);
                return modelAndView;
            }
            
            // 同步调用路径
            String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + AutoDefine.REQUEST_MAPPING
                    + AutoDefine.USER_AUTH_INVES_RETURN_ACTION + ".do";
            // 异步调用路
            String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + AutoDefine.REQUEST_MAPPING
                    + AutoDefine.USER_AUTH_INVES_BGRETURN_ACTION + ".do";
            String orderId=GetOrderIdUtils.getOrderId2(userId);
            String transactionUrl=PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + AutoDefine.REQUEST_MAPPING
                    + AutoDefine.TRANSACTION_URL_ACTION + ".do";
            String forgetPassworedUrl = CustomConstants.FORGET_PASSWORD_URL + "?sign=" + sign + "&token=" + token;
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
            bean.setChannel(BankCallConstant.CHANNEL_APP);// 交易渠道
            bean.setNotifyUrl(bgRetUrl);//异步通知地址
            bean.setRetUrl(retUrl);//同步通知地址
            bean.setForgotPwdUrl(forgetPassworedUrl);//忘记密码通知地址
            bean.setVerifyOrderUrl(verifyOrderUrl);//订单有效性连接
            bean.setTransactionUrl(transactionUrl);
            bean.setAccountId(accountChinapnrTender.getAccount());// 电子账号
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
            this.authService.insertUserAuthLog(userId, bean,0,BankCallConstant.QUERY_TYPE_1);
            // 跳转到汇付天下画面
            try {
                modelAndView = BankCallUtils.callApi(bean);
                LogUtil.endLog(AppAutoController.class.toString(), AutoDefine.USER_AUTH_INVES_ACTION);
            } catch (Exception e) {
                e.printStackTrace();
                modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
                BaseMapBean baseMapBean=new BaseMapBean();
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, "调用银行接口失败！");
                baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
                modelAndView.addObject("callBackForm", baseMapBean);
                LogUtil.errorLog(AppAutoController.class.toString(), AutoDefine.USER_AUTH_INVES_ACTION, e);
            }
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
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
    @RequestMapping(AutoDefine.USER_AUTH_INVES_RETURN_ACTION)
    public ModelAndView userAuthInvesReturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {

        LogUtil.startLog(AutoDefine.THIS_CLASS, AutoDefine.USER_AUTH_INVES_RETURN_ACTION, "[用户授权自动出借同步回调开始]");
        ModelAndView modelAndView = new ModelAndView();
        bean.convert();
        Integer userId=0;
        try {
            userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
        } catch (Exception e) {
            modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, BaseResultBeanFrontEnd.SUCCESS_MSG);
            baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_HANDING_PATH);
            modelAndView.addObject("callBackForm", bean);
            return modelAndView;
        }
        
        HjhUserAuth hjhUserAuth = this.authService.getHjhUserAuthByUserId(userId);
        // 返回失败
        if (bean.getRetCode()!=null&&!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
            modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set("message","用户授权自动出借失败,失败原因：" + authService.getBankRetMsg(bean.getRetCode()));
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, BaseResultBeanFrontEnd.SUCCESS_MSG);
            baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
            modelAndView.addObject("callBackForm", bean);
            return modelAndView;
        }
        // 判断用户授权自动出借是否已成功
        if (hjhUserAuth!=null&&hjhUserAuth.getAutoInvesStatus()==1) {
            modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set("message","用户授权自动出借成功");
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, BaseResultBeanFrontEnd.SUCCESS_MSG);
            baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_SUCCESS_PATH);
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
        LogUtil.endLog(AppAutoController.class.toString(), AutoDefine.USER_AUTH_INVES_RETURN_ACTION);
        
        try {
            if("1".equals(retBean.getState())){
                // 更新签约状态和日志表
                modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
                BaseMapBean baseMapBean=new BaseMapBean();
                baseMapBean.set("message","用户授权自动出借成功");
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, BaseResultBeanFrontEnd.SUCCESS_MSG);
                baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_SUCCESS_PATH);
                modelAndView.addObject("callBackForm", baseMapBean);
                return modelAndView;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.errorLog(AutoDefine.THIS_CLASS, AutoDefine.USER_AUTH_INVES_RETURN_ACTION, e);
        }

        modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
        BaseMapBean baseMapBean=new BaseMapBean();
        baseMapBean.set("message","用户授权自动出借失败");
        baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, BaseResultBeanFrontEnd.SUCCESS_MSG);
        baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
        modelAndView.addObject("callBackForm", baseMapBean);
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
                LogUtil.endLog(AppAutoController.class.toString(), AutoDefine.USER_AUTH_INVES_RETURN_ACTION);
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
     * 用户授权自动出借同步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(AutoDefine.TRANSACTION_URL_ACTION)
    public ModelAndView transactionUrl(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
        BaseMapBean baseMapBean=new BaseMapBean();
        baseMapBean.set("message","用户授权自动出借失败,请重试授权");
        baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, BaseResultBeanFrontEnd.SUCCESS_MSG);
        baseMapBean.setCallBackAction(CustomConstants.HOST+AutoDefine.JUMP_HTML_ERROR_PATH);
        return modelAndView;
    }
    
    
    /**
     * 根据用户Id获取用户授权自动出借状态
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(AutoDefine.GET_USER_AUTO_STATUS_BY_USERID_ACTION)
    public JSONObject getUserAutoStatusByUserId(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(AutoDefine.THIS_CLASS, AutoDefine.GET_USER_AUTO_STATUS_BY_USERID_ACTION);
        JSONObject info = new JSONObject();
        
        
        JSONObject checkResult = checkParam(request);
        if (checkResult != null) {
            info.put("status", "1");
            info.put("message", checkResult.get(CustomConstants.APP_STATUS_DESC));
            return info;
        }
        
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
        
        LogUtil.endLog(AutoDefine.THIS_CLASS, AutoDefine.GET_USER_AUTO_STATUS_BY_USERID_ACTION);
        return info;
    }
    
    
    
    /**
     * 检查参数的正确性
     *
     * @param transAmt
     * @param openBankId
     * @param rechargeType
     * @return
     */
    private JSONObject checkParam(HttpServletRequest request) {

        //String version = request.getParameter("version");
        // 网络状态
        // String netStatus = request.getParameter("netStatus");
        // 平台
        String platform = request.getParameter("platform");
        // token
        String token = request.getParameter("token");
        // 唯一标识
        String sign = request.getParameter("sign");
        // 随机字符串
        String randomString = request.getParameter("randomString");
        // Order
        String order = request.getParameter("order");

        // 检查参数正确性  || Validator.isNull(netStatus)
        if (Validator.isNull(platform) || Validator.isNull(token) || Validator.isNull(sign) || Validator.isNull(randomString)
                || Validator.isNull(order)) {
            return jsonMessage("请求参数非法", "1");
        }

        // 取得加密用的Key
        String key = SecretUtil.getKey(sign);
        if (Validator.isNull(key)) {
            return jsonMessage("请求参数非法", "1");
        }
        Integer userId = SecretUtil.getUserId(sign);
        if (userId == null) {
            return jsonMessage("用户信息不存在", "1");
        }
        return null;
    }
    
    
}
