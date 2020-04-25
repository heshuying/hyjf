package com.hyjf.api.aems.encryptpage;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.aems.util.AemsErrorCodeConstant;
import com.hyjf.api.server.openaccount.UserOpenAccountService;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.user.accountopenpage.OpenAccountPageBean;
import com.hyjf.bank.service.user.accountopenpage.UserOpenAccountPageService;
import com.hyjf.bank.service.user.bankopen.BankOpenService;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.vip.apply.ApplyDefine;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(AemsBankOpenEncryptPageDefine.REQUEST_MAPPING)
public class AemsBankOpenEncryptPageServer extends BaseController {

    Logger _log = LoggerFactory.getLogger(AemsBankOpenEncryptPageServer.class);

    @Autowired
    private UserOpenAccountService userOpenAccountService;

    @Autowired
    private UserOpenAccountPageService accountPageService;

    @Autowired
    private BankOpenService openAccountService;

    /**
     * 用户页面开户
     *
     * @param requestBean
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(AemsBankOpenEncryptPageDefine.OPEN_ACCOUNT_ACTION)
    public ModelAndView openAccont(@RequestBody AemsBankOpenEncryptPageRequestBean requestBean, HttpServletRequest request,
                                   HttpServletResponse response) {
        LogUtil.startLog(AemsBankOpenEncryptPageDefine.THIS_CLASS, AemsBankOpenEncryptPageDefine.OPEN_ACCOUNT_ACTION);
        ModelAndView modelAndView = new ModelAndView(AemsBankOpenEncryptPageDefine.PATH_OPEN_ACCOUNT_PAGE_ERROR);
        try {
            // 验证请求参数
            // 机构编号
            if (Validator.isNull(requestBean.getInstCode())) {
                getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_CE000001, "请求参数异常");
                _log.info("请求参数异常[" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            // 手机号
            if (Validator.isNull(requestBean.getMobile())) {
                getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_ZC000001, "手机号不能为空");
                _log.info("手机号不能为空[" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            if (Validator.isNull(requestBean.getRetUrl())) {
                getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_CE000001, "同步地址不能为空");
                _log.info("同步地址不能为空[" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            if (Validator.isNull(requestBean.getBgRetUrl())) {
                getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_CE000001, "异步地址不能为空");
                _log.info("异步地址不能为空[" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            // 姓名
            if (Validator.isNull(requestBean.getTrueName())) {
                getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_ZC000007, "姓名不能为空");
                _log.info("姓名不能为空[" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            // 渠道
            if (Validator.isNull(requestBean.getChannel())) {
                getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_ZC000006, "渠道号不能为空");
                _log.info("渠道号不能为空[" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            // 账户用途 写死
            requestBean.setAcctUse("00000");
            HjhInstConfig config = userOpenAccountService.selectInstConfigByInstCode(requestBean.getInstCode());
            if (config == null) {
                getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_ZC000004, "机构编号错误");
                _log.info("机构编号错误[" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            // 验签
            // 机构编号 bean.getInstCode() + bean.getMobile() + bean.getTrueName()+ bean.getRetUrl() + bean.getBgRetUrl() + bean.getTimestamp()
            if (!this.AEMSVerifyRequestSign(requestBean, AemsBankOpenEncryptPageDefine.REQUEST_MAPPING + AemsBankOpenEncryptPageDefine.OPEN_ACCOUNT_ACTION)) {
                _log.info("----验签失败----");
                getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_CE000002, "验签失败");
                _log.info("验签失败[" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            // 判断真实姓名是否包含特殊字符
            if (!ValidatorCheckUtil.verfiyChinaFormat(requestBean.getTrueName())) {
                getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_CE000001, "真实姓名包含特殊字符");
                _log.info("真实姓名包含特殊字符[" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            // 判断真实姓名的长度,不能超过10位
            if (requestBean.getTrueName().length() > 10) {
                getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_ZC000013, "真实姓名不能超过10位");
                _log.info("STATUS_ZC000013" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            // 判断身份属性
            if (Validator.isNull(requestBean.getIdentity()) || (!"1".equals(requestBean.getIdentity())
                    && !"2".equals(requestBean.getIdentity()) && !"3".equals(requestBean.getIdentity()))) {
                getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_ZC000024, "身份属性参数错误");
                _log.info("STATUS_ZC000024" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            // 根据手机号查询用户
            Users user = this.userOpenAccountService.selectUserByMobile(requestBean.getMobile());
            if (user == null) {
                getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_CE000003, "根据手机号查询用户失败");
                _log.info("根据手机号查询用户失败,手机号:[" + requestBean.getMobile() + "]");
                return modelAndView;
            }
            // 判断是否已经开户 AEMS系统追加
            if(this.isOpenAccount(user)){
                getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_CE000016, "用户重复开户");
                _log.info("用户已经开户,手机号:[" + requestBean.getMobile() + "]");
                return modelAndView;
            }
            // 拼装参数 调用江西银行
            // 同步调用路径
            String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
                    + AemsBankOpenEncryptPageDefine.REQUEST_MAPPING + AemsBankOpenEncryptPageDefine.RETURL_SYN_ACTION + ".do?acqRes="
                    + requestBean.getAcqRes() + "&phone=" + requestBean.getMobile();
            // 异步调用路(三方接口回调优化：地址拼接优化)
            String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
                    + AemsBankOpenEncryptPageDefine.REQUEST_MAPPING + AemsBankOpenEncryptPageDefine.RETURL_ASY_ACTION + ".do?acqRes="
                    + requestBean.getAcqRes() + "&phone=" + requestBean.getMobile() + "&roleId=" + requestBean.getIdentity() + "&openclient="
                    + requestBean.getPlatform() + "&callback=" + requestBean.getBgRetUrl().replace("#", "*-*-*");

            // 用户ID
            Integer userId = user.getUserId();
            OpenAccountPageBean openBean = new OpenAccountPageBean();
            PropertyUtils.copyProperties(openBean, requestBean);
            openBean.setUserId(userId);
            openBean.setIp(CustomUtil.getIpAddr(request));
            // 同步 异步
            openBean.setRetUrl(retUrl);
            openBean.setNotifyUrl(bgRetUrl);
            openBean.setCoinstName(config.getInstName());
            modelAndView = getCallbankMV(openBean, requestBean.getRetUrl().replace("#", "*-*-*"));
            //保存开户日志
            boolean isUpdateFlag = openAccountService.updateUserAccountLog(userId, user.getUsername(), requestBean.getMobile(), openBean.getOrderId(), openBean.getPlatform(), openBean.getTrueName(), openBean.getIdNo(), openBean.getCardNo());
            if (!isUpdateFlag) {
                _log.info("保存开户日志失败,手机号:[" + openBean.getMobile() + "],用户ID:[" + userId + "]");
                modelAndView = getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_CE999999, "保存开户日志失败");
                return modelAndView;
            }

            _log.info("开户end");
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            _log.info("开户异常,异常信息:[" + e.toString() + "]");
            return null;
        }
    }

    /**
     * 判断用户是否已经开户
     * @param user
     * @return
     */
    private boolean isOpenAccount(Users user) {
        if(user.getBankOpenAccount() == 1 && this.userOpenAccountService.existBankAccountId(user.getUserId())){
            return true;
        }
        return false;
    }

