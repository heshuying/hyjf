package com.hyjf.web.bank.web.user.recharge;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.bank.service.user.auth.AuthService;
import com.hyjf.bank.service.user.recharge.RechargeService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountRecharge;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BanksConfig;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.bank.web.user.bankopen.BankOpenDefine;
import com.hyjf.web.bank.web.user.bindcardpage.BindCardPageDefine;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.util.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户充值Controller
 * 
 * @author liuyang
 *
 */
@Controller
@RequestMapping(value = RechargeDefine.REQUEST_MAPPING)
public class RechargeController extends BaseController {
	
	Logger _log = LoggerFactory.getLogger(RechargeController.class);

	/** 类名 */
	private static final String THIS_CLASS = RechargeController.class.getName();

	@Autowired
	private RechargeService userRechargeService;
	@Autowired
    private AuthService authService;
	/**
	 * 
	 * 跳转充值页面
	 * 
	 * @author renxingchen
	 * @return
	 */
	@RequestMapping(value = RechargeDefine.RECHARGEPAGE_MAPPING)
	public ModelAndView rechargePage(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView(RechargeDefine.JSP_RECHARGE_NEW_PAGE);
		WebViewUser user = WebUtils.getUser(request);
		// 未登录
		if (user == null) {
			modelAndView = new ModelAndView("redirect:" + CustomConstants.HOST + "/user/login/init.do");
		}
		Integer userId = user.getUserId();
		//判断是否有用户信息
		UsersInfo userInfo = userRechargeService.getUsersInfoByUserId(userId);
		if(userInfo == null){
			modelAndView.addObject("status", "1");
			modelAndView.addObject("statusDesc","用户信息不存在");
			return modelAndView;
		}
		// 判断用户是否开户
		if (!user.isBankOpenAccount()) {
			modelAndView = new ModelAndView("redirect:" + CustomConstants.HOST + "/bank/web/user/bankopen/init.do");
			LogUtil.endLog(THIS_CLASS, RechargeDefine.RECHARGEPAGE_MAPPING, "[用户未开户]");
			return modelAndView;
		}
		// 银行卡号
		String cardNo = "";
		// 银行
		String bank ="";
		// 银行卡Id
		String cardId = "";
		// 用户是否绑卡: 0:未绑卡,1:已绑卡
		Integer isBundCardFlag = 0;
		// 单笔限额
		String singleQuota = "";
		// 单卡单日限额
		String singleCardQuota = "";
		// 单卡单月限额
		String monthCardQuota = "";
		//单笔、单日，单月 合并
		String concatAllQuota = ",";

		// 根据用户Id查询用户银行卡号
		BankCard bankCard = this.userRechargeService.selectBankCardByUserId(userId);
		if (bankCard != null) {
			cardNo = BankCardUtil.getCardNo(bankCard.getCardNo());
			cardId = String.valueOf(bankCard.getId());
			bank = bankCard.getBank();
			isBundCardFlag = 1;
			
			Integer bankId = bankCard.getBankId();
			BanksConfig banksConfig = userRechargeService.getBanksConfigByBankId(bankId);
			if (banksConfig != null && banksConfig.getQuickPayment() == 1 && banksConfig.getSingleQuota() != null && banksConfig.getSingleCardQuota() != null) {
				if(banksConfig.getSingleQuota().compareTo(BigDecimal.ZERO) > 0){
					singleQuota = CommonUtils.formatBigDecimal(banksConfig.getSingleQuota().divide(new BigDecimal(10000))) + "万";
					concatAllQuota =" 单笔"+singleQuota;
				}else{
					singleQuota = "不限";
				}
				
				if(banksConfig.getSingleCardQuota().compareTo(BigDecimal.ZERO) > 0){
					singleCardQuota = CommonUtils.formatBigDecimal(banksConfig.getSingleCardQuota().divide(new BigDecimal(10000))) + "万";
					concatAllQuota+=", 单日"+singleCardQuota;
				}else{
                    singleCardQuota = "不限";
                }

				if(banksConfig.getMonthCardQuota().compareTo(BigDecimal.ZERO) > 0){
					monthCardQuota = CommonUtils.formatBigDecimal(banksConfig.getMonthCardQuota().divide(new BigDecimal(10000))) + "万";
					concatAllQuota+=", 单月"+monthCardQuota;
				}else{
					monthCardQuota = "不限";
				}
			}
		}
		// 用户是否绑卡
		modelAndView.addObject("isBundCardFlag", isBundCardFlag);
		// 卡号
		modelAndView.addObject("cardNo", cardNo);
		// 卡号
		modelAndView.addObject("bank", bank);
		// 银行卡Id
		modelAndView.addObject("cardId", cardId);
		modelAndView.addObject("singleQuota", singleQuota);
		modelAndView.addObject("singleCardQuota", singleCardQuota);
		modelAndView.addObject("monthCardQuota", monthCardQuota);
		modelAndView.addObject("concatAllQuota", concatAllQuota.substring(1));
		Account account = this.userRechargeService.getAccount(userId);
		// 可用余额
		modelAndView.addObject("userBalance", account.getBankBalance());
		Users users = this.userRechargeService.getUsers(userId);
		String trueName = user.getTruename();
		// 用户类型 0普通用户 1企业用户
		Integer userType = users.getUserType();
		if (userType == 1) {
			// 根据用户ID查询企业用户账户信息
			CorpOpenAccountRecord record = this.userRechargeService.getCorpOpenAccountRecord(userId);
			trueName = record.getBusiName();
		}
		modelAndView.addObject("userType", userType);
		// 姓名
		modelAndView.addObject("trueName", trueName);
		// 合规三期新增  by sss
		HjhUserAuth hjhUserAuth = this.authService.getHjhUserAuthByUserId(userId);
        // 是否开启服务费授权 0未开启  1已开启
		modelAndView.addObject("paymentAuthStatus", hjhUserAuth==null?"":hjhUserAuth.getAutoPaymentStatus());
		// 是否开启服务费授权 0未开启  1已开启
		modelAndView.addObject("paymentAuthOn", CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus());

		// 是否设置交易密码
		modelAndView.addObject("isSetPassword", users.getIsSetPassword());
		if(bankCard != null){
			modelAndView.addObject("mobile", bankCard.getMobile());
			// add by liuyang 神策数据埋点追加 20180717 start
			modelAndView.addObject("bankName", StringUtils.isBlank(bankCard.getBank()) ? "" : bankCard.getBank());
			// add by liuyang 神策数据埋点追加 20180717 end
		}else{
			// 如果未绑卡
			// add by liuyang 神策数据埋点追加 20180717 start
			modelAndView.addObject("bankName", "");
			// add by liuyang 神策数据埋点追加 20180717 end
		}
		// 江西银行客户号
		modelAndView.addObject("accountId", user.getBankAccount());
		//充值提示语
		modelAndView.addObject("rcvAccountName", "惠众商务顾问（北京）有限公司<br/>");
		modelAndView.addObject("rcvAccount", "7919 1314 9300 306<br/>");
		modelAndView.addObject("rcvOpenBankName", "江西银行南昌铁路支行<br/>");
		modelAndView.addObject("kindlyReminder", "①用户必须使用在平台唯一绑定银行卡进行充值；<br/>②银行转账时，请选择（城市商业银行）江西银行或者南昌银行；<br/>③线下充值的到账时间一般为1-3天（具体到账时间以银行的实际到账时间为准）；");
		modelAndView.addObject("bindCardUrl", RechargeDefine.HOST+BindCardPageDefine.REQUEST_MAPPING+BindCardPageDefine.REQUEST_BINDCARDPAGE+".do");
		// 江西银行绑卡接口修改 update by wj 2018-5-17 start
		modelAndView.addObject("bindType",this.userRechargeService.getBandCardBindUrlType("BIND_CARD"));
		// 江西银行绑卡接口修改 update by wj 2018-5-17 end
		//合规2.0协议修改 用户信息表_huiyingdai_users_info_用户角色 add by dangzw start
		modelAndView.addObject("roleId", userInfo.getRoleId());
		//合规2.0协议修改 用户信息表_huiyingdai_users_info_用户角色 add by dangzw end

		return modelAndView;
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
	@RequestMapping(value = RechargeDefine.RECHARGE_MAPPING)
	public ModelAndView recharge(HttpServletRequest request, RechargeVo rechargeVo) {
		LogUtil.startLog(RechargeController.class.toString(), RechargeDefine.RECHARGE_MAPPING);
		ModelAndView modelAndView = new ModelAndView();
		Integer userId;
		String username;

		_log.info("充值请求参数: {}", JSONObject.toJSONString(rechargeVo));
		// 数据校验
		if (!rechargeVo.getMoney().matches("-?[0-9]+.*[0-9]*") || Validator.isNull(rechargeVo.getRechargeType())) {
			modelAndView.setViewName(RechargeDefine.RECHARGE_ERROR);
			modelAndView.addObject("message", "系统参数异常!");
		} else {
			BankCallBean bean = new BankCallBean();
			// 获取用户
			WebViewUser user = WebUtils.getUser(request);
			// 如果用户没有登录，重定向到登录页面
			if (null == user) {
				modelAndView.setViewName("redirect:" + CustomConstants.HOST + "/user/login/init.do");
				return modelAndView;
			}
			userId = user.getUserId();
			username = user.getUsername();
			// 未开户
			if (!user.isBankOpenAccount()) {
				modelAndView = new ModelAndView("redirect:" + CustomConstants.HOST + "/bank/web/user/bankopen/init.do");
				LogUtil.endLog(THIS_CLASS, RechargeDefine.RECHARGE_MAPPING, "[用户未开户]");
				return modelAndView;
			}
			// 合规三期新增  by sss
	        boolean isPaymentAuth = this.authService.checkPaymentAuthStatus(userId);
            if (!isPaymentAuth) {
                modelAndView.setViewName(RechargeDefine.RECHARGE_ERROR);
                modelAndView.addObject("paymentAuthStatus", "0");
                modelAndView.addObject("message", "请先进行服务费授权!");
                return modelAndView;
            }
			// 用户在银行的客户号
			String userCustId = user.getBankAccount();
			// 身份证号
			String certId = user.getIdcard();
			// 根据用户Id,检索用户银行卡信息
			BankCard bankCard = this.userRechargeService.selectBankCardByUserId(userId);
			// 用户银行卡号
			String cardNo = null;
			if (bankCard != null) {
				cardNo = bankCard.getCardNo();
			} else {
				modelAndView.setViewName(RechargeDefine.RECHARGE_ERROR);
				modelAndView.addObject("message", "系统参数异常!");
				return modelAndView;
			}
			
			// 调用汇付接口(4.3.11. 自动扣款转账（商户用）)
			String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + RechargeDefine.REQUEST_MAPPING + RechargeDefine.RETURN_MAPPING + CustomConstants.SUFFIX_DO;
			String bgRetUrl = PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) + RechargeDefine.REQUEST_MAPPING + RechargeDefine.CALLBACK_MAPPING + CustomConstants.SUFFIX_DO;// 支付工程路径
			bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
			bean.setTxCode(BankCallMethodConstant.TXCODE_DIRECT_RECHARGE);// 交易代码
			bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
			bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
			bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
			bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
			bean.setAccountId(userCustId); // 电子账号
			bean.setIdType(BankCallConstant.ID_TYPE_IDCARD); // 证件类型
			bean.setIdNo(certId); // 身份证号
			bean.setName(user.getTruename()); // 姓名
			bean.setMobile(user.getMobile()); // 手机号
			bean.setCardNo(cardNo); // 银行卡号
			bean.setTxAmount(CustomUtil.formatAmount(rechargeVo.getMoney())); // 交易金额
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
					modelAndView.addObject(RechargeDefine.STATUS, RechargeDefine.STATUS_TRUE);
					modelAndView = BankCallUtils.callApi(bean);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		LogUtil.endLog(RechargeController.class.toString(), RechargeDefine.RECHARGE_MAPPING);
		return modelAndView;
	}

	/**
	 * 用户充值后处理
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(RechargeDefine.RETURN_MAPPING)
	public ModelAndView rechargeReturn(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
		ModelAndView modelAndView = new ModelAndView();
		bean.convert();
		String retCode = bean.getRetCode();
		LogUtil.warnLog(THIS_CLASS, RechargeDefine.RETURN_MAPPING, "电子账号:"+bean.getAccountId()+",充值同步返回ret_code:" + retCode);
		String ip = CustomUtil.getIpAddr(request);
		Map<String, String> params = new HashMap<String, String>();
		params.put("ip", ip);
		if (BankCallStatusConstant.RESPCODE_SUCCESS.equals(retCode)) {
			LogUtil.startLog(THIS_CLASS, RechargeDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
			bean.convert();
			LogUtil.debugLog(THIS_CLASS, RechargeDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
			// 更新充值的相关信息
			JSONObject message = this.userRechargeService.handleRechargeInfo(bean, params);
			// 充值成功
			if (message != null && message.get("error").equals("0")) {
				modelAndView.setViewName(RechargeDefine.RECHARGE_SUCCESS);
				modelAndView.addObject("money", CustomConstants.DF_FOR_VIEW.format(new BigDecimal(bean.getTxAmount())));
				modelAndView.addObject("balance", CustomConstants.DF_FOR_VIEW.format(new BigDecimal(bean.getTxAmount())));
			} else {
				// 充值失败
				modelAndView.setViewName(RechargeDefine.RECHARGE_ERROR);
				modelAndView.addObject("message", message.get("data"));
			}
		} else {
			// 充值失败,更新订单状态
			this.userRechargeService.handleRechargeInfo(bean, params);
			String error = this.userRechargeService.getBankRetMsg(retCode);
			modelAndView.setViewName(RechargeDefine.RECHARGE_ERROR);
			modelAndView.addObject("message", error);
		}
		return modelAndView;
	}

	/**
	 * 用户充值后处理
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(RechargeDefine.CALLBACK_MAPPING)
	public String rechargeCallback(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
		BankCallResult result = new BankCallResult();
		String message = "充值失败";
		bean.convert();
		String ip = CustomUtil.getIpAddr(request);
		Map<String, String> params = new HashMap<String, String>();
		params.put("ip", ip);
		LogUtil.warnLog(THIS_CLASS, RechargeDefine.RETURN_MAPPING, "电子账号:"+bean.getAccountId()+",异步返回ret_code:" + bean.getRetCode());
		if (BankCallStatusConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
			LogUtil.startLog(THIS_CLASS, RechargeDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
			bean.convert();
			LogUtil.debugLog(THIS_CLASS, RechargeDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
			// 更新充值的相关信息
			JSONObject msg = this.userRechargeService.handleRechargeInfo(bean, params);
			if (msg != null && msg.get("error").equals("0")) {
				// 充值成功
				message = "充值成功";
				try {
					// 神策数据统计埋点 add by liuyang 20180725 start
					SensorsDataBean sensorsDataBean = new SensorsDataBean();
					sensorsDataBean.setOrderId(bean.getLogOrderId());
					sensorsDataBean.setEventCode("recharge_result");
					sensorsDataBean.setUserId(Integer.parseInt(bean.getLogUserId()));
					this.userRechargeService.sendSensorsDataMQ(sensorsDataBean);
					// 神策数据统计埋点 add by liuyang 20180725 end
				}catch (Exception e){
					e.printStackTrace();
				}
			} else {
				// 充值失败
				message = "充值失败";
			}
		} else {
			// 更新充值的相关信息
			this.userRechargeService.handleRechargeInfo(bean, params);
			message = "充值失败";
		}
		result.setMessage(message);
		result.setStatus(true);
		return JSONObject.toJSONString(result, true);
	}


	/**
	 * 短信充值发送验证码
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = RechargeDefine.SENDCODE_ACTION, produces = "application/json; charset=utf-8")
	public RechargeInfoResult sendCode(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(THIS_CLASS, RechargeDefine.SENDCODE_ACTION);
		RechargeInfoResult result = new RechargeInfoResult();
		String message = "短信充值失败";
		boolean status = BankOpenDefine.STATUS_FALSE;
		// 获取登陆用户userId
		WebViewUser user = WebUtils.getUser(request);
		int userId = user.getUserId();
		if (Validator.isNull(userId)) {
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		
		// 获取用户的手机号
		String mobile = request.getParameter("mobile");
		if (Validator.isNull(mobile) || !Validator.isMobile(mobile)) {
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
//		else{
//			Users users = this.userRechargeService.getUsers(userId);
//			if (Validator.isNull(users)) {
//				result.setMessage(message);
//				result.setStatus(status);
//				return result;
//			}
//			mobile = users.getMobile();
//			if (StringUtils.isBlank(mobile)) {
//				message = "用户信息错误，未获取到用户的手机号码！";
//				result.setMessage(message);
//				result.setStatus(status);
//				return result;
//			}
//		}
		
		// 获取绑定的银行卡号
		BankCard bankCard = this.userRechargeService.selectBankCardByUserId(userId);
		// 用户银行卡号
		String cardNo = null;
		if (bankCard != null) {
			cardNo = bankCard.getCardNo();
		} else {
			message = "用户信息错误，未获取到用户绑定的银行卡号！";
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		
		// 调用短信发送接口
		BankCallBean mobileBean = this.userRechargeService.sendRechargeOnlineSms(userId, cardNo, mobile, BankCallConstant.CHANNEL_PC);
		
		if (Validator.isNull(mobileBean)) {
			message = "短信验证码发送失败，请稍后再试！";
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		
		// 短信发送返回结果码
		String retCode = mobileBean.getRetCode();
		if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)
				&& !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
			message = "短信验证码发送失败，请稍后再试！";
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		if (Validator.isNull(mobileBean.getSrvAuthCode()) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
			message = "短信验证码发送失败，请稍后再试！";
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		String smsSeq = mobileBean.getSmsSeq();
		message = "短信发送成功！";
		status = BankOpenDefine.STATUS_TRUE;
		result.setMessage(message);
		result.setSmsSeq(smsSeq);
		result.setStatus(status);
		return result;
	}

	/**
	 * 短信充值
	 *
	 * @param request
	 * @param form
	 * @return
	 *//*
	@RequestMapping(method = RequestMethod.POST,value =RechargeDefine.RECHARGE_ONLINE_ACTION)
	public ModelAndView rechargeOnline(HttpServletRequest request, HttpServletResponse response, RechargeVo rechargeVo) {
		ModelAndView modelAndView = new ModelAndView();
		LogUtil.startLog(THIS_CLASS, RechargeDefine.RECHARGE_ONLINE_ACTION);
		
		// 数据校验
		String smsCode = request.getParameter("smsCode");
		// 获取用户的手机号
		String mobile = request.getParameter("mobile");
		if (!rechargeVo.getMoney().matches("-?[0-9]+.*[0-9]*") || Validator.isNull(smsCode) || Validator.isNull(mobile)
				|| !Validator.isMobile(mobile)) {
			modelAndView.setViewName(RechargeDefine.RECHARGE_ERROR);
			modelAndView.addObject("message", "系统参数异常!");
			return modelAndView;
		}
		
		// 获取用户
		WebViewUser user = WebUtils.getUser(request);
		// 如果用户没有登录，重定向到登录页面
		if (null == user) {
			modelAndView.setViewName("redirect:" + CustomConstants.HOST + "/user/login/init.do");
			return modelAndView;
		}
		// 未开户
		if (!user.isBankOpenAccount()) {
			modelAndView = new ModelAndView("redirect:" + CustomConstants.HOST + "/bank/web/user/bankopen/init.do");
			LogUtil.endLog(THIS_CLASS, RechargeDefine.RECHARGE_ONLINE_ACTION, "[用户未开户]");
			return modelAndView;
		}
		

		Integer userId = user.getUserId();
		String username = user.getUsername();
		// 短信序列号
		String smsSeq = this.userRechargeService.selectBankSmsSeq(userId,BankCallMethodConstant.TXCODE_DIRECT_RECHARGE_ONLINE);
		if (StringUtils.isBlank(smsSeq)) {
			LogUtil.debugLog(THIS_CLASS, RechargeDefine.RECHARGE_ONLINE_ACTION, "userId: " + userId+"短信序列号为空");
			modelAndView.setViewName(RechargeDefine.RECHARGE_ERROR);
			modelAndView.addObject("message", "系统参数异常!");
			return modelAndView;
		}
		
		// 用户在银行的客户号
		String userCustId = user.getBankAccount();
		// 身份证号
		String certId = user.getIdcard();
		// 根据用户Id,检索用户银行卡信息
		BankCard bankCard = this.userRechargeService.selectBankCardByUserId(userId);
		// 用户银行卡号
		String cardNo = null;
		if (bankCard != null) {
			cardNo = bankCard.getCardNo();
		} else {
			LogUtil.debugLog(THIS_CLASS, RechargeDefine.RECHARGE_ONLINE_ACTION, "userId: " + userId+"用户银行卡号为空");
			modelAndView.setViewName(RechargeDefine.RECHARGE_ERROR);
			modelAndView.addObject("message", "系统参数异常!");
			return modelAndView;
		}
		
		// 调用   2.3.4联机绑定卡到电子账户充值
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_DIRECT_RECHARGE_ONLINE);// 交易代码
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
		bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
		
		bean.setAccountId(userCustId); // 电子账号
		bean.setIdType(BankCallConstant.ID_TYPE_IDCARD); // 证件类型
		bean.setIdNo(certId); // 身份证号
		bean.setName(user.getTruename()); // 姓名
		bean.setMobile(mobile); // 手机号
		bean.setCardNo(cardNo); // 银行卡号
		bean.setTxAmount(CustomUtil.formatAmount(rechargeVo.getMoney())); // 交易金额
		bean.setCurrency(BankCallConstant.CURRENCY_TYPE_RMB); // 交易币种
		
//		bean.setCallBackAdrress(""); // 充值结果通知地址,p2p使用
		bean.setSmsCode(smsCode); // 充值时短信验证,p2p使用
		bean.setSmsSeq(smsSeq);
		//充值时短信验证,p2p使用
		bean.setUserIP(GetCilentIP.getIpAddr(request));// 客户IP
		String logOrderId = GetOrderIdUtils.getOrderId2(userId);
		bean.setLogOrderId(logOrderId);// 订单号
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogUserName(username);
		bean.setLogRemark("短信充值");
		bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_MOBILE);// 银行请求详细url
		bean.setLogClient(0);// 充值平台
		
		// 插入充值记录
		int ret = this.userRechargeService.insertRechargeInfo(bean);
		if (ret > 0) {
			;
		}else{
			LogUtil.debugLog(THIS_CLASS, RechargeDefine.RECHARGE_ONLINE_ACTION, "userId: " + userId+"  插入充值记录失败, 订单号:"+logOrderId);
			modelAndView.setViewName(RechargeDefine.RECHARGE_ERROR);
			modelAndView.addObject("message", "系统参数异常!");
			return modelAndView;
		}
		
		try {

			String ip = CustomUtil.getIpAddr(request);
			Map<String, String> params = new HashMap<String, String>();
			params.put("ip", ip);
			
			LogUtil.debugLog(THIS_CLASS, RechargeDefine.RETURN_MAPPING, "参数: " +userId + bean == null ? "无" : bean.getAllParams() + "]");
			BankCallBean bankCallBean = BankCallUtils.callApiBg(bean);
			if (Validator.isNull(bankCallBean)) {
				LogUtil.debugLog(THIS_CLASS, RechargeDefine.RECHARGE_ONLINE_ACTION, "userId: " + userId+"  调用接口返回为空, 订单号:"+logOrderId);
				modelAndView.setViewName(RechargeDefine.RECHARGE_ERROR);
				modelAndView.addObject("message", "系统参数异常!");
				return modelAndView;
			}
			String retCode = StringUtils.isNotBlank(bankCallBean.getRetCode()) ? bankCallBean.getRetCode(): "";
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
				LogUtil.debugLog(THIS_CLASS, RechargeDefine.RECHARGE_ONLINE_ACTION, "userId: " + userId+"  调用接口返回不成功, 订单号:"+logOrderId);
				this.userRechargeService.handleRechargeInfo(bankCallBean, params);
				String error = this.userRechargeService.getBankRetMsg(retCode);
				modelAndView.setViewName(RechargeDefine.RECHARGE_ERROR);
				modelAndView.addObject("message", error);
				return modelAndView;
			}
			
			// 更新充值的相关信息
			JSONObject msg = this.userRechargeService.handleRechargeInfo(bankCallBean, params);
			// 充值成功
			if (msg != null && msg.get("error").equals("0")) {
				modelAndView.setViewName(RechargeDefine.RECHARGE_SUCCESS);
				modelAndView.addObject("money", CustomConstants.DF_FOR_VIEW.format(new BigDecimal(bankCallBean.getTxAmount())));
				modelAndView.addObject("balance", CustomConstants.DF_FOR_VIEW.format(new BigDecimal(bankCallBean.getTxAmount())));
			} else {
				// 充值失败
				modelAndView.setViewName(RechargeDefine.RECHARGE_ERROR);
				modelAndView.addObject("message", msg.get("data"));
			}
			return modelAndView;
		} catch (Exception e) {
			modelAndView.setViewName(RechargeDefine.RECHARGE_ERROR);
			modelAndView.addObject("message", "系统参数异常!");
			return modelAndView;
		}
		
		
	}*/
	

	/**
	 * 短信充值JSON版
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST,value =RechargeDefine.RECHARGE_ONLINE_ACTION)
	public RechargeInfoResult rechargeOnline(HttpServletRequest request, HttpServletResponse response, RechargeVo rechargeVo) {

		RechargeInfoResult result = new RechargeInfoResult();
		
		// 数据校验
		String smsCode = request.getParameter("smsCode");
		// 获取用户的手机号
		String mobile = request.getParameter("mobile");
		if (!rechargeVo.getMoney().matches("-?[0-9]+.*[0-9]*") || Validator.isNull(smsCode) || Validator.isNull(mobile)
				|| !Validator.isMobile(mobile)) {
			result.setMessage("系统参数异常!");
			return result;
		}
		
		// 获取用户
		WebViewUser user = WebUtils.getUser(request);
		// 如果用户没有登录，重定向到登录页面加到拦截器处理//TODO:

		Integer userId = user.getUserId();
		String username = user.getUsername();
		// 短信序列号
		String smsSeq = this.userRechargeService.selectBankSmsSeq(userId,BankCallMethodConstant.TXCODE_DIRECT_RECHARGE_ONLINE);
		if (StringUtils.isBlank(smsSeq)) {
			_log.info("userId: " + userId+"短信序列号为空");
			result.setMessage("系统参数异常!");
			return result;
		}
		
		// 用户在银行的客户号
		String userCustId = user.getBankAccount();
		// 身份证号
		String certId = user.getIdcard();
		// 根据用户Id,检索用户银行卡信息
		BankCard bankCard = this.userRechargeService.selectBankCardByUserId(userId);
		// 用户银行卡号
		String cardNo = null;
		if (bankCard != null) {
			cardNo = bankCard.getCardNo();
		} else {
			_log.info("userId: " + userId+"用户银行卡号为空");
			result.setMessage("系统参数异常!");
			return result;
		}
		
		// 调用   2.3.4联机绑定卡到电子账户充值
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_DIRECT_RECHARGE_ONLINE);// 交易代码
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
		bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
		
		bean.setAccountId(userCustId); // 电子账号
		bean.setIdType(BankCallConstant.ID_TYPE_IDCARD); // 证件类型
		bean.setIdNo(certId); // 身份证号
		bean.setName(user.getTruename()); // 姓名
		bean.setMobile(mobile); // 手机号
		bean.setCardNo(cardNo); // 银行卡号
		bean.setTxAmount(CustomUtil.formatAmount(rechargeVo.getMoney())); // 交易金额
		bean.setCurrency(BankCallConstant.CURRENCY_TYPE_RMB); // 交易币种
		
//		bean.setCallBackAdrress(""); // 充值结果通知地址,p2p使用
		bean.setSmsCode(smsCode); // 充值时短信验证,p2p使用
		bean.setSmsSeq(smsSeq);
		//充值时短信验证,p2p使用
		bean.setUserIP(GetCilentIP.getIpAddr(request));// 客户IP
		String logOrderId = GetOrderIdUtils.getOrderId2(userId);
		bean.setLogOrderId(logOrderId);// 订单号
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogUserName(username);
		bean.setLogRemark("短信充值");
		bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_MOBILE);// 银行请求详细url
		bean.setLogClient(0);// 充值平台
		
		// 插入充值记录
		int ret = this.userRechargeService.insertRechargeOnlineInfo(bean);
		if (ret > 0) {
			;
		}else{
			_log.info("userId: " + userId+"  插入充值记录失败, 订单号:"+logOrderId);
			result.setMessage("系统参数异常!");
			return result;
		}
		
		try {

			String ip = CustomUtil.getIpAddr(request);
			Map<String, String> params = new HashMap<String, String>();
			params.put("ip", ip);
			params.put("mobile", mobile);
			
			_log.info("参数: " +userId + bean == null ? "无" : bean.getAllParams() + "]");
			BankCallBean bankCallBean = BankCallUtils.callApiBg(bean);
			if (Validator.isNull(bankCallBean)) {
				_log.info("userId: " + userId+"  调用接口返回为空, 订单号:"+logOrderId);
				result.setMessage("系统参数异常!");
				return result;
			}
			String retCode = StringUtils.isNotBlank(bankCallBean.getRetCode()) ? bankCallBean.getRetCode(): "";
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
				LogUtil.debugLog(THIS_CLASS, RechargeDefine.RECHARGE_ONLINE_ACTION, "userId: " + userId+"  调用接口返回不成功, 订单号:"+logOrderId);
				this.userRechargeService.handleRechargeOnlineInfo(bankCallBean, params);
				String error = this.userRechargeService.getBankRetMsg(retCode);
				result.setMessage(error);
				return result;
			}
			
			// 更新充值的相关信息
			JSONObject msg = this.userRechargeService.handleRechargeOnlineInfo(bankCallBean, params);
			// 充值成功
			if (msg != null && msg.get("error").equals("0")) {
				
				// 设置返回url
				StringBuffer sbUrl = new StringBuffer();
				sbUrl.append(PropUtils.getSystem(CustomConstants.HYJF_WEB_URL));
				sbUrl.append(RechargeDefine.REQUEST_MAPPING);
				sbUrl.append(RechargeDefine.RECHARGE_RESULT_ACTION);
				sbUrl.append(".do?").append("cardNo").append("=").append(cardNo);
				sbUrl.append("&").append("money").append("=").append(rechargeVo.getMoney());
				sbUrl.append("&").append("nid").append("=").append(logOrderId);
				result.setRechargeUrl(sbUrl.toString());
				
				
				result.setMessage("充值成功！");
				result.setStatus(RechargeDefine.STATUS_TRUE);
			} else if (msg != null) {
				// 充值失败
				result.setMessage(msg.get("data").toString());
			}
			return result;
		} catch (Exception e) {
			_log.error(e.getMessage());
			result.setMessage("系统参数异常!");
			return result;
		}
		
		
	}

	/**
	 * 充值成功跳转
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RechargeDefine.RECHARGE_RESULT_ACTION)
	public ModelAndView rechargeInfo(HttpServletRequest request, HttpServletResponse response) {
		
		ModelAndView modelAndView = new ModelAndView(RechargeDefine.RECHARGE_ERROR);
		String message = "";
		// 获取用户
		WebViewUser user = WebUtils.getUser(request);
		String cardNo = request.getParameter("cardNo");
		String money = request.getParameter("money");
		String nid = request.getParameter("nid");
		
		// 检查参数
		if(StringUtils.isBlank(nid)){
			modelAndView.addObject("message", "系统参数异常!");
			return modelAndView;
		}

		AccountRecharge accountRecharge = this.userRechargeService.selectRechargeInfo(user.getUserId(), nid);
		if(accountRecharge == null){
			message = "充值失败！";
			modelAndView.addObject("message", message);
		}else if (accountRecharge.getStatus() == 2) {
			modelAndView = new ModelAndView(RechargeDefine.RECHARGE_SUCCESS);
			modelAndView.addObject("money",  CustomConstants.DF_FOR_VIEW.format(accountRecharge.getMoney()));
			modelAndView.addObject("balance", CustomConstants.DF_FOR_VIEW.format(accountRecharge.getBalance()));
		}else {
			message = "充值失败！";
			modelAndView.addObject("message", message);
		}
		
		return modelAndView;
	}
	
	/**
	 * 
	 * 快捷充值限额
	 * @author hsy
	 * @param request
	 * @param response
	 * @return
	 */
    @RequestMapping(RechargeDefine.RECHARGE_QUOTALIMIT_ACTION)
    public ModelAndView rechargeQuotaLimit(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView=new ModelAndView(RechargeDefine.RECHARGE_QUOTALIMIT_PATH);
		JSONArray data = CommonSoaUtils.getBanksList().getJSONArray("data");
		String str = data.toJSONString();
		List<BanksConfig> list = JSONObject.parseArray(str, BanksConfig.class);
		for (BanksConfig banksConfig : list) {
			BigDecimal monthCardQuota = banksConfig.getMonthCardQuota();
			BigDecimal singleQuota = banksConfig.getSingleQuota();
			BigDecimal singleCardQuota = banksConfig.getSingleCardQuota();
			banksConfig.setSingleQuota(new BigDecimal(CommonUtils.formatBigDecimal(singleQuota.divide(new BigDecimal(10000)))));
			banksConfig.setSingleCardQuota(new BigDecimal(CommonUtils.formatBigDecimal(singleCardQuota.divide(new BigDecimal(10000)))));
			banksConfig.setMonthCardQuota(new BigDecimal(CommonUtils.formatBigDecimal(monthCardQuota.divide(new BigDecimal(10000)))));
		}
		modelAndView.addObject("date", list);
        
//        System.out.println(CommonSoaUtils.getBanksList().getJSONArray("data")+"输出");
        return modelAndView;
    }
	
}
