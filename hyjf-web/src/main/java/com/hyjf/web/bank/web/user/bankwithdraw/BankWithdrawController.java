package com.hyjf.web.bank.web.user.bankwithdraw;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.bank.service.user.auth.AuthService;
import org.apache.commons.lang.StringUtils;
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
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.bank.service.user.bankwithdraw.BankCardBean;
import com.hyjf.bank.service.user.bankwithdraw.BankWithdrawService;
import com.hyjf.bank.service.user.recharge.RechargeService;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.BankCardUtil;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountRecharge;
import com.hyjf.mybatis.model.auto.Accountwithdraw;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BanksConfig;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.bank.web.user.bindcard.BindCardDefine;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.util.WebUtils;

/**
 * 江西银行用户提现
 * 
 * @author liuyang
 *
 */
@Controller
@RequestMapping(value = BankWithdrawDefine.REQUEST_MAPPING)
public class BankWithdrawController extends BaseController {
	
	Logger _log = LoggerFactory.getLogger(BankWithdrawController.class);

	/** 类名 */
	private static final String THIS_CLASS = BankWithdrawController.class.getName();

	@Autowired
	private BankWithdrawService userWithdrawService;

	@Autowired
	private RechargeService userRechargeService;
	@Autowired
    private AuthService authService;
	/**
	 * 
	 * 跳转到提现页面
	 * 
	 * @author renxingchen
	 * @return
	 */
	@RequestMapping(value = BankWithdrawDefine.TO_WITHDRAW)
	public ModelAndView toWithdraw(HttpServletRequest request) {
		ModelAndView modelAndView;
		DecimalFormat DF_FOR_VIEW = new DecimalFormat("#,##0.00");
		// 取得用户iD
		Integer userId = WebUtils.getUserId(request); // 用户ID
		WebViewUser webViewUser = WebUtils.getUser(request);

		// 取得用户当前余额
		Account account = this.userWithdrawService.getAccount(userId);
		if (account == null) {
			modelAndView = new ModelAndView(BankWithdrawDefine.WITHDRAW_INFO);
			modelAndView.addObject(BankWithdrawDefine.STATUS, BankWithdrawDefine.STATUS_FALSE);
			modelAndView.addObject(BankWithdrawDefine.ERROR, "你的账户信息存在异常，请联系客服人员处理。");
			return modelAndView;
		}
		// 查询页面上可以挂载的银行列表
		List<BankCard> banks = userWithdrawService.getBankCardByUserId(userId);
		if (banks == null || banks.size() == 0) {
			// 用户未绑卡
			modelAndView = new ModelAndView("redirect:" + CustomConstants.HOST + "/bank/web/bindCard/bindCardNew.do");
			LogUtil.endLog(THIS_CLASS, BankWithdrawDefine.TO_WITHDRAW, "[用户未绑卡]");
			return modelAndView;
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

		modelAndView = new ModelAndView(BankWithdrawDefine.WITHDRAW);
		modelAndView.addObject("total", CustomConstants.DF_FOR_VIEW.format(account.getBankBalance()));// 可提现金额
		List<BankCardBean> bankcards = new ArrayList<BankCardBean>();
		// 银行联号
		String payAllianceCode = "";
		// 银行类型
		String bankType = "";
		String feeWithdraw = "1.00";
		if (banks != null && banks.size() != 0) {
			for (BankCard bank : banks) {
				BankCardBean bankCardBean = new BankCardBean();
				bankCardBean.setId(bank.getId());
				bankCardBean.setBankId(bank.getBankId());// 银行Id
				bankType = String.valueOf(bank.getBankId());
				payAllianceCode = bank.getPayAllianceCode() == null ? "" : bank.getPayAllianceCode();
				bankCardBean.setPayAllianceCode(bank.getPayAllianceCode() == null ? "" : bank.getPayAllianceCode());
				bankCardBean.setBank(bank.getBank());
				String cardNo = bank.getCardNo();
				String cardNoInfo = BankCardUtil.getCardNo(cardNo);
				bankCardBean.setCardNoInfo(cardNoInfo);
				bankCardBean.setCardNo(cardNo);
				bankCardBean.setIsDefault("2");// 卡类型

				
				Integer bankId = bank.getBankId();
				BanksConfig banksConfig = userWithdrawService.getBanksConfigByBankId(bankId);
				if (banksConfig != null && StringUtils.isNotEmpty(banksConfig.getBankName())) {
					bankCardBean.setBank(banksConfig.getBankName());
				}
				bankcards.add(bankCardBean);
				// add by liuyang 神策数据埋点追加 20180717 start
				modelAndView.addObject("bankName", StringUtils.isBlank(bank.getBank()) ? "" :bank.getBank());
				// add by liuyang 神策数据埋点追加 20180717 end
			}
			
			feeWithdraw = userWithdrawService.getWithdrawFee(userId, banks.get(0).getCardNo());
		}
		// 取用户信息
		Users user = this.userWithdrawService.getUsers(userId);
		if (user == null) {
			modelAndView = new ModelAndView(BankWithdrawDefine.WITHDRAW_INFO);
			modelAndView.addObject(BankWithdrawDefine.STATUS, BankWithdrawDefine.STATUS_FALSE);
			modelAndView.addObject(BankWithdrawDefine.ERROR, "你的账户信息存在异常，请联系客服人员处理。");
			return modelAndView;
		}
		
		modelAndView.addObject("userType", user.getUserType());// 是否为企业用户（
		modelAndView.addObject("banks", bankcards);
		modelAndView.addObject("isSetPassWord", user.getIsSetPassword());
		modelAndView.addObject("payAllianceCode", payAllianceCode);
		modelAndView.addObject("bankType", bankType);
		modelAndView.addObject("feeWithdraw", DF_FOR_VIEW.format(new BigDecimal(feeWithdraw)));
		modelAndView.addObject("roleId", webViewUser==null?"":webViewUser.getRoleId());
//		modelAndView.addObject("paymentAuthStatus", user.getPaymentAuthStatus());
		// 合规三期新增  by sss
        HjhUserAuth hjhUserAuth = this.authService.getHjhUserAuthByUserId(userId);
        // 是否开启服务费授权 0未开启  1已开启
        modelAndView.addObject("paymentAuthStatus", hjhUserAuth==null?"":hjhUserAuth.getAutoPaymentStatus());
        // 是否开启服务费授权 0未开启  1已开启
        modelAndView.addObject("paymentAuthOn", CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus());
		modelAndView.addObject("bankBalance", CustomConstants.DF_FOR_VIEW.format(bankBalance));
		return modelAndView;
	}
	
	/**
	 * 跳转到提现确认页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = BankWithdrawDefine.TO_SMS_CONFIRM)
	public ModelAndView toSMSConfirm(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView(BankWithdrawDefine.WITHDRAW_CONFIRM);
		String isSetPassWord = request.getParameter("isSetPassWord");
		String total = request.getParameter("total");
		String bankType = request.getParameter("bankType");
		String withdrawmoney = request.getParameter("withdrawmoney");
		String widCard = request.getParameter("widCard");
		String payAllianceCode = request.getParameter("payAllianceCode");
		
		//预留手机号
		String phoneNum = ""; 
		// 取得用户iD
		Integer userId = WebUtils.getUserId(request); 
		List<BankCard> banks = userWithdrawService.getBankCardByUserId(userId);
		if(banks != null && !banks.isEmpty()){
			// 获取用户银行预留手机号
			phoneNum = banks.get(0).getMobile();
		}
		if(StringUtils.isBlank(phoneNum)) {
			// 如果用户未预留手机号则取平台手机号
			phoneNum = WebUtils.getUser(request).getMobile();
		}
		modelAndView.addObject("phoneNum", phoneNum);
		modelAndView.addObject("bankType", bankType);
		modelAndView.addObject("isSetPassWord", isSetPassWord);
		modelAndView.addObject("total", total);
		modelAndView.addObject("withdrawmoney", withdrawmoney);
		modelAndView.addObject("widCard", widCard);
		modelAndView.addObject("payAllianceCode", payAllianceCode);
		
		return modelAndView;
	}

	/**
	 * 用户提现
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(BankWithdrawDefine.CASH_MAPPING)
	public ModelAndView cash(HttpServletRequest request, HttpServletResponse response) throws IOException {
		LogUtil.startLog(BankWithdrawController.class.toString(), BankWithdrawDefine.CASH_MAPPING);
		ModelAndView modelAndView = new ModelAndView();
		// 用户id
		WebViewUser user = WebUtils.getUser(request);
		if (user == null) {
			modelAndView = new ModelAndView(BankWithdrawDefine.WITHDRAW_FALSE);
			modelAndView.addObject(BankWithdrawDefine.STATUS, BankWithdrawDefine.STATUS_FALSE);
			modelAndView.addObject(BankWithdrawDefine.ERROR, "登录失效，请重新登陆");
			return modelAndView;
		}
		Integer userId = WebUtils.getUserId(request); // 用户ID
		String userName = WebUtils.getUser(request).getUsername(); // 用户名
		String transAmt = request.getParameter("withdrawmoney");// 交易金额
		String cardNo = request.getParameter("widCard");// 提现银行卡号
		String withdrawCode = request.getParameter("withdrawCode"); // 提现验证码
//		String mobile = request.getParameter("newRegPhoneNum");
		// String bankType = request.getParameter("bankType");// 银行类型
		String payAllianceCode = request.getParameter("payAllianceCode");// 银联行号
		// 检查参数
		JSONObject checkResult = checkParam(request, userId, transAmt, cardNo, withdrawCode, user);
		if (checkResult != null) {
			modelAndView = new ModelAndView(BankWithdrawDefine.WITHDRAW_FALSE);
			modelAndView.addObject(BankWithdrawDefine.STATUS, BankWithdrawDefine.STATUS_FALSE);
			modelAndView.addObject(BankWithdrawDefine.ERROR, checkResult.getString("message"));
			return modelAndView;
		}
		
		// 如果是出借人提现需要校验短信验证码
		/*if("1".equals(user.getRoleId())){
			int smsCheck = userWithdrawService.updateCheckMobileCode(mobile, withdrawCode, "TPL_SMS_WITHDRAW", CustomConstants.CLIENT_PC, UserRegistDefine.CKCODE_YIYAN, UserRegistDefine.CKCODE_USED);
			if (smsCheck == 0) {
				modelAndView = new ModelAndView(BankWithdrawDefine.WITHDRAW_FALSE);
				modelAndView.addObject(BankWithdrawDefine.STATUS, BankWithdrawDefine.STATUS_FALSE);
				modelAndView.addObject(BankWithdrawDefine.ERROR, "验证码无效");
				return modelAndView;
			}
		}*/
		
		// 合规三期新增  by sss
        if (!this.authService.checkPaymentAuthStatus(userId)) {
            modelAndView = new ModelAndView(BankWithdrawDefine.WITHDRAW_FALSE);
            modelAndView.addObject(BankWithdrawDefine.STATUS, BankWithdrawDefine.STATUS_FALSE);
            modelAndView.addObject("message", "请先进行服务费授权!");
            modelAndView.addObject(BankWithdrawDefine.ERROR, "请先进行服务费授权");
            return modelAndView;
        }
        
		BankOpenAccount accountChinapnrTender = userWithdrawService.getBankOpenAccount(userId);
		if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getAccount())) {
			modelAndView = new ModelAndView(BankWithdrawDefine.WITHDRAW_FALSE);
			modelAndView.addObject(BankWithdrawDefine.STATUS, BankWithdrawDefine.STATUS_FALSE);
			modelAndView.addObject(BankWithdrawDefine.ERROR, "您还未开户，请开户后重新操作");
			LogUtil.endLog(THIS_CLASS, BankWithdrawDefine.CASH_MAPPING, "[用户未开户]");
			return modelAndView;
		}
		// 工行或人行
