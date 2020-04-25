package com.hyjf.wechat.controller.user.bankopenbg;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.bankopen.BankOpenService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountLog;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.controller.user.bankopen.OpenAccountBean;
import com.hyjf.wechat.controller.user.bankopen.OpenAccountDefine;
import com.hyjf.wechat.controller.user.bankopen.OpenAccountResultVo;
import com.hyjf.wechat.util.ResultEnum;

/**
 * 用户开户接口调用
 * @author jijun
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月27日
 * @see 上午09：10
 */

@Controller(OpenAccountDefinebg.CONTROLLER_NAME)
@RequestMapping(value = OpenAccountDefinebg.REQUEST_MAPPING_BG)
public class OpenAccountController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(OpenAccountController.class);

    @Autowired
    private BankOpenService openAccountService;
    
    /**
     * 当前controller名称
     */
    public static final String THIS_CLASS = OpenAccountController.class.getName();
    
    /**
     * 开户画面
     *
     * @param request
     * @param form
     * @return
     */
    @SignValidate
    @RequestMapping(value = OpenAccountDefinebg.BANKOPEN_INIT_OPEN_ACTION, method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean openAccountInit(HttpServletRequest request, OpenAccountBean form) {
    	
        LogUtil.startLog(THIS_CLASS, OpenAccountDefinebg.BANKOPEN_INIT_OPEN_ACTION);
        OpenAccountResultVo result = new OpenAccountResultVo();
        //请求成功
        result.setEnum(ResultEnum.SUCCESS);
        
        //获取登陆用户userId
        Integer userId = requestUtil.getRequestUserId(request);
        
        if (Validator.isNull(userId)) {
        	result.setEnum(ResultEnum.ERROR_021);
            return result;
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
            //手机号只显示前3和后4位
            result.setMobile(mobile);
            //获取用户名返回给前端
            Users user  = openAccountService.getUsersByUserId(userId);
            //将用户名返回给前端
            result.setUserName(user.getUsername());
            
            UsersInfo usersInfo = openAccountService.selectByUserId(userId);
            result.setTrueName(usersInfo.getTruename()==null?"":usersInfo.getTruename());
            result.setIdCard(usersInfo.getIdcard()==null?"":usersInfo.getIdcard());
        }
        LogUtil.endLog(THIS_CLASS, OpenAccountDefinebg.BANKOPEN_INIT_OPEN_ACTION);
        return result;
    }


    /**
     * 江西银行开户协议
     *
     * @param request
     * @param form
     * @return
     */
    @SignValidate
    @RequestMapping(OpenAccountDefinebg.JX_BANK_SERVICE_ACTION)
    public ModelAndView jxBankService(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(OpenAccountDefinebg.JX_BANK_SERVICE_PATH);
        String userIdStr = String.valueOf(requestUtil.getRequestUserId(request));
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
     * 获取短信验证码
     *
     * @param request
     * @param form
     * @return
     */
    @ResponseBody
    @SignValidate
    @RequestMapping(method = RequestMethod.POST, value = OpenAccountDefinebg.BANKOPEN_SENDCODE_ACTION, produces = "application/json; charset=UTF-8")
    public BaseResultBean sendCode(HttpServletRequest request, @ModelAttribute() OpenAccountBean form) {

        LogUtil.startLog(THIS_CLASS, OpenAccountDefinebg.BANKOPEN_SENDCODE_ACTION);
        OpenAccountResult result = new OpenAccountResult();
        //String mobileStr = request.getParameter("phone");
        // 获取登陆用户userId
        Integer userId = requestUtil.getRequestUserId(request);
        String errorDesc = "短信验证码发送失败!";
        Users users = this.openAccountService.getUsers(userId);
        if (Validator.isNull(users)) {
            result.setEnum(ResultEnum.FAIL);
            return result;
        }
        String userName = users.getUsername();
        // 获取用户的手机号
        String mobile = users.getMobile();
        if (StringUtils.isBlank(mobile)) {
//            ResultEnum enums = ResultEnum.FAIL;
            result.setStatus(ResultEnum.FAIL.getStatus());
            result.setStatusDesc("用户信息错误，未获取到用户的手机号码！");
            return result;
        }
        String logOrderId = GetOrderIdUtils.getOrderId2(userId);
        boolean openAccountLog = this.openAccountService.updateUserAccountLog(userId, userName, mobile, logOrderId,
                CustomConstants.CLIENT_WECHAT);
        if (!openAccountLog) {
//            ResultEnum enums = ResultEnum.FAIL;
            result.setStatus(ResultEnum.FAIL.getStatus());
            result.setStatusDesc(errorDesc);
            return result;
        }
        // 调用短信发送接口
        BankCallBean mobileBean = this.openAccountService.sendSms(userId, BankCallConstant.TXCODE_ACCOUNT_OPEN_PLUS,
                mobile, BankCallConstant.CHANNEL_PC);
        if (Validator.isNull(mobileBean)) {
            errorDesc = "短信验证码发送失败，请稍后再试！";
//            ResultEnum enums = ResultEnum.FAIL;
            result.setStatus(ResultEnum.FAIL.getStatus());
            result.setStatusDesc(errorDesc);
            return result;
        }
        // 短信发送返回结果码
        String retCode = mobileBean.getRetCode();
        if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)
                && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
            errorDesc = "短信验证码发送失败！";
//            ResultEnum enums = ResultEnum.FAIL;
            result.setStatus(ResultEnum.FAIL.getStatus());
            result.setStatusDesc(errorDesc);
            return result;
        }
        if (Validator.isNull(mobileBean.getSrvAuthCode()) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
            errorDesc = "短信验证码发送失败！";
//            ResultEnum enums = ResultEnum.FAIL;
            result.setStatus(ResultEnum.FAIL.getStatus());
            result.setStatusDesc(errorDesc);
            return result;
        }
        // 业务授权码
        String srvAuthCode = mobileBean.getSrvAuthCode();
        if (Validator.isNotNull(mobileBean.getSrvAuthCode())) {
            // 保存用户开户日志
            boolean openAccountLogFlag = this.openAccountService.updateUserAccountLog(userId, logOrderId, srvAuthCode);
            if (!openAccountLogFlag) {
                errorDesc = "保存开户日志失败！";
//                ResultEnum enums = ResultEnum.FAIL;
                result.setStatus(ResultEnum.FAIL.getStatus());
                result.setStatusDesc(errorDesc);
                return result;
            }
        }
        errorDesc = "短信发送成功！";
//        ResultEnum enums = ResultEnum.SUCCESS;
        result.setStatus(ResultEnum.SUCCESS.getStatus());
        result.setStatusDesc(errorDesc);
        result.setLogOrderId(logOrderId);
        return result;
    }

    /**
     * 普通用户（出借人）开户
     *
     * @param request
     * @param
     * @return
     */
    @ResponseBody
    @SignValidate
    @RequestMapping(method = RequestMethod.POST, value = OpenAccountDefine.BANKOPEN_OPEN_ACCOUNT_ACTION, produces = "application/json; charset=utf-8")
    public BaseResultBean openAccount(HttpServletRequest request,
                                         @RequestBody OpenAccountBean accountBean) {

        LogUtil.startLog(THIS_CLASS, OpenAccountDefine.BANKOPEN_OPEN_ACCOUNT_ACTION);
        OpenAccountResult result = new OpenAccountResult();
//        ResultEnum enums = ResultEnum.FAIL;
        // 获取相应的订单号
        //String logOrderId = request.getParameter(BankCallConstant.PARAM_LOGORDERID);
        // 获取登陆用户userId
        String sign = request.getParameter("sign");
      
        String platform = "1";// 终端类型
        Integer userId = requestUtil.getRequestUserId(request);

        String logOrderId = accountBean.getLogOrderId();
        if (Validator.isNull(logOrderId)) {
        	result.setStatus(ResultEnum.FAIL.getStatus());
        	result.setStatusDesc("开户失败");
            return result;
        }
        Users users = this.openAccountService.getUsers(userId);
        if (Validator.isNull(users)) {
            String errorDesc = "获取用户信息失败！";
            result.setStatus(ResultEnum.FAIL.getStatus());
            result.setStatusDesc(errorDesc);
            return result;

        }
        // 取得用户在江西银行的客户号
        BankOpenAccount openAccount = openAccountService.getBankOpenAccount(userId);
        if (Validator.isNotNull(openAccount)) {
            String errorDesc = "用户已开户！";
            result.setStatus(ResultEnum.FAIL.getStatus());
            result.setStatusDesc(errorDesc);
            return result;
        }
        // 获取开户画面相应的参数
        // 获取用户的真实姓名
        String trueName = accountBean.getTrueName();
        if (StringUtils.isBlank(trueName)) {
            String errorDesc = "真实姓名不能为空！";
            result.setStatus(ResultEnum.FAIL.getStatus());
            result.setStatusDesc(errorDesc);
            return result;

        } else {
            //判断真实姓名是否包含特殊字符
            if (!ValidatorCheckUtil.verfiyChinaFormat(trueName)) {
                String errorDesc = "真实姓名不能包含空格！";
                result.setStatus(ResultEnum.FAIL.getStatus());
                result.setStatusDesc(errorDesc);
                return result;
            }
            if (trueName.length() > 10) {
                String errorDesc = "真实姓名不能超过10位";
                result.setStatus(ResultEnum.FAIL.getStatus());
                result.setStatusDesc(errorDesc);
                return result;
            }
        }
        // 获取用户的身份证号
        String idNo = accountBean.getIdNo();
        if (StringUtils.isBlank(idNo)) {
            String errorDesc = "身份证号不能为空！";
            result.setStatus(ResultEnum.FAIL.getStatus());
            result.setStatusDesc(errorDesc);
            return result;
        }
        // add by cwyang 身份证信息最后一位如果是小写x替换为大写
        String replaceIdNo = replaceIdNo(idNo);
        idNo = replaceIdNo;
        boolean isOnly = openAccountService.checkIdNo(idNo);
        if (!isOnly) {
            String errorDesc = "身份证已存在!";
            result.setStatus(ResultEnum.FAIL.getStatus());
            result.setStatusDesc(errorDesc);
            return result;
        }

        // 获取用户的银行卡号
        String cardNo = accountBean.getCardNo();
        if (StringUtils.isBlank(cardNo)) {
            String errorDesc = "银行卡号不能为空！";
            result.setStatus(ResultEnum.FAIL.getStatus());
            result.setStatusDesc(errorDesc);
            return result;
        }

        // 获取用户开户的短信验证码
        String smsCode = accountBean.getSmsCode();
        if (StringUtils.isBlank(smsCode)) {
            String errorDesc = "短信验证码不能为空！";
            result.setStatus(ResultEnum.FAIL.getStatus());
            result.setStatusDesc(errorDesc);
            return result;
        }
        // 获取用户的手机号
        String mobile = this.openAccountService.getUsersMobile(userId);
        if (StringUtils.isBlank(mobile)) {
            String errorDesc = "用户信息错误，未获取到用户的手机号码！";
            result.setStatus(ResultEnum.FAIL.getStatus());
            result.setStatusDesc(errorDesc);
            return result;
            /*if (StringUtils.isNotBlank(accountBean.getMobile())) {
                if (!openAccountService.existMobile(accountBean.getMobile())) {
                    mobile = accountBean.getMobile();
                } else {
                    errorDesc = "用户信息错误，手机号码重复！";
                    result.setReturnMsg(errorDesc);
                    result.setStatus(error);
                    return result;
                }
            } else {
                errorDesc = "用户信息错误，未获取到用户的手机号码！";
                result.setReturnMsg(errorDesc);
                result.setStatus(error);
                return result;
            }
        } else {
            if (StringUtils.isNotBlank(accountBean.getMobile()) && !mobile.equals(accountBean.getMobile())) {
                errorDesc = "用户信息错误，用户的手机号码错误！";
                result.setReturnMsg(errorDesc);
                result.setStatus(error);
                return result;
            }*/
        }
        // 获取相应的短信发送日志
        BankOpenAccountLog openAccountLog = this.openAccountService.selectUserAccountLog(userId, logOrderId);
        if (Validator.isNull(openAccountLog)) {
            logger.info("无短信发送日志");
            result.setStatus(ResultEnum.FAIL.getStatus());
            result.setStatusDesc("请重新获取验证码!");
            return result;
        }
        // 获取短信授权码
        String srvAuthCode = openAccountLog.getLastSrvAuthCode();
        if (StringUtils.isBlank(srvAuthCode)) {// add by cwyang 短信验证码判空
        	result.setStatus(ResultEnum.FAIL.getStatus());
            result.setStatusDesc("短信验证码获取失败！");
            return result;
        }
        // 获取共同参数
        String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
        String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
        String channel = BankCallConstant.CHANNEL_WEI;
        String orderDate = GetOrderIdUtils.getOrderDate();
        String txDate = GetOrderIdUtils.getTxDate();
        String txTime = GetOrderIdUtils.getTxTime();
        String seqNo = GetOrderIdUtils.getSeqNo(6);
        String idType = BankCallConstant.ID_TYPE_IDCARD;
        String acctUse = BankCallConstant.ACCOUNT_USE_COMMON;
        String ip = CustomUtil.getIpAddr(request);
        // 调用开户接口
        BankCallBean openAccoutBean = new BankCallBean();
        openAccoutBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        openAccoutBean.setTxCode(BankCallConstant.TXCODE_ACCOUNT_OPEN_PLUS);// 消息类型(用户开户，目前只保留开户协议，代码搬迁到openAccount)
        openAccoutBean.setInstCode(instCode);// 机构代码
        openAccoutBean.setBankCode(bankCode);
        openAccoutBean.setTxDate(txDate);
        openAccoutBean.setTxTime(txTime);
        openAccoutBean.setSeqNo(seqNo);
        openAccoutBean.setChannel(channel);
        openAccoutBean.setIdType(idType);
        openAccoutBean.setIdNo(idNo);
        openAccoutBean.setName(trueName);
        openAccoutBean.setMobile(mobile);
        openAccoutBean.setCardNo(cardNo);
        openAccoutBean.setAcctUse(acctUse);
        openAccoutBean.setSmsCode(smsCode);
        openAccoutBean.setUserIP(ip);
        openAccoutBean.setLastSrvAuthCode(srvAuthCode);
        openAccoutBean.setLogOrderId(logOrderId);
        openAccoutBean.setLogOrderDate(orderDate);
        openAccoutBean.setLogUserId(String.valueOf(userId));
        openAccoutBean.setLogRemark("用户开户，目前只保留开户协议，代码搬迁到openAccount");
        openAccoutBean.setLogIp(ip);
        if (Validator.isNull(platform)) {
            openAccoutBean.setLogClient(Integer.parseInt(platform));
        }

        // 保存用户开户日志
        boolean openAccountLogFlag = this.openAccountService.updateUserAccountLog(openAccountLog, openAccoutBean);
        if (!openAccountLogFlag) {
        	result.setStatus(ResultEnum.FAIL.getStatus());
            result.setStatusDesc("保存开户日志失败！");
            return result;
        }
        try {
            BankCallBean openAccountResult = BankCallUtils.callApiBg(openAccoutBean);
            logger.info("开户结果:{}", JSONObject.toJSONString(openAccountResult));
            String retCode = (openAccountResult!=null ? openAccountResult.getRetCode() : "");
            if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
                this.openAccountService.updateUserAccountLog(userId, logOrderId, 4);
                String retMsg = openAccountService.getBankRetMsg(retCode);
                logger.info("The BankRetCode is ：{}", retCode);
                logger.info("The BankRetMsg is ：{}", retMsg);
                result.setStatus(retCode);
                result.setStatusDesc(retMsg);
                return result;

            }
            if (openAccountResult != null && StringUtils.isNoneBlank(platform)) {
                openAccountResult.setLogClient(Integer.parseInt(platform));
            }

            // 保存用户的开户信息
            boolean saveBankAccountFlag = this.openAccountService.updateUserAccount(openAccountResult);
            if (!saveBankAccountFlag) {
                String errorDesc = "开户失败，请联系客服！";
                result.setStatus(ResultEnum.FAIL.getStatus());
                result.setStatusDesc(errorDesc);
                return result;
            } else {
            	CommonSoaUtils.listOpenAcc(userId);
//                enums = ResultEnum.SUCCESS;
                result.setStatus(ResultEnum.SUCCESS.getStatus());
                result.setStatusDesc("开户成功");
                // 开户成功后,发送CA认证MQ
                this.openAccountService.sendCAMQ(String.valueOf(userId));
                //开户成功，跳转江西银行设置密码
                result.setPasswordUrl(OpenAccountDefine.BANK_PASSWORD_URL + ".page?sign=" + sign + "&logOrderId=" + logOrderId + "&platform=" + platform);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
//            enums.setStatusDesc("开户失败，请联系客服！");
//            result.setEnum(enums);
            result.setStatus(ResultEnum.FAIL.getStatus());
            result.setStatusDesc("开户失败，请联系客服！");
            return result;
        }
    }
    
    private String replaceIdNo(String idNo) {
        String lastString = idNo.substring(idNo.length() - 1);
        if ("x".equalsIgnoreCase(lastString)) {
            idNo = idNo.replace(idNo.charAt(idNo.length() - 1) + "", "X");
        }
        logger.info("idNo is " + idNo);
        return idNo;
    }
}

