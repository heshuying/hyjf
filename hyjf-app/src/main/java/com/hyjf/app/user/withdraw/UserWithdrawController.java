/**
 * Description:用户提现
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: GOGTZ-T
 * @version: 1.0
 *           Created at: 2015年12月4日 下午2:32:36
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.app.user.withdraw;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.common.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseDefine;
import com.hyjf.app.BaseMapBean;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.bank.user.auto.AutoDefine;
import com.hyjf.app.bank.user.withdraw.AppWithdrawDefine;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.http.URLCodec;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountBank;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogWithBLOBs;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

/**
 * @package com.hyjf.web.user.withdraw
 * @author GOGTZ-T
 * @date 2015/12/04 14:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = UserWithdrawDefine.REQUEST_MAPPING)
public class UserWithdrawController extends BaseController {

	/** THIS_CLASS */
	private static final String THIS_CLASS = UserWithdrawController.class.getName();

	private static String HOST_URL = PropUtils.getSystem("hyjf.web.host");

	private static DecimalFormat df = new DecimalFormat("########0.00");

	@Autowired
	private UserWithdrawService userWithdrawService;

	/**
	 * 获取我的银行卡
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(UserWithdrawDefine.GET_BANKCARD_MAPPING)
	public JSONObject getBankCardAction(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, UserWithdrawDefine.GET_BANKCARD_MAPPING);

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

		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(token) || Validator.isNull(sign) || Validator.isNull(randomString)
				|| Validator.isNull(order)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		// 取得加密用的Key
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		try {
			ret.put("status", "0");
			ret.put("statusDesc", "成功");
			ret.put("request", UserWithdrawDefine.GET_BANKCARD_REQUEST);
			// 取得用户iD
			Integer userId = SecretUtil.getUserId(sign);
			// 取得银行卡信息
			List<AccountBank> banks = userWithdrawService.getBankCardByUserId(userId);
			List<BankCardBean> bankcards = new ArrayList<BankCardBean>();
			if (banks != null) {
				ret.put("cnt", banks.size() + "");
				for (AccountBank bank : banks) {
					BankCardBean bankCardBean = new BankCardBean();
					bankCardBean.setBank(bank.getBank());
					BankConfig bankConfig = userWithdrawService.getBankInfo(bank.getBank());
					bankCardBean.setLogo(HOST_URL + bankConfig.getAppLogo());// 应前台要求，logo路径给绝对路径
					bankCardBean.setBank(bankConfig.getName());// 银行名称 汉字
					bankCardBean.setCardNo(bank.getAccount());
					bankCardBean.setIsDefault(bank.getCardType());// 卡类型
																	// 0普通提现卡1默认卡2快捷支付卡
					bankcards.add(bankCardBean);
				}
			} else {
				ret.put("cnt", "0");
			}
			ret.put("banks", bankcards);
		} catch (Exception e) {
			ret.put("status", "1");
			ret.put("statusDesc", "获取我的银行卡发生错误");
		}
		LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.GET_BANKCARD_MAPPING);
		return ret;
	}

	/**
	 * 获取提现信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(UserWithdrawDefine.GET_WITHDRAW_INFO_MAPPING)
	public JSONObject getCashInfo(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, UserWithdrawDefine.GET_WITHDRAW_INFO_MAPPING);

		JSONObject ret = new JSONObject();
		ret.put("request", UserWithdrawDefine.GET_WITHDRAW_INFO_REQUEST);

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
		// bankCode 银行编号
		String bankCode = request.getParameter("bankCode");
		// getcash 提现金额
		String getcash = request.getParameter("getcash");
		// 金额显示格式
		DecimalFormat moneyFormat = null;
		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(token) || Validator.isNull(sign) || Validator.isNull(randomString)
				|| Validator.isNull(order)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
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
		// 判断选择哪种金融样式
		if (version.contains(CustomConstants.APP_VERSION_NUM)) {
			moneyFormat = CustomConstants.DF_FOR_VIEW_V1;
		} else {
			moneyFormat = CustomConstants.DF_FOR_VIEW;
		}
		// 提现规则静态页面的url
		ret.put("url", PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + UserWithdrawDefine.REQUEST_MAPPING + UserWithdrawDefine.GET_WITHDRAW_RULE_MAPPING);
		// 取得用户iD
		Integer userId = SecretUtil.getUserId(sign);
		// 取得用户当前余额
		Account account = this.userWithdrawService.getAccount(userId);
		if (account == null) {
			ret.put("status", "1");
			ret.put("statusDesc", "你的账户信息存在异常，请联系客服人员处理。");
			return ret;
		} else {
			ret.put("total", moneyFormat.format(account.getBalance()));// 可提现金额
		}
		// 取得用户在汇付天下的账户信息
		AccountChinapnr accountChinapnr = userWithdrawService.getAccountChinapnr(userId);
		// 用户未开户时,返回错误信息
		if (accountChinapnr == null) {
			ret.put("status", "1");
			ret.put("statusDesc", "用户未开户!");
			return ret;
		}
		// 取得银行卡信息
		// begin 调汇付接口查询银行卡信息 4.4.11 因为绑卡的时候汇付未能传递给我们是否默认卡
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10);
		bean.setCmdId(ChinaPnrConstant.CMDID_QUERY_CARD_INFO); // 消息类型(必须)
		bean.setMerCustId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID)); // 商户客户号
		bean.setUsrCustId(String.valueOf(accountChinapnr.getChinapnrUsrcustid())); // 用户客户号(必须)
		// 调用汇付接口
		ChinapnrBean chinaPnrBean = ChinapnrUtil.callApiBg(bean);
		// end 调汇付接口查询银行卡信息 4.4.11 因为绑卡的时候汇付未能传递给我们是否默认卡
		if (chinaPnrBean == null) {
			ret.put("status", "1");
			ret.put("statusDesc", "调用汇付接口(查询银行卡信息 4.4.11)发生错误!");
			return ret;
		}
		String UsrCardInfolist = chinaPnrBean.getUsrCardInfolist();
		JSONArray array = JSONObject.parseArray(UsrCardInfolist);
		BankConfig bankSetUp = new BankConfig();
		if (array.size() > 0) {
			ret.put("bankCnt", array.size() + "");
			List<BankCardBean> bankcards = new ArrayList<BankCardBean>();
			for (int j = 0; j < array.size(); j++) {
				JSONObject obj = array.getJSONObject(j);
				if (!obj.getString("RealFlag").equals("R")) {
					// 只有实名卡才入库
					continue;
				}
				BankConfig bankConfig = userWithdrawService.getBankInfo(obj.getString("BankId"));// eg:"BankId":"CCB"
				BankCardBean bankCardBean = new BankCardBean();
				bankCardBean.setIsDefault("0");// 普通卡
				if (obj.getString("IsDefault").equals("Y")) {
					bankCardBean.setIsDefault("1");// 默认卡
				}
				if (obj.getString("ExpressFlag").equals("Y")) {
					bankCardBean.setIsDefault("2");// 快捷卡
				}
				bankCardBean.setBankCode(obj.getString("BankId"));// 银行代号
				bankCardBean.setBank(bankConfig.getName());// 银行名称
				bankCardBean.setLogo(HOST_URL + bankConfig.getAppLogo());// 应前台要求，logo路径给绝对路径
				bankCardBean.setCardNo(obj.getString("CardId"));
				bankCardBean.setCardNo_info(BankCardUtil.getFormatCardNo(obj.getString("CardId")));
				bankcards.add(bankCardBean);
				// 判断是否已经传银行卡code，如果已传则获取该银行的信息
				if (bankCode != null && bankCode.equals(obj.getString("BankId"))) {
					bankSetUp = bankConfig;
				} else {
					// 如果没有传银行卡code，则判断是默认银行卡或快捷卡记录银行设置
					if (obj.getString("IsDefault").equals("Y") || obj.getString("ExpressFlag").equals("Y")) {
						bankSetUp = bankConfig;
					}

				}
			}
			ret.put("banks", bankcards);
			ret.put("logo", bankcards.get(0).getLogo());
			ret.put("cardNo", bankcards.get(0).getCardNo());
			ret.put("cardNo_info",bankcards.get(0).getCardNo_info());
			ret.put("bank", bankcards.get(0).getBank());
		} else {
			ret.put("bankCnt", "0");
		}

		// 银行卡支持的提现方式 开始
		int cashchlCnt = 0;
		JSONArray cashchls = new JSONArray();
		// 判断是否有一般提现
		if (1 == bankSetUp.getNormalWithdraw()) {
			JSONObject jo = new JSONObject();
			jo.put("cashchlNm", "GENERAL");
			jo.put("cashchlRemark", "一般提现");
			// 默认提现方式,0一般提现,1快速提现,2即时提现,默认0
			if (bankSetUp.getWithdrawDefaulttype() == 0) {
				jo.put("isDefaultCashchl", "1");// 是否默认提现方式1是，0否
			} else {
				jo.put("isDefaultCashchl", "0");// 是否默认提现方式1是，0否
			}
			cashchlCnt++;
			cashchls.add(jo);
		}
		// 判断是否有快速提现
		if (1 == bankSetUp.getQuickWithdraw()) {
			JSONObject jo = new JSONObject();
			jo.put("cashchlNm", "FAST");
			jo.put("cashchlRemark", "快速提现");
			// 默认提现方式,0一般提现,1快速提现,2即时提现,默认0
			if (bankSetUp.getWithdrawDefaulttype() == 1) {
				jo.put("isDefaultCashchl", "1");// 是否默认提现方式1是，0否
			} else {
				jo.put("isDefaultCashchl", "0");// 是否默认提现方式1是，0否
			}
			cashchlCnt++;
			cashchls.add(jo);
		}
		// 判断是否有即时提现
		if (1 == bankSetUp.getImmediatelyWithdraw()) {
			JSONObject jo = new JSONObject();
			jo.put("cashchlNm", "IMMEDIATE");
			jo.put("cashchlRemark", "即时提现");
			// 默认提现方式,0一般提现,1快速提现,2即时提现,默认0
			if (bankSetUp.getWithdrawDefaulttype() == 2) {
				jo.put("isDefaultCashchl", "1");// 是否默认提现方式1是，0否
			} else {
				jo.put("isDefaultCashchl", "0");// 是否默认提现方式1是，0否
			}
			cashchlCnt++;
			cashchls.add(jo);
		}
		ret.put("cashchlCnt", cashchlCnt + "");// 提现方式总数
		ret.put("cashchls", cashchls);
		// 银行卡支持的提现方式 结束
		// 如果提现金额是0
		if ("0".equals(getcash) || "".equals(getcash)) {
			ret.put("accountDesc", "手续费: 0 元；实际到账: 0 元");
			ret.put("fee", "0.00 元");
            ret.put("balance", "0.00 元");
            ret.put("buttonWord", "提现");
		} else {
		    
			String balance = "";
			try {
			    if ((new BigDecimal(getcash).subtract(BigDecimal.ONE)).compareTo(BigDecimal.ZERO) < 0) {
	                balance = "0";
	            } else {
	                balance = moneyFormat.format(new BigDecimal(getcash).subtract(BigDecimal.ONE));
	            }
            } catch (Exception e) {
                ret.put("accountDesc", "手续费: 0 元；实际到账: 0 元");
                ret.put("fee", "0.00 元");
                ret.put("balance", "0.00 元");
                ret.put("buttonWord", "提现");
                ret.put("status", "1");
                ret.put("statusDesc", "提现金额格式错误!");
                return ret;
            }
			
			ret.put("accountDesc", "手续费: 1 元；实际到账: " + balance + " 元");
			ret.put("fee", "1.00 元");
			ret.put("balance", balance+" 元");
			ret.put("buttonWord", "确认提现"+moneyFormat.format(new BigDecimal("".equals(getcash)?"0":getcash))+"元");
		}
		
		ret.put("status", "0");
		ret.put("statusDesc", "成功");
		ret.put("request", UserWithdrawDefine.REQUEST_HOME+UserWithdrawDefine.REQUEST_MAPPING+UserWithdrawDefine.GET_WITHDRAW_INFO_MAPPING);
		LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.GET_WITHDRAW_INFO_MAPPING);
		return ret;
	}

	/**
	 * 获取提现URL -- 提现前参数校验！
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(UserWithdrawDefine.GET_CASH_URL_MAPPING)
	public JSONObject getCashUrl(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, UserWithdrawDefine.GET_CASH_URL_MAPPING);

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
		String card = request.getParameter("cardNo");
		// getcash 提现金额
		String getcash = request.getParameter("total");
		// cashchl 提现方式(取现渠道) (FAST:快速取现 , GENERAL:一般取现 , IMMEDIATE:即时取现)
		String cashchl = request.getParameter("cashchl");
		cashchl="FAST";
		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(token) || Validator.isNull(sign) || Validator.isNull(randomString)
				|| Validator.isNull(order) || Validator.isNull(card) || Validator.isNull(getcash) || Validator.isNull(cashchl)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			ret.put("request", "/hyjf-app/user/withdraw/getCashUrl");
			return ret;
		}
		if (new BigDecimal(getcash).compareTo(BigDecimal.ONE) <= 0) {
			ret.put("status", "1");
			ret.put("statusDesc", "提现金额需大于1元！");
			ret.put("request", "/hyjf-app/user/withdraw/getCashUrl");
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
		// 取得用户iD
        Integer userId = SecretUtil.getUserId(sign);
        // 取得用户当前余额
        Account account = this.userWithdrawService.getAccount(userId);
        if(account.getBalance().compareTo(new BigDecimal(getcash))<0){
            ret.put("status", "1");
            ret.put("statusDesc", "提现金额不能大于账户可用金额");
            ret.put("request", "/hyjf-app/user/withdraw/getCashUrl");
            return ret;
        }
		try {
			ret.put("status", "0");
			ret.put("statusDesc", "成功");
			ret.put("request", UserWithdrawDefine.GET_CASH_URL_REQUEST);
			StringBuffer sbUrl = new StringBuffer();
			sbUrl.append(PropUtils.getSystem(CustomConstants.HYJF_WEB_URL));
			sbUrl.append(request.getContextPath());
			sbUrl.append(UserWithdrawDefine.REQUEST_MAPPING);
			sbUrl.append(UserWithdrawDefine.CASH_MAPPING);
			sbUrl.append("?").append("version").append("=").append(version);
			sbUrl.append("&").append("netStatus").append("=").append(netStatus);
			sbUrl.append("&").append("platform").append("=").append(platform);
			sbUrl.append("&").append("randomString").append("=").append(randomString);
			sbUrl.append("&").append("sign").append("=").append(sign);
			sbUrl.append("&").append("token").append("=").append(strEncode(token));
			sbUrl.append("&").append("order").append("=").append(strEncode(order));
			sbUrl.append("&").append("card").append("=").append(card);
			sbUrl.append("&").append("getcash").append("=").append(getcash);
			sbUrl.append("&").append("cashchl").append("=").append(cashchl);
			ret.put("url", sbUrl.toString());
		} catch (Exception e) {
			ret.put("status", "1");
			ret.put("statusDesc", "获取提现URL失败");
		}

		LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.GET_CASH_URL_MAPPING);
		return ret;
	}
	
	/**
	 * 用户提现
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(UserWithdrawDefine.CASH_MAPPING)
	public ModelAndView cash(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(UserWithdrawController.class.toString(), UserWithdrawDefine.CASH_MAPPING);
		ModelAndView modelAndView = new ModelAndView(UserWithdrawDefine.JSP_CHINAPNR_SEND);
		String message = "";
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

		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(token) || Validator.isNull(sign) || Validator.isNull(randomString)
				|| Validator.isNull(order)) {
			message = "请求参数非法";
			modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
			BaseMapBean baseMapBean=new BaseMapBean();
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
			baseMapBean.setCallBackAction(CustomConstants.HOST + UserWithdrawDefine.JUMP_HTML_ERROR_PATH);
			modelAndView.addObject("callBackForm", baseMapBean);
			return modelAndView;
		}

		// 取得加密用的Key
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			message = "请求参数非法";
			modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
			BaseMapBean baseMapBean=new BaseMapBean();
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
			baseMapBean.setCallBackAction(CustomConstants.HOST + UserWithdrawDefine.JUMP_HTML_ERROR_PATH);
			modelAndView.addObject("callBackForm", baseMapBean);
			return modelAndView;
		}
		Integer userId = SecretUtil.getUserId(sign); // 取得用户ID
		String userName = SecretUtil.getUserName(sign); // 用户名
		String transAmt = request.getParameter("getcash");// 交易金额
		String bankId = request.getParameter("card");// 提现银行卡号
		String cashchl = request.getParameter("cashchl");// 取现渠道(暂时无用)
		// 检查参数
		JSONObject checkResult = checkParam(request, userId, transAmt, bankId);
		if (checkResult != null) {
			message = checkResult.getString("message");
			if ("提现金额需大于1元！".equals(message)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("status", "1");
				map.put("statusDesc", "提现金额需大于1元！");
				modelAndView = new ModelAndView("jsonView", map);
			} else {
				modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
				BaseMapBean baseMapBean=new BaseMapBean();
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
				baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
				baseMapBean.setCallBackAction(CustomConstants.HOST + UserWithdrawDefine.JUMP_HTML_ERROR_PATH);
				modelAndView.addObject("callBackForm", baseMapBean);
				return modelAndView;
			}
		}

		// 取得用户在汇付天下的客户号
		AccountChinapnr accountChinapnrTender = userWithdrawService.getAccountChinapnr(userId);
		if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getChinapnrUsrcustid())) {
			message = "您还未开户，请开户后重新操作";
			modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
			BaseMapBean baseMapBean=new BaseMapBean();
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
			baseMapBean.setCallBackAction(CustomConstants.HOST + UserWithdrawDefine.JUMP_HTML_ERROR_PATH);
			modelAndView.addObject("callBackForm", baseMapBean);
			LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.CASH_MAPPING, "[用户未开户]");
			return modelAndView;
		}
		Long chinapnrUsrcustidTender = accountChinapnrTender.getChinapnrUsrcustid();

		// 取现渠道(FAST:快速取现 , GENERAL:一般取现 , IMMEDIATE:即时取现)
		if (Validator.isNull(cashchl)) {
			cashchl = "GENERAL";// 默认是一般提现
		}

		// 校验 银行卡号
		AccountBank accountBank = this.userWithdrawService.getBankInfo(userId, bankId);
		if (accountBank == null || Validator.isNull(accountBank.getAccount())) {
			message = "该银行卡信息不存在，请核实后重新操作";
			modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
			BaseMapBean baseMapBean=new BaseMapBean();
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
			baseMapBean.setCallBackAction(CustomConstants.HOST + UserWithdrawDefine.JUMP_HTML_ERROR_PATH);
			modelAndView.addObject("callBackForm", baseMapBean);
			return modelAndView;
		}

		// 取得手续费
		String fee = this.userWithdrawService.getWithdrawFee(userId, bankId, new BigDecimal(transAmt), cashchl);

		// 实际取现费用(洪刚提示跟线上保持一致)
		// 入参扩展域
		JSONArray reqExt = new JSONArray();
		JSONObject reqExtObj = new JSONObject();
		reqExtObj.put("CashFeeDeductType", "I");
		// reqExtObj.put("FeeObjFlag", "U"); // 向用户收取
		// reqExtObj.put("FeeAcctId", ""); // 忽略
		// reqExtObj.put("CashChl", cashchl); // 取现渠道
		reqExt.add(reqExtObj);

		// 调用汇付接口(提现)
		String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + UserWithdrawDefine.REQUEST_MAPPING + UserWithdrawDefine.RETURN_MAPPING;// 提现同步回调路径
		String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + UserWithdrawDefine.REQUEST_MAPPING + UserWithdrawDefine.CALLBACK_MAPPING;// 提现异步回调路径
		ChinapnrBean bean = new ChinapnrBean();
		// 2.0接口
		bean.setVersion(ChinaPnrConstant.VERSION_20);// 接口版本号
		bean.setCmdId(ChinaPnrConstant.CMDID_CASH); // 消息类型(主动投标)
		bean.setOrdId(GetOrderIdUtils.getOrderId2(Integer.valueOf(userId))); // 订单号(必须)
		bean.setUsrCustId(String.valueOf(chinapnrUsrcustidTender));// 用户客户号
		bean.setTransAmt(CustomUtil.formatAmount(transAmt));// 交易金额(必须)
		bean.setOpenAcctId(accountBank.getAccount()); // 开户银行账号
		bean.setRetUrl(retUrl); // 页面返回 URL
		bean.setBgRetUrl(bgRetUrl); // 商户后台应答地址(必须)
		bean.setMerPriv(CustomUtil.formatAmount(fee)); // 商户私有域
		bean.setPageType("1");// app 应用风格页面（无标题）
		bean.setType("user_cash"); // 日志类型(写日志用)
		bean.setReqExt(reqExt.toJSONString());
		// 插值用参数
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", String.valueOf(userId));
		params.put("userName", userName);
		params.put("ip", CustomUtil.getIpAddr(request));
		params.put("bankId", bankId);
		params.put("client", platform);// 平台类型 0pc 1WX 2AND 3IOS 4other
		// 用户提现前处理
		boolean withdrawFlag = this.userWithdrawService.updateBeforeCash(bean, params);
		if (withdrawFlag) {
			// 跳转到汇付天下画面
			try {
				modelAndView = ChinapnrUtil.callApi(bean);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "请不要重复操作";
			modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
			BaseMapBean baseMapBean=new BaseMapBean();
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
			baseMapBean.setCallBackAction(CustomConstants.HOST + UserWithdrawDefine.JUMP_HTML_ERROR_PATH);
			modelAndView.addObject("callBackForm", baseMapBean);
			return modelAndView;
		}
		LogUtil.endLog(UserWithdrawController.class.toString(), UserWithdrawDefine.CASH_MAPPING);
		return modelAndView;
	}

	/**
	 * 用户提现后处理 同步
	 *
	 * @param request
	 * @param bean
	 * @return
	 */
	@RequestMapping(UserWithdrawDefine.RETURN_MAPPING)
	public ModelAndView cashReturn(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) {
		LogUtil.startLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
		bean.convert();
		System.out.println("提现后同步处理开始: 参数: " + bean == null ? "无" : bean.getAllParams() + "]");
		LogUtil.debugLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");

		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		// 取得更新用UUID
		boolean updateFlag = false;
		String uuid = request.getParameter("uuid");
		if (Validator.isNotNull(uuid)) {
			// 取得检证数据
			ChinapnrExclusiveLogWithBLOBs record = userWithdrawService.selectChinapnrExclusiveLog(Long.parseLong(uuid));
			// 如果检证数据状态为未发送
			if (record != null && ChinaPnrConstant.STATUS_VERTIFY_OK.equals(record.getStatus())) {
				// 将状态更新成[2:处理中]
				record.setId(Long.parseLong(uuid));
				record.setStatus(ChinaPnrConstant.STATUS_RUNNING);
				int cnt = this.userWithdrawService.updateChinapnrExclusiveLog(record);
				if (cnt > 0) {
					updateFlag = true;
				}
			}
		} else {
			updateFlag = true;
		}
		System.out.println(updateFlag);

		// 其他程序正在处理中,或者返回值错误
		if (!updateFlag) {
			ModelAndView modelAndView = new ModelAndView(BaseDefine.JUMP_HTML);
			if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
//				modelAndView.addObject("TransAmt", df.format(new BigDecimal(bean.getTransAmt())));
//				modelAndView.addObject("RealTransAmt", df.format(new BigDecimal(bean.getRealTransAmt())));
				
				BaseMapBean baseMapBean = new BaseMapBean();
	            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
	            baseMapBean.set(CustomConstants.APP_STATUS_DESC,"提现成功");
	            baseMapBean.set("amount",bean.getTransAmt());
	            baseMapBean.set("charge",bean.getRealTransAmt());
	            baseMapBean.setCallBackAction(CustomConstants.HOST+AppWithdrawDefine.WITHDRAW_SUCCESS_HTML);
	            modelAndView.addObject("TransAmt", df.format(new BigDecimal(bean.getTransAmt())));
	            modelAndView.addObject("RealTransAmt", df.format(new BigDecimal(bean.getRealTransAmt())));
	            modelAndView.addObject("callBackForm",baseMapBean);
				
			} else if (ChinaPnrConstant.RESPCODE_CHECK.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
				modelAndView = new ModelAndView(BaseDefine.JUMP_HTML);
				BaseMapBean baseMapBean=new BaseMapBean();
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
				baseMapBean.set(CustomConstants.APP_STATUS_DESC, "汇付处理中，请稍后查询交易明细");
				baseMapBean.setCallBackAction(CustomConstants.HOST + UserWithdrawDefine.JUMP_HTML_HANDLING_PATH);
				modelAndView.addObject("callBackForm", baseMapBean);
				return modelAndView;
			} else {
				modelAndView = new ModelAndView(BaseDefine.JUMP_HTML);
				BaseMapBean baseMapBean=new BaseMapBean();
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
				baseMapBean.set(CustomConstants.APP_STATUS_DESC, "汇付处理中，请稍后查询交易明细");
				baseMapBean.setCallBackAction(CustomConstants.HOST + UserWithdrawDefine.JUMP_HTML_HANDLING_PATH);
				modelAndView.addObject("callBackForm", baseMapBean);
				return modelAndView;
			}
			
			
			LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易正在处理中或者已经完成,本次回调结束]");
