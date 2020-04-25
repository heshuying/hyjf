package com.hyjf.wechat.controller.user.withdraw;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
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
import com.hyjf.wechat.BaseResultBeanFrontEnd;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseMapBean;
import com.hyjf.wechat.controller.user.autoplus.AutoPlusDefine;
import com.hyjf.wechat.service.withdrawhf.WithdrawHFService;
import com.hyjf.wechat.util.ResultEnum;

@Controller(WXWithdrawDefine.CONTROLLER_NAME)
@RequestMapping(value = WXWithdrawDefine.REQUEST_MAPPING)
public class WXWithdrawController extends BaseController{
	
	private static String HOST_URL = PropUtils.getSystem("hyjf.web.host");
	private static DecimalFormat df = new DecimalFormat("########0.00");
	
    @Autowired
    private WithdrawHFService userWithdrawService;
	
    @SignValidate
	@ResponseBody
	@RequestMapping(WXWithdrawDefine.GETINFO_MAPPING)
	public JSONObject getCashInfo(HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = new JSONObject();
		ret.put("request", WXWithdrawDefine.GETINFO_MAPPING);

		// bankCode 银行编号
		String bankCode = request.getParameter("bankCode");
		// getcash 提现金额
		String getcash = request.getParameter("getcash");
		// 金额显示格式
		DecimalFormat moneyFormat = null;
		// 判断选择哪种金融样式
		moneyFormat = CustomConstants.DF_FOR_VIEW;
		// 提现规则静态页面的url
		ret.put("url", PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + WXWithdrawDefine.REQUEST_MAPPING + WXWithdrawDefine.GET_WITHDRAW_RULE_MAPPING);
		// 取得用户id
		Integer userId = requestUtil.getRequestUserId(request);
		// 取得用户当前余额
		Account account = this.userWithdrawService.getAccount(userId);
		if (account == null) {
			ret.put("status", ResultEnum.ERROR_004.getStatus());
			ret.put("statusDesc", ResultEnum.ERROR_004.getStatusDesc());
			return ret;
		} else {
			ret.put("total", moneyFormat.format(account.getBalance()));// 可提现金额
		}
		// 取得用户在汇付天下的账户信息
		AccountChinapnr accountChinapnr = userWithdrawService.getAccountChinapnr(userId);
		// 用户未开户时,返回错误信息
		if (accountChinapnr == null) {
			ret.put("status", ResultEnum.USER_ERROR_200.getStatus());
			ret.put("statusDesc", ResultEnum.USER_ERROR_200.getStatusDesc());
			return ret;
		}
		// 取得银行卡信息
		// begin 调汇付接口查询银行卡信息 4.4.11 因为绑卡的时候汇付未能传递给我们是否默认卡
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10);
		bean.setCmdId(ChinaPnrConstant.CMDID_QUERY_CARD_INFO); // 消息类型(必须)
		bean.setMerCustId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID)); // 商户客户号
		bean.setUsrCustId(String.valueOf(accountChinapnr.getChinapnrUsrcustid())); // 用户客户号(必须)
		// 调用汇付接口
		ChinapnrBean chinaPnrBean = ChinapnrUtil.callApiBg(bean);
		// end 调汇付接口查询银行卡信息 4.4.11 因为绑卡的时候汇付未能传递给我们是否默认卡
		if (chinaPnrBean == null) {
			ret.put("status", ResultEnum.USER_ERROR_209.getStatus());
			ret.put("statusDesc", ResultEnum.USER_ERROR_209.getStatusDesc());
			return ret;
		}
		String UsrCardInfolist = chinaPnrBean.getUsrCardInfolist();
		JSONArray array = JSONObject.parseArray(UsrCardInfolist);
		BankConfig bankSetUp = new BankConfig();
		if (array.size() > 0) {
			ret.put("bankCnt", array.size() + "");
			List<BankCardBean> bankcards = new ArrayList<BankCardBean>();
			for (int j = 0; j < array.size(); j++) {
				JSONObject obj = array.getJSONObject(j);
				if (!obj.getString("RealFlag").equals("R")) {
					// 只有实名卡才入库
					continue;
				}
				BankConfig bankConfig = userWithdrawService.getBankInfo(obj.getString("BankId"));// eg:"BankId":"CCB"
				BankCardBean bankCardBean = new BankCardBean();
				bankCardBean.setIsDefault("0");// 普通卡
				if (obj.getString("IsDefault").equals("Y")) {
					bankCardBean.setIsDefault("1");// 默认卡
				}
				if (obj.getString("ExpressFlag").equals("Y")) {
					bankCardBean.setIsDefault("2");// 快捷卡
				}
				bankCardBean.setBankCode(obj.getString("BankId"));// 银行代号
				bankCardBean.setBank(bankConfig.getName());// 银行名称
				bankCardBean.setLogo(HOST_URL + bankConfig.getAppLogo());// 应前台要求，logo路径给绝对路径
				bankCardBean.setCardNo(obj.getString("CardId"));
				bankcards.add(bankCardBean);
				// 判断是否已经传银行卡code，如果已传则获取该银行的信息
				if (bankCode != null && bankCode.equals(obj.getString("BankId"))) {
					bankSetUp = bankConfig;
				} else {
					// 如果没有传银行卡code，则判断是默认银行卡或快捷卡记录银行设置
					if (obj.getString("IsDefault").equals("Y") || obj.getString("ExpressFlag").equals("Y")) {
						bankSetUp = bankConfig;
					}

				}
			}
			ret.put("banks", bankcards);
			ret.put("logo", bankcards.get(0).getLogo());
			ret.put("cardNo", bankcards.get(0).getCardNo());
			ret.put("bank", bankcards.get(0).getBank());
		} else {
			ret.put("bankCnt", "0");
		}

		// 银行卡支持的提现方式 开始
		int cashchlCnt = 0;
		JSONArray cashchls = new JSONArray();
		// 判断是否有一般提现
		if (1 == bankSetUp.getNormalWithdraw()) {
			JSONObject jo = new JSONObject();
			jo.put("cashchlNm", "GENERAL");
			jo.put("cashchlRemark", "一般提现");
			// 默认提现方式,0一般提现,1快速提现,2即时提现,默认0
			if (bankSetUp.getWithdrawDefaulttype() == 0) {
				jo.put("isDefaultCashchl", "1");// 是否默认提现方式1是，0否
			} else {
				jo.put("isDefaultCashchl", "0");// 是否默认提现方式1是，0否
			}
			cashchlCnt++;
			cashchls.add(jo);
		}
		// 判断是否有快速提现
		if (1 == bankSetUp.getQuickWithdraw()) {
			JSONObject jo = new JSONObject();
			jo.put("cashchlNm", "FAST");
			jo.put("cashchlRemark", "快速提现");
			// 默认提现方式,0一般提现,1快速提现,2即时提现,默认0
			if (bankSetUp.getWithdrawDefaulttype() == 1) {
				jo.put("isDefaultCashchl", "1");// 是否默认提现方式1是，0否
			} else {
				jo.put("isDefaultCashchl", "0");// 是否默认提现方式1是，0否
			}
			cashchlCnt++;
			cashchls.add(jo);
		}
		// 判断是否有即时提现
		if (1 == bankSetUp.getImmediatelyWithdraw()) {
			JSONObject jo = new JSONObject();
			jo.put("cashchlNm", "IMMEDIATE");
			jo.put("cashchlRemark", "即时提现");
			// 默认提现方式,0一般提现,1快速提现,2即时提现,默认0
			if (bankSetUp.getWithdrawDefaulttype() == 2) {
				jo.put("isDefaultCashchl", "1");// 是否默认提现方式1是，0否
			} else {
				jo.put("isDefaultCashchl", "0");// 是否默认提现方式1是，0否
			}
			cashchlCnt++;
			cashchls.add(jo);
		}
		ret.put("cashchlCnt", cashchlCnt + "");// 提现方式总数
		ret.put("cashchls", cashchls);
		// 银行卡支持的提现方式 结束
		// 如果提现金额是0
		if ("0".equals(getcash) || "".equals(getcash)) {
			ret.put("accountDesc", "手续费: 0 元；实际到账: 0 元");
			ret.put("fee", "0.00 元");
            ret.put("balance", "0.00 元");
            ret.put("buttonWord", "提现");
		} else {
		    
			String balance = "";
			if ((new BigDecimal(getcash).subtract(BigDecimal.ONE)).compareTo(BigDecimal.ZERO) < 0) {
				balance = "0";
			} else {
				balance = moneyFormat.format(new BigDecimal(getcash).subtract(BigDecimal.ONE));
			}
			ret.put("accountDesc", "手续费: 1 元；实际到账: " + balance + " 元");
			ret.put("fee", "1.00 元");
			ret.put("balance", balance+" 元");
			ret.put("buttonWord", "确认提现"+moneyFormat.format(new BigDecimal("".equals(getcash)?"0":getcash))+"元");
		}
		
		ret.put("status", ResultEnum.SUCCESS.getStatus());
		ret.put("statusDesc", ResultEnum.SUCCESS.getStatusDesc());
		return ret;
	}
	
	/**
	 * 用户提现
	 *
	 * @param request
	 * @param response
	 * @return
	 */
    @SignValidate
	@RequestMapping(WXWithdrawDefine.CASH_MAPPING)
	public ModelAndView cash(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView(WXWithdrawDefine.JSP_CHINAPNR_SEND);
		String message = "";
		Integer userId = requestUtil.getRequestUserId(request);
        String sign = requestUtil.getRequestSign(request);
		String transAmt = request.getParameter("getcash");// 交易金额
		String bankId = request.getParameter("card");// 提现银行卡号
		String cashchl = request.getParameter("cashchl");// 取现渠道(暂时无用)
		// 检查参数
		JSONObject checkResult = checkParam(request, userId, transAmt, bankId);
		if (checkResult != null) {
			message = checkResult.getString("message");
			if ("提现金额需大于1元！".equals(message)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("status", "1");
				map.put("statusDesc", "提现金额需大于1元！");
				modelAndView = new ModelAndView("jsonView", map);
			} else {
				modelAndView = new ModelAndView(WXWithdrawDefine.JUMP_HTML);
				BaseMapBean baseMapBean=new BaseMapBean();
				baseMapBean.set(CustomConstants.APP_STATUS, ResultEnum.SUCCESS.getStatus());
				baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
				baseMapBean.setCallBackAction(CustomConstants.HOST + WXWithdrawDefine.JUMP_HTML_ERROR_PATH);
				modelAndView.addObject("callBackForm", baseMapBean);
				return modelAndView;
			}
		}

		// 取得用户在汇付天下的客户号
		AccountChinapnr accountChinapnrTender = userWithdrawService.getAccountChinapnr(userId);
		if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getChinapnrUsrcustid())) {
			return getErrorModelAndView(ResultEnum.USER_ERROR_200, sign);
		}
		Long chinapnrUsrcustidTender = accountChinapnrTender.getChinapnrUsrcustid();

		// 取现渠道(FAST:快速取现 , GENERAL:一般取现 , IMMEDIATE:即时取现)
		if (Validator.isNull(cashchl)) {
			cashchl = "GENERAL";// 默认是一般提现
		}

		// 校验 银行卡号
		AccountBank accountBank = this.userWithdrawService.getBankInfoHF(userId, bankId);
		if (accountBank == null || Validator.isNull(accountBank.getAccount())) {
			return getErrorModelAndView(ResultEnum.USER_ERROR_210, sign);
		}

		// 取得手续费
		String fee = this.userWithdrawService.getWithdrawFee(userId, bankId, new BigDecimal(transAmt), cashchl);

		// 实际取现费用(洪刚提示跟线上保持一致)
		// 入参扩展域
		JSONArray reqExt = new JSONArray();
		JSONObject reqExtObj = new JSONObject();
		reqExtObj.put("CashFeeDeductType", "I");
		// reqExtObj.put("FeeObjFlag", "U"); // 向用户收取
		// reqExtObj.put("FeeAcctId", ""); // 忽略
		// reqExtObj.put("CashChl", cashchl); // 取现渠道
		reqExt.add(reqExtObj);

		// 调用汇付接口(提现)
		String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + WXWithdrawDefine.REQUEST_MAPPING + WXWithdrawDefine.RETURN_MAPPING;// 提现同步回调路径
		String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + WXWithdrawDefine.REQUEST_MAPPING + WXWithdrawDefine.CALLBACK_MAPPING;// 提现异步回调路径
		ChinapnrBean bean = new ChinapnrBean();
		// 2.0接口
		bean.setVersion(ChinaPnrConstant.VERSION_20);// 接口版本号
		bean.setCmdId(ChinaPnrConstant.CMDID_CASH); // 消息类型(主动投标)
		bean.setOrdId(GetOrderIdUtils.getOrderId2(Integer.valueOf(userId))); // 订单号(必须)
		bean.setUsrCustId(String.valueOf(chinapnrUsrcustidTender));// 用户客户号
		bean.setTransAmt(CustomUtil.formatAmount(transAmt));// 交易金额(必须)
		bean.setOpenAcctId(accountBank.getAccount()); // 开户银行账号
		bean.setRetUrl(retUrl); // 页面返回 URL
		bean.setBgRetUrl(bgRetUrl); // 商户后台应答地址(必须)
		bean.setMerPriv(CustomUtil.formatAmount(fee)); // 商户私有域
		bean.setPageType("1");// app 应用风格页面（无标题）
		bean.setType("user_cash"); // 日志类型(写日志用)
		bean.setReqExt(reqExt.toJSONString());
		// 插值用参数
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", String.valueOf(userId));
		params.put("ip", CustomUtil.getIpAddr(request));
		params.put("bankId", bankId);
		params.put("client", "WX");// 平台类型 0pc 1WX 2AND 3IOS 4other
		// 用户提现前处理
		boolean withdrawFlag = this.userWithdrawService.updateBeforeCash(bean, params);
		if (withdrawFlag) {
			// 跳转到汇付天下画面
			try {
				modelAndView = ChinapnrUtil.callApi(bean);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return getErrorModelAndView(ResultEnum.USER_ERROR_211, sign);
		}
		return modelAndView;
	}
	
	/**
	 * 用户提现后处理 同步
	 *
	 * @param request
	 * @param bean
	 * @return
	 */
    @SignValidate
	@RequestMapping(WXWithdrawDefine.RETURN_MAPPING)
	public ModelAndView cashReturn(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) {
		bean.convert();
//		System.out.println("提现后同步处理开始: 参数: " + bean == null ? "无" : bean.getAllParams() + "]");

		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		// 取得更新用UUID
		boolean updateFlag = false;
		String uuid = request.getParameter("uuid");
		if (Validator.isNotNull(uuid)) {
			// 取得检证数据
			ChinapnrExclusiveLogWithBLOBs record = userWithdrawService.selectChinapnrExclusiveLog(Long.parseLong(uuid));
			// 如果检证数据状态为未发送
			if (record != null && ChinaPnrConstant.STATUS_VERTIFY_OK.equals(record.getStatus())) {
				// 将状态更新成[2:处理中]
				record.setId(Long.parseLong(uuid));
				record.setStatus(ChinaPnrConstant.STATUS_RUNNING);
				int cnt = this.userWithdrawService.updateChinapnrExclusiveLog(record);
				if (cnt > 0) {
					updateFlag = true;
				}
			}
		} else {
			updateFlag = true;
		}
//		System.out.println(updateFlag);

		// 其他程序正在处理中,或者返回值错误
		if (!updateFlag) {
			ModelAndView modelAndView = new ModelAndView(WXWithdrawDefine.JUMP_HTML);
			if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
//				modelAndView.addObject("TransAmt", df.format(new BigDecimal(bean.getTransAmt())));
//				modelAndView.addObject("RealTransAmt", df.format(new BigDecimal(bean.getRealTransAmt())));
				
				BaseMapBean baseMapBean = new BaseMapBean();
	            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
	            baseMapBean.set(CustomConstants.APP_STATUS_DESC,"提现成功");
	            baseMapBean.set("amount",bean.getTransAmt());
	            baseMapBean.set("charge",bean.getRealTransAmt());
	            baseMapBean.setCallBackAction(CustomConstants.HOST+WXWithdrawDefine.JUMP_HTML_SUCCESS_PATH);
	            modelAndView.addObject("TransAmt", df.format(new BigDecimal(bean.getTransAmt())));
	            modelAndView.addObject("RealTransAmt", df.format(new BigDecimal(bean.getRealTransAmt())));
	            modelAndView.addObject("callBackForm",baseMapBean);
				
			} else if (ChinaPnrConstant.RESPCODE_CHECK.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
				modelAndView = new ModelAndView(WXWithdrawDefine.JUMP_HTML);
				BaseMapBean baseMapBean=new BaseMapBean();
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
				baseMapBean.set(CustomConstants.APP_STATUS_DESC, "汇付处理中，请稍后查询交易明细");
				baseMapBean.setCallBackAction(CustomConstants.HOST + WXWithdrawDefine.JUMP_HTML_HANDLING_PATH);
				modelAndView.addObject("callBackForm", baseMapBean);
				return modelAndView;
			} else {
				modelAndView = new ModelAndView(WXWithdrawDefine.JUMP_HTML);
				BaseMapBean baseMapBean=new BaseMapBean();
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
				baseMapBean.set(CustomConstants.APP_STATUS_DESC, "汇付处理中，请稍后查询交易明细");
				baseMapBean.setCallBackAction(CustomConstants.HOST + WXWithdrawDefine.JUMP_HTML_HANDLING_PATH);
				modelAndView.addObject("callBackForm", baseMapBean);
				return modelAndView;
			}
			
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
				// 用户userId
				Integer userId = userWithdrawService.selectUserIdByUsrcustid(bean.getLong(ChinaPnrConstant.PARAM_USRCUSTID));
				// 插值用参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("userId", String.valueOf(userId));
				// params.put("userName", userName);
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
//				System.out.println("提现后同步处理结束: 成功。。");
			} catch (Exception e) {
				// 执行结果(失败)
				status = ChinaPnrConstant.STATUS_FAIL;
//				System.out.println("提现后同步处理结束: 失败: " + e);
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
//			System.out.println("提现后同步处理结束: 失败le: " + reason);
		}

		// 更新状态记录
		if (updateFlag && Validator.isNotNull(uuid)) {
			this.userWithdrawService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
		}
		if (ChinaPnrConstant.STATUS_SUCCESS.equals(status)) {
			ModelAndView modelAndView = new ModelAndView(WXWithdrawDefine.JUMP_HTML);
			BaseMapBean baseMapBean=new BaseMapBean();
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, "汇付处理中，请稍后查询交易明细");
			baseMapBean.set("amount", bean.getTransAmt());
			baseMapBean.set("charge", bean.getRealTransAmt());
			baseMapBean.setCallBackAction(CustomConstants.HOST + WXWithdrawDefine.JUMP_HTML_SUCCESS_PATH);
			modelAndView.addObject("callBackForm", baseMapBean);
			modelAndView.addObject("TransAmt", df.format(new BigDecimal(bean.getTransAmt())));
			modelAndView.addObject("RealTransAmt", df.format(new BigDecimal(bean.getRealTransAmt())));
