package com.hyjf.api.server.user.transpassword;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.mongo.dao.ChinapnrSendLogDao;
import com.hyjf.mongo.entity.ChinapnrSendlog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.server.util.ErrorCodeConstant;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.user.transpassword.TransPasswordService;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.vip.apply.ApplyDefine;

/**
 * 
 * 第三方设置/修改交易密码
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年8月12日
 * @see 上午11:51:07
 */
@Controller
@RequestMapping(ThirdPartyTransPasswordDefine.REQUEST_MAPPING)
public class ThirdPartyTransPasswordServer extends BaseController {
    Logger _log = LoggerFactory.getLogger("第三方设置/修改交易密码Controller");

    @Autowired
    private TransPasswordService transPasswordService;


    /**
     * 设置交易密码
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ThirdPartyTransPasswordDefine.SETPASSWORD_ACTION)
    public ModelAndView setPassword(@RequestBody ThirdPartyTransPasswordRequestBean transPasswordRequestBean,HttpServletRequest request, HttpServletResponse response) {
        _log.info("设置交易密码 start");
        ModelAndView modelAndView = new ModelAndView();
        _log.info(transPasswordRequestBean.getAccountId()+"设置交易密码开始-----------------------------");
        _log.info("第三方请求参数："+JSONObject.toJSONString(transPasswordRequestBean));
        //验证请求参数
        if (Validator.isNull(transPasswordRequestBean.getAccountId())||
                Validator.isNull(transPasswordRequestBean.getRetUrl())||
                Validator.isNull(transPasswordRequestBean.getBgRetUrl())||
                Validator.isNull(transPasswordRequestBean.getChannel())||
                Validator.isNull(transPasswordRequestBean.getInstCode())) {
            
            if(Validator.isNotNull(transPasswordRequestBean.getBgRetUrl())){
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", transPasswordRequestBean.getAccountId());
                params.put("statusDesc","请求参数非法");
                params.put("acqRes",transPasswordRequestBean.getAcqRes());
                BaseResultBean resultBean = new BaseResultBean();
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                params.put("status", resultBean.getStatus());
                params.put("chkValue", resultBean.getChkValue());

                CommonSoaUtils.noRetPostThree(transPasswordRequestBean.getBgRetUrl(), params);
            }
            if(Validator.isNotNull(transPasswordRequestBean.getRetUrl())){
                ThirdPartyTransPasswordResultBean repwdResult = new ThirdPartyTransPasswordResultBean();
                repwdResult.setCallBackAction(transPasswordRequestBean.getRetUrl());
                modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                // 设置交易密码
                repwdResult.set("statusDesc", "请求参数非法");
                
                BaseResultBean resultBean = new BaseResultBean();
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                repwdResult.set("chkValue", resultBean.getChkValue());
                repwdResult.set("status", resultBean.getStatus());
                repwdResult.set("acqRes",transPasswordRequestBean.getAcqRes());
                modelAndView.addObject("callBackForm", repwdResult);
                return modelAndView;
            }
            
            
            modelAndView = new ModelAndView(ThirdPartyTransPasswordDefine.PASSWORD_ERROR_PATH);
            _log.info("-------------------请求参数非法--------------------");
            modelAndView.addObject("message", "请求参数非法");
            return modelAndView;
        }
        
        //验签  暂时去掉验签
        if(!this.verifyRequestSign(transPasswordRequestBean, BaseDefine.METHOD_SERVER_SET_PASSWORD)){
            _log.info("-------------------验签失败！--------------------");
            ThirdPartyTransPasswordResultBean repwdResult = new ThirdPartyTransPasswordResultBean();
            repwdResult.setCallBackAction(transPasswordRequestBean.getRetUrl());
            repwdResult.set("accountId", transPasswordRequestBean.getAccountId());
            repwdResult.set("acqRes",transPasswordRequestBean.getAcqRes());
            modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
            // 设置交易密码
            repwdResult.set("statusDesc", "验签失败！");
            
            BaseResultBean resultBean = new BaseResultBean();
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000002);
            repwdResult.set("status", resultBean.getStatus());
            repwdResult.set("chkValue", resultBean.getChkValue());
            modelAndView.addObject("callBackForm", repwdResult);
            if(Validator.isNotNull(transPasswordRequestBean.getBgRetUrl())){
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", transPasswordRequestBean.getAccountId());
                params.put("status", resultBean.getStatus());
                params.put("statusDesc","验签失败！");
                params.put("acqRes",transPasswordRequestBean.getAcqRes());
                params.put("chkValue", resultBean.getChkValue());
                CommonSoaUtils.noRetPostThree(transPasswordRequestBean.getBgRetUrl(), params);
            }
            return modelAndView;
        }
        
        
        //根据账号找出用户ID
        BankOpenAccount bankOpenAccount = transPasswordService.getBankOpenAccount(transPasswordRequestBean.getAccountId());
        if(bankOpenAccount == null){
            modelAndView = new ModelAndView(ThirdPartyTransPasswordDefine.PASSWORD_ERROR_PATH);
            _log.info("-------------------没有根据电子银行卡找到用户"+transPasswordRequestBean.getAccountId()+"！--------------------");
            
            ThirdPartyTransPasswordResultBean repwdResult = new ThirdPartyTransPasswordResultBean();
            repwdResult.setCallBackAction(transPasswordRequestBean.getRetUrl());
            repwdResult.set("accountId", transPasswordRequestBean.getAccountId());
            modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
            // 设置交易密码
            repwdResult.set("statusDesc", "没有根据电子银行卡找到用户 ");
            
            BaseResultBean resultBean = new BaseResultBean();
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000004);
            repwdResult.set("status", resultBean.getStatus());
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("acqRes",transPasswordRequestBean.getAcqRes());
            modelAndView.addObject("callBackForm", repwdResult);
            if(Validator.isNotNull(transPasswordRequestBean.getBgRetUrl())){
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", transPasswordRequestBean.getAccountId());
                params.put("status", resultBean.getStatus());
                params.put("statusDesc","没有根据电子银行卡找到用户 ");
                params.put("chkValue", resultBean.getChkValue());
                params.put("acqRes",transPasswordRequestBean.getAcqRes());
                CommonSoaUtils.noRetPostThree(transPasswordRequestBean.getBgRetUrl(), params);
            }
            return modelAndView;
            
        }
        Users user = transPasswordService.getUsers(bankOpenAccount.getUserId());//用户ID
        if (user == null) {
            _log.info("-------------------"+transPasswordRequestBean.getAccountId() + "用户不存在汇盈金服账户！--------------------");
            ThirdPartyTransPasswordResultBean repwdResult = new ThirdPartyTransPasswordResultBean();
            repwdResult.setCallBackAction(transPasswordRequestBean.getRetUrl());
            repwdResult.set("accountId", transPasswordRequestBean.getAccountId());
            modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
            // 设置交易密码
            repwdResult.set("statusDesc","用户不存在汇盈金服账户！");
            
            BaseResultBean resultBean = new BaseResultBean();
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000006);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
            repwdResult.set("acqRes",transPasswordRequestBean.getAcqRes());
            modelAndView.addObject("callBackForm", repwdResult);
            if(Validator.isNotNull(transPasswordRequestBean.getBgRetUrl())){
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", transPasswordRequestBean.getAccountId());
                params.put("status", repwdResult.getStatus());
                params.put("statusDesc","用户不存在汇盈金服账户！");
                params.put("chkValue", resultBean.getChkValue());
                params.put("acqRes",transPasswordRequestBean.getAcqRes());
                CommonSoaUtils.noRetPostThree(transPasswordRequestBean.getBgRetUrl(), params);
            }
            return modelAndView;
            
        }
        Integer userId = user.getUserId();
        if (user.getBankOpenAccount().intValue() != 1) {// 未开户
            _log.info("-------------------用户未开户！--------------------");
            ThirdPartyTransPasswordResultBean repwdResult = new ThirdPartyTransPasswordResultBean();
            repwdResult.setCallBackAction(transPasswordRequestBean.getRetUrl());
            repwdResult.set("accountId", transPasswordRequestBean.getAccountId());
            modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
            // 设置交易密码
            repwdResult.set("statusDesc", "用户未开户！");
            
            BaseResultBean resultBean = new BaseResultBean();
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000007);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
            repwdResult.set("acqRes",transPasswordRequestBean.getAcqRes());
            modelAndView.addObject("callBackForm", repwdResult);
            if(Validator.isNotNull(transPasswordRequestBean.getBgRetUrl())){
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", transPasswordRequestBean.getAccountId());
                params.put("status", resultBean.getStatus());
                params.put("statusDesc","用户未开户！");
                params.put("chkValue", resultBean.getChkValue());
                params.put("acqRes",transPasswordRequestBean.getAcqRes());
                CommonSoaUtils.noRetPostThree(transPasswordRequestBean.getBgRetUrl(), params);
            }
            return modelAndView;
        }

        // 判断用户是否设置过交易密码

        Integer passwordFlag = user.getIsSetPassword();
        if (passwordFlag == 1) {// 已设置交易密码
            
            _log.info("-------------------已设置交易密码--------------------");
            ThirdPartyTransPasswordResultBean repwdResult = new ThirdPartyTransPasswordResultBean();
            repwdResult.setCallBackAction(transPasswordRequestBean.getRetUrl());
            repwdResult.set("accountId", transPasswordRequestBean.getAccountId());
            modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
            // 设置交易密码
            repwdResult.set("statusDesc", "已设置交易密码");
            
            BaseResultBean resultBean = new BaseResultBean();
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_TP000001);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
            repwdResult.set("acqRes",transPasswordRequestBean.getAcqRes());
            modelAndView.addObject("callBackForm", repwdResult);
            if(Validator.isNotNull(transPasswordRequestBean.getBgRetUrl())){
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", transPasswordRequestBean.getAccountId());
                params.put("status", resultBean.getStatus());
                params.put("statusDesc","已设置交易密码");
                params.put("chkValue", resultBean.getChkValue());
                params.put("acqRes",transPasswordRequestBean.getAcqRes());
                CommonSoaUtils.noRetPostThree(transPasswordRequestBean.getBgRetUrl(), params);
            }
            return modelAndView;
        }

        UsersInfo usersInfo = transPasswordService.getUsersInfoByUserId(userId);
        // 同步调用路径
        String retUrl = PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) 
                + ThirdPartyTransPasswordDefine.REQUEST_MAPPING + ThirdPartyTransPasswordDefine.RETURL_SYN_PASSWORD_ACTION + ".do?acqRes="+
                transPasswordRequestBean.getAcqRes()+"&callback="+transPasswordRequestBean.getRetUrl().replace("#", "*-*-*");
        // 异步调用路
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) 
                + ThirdPartyTransPasswordDefine.REQUEST_MAPPING + ThirdPartyTransPasswordDefine.RETURN_ASY_PASSWORD_ACTION + ".do?acqRes="+
                transPasswordRequestBean.getAcqRes()+"&callback="+transPasswordRequestBean.getBgRetUrl().replace("#", "*-*-*");
        // 调用设置密码接口
        System.out.println(retUrl+"..."+bgRetUrl);
        BankCallBean bean = new BankCallBean();
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxCode(BankCallConstant.TXCODE_PASSWORD_RESET_PAGE);// 消息类型(用户开户)
        bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
        bean.setTxDate(GetOrderIdUtils.getTxDate());
        bean.setTxTime(GetOrderIdUtils.getTxTime());
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
        bean.setChannel(transPasswordRequestBean.getChannel());
        bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);
        bean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));// 电子账号
        bean.setIdNo(usersInfo.getIdcard());
        bean.setName(usersInfo.getTruename());
        bean.setMobile(user.getMobile());

        bean.setRetUrl(retUrl);// 页面同步返回 URL
        bean.setNotifyUrl(bgRetUrl);// 页面异步返回URL(必须)
        // 操作者ID
        bean.setLogUserId(String.valueOf(userId));
        bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_PASSWORDRESETPAGE);
        bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
        // 跳转到汇付天下画面
        try {
            modelAndView = BankCallUtils.callApi(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        _log.info("设置交易密码end");
        return modelAndView;
    }

    /**
     * 设置交易密码同步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(ThirdPartyTransPasswordDefine.RETURL_SYN_PASSWORD_ACTION)
    public ModelAndView passwordReturn(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute BankCallBean bean) {
        _log.info("设置交易密码同步回调start");
        ModelAndView modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
        ThirdPartyTransPasswordResultBean repwdResult = new ThirdPartyTransPasswordResultBean();
        repwdResult.setCallBackAction(request.getParameter("callback").replace("*-*-*","#"));
        bean.convert();
        int userId = Integer.parseInt(bean.getLogUserId());
        Users user = this.transPasswordService.getUsers(userId);
        
        
        BankOpenAccount bankOpenAccount = transPasswordService.getBankOpenAccount(userId);
        
        // 调用查询电子账户密码是否设置
        BankCallBean selectbean = new BankCallBean();
        selectbean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        selectbean.setTxCode(BankCallConstant.TXCODE_PASSWORD_SET_QUERY);
        selectbean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        selectbean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
        selectbean.setTxDate(GetOrderIdUtils.getTxDate());
        selectbean.setTxTime(GetOrderIdUtils.getTxTime());
        selectbean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
        selectbean.setChannel(BankCallConstant.CHANNEL_PC);
        selectbean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));// 电子账号

        // 操作者ID
        selectbean.setLogUserId(String.valueOf(userId));
        selectbean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
        selectbean.setLogClient(0);
        // 返回参数
        BankCallBean retBean = null;
        // 调用接口
        retBean = BankCallUtils.callApiBg(selectbean);
        repwdResult.set("accountId", bankOpenAccount.getAccount());
        
        if ("1".equals(retBean.getPinFlag())) {
            // 是否设置密码中间状态
            this.transPasswordService.updateUserIsSetPassword(user, 1);
            
            modelAndView.addObject("statusDesc", "交易密码设置成功！");
            BaseResultBean resultBean = new BaseResultBean();
            resultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
        } else {
            // 设置交易密码
            modelAndView.addObject("statusDesc", "交易密码设置失败,失败原因：" + transPasswordService.getBankRetMsg(bean.getRetCode()));
            
            BaseResultBean resultBean = new BaseResultBean();
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
        }
        repwdResult.set("acqRes",request.getParameter("acqRes"));
        modelAndView.addObject("callBackForm", repwdResult);
        _log.info("设置交易密码同步回调end");
        return modelAndView;
    }

    /**
     * 设置交易密码异步回调
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(ThirdPartyTransPasswordDefine.RETURN_ASY_PASSWORD_ACTION)
    public BankCallResult passwordBgreturn(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute BankCallBean bean) {
        _log.info("设置交易密码异步回调start");
        // 返回值  9-22修改
        BankCallResult result = new BankCallResult();
        String message = "";
        String status = "";
        Map<String, String> params = new HashMap<String, String>();
        // 返回值修改 end
        bean.convert();
        int userId = Integer.parseInt(bean.getLogUserId());
        Users user = this.transPasswordService.getUsers(userId);
        
        
        BankOpenAccount bankOpenAccount = transPasswordService.getBankOpenAccount(userId);
        
        // 成功或审核中
        if (user != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE))) {
            try {
                // 开户后保存相应的数据以及日志
                this.transPasswordService.updateUserIsSetPassword(user, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            message = "交易密码设置成功";
            status = ErrorCodeConstant.SUCCESS;
        }else{
         // 设置交易密码
            message = "交易密码设置失败,失败原因：" + transPasswordService.getBankRetMsg(bean.getRetCode());
            status = ErrorCodeConstant.STATUS_CE999999;
        }
        // 返回值  9-22修改
        params.put("accountId", bankOpenAccount.getAccount());
        params.put("status", status);
        params.put("statusDesc",message);
        BaseResultBean resultBean = new BaseResultBean();
        resultBean.setStatusForResponse(status);
        params.put("chkValue", resultBean.getChkValue());
        params.put("acqRes",request.getParameter("acqRes"));
        CommonSoaUtils.noRetPostThree(request.getParameter("callback").replace("*-*-*","#"), params);
        
        _log.info("设置交易密码异步回调end");
        result.setMessage("设置交易密码成功");
        result.setStatus(true);
        // 返回值  9-22修改 end
        
        return result;
    }
    /**
     * 修改交易密码
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ThirdPartyTransPasswordDefine.RESETPASSWORD_ACTION)
    public ModelAndView resetPassword(@RequestBody ThirdPartyTransPasswordRequestBean transPasswordRequestBean,HttpServletRequest request, HttpServletResponse response) {
        _log.info("修改交易密码 start");
        ModelAndView modelAndView = new ModelAndView();
        _log.info(transPasswordRequestBean.getAccountId()+"修改交易密码开始-----------------------------");
        _log.info("第三方请求参数："+JSONObject.toJSONString(transPasswordRequestBean));
        //验证请求参数
        if (Validator.isNull(transPasswordRequestBean.getAccountId())||
                Validator.isNull(transPasswordRequestBean.getRetUrl())||
                Validator.isNull(transPasswordRequestBean.getBgRetUrl())||
                Validator.isNull(transPasswordRequestBean.getChannel())||
                Validator.isNull(transPasswordRequestBean.getInstCode())) {
            modelAndView = new ModelAndView(ThirdPartyTransPasswordDefine.PASSWORD_ERROR_PATH);
            _log.info("-------------------请求参数非法--------------------");
            if(Validator.isNotNull(transPasswordRequestBean.getBgRetUrl())){
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", transPasswordRequestBean.getAccountId());
                params.put("statusDesc","请求参数非法");
                BaseResultBean resultBean = new BaseResultBean();
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                params.put("status", resultBean.getStatus());
                params.put("chkValue", resultBean.getChkValue());
                params.put("acqRes",transPasswordRequestBean.getAcqRes());
                CommonSoaUtils.noRetPostThree(transPasswordRequestBean.getBgRetUrl(), params);
            }
            
            if(Validator.isNotNull(transPasswordRequestBean.getRetUrl())){
                ThirdPartyTransPasswordResultBean repwdResult = new ThirdPartyTransPasswordResultBean();
                repwdResult.setCallBackAction(transPasswordRequestBean.getRetUrl());
                modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
                // 设置交易密码
                repwdResult.set("statusDesc", "请求参数非法");
                
                BaseResultBean resultBean = new BaseResultBean();
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
                repwdResult.set("chkValue", resultBean.getChkValue());
                repwdResult.set("status", resultBean.getStatus());
                repwdResult.set("acqRes",transPasswordRequestBean.getAcqRes());
                modelAndView.addObject("callBackForm", repwdResult);
                return modelAndView;
            }
            
            
            modelAndView.addObject("message", "请求参数非法");
            return modelAndView;
        }
        
        //验签
        if(!this.verifyRequestSign(transPasswordRequestBean, BaseDefine.METHOD_SERVER_RESET_PASSWORD)){
            _log.info("-------------------验签失败！--------------------");
            ThirdPartyTransPasswordResultBean repwdResult = new ThirdPartyTransPasswordResultBean();
            repwdResult.setCallBackAction(transPasswordRequestBean.getRetUrl());
            repwdResult.set("accountId", transPasswordRequestBean.getAccountId());
            modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
            // 设置交易密码
            repwdResult.set("statusDesc", "验签失败！");
            
            BaseResultBean resultBean = new BaseResultBean();
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000002);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
            repwdResult.set("acqRes",transPasswordRequestBean.getAcqRes());
            modelAndView.addObject("callBackForm", repwdResult);
            if(Validator.isNotNull(transPasswordRequestBean.getBgRetUrl())){
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", transPasswordRequestBean.getAccountId());
                params.put("status", resultBean.getStatus());
                params.put("statusDesc","验签失败！");
                params.put("chkValue", resultBean.getChkValue());
                params.put("acqRes",transPasswordRequestBean.getAcqRes());
                CommonSoaUtils.noRetPostThree(transPasswordRequestBean.getBgRetUrl(), params);
            }
            return modelAndView;
        }
        
        //根据账号找出用户ID
        BankOpenAccount bankOpenAccount = transPasswordService.getBankOpenAccount(transPasswordRequestBean.getAccountId());
        if(bankOpenAccount == null){
            modelAndView = new ModelAndView(ThirdPartyTransPasswordDefine.PASSWORD_ERROR_PATH);
            _log.info("-------------------没有根据电子银行卡找到用户"+transPasswordRequestBean.getAccountId()+"！--------------------");
            
            ThirdPartyTransPasswordResultBean repwdResult = new ThirdPartyTransPasswordResultBean();
            repwdResult.setCallBackAction(transPasswordRequestBean.getRetUrl());
            repwdResult.set("accountId", transPasswordRequestBean.getAccountId());
            modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
            // 设置交易密码
            repwdResult.set("statusDesc", "没有根据电子银行卡找到用户");
            
            BaseResultBean resultBean = new BaseResultBean();
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000004);
            repwdResult.set("chkValue", resultBean.getChkValue());
            modelAndView.addObject("callBackForm", repwdResult);
            repwdResult.set("status", resultBean.getStatus());
            repwdResult.set("acqRes",transPasswordRequestBean.getAcqRes());
            if(Validator.isNotNull(transPasswordRequestBean.getBgRetUrl())){
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", transPasswordRequestBean.getAccountId());
                params.put("status", resultBean.getStatus());
                params.put("statusDesc","没有根据电子银行卡找到用户");
                params.put("chkValue", resultBean.getChkValue());
                params.put("acqRes",transPasswordRequestBean.getAcqRes());
                CommonSoaUtils.noRetPostThree(transPasswordRequestBean.getBgRetUrl(), params);
            }
            return modelAndView;
            
        }
        Users user = transPasswordService.getUsers(bankOpenAccount.getUserId());//用户ID

        if (user.getBankOpenAccount().intValue() != 1) {//未开户
            ThirdPartyTransPasswordResultBean repwdResult = new ThirdPartyTransPasswordResultBean();
            repwdResult.setCallBackAction(transPasswordRequestBean.getRetUrl());
            repwdResult.set("accountId", transPasswordRequestBean.getAccountId());
            modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
            // 设置交易密码
            repwdResult.set("statusDesc", "用户未开户！");
            
            BaseResultBean resultBean = new BaseResultBean();
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000006);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
            repwdResult.set("acqRes",transPasswordRequestBean.getAcqRes());
            modelAndView.addObject("callBackForm", repwdResult);
            if(Validator.isNotNull(transPasswordRequestBean.getBgRetUrl())){
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", transPasswordRequestBean.getAccountId());
                params.put("status", resultBean.getStatus());
                params.put("statusDesc","用户未开户！");
                params.put("chkValue", resultBean.getChkValue());
                params.put("acqRes",transPasswordRequestBean.getAcqRes());
                CommonSoaUtils.noRetPostThree(transPasswordRequestBean.getBgRetUrl(), params);
            }
            return modelAndView;
        }
        //判断用户是否设置过交易密码
        
        Integer passwordFlag = user.getIsSetPassword();
        if (passwordFlag == 0) {//未设置交易密码
            ThirdPartyTransPasswordResultBean repwdResult = new ThirdPartyTransPasswordResultBean();
            repwdResult.setCallBackAction(transPasswordRequestBean.getRetUrl());
            repwdResult.set("accountId", transPasswordRequestBean.getAccountId());
            modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
            // 设置交易密码
            repwdResult.set("statusDesc", "未设置过交易密码，请先设置交易密码");
            
            BaseResultBean resultBean = new BaseResultBean();
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_TP000002);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
            repwdResult.set("acqRes",transPasswordRequestBean.getAcqRes());
            modelAndView.addObject("callBackForm", repwdResult);
            if(Validator.isNotNull(transPasswordRequestBean.getBgRetUrl())){
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", transPasswordRequestBean.getAccountId());
                params.put("status", resultBean.getStatus());
                params.put("statusDesc","未设置过交易密码，请先设置交易密码");
                params.put("chkValue", resultBean.getChkValue());
                params.put("acqRes",transPasswordRequestBean.getAcqRes());
                CommonSoaUtils.noRetPostThree(transPasswordRequestBean.getBgRetUrl(), params);
            }
            return modelAndView;
        }
        Integer userId = user.getUserId();
        UsersInfo usersInfo=transPasswordService.getUsersInfoByUserId(userId);
        // 同步调用路径
        String retUrl = PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL)
                + ThirdPartyTransPasswordDefine.REQUEST_MAPPING + ThirdPartyTransPasswordDefine.RETURL_SYN_RESETPASSWORD_ACTION + ".do?acqRes="+
                transPasswordRequestBean.getAcqRes()+"&callback="+transPasswordRequestBean.getRetUrl().replace("#", "*-*-*");
        // 异步调用路
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL)
                + ThirdPartyTransPasswordDefine.REQUEST_MAPPING + ThirdPartyTransPasswordDefine.RETURN_ASY_RESETPASSWORD_ACTION + ".do?acqRes="+
                transPasswordRequestBean.getAcqRes()+"&callback="+transPasswordRequestBean.getBgRetUrl().replace("#", "*-*-*");
       
        System.out.println("retUrl:"+retUrl);
        System.out.println("bgRetUrl:"+bgRetUrl);
        // 调用设置密码接口 
        BankCallBean bean = new BankCallBean();
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxCode(BankCallConstant.TXCODE_PASSWORD_RESET_PAGE);// 消息类型(用户开户)
        bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
        bean.setTxDate(GetOrderIdUtils.getTxDate());
        bean.setTxTime(GetOrderIdUtils.getTxTime());
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
        bean.setChannel(transPasswordRequestBean.getChannel());
        bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);
        bean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));//电子账号
        bean.setIdNo(usersInfo.getIdcard());
        bean.setName(usersInfo.getTruename());
        bean.setMobile(user.getMobile());
        bean.setNotifyUrl(bgRetUrl);// 页面异步返回URL(必须)

        // 操作者ID
        bean.setLogUserId(String.valueOf(userId));
        bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_PASSWORDRESETPAGE);
        bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
        bean.setRetUrl(retUrl + "&ordid=" + bean.getLogOrderId());// 页面同步返回 URL
        try {
            modelAndView = BankCallUtils.callApi(bean);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView = new ModelAndView(ThirdPartyTransPasswordDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "调用银行接口失败！");
        }
        _log.info("修改交易密码 end");
        return modelAndView;
    }



    /**
     * 修改交易密码同步回调
     *
     * @param request
     * @param response
     * @return
     */
    
