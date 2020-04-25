/**
 * Description:出借控制器
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 *           Created at: 2015年12月4日 下午2:32:36
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.web.user.openaccount;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.chinapnr.MerPriv;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.web.BaseController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.login.LoginService;
import com.hyjf.web.util.WebUtils;

/**
 * @package com.hyjf.web.user.recharge
 * @author GOGTZ-T
 * @date 2015/12/04 14:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = OpenAccountDefine.REQUEST_MAPPING)
public class OpenAccountController extends BaseController {
	
	Logger _log = LoggerFactory.getLogger(OpenAccountController.class);

    @Autowired
    private OpenAccountService openAccountService;

    @Autowired
    private LoginService loginService;

    @Autowired
    @Qualifier("mailProcesser")
    private MessageProcesser mailMessageProcesser;

    /**
     * 用户开户
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(OpenAccountDefine.INIT_OPENACCOUNT_ACTION)
    public ModelAndView initOpenAccount(HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.OPENACCOUNT_ACTION);
        ModelAndView modelAndView = new ModelAndView(OpenAccountDefine.INIT_OPENACCOUNT_PATH);
        WebViewUser user = WebUtils.getUser(request);
        int userId = user.getUserId();
        boolean accountFlag = user.isOpenAccount();
        if (accountFlag) {
            return new ModelAndView("redirect:/user/pandect/pandect.do");
        } else {
            int openStatus = 10; // 企业开户状态 0初始 1提交 2审核中 3 审核拒绝 4开户失败 5开户中 6开户成功
                                 // ? 10为手动命名 区分是否有过企业开户
            String remark = "";
            // 查找企业开户记录
            CorpOpenAccountRecord record = openAccountService.getCorpOpenAccountRecordByUserId(userId);
            if (record != null) {
                openStatus = record.getStatus();
                if ((openStatus == 0 || openStatus == 3 || openStatus == 4) && record.getRemark() != null) {
                    remark = record.getRemark();
                }
            }
            modelAndView.addObject("openStatus", openStatus);
            modelAndView.addObject("remark", remark);
            return modelAndView;
        }
    }

    /**
     * 企业用户开户
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(OpenAccountDefine.CORP_OPEN_ACCOUNT_ACTION)
    public ModelAndView openCorpAccount(HttpServletRequest request, HttpServletResponse response) {

        String busiCode = request.getParameter("busiCode");
        ModelAndView modelAndView = new ModelAndView();
        // 用户id
        WebViewUser user = WebUtils.getUser(request);
        Integer userId = user.getUserId();
        String message = "开户失败";
        String guarType = request.getParameter("guarType");
        if (StringUtils.isBlank(busiCode)) {
            message = "营业执照编号格式有误";
            modelAndView.setViewName(OpenAccountDefine.OPENACCOUNT_ERROR_PATH);
            modelAndView.addObject("openAccountDesc", message);
            return modelAndView;
        } else if (busiCode.length() > 20) {
            message = "营业执照编号不能超过20位！";
            modelAndView.setViewName(OpenAccountDefine.OPENACCOUNT_ERROR_PATH);
            modelAndView.addObject("openAccountDesc", message);
            return modelAndView;
        }
        // 取得用户在汇付天下的客户号
        AccountChinapnr accountChinapnr = openAccountService.getAccountChinapnr(userId);
        if (accountChinapnr != null && accountChinapnr.getId() != null) {
            message = "用户已开户";
            modelAndView.setViewName(OpenAccountDefine.OPENACCOUNT_ERROR_PATH);
            modelAndView.addObject("openAccountDesc", message);
            return modelAndView;
        }
        // 异步调用路
        String bgRetUrl =
                PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) + OpenAccountDefine.REQUEST_MAPPING
                        + OpenAccountDefine.CORP_OPEN_ACCOUNT_CALLBACK + ".do";
        ChinapnrBean bean = new ChinapnrBean();
        // 接口版本号
        bean.setVersion(ChinaPnrConstant.VERSION_10);
        // 消息类型(用户开户)
        bean.setCmdId(ChinaPnrConstant.CMDID_CORP_REGISTER);
        // 营业执照编号
        bean.setBusiCode(busiCode);
        // 页面异步返回URL(必须)
        bean.setBgRetUrl(bgRetUrl);
        // 商户私有域，存放开户平台,用户userId
        MerPriv merPriv = new MerPriv();
        merPriv.setUserId(userId);
        bean.setMerPrivPo(merPriv);
        // 拼装用户的汇付客户号
        String usrId = GetOrderIdUtils.getUsrId(userId);
        bean.setUsrId(usrId);
        if (StringUtils.isBlank(guarType)) {
            guarType = "N";
        }
        bean.setGuarType(guarType);
        // 插入企业用户开户记录
        int count = this.openAccountService.insertCorpOpenAccountRecord(userId, user.getUsername(), busiCode, guarType);
        if (count == 1) {
            // 跳转到汇付天下画面
            try {
                modelAndView = ChinapnrUtil.callApi(bean);
            } catch (Exception e) {
                e.printStackTrace();
                message = "调用汇付接口失败！";
                modelAndView = new ModelAndView(OpenAccountDefine.OPENACCOUNT_ERROR_PATH);
                modelAndView.addObject("openAccountDesc", message);
            }
        } else if (count == 2) {
            message = "请勿重复提交企业开户请求！如有问题，请联系客服！";
            modelAndView = new ModelAndView(OpenAccountDefine.OPENACCOUNT_ERROR_PATH);
            modelAndView.addObject("openAccountDesc", message);
        } else if (count == 3) {
            message = "系统错误，请联系客服！";
            modelAndView = new ModelAndView(OpenAccountDefine.OPENACCOUNT_ERROR_PATH);
            modelAndView.addObject("openAccountDesc", message);
        } else {
            message = "数据插入失败！";
            modelAndView = new ModelAndView(OpenAccountDefine.OPENACCOUNT_ERROR_PATH);
            modelAndView.addObject("openAccountDesc", message);
        }
        return modelAndView;
    }

    /**
     * 
     * 此处为方法说明
     * 
     * @author 王坤
     * @param request
     * @param response
     * @param bean
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(OpenAccountDefine.CORP_OPEN_ACCOUNT_CALLBACK)
    public ModelAndView corpOpenAccountCallBack(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute ChinapnrBean bean) throws UnsupportedEncodingException {

        LogUtil.startLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.CORP_OPEN_ACCOUNT_CALLBACK, "[企业开户异步回调开始]");
        ModelAndView modelAndView = new ModelAndView();
        bean.convert();
        MerPriv merPriv = bean.getMerPrivPo();
        _log.info("[企业开户异步回调开始]openAccountReturn bean:----------" + JSON.toJSONString(bean));
        int userId = merPriv.getUserId();
        String message = "";
        _log.info("企业开户异步回调:openAccountReturn------------------------------" + userId);
        String resCode = bean.getRespCode();
        _log.info("resCode:" + resCode);
        if ("508".equals(resCode)) {
            message = "用户已开户";
            modelAndView = new ModelAndView(OpenAccountDefine.JSP_CHINAPNR_RESULT);
            modelAndView.addObject("message", message);
            return modelAndView;
        }
        // 取得用户开户状态
        boolean updateFlag = false;
        Users user = this.openAccountService.selectUserById(userId);
        if (user == null) {
            message = "用户未注册，请先注册！";
            modelAndView = new ModelAndView(OpenAccountDefine.JSP_CHINAPNR_RESULT);
            modelAndView.addObject("message", message);
            return modelAndView;
        } else {
            int openAccountFlag = user.getOpenAccount();
            if (openAccountFlag == 0) {
                updateFlag = true;
            }
        }
        // 用户未开户
        if (updateFlag) {
            // 发送状态
            String status = ChinaPnrConstant.STATUS_VERTIFY_OK;
            // 返回码
            String respCode = bean.get(ChinaPnrConstant.PARAM_RESPCODE);
            // 成功或审核中
            if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode) || ChinaPnrConstant.RESPCODE_CHECK.equals(respCode)
                    || ChinaPnrConstant.RESPCODE_216.equals(respCode) || ChinaPnrConstant.RESPCODE_217.equals(respCode)
                    || ChinaPnrConstant.RESPCODE_218.equals(respCode) || ChinaPnrConstant.RESPCODE_219.equals(respCode)) {
                try {
                    // 插值用参数
                    bean.setLogUserId(userId);
                    bean.setLogIp(CustomUtil.getIpAddr(request));
                    bean.setLogClient("0");
                    // 开户后保存相应的数据以及日志
                    boolean flag = false;
                    flag = this.openAccountService.corpOpenAccount(bean);
                    if (!flag) {
                        message = "企业开户失败！请联系系统管理员！";
                        // 执行结果(失败)
                        status = ChinaPnrConstant.STATUS_FAIL;
                        LogUtil.endLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION,
                                "[企业开户完成后,更新用户信息失败]");
                    } else {
                        message = "RECV_ORD_ID_" + bean.getTrxId();
                        // 执行结果(成功)
                        status = ChinaPnrConstant.STATUS_SUCCESS;
                        LogUtil.debugLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION, "成功");
                    }
                } catch (Exception e) {
                    message = "企业开户失败！请联系系统管理员！";
                    // 执行结果(失败)
                    status = ChinaPnrConstant.STATUS_FAIL;
                    LogUtil.errorLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION, e);
                }
            } else {
                if (StringUtils.isNotBlank(bean.getRespDesc())) {
                    message = URLDecoder.decode(bean.getRespDesc(), "UTF-8");
                } else {
                    message = "企业开户失败！请联系系统管理员！";
                }
                // 执行结果(失败)
                status = ChinaPnrConstant.STATUS_FAIL;
                LogUtil.debugLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION, "失败");
            }
            if (ChinaPnrConstant.STATUS_SUCCESS.equals(status)) {
                modelAndView = new ModelAndView(OpenAccountDefine.JSP_CHINAPNR_RESULT);
                modelAndView.addObject("message", message);
                WebViewUser webUser = loginService.getWebViewUserByUserId(userId);
                WebUtils.sessionLogin(request, response, webUser);
                LogUtil.endLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
                return modelAndView;
            } else {
                modelAndView = new ModelAndView(OpenAccountDefine.JSP_CHINAPNR_RESULT);
                modelAndView.addObject("message", message);
                LogUtil.endLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
                return modelAndView;
            }
        } else {
            message = "RECV_ORD_ID_" + bean.getTrxId();
            modelAndView = new ModelAndView(OpenAccountDefine.JSP_CHINAPNR_RESULT);
            modelAndView.addObject("message", message);
            WebViewUser webUser = loginService.getWebViewUserByUserId(userId);
            WebUtils.sessionLogin(request, response, webUser);
            LogUtil.endLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
            return modelAndView;
        }
    }

    /**
     * 普通用户开户
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(OpenAccountDefine.OPENACCOUNT_ACTION)
    public ModelAndView openAccount(HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.OPENACCOUNT_ACTION);
        ModelAndView modelAndView = new ModelAndView();
        // 用户id
        Integer userId = WebUtils.getUserId(request);
        String message = "开户失败";
        if (userId == null) {
            modelAndView = new ModelAndView(OpenAccountDefine.OPENACCOUNT_ERROR_PATH);
            modelAndView.addObject("openAccountDesc", message);
            return modelAndView;
        }
        _log.info("initOpenAccount ShiroUtil.getLoginUserId----------------------:" + userId);
        String mobile = this.openAccountService.getUsersMobile(userId);
        // 取得用户在汇付天下的客户号
        AccountChinapnr accountChinapnr = openAccountService.getAccountChinapnr(userId);
        if (accountChinapnr != null && accountChinapnr.getId() != null) {
            _log.info("initOpenAccount accountChinapnrTender----------------------:"
                    + JSON.toJSONString(accountChinapnr));
            message = "用户已开户";
            modelAndView = new ModelAndView(OpenAccountDefine.OPENACCOUNT_ERROR_PATH);
            modelAndView.addObject("openAccountDesc", message);
            LogUtil.endLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.OPENACCOUNT_ACTION, "[用户已开户]");
            return modelAndView;
        }

        // 调用开户接口
        // 同步调用路径
        String retUrl =
                PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + OpenAccountDefine.REQUEST_MAPPING
                        + OpenAccountDefine.RETURL_SYN_ACTION + ".do";
        // 异步调用路
        String bgRetUrl =
                PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) + OpenAccountDefine.REQUEST_MAPPING
                        + OpenAccountDefine.RETURN_ASY_ACTION + ".do";
        ChinapnrBean bean = new ChinapnrBean();
        // 接口版本号
        bean.setVersion(ChinaPnrConstant.VERSION_10);
        // 消息类型(用户开户)
        bean.setCmdId(ChinaPnrConstant.CMDID_USER_REGISTER);
        // 页面同步返回 URL
        bean.setRetUrl(retUrl);
        // 页面异步返回URL(必须)
        bean.setBgRetUrl(bgRetUrl);
        // 用户手机
        if (StringUtils.isNotBlank(mobile)) {
            bean.setUsrMp(mobile);
        }
        // 商户私有域，存放开户平台,用户userId
        MerPriv merPriv = new MerPriv();
        merPriv.setUserId(userId);
        merPriv.setClient("0");
        bean.setMerPrivPo(merPriv);
        // 拼装用户的汇付客户号
        String usrId = GetOrderIdUtils.getUsrId(userId);
        bean.setUsrId(usrId);
        // 写log用参数
        // 操作者ID
        bean.setLogUserId(userId);
        // 备注
        bean.setLogRemark("用户开户");
        // 账户开通平台 0pc 1微信 2安卓 3IOS 4其他
        bean.setLogClient("0");
        // IP地址
        bean.setLogIp(CustomUtil.getIpAddr(request));
        // 跳转到汇付天下画面
        try {
            modelAndView = ChinapnrUtil.callApi(bean);
            _log.info("发送开户请求！");
            LogUtil.endLog(OpenAccountController.class.toString(), OpenAccountDefine.OPENACCOUNT_ACTION);
        } catch (Exception e) {
            e.printStackTrace();
            message = "调用汇付接口失败！";
            modelAndView = new ModelAndView(OpenAccountDefine.OPENACCOUNT_ERROR_PATH);
            modelAndView.addObject("openAccountDesc", message);
            LogUtil.errorLog(OpenAccountController.class.toString(), OpenAccountDefine.OPENACCOUNT_ACTION, e);
        }
        return modelAndView;
    }

    /**
     * 用户开户同步处理
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(OpenAccountDefine.RETURL_SYN_ACTION)
    public ModelAndView openAccountReturn(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute ChinapnrBean bean) {

        LogUtil.startLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION, "[开户同步回调开始]");
        ModelAndView modelAndView = new ModelAndView();
        bean.convert();
        MerPriv merPriv = bean.getMerPrivPo();
        _log.info("[开户同步回调开始]openAccountReturn bean:----------" + JSON.toJSONString(bean));
        int userId = merPriv.getUserId();
        String client = merPriv.getClient();
        String message = "";
        _log.info("用户开户同步调用:openAccountReturn------------------------------" + userId);
        String resCode = bean.getRespCode();
        _log.info("resCode:" + resCode);
        if ("508".equals(resCode)) {
            message = "用户已开户";
            modelAndView = new ModelAndView(OpenAccountDefine.OPENACCOUNT_ERROR_PATH);
            modelAndView.addObject("openAccountDesc", message);
            return modelAndView;
        }
        // 取得用户开户状态
        boolean updateFlag = false;
        Users user = this.openAccountService.selectUserById(userId);
        if (user == null) {
            message = "用户未注册，请先注册！";
            modelAndView = new ModelAndView(OpenAccountDefine.OPENACCOUNT_ERROR_PATH);
            modelAndView.addObject("openAccountDesc", message);
            return modelAndView;
        } else {
            int openAccountFlag = user.getOpenAccount();
            if (openAccountFlag == 0) {
                updateFlag = true;
            }
        }
        // 用户未开户
        if (updateFlag) {
            // 发送状态
            String status = ChinaPnrConstant.STATUS_VERTIFY_OK;
            // 成功或审核中
            if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))
                    || ChinaPnrConstant.RESPCODE_CHECK.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
                try {
                    // 插值用参数
                    bean.setLogUserId(userId);
                    bean.setLogIp(CustomUtil.getIpAddr(request));
                    bean.setLogClient(client);
                    // 开户后保存相应的数据以及日志
                    boolean flag = false;
                    flag = this.openAccountService.userOpenAccount(bean);
                    if (!flag) {
                        Users user2 = openAccountService.selectUserById(userId);
                        if (user2.getOpenAccount().intValue() == 1) {
                            // 执行结果(成功)
                            status = ChinaPnrConstant.STATUS_SUCCESS;
                            LogUtil.debugLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION, "成功");
                        } else {
                            // 执行结果(失败)
                            status = ChinaPnrConstant.STATUS_FAIL;
                            LogUtil.endLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION,
                                    "[开户完成后,更新用户信息失败]");
                        }
                    } else {
                        // 执行结果(成功)
                        status = ChinaPnrConstant.STATUS_SUCCESS;
                        LogUtil.debugLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION, "成功");
                        String mobile = bean.getUsrMp();
                        JSONObject params = this.openAccountService.selectUserByMobile(userId, mobile);
                        if (params != null) {
                            Map<String, String> replaceMap = new HashMap<String, String>();
                            replaceMap.put("val_name_new", params.getString("newUserName"));
                            replaceMap.put("val_name_old", params.getString("oldUserName"));
                            replaceMap.put("val_mobile", mobile);
                            String[] email = { "sunjijin@hyjf.com", "gaohonggang@hyjf.com", "liudandan@hyjf.com" };
                            MailMessage messageMail =
                                    new MailMessage(null, replaceMap, "用户开户", null, null, email,
                                            CustomConstants.EMAILPARAM_TPL_REPEATMOBILE,
                                            MessageDefine.MAILSENDFORMAILINGADDRESS);
                            mailMessageProcesser.gather(messageMail);
                        }
                    }
                } catch (Exception e) {
                    // 执行结果(失败)
                    status = ChinaPnrConstant.STATUS_FAIL;
                    LogUtil.errorLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION, e);
                }
            } else {
                // 执行结果(失败)
                status = ChinaPnrConstant.STATUS_FAIL;
                LogUtil.debugLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION, "失败");
            }

            if (ChinaPnrConstant.STATUS_SUCCESS.equals(status)) {
                message = "开户成功";
                modelAndView = new ModelAndView(OpenAccountDefine.OPENACCOUNT_SUCCESS_PATH);
                modelAndView.addObject("openAccountDesc", message);
                WebViewUser webUser = loginService.getWebViewUserByUserId(userId);
                WebUtils.sessionLogin(request, response, webUser);
                LogUtil.endLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
                return modelAndView;
            } else {
                message = "开户失败";
                modelAndView = new ModelAndView(OpenAccountDefine.OPENACCOUNT_ERROR_PATH);
                modelAndView.addObject("openAccountDesc", message);
                LogUtil.endLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
                return modelAndView;
            }

        } else {
            message = "开户成功！";
            modelAndView = new ModelAndView(OpenAccountDefine.OPENACCOUNT_SUCCESS_PATH);
            modelAndView.addObject("openAccountDesc", message);
            WebViewUser webUser = loginService.getWebViewUserByUserId(userId);
            WebUtils.sessionLogin(request, response, webUser);
            LogUtil.endLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
            return modelAndView;
        }

    }

    /**
     * 用户开户异步处理
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(OpenAccountDefine.RETURN_ASY_ACTION)
    public ModelAndView openAsyAccountReturn(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute ChinapnrBean bean) {

        LogUtil.startLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.RETURN_ASY_ACTION, "[开户同步回调开始]");
        bean.convert();
        MerPriv merPriv = bean.getMerPrivPo();
        ModelAndView modelAndView = new ModelAndView();
        _log.info("[开户异步回调开始]openAccountReturn bean:----------" + JSON.toJSONString(bean));
        int userId = merPriv.getUserId();
        String client = merPriv.getClient();
        String message = "开户失败";
        _log.info("用户开户异步调用:openAccountReturn------------------------------" + userId);
        String resCode = bean.getRespCode();
        _log.info("resCode:" + resCode);
        if ("508".equals(resCode)) {
            message = "用户已开户";
            modelAndView = new ModelAndView(OpenAccountDefine.OPENACCOUNT_ERROR_PATH);
            modelAndView.addObject("openAccountDesc", message);
            return modelAndView;
        }
        // 查询用户开户状态
        boolean updateFlag = false;
        Users user = this.openAccountService.selectUserById(userId);
        if (user == null) {
            message = "用户未注册，请先注册！";
            modelAndView = new ModelAndView(OpenAccountDefine.OPENACCOUNT_ERROR_PATH);
            modelAndView.addObject("openAccountDesc", message);
            return modelAndView;
        } else {
            int openAccountFlag = user.getOpenAccount();
            if (openAccountFlag == 0) {
                updateFlag = true;
            }
        }
        // 用户未开户
        if (updateFlag) {
            // 发送状态
            String status = ChinaPnrConstant.STATUS_VERTIFY_OK;
            // 成功或审核中
            if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))
                    || ChinaPnrConstant.RESPCODE_CHECK.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
                try {
                    // 插值用参数
                    bean.setLogUserId(userId);
                    bean.setLogIp(CustomUtil.getIpAddr(request));
                    bean.setLogClient(client);
                    // 开户后保存相应的数据以及日志
                    boolean flag = this.openAccountService.userOpenAccount(bean);
                    if (!flag) {
                        Users user2 = openAccountService.selectUserById(userId);
                        if (user2.getOpenAccount().intValue() == 1) {
                            // 执行结果(成功)
                            status = ChinaPnrConstant.STATUS_SUCCESS;
                            LogUtil.debugLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION, "成功");
                        } else {
                            // 执行结果(失败)
                            status = ChinaPnrConstant.STATUS_FAIL;
                            LogUtil.endLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.RETURL_SYN_ACTION,
                                    "[开户完成后,更新用户信息失败]");
                        }
                    } else {
                        // 执行结果(成功)
                        status = ChinaPnrConstant.STATUS_SUCCESS;
                        LogUtil.debugLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.RETURN_ASY_ACTION, "成功");
                        String mobile = bean.getUsrMp();
                        JSONObject params = this.openAccountService.selectUserByMobile(userId, mobile);
                        if (params != null) {
                            Map<String, String> replaceMap = new HashMap<String, String>();
                            replaceMap.put("val_name_new", params.getString("newUserName"));
                            replaceMap.put("val_name_old", params.getString("oldUserName"));
                            replaceMap.put("val_mobile", mobile);
                            String[] email = { "sunjijin@hyjf.com", "gaohonggang@hyjf.com", "liudandan@hyjf.com" };
                            MailMessage messageMail =
                                    new MailMessage(null, replaceMap, "用户开户", null, null, email,
                                            CustomConstants.EMAILPARAM_TPL_REPEATMOBILE,
                                            MessageDefine.MAILSENDFORMAILINGADDRESS);
                            mailMessageProcesser.gather(messageMail);
                        }
                    }
                } catch (Exception e) {
                    // 执行结果(失败)
                    status = ChinaPnrConstant.STATUS_FAIL;
                    LogUtil.errorLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.RETURN_ASY_ACTION, e);
                }

            } else {
                // 执行结果(失败)
                status = ChinaPnrConstant.STATUS_FAIL;
                LogUtil.debugLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.RETURN_ASY_ACTION, "失败");
            }

            if (ChinaPnrConstant.STATUS_SUCCESS.equals(status)) {
                message = "开户成功";
                modelAndView = new ModelAndView(OpenAccountDefine.OPENACCOUNT_SUCCESS_PATH);
                modelAndView.addObject("openAccountDesc", message);
                WebViewUser webUser = loginService.getWebViewUserByUserId(userId);
                WebUtils.sessionLogin(request, response, webUser);
                return modelAndView;
            } else {
                message = "操作失败";
                modelAndView = new ModelAndView(OpenAccountDefine.OPENACCOUNT_ERROR_PATH);
                modelAndView.addObject("openAccountDesc", message);
                return modelAndView;
            }
        } else {
            message = "开户成功！";
            modelAndView = new ModelAndView(OpenAccountDefine.OPENACCOUNT_SUCCESS_PATH);
            modelAndView.addObject("openAccountDesc", message);
            WebViewUser webUser = loginService.getWebViewUserByUserId(userId);
            WebUtils.sessionLogin(request, response, webUser);
            return modelAndView;
        }

    }

}
