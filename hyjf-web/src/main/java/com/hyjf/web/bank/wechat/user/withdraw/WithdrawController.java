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
package com.hyjf.web.bank.wechat.user.withdraw;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.withdraw.WithdrawService;
import com.hyjf.common.http.URLCodec;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountBank;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogWithBLOBs;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.web.BaseController;
import com.hyjf.web.WeChatBaseAjaxResultBean;
import com.hyjf.web.chinapnr.ChinapnrService;

/**
 * @package com.hyjf.web.user.withdraw
 * @author GOGTZ-T
 * @date 2015/12/04 14:00
 * @version V1.0  
 */
@Controller(WithdrawDefine.CONTROLLER_NAME)
@RequestMapping(value = WithdrawDefine.REQUEST_MAPPING)
public class WithdrawController extends BaseController {

	/** THIS_CLASS */
	private static final String THIS_CLASS = WithdrawController.class.getName();

	@Autowired
	private ChinapnrService chinapnrService;

	@Autowired
	private WithdrawService userWithdrawService;

	/**
	 * 检查参数
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(WithdrawDefine.CHECK_MAPPING)
	public String check(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(THIS_CLASS, WithdrawDefine.CHECK_MAPPING);
		WeChatBaseAjaxResultBean result = new WeChatBaseAjaxResultBean();
		Integer userId = StringUtils.isNotBlank(request.getParameter("userId")) ? Integer.parseInt(request.getParameter("userId")) : null; // 用户ID
		String transAmt = request.getParameter("account");// 交易金额
		String bankId = request.getParameter("cardNo");// 提现银行卡号
		// 检查参数
		JSONObject checkResult = checkParam(request, userId, transAmt, bankId);
		if (checkResult != null) {
			result.setError(checkResult.getIntValue("error"));
			result.setErrorDesc(checkResult.getString("message"));
		}
		LogUtil.endLog(THIS_CLASS, WithdrawDefine.CHECK_MAPPING);
		return JSONObject.toJSONString(result, true);
	}

	/**
	 * 用户提现
	 *
	 * @param request
	 * @param form
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(WithdrawDefine.CASH_MAPPING)
	public ModelAndView cash(HttpServletRequest request, HttpServletResponse response) throws IOException {

		LogUtil.startLog(WithdrawController.class.toString(), WithdrawDefine.CASH_MAPPING);
		ModelAndView modelAndView = new ModelAndView();
		Integer userId = StringUtils.isNotBlank(request.getParameter("userId")) ? Integer.parseInt(request.getParameter("userId")) : null;// 用户ID
		String transAmt = request.getParameter("account");// 交易金额
		String cardNo = request.getParameter("cardNo");// 提现银行卡号
		String callback = request.getParameter("callback");// 返回地址
		// 检查参数
		JSONObject checkResult = checkParam(request, userId, transAmt, cardNo);
		if (checkResult != null) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", "1");
			map.put("errorDesc", checkResult.getString("message"));
			String info = JSON.toJSONString(map);
			try {
				info = URLEncoder.encode(info, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return new ModelAndView("redirect:" + callback + "backinfo/" + info);
		}
		Users user = this.userWithdrawService.getUsers(userId); // 用户名
		if (Validator.isNull(user)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", "1");
			map.put("errorDesc", "未查询到您的的用户信息");
			String info = JSON.toJSONString(map);
			try {
				info = URLEncoder.encode(info, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return new ModelAndView("redirect:" + callback + "backinfo/" + info);
		}
		String userName = user.getUsername();
		// 取得用户在汇付天下的客户号
		AccountChinapnr accountChinapnrTender = userWithdrawService.getAccountChinapnr(userId);
		if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getChinapnrUsrcustid())) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", "1");
			map.put("errorDesc", "您还未开户，请开户后重新操作！");
			String info = JSON.toJSONString(map);
			try {
				info = URLEncoder.encode(info, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return new ModelAndView("redirect:" + callback + "backinfo/" + info);
		}
		Long chinapnrUsrcustidTender = accountChinapnrTender.getChinapnrUsrcustid();
		// 取得银行卡号
		AccountBank accountBank = this.userWithdrawService.getBankCard(userId, cardNo);
		if (accountBank == null || Validator.isNull(accountBank.getAccount())) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", "1");
			map.put("errorDesc", "您还未开户，请开户后重新操作！");
			String info = JSON.toJSONString(map);
			try {
				info = URLEncoder.encode(info, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return new ModelAndView("redirect:" + callback + "backinfo/" + info);
		}
		int bankId = accountBank.getId();
		// 实际取现金额(洪刚提示跟线上保持一致)
		// 不去掉一块钱手续费
		if (new BigDecimal(transAmt).compareTo(BigDecimal.ZERO) == 0) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", "1");
			map.put("errorDesc", "提现金额不能小于一元！");
			String info = JSON.toJSONString(map);
			try {
				info = URLEncoder.encode(info, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return new ModelAndView("redirect:" + callback + "backinfo/" + info);
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
		String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + WithdrawDefine.REQUEST_MAPPING + WithdrawDefine.RETURN_MAPPING + ".do?callback=" + callback;// 支付工程路径
		String bgRetUrl = PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) + WithdrawDefine.REQUEST_MAPPING + WithdrawDefine.CALLBACK_MAPPING + ".do?callback=" + callback;// 支付工程路径
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
		params.put("bankId", String.valueOf(bankId));
		params.put("client", "1");//
		// 用户提现前处理
		boolean withdrawFlag = this.userWithdrawService.updateBeforeCash(bean, params);
		if (withdrawFlag) {
			// 跳转到汇付天下画面
			try {
				modelAndView.addObject(WithdrawDefine.STATUS, WithdrawDefine.STATUS_TRUE);
				modelAndView = ChinapnrUtil.callApi(bean);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", "1");
			map.put("errorDesc", "请不要重复操作！");
			String info = JSON.toJSONString(map);
			try {
				info = URLEncoder.encode(info, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return new ModelAndView("redirect:" + callback + "backinfo/" + info);
		}
		LogUtil.endLog(WithdrawController.class.toString(), WithdrawDefine.CASH_MAPPING);
		return modelAndView;
	}

	/**
	 * 用户提现后处理
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(WithdrawDefine.RETURN_MAPPING)
	public ModelAndView cashReturn(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) {

		LogUtil.startLog(THIS_CLASS, WithdrawDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
		String callback = request.getParameter("callback");
		bean.convert();
		LogUtil.debugLog(THIS_CLASS, WithdrawDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
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
		String info = "";
		// 其他程序正在处理中,或者返回值错误
		if (!updateFlag) {
			if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
				BigDecimal transAmt = bean.getBigDecimal(ChinaPnrConstant.PARAM_TRANSAMT);// 支出金额
				BigDecimal realTansAmt = bean.getBigDecimal(ChinaPnrConstant.PARAM_REALTRANSAMT);// 提现金额
				BigDecimal feeAmt = transAmt.subtract(realTansAmt);// 提现手续费
				Map<String, String> map = new HashMap<String, String>();
				map.put("error", "0");
				map.put("errorDesc", "恭喜您，提现成功！");
				map.put("account", bean.getRealTransAmt());
				map.put("fee", feeAmt.toString());
				info = JSON.toJSONString(map);
				try {
					info = URLEncoder.encode(info, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else if (ChinaPnrConstant.RESPCODE_CHECK.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("error", "2");
				map.put("errorDesc", "汇付处理中，请稍后查询交易明细！");
				info = JSON.toJSONString(map);
				try {
					info = URLEncoder.encode(info, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				Map<String, String> map = new HashMap<String, String>();
				map.put("error", "2");
				map.put("errorDesc", "汇付处理中，请稍后查询交易明细！");
				info = JSON.toJSONString(map);
				try {
					info = URLEncoder.encode(info, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

			}
			LogUtil.endLog(THIS_CLASS, WithdrawDefine.RETURN_MAPPING, "[交易正在处理中或者已经完成,本次回调结束]");
			return new ModelAndView("redirect:" + callback + "backinfo/" + info);
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
				// 用户userId
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
				LogUtil.debugLog(THIS_CLASS, WithdrawDefine.RETURN_MAPPING, "成功");
			} catch (Exception e) {
				// 执行结果(失败)
				status = ChinaPnrConstant.STATUS_FAIL;
				LogUtil.errorLog(THIS_CLASS, WithdrawDefine.RETURN_MAPPING, e);
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
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", "2");
			map.put("errorDesc", "汇付处理中，请稍后查询交易明细！");
			info = JSON.toJSONString(map);
			try {
				info = URLEncoder.encode(info, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			LogUtil.debugLog(THIS_CLASS, WithdrawDefine.RETURN_MAPPING, "失败");
			return new ModelAndView("redirect:" + callback + "backinfo/" + info);
		}

		// 更新状态记录
		if (updateFlag && Validator.isNotNull(uuid)) {
			this.chinapnrService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
		}
		if (ChinaPnrConstant.STATUS_SUCCESS.equals(status)) {
			BigDecimal transAmt = bean.getBigDecimal(ChinaPnrConstant.PARAM_TRANSAMT);// 支出金额
			BigDecimal realTansAmt = bean.getBigDecimal(ChinaPnrConstant.PARAM_REALTRANSAMT);// 提现金额
			BigDecimal feeAmt = transAmt.subtract(realTansAmt);// 提现手续费
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", "0");
			map.put("errorDesc", "恭喜您，提现成功！");
			map.put("account", bean.getRealTransAmt());
			map.put("fee", feeAmt.toString());
			info = JSON.toJSONString(map);
			try {
				info = URLEncoder.encode(info, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return new ModelAndView("redirect:" + callback + "backinfo/" + info);
		} else if (ChinaPnrConstant.RESPCODE_CHECK.equals(status)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", "2");
			map.put("errorDesc", "汇付处理中，请稍后查询交易明细！");
			info = JSON.toJSONString(map);
			try {
				info = URLEncoder.encode(info, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			LogUtil.debugLog(THIS_CLASS, WithdrawDefine.RETURN_MAPPING, "失败");
			return new ModelAndView("redirect:" + callback + "backinfo/" + info);
		} else {
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", "2");
			map.put("errorDesc", "汇付处理中，请稍后查询交易明细！");
			info = JSON.toJSONString(map);
			try {
				info = URLEncoder.encode(info, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			LogUtil.debugLog(THIS_CLASS, WithdrawDefine.RETURN_MAPPING, "失败");
			return new ModelAndView("redirect:" + callback + "backinfo/" + info);
		}
	}

	/**
	 * 用户提现后处理
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(WithdrawDefine.CALLBACK_MAPPING)
	public ModelAndView cashCallBack(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) {

		LogUtil.startLog(THIS_CLASS, WithdrawDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
		String callback = request.getParameter("callback");
		bean.convert();
		LogUtil.debugLog(THIS_CLASS, WithdrawDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
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
		String info = "";
		// 其他程序正在处理中,或者返回值错误
		if (!updateFlag) {
			BigDecimal transAmt = bean.getBigDecimal(ChinaPnrConstant.PARAM_TRANSAMT);// 支出金额
			BigDecimal realTansAmt = bean.getBigDecimal(ChinaPnrConstant.PARAM_REALTRANSAMT);// 提现金额
			BigDecimal feeAmt = transAmt.subtract(realTansAmt);// 提现手续费
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", "0");
			map.put("errorDesc", "恭喜您，提现成功！");
			map.put("account", bean.getRealTransAmt());
			map.put("fee", feeAmt.toString());
			info = JSON.toJSONString(map);
			try {
				info = URLEncoder.encode(info, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return new ModelAndView("redirect:" + callback + "backinfo/" + info);
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
				// 用户userId
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
				LogUtil.debugLog(THIS_CLASS, WithdrawDefine.RETURN_MAPPING, "成功");
			} catch (Exception e) {
				// 执行结果(失败)
				status = ChinaPnrConstant.STATUS_FAIL;
				LogUtil.errorLog(THIS_CLASS, WithdrawDefine.RETURN_MAPPING, e);
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
			LogUtil.debugLog(THIS_CLASS, WithdrawDefine.RETURN_MAPPING, "失败");
		}

		// 更新状态记录
		if (updateFlag && Validator.isNotNull(uuid)) {
			this.chinapnrService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
		}
		if (ChinaPnrConstant.STATUS_SUCCESS.equals(status)) {
			BigDecimal transAmt = bean.getBigDecimal(ChinaPnrConstant.PARAM_TRANSAMT);// 支出金额
			BigDecimal realTansAmt = bean.getBigDecimal(ChinaPnrConstant.PARAM_REALTRANSAMT);// 提现金额
			BigDecimal feeAmt = transAmt.subtract(realTansAmt);// 提现手续费
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", "0");
			map.put("errorDesc", "恭喜您，提现成功！");
			map.put("account", bean.getRealTransAmt());
			map.put("fee", feeAmt.toString());
			info = JSON.toJSONString(map);
			try {
				info = URLEncoder.encode(info, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			LogUtil.debugLog(THIS_CLASS, WithdrawDefine.RETURN_MAPPING, "成功");
			return new ModelAndView("redirect:" + callback + "backinfo/" + info);
		} else if (ChinaPnrConstant.RESPCODE_CHECK.equals(status)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", "2");
			map.put("errorDesc", "汇付处理中，请稍后查询交易明细！");
			info = JSON.toJSONString(map);
			try {
				info = URLEncoder.encode(info, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			LogUtil.debugLog(THIS_CLASS, WithdrawDefine.RETURN_MAPPING, "处理中");
			return new ModelAndView("redirect:" + callback + "backinfo/" + info);
		} else {
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", "2");
			map.put("errorDesc", "汇付处理中，请稍后查询交易明细！");
			info = JSON.toJSONString(map);
			try {
				info = URLEncoder.encode(info, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			LogUtil.debugLog(THIS_CLASS, WithdrawDefine.RETURN_MAPPING, "失败");
			return new ModelAndView("redirect:" + callback + "backinfo/" + info);
		}
	}

	/**
	 * 检查参数的正确性
	 *
	 * @param userId
	 * @param transAmtStr
	 * @param bankId
	 * @return
	 */
	private JSONObject checkParam(HttpServletRequest request, Integer userId, String transAmtStr, String cardNo) {
		// 检查参数(交易金额是否数字)
		if (Validator.isNull(transAmtStr) || !NumberUtils.isNumber(transAmtStr)) {
			return jsonMessage("请输入提现金额。", "1");
		}
		// 检查参数(交易金额是否大于0)
		BigDecimal transAmt = new BigDecimal(transAmtStr);
		if (transAmt.compareTo(BigDecimal.ONE) <= 0) {
			return jsonMessage("提现金额需大于1元！", "1");
		}
		if (Validator.isNull(userId)) {
			return jsonMessage("用户信息不能为空。", "1");
		}
		// 取得用户当前余额
		Account account = this.userWithdrawService.getAccount(userId);
		// 投标金额大于可用余额
		if (account == null || transAmt.compareTo(account.getBalance()) > 0) {
			return jsonMessage("提现金额大于可用余额，请确认后再次提现。", "1");
		}
		// 检查参数(银行卡ID是否数字)
		if (Validator.isNull(cardNo)) {
			return jsonMessage("银行卡号不正确，请确认后再次提现。", "1");
		}
		return null;
	}
}
