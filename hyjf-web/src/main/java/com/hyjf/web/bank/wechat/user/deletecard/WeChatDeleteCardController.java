package com.hyjf.web.bank.wechat.user.deletecard;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.deletecard.DeleteCardService;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.web.BaseController;

/**
 * 微信端用户解绑银行卡
 * 
 * @author liuyang
 *
 */
@Controller
@RequestMapping(value = WeChatDeleteCardDefine.REQUEST_MAPPING)
public class WeChatDeleteCardController extends BaseController {

	/** 类名 */
	private static final String THIS_CLASS = WeChatDeleteCardController.class.getName();

	@Autowired
	private DeleteCardService userDeleteCardService;

	/**
	 * 用户删除银行卡
	 *
	 * @param request
	 * @param ret
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = WeChatDeleteCardDefine.REQUEST_INDEX, produces = "application/json; charset=utf-8")
	public JSONObject deleteCard(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, WeChatDeleteCardDefine.REQUEST_INDEX);
		JSONObject info = new JSONObject();
		// 银行卡Id
		String cardId = request.getParameter("cardId");
		// 用户Id
		String userIdStr = request.getParameter("userId");
		if (StringUtils.isEmpty(userIdStr)) {
			info.put("error", "1");
			info.put("errorDesc", "您未登陆，请先登录");
			return info;
		}
		Integer userId = Integer.parseInt(userIdStr);
		if(userId == null || userId == 0 ){
			info.put("error", "1");
			info.put("errorDesc", "您未登陆，请先登录");
			return info;
		}
		// 取得用户在汇付天下的客户号
		BankOpenAccount accountChinapnrTender = userDeleteCardService.getBankOpenAccount(userId);
		if (accountChinapnrTender == null || StringUtils.isEmpty(accountChinapnrTender.getAccount())) {
			info.put("error", "1");
			info.put("errorDesc", "用户未开户");
			return info;
		}
		// 用户余额大于零不让解绑
		Account account = userDeleteCardService.getAccount(userId);
		// 用户在银行的账户余额
		BigDecimal bankBalance = userDeleteCardService.getBankBalance(userId, accountChinapnrTender.getAccount());
		if ((Validator.isNotNull(account.getBankBalance()) && account.getBankBalance().compareTo(BigDecimal.ZERO) > 0)
				|| ((Validator.isNotNull(bankBalance) && bankBalance.compareTo(BigDecimal.ZERO) > 0))) {
			info.put("error", "1");
			info.put("errorDesc", "抱歉，您还有部分余额，请清空后再删除银行卡！");
			return info;
		}
		// 根据银行卡Id获取用户的银行卡信息
		BankCard bankCard = this.userDeleteCardService.getBankCardById(userId, cardId);
		if (bankCard == null || StringUtils.isEmpty(bankCard.getCardNo())) {
			System.out.println("获取用户银行卡信息失败");
			info.put("error", "1");
			info.put("errorDesc", "获取用户银行卡信息失败");
			return info;
		}
		// 银行卡号
		String cardNo = bankCard.getCardNo();

		Users users = userDeleteCardService.getUsers(userId);
		UsersInfo usersInfo = userDeleteCardService.getUsersInfoByUserId(userId);
		// 调用汇付接口(4.2.6 删除银行卡接口)
		BankCallBean retBean = null;
		BankCallBean bean = new BankCallBean();
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserId(userId + "");
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_CARD_UNBIND);
		bean.setTxDate(GetOrderIdUtils.getOrderDate());// 交易日期
		bean.setTxTime(GetOrderIdUtils.getOrderTime());// 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_WEI);// 交易渠道
		bean.setAccountId(accountChinapnrTender.getAccount());// 存管平台分配的账号
		bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);// 证件类型01身份证
		bean.setIdNo(usersInfo.getIdcard());// 证件号
		bean.setName(usersInfo.getTruename());// 姓名
		bean.setMobile(users.getMobile());// 手机号
		bean.setCardNo(cardNo);// 银行卡号
		LogAcqResBean logAcqResBean = new LogAcqResBean();
		logAcqResBean.setCardNo(cardNo);
		logAcqResBean.setCardId(Integer.parseInt(cardId));
		bean.setLogAcqResBean(logAcqResBean);
		// 调用汇付接口
		try {
			retBean = BankCallUtils.callApiBg(bean);
		} catch (Exception e) {
			e.printStackTrace();
			info.put("error", "1");
			info.put("errorDesc", "调用银行接口失败,请联系客服!");
			return info;
		}
		// 回调数据处理
		if (retBean == null || !(BankCallStatusConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode()))) {
			LogUtil.debugLog(THIS_CLASS, "RetCode:" + (retBean == null ? "" : retBean.getRetCode()) + "&&&&&&&&&&& RetMsg:" + (retBean == null ? "" : retBean.getRetMsg()));
			info.put("error", "1");
			info.put("errorDesc", "抱歉，银行卡删除错误，请联系客服！");
			return info;
		}
		// 执行删除卡后处理,判断银行卡状态，删除平台本地银行卡信息
		try {
			boolean isdelFlag = this.userDeleteCardService.updateAfterDeleteCard(bean, userId);
			// 删除失败
			if (!isdelFlag) {
				info.put("error", "1");
				info.put("errorDesc", "抱歉，银行卡删除错误，请联系客服！");
			} else {
				info.put("error", "0");
				info.put("errorDesc", "恭喜您！您的普通银行卡删除成功");
			}
		} catch (Exception e) {
			info.put("error", "1");
			info.put("errorDesc", "抱歉，银行卡删除错误，请联系客服！");
		}
		LogUtil.endLog(THIS_CLASS, WeChatDeleteCardDefine.REQUEST_INDEX);
		return info;
	}
}
