package com.hyjf.wechat.controller.user.recharge;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.recharge.RechargeService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.BankCardUtil;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BanksConfig;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.util.ResultEnum;

/**
 * App端用户充值Controller
 *
 * @author liushouyi
 *
 */
@Controller
@RequestMapping(value = WxRechargeDefine.REQUEST_MAPPING)
public class WxRechargeController extends BaseController {

	/** THIS_CLASS */
	private static final String THIS_CLASS = WxRechargeController.class.getName();

	private Logger _log = LoggerFactory.getLogger(WxRechargeController.class);

//	@Autowired
//	private WxRechargeService wxRechargeService;
	/** 发布地址 */
	private static String HOST_URL = PropUtils.getSystem("hyjf.web.host");
	/** 充值接口 */
	public static final String RECHARGE_URL = HOST_URL + "/hyjf-wechat/wx/recharge/userRecharge?";
	/** 充值描述 */
	private final String CARD_DESC = "限额:单笔{0}，单日{1}，单月{2}";
	private final String RECHARGE_KINDLY_REMINDER = "友情提示:您可以通过支付宝转账、银行柜台转账、网银转账、手机银行转账的方式，将资金充值到您的江西银行存管账户，实现账户充值，须填写的信息如上。";
	private final String RCV_ACCOUNT_NAME = "惠众商务顾问（北京）有限公司";
	private final String RCV_ACCOUNT = "791913149300306";
	private final String RCV_OPEN_BANK_NAME = "江西银行南昌铁路支行";
	private final String KINDLY_REMINDER = "注：①用户必须使用在平台唯一绑定银行卡进行充值；<br/>②银行转账时，请选择（城市商业银行）江西银行或者南昌银行；<br/>③线下充值的到账时间一般为1-3天（具体到账时间以银行的实际到账时间为准）；";
	/** 金额单位 万元 */
	private final Integer AMOUNT_UNIT = 10000;

	@Autowired
	private RechargeService userRechargeService;

