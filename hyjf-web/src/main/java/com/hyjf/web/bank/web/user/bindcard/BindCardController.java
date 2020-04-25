package com.hyjf.web.bank.web.user.bindcard;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseAjaxResultBean;
import com.hyjf.bank.service.user.accountopenpage.UserOpenAccountPageService;
import com.hyjf.bank.service.user.bindcard.BindCardService;
import com.hyjf.bank.service.user.recharge.RechargeService;
import com.hyjf.bank.service.user.transpassword.TransPasswordAjaxBean;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.web.BaseController;
import com.hyjf.web.bank.web.user.auto.AutoDefine;
import com.hyjf.web.bank.web.user.bindcardpage.BindCardPageDefine;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.login.LoginService;
import com.hyjf.web.util.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 江西银行用户绑卡
 * @author liuyang
 *
 */
@Controller
@RequestMapping(value = BindCardDefine.REQUEST_MAPPING)
public class BindCardController extends BaseController {

	/** THIS_CLASS */
	private static final String THIS_CLASS = BindCardController.class.getName();

	@Autowired
	private BindCardService userBindCardService;
	@Autowired
    private UserOpenAccountPageService userOpenAccountPageService;
    @Autowired
    private RechargeService userRechargeService;
    @Autowired
    LoginService loginService;

    Logger _log = LoggerFactory.getLogger(BindCardController.class);

