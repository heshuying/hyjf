package com.hyjf.api.surong.user.deletecard;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.api.surong.user.bindcard.BindCardDefine;
import com.hyjf.api.surong.user.recharge.RdfRechargeService;
import com.hyjf.api.surong.user.transpassword.RetranspasswordResultBean;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.vip.apply.ApplyDefine;
import com.hyjf.bank.service.user.deletecard.DeleteCardService;
/**
 * 
 * 融东风 解绑卡接口
 * @author lixianchang
 */
@Controller
@RequestMapping(DeleteCardDefine.REQUEST_MAPPING)
public class DeleteCardServer extends BaseController {
    Logger _log = LoggerFactory.getLogger("DeleteCard");
    @Autowired
    private DeleteCardService userDeleteCardService;
    @Autowired
    private RdfRechargeService rechargeService;
    /**
     * 用户删除银行卡
     *
     * @param request
     * @param ret
     * @return
     */
    @ResponseBody
    @RequestMapping(value = DeleteCardDefine.DELETECARD_ACTION)
    public ModelAndView deleteCard(HttpServletRequest request, HttpServletResponse response) {
        _log.info("解绑卡接口start");
        //---传入参数---
        String sign = request.getParameter("sign");
        String mobile = request.getParameter("mobile"); // 用户ID
        String cardNo = request.getParameter("cardNo");
        String from = request.getParameter("from");  //来自于哪个客户端
        ModelAndView modelAndView = new ModelAndView();
        // 唯一标识
        if(StringUtils.isEmpty(sign)){
            modelAndView = new ModelAndView(DeleteCardDefine.DELETECARD_ERROR_PATH);
            modelAndView.addObject("message", "融东风绑卡sign值为空！");
            return modelAndView;
        }
        String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);
        
        String miwen =  MD5.toMD5Code(accessKey + mobile + accessKey);
        
        if(!miwen.equals(sign)){
            modelAndView = new ModelAndView(DeleteCardDefine.DELETECARD_ERROR_PATH);
            modelAndView.addObject("message", "融东风绑卡sign值非法！");
            return modelAndView;
        }
        
