package com.hyjf.api.server.user.openaccount;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.server.openaccount.UserOpenAccountService;
import com.hyjf.api.server.util.ErrorCodeConstant;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccountLog;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

/**
 * 外部服务接口:用户开户
 *
 * @author liuyang
 */
@Controller
@RequestMapping(UserOpenAccountDefine.REQUEST_MAPPING)
public class UserOpenAccountServer extends BaseController {

    Logger _log = LoggerFactory.getLogger(UserOpenAccountServer.class);

    @Autowired
    private UserOpenAccountService userOpenAccountService;
    
    
    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    /**
     * 用户开户发送短信验证码
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(UserOpenAccountDefine.SEND_SMS_ACTION)
    public UserOpenAccountResultBean sendCode(@RequestBody UserOpenAccountRequestBean userOpenAccountRequestBean, HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(UserOpenAccountDefine.THIS_CLASS, UserOpenAccountDefine.SEND_SMS_ACTION);
        UserOpenAccountResultBean resultBean = new UserOpenAccountResultBean();
        // 手机号
        String mobile = userOpenAccountRequestBean.getMobile();
        // 渠道
        String channel = userOpenAccountRequestBean.getChannel();
        // 机构编号
        String instCode = userOpenAccountRequestBean.getInstCode();
        _log.info("用户开户发送短信验证码第三方请求参数：" + JSONObject.toJSONString(userOpenAccountRequestBean));
        try {
            // 验证请求参数
            // 机构编号
            if (Validator.isNull(instCode)) {
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                resultBean.setStatusDesc("请求参数非法");
                return resultBean;
            }
            // 手机号
            if (Validator.isNull(mobile)) {
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000001);
                resultBean.setStatusDesc("手机号不能为空");
                return resultBean;
            }
            // 渠道
            if (Validator.isNull(channel)) {
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000006);
                resultBean.setStatusDesc("渠道不能为空");
                return resultBean;
            }
            // 验签
			if (!this.verifyRequestSign(userOpenAccountRequestBean, BaseDefine.METHOD_SERVER_SEND_SMS)) {
				_log.info("----验签失败----");
				resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000002);
				resultBean.setStatusDesc("验签失败！");
				return resultBean;
			}
            // 手机号合法性校验
            if (!Validator.isMobile(mobile)) {
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000003);
                resultBean.setStatusDesc("请输入您的真实手机号码");
                return resultBean;
            }
            // 根据手机号查询用户信息
            Users user = this.userOpenAccountService.selectUserByMobile(mobile);
            if (user == null) {
                _log.info("根据手机号查询用户信息失败,手机号:[" + mobile + "].");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000003);
                resultBean.setStatusDesc("根据手机号查询用户信息失败");
                return resultBean;
            }
            // 用户ID
            Integer userId = user.getUserId();
            // 用户名
            String userName = user.getUsername();
            // 发送短信订单号
            String orderId = GetOrderIdUtils.getOrderId2(userId);
            // 插入开户记录表
            boolean isUpdateFlag = userOpenAccountService.updateUserAccountLog(userId, userName, mobile, orderId);
            if (!isUpdateFlag) {
                _log.info("插入开户记录表失败,手机号:[" + mobile + "].");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
                resultBean.setStatusDesc("发送短信验证码失败");
                return resultBean;
            }
            // 调用江西银行发送短信接口
            BankCallBean bankCallBean = this.userOpenAccountService.sendOpenAccountSms(userId, orderId, BankCallConstant.TXCODE_ACCOUNT_OPEN_PLUS, mobile, channel);
            if (Validator.isNull(bankCallBean)) {
                _log.info("调用银行发送短信接口失败");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
                resultBean.setStatusDesc("发送短信验证码失败");
                return resultBean;
            }
            // 短信发送返回结果码
            String retCode = bankCallBean.getRetCode();
            if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
                _log.info("开户发送短信验证码,手机号:[" + mobile + "],银行返回结果:retCode:[" + retCode + "]");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
                resultBean.setStatusDesc("发送短信验证码失败");
                return resultBean;
            }
            if (Validator.isNull(bankCallBean.getSrvAuthCode()) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
                _log.info("开户发送短信验证码,手机号:[" + mobile + "],银行返回结果:retCode:[" + retCode + "],前导业务授权码:" + bankCallBean.getSrvAuthCode());
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
                resultBean.setStatusDesc("发送短信验证码失败");
                return resultBean;
            }

            // 业务授权码
            String srvAuthCode = bankCallBean.getSrvAuthCode();
            if (Validator.isNotNull(srvAuthCode)) {
                // 更新用户开户日志,更新前导业务授权码
                boolean openAccountLogFlag = this.userOpenAccountService.updateUserAccountLog(userId, orderId, srvAuthCode);
                if (!openAccountLogFlag) {
                    _log.info("保存开户日志失败,更新前导业务授权码,手机号:[" + mobile + "],前导业务授权码:[" + srvAuthCode + "],订单号:[" + orderId + "]");
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
                    resultBean.setStatusDesc("发送短信验证码失败");
                    return resultBean;
                }
            }
            _log.info("发送短信验证码成功,手机号:[" + mobile + "],前导业务授权码:[" + srvAuthCode + "],订单号:[" + orderId + "]");
            resultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
            resultBean.setStatusDesc("发送短信验证码成功");
            resultBean.setOrderId(orderId);// 平台返回的唯一订单号
            LogUtil.endLog(UserOpenAccountDefine.THIS_CLASS, UserOpenAccountDefine.SEND_SMS_ACTION);
            return resultBean;
        } catch (Exception e) {
            _log.info("发送短信验证码异常,手机号:[" + mobile + "],异常信息:[" + e.getMessage() + "]");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            resultBean.setStatusDesc("发送短信验证码失败");
            LogUtil.endLog(UserOpenAccountDefine.THIS_CLASS, UserOpenAccountDefine.SEND_SMS_ACTION);
            return resultBean;
        }
    }

    /**
     * 用户开户
     *
     * @param userOpenAccountRequestBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(UserOpenAccountDefine.OPEN_ACCOUNT_ACTION)
    public UserOpenAccountResultBean openAccont(@RequestBody UserOpenAccountRequestBean userOpenAccountRequestBean, HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(UserOpenAccountDefine.THIS_CLASS, UserOpenAccountDefine.OPEN_ACCOUNT_ACTION);
        UserOpenAccountResultBean resultBean = new UserOpenAccountResultBean();
        _log.info("用户开户第三方请求参数：" + JSONObject.toJSONString(userOpenAccountRequestBean));
        try {
            // 手机号
            String mobile = userOpenAccountRequestBean.getMobile();
            // 姓名
            String trueName = userOpenAccountRequestBean.getTrueName();
            // 身份证号
            String idNo = userOpenAccountRequestBean.getIdNo();
            // 银行卡号
            String cardNo = userOpenAccountRequestBean.getCardNo();
            // 手机验证码
            String smsCode = userOpenAccountRequestBean.getSmsCode();
            // 短信发送成功后,平台返回的唯一订单号
            String orderId = userOpenAccountRequestBean.getOrderId();
            // 渠道
            String channel = userOpenAccountRequestBean.getChannel();
            // 机构编号
            String instCode = userOpenAccountRequestBean.getInstCode();
            // 开户平台
            String platform = userOpenAccountRequestBean.getPlatform();
            // 验证请求参数
            // 机构编号
            if (Validator.isNull(instCode)) {
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                resultBean.setStatusDesc("请求参数非法");
                return resultBean;
            }
            // 手机号
            if (Validator.isNull(mobile)) {
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000001);
                resultBean.setStatusDesc("手机号不能为空");
                return resultBean;
            }
            // 姓名
            if (Validator.isNull(trueName)) {
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000007);
                resultBean.setStatusDesc("姓名不能为空");
                return resultBean;
            }
            // 身份证号
            if (Validator.isNull(idNo)) {
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000008);
                resultBean.setStatusDesc("身份证号不能为空");
                return resultBean;
            }
            // 银行卡号
            if (Validator.isNull(cardNo)) {
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000009);
                resultBean.setStatusDesc("银行卡号不能为空");
                return resultBean;
            }
            // 手机验证码
            if (Validator.isNull(smsCode)) {
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000010);
                resultBean.setStatusDesc("手机验证码不能为空");
                return resultBean;
            }
            // 短信发送成功,平台返回唯一订单号
            if (Validator.isNull(orderId)) {
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000011);
                resultBean.setStatusDesc("短信发送订单号为空");
                return resultBean;
            }
            // 渠道
            if (Validator.isNull(channel)) {
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000006);
                resultBean.setStatusDesc("渠道为空");
                return resultBean;
            }
            // 开户平台
            if(Validator.isNull(platform)){
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000018);
                resultBean.setStatusDesc("开户平台为空");
                return resultBean;
            }
            // 验签
            if (!this.verifyRequestSign(userOpenAccountRequestBean, BaseDefine.METHOD_SERVER_OPEN_ACCOUNT)) {
                _log.info("----验签失败----");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000002);
                resultBean.setStatusDesc("验签失败！");
                return resultBean;
            }
            // 判断真实姓名是否包含特殊字符
            if (!ValidatorCheckUtil.verfiyChinaFormat(trueName)) {
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000012);
                resultBean.setStatusDesc("真实姓名不能包含空格");
                return resultBean;
            }
            // 判断真实姓名的长度,不能超过10位
            if (trueName.length() > 10) {
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000013);
                resultBean.setStatusDesc("真实姓名不能超过10位");
                return resultBean;
            }
            if (idNo.length() != 18) {
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000021);
                resultBean.setStatusDesc("身份证(18位)校验位错误");
                return resultBean;
            }
            String replaceIdNo = replaceIdNo(idNo);
            idNo = replaceIdNo;
            // 检查用户身份证号是否在汇盈已经存在
            boolean isOnly = userOpenAccountService.checkIdNo(idNo);
            if (!isOnly) {
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000014);
                resultBean.setStatusDesc("身份证已存在");
                return resultBean;
            }
            // 根据手机号查询用户
            Users user = this.userOpenAccountService.selectUserByMobile(mobile);
            if (user == null) {
                _log.info("根据手机号查询用户失败,手机号:[" + mobile + "]");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000003);
                resultBean.setStatusDesc("根据手机号查询用户失败");
                return resultBean;
            }
            // 用户ID
            Integer userId = user.getUserId();
            // 获取相应的短信发送日志
            BankOpenAccountLog openAccountLog = this.userOpenAccountService.checkBankOpenAccountLog(userId, orderId);
            if (Validator.isNull(openAccountLog)) {
                _log.info("未查询到用户开户记录,手机号:[" + mobile + "],用户ID:[" + userId + "],短信发送订单号:[" + orderId + "]");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
                resultBean.setStatusDesc("开户失败,请重新获取验证码");
                return resultBean;
            }
            // 获取短信授权码
            String srvAuthCode = openAccountLog.getLastSrvAuthCode();
            if (Validator.isNull(srvAuthCode)) {
                _log.info("开户失败,前导业务授权码为空,手机号:[" + mobile + "],用户ID:[" + userId + "],短信发送订单号:[" + orderId + "]");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
                resultBean.setStatusDesc("开户失败,请重新获取验证码");
                return resultBean;
            }
            // 获取共同参数
            String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
            String bankInstCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
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
            openAccoutBean.setTxCode(BankCallConstant.TXCODE_ACCOUNT_OPEN_PLUS);// 消息类型(用户开户)
            openAccoutBean.setInstCode(bankInstCode);// 机构代码
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
            openAccoutBean.setLogOrderId(orderId);
            openAccoutBean.setLogOrderDate(orderDate);
            openAccoutBean.setLogUserId(String.valueOf(userId));
            openAccoutBean.setLogRemark("外部服务接口:用户开户");
            openAccoutBean.setLogIp(ip);
            openAccoutBean.setLogClient(Integer.parseInt(platform));
            // 保存用户开户日志
            boolean openAccountLogFlag = this.userOpenAccountService.updateUserAccountLog(openAccountLog, openAccoutBean);
            if (!openAccountLogFlag) {
                _log.info("保存开户日志失败,手机号:[" + mobile + "],用户ID:[" + userId + "],短信发送订单号:[" + orderId + "].");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
                resultBean.setStatusDesc("开户失败");
                return resultBean;
            }
            // 调用江西银行接口
            BankCallBean openAccountResult = BankCallUtils.callApiBg(openAccoutBean);
            if (openAccountResult == null) {
                _log.info("调用江西银行开户接口,银行返回空,手机号:[" + mobile + "],用户ID:[" + userId + "],短信发送订单号:[" + orderId + "].");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
                resultBean.setStatusDesc("开户失败,调用银行接口失败");
                return resultBean;
            }
            // 银行返回响应代码
            String retCode = StringUtils.isNotBlank(openAccountResult.getRetCode()) ? openAccountResult.getRetCode() : "";
            if ("JX900650".equals(retCode)) {
                _log.info("验证码错误,手机号:[" + mobile + "],用户ID:[" + userId + "],短信发送订单号:[" + orderId + "].");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000015);
                resultBean.setStatusDesc("开户验证码错误");
                return resultBean;
            }
            // 银行卡与姓名不符
            if ("CP9919".equals(retCode)) {
                _log.info("开户失败,银行卡与姓名不符,用户ID:[" + userId + "],短信发送订单号:[" + orderId + "],retCode:[" + retCode + "].");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000016);
                resultBean.setStatusDesc("银行卡与姓名不符");
                return resultBean;
            }
            // 银行卡与证件不符
            if ("CP9920".equals(retCode)) {
                _log.info("开户失败,银行卡与证件不符,用户ID:[" + userId + "],短信发送订单号:[" + orderId + "],retCode:[" + retCode + "].");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000017);
                resultBean.setStatusDesc("银行卡与证件不符");
                return resultBean;
            }
            // 开户失败
            if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
                // 将开户记录状态改为4
                this.userOpenAccountService.updateUserAccountLog(userId, orderId, 4);
                // 根据银行相应代码,查询错误信息
                String retMsg = userOpenAccountService.getBankRetMsg(retCode);
                _log.info("开户失败,银行返回响应代码:[" + retCode + "],retMsg:[" + retMsg + "],手机号:[" + mobile + "],短信发送订单号:[" + orderId + "].");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
                resultBean.setStatusDesc("开户失败,调用银行接口失败");
                return resultBean;
            }
            // 开户成功后,保存用户的开户信息
            boolean saveBankAccountFlag = this.userOpenAccountService.updateUserAccount(openAccountResult);
            if (!saveBankAccountFlag) {
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
                resultBean.setStatusDesc("开户失败");
                return resultBean;
            }
            //检索银行卡联号
            BankCard bankCard = this.userOpenAccountService.checkPayAllianceCode(userId);
            _log.info("开户成功,手机号:[" + mobile + "],用户ID:[" + userId + ",用户电子账户号:[" + openAccountResult.getAccountId() + "]");
            resultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
            resultBean.setStatusDesc("开户成功");
            resultBean.setIsOpenAccount("1");// 是否开户
            resultBean.setAccountId(openAccountResult.getAccountId());// 用户电子账户号
            //银行卡联行号
            if (bankCard != null) {
                if (StringUtils.isNotEmpty(bankCard.getPayAllianceCode())) {
                    resultBean.setPayAllianceCode(bankCard.getPayAllianceCode());
                }
            }
            /* add by zhadaojian 20180222 开户成功后,将用户ID加入到CA认证消息队列 start */
            // 加入到消息队列
            Map<String, String> params = new HashMap<String, String>();
            params.put("mqMsgId", GetCode.getRandomCode(10));
            params.put("userId", String.valueOf(userId));
            rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_CERTIFICATE_AUTHORITY, JSONObject.toJSONString(params));
            /* add by zhadaojian 20180222 开户成功后,将用户ID加入到CA认证消息队列 end */
            return resultBean;
        } catch (Exception e) {
            _log.info("开户异常,异常信息:[" + e.getMessage() + "]");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            resultBean.setStatusDesc("开户失败");
            return resultBean;
        }
    }


    /**
     * 静默开户接口
     *
     * @param userOpenAccountRequestBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(UserOpenAccountDefine.OPEN_ACCOUNT_SILENT_ACTION)
    public UserOpenAccountResultBean openSilentAccount(UserOpenAccountRequestBean userOpenAccountRequestBean, HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(UserOpenAccountDefine.THIS_CLASS, UserOpenAccountDefine.OPEN_ACCOUNT_SILENT_ACTION);
        UserOpenAccountResultBean resultBean = new UserOpenAccountResultBean();
        try {
            // 手机号
            String mobile = userOpenAccountRequestBean.getMobile();
            // 姓名
            String trueName = userOpenAccountRequestBean.getTrueName();
            // 身份证号
            String idNo = userOpenAccountRequestBean.getIdNo();
            // 银行卡号
            String cardNo = userOpenAccountRequestBean.getCardNo();
            // 渠道
            String channel = userOpenAccountRequestBean.getChannel();
            // 验证请求参数
            // 手机号
            if (Validator.isNull(mobile)) {
                resultBean.setStatusForResponse(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("手机号不能为空");
                return resultBean;
            }
            // 姓名
            if (Validator.isNull(trueName)) {
                resultBean.setStatusForResponse(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("姓名不能为空");
                return resultBean;
            }
            // 身份证号
            if (Validator.isNull(idNo)) {
                resultBean.setStatusForResponse(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("身份证号不能为空");
                return resultBean;
            }
            // 银行卡号
            if (Validator.isNull(cardNo)) {
                resultBean.setStatusForResponse(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("银行卡号不能为空");
                return resultBean;
            }
            // 渠道
            if (Validator.isNull(channel)) {
                resultBean.setStatusForResponse(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("渠道为空");
                return resultBean;
            }
            // 验签
            if (!this.checkSign(userOpenAccountRequestBean, BaseDefine.METHOD_SERVER_OPEN_ACCOUNT_SILENT)) {
                _log.info("----验签失败----");
                resultBean.setStatusForResponse(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("验签失败！");
                return resultBean;
            }

            // 根据手机号查询用户
            Users user = this.userOpenAccountService.selectUserByMobile(mobile);
            if (user == null) {
                _log.info("根据手机号查询用户失败,手机号:[" + mobile + "]");
                resultBean.setStatusForResponse(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("根据手机号查询用户失败");
                return resultBean;
            }
            // 用户ID
            Integer userId = user.getUserId();
            String username = user.getUsername();
            String orderId = GetOrderIdUtils.getOrderId2(userId);

            // 获取共同参数
            //银行代码
            String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
            //机构代码
            String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
            //交易日期
            String txDate = GetOrderIdUtils.getTxDate();
            //交易时间
            String txTime = GetOrderIdUtils.getTxTime();
            //交易流水号
            String seqNo = GetOrderIdUtils.getSeqNo(6);
            //证件类型
            String idType = BankCallConstant.ID_TYPE_IDCARD;
            //账户用途
            String acctUse = BankCallConstant.ACCOUNT_USE_COMMON;
            String ip = CustomUtil.getIpAddr(request);
            // 调用开户接口
            BankCallBean openAccoutBean = new BankCallBean();
            openAccoutBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
            openAccoutBean.setTxCode(BankCallConstant.TXCODE_FREE_SMS_ACCOUNT_OPEN);// 消息类型(免短信开户)
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
            openAccoutBean.setUserIP(ip);
            openAccoutBean.setLogOrderId(orderId);
            openAccoutBean.setLogUserId(String.valueOf(userId));
            openAccoutBean.setLogRemark("外部服务接口:静默开户");
            openAccoutBean.setLogIp(ip);
            openAccoutBean.setLogClient(4);
            // 插入开户记录表
            boolean isUpdateFlag = userOpenAccountService.updateUserAccountLog(userId, username, mobile, orderId);
            if (!isUpdateFlag) {
                _log.info("插入开户记录表失败,手机号:[" + mobile + "].");
                resultBean.setStatusForResponse(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("插入开户记录表失败");
                return resultBean;
            }
            // 调用江西银行接口
            BankCallBean openAccountResult = BankCallUtils.callApiBg(openAccoutBean);
            if (openAccountResult == null) {
                _log.info("调用江西银行开户接口,银行返回空,手机号:[" + mobile + "],用户ID:[" + userId + "],订单号:[" + orderId + "].");
                resultBean.setStatusForResponse(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("开户失败,调用银行接口失败");
                return resultBean;
            }
            // 银行返回响应代码
            String retCode = StringUtils.isNotBlank(openAccountResult.getRetCode()) ? openAccountResult.getRetCode() : "";
            // 开户失败
            if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
                // 将开户记录状态改为4
                this.userOpenAccountService.updateUserAccountLog(userId, orderId, 4);
                // 根据银行相应代码,查询错误信息
                String retMsg = userOpenAccountService.getBankRetMsg(retCode);
                _log.info("开户失败,银行返回响应代码:[" + retCode + "],retMsg:[" + retMsg + "],手机号:[" + mobile + "],订单号:[" + orderId + "].");
                resultBean.setStatusForResponse(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("开户失败,调用银行接口失败");
                return resultBean;
            }
            // 开户成功后,保存用户的开户信息
            boolean saveBankAccountFlag = this.userOpenAccountService.updateUserAccount(openAccountResult);
            if (!saveBankAccountFlag) {
                resultBean.setStatusForResponse(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("开户失败");
                return resultBean;
            }
            _log.info("开户成功,手机号:[" + mobile + "],用户ID:[" + userId + ",用户电子账户号:[" + openAccountResult.getAccountId() + "]");
            resultBean.setStatusForResponse(BaseResultBean.STATUS_SUCCESS);
            resultBean.setStatusDesc("开户成功");
            resultBean.setIsOpenAccount("1");// 是否开户
            resultBean.setAccountId(openAccountResult.getAccountId());// 用户电子账户号
            return resultBean;
        } catch (Exception e) {
            _log.info("开户异常,异常信息:[" + e.getMessage() + "]");
            resultBean.setStatusForResponse(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("开户失败");
            return resultBean;
        }
    }

    private String replaceIdNo(String idNo) {
        String lastString = idNo.substring(idNo.length() - 1);
        if ("x".equalsIgnoreCase(lastString)) {
            idNo = idNo.replace(idNo.charAt(idNo.length() - 1) + "", "X");
        }
        return idNo;
    }
}
