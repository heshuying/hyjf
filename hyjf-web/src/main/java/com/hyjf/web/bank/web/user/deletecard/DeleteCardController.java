package com.hyjf.web.bank.web.user.deletecard;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.login.LoginService;
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
import com.hyjf.web.util.WebUtils;

/**
 * 江西银行解绑银行卡
 * @author liuyang
 *
 */
@Controller
@RequestMapping(value = DeleteCardDefine.REQUEST_MAPPING)
public class DeleteCardController extends BaseController {

	/** THIS_CLASS */
	private static final String THIS_CLASS = DeleteCardController.class.getName();

	@Autowired
	private DeleteCardService userDeleteCardService;
	@Autowired
	LoginService loginService;

	/**
	 * 用户删除银行卡
	 *
	 * @param request
	 * @param ret
	 * @return
	 */
	@ResponseBody
	@RequestMapping(DeleteCardDefine.REQUEST_INDEX)
	public JSONObject deleteCard(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, DeleteCardDefine.REQUEST_MAPPING);
		String cardId = request.getParameter("cardId");
		JSONObject ret = new JSONObject();
		Integer userId = WebUtils.getUserId(request);
		// 检查参数
		if(userId == null || userId == 0){
			ret.put("status", "false");
			return ret;
		}
		WebViewUser user = WebUtils.getUser(request);
		UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
		userOperationLogEntity.setOperationType(11);
		userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
		userOperationLogEntity.setPlatform(0);
		userOperationLogEntity.setRemark("");
		userOperationLogEntity.setOperationTime(new Date());
		userOperationLogEntity.setUserName(user.getUsername());
		userOperationLogEntity.setUserRole(user.getRoleId());
		loginService.sendUserLogMQ(userOperationLogEntity);
		// 取得用户在汇付天下的客户号
		BankOpenAccount accountChinapnrTender = userDeleteCardService.getBankOpenAccount(userId);
		if (accountChinapnrTender == null || StringUtils.isEmpty(accountChinapnrTender.getAccount())) {
			ret.put("status", "false");
			return ret;
		}
		// 用户余额大于零不让解绑
		Account account = userDeleteCardService.getAccount(userId);
		// 用户在银行的账户余额
		BigDecimal bankBalance = userDeleteCardService.getBankBalance(userId, accountChinapnrTender.getAccount());
		if ((Validator.isNotNull(account.getBankBalance()) && account.getBankBalance().compareTo(BigDecimal.ZERO) > 0)
				|| ((Validator.isNotNull(bankBalance) && bankBalance.compareTo(BigDecimal.ZERO) > 0))) {
			ret.put("status", "money");
			return ret;
		}
		// 根据银行卡Id获取用户的银行卡信息
		BankCard bankCard = this.userDeleteCardService.getBankCardById(userId, cardId);
		if (bankCard == null || StringUtils.isEmpty(bankCard.getCardNo())) {
			System.out.println("获取用户银行卡信息失败");
			ret.put("status", "false");
			return ret;
		}
		String cardNo = bankCard.getCardNo();
		Users users = userDeleteCardService.getUsers(userId);
		UsersInfo usersInfo = userDeleteCardService.getUsersInfoByUserId(userId);
		// 调用汇付接口(4.2.6 删除银行卡接口)
		BankCallBean retBean = null;
		BankCallBean bean = new BankCallBean();
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogRemark("解绑银行卡");
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_CARD_UNBIND);
		bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
		bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
		bean.setAccountId(accountChinapnrTender.getAccount());// 存管平台分配的账号
		bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);// 证件类型01身份证
		bean.setIdNo(usersInfo.getIdcard());// 证件号
		bean.setName(usersInfo.getTruename());// 姓名
		bean.setMobile(users.getMobile());// 手机号
		bean.setCardNo(cardNo);// 银行卡号
		LogAcqResBean logAcqResBean = new LogAcqResBean();
		logAcqResBean.setCardNo(cardNo);// 银行卡号
		logAcqResBean.setCardId(Integer.parseInt(cardId)); // 银行卡Id
		bean.setLogAcqResBean(logAcqResBean);
		// 调用汇付接口
		try {
			retBean = BankCallUtils.callApiBg(bean);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("调用银行接口失败~!");
			ret.put("status", "false");
			return ret;
		}
		// 回调数据处理
		if (retBean == null || !(BankCallStatusConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode()))) {
			LogUtil.debugLog(THIS_CLASS, "RetCode:" + (retBean == null ? "" : retBean.getRetCode()) + "&&&&&&&&&&& RetMsg:" + (retBean == null ? "" : retBean.getRetMsg()));
			ret.put("status", "false");
			return ret;
		}
		// 执行删除卡后处理,判断银行卡状态，删除平台本地银行卡信息
		try {
			boolean isUpdateFlag = this.userDeleteCardService.updateAfterDeleteCard(bean, userId);
			if (!isUpdateFlag) {
				ret.put("status", "false");
			} else {
				ret.put("status", "true");
			}
		} catch (Exception e) {
			ret.put("status", "false");
		}
		LogUtil.endLog(THIS_CLASS, DeleteCardDefine.REQUEST_MAPPING);
		return ret;
	}
}