    private ModelAndView getCallbankMV(OpenAccountPageBean openBean, String url) {
        ModelAndView mv = new ModelAndView();
        // 根据身份证号码获取性别
        String gender = openBean.getGender();
        // 获取共同参数
        String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
        String bankInstCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
        String orderDate = GetOrderIdUtils.getOrderDate();
        String txDate = GetOrderIdUtils.getTxDate();
        String txTime = GetOrderIdUtils.getTxTime();
        String seqNo = GetOrderIdUtils.getSeqNo(6);
        String idType = BankCallConstant.ID_TYPE_IDCARD;
        // 调用开户接口
        BankCallBean openAccoutBean = new BankCallBean();
        openAccoutBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        openAccoutBean.setTxCode(BankCallConstant.TXCODE_ACCOUNT_OPEN_ENCRYPT_PAGE);// 消息类型(用户开户)
        openAccoutBean.setInstCode(bankInstCode);// 机构代码
        openAccoutBean.setBankCode(bankCode);
        openAccoutBean.setTxDate(txDate);
        openAccoutBean.setTxTime(txTime);
        openAccoutBean.setSeqNo(seqNo);
        openAccoutBean.setChannel(openBean.getChannel());
        openAccoutBean.setIdType(idType);
        openAccoutBean.setName(openBean.getTrueName());
        openAccoutBean.setGender(gender);
        openAccoutBean.setMobile(openBean.getMobile());
        // 代偿角色的账户类型为  00100-担保账户  其他的是 00000-普通账户
        if(openBean.getIdentity().equals("3")){
            openAccoutBean.setAcctUse(BankCallConstant.ACCOUNT_USE_GUARANTEE);
        }else{
            openAccoutBean.setAcctUse(BankCallConstant.ACCOUNT_USE_COMMON);
        }
        //openAccoutBean.setAcctUse(openBean.getAcctUse());

        openAccoutBean.setIdentity(openBean.getIdentity());
        openAccoutBean.setRetUrl(openBean.getRetUrl() + "&callback=" + url);
        openAccoutBean.setSuccessfulUrl(openBean.getRetUrl() + "&isSuccess=1&callback=" + url);
        openAccoutBean.setNotifyUrl(openBean.getNotifyUrl());
        openAccoutBean.setCoinstName(openBean.getCoinstName());
        // 页面调用必须传的
        String orderId = GetOrderIdUtils.getOrderId2(openBean.getUserId());
        openAccoutBean.setLogBankDetailUrl(BankCallConstant.BANK_URL_ACCOUNT_OPEN_ENCRYPT_PAGE);
        openAccoutBean.setLogOrderId(orderId);
        openAccoutBean.setLogOrderDate(orderDate);
        openAccoutBean.setLogUserId(String.valueOf(openBean.getUserId()));
        openAccoutBean.setLogRemark("外部服务接口:开户页面");
        openAccoutBean.setLogIp(openBean.getIp());
        openAccoutBean.setLogClient(Integer.parseInt(openBean.getPlatform()));
        openBean.setOrderId(orderId);
        _log.info("openAccoutBean:" + JSONObject.toJSONString(openAccoutBean));
        try {
            mv = BankCallUtils.callApi(openAccoutBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    private ModelAndView getErrorMV(AemsBankOpenEncryptPageRequestBean userOpenAccountRequestBean, ModelAndView modelAndView,
                                    String status, String des) {
        AemsBankOpenEncryptPageResultBean repwdResult = new AemsBankOpenEncryptPageResultBean();
        BaseResultBean resultBean = new BaseResultBean();
        resultBean.setStatusForResponse(status);
        repwdResult.setCallBackAction(userOpenAccountRequestBean.getRetUrl());
        repwdResult.set("chkValue", resultBean.getChkValue());
        repwdResult.set("status", resultBean.getStatus());
        repwdResult.set("phone", userOpenAccountRequestBean.getMobile());
        repwdResult.setAcqRes(userOpenAccountRequestBean.getAcqRes());
        if (AemsErrorCodeConstant.STATUS_CE000016.equals(status)){
            // 重复开户
            // 根据手机号查询用户
            Users user = this.userOpenAccountService.selectUserByMobile(userOpenAccountRequestBean.getMobile());
            if (user == null ){
                _log.info("根据手机号查询用户失败,手机号:[" + userOpenAccountRequestBean.getMobile() + "]");
                return modelAndView;
            }
            if (this.isOpenAccount(user)){
                // 如果已经开户,根据用户ID查询用户银行信息
                BankOpenAccount bankOpenAccount = this.userOpenAccountService.getBankOpenAccount(user.getUserId());
                if(bankOpenAccount != null){
                    repwdResult.set("accountId",bankOpenAccount.getAccount());
                    repwdResult.set("isOpenAccount","1");
                    UsersInfo usersInfo = this.userOpenAccountService.getUsersInfoByUserId(user.getUserId());
                    repwdResult.set("idNo",usersInfo.getIdcard());
                    BankCard bankCard = this.userOpenAccountService.selectBankCardByUserId(user.getUserId());
                    if (bankCard != null) {
                        repwdResult.set("cardNo", bankCard.getCardNo());
                        repwdResult.set("payAllianceCode", StringUtils.isNotBlank(bankCard.getPayAllianceCode()) ? bankCard.getPayAllianceCode() : "");
                    }
                }
            }
        }
        modelAndView.addObject("callBackForm", repwdResult);
        return modelAndView;
    }

    /**
     * 同步回调
     *
     * @param request
     * @param response
     * @param bean
     * @return
     * @author sunss
     */
    @RequestMapping(AemsBankOpenEncryptPageDefine.RETURL_SYN_ACTION)
    public ModelAndView pageReturn(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BankCallBean bean) {
        _log.info("页面开户同步回调start,请求参数为：【" + JSONObject.toJSONString(bean, true) + "】");
        ModelAndView modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRUSTEEPAY_VIEW);
        String isSuccess = request.getParameter("isSuccess");
        String phone = request.getParameter("phone");
        String url = request.getParameter("callback").replace("*-*-*", "#");
        // 成功了
        AemsBankOpenEncryptPageResultBean repwdResult = new AemsBankOpenEncryptPageResultBean();
        repwdResult.setCallBackAction(url);
        if ("1".equals(isSuccess)) {
            String accountId = userOpenAccountService.getBankOpenAccountByMobile((String) request.getParameter("phone"));
            // 成功
            BaseResultBean resultBean = new BaseResultBean();
            modelAndView.addObject("statusDesc", "页面开户成功");
            resultBean.setStatusForResponse(AemsErrorCodeConstant.SUCCESS);
            resultBean.setStatusDesc("页面开户成功");
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("acqRes", request.getParameter("acqRes"));
            repwdResult.set("accountId", accountId);
            repwdResult.set("phone", phone);
            repwdResult.set("status",resultBean.getStatus());
            repwdResult.set("statusDesc",resultBean.getStatusDesc());
            modelAndView.addObject("callBackForm", repwdResult);
            _log.info("页面开户同步第三方返回参数：" + JSONObject.toJSONString(repwdResult));
            return modelAndView;
        }
        repwdResult.setCallBackAction(request.getParameter("callback").replace("*-*-*", "#"));
        BaseResultBean resultBean = new BaseResultBean();
        modelAndView.addObject("statusDesc", "开户失败,调用银行接口失败");
        resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_CE999999);
        repwdResult.set("chkValue", resultBean.getChkValue());
        repwdResult.set("status", resultBean.getStatus());
        repwdResult.set("acqRes", request.getParameter("acqRes"));
        modelAndView.addObject("callBackForm", repwdResult);
        _log.info("页面开户同步第三方返回参数：" + JSONObject.toJSONString(repwdResult));
        return modelAndView;
    }

    /**
     * 异步回调
     */
    @ResponseBody
    @RequestMapping(AemsBankOpenEncryptPageDefine.RETURL_ASY_ACTION)
    public BankCallResult bgreturn(HttpServletRequest request, HttpServletResponse response,
                                   @ModelAttribute BankCallBean bean) {
        // 上送的异步地址里面有
        String phone = request.getParameter("phone");
        String openclient = request.getParameter("openclient");
        bean.setLogClient(Integer.parseInt(openclient));
        _log.info("页面开户异步回调start");
        BankCallResult result = new BankCallResult();
        BaseResultBean resultBean = new BaseResultBean();
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        String roleId = request.getParameter("roleId");
        bean.setIdentity(roleId);
        bean.setMobile(phone);
        bean.convert();
        int userId = Integer.parseInt(bean.getLogUserId());
        // 银行返回响应代码
        String retCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
        params.put("openStatus", bean.getStatus());
        // 开户失败
        if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode) && "0".equals(bean.getStatus())) {
            // 将开户记录状态改为4
            this.userOpenAccountService.updateUserAccountLog(userId, bean.getLogOrderId(), 4);
            // 根据银行相应代码,查询错误信息
            String retMsg = userOpenAccountService.getBankRetMsg(retCode);
            _log.info("开户失败,银行返回响应代码:[" + retCode + "],retMsg:[" + retMsg + "],订单号:[" + bean.getLogOrderId() + "].");
            params.put("status", AemsErrorCodeConstant.STATUS_CE999999);
            resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_CE999999);
            params.put("statusDesc", "开户失败,调用银行接口失败");
            result.setMessage("开户失败");
            params.put("chkValue", resultBean.getChkValue());
            params.put("acqRes", request.getParameter("acqRes"));
            result.setStatus(false);
            _log.info("页面开户异步第三方返回参数：" + JSONObject.toJSONString(params));
            CommonSoaUtils.noRetPostThree(request.getParameter("callback").replace("*-*-*", "#"), params);
            return result;
        }
        // 开户成功后,保存用户的开户信息
        boolean saveBankAccountFlag = this.accountPageService.updateUserAccount(bean);
        if (!saveBankAccountFlag) {
            _log.info("开户失败,保存用户的开户信息失败:[" + retCode + "],订单号:[" + bean.getLogOrderId() + "].");
            params.put("status", AemsErrorCodeConstant.STATUS_CE999999);
            resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_CE999999);
            params.put("statusDesc", "开户失败,调用银行接口失败");
            result.setMessage("开户失败");
            params.put("chkValue", resultBean.getChkValue());
            params.put("acqRes", request.getParameter("acqRes"));
            result.setStatus(false);
            _log.info("页面开户异步第三方返回参数：" + JSONObject.toJSONString(params));
            CommonSoaUtils.noRetPostThree(request.getParameter("callback").replace("*-*-*", "#"), params);
            return result;
        }
        // 保存银行卡信息
        boolean saveBankCardFlag = this.accountPageService.updateCardNoToBank(bean);
        if (!saveBankCardFlag) {
            _log.info("开户失败,保存银行卡信息失败:[" + retCode + "],订单号:[" + bean.getLogOrderId() + "].");
            params.put("status", AemsErrorCodeConstant.STATUS_CE999999);
            resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_CE999999);
            params.put("statusDesc", "开户失败,调用银行接口失败");
            result.setMessage("开户失败");
            params.put("chkValue", resultBean.getChkValue());
            params.put("acqRes", request.getParameter("acqRes"));
            result.setStatus(false);
            _log.info("页面开户异步第三方返回参数：" + JSONObject.toJSONString(params));
            CommonSoaUtils.noRetPostThree(request.getParameter("callback").replace("*-*-*", "#"), params);
            return result;
        }
        _log.info("开户成功,用户ID:[" + userId + ",用户电子账户号:[" + bean.getAccountId() + "]");
        // 开户成功后,发送CA认证MQ
        this.openAccountService.sendCAMQ(String.valueOf(userId));
        params.put("status", AemsErrorCodeConstant.SUCCESS);
        resultBean.setStatusForResponse(AemsErrorCodeConstant.SUCCESS);
        params.put("statusDesc", "开户成功");
        result.setMessage("开户成功");
        params.put("chkValue", resultBean.getChkValue());
        params.put("isOpenAccount", "1");
        params.put("accountId", bean.getAccountId());
        params.put("payAllianceCode", bean.getPayAllianceCode());
        params.put("idNo", bean.getIdNo());
        params.put("acqRes", request.getParameter("acqRes"));
        // 三方接口回调优化：增加返回手机号和银行卡号 add by liushouyi 20180821
        params.put("cardNo", bean.getCardNo());
        _log.info("页面开户异步第三方返回参数：" + JSONObject.toJSONString(params));
        result.setStatus(true);
        CommonSoaUtils.noRetPostThree(request.getParameter("callback").replace("*-*-*", "#"), params);
        return result;
    }
}
