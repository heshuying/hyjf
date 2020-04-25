package com.hyjf.wechat.controller.user.bankwithdraw;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.IllegalClassException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hyjf.bank.service.user.auth.AuthService;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.bank.service.user.bankwithdraw.BankCardBean;
import com.hyjf.bank.service.user.recharge.RechargeService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.BankCardUtil;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountRecharge;
import com.hyjf.mybatis.model.auto.Accountwithdraw;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BanksConfig;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.SmsConfig;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallParamConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseMapBean;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.base.SimpleResultBean;
import com.hyjf.wechat.service.regist.UserService;
import com.hyjf.wechat.service.withdraw.WxBankWithdrawService;
import com.hyjf.wechat.util.ResultEnum;

/**
 * 微信客户端提现
 * Created by cuigq on 2018/2/13.
 */
@Controller
@RequestMapping(value = WxBankWIthdrawDefine.REQUEST_MAPPING)
public class WxBankWithdrawController extends BaseController {

    private static final String THIS_CLASS = WxBankWithdrawController.class.getName();

    @Autowired
    private WxBankWithdrawService wxBankWithdrawService;
   
    @Autowired
	private UserService userService;

    @Autowired
    private RechargeService userRechargeService;

    @Autowired
    private AuthService authService;

    @Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;//指定用smsProcesser类来初始化
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 获取提现信息 update by jijun 2018/04/25
     * @param request
     * @return
     */
    @SignValidate
    @RequestMapping(value = WxBankWIthdrawDefine.QUERY_WITHDRAW_INFO, method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean queryWithdrawInfo(HttpServletRequest request) {
        SimpleResultBean<WxQueryWIthdrawInfoVO> resultBean = new SimpleResultBean<>();

        WxQueryWIthdrawInfoVO vo = new WxQueryWIthdrawInfoVO();

        Integer userId = requestUtil.getRequestUserId(request);

        //Users user = this.wxBankWithdrawService.getUsers(userId);
        //update by jijun 2018/04/25
        List<BankCard> banks = wxBankWithdrawService.getBankCardByUserId(userId);


        //预留手机号
      	String phoneNum = ""; 
        if(CollectionUtils.isNotEmpty(banks)){
			// 获取用户银行预留手机号
			phoneNum = banks.get(0).getMobile();
		}
		if(StringUtils.isBlank(phoneNum)) {
			// 如果用户未预留手机号则取平台手机号
			//phoneNum = WebUtils.getUser(request).getMobile();
			Users user = this.wxBankWithdrawService.getUsers(userId);
			phoneNum = user.getMobile();
        }
        vo.setMobile(phoneNum);

        Account account = wxBankWithdrawService.getAccount(userId);
        if (account == null) {
            resultBean.setEnum(ResultEnum.USER_ERROR_200);
            return resultBean;
        }
        //获取企业用户标识（0普通用户1企业用户企业用户）
        Users users = wxBankWithdrawService.getUsers(userId);
        if(users != null){
            vo.setUserType(users.getUserType());
        }
        //用户角色
        String userRoId = userRechargeService.getUserRoId(userId);
        //用户的可用金额
        BigDecimal bankBalance = account.getBankBalance();
        //查询用户出借记录
        int borrowTender = userRechargeService.getBorrowTender(userId);
        if(StringUtils.equals("1",userRoId)){
            if(borrowTender<=0){
                //查询用户的24小时内充值记录
                List<AccountRecharge> todayRecharge = userRechargeService.getTodayRecharge(userId);
                if(todayRecharge!=null&&!todayRecharge.isEmpty()){
                    // 计算用户当前可提现金额
                    for (AccountRecharge recharge:todayRecharge) {
                        bankBalance=bankBalance.subtract(recharge.getBalance());
                    }
                }
            }
        }

        vo.setBankBalance(CustomConstants.DF_FOR_VIEW.format(account.getBankBalance()));
        vo.setBankBalanceOriginal(account.getBankBalance().toString());

        // 查询页面上可以挂载的银行列表
//        List<BankCard> banks = wxBankWithdrawService.getBankCardByUserId(userId);
        if (banks == null || banks.size() == 0) {
            resultBean.setEnum(ResultEnum.USER_ERROR_1003);
            return resultBean;
        }

        //? 只能绑定一个银行卡？
        Preconditions.checkArgument(banks.size() == 1);
        BankCard bankCard = banks.get(0);

        BankCardBean bankCardBean = new BankCardBean();
        BeanUtils.copyProperties(bankCard, bankCardBean);

        String cardNo = bankCard.getCardNo();
        String cardNoInfo = BankCardUtil.getCardNo(cardNo);
        bankCardBean.setCardNoInfo(cardNoInfo);
        bankCardBean.setIsDefault("2");// 卡类型

        Integer bankId = bankCard.getBankId();
        BanksConfig banksConfig = wxBankWithdrawService.getBanksConfigByBankId(bankId);
        if (banksConfig != null && StringUtils.isNotEmpty(banksConfig.getBankName())) {
            bankCardBean.setBank(banksConfig.getBankName());
        }

        String feeWithdraw = wxBankWithdrawService.getWithdrawFee(userId, cardNo);
        vo.setFeeWithdraw(feeWithdraw);

        List<BankCardBean> bankcards = Lists.newArrayList();
        bankcards.add(bankCardBean);

        vo.getLstBankCard().addAll(bankcards);
        if(bankBalance!=null){
            vo.setBalance(bankBalance.toString());
        }
        // 服务费授权
        vo.setPaymentAuthOn(CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus());
        vo.setPaymentAuthStatus(users.getPaymentAuthStatus());

        // add by liuyang 神策数据统计埋点追加 20180717 start
        // 银行名称
        vo.setBankName(StringUtils.isBlank(bankCard.getBank())? "":bankCard.getBank());
        // add by liuyang 神策数据统计埋点追加 20180717 end
        resultBean.setObject(vo);

        return resultBean;
    }

    /**
     *  提现
     * @param request
     * @param qo
     * @return
     */
    @SignValidate
    @RequestMapping(value = WxBankWIthdrawDefine.WITHDRAW_ACTION)
    public ModelAndView withdraw(HttpServletRequest request, @ModelAttribute WxBankWithdrawQO qo) {
        logger.info("提现请求参数=【{}】",qo);

        ModelAndView modelAndView;

        Integer userId = requestUtil.getRequestUserId(request);
        //用户校验
        if (userId == null) {
            modelAndView = new ModelAndView(WxBankWIthdrawDefine.JUMP_HTML);
            BaseMapBean baseMapBean = convert2CallBackForm(new BaseResultBean(ResultEnum.LOGININVALID));
            baseMapBean.setCallBackAction(CustomConstants.HOST + WxBankWIthdrawDefine.WITHDRAW_FALSE);
            modelAndView.addObject(WxBankWIthdrawDefine.RESULTBEAN, baseMapBean);
            return modelAndView;
        }

        //参数校验
        BaseResultBean resultBean = checkParam(userId, qo);
        if (!resultBean.getStatus().equals(ResultEnum.SUCCESS.getStatus())) {
            modelAndView = new ModelAndView(WxBankWIthdrawDefine.JUMP_HTML);
            BaseMapBean baseMapBean = convert2CallBackForm(new BaseResultBean(ResultEnum.PARAM));
            baseMapBean.setCallBackAction(CustomConstants.HOST + WxBankWIthdrawDefine.WITHDRAW_FALSE);
            modelAndView.addObject(WxBankWIthdrawDefine.RESULTBEAN, baseMapBean);
            return modelAndView;
        }

        //开户校验
        BankOpenAccount bankOpenAccount = wxBankWithdrawService.getBankOpenAccount(userId);
        if (bankOpenAccount == null) {
            modelAndView = new ModelAndView(WxBankWIthdrawDefine.JUMP_HTML);
            BaseMapBean baseResultBean = convert2CallBackForm(new BaseResultBean(ResultEnum.USER_WITHDRAW_005));
            baseResultBean.setCallBackAction(CustomConstants.HOST + WxBankWIthdrawDefine.WITHDRAW_FALSE);
            modelAndView.addObject(WxBankWIthdrawDefine.RESULTBEAN, baseResultBean);
            return modelAndView;
        }

        //银行卡校验
        BankCard bankCard = wxBankWithdrawService.getBankInfo(userId, qo.getCardNo());
        if (bankCard == null) {
            modelAndView = new ModelAndView(WxBankWIthdrawDefine.JUMP_HTML);
            BaseMapBean baseResultBean = convert2CallBackForm(new BaseResultBean(ResultEnum.USER_WITHDRAW_006));
            baseResultBean.setCallBackAction(CustomConstants.HOST + WxBankWIthdrawDefine.WITHDRAW_FALSE);
            modelAndView.addObject(WxBankWIthdrawDefine.RESULTBEAN, baseResultBean);
            return modelAndView;
        }
        
        //出借人提现需要验证 验证码USER_ERROR_1003
        /*UsersInfo usersInfo = wxBankWithdrawService.getUsersInfoByUserId(userId);
        // 如果是出借人提现要校验短信验证码
        if(Validator.isNotNull(usersInfo)) {
        	  if(usersInfo.getRoleId() == 1){
        		  if(Validator.isNull(qo.getSrvAuthCode())){
        			 //短息验证码为空时的判断
        			 modelAndView = new ModelAndView(WxBankWIthdrawDefine.JUMP_HTML);
     				 BaseMapBean baseResultBean = convert2CallBackForm(new BaseResultBean(ResultEnum.ERROR_011));
     				 baseResultBean.setCallBackAction(CustomConstants.HOST + WxBankWIthdrawDefine.WITHDRAW_FALSE);
     				 modelAndView.addObject(WxBankWIthdrawDefine.RESULTBEAN, baseResultBean);
     				 return modelAndView;
        		  }else {
        			  //校验短信验证码
    		        JSONObject ret=validateVerificationCodeAction(request);
    		        if(!"000".equals(ret.get("status"))) {
    		        	 modelAndView = new ModelAndView(WxBankWIthdrawDefine.JUMP_HTML);
    		        	 BaseMapBean baseResultBean = convert2CallBackForm(new BaseResultBean(ResultEnum.ERROR_018));
    		             baseResultBean.setCallBackAction(CustomConstants.HOST + WxBankWIthdrawDefine.WITHDRAW_FALSE);
    		        	 modelAndView.addObject(WxBankWIthdrawDefine.RESULTBEAN, baseResultBean);
    		             return modelAndView;
    		        }
    		        
        		  }
        		  
              }
        }else {
        	 modelAndView = new ModelAndView(WxBankWIthdrawDefine.JUMP_HTML);
        	 BaseMapBean baseResultBean = convert2CallBackForm(new BaseResultBean(ResultEnum.USER_WITHDRAW_008));
             baseResultBean.setCallBackAction(CustomConstants.HOST + WxBankWIthdrawDefine.WITHDRAW_FALSE);
        	 modelAndView.addObject(WxBankWIthdrawDefine.RESULTBEAN, baseResultBean);
             return modelAndView;
        }*/

        String sign = request.getParameter("sign");
        BankCallBean bean = buildBankCallBean(request, qo, userId, bankOpenAccount, bankCard, sign);
        logger.info("提现请求~~~~~~~~~bean~~~~~~"+JSONObject.toJSONString(bean));
        Map<String, String> params = Maps.newHashMap();
        params.put("userId", String.valueOf(userId));
        params.put("userName", bean.getLogUserName());
        params.put("ip", CustomUtil.getIpAddr(request));
        params.put("cardNo", qo.getCardNo());
        params.put("fee", CustomUtil.formatAmount(bean.getTxFee()));
        params.put("client", CustomConstants.CLIENT_WECHAT);
        // 用户提现前处理
        int cnt = this.wxBankWithdrawService.updateBeforeCash(bean, params);
        if (cnt > 0) {
            // 跳转到银行提现页面
            try {
                modelAndView = BankCallUtils.callApi(bean);
                modelAndView.addObject(WxBankWIthdrawDefine.RESULTBEAN, new BaseResultBean());
                return modelAndView;
            } catch (Exception e) {
                logger.error("调用hyjf_pay 提现接口出错，" + e.getMessage(), e);
                throw new IllegalClassException("调用hyjf_pay 提现接口出错");
            }
        } else {
            modelAndView = new ModelAndView(WxBankWIthdrawDefine.WITHDRAW_FALSE);
            modelAndView.addObject(WxBankWIthdrawDefine.RESULTBEAN, new BaseResultBean(ResultEnum.USER_WITHDRAW_007));
            return modelAndView;
        }

    }

    private BaseMapBean convert2CallBackForm(BaseResultBean baseResultBean) {
        BaseMapBean baseMapBean = new BaseMapBean();
        baseMapBean.set(CustomConstants.APP_STATUS, baseResultBean.getStatus());
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, baseResultBean.getStatusDesc());
        return baseMapBean;
    }

