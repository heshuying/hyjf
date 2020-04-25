package com.hyjf.app.bank.user.openaccount.encryptpage;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.app.user.manage.AppUserService;
import com.hyjf.common.util.*;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.model.auto.UsersInfo;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseMapBean;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.bank.user.auto.AutoDefine;
import com.hyjf.app.bank.user.openaccount.OpenAccountBean;
import com.hyjf.app.bank.user.transpassword.TransPasswordController;
import com.hyjf.app.bank.user.transpassword.TransPasswordDefine;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.app.util.SignValue;
import com.hyjf.bank.service.user.accountopenpage.OpenAccountPageBean;
import com.hyjf.bank.service.user.accountopenpage.UserOpenAccountPageService;
import com.hyjf.bank.service.user.bankopen.BankOpenService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.soa.apiweb.CommonSoaUtils;

/**
 * 
 * 开户+设置交易密码
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年9月6日
 * @see 下午3:11:55
 */
@Controller
@RequestMapping(value = BankOpenEncryptPageDefine.REQUEST_MAPPING)
public class BankOpenEncryptPageController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(BankOpenEncryptPageController.class);

    @Autowired
    private BankOpenService openAccountService;

    @Autowired
    private UserOpenAccountPageService accountPageService;

    @Autowired
    private AppUserService appUserService;

    public static final String THIS_CLASS = BankOpenEncryptPageController.class.getName();

    /**
     * 开户画面
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = BankOpenEncryptPageDefine.BANKOPEN_OPEN_ACTION, method = RequestMethod.GET)
    @ResponseBody
    public JSONObject openAccountInit(HttpServletRequest request, @ModelAttribute() OpenAccountBean form) {
        LogUtil.startLog(THIS_CLASS, BankOpenEncryptPageDefine.BANKOPEN_OPEN_ACTION);
        JSONObject ret = new JSONObject();
        ret.put("status", "000");
        ret.put("statusDesc", "请求成功");
        // 获取登陆用户userId
        String sign = request.getParameter("sign");
        if (Validator.isNull(sign)) {
            ret.put("status", "99");
            ret.put("statusDesc", "用户未登录，请先登录！");
            return ret;
        }
        Integer userId = SecretUtil.getUserId(sign); // 用户ID
        ret.put("userId", userId);
        if (Validator.isNull(userId)) {
            ret.put("status", "99");
            ret.put("statusDesc", "用户未登录，请先登录！");
            return ret;
        } else {
            String mobile = "";
            try {
                mobile = this.openAccountService.getUsersMobile(userId);
            } catch (Exception e) {
                mobile = "";
            }
            if (StringUtils.isBlank(mobile)) {
                mobile = "";
            }
            ret.put(BankOpenEncryptPageDefine.MOBILE, mobile);
        }
        LogUtil.endLog(THIS_CLASS, BankOpenEncryptPageDefine.BANKOPEN_OPEN_ACTION);
        return ret;
    }

    /**
     * 江西银行开户协议
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BankOpenEncryptPageDefine.JX_BANK_SERVICE_ACTION)
    public ModelAndView jxBankService(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(BankOpenEncryptPageDefine.JX_BANK_SERVICE_PATH);
        String userIdStr = request.getParameter("userId");
        String userName = null;
        if (!StringUtils.isBlank(userIdStr)) {
            Integer userId = Integer.parseInt(userIdStr);
            Users users = openAccountService.getUsersByUserId(userId);
            if (users != null) {
                userName = users.getUsername();
            }
        }
        modelAndView.addObject("userName", userName);
        return modelAndView;
    }

    /**
     * 
     * 页面开户请求地址
     * @author sunss
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(BankOpenEncryptPageDefine.BANKOPEN_OPEN_ACCOUNT_ACTION)
    public ModelAndView openAccount(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
        BaseMapBean baseMapBean = new BaseMapBean();
        String sign = request.getParameter("sign");
        // 开户需要的参数
        String name = request.getParameter("name");
        String mobile = request.getParameter("phone");
        String platform = request.getParameter("realPlatform");
        if(StringUtils.isBlank(platform)){
             platform = request.getParameter("platform");
        }
        // 获取sign缓存
        String value = RedisUtils.get(sign);
        SignValue signValue = JSON.parseObject(value, SignValue.class);
        String token = signValue.getToken();
        if (token == null) {
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未登录！");
            baseMapBean.setCallBackAction(CustomConstants.HOST + BankOpenEncryptPageDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        // 判断用户是否登录
        Integer userId = SecretUtil.getUserId(sign);
        if (userId == null || userId <= 0) {
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未登录！");
            baseMapBean.setCallBackAction(CustomConstants.HOST + BankOpenEncryptPageDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        // 检查提交的参数是否正确
        Map<String, String> result = accountPageService.checkParm(userId, mobile, name);
        // 失败
        if ("0".equals(result.get("success"))) {
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, result.get("message"));
            baseMapBean.setCallBackAction(CustomConstants.HOST + BankOpenEncryptPageDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        try {
            // 同步调用路径
            String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
                    + BankOpenEncryptPageDefine.REQUEST_MAPPING + BankOpenEncryptPageDefine.RETURL_SYN_ACTION
                    + ".do?sign=" + sign + "&platform=" + platform + "&userId=" + userId;
            // 异步调用路
            String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
                    + BankOpenEncryptPageDefine.REQUEST_MAPPING + BankOpenEncryptPageDefine.RETURL_ASY_ACTION
                    + ".do?phone=" + mobile;
            // 拼装参数 调用江西银行
            OpenAccountPageBean openBean = new OpenAccountPageBean();
            openBean.setMobile(mobile);
            openBean.setPlatform(platform);
            openBean.setTrueName(name);
            openBean.setUserId(userId);
            openBean.setIp(CustomUtil.getIpAddr(request));
            // 同步 异步
            openBean.setRetUrl(retUrl);
            openBean.setNotifyUrl(bgRetUrl);
            openBean.setCoinstName("汇盈金服");
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
            openBean.setPlatform(platform);
            modelAndView = accountPageService.getCallbankEncryptPageMV(openBean);
            Users user = this.openAccountService.getUsers(userId);
            // 保存开户日志
            boolean isUpdateFlag = this.openAccountService.updateUserAccountLog(userId, user.getUsername(),
                    openBean.getMobile(), openBean.getOrderId(), platform, openBean.getTrueName(), openBean.getIdNo(),
                    openBean.getCardNo());
            if (!isUpdateFlag) {
                logger.info("保存开户日志失败,手机号:[" + openBean.getMobile() + "],用户ID:[" + userId + "]");
                modelAndView.addObject("message", "操作失败！");
                return modelAndView;
            }
            logger.info("开户end");
            LogUtil.endLog(TransPasswordController.class.toString(), TransPasswordDefine.SETPASSWORD_ACTION);
            return modelAndView;
        } catch (Exception e) {
            logger.error("调用银行接口失败", e);
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "调用银行接口失败！");
            baseMapBean.setCallBackAction(CustomConstants.HOST + TransPasswordDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
        }
        return modelAndView;
    }

    /**
     * 页面开户同步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(BankOpenEncryptPageDefine.RETURL_SYN_ACTION)
    public ModelAndView openAccountReturn(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute BankCallBean bean) {
        String sign = request.getParameter("sign");
        String platform = request.getParameter("platform");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
        String userId = request.getParameter("userId");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BaseMapBean baseMapBean = new BaseMapBean();
        bean.convert();
        // 银行返回响应代码
        String retCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
        logger.info("开户同步返回值,用户ID:[" + bean.getLogUserId() + "],retCode:[" + retCode + "]");
        String isSuccess = request.getParameter("isSuccess");
        // 成功了
        Users user = openAccountService.getUsers(new Integer(userId));
        if ("1".equals(isSuccess)) {
            logger.info("用户状态,用户ID:[" + userId + "],bankOpenAccount:[" + user.getBankOpenAccount() + "]");
            logger.info("发送卡户保存交易密码mq");
            UsersInfo usersInfo= appUserService.getUsersInfoByUserId(Integer.valueOf(userId));
            UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
            userOperationLogEntity.setOperationType(6);
            userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
            userOperationLogEntity.setPlatform(request.getParameter("realPlatform")==null?Integer.valueOf(platform):Integer.valueOf(request.getParameter("realPlatform")));
            userOperationLogEntity.setRemark("");
            userOperationLogEntity.setOperationTime(new Date());
            userOperationLogEntity.setUserName(user.getUsername());
            userOperationLogEntity.setUserRole(String.valueOf(usersInfo.getRoleId()));
            appUserService.sendUserLogMQ(userOperationLogEntity);
            // 成功
            baseMapBean.set(BankOpenEncryptPageDefine.SET_PAWWWORD_URL,
                    BankOpenEncryptPageDefine.BANK_PASSWORD_URL + ".do?sign=" + sign + "&platform=" + platform);
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "");
            baseMapBean.setCallBackAction(CustomConstants.HOST + BankOpenEncryptPageDefine.JUMP_HTML_SUCCESS_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        // 开户失败情况下 查询异步结果 区分是不是设置交易密码失败了
        // 如果已经开户 未设置交易密码 重定向到设置交易密码上
        if (user.getBankOpenAccount()==1 && user.getIsSetPassword() == 0) {
            logger.info("开户成功,用户ID:[" + bean.getLogUserId() + "],设置交易密码失败，引导用户设置交易密码");
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "失败原因：设置交易密码失败");
            baseMapBean.setCallBackAction(CustomConstants.HOST + BankOpenEncryptPageDefine.JUMP_HTML_HANDLING_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        String retMsg = openAccountService.getBankRetMsg(retCode);
        baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, "失败原因：" + retMsg);
        baseMapBean.setCallBackAction(CustomConstants.HOST + BankOpenEncryptPageDefine.JUMP_HTML_FAILED_PATH);
        modelAndView.addObject("callBackForm", baseMapBean);
        return modelAndView;
        
    }

    /**
     * 页面开户异步回调
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(BankOpenEncryptPageDefine.RETURL_ASY_ACTION)
    public BankCallResult bgreturn(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute BankCallBean bean) {
     // 上送的异步地址里面有
        BankCallResult result = new BankCallResult();
        String phone = request.getParameter("phone");
        String roleId = request.getParameter("roleId");
        bean.setIdentity(roleId);
        logger.info("页面开户异步回调start getStatus:{} retCode:{} ", bean.getRetCode(), bean.getStatus());
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
            logger.info("开户失败,银行返回响应代码:[" + retCode + "],retMsg:[" + retMsg + "],订单号:[" + bean.getLogOrderId() + "].");
            result.setStatus(false);
            return result;
        }
        // 开户成功后,保存用户的开户信息
        boolean saveBankAccountFlag = this.accountPageService.updateUserAccount(bean);
        if (!saveBankAccountFlag) {
            logger.info("开户失败,保存用户的开户信息失败:[" + retCode + "],订单号:[" + bean.getLogOrderId() + "].");
            result.setStatus(false);
            return result;
        }
        // 保存银行卡信息
        boolean saveBankCardFlag = this.accountPageService.updateCardNoToBank(bean);
        if (!saveBankCardFlag) {
            logger.info("开户失败,保存银行卡信息失败:[" + retCode + "],订单号:[" + bean.getLogOrderId() + "].");
            result.setStatus(false);
            return result;
        }
        // 开户成功后,发送CA认证MQ
        this.openAccountService.sendCAMQ(String.valueOf(userId));
        CommonSoaUtils.listOpenAcc(userId);
        logger.info("开户成功,用户ID:[" + userId + ",用户电子账户号:[" + bean.getAccountId() + "]");
        result.setStatus(true);
        return result;

    }

}
