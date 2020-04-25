package com.hyjf.api.surong.user.withdraw;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.surong.user.recharge.RdfRechargeService;
import com.hyjf.api.surong.user.recharge.RechargeDefine;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.user.auth.AuthService;
import com.hyjf.bank.service.user.bankwithdraw.BankWithdrawService;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.http.HttpDeal;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.Accountwithdraw;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.vip.apply.ApplyDefine;

@Controller
@RequestMapping(WithdrawDefine.REQUEST_MAPPING)
public class WithdrawServer extends BaseController {
	Logger _log = LoggerFactory.getLogger("融东风提现Controller");

	/** 发布地址 */
	private static String HOST_URL = PropUtils.getSystem("hyjf.web.host");

	@Autowired
	private RdfWithdrawService rdfWithdrawService;

	@Autowired
	private BankWithdrawService withdrawService;

	@Autowired
	private RdfRechargeService rechargeService;
	@Autowired
	private AuthService authService;
	private static DecimalFormat df = new DecimalFormat("########0.00");

	/**
	 * 用户提现
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(WithdrawDefine.CASH_MAPPING)
	public ModelAndView cash(HttpServletRequest request, HttpServletResponse response) {
		// ---传入参数---
		String nid = request.getParameter("nid");
		String sign = request.getParameter("sign");
		String mobile = request.getParameter("mobile"); // 手机号
		String transAmt = request.getParameter("getcash"); // 交易金额
		String cardNo = request.getParameter("card"); // 提现银行卡号
		String routeCode = request.getParameter("routeCode");// 路由代码
		String payAllianceCode = request.getParameter("payAllianceCode"); // 银行联号
		String forgetPwd = request.getParameter("forgetPwd"); // 忘记密码
		String platform = request.getParameter("platform"); // 平台
		String from = request.getParameter("from"); // 来自于哪个客户端
		String retUrl = request.getParameter("retUrl"); // 调用方同步回调url
		String callBackUrl = request.getParameter("callBackUrl"); // 调用方异步回调url
		// ---

		/* 调用方加密校验 */
		if (StringUtils.isEmpty(sign)) {
			_log.info("sign值为空---");
			return null;
		}
		String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);
		String miwen = MD5.toMD5Code(accessKey + mobile + transAmt + accessKey);
		if (!miwen.equals(sign)) {
			_log.info("融东风提现sign值非法---" + sign);
			return null;
		}
		/* ^^^调用方加密校验^^^ */

		String message = "";
		Users user = rechargeService.findUserByMobile(mobile);
		Integer userId = user.getUserId();
		String userName = user.getUsername();
		ModelAndView modelAndView = new ModelAndView(WithdrawDefine.JSP_CHINAPNR_SEND);
		// 检查参数
		JSONObject checkResult = checkParam(request, userId, transAmt, cardNo);
		if (checkResult != null) {
			message = checkResult.getString("statusDesc");
			if ("提现金额需大于1元！".equals(message)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("status", "1");
				map.put("statusDesc", "提现金额需大于1元！");
				modelAndView = new ModelAndView("jsonView", map);
			} else {
				modelAndView = new ModelAndView(WithdrawDefine.WITHDRAW_ERROR_PATH);
				modelAndView.addObject("message", message);
				return modelAndView;
			}
		}
		// 取得用户在汇付天下的客户号
		BankOpenAccount accountChinapnrTender = withdrawService.getBankOpenAccount(userId);
		if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getAccount())) {
			message = "您还未开户，请开户后重新操作";
			modelAndView = new ModelAndView(WithdrawDefine.WITHDRAW_ERROR_PATH);
			modelAndView.addObject("message", message);
			return modelAndView;
		}

		// 校验 银行卡号
		BankCard bankCard = this.withdrawService.getBankInfo(userId, cardNo);
		if (bankCard == null || Validator.isNull(bankCard.getCardNo())) {
			message = "该银行卡信息不存在，请核实后重新操作";
			modelAndView = new ModelAndView(WithdrawDefine.WITHDRAW_ERROR_PATH);
			modelAndView.addObject("message", message);
			return modelAndView;
		}
		// 取得手续费
		String fee = this.withdrawService.getWithdrawFee(userId, bankCard.getCardNo());
		// 交易金额
		BigDecimal txAmount = new BigDecimal(transAmt).subtract(new BigDecimal(fee));

		// 扣除手续费
		if ((txAmount.compareTo(new BigDecimal(50000)) > 0) && StringUtils.isNotBlank(payAllianceCode)) {
			routeCode = "2";// 路由代码
		}

		// 如果是大额提现,并银联行号为空
		if ("2".equals(routeCode) && Validator.isNull(payAllianceCode)) {
			message = "大额提现时,开户行号不能为空。";
			modelAndView = new ModelAndView(WithdrawDefine.WITHDRAW_ERROR_PATH);
			modelAndView.addObject("message", message);
			return modelAndView;
		}
		
		
		// 服务费授权状态和开关
        if (!authService.checkPaymentAuthStatus(userId)) {
			message = "用户未进行服务费授权";
			modelAndView = new ModelAndView(WithdrawDefine.WITHDRAW_ERROR_PATH);
			modelAndView.addObject("message", message);
			return modelAndView;
        }

		// 调用汇付接口(提现)
		String returnUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + RechargeDefine.SURONG_REQUEST_HOME
				+ WithdrawDefine.REQUEST_MAPPING + WithdrawDefine.RETURN_MAPPING + "?nid=" + nid + "&retUrl=" + retUrl
				+ "&from=" + from;// 提现同步回调路径
		String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + RechargeDefine.SURONG_REQUEST_HOME
				+ WithdrawDefine.REQUEST_MAPPING + WithdrawDefine.CALLBACK_MAPPING + "?nid=" + nid + "&callBackUrl="
				+ callBackUrl + "&from=" + from;// 提现异步回调路径
		Users users = withdrawService.getUsers(userId);
		UsersInfo usersInfo = withdrawService.getUsersInfoByUserId(userId);
		// 调用汇付接口(4.2.2 用户绑卡接口)
		BankCallBean bean = new BankCallBean();
		bean.setLogOrderId(GetOrderIdUtils.getOrderId0(userId));
		bean.setLogUserId(String.valueOf(userId));
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
		// 大额提现时,走人行通道
		if ("2".equals(routeCode) && StringUtils.isNotBlank(payAllianceCode)) {
			bean.setRouteCode(routeCode);
			bean.setCardBankCnaps(payAllianceCode);
			LogAcqResBean logAcq = new LogAcqResBean();
			logAcq.setPayAllianceCode(payAllianceCode); // 银联行号放到私有域中
			bean.setLogAcqResBean(logAcq);
		}
		String sign_forgetPwd = MD5.toMD5Code(accessKey + mobile + accessKey);
		bean.setForgotPwdUrl(forgetPwd + "?sign=" + sign_forgetPwd + "&mobile=" + mobile);
		bean.setRetUrl(returnUrl);// 商户前台台应答地址(必须)
		bean.setNotifyUrl(bgRetUrl); // 商户后台应答地址(必须)
		LogAcqResBean logAcq = new LogAcqResBean();
		bean.setLogAcqResBean(logAcq);
		// 插值用参数
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", String.valueOf(userId));
		params.put("userName", userName);
		params.put("ip", CustomUtil.getIpAddr(request));
		params.put("cardNo", cardNo);
		params.put("client", platform);// 平台类型 0pc 1WX 2AND 3IOS 4other
		params.put("fee", CustomUtil.formatAmount(fee));// 手续费
		// 用户提现前处理
		int cnt = this.withdrawService.updateBeforeCash(bean, params);
		if (cnt > 0) {
			// 跳转到江西银行画面
			try {
				_log.info("【调用江西银行提现接口   orderId:" + bean.getLogOrderId() + "-mobile:" + bean.getMobile() + "cardNo:"
						+ bean.getCardNo() + "-platform:" + bean.getLogClient() + "-txamount:" + bean.getTxAmount()
						+ " -fee:" + bean.getTxFee() + " -routeCode:" + bean.getRouteCode() + " -payAllianceCode:"
						+ bean.getPayAllianceCode() + " 】");
				modelAndView = BankCallUtils.callApi(bean);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "请不要重复操作";
			modelAndView = new ModelAndView(WithdrawDefine.WITHDRAW_ERROR_PATH);
			modelAndView.addObject("message", message);
			return modelAndView;
		}
		return modelAndView;
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
		Users users = this.withdrawService.getUsers(userId);
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
		Account account = this.withdrawService.getAccount(userId);
		// 投标金额大于可用余额
		if (account == null || transAmt.compareTo(account.getBankBalance()) > 0) {
			return jsonMessage("提现金额大于可用余额，请确认后再次提现。", "1");
		}
		// 检查参数(银行卡ID是否数字)
		if (Validator.isNotNull(bankId) && !NumberUtils.isNumber(bankId)) {
			return jsonMessage("银行卡号不正确，请确认后再次提现。", "1");
		}
		return null;
	}

	/**
	 * 获取提现信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(WithdrawDefine.GET_WITHDRAW_INFO_MAPPING)
	public JSONObject getCashInfo(HttpServletRequest request, HttpServletResponse response) {
		// ---传入参数---
		String getcash = request.getParameter("getcash"); // 提现金额
		Integer userId = Integer.valueOf(request.getParameter("userId")); // 用户ID
		// ---
		_log.info("【获取提现信息   getcash:+" + getcash + " userId:" + userId + "】");
		JSONObject ret = new JSONObject();
		// 金额显示格式
		DecimalFormat moneyFormat = null;
		moneyFormat = CustomConstants.DF_FOR_VIEW;
		// 取得用户当前余额
		Account account = this.withdrawService.getAccount(userId);
		if (account == null) {
			ret.put("status", "1");
			ret.put("statusDesc", "你的账户信息存在异常，请联系客服人员处理。");
			return ret;
		} else {
			if (account.getBankBalance() == null) {
				ret.put("total", "0");// 可提现金额
			} else {
				ret.put("total", moneyFormat.format(account.getBankBalance()));// 可提现金额
			}
		}
		// 银行类型
		String bankType = "";
		// 银联行号
		String openBankCode = "";
		// 路由代码
		String isShowBankCode = "0";
		// 是否是大额提现
		String isLargeWithdrawal = "0";
		// 取得银行卡信息
		List<BankCard> banks = withdrawService.getBankCardByUserId(userId);
		if (banks.size() > 0) {
			ret.put("bankCnt", banks.size() + "");
			List<BankCardBean> bankcards = new ArrayList<BankCardBean>();
			for (int j = 0; j < banks.size(); j++) {
				BankCard bankCard = banks.get(j);
				BankCardBean bankCardBean = new BankCardBean();
				openBankCode = bankCard.getPayAllianceCode();// 银联行号
				bankType = String.valueOf(bankCard.getBankId()); // 银行类型
				bankCardBean.setIsDefault("2");// 快捷卡
				bankCardBean.setBankCode(bankCard.getBank());// 银行代号?需要工具类
				bankCardBean.setBank(bankCard.getBank());// 银行名称
				bankCardBean.setLogo(HOST_URL + rdfWithdrawService.getBankLogoByBankName(bankCard.getBank()));
				bankCardBean.setCardNo(bankCard.getCardNo());
				bankcards.add(bankCardBean);
			}
			ret.put("banks", bankcards);
		} else {
			ret.put("bankCnt", "0");
		}

		// 如果提现金额是0
		if ("0".equals(getcash) || "".equals(getcash)) {
			ret.put("accountDesc", "手续费：0元；实际到账：0元");
		} else {
			String balance = "";
			if ((new BigDecimal(getcash).subtract(BigDecimal.ONE)).compareTo(BigDecimal.ZERO) < 0) {
				balance = "0";
			} else {
				// 扣除手续费后的提现金额
				BigDecimal transAmt = new BigDecimal(getcash).subtract(BigDecimal.ONE);
				balance = moneyFormat.format(new BigDecimal(getcash).subtract(BigDecimal.ONE));
				// 工行或中行 提现金额大于 5万时

				// 扣除手续费
				if ((transAmt.compareTo(new BigDecimal(50000)) > 0) && StringUtils.isNotBlank(openBankCode)) {
					isShowBankCode = "1";// 路由代码
					isLargeWithdrawal = "1";// 是否是大额提现表示 0:非 1:是
				}

			}
			ret.put("accountDesc", "手续费：1元；实际到账：" + balance + "元");
			ret.put("balance", balance);
		}

		ret.put("free", "1");
		ret.put("isShowBankCode", isShowBankCode); // 路由代码
		ret.put("openBankCode", openBankCode); // 银联行号
		ret.put("isLargeWithdrawal", isLargeWithdrawal); // 是否是大额提现表示 0:非 1:是
		ret.put("status", "0");
		ret.put("statusDesc", "成功");
		return ret;
	}

	// --------------------------------------------------------------------------------------------------
	// 返回参数--- status : 0客户端不做处理 1客户端做成功处理
	// nid : 客户端充值号
	// sign : 密文
	// cardNo : 银行卡号
	// total : 提现金额
	// balance : 实际到帐金额
	// fee : 手续费
	// orderId : 汇盈提现orderId
	// message : 描述

	/**
	 * 用户提现后处理 同步
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(WithdrawDefine.RETURN_MAPPING)
	public ModelAndView cashReturn(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
		bean.convert();
		_log.info("--↓↓ 提现同步回调Start ↓↓--orderId: " + bean.getLogOrderId() + " nid=" + request.getParameter("nid")
				+ " -retCode:" + bean.getRetCode());
		String logOrderId = bean.getLogOrderId();
		Accountwithdraw accountwithdraw = withdrawService.getAccountWithdrawByOrdId(logOrderId);
		/* 给调用方返回数据的封装 --begin */
		WithdrawResultBean withdrawResult = new WithdrawResultBean();
		withdrawResult.setCallBackAction(request.getParameter("retUrl"));
		String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);
		String nid = request.getParameter("nid");
		String miwen = MD5.toMD5Code(accessKey + nid + accessKey);
		withdrawResult.set("nid", nid);
		withdrawResult.set("sign", miwen);
		withdrawResult.set("status", "3");
		ModelAndView modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_VIEW);
		/* 给调用方返回数据的封装 --end */
		if (accountwithdraw != null) {
			withdrawResult.set("total", df.format(accountwithdraw.getTotal()));
			withdrawResult.set("balance", df.format(accountwithdraw.getCredited()));
			withdrawResult.set("message", "提现成功");
			_log.info("--↑↑ 提现同步回调End ↑↑-- nid=" + request.getParameter("nid") + "---提现成功");
			modelAndView.addObject("callBackForm", withdrawResult);
			return modelAndView;
		} else {
			withdrawResult.set("message", "银行处理中，请稍后查询交易明细");
			_log.info("--↑↑ 提现同步回调End ↑↑-- nid=" + request.getParameter("nid") + "---提现后同步处理结束：处理中");
			modelAndView.addObject("callBackForm", withdrawResult);
			return modelAndView;
		}

	}

	/**
	 * 用户提现后处理 异步
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(WithdrawDefine.CALLBACK_MAPPING)
	public Object cashCallBack(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
		bean.convert();
		_log.info("--↓↓ 提现异步回调Start ↓↓-- orderId: " + bean.getLogOrderId() + " nid=" + request.getParameter("nid")
				+ " -retCode:" + bean.getRetCode());
		BankCallResult bankCallResult = new BankCallResult();
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
		} else {
			// 执行结果(失败)
			status = BankCallStatusConstant.STATUS_FAIL;
		}
		// 更新提现状态
		try {
			Integer userId = Integer.parseInt(bean.getLogUserId());
			// 插值用参数
			Map<String, String> params = new HashMap<String, String>();
			params.put("userId", String.valueOf(userId));
			params.put("ip", CustomUtil.getIpAddr(request));
			// 执行提现后处理
			this.withdrawService.handlerAfterCash(bean, params);
		} catch (Exception e) {
			// 执行结果(失败)
			status = BankCallStatusConstant.STATUS_FAIL;
			_log.info("提现后异步处理结束：提现失败：" + e);
		}
		if (BankCallStatusConstant.STATUS_SUCCESS.equals(status)) {
			error = "0";
			message = "恭喜您，提现成功";
		} else {
			error = "1";
			message = "很遗憾，提现失败";
		}
		bankCallResult.setErrorCode(error);
		bankCallResult.setMessage(message);
		bankCallResult.setStatus(true);
		// 融东风专用参数返回封装
		String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);
		String nid = request.getParameter("nid");
		String miwen = MD5.toMD5Code(accessKey + nid + accessKey);
		Map<String, String> param = new HashMap<>();
		param.put("nid", nid);
		param.put("sign", miwen);
		param.put("message", message);
		param.put("status", "1");
		if ("0".equals(error)) {
			rdfWithdrawService.getWithdrawResult(Integer.parseInt(bean.getLogUserId()), bean.getLogOrderId(), param);
			String result = HttpDeal.post(request.getParameter("callBackUrl"), param);
			_log.info("【提现异步向融东风发送数据结果:url=" + request.getParameter("callBackUrl") + "---result=" + result
					+ "---status=" + param.get("status") + "---cardNo=" + param.get("cardId") + "---total="
					+ param.get("total") + "---balance=" + param.get("balance") + "---fee=" + param.get("fee")
					+ "---nid=" + nid + "】");
		} else {
			String result = HttpDeal.post(request.getParameter("callBackUrl"), param);
			_log.info(
					"【提现异步向融东风发送数据结果:url=" + request.getParameter("callBackUrl") + "---result=" + result + "---提现失败】");
		}
		return bankCallResult;
	}
}