//			System.out.println("提现后同步处理结束: 成功,回调结束]");
			return modelAndView;
		} else if (ChinaPnrConstant.RESPCODE_CHECK.equals(status)) {
			ModelAndView modelAndView = new ModelAndView(WXWithdrawDefine.JUMP_HTML);
			BaseMapBean baseMapBean=new BaseMapBean();
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, "汇付处理中，请稍后查询交易明细");
			baseMapBean.setCallBackAction(CustomConstants.HOST + WXWithdrawDefine.JUMP_HTML_HANDLING_PATH);
			modelAndView.addObject("callBackForm", baseMapBean);
//			System.out.println("提现后同步处理结束: 999 处理中]");
			return modelAndView;
		} else {
			ModelAndView modelAndView = new ModelAndView(WXWithdrawDefine.JUMP_HTML);
			BaseMapBean baseMapBean=new BaseMapBean();
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, "汇付处理中，请稍后查询交易明细");
			baseMapBean.setCallBackAction(CustomConstants.HOST + WXWithdrawDefine.JUMP_HTML_HANDLING_PATH);
			modelAndView.addObject("callBackForm", baseMapBean);
//			System.out.println("提现后同步处理结束: 失败:]");
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
	@RequestMapping(WXWithdrawDefine.CALLBACK_MAPPING)
	public ModelAndView cashCallBack(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) {
		bean.convert();
		logger.info("...提现异步接口start");
//		System.out.println("提现后异步处理开始: 参数: " + bean == null ? "无" : bean.getAllParams() + "]");
		// 取得更新用UUID
		boolean updateFlag = false;
		String uuid = request.getParameter("uuid");
		if (Validator.isNotNull(uuid)) {
			// 取得检证数据
			ChinapnrExclusiveLogWithBLOBs record = userWithdrawService.selectChinapnrExclusiveLog(Long.parseLong(uuid));
			// 如果检证数据状态为未发送
			if (record != null && ChinaPnrConstant.STATUS_VERTIFY_OK.equals(record.getStatus())) {
				// 将状态更新成[2:处理中]
				record.setId(Long.parseLong(uuid));
				record.setStatus(ChinaPnrConstant.STATUS_RUNNING);
				int cnt = this.userWithdrawService.updateChinapnrExclusiveLog(record);
				if (cnt > 0) {
					updateFlag = true;
				}
			}
		} else {
			updateFlag = true;
		}
//		System.out.println(updateFlag);

		// 其他程序正在处理中,或者返回值错误
		if (!updateFlag) {
			ModelAndView modelAndView = new ModelAndView(WXWithdrawDefine.WITHDRAW_SUCCESS_PATH);
			modelAndView.addObject("TransAmt", df.format(new BigDecimal(bean.getTransAmt())));
			modelAndView.addObject("RealTransAmt", df.format(new BigDecimal(bean.getRealTransAmt())));
			logger.info("提现后异步处理结束: 提现成功...");
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
				Integer userId = userWithdrawService.selectUserIdByUsrcustid(bean.getLong(ChinaPnrConstant.PARAM_USRCUSTID));
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
//					System.out.println("提现后异步处理结束: 成功");
				}else{
					status = ChinaPnrConstant.STATUS_FAIL;
				}
				
			} catch (Exception e) {
				// 执行结果(失败)
				status = ChinaPnrConstant.STATUS_FAIL;
//				System.out.println("提现后异步处理结束: 提现失败: " + e);
				logger.error("提现后异步处理结束: 提现失败");
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
			this.userWithdrawService.updateAccountWithdrawByOrdId(bean.getOrdId(), reason);
//			System.out.println("提现后异步处理结束: 提现失败了: " + reason);
			logger.error("提现后异步处理结束: 提现失败, reason is" + reason);
		}

		// 更新状态记录
		if (updateFlag && Validator.isNotNull(uuid)) {
			this.userWithdrawService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
		}
		if (ChinaPnrConstant.STATUS_SUCCESS.equals(status)) {
			ModelAndView modelAndView = new ModelAndView(WXWithdrawDefine.WITHDRAW_SUCCESS_PATH);
			modelAndView.addObject("TransAmt", df.format(new BigDecimal(bean.getTransAmt())));
			modelAndView.addObject("RealTransAmt", df.format(new BigDecimal(bean.getRealTransAmt())));
			logger.info("提现后异步处理结束: 提现成功,回调结束");
			return modelAndView;
		} else if (ChinaPnrConstant.RESPCODE_CHECK.equals(status)) {
			ModelAndView modelAndView = new ModelAndView(WXWithdrawDefine.WITHDRAW_INPROCESS_PATH);
			modelAndView.addObject("message", "汇付处理中，请稍后查询交易明细");
			logger.info("提现后异步处理结束: 提现交易正在处理中,回调结束");
			return modelAndView;
		} else {
			ModelAndView modelAndView = new ModelAndView(WXWithdrawDefine.WITHDRAW_INPROCESS_PATH);
			modelAndView.addObject("message", "汇付处理中，请稍后查询交易明细");
			logger.info("提现后异步处理结束: 提现不成功: ");
			return modelAndView;
		}
	}


	
    private ModelAndView getErrorModelAndView(ResultEnum param, String sign) {
        ModelAndView modelAndView = new ModelAndView(AutoPlusDefine.JUMP_HTML);
        BaseMapBean baseMapBean = new BaseMapBean();
        baseMapBean.set(CustomConstants.APP_STATUS, param.getStatus());
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, param.getStatusDesc());
        baseMapBean.set(CustomConstants.APP_SIGN, sign);
        baseMapBean.setCallBackAction(CustomConstants.HOST + AutoPlusDefine.JUMP_HTML_ERROR_PATH);
        modelAndView.addObject("callBackForm", baseMapBean);
        return modelAndView;
    }
    
    private ModelAndView getSuccessModelAndView(String sign) {
        ModelAndView modelAndView = new ModelAndView(AutoPlusDefine.JUMP_HTML);
        BaseMapBean baseMapBean = new BaseMapBean();
        baseMapBean.set(CustomConstants.APP_STATUS, ResultEnum.SUCCESS.getStatus());
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, ResultEnum.SUCCESS.getStatusDesc());
        baseMapBean.set(CustomConstants.APP_SIGN, sign);
        baseMapBean.setCallBackAction(CustomConstants.HOST + AutoPlusDefine.JUMP_HTML_ERROR_PATH);
        modelAndView.addObject("callBackForm", baseMapBean);
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
	 * 获取提现规则H5页面
	 * 
	 * @author renxingchen
	 * @return
	 */
	@RequestMapping(WXWithdrawDefine.GET_WITHDRAW_RULE_MAPPING)
	public ModelAndView withDrawRule() {
		return new ModelAndView(WXWithdrawDefine.WITHDRAW_RULE_PATH);
	}

}