//			System.out.println("提现后同步处理结束: 交易正在处理中或者已经完成,本次回调结束");
			return modelAndView;
		}

		// 发送状态
		String status = ChinaPnrConstant.STATUS_VERTIFY_OK;
		// 失败时去汇付查询交易状态
		if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
			String transStat = userWithdrawService.checkCashResult(bean.getOrdId());
			if ("S".equals(transStat)) {
				// 取得成功时的信息
				JSONObject data = userWithdrawService.getMsgData(bean.getOrdId());
				if (data != null) {
					// 设置状态
					if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_RESPCODE))) {
						bean.setRespCode(data.getString(ChinaPnrConstant.PARAM_RESPCODE));
						bean.set(ChinaPnrConstant.PARAM_RESPCODE, data.getString(ChinaPnrConstant.PARAM_RESPCODE));
					}
					// 设置结果
					if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_RESPDESC))) {
						bean.setRespDesc(data.getString(ChinaPnrConstant.PARAM_RESPDESC));
						bean.set(ChinaPnrConstant.PARAM_RESPDESC, data.getString(ChinaPnrConstant.PARAM_RESPDESC));
					}
					// 设置手续费
					if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_FEEAMT))) {
						bean.setFeeAmt(data.getString(ChinaPnrConstant.PARAM_FEEAMT));
						bean.set(ChinaPnrConstant.PARAM_FEEAMT, data.getString(ChinaPnrConstant.PARAM_FEEAMT));
					}
					// 设置取现银行
					if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_OPENBANKID))) {
						bean.setOpenBankId(data.getString(ChinaPnrConstant.PARAM_OPENBANKID));
						bean.set(ChinaPnrConstant.PARAM_OPENBANKID, data.getString(ChinaPnrConstant.PARAM_OPENBANKID));
					}
				}
			}
		}

		// 成功或审核中或提现失败
		if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE)) || ChinaPnrConstant.RESPCODE_CHECK.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))
				|| ChinaPnrConstant.RESPCODE_WITHDRAW_FAIL.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
			try {
				// 用户userId
				Integer userId = userWithdrawService.selectUserIdByUsrcustid(bean.getLong(ChinaPnrConstant.PARAM_USRCUSTID));
				// 插值用参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("userId", String.valueOf(userId));
				// params.put("userName", userName);
				params.put("ip", CustomUtil.getIpAddr(request));
				// 执行提现后处理
				boolean flag = this.userWithdrawService.handlerAfterCash(bean, params);
				if (flag) {
					// 执行结果(成功)
					if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
						status = ChinaPnrConstant.STATUS_SUCCESS;
					} else if (ChinaPnrConstant.RESPCODE_CHECK.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
						status = ChinaPnrConstant.STATUS_VERTIFY_OK;
					} else if (ChinaPnrConstant.RESPCODE_WITHDRAW_FAIL.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
						status = ChinaPnrConstant.STATUS_FAIL;
					}
				} else {
					status = ChinaPnrConstant.STATUS_FAIL;
				}
				LogUtil.debugLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "成功");
				System.out.println("提现后同步处理结束: 成功。。");
			} catch (Exception e) {
				// 执行结果(失败)
				status = ChinaPnrConstant.STATUS_FAIL;
				LogUtil.errorLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, e);
				System.out.println("提现后同步处理结束: 失败: " + e);
			}

		} else {
			// 执行结果(失败)
			status = ChinaPnrConstant.STATUS_FAIL;
			// 更新提现失败原因
			String reason = bean.getRespDesc();
			if (StringUtils.isNotEmpty(reason)) {
				if (reason.contains("%")) {
					reason = URLCodec.decodeURL(reason);
				}
			}
			if (StringUtils.isNotEmpty(bean.getOrdId())) {
				this.userWithdrawService.updateAccountWithdrawByOrdId(bean.getOrdId(), reason);
			}
			LogUtil.debugLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "失败");
			System.out.println("提现后同步处理结束: 失败le: " + reason);
		}

		// 更新状态记录
		if (updateFlag && Validator.isNotNull(uuid)) {
			this.userWithdrawService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
		}
		if (ChinaPnrConstant.STATUS_SUCCESS.equals(status)) {
			ModelAndView modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
			BaseMapBean baseMapBean=new BaseMapBean();
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, "汇付处理中，请稍后查询交易明细");
			baseMapBean.set("amount", bean.getTransAmt());
			baseMapBean.set("charge", bean.getRealTransAmt());
			baseMapBean.setCallBackAction(CustomConstants.HOST + UserWithdrawDefine.JUMP_HTML_SUCCESS_PATH);
			modelAndView.addObject("callBackForm", baseMapBean);
			modelAndView.addObject("TransAmt", df.format(new BigDecimal(bean.getTransAmt())));
			modelAndView.addObject("RealTransAmt", df.format(new BigDecimal(bean.getRealTransAmt())));
			LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易成功,回调结束]");
