/**
 * Description:用户提现
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: GOGTZ-T
 * @version: 1.0
 *           Created at: 2015年12月4日 下午2:32:36
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.web.user.withdraw;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.http.URLCodec;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountBank;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogWithBLOBs;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.web.BaseController;
import com.hyjf.web.chinapnr.ChinapnrService;
import com.hyjf.web.shiro.ShiroUtil;
import com.hyjf.web.user.bindcard.UserBindCardDefine;
import com.hyjf.web.user.recharge.UserRechargeController;
import com.hyjf.web.util.WebUtils;

/**
 * @package com.hyjf.web.user.withdraw
 * @author GOGTZ-T
 * @date 2015/12/04 14:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = UserWithdrawDefine.REQUEST_MAPPING)
public class UserWithdrawController extends BaseController {
	private static String HOST_URL = PropUtils.getSystem("hyjf.web.host");

	/** THIS_CLASS */
	private static final String THIS_CLASS = UserWithdrawController.class.getName();

	@Autowired
	private ChinapnrService chinapnrService;

	@Autowired
	private UserWithdrawService userWithdrawService;

	/** 用户提现URL */
	private static String CALLURL = "/hyjf-web/withdraw/cash";

	/**
	 * 检查参数
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(UserWithdrawDefine.CHECK_MAPPING)
	public String check(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(UserRechargeController.class.toString(), UserWithdrawDefine.CHECK_MAPPING);

		Integer userId = ShiroUtil.getLoginUserId(request); // 用户ID
		String transAmt = request.getParameter("getcash");// 交易金额
		String bankId = request.getParameter("card");// 提现银行卡号
		// 检查参数
		JSONObject checkResult = checkParam(request, userId, transAmt, bankId);
		if (checkResult != null) {
			return checkResult.toString();
		}
		checkResult = new JSONObject();
		checkResult.put("url", CALLURL);
		LogUtil.endLog(UserRechargeController.class.toString(), UserWithdrawDefine.CHECK_MAPPING);
		return checkResult.toString();
	}

	/**
	 * 
	 * 跳转到提现页面
	 * 
	 * @author renxingchen
	 * @return
	 */
	@RequestMapping(value = UserWithdrawDefine.TO_WITHDRAW)
	public ModelAndView toWithdraw(HttpServletRequest request) {
		// 取得用户iD
		Integer userId = WebUtils.getUserId(request); // 用户ID
		// 取得用户当前余额
		Account account = this.userWithdrawService.getAccount(userId);
		if (account == null) {
			ModelAndView modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_INFO);
			modelAndView.addObject(UserWithdrawDefine.STATUS, UserWithdrawDefine.STATUS_FALSE);
			modelAndView.addObject(UserWithdrawDefine.ERROR, "你的账户信息存在异常，请联系客服人员处理。");
			return modelAndView;
		} else {
			ModelAndView modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW);
			modelAndView.addObject("total", CustomConstants.DF_FOR_VIEW.format(account.getBalance()));// 可提现金额
			// 查询页面上可以挂载的银行列表
			List<AccountBank> banks = userWithdrawService.getBankCardByUserId(userId);
			List<BankCardBean> bankcards = new ArrayList<BankCardBean>();
			// 是否有快捷提现卡
			Boolean hasBindCard = false;// 是否绑了卡
			Boolean hasBindQuickCard = false;// 是否绑了快捷支付卡
			Boolean hasBindQuickCashCard = false;// 是否绑了快捷提现卡
			String quickCashCardNo = "";// 快捷提现卡卡号
			if (banks != null && banks.size() != 0) {
				hasBindCard = true;// 是否绑了卡
				for (AccountBank bank : banks) {
					BankCardBean bankCardBean = new BankCardBean();
					bankCardBean.setId(bank.getId());
					bankCardBean.setBank(bank.getBank());
					BankConfig bankConfig = userWithdrawService.getBankInfo(bank.getBank());
					bankCardBean.setLogo(HOST_URL + bankConfig.getAppLogo());// 应前台要求，logo路径给绝对路径
					bankCardBean.setBank(bankConfig.getName());// 银行名称 汉字
					bankCardBean.setCardNo(bank.getAccount());
					bankCardBean.setIsDefault(bank.getCardType());// 卡类型 0普通提现卡1默认卡2快捷支付卡
					if (bank.getCardType().equals("1")) {
						hasBindQuickCashCard = true;
						quickCashCardNo = bank.getAccount();
					}
					if (bank.getCardType().equals("2")) {
						hasBindQuickCard = true;
					}
					bankcards.add(bankCardBean);
				}
			} else {
				hasBindCard = false;// 是否绑了卡
			}
			// 取用户信息
			Users user = this.userWithdrawService.getUsers(userId);
			modelAndView.addObject("userType", user.getUserType());// 是否为企业用户（  0普通用户 1企业用户 企业用户不可绑定其他取现卡）
			modelAndView.addObject("hasBindCard", hasBindCard);// 是否绑了卡
			modelAndView.addObject("hasBindQuickCard", hasBindQuickCard);// 是否绑了快捷支付卡
			modelAndView.addObject("hasBindQuickCashCard", hasBindQuickCashCard);// 是否绑了快捷提现卡
			modelAndView.addObject("quickCashCardNo", quickCashCardNo);// 快捷提现卡卡号
			modelAndView.addObject("banks", bankcards);
			return modelAndView;
		}
	}

	/**
	 * 用户提现
	 *
	 * @param request
	 * @param form
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(UserWithdrawDefine.CASH_MAPPING)
	public ModelAndView cash(HttpServletRequest request, HttpServletResponse response) throws IOException {
		LogUtil.startLog(UserWithdrawController.class.toString(), UserWithdrawDefine.CASH_MAPPING);
		ModelAndView modelAndView = new ModelAndView(UserWithdrawDefine.JSP_CHINAPNR_SEND);

		Integer userId = WebUtils.getUserId(request); // 用户ID
		String userName = WebUtils.getUser(request).getUsername(); // 用户名
		String transAmt = request.getParameter("withdrawmoney");// 交易金额
		String bankId = request.getParameter("widCard");// 提现银行卡号
		String cashchl = request.getParameter("cashchl");// 取现渠道(暂时无用)
		// 检查参数
		JSONObject checkResult = checkParam(request, userId, transAmt, bankId);
		if (checkResult != null) {
			modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_FALSE);
			modelAndView.addObject(UserWithdrawDefine.STATUS, UserWithdrawDefine.STATUS_FALSE);
			modelAndView.addObject(UserWithdrawDefine.ERROR, checkResult.getString("message"));
			return modelAndView;
		}
		// 取得用户在汇付天下的客户号
		AccountChinapnr accountChinapnrTender = userWithdrawService.getAccountChinapnr(userId);
		if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getChinapnrUsrcustid())) {
			modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_FALSE);
			modelAndView.addObject(UserWithdrawDefine.STATUS, UserWithdrawDefine.STATUS_FALSE);
			modelAndView.addObject(UserWithdrawDefine.ERROR, "您还未开户，请开户后重新操作！");
			LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.CASH_MAPPING, "[用户未开户]");
			return modelAndView;
		}
		Long chinapnrUsrcustidTender = accountChinapnrTender.getChinapnrUsrcustid();
		// 取现渠道(FAST:快速取现 , GENERAL:一般取现 , IMMEDIATE:即时取现)
		if (Validator.isNull(cashchl)) {
			cashchl = "GENERAL";
		}
		// 取得银行卡号
		AccountBank accountBank = this.userWithdrawService.getBankInfo(userId, GetterUtil.getInteger(bankId));
		if (accountBank == null || Validator.isNull(accountBank.getAccount())) {
			modelAndView = new ModelAndView("redirect:" + UserBindCardDefine.REQUEST_MAPPING + UserBindCardDefine.REQUEST_MAPPING + ".do");
			return modelAndView;
		}
		// 取得手续费
		String fee = this.userWithdrawService.getWithdrawFee(userId, GetterUtil.getInteger(bankId), new BigDecimal(transAmt), cashchl);
		// 实际取现金额(洪刚提示跟线上保持一致)
		// 不去掉一块钱手续费
		if (new BigDecimal(transAmt).compareTo(BigDecimal.ZERO) == 0) {
			modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_FALSE);
			modelAndView.addObject(UserWithdrawDefine.STATUS, UserWithdrawDefine.STATUS_FALSE);
			modelAndView.addObject(UserWithdrawDefine.ERROR, "提现金额不能小于一元！");
			return modelAndView;
		}

		// 入参扩展域
		JSONArray reqExt = new JSONArray();
		JSONObject reqExtObj = new JSONObject();
		reqExtObj.put("CashFeeDeductType", "I");
		// reqExtObj.put("FeeObjFlag", "U"); // 向用户收取
		// reqExtObj.put("FeeAcctId", ""); // 忽略
		// reqExtObj.put("CashChl", cashchl); // 取现渠道
		reqExt.add(reqExtObj);

		// 调用汇付接口(提现)
		String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + UserWithdrawDefine.REQUEST_MAPPING + UserWithdrawDefine.RETURN_MAPPING + ".do";// 支付工程路径
		String bgRetUrl = PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) + UserWithdrawDefine.REQUEST_MAPPING + UserWithdrawDefine.CALLBACK_MAPPING + ".do";// 支付工程路径
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_20);// 接口版本号
		bean.setCmdId(ChinaPnrConstant.CMDID_CASH); // 消息类型(提现)
		bean.setOrdId(GetOrderIdUtils.getOrderId2(Integer.valueOf(userId))); // 订单号(必须)
		bean.setUsrCustId(String.valueOf(chinapnrUsrcustidTender));// 用户客户号
		bean.setTransAmt(CustomUtil.formatAmount(transAmt));// 交易金额(必须)
		bean.setOpenAcctId((accountBank != null && StringUtils.isNotBlank(accountBank.getAccount())) ? accountBank.getAccount() : ""); // 开户银行账号
		bean.setRetUrl(retUrl); // 页面返回 URL
		bean.setBgRetUrl(bgRetUrl); // 商户后台应答地址(必须)
		bean.setType("user_cash"); // 日志类型(写日志用)
		bean.setReqExt(reqExt.toJSONString());
		// 插值用参数
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", String.valueOf(userId));
		params.put("userName", userName);
		params.put("ip", CustomUtil.getIpAddr(request));
		params.put("bankId", bankId);
		params.put("fee", CustomUtil.formatAmount(fee));
		// 用户提现前处理
		boolean withdrawFlag = this.userWithdrawService.updateBeforeCash(bean, params);
		if (withdrawFlag) {
			// 跳转到汇付天下画面
			try {
				modelAndView.addObject(UserWithdrawDefine.STATUS, UserWithdrawDefine.STATUS_TRUE);
				modelAndView = ChinapnrUtil.callApi(bean);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_FALSE);
			modelAndView.addObject(UserWithdrawDefine.STATUS, UserWithdrawDefine.STATUS_FALSE);
			modelAndView.addObject(UserWithdrawDefine.ERROR, "请不要重复操作");
			return modelAndView;
		}
		LogUtil.endLog(UserWithdrawController.class.toString(), UserWithdrawDefine.CASH_MAPPING);
		return modelAndView;
	}

	/**
	 * 用户提现后处理
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(UserWithdrawDefine.RETURN_MAPPING)
	public ModelAndView cashReturn(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) {
		LogUtil.startLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
		ModelAndView modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_SUCCESS);
		bean.convert();
		LogUtil.debugLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
		// 取得更新用UUID
		boolean updateFlag = false;
		String uuid = request.getParameter("uuid");
		if (Validator.isNotNull(uuid)) {
			// 取得检证数据
			ChinapnrExclusiveLogWithBLOBs record = chinapnrService.selectChinapnrExclusiveLog(Long.parseLong(uuid));
			// 如果检证数据状态为未发送
			if (record != null && ChinaPnrConstant.STATUS_VERTIFY_OK.equals(record.getStatus())) {
				// 将状态更新成[2:处理中]
				record.setId(Long.parseLong(uuid));
				record.setStatus(ChinaPnrConstant.STATUS_RUNNING);
				int cnt = this.chinapnrService.updateChinapnrExclusiveLog(record);
				if (cnt > 0) {
					updateFlag = true;
				}
			}
		} else {
			updateFlag = true;
		}
		System.out.println(updateFlag);

		// 其他程序正在处理中,或者返回值错误
		if (!updateFlag) {
			if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
				modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_SUCCESS);
				modelAndView.addObject(UserWithdrawDefine.INFO, "恭喜您，提现成功");
				modelAndView.addObject("amt", bean.getTransAmt());
			} else if (ChinaPnrConstant.RESPCODE_CHECK.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
				modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_INFO);
				modelAndView.addObject(UserWithdrawDefine.INFO, "汇付处理中，请稍后查询交易明细");
			} else {
				modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_INFO);
				modelAndView.addObject(UserWithdrawDefine.INFO, "汇付处理中，请稍后查询交易明细");
			}
			LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易正在处理中或者已经完成,本次回调结束]");
			return modelAndView;
		}

		// 发送状态
		String status = ChinaPnrConstant.STATUS_VERTIFY_OK;
		// 失败时去汇付查询交易状态
		if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
			String transStat = userWithdrawService.checkCashResult(bean.getOrdId());
			if ("S".equals(transStat)) {
				// 取得成功时的信息
				JSONObject data = userWithdrawService.getMsgData(bean.getOrdId());
				if (data != null) {
					// 设置状态
					if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_RESPCODE))) {
						bean.setRespCode(data.getString(ChinaPnrConstant.PARAM_RESPCODE));
						bean.set(ChinaPnrConstant.PARAM_RESPCODE, data.getString(ChinaPnrConstant.PARAM_RESPCODE));
					}
					// 设置结果
					if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_RESPDESC))) {
						bean.setRespDesc(data.getString(ChinaPnrConstant.PARAM_RESPDESC));
						bean.set(ChinaPnrConstant.PARAM_RESPDESC, data.getString(ChinaPnrConstant.PARAM_RESPDESC));
					}
					// 设置手续费
					if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_FEEAMT))) {
						bean.setFeeAmt(data.getString(ChinaPnrConstant.PARAM_FEEAMT));
						bean.set(ChinaPnrConstant.PARAM_FEEAMT, data.getString(ChinaPnrConstant.PARAM_FEEAMT));
					}
					// 设置取现银行
					if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_OPENBANKID))) {
						bean.setOpenBankId(data.getString(ChinaPnrConstant.PARAM_OPENBANKID));
						bean.set(ChinaPnrConstant.PARAM_OPENBANKID, data.getString(ChinaPnrConstant.PARAM_OPENBANKID));
					}
				}
			}
		}

		// 成功或审核中或提现失败
		if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE)) || ChinaPnrConstant.RESPCODE_CHECK.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))
				|| ChinaPnrConstant.RESPCODE_WITHDRAW_FAIL.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
			try {
				//用户userId
				Integer userId = chinapnrService.selectUserIdByUsrcustid(bean.getLong(ChinaPnrConstant.PARAM_USRCUSTID)); // 用户ID
				// 插值用参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("userId", String.valueOf(userId));
				params.put("ip", CustomUtil.getIpAddr(request));
				// 执行提现后处理
				boolean flag = this.userWithdrawService.handlerAfterCash(bean, params);
				if (flag) {
					// 执行结果(成功)
					if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
						status = ChinaPnrConstant.STATUS_SUCCESS;
					} else if (ChinaPnrConstant.RESPCODE_CHECK.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
						status = ChinaPnrConstant.STATUS_VERTIFY_OK;
					} else if (ChinaPnrConstant.RESPCODE_WITHDRAW_FAIL.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
						status = ChinaPnrConstant.STATUS_FAIL;
					}
				} else {
					status = ChinaPnrConstant.STATUS_FAIL;
				}
				LogUtil.debugLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "成功");
			} catch (Exception e) {
				// 执行结果(失败)
				status = ChinaPnrConstant.STATUS_FAIL;
				LogUtil.errorLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, e);
			}

		} else {
			// 执行结果(失败)
			status = ChinaPnrConstant.STATUS_FAIL;
			modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_INFO);
			modelAndView.addObject(UserWithdrawDefine.INFO, "汇付处理中，请稍后查询交易明细");
			// 更新提现失败原因
			String reason = bean.getRespDesc();
			if (StringUtils.isNotEmpty(reason)) {
				if (reason.contains("%")) {
					reason = URLCodec.decodeURL(reason);
				}
			}
			if (StringUtils.isNotEmpty(bean.getOrdId())) {
				this.userWithdrawService.updateAccountWithdrawByOrdId(bean.getOrdId(), reason);
			}
			LogUtil.debugLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "失败");
		}

		// 更新状态记录
		if (updateFlag && Validator.isNotNull(uuid)) {
			this.chinapnrService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
		}
		if (ChinaPnrConstant.STATUS_SUCCESS.equals(status)) {
			modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_SUCCESS);
			modelAndView.addObject("amt", bean.getTransAmt());
			modelAndView.addObject(UserWithdrawDefine.INFO, "恭喜您，提现成功");
		} else if (ChinaPnrConstant.RESPCODE_CHECK.equals(status)) {
			modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_INFO);
			modelAndView.addObject(UserWithdrawDefine.INFO, "汇付处理中，请稍后查询交易明细");
		} else {
			modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_INFO);
			modelAndView.addObject(UserWithdrawDefine.INFO, "汇付处理中，请稍后查询交易明细");
		}
		LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易完成后,回调结束]");
		return modelAndView;
	}

	/**
	 * 用户提现后处理
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(UserWithdrawDefine.CALLBACK_MAPPING)
	public ModelAndView cashCallBack(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) {
		LogUtil.startLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
		ModelAndView modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_SUCCESS);
		bean.convert();
		LogUtil.debugLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");

		// 取得更新用UUID
		boolean updateFlag = false;
		String uuid = request.getParameter("uuid");
		if (Validator.isNotNull(uuid)) {
			// 取得检证数据
			ChinapnrExclusiveLogWithBLOBs record = chinapnrService.selectChinapnrExclusiveLog(Long.parseLong(uuid));
			// 如果检证数据状态为未发送
			if (record != null && ChinaPnrConstant.STATUS_VERTIFY_OK.equals(record.getStatus())) {
				// 将状态更新成[2:处理中]
				record.setId(Long.parseLong(uuid));
				record.setStatus(ChinaPnrConstant.STATUS_RUNNING);
				int cnt = this.chinapnrService.updateChinapnrExclusiveLog(record);
				if (cnt > 0) {
					updateFlag = true;
				}
			}
		} else {
			updateFlag = true;
		}
		System.out.println(updateFlag);

		// 其他程序正在处理中,或者返回值错误
		if (!updateFlag) {
			modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_SUCCESS);
			modelAndView.addObject("amt", bean.getTransAmt());
			modelAndView.addObject(UserWithdrawDefine.INFO, "恭喜您，提现成功");
			LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易正在处理中或者已经完成,本次回调结束]");
			return modelAndView;
		}
		// 发送状态
		String status = ChinaPnrConstant.STATUS_VERTIFY_OK;
		// 失败时去汇付查询交易状态
		if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
			String transStat = userWithdrawService.checkCashResult(bean.getOrdId());
			if ("S".equals(transStat)) {
				// 取得成功时的信息
				JSONObject data = userWithdrawService.getMsgData(bean.getOrdId());
				if (data != null) {
					// 设置状态
					if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_RESPCODE))) {
						bean.setRespCode(data.getString(ChinaPnrConstant.PARAM_RESPCODE));
						bean.set(ChinaPnrConstant.PARAM_RESPCODE, data.getString(ChinaPnrConstant.PARAM_RESPCODE));
					}
					// 设置结果
					if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_RESPDESC))) {
						bean.setRespDesc(data.getString(ChinaPnrConstant.PARAM_RESPDESC));
						bean.set(ChinaPnrConstant.PARAM_RESPDESC, data.getString(ChinaPnrConstant.PARAM_RESPDESC));
					}

					// 设置手续费
					if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_FEEAMT))) {
						bean.setFeeAmt(data.getString(ChinaPnrConstant.PARAM_FEEAMT));
						bean.set(ChinaPnrConstant.PARAM_FEEAMT, data.getString(ChinaPnrConstant.PARAM_FEEAMT));
					}
					// 设置取现银行
					if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_OPENBANKID))) {
						bean.setOpenBankId(data.getString(ChinaPnrConstant.PARAM_OPENBANKID));
						bean.set(ChinaPnrConstant.PARAM_OPENBANKID, data.getString(ChinaPnrConstant.PARAM_OPENBANKID));
					}
				}
			}
		}
		// 成功或审核中或提现失败
		if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE)) || ChinaPnrConstant.RESPCODE_CHECK.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))
				|| ChinaPnrConstant.RESPCODE_WITHDRAW_FAIL.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
			try {
				//用户userId
				Integer userId = chinapnrService.selectUserIdByUsrcustid(bean.getLong(ChinaPnrConstant.PARAM_USRCUSTID)); // 用户ID
				// 插值用参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("userId", String.valueOf(userId));
				params.put("ip", CustomUtil.getIpAddr(request));
				// 执行提现后处理
				boolean flag = this.userWithdrawService.handlerAfterCash(bean, params);
				if(flag){
					// 执行结果(成功)
					if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
						status = ChinaPnrConstant.STATUS_SUCCESS;
					} else if (ChinaPnrConstant.RESPCODE_CHECK.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
						status = ChinaPnrConstant.STATUS_VERTIFY_OK;
					} else if (ChinaPnrConstant.RESPCODE_WITHDRAW_FAIL.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
						status = ChinaPnrConstant.STATUS_FAIL;
					}
				}else{
					status = ChinaPnrConstant.STATUS_FAIL;
				}
				LogUtil.debugLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "成功");
			} catch (Exception e) {
				// 执行结果(失败)
				status = ChinaPnrConstant.STATUS_FAIL;
				LogUtil.errorLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, e);
			}
		} else {
			// 执行结果(失败)
			status = ChinaPnrConstant.STATUS_FAIL;
			// 更新提现失败原因
			String reason = bean.getRespDesc();
			if (StringUtils.isNotEmpty(reason)) {
				if (reason.contains("%")) {
					reason = URLCodec.decodeURL(reason);
				}
			}
			if (StringUtils.isNotEmpty(bean.getOrdId())) {
				this.userWithdrawService.updateAccountWithdrawByOrdId(bean.getOrdId(), reason);
			}
			LogUtil.debugLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "失败");
		}

		// 更新状态记录
		if (updateFlag && Validator.isNotNull(uuid)) {
			this.chinapnrService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
		}
		if (ChinaPnrConstant.STATUS_SUCCESS.equals(status)) {
			modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_SUCCESS);
			modelAndView.addObject("amt", bean.getTransAmt());
			modelAndView.addObject(UserWithdrawDefine.INFO, "恭喜您，提现成功");
		} else if (ChinaPnrConstant.RESPCODE_CHECK.equals(status)) {
			modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_INFO);
			modelAndView.addObject(UserWithdrawDefine.INFO, "汇付处理中，请稍后查询交易明细");
		} else {
			modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_INFO);
			modelAndView.addObject(UserWithdrawDefine.INFO, "汇付处理中，请稍后查询交易明细");
		}
		LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易完成后,回调结束]");
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
		if (account == null || transAmt.compareTo(account.getBalance()) > 0) {
			return jsonMessage("提现金额大于可用余额，请确认后再次提现。", "1");
		}
		// 检查参数(银行卡ID是否数字)
		if (Validator.isNotNull(bankId) && !NumberUtils.isNumber(bankId)) {
			return jsonMessage("银行卡号不正确，请确认后再次提现。", "1");
		}
		return null;
	}

	/**
	 * 
	 * 用户输入金额之后，计算实际提现金额
	 * 
	 * @author wangchao
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = UserWithdrawDefine.WITHDRAWINFO_MAPPING)
	public UserWithdrawInFoResult getWithdrawInfo(HttpServletRequest request) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
		UserWithdrawInFoResult result = new UserWithdrawInFoResult();
		// 获取用户输入提现金额
		String withdrawmoney = request.getParameter("withdrawmoney");
		// 取得用户iD
		Integer userId = WebUtils.getUserId(request); // 用户ID
		// 取得用户当前余额
		Account account = this.userWithdrawService.getAccount(userId);
		// 可提现金额
		Double total = account.getBalance().doubleValue();
		// 提现金额为空，置0
		if (withdrawmoney == null || withdrawmoney.equals("")) {
			withdrawmoney = "0";
		}
		try {
			// 提现金额不正确会报异常
			Double.valueOf(withdrawmoney);
		} catch (Exception e) {
			return result;
		}
		// 根据产品要求判断实际提现金额
		if (withdrawmoney.matches("-?[0-9]+.*[0-9]*")) {// 判断是否为数字
			Double money = (Double.valueOf(withdrawmoney) > total) ? total : Double.valueOf(withdrawmoney);
			// 计算实际提现金额
			if (money + 1 <= total) {
				result.setBalance(df.format(money));
			}
			if (money + 1 > total) {
				result.setBalance(df.format(money - 1));
			}
			result.setShowBalance(withdrawmoney);
		}
		result.success();
		return result;
	}
}
