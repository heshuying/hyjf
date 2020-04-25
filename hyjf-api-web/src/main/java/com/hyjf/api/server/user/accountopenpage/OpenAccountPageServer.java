package com.hyjf.api.server.user.accountopenpage;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
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

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.server.openaccount.UserOpenAccountService;
import com.hyjf.api.server.util.ErrorCodeConstant;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.user.accountopenpage.OpenAccountPageBean;
import com.hyjf.bank.service.user.accountopenpage.UserOpenAccountPageService;
import com.hyjf.bank.service.user.bankopen.BankOpenService;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.vip.apply.ApplyDefine;

/**
 * 用户页面开户
 *
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年1月24日 下午2:21:24
 */
@Controller
@RequestMapping(OpenAccountPageDefine.REQUEST_MAPPING)
public class OpenAccountPageServer extends BaseController {

    Logger _log = LoggerFactory.getLogger(OpenAccountPageServer.class);

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
    @ResponseBody
    @RequestMapping(OpenAccountPageDefine.OPEN_ACCOUNT_ACTION)
    public ModelAndView openAccont(@RequestBody OpenAccountPageRequestBean requestBean, HttpServletRequest request,
                                   HttpServletResponse response) {
        LogUtil.startLog(OpenAccountPageDefine.THIS_CLASS, OpenAccountPageDefine.OPEN_ACCOUNT_ACTION);
        ModelAndView modelAndView = new ModelAndView(OpenAccountPageDefine.PATH_OPEN_ACCOUNT_PAGE_ERROR);
        _log.info("用户页面开户第三方请求参数：" + JSONObject.toJSONString(requestBean));
        try {
            // 验证请求参数
            // 机构编号
            if (Validator.isNull(requestBean.getInstCode())) {
                getErrorMV(requestBean, modelAndView, ErrorCodeConstant.STATUS_CE000001, "请求参数异常");
                _log.info("请求参数异常[" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            // 手机号
            if (Validator.isNull(requestBean.getMobile())) {
                getErrorMV(requestBean, modelAndView, ErrorCodeConstant.STATUS_ZC000001, "手机号不能为空");
                _log.info("手机号不能为空[" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            if (Validator.isNull(requestBean.getRetUrl())) {
                getErrorMV(requestBean, modelAndView, ErrorCodeConstant.STATUS_CE000001, "同步地址不能为空");
                _log.info("同步地址不能为空[" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            if (Validator.isNull(requestBean.getBgRetUrl())) {
                getErrorMV(requestBean, modelAndView, ErrorCodeConstant.STATUS_CE000001, "异步地址不能为空");
                _log.info("异步地址不能为空[" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            // 姓名
            if (Validator.isNull(requestBean.getTrueName())) {
                getErrorMV(requestBean, modelAndView, ErrorCodeConstant.STATUS_ZC000007, "姓名不能为空");
                _log.info("姓名不能为空[" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            // 身份证号
            if (Validator.isNull(requestBean.getIdNo())) {
                getErrorMV(requestBean, modelAndView, ErrorCodeConstant.STATUS_ZC000008, "身份证号不能为空");
                _log.info("身份证号不能为空[" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            // 渠道
            if (Validator.isNull(requestBean.getChannel())) {
                getErrorMV(requestBean, modelAndView, ErrorCodeConstant.STATUS_ZC000006, "渠道号不能为空");
                _log.info("渠道号不能为空[" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            // 账户用途 写死
            requestBean.setAcctUse("00000");
            /*if (Validator.isNull(requestBean.getAcctUse())||!"00000".equals(requestBean.getAcctUse())) {
                getErrorMV(requestBean, modelAndView, ErrorCodeConstant.STATUS_CE000001, "账户用途参数错误");
                _log.info("账户用途参数错误[" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }*/
            HjhInstConfig config = userOpenAccountService.selectInstConfigByInstCode(requestBean.getInstCode());
            if (config == null) {
                getErrorMV(requestBean, modelAndView, ErrorCodeConstant.STATUS_ZC000004, "机构编号错误");
                _log.info("机构编号错误[" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            // 验签
            // 机构编号  mobile  idNo  cardNo  时间戳
            if (!this.verifyRequestSign(requestBean, OpenAccountPageDefine.REQUEST_MAPPING + OpenAccountPageDefine.OPEN_ACCOUNT_ACTION)) {
                _log.info("----验签失败----");
                getErrorMV(requestBean, modelAndView, ErrorCodeConstant.STATUS_CE000002, "验签失败");
                _log.info("验签失败[" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            // 判断真实姓名是否包含特殊字符
            if (!ValidatorCheckUtil.verfiyChinaFormat(requestBean.getTrueName())) {
                getErrorMV(requestBean, modelAndView, ErrorCodeConstant.STATUS_CE000001, "真实姓名包含特殊字符");
                _log.info("真实姓名包含特殊字符[" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            // 判断真实姓名的长度,不能超过10位
            if (requestBean.getTrueName().length() > 10) {
                getErrorMV(requestBean, modelAndView, ErrorCodeConstant.STATUS_ZC000013, "真实姓名不能超过10位");
                _log.info("STATUS_ZC000013" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            // 判断身份属性
            if (Validator.isNull(requestBean.getIdentity()) || (!"1".equals(requestBean.getIdentity())
                    && !"2".equals(requestBean.getIdentity()) && !"3".equals(requestBean.getIdentity()))) {
                getErrorMV(requestBean, modelAndView, ErrorCodeConstant.STATUS_ZC000024, "身份属性参数错误");
                _log.info("STATUS_ZC000024" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }

            if (requestBean.getIdNo().length() != 18) {
                getErrorMV(requestBean, modelAndView, ErrorCodeConstant.STATUS_ZC000021, "身份证(18位)校验位错误");
                _log.info("身份证(18位)校验位错误" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            String replaceIdNo = replaceIdNo(requestBean.getIdNo());
            requestBean.setIdNo(replaceIdNo);
            // 检查用户身份证号是否在汇盈已经存在
            boolean isOnly = userOpenAccountService.checkIdNo(requestBean.getIdNo());
            if (!isOnly) {
                getErrorMV(requestBean, modelAndView, ErrorCodeConstant.STATUS_ZC000014, "身份证已存在");
                _log.info("身份证已存在" + JSONObject.toJSONString(requestBean, true) + "]");
                return modelAndView;
            }
            // 根据手机号查询用户
            Users user = this.userOpenAccountService.selectUserByMobile(requestBean.getMobile());
            if (user == null) {
                getErrorMV(requestBean, modelAndView, ErrorCodeConstant.STATUS_CE000003, "根据手机号查询用户失败");
                _log.info("根据手机号查询用户失败,手机号:[" + requestBean.getMobile() + "]");
                return modelAndView;
            }

            // 拼装参数 调用江西银行
            // 同步调用路径
            String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
                    + OpenAccountPageDefine.REQUEST_MAPPING + OpenAccountPageDefine.RETURL_SYN_ACTION + ".do?acqRes="
                    + requestBean.getAcqRes() + "&phone=" + requestBean.getMobile();
            // 异步调用路(三方接口回调优化：地址拼接优化)
            String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
                    + OpenAccountPageDefine.REQUEST_MAPPING + OpenAccountPageDefine.RETURL_ASY_ACTION + ".do?acqRes="
                    + requestBean.getAcqRes() + "&phone=" + requestBean.getMobile() + "&roles=" + requestBean.getIdentity() + "&openclient="
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
                modelAndView = getErrorMV(requestBean, modelAndView, ErrorCodeConstant.STATUS_CE999999, "保存开户日志失败");
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

    private ModelAndView getCallbankMV(OpenAccountPageBean openBean, String url) {
        ModelAndView mv = new ModelAndView();
        // 根据身份证号码获取性别
        String gender = "";
        int sexInt = Integer.parseInt(openBean.getIdNo().substring(16, 17));// 性别   奇数为男，偶数为女
        if (sexInt % 2 == 0) {
            gender = "F";
        } else {
            gender = "M";
        }
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
        openAccoutBean.setTxCode(BankCallConstant.TXCODE_ACCOUNT_OPEN_PAGE);// 消息类型(用户开户)
        openAccoutBean.setInstCode(bankInstCode);// 机构代码
        openAccoutBean.setBankCode(bankCode);
        openAccoutBean.setTxDate(txDate);
        openAccoutBean.setTxTime(txTime);
        openAccoutBean.setSeqNo(seqNo);
        openAccoutBean.setChannel(openBean.getChannel());
        openAccoutBean.setIdType(idType);
        openAccoutBean.setIdNo(openBean.getIdNo());
        openAccoutBean.setName(openBean.getTrueName());
        openAccoutBean.setGender(gender);
        openAccoutBean.setMobile(openBean.getMobile());
        openAccoutBean.setAcctUse(openBean.getAcctUse());
        openAccoutBean.setIdentity(openBean.getIdentity());
        openAccoutBean.setRetUrl(openBean.getRetUrl() + "&callback=" + url);
        openAccoutBean.setSuccessfulUrl(openBean.getRetUrl() + "&isSuccess=1&callback=" + url);
        openAccoutBean.setNotifyUrl(openBean.getNotifyUrl());
        openAccoutBean.setCoinstName(openBean.getCoinstName());
        // 银行卡号
        openAccoutBean.setCardNo(openBean.getCardNo());

        // 页面调用必须传的
        String orderId = GetOrderIdUtils.getOrderId2(openBean.getUserId());
        openAccoutBean.setLogBankDetailUrl(BankCallConstant.BANK_URL_ACCOUNT_OPEN_PAGE);
        openAccoutBean.setLogOrderId(orderId);
        openAccoutBean.setLogOrderDate(orderDate);
        openAccoutBean.setLogUserId(String.valueOf(openBean.getUserId()));
        openAccoutBean.setLogRemark("外部服务接口:开户页面");
        openAccoutBean.setLogIp(openBean.getIp());
        openAccoutBean.setLogClient(Integer.parseInt(openBean.getPlatform()));

        // 跳转到汇付天下画面
        openBean.setOrderId(orderId);
        _log.info("openAccoutBean:" + JSONObject.toJSONString(openAccoutBean));
        try {
            mv = BankCallUtils.callApi(openAccoutBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    private ModelAndView getErrorMV(OpenAccountPageRequestBean userOpenAccountRequestBean, ModelAndView modelAndView,
                                    String status, String des) {
        OpenAccountPageResultBean repwdResult = new OpenAccountPageResultBean();
        BaseResultBean resultBean = new BaseResultBean();
        resultBean.setStatusForResponse(status);
        repwdResult.setCallBackAction(userOpenAccountRequestBean.getRetUrl());
        repwdResult.set("chkValue", resultBean.getChkValue());
        repwdResult.set("status", resultBean.getStatus());
        repwdResult.set("phone", userOpenAccountRequestBean.getMobile());
        repwdResult.setAcqRes(userOpenAccountRequestBean.getAcqRes());
        modelAndView.addObject("callBackForm", repwdResult);
        return modelAndView;
    }

    private String replaceIdNo(String idNo) {
        String lastString = idNo.substring(idNo.length() - 1);
        if ("x".equalsIgnoreCase(lastString)) {
            idNo = idNo.replace(idNo.charAt(idNo.length() - 1) + "", "X");
        }
        return idNo;
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
    @RequestMapping(OpenAccountPageDefine.RETURL_SYN_ACTION)
    public ModelAndView pageReturn(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BankCallBean bean) {
        _log.info("页面开户同步回调start,请求参数为：【" + JSONObject.toJSONString(bean, true) + "】");
        ModelAndView modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRUSTEEPAY_VIEW);
        String isSuccess = request.getParameter("isSuccess");
        String phone = request.getParameter("phone");
        String url = request.getParameter("callback").replace("*-*-*", "#");
        // 成功了
        OpenAccountPageResultBean repwdResult = new OpenAccountPageResultBean();
        repwdResult.setCallBackAction(url);
        if ("1".equals(isSuccess)) {
            String accountId = userOpenAccountService.getBankOpenAccountByMobile((String) request.getParameter("phone"));
            // 成功
            BaseResultBean resultBean = new BaseResultBean();
            modelAndView.addObject("statusDesc", "页面开户成功");

            resultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
            repwdResult.set("acqRes", request.getParameter("acqRes"));
            repwdResult.set("accountId", accountId);
            repwdResult.set("phone", phone);
            modelAndView.addObject("callBackForm", repwdResult);
            _log.info("页面开户同步第三方返回参数：" + JSONObject.toJSONString(repwdResult));
            return modelAndView;
        }
        String frontParams = request.getParameter("frontParams");
        if (StringUtils.isBlank(bean.getRetCode()) && StringUtils.isNotBlank(frontParams)) {
            JSONObject jsonParm = JSONObject.parseObject(frontParams);
            if (jsonParm.containsKey("RETCODE")) {
                bean.setRetCode(jsonParm.getString("RETCODE"));
            }
        }
        repwdResult.setCallBackAction(request.getParameter("callback").replace("*-*-*", "#"));

        // 银行返回响应代码
        String retCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
        // 银行卡与姓名不符
        if ("CP9919".equals(retCode)) {
            _log.info("开户失败,银行卡与姓名不符,用户ID:[" + bean.getLogUserId() + "],retCode:[" + retCode + "]");
            BaseResultBean resultBean = new BaseResultBean();
            modelAndView.addObject("statusDesc", "银行卡与姓名不符");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000016);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
            repwdResult.set("acqRes", request.getParameter("acqRes"));
            modelAndView.addObject("callBackForm", repwdResult);
            return modelAndView;
        }
        // 银行卡与证件不符
        if ("CP9920".equals(retCode)) {
            _log.info("开户失败,银行卡与证件不符,用户ID:[" + bean.getLogUserId() + "],retCode:[" + retCode + "]");
            BaseResultBean resultBean = new BaseResultBean();
            modelAndView.addObject("statusDesc", "银行卡与证件不符");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000017);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
            repwdResult.set("acqRes", request.getParameter("acqRes"));
            modelAndView.addObject("callBackForm", repwdResult);
            return modelAndView;
        }
        // 开户失败
        if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
            // 根据银行相应代码,查询错误信息
            String retMsg = userOpenAccountService.getBankRetMsg(retCode);
            _log.info("开户失败,银行返回响应代码:[" + retCode + "],retMsg:[" + retMsg + "]");

            BaseResultBean resultBean = new BaseResultBean();
            modelAndView.addObject("statusDesc", retMsg);
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
            repwdResult.set("acqRes", request.getParameter("acqRes"));
            modelAndView.addObject("callBackForm", repwdResult);
            return modelAndView;
        }
        _log.info("开户成功,用户ID:[" + bean.getLogUserId() + "]");

        if (bean != null
                && ((BankCallConstant.RESPCODE_SUCCESS.equals(retCode))
                || "JX900703".equals(retCode))) {

            String accountId = userOpenAccountService.getBankOpenAccountByMobile((String) request.getParameter("phone"));
            // 成功
            BaseResultBean resultBean = new BaseResultBean();
            modelAndView.addObject("statusDesc", "页面开户成功");
            resultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
            repwdResult.set("acqRes", request.getParameter("acqRes"));
            repwdResult.set("accountId", accountId);
            modelAndView.addObject("callBackForm", repwdResult);
            _log.info("页面开户同步第三方返回参数：" + JSONObject.toJSONString(repwdResult));
            return modelAndView;
        } else {
            String retMsg = userOpenAccountService.getBankRetMsg(retCode);
            _log.info("开户失败,银行返回响应代码:[" + retCode + "],retMsg:[" + retMsg + "]");

            BaseResultBean resultBean = new BaseResultBean();
            modelAndView.addObject("statusDesc", "开户失败,调用银行接口失败");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
            repwdResult.set("acqRes", request.getParameter("acqRes"));
            modelAndView.addObject("callBackForm", repwdResult);
            _log.info("页面开户同步第三方返回参数：" + JSONObject.toJSONString(repwdResult));
            return modelAndView;
        }
    }

    /**
     * 异步回调
     */
    @ResponseBody
    @RequestMapping(OpenAccountPageDefine.RETURL_ASY_ACTION)
    public BankCallResult bgreturn(HttpServletRequest request, HttpServletResponse response,
                                   @ModelAttribute BankCallBean bean) {
        // 上送的异步地址里面有
        String phone = request.getParameter("phone");
        String openclient = request.getParameter("openclient");
        String roles = request.getParameter("roles");
        bean.setLogClient(Integer.parseInt(openclient));
        _log.info("页面开户异步回调start");
        BankCallResult result = new BankCallResult();
        BaseResultBean resultBean = new BaseResultBean();
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        bean.setMobile(phone);
        bean.convert();
        int userId = Integer.parseInt(bean.getLogUserId());

        // 银行返回响应代码
        String retCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
        // 开户失败
        if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
            // 将开户记录状态改为4
            this.userOpenAccountService.updateUserAccountLog(userId, bean.getLogOrderId(), 4);
            // 根据银行相应代码,查询错误信息
            String retMsg = userOpenAccountService.getBankRetMsg(retCode);
            _log.info("开户失败,银行返回响应代码:[" + retCode + "],retMsg:[" + retMsg + "],订单号:[" + bean.getLogOrderId() + "].");
            params.put("status", ErrorCodeConstant.STATUS_CE999999);
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
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
            params.put("status", ErrorCodeConstant.STATUS_CE999999);
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            params.put("statusDesc", "开户失败,调用银行接口失败");
            result.setMessage("开户失败");
            params.put("chkValue", resultBean.getChkValue());
            params.put("acqRes", request.getParameter("acqRes"));
            result.setStatus(false);
            _log.info("页面开户异步第三方返回参数：" + JSONObject.toJSONString(params));
            CommonSoaUtils.noRetPostThree(request.getParameter("callback").replace("*-*-*", "#"), params);
            return result;
        }
        // 合规接口改造   修改用户属性为借款人  用户角色1出借人2借款人3垫付机构
        boolean saveRoleIdFlag = this.accountPageService.updateUserRoleId(userId, Integer.parseInt(roles));
        if (!saveRoleIdFlag) {
            _log.info("开户失败,修改用户属性信息失败:[" + retCode + "],订单号:[" + bean.getLogOrderId() + "].");
            params.put("status", ErrorCodeConstant.STATUS_CE999999);
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
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
            params.put("status", ErrorCodeConstant.STATUS_CE999999);
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
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
        params.put("status", ErrorCodeConstant.SUCCESS);
        resultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
        params.put("statusDesc", "开户成功");
        result.setMessage("开户成功");
        params.put("chkValue", resultBean.getChkValue());
        params.put("isOpenAccount", "1");
        params.put("accountId", bean.getAccountId());
        params.put("payAllianceCode", bean.getPayAllianceCode());
        params.put("acqRes", request.getParameter("acqRes"));
        // 三方接口回调优化：增加返回手机号和银行卡号 add by liushouyi 20180821
        
        params.put("cardNo", bean.getCardNo());
        _log.info("页面开户异步第三方返回参数：" + JSONObject.toJSONString(params));
        result.setStatus(true);
        CommonSoaUtils.noRetPostThree(request.getParameter("callback").replace("*-*-*", "#"), params);
        return result;
    }
}
