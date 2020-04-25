package com.hyjf.web.bank.wechat.user.bindcard;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.bindcard.BindCardService;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.BankCardUtil;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BanksConfig;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.bank.web.user.bindcard.BindCardDefine;

/**
 * 用户绑卡
 * 
 * @author liuyang
 *
 */
@Controller
@RequestMapping(value = WeChatBindCardDefine.REQUEST_MAPPING)
public class WeChatBindCardController extends BaseController {

	/** THIS_CLASS */
	private static final String THIS_CLASS = WeChatBindCardController.class.getName();

	@Autowired
	private BindCardService userBindCardService;

	/** 发布地址 */
	private static String HOST_URL = PropUtils.getSystem("hyjf.web.host");

	/**
	 * 获取绑卡数据
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = WeChatBindCardDefine.INIT_BINDCARD_DATA_ACTION)
	public JSONObject getBindCardData(HttpServletRequest request) {
		LogUtil.startLog(THIS_CLASS, WeChatBindCardDefine.INIT_BINDCARD_DATA_ACTION);
		JSONObject info = new JSONObject();
		// 用户Id
		String userIdStr = request.getParameter("userId");
		if (StringUtils.isEmpty(userIdStr)) {
			info.put("error", "1");
			info.put("errorDesc", "获取信息失败");
			return info;
		}
		
		Integer userId = Integer.parseInt(userIdStr);
		
		try {
			// 查询用户信息获取真实姓名和银行卡和手机号码
			Users users = userBindCardService.getUsersTrue(userId);
			UsersInfo usersInfo = userBindCardService.getUsersInfoByUserId(userId);

			if (users != null) {
				info.put("mobile", users.getMobile());
			} else {
				info.put("error", "1");
				info.put("errorDesc", "不存在该用户");
			}
			if (usersInfo != null) {
				//用户姓名 页面不需要真实姓名
				//info.put("userName", usersInfo.getTruename());
				//身份证号 页面不需要真实身份证号
				//info.put("idCard", usersInfo.getIdcard());
				//用户姓名 脱敏
				if(StringUtils.isNotBlank(usersInfo.getTruename())) {
					String userNameEncry = usersInfo.getTruename().replaceFirst(usersInfo.getTruename().substring(0,1),"*");
					info.put("userNameEncry",userNameEncry);					
				}else {
					info.put("error", "1");
					info.put("errorDesc", "该用户未开户");
				}
				//身份证号 脱敏
				if(StringUtils.isNotBlank(usersInfo.getIdcard())) {
					String idCardEncry = usersInfo.getIdcard().replace(usersInfo.getIdcard().substring(3, usersInfo.getIdcard().length() - 4), "***********");
					info.put("idCardEncry", idCardEncry);
				}else {
					info.put("error", "1");
					info.put("errorDesc", "该用户未开户");
				}
				
			} else {
				info.put("error", "1");
				info.put("errorDesc", "不存在该用户");
			}

		} catch (Exception e) {
			info.put("error", "1");
			info.put("errorDesc", "获取绑卡信息失败");
		}
		info.put("error", "0");
		info.put("errorDesc", "获取绑卡信息成功!");
		LogUtil.endLog(THIS_CLASS, WeChatBindCardDefine.INIT_BINDCARD_DATA_ACTION);
		return info;
	}

	/**
	 * 根据用户Id获取用户银行卡列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(WeChatBindCardDefine.GET_BANK_CARD_MAPPING)
	public JSONObject getBankCardByUserId(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, WeChatBindCardDefine.GET_BANK_CARD_MAPPING);
		JSONObject info = new JSONObject();
		List<WeChatBindCardResultBean> cardResultList = new ArrayList<WeChatBindCardResultBean>();

		// 用户Id
		String userIdStr = request.getParameter("userId");
		if (StringUtils.isEmpty(userIdStr)) {
			info.put("error", "1");
			info.put("errorDesc", "您未登陆，请先登录");
			return info;
		}
		Integer userId = Integer.parseInt(userIdStr);
		// 取得用户在汇付天下的客户号
		BankOpenAccount accountChinapnrTender = userBindCardService.getBankOpenAccount(userId);
		if (accountChinapnrTender == null || StringUtils.isEmpty(accountChinapnrTender.getAccount())) {
			info.put("error", "1");
			info.put("errorDesc", "用户未开户");
			return info;
		}

		// 根据用户Id查询用户的银行卡信息
		List<BankCard> bankList = userBindCardService.getAccountBankByUserId(String.valueOf(userId));
		if (bankList == null || bankList.size() == 0) {
			info.put("error", "1");
			info.put("errorDesc", "未查询到用户的银行卡信息");
			return info;
		}

		for (BankCard bankCard : bankList) {
			WeChatBindCardResultBean cardResultBean = new WeChatBindCardResultBean();
			BeanUtils.copyProperties(bankCard, cardResultBean);

			// 银行logo
			Integer bankId = bankCard.getBankId();
			BanksConfig banksConfig = userBindCardService.getBanksConfigByBankId(bankId);
			if (banksConfig != null && StringUtils.isNotEmpty(banksConfig.getBankIcon())) {
				cardResultBean.setLogo(HOST_URL + banksConfig.getBankIcon());
			} else {
				cardResultBean.setLogo(HOST_URL + "/data/upfiles/filetemp/image/bank_log.png");// 应前台要求，logo路径给绝对路径
			}

			// 是否快捷卡
			if (banksConfig != null && banksConfig.getQuickPayment() == 1) {
				cardResultBean.setIsDefault("2");
			} else {
				cardResultBean.setIsDefault("0");
			}

			if (banksConfig != null && StringUtils.isNotEmpty(banksConfig.getBankName())) {
				cardResultBean.setBank(banksConfig.getBankName());
			}
			cardResultBean.setCardNoInfo(BankCardUtil.getCardNo(cardResultBean.getCardNo()));
			cardResultList.add(cardResultBean);
		}

		info.put("error", "0");
		info.put("errorDesc", "获取用户银行卡信息成功!");
		info.put("data", JSONObject.toJSON(cardResultList));
		LogUtil.endLog(THIS_CLASS, WeChatBindCardDefine.GET_BANK_CARD_MAPPING);
		return info;
	}

	/**
	 * 用户绑卡增强发送验证码接口
	 */
	@ResponseBody
	@RequestMapping(value = WeChatBindCardDefine.SEND_PLUS_CODE_ACTION, produces = "application/json; charset=utf-8")
	public JSONObject sendPlusCode(HttpServletRequest request, HttpServletResponse response, String cardNo) {
		LogUtil.startLog(THIS_CLASS, BindCardDefine.SEND_PLUS_CODE_ACTION);
		JSONObject info = new JSONObject();
		Integer userId = Integer.parseInt(request.getParameter("userId"));
		Users user = userBindCardService.getUsersByUserId(userId);
		if (user == null) {
			info.put("error", "1");
			info.put("errorDesc", "用户未登陆");
			return info;
		}
		String mobile = request.getParameter("mobile"); // 手机号
		if (StringUtils.isEmpty(mobile)) {
			info.put("error", "1");
			info.put("errorDesc", "手机号不能为空");
			return info;
		}
		if (StringUtils.isEmpty(cardNo)) {
			info.put("error", "1");
			info.put("errorDesc", "银行卡号不能为空");
			return info;
		}
		// 请求发送短信验证码
		BankCallBean bean = this.userBindCardService.cardBindPlusSendSms(user.getUserId(),
				PropUtils.getSystem(BankCallConstant.BANK_INSTCODE), BankCallMethodConstant.TXCODE_CARD_BIND_PLUS,
				mobile, BankCallConstant.CHANNEL_PC, cardNo);
		if (bean == null) {
			info.put("error", "1");
			info.put("errorDesc", "发送短信验证码异常");
			return info;
		}
		// 返回失败
		if (!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
			if ("JX900651".equals(bean.getRetCode())) {
				// 成功返回业务授权码
				LogUtil.endLog(THIS_CLASS, WeChatBindCardDefine.SEND_PLUS_CODE_ACTION);
				info.put("error", "0");
				info.put("errorDesc", "发送短信验证码成功");
				info.put("lastSrvAuthCode", bean.getSrvAuthCode());
				return info;
			}
			info.put("error", "1");
			info.put("errorDesc", "发送短信验证码失败，失败原因：" + userBindCardService.getBankRetMsg(bean.getRetCode()));
			return info;
		}
		// 成功返回业务授权码
		LogUtil.endLog(THIS_CLASS, WeChatBindCardDefine.SEND_PLUS_CODE_ACTION);
		info.put("error", "0");
		info.put("errorDesc", "发送短信验证码成功");
		info.put("lastSrvAuthCode", bean.getSrvAuthCode());
		return info;
	}