    private static String HOST_URL = PropUtils.getSystem("hyjf.web.host");
	/**
	 * 绑卡画面初始化
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(BindCardDefine.INDEX_MAPPING)
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, BindCardDefine.CHECK_MAPPING);// 检查参数
		ModelAndView modelAndView = new ModelAndView(BindCardDefine.JSP_BINDCARD);
		Integer userId = WebUtils.getUserId(request);
		// 可用金额
		Account account = this.userBindCardService.getAccount(userId);
		BigDecimal bankAmount = account.getBankBalance();
		UsersInfo usersInfo = userBindCardService.getUsersInfoByUserId(userId);
        if(usersInfo!=null){
            if (usersInfo.getTruename() != null && usersInfo.getTruename().length() >= 1) {
                modelAndView.addObject("truename", usersInfo.getTruename().substring(0, 1) + "**");
            }
            if (usersInfo.getIdcard() != null && usersInfo.getIdcard().length()>= 15) {
                modelAndView.addObject("idcard", usersInfo.getIdcard().substring(0, 3) + "***********" + usersInfo.getIdcard().substring(usersInfo.getIdcard().length() - 4));
            }
        }
		modelAndView.addObject("bankAmount", bankAmount);
		modelAndView.addObject("bindCardUrl", BindCardDefine.HOST+BindCardPageDefine.REQUEST_MAPPING+BindCardPageDefine.REQUEST_BINDCARDPAGE+".do");
        // 江西银行绑卡接口修改 update by wj 2018-5-17 start
        modelAndView.addObject("bindType",this.userBindCardService.getBandCardBindUrlType("BIND_CARD"));
        // 江西银行绑卡接口修改 update by wj 2018-5-17 end
		return modelAndView;
	}


    /**
     * 解绑卡成功失败跳转
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(BindCardDefine.COLSE_BAND_CARD_RETURN)
    public ModelAndView closeBandCardReturn(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, BindCardDefine.COLSE_BAND_CARD_RETURN);// 检查参数
        String status = request.getParameter("status");
        String from = request.getParameter("from");
        ModelAndView modelAndView = null;
        if(StringUtils.isNotEmpty(status)){
           if("true".equals(status)){
               modelAndView = new ModelAndView(BindCardDefine.JSP_COLSE_BINDCARD_SUCCESS);
           }else if("money".equals(status)){
               modelAndView = new ModelAndView(BindCardDefine.JSP_COLSE_BINDCARD_ERROR);
               modelAndView.addObject("msg","抱歉，请先清空当前余额和待收后，再申请解绑。");
           }else {
               modelAndView  = new ModelAndView(BindCardDefine.JSP_COLSE_BINDCARD_ERROR);
               modelAndView.addObject("msg","抱歉，银行卡解绑错误，请联系客服！");
           }
        }else{
            modelAndView  = new ModelAndView(BindCardDefine.JSP_COLSE_BINDCARD_ERROR);
            modelAndView.addObject("msg","抱歉，银行卡解绑错误，请联系客服！");
        }
        modelAndView.addObject("from",from);
        return modelAndView;
    }
    /**
     * 绑卡成功跳转
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(BindCardDefine.BAND_CARD_SUCCESS)
    public ModelAndView bandCardSuccess(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView(BindCardDefine.JSP_BINDCARD_SUCCESS);
        return modelAndView;
    }
    /**
     * 绑卡失败跳转
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(BindCardDefine.BAND_CARD_ERROR)
    public ModelAndView bandCardError(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView(BindCardDefine.JSP_BINDCARD_ERROR);
        return modelAndView;
    }
    /**
     * 我的银行卡
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(BindCardDefine.MYCARD_INIT)
    public ModelAndView myCardInit(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView(BindCardDefine.JSP_MYCARD);
        WebViewUser user = WebUtils.getUser(request);
        // 未登录
        if (user == null) {
            modelAndView = new ModelAndView("redirect:" + CustomConstants.HOST + "/user/login/init.do");
        }
        Integer userId = user.getUserId();
        Users users=userRechargeService.getUsers(user.getUserId());
        if (users.getBankOpenAccount()==0) {// 未开户
            modelAndView = new ModelAndView("redirect:"+ CustomConstants.HOST +"/bank/web/user/bankopen/init.do");
            return modelAndView;
        }
        BankCard bankCard = this.userRechargeService.selectBankCardByUserId(userId);
        if(bankCard==null){
            //未绑卡
            modelAndView.addObject("bindType","0");
        }else {
            //已绑卡
            modelAndView.addObject("bindType","1");
            BanksConfig config = userRechargeService.getBanksConfigByBankId(bankCard.getBankId());
            if(config != null){
                modelAndView.addObject("bankicon",HOST_URL+config.getBankIcon());
                modelAndView.addObject("bankname",config.getBankName());
            }else{
                modelAndView.addObject("bankicon",HOST_URL+"/data/upfiles/filetemp/image/bank_log.png");
                modelAndView.addObject("bankname",StringUtils.isBlank(bankCard.getBank())?"":bankCard.getBank());
            }
            modelAndView.addObject("bankcard",BankCardUtil.getCardNo(bankCard.getCardNo()));
            modelAndView.addObject("cardId",bankCard.getId());
        }
        return modelAndView;
    }

    /**
     * 绑卡新页面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(BindCardDefine.BIND_CARD_NEW)
    public ModelAndView bindCardNew(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, BindCardDefine.CHECK_MAPPING);// 检查参数
        ModelAndView modelAndView = new ModelAndView(BindCardDefine.JSP_BIND_CARD_NEW);
        Integer userId = WebUtils.getUserId(request);
        // 未登录
        if (userId == null) {
            modelAndView = new ModelAndView("redirect:" + CustomConstants.HOST + "/user/login/init.do");
            return modelAndView;
        }
        WebViewUser user = WebUtils.getUser(request);
        UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
        userOperationLogEntity.setOperationType(10);
		userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
		userOperationLogEntity.setPlatform(0);
		userOperationLogEntity.setRemark("");
		userOperationLogEntity.setOperationTime(new Date());
		userOperationLogEntity.setUserName(user.getUsername());
		userOperationLogEntity.setUserRole(user.getRoleId());
        loginService.sendUserLogMQ(userOperationLogEntity);
        Users users=userRechargeService.getUsers(userId);
        _log.info("userId:"+userId+",user:"+users);
        if (users.getBankOpenAccount()==0) {// 未开户
            modelAndView = new ModelAndView("redirect:"+ CustomConstants.HOST +"/bank/web/user/bankopen/init.do");
            return modelAndView;
        }
        // 可用金额
        Account account = this.userBindCardService.getAccount(userId);
        BigDecimal bankAmount = account.getBankBalance();
        UsersInfo usersInfo = userBindCardService.getUsersInfoByUserId(userId);
        if(usersInfo!=null){
            if (usersInfo.getTruename() != null && usersInfo.getTruename().length() >= 1) {
                modelAndView.addObject("truename", usersInfo.getTruename().substring(0, 1) + "**");
            }
            if (usersInfo.getIdcard() != null && usersInfo.getIdcard().length()>= 15) {
                modelAndView.addObject("idcard", usersInfo.getIdcard().substring(0, 3) + "***********" + usersInfo.getIdcard().substring(usersInfo.getIdcard().length() - 4));
            }
        }
        modelAndView.addObject("bankAmount", bankAmount);
        modelAndView.addObject("bindCardUrl", BindCardDefine.HOST+BindCardPageDefine.REQUEST_MAPPING+BindCardPageDefine.REQUEST_BINDCARDPAGE+".do");
        // 江西银行绑卡接口修改 update by wj 2018-5-17 start
        modelAndView.addObject("bindType",this.userBindCardService.getBandCardBindUrlType("BIND_CARD"));
        // 江西银行绑卡接口修改 update by wj 2018-5-17 end
        return modelAndView;
    }

    /**
     * 用户绑卡增强发送验证码接口
     */
    @ResponseBody
    @RequestMapping(value = BindCardDefine.SEND_PLUS_CODE_ACTION, produces = "application/json; charset=utf-8")
    public TransPasswordAjaxBean sendPlusCode(HttpServletRequest request, HttpServletResponse response,String cardNo) {
        LogUtil.startLog(THIS_CLASS, BindCardDefine.SEND_PLUS_CODE_ACTION);
        TransPasswordAjaxBean ret = new TransPasswordAjaxBean();
        WebViewUser user = WebUtils.getUser(request);// 用户ID
        if (user == null) {
            ret.setStatus(BindCardDefine.STATUS_FALSE);
            ret.setMessage("用户未登陆");
            return ret;
        }
        String mobile = request.getParameter("mobile"); // 手机号
        if (StringUtils.isEmpty(mobile)) {
            ret.setStatus(BindCardDefine.STATUS_FALSE);
            ret.setMessage("手机号不能为空");
            return ret;
        }
        if (StringUtils.isEmpty(cardNo)) {
            ret.setStatus(BindCardDefine.STATUS_FALSE);
            ret.setMessage("银行卡号不能为空");
            return ret;
        }
        // 请求发送短信验证码
        BankCallBean bean = this.userBindCardService.cardBindPlusSendSms(user.getUserId(),PropUtils.getSystem(BankCallConstant.BANK_INSTCODE),
                BankCallMethodConstant.TXCODE_CARD_BIND_PLUS, mobile, BankCallConstant.CHANNEL_PC,cardNo);
        if (bean == null) {
            ret.setStatus(BindCardDefine.STATUS_FALSE);
            ret.setMessage("发送短信验证码异常");
            return ret;
        }
        // 返回失败
        if (!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
            if("JX900651".equals(bean.getRetCode())){
                // 成功返回业务授权码
                ret.setStatus(BindCardDefine.STATUS_TRUE);
                ret.setInfo(bean.getSrvAuthCode());// 业务授权码
                LogUtil.endLog(THIS_CLASS, BindCardDefine.SEND_PLUS_CODE_ACTION);
                return ret;
            }
            ret.setStatus(BindCardDefine.STATUS_FALSE);
            ret.setMessage("发送短信验证码失败，失败原因：" + userBindCardService.getBankRetMsg(bean.getRetCode()));
            return ret;
        }
        // 成功返回业务授权码
        ret.setStatus(BindCardDefine.STATUS_TRUE);
        ret.setInfo(bean.getSrvAuthCode());// 业务授权码
        LogUtil.endLog(THIS_CLASS, BindCardDefine.SEND_PLUS_CODE_ACTION);
        return ret;
    }





