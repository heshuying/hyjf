package com.hyjf.web.bank.web.user.deletecardpage;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.deletecard.DeleteCardPageBean;
import com.hyjf.bank.service.user.deletecard.DeleteCardService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.web.BaseController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.deletecard.UserDeleteCardService;
import com.hyjf.web.util.WebUtils;
import org.apache.commons.lang3.StringUtils;
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
 * 江西银行解绑银行卡
 * @author liuyang
 *
 */
@Controller
@RequestMapping(value = DeleteCardPageDefine.REQUEST_MAPPING)
public class DeleteCardPageController extends BaseController {

	/** THIS_CLASS */
	private static final String THIS_CLASS = DeleteCardPageController.class.getName();

	@Autowired
	private DeleteCardService userDeleteCardService;
	@Autowired
	private UserDeleteCardService deleteCardService;

	@RequestMapping(DeleteCardPageDefine.DELETE_CARDPAGE)
	public ModelAndView checkParam(HttpServletRequest request, HttpServletResponse response){
		ModelAndView modelAndView = new ModelAndView(DeleteCardPageDefine.DELETE_ERROR_PATH);
		LogUtil.startLog(THIS_CLASS, DeleteCardPageDefine.REQUEST_MAPPING);
		logger.info("======================解卡调用页面开始=======================");
		String cardId = request.getParameter("cardId");
		JSONObject ret = new JSONObject();
		WebViewUser user = WebUtils.getUser(request);// 用户ID
		// 检查参数
		if (user == null) {
			modelAndView.addObject("status", "false");
			modelAndView.addObject("message", "用户未登陆");
			return modelAndView;
		}
		UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
		userOperationLogEntity.setOperationType(11);
		userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
		userOperationLogEntity.setPlatform(0);
		userOperationLogEntity.setRemark("");
		userOperationLogEntity.setOperationTime(new Date());
		userOperationLogEntity.setUserName(user.getUsername());
		userOperationLogEntity.setUserRole(user.getRoleId());
		deleteCardService.sendUserLogMQ(userOperationLogEntity);
		if(user.getUserType()==1){
			//企业用户提示联系客服
			modelAndView.addObject("status", "false");
			modelAndView.addObject("message", "企业用户解绑请联系客服");
			return modelAndView;
		}
		// 取得用户在汇付天下的客户号
		BankOpenAccount accountChinapnrTender = userDeleteCardService.getBankOpenAccount(user.getUserId());
		if (accountChinapnrTender == null || StringUtils.isEmpty(accountChinapnrTender.getAccount())) {
			modelAndView.addObject("status", "false");
			modelAndView.addObject("message", "用户未开户");
			return modelAndView;
		}
		// 用户余额大于零不让解绑
		Account account = userDeleteCardService.getAccount(user.getUserId());
		// 用户在银行的账户余额
//		BigDecimal bankBalance = userDeleteCardService.getBankBalance(user.getUserId(), accountChinapnrTender.getAccount());
		BankCallBean bankCallBean = deleteCardService.getBankBalanceCall(user.getUserId(), accountChinapnrTender.getAccount());
		BigDecimal bankBalance = BigDecimal.ZERO;
		if (bankCallBean != null && BankCallStatusConstant.RESPCODE_SUCCESS.equals(bankCallBean.getRetCode())) {
			bankBalance = new BigDecimal(bankCallBean.getAvailBal().replace(",", ""));
		}else{
			modelAndView.addObject("status", "false");
			modelAndView.addObject("message", "抱歉，银行卡解绑错误，请联系客服！");
			return modelAndView;
		}
		if ((Validator.isNotNull(account.getBankBalance()) && account.getBankBalance().compareTo(BigDecimal.ZERO) > 0)
				|| ((Validator.isNotNull(bankBalance) && bankBalance.compareTo(BigDecimal.ZERO) > 0))) {
			modelAndView.addObject("status", "false");
			modelAndView.addObject("message", "抱歉，请先清空当前余额和待收后，再申请解绑。");
			return modelAndView;
		}
		// 根据银行卡Id获取用户的银行卡信息
		BankCard bankCard = this.userDeleteCardService.getBankCardById(user.getUserId(), cardId);
		if (bankCard == null || StringUtils.isEmpty(bankCard.getCardNo())) {
			System.out.println("获取用户银行卡信息失败");
			modelAndView.addObject("status", "false");
			modelAndView.addObject("message", "获取用户银行卡信息失败");
			return modelAndView;
		}
		//
		UsersInfo usersInfo = userDeleteCardService.getUsersInfoByUserId(user.getUserId());
		try {
			// 同步调用路径
			String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)
					+ DeleteCardPageDefine.REQUEST_MAPPING + DeleteCardPageDefine.RETURL_SYN_ACTION + ".do?userId=" + user.getUserId();
			// 异步调用路
			String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)
					+ DeleteCardPageDefine.REQUEST_MAPPING + DeleteCardPageDefine.RETURL_ASY_ACTION +".do";
			// 拼装参数 调用江西银行
			String forgetPassworedUrl = CustomConstants.FORGET_PASSWORD_URL;
			DeleteCardPageBean bean = new DeleteCardPageBean();
			//
			bean.setUserId(user.getUserId());
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
			bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
			bean.setPlatform("0");
			modelAndView = userDeleteCardService.getCallbankMV(bean);
			logger.info("======================解卡调用页面结束=======================");
			LogUtil.endLog(THIS_CLASS, DeleteCardPageDefine.DELETE_CARDPAGE);
			return modelAndView;
		} catch (Exception e) {
			logger.error("调用银行接口失败", e);
			modelAndView.addObject("message", "调用银行接口异常");
			return modelAndView;
		}
	}

	/**
	 * 页面解卡同步回调
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(DeleteCardPageDefine.RETURL_SYN_ACTION)
	public ModelAndView unbindCardReturn(HttpServletRequest request, HttpServletResponse response,@ModelAttribute BankCallBean bean) {

		LogUtil.startLog(THIS_CLASS, DeleteCardPageDefine.RETURL_SYN_ACTION, "[解卡同步回调开始]");
		ModelAndView modelAndView = new ModelAndView(DeleteCardPageDefine.DELETE_ERROR_PATH);
		String isSuccess = request.getParameter("isSuccess");
		String userId = request.getParameter("userId");
		bean.convert();
		// 银行返回响应代码
		String retCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
		logger.info("解卡同步返回值,用户ID:[" + userId + "],retCode:[" + retCode + "]");
		// 解卡后处理
		try {
			if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)||"1".equals(isSuccess)) {
				// 成功
				modelAndView = new ModelAndView(DeleteCardPageDefine.UNBINDCARD_SUCCESS_PATH);
				LogUtil.endLog(THIS_CLASS, DeleteCardPageDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
				return modelAndView;
			} else {
				modelAndView.addObject("status", "false");
				modelAndView.addObject("message", "抱歉，银行卡解绑错误，请联系客服！");
				LogUtil.endLog(THIS_CLASS, DeleteCardPageDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
				return modelAndView;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelAndView;
	}
	@ResponseBody
	@RequestMapping(DeleteCardPageDefine.RETURL_ASY_ACTION)
	public BankCallResult bgreturn(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BankCallBean bean) {
		BankCallResult result = new BankCallResult();
		logger.info("页面解卡异步回调start");
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