	/**
	 *
	 * wx获取快捷充值卡及手续费的数据接口
	 *
	 * @author liushouyi
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = WxRechargeDefine.GET_QP_RECHARGE_INFO_ACTION)
	@SignValidate
	public BaseResultBean getQpRechargeInfo(WxRechargeVo vo,HttpServletRequest request, HttpServletResponse response) {
		WxRechargeInfoResultVo resultVo = new WxRechargeInfoResultVo();
		Integer userId = requestUtil.getRequestUserId(request);
		if (StringUtils.isNotBlank(userId.toString())) {
			Account account = this.userRechargeService.getAccount(userId);
			if (account != null) {
				resultVo.setAvailableAmount(account.getBankBalance());
			}
			Users user = this.userRechargeService.getUsers(userId);
			// 获取用户的快捷卡信息
			BankCard bankCard = this.userRechargeService.selectBankCardByUserId(userId);
			if (null != bankCard) {
				resultVo.setStatus(CustomConstants.APP_STATUS_SUCCESS);
				resultVo.setStatusDesc(CustomConstants.APP_STATUS_DESC_SUCCESS);
				resultVo.setBank(StringUtils.isBlank(bankCard.getBank()) ? StringUtils.EMPTY : bankCard.getBank());
				// 银行卡号
				resultVo.setCardNo(bankCard.getCardNo());
				resultVo.setIsBindCard("1");
				// 脱敏显示的银行卡号
				resultVo.setCardNoInfo(BankCardUtil.getCardNo(bankCard.getCardNo()));
				resultVo.setMobile(bankCard.getMobile());//成功充值手机号码
				// 银行代码
				resultVo.setCode("");
				Integer bankId = bankCard.getBankId();
				BanksConfig banksConfig = userRechargeService.getBanksConfigByBankId(bankId);
				if (banksConfig != null && StringUtils.isNotEmpty(banksConfig.getBankIcon())) {
					resultVo.setLogo(HOST_URL + banksConfig.getBankIcon());
				} else {
					resultVo.setLogo(HOST_URL + "/data/upfiles/filetemp/image/bank_log.png");
				}

				if(banksConfig !=null && StringUtils.isNotEmpty(banksConfig.getBankName())){
					resultVo.setBank(banksConfig.getBankName());
				}

				// 是否快捷卡
				if(banksConfig != null && banksConfig.getQuickPayment() == 1){
					resultVo.setIsDefault("2");
				}else {
					resultVo.setIsDefault("0");
				}

				//限额获取
				if (banksConfig != null) {
					// 每次限额 单位：万元
					BigDecimal timesLimitAmount = banksConfig.getSingleQuota()
							.divide(new BigDecimal(AMOUNT_UNIT));
					// 每日限额 单位：万元
					BigDecimal dayLimitAmount = banksConfig.getSingleCardQuota()
							.divide(new BigDecimal(AMOUNT_UNIT));
					// 每月限额 单位: 万元
					BigDecimal monthLimitAmount = banksConfig.getMonthCardQuota().divide(new BigDecimal(AMOUNT_UNIT));
                    /*// 是否支持快捷支付1:支持 2:不支持
                    Integer quickPayment = banksConfig.getQuickPayment();*/
					if (monthLimitAmount == null) {
						monthLimitAmount = BigDecimal.ZERO;
					}
					String moneyInfo = MessageFormat.format(CARD_DESC, (BigDecimal.ZERO.compareTo(timesLimitAmount) == 0)?"不限":timesLimitAmount.toString() + "万元",
							(BigDecimal.ZERO.compareTo(dayLimitAmount)==0)?"不限":dayLimitAmount.toString() + "万元",
							(BigDecimal.ZERO.compareTo(monthLimitAmount)==0)?"不限":monthLimitAmount.toString() + "万元");
					resultVo.setMoneyInfo(moneyInfo);
				}
			} else {
				resultVo.setStatus(ResultEnum.USER_ERROR_1003.getStatus());
				resultVo.setStatusDesc(ResultEnum.USER_ERROR_1003.getStatusDesc());
				resultVo.setIsBindCard("0");
			}
			resultVo.setPaymentAuthStatus(user.getPaymentAuthStatus());
			//按钮字段显示前台处理
			//resultVo.setButtonWord("确认充值".concat(CommonUtils.formatAmount("", vo.getMoney())).concat("元"));

			//设置线下充值信息
			this.setOffLineRechageInfo(resultVo, userId);

		} else {
			resultVo.setStatus(CustomConstants.TOKEN_ERROR);
			resultVo.setStatusDesc("用户认证失败");
		}
		// 服务费授权 
		resultVo.setPaymentAuthOn(CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus());
		
		/** 充值描述 */
		resultVo.setRcvAccountName(RCV_ACCOUNT_NAME);
		resultVo.setRcvAccount(RCV_ACCOUNT);
		resultVo.setRcvOpenBankName(RCV_OPEN_BANK_NAME);
		resultVo.setKindlyReminder(KINDLY_REMINDER);
		resultVo.setEnum(ResultEnum.SUCCESS);
		return resultVo;
	}

	/**
	 * 设置线下充值信息，包含 收款方户名，收款方账号，收款方开户行名，友情提示
	 * @param resultVo
	 * @param userId
	 */
	private void setOffLineRechageInfo(WxRechargeInfoResultVo resultVo, Integer userId) {
		resultVo.setRcvOpenBankName(RCV_OPEN_BANK_NAME);
		resultVo.setKindlyReminder(RECHARGE_KINDLY_REMINDER);

		BankOpenAccount bankOpenAccount = this.userRechargeService.getBankOpenAccount(userId);
		if (bankOpenAccount != null) {
			resultVo.setRcvAccount(bankOpenAccount.getAccount());
		}

		UsersInfo usersInfo = this.userRechargeService.getUsersInfoByUserId(userId);
		if (usersInfo != null) {
			resultVo.setRcvAccountName(usersInfo.getTruename());
		}
		// 用户信息
		Users users = this.userRechargeService.getUsers(userId);
		// 用户类型
		Integer userType = users.getUserType();
		// 如果是企业用户
		if (userType == 1) {
			// 根据用户ID查询企业用户账户信息
			CorpOpenAccountRecord record = this.userRechargeService.getCorpOpenAccountRecord(userId);
			resultVo.setRcvAccountName(record.getBusiName());
		}
	}

	/**
	 *
	 * 用户充值
	 *
	 * @author renxingchen
	 * @param request
	 * @param rechargeVo
	 * @return
	 */
	@RequestMapping(value = WxRechargeDefine.RECHARGE_MAPPING)
	public BaseResultBean recharge(HttpServletRequest request, WxRechargeVo rechargeVo) {
		_log.info(WxRechargeController.class.toString(), WxRechargeDefine.RECHARGE_MAPPING);
		BaseResultBean result = new BaseResultBean();

		// 数据校验
		if (!rechargeVo.getMoney().matches("-?[0-9]+.*[0-9]*")) {
			result.setStatus(ResultEnum.FAIL.getStatus());
			result.setStatusDesc("请输入正确的充值金额！");
			return result;
		} else {
			BankCallBean bean = new BankCallBean();
			Integer userId = requestUtil.getRequestUserId(request);
			if (StringUtils.isBlank(userId.toString())) {
				result.setStatus(ResultEnum.ERROR_021.getStatus());
				result.setStatusDesc("用户未登陆，请先登陆！");
				return result;
			}

			String money = request.getParameter("money");
			if (StringUtils.isBlank(money)) {
				result.setStatus(ResultEnum.FAIL.getStatus());
				result.setStatusDesc("请输入充值金额！");
				return result;
			}

			// 获取用户
			Users user = this.userRechargeService.getUsersByUserId(userId);
			if (null == user) {
				result.setStatus(ResultEnum.ERROR_023.getStatus());
				result.setStatusDesc("获取用户信息失败！");
				return result;
			}
			// 获取用户详细信息
			UsersInfo userInfo = this.userRechargeService.getUsersInfoByUserId(userId);
			if (null == userInfo) {
				result.setStatus(ResultEnum.ERROR_023.getStatus());
				result.setStatusDesc("获取用户信息失败！");
				return result;
			}
			//获取用户账户信息
			BankOpenAccount account = this.userRechargeService.getBankOpenAccount(userId);
			// 未开户
			if (null == account) {
				result.setStatus(ResultEnum.USER_ERROR_200.getStatus());
				result.setStatusDesc("用户未开户");
				return result;
			}
			String username = user.getUsername();
			// 用户在银行的客户号
			String userCustId = account.getAccount();
			// 身份证号
			String certId = userInfo.getIdcard();
			// 根据用户Id,检索用户银行卡信息
			BankCard bankCard = this.userRechargeService.selectBankCardByUserId(userId);
			// 用户银行卡号
			String cardNo = null;
			if (bankCard != null) {
				cardNo = bankCard.getCardNo();
			} else {
				result.setStatus(ResultEnum.FAIL.getStatus());
				result.setStatusDesc("用户银行卡信息获取失败！");
				return result;
			}

			// 调用汇付接口(4.3.11. 自动扣款转账（商户用）)
			String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + WxRechargeDefine.REQUEST_MAPPING + WxRechargeDefine.RETURN_MAPPING + CustomConstants.SUFFIX_DO;
			String bgRetUrl = PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) + WxRechargeDefine.REQUEST_MAPPING + WxRechargeDefine.CALLBACK_MAPPING + CustomConstants.SUFFIX_DO;// 支付工程路径
			bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
			bean.setTxCode(BankCallMethodConstant.TXCODE_DIRECT_RECHARGE);// 交易代码
			bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
			bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
			bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
			bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
			bean.setAccountId(userCustId); // 电子账号
			bean.setIdType(BankCallConstant.ID_TYPE_IDCARD); // 证件类型
			bean.setIdNo(certId); // 身份证号
			bean.setName(userInfo.getTruename()); // 姓名
			bean.setMobile(user.getMobile()); // 手机号
			bean.setCardNo(cardNo); // 银行卡号
			bean.setTxAmount(CustomUtil.formatAmount(money)); // 交易金额
			bean.setCurrency(BankCallConstant.CURRENCY_TYPE_RMB); // 交易币种
			bean.setRetUrl(retUrl); // 前台跳转链接
			bean.setNotifyUrl(bgRetUrl); // 后台通知链接
			bean.setUserIP(GetCilentIP.getIpAddr(request));// 客户IP
			bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));// 订单号
			bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
			bean.setLogUserId(String.valueOf(userId));
			bean.setLogUserName(username);
			bean.setLogRemark("快捷充值");
			bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_MOBILE);// 银行请求详细url
			bean.setLogClient(0);// 充值平台
			// 插入充值记录
			int ret = this.userRechargeService.insertRechargeInfo(bean);
			if (ret > 0) {
				// 跳转到银行画面
				try {
					BankCallBean bankCallBean = BankCallUtils.callApiBg(bean);
					if (Validator.isNull(bankCallBean)) {
						result.setStatus(ResultEnum.FAIL.getStatus());
						result.setStatusDesc("接口系统参数异常!");
						return result;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		result.setStatus(ResultEnum.SUCCESS.getStatus());
		result.setStatusDesc("充值成功");
		_log.info(WxRechargeController.class.toString(), WxRechargeDefine.RECHARGE_MAPPING);
		return result;
	}

	/**
	 * 用户充值后处理
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(WxRechargeDefine.RETURN_MAPPING)
	public BaseResultBean rechargeReturn(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
		WxRechargeInfoResultVo result = new WxRechargeInfoResultVo();
		bean.convert();
		String retCode = bean.getRetCode();
		_log.info(THIS_CLASS, WxRechargeDefine.RETURN_MAPPING, "电子账号:"+bean.getAccountId()+",充值同步返回ret_code:" + retCode);
		String ip = CustomUtil.getIpAddr(request);
		Map<String, String> params = new HashMap<String, String>();
		params.put("ip", ip);
		if (BankCallStatusConstant.RESPCODE_SUCCESS.equals(retCode)) {
			_log.info(THIS_CLASS, WxRechargeDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
			bean.convert();
			// 更新充值的相关信息
			JSONObject message = this.userRechargeService.handleRechargeInfo(bean, params);
			// 充值成功
			if (message != null && message.get("error").equals("0")) {
				result.setBalance(CustomConstants.DF_FOR_VIEW.format(new BigDecimal(bean.getTxAmount())));
				result.setEnum(ResultEnum.SUCCESS);
			} else {
				if(message == null) {
					// 充值失败（无返回信息）
					result.setStatus(ResultEnum.FAIL.getStatus());
					result.setStatusDesc("充值失败（无返回值）");
				} else {
					// 充值失败
					result.setStatus(ResultEnum.FAIL.getStatus());
					result.setStatusDesc(message.get("data").toString());
				}
			}
		} else {
			// 充值失败,更新订单状态
			this.userRechargeService.handleRechargeInfo(bean, params);
			String error = this.userRechargeService.getBankRetMsg(retCode);
			result.setStatus(ResultEnum.FAIL.getStatus());
			result.setStatusDesc(error);
		}
		return result;
	}

	/**
	 * 用户充值后处理
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(WxRechargeDefine.CALLBACK_MAPPING)
	public BaseResultBean rechargeCallback(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
		BaseResultBean result = new BaseResultBean();
		String message = "充值失败";
		bean.convert();
		String ip = CustomUtil.getIpAddr(request);
		Map<String, String> params = new HashMap<String, String>();
		params.put("ip", ip);
		LogUtil.warnLog(THIS_CLASS, WxRechargeDefine.RETURN_MAPPING, "电子账号:"+bean.getAccountId()+",异步返回ret_code:" + bean.getRetCode());
		if (BankCallStatusConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
			LogUtil.startLog(THIS_CLASS, WxRechargeDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
			bean.convert();
			LogUtil.debugLog(THIS_CLASS, WxRechargeDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
			// 更新充值的相关信息
			JSONObject msg = this.userRechargeService.handleRechargeInfo(bean, params);
			if (msg != null && msg.get("error").equals("0")) {
				// 充值成功
				message = "充值成功";
				result.setStatus(ResultEnum.SUCCESS.getStatus());
			} else {
				// 充值失败
				message = "充值失败";
				result.setStatus(ResultEnum.FAIL.getStatus());
			}
		} else {
			// 更新充值的相关信息
			this.userRechargeService.handleRechargeInfo(bean, params);
			message = "充值失败";
			result.setStatus(ResultEnum.FAIL.getStatus());
		}
		result.setStatusDesc(message);
		return result;
	}


	/**
	 * 短信充值发送验证码
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = WxRechargeDefine.SENDCODE_ACTION, produces = "application/json; charset=utf-8")
	public BaseResultBean sendCode(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(THIS_CLASS, WxRechargeDefine.SENDCODE_ACTION);
		WxRechargeInfoResultVo result = new WxRechargeInfoResultVo();
		String message = "获取短信验证码失败";
		String status = ResultEnum.FAIL.getStatus();
		// 获取登陆用户userId
//		WebViewUser user = WebUtils.getUser(request);
		Integer userId = requestUtil.getRequestUserId(request);
		if (StringUtils.isBlank(userId.toString())) {
			result.setEnum(ResultEnum.ERROR_021);
			return result;
		}

		// 获取用户的手机号
		String mobile = request.getParameter("mobile");
		if (StringUtils.isBlank(mobile) || !Validator.isMobile(mobile)) {
			result.setStatusDesc("请正确填写手机号");
			result.setStatus(status);
			return result;
		}
		// 获取绑定的银行卡号
		BankCard bankCard = this.userRechargeService.selectBankCardByUserId(userId);
		// 用户银行卡号
		String cardNo = null;
		if (bankCard != null) {
			cardNo = bankCard.getCardNo();
		} else {
			result.setStatusDesc("用户信息错误，未获取到用户绑定的银行卡号！");
			result.setStatus(status);
			return result;
		}

		// 调用短信发送接口
		BankCallBean mobileBean = this.userRechargeService.sendRechargeOnlineSms(userId, cardNo, mobile, BankCallConstant.CHANNEL_PC);

		if (Validator.isNull(mobileBean)) {
			result.setStatusDesc("短信验证码发送失败，请稍后再试！");
			result.setStatus(status);
			return result;
		}

		// 短信发送返回结果码
		String retCode = mobileBean.getRetCode();
		if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)
				&& !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
			message = "短信验证码发送失败，请稍后再试！";
			result.setStatusDesc(message);
			result.setStatus(status);
			return result;
		}
		if (Validator.isNull(mobileBean.getSrvAuthCode()) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
			message = "短信验证码发送失败，请稍后再试！";
			result.setStatusDesc(message);
			result.setStatus(status);
			return result;
		}
		String smsSeq = mobileBean.getSmsSeq();
		message = "短信发送成功！";
		status = ResultEnum.SUCCESS.getStatus();
		result.setStatusDesc(message);
		result.setSmsSeq(smsSeq);
		result.setStatus(status);
		return result;
	}


	/**
	 * 获取提现规则H5页面
	 * @author jijun 2018/04/02
	 * @return
	 */
	@ResponseBody
	@RequestMapping(WxRechargeDefine.RECHARGE_RULE_ACTION)
	public WxRechargeDescResultBean rechargeRule() {
		_log.info("获取充值限额说明....");
		JSONArray jsonArray = CommonSoaUtils.getBanksList().getJSONArray("data");

		WxRechargeDescResultBean result = new WxRechargeDescResultBean();
		if (jsonArray != null) {
			_log.info("jsonArray....{}", jsonArray.toJSONString());
			this.conventBanksConfigToResult(result, jsonArray.toJSONString());
		} else {
			_log.info("jsonArray....{}", "未获取到返回数据");
		}
		return result;
	}


	private void conventBanksConfigToResult(WxRechargeDescResultBean result, String jsonArrayStr) {
		List<BanksConfig> configs = JSON.parseArray(jsonArrayStr, BanksConfig.class);
		if (!CollectionUtils.isEmpty(configs)) {
			List<WxRechargeDescResultBean.RechargeLimitAmountDesc> list = result.getList();
			WxRechargeDescResultBean.RechargeLimitAmountDesc bean = null;
			for (BanksConfig config : configs) {
				bean = new WxRechargeDescResultBean.RechargeLimitAmountDesc();
				bean.setBankName(config.getBankName());
				//
				if(config.getSingleCardQuota().compareTo(BigDecimal.ZERO)==0){
					bean.setDay("不限");
				}else{
					bean.setDay(String.valueOf(config.getSingleCardQuota().divide(new BigDecimal(10000)))+"万");
				}
				if(config.getSingleQuota().compareTo(BigDecimal.ZERO)==0){
					bean.setOnce("不限");
				}else{
					bean.setOnce(String.valueOf(config.getSingleQuota().divide(new BigDecimal(10000)))+"万");
				}
				if(config.getMonthCardQuota().compareTo(BigDecimal.ZERO)==0){
					bean.setMonth("不限");
				}else{
					bean.setMonth(String.valueOf(config.getMonthCardQuota().divide(new BigDecimal(10000)))+"万");
				}

				list.add(bean);
			}
		}
	}
}
