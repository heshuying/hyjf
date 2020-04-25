package com.hyjf.api.surong.user.bindcard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.hyjf.api.surong.user.recharge.RdfRechargeService;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.user.bindcard.BindCardService;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.http.HttpDeal;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankCard;
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

@Controller
@RequestMapping(BindCardDefine.REQUEST_MAPPING)
public class BindCardServer extends BaseController{

	Logger _log = LoggerFactory.getLogger("BindCard");
	@Autowired
	private BindCardService bindCardService;
	
	@Autowired
	private RdfRechargeService rechargeService;
	
	/**
	 * 用户绑卡
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BindCardDefine.BIND_CARD)
	public ModelAndView bindCard(HttpServletRequest request, HttpServletResponse response) {
		//---传入参数---
		String sign = request.getParameter("sign");
		String mobile = request.getParameter("mobile"); // 用户ID
		String cardNo = request.getParameter("cardNo");
		String from = request.getParameter("from");  //来自于哪个客户端
		String retUrl = request.getParameter("retUrl");  //客户端同步回调url
		String callBackUrl = request.getParameter("callBackUrl"); //客户端异步回调url
		//---
		ModelAndView modelAndView = new ModelAndView(BindCardDefine.JSP_CHINAPNR_SEND);
		// 唯一标识
		if(StringUtils.isEmpty(sign)){
			_log.info("融东风绑卡sign值为空---");
			modelAndView = new ModelAndView(BindCardDefine.BINDCARD_ERROR_PATH);
			return modelAndView;
		}
        String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);
        
		String miwen =  MD5.toMD5Code(accessKey + mobile + accessKey);
		
		if(!miwen.equals(sign)){
			_log.info("融东风绑卡sign值非法---"+sign);
			modelAndView = new ModelAndView(BindCardDefine.BINDCARD_ERROR_PATH);
			return modelAndView;
		}
		
		Users user = rechargeService.findUserByMobile(mobile);
		if(user == null){
			_log.info(mobile+"用户不存在汇盈金服账户---");
			modelAndView = new ModelAndView(BindCardDefine.BINDCARD_ERROR_PATH);
			return modelAndView;
		}
		Integer userId = user.getUserId();	
		if (Validator.isNull(cardNo)) {
			modelAndView = new ModelAndView(BindCardDefine.BINDCARD_ERROR_PATH);
			modelAndView.addObject("message", "获取银行卡号为空");
			return modelAndView;
		}
		if (userId == null || userId == 0) {
			modelAndView = new ModelAndView(BindCardDefine.BINDCARD_ERROR_PATH);
			modelAndView.addObject("message", "用户未登录");
			return modelAndView;
		}
		// 取得用户在汇付天下的客户号
		BankOpenAccount accountChinapnrTender = bindCardService.getBankOpenAccount(userId);
		if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getAccount())) {
			modelAndView = new ModelAndView(BindCardDefine.BINDCARD_ERROR_PATH);
			modelAndView.addObject("message", "用户未开户");
			return modelAndView;
		}
		Users users = bindCardService.getUsers(userId);
		UsersInfo usersInfo = bindCardService.getUsersInfoByUserId(userId);
		// 调用汇付接口(4.2.2 用户绑卡接口)
		BankCallBean bean = new BankCallBean();
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_MOBILE);
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_CARD_BIND);
		bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
		bean.setChannel(BankCallConstant.CHANNEL_APP);//
		bean.setAccountId(accountChinapnrTender.getAccount());// 存管平台分配的账号
		bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);// 证件类型01身份证
		bean.setIdNo(usersInfo.getIdcard());// 证件号
		bean.setName(usersInfo.getTruename());// 姓名
		bean.setMobile(users.getMobile());// 手机号
		bean.setCardNo(cardNo);// 银行卡号
		bean.setRetUrl(PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + BindCardDefine.REQUEST_MAPPING + BindCardDefine.RETURN_MAPPING+"?mobile="+mobile+"&retUrl="+retUrl+"&from="+from);// 商户前台台应答地址(必须)
		bean.setNotifyUrl(PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + BindCardDefine.REQUEST_MAPPING + BindCardDefine.NOTIFY_RETURN_MAPPING+"?mobile="+mobile+"&callBackUrl="+callBackUrl+"&from="+from); // 商户后台应答地址(必须)
		bean.setUserIP(GetCilentIP.getIpAddr(request));// 客户IP
		LogAcqResBean logAcq = new LogAcqResBean();
		logAcq.setCardNo(cardNo);
		bean.setLogAcqResBean(logAcq);
		// 跳转到江西银行画面
		try {
			_log.info("【调用江西银行绑卡接口  orderId:"+ bean.getLogOrderId()+ " -mobile:"+bean.getMobile()+" cardNo:"+bean.getCardNo()+" -platform:"+bean.getLogClient()+"】");
			modelAndView = BankCallUtils.callApi(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelAndView;
	}
	
	//------------------------------------------------------------------------------------
	//返回参数--- status: 0客户端不做处理 1客户端成功处理 
	//          sign: 密文
	//          mobile:手机号
	//          cardNo:银行卡号
	//          bank: 银行代码
	//          cardType: 0 普通卡
	//          message
	/**
	 * 用户绑卡后处理
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(BindCardDefine.RETURN_MAPPING)
	public ModelAndView bindCardReturn(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
		bean.convert();
		_log.info("--↓↓ 绑卡同步回调 start ↓↓-- orderId: "+bean.getLogOrderId()+"--mobile="+request.getParameter("mobile")+" -retCode:"+bean.getRetCode());
		try {
			// 执行绑卡后处理
			this.bindCardService.updateAfterBindCard(bean);
		} catch (Exception e) {
			// 执行结果(失败)
			e.printStackTrace();
		}
		List<BankCard> accountBankList = bindCardService.getAccountBankByUserId(bean.getLogUserId());
		/* 给调用方返回数据的封装  --begin */
		ModelAndView modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_VIEW);
		BindCardResultBean bindCardResult = new BindCardResultBean();
		bindCardResult.setCallBackAction(request.getParameter("retUrl"));
		_log.info("retUrl="+request.getParameter("retUrl"));
        String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);	
		String mobile = request.getParameter("mobile");
		String miwen =  MD5.toMD5Code(accessKey + mobile + accessKey);
		bindCardResult.set("sign",miwen);
		bindCardResult.set("mobile",mobile);
		bindCardResult.set("status","1");
		System.out.println("同步回调返回码 ："+bean.getRetCode());
		/* 给调用方返回数据的封装  --end */
		if (accountBankList != null && accountBankList.size() > 0) {
			BankCard bankCard = accountBankList.get(0);
			bindCardResult.set("cardNo",bankCard.getCardNo());
			bindCardResult.set("bank", bean.getBankCode());
			bindCardResult.set("cardType", "0");
			bindCardResult.set("status","0");
		} else {
			// add by cwyang 2017-5-3 银行处理属成功页面
			modelAndView.addObject("message", "银行处理中，请稍后查看");
		}
		modelAndView.addObject("callBackForm",bindCardResult);
		_log.info("--↑↑ 绑卡同步回调 end ↑↑--  【绑卡成功】");
		return modelAndView;
	}

	/**
	 * 用户绑卡后处理
	 *
	 * modelAndViewst
	 * 
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(BindCardDefine.NOTIFY_RETURN_MAPPING)
	public Object notifyReturn(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
		bean.convert();
		_log.info("--↓↓ 绑卡异步回调 start ↓↓-- orderId: "+bean.getLogOrderId()+"--mobile="+request.getParameter("mobile")+" -retCode:"+bean.getRetCode());
		BankCallResult bankCallResult = new BankCallResult();
		// 银行回调返码
		String retCode = bean == null ? "" : bean.getRetCode();
		Map<String, String> result = new HashMap<>();
		String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);	
        String mobile = request.getParameter("mobile");
		String miwen =  MD5.toMD5Code(accessKey + mobile + accessKey);
		// 成功
		if (BankCallStatusConstant.RESPCODE_SUCCESS.equals(retCode)) {
			try {
				// 执行绑卡后处理
				this.bindCardService.updateAfterBindCard(bean);
				List<BankCard> accountBankList = bindCardService.getAccountBankByUserId(bean.getLogUserId());
				BankCard bankCard = accountBankList.get(0);
				result.put("cardNo", bankCard.getCardNo());
				result.put("bank", bankCard.getBank());
				result.put("cardType", "0");
				result.put("status", "0");
				result.put("mobile", mobile);
				result.put("sign", miwen);
				_log.info("callBackUrl="+request.getParameter("callBackUrl"));
		    	String resultStr = HttpDeal.post(request.getParameter("callBackUrl"), result);
		    	_log.info("--↑↑ 绑卡异步回调 end ↑↑--  【--result="+resultStr+"--mobile="+result.get("mobile")+"】");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 执行结果(失败)
			String message = this.bindCardService.getBankRetMsg(retCode);
			_log.info("--↑↑ 绑卡异步回调 end ↑↑--  【"+message+"】");
		}
		bankCallResult.setStatus(true);
		return bankCallResult;
	}
}
