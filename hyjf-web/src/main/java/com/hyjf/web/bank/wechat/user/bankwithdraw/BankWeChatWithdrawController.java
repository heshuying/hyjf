package com.hyjf.web.bank.wechat.user.bankwithdraw;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.bankwithdraw.BankWithdrawService;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.Accountwithdraw;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.user.regist.UserRegistDefine;

/**
 * 微信端用户提现Controller
 * 
 * @author liuyang
 *
 */
@Controller
@RequestMapping(value = BankWeChatWithdrawDefine.REQUEST_MAPPING)
public class BankWeChatWithdrawController extends BaseController {

	/** THIS_CLASS */
	private static final String THIS_CLASS = BankWeChatWithdrawController.class.getName();

	@Autowired
	private BankWithdrawService userWithdrawService;

	/**
	 * 用户提现
	 *
	 * @param request
	 * @param form
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(BankWeChatWithdrawDefine.CASH_MAPPING)
	public ModelAndView cash(HttpServletRequest request, HttpServletResponse response) throws IOException {
		LogUtil.startLog(THIS_CLASS, BankWeChatWithdrawDefine.CASH_MAPPING);
		ModelAndView modelAndView = new ModelAndView();
		String error = "1";
		String errorDesc = "提现失败";
		String callback = request.getParameter("callback");
		String pageFrom = request.getParameter("pageFrom");
		String userIdStr = request.getParameter("userId");
		String withdrawCode = request.getParameter("withdrawCode"); // 提现验证码
		String mobile = request.getParameter("mobile");
		// 用户ID
		if(StringUtils.isEmpty(userIdStr)){
			error = "1";
			errorDesc = "您未登陆，请先登录";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		Integer userId = Integer.parseInt(userIdStr); // 用户ID
		if(userId == null || userId == 0){
			error = "1";
			errorDesc = "您未登陆，请先登录";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		Users users = userWithdrawService.getUsers(userId);
		UsersInfo usersInfo = userWithdrawService.getUsersInfoByUserId(userId);
		String userName = users.getUsername(); // 用户名
		String transAmt = request.getParameter("account");// 交易金额
		String cardNo = request.getParameter("cardNo");// 提现银行卡号
//		String bankType = request.getParameter("bankType");// 银行类型
		String payAllianceCode = request.getParameter("payAllianceCode");// 银联行号
		// 检查参数
		JSONObject checkResult = checkParam(request, userId, transAmt, cardNo);
		if (checkResult != null) {
			error = checkResult.getString("error");
			errorDesc = checkResult.getString("errorDesc");
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		
		// 如果是出借人提现需要校验短信验证码
		if(usersInfo.getRoleId() == 1){
			if (StringUtils.isBlank(withdrawCode)) {
				error = "1";
				errorDesc = "验证码未填写";
				return returnModelView(error, errorDesc, callback, "", "", pageFrom);
			}
			
			int smsCheck = userWithdrawService.updateCheckMobileCode(mobile, withdrawCode, "TPL_SMS_WITHDRAW", CustomConstants.CLIENT_PC, UserRegistDefine.CKCODE_YIYAN, UserRegistDefine.CKCODE_USED);
			if (smsCheck == 0) {
				error = "1";
				errorDesc = "验证码错误";
				return returnModelView(error, errorDesc, callback, "", "", pageFrom);
			}
		}
		
		BankOpenAccount accountChinapnrTender = userWithdrawService.getBankOpenAccount(userId);
		if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getAccount())) {
			error = "1";
			errorDesc = "您还未开户，请开户后重新操作";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		// 取得银行卡号
		BankCard bankCard = this.userWithdrawService.getBankInfo(userId, cardNo);
		if (bankCard == null || Validator.isNull(bankCard.getCardNo())) {
			error = "1";
			errorDesc = "您还未绑定银行卡，请绑定银行卡后重新操作";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		// 检测用户是否开通缴费授权，如果未开通，弹窗提示，并引导用户去账户中心-账户设置做缴费授权
//		Integer payAuthStatus = userWithdrawService.checkPaymentAuth(userId);
		//update by jijun 2018/04/09 合规接口改造一期
		/*if (users.getPaymentAuthStatus() != 1) {
			error = "1";
			errorDesc = "您还未开通缴费授权，请开通后重新操作";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}*/

//		// 工行或人行
//		if (bankType.equals("7") || bankType.equals("56")) {
			if ((new BigDecimal(transAmt).compareTo(new BigDecimal(50001)) > 0) && StringUtils.isBlank(payAllianceCode)) {
				error = "1";
				errorDesc = "大额提现时,开户银联行号不能为空!";
				return returnModelView(error, errorDesc, callback, "", "", pageFrom);
			}
//		}
//		// 其他银行
//		if ((new BigDecimal(transAmt).compareTo(new BigDecimal(200001)) > 0) && StringUtils.isBlank(payAllianceCode)) {
//			error = "1";
//			errorDesc = "大额提现时,开户银联行号不能为空!";
//			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
//		}

