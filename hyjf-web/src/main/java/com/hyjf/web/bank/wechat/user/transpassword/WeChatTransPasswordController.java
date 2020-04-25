/**
 * 个人设置控制器
 */
package com.hyjf.web.bank.wechat.user.transpassword;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.mongo.dao.ChinapnrSendLogDao;
import com.hyjf.mongo.entity.ChinapnrSendlog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseAjaxResultBean;
import com.hyjf.bank.service.user.transpassword.TransPasswordService;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.SmsConfig;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.bank.web.user.transpassword.TransPasswordController;
import com.hyjf.web.bank.web.user.transpassword.TransPasswordDefine;
import com.hyjf.web.bank.wechat.user.bankopen.BankOpenDefine;
import com.hyjf.web.bank.wechat.user.bindcard.WeChatBindCardDefine;

/**
 * @package com.hyjf.web.user.recharge
 * @author GOGTZ-T
 * @date 2015/12/04 14:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = WeChatTransPasswordDefine.REQUEST_MAPPING)
public class WeChatTransPasswordController extends BaseController {

	@Autowired
	private TransPasswordService transPasswordService;

    @Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;
	
	private static String HOST = PropUtils.getSystem("hyjf.web.host").trim();


    
    
	/**
	 * 检查参数的正确性
	 *
	 * @param userId
	 * @param transAmtStr
	 * @param bankId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = WeChatTransPasswordDefine.CHECK, produces = "application/json; charset=utf-8")
	private JSONObject check(HttpServletRequest request,  HttpServletResponse response) {
		JSONObject info = new JSONObject();
		// 用户id
		String userId = request.getParameter("userId");
		//更新标识   0设置1更新
		String isUpdate = request.getParameter("isUpdate");
		if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(isUpdate)){
			info.put("error", "1");
			info.put("errorDesc", "请求参数非法");
			return info;
		}
		//用户信息
		Users user = this.transPasswordService.getUsers(Integer.valueOf(userId));
		if(user == null){
			info.put("error", "1");
			info.put("errorDesc", "用户不存在");
			return info;
		}
		//开户标识
		Integer acountFlag = user.getOpenAccount();
		if(acountFlag == 0){
			info.put("error", "1");
			info.put("errorDesc", "用户未开户");
			return info;
		}
		
		if(isUpdate.equals("1")){
			//判断用户是否设置过交易密码
			Integer passwordFlag = user.getIsSetPassword();
			if(passwordFlag == 1){
				info.put("error", "1");
				info.put("errorDesc", "已设置交易密码");
				return info;
			}
		}
		info.put("error", "0");
		info.put("errorDesc", "校验通过");
		if(isUpdate.equals("1")){
			info.put("url", HOST+WeChatTransPasswordDefine.REQUEST_MAPPING + WeChatTransPasswordDefine.RESETPASSWORD_ACTION + ".do?userId="+userId);
		}else{
			info.put("url", HOST+WeChatTransPasswordDefine.REQUEST_MAPPING + WeChatTransPasswordDefine.SETPASSWORD_ACTION + ".do?userId="+userId);
		}
		return info;
	}
    
    
    
	/**
	 * 设置交易密码
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value =WeChatTransPasswordDefine.SETPASSWORD_ACTION)
	public ModelAndView setPassword(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.SETPASSWORD_ACTION);
		ModelAndView modelAndView = new ModelAndView();
		// 用户id
		String userId = request.getParameter("userId");
		//返回结果url，微信端提供
		String callback = request.getParameter("callback");
		//返回结果
		String status = "";
		String message = "";
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(callback)) {
		    status = "1";
		    message = "请求参数错误";
			modelAndView = new ModelAndView(returnUrl(status, message, callback));
			return modelAndView;
		}
		//用户
		Users user = this.transPasswordService.getUsers(Integer.valueOf(userId));
		UsersInfo usersInfo = this.transPasswordService.getUsersInfoByUserId(Integer.valueOf(userId));
		if(user == null){
		    status = "1";
		    message = "该用户不存在";
			modelAndView = new ModelAndView(returnUrl(status, message, callback));
			return modelAndView;
		}
		
		BankOpenAccount bankOpenAccount = transPasswordService.getBankOpenAccount(user.getUserId());
		if(bankOpenAccount == null){
		    status = "1";
		    message = "开户信息不存在";
			modelAndView = new ModelAndView(returnUrl(status, message, callback));
			return modelAndView;
		}
		// 同步调用路径
		String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + WeChatTransPasswordDefine.REQUEST_MAPPING
				+ WeChatTransPasswordDefine.RETURL_SYN_PASSWORD_ACTION + ".do?callback="+callback;
		// 异步调用路
		String bgRetUrl = PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) + WeChatTransPasswordDefine.REQUEST_MAPPING
				+ WeChatTransPasswordDefine.RETURN_ASY_PASSWORD_ACTION + ".do?callback="+callback;
		// 调用设置密码接口
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_PASSWORD_RESET_PAGE);// 消息类型(设置密码)
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
		bean.setTxDate(GetOrderIdUtils.getTxDate());
		bean.setTxTime(GetOrderIdUtils.getTxTime());
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
		bean.setChannel(BankCallConstant.CHANNEL_WEI);
		if(user.getUserType() == 1){ //企业用户 传组织机构代码
			CorpOpenAccountRecord record = transPasswordService.getCorpOpenAccountRecord(Integer.valueOf(userId));
			bean.setIdType(record.getCardType() != null ? String.valueOf(record.getCardType()) : BankCallConstant.ID_TYPE_COMCODE);// 证件类型 20：其他证件（组织机构代码）25：社会信用号
			bean.setIdNo(record.getBusiCode());
			bean.setName(record.getBusiName());
		}else{
			bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);
			bean.setIdNo(usersInfo.getIdcard());
			bean.setName(usersInfo.getTruename());
		}
		bean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));//电子账号
		bean.setMobile(user.getMobile());
		bean.setRetUrl(retUrl);// 页面同步返回 URL
		bean.setNotifyUrl(bgRetUrl);// 页面异步返回URL(必须)
		// 商户私有域，存放开户平台,用户userId
		LogAcqResBean acqRes = new LogAcqResBean();
		acqRes.setUserId(user.getUserId());
		bean.setLogAcqResBean(acqRes);
		// 操作者ID
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_PASSWORDRESETPAGE);
		bean.setLogOrderId(GetOrderIdUtils.getUsrId(user.getUserId()));
		// 跳转到江西银行接口
		try {
		    modelAndView.addObject(WeChatBindCardDefine.STATUS, WeChatBindCardDefine.STATUS_TRUE);
			modelAndView = BankCallUtils.callApi(bean);
			LogUtil.endLog(WeChatTransPasswordController.class.toString(), WeChatTransPasswordDefine.SETPASSWORD_ACTION);
		} catch (Exception e) {
		    status = "1";
		    message = "调用银行接口异常";
			modelAndView = new ModelAndView(returnUrl(status, message, callback));
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
	@RequestMapping(WeChatTransPasswordDefine.RETURL_SYN_PASSWORD_ACTION)
	public ModelAndView passwordReturn(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute BankCallBean bean) {

		LogUtil.startLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.RETURL_SYN_PASSWORD_ACTION, "[开户同步回调开始]");
		ModelAndView modelAndView = new ModelAndView();
		//返回地址
		String callback = request.getParameter("callback");
		bean.convert();
		LogAcqResBean acqes = bean.getLogAcqResBean();
		int userId = acqes.getUserId();
		Users user = this.transPasswordService.getUsers(userId);
		//返回结果
		String status = "";
		String message = "";
		// 返回失败
        if (bean.getRetCode()!=null&&!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
            status = "1";
            message = "交易密码设置失败,失败原因：" + transPasswordService.getBankRetMsg(bean.getRetCode());
            modelAndView = new ModelAndView(returnUrl(status, message, callback));
            return modelAndView;
        }
		//判断用户是否设置了交易密码
		boolean flag = user.getIsSetPassword() == 1 ? true : false ;
		if(flag){
		    status = "0";
		    message = "交易密码设置成功";
			modelAndView = new ModelAndView(returnUrl(status, message, callback));
			return modelAndView;
		}
		BankOpenAccount bankOpenAccount = transPasswordService.getBankOpenAccount(userId);
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
        selectbean.setLogUserId(String.valueOf(userId));
        selectbean.setLogOrderId(GetOrderIdUtils.getUsrId(userId));
        selectbean.setLogClient(0);
        // 返回参数
        BankCallBean retBean = null;
        // 调用接口
        retBean = BankCallUtils.callApiBg(selectbean);
        LogUtil.endLog(TransPasswordController.class.toString(), TransPasswordDefine.RETURL_SYN_PASSWORD_ACTION);
        

        if("1".equals(retBean.getPinFlag())){
            // 是否设置密码中间状态
            this.transPasswordService.updateUserIsSetPassword(user, 1);
            status = "0";
            message = "交易密码设置成功";
            modelAndView = new ModelAndView(returnUrl(status, message, callback));
            return modelAndView;
        }
		status = "1";
		message = "交易密码设置失败";
        modelAndView = new ModelAndView(returnUrl(status, message, callback));
		LogUtil.endLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.RETURL_SYN_PASSWORD_ACTION, "[交易完成后,回调结束]");
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
	@RequestMapping(WeChatTransPasswordDefine.RETURN_ASY_PASSWORD_ACTION)
	public String passwordBgreturn(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute BankCallBean bean) {
	    BankCallResult result = new BankCallResult();
		LogUtil.startLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.RETURN_ASY_PASSWORD_ACTION, "[开户异步回调开始]");
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
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.errorLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.RETURN_ASY_PASSWORD_ACTION, e);
			}
		} 
		LogUtil.endLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.RETURN_ASY_PASSWORD_ACTION, "[交易完成后,回调结束]");
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
	@RequestMapping(WeChatTransPasswordDefine.RESETPASSWORD_ACTION)
	public ModelAndView resetPassword(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.RESETPASSWORD_ACTION);
		ModelAndView modelAndView = new ModelAndView();
		// 用户id
        String userId = request.getParameter("userId");
		//返回地址
		String callback = request.getParameter("callback");
		//返回结果
		String error = "";
		String errorDesc = "";
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(callback)) {
		    error = "1";
		    errorDesc = "请求参数错误";
            modelAndView = new ModelAndView(returnUrl(error, errorDesc, callback));
            return modelAndView;
        }
        //用户
        Users user = this.transPasswordService.getUsers(Integer.valueOf(userId));
        UsersInfo usersInfo = this.transPasswordService.getUsersInfoByUserId(Integer.valueOf(userId));
		
        BankOpenAccount bankOpenAccount = transPasswordService.getBankOpenAccount(user.getUserId());
        if(bankOpenAccount == null){
            error = "1";
            errorDesc = "开户信息不存在";
            modelAndView = new ModelAndView(returnUrl(error, errorDesc, callback));
            return modelAndView;
        }
		// 同步调用路径
		String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + WeChatTransPasswordDefine.REQUEST_MAPPING
				+ WeChatTransPasswordDefine.RETURL_SYN_RESETPASSWORD_ACTION + ".do?callback="+callback;
		// 异步调用路
		String bgRetUrl = PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) + WeChatTransPasswordDefine.REQUEST_MAPPING
				+ WeChatTransPasswordDefine.RETURN_ASY_RESETPASSWORD_ACTION + ".do";
		
		// 调用设置密码接口
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_PASSWORD_RESET_PAGE);// 消息类型(用户开户)
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
		bean.setTxDate(GetOrderIdUtils.getTxDate());
		bean.setTxTime(GetOrderIdUtils.getTxTime());
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
		bean.setChannel(BankCallConstant.CHANNEL_WEI);
		if(user.getUserType() == 1){ //企业用户 传组织机构代码
			CorpOpenAccountRecord record = transPasswordService.getCorpOpenAccountRecord(Integer.valueOf(userId));
			bean.setIdType(record.getCardType() != null ? String.valueOf(record.getCardType()) : BankCallConstant.ID_TYPE_COMCODE);// 证件类型 20：其他证件（组织机构代码）25：社会信用号
			bean.setIdNo(record.getBusiCode());
			bean.setName(record.getBusiName());
		}else{
			bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);
			bean.setIdNo(usersInfo.getIdcard());
			bean.setName(usersInfo.getTruename());
		}
		bean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));//电子账号
		bean.setMobile(user.getMobile());
		bean.setRetUrl(retUrl);// 页面同步返回 URL
		bean.setNotifyUrl(bgRetUrl);// 页面异步返回URL(必须)
		// 商户私有域，存放开户平台,用户userId
		LogAcqResBean acqRes = new LogAcqResBean();
		acqRes.setUserId(user.getUserId());
		bean.setLogAcqResBean(acqRes);
		// 操作者ID
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_PASSWORDRESETPAGE);
		bean.setLogOrderId(GetOrderIdUtils.getUsrId(user.getUserId()));
		// 跳转到汇付天下画面
		try {
		    modelAndView.addObject(WeChatBindCardDefine.STATUS, WeChatBindCardDefine.STATUS_TRUE);
			modelAndView = BankCallUtils.callApi(bean);
			LogUtil.endLog(WeChatTransPasswordController.class.toString(), WeChatTransPasswordDefine.RESETPASSWORD_ACTION);
		} catch (Exception e) {
			e.printStackTrace();
			error = "1";
			errorDesc = "调用银行接口失败！";
			modelAndView = new ModelAndView(returnUrl(error, errorDesc, callback));
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
	@RequestMapping(WeChatTransPasswordDefine.RETURL_SYN_RESETPASSWORD_ACTION)
	public ModelAndView resetPasswordReturn(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute BankCallBean bean) {

		LogUtil.startLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.RETURL_SYN_RESETPASSWORD_ACTION, "[重置交易密码同步回调开始]");
		ModelAndView modelAndView = new ModelAndView();

		//返回地址
        String callback = request.getParameter("callback");
		// 返回失败
        if ((bean.getRetCode()!=null&&!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode()))) {
            modelAndView = new ModelAndView(returnUrl("1", "交易密码修改失败,失败原因：" + transPasswordService.getBankRetMsg(bean.getRetCode()), callback));
            return modelAndView;
        }
		
		modelAndView = new ModelAndView(returnUrl("0", "交易密码修改成功", callback));
		LogUtil.endLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.RETURL_SYN_RESETPASSWORD_ACTION, "[重置交易密码同步回调结束]");
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
	@RequestMapping(WeChatTransPasswordDefine.RETURN_ASY_RESETPASSWORD_ACTION)
	public String resetPasswordBgreturn(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute BankCallBean bean) {
	    BankCallResult result = new BankCallResult();
		LogUtil.startLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.RETURN_ASY_RESETPASSWORD_ACTION, "[重置交易密码回调开始]");
		result.setMessage("交易密码修改成功");
        result.setStatus(true);
        LogUtil.endLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.RETURN_ASY_RESETPASSWORD_ACTION, "[重置交易密码回调结束]");
        return JSONObject.toJSONString(result, true);
		
	}
	
	
	
	
	
	/**
     * 发送短信验证码（ajax请求） 短信验证码数据保存
     */
    @ResponseBody
    @RequestMapping(value = WeChatTransPasswordDefine.TRANS_PASSWORD_SEND_CODE_ACTION, produces = "application/json; charset=utf-8")
    public WeChatTransPasswordAjaxResultBean transPasswordSendCode(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.SEND_CODE_ACTION);
        WeChatTransPasswordAjaxResultBean ret = new WeChatTransPasswordAjaxResultBean();
        // 手机号码(必须,数字,最大长度)
        String mobile = request.getParameter("mobile");
        if (StringUtils.isEmpty(mobile)) {
            ret.setStatus(WeChatTransPasswordDefine.STATUS_FALSE); 
            ret.setMessage("请填写手机号!");
            return ret;
        } 
        //短信配置
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
                    LogUtil.errorLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.SEND_CODE_ACTION, e);
                }
                RedisUtils.set(ip + ":MaxIpCount", (Integer.valueOf(ipCount) + 1) + "", 24 * 60 * 60);
            }
            ret.setStatus(WeChatTransPasswordDefine.STATUS_FALSE); 
            ret.setMessage("该设备短信请求次数超限，请明日再试");
            LogUtil.errorLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.SEND_CODE_ACTION, "短信验证码发送失败", null);
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
                    LogUtil.errorLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.SEND_CODE_ACTION, e);
                }

                RedisUtils.set(mobile + ":MaxPhoneCount", (Integer.valueOf(count) + 1) + "", 24 * 60 * 60);
            }
            ret.setStatus(WeChatTransPasswordDefine.STATUS_FALSE); 
            ret.setMessage("该手机号短信请求次数超限，请明日再试");
            LogUtil.errorLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.SEND_CODE_ACTION, "短信验证码发送失败", null);
            return ret;
        }

        // 生成验证码
        String checkCode = GetCode.getRandomSMSCode(6);
        Map<String, String> param = new HashMap<String, String>();
        param.put("val_code", checkCode);
        // 发送短信验证码
        SmsMessage smsMessage = new SmsMessage(null, param, mobile, null, MessageDefine.SMSSENDFORMOBILE, null,
                CustomConstants.PARAM_TPL_ZHUCE, CustomConstants.CHANNEL_TYPE_NORMAL);
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
        //最大间隔时间
        Integer maxValidTime = smsConfig.getMaxIntervalTime() == null ? 60 : smsConfig.getMaxIntervalTime();
        ret.setMaxValidTime(maxValidTime);
        ret.setStatus(WeChatTransPasswordDefine.STATUS_TRUE); 
        ret.setMessage("短信发送成功");
        LogUtil.endLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.SEND_CODE_ACTION);
        return ret;

    }

    /**
     * 短信验证码校验
     * 
     * 用户注册数据提交（获取session数据并保存） 1.校验验证码
     * 2.若验证码正确，则获取session数据，并将相应的注册数据写入数据库（三张表），跳转相应的注册成功界面
     */
    @ResponseBody
    @RequestMapping(value = WeChatTransPasswordDefine.TRANS_PASSWORD_CHECK_CODE_ACTION, produces = "application/json; charset=utf-8")
    public WeChatTransPasswordAjaxResultBean transPasswordCheckCode(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.CHECK_CODE_ACTION);
        JSONObject info = new JSONObject();
        WeChatTransPasswordAjaxResultBean ret = new WeChatTransPasswordAjaxResultBean();
        // 手机号码(必须,数字,最大长度)
        String mobile = request.getParameter("mobile");
        if (!ValidatorCheckUtil.validateRequired(info, null, null, mobile)) {
            ret.setStatus(WeChatTransPasswordDefine.STATUS_FALSE); 
            ret.setMessage("手机号不能为空");
            return ret;
        } else if (!ValidatorCheckUtil.validateMobile(info, null, null, mobile, 11, false)) {
            ret.setStatus(WeChatTransPasswordDefine.STATUS_FALSE); 
            ret.setMessage("手机号格式有误");
            return ret;
        }
        // 短信验证码
        String code = request.getParameter("code");
        if (!ValidatorCheckUtil.validateRequired(info, null, null, code)) {
            ret.setStatus(WeChatTransPasswordDefine.STATUS_FALSE); 
            ret.setMessage("验证码不能为空");
            return ret;
        }
        int cnt = this.transPasswordService.checkMobileCode(mobile, code);
        if (cnt > 0) {
            LogUtil.endLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.CHECK_CODE_ACTION);
            ret.setStatus(WeChatTransPasswordDefine.STATUS_TRUE); 
            ret.setMessage("校验成功");
            return ret;
        } else {
            LogUtil.endLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.CHECK_CODE_ACTION);
            ret.setStatus(WeChatTransPasswordDefine.STATUS_FALSE); 
            ret.setMessage("验证码错误请重新输入");
            return ret;
        }
    }
	
	
	
	
	/**
	 * 修改手机号增强发送验证码接口
	 * @param ret 
	 */
	@ResponseBody
	@RequestMapping(value = WeChatTransPasswordDefine.SEND_PLUS_CODE_ACTION, produces = "application/json; charset=utf-8")
	public String sendPlusCode(HttpServletRequest request, HttpServletResponse response, String ret) {
		LogUtil.startLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.SEND_PLUS_CODE_ACTION);
		WeChatTransPasswordAjaxResultBean result = new WeChatTransPasswordAjaxResultBean();
		String message = "短信发送失败";
		boolean status = BankOpenDefine.STATUS_FALSE;
		// 获取登陆用户userId
        Integer userId = StringUtils.isNotBlank(request.getParameter("userId")) ? Integer.parseInt(request.getParameter("userId")) : null;
        if (Validator.isNull(userId)) {
            message = "用户未登陆，请先登陆！";
            result.setMessage(message);
            result.setStatus(status);
            return JSONObject.toJSONString(result);
        }

        Users users = this.transPasswordService.getUsers(userId);
        if (Validator.isNull(users)) {
            message = "获取用户信息失败！";
            result.setMessage(message);
            result.setStatus(status);
            return JSONObject.toJSONString(result);

        }
        String mobile = request.getParameter("mobile"); // 手机号
        if (StringUtils.isEmpty(mobile)) {
            result.setStatus(status);
            result.setMessage("手机号不能为空");
            return ret;
        }
        Users user = this.transPasswordService.getUsers(userId);
        // 判断用户是否开户   合规产品要求   未开户 只修改平台的手机号   已开户修改江西银行和平台的
        Integer accountFlag = user.getBankOpenAccount();
        if (accountFlag == 0) {// 0未开户 1已开户
            // 未开户  调用原来的发送验证码
            return JSONObject.toJSONString(transPasswordSendCode(request, response));
        }
        
		// 请求发送短信验证码
		BankCallBean bean = this.transPasswordService.sendSms(users.getUserId(), BankCallMethodConstant.TXCODE_MOBILE_MODIFY_PLUS, mobile, BankCallConstant.CHANNEL_WEI);
		if(bean == null){
		    message = "发送短信验证码异常";
            result.setMessage(message);
            result.setStatus(status);
            return JSONObject.toJSONString(result);
		}
		//返回失败
		if(!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())){
		    if("JX900651".equals(bean.getRetCode())){
		        //成功返回业务授权码
		        result.setMessage("发送验证码成功");
		        result.setStatus(WeChatTransPasswordDefine.STATUS_TRUE);
		        result.setSrvAuthCode(bean.getSrvAuthCode());
		        LogUtil.endLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.SEND_PLUS_CODE_ACTION);
		        return JSONObject.toJSONString(result);
		    }
		    message = "发送短信验证码失败，失败原因：" + transPasswordService.getBankRetMsg(bean.getRetCode());
            result.setMessage(message);
            result.setStatus(status);
            return JSONObject.toJSONString(result);
		}
		
		//成功返回业务授权码
        result.setMessage("发送验证码成功");
        result.setStatus(WeChatTransPasswordDefine.STATUS_TRUE);
        result.setSrvAuthCode(bean.getSrvAuthCode());
        LogUtil.endLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.SEND_PLUS_CODE_ACTION);
        return JSONObject.toJSONString(result);

	}
	   

	/**
	 * 修改手机号
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value =WeChatTransPasswordDefine.MOBILEMODIFY_ACTION, produces = "application/json; charset=utf-8")
	public String mobileModify(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.MOBILEMODIFY_ACTION);
		BaseAjaxResultBean result = new BaseAjaxResultBean();
		// 用户id
		String userId = request.getParameter("userId");
		//返回参数
		String message = "修改失败";
        boolean status = BankOpenDefine.STATUS_FALSE;
	
		if (StringUtils.isEmpty(userId)) {
		    message = "请求参数错误，用户ID为空";
			result.setMessage(message);
            result.setStatus(status);
            return JSONObject.toJSONString(result);
		}
		//接收参数
		String newMobile = request.getParameter("newMobile"); //手机号
		String srvAuthCode = request.getParameter("srvAuthCode");//业务授权码
		String smsCode = request.getParameter("smsCode");//校验码
		
		if (StringUtils.isEmpty(newMobile) ) {
            message = "手机号码不能为空";
            result.setMessage(message);
            result.setStatus(status);
            return JSONObject.toJSONString(result);
        }
       
        if (StringUtils.isEmpty(smsCode)) {
            message = "请验证码不能为空";
            result.setMessage(message);
            result.setStatus(status);
            return JSONObject.toJSONString(result);
        }
		
        Users user = this.transPasswordService.getUsers(Integer.parseInt(userId));
        // 判断用户是否开户   合规产品要求   未开户 只修改平台的手机号   已开户修改江西银行和平台的
        Integer accountFlag = user.getBankOpenAccount();
        if (accountFlag == 0) {// 0未开户 1已开户
            // 未开户  直接修改数据库手机号
            // 验证验证码是否正确
            
            int cnt = this.transPasswordService.checkMobileCode(newMobile, smsCode);
            if (cnt > 0) {
                //修改手机号
                this.transPasswordService.updateUserMobile(Integer.valueOf(userId), newMobile);
                message = "恭喜您手机号修改成功！";
                result.setMessage(message);
                result.setStatus(WeChatTransPasswordDefine.STATUS_TRUE);
                return JSONObject.toJSONString(result);
            } else {
                message = "验证码错误请重新输入";
                result.setMessage(message);
                result.setStatus(status);
                return JSONObject.toJSONString(result);
            }
        }
        if (StringUtils.isEmpty(srvAuthCode) ) {
            message = "请先获取验证码";
            result.setMessage(message);
            result.setStatus(status);
            return JSONObject.toJSONString(result);
        }
        
		BankOpenAccount bankOpenAccount = transPasswordService.getBankOpenAccount(Integer.valueOf(userId));
		// 调用电子账号手机号修改增强
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_MOBILE_MODIFY_PLUS);// 消息类型 修改手机号增强 mobileModifyPlus
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
		bean.setTxDate(GetOrderIdUtils.getTxDate());
		bean.setTxTime(GetOrderIdUtils.getTxTime());
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
		bean.setChannel(BankCallConstant.CHANNEL_WEI);
		bean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));//电子账号
		bean.setOption(BankCallConstant.OPTION_1);//修改
		bean.setMobile(newMobile);// 新手机号		
		bean.setLastSrvAuthCode(srvAuthCode);//业务授权码
		bean.setSmsCode(smsCode);//短信验证码
		// 商户私有域，存放开户平台,用户userId
		LogAcqResBean acqRes = new LogAcqResBean();
		acqRes.setUserId(Integer.valueOf(userId));
		bean.setLogAcqResBean(acqRes);
		// 操作者ID
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogOrderId(GetOrderIdUtils.getUsrId(Integer.valueOf(userId)));
		//返回参数
		BankCallBean retBean = null;
		try {
			//调用接口
		    retBean = BankCallUtils.callApiBg(bean);
			LogUtil.endLog(WeChatTransPasswordController.class.toString(), WeChatTransPasswordDefine.MOBILEMODIFY_ACTION);
		} catch (Exception e) {
			message = "调用银行接口异常";
            result.setMessage(message);
            result.setStatus(status);
            return JSONObject.toJSONString(result);
		}
		if(retBean == null){
			message = "修改手机号失败，系统异常";
            result.setMessage(message);
            result.setStatus(status);
            return JSONObject.toJSONString(result);
		}
		//返回失败
		if(!BankCallConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode())){
			message = "修改手机号失败,失败原因："+transPasswordService.getBankRetMsg(retBean.getRetCode());
            result.setMessage(message);
            result.setStatus(status);
            return JSONObject.toJSONString(result);
		}
		//修改手机号
		this.transPasswordService.updateUserMobile(Integer.valueOf(userId), newMobile);
		// 修改之后,发送CA认证客户信息修改MQ
		this.transPasswordService.sendCAMQ(Integer.parseInt(userId));
		message = "恭喜您手机号修改成功！";
        result.setMessage(message);
        result.setStatus(WeChatTransPasswordDefine.STATUS_TRUE);
        return JSONObject.toJSONString(result);

	}

	
	/**
	 * 发送短信验证码（ajax请求） 短信验证码数据保存
	 */
	@ResponseBody
	@RequestMapping(value = WeChatTransPasswordDefine.SEND_CODE_ACTION, produces = "application/json; charset=utf-8")
	public String sendCode(HttpServletRequest request, HttpServletResponse response) {
	    LogUtil.startLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.SEND_CODE_ACTION);
        WeChatTransPasswordAjaxResultBean ret = new WeChatTransPasswordAjaxResultBean();
        // 手机号码(必须,数字,最大长度)
        String mobile = request.getParameter("mobile");
        if (StringUtils.isEmpty(mobile)) {
            ret.setStatus(WeChatTransPasswordDefine.STATUS_FALSE); 
            ret.setMessage("请填写手机号!");
            return JSONObject.toJSONString(ret);
        } 
        //短信配置
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
                    LogUtil.errorLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.SEND_CODE_ACTION, e);
                }
                RedisUtils.set(ip + ":MaxIpCount", (Integer.valueOf(ipCount) + 1) + "", 24 * 60 * 60);
            }
            ret.setStatus(WeChatTransPasswordDefine.STATUS_FALSE); 
            ret.setMessage("该设备短信请求次数超限，请明日再试");
            LogUtil.errorLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.SEND_CODE_ACTION, "短信验证码发送失败", null);
            return JSONObject.toJSONString(ret);
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
                    LogUtil.errorLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.SEND_CODE_ACTION, e);
                }

                RedisUtils.set(mobile + ":MaxPhoneCount", (Integer.valueOf(count) + 1) + "", 24 * 60 * 60);
            }
            ret.setStatus(WeChatTransPasswordDefine.STATUS_FALSE); 
            ret.setMessage("该手机号短信请求次数超限，请明日再试");
            LogUtil.errorLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.SEND_CODE_ACTION, "短信验证码发送失败", null);
            return JSONObject.toJSONString(ret);
        }

        // 生成验证码
        String checkCode = GetCode.getRandomSMSCode(6);
        Map<String, String> param = new HashMap<String, String>();
        param.put("val_code", checkCode);
        // 发送短信验证码
        SmsMessage smsMessage = new SmsMessage(null, param, mobile, null, MessageDefine.SMSSENDFORMOBILE, null,
                CustomConstants.PARAM_TPL_ZHUCE, CustomConstants.CHANNEL_TYPE_NORMAL);
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
        //最大间隔时间
        Integer maxValidTime = smsConfig.getMaxIntervalTime() == null ? 60 : smsConfig.getMaxIntervalTime();
        ret.setMaxValidTime(maxValidTime);
        ret.setStatus(WeChatTransPasswordDefine.STATUS_TRUE); 
        ret.setMessage("短信发送成功");
        LogUtil.endLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.SEND_CODE_ACTION);
        return JSONObject.toJSONString(ret);

	}

	/**
	 * 短信验证码校验
	 * 
	 * 用户注册数据提交（获取session数据并保存） 1.校验验证码
	 * 2.若验证码正确，则获取session数据，并将相应的注册数据写入数据库（三张表），跳转相应的注册成功界面
	 */
	@ResponseBody
	@RequestMapping(value = WeChatTransPasswordDefine.CHECK_CODE_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String checkcode(HttpServletRequest request, HttpServletResponse response) {
	    LogUtil.startLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.CHECK_CODE_ACTION);
        JSONObject info = new JSONObject();
        WeChatTransPasswordAjaxResultBean ret = new WeChatTransPasswordAjaxResultBean();
        // 手机号码(必须,数字,最大长度)
        String mobile = request.getParameter("mobile");
        if (!ValidatorCheckUtil.validateRequired(info, null, null, mobile)) {
            ret.setStatus(WeChatTransPasswordDefine.STATUS_FALSE); 
            ret.setMessage("手机号不能为空");
            return JSONObject.toJSONString(ret);
        } else if (!ValidatorCheckUtil.validateMobile(info, null, null, mobile, 11, false)) {
            ret.setStatus(WeChatTransPasswordDefine.STATUS_FALSE); 
            ret.setMessage("手机号格式有误");
            return JSONObject.toJSONString(ret);
        }
        // 短信验证码
        String code = request.getParameter("code");
        if (!ValidatorCheckUtil.validateRequired(info, null, null, code)) {
            ret.setStatus(WeChatTransPasswordDefine.STATUS_FALSE); 
            ret.setMessage("验证码不能为空");
            return JSONObject.toJSONString(ret);
        }
        int cnt = this.transPasswordService.checkMobileCode(mobile, code);
        if (cnt > 0) {
            LogUtil.endLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.CHECK_CODE_ACTION);
            ret.setStatus(WeChatTransPasswordDefine.STATUS_TRUE); 
            ret.setMessage("校验成功");
            return JSONObject.toJSONString(ret);
        } else {
            LogUtil.endLog(WeChatTransPasswordDefine.THIS_CLASS, WeChatTransPasswordDefine.CHECK_CODE_ACTION);
            ret.setStatus(WeChatTransPasswordDefine.STATUS_FALSE); 
            ret.setMessage("验证码错误请重新输入");
            return JSONObject.toJSONString(ret);
        }
	}
	
	
	/**
	 * 拼接返回结果
	 * 
	 * @param error
	 * @param errorDesc
	 * @param callback
	 * @return
	 */
	public String returnUrl(String error, String errorDesc, String callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("error", error);
		map.put("errorDesc", errorDesc);
		String data = JSON.toJSONString(map);
		try {
			data = URLEncoder.encode(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String url = callback + "backinfo/" + data;
		return "redirect:" + url;
	}
	
}
