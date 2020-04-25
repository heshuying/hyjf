package com.hyjf.web.bank.web.user.auth.paymentauth;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.auth.AuthBean;
import com.hyjf.bank.service.user.auth.AuthService;
import com.hyjf.common.enums.utils.ProtocolEnum;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.ProtocolTemplate;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.web.BaseController;
import com.hyjf.web.agreement.AgreementService;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.login.LoginService;
import com.hyjf.web.util.WebUtils;
import org.apache.commons.collections.CollectionUtils;
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
import java.util.List;

/**
 * 服务费授权
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = PaymentAuthPagePlusDefine.REQUEST_MAPPING)
public class PaymentAuthPagePlusController extends BaseController {

	Logger _log = LoggerFactory.getLogger(PaymentAuthPagePlusController.class);

	@Autowired
	private AuthService authService;

	@Autowired
	private LoginService loginService;

	@Autowired
	private AgreementService agreementService;

	/**
	 * 授权须知接口
	 * @param request
	 * @return
	 */
	@RequestMapping(PaymentAuthPagePlusDefine.AUTH_REQUIRE_ACTION)
	public ModelAndView authRequirePage(HttpServletRequest request) {
        _log.info("授权须知接口查询开始request：{}", request);
		ModelAndView modelAndView = new ModelAndView(PaymentAuthPagePlusDefine.AUTH_REQUIRE_PATH);;
		// 用户信息
		WebViewUser user = WebUtils.getUser(request);
        if (user == null){
            modelAndView = new ModelAndView(PaymentAuthPagePlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "用户未开户！");
            _log.info("授权须知接口查询异常：{}", "用户未开户！");
            return modelAndView;
        }
		UsersInfo userInfo = authService.getUsersInfoByUserId(user.getUserId());
        // 未开户
        if (userInfo == null || userInfo.getRoleId() == null){
            modelAndView = new ModelAndView(PaymentAuthPagePlusDefine.USER_AUTH_ERROR_PATH);
			modelAndView.addObject("message", "用户信息不存在！");
            _log.info("授权须知接口查询异常：{}", "用户信息不存在！");
			return modelAndView;
		}
		//1、出借人（投资人）2、借款人3、担保机构授权
		modelAndView.addObject("roleId", userInfo.getRoleId());
		//协议名称 动态获得
		List<ProtocolTemplate> list = agreementService.getdisplayNameDynamic();
		if(CollectionUtils.isNotEmpty(list)){
			//是否在枚举中有定义
			for (ProtocolTemplate p : list) {
				String protocolType = p.getProtocolType();
				String alia = ProtocolEnum.getAlias(protocolType);
				if (alia != null){
					modelAndView.addObject(alia, p.getDisplayName());
				}
			}
		}
		_log.info("授权须知接口查询结束");
		return modelAndView;
	}

	/**
	 * 
	 * 服务费授权
	 * 
	 * @author pcc
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(PaymentAuthPagePlusDefine.PAYMENT_AUTH_ACTION)
	public ModelAndView page(HttpServletRequest request,
			HttpServletResponse response) {

		LogUtil.startLog(PaymentAuthPagePlusDefine.THIS_CLASS,
				PaymentAuthPagePlusDefine.PAYMENT_AUTH_ACTION);
		ModelAndView modelAndView = new ModelAndView();

		// 用户id
		WebViewUser user = WebUtils.getUser(request);
		if (user == null) {
			modelAndView = new ModelAndView(
					PaymentAuthPagePlusDefine.USER_AUTH_ERROR_PATH);
			modelAndView.addObject("message", "登录失效，请重新登陆");
			return modelAndView;
		}
		Users users = authService.getUsers(user.getUserId());
		if (users.getBankOpenAccount() == 0) {// 未开户
			modelAndView = new ModelAndView(
					PaymentAuthPagePlusDefine.USER_AUTH_ERROR_PATH);
			modelAndView.addObject("message", "用户未开户！");
			return modelAndView;
		}

		// 判断用户是否设置过交易密码
		if (users.getIsSetPassword() == 0) {// 未设置交易密码
			modelAndView = new ModelAndView(
					PaymentAuthPagePlusDefine.USER_AUTH_ERROR_PATH);
			modelAndView.addObject("message", "用户未设置交易密码");
			return modelAndView;
		}

		// 判断是否授权过
		if (authService.checkIsAuth(user.getUserId(),
				AuthBean.AUTH_TYPE_PAYMENT_AUTH)) {
			modelAndView = new ModelAndView(
					PaymentAuthPagePlusDefine.USER_AUTH_ERROR_PATH);
			modelAndView.addObject("message", "用户已授权,无需重复授权");
			return modelAndView;
		}

		// 拼装参数 调用江西银行
		// 同步调用路径
		String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)
				+ PaymentAuthPagePlusDefine.REQUEST_MAPPING
				+ PaymentAuthPagePlusDefine.RETURL_SYN_ACTION + ".do?authType="+AuthBean.AUTH_TYPE_PAYMENT_AUTH;
		// 异步调用路
		String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)
				+ PaymentAuthPagePlusDefine.REQUEST_MAPPING
				+ PaymentAuthPagePlusDefine.RETURL_ASY_ACTION + ".do";

		UsersInfo usersInfo =authService.getUsersInfoByUserId(users.getUserId());
		// 用户ID
		AuthBean authBean = new AuthBean();
		authBean.setUserId(user.getUserId());
		authBean.setIp(CustomUtil.getIpAddr(request));
		authBean.setAccountId(user.getBankAccount());
		// 同步 异步
		authBean.setRetUrl(retUrl);
		authBean.setNotifyUrl(bgRetUrl);
		// 0：PC 1：微官网 2：Android 3：iOS 4：其他
		authBean.setPlatform(CustomConstants.CLIENT_PC);
		authBean.setAuthType(AuthBean.AUTH_TYPE_PAYMENT_AUTH);
		authBean.setChannel(BankCallConstant.CHANNEL_PC);
		authBean.setForgotPwdUrl(CustomConstants.FORGET_PASSWORD_URL);
		authBean.setName(usersInfo.getTruename());
		authBean.setIdNo(usersInfo.getIdcard());
		authBean.setIdentity(usersInfo.getRoleId()+"");
		authBean.setUserType(users.getUserType());
		// 跳转到汇付天下画面
		try {
			String orderId = GetOrderIdUtils.getOrderId2(authBean.getUserId());
			authBean.setOrderId(orderId);
			modelAndView = authService.getCallbankMV(authBean);
			authService.insertUserAuthLog(authBean.getUserId(), orderId,
					Integer.parseInt(authBean.getPlatform()), "5");
			LogUtil.endLog(PaymentAuthPagePlusController.class.toString(),
					PaymentAuthPagePlusDefine.PAYMENT_AUTH_ACTION);
		} catch (Exception e) {
			e.printStackTrace();
			modelAndView = new ModelAndView(
					PaymentAuthPagePlusDefine.USER_AUTH_ERROR_PATH);
			modelAndView.addObject("message", "调用银行接口失败！");
			LogUtil.errorLog(PaymentAuthPagePlusController.class.toString(),
					PaymentAuthPagePlusDefine.PAYMENT_AUTH_ACTION, e);
		}

		return modelAndView;
	}

	/**
	 * 
	 * 服务费授权
	 * 
	 * @author pcc
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@RequestMapping(PaymentAuthPagePlusDefine.RETURL_SYN_ACTION)
	public ModelAndView paymentauthReturn(HttpServletRequest request,
			HttpServletResponse response, @ModelAttribute BankCallBean bean) {

		LogUtil.startLog(PaymentAuthPagePlusDefine.THIS_CLASS,
				PaymentAuthPagePlusDefine.RETURL_SYN_ACTION, "[服务费授权同步回调开始]");
		ModelAndView modelAndView = new ModelAndView();
		bean.convert();
		String frontParams = request.getParameter("frontParams");
		
		if (StringUtils.isBlank(bean.getRetCode())
				&& StringUtils.isNotBlank(frontParams)) {
			JSONObject jsonParm = JSONObject.parseObject(frontParams);
			if (jsonParm.containsKey("RETCODE")) {
				bean.setRetCode(jsonParm.getString("RETCODE"));
			}
		}
		String retCode = bean.getRetCode();
		if (StringUtils.isNotBlank(retCode)
				&& !BankCallStatusConstant.RESPCODE_SUCCESS.equals(retCode)) {
			modelAndView = new ModelAndView(
					PaymentAuthPagePlusDefine.USER_AUTH_ERROR_PATH);
			modelAndView.addObject("message",
					authService.getBankRetMsg(bean.getRetCode()));
			return modelAndView;
		}

		// 出借人签约状态查询
		_log.info("服务费授权同步回调调用查询接口查询状态,userId:" + bean.getLogUserId());
		BankCallBean retBean = authService.getTermsAuthQuery(
				Integer.parseInt(bean.getLogUserId()),
				BankCallConstant.CHANNEL_PC);
		_log.info("服务费授权同步回调调用查询接口查询状态结束  结果为:"
				+ (retBean == null ? "空" : retBean.getRetCode()));
		String isSuccess = request.getParameter("isSuccess");
        if (!"1".equals(isSuccess)) {
        	modelAndView = new ModelAndView(PaymentAuthPagePlusDefine.USER_AUTH_ERROR_PATH);
            return modelAndView;
        }
		// 返回失败
		if (retBean != null
				&& BankCallStatusConstant.RESPCODE_SUCCESS.equals(retBean
						.getRetCode()) && "1".equals(retBean.getPaymentAuth())) {
			
			try {
				if(authService.checkDefaultConfig(retBean,
						AuthBean.AUTH_TYPE_PAYMENT_AUTH)){
		        	modelAndView = new ModelAndView(PaymentAuthPagePlusDefine.AUTH_TENDER_AGAIN_ERROR_PATH);
		            modelAndView.addObject("message", "授权期限过短或额度过低，<br>请重新授权！");
		            return modelAndView;
		        }
				retBean.setOrderId(bean.getLogOrderId());
				// 更新签约状态和日志表
				this.authService.updateUserAuth(
						Integer.parseInt(bean.getLogUserId()), retBean,
						AuthBean.AUTH_TYPE_PAYMENT_AUTH);
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.errorLog(PaymentAuthPagePlusDefine.THIS_CLASS,
						PaymentAuthPagePlusDefine.RETURL_ASY_ACTION, e);
			}
			
			// 成功
			WebViewUser webUser = loginService.getWebViewUserByUserId(Integer
					.parseInt(bean.getLogUserId()));
			WebUtils.sessionLogin(request, response, webUser);
		} else {
			modelAndView = new ModelAndView(
					PaymentAuthPagePlusDefine.USER_AUTH_ERROR_PATH);
			return modelAndView;
		}
		modelAndView = new ModelAndView(
				PaymentAuthPagePlusDefine.USER_AUTH_SUCCESS_PATH);
		modelAndView.addObject("message", "服务费授权成功");
		LogUtil.endLog(PaymentAuthPagePlusDefine.THIS_CLASS,
				PaymentAuthPagePlusDefine.RETURL_SYN_ACTION, "[服务费授权完成后,回调结束]");
		return modelAndView;
	}

	/**
	 * 服务费授权异步回调
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(PaymentAuthPagePlusDefine.RETURL_ASY_ACTION)
	public BankCallResult paymentauthBgreturn(HttpServletRequest request,
			HttpServletResponse response, @ModelAttribute BankCallBean bean) {
		BankCallResult result = new BankCallResult();
		_log.info("[服务费授权异步回调开始]");
		LogUtil.startLog(PaymentAuthPagePlusDefine.THIS_CLASS,
				PaymentAuthPagePlusDefine.RETURL_ASY_ACTION, "[服务费授权异步回调开始]");
		bean.convert();
		Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
		// 查询用户开户状态
		Users user = this.authService.getUsers(userId);
		if(authService.checkDefaultConfig(bean,
				AuthBean.AUTH_TYPE_PAYMENT_AUTH)){
	       	 _log.info("[用户服务费授权完成后,回调结束]");
	            result.setMessage("授权成功");
	            result.setStatus(true);
	            return result;
	       }
		// 成功
		if (user != null
				&& bean != null
				&& (BankCallConstant.RESPCODE_SUCCESS.equals(bean
						.get(BankCallConstant.PARAM_RETCODE)) && "1"
						.equals(bean.getPaymentAuth()))) {
			try {
				bean.setOrderId(bean.getLogOrderId());
				// 更新签约状态和日志表
				this.authService.updateUserAuth(
						Integer.parseInt(bean.getLogUserId()), bean,
						AuthBean.AUTH_TYPE_PAYMENT_AUTH);
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.errorLog(PaymentAuthPagePlusDefine.THIS_CLASS,
						PaymentAuthPagePlusDefine.RETURL_ASY_ACTION, e);
			}
		}
		LogUtil.endLog(PaymentAuthPagePlusDefine.THIS_CLASS,
				PaymentAuthPagePlusDefine.RETURL_ASY_ACTION,
				"[用户服务费授权完成后,回调结束]");
		_log.info("[用户服务费授权完成后,回调结束]");
		result.setMessage("服务费授权成功");
		result.setStatus(true);
		return result;
	}
}