    @RequestMapping(ThirdPartyTransPasswordDefine.RETURL_SYN_RESETPASSWORD_ACTION)
    public ModelAndView resetPasswordReturn(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute BankCallBean bean) {
        _log.info("修改交易密码同步回调start");
        ModelAndView modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRANSPASSWORD_VIEW);
        ThirdPartyTransPasswordResultBean repwdResult = new ThirdPartyTransPasswordResultBean();
        bean.convert();
        repwdResult.setCallBackAction(request.getParameter("callback").replace("*-*-*","#"));
        int userId = Integer.parseInt(bean.getLogUserId());
        BankOpenAccount bankOpenAccount = transPasswordService.getBankOpenAccount(userId);

        //add by cwyang 防止同步比异步快
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String ordid = request.getParameter("ordid");
        //add by cwyang 查询银行设置交易密码是否成功
        boolean backIsSuccess = transPasswordService.backLogIsSuccess(ordid);
        repwdResult.set("accountId", bankOpenAccount.getAccount());
        // 返回失败
        if ((bean.getRetCode()!=null&&!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) || !backIsSuccess) {
            BaseResultBean resultBean = new BaseResultBean();
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
            repwdResult.set("acqRes",request.getParameter("acqRes"));
            modelAndView.addObject("statusDesc", "交易密码修改失败,失败原因：" + transPasswordService.getBankRetMsg(bean.getRetCode()));
            modelAndView.addObject("callBackForm", repwdResult);
            _log.info("修改交易密码同步回调end(密码修改失败)");
            return modelAndView;
        }
        
