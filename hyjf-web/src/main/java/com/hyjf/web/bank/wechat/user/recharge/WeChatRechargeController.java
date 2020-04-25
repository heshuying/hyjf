package com.hyjf.web.bank.wechat.user.recharge;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.recharge.RechargeService;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AccountRecharge;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.bank.web.user.bankopen.BankOpenDefine;
import com.hyjf.web.bank.web.user.recharge.RechargeDefine;
import com.hyjf.web.bank.web.user.recharge.RechargeInfoResult;

/**
 * 微信端用户充值Controller
 * 
 * @author liuyang
 *
 */
@Controller
@RequestMapping(value = WeChatRechargeDefine.REQUEST_MAPPING)
public class WeChatRechargeController extends BaseController {
	
	Logger _log = LoggerFactory.getLogger(WeChatRechargeController.class);

	@Autowired
	private RechargeService rechargeSerivce;

	/**
	 * 根据用户Id,检索用户银行卡信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = WeChatRechargeDefine.SEARCHCARDINFO_MAPPING, produces = "application/json; charset=utf-8")
	public JSONObject searchCardInfo(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(WeChatRechargeDefine.THIS_CLASS, WeChatRechargeDefine.SEARCHCARDINFO_MAPPING);
		JSONObject info = new JSONObject();
		JSONObject data = new JSONObject();
		// 用户Id
		String userIdStr = request.getParameter("userId");
		if (StringUtils.isEmpty(userIdStr)) {
			info.put("error", "1");
			info.put("errorDesc", "您未登陆，请先登录");
			return info;
		}
		Integer userId = Integer.parseInt(userIdStr);
		if (userId == null || userId == 0) {
			info.put("error", "1");
			info.put("errorDesc", "您未登陆，请先登录");
			return info;
		}
		// 用户信息
		Users users = this.rechargeSerivce.getUsers(userId);
		if (users == null) {
			info.put("error", 1);
			info.put("errorDesc", "用户不存在");
			return info;
		}
		// 根据用户Id查询用户信息
		UsersInfo userInfo = this.rechargeSerivce.getUsersInfoByUserId(userId);
		if (userInfo == null) {
			info.put("error", "1");
			info.put("errorDesc", "获取用户信息失败");
			return info;
		}

		// 判断用户是否开户
		BankOpenAccount bankOpenAccount = this.rechargeSerivce.getBankOpenAccount(userId);
		if (bankOpenAccount == null || StringUtils.isEmpty(bankOpenAccount.getAccount())) {
			info.put("error", "1");
			info.put("errorDesc", "用户未开户");
			return info;
		}
		String trueName = userInfo.getTruename();
		// 用户类型
		Integer userType = users.getUserType();
		// 企业用户充值
		if(userType == 1) {
			// 根据用户ID查询企业用户账户信息
			CorpOpenAccountRecord record = this.rechargeSerivce.getCorpOpenAccountRecord(userId);
			trueName = record.getBusiName();
		}
		data.put("userName", trueName);
		data.put("accountId", bankOpenAccount.getAccount());
		// 根据用户Id查询用户的银行卡信息
		BankCard bankcard = this.rechargeSerivce.selectBankCardByUserId(userId);
		if (bankcard == null || StringUtils.isEmpty(bankcard.getCardNo())) {
			info.put("error", "2");
			info.put("errorDesc", "用户未绑卡!");
			info.put("data", data);
			return info;
		}
		data.put("cardNo", bankcard.getCardNo());
		// 用户类型
		data.put("userType", userType);
		info.put("error", "0");
		info.put("errorDesc", "获取银行卡号成功!");
		info.put("data", data);
		LogUtil.endLog(WeChatRechargeDefine.THIS_CLASS, WeChatRechargeDefine.SEARCHCARDINFO_MAPPING);
		return info;
	}

	/**
	 * 用户充值
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = WeChatRechargeDefine.RECHARGE_MAPPING)
	public ModelAndView recharge(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(WeChatRechargeDefine.THIS_CLASS, WeChatRechargeDefine.RECHARGE_MAPPING);
		ModelAndView modelAndView = new ModelAndView();

		String error = "1";
		String errorDesc = "充值异常";
		// 充值金额
		String account = request.getParameter("account");
		// 用户Id
		String userIdStr = request.getParameter("userId");
		// 结果页URL
		String callback = request.getParameter("callback");
		// pageFrom
		String pageFrom = request.getParameter("pageFrom ");
		Integer userId = Integer.parseInt(userIdStr);
		if (!account.matches("-?[0-9]+.*[0-9]*")) {
			error = "1";
			errorDesc = "参数校验失败";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		if (userId == null || userId == 0) {
			error = "1";
			errorDesc = "您未登陆，请先登录";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		if (StringUtils.isEmpty(callback)) {
			error = "1";
			errorDesc = "结果页URL获取失败";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		// 获取用户在银行的客户号
		BankOpenAccount bankOpenAccountTender = this.rechargeSerivce.getBankOpenAccount(userId);
		if (bankOpenAccountTender == null || Validator.isNull(bankOpenAccountTender.getAccount())) {
			error = "1";
			errorDesc = "用户未开户";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		// 用户信息
		Users user = this.rechargeSerivce.getUsers(userId);
		if (user == null) {
			error = "1";
			errorDesc = "用户不存在";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		// 查询用户信息
		UsersInfo usersInfo = this.rechargeSerivce.getUsersInfoByUserId(userId);
		// 用户银行卡号
		String cardNo = null;
		// 用户在银行的客户号
		String userCustId = bankOpenAccountTender.getAccount();
		// 身份证号
		String certId = this.rechargeSerivce.getUserIdcard(userId);
		// 根据用户Id,检索用户银行卡信息
		BankCard bankCard = this.rechargeSerivce.selectBankCardByUserId(userId);
		if (bankCard == null || StringUtils.isEmpty(bankCard.getCardNo())) {
			modelAndView.addObject(WeChatRechargeDefine.STATUS, WeChatRechargeDefine.STATUS_FALSE);
			modelAndView.addObject(WeChatRechargeDefine.MESSAGE, "用户未绑卡!");
			error = "1";
			errorDesc = "用户未绑卡!";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		cardNo = bankCard.getCardNo();
		// 调用出借接口
		// 调用汇付接口(4.3.11. 自动扣款转账（商户用）)
		BankCallBean bean = new BankCallBean();
		String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + WeChatRechargeDefine.REQUEST_MAPPING + WeChatRechargeDefine.RETURN_MAPPING + CustomConstants.SUFFIX_DO + "?callback="
				+ callback;
		String bgRetUrl = PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) + WeChatRechargeDefine.REQUEST_MAPPING + WeChatRechargeDefine.CALLBACK_MAPPING + CustomConstants.SUFFIX_DO
				+ "?callback=" + callback;// 支付工程路径
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_DIRECT_RECHARGE);// 交易代码
		bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_WEI); // 交易渠道
		bean.setAccountId(userCustId); // 电子账号
		bean.setIdType(BankCallConstant.ID_TYPE_IDCARD); // 证件类型
		bean.setIdNo(certId); // 身份证号
		bean.setName(usersInfo.getTruename()); // 姓名
		bean.setMobile(user.getMobile()); // 手机号
		bean.setCardNo(cardNo); // 银行卡号
		bean.setTxAmount(CustomUtil.formatAmount(account)); // 交易金额
		bean.setCurrency(BankCallConstant.CURRENCY_TYPE_RMB); // 交易币种
		bean.setRetUrl(retUrl); // 前台跳转链接
		bean.setNotifyUrl(bgRetUrl); // 后台通知链接
		bean.setUserIP(GetCilentIP.getIpAddr(request));// 客户IP
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));// 订单号
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogUserName(user.getUsername());
		bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_MOBILE);// 银行请求详细url
		bean.setLogClient(1);// 提现平台
		LogAcqResBean logAcq = new LogAcqResBean();
		logAcq.setPageFrom(pageFrom);
		bean.setLogAcqResBean(logAcq);
		// 插入充值记录
		int ret = this.rechargeSerivce.insertRechargeInfo(bean);
		if (ret > 0) {
			try {
				// 跳转到银行画面
				modelAndView.addObject(WeChatRechargeDefine.STATUS, WeChatRechargeDefine.STATUS_TRUE);
				modelAndView = BankCallUtils.callApi(bean);
			} catch (Exception e) {
				e.printStackTrace();
				error = "1";
				errorDesc = "充值异常";
				return returnModelView(error, errorDesc, callback, "", "", pageFrom);
			}
		}
		LogUtil.endLog(WeChatRechargeDefine.THIS_CLASS, WeChatRechargeDefine.RECHARGE_MAPPING);
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
	@RequestMapping(WeChatRechargeDefine.RETURN_MAPPING)
	public ModelAndView rechargeReturn(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
		LogUtil.startLog(WeChatRechargeDefine.THIS_CLASS, WeChatRechargeDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
		String callback = request.getParameter("callback");
		bean.convert();
		Map<String, String> params = new HashMap<String, String>();
		String ip = CustomUtil.getIpAddr(request);
		params.put("ip", ip);
		String pageFrom = bean.getLogAcqResBean() == null ? "" : bean.getLogAcqResBean().getPageFrom();
		if (BankCallStatusConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
			LogUtil.debugLog(WeChatRechargeDefine.THIS_CLASS, WeChatRechargeDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
			// 更新充值的相关信息
			JSONObject errorDesc = this.rechargeSerivce.handleRechargeInfo(bean, params);
			// 充值成功
			if (errorDesc != null && errorDesc.get("error").equals("0")) {
				return returnModelView("0", "恭喜您，充值成功", callback, bean.getTxAmount(), "0.00", pageFrom);
			} else {
				// 充值失败
				return returnModelView("1", "充值失败", callback, "", "", pageFrom);
			}
		} else {
			this.rechargeSerivce.handleRechargeInfo(bean, params);
			String errorMessage = this.rechargeSerivce.getBankRetMsg(bean.getRetCode());
			// 充值失败
			return returnModelView("1", errorMessage, callback, "", "", pageFrom);
		}
	}

	/**
	 * 用户充值后处理
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(WeChatRechargeDefine.CALLBACK_MAPPING)
	public String rechargeCallback(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
		LogUtil.startLog(WeChatRechargeDefine.THIS_CLASS, WeChatRechargeDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
		bean.convert();
		LogUtil.debugLog(WeChatRechargeDefine.THIS_CLASS, WeChatRechargeDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
		BankCallResult result = new BankCallResult();
		String ip = CustomUtil.getIpAddr(request);
		Map<String, String> params = new HashMap<String, String>();
		params.put("ip", ip);
		// 更新充值的相关信息
		this.rechargeSerivce.handleRechargeInfo(bean, params);
		result.setStatus(true);
		return JSONObject.toJSONString(result, true);
	}

	/**
	 * 拼接返回结果
	 * 
	 * @param error
	 * @param data
	 * @param callback
	 * @param account
	 * @param fee
	 * @return
	 */
	public ModelAndView returnModelView(String error, String data, String callback, String account, String fee, String pageFrom) {
		// RespCode:000 成功；否则，失败
		Map<String, String> map = new HashMap<String, String>();
		map.put("error", error);
		map.put("errorDesc", data);
		if (error.equals("0")) {
			map.put("account", account);
			map.put("fee", fee);
		}
		data = JSON.toJSONString(map);
		try {
			data = URLEncoder.encode(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LogUtil.errorLog(WeChatRechargeDefine.THIS_CLASS, "returnModelView", e);
			e.printStackTrace();
		}
		StringBuffer url = new StringBuffer();
		url.append(callback).append("backinfo/").append(data);
		if (StringUtils.isNotEmpty(pageFrom)) {
			url.append("&pageFrom=" + pageFrom);
		}
		return new ModelAndView("redirect:" + url.toString());
	}/*

	*//**
	 * 用户充值
	 * 
	 * @param request
	 * @param response
	 * @return
	 *//*
	@RequestMapping(value = WeChatRechargeDefine.RECHARGE_ONLINE_ACTION)
	public ModelAndView rechargeOnline(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(WeChatRechargeDefine.THIS_CLASS, WeChatRechargeDefine.RECHARGE_ONLINE_ACTION);
		ModelAndView modelAndView = new ModelAndView();

		String error = "1";
		String errorDesc = "充值异常";
		// 充值金额
		String account = request.getParameter("account");
		// 用户Id
		String userIdStr = request.getParameter("userId");
		// 结果页URL
		String callback = request.getParameter("callback");
		// pageFrom
		String pageFrom = request.getParameter("pageFrom ");
		Integer userId = Integer.parseInt(userIdStr);
		
		// 手机号码
		String mobile = request.getParameter("mobile");
		// 短信验证码
		String smsCode = request.getParameter("smsCode");
		
		
		if (!account.matches("-?[0-9]+.*[0-9]*") || mobile==null || smsCode == null || !Validator.isMobile(mobile)) {
			error = "1";
			errorDesc = "参数校验失败";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		if (userId == null || userId == 0) {
			error = "1";
			errorDesc = "您未登陆，请先登录";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		if (StringUtils.isEmpty(callback)) {
			error = "1";
			errorDesc = "结果页URL获取失败";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		// 获取用户在银行的客户号
		BankOpenAccount bankOpenAccountTender = this.rechargeSerivce.getBankOpenAccount(userId);
		if (bankOpenAccountTender == null || Validator.isNull(bankOpenAccountTender.getAccount())) {
			error = "1";
			errorDesc = "用户未开户";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		// 用户信息
		Users user = this.rechargeSerivce.getUsers(userId);
		if (user == null) {
			error = "1";
			errorDesc = "用户不存在";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		// 查询用户信息
		UsersInfo usersInfo = this.rechargeSerivce.getUsersInfoByUserId(userId);
		// 用户银行卡号
		String cardNo = null;
		// 用户在银行的客户号
		String userCustId = bankOpenAccountTender.getAccount();
		// 身份证号
		String certId = this.rechargeSerivce.getUserIdcard(userId);
		// 短信序列号
		String smsSeq = this.rechargeSerivce.selectBankSmsSeq(userId,BankCallMethodConstant.TXCODE_DIRECT_RECHARGE_ONLINE);
		if (StringUtils.isBlank(smsSeq)) {
			error = "1";
			errorDesc = "未获取到短信序列号!";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		// 根据用户Id,检索用户银行卡信息
		BankCard bankCard = this.rechargeSerivce.selectBankCardByUserId(userId);
		if (bankCard == null || StringUtils.isEmpty(bankCard.getCardNo())) {
			modelAndView.addObject(WeChatRechargeDefine.STATUS, WeChatRechargeDefine.STATUS_FALSE);
			modelAndView.addObject(WeChatRechargeDefine.MESSAGE, "用户未绑卡!");
			error = "1";
			errorDesc = "用户未绑卡!";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		cardNo = bankCard.getCardNo();
		
		// 调用   2.3.4联机绑定卡到电子账户充值
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_DIRECT_RECHARGE_ONLINE);// 交易代码
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
		bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_WEI); // 交易渠道
		
		bean.setAccountId(userCustId); // 电子账号
		bean.setIdType(BankCallConstant.ID_TYPE_IDCARD); // 证件类型
		bean.setIdNo(certId); // 身份证号
		bean.setName(usersInfo.getTruename()); // 姓名
		bean.setMobile(mobile); // 手机号
		bean.setCardNo(cardNo); // 银行卡号
		bean.setTxAmount(CustomUtil.formatAmount(account)); // 交易金额
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
		bean.setLogUserName(user.getUsername());
		bean.setLogRemark("短信充值");
		bean.setLogClient(0);// 充值平台
		
		// 插入充值记录
		int ret = this.rechargeSerivce.insertRechargeInfo(bean);
		if (ret > 0) {
			;
		}else{
			error = "1";
			errorDesc = "充值异常";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		
		try {

			String ip = CustomUtil.getIpAddr(request);
			Map<String, String> params = new HashMap<String, String>();
			params.put("ip", ip);
			
			BankCallBean bankCallBean = BankCallUtils.callApiBg(bean);
			if (Validator.isNull(bankCallBean)) {
				error = "1";
				errorDesc = "充值异常";
				return returnModelView(error, errorDesc, callback, "", "", pageFrom);
			}
			String retCode = StringUtils.isNotBlank(bankCallBean.getRetCode()) ? bankCallBean.getRetCode(): "";
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
				this.rechargeSerivce.handleRechargeInfo(bankCallBean, params);
				errorDesc = this.rechargeSerivce.getBankRetMsg(retCode);
				error = "1";
				return returnModelView(error, errorDesc, callback, "", "", pageFrom);
			}
			
			// 更新充值的相关信息
			JSONObject msg = this.rechargeSerivce.handleRechargeInfo(bankCallBean, params);
			// 充值成功
			if (msg != null && msg.get("error").equals("0")) {
				return returnModelView("0", "恭喜您，充值成功", callback, bean.getTxAmount(), "0.00", pageFrom);
			} else {
				// 充值失败
				return returnModelView("1", "充值失败", callback, "", "", pageFrom);
			}
		} catch (Exception e) {
			e.printStackTrace();
			error = "1";
			errorDesc = "充值异常";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		
		
	}*/


	/**
	 * 短信充值发送验证码
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = WeChatRechargeDefine.SENDCODE_ACTION)
	public RechargeInfoResult sendCode(HttpServletRequest request, HttpServletResponse response) {
		
		LogUtil.startLog(WeChatRechargeDefine.THIS_CLASS, WeChatRechargeDefine.SENDCODE_ACTION);
		RechargeInfoResult result = new RechargeInfoResult();
		String message = "短信充值失败";
		boolean status = BankOpenDefine.STATUS_FALSE;
	
		// 用户Id
		String userIdStr = request.getParameter("userId");
		Integer userId = Integer.parseInt(userIdStr);
		// 手机号码
		String mobile = request.getParameter("mobile");
		
		
		if (mobile == null || !Validator.isMobile(mobile)) {
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		if (userId == null || userId == 0) {
			result.setMessage("您未登陆，请先登录");
			result.setStatus(status);
			return result;
		}
		
		// 获取绑定的银行卡号
		BankCard bankCard = this.rechargeSerivce.selectBankCardByUserId(userId);
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
		BankCallBean mobileBean = this.rechargeSerivce.sendRechargeOnlineSms(userId, cardNo, mobile, BankCallConstant.CHANNEL_WEI);
		
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
			message = "短信验证码发送失败！";
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		if (Validator.isNull(mobileBean.getSrvAuthCode()) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
			message = "短信验证码发送失败！";
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
	 * 用户充值JSON版
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = WeChatRechargeDefine.RECHARGE_ONLINE_ACTION)
	public RechargeInfoResult rechargeOnline(HttpServletRequest request, HttpServletResponse response) {
		
		RechargeInfoResult result = new RechargeInfoResult();

		String error = "1";
		String errorDesc = "充值异常";
		// 充值金额
		String account = request.getParameter("account");
		// 用户Id
		String userIdStr = request.getParameter("userId");
		// 结果页URL
		String callback = request.getParameter("callback");
		// pageFrom
		String pageFrom = request.getParameter("pageFrom");
		Integer userId = Integer.parseInt(userIdStr);
		
		// 手机号码
		String mobile = request.getParameter("mobile");
		// 短信验证码
		String smsCode = request.getParameter("smsCode");
		// 短信序列码，以最新的数据库为准
		String smsSeq = request.getParameter("smsSeq");
		
		
		if (!account.matches("-?[0-9]+.*[0-9]*") || mobile==null || smsCode == null || !Validator.isMobile(mobile)) {
			error = "1";
			errorDesc = "参数校验失败";
			result.setMessage(errorDesc);
			return result;
		}
		if (userId == null || userId == 0) {
			error = "1";
			errorDesc = "您未登陆，请先登录";
			result.setMessage(errorDesc);
			return result;
		}
		if (StringUtils.isEmpty(callback)) {
			error = "1";
			errorDesc = "结果页URL获取失败";
			result.setMessage(errorDesc);
			return result;
		}
		if(StringUtils.isBlank(smsSeq)){
			error = "1";
			errorDesc = "请填写正确的验证码";
			result.setMessage(errorDesc);
			return result;
		}
		// 获取用户在银行的客户号
		BankOpenAccount bankOpenAccountTender = this.rechargeSerivce.getBankOpenAccount(userId);
		if (bankOpenAccountTender == null || Validator.isNull(bankOpenAccountTender.getAccount())) {
			error = "1";
			errorDesc = "用户未开户";
			result.setMessage(errorDesc);
			return result;
		}
		// 用户信息
		Users user = this.rechargeSerivce.getUsers(userId);
		if (user == null) {
			error = "1";
			errorDesc = "用户不存在";
			result.setMessage(errorDesc);
			return result;
		}
		// 查询用户信息
		UsersInfo usersInfo = this.rechargeSerivce.getUsersInfoByUserId(userId);
		// 用户银行卡号
		String cardNo = null;
		// 用户在银行的客户号
		String userCustId = bankOpenAccountTender.getAccount();
		// 身份证号
		String certId = this.rechargeSerivce.getUserIdcard(userId);
		// 短信序列号
		if(StringUtils.isBlank(smsSeq)){
			smsSeq = this.rechargeSerivce.selectBankSmsSeq(userId,BankCallMethodConstant.TXCODE_DIRECT_RECHARGE_ONLINE);
			if (StringUtils.isBlank(smsSeq)) {
				error = "1";
				errorDesc = "未获取到短信序列号!";
				result.setMessage(errorDesc);
				return result;
			}
		}
		// 根据用户Id,检索用户银行卡信息
		BankCard bankCard = this.rechargeSerivce.selectBankCardByUserId(userId);
		if (bankCard == null || StringUtils.isEmpty(bankCard.getCardNo())) {
			error = "1";
			errorDesc = "用户未绑卡!";
			result.setMessage(errorDesc);
			return result;
		}
		cardNo = bankCard.getCardNo();
		
		// 调用   2.3.4联机绑定卡到电子账户充值
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_DIRECT_RECHARGE_ONLINE);// 交易代码
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
		bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_WEI); // 交易渠道
		
		bean.setAccountId(userCustId); // 电子账号
		bean.setIdType(BankCallConstant.ID_TYPE_IDCARD); // 证件类型
		bean.setIdNo(certId); // 身份证号
		bean.setName(usersInfo.getTruename()); // 姓名
		bean.setMobile(mobile); // 手机号
		bean.setCardNo(cardNo); // 银行卡号
		bean.setTxAmount(CustomUtil.formatAmount(account)); // 交易金额
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
		bean.setLogUserName(user.getUsername());
		bean.setLogRemark("短信充值");
		bean.setLogClient(0);// 充值平台
		
		// 插入充值记录
		int ret = this.rechargeSerivce.insertRechargeOnlineInfo(bean);
		if (ret > 0) {
			;
		}else{
			error = "1";
			errorDesc = "充值异常";
			result.setMessage(errorDesc);
			return result;
		}
		
		try {

			String ip = CustomUtil.getIpAddr(request);
			Map<String, String> params = new HashMap<String, String>();
			params.put("ip", ip);
			params.put("mobile", mobile);
			
			BankCallBean bankCallBean = BankCallUtils.callApiBg(bean);
			if (Validator.isNull(bankCallBean)) {
				error = "1";
				errorDesc = "充值异常";
				result.setMessage(errorDesc);
				return result;
			}
			String retCode = StringUtils.isNotBlank(bankCallBean.getRetCode()) ? bankCallBean.getRetCode(): "";
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
				this.rechargeSerivce.handleRechargeOnlineInfo(bankCallBean, params);
				errorDesc = this.rechargeSerivce.getBankRetMsg(retCode);
				error = "1";
				result.setMessage(errorDesc);
				return result;
			}
			
			// 更新充值的相关信息
			JSONObject msg = this.rechargeSerivce.handleRechargeOnlineInfo(bankCallBean, params);
			// 充值成功
			if (msg != null && msg.get("error").equals("0")) {
				
				// 设置返回url
				StringBuffer sbUrl = new StringBuffer();
				sbUrl.append(PropUtils.getSystem(CustomConstants.HYJF_WEB_URL));
				sbUrl.append(WeChatRechargeDefine.REQUEST_MAPPING);
				sbUrl.append(WeChatRechargeDefine.RECHARGE_RESULT_ACTION);
				sbUrl.append(".do?").append("cardNo").append("=").append(cardNo);
				sbUrl.append("&").append("money").append("=").append(account);
				sbUrl.append("&").append("userId").append("=").append(userId);
				sbUrl.append("&").append("nid").append("=").append(logOrderId);
				sbUrl.append("&").append("pageFrom").append("=").append(pageFrom);
				sbUrl.append("&").append("callback").append("=").append(callback);
				result.setRechargeUrl(sbUrl.toString());
				
				result.setMessage("充值成功！");
				result.setStatus(RechargeDefine.STATUS_TRUE);
				
				
			} else if(msg != null) {
				// 充值失败
				result.setMessage(msg.get("data").toString());
				return result;
			}
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			error = "1";
			errorDesc = "充值异常";
			result.setMessage(errorDesc);
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
	@RequestMapping(WeChatRechargeDefine.RECHARGE_RESULT_ACTION)
	public ModelAndView rechargeInfo(HttpServletRequest request, HttpServletResponse response) {
		String error = "1";
		String errorDesc = "充值异常";
		// 获取用户
		String cardNo = request.getParameter("cardNo");
		String money = request.getParameter("money");
		String nid = request.getParameter("nid");
		// 用户Id
		String userIdStr = request.getParameter("userId");
		// 结果页URL
		String callback = request.getParameter("callback");
		// pageFrom
		String pageFrom = request.getParameter("pageFrom ");
		Integer userId = Integer.parseInt(userIdStr);
		
		// 检查参数
		if(StringUtils.isBlank(nid)){
			error = "1";
			errorDesc = "充值异常";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}

		AccountRecharge accountRecharge = this.rechargeSerivce.selectRechargeInfo(userId, nid);
		if(accountRecharge == null){
			errorDesc = "充值失败！";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}else if (accountRecharge.getStatus() == 2) {
			return returnModelView("0", "恭喜您，充值成功", callback, CustomConstants.DF_FOR_VIEW.format(accountRecharge.getMoney()), "0.00", pageFrom);
		}else {
			errorDesc = "充值失败！";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		
		
	}
	
	
}
