package com.hyjf.api.aems.bindcardpage;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.aems.assetpush.AemsPushDefine;
import com.hyjf.api.server.user.paymentauthpage.PaymentAuthPageDefine;
import com.hyjf.api.server.util.ErrorCodeConstant;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.user.bindcard.BindCardPageBean;
import com.hyjf.bank.service.user.bindcard.BindCardService;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.vip.apply.ApplyDefine;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(AemsBindCardPageDefine.REQUEST_MAPPING)
public class AemsBindCardPageServer extends BaseController{

    Logger logger = LoggerFactory.getLogger(AemsBindCardPageServer.class);

    @Autowired
    private BindCardService bindCardService;


    /**
     * 用户页面绑卡
     *
     * @param bankCardRequestBean
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = AemsBindCardPageDefine.BIND_CARD_PAGE)
    public ModelAndView userBindCardPlus(@RequestBody AemsBindCardPageRequestBean bankCardRequestBean,HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView(PaymentAuthPageDefine.PATH_OPEN_ACCOUNT_PAGE_ERROR);
        logger.info("请求页面绑卡接口参数" + JSONObject.toJSONString(bankCardRequestBean, true) + "]");
        
        //验证请求参数
        if (Validator.isNull(bankCardRequestBean.getAccountId())||
                Validator.isNull(bankCardRequestBean.getInstCode())||
                Validator.isNull(bankCardRequestBean.getRetUrl())||
                Validator.isNull(bankCardRequestBean.getPlatform())||
                Validator.isNull(bankCardRequestBean.getChannel())||
                Validator.isNull(bankCardRequestBean.getNotifyUrl())) {
            logger.info("-------------------绑卡请求参数非法--------------------");
            getErrorMV(bankCardRequestBean, modelAndView,ErrorCodeConstant.STATUS_CE000001);
            bankCardRequestBean.doNotify(bankCardRequestBean.getErrorMap(ErrorCodeConstant.STATUS_CE000001, "请求参数非法"));
            return modelAndView;
        }
        // 验签
        if (!this.AEMSVerifyRequestSign(bankCardRequestBean, AemsBindCardPageDefine.REQUEST_MAPPING+AemsBindCardPageDefine.BIND_CARD_PAGE)) {
            getErrorMV(bankCardRequestBean, modelAndView,ErrorCodeConstant.STATUS_CE000002);
            bankCardRequestBean.doNotify(bankCardRequestBean.getErrorMap(ErrorCodeConstant.STATUS_CE000002, "验签失败"));
            return modelAndView;
        }
        
        //根据账号找出用户ID
        BankOpenAccount bankOpenAccount = bindCardService.getBankOpenAccount(bankCardRequestBean.getAccountId());
        if(bankOpenAccount == null){
            logger.info("-------------------没有根据电子银行卡找到用户"+bankCardRequestBean.getAccountId()+"！--------------------");
            
            getErrorMV(bankCardRequestBean, modelAndView,ErrorCodeConstant.STATUS_CE000004);
            bankCardRequestBean.doNotify(bankCardRequestBean.getErrorMap(ErrorCodeConstant.STATUS_CE000004, "没有根据电子银行卡找到用户"));
            return modelAndView;
        }
        Users user = bindCardService.getUsers(bankOpenAccount.getUserId());//用户ID
        if(user == null){
            logger.info("---用户不存在汇盈金服账户---");
            getErrorMV(bankCardRequestBean, modelAndView,ErrorCodeConstant.STATUS_CE000007);
            bankCardRequestBean.doNotify(bankCardRequestBean.getErrorMap(ErrorCodeConstant.STATUS_CE000007, "用户不存在汇盈金服账户"));
            return modelAndView;
        }
        if (user.getBankOpenAccount()==0) {
            logger.info("---用户未开户---");
            getErrorMV(bankCardRequestBean, modelAndView,ErrorCodeConstant.STATUS_CE000006);
            bankCardRequestBean.doNotify(bankCardRequestBean.getErrorMap(ErrorCodeConstant.STATUS_CE000006, "用户未开户"));
            return modelAndView;
        }

        if (user.getIsSetPassword()==0) {
            logger.info("---用户未设置交易密码---");
            getErrorMV(bankCardRequestBean, modelAndView,ErrorCodeConstant.STATUS_TP000002);
            bankCardRequestBean.doNotify(bankCardRequestBean.getErrorMap(ErrorCodeConstant.STATUS_TP000002, "用户未设置交易密码"));
            return modelAndView;
        }

        Integer userId = user.getUserId();
        List<BankCard> bankCardList=bindCardService.getAccountBankByUserId(userId+"");
        if(bankCardList!=null&&bankCardList.size()>0){
            getErrorMV(bankCardRequestBean, modelAndView,ErrorCodeConstant.STATUS_BC000001);
            bankCardRequestBean.doNotify(bankCardRequestBean.getErrorMap(ErrorCodeConstant.STATUS_BC000001, "用户已绑定银行卡,请先解除绑定,然后重新操作"));
            return modelAndView;
        }
        UsersInfo userInfo = bindCardService.getUsersInfoByUserId(userId);

       
        // 跳转到江西银行天下画面
        try {
            // 同步调用路径
            String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
                    + AemsBindCardPageDefine.REQUEST_MAPPING + AemsBindCardPageDefine.RETURL_SYN_ACTION + ".do?acqRes="
                    + bankCardRequestBean.getAcqRes() 
                    + "&callback=" + bankCardRequestBean.getRetUrl().replace("#", "*-*-*");
            // 异步调用路
            String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
                    + AemsBindCardPageDefine.REQUEST_MAPPING + AemsBindCardPageDefine.RETURL_ASY_ACTION + ".do?phone="+user.getMobile()+"&acqRes="
                    + bankCardRequestBean.getAcqRes() + "&callback=" + bankCardRequestBean.getNotifyUrl().replace("#", "*-*-*");
            
            // 拼装参数 调用江西银行
            BindCardPageBean bean = new BindCardPageBean();
            bean.setTxCode(BankCallConstant.TXCODE_BIND_CARD_PAGE);
            bean.setChannel(bankCardRequestBean.getChannel());
            bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);
            bean.setIdNo(userInfo.getIdcard());
            bean.setName(userInfo.getTruename());
            bean.setAccountId(bankOpenAccount.getAccount());
            bean.setUserIP(GetCilentIP.getIpAddr(request));
            bean.setUserId(userId);
            bean.setRetUrl(retUrl);
            bean.setSuccessfulUrl(retUrl+"&isSuccess=1");
            bean.setNotifyUrl(bgRetUrl);
            bean.setPlatform(bankCardRequestBean.getPlatform());
            modelAndView = bindCardService.getCallbankMV(bean);

            logger.info("绑卡调用页面end");
            return modelAndView;
        } catch (Exception e) {
            logger.info("---调用银行接口失败~!---");
            e.printStackTrace();
            
            getErrorMV(bankCardRequestBean, modelAndView,ErrorCodeConstant.STATUS_CE999999);
            bankCardRequestBean.doNotify(bankCardRequestBean.getErrorMap(ErrorCodeConstant.STATUS_CE999999, "调用银行接口失败"));
            return modelAndView;
        }
    }
    
    /**
     * 页面绑卡同步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(AemsBindCardPageDefine.RETURL_SYN_ACTION)
    public ModelAndView openAccountReturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {
        AemsBindCardPageResultBean repwdResult = new AemsBindCardPageResultBean();
        repwdResult.setCallBackAction(request.getParameter("callback").replace("*-*-*","#"));
        ModelAndView modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRUSTEEPAY_VIEW);
        String frontParams = request.getParameter("frontParams");
        String isSuccess = request.getParameter("isSuccess");
        if(StringUtils.isBlank(bean.getRetCode())&&StringUtils.isNotBlank(frontParams)){
            JSONObject jsonParm = JSONObject.parseObject(frontParams);
            if(jsonParm.containsKey("RETCODE")){
                bean.setRetCode(jsonParm.getString("RETCODE"));
            }
        }
        bean.convert();
        // 银行返回响应代码
        String retCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
        logger.info("绑卡同步返回值,用户ID:[" + bean.getLogUserId() + "],retCode:[" + retCode + "]");

        if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)||"1".equals(isSuccess)) {
            // 成功
            modelAndView.addObject("statusDesc", "绑卡成功！");
            BaseResultBean resultBean = new BaseResultBean();
            resultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
        } else {
            BaseResultBean resultBean = new BaseResultBean();
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
        }
        repwdResult.set("acqRes",request.getParameter("acqRes"));
        modelAndView.addObject("callBackForm", repwdResult);
        logger.info("绑卡同步回调end");
        return modelAndView;
    }

    /**
     * 页面绑卡异步回调
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(AemsBindCardPageDefine.RETURL_ASY_ACTION)
    public BankCallResult bgreturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {
        // 上送的异步地址里面有
        Map<String, String> params = new HashMap<String, String>();
        BankCallResult result = new BankCallResult();
        String phone = request.getParameter("phone");
        logger.info("页面绑卡异步回调start");
        bean.setMobile(phone);
        bean.convert();
        String status = "";
        String statusDesc = "";
        int userId = Integer.parseInt(bean.getLogUserId());

        // 绑卡后处理
        try {
            // 保存银行卡信息
            bindCardService.updateCardNoToBank(bean);
            if (BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
                params.put("cardNo", bean.getCardNo());
                params.put("payAllianceCode", bean.getPayAllianceCode());
                status = ErrorCodeConstant.SUCCESS;
                statusDesc = "绑卡成功";
            }else{
                status = ErrorCodeConstant.SUCCESS;
                statusDesc = bindCardService.getBankRetMsg(bean.getRetCode());
            }
           
        } catch (Exception e) {
            e.printStackTrace();
            status = ErrorCodeConstant.STATUS_CE999999;
            statusDesc = "调用银行接口失败";
        }
        params.put("accountId", bean.getAccountId());
        params.put("status",status);
        params.put("statusDesc",statusDesc);
        BaseResultBean resultBean = new BaseResultBean();
        resultBean.setStatusForResponse(status);
        params.put("chkValue", resultBean.getChkValue());
        params.put("acqRes",request.getParameter("acqRes"));
        // 三方接口回调优化：增加返回手机号和银行卡号 add by liushouyi 20180821
        params.put("phone",phone);
        CommonSoaUtils.noRetPostThree(request.getParameter("callback").replace("*-*-*","#"), params);
        logger.info("页面绑卡成功,用户ID:[" + userId + ",用户电子账户号:[" + bean.getAccountId() + "]");
        result.setStatus(true);
        return result;
    }
    
    private ModelAndView getErrorMV(AemsBindCardPageRequestBean payRequestBean, ModelAndView modelAndView,String status) {
        AemsBindCardPageResultBean repwdResult = new AemsBindCardPageResultBean();
        BaseResultBean resultBean = new BaseResultBean();
        resultBean.setStatusForResponse(status);
        repwdResult.setCallBackAction(payRequestBean.getRetUrl());
        repwdResult.set("chkValue", resultBean.getChkValue());
        repwdResult.set("status", resultBean.getStatus());
        repwdResult.set("acqRes",payRequestBean.getAcqRes());
        modelAndView.addObject("callBackForm", repwdResult);
        return modelAndView;
    }
}
