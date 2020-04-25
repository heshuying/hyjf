package com.hyjf.api.surong.user.transpassword;

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

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.surong.user.recharge.RdfRechargeService;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.user.transpassword.TransPasswordService;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.vip.apply.ApplyDefine;

/**
 * 融东风 修改交易密码接口
 * @author LXC
 *
 */
@Controller
@RequestMapping(TransPasswordDefine.REQUEST_MAPPING)
public class TransPasswordServer extends BaseController {
    Logger _log = LoggerFactory.getLogger("融东风修改交易密码Controller");

    @Autowired
    private TransPasswordService transPasswordService;

    @Autowired
    private RdfRechargeService rdfRechargeService;

    /**
     * 设置交易密码
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(TransPasswordDefine.SETPASSWORD_ACTION)
    public ModelAndView setPassword(HttpServletRequest request, HttpServletResponse response) {
        _log.info("设置交易密码 start");
        ModelAndView modelAndView = new ModelAndView();
        String sign = request.getParameter("sign");
        if (StringUtils.isEmpty(sign)) {
            modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "sign验证值为空！");
            return modelAndView;
        }
        String mobile = request.getParameter("mobile"); // 用户手机
        String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);
        String miwen = MD5.toMD5Code(accessKey + mobile + accessKey);
        if (!miwen.equals(sign)) {
            modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "sign值验证失败！");
            return modelAndView;
        }
        Users user = rdfRechargeService.findUserByMobile(mobile);
        if (user == null) {
            modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", mobile + "用户不存在汇盈金服账户！");
            return modelAndView;
        }
        Integer userId = user.getUserId();
        String message = "";
        JSONObject checkResult;
        if (user.getBankOpenAccount().intValue() != 1) {// 未开户
            modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "用户未开户！");
            return modelAndView;
        }

        // 判断用户是否设置过交易密码

        Integer passwordFlag = user.getIsSetPassword();
        if (passwordFlag == 1) {// 已设置交易密码
            modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "已设置交易密码");
            return modelAndView;
        }

        UsersInfo usersInfo = transPasswordService.getUsersInfoByUserId(userId);
        BankOpenAccount bankOpenAccount = transPasswordService.getBankOpenAccount(userId);
        // 同步调用路径
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
                + TransPasswordDefine.REQUEST_MAPPING + TransPasswordDefine.RETURL_SYN_PASSWORD_ACTION + ".do";
        // 异步调用路
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
                + TransPasswordDefine.REQUEST_MAPPING + TransPasswordDefine.RETURN_ASY_PASSWORD_ACTION + ".do";
        // 调用设置密码接口
        BankCallBean bean = new BankCallBean();
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxCode(BankCallConstant.TXCODE_PASSWORD_RESET_PAGE);// 消息类型(用户开户)
        bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
        bean.setTxDate(GetOrderIdUtils.getTxDate());
        bean.setTxTime(GetOrderIdUtils.getTxTime());
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
        bean.setChannel(BankCallConstant.CHANNEL_APP);
        bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);
        bean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));// 电子账号
        bean.setIdNo(usersInfo.getIdcard());
        bean.setName(usersInfo.getTruename());
        bean.setMobile(user.getMobile());

        bean.setRetUrl(retUrl);// 页面同步返回 URL
        bean.setNotifyUrl(bgRetUrl);// 页面异步返回URL(必须)
        // 商户私有域，存放开户平台,用户userId
        LogAcqResBean acqRes = new LogAcqResBean();
        acqRes.setUserId(userId);
        bean.setLogAcqResBean(acqRes);
        // 操作者ID
        bean.setLogUserId(String.valueOf(userId));
        bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_PASSWORDRESETPAGE);
        bean.setLogOrderId(GetOrderIdUtils.getUsrId(userId));
        // 跳转到汇付天下画面
        try {
            modelAndView = BankCallUtils.callApi(bean);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "调用银行接口失败！");
        }
        _log.info("设置交易密码end");
        return modelAndView;
    }

    /**
     * 设置交易密码同步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(TransPasswordDefine.RETURL_SYN_PASSWORD_ACTION)
    public ModelAndView passwordReturn(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute BankCallBean bean) {
        _log.info("设置交易密码同步回调start");
        ModelAndView modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
        bean.convert();
        RetranspasswordResultBean repwdResult = new RetranspasswordResultBean();
        repwdResult.setCallBackAction(PropUtils.getSystem("wcsr.retranspassword"));
        String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);
        LogAcqResBean acqes = bean.getLogAcqResBean();
        int userId = acqes.getUserId();
        Users user = this.transPasswordService.getUsers(userId);
        String miwen = MD5.toMD5Code(accessKey + user.getMobile() + accessKey);
        repwdResult.set("mobile", user.getMobile());
        BankOpenAccount bankOpenAccount = transPasswordService.getBankOpenAccount(userId);
        // 调用查询电子账户密码是否设置
        BankCallBean selectbean = new BankCallBean();
        selectbean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        selectbean.setTxCode(BankCallConstant.TXCODE_PASSWORD_SET_QUERY);
        selectbean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        selectbean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
        selectbean.setTxDate(GetOrderIdUtils.getTxDate());
        selectbean.setTxTime(GetOrderIdUtils.getTxTime());
        selectbean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
        selectbean.setChannel(BankCallConstant.CHANNEL_PC);
        selectbean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));// 电子账号

        // 操作者ID
        selectbean.setLogUserId(String.valueOf(userId));
        selectbean.setLogOrderId(GetOrderIdUtils.getUsrId(userId));
        selectbean.setLogClient(0);
        // 返回参数
        BankCallBean retBean = null;
        // 调用接口
        retBean = BankCallUtils.callApiBg(selectbean);
        if ("1".equals(retBean.getPinFlag())) {
            // 是否设置密码中间状态
            this.transPasswordService.updateUserIsSetPassword(user, 1);
            repwdResult.set("status", "1");
            repwdResult.set("sign", miwen);
        } else {
            // 充值失败
            repwdResult.set("sign", miwen);
            modelAndView.addObject("message", "交易密码设置失败,失败原因：" + transPasswordService.getBankRetMsg(bean.getRetCode()));
            repwdResult.set("status", "0");
        }
        repwdResult.set("way", "同步");
        modelAndView.addObject("callBackForm", repwdResult);
        _log.info("设置交易密码同步回调end");
        return modelAndView;
    }

    /**
     * 设置交易密码异步回调
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(TransPasswordDefine.RETURN_ASY_PASSWORD_ACTION)
    public Object passwordBgreturn(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute BankCallBean bean) {
        _log.info("设置交易密码异步回调start");
        BankCallResult result = new BankCallResult();   
        bean.convert();
        LogAcqResBean acqes = bean.getLogAcqResBean();
        int userId = acqes.getUserId();
        // 查询用户开户状态
        Users user = this.transPasswordService.getUsers(userId);
        // 成功或审核中
        if (user != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE))) {
            try {
                // 开户后保存相应的数据以及日志
                this.transPasswordService.updateUserIsSetPassword(user, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        _log.info("设置交易密码异步回调end");
        result.setMessage("交易密码设置成功");
        result.setStatus(true);
        return result;
    }
    /**
     * 修改交易密码
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(TransPasswordDefine.RESETPASSWORD_ACTION)
    public ModelAndView resetPassword(HttpServletRequest request, HttpServletResponse response) {
        _log.info("修改交易密码 start");
        ModelAndView modelAndView = new ModelAndView();
        String sign = request.getParameter("sign");
        if (StringUtils.isEmpty(sign)) {
            modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "sign验证值为空！");
            return modelAndView;
        }
        //获取miwen
        String miwen = request.getParameter("miwen");
        String mobile = request.getParameter("mobile");
        Users user = rdfRechargeService.findUserByMobile(mobile);
        Integer userId = user.getUserId();
//        Integer userId = 22401107;
        if(userId==null||userId<=0){
            modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "用户未登录");
            return modelAndView; 
        }
        if (user.getBankOpenAccount().intValue() != 1) {//未开户
            modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "用户未开户！");
            return modelAndView;
        }
        //验证sign值
        String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);
        String miwenNew = MD5.toMD5Code(accessKey + mobile + accessKey);
        if (!miwen.equals(miwenNew)) {
            modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "sign值验证失败！");
            return modelAndView;
        }
        //判断用户是否设置过交易密码
        
        Integer passwordFlag = user.getIsSetPassword();
        if (passwordFlag == 0) {//未设置交易密码
            modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "未设置过交易密码，请先设置交易密码");
            return modelAndView;
        }
        BankOpenAccount bankOpenAccount = transPasswordService.getBankOpenAccount(userId);
        UsersInfo usersInfo=transPasswordService.getUsersInfoByUserId(userId);
        // 同步调用路径
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() +  TransPasswordDefine.REQUEST_MAPPING
                + TransPasswordDefine.RETURL_SYN_RESETPASSWORD_ACTION + ".do";
        // 异步调用路
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() +  TransPasswordDefine.REQUEST_MAPPING
                + TransPasswordDefine.RETURN_ASY_RESETPASSWORD_ACTION + ".do";
        
        // 调用设置密码接口
        BankCallBean bean = new BankCallBean();
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxCode(BankCallConstant.TXCODE_PASSWORD_RESET_PAGE);// 消息类型(用户开户)
        bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
        bean.setTxDate(GetOrderIdUtils.getTxDate());
        bean.setTxTime(GetOrderIdUtils.getTxTime());
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
        bean.setChannel(BankCallConstant.CHANNEL_APP);
        bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);
        bean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));//电子账号
        bean.setIdNo(usersInfo.getIdcard());
        bean.setName(usersInfo.getTruename());
        bean.setMobile(user.getMobile());
        bean.setNotifyUrl(bgRetUrl);// 页面异步返回URL(必须)
        // 商户私有域，存放开户平台,用户userId
        LogAcqResBean acqRes = new LogAcqResBean();
        acqRes.setUserId(userId);
        bean.setLogAcqResBean(acqRes);
        // 操作者ID
        bean.setLogUserId(String.valueOf(userId));
        bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_PASSWORDRESETPAGE);
        bean.setLogOrderId(GetOrderIdUtils.getUsrId(userId));
        bean.setRetUrl(retUrl + "?ordid=" + bean.getLogOrderId());// 页面同步返回 URL
        // 跳转到汇付天下画面
        try {
            modelAndView = BankCallUtils.callApi(bean);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "调用银行接口失败！");
        }
        _log.info("修改交易密码 end");
        return modelAndView;
    }
    /**
     * 修改交易密码同步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(TransPasswordDefine.RETURL_SYN_RESETPASSWORD_ACTION)
    public ModelAndView resetPasswordReturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {
        _log.info("设置交易密码同步回调start");
        ModelAndView modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
        bean.convert();
        RetranspasswordResultBean repwdResult = new RetranspasswordResultBean();
        repwdResult.setCallBackAction(PropUtils.getSystem("wcsr.resetpassword"));
        String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);
        LogAcqResBean acqes = bean.getLogAcqResBean();
        int userId = acqes.getUserId();
        Users user = this.transPasswordService.getUsers(userId);
        String miwen = MD5.toMD5Code(accessKey + user.getMobile() + accessKey);
        repwdResult.set("mobile", user.getMobile());
        //add by cwyang 防止同步比异步快
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String ordid = request.getParameter("ordid");
        boolean backIsSuccess = transPasswordService.backLogIsSuccess(ordid);
        // 返回失败
        if (bean.getRetCode()!=null&&!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode()) || !backIsSuccess) {
            modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "交易密码修改失败,失败原因：" + transPasswordService.getBankRetMsg(bean.getRetCode()));
            return modelAndView;
        }
        repwdResult.set("status", "1");
        repwdResult.set("sign", miwen);
        modelAndView.addObject("callBackForm", repwdResult);
        _log.info("设置交易密码同步回调end");
        return modelAndView;
    }
    /**
     * 修改交易密码异步回调
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(TransPasswordDefine.RETURN_ASY_RESETPASSWORD_ACTION)
    public Object resetPasswordBgreturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {
        BankCallResult result = new BankCallResult();
        result.setMessage("交易密码修改成功");
        result.setStatus(true);
        return result;
    }
    public static void main(String[] args) {
        String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);
        String miwen = MD5.toMD5Code(accessKey + "13600000010" + accessKey);
        System.out.println(miwen);
    }
}
