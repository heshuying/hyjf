package com.hyjf.app.bank.user.deletecard;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.app.user.manage.AppUserService;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.model.auto.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseMapBean;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

/**
 * App银行卡解绑Controller
 * 
 * @author liuyang
 *
 */
@Controller
@RequestMapping(value = AppDeleteCardDefine.REQUEST_MAPPING)
public class AppDeleteCardController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(AppDeleteCardController.class);
	/** THIS_CLASS */
	private static final String THIS_CLASS = AppDeleteCardController.class.getName();

	@Autowired
	private AppDeleteCardService userDeleteCardService;
	@Autowired
	private AppUserService appUserService;
	/**
	 * 用户删除银行卡
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppDeleteCardDefine.REQUEST_INDEX, produces = "application/json; charset=utf-8")
	public JSONObject deleteCard(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");
		LogUtil.startLog(THIS_CLASS, AppDeleteCardDefine.REQUEST_INDEX);
		JSONObject info = new JSONObject();
		info.put("request", AppDeleteCardDefine.REQUEST_HOME + AppDeleteCardDefine.REQUEST_MAPPING + AppDeleteCardDefine.REQUEST_INDEX + ".do");
		String sign = request.getParameter("sign");
		Integer userId = SecretUtil.getUserId(sign); // 用户ID
		String cardNo = request.getParameter("bankNumber");// 银行卡号
		String platform = request.getParameter("platform");

		logger.info("delete bankcard userId IS:{}, cardNo IS:{}", userId, cardNo);
		if (userId == null || userId == 0) {
			info.put(CustomConstants.APP_STATUS, 1);
			info.put(CustomConstants.APP_STATUS_DESC, "您未登陆，请先登录");
			info.put("successUrl", "");
            return info;
		}
		//判断用户是否开户
		Users users = userDeleteCardService.getUsers(userId);
		UsersInfo usersInfo = userDeleteCardService.getUsersInfoByUserId(userId);
		// 取得用户在汇付天下的客户号
		BankOpenAccount accountChinapnrTender = userDeleteCardService.getBankOpenAccount(userId);
		if (accountChinapnrTender == null || StringUtils.isEmpty(accountChinapnrTender.getAccount())) {
			info.put(CustomConstants.APP_STATUS, 1);
            info.put(CustomConstants.APP_STATUS_DESC, "用户未开户");
            info.put("successUrl", "");
            return info;
		}
		// 用户余额大于零不让解绑
		Account account = userDeleteCardService.getAccount(userId);
		// 用户在银行的账户余额
		BigDecimal bankBalance = userDeleteCardService.getBankBalance(userId, accountChinapnrTender.getAccount());
		if ((Validator.isNotNull(account.getBankBalance()) && account.getBankBalance().compareTo(BigDecimal.ZERO) > 0)
				|| ((Validator.isNotNull(bankBalance) && bankBalance.compareTo(BigDecimal.ZERO) > 0))) {
			info.put(CustomConstants.APP_STATUS, 1);
            info.put(CustomConstants.APP_STATUS_DESC, "抱歉，您还有部分余额，请清空后再删除银行卡！");
            info.put("successUrl", "");
            return info;
		}
		// 根据银行卡Id获取用户的银行卡信息
		BankCard bankCard = this.userDeleteCardService.getBankCardByCardNo(userId, cardNo);
		if (bankCard == null || StringUtils.isEmpty(bankCard.getCardNo())) {
			info.put(CustomConstants.APP_STATUS, 1);
            info.put(CustomConstants.APP_STATUS_DESC, "获取用户银行卡信息失败");
            info.put("successUrl", "");
            return info;
		}
		// 银行卡Id
		Integer cardId = bankCard.getId();
		// 调用汇付接口(4.2.6 删除银行卡接口)
		BankCallBean retBean = null;
		BankCallBean bean = new BankCallBean();
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogRemark("解绑银行卡");
		bean.setLogUserId(String.valueOf(userId));
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_CARD_UNBIND);
		bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_APP);// 交易渠道
		bean.setAccountId(accountChinapnrTender.getAccount());// 存管平台分配的账号
		bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);// 证件类型01身份证
		bean.setIdNo(usersInfo.getIdcard());// 证件号
		bean.setName(usersInfo.getTruename());// 姓名
		bean.setMobile(users.getMobile());// 手机号
		bean.setCardNo(cardNo);// 银行卡号
		LogAcqResBean logAcqResBean = new LogAcqResBean();
		logAcqResBean.setCardNo(cardNo);
		logAcqResBean.setCardId(cardId);
		bean.setLogAcqResBean(logAcqResBean);
		// 调用江西银行解绑银行卡接口
		try {
			retBean = BankCallUtils.callApiBg(bean);
			logger.info("调用江西银行解绑银行卡接口结束.... ");
		} catch (Exception e) {
			logger.error("调用江西银行解绑银行卡接口出错...", e);
			info.put(CustomConstants.APP_STATUS, 1);
            info.put(CustomConstants.APP_STATUS_DESC, "调用银行接口失败,请联系客服!");
            info.put("successUrl", "");
            return info;
		}
		// 回调数据处理
		if (retBean == null || !(BankCallStatusConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode()))) {
			LogUtil.debugLog(THIS_CLASS, "RetCode:" + (retBean == null ? "" : retBean.getRetCode()) + "&&&&&&&&&&& RetMsg:" + (retBean == null ? "" : retBean.getRetMsg()));
			info.put(CustomConstants.APP_STATUS, 1);
            info.put(CustomConstants.APP_STATUS_DESC, "抱歉，银行卡删除错误，请联系客服！");
            info.put("successUrl", "");
            return info;
		}

		logger.info("执行删除卡后处理,判断银行卡状态，删除平台本地银行卡信息...");
		// 执行删除卡后处理,判断银行卡状态，删除平台本地银行卡信息
		try {
			boolean isdelFlag = this.userDeleteCardService.updateAfterDeleteCard(bean, userId);
			// 删除失败
			if (!isdelFlag) {
				info.put(CustomConstants.APP_STATUS, 1);
	            info.put(CustomConstants.APP_STATUS_DESC, "抱歉，银行卡删除错误，请联系客服！");
	            info.put("successUrl", "");
	            return info;
			} else {
				BaseMapBean baseMapBean=new BaseMapBean();
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
				//baseMapBean.set(CustomConstants.APP_STATUS_DESC, "恭喜您！您的普通银行卡删除成功");
				baseMapBean.set(CustomConstants.APP_STATUS_DESC, "");
				baseMapBean.set("token", URLEncoder.encode(token, "UTF-8"));
				baseMapBean.set("sign", URLEncoder.encode(sign, "UTF-8"));
				Integer urlType = userDeleteCardService.getBandCardBindUrlType("BIND_CARD");
				baseMapBean.set("urlType", urlType.toString());//绑卡开关 0跳转老接口  1跳转新接口
				baseMapBean.setCallBackAction(CustomConstants.HOST + AppDeleteCardDefine.JUMP_HTML_SUCCESS_PATH);
				info.put(CustomConstants.APP_STATUS, 0);
                //info.put(CustomConstants.APP_STATUS_DESC, "恭喜您！您的普通银行卡删除成功");
                info.put(CustomConstants.APP_STATUS_DESC, "");
                info.put("successUrl", baseMapBean.getUrl());
                return info;
			}
		} catch (Exception e) {
			info.put(CustomConstants.APP_STATUS, 1);
            info.put(CustomConstants.APP_STATUS_DESC, "抱歉，银行卡删除错误，请联系客服！");
            info.put("successUrl", "");
		}
		LogUtil.endLog(THIS_CLASS, AppDeleteCardDefine.REQUEST_INDEX);
		return info;
	}
	
}
