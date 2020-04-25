package com.hyjf.api.surong.user.recharge;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.user.recharge.RechargeService;
import com.hyjf.common.http.HttpDeal;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.vip.apply.ApplyDefine;
/**
 * 融东风 充值接口
 * @author ZPC
 *
 */
@Controller
@RequestMapping(RechargeDefine.REQUEST_MAPPING)
public class RechargeServer extends BaseController{
	
	Logger _log = LoggerFactory.getLogger("融东风充值Controller");
	
	@Autowired
	private RdfRechargeService rdfRechargeService;
	
	@Autowired
	private RechargeService bankRechargeService;
	
	/**
	 * 
	 * app端用户充值
	 * 
	 * @author liuyang
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(RechargeDefine.USER_RECHARGE_ACTION)
	public ModelAndView userRecharge(HttpServletRequest request, HttpServletResponse response) {
		// ---传入参数---
		String nid = request.getParameter("nid");
		String sign = request.getParameter("sign");
		String mobile = request.getParameter("mobile"); // 用户手机号
		String transAmt = request.getParameter("money");// 交易金额
		String cardNo = request.getParameter("cardNo");// 开户银行代号
		String platform = request.getParameter("platform");// 终端类型
		String from = request.getParameter("from");  //来自于哪个客户端
		String retUrl = request.getParameter("retUrl");  //调用方同步回调url
		String callBackUrl = request.getParameter("callBackUrl"); //调用方异步回调url
		// ---
		
		/* 调用方加密校验*/	
		if(StringUtils.isEmpty(sign)){
			_log.info("【sign值为空】");
			return null;
		}
		String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);
		String miwen =  MD5.toMD5Code(accessKey + mobile + transAmt + accessKey);
		if(!miwen.equals(sign)){
			_log.info("sign值非法---"+sign);
			return null;
		}
		/* ^^^调用方加密校验^^^  */
		
		Users user = rdfRechargeService.findUserByMobile(mobile);
		if(user == null){
			_log.info(mobile+"【用户不存在汇盈金服账户】");
			return null;
		}     
		Integer userId = user.getUserId();
		String username = user.getUsername();
		String message = "";
		JSONObject checkResult;
		ModelAndView modelAndView = new ModelAndView(RechargeDefine.JSP_CHINAPNR_SEND);
		// 检查参数
		checkResult = checkParam(request, userId, transAmt);
		if (checkResult != null) {
			message = checkResult.getString("statusDesc");
			modelAndView = new ModelAndView(RechargeDefine.RECHARGE_ERROR);
			modelAndView.addObject(RechargeDefine.MESSAGE, message);
			return modelAndView;
		}
		// 取得用户在银行的客户号
		BankOpenAccount accountChinapnrTender = bankRechargeService.getBankOpenAccount(userId);
		if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getAccount())) {
			message = "您还未开户，请开户后重新操作";
			modelAndView = new ModelAndView(RechargeDefine.RECHARGE_ERROR);
			modelAndView.addObject(RechargeDefine.MESSAGE, message);
			return modelAndView;
		}
		// 用户的电子账户
		String chinapnrUsrcustidTender = accountChinapnrTender.getAccount();
		// 身份证号
		String certId = this.bankRechargeService.getUserIdcard(userId);
		// 用户信息
		UsersInfo usersInfo = bankRechargeService.getUsersInfoByUserId(userId);
		BankCallBean bean = new BankCallBean();
		// 调用汇付接口(4.3.11. 自动扣款转账（商户用）)
		String returnUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + RechargeDefine.SURONG_REQUEST_HOME + RechargeDefine.REQUEST_MAPPING + RechargeDefine.RETURN_MAPPING;
		String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + RechargeDefine.SURONG_REQUEST_HOME + RechargeDefine.REQUEST_MAPPING + RechargeDefine.CALLBACK_MAPPING;// 支付工程路径
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_DIRECT_RECHARGE);// 交易代码
		bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_APP); // 交易渠道
		bean.setAccountId(chinapnrUsrcustidTender); // 电子账号
		bean.setIdType(BankCallConstant.ID_TYPE_IDCARD); // 证件类型
		bean.setIdNo(certId); // 身份证号
		bean.setName(usersInfo.getTruename()); // 姓名
		bean.setMobile(user.getMobile()); // 手机号
		bean.setCardNo(cardNo); // 银行卡号
		bean.setTxAmount(CustomUtil.formatAmount(transAmt)); // 交易金额
		bean.setCurrency(BankCallConstant.CURRENCY_TYPE_RMB); // 交易币种
		bean.setRetUrl(returnUrl+"?nid="+nid+"&retUrl="+retUrl+"&from="+from); // 前台跳转链接
		bean.setNotifyUrl(bgRetUrl+"?nid="+nid+"&callBackUrl="+callBackUrl+"&from="+from); // 后台通知链接
		bean.setUserIP(GetCilentIP.getIpAddr(request));// 客户IP
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));// 订单号
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogUserName(username);
		bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_MOBILE);// 银行请求详细url
		bean.setLogClient(Integer.parseInt(platform));// 充值平台
		// 用户充值前处理
		int cnt = this.bankRechargeService.insertRechargeInfo(bean);
		if (cnt > 0) {
			// 跳转到汇付天下画面
			try {
				modelAndView.addObject(RechargeDefine.STATUS, RechargeDefine.STATUS_TRUE);
				_log.info("【调用江西银行充值接口  orderId:"+ bean.getLogOrderId()+ " -mobile:"+bean.getMobile()+" cardNo:"+bean.getCardNo()+" -platform:"+bean.getLogClient()+"】");
				modelAndView = BankCallUtils.callApi(bean);
			} catch (Exception e) {
				message = ("系统异常");
				modelAndView = new ModelAndView(RechargeDefine.RECHARGE_ERROR);
				modelAndView.addObject(RechargeDefine.MESSAGE, message);
				return modelAndView;
			}
		} else {
			message = "请不要重复操作";
			modelAndView = new ModelAndView(RechargeDefine.RECHARGE_ERROR);
			modelAndView.addObject(RechargeDefine.MESSAGE, message);
			return modelAndView;
		}
		return modelAndView;
	}

	/**
	 * 
	 * 数据校验
	 * 
	 * @author liuyang
	 * @param request
	 * @param userId
	 * @param transAmt
	 * @param openBankId
	 * @return
	 */
	private JSONObject checkParam(HttpServletRequest request, Integer userId, String transAmtStr) {
		// 判断用户是否被禁用
		Users users = this.bankRechargeService.getUsers(userId);
		if (users == null || users.getStatus() == 1) {
			return jsonMessage("对不起,该用户已经被禁用。", "1");
		}
		if (users.getUserType() == 1) {
			return jsonMessage("对不起,企业用户只能通过线下充值。", "1");
		}
		// 检查参数(交易金额是否数字)
		if (Validator.isNull(transAmtStr) || !NumberUtils.isNumber(transAmtStr)) {
			return jsonMessage("请输入充值金额。", "1");
		}
		// 检查参数(交易金额是否大于0)
		BigDecimal transAmt = new BigDecimal(transAmtStr);
		if (transAmt.compareTo(BigDecimal.ZERO) != 1) {
			return jsonMessage("充值金额不能为负数或0", "1");
		}
		if (transAmt.compareTo(new BigDecimal(99999999.99)) > 0) {
			return jsonMessage("充值金额不能大于99,999,999.99元。", "1");
		}
		// 取得用户在银行的客户号
		BankOpenAccount accountChinapnrTender = this.bankRechargeService.getBankOpenAccount(userId);
		if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getAccount())) {
			return jsonMessage("用户未开户，请开户后再充值。", "1");
		}
		return null;
	}
	
	//--------------------------------------------------------------------------------------------------
	//返回参数--- status : 0客户端不做处理 1客户端做成功处理
	//          nid : 客户端充值号
    //          sign : 密文
	//          money : 充值金额
	//          balance : 实际到帐金额
	//          orderId : 汇盈充值orderId
	//          message
	
	/**
	 * 用户充值后同步处理
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(RechargeDefine.RETURN_MAPPING)
	public ModelAndView rechargeReturn(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
		bean.convert();
		_log.info("--↓↓ 充值同步回调 start ↓↓-- orderId: "+bean.getLogOrderId()+" nid="+request.getParameter("nid")+" -retCode:"+bean.getRetCode());
		/* 给调用方返回数据的封装 --begin */
		ModelAndView modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_VIEW);
		RechargeResultBean rechargeResult = new RechargeResultBean();
		rechargeResult.setCallBackAction(request.getParameter("retUrl"));
        String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);	
		String nid = request.getParameter("nid");
		String miwen =  MD5.toMD5Code(accessKey + nid + accessKey);
		/* 给调用方返回数据的封装  --end */
		String retCode = bean == null ? "" : bean.getRetCode();
		if (BankCallStatusConstant.RESPCODE_SUCCESS.equals(retCode)) {
			String ip = CustomUtil.getIpAddr(request);
			Map<String, String> params = new HashMap<String, String>();
			params.put("ip", ip);
			// 更新充值的相关信息
			JSONObject message = this.bankRechargeService.handleRechargeInfo(bean, params);
			// 充值成功
			if (message != null && message.get("error").equals("0")) {
				rechargeResult.set("money", bean.getTxAmount());
				rechargeResult.set("balance", bean.getTxAmount());
				rechargeResult.set("orderId", bean.getLogOrderId());
				rechargeResult.set("status", "1");
				rechargeResult.set("nid", nid);
				rechargeResult.set("sign", miwen);
			} else {
				// 充值失败
				modelAndView.addObject("message", message.get("data"));
				_log.info("【充值失败】");
				rechargeResult.set("status", "0");
			}
		} else {
			modelAndView.addObject("message", "验签失败！交易数据可能被篡改，请确认您的银行卡信息后重试。");
			_log.info("【验签失败！交易数据可能被篡改，请确认您的银行卡信息后重试。】");
			rechargeResult.set("status", "0");
		}
		modelAndView.addObject("callBackForm",rechargeResult);
		_log.info("--↑↑ 充值同步回调 end ↑↑-- nid="+request.getParameter("nid"));
		return modelAndView;
	}

	/**
	 * 用户充值后异步处理
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(RechargeDefine.CALLBACK_MAPPING)
	public Object rechargeCallback(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
		bean.convert();
		_log.info("--↓↓ 充值异步回调 start ↓↓-- orderId: "+bean.getLogOrderId()+" nid="+request.getParameter("nid")+" -retCode:"+bean.getRetCode());
		BankCallResult bankCallResult = new BankCallResult();
		String ip = CustomUtil.getIpAddr(request);
		Map<String, String> params = new HashMap<String, String>();
		params.put("ip", ip);
		// 更新充值的相关信息
		JSONObject message =this.bankRechargeService.handleRechargeInfo(bean, params);
		//给融东风返回参数
		// 充值成功
		if (message != null && message.get("error").equals("0")) {
			String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);	
	        String nid = request.getParameter("nid");
			String miwen =  MD5.toMD5Code(accessKey + nid + accessKey);
			Map<String, String> result = new HashMap<>();
			result.put("money", bean.getTxAmount());
			result.put("balance", bean.getTxAmount());
			result.put("orderId", bean.getLogOrderId());
			result.put("status", "1");
			result.put("nid", nid);
			result.put("sign", miwen);
	    	String resultStr = HttpDeal.post(request.getParameter("callBackUrl"), result);
	    	_log.info("--↑↑ 充值异步回调 end ↑↑--  【url="+request.getParameter("callBackUrl")+"--result="+resultStr+"--status="+result.get("status")+"--money="+result.get("money")+"--balance="+result.get("balance")+"--nid="+nid+"】");
		}
		bankCallResult.setStatus(true);
		return bankCallResult;
	}
}
