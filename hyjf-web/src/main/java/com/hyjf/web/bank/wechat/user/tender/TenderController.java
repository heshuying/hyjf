/**
 * Description:出借控制器
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 * Created at: 2015年12月4日 下午2:32:36
 * Modification History:
 * Modified by :
 */
package com.hyjf.web.bank.wechat.user.tender;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.tender.TenderService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.*;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.web.BaseController;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @package com.hyjf.web.borrow.invest
 * @author 郭勇
 * @date 2015/12/04 14:00
 * @version V1.0  
 */
@Controller(TenderDefine.CONTROLLER_NAME)
@RequestMapping(value = TenderDefine.REQUEST_MAPPING)
public class TenderController extends BaseController {
	
	Logger _log = LoggerFactory.getLogger(TenderController.class);

	@Autowired
	private TenderService tenderService;

	/** 当前controller名称 */
	public static final String THIS_CLASS = TenderController.class.getName();

	/**
	 * 获取相应的待投金额
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = TenderDefine.GET_BORROW_ACCOUNT_WAIT_ACTION, produces = "application/json; charset=UTF-8")
	public String getBorrowAccountWait(HttpServletRequest request, HttpServletResponse response) {

		TenderAjaxResultBean result = new TenderAjaxResultBean();
		String borrowNid = request.getParameter("borrowNid");
		if (StringUtils.isBlank(borrowNid)) {
			result.setError(TenderDefine.ERROR_FAIL);
			result.setErrorDesc("请求参数不可为空");
			return JSONObject.toJSONString(result, true);
		}
		String wait = null;
		try {
			wait = RedisUtils.get(borrowNid);
		} catch (Exception e) {
			result.setError(TenderDefine.ERROR_FAIL);
			result.setErrorDesc("请稍后再试");
			return JSONObject.toJSONString(result, true);
		}
		if (StringUtils.isBlank(wait)) {
			wait = "0.00";
		}
		result.setError(TenderDefine.ERROR_SUCCESS);
		result.setWait(wait);
		return JSONObject.toJSONString(result, true);
	}

	/**
	 * 
	 * 根据出借项目id获取出借信息
	 * 
	 * @author 王坤
	 * @param request
	 * @param response
	 * @return
	 */
	// @ResponseBody
	// @RequestMapping(value = TenderDefine.GET_INVEST_INFO_MAPPING, produces =
	// "application/json; charset=UTF-8")
	// public String getInvestInfo(HttpServletRequest request,
	// HttpServletResponse response) {
	//
	// String borrowNid = request.getParameter("borrowNid");
	// String money = request.getParameter("money");
	// BigDecimal earnings = BigDecimal.ZERO;
	// TenderAjaxResultBean result = new TenderAjaxResultBean();
	// // 查询项目信息
	// Borrow borrow = tenderService.getBorrowByNid(borrowNid);
	// if (null != borrow) {
	// String balanceWait = borrow.getBorrowAccountWait() + "";
	// if (balanceWait == null || balanceWait.equals("")) {
	// balanceWait = "0";
	// }
	// // 刚加载页面并且可投小于起投
	// if ((StringUtils.isBlank(money) || money.equals("0")) && new
	// BigDecimal(balanceWait).compareTo(new
	// BigDecimal(borrow.getTenderAccountMin())) < 1) {
	// money = new BigDecimal(balanceWait).intValue() + "";
	// }
	// if (!StringUtils.isBlank(money) && Long.parseLong(money) > 0) {//
	// 如果出借金额不为空
	// String borrowStyle = borrow.getBorrowStyle();
	// // 收益率
	// BigDecimal borrowApr = borrow.getBorrowApr();
	// if (borrow.getProjectType() == 13 && borrow.getBorrowExtraYield() != null
	// && borrow.getBorrowExtraYield().compareTo(BigDecimal.ZERO) > 0) {
	// borrowApr = borrowApr.add(borrow.getBorrowExtraYield());
	// }
	// // 周期
	// Integer borrowPeriod = borrow.getBorrowPeriod();
	// switch (borrowStyle) {
	// case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
	// // 计算历史回报
	// earnings = DuePrincipalAndInterestUtils.getMonthInterest(new
	// BigDecimal(money), borrowApr.divide(new BigDecimal("100")),
	// borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
	// break;
	// case CalculatesUtil.STYLE_ENDDAY://
	// 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷360*天数；
	// earnings = DuePrincipalAndInterestUtils.getDayInterest(new
	// BigDecimal(money), borrowApr.divide(new BigDecimal("100")),
	// borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
	// break;
	// case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：历史回报=出借金额*年化收益÷12*月数；
	// earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(new
	// BigDecimal(money), borrowApr.divide(new BigDecimal("100")), borrowPeriod,
	// borrowPeriod).setScale(2,
	// BigDecimal.ROUND_DOWN);
	// break;
	// case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：历史回报=出借金额*年化收益÷12*月数；
	// earnings = AverageCapitalPlusInterestUtils.getInterestCount(new
	// BigDecimal(money), borrowApr.divide(new BigDecimal("100")),
	// borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
	// break;
	// case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
	// earnings = AverageCapitalUtils.getInterestCount(new BigDecimal(money),
	// borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2,
	// BigDecimal.ROUND_DOWN);
	// break;
	// default:
	// break;
	// }
	// }
	// }
	// result.setError(TenderDefine.ERROR_SUCCESS);
	// result.setEarnings(earnings.toString());
	// return JSONObject.toJSONString(result, true);
	// }

