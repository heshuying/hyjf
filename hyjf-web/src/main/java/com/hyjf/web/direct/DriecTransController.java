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
package com.hyjf.web.direct;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.hyjf.common.chinapnr.MerPriv;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogWithBLOBs;
import com.hyjf.mybatis.model.auto.DirectionalTransferAssociatedRecords;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.web.BaseController;
import com.hyjf.web.chinapnr.ChinapnrService;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.util.WebUtils;

/**
 * 定向转账接口
 * @author Michael
 */
@Controller
@RequestMapping(value = DriecTransDefine.REQUEST_MAPPING)
public class DriecTransController extends BaseController {
    /** THIS_CLASS */
    private static final String THIS_CLASS = DriecTransController.class.getName();

    @Autowired
    private DriecTransService driecTransService;

    @Autowired
    private ChinapnrService chinapnrService;

    @Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;

    /**
     * 用户绑定页面
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = DriecTransDefine.BIND_USER_INIT)
    public ModelAndView bindInit(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, DriecTransDefine.BIND_USER_INIT);
        ModelAndView modelAndView = new ModelAndView(DriecTransDefine.BIND_USER_PAGE);
        DriecTransBean driecTransBean = new DriecTransBean();
        Integer userId = WebUtils.getUserId(request);
        WebViewUser users = this.driecTransService.getWebViewUserByUserId(userId);
        driecTransBean.setOutUserId(users.getUserId());
        driecTransBean.setOutUserName(users.getUsername());
        driecTransBean.setOutTureName(users.getTruename());
        driecTransBean.setOutMobile(users.getMobile());
        modelAndView.addObject(DriecTransDefine.FORM, driecTransBean);
        LogUtil.endLog(THIS_CLASS, DriecTransDefine.BIND_USER_INIT);
        return modelAndView;
    }

    /**
     * 定向转账页面
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = DriecTransDefine.DIRECT_TRANS_INIT)
    public ModelAndView directInit(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, DriecTransDefine.DIRECT_TRANS_INIT);
        ModelAndView modelAndView = new ModelAndView(DriecTransDefine.DIRECT_TRANS_PAGE);
        DriecTransBean driecTransBean = new DriecTransBean();
        // 转出用户信息
        Integer userId = WebUtils.getUserId(request);
        WebViewUser outusers = this.driecTransService.getWebViewUserByUserId(userId);
        driecTransBean.setOutUserId(outusers.getUserId());
        driecTransBean.setOutUserName(outusers.getUsername());
        driecTransBean.setOutTureName(outusers.getTruename());
        driecTransBean.setOutMobile(outusers.getMobile());
        driecTransBean.setOutChinapnrUsrcustid(outusers.getChinapnrUsrcustid());
        driecTransBean.setOutUserBalance(this.driecTransService.getAccount(outusers.getUserId()).getBalance());
        // 获取绑定用户
        DirectionalTransferAssociatedRecords ditTrans = driecTransService.getDirectByOutUserId(outusers.getUserId());
        // 转入用户信息
        WebViewUser inusers = driecTransService.getWebViewUserByUserId(ditTrans.getShiftToUserId());
        driecTransBean.setInUserId(inusers.getUserId());
        driecTransBean.setInUserName(inusers.getUsername());
        driecTransBean.setInTureName(inusers.getTruename());
        driecTransBean.setInMobile(inusers.getMobile());
        driecTransBean.setInChinapnrUsrcustid(inusers.getChinapnrUsrcustid());
        driecTransBean.setInUserBalance(this.driecTransService.getAccount(inusers.getUserId()).getBalance());
        modelAndView.addObject(DriecTransDefine.FORM, driecTransBean);
        LogUtil.endLog(THIS_CLASS, DriecTransDefine.DIRECT_TRANS_INIT);
        return modelAndView;
    }

    /**
     * 检查用户名是否存在
     * 
     * @param request
     * @param form
     * @return
     */
    @ResponseBody
    @RequestMapping(value = DriecTransDefine.CHECK_MAPPING, produces = "application/json; charset=UTF-8")
    public String check(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, DriecTransDefine.CHECK_MAPPING);
        // 返回信息
        Map<String, String> result = new HashMap<String, String>();
        // 登陆用户id
        int userid = WebUtils.getUserId(request);
        // 用户名
        String username = request.getParameter("inUserName");
        if (StringUtils.isEmpty(username)) { // 如果用户名为空
            result.put("error", "1");
            result.put("data", "用户名不能为空");
            return JSON.toJSONString(result);
        }
        WebViewUser user = this.driecTransService.getWebViewUserByUserName(username);

