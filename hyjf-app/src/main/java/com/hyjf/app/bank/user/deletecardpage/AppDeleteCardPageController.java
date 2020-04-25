package com.hyjf.app.bank.user.deletecardpage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseMapBean;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.bank.user.auto.AutoDefine;
import com.hyjf.app.bank.user.deletecard.AppDeleteCardService;
import com.hyjf.app.user.manage.AppUserService;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.app.util.SignValue;
import com.hyjf.bank.service.user.deletecard.DeleteCardPageBean;
import com.hyjf.bank.service.user.deletecard.DeleteCardService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
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
import java.math.BigDecimal;
import java.util.Date;

/**
 * App银行卡解绑Controller
 * 
 * @author liuyang
 *
 */
@Controller
@RequestMapping(value = AppDeleteCardPageDefine.REQUEST_MAPPING)
public class AppDeleteCardPageController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(AppDeleteCardPageController.class);
	/** THIS_CLASS */
	private static final String THIS_CLASS = AppDeleteCardPageController.class.getName();

	@Autowired
	private AppDeleteCardService userDeleteCardService;
	@Autowired
	private DeleteCardService deleteCardService;
	@Autowired
	private AppUserService appUserService;

	@ResponseBody
	@RequestMapping(AppDeleteCardPageDefine.REQUEST_DELETE_CARD)
	public JSONObject getCashUrl(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, AppDeleteCardPageDefine.REQUEST_DELETE_CARD);
		JSONObject ret = new JSONObject();
		// 版本号
		String version = request.getParameter("version");
		// 网络状态
		String netStatus = request.getParameter("netStatus");
		// 平台
		String platform = request.getParameter("platform");
		// 随机字符串
		String randomString = request.getParameter("randomString");
		// 唯一标识
		String sign = request.getParameter("sign");
		// token
		String token = request.getParameter("token");
		// order
		String order = request.getParameter("order");
		// card 银行卡号
		String cardNo = request.getParameter("bankNumber");// 银行卡号

		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(token) || Validator.isNull(sign) || Validator.isNull(randomString)
				|| Validator.isNull(order) || Validator.isNull(cardNo)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			ret.put("request", AppDeleteCardPageDefine.GET_REQUEST_DELETE_CARD_REQUEST);
			return ret;
		}
		Integer userId = SecretUtil.getUserId(sign); // 取得用户ID
		if (userId == null || userId <= 0) {
			ret.put("status", "1");
			ret.put("statusDesc", "用户未登录！");
			ret.put("request", AppDeleteCardPageDefine.GET_REQUEST_DELETE_CARD_REQUEST);
			return ret;
		}
		// 取得用户在汇付天下的客户号
		BankOpenAccount accountChinapnrTender = userDeleteCardService.getBankOpenAccount(userId);
		if (accountChinapnrTender == null || StringUtils.isEmpty(accountChinapnrTender.getAccount())) {
			ret.put("status", "1");
			ret.put("statusDesc", "用户未开户！");
			ret.put("request", AppDeleteCardPageDefine.GET_REQUEST_DELETE_CARD_REQUEST);
			return ret;
		}
		// 用户余额大于零不让解绑
		Account account = userDeleteCardService.getAccount(userId);
		// 用户在银行的账户余额
		BigDecimal bankBalance = userDeleteCardService.getBankBalance(userId, accountChinapnrTender.getAccount());
		if ((Validator.isNotNull(account.getBankBalance()) && account.getBankBalance().compareTo(BigDecimal.ZERO) > 0)
				|| ((Validator.isNotNull(bankBalance) && bankBalance.compareTo(BigDecimal.ZERO) > 0))) {
			ret.put("status", "1");
			ret.put("statusDesc", "抱歉，银行卡解绑错误，请联系客服！");
			ret.put("request", AppDeleteCardPageDefine.GET_REQUEST_DELETE_CARD_REQUEST);
			return ret;
		}
		// 根据银行卡Id获取用户的银行卡信息
		BankCard bankCard = this.userDeleteCardService.getBankCardByCardNo(userId, cardNo);
		if (bankCard == null || StringUtils.isEmpty(bankCard.getCardNo())) {
			ret.put("status", "1");
			ret.put("statusDesc", "获取用户银行卡信息失败！");
			ret.put("request", AppDeleteCardPageDefine.GET_REQUEST_DELETE_CARD_REQUEST);
			return ret;
		}
		// 取得加密用的Key
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		// 验证Order
		if (!SecretUtil.checkOrder(key, token, randomString, order)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		try {
			ret.put("status", "0");
			ret.put("statusDesc", "成功");
			ret.put("request", AppDeleteCardPageDefine.GET_REQUEST_DELETE_CARD_REQUEST);
			StringBuffer sbUrl = new StringBuffer();
			sbUrl.append(PropUtils.getSystem(CustomConstants.HYJF_WEB_URL));
			sbUrl.append(request.getContextPath());
			sbUrl.append(AppDeleteCardPageDefine.REQUEST_MAPPING);
			sbUrl.append(AppDeleteCardPageDefine.REQUEST_INDEXPAGE);
			sbUrl.append("?").append("version").append("=").append(version);
			sbUrl.append("&").append("netStatus").append("=").append(netStatus);
			sbUrl.append("&").append("platform").append("=").append(platform);
			sbUrl.append("&").append("randomString").append("=").append(randomString);
			sbUrl.append("&").append("sign").append("=").append(sign);
			sbUrl.append("&").append("token").append("=").append(strEncode(token));
			sbUrl.append("&").append("order").append("=").append(strEncode(order));
			sbUrl.append("&").append("bankNumber").append("=").append(cardNo);
			logger.info("返回的解卡url为: {}", sbUrl.toString());
			ret.put("url", sbUrl.toString());
		} catch (Exception e) {
			ret.put("status", "1");
			ret.put("statusDesc", "获取解卡URL失败");
		}
		LogUtil.endLog(THIS_CLASS,AppDeleteCardPageDefine.REQUEST_DELETE_CARD);
		return ret;
	}
	/**
	 * 合规四期,解绑银行卡
	 * @param request
	 * @return
	 */
	@RequestMapping(value = AppDeleteCardPageDefine.REQUEST_INDEXPAGE)
	public ModelAndView deleteCardPage(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
		BaseMapBean baseMapBean = new BaseMapBean();
		String sign = request.getParameter("sign");
		String cardNo = request.getParameter("bankNumber");// 银行卡号
		// 平台
		String platform = request.getParameter("platform");
		// 版本号
		String version = request.getParameter("version");
		// 网络状态
		String netStatus = request.getParameter("netStatus");

		// 随机字符串
		String randomString = request.getParameter("randomString");
		// order
		String order = request.getParameter("order");
		// 获取sign缓存
		String value = RedisUtils.get(sign);
		SignValue signValue = JSON.parseObject(value, SignValue.class);
		String token = signValue.getToken();
		if (token == null) {
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未登录！");
			baseMapBean.setCallBackAction(CustomConstants.HOST + AppDeleteCardPageDefine.JUMP_HTML_ERROR_PATH);
			modelAndView.addObject("callBackForm", baseMapBean);
			return modelAndView;
		}
		// 判断用户是否登录
		Integer userId = SecretUtil.getUserId(sign);
		if (userId == null || userId <= 0) {
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未登录！");
			baseMapBean.setCallBackAction(CustomConstants.HOST + AppDeleteCardPageDefine.JUMP_HTML_ERROR_PATH);
			modelAndView.addObject("callBackForm", baseMapBean);
			return modelAndView;
		}
		Users user = userDeleteCardService.getUsersByUserId(userId);
		UsersInfo usersInfo = userDeleteCardService.getUsersInfoByUserId(userId);
		UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
		userOperationLogEntity.setOperationType(11);
		userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
		userOperationLogEntity.setPlatform(request.getParameter("realPlatform")==null?Integer.valueOf(platform):Integer.valueOf(request.getParameter("realPlatform")));
		userOperationLogEntity.setRemark("");
		userOperationLogEntity.setOperationTime(new Date());
		userOperationLogEntity.setUserName(user.getUsername());
		userOperationLogEntity.setUserRole(String.valueOf(usersInfo.getRoleId()));
		appUserService.sendUserLogMQ(userOperationLogEntity);
		// 取得用户在汇付天下的客户号
		BankOpenAccount accountChinapnrTender = userDeleteCardService.getBankOpenAccount(userId);
		if (accountChinapnrTender == null || StringUtils.isEmpty(accountChinapnrTender.getAccount())) {
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未开户！");
			baseMapBean.setCallBackAction(CustomConstants.HOST + AppDeleteCardPageDefine.JUMP_HTML_ERROR_PATH);
			modelAndView.addObject("callBackForm", baseMapBean);
			return modelAndView;
		}
		// 用户余额大于零不让解绑
		Account account = userDeleteCardService.getAccount(userId);
		// 用户在银行的账户余额
		BigDecimal bankBalance = userDeleteCardService.getBankBalance(userId, accountChinapnrTender.getAccount());
		if ((Validator.isNotNull(account.getBankBalance()) && account.getBankBalance().compareTo(BigDecimal.ZERO) > 0)
				|| ((Validator.isNotNull(bankBalance) && bankBalance.compareTo(BigDecimal.ZERO) > 0))) {
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, "抱歉，请先清空当前余额和待收后，再申请解绑。");
			baseMapBean.setCallBackAction(CustomConstants.HOST + AppDeleteCardPageDefine.JUMP_HTML_ERROR_PATH);
			modelAndView.addObject("callBackForm", baseMapBean);
			return modelAndView;
		}
		// 根据银行卡Id获取用户的银行卡信息
		BankCard bankCard = this.userDeleteCardService.getBankCardByCardNo(userId, cardNo);
		if (bankCard == null || StringUtils.isEmpty(bankCard.getCardNo())) {
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, "获取用户银行卡信息失败!");
			baseMapBean.setCallBackAction(CustomConstants.HOST + AppDeleteCardPageDefine.JUMP_HTML_ERROR_PATH);
			modelAndView.addObject("callBackForm", baseMapBean);
			return modelAndView;
		}
		try {
			// 同步调用路径
			StringBuffer sbf = new StringBuffer();
			sbf.append(PropUtils.getSystem(CustomConstants.HYJF_WEB_URL));
			sbf.append(request.getContextPath());
			sbf.append(AppDeleteCardPageDefine.REQUEST_MAPPING);
			sbf.append(AppDeleteCardPageDefine.RETURL_SYN_ACTION);
			sbf.append( ".do?userId=").append(userId);
			sbf.append("&").append("version").append("=").append(version);
			sbf.append("&").append("netStatus").append("=").append(netStatus);
			sbf.append("&").append("platform").append("=").append(platform);
			sbf.append("&").append("randomString").append("=").append(randomString);
			sbf.append("&").append("sign").append("=").append(sign);
			sbf.append("&").append("order").append("=").append(strEncode(order));
			String retUrl =sbf.toString();
			logger.info("==========解卡同步返回路径为:"+retUrl+"===================");
			/*String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
					+ AppDeleteCardPageDefine.REQUEST_MAPPING + AppDeleteCardPageDefine.RETURL_SYN_ACTION  + ".do?userId=" + userId+"";*/
			// 异步调用路
			String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
					+ AppDeleteCardPageDefine.REQUEST_MAPPING + AppDeleteCardPageDefine.RETURL_ASY_ACTION + ".do" ;
			// 拼装参数 调用江西银行
			String forgetPassworedUrl = CustomConstants.FORGET_PASSWORD_URL+ "?sign=" + sign + "&token=" + token+"&platform="+platform;
			DeleteCardPageBean bean = new DeleteCardPageBean();
			bean.setUserId(userId);
			bean.setTxCode(BankCallConstant.TXCODE_BIND_CARD_PAGE);
			bean.setAccountId(accountChinapnrTender.getAccount());
			bean.setName(usersInfo.getTruename());
			bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);
			bean.setIdNo(usersInfo.getIdcard());
			bean.setCardNo(bankCard.getCardNo());// 银行卡号
			bean.setMobile(user.getMobile());
			bean.setRetUrl(retUrl);
			bean.setForgotPwdUrl(forgetPassworedUrl);
			bean.setSuccessfulUrl(retUrl+"&isSuccess=1");
			bean.setNotifyUrl(bgRetUrl);
			bean.setChannel(BankCallConstant.CHANNEL_APP);// 交易渠道
			bean.setPlatform(platform);
			modelAndView = deleteCardService.getCallbankMV(bean);
			logger.info("解卡调用页面end");
			LogUtil.endLog(THIS_CLASS, AppDeleteCardPageDefine.REQUEST_INDEXPAGE);
			return modelAndView;
		} catch (Exception e) {
			logger.error("调用银行接口失败", e);
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, "调用银行接口失败！");
			baseMapBean.setCallBackAction(CustomConstants.HOST + AppDeleteCardPageDefine.JUMP_HTML_ERROR_PATH);
			modelAndView.addObject("callBackForm", baseMapBean);
			return modelAndView;
		}
	}
	/**
	 * 页面解卡同步回调
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(AppDeleteCardPageDefine.RETURL_SYN_ACTION)
	public ModelAndView unbindCardReturn(HttpServletRequest request, HttpServletResponse response,@ModelAttribute BankCallBean bean) {

		LogUtil.startLog(THIS_CLASS, AppDeleteCardPageDefine.RETURL_SYN_ACTION, "[解卡同步回调开始]");
		ModelAndView modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
		String isSuccess = request.getParameter("isSuccess");
		String userId = request.getParameter("userId");

		BaseMapBean baseMapBean = new BaseMapBean();
		//返回解卡页面
		String bindFlg = userDeleteCardService.getResultUrl(userId);
		baseMapBean.set("bindCardFlg",bindFlg);
		//返回sign值等
		baseMapBean.set("sign", request.getParameter("sign"));
		baseMapBean.set("platform", request.getParameter("platform"));
		baseMapBean.set("version", request.getParameter("version"));
		baseMapBean.set("netStatus", request.getParameter("netStatus"));
		baseMapBean.set("randomString", request.getParameter("randomString"));
		baseMapBean.set("order", request.getParameter("order"));
		bean.convert();
		// 银行返回响应代码
		String retCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
		logger.info("解卡同步返回值,用户ID:[" + userId + "],retCode:[" + retCode + "]");
		// 绑卡后处理
		try {
			if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)||"1".equals(isSuccess)) {
				// 成功
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
				baseMapBean.set(CustomConstants.APP_STATUS_DESC, "解卡成功");
				baseMapBean.setCallBackAction(CustomConstants.HOST + AppDeleteCardPageDefine.JUMP_HTML_SUCCESS_PATH);
				modelAndView.addObject("callBackForm", baseMapBean);
				logger.info("用户"+userId+"解卡成功后,回调结束");
				LogUtil.endLog(THIS_CLASS, AppDeleteCardPageDefine.RETURL_SYN_ACTION, "用户"+userId+"解卡成功后,回调结束");
				return modelAndView;
			}
			String retMsg = deleteCardService.getBankRetMsg(retCode);
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, "失败原因：" + retMsg);
			baseMapBean.setCallBackAction(CustomConstants.HOST + AppDeleteCardPageDefine.JUMP_HTML_ERROR_PATH);
			LogUtil.endLog(THIS_CLASS, AppDeleteCardPageDefine.RETURL_SYN_ACTION, "用户"+userId+"解卡失败,失败原因为"+retMsg);
			modelAndView.addObject("callBackForm", baseMapBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelAndView;
	}

	/**
	 * 解卡异步回调方法
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(AppDeleteCardPageDefine.RETURL_ASY_ACTION)
	public BankCallResult bgreturn(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BankCallBean bean) {

		BankCallResult result = new BankCallResult();
		logger.info("页面解卡异步回调start  retcode {}  userId  {}",bean.getRetCode(),bean.getLogUserId());
		bean.convert();
		int userId = Integer.parseInt(bean.getLogUserId());
		// 绑卡后处理
		try {
			if(BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())){
				logger.info("删除银行卡成功");
				// 删除银行卡信息
				userDeleteCardService.deleteBankCard(bean, userId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("页面解卡成功,用户ID:[" + userId + ",用户电子账户号:[" + bean.getAccountId() + "]");
		result.setStatus(true);
		return result;
	}
}
