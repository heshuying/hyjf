package com.hyjf.api.server.user.recharge;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.server.recharge.ThirdPartyUserRechargeService;
import com.hyjf.api.server.util.ErrorCodeConstant;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.user.auth.AuthService;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

/**
 * 外部服务接口:用户充值
 *
 * @author liuyang
 */
@Controller
@RequestMapping(UserRechargeDefine.REQUEST_MAPPING)
public class UserRechargeServer extends BaseController {

    Logger _log = LoggerFactory.getLogger(UserRechargeServer.class);

    @Autowired
    private ThirdPartyUserRechargeService userRechargeService;
    @Autowired
	private AuthService authService;
    /**
     * 短信充值发送短信验证码
     *
     * @param userRechargeRequestBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(UserRechargeDefine.SENDCODE_ACTION)
    public UserRechargeResultBean sendSms(@RequestBody UserRechargeRequestBean userRechargeRequestBean, HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(UserRechargeDefine.THIS_CLASS, UserRechargeDefine.SENDCODE_ACTION);
        UserRechargeResultBean resultBean = new UserRechargeResultBean();
        try {
            // 银行预留手机号
            String mobile = userRechargeRequestBean.getMobile();
            // 银行卡号
            String cardNo = userRechargeRequestBean.getCardNo();
            // 银行电子账户号
            String accountId = userRechargeRequestBean.getAccountId();
            // 渠道
            String channel = userRechargeRequestBean.getChannel();
            // 机构编号
            String instCode = userRechargeRequestBean.getInstCode();
            // 验证请求参数
            // 手机号
            if (Validator.isNull(mobile)) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("银行预留手机号不能为空");
                return resultBean;
            }
            // 银行卡号
            if (Validator.isNull(cardNo)) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("银行卡号不能为空");
                return resultBean;
            }
            // 银行电子账户号
            if (Validator.isNull(accountId)) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("银行电子账户号不能为空");
                return resultBean;
            }
            // 渠道
            if (Validator.isNull(channel)) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("渠道不能为空");
                return resultBean;
            }
            // 机构编号
            if (Validator.isNull(instCode)) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("机构编号不能为空");
                return resultBean;
            }
            // 验签
            if (!this.verifyRequestSign(userRechargeRequestBean, BaseDefine.METHOD_SERVER_SEND_RECHARGE_SMS)) {
                _log.info("----验签失败----");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("验签失败！");
                return resultBean;
            }
            // 手机号合法性校验
            if (!Validator.isMobile(mobile)) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("请输入您的真实手机号码");
                return resultBean;
            }
            // 根据用户电子账户号查询用户信息
            BankOpenAccount bankOpenAccount = this.userRechargeService.selectBankOpenAccountByAccountId(accountId);
            if (bankOpenAccount == null) {
                _log.info("查询用户开户信息失败,用户电子账户号:[" + accountId + "]");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("短信验证码发送失败");
                return resultBean;
            }
            // 用户ID
            Integer userId = bankOpenAccount.getUserId();
            // 根据用户ID查询用户信息
            Users user = this.userRechargeService.getUsersByUserId(userId);
            if (user == null) {
                _log.info("根据用户ID查询用户信息失败,用户电子账户号:[" + accountId + "],用户ID:[" + userId + "].");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("短信验证码发送失败");
                return resultBean;
            }
            // 根据用户ID查询用户平台银行卡信息
            BankCard bankCard = this.userRechargeService.getBankCardByUserId(userId);
            if (bankCard == null) {
                _log.info("根据用户ID查询用户银行卡信息失败,用户电子账户号:[" + accountId + "],用户ID:[" + userId + "].");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("短信验证码发送失败");
                return resultBean;
            }
            // 用户汇盈平台的银行卡卡号
            String localCardNo = bankCard.getCardNo() == null ? "" : bankCard.getCardNo();
            // 如果两边银行卡号不一致
            if (!cardNo.equals(localCardNo)) {
                _log.info("用户银行卡信息不一致,用户电子账户号:[" + accountId + "],请求银行卡号:[" + cardNo + "],平台保存的银行卡号:[" + localCardNo + "].");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("短信验证码发送失败");
                return resultBean;
            }
            
            // 服务费授权状态和开关
            if (!authService.checkPaymentAuthStatus(bankOpenAccount.getUserId())) {
            	resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000011);
    			resultBean.setStatusDesc("用户未进行缴费授权");
    			return resultBean; 
            }
            
            // 调用江西银行发送短信接口
            BankCallBean bankResultBean = this.userRechargeService.sendRechargeOnlineSms(userId, cardNo, mobile, channel);
            if (Validator.isNull(bankResultBean)) {
                _log.info("调用短信发送接口失败,用户电子账户号:[" + accountId + "],请求银行卡号:[" + cardNo + "],平台保存的银行卡号:[" + localCardNo + "].");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("短信验证码发送失败");
                return resultBean;
            }
            // 短信发送返回结果码
            String retCode = bankResultBean.getRetCode();
            if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
                _log.info("调用短信发送接口失败,用户电子账户号:[" + accountId + "],请求银行卡号:[" + cardNo + "],平台保存的银行卡号:[" + localCardNo + "],银行返回结果码:[" + retCode + "]");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("短信验证码发送失败");
                return resultBean;
            }
            if (Validator.isNull(bankResultBean.getSrvAuthCode()) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
                _log.info("调用短信发送接口失败,前导业务授权码为空,用户电子账户号:[" + accountId + "],用户ID:[" + userId + "].");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("短信验证码发送失败");
                return resultBean;
            }
            _log.info("短信验证码发送成功");
            resultBean.setStatus(ErrorCodeConstant.SUCCESS);
            resultBean.setStatusDesc("短信验证码发送成功");
            LogUtil.endLog(UserRechargeDefine.THIS_CLASS, UserRechargeDefine.SENDCODE_ACTION);
            return resultBean;
        } catch (Exception e) {
            _log.info("调用短信发送接口发生异常");
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("短信验证码发送失败");
            return resultBean;
        }
    }

    /**
     * 短信充值
     *
     * @param userRechargeRequestBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(UserRechargeDefine.RECHARGE_ACTION)
    public UserRechargeResultBean recharge(@RequestBody UserRechargeRequestBean userRechargeRequestBean, HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(UserRechargeDefine.THIS_CLASS, UserRechargeDefine.RECHARGE_ACTION);
        _log.info("-----------短信充值-----------");
        UserRechargeResultBean resultBean = new UserRechargeResultBean();
        try {
            // 短信验证码
            String smsCode = userRechargeRequestBean.getSmsCode();
            // 银行卡预留手机号
            String mobile = userRechargeRequestBean.getMobile();
            // 充值银行卡号
            String cardNo = userRechargeRequestBean.getCardNo();
            // 充值金额
            String account = userRechargeRequestBean.getAccount();
            // 银行电子账户号
            String accountId = userRechargeRequestBean.getAccountId();
            // 渠道
            String channel = userRechargeRequestBean.getChannel();
            // 机构编号
            String instCode = userRechargeRequestBean.getInstCode();
            // 充值平台
            String platform = userRechargeRequestBean.getPlatform();
            // 验证请求参数
            // 手机号
            if (Validator.isNull(mobile)) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("银行预留手机号不能为空");
                return resultBean;
            }
            // 银行卡号
            if (Validator.isNull(cardNo)) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("银行卡号不能为空");
                return resultBean;
            }
            // 银行电子账户号
            if (Validator.isNull(accountId)) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("银行电子账户号不能为空");
                return resultBean;
            }
            // 渠道
            if (Validator.isNull(channel)) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("渠道不能为空");
                return resultBean;
            }
            // 充值金额
            if (Validator.isNull(account)) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("充值金额不能为空");
                return resultBean;
            }
            // 短信验证码
            if (Validator.isNull(smsCode)) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("短信验证码不能为空");
                return resultBean;
            }
            // 机构编号
            if (Validator.isNull(instCode)) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("机构编号不能为空");
                return resultBean;
            }
            if (Validator.isNull(platform)) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("充值平台不能为空");
                return resultBean;
            }
            // 验签
            if (!this.verifyRequestSign(userRechargeRequestBean, BaseDefine.METHOD_SERVER_RECHARGE)) {
                _log.info("----验签失败----");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("验签失败！");
                return resultBean;
            }
            // 手机号合法性校验
            if (!Validator.isMobile(mobile)) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("请输入您的真实手机号码");
                return resultBean;
            }
            // 充值金额校验
            if (!account.matches("^[1-9][0-9]*$")) {
                _log.info("充值金额格式错误,充值金额:[" + account + "]");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("充值金额格式错误");
                return resultBean;
            }
            // 根据机构编号检索机构信息
            HjhInstConfig instConfig = this.userRechargeService.selectInstConfigByInstCode(instCode);
            // 机构编号
            if (instConfig == null) {
                _log.info("获取机构信息为空,机构编号:[" + instCode + "].");
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000004);
                resultBean.setStatusDesc("机构编号错误");
                return resultBean;
            }
            // 根据用户电子账户号查询用户信息
            BankOpenAccount bankOpenAccount = this.userRechargeService.selectBankOpenAccountByAccountId(accountId);
            if (bankOpenAccount == null) {
                _log.info("查询用户开户信息失败,用户电子账户号:[" + accountId + "]");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("充值失败");
                return resultBean;
            }
            // 用户ID
            Integer userId = bankOpenAccount.getUserId();
            // 根据用户ID查询用户信息
            Users user = this.userRechargeService.getUsersByUserId(userId);
            if (user == null) {
                _log.info("根据用户ID查询用户信息失败,用户电子账户号:[" + accountId + "],用户ID:[" + userId + "].");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("充值失败");
                return resultBean;
            }
            // 根据用户ID查询用户详情
            UsersInfo userInfo = this.userRechargeService.getUsersInfoByUserId(userId);
            if (userInfo == null) {
                _log.info("根据用户ID查询用户详情失败,用户电子账户号:[" + accountId + ",用户ID:[" + userId + "]");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("充值失败");
                return resultBean;
            }
            // 身份证号
            String idNo = userInfo.getIdcard();
            // 姓名
            String trueName = userInfo.getTruename();
            // 根据用户ID查询用户平台银行卡信息
            BankCard bankCard = this.userRechargeService.getBankCardByUserId(userId);
            if (bankCard == null) {
                _log.info("根据用户ID查询用户银行卡信息失败,用户电子账户号:[" + accountId + "],用户ID:[" + userId + "].");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("充值失败");
                return resultBean;
            }
            // 用户汇盈平台的银行卡卡号
            String localCardNo = bankCard.getCardNo() == null ? "" : bankCard.getCardNo();
            // 如果两边银行卡号不一致
            if (!cardNo.equals(localCardNo)) {
                _log.info("用户银行卡信息不一致,用户电子账户号:[" + accountId + "],请求银行卡号:[" + cardNo + "],平台保存的银行卡号:[" + localCardNo + "].");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("充值失败");
                return resultBean;
            }
            // 获取短信序列号
            String smsSeq = this.userRechargeService.selectBankSmsSeq(userId, BankCallMethodConstant.TXCODE_DIRECT_RECHARGE_ONLINE);
            if (StringUtils.isBlank(smsSeq)) {
                _log.info("短信序列号为空,电子账户号:[" + accountId + "],用户ID:[" + userId + "],短信序列号:[" + smsSeq + "].");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("充值失败");
                return resultBean;
            }
            // 充值订单号
            String logOrderId = GetOrderIdUtils.getOrderId2(userId);
            // 充值订单日期
            String orderDate = GetOrderIdUtils.getOrderDate();
            // 调用 2.3.4联机绑定卡到电子账户充值
            BankCallBean bean = new BankCallBean();
            bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
            bean.setTxCode(BankCallMethodConstant.TXCODE_DIRECT_RECHARGE_ONLINE);// 交易代码
            bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
            bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
            bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
            bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
            bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
            bean.setChannel(channel); // 交易渠道
            bean.setAccountId(accountId); // 电子账号
            bean.setIdType(BankCallConstant.ID_TYPE_IDCARD); // 证件类型
            bean.setIdNo(idNo); // 身份证号
            bean.setName(trueName); // 姓名
            bean.setMobile(mobile); // 手机号
            bean.setCardNo(cardNo); // 银行卡号
            bean.setTxAmount(CustomUtil.formatAmount(account)); // 交易金额
            bean.setCurrency(BankCallConstant.CURRENCY_TYPE_RMB); // 交易币种
            bean.setSmsCode(smsCode); // 充值时短信验证,p2p使用
            bean.setSmsSeq(smsSeq);
            // 充值时短信验证,p2p使用
            bean.setUserIP(GetCilentIP.getIpAddr(request));// 客户IP
            bean.setLogOrderId(logOrderId);// 订单号
            bean.setLogOrderDate(orderDate);// 充值订单日期
            bean.setLogUserId(String.valueOf(userId));
            bean.setLogUserName(user.getUsername());
            bean.setLogRemark("短信充值");
            bean.setLogClient(Integer.parseInt(platform));// 充值平台
            // 插入充值记录
            int ret = this.userRechargeService.insertRechargeInfo(bean);
            if (ret == 0) {
                _log.info("调用接口前,插入充值记录表失败,用户电子账户号:[" + accountId + "],用户ID:[" + userId + "],充值订单号:[" + logOrderId + "].");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("充值失败");
                return resultBean;
            }
            // 调用短信充值接口
            BankCallBean bankCallBean = BankCallUtils.callApiBg(bean);
            if (Validator.isNull(bankCallBean)) {
                _log.info("调用银行接口失败,银行返回为空,用户电子账户号:[" + accountId + "],用户ID:[" + userId + "],充值订单号:[" + logOrderId + "].");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("充值失败");
                resultBean.setOrderId(logOrderId);
                return resultBean;
            }
            // 银行返回响应代码
            String retCode = StringUtils.isNotBlank(bankCallBean.getRetCode()) ? bankCallBean.getRetCode() : "";
            if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
                String error = this.userRechargeService.getBankRetMsg(retCode);
                _log.info("调用银行接口失败,银行返回结果retCode:[" + retCode + "],用户电子账户号:[" + accountId + "],用户ID:[" + userId + "],充值订单号:[" + logOrderId + "],银行返回错误信息:[" + error + "].");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("充值失败");
                resultBean.setOrderId(logOrderId);
                return resultBean;
            }
            // 充值成功
            // 更新充值的相关信息
            String ip = CustomUtil.getIpAddr(request);
            Map<String, String> params = new HashMap<String, String>();
            params.put("ip", ip);
            params.put("mobile",mobile);
            JSONObject msg = this.userRechargeService.handleRechargeInfo(bankCallBean, params);
            // 充值成功
            if (msg != null && msg.get("error").equals("0")) {
                _log.info("短信充值成功,手机号:[" + mobile + "],用户ID:[" + userId + "],充值金额:[" + bankCallBean.getTxAmount() + "]");
                resultBean.setStatus(ErrorCodeConstant.SUCCESS);
                resultBean.setStatusDesc("充值成功");// 充值成功
                resultBean.setTxAmount(bankCallBean.getTxAmount());
                resultBean.setOrderId(logOrderId);
                return resultBean;
            } else {
                _log.info("短信充值失败,手机号:[" + mobile + "],用户ID:[" + userId + "],冲值金额:[" + bankCallBean.getTxAmount() + "],失败原因:[" + msg.get("data") + "].");
                // 充值失败
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("充值失败");
                resultBean.setOrderId(logOrderId);
                return resultBean;
            }
        } catch (Exception e) {
            _log.info("短信充值发生异常,错误信息:[" + e.getMessage() + "]");
            // 充值失败
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("充值失败");
            return resultBean;
        }
    }

}
