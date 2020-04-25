    package com.hyjf.api.server.user.bankcard;

import java.math.BigDecimal;
import java.util.List;

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
import com.hyjf.api.server.util.ErrorCodeConstant;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.user.bindcard.BindCardService;
import com.hyjf.bank.service.user.deletecard.DeleteCardService;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringUtil;
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
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;

@Controller
@RequestMapping(ThirdPartyBankCardDefine.REQUEST_MAPPING)
public class ThirdPartyBankCardServer extends BaseController{

	Logger _log = LoggerFactory.getLogger("BankCard");
	@Autowired
	private BindCardService bindCardService;

	@Autowired
    private DeleteCardService userDeleteCardService;
	
	
	/**
     * 用户绑卡增强发送验证码接口
     */
    @ResponseBody
    @RequestMapping(value = ThirdPartyBankCardDefine.SEND_PLUS_CODE_ACTION, produces = "application/json; charset=utf-8")
    public BaseResultBean sendPlusCode(HttpServletRequest request, HttpServletResponse response,@RequestBody ThirdPartyBankCardRequestBean bankCardRequestBean) {
        _log.info(bankCardRequestBean.getMobile()+"用户绑卡增强发送验证码接口-----------------------------");
        _log.info("第三方请求参数："+JSONObject.toJSONString(bankCardRequestBean));
        ThirdPartyBankCardPlusResultBean ret = new ThirdPartyBankCardPlusResultBean();
        
        if (Validator.isNull(bankCardRequestBean.getAccountId())||
                Validator.isNull(bankCardRequestBean.getMobile())||
                Validator.isNull(bankCardRequestBean.getCardNo())||
                Validator.isNull(bankCardRequestBean.getInstCode())) {
            
            _log.info("-------------------请求参数非法--------------------");
            ret.setStatus(ErrorCodeConstant.STATUS_CE000001);
            ret.setStatusDesc("请求参数非法");
            return ret;
        }
        
        //验签
        if(!this.verifyRequestSign(bankCardRequestBean, BaseDefine.METHOD_SERVER_BIND_CARD_SEND_PLUS_CODE)){
            _log.info("-------------------验签失败！--------------------");
            ret.setStatus(ErrorCodeConstant.STATUS_CE000002);
            ret.setStatusDesc("验签失败");
            return ret;
        }

        
        //根据账号找出用户ID
        BankOpenAccount bankOpenAccount = bindCardService.getBankOpenAccount(bankCardRequestBean.getAccountId());
        if(bankOpenAccount == null){
            _log.info("-------------------没有根据电子银行卡找到用户"+bankCardRequestBean.getAccountId()+"！--------------------");
            ret.setStatus(ErrorCodeConstant.STATUS_CE000004);
            ret.setStatusDesc("没有根据电子银行卡找到用户");
            return ret;
        }
        Users user = bindCardService.getUsers(bankOpenAccount.getUserId());//用户ID
        if(user == null){
            _log.info("---用户不存在汇盈金服账户---");
            ret.setStatus(ErrorCodeConstant.STATUS_CE000006);
            ret.setStatusDesc("用户不存在汇盈金服账户");
            return ret;
        }
        if (user.getBankOpenAccount()==0) {
            _log.info("-------------------没有根据电子银行卡找到用户"+bankCardRequestBean.getAccountId()+"！--------------------");
            ret.setStatus(ErrorCodeConstant.STATUS_CE000004);
            ret.setStatusDesc("没有根据电子银行卡找到用户");
            return ret;
        }
        
        
        // 请求发送短信验证码
        BankCallBean bean = this.bindCardService.cardBindPlusSendSms(user.getUserId(),PropUtils.getSystem(BankCallConstant.BANK_INSTCODE),
                BankCallMethodConstant.TXCODE_CARD_BIND_PLUS, bankCardRequestBean.getMobile(), BankCallConstant.CHANNEL_PC,bankCardRequestBean.getCardNo());
        if (bean == null) {
            _log.info("-------------------发送短信验证码异常！--------------------");
            ret.setStatus(ErrorCodeConstant.STATUS_CE999999);
            ret.setStatusDesc("发送短信验证码异常");
            return ret;
        }
        // 返回失败
        if (!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
            if("JX900651".equals(bean.getRetCode())){
                // 成功返回业务授权码
                ret.setStatus(ErrorCodeConstant.STATUS_CE999999);
                ret.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
                ret.setSrvAuthCode(bean.getSrvAuthCode());
                return ret;
            }
            ret.setStatus(BaseResultBean.STATUS_FAIL);
            ret.setStatusDesc("发送短信验证码失败，失败原因：" + bindCardService.getBankRetMsg(bean.getRetCode()));
            _log.info("-------------------发送短信验证码失败，失败原因：" + bindCardService.getBankRetMsg(bean.getRetCode())+"--------------------");
            return ret;
        }
        // 成功返回业务授权码
        ret.setStatus(ErrorCodeConstant.SUCCESS);
        ret.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        ret.setSrvAuthCode(bean.getSrvAuthCode());
        return ret;
    }
    
    
    