        modelAndView.addObject("statusDesc", "修改交易密码成功");
        BaseResultBean resultBean = new BaseResultBean();
        resultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
        repwdResult.set("chkValue", resultBean.getChkValue());
        repwdResult.set("status", resultBean.getStatus());
        repwdResult.set("acqRes",request.getParameter("acqRes"));
        modelAndView.addObject("callBackForm", repwdResult);
        _log.info("修改交易密码同步回调end");
        return modelAndView;
    }
    /**
     * 修改交易密码异步回调
     *
     * @param request
     * 
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(ThirdPartyTransPasswordDefine.RETURN_ASY_RESETPASSWORD_ACTION)
    public BankCallResult resetPasswordBgreturn(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute BankCallBean bean) {
        _log.info("修改交易密码异步回调start");
        // 返回值  9-22修改
        BankCallResult result = new BankCallResult();
        String message = "";
        String status = "";
        Map<String, String> params = new HashMap<String, String>();
        bean.convert();
        int userId = Integer.parseInt(bean.getLogUserId());
        Users user = this.transPasswordService.getUsers(userId);
        
        BankOpenAccount bankOpenAccount = transPasswordService.getBankOpenAccount(userId);
        
        // 成功或审核中
        if (user != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE))) {
            message = "交易密码修改成功";
            status = ErrorCodeConstant.SUCCESS;
        }else{
            // 设置交易密码
            message = "交易密码修改失败,失败原因：" + transPasswordService.getBankRetMsg(bean.getRetCode());
            // upd by liushouyi 修改失败时返回的状态码
            status = ErrorCodeConstant.STATUS_CE999999;
        }
        
        
        _log.info("修改交易密码异步回调end");
        
        params.put("accountId", bankOpenAccount.getAccount());
        params.put("status", status);
        params.put("statusDesc",message);
        params.put("acqRes",request.getParameter("acqRes"));
        BaseResultBean resultBean = new BaseResultBean();
        resultBean.setStatusForResponse(status);
        params.put("chkValue", resultBean.getChkValue());
        

        CommonSoaUtils.noRetPostThree(request.getParameter("callback").replace("*-*-*","#"), params);
       
        _log.info("修改交易密码异步回调end");
        result.setMessage("设置交易密码成功");
        result.setStatus(true);
        
        return result;

    }
    public static void main(String[] args) {
        String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);
        String miwen = MD5.toMD5Code(accessKey + "13600000010" + accessKey);
        System.out.println(miwen);
    }
}
