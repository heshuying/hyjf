package com.hyjf.api.server.user.registeropenaccount;

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

import com.hyjf.api.server.registeropenaccount.UserRegisterAndOpenAccountService;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

/**
 * 外部服务接口:用户注册加开户Server
 *
 * @author liuyang
 */
@Controller
@RequestMapping(value = UserRegisterAndOpenAccountDefine.REQUEST_MAPPING)
public class UserRegisterAndOpenAccountServer extends BaseController {

    Logger _log = LoggerFactory.getLogger(UserRegisterAndOpenAccountServer.class);

    @Autowired
    private UserRegisterAndOpenAccountService registerAndOpenAccountService;

    /**
     * 用户注册加开户
     *
     * @param requestBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(UserRegisterAndOpenAccountDefine.REGISTER_OPENACCOUNT_ACTION)
    public UserRegisterAndOpenAccountResultBean registerAndOpenAccount(@RequestBody UserRegisterAndOpenAccountRequestBean requestBean, HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(UserRegisterAndOpenAccountDefine.THIS_CLASS, UserRegisterAndOpenAccountDefine.REGISTER_OPENACCOUNT_ACTION);
        UserRegisterAndOpenAccountResultBean resultBean = new UserRegisterAndOpenAccountResultBean();
        // 手机号
        String mobile = requestBean.getMobile();
        // 姓名
        String trueName = requestBean.getTrueName();
        // 身份证号
        String idCard = requestBean.getIdCard();
        // 银行卡号
        String cardNo = requestBean.getCardNo();
        // 机构编号
        String instCode = requestBean.getInstCode();
        // 渠道
        String channel = requestBean.getChannel();
        try {
            // 验证请求参数
            // 手机号
            if (Validator.isNull(mobile)) {
                resultBean.setStatus(UserRegisterAndOpenAccountDefine.STATUS_REGISTER_FAIL);
                resultBean.setStatusDesc("手机号不能为空");
                return resultBean;
            }
            // 姓名
            if (Validator.isNull(trueName)) {
                resultBean.setStatus(UserRegisterAndOpenAccountDefine.STATUS_REGISTER_FAIL);
                resultBean.setStatusDesc("姓名不能为空");
                return resultBean;
            }
            // 身份证号
            if (Validator.isNull(idCard)) {
                resultBean.setStatus(UserRegisterAndOpenAccountDefine.STATUS_REGISTER_FAIL);
                resultBean.setStatusDesc("身份证不能为空");
                return resultBean;
            }
            // 银行卡号
            if (Validator.isNull(cardNo)) {
                resultBean.setStatus(UserRegisterAndOpenAccountDefine.STATUS_REGISTER_FAIL);
                resultBean.setStatusDesc("银行卡号不能为空");
                return resultBean;
            }
            // 机构编号
            if (Validator.isNull(instCode)) {
                resultBean.setStatus(UserRegisterAndOpenAccountDefine.STATUS_REGISTER_FAIL);
                resultBean.setStatusDesc("机构编号不能为空");
                return resultBean;
            }
            // 渠道
            if (Validator.isNull(channel)) {
                resultBean.setStatus(UserRegisterAndOpenAccountDefine.STATUS_REGISTER_FAIL);
                resultBean.setStatusDesc("渠道不能为空");
                return resultBean;
            }
            // 验签
            if (!this.checkSign(requestBean, BaseDefine.METHOD_SERVER_REGISTER_OPENACCOUNT)) {
                _log.info("----验签失败----");
                resultBean.setStatus(UserRegisterAndOpenAccountDefine.STATUS_REGISTER_FAIL);
                resultBean.setStatusDesc("验签失败！");
                return resultBean;
            }
            // 手机号合法性校验
            if (!Validator.isMobile(mobile)) {
                resultBean.setStatus(UserRegisterAndOpenAccountDefine.STATUS_REGISTER_FAIL);
                resultBean.setStatusDesc("请输入您的真实手机号码");
                return resultBean;
            }
            // 根据机构编号检索机构信息
            HjhInstConfig instConfig = this.registerAndOpenAccountService.selectInstConfigByInstCode(instCode);
            // 机构编号
            if (instConfig == null) {
                _log.info("获取机构信息为空,机构编号:[" + instCode + "].");
                resultBean.setStatus(UserRegisterAndOpenAccountDefine.STATUS_REGISTER_FAIL);
                resultBean.setStatusDesc("机构编号错误");
                return resultBean;
            }
            // 根据手机号检索用户是否已在平台注册
            Users user = this.registerAndOpenAccountService.selectUserByMobile(mobile);
            // 手机号已在平台注册
            if (user != null) {
                _log.info("用户手机号已在平台注册:用户名:[" + user.getUsername() + ",用户手机号:[" + mobile + "]");
                resultBean.setStatus(UserRegisterAndOpenAccountDefine.STATUS_REGISTER_FAIL);
                resultBean.setStatusDesc("手机号已在平台注册");
                return resultBean;
            }
            // 用户注册
            Integer userId = this.registerAndOpenAccountService.insertUserAction(mobile, instCode, request);
            // 注册失败
            if (userId == null || userId == 0) {
                _log.info("用户注册失败,用户手机号:[" + mobile + "].");
                resultBean.setStatus(UserRegisterAndOpenAccountDefine.STATUS_REGISTER_FAIL);
                resultBean.setStatusDesc("注册失败");
                return resultBean;
            }
            // 根据用户ID查询用户信息
            Users users = this.registerAndOpenAccountService.getUsersByUserId(userId);
            // 用户名
            String userName = users.getUsername();
            // 获取共同参数
            String orderDate = GetOrderIdUtils.getOrderDate();
            String txDate = GetOrderIdUtils.getTxDate();
            String txTime = GetOrderIdUtils.getTxTime();
            String seqNo = GetOrderIdUtils.getSeqNo(6);
            String idType = BankCallConstant.ID_TYPE_IDCARD;
            String acctUse = BankCallConstant.ACCOUNT_USE_COMMON;
            String ip = CustomUtil.getIpAddr(request);
            // 订单号
            String orderId = GetOrderIdUtils.getOrderId2(userId);
            // 用户注册成功后,用户开户 TODO
            // 调用开户接口
            BankCallBean openAccoutBean = new BankCallBean();
            openAccoutBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
            openAccoutBean.setTxCode(BankCallConstant.TXCODE_FREE_SMS_ACCOUNT_OPEN);// 消息类型(免短信开户)
            openAccoutBean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
            openAccoutBean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
            openAccoutBean.setTxDate(txDate);
            openAccoutBean.setTxTime(txTime);
            openAccoutBean.setSeqNo(seqNo);
            openAccoutBean.setChannel(channel);
            openAccoutBean.setIdType(idType);
            openAccoutBean.setIdNo(idCard);
            openAccoutBean.setName(trueName);
            openAccoutBean.setMobile(mobile);
            openAccoutBean.setCardNo(cardNo);
            openAccoutBean.setAcctUse(acctUse);
            openAccoutBean.setUserIP(ip);
            openAccoutBean.setLogOrderId(orderId);
            openAccoutBean.setLogOrderDate(orderDate);
            openAccoutBean.setLogUserId(String.valueOf(userId));
            openAccoutBean.setLogRemark("外部服务接口:静默开户");
            openAccoutBean.setLogIp(ip);
            openAccoutBean.setLogClient(4);
            // 插入开户记录表
            boolean isUpdateFlag = registerAndOpenAccountService.updateUserAccountLog(userId, userName, mobile, orderId);
            if (!isUpdateFlag) {
                // 失败后,删除用户注册信息
                _log.info("开户失败后,删除用户相关信息:用户ID:[" + userId + "],用户名:[" + userName + "].");
                this.registerAndOpenAccountService.userDeleteByUserId(userId);
                _log.info("插入开户记录表失败,手机号:[" + mobile + "].");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("注册失败");
                return resultBean;
            }
            // 调用江西银行接口
            BankCallBean openAccountResult = BankCallUtils.callApiBg(openAccoutBean);
            if (openAccountResult == null) {
                _log.info("调用江西银行开户接口,银行返回空,手机号:[" + mobile + "],用户ID:[" + userId + "],订单号:[" + orderId + "].");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("开户失败");
                return resultBean;
            }
            // 银行返回响应代码
            String retCode = StringUtils.isNotBlank(openAccountResult.getRetCode()) ? openAccountResult.getRetCode() : "";
            // 开户失败
            if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
                // 将开户记录状态改为4
                this.registerAndOpenAccountService.updateUserAccountLog(userId, orderId, 4);
                // 根据银行相应代码,查询错误信息
                String retMsg = registerAndOpenAccountService.getBankRetMsg(retCode);
                _log.info("开户失败,银行返回响应代码:[" + retCode + "],retMsg:[" + retMsg + "],手机号:[" + mobile + "],短信发送订单号:[" + orderId + "].");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("开户失败,调用银行接口失败");
                return resultBean;
            }
            // 开户成功后,保存用户的开户信息
            boolean saveBankAccountFlag = this.registerAndOpenAccountService.updateUserAccount(openAccountResult);
            if (!saveBankAccountFlag) {
                _log.info("银行开户成功后,本地处理失败,用户ID:[" + userId + ",手机号:[" + mobile + "]");
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("开户失败");
                return resultBean;
            }
            _log.info("开户成功,手机号:[" + mobile + "],用户ID:[" + userId + ",用户电子账户号:[" + openAccountResult.getAccountId() + "]");
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            resultBean.setStatusDesc("开户成功");
            resultBean.setIsOpenAccount("1");// 是否开户
            resultBean.setAccountId(openAccountResult.getAccountId());// 用户电子账户号
            return resultBean;
        } catch (Exception e) {

        }
        LogUtil.endLog(UserRegisterAndOpenAccountDefine.THIS_CLASS, UserRegisterAndOpenAccountDefine.REGISTER_OPENACCOUNT_ACTION);
        return null;
    }

}
