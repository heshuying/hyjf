package com.hyjf.web.bank.web.user.bankopen.encryptpage;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.accountopenpage.OpenAccountPageBean;
import com.hyjf.bank.service.user.accountopenpage.UserOpenAccountPageService;
import com.hyjf.bank.service.user.bankopen.BankOpenService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.login.LoginService;
import com.hyjf.web.util.WebUtils;

/**
 * 
 * 开户+设置交易密码
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年9月5日
 * @see 上午11:30:40
 */
@Controller(BankOpenEncryptPageDefine.CONTROLLER_NAME)
@RequestMapping(value = BankOpenEncryptPageDefine.REQUEST_MAPPING)
public class BankOpenEncryptPageController extends BaseController {
    Logger _log = LoggerFactory.getLogger(BankOpenEncryptPageController.class);

    @Autowired
    private BankOpenService openAccountService;

    @Autowired
    private UserOpenAccountPageService accountPageService;

    @Autowired
    private LoginService loginService;

    /** 当前controller名称 */
    public static final String THIS_CLASS = BankOpenEncryptPageController.class.getName();

    /**
     * 用户开户
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(BankOpenEncryptPageDefine.BANKOPEN_INIT_ACTION)
    public ModelAndView initOpenAccount(HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(THIS_CLASS, BankOpenEncryptPageDefine.BANKOPEN_INIT_ACTION);
        ModelAndView modelAndView = new ModelAndView(BankOpenEncryptPageDefine.BANKOPEN_INIT_PATH);
        WebViewUser user = WebUtils.getUser(request);
        int userId = user.getUserId();
        boolean accountFlag = user.isBankOpenAccount();
        if (accountFlag) {
            modelAndView = new ModelAndView("redirect:/user/pandect/pandect.do");
            return modelAndView;
        }
        String logOrderId = GetOrderIdUtils.getOrderId2(userId);
        modelAndView.addObject("logOrderId", logOrderId);
        modelAndView.addObject("mobile", user.getMobile());
        return modelAndView;
    }

    // 开户
    @RequestMapping(method = RequestMethod.POST, value = BankOpenEncryptPageDefine.BANKOPEN_OPEN_ACTION)
    public ModelAndView openAccount(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute BankOpenEncryptPageBean accountBean) {
        _log.info("web请求页面开户，参数为：" + JSONObject.toJSONString(accountBean));
        ModelAndView modelAndView = new ModelAndView(BankOpenEncryptPageDefine.BANKOPEN_ERROR_PATH);
        try {
            // 获取登陆用户userId
            Integer userId = WebUtils.getUserId(request);
            // 验证请求参数
            if (Validator.isNull(userId)) {
                modelAndView.addObject("message", "用户未登陆，请先登陆！");
                return modelAndView;
            }
            Users user = this.openAccountService.getUsers(userId);
            if (Validator.isNull(user)) {
                modelAndView.addObject("message", "获取用户信息失败！");
                return modelAndView;
            }

            // 检查提交的参数是否正确
            Map<String, String> result =
                    accountPageService.checkParm(userId, accountBean.getMobile(), accountBean.getTrueName());
            // 失败
            if ("0".equals(result.get("success"))) {
                modelAndView.addObject("message", result.get("message"));
                return modelAndView;
            }
            // 拼装参数 调用江西银行
            // 同步调用路径
            String retUrl =
                    PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + BankOpenEncryptPageDefine.REQUEST_MAPPING
                            + BankOpenEncryptPageDefine.RETURL_SYN_ACTION + ".do?phone=" + accountBean.getMobile()+"&uid="+userId;
            // 异步调用路
            String bgRetUrl =
                    PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + BankOpenEncryptPageDefine.REQUEST_MAPPING
                            + BankOpenEncryptPageDefine.RETURL_ASY_ACTION + ".do?phone=" + accountBean.getMobile();

            OpenAccountPageBean openBean = new OpenAccountPageBean();
            PropertyUtils.copyProperties(openBean, accountBean);
            openBean.setChannel(BankCallConstant.CHANNEL_PC);
            openBean.setUserId(userId);
            openBean.setIp(CustomUtil.getIpAddr(request));
            // 同步 异步
            openBean.setRetUrl(retUrl);
            openBean.setNotifyUrl(bgRetUrl);
            openBean.setCoinstName("汇盈金服");
            openBean.setPlatform("0");
            // 账户用途 写死
            /*
             * 00000-普通账户
             * 10000-红包账户（只能有一个）
             * 01000-手续费账户（只能有一个）
             * 00100-担保账户
             */
            openBean.setAcctUse("00000");
            /**
             * 1：出借角色
             * 2：借款角色
             * 3：代偿角色
             */
            openBean.setIdentity("1");
            modelAndView = accountPageService.getCallbankEncryptPageMV(openBean);
            // 保存开户日志
            boolean isUpdateFlag = this.openAccountService.updateUserAccountLog(userId, user.getUsername(),
                    openBean.getMobile(), openBean.getOrderId(), CustomConstants.CLIENT_PC, openBean.getTrueName(),
                    openBean.getIdNo(), openBean.getCardNo());
            if (!isUpdateFlag) {
                _log.info("保存开户日志失败,手机号:[" + openBean.getMobile() + "],用户ID:[" + userId + "]");
                modelAndView.addObject("message", "操作失败！");
                return modelAndView;
            }
            _log.info("开户end");
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            _log.info("开户异常,异常信息:[" + e.toString() + "]");
            modelAndView.addObject("message", "开户异常！");
            return modelAndView;
        }
    }

    /**
     * 
     * 同步回调
     * @author sunss
     * @param request
     * @param response
     * @param bean
     * @return
     */
    @RequestMapping(BankOpenEncryptPageDefine.RETURL_SYN_ACTION)
    public ModelAndView bankOpenReturn(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute BankCallBean bean) {
        ModelAndView modelAndView = new ModelAndView(BankOpenEncryptPageDefine.BANKOPEN_ERROR_PATH);
        String isSuccess = request.getParameter("isSuccess");
        String uid = request.getParameter("uid");
        Integer userId = WebUtils.getUserId(request);
        try{
            if(userId==null && (uid!=null && !"null".equals(uid))){
                userId = Integer.parseInt(uid);
            }
        }catch (Exception e){
        }
        if(userId==null ){
            // 跳转到登录
            modelAndView = new ModelAndView("redirect:" + CustomConstants.HOST + "/user/login/init.do");
            return modelAndView;
        }
        bean.convert();
        _log.info("用户开户  同步  userId:{}",userId);
        // 用户id
        WebViewUser user = loginService.getWebViewUserByUserId(userId);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            LogUtil.errorLog(BankOpenEncryptPageDefine.RETURL_SYN_ACTION, "开户异步可能比同步先到", e);
        }
        if ("1".equals(isSuccess)) {
            // 成功
            WebViewUser webUser = loginService.getWebViewUserByUserId(userId);
            // 用户id
            UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
            userOperationLogEntity.setOperationType(6);
            userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
            userOperationLogEntity.setPlatform(0);
            userOperationLogEntity.setRemark("");
            userOperationLogEntity.setOperationTime(new Date());
            userOperationLogEntity.setUserName(user.getUsername());
            userOperationLogEntity.setUserRole(user.getRoleId());
            loginService.sendUserLogMQ(userOperationLogEntity);
            WebUtils.sessionLogin(request, response, webUser);
            modelAndView = new ModelAndView(BankOpenEncryptPageDefine.BANKOPEN_ERROR_SUCCESS);
            modelAndView.addObject("message", "开户成功");
            _log.info("开户成功,用户ID:[" + userId + "]");
            return modelAndView;
        }
        // 开户失败情况下 查询异步结果 区分是不是设置交易密码失败了
        WebViewUser webUser = loginService.getWebViewUserByUserId(userId);
        // 如果已经开户 未设置交易密码 重定向到设置交易密码上
        if (webUser.isBankOpenAccount() && webUser.getIsSetPassword() == 0) {
            _log.info("开户成功,用户ID:[" + userId + "],设置交易密码失败，引导用户设置交易密码");
            modelAndView = new ModelAndView(BankOpenEncryptPageDefine.BANKOPEN_ERROR_SET_PASSWORD);
            return modelAndView;
        }
        _log.info("开户失败,用户ID:[" + userId + "]");
        modelAndView = new ModelAndView(BankOpenEncryptPageDefine.BANKOPEN_ERROR_PATH);
        return modelAndView;
    }

    /**
     * 异步回调
     */
    @ResponseBody
    @RequestMapping(BankOpenEncryptPageDefine.RETURL_ASY_ACTION)
    public BankCallResult bankOpenBgreturn(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute BankCallBean bean) {
        // 上送的异步地址里面有
        BankCallResult result = new BankCallResult();
        String phone = request.getParameter("phone");
        String roleId = request.getParameter("roleId");
        bean.setIdentity(roleId);
        _log.info("页面开户异步回调start getStatus:{} retCode:{} ", bean.getStatus() , bean.getRetCode());
        String openclient = request.getParameter("openclient");
        bean.setLogClient(Integer.parseInt(openclient));
        bean.setMobile(phone);
        bean.convert();
        int userId = Integer.parseInt(bean.getLogUserId());
        // 银行返回响应代码
        String retCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
        // 开户失败
        // State为0时候为0：交易失败 1：交易成功 2：开户成功设置交易密码失败
        if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode) || "0".equals(bean.getStatus())) {
            // 将开户记录状态改为4
            this.accountPageService.updateUserAccountLog(userId, bean.getLogOrderId(), 4);
            // 根据银行相应代码,查询错误信息
            String retMsg = accountPageService.getBankRetMsg(retCode);
            _log.info("开户失败,银行返回响应代码:[" + retCode + "],retMsg:[" + retMsg + "],订单号:[" + bean.getLogOrderId() + "].");
            result.setStatus(false);
            return result;
        }
        // 开户成功后,保存用户的开户信息
        boolean saveBankAccountFlag = this.accountPageService.updateUserAccount(bean);
        if (!saveBankAccountFlag) {
            _log.info("开户失败,保存用户的开户信息失败:[" + retCode + "],订单号:[" + bean.getLogOrderId() + "].");
            result.setStatus(false);
            return result;
        }
        // 保存银行卡信息
        boolean saveBankCardFlag = this.accountPageService.updateCardNoToBank(bean);
        if (!saveBankCardFlag) {
            _log.info("开户失败,保存银行卡信息失败:[" + retCode + "],订单号:[" + bean.getLogOrderId() + "].");
            result.setStatus(false);
            return result;
        }
        // 开户成功后,发送CA认证MQ
        this.openAccountService.sendCAMQ(String.valueOf(userId));
        CommonSoaUtils.listOpenAcc(userId);
        _log.info("开户成功,用户ID:[" + userId + ",用户电子账户号:[" + bean.getAccountId() + "]");
        result.setStatus(true);
        return result;
    }

}
