/**
 * 个人设置控制器
 */
package com.hyjf.web.bank.web.user.transpassword;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.recharge.RechargeService;
import com.hyjf.bank.service.user.transpassword.TransPasswordAjaxBean;
import com.hyjf.bank.service.user.transpassword.TransPasswordService;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.SmsConfig;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.login.LoginService;
import com.hyjf.web.util.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @package com.hyjf.web.user.recharge
 * @author GOGTZ-T
 * @date 2015/12/04 14:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = TransPasswordDefine.REQUEST_MAPPING)
public class TransPasswordController extends BaseController {

	Logger _log = LoggerFactory.getLogger(TransPasswordController.class);
	@Autowired
	private TransPasswordService transPasswordService;
	@Autowired
	private RechargeService userRechargeService;
	@Autowired
	private LoginService loginService;
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	/**
	 * 设置交易密码
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(TransPasswordDefine.SETPASSWORD_ACTION)
	public ModelAndView setPassword(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.SETPASSWORD_ACTION);
		ModelAndView modelAndView = new ModelAndView();
		// 用户id
		WebViewUser user = WebUtils.getUser(request);
		 
		if (user == null) {
			modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
			modelAndView.addObject("message", "登录失效，请重新登陆");
			return modelAndView;
		}
		_log.info("开户第一次设置密码==========================");
		// 用户id
		UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
		userOperationLogEntity.setOperationType(6);
		userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
		userOperationLogEntity.setPlatform(0);
		userOperationLogEntity.setRemark("");
		userOperationLogEntity.setOperationTime(new Date());
		userOperationLogEntity.setUserName(user.getUsername());
		userOperationLogEntity.setUserRole(user.getRoleId());
		loginService.sendUserLogMQ(userOperationLogEntity);
		_log.info("开户第一次设置密码==========================结束");
		try {
             Thread.sleep(2000);
         } catch (Exception e) {
             e.printStackTrace();
         }
		// 设置交易密码时候 为了防止开户异步比同步后到  所以重新从数据库查询一遍
		user = loginService.getWebViewUserByUserId(user.getUserId());
        WebUtils.sessionLogin(request, response, user);
		// 判断用户是否开户
		boolean accountFlag = user.isBankOpenAccount();
		if (!accountFlag) {// 未开户
			modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
			modelAndView.addObject("message", "用户未开户！");
			return modelAndView;
		}

		// 判断用户是否綁卡
		/*BankCard bankCard = this.userRechargeService.selectBankCardByUserId(user.getUserId());
		String cardNo = "";
		if (bankCard==null) {
		    // 存在有效绑卡的用户在银行页面上输入证件号、手机号、姓名、绑定卡号进行四要素认证；未绑卡用户需输入绑定卡号，但不校验绑定卡号
		    cardNo = bankCard.getCardNo();
		}*/
		// 判断用户是否设置过交易密码
		Integer passwordFlag = user.getIsSetPassword();
		if (passwordFlag == 1) {// 已设置交易密码
			modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
			modelAndView.addObject("message", "已设置交易密码");
			return modelAndView;
		}
		int userId = user.getUserId();
		// 同步调用路径
		String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + TransPasswordDefine.REQUEST_MAPPING
				+ TransPasswordDefine.RETURL_SYN_PASSWORD_ACTION + ".do";
		// 异步调用路
		String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + TransPasswordDefine.REQUEST_MAPPING
				+ TransPasswordDefine.RETURN_ASY_PASSWORD_ACTION + ".do";
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
		if(user.getUserType() == 1){ //企业用户 传组织机构代码
			CorpOpenAccountRecord record = transPasswordService.getCorpOpenAccountRecord(userId);
			bean.setIdType(record.getCardType() != null ? String.valueOf(record.getCardType()) : BankCallConstant.ID_TYPE_COMCODE);// 证件类型 20：其他证件（组织机构代码）25：社会信用号
			bean.setIdNo(record.getBusiCode());
			bean.setName(record.getBusiName());
		}else{
			bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);
			bean.setIdNo(user.getIdcard());
			bean.setName(user.getTruename());
		}
		bean.setAccountId(user.getBankAccount());// 电子账号
		bean.setMobile(user.getMobile());
		bean.setRetUrl(retUrl);// 页面同步返回 URL
		bean.setNotifyUrl(bgRetUrl);// 页面异步返回URL(必须)
		// 商户私有域，存放开户平台,用户userId
		LogAcqResBean acqRes = new LogAcqResBean();
		acqRes.setUserId(userId);
		bean.setLogAcqResBean(acqRes);
		// 操作者ID
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_PASSWORDRESETPAGE);
		bean.setLogOrderId(GetOrderIdUtils.getUsrId(userId));
		bean.setLogRemark("设置交易密码");
		// 跳转到汇付天下画面
		try {
			modelAndView = BankCallUtils.callApi(bean);
			LogUtil.endLog(TransPasswordController.class.toString(), TransPasswordDefine.SETPASSWORD_ACTION);
		} catch (Exception e) {
			e.printStackTrace();
			modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
			modelAndView.addObject("message", "调用银行接口失败！");
			LogUtil.errorLog(TransPasswordController.class.toString(), TransPasswordDefine.SETPASSWORD_ACTION, e);
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
	@RequestMapping(TransPasswordDefine.RETURL_SYN_PASSWORD_ACTION)
	public ModelAndView passwordReturn(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute BankCallBean bean) {

		LogUtil.startLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.RETURL_SYN_PASSWORD_ACTION, "[开户同步回调开始]");
		ModelAndView modelAndView = new ModelAndView();
		// 用户id
        WebViewUser webUser = WebUtils.getUser(request);
        if(webUser==null){
            modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "登录过期，请重新登录");
            return modelAndView;
        }
		Users user = this.transPasswordService.getUsers(webUser.getUserId());

		// 返回失败
        if (bean.getRetCode()!=null&&!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
            modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "交易密码设置失败,失败原因：" + transPasswordService.getBankRetMsg(bean.getRetCode()));
            return modelAndView;
        }
		// 判断用户是否设置了交易密码
		boolean flag = user.getIsSetPassword() == 1 ? true : false;
		if (flag) {
			modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_SUCCESS_PATH);
			modelAndView.addObject("message", "交易密码设置成功");
			return modelAndView;
		}
		
        BankOpenAccount bankOpenAccount = transPasswordService.getBankOpenAccount(webUser.getUserId());
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
        selectbean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));// 电子账号

        // 操作者ID
        selectbean.setLogUserId(String.valueOf(webUser.getUserId()));
        selectbean.setLogOrderId(GetOrderIdUtils.getOrderId2(webUser.getUserId()));
        selectbean.setLogClient(0);
        // 返回参数
        BankCallBean retBean = null;
        // 调用接口
        retBean = BankCallUtils.callApiBg(selectbean);
        LogUtil.endLog(TransPasswordController.class.toString(), TransPasswordDefine.RETURL_SYN_PASSWORD_ACTION);
		
		try {
		    if("1".equals(retBean.getPinFlag())){
		     // 是否设置密码
	            this.transPasswordService.updateUserIsSetPassword(user, 1);
	            webUser = loginService.getWebViewUserByUserId(webUser.getUserId());
	            WebUtils.sessionLogin(request, response, webUser); 
	            modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_SUCCESS_PATH);
	            modelAndView.addObject("message", "交易密码设置成功");
	            return modelAndView;
		    }

		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.errorLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.RETURN_ASY_PASSWORD_ACTION, e);
		}

		modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
		modelAndView.addObject("message", "交易密码设置失败");
		LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.RETURL_SYN_PASSWORD_ACTION, "[交易完成后,回调结束]");
		return modelAndView;
	}

	/**
	 * 设置交易密码异步回调
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(TransPasswordDefine.RETURN_ASY_PASSWORD_ACTION)
	public String passwordBgreturn(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute BankCallBean bean) {
	    BankCallResult result = new BankCallResult();
		LogUtil.startLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.RETURN_ASY_PASSWORD_ACTION, "[开户异步回调开始]");
		bean.convert();
		LogAcqResBean acqes = bean.getLogAcqResBean();
		int userId = acqes.getUserId();
		// 查询用户开户状态
		Users user = this.transPasswordService.getUsers(userId);

		// 成功或审核中
		if (user != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE))) {
			try {
				// 开户后保存相应的数据以及日志
				this.transPasswordService.updateUserIsSetPassword(user, 1);
				WebViewUser webUser = loginService.getWebViewUserByUserId(userId);
				WebUtils.sessionLogin(request, response, webUser);
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.errorLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.RETURN_ASY_PASSWORD_ACTION, e);
			}
		}
		LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.RETURN_ASY_PASSWORD_ACTION, "[交易完成后,回调结束]");
		result.setMessage("交易密码设置成功");
        result.setStatus(true);
        return JSONObject.toJSONString(result, true);
	}

	/**
	 * 重置交易密码
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(TransPasswordDefine.RESETPASSWORD_ACTION)
	public ModelAndView resetPassword(HttpServletRequest request) {


		ModelAndView modelAndView = new ModelAndView();
		// 用户id
		WebViewUser user = WebUtils.getUser(request);

		String message = "登录失效，请重新登陆";
		if (user == null) {
			modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
			modelAndView.addObject("message", message);
			return modelAndView;
		}
		int userId = user.getUserId();
		// 同步调用路径
		String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + TransPasswordDefine.REQUEST_MAPPING
				+ TransPasswordDefine.RETURL_SYN_RESETPASSWORD_ACTION + ".do";
		// 异步调用路
		String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + TransPasswordDefine.REQUEST_MAPPING
				+ TransPasswordDefine.RETURN_ASY_RESETPASSWORD_ACTION + ".do";

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
		if(user.getUserType() == 1){ //企业用户 传组织机构代码
			CorpOpenAccountRecord record = transPasswordService.getCorpOpenAccountRecord(userId);
			bean.setIdType(record.getCardType() != null ? String.valueOf(record.getCardType()) : BankCallConstant.ID_TYPE_COMCODE);// 证件类型 20：其他证件（组织机构代码）25：社会信用号
			bean.setIdNo(record.getBusiCode());
			bean.setName(record.getBusiName());
		}else{
			bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);
			bean.setIdNo(user.getIdcard());
			bean.setName(user.getTruename());
		}		
		bean.setAccountId(user.getBankAccount());// 电子账号
		bean.setMobile(user.getMobile());
		bean.setNotifyUrl(bgRetUrl);// 页面异步返回URL(必须)
		// 商户私有域，存放开户平台,用户userId
		LogAcqResBean acqRes = new LogAcqResBean();
		acqRes.setUserId(userId);
		bean.setLogAcqResBean(acqRes);
		// 操作者ID
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_PASSWORDRESETPAGE);
		bean.setLogOrderId(GetOrderIdUtils.getUsrId(userId));
		bean.setSuccessfulUrl(retUrl+"?isSuccess=1&ordid=" + bean.getLogOrderId());
		bean.setRetUrl(retUrl+"?isSuccess=1&ordid=" + bean.getLogOrderId());// 页面同步返回 URL
		logger.info("--------------------开始重置交易密码，bean.getLogOrderId()：" + bean.getLogOrderId());
		// 用户id
		UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
		userOperationLogEntity.setOperationType(6);
		userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
		userOperationLogEntity.setPlatform(0);
		userOperationLogEntity.setRemark("");
		userOperationLogEntity.setOperationTime(new Date());
		userOperationLogEntity.setUserName(user.getUsername());
		userOperationLogEntity.setUserRole(user.getRoleId());
		loginService.sendUserLogMQ(userOperationLogEntity);
		// 跳转到汇付天下画面
		try {
			modelAndView = BankCallUtils.callApi(bean);
			LogUtil.endLog(TransPasswordController.class.toString(), TransPasswordDefine.RESETPASSWORD_ACTION);
		} catch (Exception e) {
			e.printStackTrace();
			message = "调用银行接口失败！";
			modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
			modelAndView.addObject("message", message);
			LogUtil.errorLog(TransPasswordController.class.toString(), TransPasswordDefine.RESETPASSWORD_ACTION, e);
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
	@RequestMapping(TransPasswordDefine.RETURL_SYN_RESETPASSWORD_ACTION)
	public ModelAndView resetPasswordReturn(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute BankCallBean bean) {

		LogUtil.startLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.RETURL_SYN_RESETPASSWORD_ACTION,
				"[重置交易密码同步回调开始]");
		String isSuccess = request.getParameter("isSuccess");
		String ordid = request.getParameter("ordid");
		ModelAndView modelAndView = new ModelAndView();
		//add by cwyang 防止同步比异步快
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		//add by cwyang 查询银行设置交易密码是否成功
		boolean backIsSuccess = transPasswordService.backLogIsSuccess(ordid);
		// 返回失败
		if (StringUtils.isBlank(isSuccess) || !backIsSuccess) {
            modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "交易密码修改失败,失败原因：" + transPasswordService.getBankRetMsg(bean.getRetCode()));
            return modelAndView;
        }
		modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_SUCCESS_PATH); // 重置密码如何判断是否重置
		modelAndView.addObject("message", "交易密码修改成功");
		LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.RETURL_SYN_RESETPASSWORD_ACTION,
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
	@ResponseBody
	@RequestMapping(TransPasswordDefine.RETURN_ASY_RESETPASSWORD_ACTION)
	public String resetPasswordBgreturn(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute BankCallBean bean) {
	    BankCallResult result = new BankCallResult();
		LogUtil.startLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.RETURN_ASY_RESETPASSWORD_ACTION,
				"[重置交易密码回调开始]");

		LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.RETURN_ASY_RESETPASSWORD_ACTION,
				"[重置交易密码回调结束]");
		result.setMessage("交易密码修改成功");
        result.setStatus(true);
        return JSONObject.toJSONString(result, true);
	}

	/**
	 * 修改手机号增强发送验证码接口
	 */
	@ResponseBody
	@RequestMapping(value = TransPasswordDefine.SEND_PLUS_CODE_ACTION, produces = "application/json; charset=utf-8")
	public TransPasswordAjaxBean sendPlusCode(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.SEND_PLUS_CODE_ACTION);
		TransPasswordAjaxBean ret = new TransPasswordAjaxBean();
		WebViewUser user = WebUtils.getUser(request);// 用户ID
		if (user == null) {
			ret.setStatus(TransPasswordDefine.STATUS_FALSE);
			ret.setMessage("用户未登陆");
			return ret;
		}
		String mobile = request.getParameter("mobile"); // 手机号
		if (StringUtils.isEmpty(mobile)) {
			ret.setStatus(TransPasswordDefine.STATUS_FALSE);
			ret.setMessage("手机号不能为空");
			return ret;
		}
		// 请求发送短信验证码
		BankCallBean bean = this.transPasswordService.sendSms(user.getUserId(),
				BankCallMethodConstant.TXCODE_MOBILE_MODIFY_PLUS, mobile, BankCallConstant.CHANNEL_PC);
		if (bean == null) {
			ret.setStatus(TransPasswordDefine.STATUS_FALSE);
			ret.setMessage("发送短信验证码异常");
			return ret;
		}
		// 返回失败
		if (!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
		    if("JX900651".equals(bean.getRetCode())){
		        // 成功返回业务授权码
		        ret.setStatus(TransPasswordDefine.STATUS_TRUE);
		        ret.setInfo(bean.getSrvAuthCode());// 业务授权码
		        LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.SEND_PLUS_CODE_ACTION);
		        return ret;
            }
			ret.setStatus(TransPasswordDefine.STATUS_FALSE);
			ret.setMessage("发送短信验证码失败，失败原因：" + transPasswordService.getBankRetMsg(bean.getRetCode()));
			return ret;
		}
		// 成功返回业务授权码
		ret.setStatus(TransPasswordDefine.STATUS_TRUE);
		ret.setInfo(bean.getSrvAuthCode());// 业务授权码
		LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.SEND_PLUS_CODE_ACTION);
		return ret;
	}

	/**
	 * 修改手机号页面初始化(手机认证画面)
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(TransPasswordDefine.INIT_MOBILE)
	public ModelAndView initMobile(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.INIT_MOBILE);
		ModelAndView modelAndView = new ModelAndView(TransPasswordDefine.INIT_MOBILE_PATH);
		Integer userId = WebUtils.getUserId(request);
		// 修复 java.lang.NullPointerException at com.hyjf.web.bank.web.user.transpassword.TransPasswordController.initMobile(TransPasswordController.java:453)
		if (userId == null)
			throw new RuntimeException("用户未登录...");
		// 用户信息
		Users user = this.transPasswordService.getUsers(userId);
		BankOpenAccount bankOpenAccount = transPasswordService.getBankOpenAccount(userId);
		modelAndView.addObject("bankOpenAccount", bankOpenAccount);
		/*// 判断用户是否开户   合规产品要求   未开户 只修改平台的手机号   已开户修改江西银行和平台的
		Integer accountFlag = user.getBankOpenAccount();
		
		if (accountFlag == 0) {// 0未开户 1已开户
			modelAndView = new ModelAndView(TransPasswordDefine.INIT_PLAT_MOBILE_PATH);//未开户，跳开户画面
		} */
		String mobile = user.getMobile();
		String hideMobile = "";
		if (StringUtils.isNotBlank(mobile)) {// add by cywang 增加判空
			hideMobile = mobile.substring(0,mobile.length()-(mobile.substring(3)).length())+"****"+mobile.substring(7);
		}
		modelAndView.addObject("mobile", mobile);
		modelAndView.addObject("hideMobile", hideMobile);
		return modelAndView;
	}

	/**
	 * 修改平台手机号（未开户状态）platMobileModify
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(TransPasswordDefine.PLAT_MOBILE_MODIFY_ACTION)
	public TransPasswordAjaxBean platMobileModify(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.PLAT_MOBILE_MODIFY_ACTION);
		TransPasswordAjaxBean ret = new TransPasswordAjaxBean();
		// 用户id
		WebViewUser user = WebUtils.getUser(request);
		if (user == null) {
		    ret.setStatus(TransPasswordDefine.STATUS_FALSE);
            ret.setMessage("登录失效，请重新登陆");
            return ret;
		}
		int userId = user.getUserId();
		// 接收参数
		String newMobile = request.getParameter("newMobile"); // 手机号
		if (StringUtils.isEmpty(newMobile)) {
		    ret.setStatus(TransPasswordDefine.STATUS_FALSE);
            ret.setMessage("新手机号不能为空");
            return ret;
		}
		// 验证验证码正不正确
		boolean isCheck = checkcode(request,response);
		if(!isCheck){
		    ret.setStatus(TransPasswordDefine.STATUS_FALSE);
            ret.setMessage("验证码错误");
            return ret;
		}
		try {
			// 修改手机号
			this.transPasswordService.updateUserMobile(userId, newMobile);
		} catch (Exception e) {
		    ret.setStatus(TransPasswordDefine.STATUS_FALSE);
            ret.setMessage("系统异常");
            return ret;
		}
		WebViewUser webUser = loginService.getWebViewUserByUserId(userId);
        WebUtils.sessionLogin(request, response, webUser);
        ret.setStatus(TransPasswordDefine.STATUS_TRUE);
        ret.setMessage("恭喜您手机号修改成功！");
        return ret;

	}

	/**
	 * 修改手机号
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(TransPasswordDefine.MOBILEMODIFY_ACTION)
	public TransPasswordAjaxBean mobileModify(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.MOBILEMODIFY_ACTION);
		TransPasswordAjaxBean ret = new TransPasswordAjaxBean();
		// 用户id
		WebViewUser user = WebUtils.getUser(request);
		if (user == null) {
			ret.setStatus(TransPasswordDefine.STATUS_FALSE);
			ret.setMessage("登录失效，请重新登陆");
			return ret;
		}
		int userId = user.getUserId();
		// 接收参数
		String newMobile = request.getParameter("newMobile"); // 手机号
		String srvAuthCode = request.getParameter("srvAuthCode");// 业务授权码
		String smsCode = request.getParameter("smsCode");// 校验码

		if (StringUtils.isEmpty(newMobile) ) {
			ret.setStatus(TransPasswordDefine.STATUS_FALSE);
			ret.setMessage("手机号码不能为空");
			return ret;
		}
		if (StringUtils.isEmpty(srvAuthCode) ) {
            ret.setStatus(TransPasswordDefine.STATUS_FALSE);
            ret.setMessage("请先获取验证码");
            return ret;
        }
		if (StringUtils.isEmpty(smsCode)) {
            ret.setStatus(TransPasswordDefine.STATUS_FALSE);
            ret.setMessage("请验证码不能为空");
            return ret;
        }

		BankOpenAccount bankOpenAccount = transPasswordService.getBankOpenAccount(userId);
		// 调用电子账号手机号修改增强
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_MOBILE_MODIFY_PLUS);// 消息类型
																	// 修改手机号增强
																	// mobileModifyPlus
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
		bean.setTxDate(GetOrderIdUtils.getTxDate());
		bean.setTxTime(GetOrderIdUtils.getTxTime());
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
		bean.setChannel(BankCallConstant.CHANNEL_PC);
		bean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));// 电子账号
		bean.setOption(BankCallConstant.OPTION_1);// 修改
		bean.setMobile(newMobile);// 新手机号
		bean.setLastSrvAuthCode(srvAuthCode);// 业务授权码
		bean.setSmsCode(smsCode);// 短信验证码
		// 商户私有域，存放开户平台,用户userId
		LogAcqResBean acqRes = new LogAcqResBean();
		acqRes.setUserId(userId);
		bean.setLogAcqResBean(acqRes);
		// 操作者ID
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogOrderId(GetOrderIdUtils.getUsrId(userId));
		// 返回参数
		BankCallBean retBean = null;
		try {
			// 调用接口
			retBean = BankCallUtils.callApiBg(bean);
			LogUtil.endLog(TransPasswordController.class.toString(), TransPasswordDefine.MOBILEMODIFY_ACTION);
		} catch (Exception e) {
			LogUtil.errorLog(TransPasswordController.class.toString(), TransPasswordDefine.MOBILEMODIFY_ACTION, e);
			ret.setStatus(TransPasswordDefine.STATUS_FALSE);
			ret.setMessage("调用银行接口异常！");
			return ret;
		}
		if (retBean == null) {
			ret.setStatus(TransPasswordDefine.STATUS_FALSE);
			ret.setMessage("修改手机号失败，系统异常");
			return ret;
		}
		// 返回失败
		if (!BankCallConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode())) {
			ret.setStatus(TransPasswordDefine.STATUS_FALSE);
			ret.setMessage("修改手机号失败,失败原因：" + transPasswordService.getBankRetMsg(retBean.getRetCode()));
			return ret;
		}
		// 修改手机号
		boolean successFlag = this.transPasswordService.updateUserMobile(userId, newMobile);
		if (successFlag) {
            // 修改成功后,发送CA认证信息修改MQ
            this.transPasswordService.sendCAMQ(userId);
			WebViewUser webUser = loginService.getWebViewUserByUserId(userId);
			WebUtils.sessionLogin(request, response, webUser);
			ret.setStatus(TransPasswordDefine.STATUS_TRUE);
			ret.setMessage("恭喜您手机号修改成功！");
		} else {
			ret.setStatus(TransPasswordDefine.STATUS_TRUE);
			ret.setMessage("系统异常，请重新操作！");
		}

		return ret;
	}

	/**
	 * 发送短信验证码（ajax请求） 短信验证码数据保存
	 */
	@ResponseBody
	@RequestMapping(value = TransPasswordDefine.SEND_CODE_ACTION, produces = "application/json; charset=utf-8")
	public TransPasswordAjaxBean sendCode(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.SEND_CODE_ACTION);
		TransPasswordAjaxBean ret = new TransPasswordAjaxBean();
		// 手机号码(必须,数字,最大长度)
		String mobile = request.getParameter("mobile");//是JSP name标签
		if (StringUtils.isEmpty(mobile)) {
			ret.setStatus(TransPasswordDefine.STATUS_FALSE);
			ret.setMessage("请填写手机号!");
			return ret;
		}
		// 短信配置
		SmsConfig smsConfig = transPasswordService.getSmsConfig();

		String ip = GetCilentIP.getIpAddr(request);
		String ipCount = RedisUtils.get(ip + ":MaxIpCount");
		if (StringUtils.isBlank(ipCount) || !Validator.isNumber(ipCount)) {
			ipCount = "0";
			RedisUtils.set(ip + ":MaxIpCount", "0");
		}
		if (Integer.valueOf(ipCount) >= smsConfig.getMaxIpCount()) {
			if (Integer.valueOf(ipCount) == smsConfig.getMaxIpCount()) {
				try {
					transPasswordService.sendSms(mobile, "IP访问次数超限:" + ip);
				} catch (Exception e) {
					LogUtil.errorLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.SEND_CODE_ACTION, e);
				}
				RedisUtils.set(ip + ":MaxIpCount", (Integer.valueOf(ipCount) + 1) + "", 24 * 60 * 60);
			}
			ret.setStatus(TransPasswordDefine.STATUS_FALSE);
			ret.setMessage("该设备短信请求次数超限，请明日再试");
			LogUtil.errorLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.SEND_CODE_ACTION, "短信验证码发送失败", null);
			return ret;
		}

		// 判断最大发送数max_phone_count
		String count = RedisUtils.get(mobile + ":MaxPhoneCount");
		if (StringUtils.isBlank(count) || !Validator.isNumber(count)) {
			count = "0";
			RedisUtils.set(mobile + ":MaxPhoneCount", "0");
		}

		if (Integer.valueOf(count) >= smsConfig.getMaxPhoneCount()) {
			if (Integer.valueOf(count) == smsConfig.getMaxPhoneCount()) {
				try {
					transPasswordService.sendSms(mobile, "手机验证码发送次数超限");
				} catch (Exception e) {
					LogUtil.errorLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.SEND_CODE_ACTION, e);
				}

				RedisUtils.set(mobile + ":MaxPhoneCount", (Integer.valueOf(count) + 1) + "", 24 * 60 * 60);
			}
			ret.setStatus(TransPasswordDefine.STATUS_FALSE);
			ret.setMessage("该手机号短信请求次数超限，请明日再试");
			LogUtil.errorLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.SEND_CODE_ACTION, "短信验证码发送失败", null);
			return ret;
		}

		// 生成验证码
		String checkCode = GetCode.getRandomSMSCode(6);
		Map<String, String> param = new HashMap<String, String>();
		param.put("val_code", checkCode);
		// 发送短信验证码
		SmsMessage smsMessage = new SmsMessage(null, param, mobile, null, MessageDefine.SMSSENDFORMOBILE, null,
				CustomConstants.PARAM_TPL_YZYSJH, CustomConstants.CHANNEL_TYPE_NORMAL);
		Integer result = (smsProcesser.gather(smsMessage) == 1) ? 0 : 1;
		// 短信发送成功后处理
		if (result != null && result == 0) {
			// 累计IP次数
			String currentMaxIpCount = RedisUtils.get(ip + ":MaxIpCount");
			if (StringUtils.isBlank(currentMaxIpCount)) {
				currentMaxIpCount = "0";
			}
			// 累加手机次数
			String currentMaxPhoneCount = RedisUtils.get(mobile + ":MaxPhoneCount");
			if (StringUtils.isBlank(currentMaxPhoneCount)) {
				currentMaxPhoneCount = "0";
			}
			RedisUtils.set(ip + ":MaxIpCount", (Integer.valueOf(currentMaxIpCount) + 1) + "", 24 * 60 * 60);
			RedisUtils.set(mobile + ":MaxPhoneCount", (Integer.valueOf(currentMaxPhoneCount) + 1) + "", 24 * 60 * 60);
		}
		System.out.println(checkCode);
		// 保存短信验证码
		this.transPasswordService.saveSmsCode(mobile, checkCode);
		// 最大间隔时间
		Integer maxValidTime = smsConfig.getMaxIntervalTime() == null ? 60 : smsConfig.getMaxIntervalTime();
		ret.setMaxValidTime(maxValidTime);
		ret.setStatus(TransPasswordDefine.STATUS_TRUE);
		ret.setInfo("短信发送成功");
		LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.SEND_CODE_ACTION);
		return ret;

	}

	/**
	 * 短信验证码校验
	 * 
	 * 用户注册数据提交（获取session数据并保存） 1.校验验证码
	 * 2.若验证码正确，则获取session数据，并将相应的注册数据写入数据库（三张表），跳转相应的注册成功界面
	 */
	@ResponseBody
	@RequestMapping(value = TransPasswordDefine.CHECK_CODE_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public boolean checkcode(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.CHECK_CODE_ACTION);
		JSONObject info = new JSONObject();
		// 手机号码(必须,数字,最大长度)
		String mobile = request.getParameter("mobile");
		if (!ValidatorCheckUtil.validateRequired(info, null, null, mobile)) {
			return false;
		} else if (!ValidatorCheckUtil.validateMobile(info, null, null, mobile, 11, false)) {
			return false;
		}
		// 短信验证码
		String code = request.getParameter("code");
		if (!ValidatorCheckUtil.validateRequired(info, null, null, code)) {
			return false;
		}
		int cnt = this.transPasswordService.checkMobileCode(mobile, code);
		if (cnt > 0) {
			LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.CHECK_CODE_ACTION);
			return true;
		} else {
			LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.CHECK_CODE_ACTION);
			return false;
		}
	}
	
	/**
	 * 检查手机号是否已存在
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = TransPasswordDefine.CHECK_PHONE, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public boolean checkPhone(HttpServletRequest request) {
		LogUtil.startLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.CHECK_PHONE);

		String mobile = request.getParameter("mobile");
		//用户号为空
		if (mobile == null || "".equals(mobile.trim())) {
			return false;
		} 
		//手机号格式不正确
		if (!Validator.isMobile(mobile)) {
			return false;
		}
		//手机号已存在
//		if (loginService.existUser(mobile)) {
//			// 存在用户,返回false
//			return false;
//		}
		boolean isMobileExistUserFlag = this.loginService.existUser(mobile);
		if(isMobileExistUserFlag){
			WebViewUser users = WebUtils.getUser(request);
			return users.getMobile().equals(mobile);
		}
		return true;
	}
	
	
	/**
	 * 修改手机号成功页面
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(TransPasswordDefine.MOBILEMODIFY_SUCCESS_ACTION)
	public ModelAndView successMobile(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.MOBILEMODIFY_SUCCESS_ACTION);
		ModelAndView modelAndView = new ModelAndView(TransPasswordDefine.MOBILE_SUCCESS_PATH);
		LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.MOBILEMODIFY_SUCCESS_ACTION);
		return modelAndView;
	}

}