    /**
     * 用户绑卡
     * @param request
     * @param form
     * @return
     */
    @ResponseBody
    @RequestMapping(ThirdPartyBankCardDefine.BIND_CARD_PLUS)
    public BaseResultBean userBindCardPlus(@RequestBody ThirdPartyBankCardRequestBean bankCardRequestBean,HttpServletRequest request, HttpServletResponse response) {
        //---
        ThirdPartyBankCardPlusResultBean ret = new ThirdPartyBankCardPlusResultBean();
        _log.info(bankCardRequestBean.getAccountId()+"用户绑卡开始-----------------------------");
        _log.info("第三方请求参数："+JSONObject.toJSONString(bankCardRequestBean));
        //验证请求参数
        if (Validator.isNull(bankCardRequestBean.getAccountId())||
                Validator.isNull(bankCardRequestBean.getCardNo())||
                Validator.isNull(bankCardRequestBean.getChannel())||
                Validator.isNull(bankCardRequestBean.getInstCode())||
                Validator.isNull(bankCardRequestBean.getLastSrvAuthCode())||
                Validator.isNull(bankCardRequestBean.getCode())||
                Validator.isNull(bankCardRequestBean.getMobile())) {
            
            _log.info("-------------------请求参数非法--------------------");
            ret.setStatus(BaseResultBean.STATUS_FAIL);
            ret.setStatusDesc("请求参数非法");
            return ret;
        }
        
        //验签
        if(!this.verifyRequestSign(bankCardRequestBean, BaseDefine.METHOD_SERVER_BIND_CARD)){
            _log.info("-------------------验签失败！--------------------");
            ret.setStatus(BaseResultBean.STATUS_FAIL);
            ret.setStatusDesc("验签失败");
            return ret;
        }
        
        //根据账号找出用户ID
        BankOpenAccount bankOpenAccount = bindCardService.getBankOpenAccount(bankCardRequestBean.getAccountId());
        if(bankOpenAccount == null){
            _log.info("-------------------没有根据电子银行卡找到用户"+bankCardRequestBean.getAccountId()+"！--------------------");
            
            ret.setStatus(BaseResultBean.STATUS_FAIL);
            ret.setStatusDesc("没有根据电子银行卡找到用户");
            return ret;
        }
        Users user = bindCardService.getUsers(bankOpenAccount.getUserId());//用户ID
        if(user == null){
            _log.info("---用户不存在汇盈金服账户---");
            ret.setStatus(BaseResultBean.STATUS_FAIL);
            ret.setStatusDesc("用户不存在汇盈金服账户");
            return ret;
        }
        if (user.getBankOpenAccount()==0) {
            _log.info("---用户未开户---");
            ret.setStatus(BaseResultBean.STATUS_FAIL);
            ret.setStatusDesc("用户未开户");
            return ret;
        }

        if (user.getIsSetPassword()==0) {
            _log.info("---用户未设置交易密码---");
            ret.setStatus(BaseResultBean.STATUS_FAIL);
            ret.setStatusDesc("未设置交易密码");
            return ret;
        }

        Integer userId = user.getUserId();
        List<BankCard> bankCardList=bindCardService.getAccountBankByUserId(userId+"");
        if(bankCardList!=null&&bankCardList.size()>0){
            ret.setStatus(BaseResultBean.STATUS_FAIL);
            ret.setStatusDesc("用户已绑定银行卡,请先解除绑定,然后重新操作！");
            return ret;
        }
        UsersInfo usersInfo = bindCardService.getUsersInfoByUserId(userId);

        // 调用汇付接口(4.2.2 用户绑卡接口)
        BankCallBean bean = new BankCallBean();
        bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
        bean.setLogUserId(StringUtil.valueOf(userId));
        bean.setLogRemark("用户绑卡增强");
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxCode(BankCallConstant.TXCODE_CARD_BIND_PLUS);
        bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
        bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
        bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
        bean.setAccountId(bankCardRequestBean.getAccountId());// 存管平台分配的账号
        bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);// 证件类型01身份证
        bean.setIdNo(usersInfo.getIdcard());// 证件号
        bean.setName(usersInfo.getTruename());// 姓名
        bean.setMobile(bankCardRequestBean.getMobile());// 手机号
        bean.setCardNo(bankCardRequestBean.getCardNo());// 银行卡号
        bean.setLastSrvAuthCode(bankCardRequestBean.getLastSrvAuthCode());
        bean.setSmsCode(bankCardRequestBean.getCode());
        bean.setUserIP(GetCilentIP.getIpAddr(request));// 客户IP
        LogAcqResBean logAcq = new LogAcqResBean();
        logAcq.setCardNo(bankCardRequestBean.getCardNo());
        bean.setLogAcqResBean(logAcq);
        BankCallBean retBean=null;
        // 跳转到江西银行天下画面
        try {
            retBean  = BankCallUtils.callApiBg(bean);
        } catch (Exception e) {
            _log.info("---调用银行接口失败~!---");
            e.printStackTrace();
            
            ret.setStatus(BaseResultBean.STATUS_FAIL);
            ret.setStatusDesc("调用银行接口失败~!");
            return ret;
        }
        