//			System.out.println("提现后同步处理结束: 成功,回调结束]");
			return modelAndView;
		} else if (ChinaPnrConstant.RESPCODE_CHECK.equals(status)) {
			ModelAndView modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
			BaseMapBean baseMapBean=new BaseMapBean();
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, "汇付处理中，请稍后查询交易明细");
			baseMapBean.setCallBackAction(CustomConstants.HOST + UserWithdrawDefine.JUMP_HTML_HANDLING_PATH);
			modelAndView.addObject("callBackForm", baseMapBean);
			LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易正在处理中,回调结束]");
//			System.out.println("提现后同步处理结束: 999 处理中]");
			return modelAndView;
		} else {
			ModelAndView modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
			BaseMapBean baseMapBean=new BaseMapBean();
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, "汇付处理中，请稍后查询交易明细");
			baseMapBean.setCallBackAction(CustomConstants.HOST + UserWithdrawDefine.JUMP_HTML_HANDLING_PATH);
			modelAndView.addObject("callBackForm", baseMapBean);
			LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易失败,回调结束]");
//			System.out.println("提现后同步处理结束: 失败:]");
			return modelAndView;
		}
	}

	/**
	 * 用户提现后处理 异步
	 *
	 * @param request
	 * @param bean
	 * @return
	 */
	@RequestMapping(UserWithdrawDefine.CALLBACK_MAPPING)
	public ModelAndView cashCallBack(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) {
		LogUtil.startLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
		bean.convert();
		LogUtil.debugLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
		System.out.println("提现后异步处理开始: 参数: " + bean == null ? "无" : bean.getAllParams() + "]");
		// 取得更新用UUID
		boolean updateFlag = false;
		String uuid = request.getParameter("uuid");
		if (Validator.isNotNull(uuid)) {
			// 取得检证数据
			ChinapnrExclusiveLogWithBLOBs record = userWithdrawService.selectChinapnrExclusiveLog(Long.parseLong(uuid));
			// 如果检证数据状态为未发送
			if (record != null && ChinaPnrConstant.STATUS_VERTIFY_OK.equals(record.getStatus())) {
				// 将状态更新成[2:处理中]
				record.setId(Long.parseLong(uuid));
				record.setStatus(ChinaPnrConstant.STATUS_RUNNING);
				int cnt = this.userWithdrawService.updateChinapnrExclusiveLog(record);
				if (cnt > 0) {
					updateFlag = true;
				}
			}
		} else {
			updateFlag = true;
		}
		System.out.println(updateFlag);

		// 其他程序正在处理中,或者返回值错误
		if (!updateFlag) {
			ModelAndView modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_SUCCESS_PATH);
			modelAndView.addObject("TransAmt", df.format(new BigDecimal(bean.getTransAmt())));
			modelAndView.addObject("RealTransAmt", df.format(new BigDecimal(bean.getRealTransAmt())));
			LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易正在处理中或者已经完成,本次回调结束]");
			System.out.println("提现后异步处理结束: 提现成功");
			return modelAndView;
		}

		// 发送状态
		String status = ChinaPnrConstant.STATUS_VERTIFY_OK;
		// 失败时去汇付查询交易状态
		if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
			String transStat = userWithdrawService.checkCashResult(bean.getOrdId());
			if ("S".equals(transStat)) {
				// 取得成功时的信息
				JSONObject data = userWithdrawService.getMsgData(bean.getOrdId());
				if (data != null) {
					// 设置状态
					if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_RESPCODE))) {
						bean.setRespCode(data.getString(ChinaPnrConstant.PARAM_RESPCODE));
						bean.set(ChinaPnrConstant.PARAM_RESPCODE, data.getString(ChinaPnrConstant.PARAM_RESPCODE));
					}
					// 设置结果
					if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_RESPDESC))) {
						bean.setRespDesc(data.getString(ChinaPnrConstant.PARAM_RESPDESC));
						bean.set(ChinaPnrConstant.PARAM_RESPDESC, data.getString(ChinaPnrConstant.PARAM_RESPDESC));
					}

					// 设置手续费
					if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_FEEAMT))) {
						bean.setFeeAmt(data.getString(ChinaPnrConstant.PARAM_FEEAMT));
						bean.set(ChinaPnrConstant.PARAM_FEEAMT, data.getString(ChinaPnrConstant.PARAM_FEEAMT));
					}
					// 设置取现银行
					if (Validator.isNotNull(data.getString(ChinaPnrConstant.PARAM_OPENBANKID))) {
						bean.setOpenBankId(data.getString(ChinaPnrConstant.PARAM_OPENBANKID));
						bean.set(ChinaPnrConstant.PARAM_OPENBANKID, data.getString(ChinaPnrConstant.PARAM_OPENBANKID));
					}
				}
			}
		}

		// 成功或审核中或提现失败
		if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE)) || ChinaPnrConstant.RESPCODE_CHECK.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))
				|| ChinaPnrConstant.RESPCODE_WITHDRAW_FAIL.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
			try {
				//用户userId
				Integer userId = userWithdrawService.selectUserIdByUsrcustid(bean.getLong(ChinaPnrConstant.PARAM_USRCUSTID));
				// 插值用参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("userId", String.valueOf(userId));
				params.put("ip", CustomUtil.getIpAddr(request));
				// 执行提现后处理
				boolean flag = this.userWithdrawService.handlerAfterCash(bean, params);
				if(flag){
					// 执行结果(成功)
					if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
						status = ChinaPnrConstant.STATUS_SUCCESS;
					} else if (ChinaPnrConstant.RESPCODE_CHECK.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
						status = ChinaPnrConstant.STATUS_VERTIFY_OK;
					} else if (ChinaPnrConstant.RESPCODE_WITHDRAW_FAIL.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
						status = ChinaPnrConstant.STATUS_FAIL;
					}
					System.out.println("提现后异步处理结束: 成功");
					LogUtil.debugLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "成功");
				}else{
					status = ChinaPnrConstant.STATUS_FAIL;
				}
				
			} catch (Exception e) {
				// 执行结果(失败)
				status = ChinaPnrConstant.STATUS_FAIL;
				LogUtil.errorLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, e);
				System.out.println("提现后异步处理结束: 提现失败: " + e);
			}
		} else {
			// 执行结果(失败)
			status = ChinaPnrConstant.STATUS_FAIL;
			// 更新提现失败原因
			String reason = bean.getRespDesc();
			if (StringUtils.isNotEmpty(reason)) {
				if (reason.contains("%")) {
					reason = URLCodec.decodeURL(reason);
				}
			}
			this.userWithdrawService.updateAccountWithdrawByOrdId(bean.getOrdId(), reason);
			System.out.println("提现后异步处理结束: 提现失败了: " + reason);
			LogUtil.debugLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "失败");
		}

		// 更新状态记录
		if (updateFlag && Validator.isNotNull(uuid)) {
			this.userWithdrawService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
		}
		if (ChinaPnrConstant.STATUS_SUCCESS.equals(status)) {
			ModelAndView modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_SUCCESS_PATH);
			modelAndView.addObject("TransAmt", df.format(new BigDecimal(bean.getTransAmt())));
			modelAndView.addObject("RealTransAmt", df.format(new BigDecimal(bean.getRealTransAmt())));
			LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易成功,回调结束]");
			System.out.println("提现后异步处理结束: 提现成功,回调结束");
			return modelAndView;
		} else if (ChinaPnrConstant.RESPCODE_CHECK.equals(status)) {
			ModelAndView modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_INPROCESS_PATH);
			modelAndView.addObject("message", "汇付处理中，请稍后查询交易明细");
			LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易正在处理中,回调结束]");
			System.out.println("提现后异步处理结束: 提现交易正在处理中,回调结束");
			return modelAndView;
		} else {
			ModelAndView modelAndView = new ModelAndView(UserWithdrawDefine.WITHDRAW_INPROCESS_PATH);
			modelAndView.addObject("message", "汇付处理中，请稍后查询交易明细");
			LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易失败,回调结束]");
			System.out.println("提现后异步处理结束: 提现不成功: ");
			return modelAndView;
		}
	}

	/**
	 * 检查参数的正确性
	 *
	 * @param userId
	 * @param transAmtStr
	 * @param bankId
	 * @return
	 */
	private JSONObject checkParam(HttpServletRequest request, Integer userId, String transAmtStr, String bankId) {
		// 检查用户是否登录
		if (Validator.isNull(userId)) {
			return jsonMessage("您没有登录，请登录后再进行提现。", "1");
		}
		// 判断用户是否被禁用
		Users users = this.userWithdrawService.getUsers(userId);
		if (users == null || users.getStatus() == 1) {
			return jsonMessage("对不起,该用户已经被禁用。", "1");
		}
		// 检查参数(交易金额是否数字)
		if (Validator.isNull(transAmtStr) || !NumberUtils.isNumber(transAmtStr)) {
			return jsonMessage("请输入提现金额。", "1");
		}
		// 检查参数(交易金额是否大于0)
		BigDecimal transAmt = new BigDecimal(transAmtStr);
		if (transAmt.compareTo(BigDecimal.ONE) <= 0) {
			return jsonMessage("提现金额需大于1元！", "1");
		}
		// 取得用户当前余额
		Account account = this.userWithdrawService.getAccount(userId);
		// 投标金额大于可用余额
		if (account == null || transAmt.compareTo(account.getBalance()) > 0) {
			return jsonMessage("提现金额大于可用余额，请确认后再次提现。", "1");
		}
		// 检查参数(银行卡ID是否数字)
		if (Validator.isNotNull(bankId) && !NumberUtils.isNumber(bankId)) {
			return jsonMessage("银行卡号不正确，请确认后再次提现。", "1");
		}
		return null;
	}

	/**
	 * 组成返回URL for app
	 *
	 * @param message
	 * @param error
	 * @return
	 */
	public String redirectAppUrl(HttpServletRequest request, String error, String message) {

		String url = "";
		if ("0".equals(error)) {
			url = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + UserWithdrawDefine.WITHDRAW_SUCCESS_PATH;
		} else {
			url = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + UserWithdrawDefine.WITHDRAW_ERROR_PATH;
		}
		return url;
	}

	/**
	 * 
	 * 获取提现规则H5页面
	 * 
	 * @author renxingchen
	 * @return
	 */
	@RequestMapping(UserWithdrawDefine.GET_WITHDRAW_RULE_MAPPING)
	public ModelAndView rechargeRule() {
		return new ModelAndView(UserWithdrawDefine.WITHDRAW_RULE_PATH);
	}
}