//		if (bankType.equals("7") || bankType.equals("56")) {
			if ((new BigDecimal(transAmt).compareTo(new BigDecimal(50001)) >=0) && StringUtils.isBlank(payAllianceCode)) {
				modelAndView = new ModelAndView(BankWithdrawDefine.WITHDRAW_FALSE);
				modelAndView.addObject(BankWithdrawDefine.STATUS, BankWithdrawDefine.STATUS_FALSE);
				modelAndView.addObject(BankWithdrawDefine.ERROR, "大额提现时,开户行号不能为空!");
				LogUtil.endLog(THIS_CLASS, BankWithdrawDefine.CASH_MAPPING, "[大额提现时,开户行号不能为空!]");
				return modelAndView;
			}
//		}
		// 其他银行
//		if ((new BigDecimal(transAmt).compareTo(new BigDecimal(200001)) > 0) && StringUtils.isBlank(payAllianceCode)) {
//			modelAndView = new ModelAndView(BankWithdrawDefine.WITHDRAW_FALSE);
//			modelAndView.addObject(BankWithdrawDefine.STATUS, BankWithdrawDefine.STATUS_FALSE);
//			modelAndView.addObject(BankWithdrawDefine.ERROR, "大额提现时,开户行号不能为空!");
//			LogUtil.endLog(THIS_CLASS, BankWithdrawDefine.CASH_MAPPING, "[大额提现时,开户行号不能为空!]");
//			return modelAndView;
//		}
		// 取得银行卡号
		BankCard bankCard = this.userWithdrawService.getBankInfo(userId, cardNo);
		if (bankCard == null || Validator.isNull(bankCard.getCardNo())) {
			modelAndView = new ModelAndView("redirect:" + BindCardDefine.REQUEST_MAPPING + BindCardDefine.REQUEST_MAPPING + ".do");
			return modelAndView;
		}

		// 取得手续费 默认1
		String fee = this.userWithdrawService.getWithdrawFee(userId, cardNo);

		// 实际取现金额
		// 去掉一块钱手续费
		transAmt = new BigDecimal(transAmt).subtract(new BigDecimal(Validator.isNull(fee) ? "0" : fee)).toString();
		if (new BigDecimal(transAmt).compareTo(BigDecimal.ZERO) == 0) {
			modelAndView = new ModelAndView(BankWithdrawDefine.WITHDRAW_FALSE);
			modelAndView.addObject(BankWithdrawDefine.STATUS, BankWithdrawDefine.STATUS_FALSE);
			modelAndView.addObject(BankWithdrawDefine.ERROR, "提现金额不能小于一块");
			return modelAndView;
		}
		Users users = userWithdrawService.getUsers(userId);
		UsersInfo usersInfo = userWithdrawService.getUsersInfoByUserId(userId);
		// 调用汇付接口(提现) update by jijun 20180601
		String logOrderId=GetOrderIdUtils.getOrderId2(userId);
		String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + BankWithdrawDefine.REQUEST_MAPPING +
				BankWithdrawDefine.RETURN_MAPPING + ".do?logOrderId="+logOrderId;// 支付工程路径
		String bgRetUrl = PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) + BankWithdrawDefine.REQUEST_MAPPING + BankWithdrawDefine.CALLBACK_MAPPING + ".do";// 支付工程路径
		String successfulUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + BankWithdrawDefine.REQUEST_MAPPING
				+ BankWithdrawDefine.RETURN_MAPPING + ".do?isSuccess=1&withdrawmoney=" + transAmt
				+ "&wifee=" + fee+"&logOrderId="+logOrderId;//
		// 路由代码
		String routeCode = "";
		// 调用汇付接口(4.2.2 用户绑卡接口)
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
		bean.setAccountId(accountChinapnrTender.getAccount());// 存管平台分配的账号
		bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);// 证件类型01身份证
		bean.setIdNo(usersInfo.getIdcard());// 证件号
		bean.setName(usersInfo.getTruename());// 姓名
		bean.setMobile(users.getMobile());// 手机号
		bean.setCardNo(bankCard.getCardNo());// 银行卡号
		bean.setTxAmount(CustomUtil.formatAmount(transAmt));
		bean.setTxFee(fee);
		// 成功跳转的url
		bean.setSuccessfulUrl(successfulUrl);