     // 回调数据处理
        if (retBean == null || !(BankCallStatusConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode()))) {
         // 执行结果(失败)
            String message = this.bindCardService.getBankRetMsg(retBean.getRetCode());
            _log.info(ThirdPartyBankCardDefine.BIND_CARD_PLUS, "银行返码:" + retBean.getRetCode() + "绑卡失败:" + message);
            ret.setStatus(BaseResultBean.STATUS_FAIL);
            ret.setStatusDesc("绑卡失败");
            return ret;
        }
        
        try {
            // 绑卡后处理
            this.bindCardService.updateAfterBindCard(bean);
            List<BankCard> accountBankList = bindCardService.getAccountBankByUserId(userId+"");
            if (accountBankList != null && accountBankList.size() > 0) {
                ret.setStatus(BaseResultBean.STATUS_SUCCESS);
                ret.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
                return ret;
            } else {
                ret.setStatus(BaseResultBean.STATUS_FAIL);
                ret.setStatusDesc("绑卡失败");
                return ret;
            }
        } catch (Exception e) {
            // 执行结果(失败)
            e.printStackTrace();
            ret.setStatus(BaseResultBean.STATUS_FAIL);
            ret.setStatusDesc("绑卡失败");
            return ret;
        }
    }
	
	
	/**
     * 用户删除银行卡
     *
     * @param request
     * @param ret
     * @return
     */
    @ResponseBody
    @RequestMapping(ThirdPartyBankCardDefine.DELETE_CARD)
    public ThirdPartyDeleteBankCardResultBean deleteCard(@RequestBody ThirdPartyBankCardRequestBean bankCardRequestBean,HttpServletRequest request, HttpServletResponse response) {
        _log.info(bankCardRequestBean.getAccountId()+"用户解除绑定银行卡开始-----------------------------");
        _log.info("第三方请求参数："+JSONObject.toJSONString(bankCardRequestBean));
        ThirdPartyDeleteBankCardResultBean resultBean = new ThirdPartyDeleteBankCardResultBean();
        //验证请求参数
        if (Validator.isNull(bankCardRequestBean.getAccountId())||
                Validator.isNull(bankCardRequestBean.getCardNo())||
                Validator.isNull(bankCardRequestBean.getChannel())||
                Validator.isNull(bankCardRequestBean.getInstCode())) {
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
            _log.info("-------------------请求参数非法--------------------");
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        _log.info(bankCardRequestBean.getAccountId()+"第三方同步余额开始-----------------------------");
        //验签
        if(!this.verifyRequestSign(bankCardRequestBean, BaseDefine.METHOD_SERVER_DELETE_CARD)){
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000002);
            _log.info("-------------------验签失败！--------------------");
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        resultBean.setAccountId(bankCardRequestBean.getAccountId());
        //根据账号找出用户ID
        BankOpenAccount bankOpenAccount = userDeleteCardService.getBankOpenAccount(bankCardRequestBean.getAccountId());
        if(bankOpenAccount == null){
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000004);
            _log.info("没有根据电子银行卡找到用户 "+bankCardRequestBean.getAccountId());
            resultBean.setStatusDesc("根据电子账户号查询用户信息失败 ");
            return resultBean;
        }
        
        Users user = userDeleteCardService.getUsers(bankOpenAccount.getUserId());//用户ID
        Integer userId=user.getUserId();
        // 用户余额大于零不让解绑
        Account account = userDeleteCardService.getAccount(userId);
        // 用户在银行的账户余额
        BigDecimal bankBalance = userDeleteCardService.getBankBalance(userId, bankOpenAccount.getAccount());
        if ((Validator.isNotNull(account.getBankBalance()) && account.getBankBalance().compareTo(BigDecimal.ZERO) > 0)
                || ((Validator.isNotNull(bankBalance) && bankBalance.compareTo(BigDecimal.ZERO) > 0))) {
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_BC000004);
            _log.info("解绑失败，余额大于0元是不能解绑银行卡"+bankCardRequestBean.getAccountId());
            resultBean.setStatusDesc("解绑失败，余额大于0元是不能解绑银行卡 ");
            return resultBean;
        }
        // 根据银行卡Id获取用户的银行卡信息
        BankCard bankCard = this.userDeleteCardService.getBankCardByCardNO(userId, bankCardRequestBean.getCardNo());
        if (bankCard == null || StringUtils.isEmpty(bankCard.getCardNo())) {
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_BC000002);
            _log.info("获取用户银行卡信息失败"+bankCardRequestBean.getCardNo());
            resultBean.setStatusDesc("获取用户银行卡信息失败 ");
            return resultBean;
        }
        String cardNo = bankCard.getCardNo();
        UsersInfo usersInfo = userDeleteCardService.getUsersInfoByUserId(userId);
        // 调用汇付接口(4.2.6 删除银行卡接口)
        BankCallBean retBean = null;
        BankCallBean bean = new BankCallBean();
        bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
        bean.setLogUserId(String.valueOf(userId));
        bean.setLogRemark("解绑银行卡");
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxCode(BankCallMethodConstant.TXCODE_CARD_UNBIND);
        bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
        bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
        bean.setChannel(bankCardRequestBean.getChannel());// 交易渠道
        bean.setAccountId(bankCardRequestBean.getAccountId());// 存管平台分配的账号
        bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);// 证件类型01身份证
        bean.setIdNo(usersInfo.getIdcard());// 证件号
        bean.setName(usersInfo.getTruename());// 姓名
        bean.setMobile(user.getMobile());// 手机号
        bean.setCardNo(cardNo);// 银行卡号
        LogAcqResBean logAcqResBean = new LogAcqResBean();
        logAcqResBean.setCardNo(cardNo);// 银行卡号
        logAcqResBean.setCardId(bankCard.getId()); // 银行卡Id
        bean.setLogAcqResBean(logAcqResBean);
        // 调用汇付接口
        try {
            retBean = BankCallUtils.callApiBg(bean);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            _log.info("调用银行接口失败~!"+bankCardRequestBean.getAccountId());
            resultBean.setStatusDesc("调用银行接口失败~!");
            return resultBean;
        }
        // 回调数据处理
        if (retBean == null || !(BankCallStatusConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode()))) {
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            _log.info((retBean == null ? "" : retBean.getRetMsg())+bankCardRequestBean.getAccountId());
            resultBean.setStatusDesc((retBean == null ? "" : retBean.getRetMsg()));
            return resultBean;
        }
        // 执行删除卡后处理,判断银行卡状态，删除平台本地银行卡信息
        try {
            boolean isUpdateFlag = this.userDeleteCardService.updateAfterDeleteCard(bean, userId);
            if (!isUpdateFlag) {
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
                _log.info((retBean == null ? "" : retBean.getRetMsg())+bankCardRequestBean.getAccountId());
                resultBean.setStatusDesc("系统异常,请稍后再试!");
            } else {
                resultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
                resultBean.setStatusDesc("解绑银行卡成功!");
            }
        } catch (Exception e) {
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            _log.info("操作异常");
            resultBean.setStatusDesc("系统异常,请稍后再试!");
        }
        
        _log.info(bankCardRequestBean.getAccountId()+"用户解除绑定银行卡结束-----------------------------");
        return resultBean;
    }
	
}
