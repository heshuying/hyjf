package com.hyjf.app.bank.user.withdraw;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.bank.service.user.auth.AuthService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseMapBean;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.bank.user.recharge.AppRechargeDefine;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.bank.service.user.auto.AutoService;
import com.hyjf.bank.service.user.bankwithdraw.BankWithdrawService;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.BankCardUtil;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.UpdateNoticeUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountBank;
import com.hyjf.mybatis.model.auto.AccountRecharge;
import com.hyjf.mybatis.model.auto.Accountwithdraw;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BanksConfig;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

/**
 * App提现Controller
 *
 * @author liuyang
 */
@Controller
@RequestMapping(value = AppWithdrawDefine.REQUEST_MAPPING)
public class AppWithdrawController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(AppWithdrawController.class);

    /**
     * THIS_CLASS
     */
    private final String THIS_CLASS = AppWithdrawController.class.getName();

    private final DecimalFormat df = new DecimalFormat("########0.00");

    /**
     * 发布地址
     */
    private final String HOST_URL = PropUtils.getSystem("hyjf.web.host");

    /**
     * 第三方提供的开户行查询url
     */
    private final String THIRD_QUERY_OPEN_BANK_URL = "http://www.tui78.com/bank";

    /**
     * logo的路径
     */
    private final String BANK_LOG_LOGO_PATH = "/data/upfiles/filetemp/image/bank_log.png";

    /**
     * 温馨提示URL
     */
    private final String WARM_AND_SWEET_REMINDERS_URL = "#";
    /**
     * 提现规格URL
     */
    private final String WITHDRAW_RULE_URL = "/user/withdraw/withdrawRule";
    /**
     * 卡片描述
     */
    private final String CARD_DESC = "充值限额:{0}{1}{2}";
    /**
     * 金额单位 万元
     */
    private final Integer AMOUNT_UNIT = 10000;

    @Autowired
    private AppWithdrawService userWithdrawService;
    @Autowired
    private BankWithdrawService bankWithdrawService;
    @Autowired
    private AuthService authService;


    /**
     * 获取我的银行卡
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(AppWithdrawDefine.GET_BANKCARD_MAPPING)
    public JSONObject getBankCardAction(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, AppWithdrawDefine.GET_BANKCARD_MAPPING);
        JSONObject ret = new JSONObject();
        // 版本号
        String version = request.getParameter("version");
        // 网络状态
        String netStatus = request.getParameter("netStatus");
        // 平台
        String platform = request.getParameter("platform");
        // 随机字符串
        String randomString = request.getParameter("randomString");
        // 唯一标识
        String sign = request.getParameter("sign");
        // token
        String token = request.getParameter("token");
        // order
        String order = request.getParameter("order");
        // 是否是江西银行卡列表
        String isJX = request.getParameter("isJX");
        // 检查参数正确性
        if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(token) || Validator.isNull(sign) || Validator.isNull(randomString)
                || Validator.isNull(order)) {
            ret.put("status", "1");
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }

        // 取得加密用的Key
        String key = SecretUtil.getKey(sign);
        if (Validator.isNull(key)) {
            ret.put("status", "1");
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }
        try {
            ret.put("status", "0");
            ret.put("statusDesc", "成功");
            ret.put("request", AppWithdrawDefine.GET_BANKCARD_REQUEST);

            // 取得用户iD
            Integer userId = SecretUtil.getUserId(sign);
            // 服务费授权状态和开关
            Users user = userWithdrawService.getUsers(userId);
            ret.put("paymentAuthStatus", user.getPaymentAuthStatus());
            ret.put("paymentAuthOn", CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus());

            List<AppBankCardBean> bankcards = new ArrayList<AppBankCardBean>();
            if ("0".equals(isJX)) {
                // 取得银行卡信息
                List<AccountBank> banks = userWithdrawService.getBankHfCardByUserId(userId);
                if (banks != null) {
                    ret.put("cnt", banks.size() + "");
                    for (AccountBank bank : banks) {
                        AppBankCardBean bankCardBean = new AppBankCardBean();
                        bankCardBean.setBank(bank.getBank());
                        BankConfig bankConfig = userWithdrawService.getBankInfo(bank.getBank());
                        bankCardBean.setLogo(HOST_URL + bankConfig.getAppLogo());// 应前台要求，logo路径给绝对路径
                        bankCardBean.setBank(bankConfig.getName());// 银行名称 汉字
                        bankCardBean.setCardNo(bank.getAccount());
                        bankCardBean.setCardNo_info(BankCardUtil.getCardNo(bank.getAccount()));
                        bankCardBean.setIsDefault(bank.getCardType());// 卡类型  0普通提现卡1默认卡2快捷支付卡

                        bankcards.add(bankCardBean);
                    }
                } else {
                    ret.put("cnt", "0");
                }
                ret.put("banks", bankcards);
            } else {
                // 取得银行卡信息
                List<BankCard> banks = userWithdrawService.getBankCardByUserId(userId);
                if (banks != null) {
                    ret.put("cnt", banks.size() + "");
                    for (BankCard bank : banks) {
                        AppBankCardBean bankCardBean = new AppBankCardBean();
                        Integer bankId = bank.getBankId();
                        BanksConfig banksConfig = userWithdrawService.getBanksConfigByBankId(bankId);
                        if (banksConfig != null && StringUtils.isNotEmpty(banksConfig.getBankIcon())) {
                            bankCardBean.setLogo(HOST_URL + banksConfig.getBankIcon());
                        } else {
                            bankCardBean.setLogo(HOST_URL + "/data/upfiles/filetemp/image/bank_log.png");// 应前台要求，logo路径给绝对路径
                        }

                        // 是否快捷卡
                        if (banksConfig != null && banksConfig.getQuickPayment() == 1) {
                            bankCardBean.setIsDefault("2");
                        } else {
                            bankCardBean.setIsDefault("0");
                        }
                        bankCardBean.setBank(bank.getBank());

                        if (banksConfig != null && StringUtils.isNotEmpty(banksConfig.getBankName())) {
                            bankCardBean.setBank(banksConfig.getBankName());
                        }

                        bankCardBean.setCardNo(bank.getCardNo());
                        bankCardBean.setCardNo_info(BankCardUtil.getCardNo(bank.getCardNo()));
                        // add by xiashuqing 20171205 app2.1改版新增 begin
                        // 每次限额 单位：万元
                        BigDecimal timesLimitAmount = banksConfig.getSingleQuota().divide(new BigDecimal(AMOUNT_UNIT));
                        // 每日限额 单位：万元
                        BigDecimal dayLimitAmount = banksConfig.getSingleCardQuota().divide(new BigDecimal(AMOUNT_UNIT));
                        // 每月限额 单位: 万元
                        BigDecimal monthLimitAmount = banksConfig.getMonthCardQuota().divide(new BigDecimal(AMOUNT_UNIT));
                        if (monthLimitAmount == null) {
                            monthLimitAmount = BigDecimal.ZERO;
                        }
                        // 是否支持快捷支付1:支持 2:不支持
                        String symbol = ",";
                        String symBol2 = ",";
                        if(BigDecimal.ZERO.compareTo(dayLimitAmount)==0 && BigDecimal.ZERO.compareTo(monthLimitAmount)==0){
                            symbol = "";
                            symBol2 = "";
                        }
                        if(BigDecimal.ZERO.compareTo(monthLimitAmount)==0){
                            symBol2 = "";
                        }
                        Integer quickPayment = banksConfig.getQuickPayment();
                        bankCardBean.setDesc(MessageFormat.format(CARD_DESC, (BigDecimal.ZERO.compareTo(timesLimitAmount) == 0) ? "" : "单笔" + timesLimitAmount.toString() + "万元" + symbol,
                                (BigDecimal.ZERO.compareTo(dayLimitAmount) == 0) ? "" : "单日" + dayLimitAmount.toString() + "万元" + symBol2, (BigDecimal.ZERO.compareTo(monthLimitAmount) == 0) ? "" : "单月" + monthLimitAmount.toString() + "万元"));
                        bankCardBean.setNotice(WARM_AND_SWEET_REMINDERS_URL);
                        // add by xiashuqing 20171205 app2.1改版新增 end

//						bankCardBean.setIsDefault("2");// 卡类型

                        bankcards.add(bankCardBean);
                    }
                } else {
                    ret.put("cnt", "0");
                }
            }
            ret.put("banks", bankcards);
        } catch (Exception e) {
            ret.put("status", "1");
            ret.put("statusDesc", "获取我的银行卡发生错误");
        }
        LogUtil.endLog(THIS_CLASS, AppWithdrawDefine.GET_BANKCARD_MAPPING);
        return ret;
    }

    /**
     * 获取提现信息
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(AppWithdrawDefine.GET_WITHDRAW_INFO_MAPPING)
    public AppWithdrawResult getCashInfo(HttpServletRequest request) {
        LogUtil.startLog(THIS_CLASS, AppWithdrawDefine.GET_WITHDRAW_INFO_MAPPING);

        AppWithdrawResult result = new AppWithdrawResult(AppWithdrawResult.APP_SUCCESS, AppWithdrawResult.SUCCESS_MSG, AppWithdrawDefine.GET_WITHDRAW_INFO_REQUEST);

        // 版本号
        String version = request.getParameter("version");
        // 网络状态
        String netStatus = request.getParameter("netStatus");
        // 平台
        String platform = request.getParameter("platform");
        // 随机字符串
        String randomString = request.getParameter("randomString");
        // 唯一标识
        String sign = request.getParameter("sign");
        String token = request.getParameter("token");
        String order = request.getParameter("order");
        // 提现金额
        String getcash = request.getParameter("getcash");
        // 检查参数正确性
        if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform)
                || Validator.isNull(token) || Validator.isNull(sign) || Validator.isNull(randomString)
                || Validator.isNull(order)) {
            result.setStatus("1");
            result.setStatusDesc("请求参数非法");
            return result;
        }
		
		//判断金额格式
		if(Validator.isNotNull(getcash)){
		    Pattern pattern=Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式  
	        Matcher match=pattern.matcher(getcash);   
	        if(!match.matches()){   
	            result.setStatus("1");
	            result.setStatusDesc("提现金额输入格式有误");
	            return result;
	        }
		}
        // 取得加密用的Key
        String key = SecretUtil.getKey(sign);
        if (Validator.isNull(key)) {
            result.setStatus("1");
            result.setStatusDesc("请求参数非法");
            return result;
        }

        // 验证Order
        if (!SecretUtil.checkOrder(key, token, randomString, order)) {
            result.setStatus("1");
            result.setStatusDesc("请求参数非法");
            return result;
        }

        // 提现规则静态页面的url
        result.setUrl(PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + WITHDRAW_RULE_URL);

        // 取得用户iD
        Integer userId = SecretUtil.getUserId(sign);

        // 获取用户信息
        Users user = userWithdrawService.getUsers(userId);
		logger.info("提现可用余额："+result.getTotal());
        // 取得用户当前余额
        Account account = this.userWithdrawService.getAccount(userId);

        // 服务费授权状态
        if (!authService.checkPaymentAuthStatus(userId)) {
            result.setStatus("1");
            result.setStatusDesc("请先进行服务费授权。");
            return result;
        }

        result.setTotal(CommonUtils.formatAmount(version, account.getBankBalance()));
        if (account == null) {
            result.setStatus("1");
            result.setStatusDesc("您的账户信息存在异常，请联系客服人员处理。");
            return result;
        }/*else {
        //先判断取现金额是否大于可用余额
        if (StringUtils.isNotBlank(getcash) && new BigDecimal(getcash).compareTo(account.getBankBalance()) > 0) {
            result.setStatus("1");
            result.setStatusDesc("提现金额大于可用余额");
            return result;
        }

    	}*/

        String phoneNum = "";
        // 取得银行卡信息
        List<BankCard> banks = userWithdrawService.getBankCardByUserId(userId);
        if (!CollectionUtils.isEmpty(banks)) {
            // 只会有一张银行卡
            BankCard bankCard = banks.get(0);
            // 发卡行的名称
            result.setBank(StringUtils.isBlank(bankCard.getBank())?"": bankCard.getBank());
            // 卡号
            String cardNo = bankCard.getCardNo();
            result.setCardNo(cardNo);
            result.setCardNo_info(BankCardUtil.getFormatCardNo(cardNo));
            BanksConfig banksConfig = userWithdrawService.getBanksConfigByBankId(bankCard.getBankId());
            if (banksConfig != null && StringUtils.isNotEmpty(banksConfig.getBankIcon())) {
                result.setLogo(HOST_URL + banksConfig.getBankIcon());
            } else {
                // 应前台要求，logo路径给绝对路径
                result.setLogo(HOST_URL + BANK_LOG_LOGO_PATH);
            }

            BigDecimal feeWithdraw = BigDecimal.ONE;
            String feeTemp = userWithdrawService.getWithdrawFee(userId, cardNo);
            if (StringUtils.isNotEmpty(feeTemp)) {
                feeWithdraw = new BigDecimal(feeTemp);
            }

            logger.info("提现金额 getcash: {}", getcash);
            //判断金额是否合法优化 add by cwyang 2018-4-2
            boolean legalAmount = isLegalAmount(getcash);
            // 如果提现金额是0
            if ("0".equals(getcash) || StringUtils.isEmpty(getcash) || !CommonUtils.isNumber(getcash) || !legalAmount) {
                result.setButtonWord("提现");
                // 提现手续费
                result.setFee("0.00 元");
                // 提现金额
                result.setBalance("0.00 元");
            } else {
                result.setButtonWord("确认提现".concat(CommonUtils.formatAmount(version, getcash)).concat("元"));

                String balance = "";
                if ((new BigDecimal(getcash).subtract(feeWithdraw)).compareTo(BigDecimal.ZERO) < 0) {
                    balance = "0.00";
                } else {
                    // 扣除手续费后的提现金额
                    BigDecimal transAmt = new BigDecimal(getcash).subtract(feeWithdraw);
                    balance = CommonUtils.formatAmount(version, transAmt);

                    // 大额支付需要传开户行号
                    if ((new BigDecimal(getcash).compareTo(new BigDecimal(50001)) >= 0)) {
                        // 是否是大额提现表示 0:非 1:是
                        result.setIsDisplay("1");
                        // 开户行号
                        String payAllianceCode = bankCard.getPayAllianceCode();
                        logger.info("查询开户行号：{}", payAllianceCode);
                        result.setOpenCardBankCode(payAllianceCode);
                        // 未查到到开户行号，则提供第三方网页引导用户自助查询
                        result.setOpenCardBankCodeUrl(THIRD_QUERY_OPEN_BANK_URL);
                    } else {
                        result.setIsDisplay("0");
                    }

                }
                // 提现手续费
                result.setFee(CommonUtils.formatAmount(version, feeWithdraw) + " 元");
                // 提现金额
                result.setBalance(balance + " 元");
            }
            phoneNum = bankCard.getMobile();
            // 判断是否为企业用户
            // 获取企业用户标识（0普通用户1企业用户企业用户）
            // 开户行号
            Integer userType = user.getUserType();
            if(userType!=null&&userType-1==0){
                result.setIsDisplay("1");
                String payAllianceCode = bankCard.getPayAllianceCode();
                if(payAllianceCode != null){
                    result.setOpenCardBankCode(payAllianceCode);
                    result.setOpenCardBankCodeUrl(THIRD_QUERY_OPEN_BANK_URL);
                }
            }
        }else{
            result.setStatus("1");
            result.setStatusDesc("您未绑定银行卡，请先绑卡。");
            return result;
        }

        if (StringUtils.isBlank(phoneNum)) {
            // 如果用户未绑卡则取平台手机号
            phoneNum = user.getMobile();
        }
        result.setPhoneNumber(phoneNum);

        result.setNeedSMSCode("0");

        LogUtil.endLog(THIS_CLASS, AppWithdrawDefine.GET_WITHDRAW_INFO_MAPPING);
        return result;

    }


    /**
     * 判断金额是否合法
     *
     * @param getcash
     * @return
     */
    private boolean isLegalAmount(String getcash) {

        try {
            BigDecimal amount = new BigDecimal(getcash);
        } catch (Exception e) {
            logger.info("--------提现金额错误---------");
            return false;
        }

        return true;
    }

    /**
     * 获取提现URL -- 提现前参数校验！
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(AppWithdrawDefine.GET_CASH_URL_MAPPING)
    public JSONObject getCashUrl(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, AppWithdrawDefine.GET_CASH_URL_MAPPING);
        JSONObject ret = new JSONObject();
        // 版本号
        String version = request.getParameter("version");
        // 网络状态
        String netStatus = request.getParameter("netStatus");
        // 平台
        String platform = request.getParameter("platform");
        // 随机字符串
        String randomString = request.getParameter("randomString");
        // 唯一标识
        String sign = request.getParameter("sign");
        // token
        String token = request.getParameter("token");
        // order
        String order = request.getParameter("order");
        // card 银行卡号
        String cardNo = request.getParameter("cardNo");
        // getcash 提现金额
        String total = request.getParameter("total");
        // 银联行号
        String openCardBankCode = request.getParameter("openCardBankCode");
        String phoneNumber = request.getParameter("phoneNumber");
        String smsCodeWithDraw = request.getParameter("smsCodeWithDraw");

        // 检查参数正确性
        if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(token) || Validator.isNull(sign) || Validator.isNull(randomString)
                || Validator.isNull(order) || Validator.isNull(cardNo) || Validator.isNull(total) || !CommonUtils.isNumber(total)) {
            ret.put("status", "1");
            ret.put("statusDesc", "请求参数非法");
            ret.put("request", AppWithdrawDefine.GET_CASH_URL_REQUEST);
            return ret;
        }
        if (new BigDecimal(total).compareTo(BigDecimal.ONE) <= 0) {
            ret.put("status", "1");
            ret.put("statusDesc", "提现金额需大于1元！");
            ret.put("request", AppWithdrawDefine.GET_CASH_URL_REQUEST);
            return ret;
        }

        Integer userId = SecretUtil.getUserId(sign); // 取得用户ID


        String transAmt = request.getParameter("total");// 交易金额
        // 取得用户当前余额
        Account account = this.userWithdrawService.getAccount(userId);
        // 投标金额大于可用余额
        if (account == null || new BigDecimal(transAmt).compareTo(account.getBankBalance()) > 0) {
            ret.put("status", "1");
            ret.put("statusDesc", "提现金额大于可用金额，请确认后再次提现。");
            ret.put("request", AppWithdrawDefine.GET_CASH_URL_REQUEST);
            return ret;
        }
        //可用金额
        BigDecimal total2 = account.getBankBalance();
        //可提现金额
        BigDecimal availableCash = null;
        // 获取用户角色
        UsersInfo usersInfo = userWithdrawService.getUsersInfoByUserId(userId);
        if (usersInfo != null && usersInfo.getRoleId() == 1) {
            int tenderRecord = userWithdrawService.getTenderRecord(userId);
            if (tenderRecord <= 0) {
                List<AccountRecharge> accountRecharges = userWithdrawService.getRechargeMoney(userId);
                // 当天充值，提现金额为当前余额减去当日充值金额
                if (!CollectionUtils.isEmpty(accountRecharges)) {
                    for (AccountRecharge accountRecharge : accountRecharges) {
                        total2 = total2.subtract(accountRecharge.getBalance());
                        availableCash = total2;
                    }
                    if (StringUtils.isNotBlank(transAmt) && new BigDecimal(transAmt).compareTo(availableCash) > 0) {
                        ret.put("status", "1");
                        ret.put("statusDesc", "当天充值资金当天无法提现，请调整取现金额。");
                        ret.put("request", AppWithdrawDefine.GET_CASH_URL_REQUEST);
                        return ret;
                    }
                }
            }
        }

        logger.info("开户行号openCardBankCode is :{}", openCardBankCode);
        // 人行通道 提现校验

        String routeCode = "";
        if (new BigDecimal(total).compareTo(new BigDecimal(50001)) >= 0) {
            routeCode = "2";// 路由代码
        }

        if ("2".equals(routeCode) && StringUtils.isBlank(openCardBankCode)) {

            ret.put("status", "1");
            ret.put("statusDesc", "大额提现时,开户行号不能为空");
            ret.put("request", AppWithdrawDefine.GET_CASH_URL_REQUEST);
            return ret;
        }

        // 取得加密用的Key
        String key = SecretUtil.getKey(sign);
        if (Validator.isNull(key)) {
            ret.put("status", "1");
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }
        // 验证Order
        if (!SecretUtil.checkOrder(key, token, randomString, order)) {
            ret.put("status", "1");
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }

        try {
        	String uuid = getUUID();
			RedisUtils.set("widraw"+cardNo, uuid);
            ret.put("status", "0");
            ret.put("statusDesc", "成功");
            ret.put("request", AppWithdrawDefine.GET_CASH_URL_REQUEST);
            StringBuffer sbUrl = new StringBuffer();
            sbUrl.append(PropUtils.getSystem(CustomConstants.HYJF_WEB_URL));
            sbUrl.append(request.getContextPath());
            sbUrl.append(AppWithdrawDefine.REQUEST_MAPPING);
            sbUrl.append(AppWithdrawDefine.CASH_MAPPING);
            sbUrl.append("?").append("version").append("=").append(version);
            sbUrl.append("&").append("netStatus").append("=").append(netStatus);
            sbUrl.append("&").append("platform").append("=").append(platform);
            sbUrl.append("&").append("randomString").append("=").append(randomString);
            sbUrl.append("&").append("sign").append("=").append(sign);
            sbUrl.append("&").append("token").append("=").append(strEncode(token));
            sbUrl.append("&").append("order").append("=").append(strEncode(order));
            sbUrl.append("&").append("cardNo").append("=").append(cardNo);
            sbUrl.append("&").append("total").append("=").append(total);
            sbUrl.append("&").append("routeCode").append("=").append(routeCode);
            sbUrl.append("&").append("openCardBankCode").append("=").append(openCardBankCode);
            sbUrl.append("&").append("phoneNumber").append("=").append(phoneNumber);
            sbUrl.append("&").append("smsCodeWithDraw").append("=").append(smsCodeWithDraw);
            logger.info("返回提现url为: {}", sbUrl.toString());
            ret.put("url", sbUrl.toString());
        } catch (Exception e) {
            ret.put("status", "1");
            ret.put("statusDesc", "获取提现URL失败");
        }

        LogUtil.endLog(THIS_CLASS, AppWithdrawDefine.GET_CASH_URL_MAPPING);
        return ret;
    }

    /**
	 * 生成UUID
	 * @return
	 */
	public static String getUUID(){
        UUID uuid=UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr=str.replace("-", "");
        return uuidStr;
      }

    /**
     * 用户提现
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(AppWithdrawDefine.CASH_MAPPING)
    public ModelAndView cash(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(AppWithdrawController.class.toString(), AppWithdrawDefine.CASH_MAPPING);
        ModelAndView modelAndView = new ModelAndView(AppWithdrawDefine.JSP_CHINAPNR_SEND);
        String message = "";
        // 版本号
        String version = request.getParameter("version");
        // 网络状态
        String netStatus = request.getParameter("netStatus");
        // 平台

        String platform = request.getParameter("platform");
        // 随机字符串
        String randomString = request.getParameter("randomString");
        // 唯一标识
        String sign = request.getParameter("sign");
        // token
        String token = request.getParameter("token");
        // order
        String order = request.getParameter("order");
        // 路由代码
        String routeCode = request.getParameter("routeCode");

        String phoneNumber = request.getParameter("phoneNumber");
        String smsCodeWithDraw = request.getParameter("smsCodeWithDraw");

        // 银行联号
        String payAllianceCode = request.getParameter("openCardBankCode");
        logger.info("payAllianceCode: {}", payAllianceCode);


        logger.info("================cwyang 开始江西银行提现==================");
        // 检查参数正确性
        if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(token) || Validator.isNull(sign) || Validator.isNull(randomString)
                || Validator.isNull(order)) {
            message = "请求参数非法";
            modelAndView = new ModelAndView(AppRechargeDefine.JUMP_HTML);
            BaseMapBean baseMapBean = new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
            baseMapBean.setCallBackAction(CustomConstants.HOST + AppWithdrawDefine.WITHDRAW_FAIL_HTML);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        // 取得加密用的Key
        String key = SecretUtil.getKey(sign);
        if (Validator.isNull(key)) {
            message = "请求参数非法";
            modelAndView = new ModelAndView(AppRechargeDefine.JUMP_HTML);
            BaseMapBean baseMapBean = new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
            baseMapBean.setCallBackAction(CustomConstants.HOST + AppWithdrawDefine.WITHDRAW_FAIL_HTML);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        Integer userId = SecretUtil.getUserId(sign); // 取得用户ID
        String userName = SecretUtil.getUserName(sign); // 用户名
        String transAmt = request.getParameter("total");// 交易金额
        logger.info("================原始交易金额为:" + transAmt);
        String cardNo = request.getParameter("cardNo");// 提现银行卡号
        logger.info("银行卡："+cardNo+"开始提现！");
        //获取唯一标识uuid
  		String uuid = RedisUtils.get("widraw"+cardNo);
  		logger.info("删除前uuid:"+uuid);
  		if (Validator.isNull(uuid)) {

  			message = "重复提现";
            modelAndView = new ModelAndView(AppRechargeDefine.JUMP_HTML);
            BaseMapBean baseMapBean = new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
            baseMapBean.setCallBackAction(CustomConstants.HOST + AppWithdrawDefine.WITHDRAW_FAIL_HTML);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
  		}else{
  			RedisUtils.del("widraw"+cardNo);
  		}
  		String afuuid = RedisUtils.get("widraw"+cardNo);
  		logger.info("删除后uuid:"+afuuid);
        // 检查参数
        JSONObject checkResult = checkParam(request, userId, transAmt, cardNo);
        if (checkResult != null) {
            message = checkResult.getString("statusDesc");
            if ("提现金额需大于1元！".equals(message)) {
                modelAndView = new ModelAndView(AppRechargeDefine.JUMP_HTML);
                BaseMapBean baseMapBean = new BaseMapBean();
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
                baseMapBean.setCallBackAction(CustomConstants.HOST + AppWithdrawDefine.WITHDRAW_FAIL_HTML);
                modelAndView.addObject("callBackForm", baseMapBean);
                return modelAndView;

            } else if ("提现金额大于可用余额，请确认后再次提现。".equals(message)) {
                modelAndView = new ModelAndView(AppRechargeDefine.JUMP_HTML);
                BaseMapBean baseMapBean = new BaseMapBean();
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
                baseMapBean.setCallBackAction(CustomConstants.HOST + AppWithdrawDefine.WITHDRAW_FAIL_HTML);
                modelAndView.addObject("callBackForm", baseMapBean);
                return modelAndView;
            } else {
                modelAndView = new ModelAndView(AppRechargeDefine.JUMP_HTML);
                BaseMapBean baseMapBean = new BaseMapBean();
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
                baseMapBean.setCallBackAction(CustomConstants.HOST + AppWithdrawDefine.WITHDRAW_FAIL_HTML);
                modelAndView.addObject("callBackForm", baseMapBean);
                return modelAndView;
            }
        }

            Users users = userWithdrawService.getUsers(userId);
            UsersInfo usersInfo = userWithdrawService.getUsersInfoByUserId(userId);
            if(UpdateNoticeUtils.checkForAppVersion(version,"3.0.6")){
            // 如果是出借人提现要校验短信验证码
                       if (usersInfo.getRoleId() == 1) {
                           if (Validator.isNull(smsCodeWithDraw)) {
                               message = "请求参数非法";
                               modelAndView = new ModelAndView(AppRechargeDefine.JUMP_HTML);
                               BaseMapBean baseMapBean = new BaseMapBean();
                               baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                               baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
                               baseMapBean.setCallBackAction(CustomConstants.HOST + AppWithdrawDefine.WITHDRAW_FAIL_HTML);
                               modelAndView.addObject("callBackForm", baseMapBean);
                               return modelAndView;
                           }

                           int smsCheck = bankWithdrawService.updateCheckMobileCode(phoneNumber, smsCodeWithDraw, "TPL_SMS_WITHDRAW", platform, 8, 9);
                           if (smsCheck == 0) {
                               modelAndView = new ModelAndView(AppRechargeDefine.JUMP_HTML);
                               BaseMapBean baseMapBean = new BaseMapBean();
                               baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                               baseMapBean.set(CustomConstants.APP_STATUS_DESC, "验证码无效");
                               baseMapBean.setCallBackAction(CustomConstants.HOST + AppWithdrawDefine.WITHDRAW_FAIL_HTML);
                               modelAndView.addObject("callBackForm", baseMapBean);
                               return modelAndView;
                           }
                }
         }


        // 取得用户在汇付天下的客户号
        BankOpenAccount accountChinapnrTender = userWithdrawService.getBankOpenAccount(userId);
        if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getAccount())) {
            message = "您还未开户，请开户后重新操作";
            LogUtil.endLog(THIS_CLASS, AppWithdrawDefine.CASH_MAPPING, "[用户未开户]");
            modelAndView = new ModelAndView(AppRechargeDefine.JUMP_HTML);
            BaseMapBean baseMapBean = new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
            baseMapBean.setCallBackAction(CustomConstants.HOST + AppWithdrawDefine.WITHDRAW_FAIL_HTML);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }

        // 校验 银行卡号
        BankCard bankCard = this.userWithdrawService.getBankInfo(userId, cardNo);
        if (bankCard == null || Validator.isNull(bankCard.getCardNo())) {
            message = "该银行卡信息不存在，请核实后重新操作";
            LogUtil.endLog(THIS_CLASS, AppWithdrawDefine.CASH_MAPPING, "该银行卡信息不存在，请核实后重新操作");
            modelAndView = new ModelAndView(AppRechargeDefine.JUMP_HTML);
            BaseMapBean baseMapBean = new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
            baseMapBean.setCallBackAction(CustomConstants.HOST + AppWithdrawDefine.WITHDRAW_FAIL_HTML);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;

        }
        // 取得手续费
        String fee = this.userWithdrawService.getWithdrawFee(userId, bankCard.getCardNo());
        // 交易金额
        BigDecimal txAmount = new BigDecimal(transAmt).subtract(new BigDecimal(fee));

//		// 工行或中行 提现金额大于 5万时
//		if (bankId == 7 || bankId == 56) {
        // 扣除手续费
        if ((txAmount.compareTo(new BigDecimal(50000)) >= 0) && StringUtils.isNotBlank(payAllianceCode)) {
            routeCode = "2";// 路由代码
        }
//		} else {
//			// 其他银行 提现金额大于20万时
//			if ((txAmount.compareTo(new BigDecimal(200000)) > 0) && StringUtils.isNotBlank(payAllianceCode)) {
//				routeCode = "2";// 路由代码
//			}
//		}
        // 如果是大额提现,并银联行号为空
        if ("2".equals(routeCode) && Validator.isNull(payAllianceCode)) {
            message = "大额提现时,开户行号不能为空。";
            LogUtil.endLog(THIS_CLASS, AppWithdrawDefine.CASH_MAPPING, "大额提现时,开户行号不能为空。");
            modelAndView = new ModelAndView(AppRechargeDefine.JUMP_HTML);
            BaseMapBean baseMapBean = new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
            baseMapBean.setCallBackAction(CustomConstants.HOST + AppWithdrawDefine.WITHDRAW_FAIL_HTML);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        logger.info("==============cwyang 交易金额 = 原始金额减去手续费 " + txAmount.toString());

        // 调用汇付接口(提现)
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + AppWithdrawDefine.REQUEST_MAPPING + AppWithdrawDefine.RETURN_MAPPING;// 提现同步回调路径
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + AppWithdrawDefine.REQUEST_MAPPING + AppWithdrawDefine.CALLBACK_MAPPING;// 提现异步回调路径
        String successfulUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
                + AppWithdrawDefine.REQUEST_MAPPING + AppWithdrawDefine.RETURN_MAPPING + "?isSuccess=1&fee="
                + CustomUtil.formatAmount(fee) + "&amt=" + CustomUtil.formatAmount(transAmt.toString());//

        // 调用汇付接口(4.2.2 用户绑卡接口)
        BankCallBean bean = new BankCallBean();
        bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
        bean.setLogUserId(String.valueOf(userId));
        bean.setLogRemark("用户提现");
        bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_MOBILE_WITHDRAW);
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxCode(BankCallMethodConstant.TXCODE_WITHDRAW);
        bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
        bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
        bean.setChannel(BankCallConstant.CHANNEL_APP);// 交易渠道
        bean.setAccountId(accountChinapnrTender.getAccount());// 存管平台分配的账号
        bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);// 证件类型01身份证
        bean.setIdNo(usersInfo.getIdcard());// 证件号
        bean.setName(usersInfo.getTruename());// 姓名
        bean.setMobile(users.getMobile());// 手机号
        bean.setCardNo((bankCard != null && StringUtils.isNotBlank(bankCard.getCardNo())) ? bankCard.getCardNo() : "");// 银行卡号
        bean.setTxAmount(CustomUtil.formatAmount(txAmount.toString()));
        bean.setTxFee(fee);
        // 交易成功后的链接
        bean.setSuccessfulUrl(successfulUrl);

        // 大额提现时,走人行通道
        if ("2".equals(routeCode) && StringUtils.isNotBlank(payAllianceCode)) {
            bean.setRouteCode(routeCode);
            bean.setCardBankCnaps(payAllianceCode);// 绑定银行联行号
            LogAcqResBean logAcq = new LogAcqResBean();
            logAcq.setPayAllianceCode(payAllianceCode); // 银联行号放到私有域中
            bean.setLogAcqResBean(logAcq);
        }
        // 企业用户提现
        if (users.getUserType() == 1) { // 企业用户 传组织机构代码
            CorpOpenAccountRecord record = userWithdrawService.getCorpOpenAccountRecord(userId);
            bean.setIdType(record.getCardType() != null ? String.valueOf(record.getCardType()) : BankCallConstant.ID_TYPE_COMCODE);// 证件类型 20：其他证件（组织机构代码）25：社会信用号
            bean.setIdNo(record.getBusiCode());
            bean.setName(record.getBusiName());
            bean.setRouteCode("2");
            bean.setCardBankCnaps(StringUtils.isEmpty(payAllianceCode) ? bankCard.getPayAllianceCode() : payAllianceCode);
        }

        String forgetPassworedUrl = CustomConstants.FORGET_PASSWORD_URL + "?sign=" + sign + "&token=" + token+"&platform="+platform;
        bean.setForgotPwdUrl(forgetPassworedUrl);
        bean.setRetUrl(retUrl);// 商户前台台应答地址(必须)
        bean.setNotifyUrl(bgRetUrl); // 商户后台应答地址(必须)
        logger.info("绑卡前台回调函数：\n" + bean.getRetUrl());
        logger.info("绑卡后台回调函数：\n" + bean.getNotifyUrl());
        // 插值用参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", String.valueOf(userId));
        params.put("userName", userName);
        params.put("ip", CustomUtil.getIpAddr(request));
        params.put("cardNo", cardNo);
        params.put("client", platform);// 平台类型 0pc 1WX 2AND 3IOS 4other
        params.put("fee", CustomUtil.formatAmount(fee));// 手续费
        // 用户提现前处理
        int cnt = this.userWithdrawService.updateBeforeCash(bean, params);
        if (cnt > 0) {
            // 跳转到江西银行画面
            try {
                modelAndView = BankCallUtils.callApi(bean);
            } catch (Exception e) {
                e.printStackTrace();
                message = "系统异常请联系客服";
                modelAndView = new ModelAndView(AppRechargeDefine.JUMP_HTML);
                BaseMapBean baseMapBean = new BaseMapBean();
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
                baseMapBean.setCallBackAction(CustomConstants.HOST + AppWithdrawDefine.WITHDRAW_FAIL_HTML);
                modelAndView.addObject("callBackForm", baseMapBean);
                return modelAndView;
            }
        } else {
            message = "请不要重复操作";
            modelAndView = new ModelAndView(AppRechargeDefine.JUMP_HTML);
            BaseMapBean baseMapBean = new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
            baseMapBean.setCallBackAction(CustomConstants.HOST + AppWithdrawDefine.WITHDRAW_FAIL_HTML);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
        LogUtil.endLog(AppWithdrawController.class.toString(), AppWithdrawDefine.CASH_MAPPING);
        return modelAndView;
    }

    /**
     * 用户提现后处理 同步
     *
     * @param request
     * @param bean
     * @return
     */
    @RequestMapping(AppWithdrawDefine.RETURN_MAPPING)
    public ModelAndView cashReturn(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
        LogUtil.startLog(THIS_CLASS, AppWithdrawDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
        bean.convert();
        logger.info("提现后同步处理开始：参数: " + bean == null ? "无" : bean.getAllParams() + "]");
        LogUtil.debugLog(THIS_CLASS, AppWithdrawDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
        String logOrderId = bean.getLogOrderId();
        Accountwithdraw accountwithdraw = userWithdrawService.getAccountWithdrawByOrdId(logOrderId);
        String isSuccess = request.getParameter("isSuccess");
        // 调用成功了
        if (isSuccess != null && "1".equals(isSuccess)) {
            if (accountwithdraw != null) {
                ModelAndView modelAndView = new ModelAndView(AppRechargeDefine.JUMP_HTML);
                BaseMapBean baseMapBean = new BaseMapBean();
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, "提现成功");
                baseMapBean.set("amount", accountwithdraw.getTotal().toString());
                baseMapBean.set("charge", accountwithdraw.getFee().toString());
                baseMapBean.setCallBackAction(CustomConstants.HOST + AppWithdrawDefine.WITHDRAW_SUCCESS_HTML);
                modelAndView.addObject("callBackForm", baseMapBean);
                logger.info("提现后同步处理结束：成功,回调结束]");
                return modelAndView;
            } else {
                ModelAndView modelAndView = new ModelAndView(AppRechargeDefine.JUMP_HTML);
                BaseMapBean baseMapBean = new BaseMapBean();
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, "提现成功");
                baseMapBean.set("amount", request.getParameter("amt"));
                baseMapBean.set("charge", request.getParameter("fee"));
                baseMapBean.setCallBackAction(CustomConstants.HOST + AppWithdrawDefine.WITHDRAW_SUCCESS_HTML);
                modelAndView.addObject("callBackForm", baseMapBean);
                logger.info("提现后同步处理结束：成功,回调结束]");
                return modelAndView;
            }
        }

        if (accountwithdraw != null) {
            ModelAndView modelAndView = new ModelAndView(AppRechargeDefine.JUMP_HTML);
            BaseMapBean baseMapBean = new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "提现成功");
            baseMapBean.set("amount", accountwithdraw.getTotal().toString());
            baseMapBean.set("charge", accountwithdraw.getFee().toString());
            baseMapBean.setCallBackAction(CustomConstants.HOST + AppWithdrawDefine.WITHDRAW_SUCCESS_HTML);
            modelAndView.addObject("callBackForm", baseMapBean);
            logger.info("提现后同步处理结束：成功,回调结束]");
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView(AppRechargeDefine.JUMP_HTML);
            BaseMapBean baseMapBean = new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "银行处理中，请稍后查询交易明细");
            baseMapBean.setCallBackAction(CustomConstants.HOST + AppWithdrawDefine.WITHDRAW_HANDING_HTML);
            modelAndView.addObject("callBackForm", baseMapBean);
            logger.info("提现后同步处理结束：成功,回调结束]");
            return modelAndView;
        }

    }

    /**
     * 用户提现后处理 异步
     *
     * @param request
     * @param bean
     * @return
     */
    @ResponseBody
    @RequestMapping(AppWithdrawDefine.CALLBACK_MAPPING)
    public String cashCallBack(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
        LogUtil.startLog(THIS_CLASS, AppWithdrawDefine.RETURN_MAPPING, "[交易完成后,异步回调开始]");
        BankCallResult result = new BankCallResult();
        bean.convert();
        LogUtil.debugLog(THIS_CLASS, AppWithdrawDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
        logger.info("提现后异步处理开始：参数: " + bean == null ? "无" : bean.getAllParams() + "]");
        String error = "0";
        String message = "";
        // 发送状态
        String status = BankCallStatusConstant.STATUS_VERTIFY_OK;
        // 结果返回码
        String retCode = bean == null ? "" : bean.getRetCode();
        // 提现成功 大额提现是返回 CE999028
        if (BankCallStatusConstant.RESPCODE_SUCCESS.equals(retCode) || "CE999028".equals(retCode)) {
            // 执行结果(成功)
            status = BankCallStatusConstant.STATUS_SUCCESS;
            LogUtil.debugLog(THIS_CLASS, AppWithdrawDefine.RETURN_MAPPING, "成功");
        } else {
            // 执行结果(失败)
            status = BankCallStatusConstant.STATUS_FAIL;
            LogUtil.debugLog(THIS_CLASS, AppWithdrawDefine.RETURN_MAPPING, "失败");
        }
        // 更新提现状态
        try {
            Integer userId = Integer.parseInt(bean.getLogUserId());
            // 插值用参数
            Map<String, String> params = new HashMap<String, String>();
            params.put("userId", String.valueOf(userId));
            params.put("ip", CustomUtil.getIpAddr(request));
            // 执行提现后处理
            this.userWithdrawService.handlerAfterCash(bean, params);
            // 神策数据统计 add by liuyang 20180725 start
            // 提现成功 大额提现是返回 CE999028
            if (BankCallStatusConstant.RESPCODE_SUCCESS.equals(retCode) || "CE999028".equals(retCode)) {
                // 提现成功后,发送神策数据统计MQ
                SensorsDataBean sensorsDataBean = new SensorsDataBean();
                sensorsDataBean.setOrderId(bean.getLogOrderId());
                sensorsDataBean.setEventCode("withdraw_result");
                sensorsDataBean.setUserId(Integer.parseInt(bean.getLogUserId()));
                this.bankWithdrawService.sendSensorsDataMQ(sensorsDataBean);
            }
            // 神策数据统计 add by liuyang 20180725 end
        } catch (Exception e) {
            // 执行结果(失败)
            status = BankCallStatusConstant.STATUS_FAIL;
            LogUtil.errorLog(THIS_CLASS, AppWithdrawDefine.RETURN_MAPPING, e);
            logger.info("提现后异步处理结束：提现失败：" + e);
        }
        if (BankCallStatusConstant.STATUS_SUCCESS.equals(status)) {
            error = "0";
            message = "恭喜您，提现成功";
        } else {
            error = "1";
            message = "很遗憾，提现失败";
        }
        result.setErrorCode(error);
        result.setMessage(message);
        result.setStatus(true);
        return JSONObject.toJSONString(result, true);
    }

    /**
     * 检查参数的正确性
     *
     * @param userId
     * @param transAmtStr
     * @param bankId
     * @return
     */
    private JSONObject checkParam(HttpServletRequest request, Integer userId, String transAmtStr, String bankId) {
        // 检查用户是否登录
        if (Validator.isNull(userId)) {
            return jsonMessage("您没有登录，请登录后再进行提现。", "1");
        }
        // 判断用户是否被禁用
        Users users = this.userWithdrawService.getUsers(userId);
        if (users == null || users.getStatus() == 1) {
            return jsonMessage("对不起,该用户已经被禁用。", "1");
        }
        // 检查参数(交易金额是否数字)
        if (Validator.isNull(transAmtStr) || !NumberUtils.isNumber(transAmtStr)) {
            return jsonMessage("请输入提现金额。", "1");
        }
        // 检查参数(交易金额是否大于0)
        BigDecimal transAmt = new BigDecimal(transAmtStr);
        if (transAmt.compareTo(BigDecimal.ONE) <= 0) {
            return jsonMessage("提现金额需大于1元！", "1");
        }
        // 取得用户当前余额
        Account account = this.userWithdrawService.getAccount(userId);
        // 投标金额大于可用余额
        if (account == null || transAmt.compareTo(account.getBankBalance()) > 0) {
            return jsonMessage("提现金额大于可用余额，请确认后再次提现。", "1");
        }
        // 检查参数(银行卡ID是否数字)
        if (Validator.isNotNull(bankId) && !NumberUtils.isNumber(bankId)) {
            return jsonMessage("银行卡号不正确，请确认后再次提现。", "1");
        }
        // 缴费授权状态  合规接口改造一期 update by jijun 2018/04/09
		/*if (users.getPaymentAuthStatus() != 1) {
			return jsonMessage("未服务费授权。", "1");
		}*/
        return null;
    }

    /**
     * 组成返回URL for app
     *
     * @param message
     * @param error
     * @return
     */
    public String redirectAppUrl(HttpServletRequest request, String error, String message) {

        String url = "";
        if ("0".equals(error)) {
            url = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + AppWithdrawDefine.WITHDRAW_SUCCESS_PATH;
        } else {
            url = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + AppWithdrawDefine.WITHDRAW_ERROR_PATH;
        }
        return url;
    }

    /**
     * 获取提现规则H5页面
     *
     * @return
     * @author renxingchen
     */
    @RequestMapping(AppWithdrawDefine.GET_WITHDRAW_RULE_MAPPING)
    public ModelAndView rechargeRule() {
        return new ModelAndView(AppWithdrawDefine.WITHDRAW_RULE_PATH);
    }
}
