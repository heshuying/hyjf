/**
 * Description:老版的银行开户控制器
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 *           Created at: 2018年04月11日 上午10:20
 *           Modification History:
 *           Modified by :jijun
 */
package com.hyjf.web.bank.wechat.user.bankopenold;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.bankopen.BankOpenService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import org.apache.commons.lang3.StringUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountLog;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.WeChatBaseAjaxResultBean;
import com.hyjf.web.bank.wechat.user.bankopen.BankOpenAjaxResultBean;
import com.hyjf.web.bank.wechat.user.bankopen.BankOpenBean;

/**
 * @package com.hyjf.web.user.recharge
 * @author GOGTZ-T
 * @date 2015/12/04 14:00
 * @version V1.0  
 */
@Controller(BankOpenDefineOld.CONTROLLER_NAME)
@RequestMapping(value = BankOpenDefineOld.REQUEST_MAPPING)
public class BankOpenController extends BaseController {

	@Autowired
	private BankOpenService openAccountService;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

	/** 当前controller名称 */
	public static final String THIS_CLASS = BankOpenController.class.getName();
	
	Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 普通用户开户
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = BankOpenDefineOld.BANKOPEN_SENDCODE_ACTION, produces = "application/json; charset=UTF-8")
	public String sendCode(HttpServletRequest request, HttpServletResponse response) {

		
		BankOpenAjaxResultBean result = new BankOpenAjaxResultBean();
		String mobileStr = request.getParameter("mobile");
		// 获取登陆用户userId
		String userIdStr = request.getParameter("userId");
		log.info("=====================cwyang 微官网开户获取手机验证码,userID = " + userIdStr);
		String errorDesc = "微信短信验证码发送失败!";
		int error = BankOpenDefineOld.ERROR_FAIL;
		if (Validator.isNull(userIdStr)) {
			result.setErrorDesc(errorDesc);
			result.setError(error);
			return JSONObject.toJSONString(result);
		}
		int userId = Integer.parseInt(userIdStr);
		Users users = this.openAccountService.getUsers(userId);
		if (Validator.isNull(users)) {
			errorDesc = "微信开户用户不存在！";
			result.setErrorDesc(errorDesc);
			result.setError(error);
			return JSONObject.toJSONString(result);
		}
		String userName = users.getUsername();
		// 获取用户的手机号
		String mobile = users.getMobile();
		if (StringUtils.isBlank(mobile)) {
			if (StringUtils.isNotBlank(mobileStr)) {
				if (!openAccountService.existMobile(mobileStr)) {
					mobile = mobileStr;
				} else {
					errorDesc = "用户信息错误，手机号码重复！";
					result.setErrorDesc(errorDesc);
					result.setError(error);
					return JSONObject.toJSONString(result);
				}
			} else {
				errorDesc = "用户信息错误，未获取到用户的手机号码！";
				result.setErrorDesc(errorDesc);
				result.setError(error);
				return JSONObject.toJSONString(result);
			}
		} else {
			if (StringUtils.isNotBlank(mobileStr) && !mobile.equals(mobileStr)) {
				errorDesc = "用户信息错误，用户的手机号码错误！";
				result.setErrorDesc(errorDesc);
				result.setError(error);
				return JSONObject.toJSONString(result);
			}
		}
		String logOrderId = GetOrderIdUtils.getOrderId2(userId);
		boolean openAccountLog = this.openAccountService.updateUserAccountLog(userId, userName, mobile, logOrderId, CustomConstants.CLIENT_WECHAT);
		if (!openAccountLog) {
			result.setErrorDesc(errorDesc);
			result.setError(error);
			return JSONObject.toJSONString(result);
		}
		log.info("=====================cwyang 银行开户开始获取手机验证码,userID = " + userId);
		// 调用短信发送接口
		BankCallBean mobileBean = this.openAccountService.sendSms(userId, BankCallConstant.TXCODE_ACCOUNT_OPEN_PLUS, mobile, BankCallConstant.CHANNEL_PC);
		if (Validator.isNull(mobileBean)) {
			errorDesc = "短信验证码发送失败，请稍后再试！";
			result.setErrorDesc(errorDesc);
			result.setError(error);
			return JSONObject.toJSONString(result);
		}
		log.info("=====================cwyang 银行开户结束获取手机验证码,userID = " + userId);
		// 短信发送返回结果码
		String retCode = mobileBean.getRetCode();
		if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
			errorDesc = "短信验证码发送失败！";
			result.setErrorDesc(errorDesc);
			result.setError(error);
			return JSONObject.toJSONString(result);
		}
		if (Validator.isNull(mobileBean.getSrvAuthCode()) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
			errorDesc = "短信验证码发送失败！";
			result.setErrorDesc(errorDesc);
			result.setError(error);
			return JSONObject.toJSONString(result);
		}
		// 业务授权码
		String srvAuthCode = mobileBean.getSrvAuthCode();
		if (Validator.isNotNull(mobileBean.getSrvAuthCode())) {
			// 保存用户开户日志
			boolean openAccountLogFlag = this.openAccountService.updateUserAccountLog(userId, logOrderId, srvAuthCode);
			if (!openAccountLogFlag) {
				errorDesc = "保存开户日志失败！";
				result.setErrorDesc(errorDesc);
				result.setError(error);
				return JSONObject.toJSONString(result);
			}
		}
		errorDesc = "短信发送成功！";
		error = BankOpenDefineOld.ERROR_SUCCESS;
		result.setErrorDesc(errorDesc);
		result.setError(error);
		result.setLogOrderId(logOrderId);
		return JSONObject.toJSONString(result, true);
	}

	/**
	 * 普通用户开户
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = BankOpenDefineOld.BANKOPEN_OPEN_ACTION, produces = "application/json; charset=UTF-8")
	public String openAccount(HttpServletRequest request, HttpServletResponse respons, @ModelAttribute BankOpenBean accountBean) {

		LogUtil.startLog(THIS_CLASS, BankOpenDefineOld.BANKOPEN_OPEN_ACTION);
		WeChatBaseAjaxResultBean result = new WeChatBaseAjaxResultBean();
		String errorDesc = "开户失败";
		int error = BankOpenDefineOld.ERROR_FAIL;
		// 获取相应的订单号
		String logOrderId = request.getParameter(BankCallConstant.PARAM_LOGORDERID);
		// 获取登陆用户userId update by jijun 2018/04/11
		Integer userId = accountBean.getUserId();
//		Integer userId = StringUtils.isNotBlank(accountBean.getUserId()) ? Integer.parseInt(accountBean.getUserId()) : null;
		if (Validator.isNull(userId)) {
			errorDesc = "用户未登陆，请先登陆！";
			result.setErrorDesc(errorDesc);
			result.setError(error);
			return JSONObject.toJSONString(result);
		}
		log.info("用户银行开户获取登陆用户userId----------------------:" + userId);
		if (Validator.isNull(logOrderId)) {
			result.setErrorDesc(errorDesc);
			result.setError(error);
			return JSONObject.toJSONString(result);
		}
		Users users = this.openAccountService.getUsers(userId);
		if (Validator.isNull(users)) {
			errorDesc = "获取用户信息失败！";
			result.setErrorDesc(errorDesc);
			result.setError(error);
			return JSONObject.toJSONString(result);

		}
		// 取得用户在汇付天下的客户号
		BankOpenAccount openAccount = openAccountService.getBankOpenAccount(userId);
		if (Validator.isNotNull(openAccount)) {
			errorDesc = "用户已开户！";
			result.setErrorDesc(errorDesc);
			result.setError(error);
			return JSONObject.toJSONString(result);

		}
		// 获取开户画面相应的参数
		// 获取用户的真实姓名
		String trueName = accountBean.getTrueName();
		if (StringUtils.isBlank(trueName)) {
			errorDesc = "真实姓名不能为空！";
			result.setErrorDesc(errorDesc);
			result.setError(error);
			return JSONObject.toJSONString(result);

		}else{
			//判断真实姓名是否包含特殊字符
			if (!ValidatorCheckUtil.verfiyChinaFormat(trueName)) {
				errorDesc = "真实姓名不能包含空格！";
				result.setErrorDesc(errorDesc);
				result.setError(error);
				return JSONObject.toJSONString(result);
			}
			//判断真实姓名的长度,不能超过10位
			if (trueName.length() > 10) {
				errorDesc = "真实姓名不能超过10位";
				result.setErrorDesc(errorDesc);
				result.setError(error);
				return JSONObject.toJSONString(result);
			}
		}
		// 获取用户的身份证号
		String idNo = accountBean.getIdNo();
		if (StringUtils.isBlank(idNo)) {
			errorDesc = "身份证号不能为空！";
			result.setErrorDesc(errorDesc);
			result.setError(error);
			return JSONObject.toJSONString(result);

		}
		// add by cwyang 身份证信息最后一位如果是小写x替换为大写
		String replaceIdNo = replaceIdNo(idNo);
		idNo = replaceIdNo;
		
		boolean isOnly = openAccountService.checkIdNo(idNo);
		if (!isOnly) {
			errorDesc = "身份证已存在!";
			result.setErrorDesc(errorDesc);
			result.setError(error);
			return JSONObject.toJSONString(result);
		}
		
		// 获取用户的银行卡号
		String cardNo = accountBean.getCardNo();
		if (StringUtils.isBlank(cardNo)) {
			errorDesc = "银行卡号不能为空！";
			result.setErrorDesc(errorDesc);
			result.setError(error);
			return JSONObject.toJSONString(result);
		}
		// 获取用户开户的短信验证码
		String smsCode = accountBean.getSmsCode();
		if (StringUtils.isBlank(smsCode)) {
			errorDesc = "短信验证码不能为空！";
			result.setErrorDesc(errorDesc);
			result.setError(error);
			return JSONObject.toJSONString(result);
		}
		// 获取用户的手机号
		String mobile = this.openAccountService.getUsersMobile(userId);
		if (StringUtils.isBlank(mobile)) {
			if (StringUtils.isNotBlank(accountBean.getMobile())) {
				if (!openAccountService.existMobile(accountBean.getMobile())) {
					mobile = accountBean.getMobile();
				} else {
					errorDesc = "用户信息错误，手机号码重复！";
					result.setErrorDesc(errorDesc);
					result.setError(error);
					return JSONObject.toJSONString(result);
				}
			} else {
				errorDesc = "用户信息错误，未获取到用户的手机号码！";
				result.setErrorDesc(errorDesc);
				result.setError(error);
				return JSONObject.toJSONString(result);
			}
		} else {
			if (StringUtils.isNotBlank(accountBean.getMobile()) && !mobile.equals(accountBean.getMobile())) {
				errorDesc = "用户信息错误，用户的手机号码错误！";
				result.setErrorDesc(errorDesc);
				result.setError(error);
				return JSONObject.toJSONString(result);
			}
		}
		// 获取相应的短信发送日志
		BankOpenAccountLog openAccountLog = this.openAccountService.selectUserAccountLog(userId, logOrderId);
		if (Validator.isNull(openAccountLog)) {
			result.setErrorDesc(errorDesc);
			result.setError(error);
			return JSONObject.toJSONString(result);
		}
		// 获取短信授权码
		String srvAuthCode = openAccountLog.getLastSrvAuthCode();
		if (StringUtils.isBlank(srvAuthCode)) {// add by cwyang 短信验证码判空
			errorDesc = "短信验证码获取失败！";
			result.setErrorDesc(errorDesc);
			result.setError(error);
			return JSONObject.toJSONString(result);
		}
		// 获取共同参数
		String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
		String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
		String channel = BankCallConstant.CHANNEL_WEI;
		String orderDate = GetOrderIdUtils.getOrderDate();
		String txDate = GetOrderIdUtils.getTxDate();
		String txTime = GetOrderIdUtils.getTxTime();
		String seqNo = GetOrderIdUtils.getSeqNo(6);
		String idType = BankCallConstant.ID_TYPE_IDCARD;
		String acctUse = BankCallConstant.ACCOUNT_USE_COMMON;
		String ip = CustomUtil.getIpAddr(request);
		// 调用开户接口
		BankCallBean openAccoutBean = new BankCallBean();
		openAccoutBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		openAccoutBean.setTxCode(BankCallConstant.TXCODE_ACCOUNT_OPEN_PLUS);// 消息类型(用户开户)
		openAccoutBean.setInstCode(instCode);// 机构代码
		openAccoutBean.setBankCode(bankCode);
		openAccoutBean.setTxDate(txDate);
		openAccoutBean.setTxTime(txTime);
		openAccoutBean.setSeqNo(seqNo);
		openAccoutBean.setChannel(channel);
		openAccoutBean.setIdType(idType);
		openAccoutBean.setIdNo(idNo);
		openAccoutBean.setName(trueName);
		openAccoutBean.setMobile(mobile);
		openAccoutBean.setCardNo(cardNo);
		openAccoutBean.setAcctUse(acctUse);
		openAccoutBean.setSmsCode(smsCode);
		openAccoutBean.setUserIP(ip);
		openAccoutBean.setLastSrvAuthCode(srvAuthCode);
		openAccoutBean.setLogOrderId(logOrderId);
		openAccoutBean.setLogOrderDate(orderDate);
		openAccoutBean.setLogUserId(String.valueOf(userId));
		openAccoutBean.setLogRemark("用户开户");
		openAccoutBean.setLogIp(ip);
		openAccoutBean.setLogClient(1);// 开户平台:1:微官网
		// 保存用户开户日志
		boolean openAccountLogFlag = this.openAccountService.updateUserAccountLog(openAccountLog, openAccoutBean);
		if (!openAccountLogFlag) {
			errorDesc = "保存开户日志失败！";
			result.setErrorDesc(errorDesc);
			result.setError(error);
			return JSONObject.toJSONString(result);
		}
		try {
			BankCallBean openAccountResult = BankCallUtils.callApiBg(openAccoutBean);
			String retCode = StringUtils.isNotBlank(openAccountResult.getRetCode()) ? openAccountResult.getRetCode() : "";
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
				this.openAccountService.updateUserAccountLog(userId, logOrderId, 4);
				String message = openAccountService.getBankRetMsg(retCode);
				log.info("=====================银行开户接口返回码:" + retCode);
				errorDesc = "开户失败,"+ message;
				result.setErrorDesc(errorDesc);
				result.setError(error);
				return JSONObject.toJSONString(result);

			}
			if (openAccountResult!=null) {
				openAccountResult.setLogClient(1);//微信端
			}
			// 保存用户的开户信息
			boolean saveBankAccountFlag = this.openAccountService.updateUserAccount(openAccountResult);
			if (!saveBankAccountFlag) {
				errorDesc = "开户失败，请联系客服！";
				result.setErrorDesc(errorDesc);
				result.setError(error);
				return JSONObject.toJSONString(result);
			} else {
				// 开户成功后,发送CA认证MQ
				this.openAccountService.sendCAMQ(String.valueOf(userId));
				CommonSoaUtils.listOpenAcc(userId);
				error = BankOpenDefineOld.ERROR_SUCCESS;
				errorDesc = "开户成功！";
				result.setErrorDesc(errorDesc);
				result.setError(error);
				return JSONObject.toJSONString(result);
			}
		} catch (Exception e) {
			log.info("==============调用银行接口异常========");
			errorDesc = "调用银行接口失败！";
			result.setErrorDesc(errorDesc);
			result.setError(error);
			return JSONObject.toJSONString(result);
		}
	}

	private String replaceIdNo(String idNo) {
		String lastString = idNo.substring(idNo.length()-1);
		if ("x".equalsIgnoreCase(lastString)) {
			idNo = idNo.replace(idNo.charAt(idNo.length()-1)+"", "X");
		}
		log.info("=============idNo is " + idNo);
		return idNo;
	}
}
