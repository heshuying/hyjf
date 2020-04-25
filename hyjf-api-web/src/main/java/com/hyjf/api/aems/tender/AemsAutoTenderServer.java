package com.hyjf.api.aems.tender;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.server.util.ErrorCodeConstant;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.user.auto.AutoService;
import com.hyjf.bank.service.user.tender.TenderService;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * aems自动投资接口，针对散标
 * @author jijun
 * @date 20180904
 */
@Controller
@RequestMapping(value = AemsAutoTenderDefine.REQUEST_MAPPING)
public class AemsAutoTenderServer extends BaseController {

	Logger logger = LoggerFactory.getLogger(AemsAutoTenderServer.class);

	@Autowired
	private TenderService tenderService;
	
	@Autowired
	private AutoService autoService;

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST,value = AemsAutoTenderDefine.AUTOTENDER_ACTION)
	public AemsAutoTenderResultBean autoTender(@RequestBody AemsAutoTenderRequestBean autoTenderRequestBean, HttpServletRequest request, HttpServletResponse response) {
		logger.info("投资输入参数.... autoTenderRequestBean is :{}", JSONObject.toJSONString(autoTenderRequestBean));
		AemsAutoTenderResultBean resultBean = new AemsAutoTenderResultBean();
		resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
		resultBean.setStatusDesc("请求参数非法");

		// ————> 验证请求参数
		if (Validator.isNull(autoTenderRequestBean) || Validator.isNull(autoTenderRequestBean.getInstCode())
				|| Validator.isNull(autoTenderRequestBean.getMoney())
				|| autoTenderRequestBean.getAccountId() == null
				|| Validator.isNull(autoTenderRequestBean.getBorrowNid())
				|| Validator.isNull(autoTenderRequestBean.getChkValue())
				|| Validator.isNull(autoTenderRequestBean.getTimestamp())) {
			logger.info("请求参数非法-------" + autoTenderRequestBean);
			return resultBean;
		}

		// ————> 验签
		if (!this.AEMSVerifyRequestSign(autoTenderRequestBean, AemsAutoTenderDefine.REQUEST_MAPPING+AemsAutoTenderDefine.AUTOTENDER_ACTION)) {
			resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000002);
			logger.info("验签失败！---------"+autoTenderRequestBean.getAccountId()+ " borrowNid: "+autoTenderRequestBean.getBorrowNid());
			resultBean.setStatusDesc("验签失败！");
			return resultBean;
		}

		logger.info("开始自动投资，请求参数 " + JSON.toJSONString(autoTenderRequestBean));
		String couponGrantId = "";
		String bizAccount = autoTenderRequestBean.getAccountId();
		// 借款borrowNid
		String borrowNid = autoTenderRequestBean.getBorrowNid();
		// 投资金额
		String accountStr = autoTenderRequestBean.getMoney();
		if (accountStr == null || "".equals(accountStr)) {
			accountStr = "0";
		}
		
		// ————> 投资校验
		JSONObject result = tenderService.checkAutoTenderParam(borrowNid, accountStr, bizAccount, "0", couponGrantId);
		if (result == null) {
			logger.info("投资校验失败---------"+autoTenderRequestBean.getAccountId()+ " borrowNid: "+autoTenderRequestBean.getBorrowNid());
			resultBean.setStatusForResponse("TZ000099");
			resultBean.setStatusDesc("投资失败,系统错误");
			return resultBean;
		} else if (result.get("error") != null && result.get("error").equals("1")) {
			logger.info("投资校验失败，原因: "+result.get("data").toString()+"  "+autoTenderRequestBean.getAccountId()+ " borrowNid: "+autoTenderRequestBean.getBorrowNid());
			resultBean.setStatusForResponse("TZ000096");
			resultBean.setStatusDesc("投资失败，原因: "+result.get("data").toString());
			return resultBean;
		}
		
		// 根据借款Id检索标的信息
		BorrowWithBLOBs borrow = this.tenderService.getBorrowByNid(borrowNid);
		if (borrow == null || borrow.getBorrowNid() == null) {
			resultBean.setStatusForResponse("TZ000001");
			resultBean.setStatusDesc("标的编号不存在！");
			return resultBean;
		}
		
		String account = accountStr;
		String tenderUsrcustid = result.getString("tenderUsrcustid");
		String tenderUserName = result.getString("tenderUserName");
		String autoOrderId = result.getString("tenderAutoOrderId");
		Integer userId = Integer.parseInt(result.getString("userId"));
		
		// 生成订单
		String orderId = GetOrderIdUtils.getOrderId2(userId);
		// ————> 写日志 调用投资接口
		Boolean flag = false;
		BankCallBean registResult = null;
		try {
			flag = tenderService.updateTenderLog(borrowNid, orderId, userId, account, GetCilentIP.getIpAddr(request), couponGrantId, tenderUserName);
			
			if (!flag) {
				resultBean.setStatusForResponse("TZ000099");
				resultBean.setStatusDesc("投资失败,系统错误");
				return resultBean;
			}
			logger.info("投资调用接口前---------"+autoTenderRequestBean.getAccountId()+ " borrowNid: "+autoTenderRequestBean.getBorrowNid()+" ordId: "+orderId);
			BankCallBean bean = new BankCallBean();
			// 获取共同参数
			String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
			String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
			// 生成订单
			bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
			bean.setTxCode(BankCallConstant.TXCODE_BIDAUTO_APPLY);// 交易代码
			bean.setInstCode(instCode);
			bean.setBankCode(bankCode);
			bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
			bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
			bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
			bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
			
			bean.setAccountId(tenderUsrcustid);// 电子账号
			bean.setOrderId(orderId);// 订单号
			bean.setTxAmount(CustomUtil.formatAmount(account.toString()));// 交易金额
			bean.setProductId(borrowNid);// 标的号
//			bean.setFrzFlag(BankCallConstant.DEBT_FRZFLAG_FREEZE);// 是否冻结金额
			bean.setFrzFlag(BankCallConstant.DEBT_FRZFLAG_UNFREEZE);// 是否冻结金额
			bean.setContOrderId(autoOrderId);
			
			bean.setLogOrderId(orderId);// 订单号
			bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单日期
			bean.setLogUserId(String.valueOf(userId));// 投资用户
			bean.setLogRemark("自动投标申请");
			bean.setLogClient(0);
			try {
				registResult = BankCallUtils.callApiBg(bean);
			} catch (Exception e) {
				logger.info("投资调用接口异常---------"+autoTenderRequestBean.getAccountId()+ " borrowNid: "+autoTenderRequestBean.getBorrowNid()+" ordId: "+orderId);
				removeTenderTmp(borrowNid, userId, orderId);
				
				e.printStackTrace();
				resultBean.setStatusForResponse("TZ000098");
				resultBean.setStatusDesc("调用投资银行接口异常");
				return resultBean;
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
			resultBean.setStatusForResponse("TZ000099");
			resultBean.setStatusDesc("投资失败,系统错误");
			return resultBean;
		}
		
		if(registResult == null || StringUtils.isBlank(registResult.getRetCode())){
			logger.info("投资调用接口返回为空---------"+autoTenderRequestBean.getAccountId()+ " borrowNid: "+autoTenderRequestBean.getBorrowNid()+" ordId: "+orderId);
//			removeTenderTmp(borrowNid, userId, orderId);
			
			resultBean.setStatusForResponse("TZ000098");
			resultBean.setStatusDesc("调用投资银行接口异常");
			return resultBean;
		}
		
		if (!BankCallConstant.RESPCODE_SUCCESS.equals(registResult.getRetCode())) {
			// 返回码提示余额不足，不结冻
			logger.info("用户:" + userId + " 投资接口调用失败，错误码：" + registResult.getRetCode());
			removeTenderTmp(borrowNid, userId, orderId);
			
			if (BankCallConstant.RETCODE_BIDAPPLY_YUE_FAIL.equals(registResult.getRetCode())) {
				resultBean.setStatusForResponse("TZ000097");
				resultBean.setStatusDesc("投资失败，可用余额不足！请联系客服.");
				return resultBean;
			} else {
				resultBean.setStatusForResponse("TZ000096");
				resultBean.setStatusDesc("投资失败，原因: "+registResult.getRetMsg());
				return resultBean;
			}
		}
		
		registResult.convert();
		String message ="";
		
		try {
			// 进行投资, tendertmp锁住
			JSONObject tenderResult = this.tenderService.userAutoTender(borrow, registResult);
			// 投资成功
			if (tenderResult.getString("status").equals("1")) {
				logger.info("用户:" + userId + "  投资成功：" + account);
				message = "恭喜您投资成功!";
			}
			// 投资失败 回滚redis
			else {
				logger.info("用户:" + userId + "   投资失败：" + account);
				message = "投资失败";
				// 投资失败,投资撤销
				try {
					boolean canlflag = tenderService.bidCancel(userId, borrowNid, orderId, account);
					boolean updateFlag = tenderService.deleteBorrowTenderTmp(userId, borrowNid, orderId);
				} catch (Exception e) {
					e.printStackTrace();
					resultBean.setStatusForResponse("TZ000099");
					resultBean.setStatusDesc("投资失败,系统错误");
					return resultBean;
				}
				
				if(tenderResult != null && tenderResult.getString("message") != null){
					message = "投资失败, 原因："+tenderResult.getString("message");
				}
				resultBean.setStatusForResponse("TZ000096");
				resultBean.setStatusDesc(message);
				return resultBean;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultBean.setStatusForResponse("TZ000099");
			resultBean.setStatusDesc("投资失败,系统错误");
			return resultBean;
		}

		logger.info(autoTenderRequestBean.getInstCode()+" userid: "+userId+" 结束投资");
		resultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
		resultBean.setStatusDesc(message);
		resultBean.setOrderId(orderId);

		return resultBean;
	}

	/**
	 * 投资失败,删除投资临时表
	 * @param borrowNid
	 * @param userId
	 * @param orderId
	 */
	private void removeTenderTmp(String borrowNid, Integer userId,
			String orderId) {
		// 投资失败,投资撤销
		try {
			boolean updateFlag = tenderService.deleteBorrowTenderTmp(userId, borrowNid, orderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