//		// 中行跟工行,提现金额大于五万,走人行通道,路由代码传2
//		if (bankType.equals("7") || bankType.equals("56")) {
			// 扣除手续费
			if ((new BigDecimal(transAmt).compareTo(new BigDecimal(50000)) >= 0) && StringUtils.isNotBlank(payAllianceCode)) {
				routeCode = "2";// 路由代码
				bean.setCardBankCnaps(payAllianceCode);// 绑定银行联行号
			}
//		} else if ((new BigDecimal(transAmt).compareTo(new BigDecimal(200000)) > 0) && StringUtils.isNotBlank(payAllianceCode)) {
//			// 其他银行提现金额大于20万,走人行通道,路由代码传2
//			routeCode = "2";// 路由代码
//			bean.setCardBankCnaps(payAllianceCode);// 绑定银行联行号
//		}
		if (routeCode.equals("2")) {
			bean.setRouteCode(routeCode);
			LogAcqResBean logAcq = new LogAcqResBean();
			logAcq.setPayAllianceCode(payAllianceCode);
			bean.setLogAcqResBean(logAcq);
		}
		// 企业用户提现
		if (user.getUserType() == 1) { // 企业用户 传组织机构代码
			CorpOpenAccountRecord record = userWithdrawService.getCorpOpenAccountRecord(userId);
			bean.setIdType(record.getCardType() != null ? String.valueOf(record.getCardType()) : BankCallConstant.ID_TYPE_COMCODE);// 证件类型 20：其他证件（组织机构代码）25：社会信用号
			bean.setIdNo(record.getBusiCode());
			bean.setName(record.getBusiName());
			bean.setRouteCode("2");
			bean.setCardBankCnaps(StringUtils.isEmpty(payAllianceCode) ? bankCard.getPayAllianceCode() : payAllianceCode);
		}

		bean.setForgotPwdUrl(CustomConstants.FORGET_PASSWORD_URL);
		bean.setRetUrl(retUrl);// 商户前台台应答地址(必须)
		bean.setNotifyUrl(bgRetUrl); // 商户后台应答地址(必须)
		System.out.println("提现前台回调函数：\n" + bean.getRetUrl());
		System.out.println("提现后台回调函数：\n" + bean.getNotifyUrl());
		// 插值用参数
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", String.valueOf(userId));
		params.put("userName", userName);
		params.put("ip", CustomUtil.getIpAddr(request));
		params.put("cardNo", cardNo);
		params.put("fee", CustomUtil.formatAmount(fee));
		params.put("client", "0");
		// 用户提现前处理
		int cnt = this.userWithdrawService.updateBeforeCash(bean, params);
		if (cnt > 0) {
			// 跳转到汇付天下画面
			try {
				modelAndView.addObject(BankWithdrawDefine.STATUS, BankWithdrawDefine.STATUS_TRUE);
				modelAndView = BankCallUtils.callApi(bean);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			modelAndView = new ModelAndView(BankWithdrawDefine.WITHDRAW_FALSE);
			modelAndView.addObject(BankWithdrawDefine.STATUS, BankWithdrawDefine.STATUS_FALSE);
			modelAndView.addObject(BankWithdrawDefine.ERROR, "请不要重复操作");
			return modelAndView;
		}

		LogUtil.endLog(BankWithdrawController.class.toString(), BankWithdrawDefine.CASH_MAPPING);
		return modelAndView;
	}

	/**
	 * 用户提现后处理
	 *
	 * @param request
	 * @param bean
	 * @return
	 */
	@RequestMapping(BankWithdrawDefine.RETURN_MAPPING)
	public ModelAndView cashReturn(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
		LogUtil.startLog(THIS_CLASS, BankWithdrawDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
		ModelAndView modelAndView;
		bean.convert();
		//update by jijun 20180601
//		String logOrderId = bean.getLogOrderId();
		String logOrderId=request.getParameter("logOrderId");
		Accountwithdraw accountwithdraw = userWithdrawService.getAccountWithdrawByOrdId(logOrderId);
		String isSuccess = request.getParameter("isSuccess");
		// 调用成功了
        if (isSuccess != null && "1".equals(isSuccess)) {
            if (accountwithdraw != null) {
                modelAndView = new ModelAndView(BankWithdrawDefine.WITHDRAW_SUCCESS);
                modelAndView.addObject("amt", accountwithdraw.getTotal());
                modelAndView.addObject("arrivalAmount", accountwithdraw.getCredited());
                modelAndView.addObject("fee", accountwithdraw.getFee());
                modelAndView.addObject(BankWithdrawDefine.INFO, "恭喜您，提现成功");
                return modelAndView;
            }else{
                modelAndView = new ModelAndView(BankWithdrawDefine.WITHDRAW_SUCCESS);
                modelAndView.addObject("amt", CustomUtil.formatAmount(request.getParameter("withdrawmoney")));
                modelAndView.addObject("fee", CustomUtil.formatAmount(request.getParameter("wifee")));
                modelAndView.addObject(BankWithdrawDefine.INFO, "恭喜您，提现成功");
                return modelAndView;
            }
        }
		if (accountwithdraw != null) {
			modelAndView = new ModelAndView(BankWithdrawDefine.WITHDRAW_SUCCESS);
			modelAndView.addObject("amt", accountwithdraw.getTotal());
			modelAndView.addObject("arrivalAmount", accountwithdraw.getCredited());
			modelAndView.addObject("fee", accountwithdraw.getFee());
			modelAndView.addObject(BankWithdrawDefine.INFO, "恭喜您，提现成功");
		} else {
			modelAndView = new ModelAndView(BankWithdrawDefine.WITHDRAW_INFO);
			modelAndView.addObject(BankWithdrawDefine.INFO, "银行处理中，请稍后查询交易明细");
		}
		return modelAndView;
	}

	/**
	 * 用户提现后处理
	 *
	 * @param request
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(BankWithdrawDefine.CALLBACK_MAPPING)
	public String cashCallBack(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
		LogUtil.startLog(THIS_CLASS, BankWithdrawDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
		LogUtil.debugLog(THIS_CLASS, BankWithdrawDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
		BankCallResult result = new BankCallResult();
		bean.convert();
		try {
			Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
			// 插值用参数
			Map<String, String> params = new HashMap<String, String>();
			params.put("userId", String.valueOf(userId));
			params.put("ip", CustomUtil.getIpAddr(request));
			// 执行提现后处理
			this.userWithdrawService.handlerAfterCash(bean, params);
			// 神策数据统计 add by liuyang 20180725 start
			try {
				if (BankCallStatusConstant.RESPCODE_SUCCESS.equals(bean.getRetCode()) || "CE999028".equals(bean.getRetCode())) {
					// 如果提现成功,发送神策数据统计MQ
					SensorsDataBean sensorsDataBean = new SensorsDataBean();
					sensorsDataBean.setOrderId(bean.getLogOrderId());
					sensorsDataBean.setEventCode("withdraw_result");
					sensorsDataBean.setUserId(Integer.parseInt(bean.getLogUserId()));
					this.userWithdrawService.sendSensorsDataMQ(sensorsDataBean);
				}
			}catch (Exception e){
				e.printStackTrace();
			}
			// 神策数据统计 add by liuyang 20180725 end
			LogUtil.debugLog(THIS_CLASS, BankWithdrawDefine.RETURN_MAPPING, "成功");
		} catch (Exception e) {
			LogUtil.errorLog(THIS_CLASS, BankWithdrawDefine.RETURN_MAPPING, e);
		}
		result.setStatus(true);
		LogUtil.endLog(THIS_CLASS, BankWithdrawDefine.RETURN_MAPPING, "[交易完成后,回调结束]");
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
	private JSONObject checkParam(HttpServletRequest request, Integer userId, String transAmtStr, String bankId, String withdrawCode, WebViewUser user) {
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
		if (Validator.isNull(bankId)) {
			return jsonMessage("银行卡号不正确，请确认后再次提现。", "1");
		}
		/*if("1".equals(user.getRoleId())){
			// 验证码必填
			if (Validator.isNull(withdrawCode) || !NumberUtils.isNumber(withdrawCode)) {
				return jsonMessage("短信验证码未填写或格式不正确。", "1");
			}
		}*/
		return null;
	}
}