	/**
	 * 用户绑卡增强
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(WeChatBindCardDefine.BIND_CARD_PLUS)
	@ResponseBody
	public JSONObject bindCardPlus(HttpServletRequest request, HttpServletResponse response, String lastSrvAuthCode,
			String smsCode, String cardNo, String mobile) {
		LogUtil.startLog(THIS_CLASS, WeChatBindCardDefine.BIND_CARD_PLUS);
		JSONObject info = new JSONObject();
		Integer userId = Integer.parseInt(request.getParameter("userId"));
		// 检查参数
		if (checkParam(request, userId) != null) {
			info.put("error", "1");
			info.put("errorDesc", checkParam(request, userId));
			return info;
		}

		// 检查用户是否登录
		if (Validator.isNull(lastSrvAuthCode) || Validator.isNull(smsCode) || Validator.isNull(cardNo)
				|| Validator.isNull(mobile)) {
			LogUtil.endLog(THIS_CLASS, WeChatBindCardDefine.BIND_CARD_PLUS, "[参数不全]");
			info.put("error", "1");
			info.put("errorDesc", "参数不全");
			return info;
		}
		BankOpenAccount accountChinapnrTender = userBindCardService.getBankOpenAccount(userId);
		if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getAccount())) {
			LogUtil.endLog(THIS_CLASS, WeChatBindCardDefine.BIND_CARD_PLUS, "[用户未开户]");
			info.put("error", "1");
			info.put("errorDesc", "用户未开户");
			return info;
		}
		UsersInfo usersInfo = userBindCardService.getUsersInfoByUserId(userId);
		// 调用汇付接口(4.2.2 用户绑卡接口)
		BankCallBean bean = new BankCallBean();
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserId(StringUtil.valueOf(userId));
		bean.setLogRemark("用户绑卡增强");
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_CARD_BIND_PLUS);
		bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
		bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
		bean.setAccountId(accountChinapnrTender.getAccount());// 存管平台分配的账号
		bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);// 证件类型01身份证
		bean.setIdNo(usersInfo.getIdcard());// 证件号
		bean.setName(usersInfo.getTruename());// 姓名
		bean.setMobile(mobile);// 手机号
		bean.setCardNo(cardNo);// 银行卡号
		bean.setLastSrvAuthCode(lastSrvAuthCode);
		bean.setSmsCode(smsCode);
		bean.setUserIP(GetCilentIP.getIpAddr(request));// 客户IP
		LogAcqResBean logAcq = new LogAcqResBean();
		logAcq.setCardNo(cardNo);
		bean.setLogAcqResBean(logAcq);
		BankCallBean retBean = null;
		// 跳转到江西银行天下画面
		try {
			retBean = BankCallUtils.callApiBg(bean);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.endLog(THIS_CLASS, WeChatBindCardDefine.BIND_CARD_PLUS, "[调用银行接口失败~!]");
			info.put("error", "1");
			info.put("errorDesc", "调用银行接口失败~!");
			return info;
		}
		// 回调数据处理
		if (retBean == null || !(BankCallStatusConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode()))) {
			// 执行结果(失败)
			String message = this.userBindCardService.getBankRetMsg(retBean.getRetCode());
			LogUtil.debugLog(THIS_CLASS, WeChatBindCardDefine.BIND_CARD_PLUS,
					"银行返码:" + retBean.getRetCode() + "绑卡失败:" + message);
			LogUtil.endLog(THIS_CLASS, WeChatBindCardDefine.BIND_CARD_PLUS, "[绑卡失败]");
			info.put("error", "1");
			info.put("errorDesc", message);
			return info;
		}
		try {
			// 绑卡后处理
			this.userBindCardService.updateAfterBindCard(bean);
			List<BankCard> accountBankList = userBindCardService.getAccountBankByUserId(userId + "");
			if (accountBankList != null && accountBankList.size() > 0) {
				info.put("error", "0");
				info.put("errorDesc", "绑卡成功");
				return info;
			} else {
				info.put("error", "2");
				info.put("errorDesc", "银行处理中，请稍后查看");
			}
		} catch (Exception e) {
			// 执行结果(失败)
			e.printStackTrace();
			LogUtil.errorLog(THIS_CLASS, WeChatBindCardDefine.BIND_CARD_PLUS, e);
		}
		LogUtil.endLog(THIS_CLASS, WeChatBindCardDefine.REQUEST_MAPPING);
		return info;
	}

	/**
	 * 检查参数的正确性
	 *
	 * @param transAmt
	 * @param openBankId
	 * @param rechargeType
	 * @return
	 */
	private String checkParam(HttpServletRequest request, Integer userId) {
		BankOpenAccount accountChinapnrTender = userBindCardService.getBankOpenAccount(userId);
		if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getAccount())) {
			return "用户未开户，请开户后再操作。";
		}
		return null;
	}

}
