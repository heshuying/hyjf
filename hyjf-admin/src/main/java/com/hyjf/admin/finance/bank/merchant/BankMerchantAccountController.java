/**
 * 江西银行商户子账户
 */
package com.hyjf.admin.finance.bank.merchant;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.bank.service.user.transpassword.TransPasswordService;
import com.hyjf.mongo.dao.ChinapnrSendLogDao;
import com.hyjf.mongo.entity.ChinapnrSendlog;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.coupon.config.CouponConfigBean;
import com.hyjf.admin.coupon.config.CouponConfigDefine;
import com.hyjf.admin.manager.user.manageruser.ManageUsersDefine;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.http.URLCodec;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BankMerchantAccount;
import com.hyjf.mybatis.model.auto.BankMerchantAccountInfo;
import com.hyjf.mybatis.model.auto.BankMerchantAccountList;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallParamConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Controller
@RequestMapping(value = BankMerchantAccountDefine.REQUEST_MAPPING)
public class BankMerchantAccountController extends BaseController {
	
	Logger _log = LoggerFactory.getLogger(BankMerchantAccountController.class);

	@Autowired
	private BankMerchantAccountService bankMerchantAccountService;

	@Autowired
	private TransPasswordService transPasswordService;


	/**
	 * 商户子账户列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankMerchantAccountDefine.INIT)
	@RequiresPermissions(BankMerchantAccountDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(BankMerchantAccountDefine.ACCOUNT_LIST_FORM) BankMerchantAccountListBean form) {

		LogUtil.startLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BankMerchantAccountDefine.ACCOUNT_LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.INIT);
		return modelAndView;
	}

	/**
	 * 转账列表分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BankMerchantAccountListBean form) {
		// 账户余额总计
		BigDecimal accountBalanceSum = BigDecimal.ZERO;
		// 可用余额总计
		BigDecimal availableBalanceSum = BigDecimal.ZERO;
		// 冻结金额总计
		BigDecimal frostSum = BigDecimal.ZERO;
		int recordTotal = this.bankMerchantAccountService.queryRecordTotal(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			List<BankMerchantAccount> recordList = this.bankMerchantAccountService.selectRecordList(form, paginator.getOffset(), paginator.getLimit());
			// 更新数据
			recordList = this.bankMerchantAccountService.updateBankMerchantAccount(recordList);
			// 返回页面
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			// 算统计数据
			for (int i = 0; i < recordTotal; i++) {
				BankMerchantAccount account = recordList.get(i);
				accountBalanceSum = accountBalanceSum.add(account.getAccountBalance());
				availableBalanceSum = availableBalanceSum.add(account.getAvailableBalance());
				frostSum = frostSum.add(account.getFrost());
			}
		}
		form.setAccountBalanceSum(String.valueOf(accountBalanceSum));
		form.setAvailableBalanceSum(String.valueOf(availableBalanceSum));
		form.setFrostSum(String.valueOf(frostSum));
		modelAndView.addObject(BankMerchantAccountDefine.ACCOUNT_LIST_FORM, form);
	}

	/**
	 * 用户同步余额
	 */
	@ResponseBody
	@RequestMapping(value = BankMerchantAccountDefine.SYNBALANCE, produces = "application/json; charset=utf-8")
	public JSONObject synbalance(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.SYNBALANCE);

		JSONObject ret = new JSONObject();

		String accountCode = request.getParameter("accountCode");