        Users user = rechargeService.findUserByMobile(mobile);
        if(user == null){
            modelAndView = new ModelAndView(DeleteCardDefine.DELETECARD_ERROR_PATH);
            modelAndView.addObject("message", "用户不存在汇盈金服账户！");
            return modelAndView;
        }
        if (Validator.isNull(cardNo)) {
            modelAndView = new ModelAndView(DeleteCardDefine.DELETECARD_ERROR_PATH);
            modelAndView.addObject("message", "获取银行卡号为空");
            return modelAndView;
        }
        Integer userId = user.getUserId();  
        if (userId == null || userId == 0) {
            modelAndView = new ModelAndView(DeleteCardDefine.DELETECARD_ERROR_PATH);
            modelAndView.addObject("message", "用户未登录");
            return modelAndView;
        }
        BankOpenAccount accountChinapnrTender = userDeleteCardService.getBankOpenAccount(userId);
        if (accountChinapnrTender == null || StringUtils.isEmpty(accountChinapnrTender.getAccount())) {
            modelAndView = new ModelAndView(DeleteCardDefine.DELETECARD_ERROR_PATH);
            modelAndView.addObject("message", "用户未开户");
            return modelAndView;
        }
        // 用户余额大于零不让解绑
        Account account = userDeleteCardService.getAccount(userId);
        // 用户在银行的账户余额
        BigDecimal bankBalance = userDeleteCardService.getBankBalance(userId, accountChinapnrTender.getAccount());
        if ((Validator.isNotNull(account.getBankBalance()) && account.getBankBalance().compareTo(BigDecimal.ZERO) > 0)
                || ((Validator.isNotNull(bankBalance) && bankBalance.compareTo(BigDecimal.ZERO) > 0))) {
            modelAndView = new ModelAndView(DeleteCardDefine.DELETECARD_ERROR_PATH);
            modelAndView.addObject("message", "抱歉，您还有部分余额，请清空后再删除银行卡！");
            return modelAndView;
        }
        //Integer cardId = Integer.parseInt(this.userDeleteCardService.getBankIdByCardNo(cardNo));
        // 根据银行卡Id获取用户的银行卡信息
        BankCard bankCard = this.userDeleteCardService.getBankCardByCardNo(userId, cardNo);
        if (bankCard == null || StringUtils.isEmpty(bankCard.getCardNo())) {
            modelAndView = new ModelAndView(DeleteCardDefine.DELETECARD_ERROR_PATH);
            modelAndView.addObject("message", "获取用户银行卡信息失败");
            return modelAndView;
        }
        // 银行卡Id
        Integer cardId = bankCard.getId();
        Users users = userDeleteCardService.getUsers(userId);
        UsersInfo usersInfo = userDeleteCardService.getUsersInfoByUserId(userId);
        // 调用汇付接口(4.2.6 删除银行卡接口)
        BankCallBean retBean = null;
        BankCallBean bean = new BankCallBean();
        bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
        bean.setLogRemark("解绑银行卡");
        bean.setLogUserId(String.valueOf(userId));
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxCode(BankCallMethodConstant.TXCODE_CARD_UNBIND);
        bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
        bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
        bean.setChannel(BankCallConstant.CHANNEL_APP);// 交易渠道
        bean.setAccountId(accountChinapnrTender.getAccount());// 存管平台分配的账号
        bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);// 证件类型01身份证
        bean.setIdNo(usersInfo.getIdcard());// 证件号
        bean.setName(usersInfo.getTruename());// 姓名
        bean.setMobile(users.getMobile());// 手机号
        bean.setCardNo(cardNo);// 银行卡号
        LogAcqResBean logAcqResBean = new LogAcqResBean();
        logAcqResBean.setCardNo(cardNo);
        logAcqResBean.setCardId(cardId);
        bean.setLogAcqResBean(logAcqResBean);
        // 调用汇付接口
        try {
            retBean = BankCallUtils.callApiBg(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 回调数据处理
        if (retBean == null || !(BankCallStatusConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode()))) {
            _log.info("RetCode:" + (retBean == null ? "" : retBean.getRetCode()) + "&&&&&&&&&&& RetMsg:" + (retBean == null ? "" : retBean.getRetMsg()));
            modelAndView = new ModelAndView(DeleteCardDefine.DELETECARD_ERROR_PATH);
            modelAndView.addObject("message", "抱歉，银行卡删除错误，请联系客服！");
            return modelAndView;
        }
        // 执行删除卡后处理,判断银行卡状态，删除平台本地银行卡信息
        try {
            boolean isdelFlag = this.userDeleteCardService.updateAfterDeleteCard(bean, userId);
            // 删除失败
            if (!isdelFlag) {
                modelAndView = new ModelAndView(DeleteCardDefine.DELETECARD_ERROR_PATH);
                modelAndView.addObject("message", "抱歉，银行卡删除错误，请联系客服！");
            } else {
                modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_VIEW);
                RedeletecardResultBean repwdResult = new RedeletecardResultBean();
                repwdResult.setCallBackAction(PropUtils.getSystem("wcsr.deletecard.callback"));
                repwdResult.set("mobile", mobile);
                repwdResult.set("sign",sign);
                repwdResult.set("status", "0");
                repwdResult.set("cardNo",cardNo);
                modelAndView.addObject("callBackForm", repwdResult);
            }
        } catch (Exception e) {
            modelAndView = new ModelAndView(DeleteCardDefine.DELETECARD_ERROR_PATH);
            modelAndView.addObject("message", "抱歉，银行卡删除错误，请联系客服！");
        }
        _log.info("解绑卡接口end");
        return modelAndView;
    }
}