	/**
     * 用户绑卡增强
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BindCardDefine.BIND_CARD_PLUS)
    @ResponseBody
    public BaseAjaxResultBean bindCardPlus(HttpServletRequest request, HttpServletResponse response,String lastSrvAuthCode, String smsCode, String cardNo, String mobile) {
        LogUtil.startLog(THIS_CLASS, BindCardDefine.BIND_CARD_PLUS);
        BaseAjaxResultBean ret = new BaseAjaxResultBean();
        Integer userId = WebUtils.getUserId(request);
        // 检查参数
        if (checkParam(request, userId) != null) {
            ret.setMessage(checkParam(request, userId));;
            return ret;
        }

        // 检查用户是否登录
        if (Validator.isNull(lastSrvAuthCode)||Validator.isNull(smsCode)||Validator.isNull(cardNo)||Validator.isNull(mobile)) {
            LogUtil.endLog(THIS_CLASS, BindCardDefine.BIND_CARD_PLUS, "[参数不全]");
            ret.setMessage("参数不全");;
            return ret;
        }
        BankOpenAccount accountChinapnrTender = userBindCardService.getBankOpenAccount(userId);
        if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getAccount())) {
            LogUtil.endLog(THIS_CLASS, BindCardDefine.BIND_CARD_PLUS, "[用户未开户]");
            ret.setMessage("用户未开户");;
            return ret;
        }
        UsersInfo usersInfo = userBindCardService.getUsersInfoByUserId(userId);
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
        bean.setAccountId(accountChinapnrTender.getAccount());// 存管平台分配的账号
        bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);// 证件类型01身份证
        bean.setIdNo(usersInfo.getIdcard());// 证件号
        bean.setName(usersInfo.getTruename());// 姓名
        bean.setMobile(mobile);// 手机号
        bean.setCardNo(cardNo);// 银行卡号
        bean.setLastSrvAuthCode(lastSrvAuthCode);
        bean.setSmsCode(smsCode);
        bean.setUserIP(GetCilentIP.getIpAddr(request));// 客户IP
        LogAcqResBean logAcq = new LogAcqResBean();
        logAcq.setCardNo(cardNo);
        bean.setLogAcqResBean(logAcq);
        BankCallBean retBean=null;
        // 跳转到江西银行天下画面
        try {
            retBean  = BankCallUtils.callApiBg(bean);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.endLog(THIS_CLASS, BindCardDefine.BIND_CARD_PLUS, "[调用银行接口失败~!]");
            ret.setMessage("调用银行接口失败~!");;
            return ret;
        }
        if(retBean!=null&&BankCallStatusConstant.RESPCODE_MOBILE_ERROR.equals(retBean.getRetCode())){
            this.userBindCardService.getBankRetMsg(retBean.getRetCode());
            _log.info("短信验证码错误");
            ret.setMessage("短信验证码错误");
            return ret;
        }

        // 回调数据处理
        if (retBean == null || !(BankCallStatusConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode()))) {
            _log.info("银行返回状态码====="+retBean.getRetCode());
         // 执行结果(失败)
            String message = this.userBindCardService.getBankRetMsg(retBean.getRetCode());
            LogUtil.debugLog(THIS_CLASS, BindCardDefine.BIND_CARD_PLUS, "银行返码:" + retBean.getRetCode() + "绑卡失败:" + message);
            LogUtil.endLog(THIS_CLASS, BindCardDefine.BIND_CARD_PLUS, "[绑卡失败]");
            ret.setMessage("绑卡失败:" + message);
            return ret;
        }
        try {
            // 绑卡后处理
            this.userBindCardService.updateAfterBindCard(bean);
            List<BankCard> accountBankList = userBindCardService.getAccountBankByUserId(userId+"");
            if (accountBankList != null && accountBankList.size() > 0) {
                ret.success();
                ret.setMessage("绑卡成功");
                return ret;
            } else {
                ret.setMessage("银行处理中，请稍后查看");
            }
        } catch (Exception e) {
            // 执行结果(失败)
            e.printStackTrace();
            LogUtil.errorLog(THIS_CLASS, BindCardDefine.BIND_CARD_PLUS, e);
        }
        LogUtil.endLog(THIS_CLASS, BindCardDefine.REQUEST_MAPPING);
        return ret;
    }

    /**
     * 检查参数的正确性
     *
     * @param transAmt
     * @param openBankId
     * @param rechargeType
     * @return
     */
    private String checkParam(HttpServletRequest request, Integer userId) {
        // 检查用户是否登录
        if (Validator.isNull(WebUtils.getUserId(request))) {
            return "您没有登录，请登录后再进行操作。";
        }
        BankOpenAccount accountChinapnrTender = userBindCardService.getBankOpenAccount(userId);
        if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getAccount())) {
            return "用户未开户，请开户后再操作。";
        }

        // 检查用户是否登录
        if (Validator.isNull(WebUtils.getUserId(request))) {
            return "您没有登录，请登录后再进行操作。";
        }
        return null;
    }

    /**
     * 用户绑卡增强发送验证码接口
     */
    @ResponseBody
    @RequestMapping(value = "/aaa", produces = "application/json; charset=utf-8")
    public BankCallBean aaa(HttpServletRequest request, HttpServletResponse response,String m) {
        BankCallBean retBean  =  null;
        // IP地址
        String ip = CustomUtil.getIpAddr(request);
        BankCallBean bean = new BankCallBean();



        bean.setLogOrderId(GetOrderIdUtils.getOrderId2(1));
        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
        bean.setLogUserId(StringUtil.valueOf(1));
        bean.setLogRemark("用户绑卡增强");
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
        bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
        bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
        bean.setVersion(ChinaPnrConstant.VERSION_10);
        bean.setTxCode(BankCallMethodConstant.TXCODE_ACCOUNT_QUERY_BY_MOBILE);
        bean.setMobile(m);
        bean.setLogIp(ip); // IP地址


        // 调用汇付接口
        retBean = BankCallUtils.callApiBg(bean);
        System.out.println(JSONObject.toJSON(retBean));
        System.out.println(retBean.getAccountId());
        return retBean;
    }

    /**
     * 用户绑卡增强发送验证码接口
     */
    @ResponseBody
    @RequestMapping(value = "/bbb", produces = "application/json; charset=utf-8")
    public BankCallBean bbb(HttpServletRequest request, HttpServletResponse response,String accountId,String userId) {
        // 调用汇付接口(4.2.2 用户绑卡接口)
        BankCallBean bean = new BankCallBean();
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxCode(BankCallMethodConstant.TXCODE_CARD_BIND_DETAILS_QUERY);
        bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
        bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
        bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
        bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
        bean.setAccountId(accountId);// 存管平台分配的账号
        bean.setState("1"); // 查询状态 0-所有（默认） 1-当前有效的绑定卡
        bean.setLogOrderId(userId);
        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
        bean.setLogUserId(userId);
        // 调用汇付接口 4.4.11 银行卡查询接口
        BankCallBean call = BankCallUtils.callApiBg(bean);
        System.out.println( JSONObject.toJSONString(call, true));
        return call;
    }



}