	/**
	 * 微信出借验证
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = TenderDefine.INVEST_CHECK_ACTION, produces = "application/json; charset=UTF-8")
	public String tenderCheck(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(THIS_CLASS, TenderDefine.INVEST_CHECK_ACTION);
		TenderAjaxResultBean result = new TenderAjaxResultBean();
		String borrowNid = request.getParameter("borrowNid");// 借款borrowNid,如HBD120700101
		String account = request.getParameter("money");// 出借金额
		String couponGrantId = request.getParameter("couponGrantId");// 优惠券id
		String userId = request.getParameter("userId");// 用户id

		CouponConfigCustomizeV2 cuc = null;
		if (StringUtils.isNotEmpty(couponGrantId) && !StringUtils.equals("-1", couponGrantId)) {
			cuc = tenderService.getCouponUser(couponGrantId, Integer.parseInt(userId));
		}
		if (!(cuc != null && (cuc.getCouponType() == 3 || cuc.getCouponType() == 1))) {
			if (StringUtils.isBlank(account)) {
				result.setError(TenderDefine.ERROR_FAIL);
				result.setErrorDesc("出借金额不可为空");
				return JSON.toJSONString(result, true);
			}
		} else {
			if (StringUtils.isBlank(account)) {
				account = "0";
			}
		}

		if (StringUtils.isBlank(borrowNid)) {
			result.setError(TenderDefine.ERROR_FAIL);
			result.setErrorDesc("项目编号不可为空");
			return JSON.toJSONString(result, true);
		}
		if (StringUtils.isBlank(userId)) {
			result.setError(TenderDefine.ERROR_FAIL);
			result.setErrorDesc("用户id不可为空");
			return JSON.toJSONString(result, true);
		}
		// 校验
		JSONObject checkResult = this.tenderService.checkParam(borrowNid, account, Integer.parseInt(userId), CustomConstants.CLIENT_WECHAT, couponGrantId);
		// 参数验证成功，则返回url，否则不返回
		if (checkResult == null || checkResult.get("error") == null) {
			result.setError(TenderDefine.ERROR_FAIL);
			result.setErrorDesc("出借失败，校验不通过");
			return JSON.toJSONString(result, true);
		}
		// 校验失败
		if (!checkResult.get("error").equals("0")) {
			result.setError(TenderDefine.ERROR_FAIL);
			result.setErrorDesc(checkResult.getString("data"));
			return JSON.toJSONString(result, true);
		}
		// 返回地址
		String returl = CustomConstants.HOST + request.getContextPath() + TenderDefine.REQUEST_MAPPING + TenderDefine.INVEST_ACTION + ".do";

		// 校验成功
		if (StringUtils.isNotEmpty(couponGrantId) && !couponGrantId.equals("-1")) {
			// 调用 优惠券出借校验接口
			JSONObject couponCheck = CommonSoaUtils.CheckCoupon(userId, borrowNid, account, "1", couponGrantId);
			LogUtil.infoLog(THIS_CLASS, "updateCouponTender", "优惠券出借校验结果：" + couponCheck);
			_log.info("updateCouponTender" + "优惠券出借校验结果：" + couponCheck);
			if (MapUtils.isNotEmpty(couponCheck)) {
				if (couponCheck.get("status") != null && (couponCheck.get("status") + "").equals("0")) {
					result.setError(TenderDefine.ERROR_SUCCESS);
					result.setErrorDesc("校验成功");
					result.setUrl(returl);
				} else {
					result.setError(TenderDefine.ERROR_FAIL);
					result.setErrorDesc(couponCheck.getString("statusDesc"));
				}
			} else {
				result.setError(TenderDefine.ERROR_FAIL);
				result.setErrorDesc("出借失败");
			}
		} else {
			result.setError(TenderDefine.ERROR_SUCCESS);
			result.setErrorDesc("校验成功");
			result.setUrl(returl);
		}

		String data = JSON.toJSONString(result, true);
		LogUtil.endLog(THIS_CLASS, TenderDefine.INVEST_CHECK_ACTION);
		return data;
	}

	/**
	 * 微信出借
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = TenderDefine.INVEST_ACTION)
	public ModelAndView tender(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, TenderDefine.INVEST_ACTION);

		ModelAndView modelAndView = new ModelAndView();
		String borrowNid = request.getParameter("borrowNid");// 借款borrowNid,如HBD120700101
		String account = request.getParameter("money");// 出借金额
		String userId = StringUtils.isBlank(request.getParameter("userId")) ? "0" : request.getParameter("userId");// 用户id
		String callback = request.getParameter("callback");// 返回地址
		// 出借金额
		if (account == null || "".equals(account)) {
			account = "0";
		}
		// add by zhangjp 优惠券出借 start
		// 优惠券用户id （coupon_user的id）
		String couponGrantId = request.getParameter("couponGrantId");
		_log.info("couponGrantId:" + couponGrantId);
		// 排他校验时间
		int couponOldTime = Integer.MIN_VALUE;
		CouponConfigCustomizeV2 cuc = null;
		// -1:有可用的优惠券，但是出借时不选择优惠券 空或null：用户没有可用优惠券
		if (StringUtils.isNotEmpty(couponGrantId) && !StringUtils.equals("-1", couponGrantId)) {
			cuc = tenderService.getCouponUser(couponGrantId, Integer.parseInt(userId));
			// 排他check用
			couponOldTime = cuc.getUserUpdateTime();
		}
		// add by zhangjp 优惠券出借 end
		BigDecimal decimalAccount = StringUtils.isNotEmpty(account) ? new BigDecimal(account) : BigDecimal.ZERO;
		if (decimalAccount.compareTo(BigDecimal.ZERO) != 1 && cuc != null && (cuc.getCouponType() == 3 || cuc.getCouponType() == 1)) {
			// 优惠券出借调用
			JSONObject resultcoupon = CommonSoaUtils.CouponInvestForPC(userId, borrowNid, account, "1", couponGrantId, "", GetCilentIP.getIpAddr(request), couponOldTime + "");
			if (MapUtils.isNotEmpty(resultcoupon)) {
				if ((resultcoupon.get("status") + "").equals("0")) {
					Borrow borrow = tenderService.getBorrowByNid(borrowNid);
					// 取得项目类别
					BorrowProjectType borrowProjectType = tenderService.getBorrowProjectType(borrow.getProjectType().toString());
					/** 修改出借成功页面显示修改开始 */
					String projectTypeName = tenderService.getProjectTypeName(borrowProjectType);
					// 跳转到成功画面
					Map<String, String> map = new HashMap<String, String>();
					map.put("error", "0");
					map.put("errorDesc", "优惠券出借成功");
					map.put("borrowNid", borrowNid);
					map.put("projectTypeName", projectTypeName);
					map.put("couponInterest", resultcoupon.getString("couponInterest")); // 优惠券收益
					map.put("couponType", resultcoupon.getString("couponType"));// 优惠券类别
					map.put("couponQuota", resultcoupon.getString("couponQuota"));// 优惠券额度
					String url = JSON.toJSONString(map);
					try {
						url = URLEncoder.encode(url, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						_log.info("==============url编码异常=======");
					}
					return new ModelAndView("redirect:" + callback + "backinfo/" + url);
				} else {
					Map<String, String> map = new HashMap<String, String>();
					map.put("error", "1");
					map.put("errorDesc", resultcoupon.get(CustomConstants.APP_STATUS_DESC) + "");
					String url = JSON.toJSONString(map);
					try {
						url = URLEncoder.encode(url, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						_log.info("================url编码异常=========");
					}
					return new ModelAndView("redirect:" + callback + "backinfo/" + url);
				}

			} else {
				Map<String, String> map = new HashMap<String, String>();
				map.put("error", "1");
				map.put("errorDesc", "优惠券出借失败！");
				String url = JSON.toJSONString(map);
				try {
					url = URLEncoder.encode(url, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					_log.info("================url编码异常=========");
				}
				return new ModelAndView("redirect:" + callback + "backinfo/" + url);
			}
		}
		JSONObject result = this.tenderService.checkParam(borrowNid, account, Integer.parseInt(userId), "1", couponGrantId);
		// 参数验证成功，则返回url，否则不返回
		Map<String, String> map = new HashMap<String, String>();
		if (result == null) {
			map.put("error", "1");
			map.put("errorDesc", "出借失败");
			String url = JSON.toJSONString(map);
			try {
				url = URLEncoder.encode(url, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				_log.info("================url编码异常=========");
			}
			return new ModelAndView("redirect:" + "backinfo/" + callback + url);
		}
		if (result.get("error") != null && result.get("error").equals("1")) {
			String url = JSON.toJSONString(result);
			try {
				url = URLEncoder.encode(url, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				_log.info("================url编码异常=========");
			}
			return new ModelAndView("redirect:" + callback + "backinfo/" + url);
		}
		// String borrowId = result.getString("borrowId");
		String tenderUserName = result.getString("tenderUserName");
		String tenderUsrcustid = result.getString("tenderUsrcustid");
		// 生成订单
		String orderId = GetOrderIdUtils.getOrderId2(Integer.valueOf(userId));
		// 写日志
		Boolean flag = false;
		try {
			flag = tenderService.updateTenderLog(borrowNid, orderId, Integer.valueOf(userId), account, GetCilentIP.getIpAddr(request), couponGrantId,tenderUserName);
		} catch (Exception e2) {
			//TODO 跳转出借错误页面
			_log.info("=================写入日志失败!===========");
		}
		// 成功后调用出借接口
		if (flag) {
			/** userId和callback不能替换,如果需要添加参数，请添加于callback前，王坤 20160812 耗费时间8H */
			// 获取共同参数
			String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
			String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
			// 回调路径
			String retUrl = CustomConstants.HOST + TenderDefine.REQUEST_MAPPING + TenderDefine.RETURL_SYN_ACTION + ".do?couponGrantId=" + couponGrantId + "&couponOldTime=" + couponOldTime
					+ "&callback=" + callback;

			// 异步回调路径
			String notifyUrl = CustomConstants.HOST + TenderDefine.REQUEST_MAPPING + TenderDefine.RETURL_ASY_ACTION + ".do?couponGrantId=" + couponGrantId + "&couponOldTime=" + couponOldTime
					+ "&callback=" + callback;
			BankCallBean bean = new BankCallBean();
			bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
			bean.setTxCode(BankCallConstant.TXCODE_BID_APPLY);// 交易代码
			bean.setInstCode(instCode);
			bean.setBankCode(bankCode);
			bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
			bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
			bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
			bean.setChannel(BankCallConstant.CHANNEL_WEI);// 交易渠道
			bean.setAccountId(tenderUsrcustid);// 电子账号
			bean.setOrderId(orderId);// 订单号
			bean.setTxAmount(CustomUtil.formatAmount(account));// 交易金额
			bean.setProductId(borrowNid);// 标的号
			bean.setFrzFlag(BankCallConstant.DEBT_FRZFLAG_UNFREEZE);// 是否冻结金额
			//微信忘记交易密码url
			String forgetPassUrl = CustomConstants.WECHAT_FORGET_PASSWORD_URL+"?userId="+userId+"&callback="+CustomConstants.WECHAT_FORGET_PASSWORD_CALLBACK_URL;
			bean.setForgotPwdUrl(forgetPassUrl);
			bean.setSuccessfulUrl(retUrl+"&isSuccess=1");// 银行同步返回地址
			bean.setRetUrl(retUrl);// 银行同步返回地址
			bean.setNotifyUrl(notifyUrl);// 银行异步返回地址
			bean.setLogOrderId(orderId);// 订单号
			bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单日期
			bean.setLogIp(GetCilentIP.getIpAddr(request));// 客户IP
			bean.setLogUserId(String.valueOf(userId));// 出借用户
			bean.setLogUserName(tenderUserName);// 出借用户名
			bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_MOBILE_BIDAPPLY);// 银行请求详细url
			try {
				modelAndView = BankCallUtils.callApi(bean);
			} catch (Exception e) {
				_log.info("================调用银行接口异常=========");
				map.put("error", "1");
				map.put("errorDesc", "出借失败");
				String url = JSON.toJSONString(map);
				try {
					url = URLEncoder.encode(url, "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					_log.info("================url编码异常=========");
				}
				return new ModelAndView("redirect:" + callback + "backinfo/" + url);
			}
		}
		LogUtil.endLog(THIS_CLASS, TenderDefine.INVEST_ACTION);
		return modelAndView;
	}

	/**
	 * 同步回调结果
	 * 
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@RequestMapping(TenderDefine.RETURL_SYN_ACTION)
	public String tenderRetUrl(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BankCallBean bean) {

		_log.info("微信出借同步开始！");
		String error = "1";
		String errorDesc = "出借失败";
		String callback = request.getParameter("callback");
		String logOrderId = StringUtils.isBlank(bean.getLogOrderId()) ? "" : bean.getLogOrderId();// 订单号
		int userId = StringUtils.isBlank(bean.getLogUserId()) ? 0 : Integer.parseInt(bean.getLogUserId());// 用户Userid
		String ip = bean.getUserIP();// 用户操作ip
		String couponGrantId = request.getParameter("couponGrantId");// 优惠券用户id
		String couponOldTime = StringUtils.isNotEmpty(request.getParameter("couponOldTime")) ? request.getParameter("couponOldTime") : "0";
		String respCode = bean.getRetCode();// 出借结果返回码
		// 是否用优惠券 0没用 1 用了
		String couponFlag = "0";
		
		String isSuccess = request.getParameter("isSuccess");// 优惠券用户id
		_log.info("微信用户:" + userId + "***出借接口isSuccess：" + isSuccess);
		// 打印返回码
		_log.info("微信用户:" + userId + "***出借接口结果码：" + respCode);

		if (StringUtils.isBlank(respCode)||!"1".equals(isSuccess)) {
			return returnUrl(error, errorDesc, callback, "", "", "", 0, "", "", "");
		}
		if (!BankCallConstant.RESPCODE_SUCCESS.equals(respCode)&&!"1".equals(isSuccess)) {
			// 返回码提示余额不足
			if (BankCallConstant.RETCODE_BIDAPPLY_YUE_FAIL.equals(respCode)) {
				_log.info("微信用户:" + userId + "**出借接口调用失败，余额不足，错误码：" + respCode);
				errorDesc = "出借失败，可用余额不足！请联系客服.";
				return returnUrl(error, errorDesc, callback, "", "", "", 0, "", "", "");
			} else {
				errorDesc = bean.getRetMsg();
				_log.info("微信用户:" + userId + "**出借接口调用失败,系统订单号：" + logOrderId + "**调用解冻接口**汇付接口返回错误码" + respCode);
				return returnUrl(error, errorDesc, callback, "", "", "", 0, "", "", "");
			}
		}
		bean.convert();
		String borrowId = bean.getProductId();// 借款Id
		String account = bean.getTxAmount();// 借款金额
		String orderId = bean.getOrderId();// 订单id
		_log.info("============cwyang bean.getLogClient = " + bean.getLogClient());
		bean.setLogClient(1);// modify by cwyang 2017-5-4 设置微信出借的出借平台为微信端
		// 根据借款Id检索标的信息
		BorrowWithBLOBs borrow = this.tenderService.getBorrowByNid(borrowId);
		String borrowNid = borrowId == null ? "" : borrow.getBorrowNid();// 项目编号
		if (StringUtils.isBlank(borrowNid)) {
			errorDesc = "回调时,borrowNid为空";
			return returnUrl(error, errorDesc, callback, "", "", "", 0, "", "", "");
		}
		// 出借状态
		String status = BankCallConstant.STATUS_FAIL;

		JSONObject resultcoupon = new JSONObject();
		try {
			// 进行出借 tendertmp锁住
			JSONObject tenderResult = this.tenderService.userTender(borrow, bean);
			// 出借成功
			if (tenderResult.getString("status").equals("1")) {
				_log.info("微信用户:" + userId + "***出借成功：" + account);
				error = "0";
				errorDesc = "恭喜您出借成功!";
				status = BankCallConstant.STATUS_SUCCESS;
				if (StringUtils.isNotEmpty(couponGrantId) && !"-1".equals(couponGrantId)) {
					// 优惠券出借校验
					try {
						// 优惠券出借校验
						JSONObject couponCheckResult = CommonSoaUtils.CheckCoupon(userId + "", borrowNid, account, CustomConstants.CLIENT_WECHAT, couponGrantId);
						int couponStatus = couponCheckResult.getIntValue("status");
						String statusDesc = couponCheckResult.getString("statusDesc");
						_log.info("updateCouponTender" + "优惠券出借校验结果：" + statusDesc);
						if (couponStatus == 0) {
							// 优惠券出借
							resultcoupon = CommonSoaUtils.CouponInvestForPC(userId + "", borrowNid, account, CustomConstants.CLIENT_WECHAT, couponGrantId, orderId, ip, couponOldTime);

							couponFlag = "1";
						}
					} catch (Exception e) {
						LogUtil.infoLog(TenderController.class.getName(), "tenderRetUrl", "优惠券出借失败");
					}
				}
				// 如果是融通宝项目且存在加息
				if (borrow.getProjectType() == 13) {
					if (borrow.getBorrowExtraYield() != null && borrow.getBorrowExtraYield().compareTo(new BigDecimal("0")) > 0) {
						tenderService.extraUeldInvest(borrow, bean);
					}
				}
			}
			// 出借失败 回滚redis
			else {
				// 更新tendertmp
				boolean updateFlag = tenderService.updateBorrowTenderTmp(userId, borrowNid, orderId);
				// 更新失败，出借失败
				if (updateFlag) {
					if(tenderResult.getString("status").equals("-1")){//同步/异步 优先执行完毕
						//add by cwyang 增加redis防重校验 2017-08-02
						boolean checkTender = RedisUtils.tranactionSet("tendersuccess_orderid" + orderId, 20);
						if(!checkTender){//同步/异步 优先执行完毕
							if (StringUtils.isNotEmpty(couponGrantId)) {
								couponFlag = "1";
							}
							error = "0";
							errorDesc = "恭喜您出借成功!";
							status = BankCallConstant.STATUS_SUCCESS;
						}else{
							errorDesc = "投标失败,请联系客服人员!";
						}
						
					}else{
						// 出借失败,出借撤销
						try {
							boolean flag = tenderService.bidCancel(userId, borrowId, orderId, account);
							if (!flag) {
								errorDesc = "投标失败,请联系客服人员!";
							}
						} catch (Exception e) {
							_log.info("==============投标申请撤销=========");
							errorDesc = "投标申请撤销失败,请联系客服人员!";
						}
					}
				} else {
					if (StringUtils.isNotEmpty(couponGrantId)) {
						couponFlag = "1";
					}
					error = "0";
					errorDesc = "恭喜您出借成功!";
					status = BankCallConstant.STATUS_SUCCESS;
				}
			}
		} catch (Exception e) {
			// 更新tendertmp
			boolean updateFlag = tenderService.updateBorrowTenderTmp(userId, borrowNid, orderId);
			// 更新失败，出借失败
			if (updateFlag) {
				// 出借失败,出借撤销
				try {
					boolean flag = tenderService.bidCancel(userId, borrowId, orderId, account);
					if (!flag) {
						errorDesc = "投标失败,请联系客服人员!";
					}
				} catch (Exception e1) {
					_log.info("==============投标申请撤销=============");
					errorDesc = "投标申请撤销失败,请联系客服人员!";
				}
			} else {
				error = "0";
				errorDesc = "恭喜您出借成功!";
				status = BankCallConstant.STATUS_SUCCESS;
			}
		}
		if (status.equals(ChinaPnrConstant.STATUS_FAIL)) {
			return returnUrl(error, errorDesc, callback, "", "", "", 0, "", "", "");
		} else {
			String result = "";
			BigDecimal earnings = new BigDecimal("0");
			DecimalFormat df = CustomConstants.DF_FOR_VIEW;
			// 如果出借金额不为空
			if (!StringUtils.isBlank(account) && Double.parseDouble(account) > 0) {
				String borrowStyle = borrow.getBorrowStyle();
				// 收益率
				BigDecimal borrowApr = borrow.getBorrowApr();
				// 融通宝收益叠加
				if (borrow.getProjectType() == 13 && borrow.getBorrowExtraYield() != null && borrow.getBorrowExtraYield().compareTo(BigDecimal.ZERO) > 0) {
					borrowApr = borrowApr.add(borrow.getBorrowExtraYield());
				}
				// 周期
				Integer borrowPeriod = borrow.getBorrowPeriod();
				switch (borrowStyle) {
				case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
					// 计算历史回报
					earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(account), borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					break;
				case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷360*天数；
					earnings = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(account), borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					break;
				case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：历史回报=出借金额*年化收益÷12*月数；
					earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(account), borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2,
							BigDecimal.ROUND_DOWN);
					break;
				case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：历史回报=出借金额*年化收益÷12*月数；
					earnings = AverageCapitalPlusInterestUtils.getInterestCount(new BigDecimal(account), borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					break;
				case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
					earnings = AverageCapitalUtils.getInterestCount(new BigDecimal(account), borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
					break;
				default:
					break;
				}
				result = df.format(earnings);
			}
			int projectType = 0;
			if (borrow != null && borrow.getProjectType() != null) {
				projectType = borrow.getProjectType();
			}
			// 判断一下优惠券是否已被使用(由于异步调用可能在同步调用之前执行,导致无法获得优惠券的使用情况,所以这里需要重新获取一下)并且同步调用未进行优惠券出借
			if (StringUtils.isNotEmpty(couponGrantId)) {
				try {
					Thread.sleep(500);
					_log.info("==================cwyang 异步调用优先执行,重新获取优惠券信息.============");
					resultcoupon = tenderService.getCouponIsUsed(orderId,couponGrantId, userId);
					if (!CustomConstants.RESULT_SUCCESS.equals(resultcoupon.getString("isSuccess"))) {
						_log.info("====================cwyang 获取优惠券信息失败!==================");
						resultcoupon = new JSONObject();
					}
				} catch (Exception e) {
					_log.info("====================cwyang 获取优惠券信息异常!==================");
				}
			}
			String couponInterest = resultcoupon.getString("couponInterest"); // 优惠券收益
			String couponType = resultcoupon.getString("couponType");// 优惠券类别
			String couponQuota = resultcoupon.getString("couponQuota");// 优惠券额度
			return returnUrl(error, errorDesc, callback, result, account, couponFlag, projectType, couponInterest, couponType, couponQuota);
		}
	}

	/**
	 * 出借异步回调结果
	 * 
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(TenderDefine.RETURL_ASY_ACTION)
	public String tenderBgtUrl(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BankCallBean bean) {

		_log.info("微信出借异步回调开始！");
		String logOrderId = StringUtils.isBlank(bean.getLogOrderId()) ? "" : bean.getLogOrderId();// 订单号
		int userId = StringUtils.isBlank(bean.getLogUserId()) ? 0 : Integer.parseInt(bean.getLogUserId());// 用户Userid
		String ip = bean.getUserIP();// 用户操作ip
		String couponGrantId = request.getParameter("couponGrantId");// 优惠券用户id
		String couponOldTime = StringUtils.isNotEmpty(request.getParameter("couponOldTime")) ? request.getParameter("couponOldTime") : "0";
		String respCode = bean.getRetCode();// 出借结果返回码
		BankCallResult result = new BankCallResult();
		// 打印返回码
		_log.info("微信用户:" + userId + "***出借接口结果码：" + respCode);
		String errorDesc = "出借失败";
		if (StringUtils.isBlank(respCode)) {
			result.setMessage(errorDesc);
			return JSONObject.toJSONString(result, true);
		}
		if (!BankCallConstant.RESPCODE_SUCCESS.equals(respCode)) {
			// 返回码提示余额不足
			if (BankCallConstant.RETCODE_BIDAPPLY_YUE_FAIL.equals(respCode)) {
				_log.info("微信用户:" + userId + "**出借接口调用失败，余额不足，错误码：" + respCode);
				errorDesc = "出借失败，可用余额不足！请联系客服.";
				result.setMessage(errorDesc);
				return JSONObject.toJSONString(result, true);
			} else {
				errorDesc = bean.getRetMsg();
				_log.info("微信用户:" + userId + "**出借接口调用失败,系统订单号：" + logOrderId + "**调用解冻接口**汇付接口返回错误码" + respCode);
				result.setMessage(errorDesc);
				return JSONObject.toJSONString(result, true);

			}
		}
		bean.convert();
		String borrowId = bean.getProductId();// 借款Id
		String account = bean.getTxAmount();// 借款金额
		String orderId = bean.getOrderId();// 订单id
		_log.info("============cwyang bean.getLogClient = " + bean.getLogClient());
		bean.setLogClient(1);// modify by cwyang 2017-5-4 设置微信出借的出借平台为微信端
		// 根据借款Id检索标的信息
		BorrowWithBLOBs borrow = this.tenderService.getBorrowByNid(borrowId);
		String borrowNid = borrowId == null ? "" : borrow.getBorrowNid();// 项目编号
		_log.info("出借同步回调" + bean.getAllParams().toString());
		_log.info("标号:" + borrowNid);
		if (StringUtils.isBlank(borrowNid)) {
			errorDesc = "回调时,borrowNid为空";
			result.setMessage(errorDesc);
			return JSONObject.toJSONString(result, true);
		}
		try {
			// 进行出借 tendertmp锁住
			JSONObject tenderResult = this.tenderService.userTender(borrow, bean);
			// 出借成功
			if (tenderResult.getString("status").equals("1")) {
				_log.info("微信用户:" + userId + "***出借成功：" + account);
				errorDesc = "恭喜您出借成功!";
				if (StringUtils.isNotEmpty(couponGrantId)) {
					// 优惠券出借校验
					try {
						// 优惠券出借校验
						JSONObject couponCheckResult = CommonSoaUtils.CheckCoupon(userId + "", borrowNid, account, CustomConstants.CLIENT_WECHAT, couponGrantId);
						int couponStatus = couponCheckResult.getIntValue("status");
						String statusDesc = couponCheckResult.getString("statusDesc");
						_log.info("updateCouponTender" + "优惠券出借校验结果：" + statusDesc);
						if (couponStatus == 0) {
							// 优惠券出借
							CommonSoaUtils.CouponInvestForPC(userId + "", borrowNid, account, CustomConstants.CLIENT_WECHAT, couponGrantId, orderId, ip, couponOldTime);
						}
					} catch (Exception e) {
						LogUtil.infoLog(TenderController.class.getName(), "tenderRetUrl", "优惠券出借失败");
					}
				}
				// 如果是融通宝项目且存在加息
				if (borrow.getProjectType() == 13) {
					if (borrow.getBorrowExtraYield() != null && borrow.getBorrowExtraYield().compareTo(new BigDecimal("0")) > 0) {
						tenderService.extraUeldInvest(borrow, bean);
					}
				}
			}
			// 出借失败 回滚redis
			else {
				// 更新tendertmp
				boolean updateFlag = tenderService.updateBorrowTenderTmp(userId, borrowNid, orderId);
				// 更新失败，出借失败
				if (updateFlag) {
					if(tenderResult.getString("status").equals("-1")){//同步/异步 优先执行完毕
						//add by cwyang 增加redis防重校验 2017-08-02
						boolean checkTender = RedisUtils.tranactionSet("tendersuccess_orderid" + orderId, 20);
						if(!checkTender){//同步/异步 优先执行完毕
							errorDesc = "恭喜您出借成功!";
							boolean updateTenderFlag = this.tenderService.updateTender(userId, borrowNid, orderId, bean);
							if (!updateTenderFlag) {
								errorDesc = "投标出現错误,请联系客服人员!";
							}
						}else{
							errorDesc = "投标失败,请联系客服人员!";
						}
						
						
						
					}else{
						// 出借失败,出借撤销
						try {
							boolean flag = tenderService.bidCancel(userId, borrowId, orderId, account);
							if (!flag) {
								errorDesc = "投标失败,请联系客服人员!";
							}
						} catch (Exception e) {
							_log.info("==============投标申请撤销=======");
							errorDesc = "投标申请撤销失败,请联系客服人员!";
						}
					}
					
				} else {
					errorDesc = "恭喜您出借成功!";
					boolean updateTenderFlag = this.tenderService.updateTender(userId, borrowNid, orderId, bean);
					if (!updateTenderFlag) {
						errorDesc = "投标出現错误,请联系客服人员!";
					}
				}
			}
		} catch (Exception e) {
			// 更新tendertmp
			boolean updateFlag = tenderService.updateBorrowTenderTmp(userId, borrowNid, orderId);
			// 更新失败，出借失败
			if (updateFlag) {
				// 出借失败,出借撤销
				try {
					boolean flag = tenderService.bidCancel(userId, borrowId, orderId, account);
					if (!flag) {
						errorDesc = "投标失败,请联系客服人员!";
					}
				} catch (Exception e1) {
					_log.info("==============投标申请撤销=======");
					errorDesc = "投标申请撤销失败,请联系客服人员!";
				}
			} else {
				errorDesc = "恭喜您出借成功!";
				boolean updateTenderFlag = this.tenderService.updateTender(userId, borrowNid, orderId, bean);
				if (!updateTenderFlag) {
					errorDesc = "投标出現错误,请联系客服人员!";
				}
			}
		}
		result.setStatus(true);
		return JSONObject.toJSONString(result, true);

	}

	/**
	 * 拼接返回结果
	 * 
	 * @param error
	 * @param data
	 * @param callback
	 * @param result
	 * @param account
	 * @param couponQuota
	 * @param couponType
	 * @param couponInterest
	 * @param couponType
	 * @return
	 */
	public String returnUrl(String error, String data, String callback, String result, String account, String couponFlag, Integer projectType, String couponInterest, String couponType,
			String couponQuota) {
		// RespCode:000 成功；否则，失败
		Map<String, String> map = new HashMap<String, String>();
		map.put("error", error);
		map.put("errorDesc", data);
		if (error.equals("0")) {
			map.put("investMoney", account);
			map.put("interest", result);
			map.put("couponFlag", couponFlag);
			map.put("projectType", String.valueOf(projectType));
		}
		data = JSON.toJSONString(map);
		try {
			data = URLEncoder.encode(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LogUtil.errorLog(TenderController.class.getName(), "tenderRetUrl", e);
		}
		String url = callback + "backinfo/" + data;
		return "redirect:" + url;

	}

}
