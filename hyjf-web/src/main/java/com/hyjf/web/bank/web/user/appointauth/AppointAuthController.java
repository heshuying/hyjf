package com.hyjf.web.bank.web.user.appointauth;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.bank.service.user.appiontment.AppointmentService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.util.WebUtils;

/**
 * 账户总览
 *
 * @author HBZ
 */
@Controller
@RequestMapping(value = AppointAuthDefine.REQUEST_MAPPING)
public class AppointAuthController extends BaseController {
	@Autowired
	AppointmentService appointmentService;

	private static String HOST = PropUtils.getSystem("hyjf.web.host").trim();

	/**
	 * 自动出借授权
	 * @param request
	 * @param modelMap
	 * @param money
	 * @return
	 */
	@RequestMapping(AppointAuthDefine.APPOINTMENT_ACTION)
	public ModelAndView appointment(HttpServletRequest request, ModelMap modelMap,String money) {
		LogUtil.startLog(AppointAuthController.class.toString(), AppointAuthDefine.APPOINTMENT_ACTION);
		Integer userId = WebUtils.getUserId(request);

		AccountChinapnr accountChinapnrTender = appointmentService.getAccountChinapnr(userId);
		// 用户未在平台开户
		if (accountChinapnrTender == null) {
			modelMap.put("message", "用户开户信息不存在");
			return new ModelAndView(AppointAuthDefine.APPOINTMENT_FAIL_PATH);
		} 
		// 判断借款人开户信息是否存在
		if (accountChinapnrTender.getChinapnrUsrcustid() == null) {
			modelMap.put("message", "用户电子账号不存在");
			return new ModelAndView(AppointAuthDefine.APPOINTMENT_FAIL_PATH);
		} 
		// 预约接口查询
		Map<String, Object> appointmentMap = appointmentService.checkAppointmentStatus(userId, 1);
		boolean appointmentFlag = (boolean) appointmentMap.get("appointmentFlag");
		String isError = appointmentMap.get("isError") + "";
		if (appointmentFlag) {
			// 回调路径
			String returl = HOST + AppointAuthDefine.REQUEST_MAPPING + AppointAuthDefine.RETURL_SYN_ACTION + ".do?userId=" + userId;
			// 回调路径
			String notifyUrl = HOST + AppointAuthDefine.REQUEST_MAPPING + AppointAuthDefine.RETURL_ASY_ACTION + ".do?userId=" + userId;
			
			Long tenderUsrcustid = accountChinapnrTender.getChinapnrUsrcustid();
			
			// 调用存管接口
			BankCallBean bean = new BankCallBean();
			bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
			// 银行存管
			bean.setTxCode(BankCallMethodConstant.TXCODE_AUTOBID_AUTH);// 交易代码autoBidAuth
			bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
			bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
			bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
			bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
			bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
			bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道000001手机APP 000002网页  000003微信 000004柜面
			bean.setAccountId(String.valueOf(tenderUsrcustid));// 存管平台分配的账号19位
			bean.setOrderId(GetOrderIdUtils.getUsrId(userId));//订单号
			bean.setTxAmount("");//单笔投标金额的上限
			bean.setTotAmount("");//自动投标总金额上限（不算已还金额）
			bean.setRetUrl(returl);// 同步返回地址
			bean.setNotifyUrl(notifyUrl); // 异步返回地址
			bean.setAcqRes("");// 请求方保留
			
			// 操作者ID
			bean.setLogUserId(String.valueOf(userId));
			bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_MOBILE);
			bean.setLogOrderId(GetOrderIdUtils.getUsrId(userId));
			// 跳转到汇付天下画面
			try {
				return BankCallUtils.callApi(bean);
			} catch (Exception e) {
				e.printStackTrace();
				modelMap.put("message", "自动出借授权操作失败!");
				return new ModelAndView(AppointAuthDefine.APPOINTMENT_FAIL_PATH);
			}
		} else {
			Users user = appointmentService.getUsers(userId);
			Integer AuthStatus = 0;
			if (user.getAuthStatus() != null) {
				AuthStatus = user.getAuthStatus();
			}
			if (isError.equals("0")) {
				if (AuthStatus == 0) {
					// 开启授权操作
					appointmentService.updateUserAuthStatus(null, "1", String.valueOf(userId));
					modelMap.put("message", "恭喜你自动出借授权成功!");
				} else if (AuthStatus == 1) {
					// 已经开启授权
					modelMap.put("message", "恭喜你自动出借授权成功!");
				} 
				return new ModelAndView(AppointAuthDefine.APPOINTMENT_SUCCESS_PATH);
			} else {
				modelMap.put("message", "自动出借授权失败!");
				modelMap.put("error", "调用自动出借授权查询接口失败");
				return new ModelAndView(AppointAuthDefine.APPOINTMENT_FAIL_PATH);
			}
		}
	}

	
	/**
	 * 取消自动出借授权
	 * @param request
	 * @param modelMap
	 * @param money
	 * @return
	 */
	@RequestMapping(AppointAuthDefine.APPOINTMENT_CANCEL_ACTION)
	public ModelAndView appointmentCancel(HttpServletRequest request, ModelMap modelMap) {
		LogUtil.startLog(AppointAuthController.class.toString(), AppointAuthDefine.APPOINTMENT_ACTION);
		Integer userId = WebUtils.getUserId(request);

		AccountChinapnr accountChinapnrTender = appointmentService.getAccountChinapnr(userId);
		// 用户未在平台开户
		if (accountChinapnrTender == null) {
			modelMap.put("message", "用户开户信息不存在");
			return new ModelAndView(AppointAuthDefine.APPOINTMENT_FAIL_PATH);
		} 
		// 判断开户信息是否存在
		if (accountChinapnrTender.getChinapnrUsrcustid() == null) {
			modelMap.put("message", "用户电子账号不存在");
			return new ModelAndView(AppointAuthDefine.APPOINTMENT_FAIL_PATH);
		} 
		// 预约接口查询
		Map<String, Object> appointmentMap = appointmentService.checkAppointmentStatus(userId, 2);
		boolean appointmentFlag = (boolean) appointmentMap.get("appointmentFlag");
		String isError = appointmentMap.get("isError") + "";
		if (appointmentFlag) {
			//电子账号
			Long tenderUsrcustid = accountChinapnrTender.getChinapnrUsrcustid();
			// 调用存管接口
			BankCallBean bean = new BankCallBean();
			bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
			// 银行存管
			bean.setTxCode(BankCallMethodConstant.TXCODE_AUTOBID_AUTH_CANCEL);// 交易代码autoBidAuthCancel
			bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
			bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
			bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
			bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
			bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
			bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道000001手机APP 000002网页  000003微信 000004柜面
			bean.setAccountId(String.valueOf(tenderUsrcustid));// 存管平台分配的账号19位
			bean.setOrderId(GetOrderIdUtils.getUsrId(userId));//订单号
			bean.setOrgOrderId(appointmentMap.get("orderId").toString());//原订单号
			bean.setAcqRes("");// 请求方保留
			// 操作者ID
			bean.setLogUserId(String.valueOf(userId));
			bean.setLogOrderId(GetOrderIdUtils.getUsrId(userId));
			// 跳转到汇付天下画面
			try {
				BankCallBean retBean = BankCallUtils.callApiBg(bean);
				if(retBean == null){
					modelMap.put("message", "关闭自动出借授权失败!");
					modelMap.put("error", "调用自动出借授权取消接口失败");
					return new ModelAndView(AppointAuthDefine.APPOINTMENT_FAIL_PATH);
				}
				if(BankCallConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode())){
					// 关闭授权操作
					appointmentService.updateUserAuthStatus(null, "0", String.valueOf(userId));
					modelMap.put("message", "您的自动出借授权已取消");
					return new ModelAndView(AppointAuthDefine.APPOINTMENT_SUCCESS_PATH);
				}else{
					modelMap.put("message", "关闭自动出借授权失败!");
					modelMap.put("error", "调用自动出借授权取消接口失败,返回码："+retBean.getRetCode());
					return new ModelAndView(AppointAuthDefine.APPOINTMENT_FAIL_PATH);
				}
			} catch (Exception e) {
				e.printStackTrace();
				modelMap.put("message", "自动出借授权操作失败!");
				return new ModelAndView(AppointAuthDefine.APPOINTMENT_FAIL_PATH);
			}
		} else {
			Users user = appointmentService.getUsers(userId);
			Integer AuthStatus = 0;
			if (user.getAuthStatus() != null) {
				AuthStatus = user.getAuthStatus();
			}
			if (isError.equals("0")) {
				if (AuthStatus == 1) {
					// 关闭授权操作
					appointmentService.updateUserAuthStatus(null, "0", String.valueOf(userId));
					modelMap.put("message", "您的自动出借授权已取消!");
				} else if (AuthStatus == 0) {
					// 已经授权
					modelMap.put("message", "您的自动出借授权已取消!");
				}
				return new ModelAndView(AppointAuthDefine.APPOINTMENT_SUCCESS_PATH);
			} else if (isError.equals("2")) {
				// 用户还有汇添金计划
				modelMap.put("message", "关闭自动出借授权失败!");
				modelMap.put("error", "您还有有申购中/锁定中的汇添金计划，暂时不能取消授权");
				return new ModelAndView(AppointAuthDefine.APPOINTMENT_FAIL_PATH);
			} else {
				modelMap.put("message", "关闭自动出借授权失败!");
				modelMap.put("error", "调用自动出借授权查询接口失败");
				return new ModelAndView(AppointAuthDefine.APPOINTMENT_FAIL_PATH);
			}
		}

	}
	
	/**
	 * 
	 * @method: appointmentRetUrl
	 * @description: 预约授权同步回调
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 * @return: ModelAndView
	 * @mender: zhouxiaoshuai
	 * @date: 2016年7月26日 下午1:35:42
	 */
	// pay模块回调此方法
	@RequestMapping(AppointAuthDefine.RETURL_SYN_ACTION)
	public ModelAndView appointmentRetUrl(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute BankCallBean bean, String userId, ModelMap modelMap) {

		return new ModelAndView(AppointAuthDefine.APPOINTMENT_SUCCESS_PATH);
	}
	/**
	 * 预约授权异步回调
	 * @param request
	 * @param response
	 * @param bean
	 * @param userId
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(AppointAuthDefine.RETURL_ASY_ACTION)
	public ModelAndView appointmentNotifyUrl(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute BankCallBean bean, String userId, ModelMap modelMap) {

		if (userId == null) {
			modelMap.put("message", "自动出借授权失败!");
			modelMap.put("error", "回调的参数为null");
			return new ModelAndView(AppointAuthDefine.APPOINTMENT_FAIL_PATH);
		} 
		bean.convert();
		modelMap.put("userId", userId);
		if(BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())){
			// 关闭授权操作
			appointmentService.updateUserAuthStatus(null, "1", String.valueOf(userId));
			modelMap.put("message", "恭喜你自动出借授权成功!");
			return new ModelAndView(AppointAuthDefine.APPOINTMENT_SUCCESS_PATH);
		}else{
			modelMap.put("message", "自动出借授权失败!");
			modelMap.put("error", "自动出借授权失败,返回码："+bean.getRetCode());
			return new ModelAndView(AppointAuthDefine.APPOINTMENT_FAIL_PATH);
		}
	}
}
