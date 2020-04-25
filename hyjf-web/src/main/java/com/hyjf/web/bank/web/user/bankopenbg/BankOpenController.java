/**
 * Description:出借控制器
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 *           Created at: 2015年12月4日 下午2:32:36
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.web.bank.web.user.bankopenbg;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseAjaxResultBean;
import com.hyjf.bank.service.user.bankopen.BankOpenService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountLog;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.bank.web.user.bankopen.BankOpenBean;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.login.LoginService;
import com.hyjf.web.util.WebUtils;
/**
 * @package com.hyjf.web.user.recharge
 * @author GOGTZ-T
 * @date 2015/12/04 14:00
 * @version V1.0
 */
@Controller(BankOpenDefinebg.CONTROLLER_NAME)
@RequestMapping(value = BankOpenDefinebg.REQUEST_MAPPING_BG)
public class BankOpenController extends BaseController {
	Logger _log = LoggerFactory.getLogger(BankOpenController.class);
	@Autowired
	private BankOpenService openAccountService;

	@Autowired
	private LoginService loginService;

	/** 当前controller名称 */
	public static final String THIS_CLASS = BankOpenController.class.getName();

	/**
	 * 用户开户
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankOpenDefinebg.BANKOPEN_INIT_ACTION)
	public ModelAndView initOpenAccount(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(THIS_CLASS, BankOpenDefinebg.BANKOPEN_INIT_ACTION);
		ModelAndView modelAndView = new ModelAndView(BankOpenDefinebg.BANKOPEN_INIT_PATH);
		WebViewUser user = WebUtils.getUser(request);
		int userId = user.getUserId();
		boolean accountFlag = user.isBankOpenAccount();
		if (accountFlag) {
			modelAndView = new ModelAndView("redirect:/user/pandect/pandect.do");
			return modelAndView;
		}
		String logOrderId = GetOrderIdUtils.getOrderId2(userId);
		modelAndView.addObject("logOrderId", logOrderId);
		modelAndView.addObject("mobile", user.getMobile());
		return modelAndView;
	}

	/**
	 * 检查手机号码或用户名唯一性
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = BankOpenDefinebg.MOBILE_CHECK_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public boolean mobileCheck(HttpServletRequest request) {

		String param = request.getParameter("param");
		JSONObject info = new JSONObject();
		boolean mobileFlag = ValidatorCheckUtil.validateMobile(info, null, null, param, 11, false);
		if (mobileFlag) {
			boolean isMobile = Validator.isMobile(param);
			if (isMobile) {
				if (!openAccountService.existMobile(param)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 用户开户-发送短信验证码
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = BankOpenDefinebg.BANKOPEN_SENDCODE_ACTION, produces = "application/json; charset=utf-8")
	public BaseAjaxResultBean sendCode(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(THIS_CLASS, BankOpenDefinebg.BANKOPEN_SENDCODE_ACTION);
		BaseAjaxResultBean result = new BaseAjaxResultBean();
		String mobileStr = request.getParameter("mobile");
		String message = "开户失败";
		boolean status = BankOpenDefinebg.STATUS_FALSE;
		// 获取登陆用户userId
		WebViewUser user = WebUtils.getUser(request);
		// 未登录时候返回错误信息
		if (user==null) {
            result.setMessage("登录失效，请重新登录!");
            result.setStatus(status);
            return result;
        }
		int userId = user.getUserId();
		if (Validator.isNull(userId)) {
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		String userName = user.getUsername();
		String logOrderId = request.getParameter(BankCallConstant.PARAM_LOGORDERID);
		if (Validator.isNull(logOrderId)) {
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		// 获取用户的手机号
		Users users = this.openAccountService.getUsers(userId);
		if (Validator.isNull(users)) {
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		String mobile = users.getMobile();
		if (StringUtils.isBlank(mobile)) {
			if (StringUtils.isNotBlank(mobileStr)) {
				if(!openAccountService.existMobile(mobileStr)){
					mobile = mobileStr;
				}else{
					message = "用户信息错误，手机号码重复！";
					result.setMessage(message);
					result.setStatus(status);
					return result;
				}
			} else {
				message = "用户信息错误，未获取到用户的手机号码！";
				result.setMessage(message);
				result.setStatus(status);
				return result;
			}
		} else {
			if (StringUtils.isNotBlank(mobileStr) && !mobile.equals(mobileStr)) {
				message = "用户信息错误，用户的手机号码错误！";
				result.setMessage(message);
				result.setStatus(status);
				return result;
			}
		}
		boolean openAccountLog = this.openAccountService.updateUserAccountLog(userId, userName, mobile, logOrderId,
				CustomConstants.CLIENT_PC);
		if (!openAccountLog) {
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		_log.info("=====================cwyang 银行开户开始获取手机验证码,userID = " + userId);
		// 调用短信发送接口
		BankCallBean mobileBean = this.openAccountService.sendSms(userId, BankCallConstant.TXCODE_ACCOUNT_OPEN_PLUS,
				mobile, BankCallConstant.CHANNEL_PC);
		if (Validator.isNull(mobileBean)) {
			message = "短信验证码发送失败，请稍后再试！";
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		_log.info("=====================cwyang 银行开户获取手机验证码完毕,userID = " + userId);
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
		// 业务授权码
		String srvAuthCode = mobileBean.getSrvAuthCode();
		if (Validator.isNotNull(mobileBean.getSrvAuthCode())) {
			// 保存用户开户日志
			boolean openAccountLogFlag = this.openAccountService.updateUserAccountLog(userId, logOrderId, srvAuthCode);
			if (!openAccountLogFlag) {
				message = "保存开户日志失败！";
				result.setMessage(message);
				result.setStatus(status);
				return result;
			}
		}
		message = "短信发送成功！";
		status = BankOpenDefinebg.STATUS_TRUE;
		result.setMessage(message);
		result.setStatus(status);
		return result;
	}

	/**
	 * 普通用户开户
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST,value =BankOpenDefinebg.BANKOPEN_OPEN_ACTION, produces = "application/json; charset=utf-8")
	public BaseAjaxResultBean openAccount(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute BankOpenBean accountBean) {
		BaseAjaxResultBean result = new BaseAjaxResultBean();
		boolean status = BankOpenDefinebg.STATUS_FALSE;
		LogUtil.startLog(THIS_CLASS, BankOpenDefinebg.BANKOPEN_OPEN_ACTION);
		// 获取相应的订单号
		String logOrderId = request.getParameter(BankCallConstant.PARAM_LOGORDERID);
		// 获取登陆用户userId
		Integer userId = WebUtils.getUserId(request);
		String message = "开户失败";
		if (Validator.isNull(userId)) {
			message = "用户未登陆，请先登陆！";
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		_log.info("用户银行开户获取登陆用户userId----------------------:" + userId);
		if (Validator.isNull(logOrderId)) {
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		Users users = this.openAccountService.getUsers(userId);
		if (Validator.isNull(users)) {
			message = "获取用户信息失败！";
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		// 取得用户在汇付天下的客户号
		BankOpenAccount openAccount = openAccountService.getBankOpenAccount(userId);
		if (Validator.isNotNull(openAccount)) {
			message = "用户已开户！";
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		// 获取开户画面相应的参数
		// 获取用户的真实姓名
		String trueName = accountBean.getTrueName();
		if (StringUtils.isBlank(trueName)) {
			message = "真实姓名不能为空！";
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}else{
			//判断真实姓名是否包含空格
			if (!ValidatorCheckUtil.verfiyChinaFormat(trueName)) {
				message = "真实姓名不能包含空格！";
				result.setMessage(message);
				result.setStatus(status);
				return result;
			}
			//判断真实姓名的长度,不能超过10位
			if (trueName.length() > 10) {
				message = "真实姓名不能超过10位！";
				result.setMessage(message);
				result.setStatus(status);
				return result;
			}
		}
		
		// 获取用户的身份证号
		String idNo = accountBean.getIdNo();
		if (StringUtils.isBlank(idNo)) {
			message = "身份证号不能为空！";
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		
		// add by cwyang 身份证信息最后一位如果是小写x替换为大写
		//String replaceIdNo = replaceIdNo(idNo);
		//idNo = replaceIdNo;
		idNo = StringUtils.upperCase(idNo.trim());
		//增加身份证唯一性校验
		boolean isOnly = openAccountService.checkIdNo(idNo);
		if (!isOnly) {
			message = "身份证已存在!";
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		// 获取用户的银行卡号
		String cardNo = accountBean.getCardNo();
		if (StringUtils.isBlank(cardNo)) {
			message = "银行卡号不能为空！";
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		// 获取用户开户的短信验证码
		String smsCode = accountBean.getSmsCode();
		if (StringUtils.isBlank(smsCode)) {
			message = "短信验证码不能为空！";
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		// 获取用户的手机号
		String mobile = this.openAccountService.getUsersMobile(userId);
		if (StringUtils.isBlank(mobile)) {
			if (StringUtils.isNotBlank(accountBean.getMobile())) {
				if(!openAccountService.existMobile(accountBean.getMobile())){
					mobile = accountBean.getMobile();
				}else{
					message = "用户信息错误，手机号码重复！";
					result.setMessage(message);
					result.setStatus(status);
					return result;
				}
			} else {
				message = "用户信息错误，未获取到用户的手机号码！";
				result.setMessage(message);
				result.setStatus(status);
				return result;
			}
		} else {
			if (StringUtils.isNotBlank(accountBean.getMobile()) && !mobile.equals(accountBean.getMobile())) {
				message = "用户信息错误，用户的手机号码错误！";
				result.setMessage(message);
				result.setStatus(status);
				return result;
			}
		}
		// 获取相应的短信发送日志
		BankOpenAccountLog openAccountLog = this.openAccountService.selectUserAccountLog(userId, logOrderId);
		if (Validator.isNull(openAccountLog)) {
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		// 获取短信授权码
		String srvAuthCode = openAccountLog.getLastSrvAuthCode();
		if (StringUtils.isBlank(srvAuthCode)) {// add by cwyang 短信验证码判空
			message = "短信验证码获取失败！";
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		// 获取共同参数
		String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
		String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
		String channel = BankCallConstant.CHANNEL_PC;
		String orderDate = GetOrderIdUtils.getOrderDate();
		String txDate = GetOrderIdUtils.getTxDate();
		String txTime = GetOrderIdUtils.getTxTime();
		String seqNo = GetOrderIdUtils.getSeqNo(6);
		String idType = BankCallConstant.ID_TYPE_IDCARD;
		String acctUse = BankCallConstant.ACCOUNT_USE_COMMON;
//		String acctUse = BankCallConstant.ACCOUNT_USE_GUARANTEE; 担保户
		
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
		openAccoutBean.setLogUserId(String.valueOf(userId));
		openAccoutBean.setLogOrderId(logOrderId);
		openAccoutBean.setLogOrderDate(orderDate);
		openAccoutBean.setLogUserId(String.valueOf(userId));
		openAccoutBean.setLogRemark("用户开户");
		openAccoutBean.setLogIp(ip);
		openAccoutBean.setLogClient(0);
		// 保存用户开户日志
		boolean openAccountLogFlag = this.openAccountService.updateUserAccountLog(openAccountLog, openAccoutBean);
		if (!openAccountLogFlag) {
			message = "保存开户日志失败！";
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		try {
			BankCallBean openAccountResult = BankCallUtils.callApiBg(openAccoutBean);
			if (Validator.isNull(openAccountResult)) {
				message = "调用银行接口失败！";
				result.setMessage(message);
				result.setStatus(status);
				return result;
			}
			String retCode = StringUtils.isNotBlank(openAccountResult.getRetCode()) ? openAccountResult.getRetCode()
					: "";
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
				this.openAccountService.updateUserAccountLog(userId, logOrderId, 4);
				message = openAccountService.getBankRetMsg(retCode);
				_log.info("=========银行开户返回码:" + retCode);
				result.setMessage("开户失败,"+ message);
				result.setStatus(status);
				return result;     
			}
			// 保存用户的开户信息
			boolean saveBankAccountFlag = this.openAccountService.updateUserAccount(openAccountResult);
			
			if (!saveBankAccountFlag) {
				message = "开户失败，请联系客服！";
				result.setMessage(message);
				result.setStatus(status);
				return result;
			}
			WebViewUser webUser = loginService.getWebViewUserByUserId(userId);
			WebUtils.sessionLogin(request, response, webUser);
			// 开户成功后,发送CA认证MQ
            this.openAccountService.sendCAMQ(String.valueOf(userId));
            CommonSoaUtils.listOpenAcc(userId);
			message = "开户成功!";
			status = BankOpenDefinebg.STATUS_TRUE;
			result.setMessage(message);
			result.setStatus(status);
			return result;
		} catch (Exception e) {
			_log.info("=========调用银行接口异常!========");
			message = "调用银行接口失败！";
			result.setMessage(message);
			result.setStatus(status);
			return result;
		}
		
	}

	/*private String replaceIdNo(String idNo) {
		
		String lastString = idNo.substring(idNo.length()-1);
		if ("x".equalsIgnoreCase(lastString)) {
			idNo = idNo.replace(idNo.charAt(idNo.length()-1)+"", "X");
		}
		System.out.println("=============idNo is " + idNo);
		return idNo;
	}*/
}
