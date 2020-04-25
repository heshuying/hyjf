/**
 * Description:开户控制器
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 *           Created at: 2015年12月4日 下午2:32:36
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.api.surong.user.bankopen;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseController;
import com.hyjf.client.autoinvestsys.InvestSysUtils;
import com.hyjf.common.http.HttpClientUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.BankUtil;
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

/**
 * @package com.hyjf.web.user.recharge
 * @author GOGTZ-T
 * @date 2015/12/04 14:00
 * @version V1.0  
 */
@Controller(OpenDefine.CONTROLLER_NAME)
@RequestMapping(value = OpenDefine.REQUEST_MAPPING)
public class OpenController extends BaseController {

	@Autowired
	private OpenService openAccountService;

	/** 当前controller名称 */
	public static final String THIS_CLASS = OpenController.class.getName();

	/**
	 * 开户画面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = OpenDefine.BANKOPEN_OPEN_ACTION)
	public ModelAndView openAccountInit(HttpServletRequest request, HttpServletResponse response) {
		
		String platform = request.getParameter("platform");
		String message = "";
		
		ModelAndView modelAndView = new ModelAndView();
		Integer userId = null;
		if(!Validator.isNull(request.getParameter("userId"))){
		    userId = Integer.valueOf(request.getParameter("userId"));
		}
		if (Validator.isNull(userId)) {
			message = "用户未登陆，请先登陆！";
			modelAndView = new ModelAndView(OpenDefine.BANKOPEN_OPEN_ERROR_PATH);
			modelAndView.addObject(OpenDefine.STATUSDESC, message);
			return modelAndView;
		}else{
			String mobile = ""; 
			try {
				mobile = this.openAccountService.getUsersMobile(userId);
			} catch (Exception e) {
				mobile = "";
			}
			if (StringUtils.isBlank(mobile)) {
				mobile = "";
			}
			String logOrderId = GetOrderIdUtils.getOrderId2(userId);
			modelAndView = new ModelAndView(OpenDefine.BANKOPEN_OPEN_PATH);
			modelAndView.addObject(OpenDefine.MOBILE,mobile);
			modelAndView.addObject(OpenDefine.USER_ID,userId);
			modelAndView.addObject(OpenDefine.LOGORDERID,logOrderId);
			modelAndView.addObject(OpenDefine.PLATFORM,platform);
		}
		System.out.println("用户银行开户获取登陆用户userId----------------------:" + userId);
				
		return modelAndView;
	}
	
	
	
	/**
     * 开户画面
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/openinit")
    public ModelAndView openAccount(HttpServletRequest request, HttpServletResponse response) {
        String message = "";
        
        ModelAndView modelAndView = new ModelAndView();
        Integer userId = Integer.valueOf(request.getParameter("userId"));
        if (Validator.isNull(userId)) {
            message = "用户未登陆，请先登陆！";
            modelAndView = new ModelAndView(OpenDefine.BANKOPEN_OPEN_ERROR_PATH);
            modelAndView.addObject(OpenDefine.STATUSDESC, message);
            return modelAndView;
        }else{
            String mobile = ""; 
            try {
                mobile = this.openAccountService.getUsersMobile(userId);
            } catch (Exception e) {
                mobile = "";
            }
            if (StringUtils.isBlank(mobile)) {
                mobile = "";
            }
            String logOrderId = GetOrderIdUtils.getOrderId2(userId);
            modelAndView = new ModelAndView(OpenDefine.BANKOPEN_OPEN_PATH);
            modelAndView.addObject(OpenDefine.MOBILE,mobile);
            modelAndView.addObject(OpenDefine.USER_ID,userId);
            modelAndView.addObject(OpenDefine.LOGORDERID,logOrderId);
        }
        System.out.println("用户银行开户获取登陆用户userId----------------------:" + userId);
                
        return modelAndView;
    }

	/**
	 * 开户成功画面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = OpenDefine.BANKOPEN_OPEN_SUCESS_ACTION)
	public ModelAndView openAccountSuccess(HttpServletRequest request, HttpServletResponse response) {
        Integer userId = null;
        if(!Validator.isNull(request.getParameter("userId"))){
            userId = Integer.valueOf(request.getParameter("userId"));
        }
		String message = "开户成功";
		
		ModelAndView modelAndView = new ModelAndView();
		if (Validator.isNull(userId)) {
			message = "用户未登陆，请先登陆！";
			modelAndView = new ModelAndView(OpenDefine.BANKOPEN_OPEN_ERROR_PATH);
			modelAndView.addObject(OpenDefine.STATUSDESC, message);
			return modelAndView;
		}else{
			modelAndView = new ModelAndView(OpenDefine.BANKOPEN_OPEN_SUCCESS_PATH);
			modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
			modelAndView.addObject("activityFlg", true);
			modelAndView.addObject("activityMsg", "开户成功!");
		}
		System.out.println("用户银行开户成功跳转获取登陆用户userId----------------------:" + userId);
				
		return modelAndView;
	}
	/**
	 * 获取短信验证码
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = OpenDefine.BANKOPEN_SENDCODE_ACTION, produces = "application/json; charset=UTF-8")
	public String sendCode(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(THIS_CLASS, OpenDefine.BANKOPEN_SENDCODE_ACTION);
		OpenAjaxResultBean result = new OpenAjaxResultBean();
		String mobileStr = request.getParameter("phone");
		// 获取登陆用户userId
		String userIdStr = request.getParameter("userId");
		String errorDesc = "短信验证码发送失败!";
		int error = OpenDefine.ERROR_FAIL;
		if (Validator.isNull(userIdStr)) {
			result.setReturnMsg(errorDesc);
			result.setStatus(error);
			return JSONObject.toJSONString(result);
		}
		int userId = Integer.parseInt(userIdStr);
		Users users = this.openAccountService.getUsers(userId);
		if (Validator.isNull(users)) {
			errorDesc = "用户不存在！";
			result.setReturnMsg(errorDesc);
			result.setStatus(error);
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
					result.setReturnMsg(errorDesc);
					result.setStatus(error);
					return JSONObject.toJSONString(result);
				}
			} else {
				errorDesc = "用户信息错误，未获取到用户的手机号码！";
				result.setReturnMsg(errorDesc);
				result.setStatus(error);
				return JSONObject.toJSONString(result);
			}
		} else {
			if (StringUtils.isNotBlank(mobileStr) && !mobile.equals(mobileStr)) {
				errorDesc = "用户信息错误，用户的手机号码错误！";
				result.setReturnMsg(errorDesc);
				result.setStatus(error);
				return JSONObject.toJSONString(result);
			}
		}
		String logOrderId = GetOrderIdUtils.getOrderId2(userId);
		boolean openAccountLog = this.openAccountService.updateUserAccountLog(userId, userName, mobile, logOrderId,
				CustomConstants.CLIENT_WECHAT); 
		if (!openAccountLog) {
			result.setReturnMsg(errorDesc);
			result.setStatus(error);
			return JSONObject.toJSONString(result);
		}
		// 调用短信发送接口
		BankCallBean mobileBean = this.openAccountService.sendSms(userId, BankCallConstant.TXCODE_ACCOUNT_OPEN_PLUS,
				mobile, BankCallConstant.CHANNEL_PC);
		if (Validator.isNull(mobileBean)) {
			errorDesc = "短信验证码发送失败，请稍后再试！";
			result.setReturnMsg(errorDesc);
			result.setStatus(error);
			return JSONObject.toJSONString(result);
		}
		// 短信发送返回结果码
		String retCode = mobileBean.getRetCode();
		if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)
				&& !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
			errorDesc = "短信验证码发送失败！";
			result.setReturnMsg(errorDesc);
			result.setStatus(error);
			return JSONObject.toJSONString(result);
		}
		if (Validator.isNull(mobileBean.getSrvAuthCode()) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
			errorDesc = "短信验证码发送失败！";
			result.setReturnMsg(errorDesc);
			result.setStatus(error);
			return JSONObject.toJSONString(result);
		}
		// 业务授权码
		String srvAuthCode = mobileBean.getSrvAuthCode();
		if (Validator.isNotNull(mobileBean.getSrvAuthCode())) {
			// 保存用户开户日志
			boolean openAccountLogFlag = this.openAccountService.updateUserAccountLog(userId, logOrderId, srvAuthCode);
			if (!openAccountLogFlag) {
				errorDesc = "保存开户日志失败！";
				result.setReturnMsg(errorDesc);
				result.setStatus(error);
				return JSONObject.toJSONString(result);
			}
		}
		errorDesc = "短信发送成功！";
		error = OpenDefine.ERROR_SUCCESS;
		result.setReturnMsg(errorDesc);
		result.setStatus(error);
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
	@RequestMapping(method = RequestMethod.POST,value = OpenDefine.BANKOPEN_OPEN_ACCOUNT_ACTION,produces = "application/json; charset=utf-8")
	public String openAccount(HttpServletRequest request, HttpServletResponse respons,
			@ModelAttribute OpenBean accountBean) {

		LogUtil.startLog(THIS_CLASS, OpenDefine.BANKOPEN_OPEN_ACTION);
		OpenAjaxResultBean result = new OpenAjaxResultBean();
		String errorDesc = "开户失败";
		int error = OpenDefine.ERROR_FAIL;
		// 获取相应的订单号
		String logOrderId = request.getParameter(BankCallConstant.PARAM_LOGORDERID);
		// 获取登陆用户userId
//		String sign = request.getParameter("sign");
		Integer userId = null; // 用户ID
		String platform = request.getParameter("platform");// 终端类型
		System.out.println("==================cwyang 终端类型为 " + platform);
		if (!Validator.isNull(accountBean.getUserId())) {
			userId = Integer.parseInt(accountBean.getUserId());
		}
		
		if (Validator.isNull(userId)) {
			errorDesc = "用户未登陆，请先登陆！";
			result.setReturnMsg(errorDesc);
			result.setStatus(error);
			return JSONObject.toJSONString(result);
		}
		System.out.println("用户银行开户获取登陆用户userId----------------------:" + userId);
		if (Validator.isNull(logOrderId)) {
			result.setReturnMsg(errorDesc);
			result.setStatus(error);
			return JSONObject.toJSONString(result);
		}
		Users users = this.openAccountService.getUsers(userId);
		if (Validator.isNull(users)) {
			errorDesc = "获取用户信息失败！";
			result.setReturnMsg(errorDesc);
			result.setStatus(error);
			return JSONObject.toJSONString(result);

		}
		// 取得用户在汇付天下的客户号
		BankOpenAccount openAccount = openAccountService.getBankOpenAccount(userId);
		if (Validator.isNotNull(openAccount)) {
			errorDesc = "用户已开户！";
			result.setReturnMsg(errorDesc);
			result.setStatus(error);
			return JSONObject.toJSONString(result);

		}
		// 获取开户画面相应的参数
		// 获取用户的真实姓名
		String trueName = accountBean.getTrueName();
		if (StringUtils.isBlank(trueName)) {
			errorDesc = "真实姓名不能为空！";
			result.setReturnMsg(errorDesc);
			result.setStatus(error);
			return JSONObject.toJSONString(result);

		}else{
			//判断真实姓名是否包含特殊字符
			if (!ValidatorCheckUtil.verfiyChinaFormat(trueName)) {
				errorDesc = "真实姓名不能包含空格！";
				result.setReturnMsg(errorDesc);
				result.setStatus(error);
				return JSONObject.toJSONString(result);
			}
			if (trueName.length() > 10) {
				errorDesc = "真实姓名不能超过10位";
				result.setReturnMsg(errorDesc);
				result.setStatus(error);
				return JSONObject.toJSONString(result);
			}
		}
		// 获取用户的身份证号
		String idNo = accountBean.getIdNo();
		if (StringUtils.isBlank(idNo)) {
			errorDesc = "身份证号不能为空！";
			result.setReturnMsg(errorDesc);
			result.setStatus(error);
			return JSONObject.toJSONString(result);

		}
		
		boolean isOnly = openAccountService.checkIdNo(idNo);
		if (!isOnly) {
			errorDesc = "身份证已存在!";
			result.setReturnMsg(errorDesc);
			result.setStatus(error);
			return JSONObject.toJSONString(result);
		}
		
		// 获取用户的银行卡号
		String cardNo = accountBean.getCardNo();
		if (StringUtils.isBlank(cardNo)) {
			errorDesc = "银行卡号不能为空！";
			result.setReturnMsg(errorDesc);
			result.setStatus(error);
			return JSONObject.toJSONString(result);
		}
		// 获取用户开户的短信验证码
		String smsCode = accountBean.getSmsCode();
		if (StringUtils.isBlank(smsCode)) {
			errorDesc = "短信验证码不能为空！";
			result.setReturnMsg(errorDesc);
			result.setStatus(error);
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
					result.setReturnMsg(errorDesc);
					result.setStatus(error);
					return JSONObject.toJSONString(result);
				}
			} else {
				errorDesc = "用户信息错误，未获取到用户的手机号码！";
				result.setReturnMsg(errorDesc);
				result.setStatus(error);
				return JSONObject.toJSONString(result);
			}
		} else {
			if (StringUtils.isNotBlank(accountBean.getMobile()) && !mobile.equals(accountBean.getMobile())) {
				errorDesc = "用户信息错误，用户的手机号码错误！";
				result.setReturnMsg(errorDesc);
				result.setStatus(error);
				return JSONObject.toJSONString(result);
			}
		}
		// 获取相应的短信发送日志
		BankOpenAccountLog openAccountLog = this.openAccountService.selectUserAccountLog(userId, logOrderId);
		if (Validator.isNull(openAccountLog)) {
			System.out.println("=======================cwyang 无短信发送日志! ================");
			result.setReturnMsg(errorDesc + "请重新获取验证码!");
			result.setStatus(error);
			return JSONObject.toJSONString(result);
		}
		// 获取短信授权码
		String srvAuthCode = openAccountLog.getLastSrvAuthCode();
		if (StringUtils.isBlank(srvAuthCode)) {// add by cwyang 短信验证码判空
			errorDesc = "短信验证码获取失败！";
			result.setReturnMsg(errorDesc);
			result.setStatus(error);
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
		if (Validator.isNull(platform)) {
			openAccoutBean.setLogClient(Integer.parseInt(platform));
		}
		
		// 保存用户开户日志
		boolean openAccountLogFlag = this.openAccountService.updateUserAccountLog(openAccountLog, openAccoutBean);
		if (!openAccountLogFlag) {
			errorDesc = "保存开户日志失败！";
			result.setReturnMsg(errorDesc);
			result.setStatus(error);
			return JSONObject.toJSONString(result);
		}
		try {
			BankCallBean openAccountResult = BankCallUtils.callApiBg(openAccoutBean);
			String retCode = StringUtils.isNotBlank(openAccountResult.getRetCode()) ? openAccountResult.getRetCode()
					: "";
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
				this.openAccountService.updateUserAccountLog(userId, logOrderId, 4);
				String retMsg = openAccountService.getBankRetMsg(retCode);
				System.out.println("====================The BankRetCode is " + retCode + "============");
				System.out.println("====================The BankRetMsg is " + retMsg + "============");
				errorDesc = retMsg;
				result.setReturnMsg(errorDesc);
				result.setStatus(error);
				return JSONObject.toJSONString(result);

			}
			if (openAccountResult!=null&&StringUtils.isNoneBlank(platform)) {
				openAccountResult.setLogClient(Integer.parseInt(platform));
			}
			
			// 保存用户的开户信息
			boolean saveBankAccountFlag = this.openAccountService.updateUserAccount(openAccountResult);
			if (!saveBankAccountFlag) {
				errorDesc = "开户失败，请联系客服！";
				result.setReturnMsg(errorDesc);
				result.setStatus(error);
				return JSONObject.toJSONString(result);
			} else {
				error = OpenDefine.ERROR_SUCCESS;
				errorDesc = "开户成功！";
				result.setReturnMsg(errorDesc);
				result.setStatus(error);
				this.callBack(userId.toString(),openAccountResult.getAccountId(),trueName,idNo,cardNo,BankUtil.getNameOfBank(cardNo));
				return JSONObject.toJSONString(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorDesc = "调用银行接口失败！";
			result.setReturnMsg(errorDesc);
			result.setStatus(error);
			return JSONObject.toJSONString(result);
		}
	}
	
	/**
     * 融东风回调方法
     */
    public static void callBack(String userId, String chinapnrUsrid,String truename,String idCard, String bankcard, String bank){
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userId);
        params.put("chinapnrUsrid", chinapnrUsrid);
        params.put("truename", truename);
        params.put("idCard", idCard);
        params.put("bankcard", bankcard);
        params.put("bank", bank);
        // 请求路径
        String requestUrl = PropUtils.getSystem("wcsr.open");
        InvestSysUtils.noRetPost(requestUrl,params);
    }
    

    /**
     * 无需等待返回的http请求
     * @param requestUrl
     * @param paramsMap
     */
    public static void noRetPost(String requestUrl, Map<String,String> paramsMap) {
            ExecutorService exec = Executors.newFixedThreadPool(15);
            NoRetTask task = new NoRetTask(requestUrl,paramsMap);
            exec.execute(task);
        }
    }

    /**
     * 无需等待返回的http请求类
     * @author zhangjinpeng
     *
     */
    class NoRetTask implements Runnable {
        String requestUrl = StringUtils.EMPTY;
        private Map<String, String> params;
        
        public NoRetTask(String requestUrl, Map<String, String> params){
            this.requestUrl = requestUrl;
            this.params = params;
        }
    
        @Override
        public void run() {
            // 调用服务接口
            HttpClientUtils.post(requestUrl, params);
        }


}