        if (user != null) {
            // 不可绑定本人
            if (userid == user.getUserId()) {
                result.put("error", "1");
                result.put("data", "不可绑定本人，请输入正确的用户名");
                return JSON.toJSONString(result);
            }
            // 必须要开户
            if (!user.isOpenAccount()) {
                result.put("error", "1");
                result.put("data", "该用户未开户，不可绑定");
                return JSON.toJSONString(result);
            }
            // 用户必须为启用状态
            Users u = this.driecTransService.getUsers(user.getUserId());
            if (u.getStatus() != 0) {
                result.put("error", "1");
                result.put("data", "该用户已禁用");
                return JSON.toJSONString(result);
            }
            result.put("error", "0");
            result.put("userid", String.valueOf(user.getUserId()));
            result.put("truename", user.getTruename());
            result.put("mobile", user.getMobile());
        } else {
            result.put("error", "1");
            result.put("data", "该用户不存在");
        }
        String res = JSON.toJSONString(result);
        LogUtil.endLog(THIS_CLASS, DriecTransDefine.CHECK_MAPPING);
        return res;
    }

    /**
     * 定向转账用户绑定
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = DriecTransDefine.BIND_USER_MAPPING)
    public ModelAndView bindUser(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute DriecTransBean form) {
        LogUtil.startLog(THIS_CLASS, DriecTransDefine.BIND_USER_MAPPING);
        ModelAndView modelAndView = new ModelAndView(DriecTransDefine.JSP_CHINAPNR_SEND);
        // 企业用户与绑定用户不可为空
        if (form.getInUserId() == null || form.getOutUserId() == null) {
            modelAndView = new ModelAndView(DriecTransDefine.BIND_ERROR_PAGE);
            modelAndView.addObject("message", "绑定账户失败,请重新登陆绑定");
            return modelAndView;
        }
        // 验证企业用户是否已禁用
        Users user = this.driecTransService.getUsers(form.getOutUserId());
        if (user != null) {
            if (user.getStatus() == 1) { // 已禁用
                modelAndView = new ModelAndView(DriecTransDefine.BIND_ERROR_PAGE);
                modelAndView.addObject("message", "绑定账户失败,您的账户已被禁用");
                return modelAndView;
            }
        }
        // 如果已绑定成功不可绑定
        DirectionalTransferAssociatedRecords dinfo = this.driecTransService.getDirectByOutUserId(form.getOutUserId());
        if (dinfo != null) {
            modelAndView = new ModelAndView(DriecTransDefine.BIND_ERROR_PAGE);
            modelAndView.addObject("message", "绑定账户失败,该账户已绑定用户不可重复绑定");
            return modelAndView;
        }
        // 取得用户在汇付天下的客户号(转出账户)
        AccountChinapnr accountChinapnrTender = this.driecTransService.getAccountChinapnr(form.getOutUserId());
        // 取得用户在汇付天下的客户号(转入账户)
        AccountChinapnr accountChinapnrReceiver = this.driecTransService.getAccountChinapnr(form.getInUserId());
        // 调用汇付接口(定向支付授权接口)
        Properties properties = PropUtils.getSystemResourcesProperties();
        String host = properties.getProperty("hyjf.web.host").trim();
        String retUrl = host + DriecTransDefine.REQUEST_MAPPING + DriecTransDefine.BIND_USER_RETURN_MAPPING; // 返回路径
        // String retBgUrl = host + request.getContextPath() +
        // HtlCommonDefine.REQUEST_MAPPING+HtlCommonDefine.BUY_RETURN_MAPPING;
        // //返回路径
        ChinapnrBean bean = new ChinapnrBean();
        bean.setVersion(ChinaPnrConstant.VERSION_10);// 接口版本号
        bean.setCmdId(ChinaPnrConstant.CMDID_DIRECT_TRF_AUTH); // 消息类型(定向转账接口授权)
        bean.setUsrCustId(String.valueOf(accountChinapnrTender.getChinapnrUsrcustid()));// 用户客户号
                                                                                        // (转出)
        bean.setInUsrCustId(String.valueOf(accountChinapnrReceiver.getChinapnrUsrcustid()));// 用户客户号(转入)
        bean.setAuthAmt(CustomConstants.DIRECT_AUTHAMT);
        bean.setRetUrl(retUrl); // 页面返回
        bean.setBgRetUrl(ChinapnrUtil.getBgRetUrl()); // 商户后台应答地址(必须)
        // 调用接口前操作
        try {
            form.setOutChinapnrUsrcustid(accountChinapnrTender.getChinapnrUsrcustid());
            form.setInChinapnrUsrcustid(accountChinapnrReceiver.getChinapnrUsrcustid());
            // 查询该用户是否与其他用户进行了绑定
            DirectionalTransferAssociatedRecords transferAssociatedRecord =
                    this.driecTransService.getDirectByOutUserId(user.getUserId(), 0);
            Integer recordId = 0;
            if (null != transferAssociatedRecord) {
                recordId = transferAssociatedRecord.getId();
                if (transferAssociatedRecord.getAssociatedState() == 0) {// 如果有绑定记录，并且没有绑定成功
                    this.driecTransService.updateTransferAssociatedRecord(transferAssociatedRecord, form.getInUserId());
                }
            } else {
                recordId = this.driecTransService.insertBindUser(form);
            }
            // 插入日志
            this.driecTransService.insertDirectLog(form, 0);
            // 如果为0
            if (recordId == 0) {
                modelAndView = new ModelAndView(DriecTransDefine.ERROR_PAGE);
                return modelAndView;
            }
            // 跳转到汇付天下画面
            modelAndView = ChinapnrUtil.callApi(bean);
        } catch (Exception e) {
            // 错误页面
            modelAndView = new ModelAndView(DriecTransDefine.ERROR_PAGE);
        }
        LogUtil.endLog(THIS_CLASS, DriecTransDefine.BIND_USER_MAPPING);
        return modelAndView;
    }

    /**
     * 定向转账用户绑定
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = DriecTransDefine.UPDATE_BIND_USER_MAPPING)
    public ModelAndView updateBindUser(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute DriecTransBean form) {
        LogUtil.startLog(THIS_CLASS, DriecTransDefine.BIND_USER_MAPPING);
        ModelAndView modelAndView = new ModelAndView(DriecTransDefine.JSP_CHINAPNR_SEND);
        // 企业用户与绑定用户不可为空
        if (form.getInUserId() == null || form.getOutUserId() == null) {
            modelAndView = new ModelAndView(DriecTransDefine.BIND_ERROR_PAGE);
            modelAndView.addObject("message", "绑定账户失败,请重新登陆绑定");
            return modelAndView;
        }
        // 验证企业用户是否已禁用
        Users user = this.driecTransService.getUsers(form.getOutUserId());
        if (user != null) {
            if (user.getStatus() == 1) { // 已禁用
                modelAndView = new ModelAndView(DriecTransDefine.BIND_ERROR_PAGE);
                modelAndView.addObject("message", "绑定账户失败,您的账户已被禁用");
                return modelAndView;
            }
        }
        // 取得用户在汇付天下的客户号(转出账户)
        AccountChinapnr accountChinapnrTender = this.driecTransService.getAccountChinapnr(form.getOutUserId());
        // 取得用户在汇付天下的客户号(转入账户)
        AccountChinapnr accountChinapnrReceiver = this.driecTransService.getAccountChinapnr(form.getInUserId());
        // 调用汇付接口(定向支付授权接口)
        Properties properties = PropUtils.getSystemResourcesProperties();
        String host = properties.getProperty("hyjf.web.host").trim();
        String retUrl = host + DriecTransDefine.REQUEST_MAPPING + DriecTransDefine.BIND_USER_RETURN_MAPPING; // 返回路径
        // String retBgUrl = host + request.getContextPath() +
        // HtlCommonDefine.REQUEST_MAPPING+HtlCommonDefine.BUY_RETURN_MAPPING;
        // //返回路径
        ChinapnrBean bean = new ChinapnrBean();
        bean.setVersion(ChinaPnrConstant.VERSION_10);// 接口版本号
        bean.setCmdId(ChinaPnrConstant.CMDID_DIRECT_TRF_AUTH); // 消息类型(定向转账接口授权)
        bean.setUsrCustId(String.valueOf(accountChinapnrTender.getChinapnrUsrcustid()));// 用户客户号
                                                                                        // (转出)
        bean.setInUsrCustId(String.valueOf(accountChinapnrReceiver.getChinapnrUsrcustid()));// 用户客户号(转入)
        bean.setAuthAmt(CustomConstants.DIRECT_AUTHAMT);
        bean.setRetUrl(retUrl); // 页面返回
        bean.setBgRetUrl(ChinapnrUtil.getBgRetUrl()); // 商户后台应答地址(必须)
        // 调用接口前操作
        try {
            // 插入日志
            this.driecTransService.insertDirectLog(form, 1);

            int recordId = 0;
            // 如果已绑定成功不可绑定
            DirectionalTransferAssociatedRecords dinfo =
                    this.driecTransService.getDirectByOutUserId(form.getOutUserId());
            if (dinfo != null) {
                recordId = dinfo.getId();
            } else {
                modelAndView = new ModelAndView(DriecTransDefine.BIND_ERROR_PAGE);
                modelAndView.addObject("message", "修改绑定账户失败,绑定记录不存在");
                return modelAndView;
            }
            // 如果为0
            if (recordId == 0) {
                modelAndView = new ModelAndView(DriecTransDefine.ERROR_PAGE);
                return modelAndView;
            }
            // 跳转到汇付天下画面
            modelAndView = ChinapnrUtil.callApi(bean);
        } catch (Exception e) {
            // 错误页面
            modelAndView = new ModelAndView(DriecTransDefine.ERROR_PAGE);
        }
        LogUtil.endLog(THIS_CLASS, DriecTransDefine.BIND_USER_MAPPING);
        return modelAndView;
    }

    /**
     * 定向转账用户绑定回调函数
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = DriecTransDefine.BIND_USER_RETURN_MAPPING)
    public ModelAndView bindUserReturn(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) {
        LogUtil.startLog(THIS_CLASS, DriecTransDefine.BIND_USER_RETURN_MAPPING, "[交易完成后,回调开始]");
        ModelAndView modelAndView = null;
        // 参数转换
        bean.convert();
        try {
            this.driecTransService.insertBindUserReturn(bean);
        } catch (Exception e) {
            // 失败返回
            modelAndView = new ModelAndView(DriecTransDefine.BIND_ERROR_PAGE);
            modelAndView.addObject("message", "绑定账户失败," + bean.get(ChinaPnrConstant.PARAM_RESPDESC));
            return modelAndView;
        }
        // 成功
        if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
            modelAndView = new ModelAndView(DriecTransDefine.BIND_SUCCESS_PAGE);
        } else {
            // 失败返回
            modelAndView = new ModelAndView(DriecTransDefine.BIND_ERROR_PAGE);
            modelAndView.addObject("message", "绑定账户失败," + bean.get(ChinaPnrConstant.PARAM_RESPDESC));
            return modelAndView;
        }
        LogUtil.endLog(THIS_CLASS, DriecTransDefine.BIND_USER_RETURN_MAPPING, "[交易完成后,回调结束]");
        return modelAndView;
    }

    /**
     * 定向转账
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = DriecTransDefine.DIRECT_TRANS_MAPPING)
    public ModelAndView direcTrans(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute DriecTransBean form) {
        LogUtil.startLog(THIS_CLASS, DriecTransDefine.DIRECT_TRANS_MAPPING);
        ModelAndView modelAndView = new ModelAndView(DriecTransDefine.JSP_CHINAPNR_SEND);
        // 企业用户与绑定用户不可为空
        if (form.getInUserId() == null || form.getOutUserId() == null) {
            modelAndView = new ModelAndView(DriecTransDefine.ERROR_PAGE);
            return modelAndView;
        }
        // 手动输入的验证码
        String code = request.getParameter("code");
        String mobile = request.getParameter("mobile");
        String smsCodeId = request.getParameter("smsCodeId");
        boolean flag = false;
        if (StringUtils.isNotEmpty(smsCodeId)) {
            flag = this.driecTransService.checkMobileCode(Integer.valueOf(smsCodeId), code, mobile);
        }
        if (StringUtils.isEmpty(code) || !flag) {
            modelAndView = new ModelAndView(DriecTransDefine.DIRECT_ERROR_PAGE);
            modelAndView.addObject("message", "验证码不正确");
            return modelAndView;
        }
        // 验证企业用户是否已禁用
        Users userout = this.driecTransService.getUsers(form.getOutUserId());
        if (userout != null) {
            if (userout.getStatus() == 1) { // 已禁用
                modelAndView = new ModelAndView(DriecTransDefine.DIRECT_ERROR_PAGE);
                modelAndView.addObject("message", "您的账户已被禁用");
                return modelAndView;
            }
        }
        // 验证绑定用户是否已禁用
        Users userin = this.driecTransService.getUsers(form.getInUserId());
        if (userin != null) {
            if (userin.getStatus() == 1) { // 已禁用
                modelAndView = new ModelAndView(DriecTransDefine.DIRECT_ERROR_PAGE);
                modelAndView.addObject("message", "转账失败" + userin.getUsername() + "账户已禁用");
                return modelAndView;
            }
        }
        // 取得用户在汇付天下的客户号(转出账户)
        AccountChinapnr accountChinapnrTender = this.driecTransService.getAccountChinapnr(form.getOutUserId());
        // 取得用户在汇付天下的客户号(转入账户)
        AccountChinapnr accountChinapnrReceiver = this.driecTransService.getAccountChinapnr(form.getInUserId());
        // 调用汇付接口(定向支付授权接口)
        Properties properties = PropUtils.getSystemResourcesProperties();
        String host = properties.getProperty("hyjf.web.host").trim();
        String retUrl = host + DriecTransDefine.REQUEST_MAPPING + DriecTransDefine.DIRECT_TRANS_RETURN_MAPPING; // 返回路径
        // String retBgUrl = host + request.getContextPath() +
        // HtlCommonDefine.REQUEST_MAPPING+HtlCommonDefine.BUY_RETURN_MAPPING;
        // //返回路径
        String orderId = GetOrderIdUtils.getOrderId2(form.getOutUserId());
        ChinapnrBean bean = new ChinapnrBean();
        bean.setVersion(ChinaPnrConstant.VERSION_10);// 接口版本号
        bean.setCmdId(ChinaPnrConstant.CMDID_DIRECT_TRF); // 消息类型(定向转账接口)
        bean.setOrdId(orderId);// 订单号
        bean.setUsrCustId(String.valueOf(accountChinapnrTender.getChinapnrUsrcustid()));// 用户客户号
                                                                                        // (转出)
        bean.setInUsrCustId(String.valueOf(accountChinapnrReceiver.getChinapnrUsrcustid()));// 用户客户号(转入)
        bean.setTransAmt(CustomUtil.formatAmount(form.getTransAmt().toString()));// 转账金额
        bean.setRetUrl(retUrl); // 页面返回
        bean.setBgRetUrl(ChinapnrUtil.getBgRetUrl()); // 商户后台应答地址(必须)
        // 调用接口前操作
        try {
            int recordId = this.driecTransService.insertDirecTrans(form, orderId);
            // 如果为0
            if (recordId == 0) {
                modelAndView = new ModelAndView(DriecTransDefine.ERROR_PAGE);
                return modelAndView;
            }
            MerPriv mpr = new MerPriv();
            mpr.setRecordId(recordId);
            bean.setMerPrivPo(mpr);// 商户私有域 ，查看返回时是否有值
            // 跳转到汇付天下画面
            modelAndView = ChinapnrUtil.callApi(bean);
        } catch (Exception e) {
            // 错误页面
            modelAndView = new ModelAndView(DriecTransDefine.ERROR_PAGE);
        }
        LogUtil.endLog(THIS_CLASS, DriecTransDefine.DIRECT_TRANS_MAPPING);
        return modelAndView;
    }

    /**
     * 定向转账回调函数
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = DriecTransDefine.DIRECT_TRANS_RETURN_MAPPING)
    public ModelAndView direcTransReturn(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) {
        LogUtil.startLog(THIS_CLASS, DriecTransDefine.BIND_USER_RETURN_MAPPING, "[交易完成后,回调开始]");
        ModelAndView modelAndView = null;
        // 参数转换
        bean.convert();
        String uuid = request.getParameter("uuid");
        boolean updateFlag = false;
        if (Validator.isNotNull(uuid)) {
            // 取得检证数据
            ChinapnrExclusiveLogWithBLOBs record = chinapnrService.selectChinapnrExclusiveLog(Long.parseLong(uuid));
            // 如果检证数据状态为未发送
            if (record != null && ChinaPnrConstant.STATUS_VERTIFY_OK.equals(record.getStatus())) {
                // 将状态更新成[2:处理中]
                record.setId(Long.parseLong(uuid));
                record.setStatus(ChinaPnrConstant.STATUS_RUNNING);
                this.chinapnrService.updateChinapnrExclusiveLog(record);
                updateFlag = true;
            }
        } else {
            updateFlag = true;
        }
        // 其他程序正在处理中,或者返回值错误
        if (!updateFlag) {
            // 执行结果(失败)
            modelAndView = new ModelAndView(DriecTransDefine.DIRECT_ERROR_PAGE);
            modelAndView.addObject("message", "转账失败,其他程序正在处理中");
            return modelAndView;
        }
        // 发送状态
        String status = ChinaPnrConstant.STATUS_VERTIFY_OK;
        try {
            // 取得更新用id
            Integer recordId = bean.getMerPrivPo().getRecordId();
            if (recordId != null) {
                this.driecTransService.insertDirecTransReturn(bean, recordId);
            }
        } catch (Exception e) {
            // 执行结果(失败)
            status = ChinaPnrConstant.STATUS_FAIL;
            // 失败返回
            modelAndView = new ModelAndView(DriecTransDefine.DIRECT_ERROR_PAGE);
            modelAndView.addObject("message", "定向转账失败," + bean.get(ChinaPnrConstant.PARAM_RESPDESC));
            return modelAndView;
        }
        // 成功
        if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
            status = ChinaPnrConstant.STATUS_SUCCESS;
        } else {
            // 执行结果(失败)
            status = ChinaPnrConstant.STATUS_FAIL;
            // 失败返回
            modelAndView = new ModelAndView(DriecTransDefine.DIRECT_ERROR_PAGE);
            modelAndView.addObject("message", "定向转账失败," + bean.get(ChinaPnrConstant.PARAM_RESPDESC));
            return modelAndView;
        }
        // 更新状态记录
        if (updateFlag && Validator.isNotNull(uuid)) {
            this.chinapnrService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
        }
        // 成功
        modelAndView = new ModelAndView(DriecTransDefine.DIRECT_SUCCESS_PAGE);
        LogUtil.endLog(THIS_CLASS, DriecTransDefine.BIND_USER_RETURN_MAPPING, "[交易完成后,回调结束]");
        return modelAndView;
    }

    /**
     * 发送短信校验码
     * @param request
     * @param form
     * @return
     */
    @ResponseBody
    @RequestMapping(value = DriecTransDefine.SEND_CODE_MAPPING, produces = "application/json; charset=UTF-8")
    public String sendSmsCode(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, DriecTransDefine.SEND_CODE_MAPPING);
        // 返回信息
        Map<String, String> result = new HashMap<String, String>();
        // 用户名
        String mobile = request.getParameter("mobile");
        if (StringUtils.isEmpty(mobile)) { // 如果用户名为空
            result.put("error", "1");
            result.put("data", "手机号不能为空");
            return JSON.toJSONString(result);
        }
        if (!Validator.isMobile(mobile)) {
            result.put("error", "1");
            result.put("data", "手机号格式不正确");
            return JSON.toJSONString(result);
        }
        // 获取六位随机验证码
        String code = GetCode.getRandomSMSCode(6);
        // 发送短信
        Map<String, String> replaceStrs = new HashMap<String, String>();
        replaceStrs.put("val_code", code);

        // 发送短信验证码
        SmsMessage smsMessage =
                new SmsMessage(null, replaceStrs, mobile, null, MessageDefine.SMSSENDFORMOBILE, null,
                        CustomConstants.PARAM_TPL_DXZZ, CustomConstants.CHANNEL_TYPE_NORMAL);
        int status = (smsProcesser.gather(smsMessage) == 1) ? 0 : 1;
        // 保存验证码
        int smsCodeId = this.driecTransService.saveSmsCode(mobile, code, status);
        // 发送失败
        if (status != 0) {
            result.put("error", "1");
            result.put("data", "验证码发送失败");
            return JSON.toJSONString(result);
        }
        result.put("error", "0");
        result.put("smsCodeId", String.valueOf(smsCodeId));
        result.put("code", code);
        String res = JSON.toJSONString(result);
        LogUtil.endLog(THIS_CLASS, DriecTransDefine.SEND_CODE_MAPPING);
        return res;
    }

}
