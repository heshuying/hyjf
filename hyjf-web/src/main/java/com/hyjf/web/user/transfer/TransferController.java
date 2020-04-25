package com.hyjf.web.user.transfer;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.chinapnr.MerPriv;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.ThreeDESUtils;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.UserTransfer;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.web.BaseController;
import com.hyjf.web.chinapnr.ChinapnrService;

/**
 * Description:用户转账控制器 Copyright: Copyright (HYJF Corporation)2015 Company: HYJF
 * Corporation
 * 
 * @author: wangkun
 * @version: 1.0 Created at: 2016年5月23日 上午9:32:36 
 * @Modification History: Modified
 *           by :
 */
@Controller
@RequestMapping(value = TransferDefine.REQUEST_MAPPING)
public class TransferController extends BaseController {

	@Autowired
	private TransferService transferService;
	@Autowired
	private ChinapnrService chinapnrService;

	private static String KEY = PropUtils.getSystem("hyjf.transfer.3des.key").trim();

	/**
	 * 用户转账
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(TransferDefine.INTI_TRANSFER_ACTION)
	public ModelAndView initTransfer(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(TransferDefine.THIS_CLASS, TransferDefine.INTI_TRANSFER_ACTION);
		ModelAndView modelAndView = new ModelAndView();
		// 用户转账订单号
		String orderId = request.getParameter("orderId").replaceAll(" ", "+");
		try {
			orderId = ThreeDESUtils.Decrypt3DES(KEY, orderId);
			System.out.println("用户转账 订单号：--------------------【" + orderId + "】");
			JSONObject ret = new JSONObject();
			UserTransfer userTransfer = transferService.checkTransferParam(orderId, ret);
			if (ret.get("error") != null && ret.get("error").equals("1")) {
				System.out.println(ret.toJSONString());
				modelAndView = new ModelAndView(TransferDefine.TRANSFER_ERROR_PATH);
				modelAndView.addObject("message", ret.getString("data"));
				return modelAndView;
			}
			// 获取转账用户
			int userId = userTransfer.getOutUserId();
			AccountChinapnr chinapnrTransfer = this.transferService.getAccountChinapnr(userId);
			if (chinapnrTransfer == null) {
				modelAndView = new ModelAndView(TransferDefine.TRANSFER_ERROR_PATH);
				modelAndView.addObject("message", "您未开户,请登陆后开户");
				return modelAndView;
			}
			// 转账用户汇付客户号
			long transferUsrcustid = chinapnrTransfer.getChinapnrUsrcustid();
			// 用户转账金额
			BigDecimal transferAmount = userTransfer.getTransferAmount();
			// 更新转账为进行中
			Boolean flag = transferService.updateUserTransfer(userTransfer);
			// 成功后调用出借接口
			if (flag) {
				// 回调路径
				String returl = PropUtils.getSystem("hyjf.web.host").trim() + TransferDefine.REQUEST_MAPPING
						+ TransferDefine.RETURL_SYN_ACTION + ".do";
				// 商户后台应答地址(必须)
				String bgRetUrl = PropUtils.getSystem("http.hyjf.web.host").trim() + TransferDefine.REQUEST_MAPPING
						+ TransferDefine.RETURL_ASY_ACTION + ".do";
				ChinapnrBean chinapnrBean = new ChinapnrBean();
				// 接口版本号
				chinapnrBean.setVersion(ChinaPnrConstant.VERSION_10);
				// 消息类型(主动投标)
				chinapnrBean.setCmdId(ChinaPnrConstant.CMDID_USR_ACCT_PAY);
				// 订单号(必须)
				chinapnrBean.setOrdId(orderId);
				// 交易金额(必须)
				chinapnrBean.setTransAmt(transferAmount.toString());
				// 用户客户号
				chinapnrBean.setUsrCustId(String.valueOf(transferUsrcustid));
				//入账账户
				chinapnrBean.setInAcctId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT15).trim());
				// 入账账户类型
				chinapnrBean.setInAcctType("MERDT");
				// 商户私有域
				MerPriv merPriv = new MerPriv();
				merPriv.setUserId(userId);
				chinapnrBean.setMerPrivPo(merPriv);
				// 页面返回
				chinapnrBean.setRetUrl(returl);
				// 商户后台应答地址(必须)
				chinapnrBean.setBgRetUrl(bgRetUrl);
				// log用户
				chinapnrBean.setLogUserId(Integer.valueOf(userId));
				// 日志类型
				chinapnrBean.setType("user_transfer");
				try {
					modelAndView = ChinapnrUtil.callApi(chinapnrBean);
					LogUtil.endLog(TransferDefine.THIS_CLASS, TransferDefine.INTI_TRANSFER_ACTION);
				} catch (Exception e) {
					e.printStackTrace();
					modelAndView = new ModelAndView(TransferDefine.TRANSFER_ERROR_PATH);
					modelAndView.addObject("message", "转账失败！");
				}
			} else {
				modelAndView = new ModelAndView(TransferDefine.TRANSFER_ERROR_PATH);
				modelAndView.addObject("message", "转账失败！");
			}

		} catch (Exception e1) {
			e1.printStackTrace();
			modelAndView = new ModelAndView(TransferDefine.TRANSFER_ERROR_PATH);
			modelAndView.addObject("message", "转账失败");
		}
		return modelAndView;
	}

	// pay模块同步回调此方法
	@RequestMapping(TransferDefine.RETURL_SYN_ACTION)
	public ModelAndView transferReturnSyn(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute ChinapnrBean bean) {

		System.out.println("用户转账------------------开始用户转账同步方法");
		ModelAndView modelAndView = new ModelAndView();
		bean.convert();
		// 错误消息
		String data = "";
		// 获取项目编号
		MerPriv merPriv = bean.getMerPrivPo();
		int userId = merPriv.getUserId();
		if (Validator.isNull(userId)) {
			modelAndView = new ModelAndView(TransferDefine.TRANSFER_ERROR_PATH);
			modelAndView.addObject("message", "回调时,borrowNid为空");
			return modelAndView;
		}
		// 借款金额
		String account = bean.getTransAmt();
		// 订单id
		String ordId = bean.getOrdId();
		// 校验此笔订单
		JSONObject ret = new JSONObject();
		UserTransfer userTransfer = this.transferService.checkTransferParam(ordId, ret);
		if (ret.get("error") != null && ret.get("error").equals("1")) {
			System.out.println(ret.toJSONString());
			if(StringUtils.isNotBlank(ret.getString("status"))&&ret.getString("status").equals("2")){
				modelAndView = new ModelAndView(TransferDefine.TRANSFER_SUCCESS_PATH);
				modelAndView.addObject("message", "转账成功！");
				return modelAndView;
			}else{
				modelAndView = new ModelAndView(TransferDefine.TRANSFER_ERROR_PATH);
				modelAndView.addObject("message", ret.getString("data"));
				return modelAndView;
			}
		}
		// excusive的uuid
		String uuid = request.getParameter("uuid");
		// 状态返回码
		String respCode = bean.getRespCode();
		boolean updateFlag = false;
		if (userTransfer.getStatus() == 1||userTransfer.getStatus()==0) {
			updateFlag = true;
		}
		// 用户转账
		System.out.println("PC用户:" + userId + "***********************************用户转账结果码：" + respCode);
		// 发送状态
		String status = ChinaPnrConstant.STATUS_VERTIFY_OK;
		// 未调用过异步请求
		if (updateFlag) {
			if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))
					|| ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
				boolean afterDealFlag = false;
				// 冻结成功，写各个表
				try {
					String ip =GetCilentIP.getIpAddr(request);
					afterDealFlag = this.transferService.updateAfterUserTansfer(userTransfer,ip);
					if (afterDealFlag) {
						data = "转账成功";
						status = ChinaPnrConstant.STATUS_SUCCESS;
						System.out.println(
								"PC用户:" + userId + "**********用户转账成功：" + "订单号：【" + ordId + "】" + "--金额：" + account);
					} else {
						data = "转账失败";
						status = ChinaPnrConstant.STATUS_FAIL;
						this.transferService.updateUserTransferFail(userTransfer,new Date());
						System.out.println(
								"PC用户:" + userId + "**********用户转账失败：" + "订单号：【" + ordId + "】" + "--金额：" + account);
					}
				} catch (Exception e) {
					data = "转账失败";
					status = ChinaPnrConstant.STATUS_FAIL;
					//this.transferService.updateUserTransferFail(userTransfer,new Date());
					e.printStackTrace();
				}
			} else {
				data = bean.getRespDesc();
				status = ChinaPnrConstant.STATUS_FAIL;
				this.transferService.updateUserTransferFail(userTransfer,new Date());
				System.out.println("PC用户:" + userId + "**********用户转账失败：" + "订单号：【" + ordId + "】" + "--金额：" + account
						+ "失败原因：" + data);
			}
		} else {
			UserTransfer userTransferNew = this.transferService.selectUserTransfer(userTransfer);
			if(userTransferNew.getStatus() == 2){
				data = "转账成功";
				status = ChinaPnrConstant.STATUS_SUCCESS;
				System.out.println(
						"PC用户:" + userId + "**********用户转账成功：" + "订单号：【" + ordId + "】" + "--金额：" + account);
			}else{
				data = "转账失败";
				status = ChinaPnrConstant.STATUS_FAIL;
				this.transferService.updateUserTransferFail(userTransfer,new Date());
				System.out.println("PC用户:" + userId + "**********用户转账失败：" + "订单号：【" + ordId + "】" + "--金额：" + account
						+ "调用汇付接口返回错误码:" + bean.getRespDesc());
			}
		}
		// 更新状态记录
		if (updateFlag && Validator.isNotNull(uuid)) {
			this.chinapnrService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
		}
		if (status.equals(ChinaPnrConstant.STATUS_FAIL)) {
			modelAndView = new ModelAndView(TransferDefine.TRANSFER_ERROR_PATH);
			modelAndView.addObject("message", data);
		} else {
			modelAndView = new ModelAndView(TransferDefine.TRANSFER_SUCCESS_PATH);
			modelAndView.addObject("message", data);
		}
		return modelAndView;
	}
	// pay模块异步回调此方法
	@RequestMapping(TransferDefine.RETURL_ASY_ACTION)
	public ModelAndView transferReturnAsy(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute ChinapnrBean bean) {

		System.out.println("用户转账------------------开始用户转账同步方法");
		ModelAndView modelAndView = new ModelAndView();
		bean.convert();
		// 错误消息
		String data = "";
		// 获取项目编号
		MerPriv merPriv = bean.getMerPrivPo();
		int userId = merPriv.getUserId();
		if (Validator.isNull(userId)) {
			modelAndView = new ModelAndView(TransferDefine.TRANSFER_ERROR_PATH);
			modelAndView.addObject("message", "回调时,borrowNid为空");
			return modelAndView;
		}
		// 借款金额
		String account = bean.getTransAmt();
		// 订单id
		String ordId = bean.getOrdId();
		// 校验此笔订单
		JSONObject ret = new JSONObject();
		UserTransfer userTransfer = this.transferService.checkTransferParam(ordId, ret);
		if (ret.get("error") != null && ret.get("error").equals("1")) {
			System.out.println(ret.toJSONString());
			if(StringUtils.isNotBlank(ret.getString("status"))&&ret.getString("status").equals("2")){
				modelAndView = new ModelAndView(TransferDefine.TRANSFER_SUCCESS_PATH);
				modelAndView.addObject("message", "转账成功！");
				return modelAndView;
			}else{
				modelAndView = new ModelAndView(TransferDefine.TRANSFER_ERROR_PATH);
				modelAndView.addObject("message", ret.getString("data"));
				return modelAndView;
			}
		}
		// excusive的uuid
		String uuid = request.getParameter("uuid");
		// 状态返回码
		String respCode = bean.getRespCode();
		boolean updateFlag = false;
		if (userTransfer.getStatus()==1||userTransfer.getStatus()==0) {
			updateFlag = true;
		}
		// 用户转账
		System.out.println("PC用户:" + userId + "***********************************用户转账结果码：" + respCode);
		// 发送状态
		String status = ChinaPnrConstant.STATUS_VERTIFY_OK;
		// 未调用过异步请求
		if (updateFlag) {
			if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))
					|| ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
				boolean afterDealFlag = false;
				// 冻结成功，写各个表
				try {
					String ip =GetCilentIP.getIpAddr(request);
					afterDealFlag = this.transferService.updateAfterUserTansfer(userTransfer,ip);
					if (afterDealFlag) {
						data = "转账成功";
						status = ChinaPnrConstant.STATUS_SUCCESS;
						System.out.println(
								"PC用户:" + userId + "**********用户转账成功：" + "订单号：【" + ordId + "】" + "--金额：" + account);
					} else {
						data = "转账失败";
						status = ChinaPnrConstant.STATUS_FAIL;
						this.transferService.updateUserTransferFail(userTransfer,new Date());
						System.out.println(
								"PC用户:" + userId + "**********用户转账失败：" + "订单号：【" + ordId + "】" + "--金额：" + account);
					}
				} catch (Exception e) {
					data = "转账失败";
					status = ChinaPnrConstant.STATUS_FAIL;
					//this.transferService.updateUserTransferFail(userTransfer,new Date());
					e.printStackTrace();
				}
			} else {
				data = bean.getRespDesc();
				status = ChinaPnrConstant.STATUS_FAIL;
				this.transferService.updateUserTransferFail(userTransfer,new Date());
				System.out.println("PC用户:" + userId + "**********用户转账失败：" + "订单号：【" + ordId + "】" + "--金额：" + account
						+ "失败原因：" + data);
			}
		} else {
			UserTransfer userTransferNew = this.transferService.selectUserTransfer(userTransfer);
			if(userTransferNew.getStatus() ==2){
				data = "转账成功";
				status = ChinaPnrConstant.STATUS_SUCCESS;
				System.out.println("PC用户:" + userId + "**********用户转账成功：" + "订单号：【" + ordId + "】" + "--金额：" + account);
			}else{
				data = "转账失败";
				status = ChinaPnrConstant.STATUS_FAIL;
				this.transferService.updateUserTransferFail(userTransfer,new Date());
				System.out.println("PC用户:" + userId + "**********用户转账失败：" + "订单号：【" + ordId + "】" + "--金额：" + account
						+ "调用汇付接口返回错误码:" + bean.getRespDesc());
			}
		}
		// 更新状态记录
		if (updateFlag && Validator.isNotNull(uuid)) {
			this.chinapnrService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
		}
		if (status.equals(ChinaPnrConstant.STATUS_FAIL)) {
			modelAndView = new ModelAndView(TransferDefine.TRANSFER_ERROR_PATH);
			modelAndView.addObject("message", data);
		} else {
			modelAndView = new ModelAndView(TransferDefine.TRANSFER_SUCCESS_PATH);
			modelAndView.addObject("message", data);
		}
		return modelAndView;
	}
}
