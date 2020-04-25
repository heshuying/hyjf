/**
 * Description:出借控制器
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 *           Created at: 2015年12月4日 下午2:32:36
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.web.user.recharge;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.chinapnr.MerPriv;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AccountBank;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.BankRechargeLimitConfig;
import com.hyjf.mybatis.model.auto.FeeConfig;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.QpCardInfo;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.web.BaseController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.shiro.ShiroUtil;
import com.hyjf.web.util.WebUtils;

/**
 * @package com.hyjf.web.user.recharge
 * @author GOGTZ-T
 * @date 2015/12/04 14:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = UserRechargeDefine.REQUEST_MAPPING)
public class UserRechargeController extends BaseController {

	/** THIS_CLASS */
	private static final String THIS_CLASS = UserRechargeController.class.getName();

	@Autowired
	private UserRechargeService userRechargeService;

	/** 用户提现URL */
	private static String CALLURL = "/hyjf-web/recharge/netsave";

	/**
	 * 
	 * 用户输入金额之后，计算并获取手续费用和到账金额
	 * 
	 * @author renxingchen
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = UserRechargeDefine.RECHARGEINFO_MAPPING)
	public RechargeInfoResult getRechargeInfo(HttpServletRequest request, RechargeInfoVo rechargeInfoVo) {
		RechargeInfoResult rechargeInfoResult = new RechargeInfoResult();
		String money = rechargeInfoVo.getMoney();
		if (money == null || money.equals("")) {
			money = "0";
		}
		String rechargeType = rechargeInfoVo.getRechargeType();
		String bankCode = rechargeInfoVo.getBankCode();
		BigDecimal fee = BigDecimal.ZERO;
		BigDecimal balance = BigDecimal.ZERO;
		// 校验数据
		if (money.matches("-?[0-9]+.*[0-9]*") && Validator.isNotNull(rechargeType)) {
			WebViewUser viewUser = WebUtils.getUser(request);
			Integer userId = viewUser.getUserId();
			Integer borrowerType = viewUser.getBorrowerType();
			int roleId = Integer.parseInt(viewUser.getRoleId());
			if ((2 == roleId) && (null != borrowerType && borrowerType > 1)) {// 如果是借款人用户，并且是外部机构用户，需要计算手续费
				// 获取银行卡快捷支付费率
				FeeConfig feeConfig;
				// 如果是网关充值
				if (rechargeType.equals("0")) {// 网关充值
					if (null != bankCode) {
						// 查询银行费率
						feeConfig = this.userRechargeService.getCardCharges(bankCode);
						if (null != feeConfig) {
							// 计算快捷充值手续费
							fee = new BigDecimal(money).multiply(new BigDecimal(feeConfig.getPersonalCredit()));
							balance = new BigDecimal(money).subtract(new BigDecimal(CustomConstants.DF_FOR_VIEW_V1.format(fee)));
						} else {
							fee = BigDecimal.ZERO;
							balance = new BigDecimal(money);
						}
						rechargeInfoResult.success();
					} else {
						rechargeInfoResult.setMessage("数据异常，请稍后再试！");
					}
				} else if (rechargeType.equals("1")) {// 企业网银充值
					if (null != bankCode) {
						// 查询银行费率
						feeConfig = this.userRechargeService.getCardCharges(bankCode);
						if (null != feeConfig) {
							// 计算快捷充值手续费
							fee = new BigDecimal(money).multiply(new BigDecimal(feeConfig.getEnterpriseCredit()));
							balance = new BigDecimal(money).subtract(new BigDecimal(CustomConstants.DF_FOR_VIEW_V1.format(fee)));
						} else {
							fee = BigDecimal.ZERO;
							balance = new BigDecimal(money);
						}
						rechargeInfoResult.success();
					} else {
						rechargeInfoResult.setMessage("数据异常，请稍后再试！");
					}
				} else if (rechargeType.equals("2")) {// 快捷充值
					// 查询用户的快捷卡
					QpCardInfo qpCardInfo = this.userRechargeService.getUserQpCard(userId);
					if (null != qpCardInfo) {
						// 查询该快捷卡的银行费率
						feeConfig = this.userRechargeService.getCardCharges(qpCardInfo.getCode());
						if (null != feeConfig) {
							// 计算快捷充值手续费
							fee = new BigDecimal(money).multiply(new BigDecimal(feeConfig.getQuickPayment()));
							balance = new BigDecimal(money).subtract(new BigDecimal(CustomConstants.DF_FOR_VIEW_V1.format(fee)));
						} else {
							fee = BigDecimal.ZERO;
							balance = new BigDecimal(money);
						}
					} else {
						fee = BigDecimal.ZERO;
						balance = new BigDecimal(money);
					}
					rechargeInfoResult.success();
				}
			} else {// 如果是向商户收取手续费
				fee = BigDecimal.ZERO;
				balance = new BigDecimal(money);
				rechargeInfoResult.success();
			}
			rechargeInfoResult.setFee(CustomConstants.DF_FOR_VIEW.format(fee));
			rechargeInfoResult.setBalance(CustomConstants.DF_FOR_VIEW.format(balance));
		} else {
			rechargeInfoResult.setMessage("数据异常，请稍后再试！");
		}
		return rechargeInfoResult;
	}

	/**
	 * 
	 * 跳转充值页面
	 * 
	 * @author renxingchen
	 * @return
	 */
	@RequestMapping(value = UserRechargeDefine.RECHARGEPAGE_MAPPING)
	public ModelAndView rechargePage(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView(UserRechargeDefine.JSP_RECHARGE_PAGE);
		// 查询页面上可以挂载的个人银行列表
		List<BankConfig> personBankConfigs = userRechargeService.getPersonBankInfos();
		modelAndView.addObject("personBankConfigs", personBankConfigs);
		// 查询页面上可以挂载的企业银行列表
		List<BankConfig> corpBankConfigs = userRechargeService.getCorpBankInfos();
		modelAndView.addObject("corpBankConfigs", corpBankConfigs);
		// 快捷充值卡列表
		List<BankConfig> quickBankList = userRechargeService.getBankQuickList();
		modelAndView.addObject("quickBankList", quickBankList);
		// 充值卡限额列表
		List<BankRechargeLimitConfig> bankRechargeLimitList = userRechargeService.getRechargeLimitList();
		modelAndView.addObject("bankRechargeLimitList", bankRechargeLimitList);
		Integer userId = WebUtils.getUserId(request);
		Users user = this.userRechargeService.getUsers(userId);
		modelAndView.addObject("userType", user.getUserType());
		return modelAndView;
	}

	/**
	 * 获取充值手续费
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	// ,method = RequestMethod.GET
	@ResponseBody
	@RequestMapping(value = UserRechargeDefine.GET_FEE)
	public String getFee(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(UserRechargeController.class.toString(), UserRechargeDefine.GET_FEE);

		Integer userId = ShiroUtil.getLoginUserId(request); // 用户ID
		String transAmt = request.getParameter("tranAmt");// 交易金额
		String bankCode = request.getParameter("bankCode");// 开户银行代号
		String rechargeType = request.getParameter("rechargeType");// 充值类型
		// 检查参数
		JSONObject checkResult = checkParam(request, userId, transAmt, bankCode, "");
		if (checkResult != null) {
			return checkResult.toString();
		}
		checkResult = new JSONObject();
		// 支付网关业务代号(充值类型)
		String gateBusiId = "";
		switch (rechargeType) {// 充值类型
		case UserRechargeDefine.RECHARGETYPE_0:
			gateBusiId = "B2C";// 个人网银充值
			if (StringUtils.isEmpty(bankCode)) {
				checkResult = jsonMessage("提现银行不可为空", "1");
				return checkResult.toJSONString();
			}
			break;
		case UserRechargeDefine.RECHARGETYPE_1:
			gateBusiId = "B2B";// 企业网银充值
			if (StringUtils.isEmpty(bankCode)) {
				checkResult = jsonMessage("提现银行不可为空", "1");
				return checkResult.toJSONString();
			}
			break;
		case UserRechargeDefine.RECHARGETYPE_2:
			gateBusiId = "QP";// 快捷充值
			if (gateBusiId.equals("QP")) {
				AccountBank accountBank = userRechargeService.getBankInfo(userId, null);
				if (accountBank == null || StringUtils.isEmpty(accountBank.getBank())) {
					checkResult = jsonMessage("用户不存在快捷卡", "1");
					return checkResult.toJSONString();
				} else {
					bankCode = accountBank.getBank();
				}
			}
			break;
		default:
			checkResult = jsonMessage("无效的充值类型", "1");
			return checkResult.toJSONString();
		}
		checkResult.put("bankFee", userRechargeService.getFee(bankCode, new BigDecimal(transAmt), gateBusiId));
		LogUtil.endLog(UserRechargeController.class.toString(), UserRechargeDefine.GET_FEE);
		return checkResult.toString();
	}

	/**
	 * 检查参数
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(UserRechargeDefine.CHECK_MAPPING)
	public String check(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(UserRechargeController.class.toString(), UserRechargeDefine.CHECK_MAPPING);

		Integer userId = ShiroUtil.getLoginUserId(request); // 用户ID
		String transAmt = request.getParameter("tranAmt");// 交易金额
		String bankCode = request.getParameter("bankCode");// 开户银行代号
		// 检查参数
		JSONObject checkResult = checkParam(request, userId, transAmt, bankCode, "");
		if (checkResult != null) {
			return checkResult.toString();
		}

		checkResult = new JSONObject();
		checkResult.put("url", CALLURL);
		LogUtil.endLog(UserRechargeController.class.toString(), UserRechargeDefine.CHECK_MAPPING);
		return checkResult.toString();
	}

	/**
	 * 
	 * 用户充值
	 * 
	 * @author renxingchen
	 * @param request
	 * @param rechargeVo
	 * @return
	 */
	@RequestMapping(value = UserRechargeDefine.RECHARGE_MAPPING)
	public ModelAndView recharge(HttpServletRequest request, RechargeVo rechargeVo) {
		LogUtil.startLog(UserRechargeController.class.toString(), UserRechargeDefine.RECHARGE_MAPPING);
		ModelAndView modelAndView = new ModelAndView(UserRechargeDefine.JSP_CHINAPNR_SEND);
		String gateBusiId = "";
		Integer userId;
		String username;
		String bankCode = "";
		// 数据校验
		if (!rechargeVo.getMoney().matches("-?[0-9]+.*[0-9]*") || Validator.isNull(rechargeVo.getRechargeType())) {
			modelAndView.setViewName(UserRechargeDefine.RECHARGE_ERROR);
			modelAndView.addObject("message", "系统参数异常!");
		} else {
			ChinapnrBean bean = new ChinapnrBean();
			// 获取用户编号\
			WebViewUser user = WebUtils.getUser(request);
			userId = user.getUserId();
			username = user.getUsername();
			UsersInfo usersInfo = userRechargeService.getUsersInfoByUserId(userId);
			int roleId = usersInfo.getRoleId();
			String feeFrom;
			JSONObject jobj = new JSONObject();
			if (2 == roleId) {// 如果是借款用户需要判断用户是来自内部机构还是外部机构
				if (null != usersInfo.getBorrowerType() && usersInfo.getBorrowerType() > 1) {// 如果是外部机构
					feeFrom = "U";
					jobj.put("FeeObjFlag", feeFrom);
				} else {// 如果是内部机构 需要传递出账子账户
					feeFrom = "M";
					jobj.put("FeeObjFlag", feeFrom);
					jobj.put("FeeAcctId", PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT07));
				}
			} else {// 如果是出借用户需则手续费从商户收取 需要传递出账子账户
				feeFrom = "M";
				jobj.put("FeeObjFlag", feeFrom);
				jobj.put("FeeAcctId", PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT07));
			}
			if (null == userId) {// 如果用户没有登录，重定向到登录页面
				modelAndView.setViewName("redirect:" + CustomConstants.HOST + "/user/login/init.do");
				return modelAndView;
			}
			switch (rechargeVo.getRechargeType()) {
			case UserRechargeDefine.RECHARGETYPE_0:
				gateBusiId = "B2C";
				if (StringUtils.isEmpty(rechargeVo.getBankCode())) {// 如果银行编号为空则提示异常
					// TODO
				} else {
					bankCode = rechargeVo.getBankCode();
				}
				break;
			case UserRechargeDefine.RECHARGETYPE_1:
				gateBusiId = "B2B";
				if (StringUtils.isEmpty(rechargeVo.getBankCode())) {// 如果银行编号为空则提示异常
					// TODO
				} else {
					bankCode = rechargeVo.getBankCode();
				}
				break;
			case UserRechargeDefine.RECHARGETYPE_2:
				gateBusiId = "QP";
				// 查询用户快捷卡信息
				AccountBank accountBank = userRechargeService.getBankInfo(userId, null);
				if (accountBank == null || StringUtils.isEmpty(accountBank.getBank())) {
				} else {
					bankCode = accountBank.getBank();
					bean.setOpenAcctId(accountBank.getAccount());// 设置快捷支付银行卡号
				}
				break;
			default:
				break;
			}
			bean.setGateBusiId(gateBusiId);
			// 获取用户在汇付天下的客户号
			AccountChinapnr accountChinapnrTender = this.userRechargeService.getAccountChinapnr(userId);
			if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getChinapnrUsrcustid())) {
				modelAndView = new ModelAndView("redirect:" + CustomConstants.HOST + "/user/openaccount/init.do");
				LogUtil.endLog(THIS_CLASS, UserRechargeDefine.NETSAVE_MAPPING, "[用户未开户]");
				return modelAndView;
			}
			Long chinapnrUsrcustidTender = accountChinapnrTender.getChinapnrUsrcustid();// 用户在汇付天下的客户号
			String certId = this.userRechargeService.getUserIdcard(userId); // 身份证号
			// 调用出借接口
			// 调用汇付接口(4.3.11. 自动扣款转账（商户用）)
			String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + UserRechargeDefine.REQUEST_MAPPING + UserRechargeDefine.RETURN_MAPPING + CustomConstants.SUFFIX_DO;
			String bgRetUrl = PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) + UserRechargeDefine.REQUEST_MAPPING + UserRechargeDefine.CALLBACK_MAPPING + CustomConstants.SUFFIX_DO;// 支付工程路径
			bean.setVersion(ChinaPnrConstant.VERSION_10);// 接口版本号
			bean.setCmdId(ChinaPnrConstant.CMDID_NET_SAVE); // 消息类型(充值)
			bean.setUsrCustId(String.valueOf(chinapnrUsrcustidTender));// 用户客户号
			bean.setOrdId(GetOrderIdUtils.getOrderId2(Integer.valueOf(userId))); // 订单号(必须)
			bean.setOrdDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
			bean.setGateBusiId(gateBusiId);// 支付网关业务代号(充值类型)
			bean.setOpenBankId(bankCode);// 开户银行代号
			bean.setTransAmt(CustomUtil.formatAmount(rechargeVo.getMoney()));// 交易金额(必须)
			bean.setRetUrl(retUrl); // 页面返回 URL
			bean.setBgRetUrl(bgRetUrl); // 商户后台应答地址(必须)
			bean.setCertId(certId); // 身份证号
			bean.setDcFlag("D");
			bean.setReqExt(jobj.toJSONString());
			MerPriv merPriv = new MerPriv();
			merPriv.setUserId(userId);
			merPriv.setFeeFrom(feeFrom);
			bean.setMerPrivPo(merPriv);// 商户私有域
			bean.setType("user_recharge"); // 日志类型(写日志用)

			// 插值用参数
			Map<String, String> params = new HashMap<String, String>();
			params.put("userId", String.valueOf(userId));
			params.put("ip", GetCilentIP.getIpAddr(request));
			params.put("username", username);
			params.put("feeFrom", feeFrom);

			// 插入充值记录
			int ret = this.userRechargeService.insertRechargeInfo(bean, params);
			if (ret > 0) {
				// 跳转到汇付天下画面
				try {
					modelAndView = ChinapnrUtil.callApi(bean);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {

			}
		}
		return modelAndView;
	}

	/**
	 * 用户充值后处理
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(UserRechargeDefine.RETURN_MAPPING)
	public ModelAndView rechargeReturn(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) {
		System.err.println("-----------------------------------我是充值分割线------------------------------");
		ModelAndView modelAndView = new ModelAndView();
		if (ChinaPnrConstant.STATUS_VERTIFY_OK.equals(bean.getChkValueStatus())) {
			LogUtil.startLog(THIS_CLASS, UserRechargeDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
			bean.convert();
			LogUtil.debugLog(THIS_CLASS, UserRechargeDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
			String ip = CustomUtil.getIpAddr(request);
			// 更新充值的相关信息
			modelAndView = this.userRechargeService.handleRechargeInfo(ip, bean.getMerPrivPo().getFeeFrom() + "", bean.getMerPrivPo().getUserId(), bean, modelAndView);
		} else {
			modelAndView.setViewName(UserRechargeDefine.RECHARGE_ERROR);
			modelAndView.addObject("message", "验签失败！交易数据可能被篡改，请确认您的银行卡信息后重试。");
		}
		return modelAndView;
	}

	/**
	 * 用户充值后处理
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(UserRechargeDefine.CALLBACK_MAPPING)
	public ModelAndView rechargeCallback(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) {
		ModelAndView modelAndView = new ModelAndView();
		if (ChinaPnrConstant.STATUS_VERTIFY_OK.equals(bean.getChkValueStatus())) {
			LogUtil.startLog(THIS_CLASS, UserRechargeDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
			bean.convert();
			LogUtil.debugLog(THIS_CLASS, UserRechargeDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
			String ip = CustomUtil.getIpAddr(request);
			// 更新充值的相关信息
			modelAndView = this.userRechargeService.handleRechargeInfo(ip, bean.getMerPrivPo().getFeeFrom() + "", bean.getMerPrivPo().getUserId(), bean, modelAndView);
		} else {
			modelAndView.setViewName(UserRechargeDefine.RECHARGE_ERROR);
			modelAndView.addObject("message", "验签失败！交易数据可能被篡改，请确认您的银行卡信息后重试。");
		}
		return modelAndView;
	}

	/**
	 * 检查参数的正确性
	 *
	 * @param transAmt
	 * @param openBankId
	 * @param rechargeType
	 * @return
	 */
	private JSONObject checkParam(HttpServletRequest request, Integer userId, String transAmtStr, String bankCode, String rechargeType) {
		// 检查用户是否登录
		if (Validator.isNull(ShiroUtil.getLoginUserId(request))) {
			return jsonMessage("您没有登录，请登录后再进行出借。", "1");
		}
		// 判断用户是否被禁用
		Users users = this.userRechargeService.getUsers(userId);
		if (users == null || users.getStatus() == 1) {
			return jsonMessage("对不起,该用户已经被禁用。", "1");
		}
		// 检查参数(交易金额是否数字)
		if (Validator.isNull(transAmtStr) || !NumberUtils.isNumber(transAmtStr)) {
			return jsonMessage("请输入充值金额。", "1");
		}
		// 检查参数(交易金额是否大于0)
		BigDecimal transAmt = new BigDecimal(transAmtStr);
		if (transAmt.compareTo(BigDecimal.ZERO) < 0) {
			return jsonMessage("充值金额不能为负数。", "1");
		}
		if (transAmt.compareTo(new BigDecimal(99999999.99)) > 0) {
			return jsonMessage("充值金额不能大于99,999,999.99元。", "1");
		}
		// 取得用户在汇付天下的客户号
		AccountChinapnr accountChinapnrTender = userRechargeService.getAccountChinapnr(userId);
		if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getChinapnrUsrcustid())) {
			return jsonMessage("用户未开户，请开户后再充值。", "1");
		}
		if (StringUtils.isNotEmpty(bankCode) && StringUtils.isNotEmpty(rechargeType) && rechargeType.equals(UserRechargeDefine.RECHARGETYPE_2)) {
			AccountBank accountBank = userRechargeService.getBankInfo(userId, bankCode);
			if (accountBank == null) {
				return jsonMessage("用户不存在该行快捷卡", "1");
			}
		}
		return null;
	}
}