		// 取得手续费 默认1
		String fee = this.userWithdrawService.getWithdrawFee(userId, cardNo);
		// 实际取现金额
		// 去掉一块钱手续费
		transAmt = new BigDecimal(transAmt).subtract(new BigDecimal(Validator.isNull(fee) ? "0" : fee)).toString();
		if (new BigDecimal(transAmt).compareTo(BigDecimal.ZERO) == 0) {
			error = "1";
			errorDesc = "提现金额不能小于一块";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		// 调用汇付接口(提现)
		String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + BankWeChatWithdrawDefine.REQUEST_MAPPING + BankWeChatWithdrawDefine.RETURN_MAPPING + ".do?callback=" + callback;// 支付工程路径
		String bgRetUrl = PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) + BankWeChatWithdrawDefine.REQUEST_MAPPING + BankWeChatWithdrawDefine.CALLBACK_MAPPING + ".do?callback=" + callback;// 支付工程路径
		String successfulUrl = PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) + BankWeChatWithdrawDefine.REQUEST_MAPPING + BankWeChatWithdrawDefine.RETURN_MAPPING + ".do?isSuccess=1&"
		        + "&amt"+CustomUtil.formatAmount(transAmt)+"&fee="+CustomUtil.formatAmount(fee)+"callback=" + callback;
		// 路由代码
		String routeCode = "";
		BankCallBean bean = new BankCallBean();
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_MOBILE_WITHDRAW);
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_WITHDRAW);
		bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
		bean.setChannel(BankCallConstant.CHANNEL_WEI);// 交易渠道
		bean.setAccountId(accountChinapnrTender.getAccount());// 存管平台分配的账号
		bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);// 证件类型01身份证
		bean.setIdNo(usersInfo.getIdcard());// 证件号
		bean.setName(usersInfo.getTruename());// 姓名
		bean.setMobile(users.getMobile());// 手机号
		bean.setCardNo(bankCard.getCardNo());// 银行卡号
		bean.setTxAmount(CustomUtil.formatAmount(transAmt));
		bean.setTxFee(fee);
		// 交易成功后跳转的链接
		bean.setSuccessfulUrl(successfulUrl);
//		// 中行跟工行,提现金额大于五万,走人行通道,路由代码传2
//		if (bankType.equals("7") || bankType.equals("56")) {
			// 扣除手续费
			if ((new BigDecimal(transAmt).compareTo(new BigDecimal(50000)) > 0) && StringUtils.isNotBlank(payAllianceCode)) {
				routeCode = "2";// 路由代码
				bean.setRouteCode(routeCode); // 路由代码
				bean.setCardBankCnaps(payAllianceCode);// 绑定银行联行号
			}