		// 账户可用余额
		BigDecimal balance = BigDecimal.ZERO;
		// 账户冻结金额
		BigDecimal frost = BigDecimal.ZERO;
		// 账面余额
		BigDecimal currBalance = BigDecimal.ZERO;
		System.out.println("相应的登陆，用户userId："+ShiroUtil.getLoginUserId());
		BankCallBean bean = new BankCallBean();
		// 获取共同参数
		String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
		String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
		String channel = BankCallConstant.CHANNEL_PC;
		bean.setVersion(BankCallConstant.VERSION_10);// 版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_BALANCE_QUERY);// 交易代码
		bean.setInstCode(instCode);// 机构代码
		bean.setBankCode(bankCode);
		bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		bean.setChannel(channel); // 交易渠道
		bean.setAccountId(accountCode);// 电子账号
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(Integer.valueOf(ShiroUtil.getLoginUserId())));// 订单号
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserId(String.valueOf(ShiroUtil.getLoginUserId()));
		bean.setLogClient(0);// 平台
		try {
			BankCallBean resultBean = BankCallUtils.callApiBg(bean);
			if (resultBean == null) {
				ret.put("status", 1);
				ret.put("message", "更新发生错误,请重新操作!");
				LogUtil.errorLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.SYNBALANCE, new Exception("调用银行接口接口发生错误"));
				return ret;
			}
			if (resultBean != null && BankCallStatusConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
				// 账户余额
				balance = new BigDecimal(resultBean.getAvailBal().replace(",", ""));
				// 账面余额
				currBalance = new BigDecimal(resultBean.getCurrBal().replace(",", ""));
				// 账户冻结金额
				frost = currBalance.subtract(balance);
			} else {
				LogUtil.errorLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.SYNBALANCE, new Exception("调用银行接口接口发生错误"));
				ret.put("status", 1);
				ret.put("message", "更新发生错误,请重新操作!");
				return ret;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		int cnt = 0;

		try {
			// 更新处理
			cnt = this.bankMerchantAccountService.updateBankMerchantAccount(accountCode, currBalance, balance, frost);
		} catch (Exception e) {
			LogUtil.errorLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.SYNBALANCE, e);
		}

		// 返现成功
		if (cnt > 0) {
			// 成功
			ret.put("status", 0);
			ret.put("message", "更新成功");
		} else {
			// 成功
			ret.put("status", 1);
			ret.put("message", "更新发生错误,请重新操作!");
		}

		LogUtil.startLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.SYNBALANCE);
		return ret;
	}

	/**
	 * 商户子账户列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankMerchantAccountDefine.WITHDRAWALS_INIT)
	public ModelAndView withdrawalsInit(HttpServletRequest request, RedirectAttributes attr) {
		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		LogUtil.startLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.WITHDRAWALS_INIT);
		ModelAndView modelAndView = new ModelAndView(BankMerchantAccountDefine.WITHDRAWALS_INIT_PATH);
		String accountCode = request.getParameter("accountCode");
		BankMerchantAccount bankMerchantAccount = bankMerchantAccountService.getBankMerchantAccount(accountCode);
		modelAndView.addObject("availableBalance", df.format(bankMerchantAccount.getAvailableBalance()));
		modelAndView.addObject("account", accountCode);
		// 显示手机号
        BankMerchantAccountInfo info=bankMerchantAccountService.getBankMerchantAccountInfoByCode(accountCode);
        modelAndView.addObject("info", info);
		LogUtil.endLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.WITHDRAWALS_INIT);
		return modelAndView;
	}

	/**
	 * 交易提现信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(BankMerchantAccountDefine.CHECK_ACTION)
	public String checkAction(HttpServletRequest request, RedirectAttributes attr) {

		LogUtil.startLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.CHECK_ACTION);
		JSONObject ret = new JSONObject();
		String param = request.getParameter("param");
		String accountCode = request.getParameter("accountCode");
		String msg = bankMerchantAccountService.check(param, accountCode);
		if (msg != null && msg.length() != 0) {
			ret.put(BankMerchantAccountDefine.JSON_VALID_INFO_KEY, msg);
			return ret.toString();
		}
		ret.put(BankMerchantAccountDefine.JSON_VALID_STATUS_KEY, BankMerchantAccountDefine.JSON_VALID_STATUS_OK);
		LogUtil.endLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.CHECK_ACTION);
		return ret.toString();
	}

	/**
	 * 提现
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankMerchantAccountDefine.WITHDRAWALS)
	public ModelAndView withdrawals(HttpServletRequest request, CouponConfigBean form) {
	    _log.info(ShiroUtil.getLoginUsername()+ " 开始操作体现");
	    
        ModelAndView modelAndView = new ModelAndView(BankMerchantAccountDefine.WITHDRAWALS_INIT_PATH);
        DecimalFormat df = CustomConstants.DF_FOR_VIEW;
        String accountCode = request.getParameter("accountCode");
        BankMerchantAccount bankMerchantAccount = bankMerchantAccountService.getBankMerchantAccount(accountCode);
        String forgotPwdUrl="";
        if(bankMerchantAccount.getIsSetPassword()==0){
            forgotPwdUrl=PropUtils.getSystem(CustomConstants.HYJF_ADMIN_URL) + BankMerchantAccountDefine.REQUEST_MAPPING
            + BankMerchantAccountDefine.SETPASSWORD_ACTION + ".do?accountCode="+accountCode ;
        }else{
            forgotPwdUrl=PropUtils.getSystem(CustomConstants.HYJF_ADMIN_URL) + BankMerchantAccountDefine.REQUEST_MAPPING
                    + BankMerchantAccountDefine.RESETPASSWORD_ACTION + ".do?accountCode="+accountCode ;
        }
        modelAndView.addObject("availableBalance", df.format(bankMerchantAccount.getAvailableBalance()));
        modelAndView.addObject("account", accountCode);

        
        BankMerchantAccountInfo info=bankMerchantAccountService.getBankMerchantAccountInfoByCode(accountCode);
        String transAmt = request.getParameter("amount");// 交易金额

		// 取得手续费 默认1
		String fee = "0";
		// 实际取现金额
/*		 去掉一块钱手续费
		transAmt = new BigDecimal(transAmt).subtract(new BigDecimal(Validator.isNull(fee) ? "0" : fee)).toString();*/
		if (new BigDecimal(transAmt).compareTo(BigDecimal.ZERO) == 0) {
            modelAndView = new ModelAndView(BankMerchantAccountDefine.WITHDRAW_FALSE);
            modelAndView.addObject("info", "提现金额不能小于一块");
            return modelAndView;
		}
        
        String bankId = "";// 提现银行卡号
        String userName = ""; // 用户名
        String mobile="";
        String idNo="";
        String idType="";
        String userId ="0"; // 后续待优化，目前先用0代表，平台用户体现应该插入别的表 hyjf_bank_merchant_account_list
        if(info!=null){
            //服务费账户
            mobile=info.getMobile();
            idNo=info.getIdNo();
            idType=info.getIdType();
            userName = info.getAccountName(); // 用户名
            bankId = info.getBankCard();// 提现银行卡号
        } else{
            modelAndView = new ModelAndView(BankMerchantAccountDefine.WITHDRAW_FALSE);
            modelAndView.addObject("info", "账户不存在");
            return modelAndView;
        }
        
        // 调用汇付接口(提现)
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_ADMIN_URL) + BankMerchantAccountDefine.REQUEST_MAPPING + BankMerchantAccountDefine.RETURN_MAPPING + ".do";// 支付工程路径
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_ADMIN_URL) + BankMerchantAccountDefine.REQUEST_MAPPING + BankMerchantAccountDefine.CALLBACK_MAPPING + ".do";// 支付工程路径

        // 调用汇付接口(4.2.2 用户绑卡接口)
        BankCallBean bean = new BankCallBean();
        bean.setLogOrderId(GetOrderIdUtils.getOrderId2(0));
        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
        bean.setLogUserId("0");
        bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_CREDIT_FOR_UNLOAD_PAGE);
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        // 银行存管
        bean.setTxCode(BankCallMethodConstant.TXCODE_CREDIT_FOR_UNLOAD_PAGE);
        bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
        bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
        bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
        bean.setAccountId(accountCode);// 存管平台分配的账号
        bean.setCurrency(BankCallConstant.CURRENCY_TYPE_RMB);
        bean.setCardNo(bankId);// 银行卡号
        bean.setIdType(idType);// 证件类型25组织机构代码
        bean.setIdNo(idNo);// 证件号
        bean.setName(userName);// 姓名
        bean.setMobile(mobile);// 手机号
        
        bean.setTxAmount(CustomUtil.formatAmount(transAmt));
        bean.setTxFee(fee);
        bean.setForgotPwdUrl(forgotPwdUrl);
        bean.setRetUrl(retUrl);// 商户前台台应答地址(必须)
        bean.setNotifyUrl(bgRetUrl); // 商户后台应答地址(必须)
        
        bean.setRouteCode("2"); // 2-人行大额通道
        bean.setCardBankCnaps("313421081204"); // routeCode=2，必输 南昌银行铁路支行
        
        System.out.println("提现前台回调函数：\n" + bean.getRetUrl());
        System.out.println("提现后台回调函数：\n" + bean.getNotifyUrl());
        LogAcqResBean logAcq = new LogAcqResBean();
        
        logAcq.setCardNo(accountCode);
        logAcq.setFee("0");
        bean.setLogAcqResBean(logAcq);
        // 插值用参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userId);
        params.put("userName", userName);
        params.put("ip", CustomUtil.getIpAddr(request));
        params.put("bankId", bankId);
        params.put("fee", CustomUtil.formatAmount("0"));
        params.put("client", "0");
        // 用户提现前处理
        int cnt = this.bankMerchantAccountService.updateBeforeCash(bean, params,bankMerchantAccount);
        if (cnt > 0) {
            // 跳转到江西银行画面
            try {
                modelAndView.addObject("status", true);
                modelAndView = BankCallUtils.callApi(bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            modelAndView = new ModelAndView(BankMerchantAccountDefine.WITHDRAW_FALSE);
            modelAndView.addObject("status", false);
            modelAndView.addObject("error", "请不要重复操作");
            return modelAndView;
        }
        // 跳转页面用（info里面有）
        modelAndView.addObject(CouponConfigDefine.SUCCESS, CouponConfigDefine.SUCCESS);
        LogUtil.endLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.WITHDRAWALS);
        return modelAndView;
	}

	/**
	 * 用户提现后处理
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(BankMerchantAccountDefine.RETURN_MAPPING)
	public ModelAndView cashReturn(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
		LogUtil.startLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
		ModelAndView modelAndView;
		bean.convert();
		String logOrderId = bean.getLogOrderId();
		BankMerchantAccountList accountwithdraw = bankMerchantAccountService.getAccountWithdrawByOrdId(logOrderId);
		if (accountwithdraw != null) {
			modelAndView = new ModelAndView(BankMerchantAccountDefine.WITHDRAW_SUCCESS);
			modelAndView.addObject("amt", accountwithdraw.getAmount());
			modelAndView.addObject("info", "恭喜您，提现成功");
		} else {
			modelAndView = new ModelAndView(BankMerchantAccountDefine.WITHDRAW_INFO);
			modelAndView.addObject("info", "银行处理中，请稍后查询交易明细");
		}

		return modelAndView;
	}

	/**
	 * 用户提现后处理
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(BankMerchantAccountDefine.CALLBACK_MAPPING)
	@ResponseBody
	public BankCallResult cashCallBack(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
		_log.info(bean.getLogOrderId()+ " 公司账户提现回调开始");
		BankCallResult result = new BankCallResult();
		bean.convert();
		LogUtil.debugLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");

		// 发送状态
		String status = BankCallStatusConstant.STATUS_VERTIFY_OK;

		// 成功或审核中或提现失败
		if (BankCallStatusConstant.RESPCODE_SUCCESS.equals(bean.getRetCode()) || "CE999028".equals(bean.getRetCode())) {
			try {
				Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
				// 插值用参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("userId", String.valueOf(userId));
				params.put("ip", CustomUtil.getIpAddr(request));

				// 执行提现后处理
				this.bankMerchantAccountService.handlerAfterCash(bean, params);
				// 执行结果(成功)
				if (BankCallStatusConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
					status = BankCallStatusConstant.STATUS_SUCCESS;
				} else {
					status = BankCallStatusConstant.STATUS_FAIL;
				}
				_log.info(bean.getLogOrderId()+ " 成功");
			} catch (Exception e) {
				// 执行结果(失败)
				_log.error("公司账户提现回调"+e.getMessage());
			}

		} else {
			_log.info("公司账户提现失败 "+bean.get(BankCallParamConstant.PARAM_RETCODE)); 
			// 执行结果(失败)
			status = BankCallStatusConstant.STATUS_FAIL;
			// 更新提现失败原因
			String reason = bean.getRetMsg();
			if (StringUtils.isNotEmpty(reason)) {
				if (reason.contains("%")) {
					reason = URLCodec.decodeURL(reason);
				}
			}
			if (StringUtils.isNotEmpty(bean.getLogOrderId())) {
				this.bankMerchantAccountService.updateAccountWithdrawByOrdId(bean.getLogOrderId(), reason);
			}
			_log.info(bean.getLogOrderId()+ " 失败");
		}

		if (BankCallStatusConstant.STATUS_SUCCESS.equals(status)) {
			result.setStatus(true);
			result.setMessage("恭喜您，提现成功");
		} else {
			result.setStatus(true);
			result.setMessage("银行处理中，请稍后查询交易明细");
		}
		return result;
	}

	
	/**
	 * 初始化红包发放
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankMerchantAccountDefine.INIT_POCKET_SEND_ACTION)
	@RequiresPermissions(BankMerchantAccountDefine.PERMISSIONS_REDPOCKETSEND)
	public ModelAndView initPocketSendAction(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView(BankMerchantAccountDefine.RED_POCKET_SEND_PATH);
		return modelAndView;
	}
	/**
	 * 发红包
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankMerchantAccountDefine.RED_POCKET_SEND_ACTION)
	@RequiresPermissions(BankMerchantAccountDefine.PERMISSIONS_REDPOCKETSEND)
	public ModelAndView redPocketSendAction(HttpServletRequest request, @ModelAttribute RedPocketBean form) {

		ModelAndView modelAndView = new ModelAndView(BankMerchantAccountDefine.RED_POCKET_SEND_PATH);
		LogUtil.startLog(this.getClass().getName(), BankMerchantAccountDefine.RED_POCKET_SEND_ACTION);
		// 查询商户子账户余额
		String merrpAccount = PropUtils.getSystem(BankCallConstant.BANK_MERRP_ACCOUNT);
		BigDecimal bankBalance = bankMerchantAccountService.getBankBalance(Integer.parseInt(ShiroUtil.getLoginUserId()), merrpAccount);
		// 画面验证
		this.validatorFieldCheck(modelAndView, form, bankBalance);
		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject(BankMerchantAccountDefine.RED_POCKET_SEND_FORM, form);
			LogUtil.errorLog(ManageUsersDefine.THIS_CLASS, BankMerchantAccountDefine.RED_POCKET_SEND_ACTION, "输入内容验证失败", null);
			return modelAndView;
		}
		// IP地址
		String ip = CustomUtil.getIpAddr(request);
		String orderId = GetOrderIdUtils.getOrderId2(Integer.valueOf(ShiroUtil.getLoginUserId()));
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_VOUCHER_PAY);// 交易代码
		bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
		bean.setAccountId(merrpAccount);// 电子账号
		bean.setTxAmount(form.getAmount());
		bean.setForAccountId(form.getUserAccount());
		bean.setDesLineFlag("1");
		bean.setDesLine(orderId);
		bean.setLogOrderId(orderId);// 订单号
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserId(String.valueOf(ShiroUtil.getLoginUserId()));
		bean.setLogClient(0);// 平台
		bean.setLogIp(ip);
		BankCallBean resultBean;
		try {
			resultBean = BankCallUtils.callApiBg(bean);
		} catch (Exception e) {
			e.printStackTrace();
			ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "amount", "null", "请求红包接口失败");
			return modelAndView;
		}
		if (Validator.isNotNull(resultBean) && BankCallConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
			modelAndView.addObject(ManageUsersDefine.SUCCESS, ManageUsersDefine.SUCCESS);
			return modelAndView;
		}
		ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "amount", "null", "调用红包接口发生错误");
		return modelAndView;
	}

	/**
	 * 校验相应的银行账户金额
	 * 
	 * @param modelAndView
	 * @param form
	 * @param bankBalance
	 */
	private void validatorFieldCheck(ModelAndView modelAndView, RedPocketBean form, BigDecimal bankBalance) {

		String amount = form.getAmount();
		String userAccount = form.getUserAccount();
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "amount", amount);
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "userAccount", userAccount);

	}
	
	
	
	/**
     * 设置交易密码
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BankMerchantAccountDefine.SETPASSWORD_ACTION)
    public ModelAndView setPassword(HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.SETPASSWORD_ACTION);
        ModelAndView modelAndView = new ModelAndView();

        String accountCode = request.getParameter("accountCode");
        BankMerchantAccount bankMerchantAccount = bankMerchantAccountService.getBankMerchantAccount(accountCode);
        
        
        // 判断用户是否设置过交易密码
        Integer passwordFlag = bankMerchantAccount.getIsSetPassword();
        if (passwordFlag == 1) {// 已设置交易密码
            modelAndView = new ModelAndView(BankMerchantAccountDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "已设置交易密码");
            return modelAndView;
        }
        BankMerchantAccountInfo info=bankMerchantAccountService.getBankMerchantAccountInfoByCode(accountCode);
        
        String userName="";
        String mobile="";
        String idNo="";
        String idType="";
        if(info!=null){
            //服务费账户
            mobile=info.getMobile();
            idNo=info.getIdNo();
            idType=info.getIdType();
            userName = info.getAccountName(); // 用户名
        } else{
            modelAndView = new ModelAndView(BankMerchantAccountDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "账户不存在");
            return modelAndView;
        }
        // 同步调用路径
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_ADMIN_URL) + BankMerchantAccountDefine.REQUEST_MAPPING
                + BankMerchantAccountDefine.RETURL_SYN_PASSWORD_ACTION + ".do";
        // 异步调用路
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_ADMIN_URL) + BankMerchantAccountDefine.REQUEST_MAPPING
                + BankMerchantAccountDefine.RETURN_ASY_PASSWORD_ACTION + ".do";
        // 调用设置密码接口
        BankCallBean bean = new BankCallBean();
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxCode(BankCallConstant.TXCODE_PASSWORD_RESET_PAGE);// 消息类型(用户开户)
        bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
        bean.setTxDate(GetOrderIdUtils.getTxDate());
        bean.setTxTime(GetOrderIdUtils.getTxTime());
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
        bean.setChannel(BankCallConstant.CHANNEL_PC);

        
        bean.setIdType(idType);
        bean.setIdNo(idNo);
        bean.setName(userName);

        bean.setAccountId(accountCode);// 电子账号
        bean.setMobile(mobile);
        bean.setRetUrl(retUrl);// 页面同步返回 URL
        bean.setNotifyUrl(bgRetUrl);// 页面异步返回URL(必须)
        // 商户私有域，存放开户平台,用户userId
        LogAcqResBean acqRes = new LogAcqResBean();
        acqRes.setUserId(40);
        bean.setLogAcqResBean(acqRes);
        // 操作者ID
        bean.setLogUserId(String.valueOf(40));
        bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_PASSWORDRESETPAGE);
        bean.setLogOrderId(GetOrderIdUtils.getUsrId(40));
        // 跳转到江西银行画面
        try {
            modelAndView = BankCallUtils.callApi(bean);
            LogUtil.endLog(BankMerchantAccountDefine.class.toString(), BankMerchantAccountDefine.SETPASSWORD_ACTION);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView = new ModelAndView(BankMerchantAccountDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "调用银行接口失败！");
            LogUtil.errorLog(BankMerchantAccountDefine.class.toString(), BankMerchantAccountDefine.SETPASSWORD_ACTION, e);
        }
        return modelAndView;
    }



    /**
     * 设置交易密码同步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(BankMerchantAccountDefine.RETURL_SYN_PASSWORD_ACTION)
    public ModelAndView passwordReturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {

        LogUtil.startLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.RETURL_SYN_PASSWORD_ACTION, "[开户同步回调开始]");
        ModelAndView modelAndView = new ModelAndView();
        bean.convert();

        // 返回失败
        if (bean.getRetCode()!=null&&!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
            modelAndView = new ModelAndView(BankMerchantAccountDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "交易密码设置失败,失败原因：" + bankMerchantAccountService.getBankRetMsg(bean.getRetCode()));
            return modelAndView;
        }
        // 判断用户是否设置了交易密码
        BankMerchantAccount bankMerchantAccount = bankMerchantAccountService.getBankMerchantAccount(bean.getAccountId());
        boolean flag = bankMerchantAccount.getIsSetPassword() == 1 ? true : false;
        if (flag) {
            modelAndView = new ModelAndView(BankMerchantAccountDefine.PASSWORD_SUCCESS_PATH);
            modelAndView.addObject("message", "交易密码设置成功");
            return modelAndView;
        }
        
        // 调用查询电子账户密码是否设置
        BankCallBean selectbean = new BankCallBean();
        selectbean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        selectbean.setTxCode(BankCallConstant.TXCODE_PASSWORD_SET_QUERY);
        selectbean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        selectbean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
        selectbean.setTxDate(GetOrderIdUtils.getTxDate());
        selectbean.setTxTime(GetOrderIdUtils.getTxTime());
        selectbean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
        selectbean.setChannel(BankCallConstant.CHANNEL_PC);
        selectbean.setAccountId(String.valueOf(bean.getAccountId()));// 电子账号

        // 操作者ID
        selectbean.setLogUserId(String.valueOf(40));
        selectbean.setLogOrderId(GetOrderIdUtils.getUsrId(40));
        selectbean.setLogClient(0);
        // 返回参数
        BankCallBean retBean = null;
        // 调用接口
        retBean = BankCallUtils.callApiBg(selectbean);
        LogUtil.endLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.RETURL_SYN_PASSWORD_ACTION);
        
        try {
            if("1".equals(retBean.getPinFlag())){
             // 是否设置密码
                this.bankMerchantAccountService.updateBankMerchantAccountIsSetPassword(bean.getAccountId(), 1);
                modelAndView = new ModelAndView(BankMerchantAccountDefine.PASSWORD_SUCCESS_PATH);
                modelAndView.addObject("message", "交易密码设置成功");
                return modelAndView;
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.errorLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.RETURN_ASY_PASSWORD_ACTION, e);
        }

        modelAndView = new ModelAndView(BankMerchantAccountDefine.PASSWORD_ERROR_PATH);
        modelAndView.addObject("message", "交易密码设置失败");
        LogUtil.endLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.RETURL_SYN_PASSWORD_ACTION, "[交易完成后,回调结束]");
        return modelAndView;
    }

    /**
     * 设置交易密码异步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(BankMerchantAccountDefine.RETURN_ASY_PASSWORD_ACTION)
    public String passwordBgreturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {
        BankCallResult result = new BankCallResult();
        LogUtil.startLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.RETURN_ASY_PASSWORD_ACTION, "[开户异步回调开始]");
        bean.convert();
        BankMerchantAccount bankMerchantAccount = bankMerchantAccountService.getBankMerchantAccount(bean.getAccountId());

        // 成功或审核中
        if (bankMerchantAccount != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE))) {
            this.bankMerchantAccountService.updateBankMerchantAccountIsSetPassword(bean.getAccountId(), 1);
        }
        LogUtil.endLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.RETURN_ASY_PASSWORD_ACTION, "[交易完成后,回调结束]");
        result.setMessage("交易密码设置成功");
        result.setStatus(true);
        return JSONObject.toJSONString(result, true);
    }

    /**
     * 重置交易密码
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BankMerchantAccountDefine.RESETPASSWORD_ACTION)
    public ModelAndView resetPassword(HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.RESETPASSWORD_ACTION);
        ModelAndView modelAndView = new ModelAndView();
        String accountCode = request.getParameter("accountCode");
        BankMerchantAccount bankMerchantAccount = bankMerchantAccountService.getBankMerchantAccount(accountCode);
        
        
        // 判断用户是否设置过交易密码
        Integer passwordFlag = bankMerchantAccount.getIsSetPassword();
        if (passwordFlag == 0) {// 未设置交易密码
            modelAndView = new ModelAndView(BankMerchantAccountDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "请先设置交易密码");
            return modelAndView;
        }
        BankMerchantAccountInfo info=bankMerchantAccountService.getBankMerchantAccountInfoByCode(accountCode);
        String userName="";
        String mobile="";
        String idNo="";
        String idType="";
        if(info!=null){
            //服务费账户
            mobile=info.getMobile();
            idNo=info.getIdNo();
            idType=info.getIdType();
            userName = info.getAccountName(); // 用户名
        } else{
            modelAndView = new ModelAndView(BankMerchantAccountDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "账户不存在");
            return modelAndView;
        }

        // 同步调用路径
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_ADMIN_URL) + BankMerchantAccountDefine.REQUEST_MAPPING
                + BankMerchantAccountDefine.RETURL_SYN_RESETPASSWORD_ACTION + ".do";
        // 异步调用路
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_ADMIN_URL) + BankMerchantAccountDefine.REQUEST_MAPPING
                + BankMerchantAccountDefine.RETURN_ASY_RESETPASSWORD_ACTION + ".do";

        // 调用设置密码接口
        BankCallBean bean = new BankCallBean();
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxCode(BankCallConstant.TXCODE_PASSWORD_RESET_PAGE);// 消息类型(用户开户)
        bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
        bean.setTxDate(GetOrderIdUtils.getTxDate());
        bean.setTxTime(GetOrderIdUtils.getTxTime());
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
        bean.setChannel(BankCallConstant.CHANNEL_PC);
        bean.setIdType(idType);
        bean.setIdNo(idNo);
        bean.setName(userName);      
        bean.setAccountId(accountCode);// 电子账号
        bean.setMobile(mobile);

        bean.setNotifyUrl(bgRetUrl);// 页面异步返回URL(必须)
        // 商户私有域，存放开户平台,用户userId
        LogAcqResBean acqRes = new LogAcqResBean();
        acqRes.setUserId(40);
        bean.setLogAcqResBean(acqRes);
        // 操作者ID
        bean.setLogUserId(String.valueOf(40));
        bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_PASSWORDRESETPAGE);
        bean.setLogOrderId(GetOrderIdUtils.getUsrId(40));
        bean.setRetUrl(retUrl + "?ordid=" + bean.getLogOrderId());// 页面同步返回 URL
        // 跳转到江西银行画面
        try {
            modelAndView = BankCallUtils.callApi(bean);
            LogUtil.endLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.RESETPASSWORD_ACTION);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView = new ModelAndView(BankMerchantAccountDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "调用银行接口失败！");
            LogUtil.errorLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.RESETPASSWORD_ACTION, e);
        }
        return modelAndView;
    }

    /**
     * 重置交易密码同步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(BankMerchantAccountDefine.RETURL_SYN_RESETPASSWORD_ACTION)
    public ModelAndView resetPasswordReturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {

        LogUtil.startLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.RETURL_SYN_RESETPASSWORD_ACTION,
                "[重置交易密码同步回调开始]");
        ModelAndView modelAndView = new ModelAndView();
		//add by cwyang 防止同步比异步快
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String ordid = request.getParameter("ordid");
		//add by cwyang 查询银行设置交易密码是否成功
		boolean backIsSuccess = transPasswordService.backLogIsSuccess(ordid);
        // 返回失败
        if ((bean.getRetCode()!=null&&!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) || !backIsSuccess) {
            modelAndView = new ModelAndView(BankMerchantAccountDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "交易密码修改失败,失败原因：" + bankMerchantAccountService.getBankRetMsg(bean.getRetCode()));
            return modelAndView;
        }
        
        modelAndView = new ModelAndView(BankMerchantAccountDefine.PASSWORD_SUCCESS_PATH); // 重置密码如何判断是否重置
        modelAndView.addObject("message", "交易密码修改成功");
        LogUtil.endLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.RETURL_SYN_RESETPASSWORD_ACTION,
                "[重置交易密码同步回调结束]");
        return modelAndView;
    }

    /**
     * 重置交易密码异步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(BankMerchantAccountDefine.RETURN_ASY_RESETPASSWORD_ACTION)
    public String resetPasswordBgreturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {
        BankCallResult result = new BankCallResult();
        LogUtil.startLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.RETURN_ASY_RESETPASSWORD_ACTION,
                "[重置交易密码回调开始]");

        LogUtil.endLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.RETURN_ASY_RESETPASSWORD_ACTION,
                "[重置交易密码回调结束]");
        result.setMessage("交易密码修改成功");
        result.setStatus(true);
        return JSONObject.toJSONString(result, true);
    }
	
    /**
     * 
     * 圈存弹出框
     * @author sunss
     * @param request
     * @param attr
     * @return
     */
    @RequestMapping(BankMerchantAccountDefine.RECHARGE_INIT)
    public ModelAndView rechargeInit(HttpServletRequest request, RedirectAttributes attr) {
        DecimalFormat df = CustomConstants.DF_FOR_VIEW;
        LogUtil.startLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.RECHARGE_INIT);
        ModelAndView modelAndView = new ModelAndView(BankMerchantAccountDefine.RECHARGE_INIT_PATH);
        String accountCode = request.getParameter("accountCode");
        BankMerchantAccount bankMerchantAccount = bankMerchantAccountService.getBankMerchantAccount(accountCode);
        modelAndView.addObject("availableBalance", df.format(bankMerchantAccount.getAvailableBalance()));
        modelAndView.addObject("account", accountCode);
        // 显示手机号
        BankMerchantAccountInfo info=bankMerchantAccountService.getBankMerchantAccountInfoByCode(accountCode);
        modelAndView.addObject("info", info);
        LogUtil.endLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.RECHARGE_INIT);
        return modelAndView;
    }
    
    /**
     * 
     * 圈存
     * @author sunss
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BankMerchantAccountDefine.TO_RECHARGE)
    public ModelAndView recharge(HttpServletRequest request, CouponConfigBean form) {
        _log.info(ShiroUtil.getLoginUsername()+ " 开始圈存操作");
        
        ModelAndView modelAndView = new ModelAndView(BankMerchantAccountDefine.RECHARGE_INIT_PATH);
        DecimalFormat df = CustomConstants.DF_FOR_VIEW;
        String accountCode = request.getParameter("accountCode");
        BankMerchantAccount bankMerchantAccount = bankMerchantAccountService.getBankMerchantAccount(accountCode);
        String forgotPwdUrl="";
        if(bankMerchantAccount.getIsSetPassword()==0){
            forgotPwdUrl=PropUtils.getSystem(CustomConstants.HYJF_ADMIN_URL) + BankMerchantAccountDefine.REQUEST_MAPPING
            + BankMerchantAccountDefine.SETPASSWORD_ACTION + ".do?accountCode="+accountCode ;
        }else{
            forgotPwdUrl=PropUtils.getSystem(CustomConstants.HYJF_ADMIN_URL) + BankMerchantAccountDefine.REQUEST_MAPPING
                    + BankMerchantAccountDefine.RESETPASSWORD_ACTION + ".do?accountCode="+accountCode ;
        }
        modelAndView.addObject("availableBalance", df.format(bankMerchantAccount.getAvailableBalance()));
        modelAndView.addObject("account", accountCode);

        
        BankMerchantAccountInfo info=bankMerchantAccountService.getBankMerchantAccountInfoByCode(accountCode);
        String transAmt = request.getParameter("amount");// 交易金额

        String bankId = "";// 充值银行卡号
        String userName = ""; // 用户名
        String mobile="";
        String idNo="";
        String idType="";
        String userId ="0"; // 后续待优化，目前先用0代表，平台用户体现应该插入别的表 hyjf_bank_merchant_account_list
        if(info!=null){
            //服务费账户
            mobile=info.getMobile();
            idNo=info.getIdNo();
            idType=info.getIdType();
            userName = info.getAccountName(); // 用户名
            bankId = info.getBankCard();// 提现银行卡号
        } else{
            modelAndView = new ModelAndView(BankMerchantAccountDefine.RECHARGE_FALSE);
            modelAndView.addObject("info", "账户不存在");
            return modelAndView;
        }

        // 调用江西银行接口(2.3.3.圈存)
        BankCallBean bean = new BankCallBean();
        bean.setLogOrderId(GetOrderIdUtils.getOrderId2(0));
        
        // 调用江西银行接口(提现)
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_ADMIN_URL) + BankMerchantAccountDefine.REQUEST_MAPPING
                + BankMerchantAccountDefine.RECHARGE_RETURN_MAPPING + ".do?logOrderId+" + bean.getLogOrderId();// 支付工程路径
        String bgRetUrl =
                PropUtils.getSystem(CustomConstants.HYJF_ADMIN_URL) + BankMerchantAccountDefine.REQUEST_MAPPING
                        + BankMerchantAccountDefine.RECHARGE_CALLBACK_MAPPING + ".do";// 支付工程路径

        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
        bean.setLogUserId("0");
        bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_TRANSFERENCE);
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        // 银行存管
        bean.setTxCode(BankCallMethodConstant.TXCODE_CREDIT_FOR_LOADPAGE);
        bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
        bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
        bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
        bean.setAccountId(accountCode);// 存管平台分配的账号
        bean.setCardNo(bankId);// 银行卡号
        bean.setCurrency("156");
        bean.setTxAmount(CustomUtil.formatAmount(transAmt));
        bean.setIdType(idType);// 证件类型25组织机构代码
        bean.setIdNo(idNo);// 证件号
        bean.setName(userName);// 姓名
        bean.setMobile(mobile);// 手机号
        bean.setForgotPwdUrl(forgotPwdUrl);
        bean.setRetUrl(retUrl);// 商户前台台应答地址(必须)
        bean.setNotifyUrl(bgRetUrl); // 商户后台应答地址(必须)
        bean.setSuccessfulUrl(retUrl+"&isSuccess=1");
        
        LogAcqResBean logAcq = new LogAcqResBean();
        
        logAcq.setCardNo(accountCode);
        bean.setLogAcqResBean(logAcq);
        // 插值用参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userId);
        params.put("userName", userName);
        params.put("ip", CustomUtil.getIpAddr(request));
        params.put("bankId", bankId);
        params.put("client", "0");
        // 当前登录用户
        params.put("loginUserid", ShiroUtil.getLoginUserId());
        // 用户充值前处理
        int cnt = this.bankMerchantAccountService.updateBeforeRecharge(bean, params,bankMerchantAccount);
        if (cnt > 0) {
            // 跳转到江西银行画面
            try {
                modelAndView.addObject("status", true);
                modelAndView = BankCallUtils.callApi(bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            modelAndView = new ModelAndView(BankMerchantAccountDefine.RECHARGE_FALSE);
            modelAndView.addObject("status", false);
            modelAndView.addObject("error", "请不要重复操作");
            return modelAndView;
        }
        // 跳转页面用（info里面有）
        modelAndView.addObject(CouponConfigDefine.SUCCESS, CouponConfigDefine.SUCCESS);
        LogUtil.endLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.TO_RECHARGE);
        return modelAndView;
    }
    
    /**
     * 圈存同步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(BankMerchantAccountDefine.RECHARGE_RETURN_MAPPING)
    public ModelAndView rechargeReturn(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
        LogUtil.startLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.RECHARGE_RETURN_MAPPING, "[交易完成后,回调开始]");
        ModelAndView modelAndView;
        String isSuccess = request.getParameter("isSuccess");
        String logOrderId = request.getParameter("logOrderId");
        // 成功
        if (isSuccess != null && "1".equals(isSuccess)) {
            modelAndView = new ModelAndView(BankMerchantAccountDefine.RECHARGE_SUCCESS);
            modelAndView.addObject("info", "恭喜您，充值成功");
            return modelAndView;
        }
        BankMerchantAccountList accountwithdraw = bankMerchantAccountService.getAccountWithdrawByOrdId(logOrderId);
        if (accountwithdraw != null) {
            modelAndView = new ModelAndView(BankMerchantAccountDefine.RECHARGE_SUCCESS);
            modelAndView.addObject("amt", accountwithdraw.getAmount());
            modelAndView.addObject("info", "恭喜您，充值成功");
        } else {
            modelAndView = new ModelAndView(BankMerchantAccountDefine.RECHARGE_FALSE);
            modelAndView.addObject("info", "充值失败");
        }

        return modelAndView;
    }

    /**
     * 圈存异步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(BankMerchantAccountDefine.RECHARGE_CALLBACK_MAPPING)
    @ResponseBody
    public BankCallResult rechargeCallBack(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
        _log.info(bean.getLogOrderId()+ " 公司账户充值回调开始");
        BankCallResult result = new BankCallResult();
        bean.convert();
        LogUtil.debugLog(BankMerchantAccountDefine.THIS_CLASS, BankMerchantAccountDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");

        // 发送状态
        String status = BankCallStatusConstant.STATUS_VERTIFY_OK;

        // 成功或审核中或提现失败
        if (BankCallStatusConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
            try {
                Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
                // 插值用参数
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", String.valueOf(userId));
                params.put("ip", CustomUtil.getIpAddr(request));

                // 执行充值后处理
                this.bankMerchantAccountService.handlerAfterRecharge(bean, params);
                // 执行结果(成功)
                if (BankCallStatusConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
                    status = BankCallStatusConstant.STATUS_SUCCESS;
                } else {
                    status = BankCallStatusConstant.STATUS_FAIL;
                }
                _log.info(bean.getLogOrderId()+ " 成功");
            } catch (Exception e) {
                // 执行结果(失败)
                _log.error("公司账户提现回调"+e.getMessage());
            }

        } else {
            _log.info("公司账户提现失败 "+bean.get(BankCallParamConstant.PARAM_RETCODE)); 
            // 执行结果(失败)
            status = BankCallStatusConstant.STATUS_FAIL;
            // 更新提现失败原因
            String reason = bean.getRetMsg();
            if (StringUtils.isNotEmpty(reason)) {
                if (reason.contains("%")) {
                    reason = URLCodec.decodeURL(reason);
                }
            }
            if (StringUtils.isNotEmpty(bean.getLogOrderId())) {
                this.bankMerchantAccountService.updateAccountWithdrawByOrdId(bean.getLogOrderId(), reason);
            }
            _log.info(bean.getLogOrderId()+ " 失败");
        }

        if (BankCallStatusConstant.STATUS_SUCCESS.equals(status)) {
            result.setStatus(true);
            result.setMessage("恭喜您，提现成功");
        } else {
            result.setStatus(false);
            result.setMessage("银行处理中，请稍后查询交易明细");
        }
        return result;
    }
}