    private BankCallBean buildBankCallBean(HttpServletRequest request,WxBankWithdrawQO qo, Integer userId, BankOpenAccount bankOpenAccount, BankCard bankCard, String sign) {
        Users users = wxBankWithdrawService.getUsers(userId);
        UsersInfo usersInfo = wxBankWithdrawService.getUsersInfoByUserId(userId);

        //回调地址
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + 
        		WxBankWIthdrawDefine.REQUEST_MAPPING + WxBankWIthdrawDefine.RETURN_MAPPING+".page?sign="+sign;
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + 
        		WxBankWIthdrawDefine.REQUEST_MAPPING + WxBankWIthdrawDefine.CALLBACK_MAPPING;
        String logOrderId=GetOrderIdUtils.getOrderId2(userId);
        BankCallBean bean = new BankCallBean();
        bean.setLogOrderId(logOrderId);
        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间
        bean.setLogUserId(String.valueOf(userId));
        bean.setLogRemark("用户提现");
        bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_MOBILE_WITHDRAW);
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxCode(BankCallMethodConstant.TXCODE_WITHDRAW);
        bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
        bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
        bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
        bean.setAccountId(bankOpenAccount.getAccount());// 存管平台分配的账号
        bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);// 证件类型01身份证
        bean.setIdNo(usersInfo.getIdcard());// 证件号
        bean.setName(usersInfo.getTruename());// 姓名
        bean.setMobile(users.getMobile());// 手机号
        bean.setCardNo(bankCard.getCardNo());// 银行卡号
        bean.setTxAmount((new BigDecimal(qo.getTransAmt()).subtract(new BigDecimal("1"))).toString());
        bean.setTxFee(wxBankWithdrawService.getWithdrawFee(userId, qo.getCardNo()));

        bean.setRetUrl(retUrl);// 商户前台台应答地址(必须)
        bean.setSuccessfulUrl(retUrl+"&isSuccess=1&logOrderId="+logOrderId);// 商户前台台应答地址(必须)
        bean.setNotifyUrl(bgRetUrl); // 商户后台应答地址(必须)
        String forgetPassworedUrl = CustomConstants.FORGET_PASSWORD_URL + "?sign=" + sign;
        bean.setForgotPwdUrl(forgetPassworedUrl);
        if (new BigDecimal(qo.getTransAmt()).compareTo(new BigDecimal(50001)) >= 0) {
            bean.setRouteCode("2");
            bean.setCardBankCnaps(StringUtils.isEmpty(qo.getOpenCardBankCode()) ? bankCard.getPayAllianceCode() : qo.getOpenCardBankCode());
        }

        // 企业用户提现
        if (users.getUserType() == 1) { // 企业用户 传组织机构代码
            CorpOpenAccountRecord record = wxBankWithdrawService.getCorpOpenAccountRecord(userId);
            bean.setIdType(record.getCardType() != null ? String.valueOf(record.getCardType()) : BankCallConstant.ID_TYPE_COMCODE);// 证件类型 20：其他证件（组织机构代码）25：社会信用号
            bean.setIdNo(record.getBusiCode());
            bean.setName(record.getBusiName());
            bean.setRouteCode("2");
            bean.setCardBankCnaps(StringUtils.isEmpty(qo.getOpenCardBankCode()) ? bankCard.getPayAllianceCode() : qo.getOpenCardBankCode());
        }
        return bean;
    }

    /**
     * 参数校验
     *
     * @param qo
     * @return
     */
    private BaseResultBean checkParam(Integer userId, WxBankWithdrawQO qo) {
        BaseResultBean baseResultBean = new BaseResultBean();

        if (qo == null || Strings.isNullOrEmpty(qo.getTransAmt()) || Strings.isNullOrEmpty(qo.getCardNo())) {
            return new BaseResultBean(ResultEnum.PARAM);
        }

        //提现金额大于手续费
        BigDecimal transAmt = new BigDecimal(qo.getTransAmt());
        String feeWithdraw = wxBankWithdrawService.getWithdrawFee(userId, qo.getCardNo());
        if (transAmt.compareTo(new BigDecimal(feeWithdraw)) <= 0) {
            return new BaseResultBean(ResultEnum.USER_WITHDRAW_001);
        }

        Account account = this.wxBankWithdrawService.getAccount(userId);
        // 提现金额大于可用余额
        if (account == null || transAmt.compareTo(account.getBankBalance()) > 0) {
            return new BaseResultBean(ResultEnum.USER_WITHDRAW_002);
        }
        // 检查银行卡ID是否数字
        String bankId = qo.getCardNo();
        if (!NumberUtils.isNumber(bankId)) {
            return new BaseResultBean(ResultEnum.USER_WITHDRAW_003);
        }

        // 提现金额超过5万检查"银联行号"
        if ((transAmt.compareTo(new BigDecimal(50001)) >= 0) && Strings.isNullOrEmpty(qo.getOpenCardBankCode())) {
            return new BaseResultBean(ResultEnum.USER_WITHDRAW_004);
        }
        // 服务费授权校验
        if (!authService.checkPaymentAuthStatus(userId)) {
            return new BaseResultBean(ResultEnum.USER_WITHDRAW_009);
        }
        return baseResultBean;
    }


    /**
     * 用户提现后处理 同步
     *
     * @param request
     * @param bean
     * @return
     */
    @RequestMapping(value = WxBankWIthdrawDefine.RETURN_MAPPING)
    public ModelAndView cashReturn(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
        ModelAndView modelAndView;
        LogUtil.startLog(THIS_CLASS, WxBankWIthdrawDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
        bean.convert();
        logger.info("提现后同步处理开始：参数: " + bean == null ? "无" : bean.getAllParams() + "]");
        LogUtil.debugLog(THIS_CLASS, WxBankWIthdrawDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
        
        //String logOrderId = bean.getLogOrderId()==null?request.getParameter("logOrderId"):bean.getLogOrderId();
        
        Integer userId = requestUtil.getRequestUserId(request);
        //用户校验
        if (userId == null) {
        	  modelAndView = new ModelAndView(WxBankWIthdrawDefine.WITHDRAW_FALSE);
              modelAndView.addObject("message", "登录失效，请重新登陆");
              return modelAndView;
        }
        
        Accountwithdraw accountwithdraw = wxBankWithdrawService.getAccountWithdrawByOrdId(String.valueOf(userId));
        if (accountwithdraw != null) {
            modelAndView = new ModelAndView(WxBankWIthdrawDefine.JUMP_HTML);
            BaseMapBean baseMapBean = new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, ResultEnum.SUCCESS.getStatus());
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "提现成功");
            baseMapBean.set("amount", accountwithdraw.getTotal().toString());
            baseMapBean.set("charge", accountwithdraw.getFee().toString());
            baseMapBean.setCallBackAction(CustomConstants.HOST + WxBankWIthdrawDefine.WITHDRAW_SUCCESS);
            modelAndView.addObject(WxBankWIthdrawDefine.RESULTBEAN, baseMapBean);
            logger.info("提现后同步处理结束：成功,回调结束]");
        } else {
            modelAndView = new ModelAndView(WxBankWIthdrawDefine.JUMP_HTML);
            BaseMapBean baseMapBean = new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, ResultEnum.SUCCESS.getStatus());
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "银行处理中，请稍后查询交易明细");
            baseMapBean.setCallBackAction(CustomConstants.HOST + WxBankWIthdrawDefine.WITHDRAW_HANDING);
            modelAndView.addObject(WxBankWIthdrawDefine.RESULTBEAN, baseMapBean);
            logger.info("提现后同步处理结束：成功,回调结束]");
        }
        return modelAndView;
    }

    /**
     * 用户提现后处理 异步
     *
     * @param request
     * @param bean
     * @return
     */
    @ResponseBody
    @RequestMapping(WxBankWIthdrawDefine.CALLBACK_MAPPING)
    public BankCallResult cashCallBack(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
        LogUtil.startLog(THIS_CLASS, WxBankWIthdrawDefine.RETURN_MAPPING, "[交易完成后,异步回调开始]");
        BankCallResult result = new BankCallResult();
        bean.convert();
        LogUtil.debugLog(THIS_CLASS, WxBankWIthdrawDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
        logger.info("提现后异步处理开始：参数: " + bean == null ? "无" : bean.getAllParams() + "]");

        String error;
        String message;
        // 发送状态
        String status;
        // 结果返回码
        String retCode = StringUtils.isBlank(bean.getRetCode()) ? "" : bean.getRetCode();
        // 提现成功 大额提现是返回 CE999028
        if (BankCallStatusConstant.RESPCODE_SUCCESS.equals(retCode) || "CE999028".equals(retCode)) {
            // 执行结果(成功)
            status = BankCallStatusConstant.STATUS_SUCCESS;
            LogUtil.debugLog(THIS_CLASS, WxBankWIthdrawDefine.RETURN_MAPPING, "成功");
        } else {
            // 执行结果(失败)
            status = BankCallStatusConstant.STATUS_FAIL;
            LogUtil.debugLog(THIS_CLASS, WxBankWIthdrawDefine.RETURN_MAPPING, "失败");
        }
        // 更新提现状态
        try {
            Integer userId = Integer.parseInt(bean.getLogUserId());
            // 插值用参数
            Map<String, String> params = new HashMap<String, String>();
            params.put("userId", String.valueOf(userId));
            params.put("ip", CustomUtil.getIpAddr(request));
            // 执行提现后处理
            wxBankWithdrawService.handlerAfterCash(bean, params);
        } catch (Exception e) {
            // 执行结果(失败)
            status = BankCallStatusConstant.STATUS_FAIL;
            LogUtil.errorLog(THIS_CLASS, WxBankWIthdrawDefine.RETURN_MAPPING, e);
            logger.info("提现后异步处理结束：提现失败：" + e);
        }
        if (BankCallStatusConstant.STATUS_SUCCESS.equals(status)) {
            error = "000";
            message = "恭喜您，提现成功";
            // 提现金额
            BigDecimal transAmt = bean.getBigDecimal(BankCallParamConstant.PARAM_TXAMOUNT);
            transAmt = transAmt.add(new BigDecimal("1"));
            CommonSoaUtils.listedTwoWithdraw(Integer.valueOf(bean.getLogUserId()), transAmt);
            // 神策数据统计 add by liuyang 20180726 start
            try {
                if (BankCallStatusConstant.RESPCODE_SUCCESS.equals(bean.getRetCode()) || "CE999028".equals(bean.getRetCode())) {
                    // 如果提现成功,发送神策数据统计MQ
                    SensorsDataBean sensorsDataBean = new SensorsDataBean();
                    sensorsDataBean.setOrderId(bean.getLogOrderId());
                    sensorsDataBean.setEventCode("withdraw_result");
                    sensorsDataBean.setUserId(Integer.parseInt(bean.getLogUserId()));
                    this.wxBankWithdrawService.sendSensorsDataMQ(sensorsDataBean);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            // 神策数据统计 add by liuyang 20180726 end
        } else {
            error = "99";
            message = "很遗憾，提现失败";
        }
        result.setStatus(true);
        result.setErrorCode(error);
        result.setMessage(message);
        return result;
    }



    /**
     * 发送验证码
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = WxBankWIthdrawDefine.SEND_VERIFICATIONCODE_ACTION, method = RequestMethod.POST)
    public JSONObject sendVerificationCodeAction(HttpServletRequest request,@RequestBody WxBankWithdrawQO parm, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, WxBankWIthdrawDefine.SEND_VERIFICATIONCODE_ACTION);
        JSONObject ret = new JSONObject();
        ret.put("request", WxBankWIthdrawDefine.SEND_VERIFICATIONCODE_REQUEST);

        // 验证码类型
        String verificationType = parm.getVerificationType();
        // 手机号
        String mobile = parm.getMobile();

        // 业务逻辑
        try {
            // 解密
            if (Validator.isNull(verificationType)) {
                ret.put("status", "99");
                ret.put("statusDesc", "验证码类型不能为空");
                return ret;
            }
            if (Validator.isNull(mobile)) {
                ret.put("status", "99");
                ret.put("statusDesc", "手机号不能为空");
                return ret;
            }
            if (!Validator.isMobile(mobile)) {
                ret.put("status", "99");
                ret.put("statusDesc", "请输入您的真实手机号码");
                return ret;
            }
            //短信验证码模板类型 必须是 TPL_SMS_WITHDRAW
            if (!WxBankWIthdrawDefine.TPL_SMS_WITHDRAW.equals(verificationType)) {
                ret.put("status", "1");
                ret.put("statusDesc", "无效的验证码类型");
                return ret;
            }
            
            SmsConfig smsConfig = userService.getSmsConfig();
            // 短信加固
            validateSmsTemplate(mobile, smsConfig, request);

            // 发送短信
    	    boolean success = sendVerificationCodeAction(mobile, smsConfig, verificationType, request ,parm.getTransAmt());

            if (success) {
                ret.put("status", "000");
                ret.put("statusDesc", "发送验证码成功");
            } else {
                ret.put("status", "99");
                ret.put("statusDesc", "发送验证码失败");
            }

        } catch (Exception e) {
            ret.put("status", "99");
            ret.put("statusDesc", e.getMessage());
        }

        LogUtil.endLog(THIS_CLASS, WxBankWIthdrawDefine.SEND_VERIFICATIONCODE_ACTION);
        return ret;

    }


    /**
     * 短信发送时的加固验证
     * @param mobile
     * @param smsConfig
     * @param request
     * @throws Exception
     */
	private void validateSmsTemplate(String mobile, SmsConfig smsConfig, HttpServletRequest request) throws Exception {
		String ip = CustomUtil.getIpAddr(request);
		String ipCount = RedisUtils.get(ip + ":MaxIpCount");
		if (StringUtils.isBlank(ipCount)) {
			ipCount = "0";
			RedisUtils.set(ip + ":MaxIpCount", "0");
		}
//		System.out.println(mobile + "------ip---" + ip + "----------MaxIpCount-----------" + ipCount);
		if (Integer.valueOf(ipCount) >= smsConfig.getMaxIpCount()) {
			if (Integer.valueOf(ipCount).equals(smsConfig.getMaxIpCount())) {
				try {
					userService.sendSms(mobile, "IP访问次数超限:" + ip);
				} catch (Exception e) {
				}
				RedisUtils.set(ip + ":MaxIpCount", (Integer.valueOf(ipCount) + 1) + "", 24 * 60 * 60);
			}
			try {
				userService.sendEmail(mobile, "IP访问次数超限" + ip);
			} catch (Exception e) {
			}
			throw new Exception("IP访问次数超限");
		}

		// 判断最大发送数max_phone_count
		String count = RedisUtils.get(mobile + ":MaxPhoneCount");
		if (StringUtils.isBlank(count)) {
			count = "0";
			RedisUtils.set(mobile + ":MaxPhoneCount", "0");
		}
		int maxPhoneCount = smsConfig.getMaxPhoneCount();
		;// 测试时临时是30,正式上线改为smsConfig.getMaxPhoneCount();
		if (Integer.valueOf(count) >= maxPhoneCount) {
			if (Integer.valueOf(count) == maxPhoneCount) {
				try {
					userService.sendSms(mobile, "手机发送次数超限");
				} catch (Exception e) {
				}
				RedisUtils.set(mobile + ":MaxPhoneCount", (Integer.valueOf(count) + 1) + "", 24 * 60 * 60);
			}
			try {
				userService.sendEmail(mobile, "手机发送次数超限");
			} catch (Exception e) {
			}
			throw new Exception("手机发送次数超限");
		}
		// 判断发送间隔时间
		String intervalTime = RedisUtils.get(mobile + ":IntervalTime");
		if (StringUtils.isNotBlank(intervalTime)) {
			throw new Exception("验证码发送过于频繁");
		}
	}

	/**
	 * 发送短信验证码
	 * @param mobile
	 * @param smsConfig
	 * @param verificationType
	 * @param request
	 * @param transAmt
	 * @return
	 */
	private Boolean sendVerificationCodeAction(String mobile, SmsConfig smsConfig, String verificationType, HttpServletRequest request, String transAmt) {
		// 生成验证码
		String checkCode = GetCode.getRandomSMSCode(6);
		Map<String, String> param = new HashMap<String, String>();
		param.put("val_code", checkCode);
		
		//通过sign值获取当前登录用户的userId add by jijun 2018/03/29
		Integer userId = requestUtil.getRequestUserId(request);
		if(verificationType.equals(WxBankWIthdrawDefine.TPL_SMS_WITHDRAW) && userId != null){
	       UsersInfo user = userService.getUsersInfoByUserId(userId);
	       param.put("val_name", user.getTruename().substring(0, 1));
		      param.put("val_sex", user.getSex() == 2 ? "女士" : "先生");
		      param.put("val_amount", transAmt);
		      
	    }
		
		// 发送短信验证码
	    SmsMessage smsMessage =
                new SmsMessage(null, param, mobile, null, MessageDefine.SMSSENDFORMOBILE, null,
                		verificationType, CustomConstants.CHANNEL_TYPE_NORMAL);
        Integer result = (smsProcesser.gather(smsMessage) >= 1) ? 0 : 1;
        
       
        // checkCode过期时间，默认120秒
        RedisUtils.set(mobile + ":MaxValidTime", checkCode, smsConfig.getMaxValidTime() == null ? 120 : smsConfig.getMaxValidTime() * 60);

        // 发送checkCode最大时间间隔，默认60秒
        RedisUtils.set(mobile + ":IntervalTime", mobile, smsConfig.getMaxIntervalTime() == null ? 60 : smsConfig.getMaxIntervalTime());

		// 短信发送成功后处理 临时注释掉
		if (result != null && result == 0) {
			// 累加IP次数
			String ip = CustomUtil.getIpAddr(request);
			String currentMaxIpCount = RedisUtils.get(ip + ":MaxIpCount");
			if (StringUtils.isBlank(currentMaxIpCount)) {
				currentMaxIpCount = "0";
			}
			// 累加手机次数
			String currentMaxPhoneCount = RedisUtils.get(mobile + ":MaxPhoneCount");
			if (StringUtils.isBlank(currentMaxPhoneCount)) {
				currentMaxPhoneCount = "0";
			}
			RedisUtils.set(ip + ":MaxIpCount", (Integer.valueOf(currentMaxIpCount) + 1) + "", 24 * 60 * 60);
			RedisUtils.set(mobile + ":MaxPhoneCount", (Integer.valueOf(currentMaxPhoneCount) + 1) + "", 24 * 60 * 60);
		}

		// 保存短信验证码
		userService.saveSmsCode(mobile, checkCode, verificationType, result);
		return true;
	}
	
	
	
	
	/**
	 * 验证验证码
	 */
	public JSONObject validateVerificationCodeAction(HttpServletRequest request) {

		LogUtil.startLog(THIS_CLASS, WxBankWIthdrawDefine.VALIDATE_VERIFICATIONCODE_ACTION);
		JSONObject ret = new JSONObject();
		ret.put("request", WxBankWIthdrawDefine.VALIDATE_VERIFICATIONCODE_REQUEST);

		// 验证方式
		String verificationType = request.getParameter("verificationType");
		// 验证码
		//verificationCode
		String verificationCode = request.getParameter("srvAuthCode");
		// 手机号
		String mobile = request.getParameter("mobile");

		if (Validator.isNull(verificationType)) {
			ret.put("status", "99");
			ret.put("statusDesc", "验证码类型不能为空");
			return ret;
		}
		if (Validator.isNull(verificationCode)) {
			ret.put("status", "99");
			ret.put("statusDesc", "验证码不能为空");
			return ret;
		}
		//验证码类型必须是TPL_SMS_WITHDRAW
		if (!WxBankWIthdrawDefine.TPL_SMS_WITHDRAW.equals(verificationType)) {
			ret.put("status", "99");
			ret.put("statusDesc", "无效的验证码类型");
			return ret;
		}

		// 业务逻辑
		try {
			if (Validator.isNull(mobile)) {
				ret.put("status", "99");
				ret.put("statusDesc", "手机号不能为空");
				return ret;
			}
			if (!Validator.isMobile(mobile)) {
				ret.put("status", "99");
				ret.put("statusDesc", "请输入您的真实手机号码");
				return ret;
			}

			int cnt = userService.updateCheckMobileCode(mobile, verificationCode, verificationType, WxBankWIthdrawDefine.CKCODE_NEW, WxBankWIthdrawDefine.CKCODE_YIYAN);

			if (cnt > 0) {
				ret.put("status", "000");
				ret.put("statusDesc", "验证验证码成功");
			} else {
				ret.put("status", "99");
				ret.put("statusDesc", "验证码无效");
			}

		} catch (Exception e) {
			ret.put("status", "99");
			ret.put("statusDesc", "验证验证码发生错误");
		}

		LogUtil.endLog(THIS_CLASS, WxBankWIthdrawDefine.VALIDATE_VERIFICATIONCODE_ACTION);
		return ret;
	}
	
}