//		} else if ((new BigDecimal(transAmt).compareTo(new BigDecimal(200000)) > 0) && StringUtils.isNotBlank(payAllianceCode)) {
//			// 其他银行提现金额大于20万,走人行通道,路由代码传2
//			routeCode = "2";// 路由代码
//			bean.setRouteCode(routeCode); // 路由代码
//			bean.setCardBankCnaps(payAllianceCode);// 绑定银行联行号
//		}
		// 企业用户提现
		if (users.getUserType() == 1) { // 企业用户 传组织机构代码
			CorpOpenAccountRecord record = userWithdrawService.getCorpOpenAccountRecord(userId);
			bean.setIdType(record.getCardType() != null ? String.valueOf(record.getCardType()) : BankCallConstant.ID_TYPE_COMCODE);// 证件类型 20：其他证件（组织机构代码）25：社会信用号
			bean.setIdNo(record.getBusiCode());
			bean.setName(record.getBusiName());
			bean.setRouteCode("2");
			bean.setCardBankCnaps(StringUtils.isEmpty(payAllianceCode) ? bankCard.getPayAllianceCode() : payAllianceCode);
		}
		// 微信忘记交易密码url
		String forgetPassUrl = CustomConstants.WECHAT_FORGET_PASSWORD_URL + "?userId=" + userId + "&callback=" + CustomConstants.WECHAT_FORGET_PASSWORD_CALLBACK_URL;
		bean.setForgotPwdUrl(forgetPassUrl);
		bean.setRetUrl(retUrl);// 商户前台台应答地址(必须)
		bean.setNotifyUrl(bgRetUrl); // 商户后台应答地址(必须)
		LogAcqResBean logAcq = new LogAcqResBean();
		logAcq.setPageFrom(pageFrom);
		if ("2".equals(routeCode)) {
			logAcq.setPayAllianceCode(payAllianceCode);
		}
		bean.setLogAcqResBean(logAcq);
		System.out.println("绑卡前台回调函数：\n" + bean.getRetUrl());
		System.out.println("绑卡后台回调函数：\n" + bean.getNotifyUrl());
		// 插值用参数
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", String.valueOf(userId));
		params.put("userName", userName);
		params.put("ip", CustomUtil.getIpAddr(request));
		params.put("cardNo", cardNo);
		params.put("fee", CustomUtil.formatAmount(fee));
		params.put("client", "1");// 微官网
		// 用户提现前处理
		int cnt = this.userWithdrawService.updateBeforeCash(bean, params);
		if (cnt > 0) {
			// 跳转到汇付天下画面
			try {
				modelAndView.addObject(BankWeChatWithdrawDefine.STATUS, BankWeChatWithdrawDefine.STATUS_TRUE);
				modelAndView = BankCallUtils.callApi(bean);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			error = "1";
			errorDesc = "请不要重复操作";
			return returnModelView(error, errorDesc, callback, "", "", pageFrom);
		}
		LogUtil.endLog(THIS_CLASS, BankWeChatWithdrawDefine.CASH_MAPPING);
		return modelAndView;
	}

	/**
	 * 用户提现后同步回调处理
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(BankWeChatWithdrawDefine.RETURN_MAPPING)
	public ModelAndView cashReturn(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
		LogUtil.startLog(THIS_CLASS, BankWeChatWithdrawDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
		bean.convert();
		String logOrderId = bean.getLogOrderId();
		String callback = request.getParameter("callback");
		String pageFrom = bean.getLogAcqResBean() == null ? "" : bean.getLogAcqResBean().getPageFrom();
		Accountwithdraw accountwithdraw = userWithdrawService.getAccountWithdrawByOrdId(logOrderId);
		String isSuccess = request.getParameter("isSuccess");
		// 调用成功了
        if (isSuccess != null && "1".equals(isSuccess)) {
            if (accountwithdraw != null) {
                return returnModelView("0", "恭喜您，提现成功", callback, accountwithdraw.getFee().toString(), accountwithdraw.getCredited().toString(), pageFrom);
            } else {
                return returnModelView("0", "恭喜您，提现成功", callback, request.getParameter("fee"), request.getParameter("amt"), pageFrom);
            }
        }
		if (accountwithdraw != null) {
			return returnModelView("0", "恭喜您，提现成功", callback, accountwithdraw.getFee().toString(), accountwithdraw.getCredited().toString(), pageFrom);
		} else {
			return returnModelView("2", "银行处理中，请稍后查询交易明细", callback, "", "", pageFrom);
		}
	}

	/**
	 * 用户提现后异步回调处理
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(BankWeChatWithdrawDefine.CALLBACK_MAPPING)
	public String cashCallBack(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
		LogUtil.startLog(THIS_CLASS, BankWeChatWithdrawDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
		BankCallResult result = new BankCallResult();
		bean.convert();
		LogUtil.debugLog(THIS_CLASS, BankWeChatWithdrawDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
		try {
			Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
			// 插值用参数
			Map<String, String> params = new HashMap<String, String>();
			params.put("userId", String.valueOf(userId));
			params.put("ip", CustomUtil.getIpAddr(request));
			// 执行提现后处理
			this.userWithdrawService.handlerAfterCash(bean, params);
			// 执行结果(成功)
			LogUtil.debugLog(THIS_CLASS, BankWeChatWithdrawDefine.RETURN_MAPPING, "成功");
		} catch (Exception e) {
			// 执行结果(失败)
			LogUtil.errorLog(THIS_CLASS, BankWeChatWithdrawDefine.RETURN_MAPPING, e);
		}
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
		// 检查参数(交易金额是否数字)
		if (Validator.isNull(transAmtStr) || !NumberUtils.isNumber(transAmtStr)) {
			return jsonMessage("请输入提现金额。", "1");
		}
		// 检查参数(交易金额是否大于0)
		BigDecimal transAmt = new BigDecimal(transAmtStr);
		String feetmp = PropUtils.getSystem(BankCallConstant.BANK_FEE);
		if (feetmp == null) {
			feetmp = "1";
		}
		if (transAmt.compareTo(new BigDecimal(feetmp)) <= 0) {
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
		return null;
	}

	/**
	 * 拼接返回结果
	 * 
	 * @param error
	 * @param data
	 * @param callback
	 * @param result
	 * @param account
	 * @param integer
	 * @return
	 */
	public ModelAndView returnModelView(String error, String data, String callback, String fee, String account, String pageFrom) {
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
			LogUtil.errorLog(THIS_CLASS, "returnModelView", e);
			e.printStackTrace();
		}
		StringBuffer url = new StringBuffer();
		url.append(callback).append("backinfo/").append(data);
		if (StringUtils.isNotEmpty(pageFrom)) {
			url.append("&pageFrom=" + pageFrom);
		}
		return new ModelAndView("redirect:" + url.toString());

	}
}
