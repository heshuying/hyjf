package com.hyjf.app.bank.user.recharge;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.qos.logback.core.pattern.ConverterUtil;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseMapBean;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.bank.user.withdraw.AppWithdrawController;
import com.hyjf.app.bank.user.withdraw.AppWithdrawDefine;
import com.hyjf.app.util.DES;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountRecharge;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BanksConfig;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.soa.apiweb.CommonSoaUtils;


/**
 * App端用户充值Controller
 * 
 * @author liuyang
 *
 */
@Controller
@RequestMapping(value = AppRechargeDefine.REQUEST_MAPPING)
public class AppRechargeController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(AppRechargeController.class);

	/** THIS_CLASS */
	private static final String THIS_CLASS = AppRechargeController.class.getName();

	@Autowired
	private AppRechargeService appRechargeService;
	/** 发布地址 */
	private static String HOST_URL = PropUtils.getSystem("hyjf.web.host");
	/** 充值接口 */
	public static final String RECHARGE_URL = HOST_URL + "/hyjf-app/bank/user/userDirectRecharge/recharge?";


	/** 充值描述 */
	private final String CARD_DESC = "限额:{0}{1}{2}";
	private final String RECHARGE_KINDLY_REMINDER = "注：网银转账时，银行请选择（城市商业银行）江西银行或南昌银行。线下充值的到账时间一般为1-3天（具体到账时间以银行的实际到账时间为准）。";
	private final String RCV_ACCOUNT_NAME = "惠众商务顾问（北京）有限公司";
	private final String RCV_ACCOUNT = "791913149300306";
	private final String RCV_OPEN_BANK_NAME = "江西银行南昌铁路支行";
	private final String KINDLY_REMINDER = "温馨提示：\n①转账充值使用网银转账时，付款账户须与平台绑定银行卡一致，不支持非平台绑定银行卡的网银转账充值功能，即“同卡进出”原则；\n②银行转账时，请选择（城市商业银行）江西银行或者南昌银行；\n③转账充值在工作日17:00前完成操作，当日到账，否则资金最晚将于下个工作日到账；\n④转账充值不符合“同卡进出”原则的，充值资金最长T+1工作日会被退回至付款账户；\n⑤不支持支付宝、微信等第三方支付平台的转账充值功能。";
	private final String IMPORTANT_HINTS = "请务必使用该卡作为付款账户进行转账";

	/** 金额单位 万元 */
	private final Integer AMOUNT_UNIT = 10000;

	/**
	 * 线下充值信息
	 * 以前嵌入充值页的线下充值信息，现在修改为获取充值接口中一起返回了
	 * @param request
	 * @return
	 */
	@Deprecated
	@RequestMapping(AppRechargeDefine.OFFLINERECHAGEINFO_ACTION)
	public ModelAndView offLineRechageInfo(HttpServletRequest request) {
		LogUtil.startLog(THIS_CLASS, AppRechargeDefine.OFFLINERECHAGEINFO_ACTION, "获取线下充值信息开始");
		String sign = request.getParameter("sign");
		Integer userId = SecretUtil.getUserId(sign); // 用户ID
		ModelAndView modelAndView = new ModelAndView(AppRechargeDefine.OFFLINERECHAGEINFO__PATH);
		// 根据用户Id查询用户卡户信息
		BankOpenAccount bankOpenAccount = this.appRechargeService.getBankOpenAccount(userId);
		if (bankOpenAccount != null && StringUtils.isNotEmpty(bankOpenAccount.getAccount())) {
			modelAndView.addObject("account", bankOpenAccount.getAccount());
		}
		// 用户信息
		Users users = this.appRechargeService.getUsers(userId);
		// 用户类型
		Integer userType = users.getUserType();

		// 根据用户Id获取用户信息
		UsersInfo usersInfo = this.appRechargeService.getUsersInfoByUserId(userId);
		String trueName = usersInfo.getTruename();
		// 如果是企业用户
		if (userType == 1) {
			// 根据用户ID查询企业用户账户信息
			CorpOpenAccountRecord record = this.appRechargeService.getCorpOpenAccountRecord(userId);
			trueName = record.getBusiName();
		}
		modelAndView.addObject("userName", trueName);
		LogUtil.endLog(THIS_CLASS, AppRechargeDefine.OFFLINERECHAGEINFO_ACTION, "获取线下充值信息结束");
		return modelAndView;
	}

	/**
	 * 
	 * app获取快捷充值卡及手续费的数据接口
	 * 
	 * @author liuyang
	 * @param vo
	 * @return
	 */
	@Deprecated
	@ResponseBody
	@RequestMapping(value = AppRechargeDefine.GET_QP_RECHARGE_INFO_ACTION)
	public AppRechargeInfoResultVo getQpRechargeInfo(AppRechargeVo vo) {
		AppRechargeInfoResultVo resultVo = new AppRechargeInfoResultVo(AppRechargeDefine.GET_QP_RECHARGE_INFO, HOST_URL + AppRechargeDefine.RECHARGE_RULE_URL, HOST_URL
				+ AppRechargeDefine.RECHARGE_OTHER_URL + "?sign=" + vo.getSign());

		// 版本号
		String version = vo.getVersion();
		// 获取key
		String key = SecretUtil.getKey(vo.getSign());
		if (StringUtils.isEmpty(key)) {
			resultVo.setStatus(CustomConstants.SIGN_ERROR);
			resultVo.setStatusDesc("获取数据加密秘钥失败");
		} else {
			// 验证order
			if (SecretUtil.checkOrder(key, vo.getToken(), vo.getRandomString(), vo.getOrder())) {
				// 获取用户编号
				Integer userId = SecretUtil.getUserId(vo.getSign());
				if (null != userId) {

					//add by xiashuqing 20171204 begin app改版2.1新增查询账户余额
					Account account = this.appRechargeService.getAccount(userId);
					if (account != null) {
						resultVo.setAvailableAmount(CommonUtils.formatAmount(account.getBankBalance()));
					}
					//add by xiashuqing 20171204 end app改版2.1新增查询账户余额

					// 获取用户的快捷卡信息
					BankCard bankCard = this.appRechargeService.selectBankCardByUserId(userId);
					if (null != bankCard) {
						resultVo.setStatus(CustomConstants.APP_STATUS_SUCCESS);
						resultVo.setStatusDesc(CustomConstants.APP_STATUS_DESC_SUCCESS);
						resultVo.setBank(StringUtils.isBlank(bankCard.getBank()) ? StringUtils.EMPTY : bankCard.getBank());
						// 银行卡号
						resultVo.setCardNo(bankCard.getCardNo());
						resultVo.setCardNo_info(BankCardUtil.getFormatCardNo(bankCard.getCardNo()));
						resultVo.setMobile(bankCard.getMobile());//成功充值手机号码
						// 银行代码
						resultVo.setCode("");
						Integer bankId = bankCard.getBankId();
						BanksConfig banksConfig = appRechargeService.getBanksConfigByBankId(bankId);
						if (banksConfig != null && StringUtils.isNotEmpty(banksConfig.getBankIcon())) {
							resultVo.setLogo(HOST_URL + banksConfig.getBankIcon());
						} else {
							resultVo.setLogo(HOST_URL + "/data/upfiles/filetemp/image/bank_log.png");
						}
						
						if(banksConfig !=null && StringUtils.isNotEmpty(banksConfig.getBankName())){
							resultVo.setBank(banksConfig.getBankName());
						}
						
						// 是否快捷卡
						if(banksConfig != null && banksConfig.getQuickPayment() == 1){
							resultVo.setIsDefault("2");
						}else {
							resultVo.setIsDefault("0");
						}
						
						if (!StringUtils.isEmpty(vo.getMoney())) {
							// 根据快捷卡计算充值手续费
							Long money;
							try {
								money = Long.parseLong(vo.getMoney());
							} catch (Exception e) {
								resultVo = new AppRechargeInfoResultVo(AppRechargeDefine.GET_QP_RECHARGE_INFO, HOST_URL + AppRechargeDefine.RECHARGE_RULE_URL, HOST_URL
										+ AppRechargeDefine.RECHARGE_OTHER_URL + "?sign=" + vo.getSign());
								resultVo.setStatusDesc("请输入有效的充值金额");
								return resultVo;
							}
							// 手续费
							BigDecimal fee = BigDecimal.ZERO;
							// 时间到账金额
							BigDecimal balance = new BigDecimal(money);
							resultVo.setBalance(CustomConstants.DF_FOR_VIEW.format(balance));
							resultVo.setFee(CustomConstants.DF_FOR_VIEW.format(fee));
							// 拼接展示信息字符串
							//String moneyInfo = AppRechargeDefine.FEE + CustomConstants.DF_FOR_VIEW.format(fee) + AppRechargeDefine.RECHARGE_INFO_SUFFIX + AppRechargeDefine.BALANCE
									//+ CustomConstants.DF_FOR_VIEW.format(balance) + AppRechargeDefine.RECHARGE_INFO_SUFFIX;
							if (banksConfig != null) {
								// 每次限额 单位：万元
								BigDecimal timesLimitAmount = banksConfig.getSingleQuota()
										.divide(new BigDecimal(AMOUNT_UNIT));
								// 每日限额 单位：万元
								BigDecimal dayLimitAmount = banksConfig.getSingleCardQuota()
										.divide(new BigDecimal(AMOUNT_UNIT));
								// 每月限额 单位: 万元
								BigDecimal monthLimitAmount = banksConfig.getMonthCardQuota().divide(new BigDecimal(AMOUNT_UNIT));
								/*// 是否支持快捷支付1:支持 2:不支持
								Integer quickPayment = banksConfig.getQuickPayment();*/
                                if (monthLimitAmount == null) {
                                    monthLimitAmount = BigDecimal.ZERO;
                                }
                                String symbol = ",";
                                String symBol2 = ",";
                                if(BigDecimal.ZERO.compareTo(dayLimitAmount)==0 && BigDecimal.ZERO.compareTo(monthLimitAmount)==0){
									symbol = "";
									symBol2 = "";
								}
								if(BigDecimal.ZERO.compareTo(monthLimitAmount)==0){
                                	symBol2 = "";
								}
								String moneyInfo = MessageFormat.format(CARD_DESC, (BigDecimal.ZERO.compareTo(timesLimitAmount) == 0)?"":"单笔 "+timesLimitAmount.toString() + "万元" + symbol,
										(BigDecimal.ZERO.compareTo(dayLimitAmount)==0)?"":"单日 " + dayLimitAmount.toString() + "万元" + symBol2,
										(BigDecimal.ZERO.compareTo(monthLimitAmount)==0)?"":"单月 " + monthLimitAmount.toString() + "万元");
								resultVo.setMoneyInfo(moneyInfo);
							}
						}
					} else {
						resultVo.setStatus(CustomConstants.APP_STATUS_SUCCESS);
						resultVo.setStatusDesc("未查询到用户快捷卡信息");
					}

					resultVo.setButtonWord("确认充值".concat(CommonUtils.formatAmount(version, vo.getMoney())).concat("元"));

					//设置线下充值信息
					this.setOffLineRechageInfo(resultVo, userId);

				} else {
					resultVo.setStatus(CustomConstants.TOKEN_ERROR);
					resultVo.setStatusDesc("用户认证失败");
				}
			} else {
				resultVo.setStatusDesc(AppRechargeDefine.GET_QP_RECHARGE_INFO);
				resultVo.setStatusDesc("数据验证失败");
			}
			//add by cwyang 20180629 增加app3.0.9的转账指南
			String transferUrl = PropUtils.getSystem("hyjf.app.recharge.guide.url");
			resultVo.setTransferGuideURL(HOST_URL + transferUrl);
		}
		/** 充值描述 */
		resultVo.setRcvAccountName(RCV_ACCOUNT_NAME);
		resultVo.setRcvAccount(RCV_ACCOUNT);
		resultVo.setRcvOpenBankName(RCV_OPEN_BANK_NAME);
		resultVo.setKindlyReminder(KINDLY_REMINDER);
		resultVo.setHints(IMPORTANT_HINTS);
		return resultVo;
	}
	
	public static void main(String[] args) {
		String mobile="18253678913";
		System.out.println(Validator.isMobile(mobile));
		//18253678913
	}

	/**
	 * 
	 * app获取快捷充值地址的数据接口 需要将请求参数拼接到地址上并带回
	 * 
	 * @author renxingchen
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppRechargeDefine.GET_RECHARGE_URL_ACTION)
	public Object getRechargeUrl(AppRechargeVo vo) {
		AppRechargeUrlResultVo resultVo = new AppRechargeUrlResultVo(AppRechargeDefine.GET_RECHARGE_URL);
		String mobile = "";
		String token = "";
		String order = "";
		
		// 校验数据并拼接回传地址
		if (Validator.isNull(vo.getMoney())||Validator.isNull(vo.getMobile())) {
			resultVo.setStatusDesc("请求参数非法");
		} else {// 拼接充值地址并返回
            mobile = strEncode(vo.getMobile());
            token = strEncode(vo.getToken());
            order = strEncode(vo.getOrder());
			StringBuffer sb = new StringBuffer(RECHARGE_URL);
			sb.append("version=").append(vo.getVersion()).append(CustomConstants.APP_PARM_AND).append("netStatus=").append(vo.getNetStatus()).append(CustomConstants.APP_PARM_AND).append("platform=")
					.append(vo.getPlatform()).append(CustomConstants.APP_PARM_AND).append("token=").append(token).append(CustomConstants.APP_PARM_AND).append("sign=")
					.append(vo.getSign()).append(CustomConstants.APP_PARM_AND).append("randomString=").append(vo.getRandomString()).append(CustomConstants.APP_PARM_AND).append("order=")
					.append(order).append(CustomConstants.APP_PARM_AND).append("platform=").append(strEncode(vo.getPlatform())).append(CustomConstants.APP_PARM_AND)
					.append("money=").append(vo.getMoney()).append(CustomConstants.APP_PARM_AND).append("cardNo=").append(vo.getCardNo()).append(CustomConstants.APP_PARM_AND).append("code=")
					.append(vo.getCode()).append("&isMencry=").append("2").append("&mobile=").append(mobile);
			
			resultVo.setRechargeUrl(sb.toString());
			logger.info("请求充值接口url："+sb.toString());
			resultVo.setStatus(CustomConstants.APP_STATUS_SUCCESS);
			resultVo.setStatusDesc(CustomConstants.APP_STATUS_DESC_SUCCESS);
		}
		return resultVo;
	}

	/**
	 * 
	 * app端用户充值
	 * 
	 * @author liuyang
	 * @param request
	 * @param response
	 * @return
	 */
	@Deprecated
	@RequestMapping(AppRechargeDefine.USER_RECHARGE_ACTION)
	public ModelAndView userRecharge(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, AppRechargeDefine.USER_RECHARGE_ACTION);
		ModelAndView modelAndView = new ModelAndView();
		String sign = request.getParameter("sign");
		Integer userId = SecretUtil.getUserId(sign); // 用户ID
		String transAmt = request.getParameter("money");// 交易金额
		String cardNo = request.getParameter("cardNo");// 开户银行代号
		String platform = request.getParameter("platform");// 终端类型
		String message = "";
		JSONObject checkResult;
		// 检查参数
		checkResult = checkParam(request, userId, transAmt);
		if (checkResult != null) {
			message = checkResult.getString("statusDesc");
			modelAndView = new ModelAndView(AppRechargeDefine.RECHARGE_ERROR);
			modelAndView.addObject(AppRechargeDefine.MESSAGE, message);
			return modelAndView;
		}
//		Users user = this.appRechargeService.getUsers(userId);
		// 取得用户在银行的客户号
		BankOpenAccount accountChinapnrTender = appRechargeService.getBankOpenAccount(userId);
		if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getAccount())) {
			message = "您还未开户，请开户后重新操作";
			modelAndView = new ModelAndView(AppRechargeDefine.RECHARGE_ERROR);
			modelAndView.addObject(AppRechargeDefine.MESSAGE, message);
			LogUtil.endLog(THIS_CLASS, AppRechargeDefine.USER_RECHARGE_ACTION, "[用户未开户]");
			return modelAndView;
		}
		
		modelAndView = new ModelAndView(AppRechargeDefine.RECHARGE_ONLINE_INIT_PATH);

		// 获取充值手机号码
		BankCard bankCard = this.appRechargeService.selectBankCardByUserId(userId);
		if(bankCard != null && bankCard.getMobile() != null){
			modelAndView.addObject("mobile", bankCard.getMobile());
		}
		modelAndView.addObject("sign", sign);
		modelAndView.addObject("platform", platform);
		modelAndView.addObject("cardNo", cardNo);
		modelAndView.addObject("money", transAmt);
		
		return modelAndView;
	}

	/**
	 * 用户充值后处理
	 *
	 * @param request
	 * @param bean
	 * @return
	 */
	@Deprecated
	@RequestMapping(AppRechargeDefine.RETURN_MAPPING)
	public ModelAndView rechargeReturn(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
		LogUtil.startLog(THIS_CLASS, AppRechargeDefine.RETURN_MAPPING, "[交易完成后,回调开始]");
		bean.convert();
		LogUtil.debugLog(THIS_CLASS, AppRechargeDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
		ModelAndView modelAndView = new ModelAndView();
		// 银行返回码
		String retCode = bean == null ? "" : bean.getRetCode();
		String ip = CustomUtil.getIpAddr(request);
		Map<String, String> params = new HashMap<String, String>();
		params.put("ip", ip);
		System.out.println("=================cwyang 充值完银行返回码: " + retCode);
		if (BankCallStatusConstant.RESPCODE_SUCCESS.equals(retCode)) {
			// 更新充值的相关信息
			JSONObject message = this.appRechargeService.handleRechargeInfo(bean, params);
			// 充值成功
			if (message != null && message.get("error").equals("0")) {
				modelAndView = new ModelAndView(AppRechargeDefine.RECHARGE_SUCCESS);
				modelAndView.addObject("money", CustomConstants.DF_FOR_VIEW.format(new BigDecimal(bean.getTxAmount())));
				modelAndView.addObject("balance", CustomConstants.DF_FOR_VIEW.format(new BigDecimal(bean.getTxAmount())));
			} else {
				// 充值失败
				modelAndView = new ModelAndView(AppRechargeDefine.RECHARGE_ERROR);
				modelAndView.addObject("message", message.get("data"));
			}
		} else {
			// 更新充值失败状态
			this.appRechargeService.handleRechargeInfo(bean, params);
			String error = this.appRechargeService.getBankRetMsg(retCode);
			modelAndView = new ModelAndView(AppRechargeDefine.RECHARGE_ERROR);
			modelAndView.addObject("message", error);
		}
		return modelAndView;
	}

	/**
	 * 用户充值后处理
	 *
	 * @param request
	 * @param bean
	 * @return
	 */
	@Deprecated
	@ResponseBody
	@RequestMapping(AppRechargeDefine.CALLBACK_MAPPING)
	public String rechargeCallback(HttpServletRequest request, @ModelAttribute BankCallBean bean) {
		LogUtil.startLog(THIS_CLASS, AppRechargeDefine.CALLBACK_MAPPING, "[交易完成后,回调开始]");
		bean.convert();
		LogUtil.debugLog(THIS_CLASS, AppRechargeDefine.CALLBACK_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
		BankCallResult result = new BankCallResult();
		String ip = CustomUtil.getIpAddr(request);
		Map<String, String> params = new HashMap<String, String>();
		params.put("ip", ip);
		// 更新充值的相关信息
		this.appRechargeService.handleRechargeInfo(bean, params);
		result.setStatus(true);
		return JSONObject.toJSONString(result, true);
	}

	/**
	 * 数据校验
	 * @param request
	 * @param userId
	 * @param transAmtStr
	 * @return
	 */
	private JSONObject checkParam(HttpServletRequest request, Integer userId, String transAmtStr) {
		// 判断用户是否被禁用
		Users users = this.appRechargeService.getUsers(userId);
		if (users == null || users.getStatus() == 1) {
			return jsonMessage("对不起,该用户已经被禁用。", "1");
		}
		if (users.getUserType() == 1) {
			return jsonMessage("对不起,企业用户只能通过线下充值。", "1");
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
		// 取得用户在银行的客户号
		BankOpenAccount accountChinapnrTender = this.appRechargeService.getBankOpenAccount(userId);
		if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getAccount())) {
			return jsonMessage("用户未开户，请开户后再充值。", "1");
		}
		return null;
	}

	/**
	 * 
	 * 获取提现规则H5页面
	 * 
	 * @author renxingchen
	 * @return
	 */
	@ResponseBody
	@RequestMapping(AppRechargeDefine.RECHARGE_RULE_ACTION)
	public AppRechargeDescResultBean rechargeRule() {
		logger.info("获取充值限额说明....");
		JSONArray jsonArray = CommonSoaUtils.getBanksList().getJSONArray("data");
		//logger.info("jsonArray....{}", jsonArray.toJSONString());

		AppRechargeDescResultBean result = new AppRechargeDescResultBean();
		if (jsonArray != null) {
			this.conventBanksConfigToResult(result, jsonArray.toJSONString());
		}
		return result;
	}

	/**
	 * 短信充值发送验证码
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppRechargeDefine.SENDCODE_ACTION)
	public AppRechargeInfoResultVo sendCode(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(THIS_CLASS, AppRechargeDefine.SENDCODE_ACTION);
		AppRechargeInfoResultVo result = new AppRechargeInfoResultVo(AppRechargeDefine.SENDCODE_ACTION_FULL);
		String sign = request.getParameter("sign");
		Integer userId = SecretUtil.getUserId(sign); // 用户ID
		String cardNo = request.getParameter("cardNo");// 开户银行代号
		String mobile = request.getParameter("mobile");// 用户的手机号
		String isMencry = request.getParameter("isMencry");
		
		String message = "系统参数错误";
		String status = CustomConstants.APP_STATUS_FAIL;
		JSONObject checkResult;
	
		// 检查参数
		if(userId == null || StringUtils.isBlank(mobile) || StringUtils.isBlank(sign) || StringUtils.isBlank(cardNo)){
			result.setStatusDesc(message);
			result.setStatus(status);
			return result;
		}
		
		// 取得加密用的Key
		if(!"1".equals(isMencry)){
			String key = SecretUtil.getKey(sign);
			if (Validator.isNull(key)) {
				result.setStatus("1");
				result.setStatusDesc("请求参数非法");
				return result;
			}

			// 解密
			mobile = DES.decodeValue(key, mobile);
			
		}
		
		if(!Validator.isMobile(mobile)){
			result.setStatusDesc(message);
			result.setStatus(status);
			return result;
		}
		
		// 判断用户是否被禁用
		Users users = this.appRechargeService.getUsers(userId);
		if (users == null || users.getStatus() == 1) {
			result.setStatusDesc("对不起,该用户已经被禁用。");
			result.setStatus(status);
			return result;
		}
		if (users.getUserType() == 1) {
			result.setStatusDesc("对不起,企业用户只能通过线下充值。");
			result.setStatus(status);
			return result;
		}
		// 用户银行卡号
		if (StringUtils.isBlank(cardNo)) {
			// 获取绑定的银行卡号
			BankCard bankCard = this.appRechargeService.selectBankCardByUserId(userId);
			cardNo = bankCard.getCardNo();
		} 
		
		// 调用短信发送接口
		BankCallBean mobileBean = this.appRechargeService.sendRechargeOnlineSms(userId, cardNo, mobile, BankCallConstant.CHANNEL_APP);
		
		if (Validator.isNull(mobileBean)) {
			message = "短信验证码发送失败，请稍后再试！";
			result.setStatusDesc(message);
			result.setStatus(status);
			return result;
		}
		
		// 短信发送返回结果码
		String retCode = mobileBean.getRetCode();
		if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)
				&& !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
			message = "短信验证码发送失败！";
			result.setStatusDesc(message);
			result.setStatus(status);
			return result;
		}
		if (Validator.isNull(mobileBean.getSrvAuthCode()) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
			message = "短信验证码发送失败！";
			result.setStatusDesc(message);
			result.setStatus(status);
			return result;
		}
		String smsSeq = mobileBean.getSmsSeq();
		message = "短信发送成功！";
		status = CustomConstants.APP_STATUS_SUCCESS;
		result.setStatusDesc(message);
		result.setSmsSeq(smsSeq);
		result.setStatus(status);
		return result;
	}

	/**
	 * app端用户充值
	 * @param request
	 * @param response
	 * @param sensorsDataBean
	 * @return
	 */
	@RequestMapping(AppRechargeDefine.RECHARGE_ONLINE_ACTION)
	@ResponseBody
	public AppRechargeInfoResultVo rechargeOnline(HttpServletRequest request, HttpServletResponse response, SensorsDataBean sensorsDataBean) {
		LogUtil.startLog(THIS_CLASS, AppRechargeDefine.RECHARGE_ONLINE_ACTION);
		AppRechargeInfoResultVo result = new AppRechargeInfoResultVo(AppRechargeDefine.RECHARGE_ONLINE_ACTION_FULL);
		
		String sign = request.getParameter("sign");
		Integer userId = SecretUtil.getUserId(sign); // 用户ID
		String username = SecretUtil.getUserName(sign);// 用户名
		String transAmt = request.getParameter("money");// 交易金额
		String cardNo = request.getParameter("cardNo");// 开户银行代号
		String platform = request.getParameter("platform");// 终端类型

		// modify by xiashuqing begin APP改版2.1
		//String smsCode = request.getParameter("smsCode");// 用户短信验证码
		String smsCode = request.getParameter("SMS");// 用户短信验证码
		logger.info("smsCode is:{}", smsCode);
		// modify by xiashuqing end APP改版2.1

		String mobile = request.getParameter("mobile");// 用户的手机号
		String isMencry = request.getParameter("isMencry");// 版本号
		String version = request.getParameter("version");
		String smsSeq = request.getParameter("smsSeq");
		// 网络状态
		String netStatus = request.getParameter("netStatus");
		// order
		String order = request.getParameter("order");
		// 路由代码
		String routeCode = request.getParameter("routeCode");
		// 随机字符串
		String randomString = request.getParameter("randomString");
		// token
		String token = request.getParameter("token");
		
		String message = "系统参数错误";
		String status = CustomConstants.APP_STATUS_FAIL;
		JSONObject checkResult;
		
		// 检查参数
		if(userId == null || StringUtils.isBlank(mobile) || StringUtils.isBlank(sign) || StringUtils.isBlank(cardNo)
				|| StringUtils.isBlank(transAmt) || StringUtils.isBlank(smsCode)){
			message = "系统参数错误！";
			result.setStatusDesc(message);
			result.setStatus(status);
			return result;
		}
		checkResult = checkParam(request, userId, transAmt);
		if (checkResult != null) {
			message = checkResult.getString("statusDesc");
			result.setStatusDesc(message);
			result.setStatus(status);
			return result;
		}
		
//		if(StringUtils.isBlank(smsSeq)){
//			message = "请填写正确的验证码";
//			result.setStatusDesc(message);
//			result.setStatus(status);
//			return result;
//		}
		
		// 取得加密用的Key
		if(!"1".equals(isMencry)){
			String key = SecretUtil.getKey(sign);
			if (Validator.isNull(key)) {
				message = "请求参数非法！";
				result.setStatusDesc(message);
				result.setStatus(status);
				return result;
			}

			// 解密
			mobile = DES.decodeValue(key, mobile);
			
		}
		
		if(!Validator.isMobile(mobile)){
			message = "手机号码不正确！";
			result.setStatusDesc(message);
			result.setStatus(status);
			return result;
		}
		// 短信序列号
		if(StringUtils.isBlank(smsSeq)){
			smsSeq = this.appRechargeService.selectBankSmsSeq(userId,BankCallMethodConstant.TXCODE_DIRECT_RECHARGE_ONLINE);
			if (StringUtils.isBlank(smsSeq)) {
				message = "短信序列号获取失败！";
				result.setStatusDesc(message);
				result.setStatus(status);
				return result;
			}
		}
		
//		Users user = this.appRechargeService.getUsers(userId);
		// 取得用户在银行的客户号
		BankOpenAccount accountChinapnrTender = appRechargeService.getBankOpenAccount(userId);
		if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getAccount())) {
			message = "您还未开户，请开户后重新操作";
			result.setStatusDesc(message);
			result.setStatus(status);
			return result;
		}
		// 用户的电子账户
		String chinapnrUsrcustidTender = accountChinapnrTender.getAccount();
		// 身份证号
		String certId = this.appRechargeService.getUserIdcard(userId);
		// 用户信息
		UsersInfo usersInfo = appRechargeService.getUsersInfoByUserId(userId);
		
		// 调用   2.3.4联机绑定卡到电子账户充值
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_DIRECT_RECHARGE_ONLINE);// 交易代码
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
		bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_APP); // 交易渠道
		
		bean.setAccountId(chinapnrUsrcustidTender); // 电子账号
		bean.setIdType(BankCallConstant.ID_TYPE_IDCARD); // 证件类型
		bean.setIdNo(certId); // 身份证号
		bean.setName(usersInfo.getTruename()); // 姓名
		bean.setMobile(mobile); // 手机号
		bean.setCardNo(cardNo); // 银行卡号
		bean.setTxAmount(CustomUtil.formatAmount(transAmt)); // 交易金额
		bean.setCurrency(BankCallConstant.CURRENCY_TYPE_RMB); // 交易币种
		
//		bean.setCallBackAdrress(""); // 充值结果通知地址,p2p使用
		bean.setSmsCode(smsCode); // 充值时短信验证,p2p使用
		bean.setSmsSeq(smsSeq);
		//充值时短信验证,p2p使用
		bean.setUserIP(GetCilentIP.getIpAddr(request));// 客户IP
		String logOrderId = GetOrderIdUtils.getOrderId2(userId);
		bean.setLogOrderId(logOrderId);// 订单号
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogUserName(username);
		bean.setLogRemark("短信充值");
		bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_MOBILE);// 银行请求详细url
		bean.setLogClient(Integer.parseInt(platform));// 充值平台
		
		// 插入充值记录
		int ret = this.appRechargeService.insertRechargeOnlineInfo(bean);


		if (ret > 0) {
			;
		}else{
			message = "请不要重复操作";
			result.setStatusDesc(message);
			result.setStatus(status);
			return result;
		}
		
		try {

			String ip = CustomUtil.getIpAddr(request);
			Map<String, String> params = new HashMap<String, String>();
			params.put("ip", ip);
			params.put("mobile", mobile);
			
			// 设置返回url
			StringBuffer sbUrl = new StringBuffer();

			BankCallBean bankCallBean = BankCallUtils.callApiBg(bean);
			if (Validator.isNull(bankCallBean)) {
				message = "接口系统参数异常!";
				result.setStatusDesc(message);
				result.setStatus(status);
				return result;
			}
			String retCode = StringUtils.isNotBlank(bankCallBean.getRetCode()) ? bankCallBean.getRetCode(): "";
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
				this.appRechargeService.handleRechargeOnlineInfo(bankCallBean, params);
				String error = this.appRechargeService.getBankRetMsg(retCode);
				result.setStatusDesc(error);
				result.setStatus(status);
				return result;
			}
			
			// 更新充值的相关信息
			JSONObject msg = this.appRechargeService.handleRechargeOnlineInfo(bankCallBean, params);
			// 充值成功
			if (msg != null && msg.get("error").equals("0")) {
				result.setStatusDesc("充值成功！");
				result.setStatus(CustomConstants.APP_STATUS_SUCCESS);
				
				// 设置返回url
				sbUrl = new StringBuffer();
				sbUrl.append(PropUtils.getSystem(CustomConstants.HYJF_WEB_URL));
				sbUrl.append(request.getContextPath());
				sbUrl.append(AppRechargeDefine.REQUEST_MAPPING);
				sbUrl.append(AppRechargeDefine.RECHARGE_RESULT_ACTION);
				sbUrl.append("?").append("version").append("=").append(version);
				sbUrl.append("&").append("netStatus").append("=").append(netStatus);
				sbUrl.append("&").append("platform").append("=").append(platform);
				sbUrl.append("&").append("randomString").append("=").append(randomString);
				sbUrl.append("&").append("sign").append("=").append(sign);
				if(token != null && order != null){
					sbUrl.append("&").append("token").append("=").append(strEncode(token));
					sbUrl.append("&").append("order").append("=").append(strEncode(order));
				}
				sbUrl.append("&").append("cardNo").append("=").append(cardNo);
				sbUrl.append("&").append("money").append("=").append(transAmt);
				sbUrl.append("&").append("nid").append("=").append(logOrderId);
				sbUrl.append("&").append("routeCode").append("=").append(routeCode);
				result.setRechargeUrl(sbUrl.toString());

				return result;
			} else {
				// 充值失败
				result.setStatusDesc(message);
				result.setStatus(status);
				return result;
			}
		} catch (Exception e) {
			message = ("系统异常");
			result.setStatusDesc(message);
			result.setStatus(status);
			return result;
		}
		
	}
	

	/**
	 * 充值成功跳转
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(AppRechargeDefine.RECHARGE_RESULT_ACTION)
	public ModelAndView rechargeInfo(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(AppWithdrawController.class.toString(), AppWithdrawDefine.CASH_MAPPING);
		ModelAndView modelAndView = new ModelAndView();
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
		// 路由代码
		String routeCode = request.getParameter("routeCode");
		String cardNo = request.getParameter("cardNo");
		String money = request.getParameter("money");
		String nid = request.getParameter("nid");

		// 检查参数
		if(StringUtils.isBlank(sign) || StringUtils.isBlank(platform) || StringUtils.isBlank(cardNo)
				|| StringUtils.isBlank(money) || StringUtils.isBlank(nid)){
			modelAndView = new ModelAndView(AppRechargeDefine.JUMP_HTML);
			BaseMapBean baseMapBean = new BaseMapBean();
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC,"系统参数错误！");
			baseMapBean.setCallBackAction(CustomConstants.HOST+AppRechargeDefine.RECHARGE_FAIL_HTML);
			modelAndView.addObject("callBackForm",baseMapBean);
			return modelAndView;
		}
		
		Integer userId = SecretUtil.getUserId(sign); // 用户ID
		String username = SecretUtil.getUserName(sign);// 用户名

		AccountRecharge accountRecharge = this.appRechargeService.selectRechargeInfo(userId, nid);
		if(accountRecharge == null || accountRecharge.getStatus()==3){
			modelAndView = new ModelAndView(AppRechargeDefine.JUMP_HTML);
			BaseMapBean baseMapBean = new BaseMapBean();
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC,"系统异常，请稍后重试！");
			baseMapBean.setCallBackAction(CustomConstants.HOST+AppRechargeDefine.RECHARGE_FAIL_HTML);
			modelAndView.addObject("callBackForm",baseMapBean);
			return modelAndView;
		}else if (accountRecharge.getStatus() == 2) {
			modelAndView = new ModelAndView(AppRechargeDefine.JUMP_HTML);
			BaseMapBean baseMapBean = new BaseMapBean();
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC,"充值成功！");
			baseMapBean.set("money",CustomConstants.DF_FOR_VIEW.format(accountRecharge.getMoney()));
			baseMapBean.setCallBackAction(CustomConstants.HOST+AppRechargeDefine.RECHARGE_SUCCESS_HTML);
			modelAndView.addObject("callBackForm",baseMapBean);
			return modelAndView;
		}else if (accountRecharge.getStatus()==1){
			modelAndView = new ModelAndView(AppRechargeDefine.JUMP_HTML);
			BaseMapBean baseMapBean = new BaseMapBean();
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC,"银行处理中");
			baseMapBean.setCallBackAction(CustomConstants.HOST+AppRechargeDefine.RECHARGE_HANDING_HTML);
			modelAndView.addObject("callBackForm",baseMapBean);
			return modelAndView;
		}else {
			modelAndView = new ModelAndView(AppRechargeDefine.JUMP_HTML);
			BaseMapBean baseMapBean = new BaseMapBean();
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			baseMapBean.set(CustomConstants.APP_STATUS_DESC,"系统异常，请稍后重试！");
			baseMapBean.setCallBackAction(CustomConstants.HOST+AppRechargeDefine.RECHARGE_FAIL_HTML);
			modelAndView.addObject("callBackForm",baseMapBean);
			return modelAndView;
		}
	}

	private void conventBanksConfigToResult(AppRechargeDescResultBean result, String jsonArrayStr) {
		List<BanksConfig> configs = JSON.parseArray(jsonArrayStr, BanksConfig.class);
		if (!CollectionUtils.isEmpty(configs)) {
			List<AppRechargeDescResultBean.RechargeLimitAmountDesc> list = result.getList();
			AppRechargeDescResultBean.RechargeLimitAmountDesc bean = null;
			for (BanksConfig config : configs) {
				bean = new AppRechargeDescResultBean.RechargeLimitAmountDesc();
				bean.setBankName(config.getBankName());
				bean.setOnce((BigDecimal.ZERO.compareTo(config.getSingleQuota())==0)?"不限":String.valueOf(config.getSingleQuota().divide(new BigDecimal(10000))) + "万");
				bean.setDay((BigDecimal.ZERO.compareTo(config.getSingleCardQuota())==0)?"不限":String.valueOf(config.getSingleCardQuota().divide(new BigDecimal(10000))) + "万");
                BigDecimal month = config.getMonthCardQuota()==null?BigDecimal.ZERO:config.getMonthCardQuota().divide(new BigDecimal(10000));
                bean.setMonth((BigDecimal.ZERO.compareTo(month)==0)?"不限":String.valueOf(month) + "万");
				list.add(bean);
			}
		}
	}

	/**
	 * 设置线下充值信息，包含 收款方户名，收款方账号，收款方开户行名，友情提示
	 * @param resultVo
	 * @param userId
	 */
	private void setOffLineRechageInfo(AppRechargeInfoResultVo resultVo, Integer userId) {
		resultVo.setRcvOpenBankName(RCV_OPEN_BANK_NAME);
		resultVo.setKindlyReminder(RECHARGE_KINDLY_REMINDER);

		BankOpenAccount bankOpenAccount = this.appRechargeService.getBankOpenAccount(userId);
		if (bankOpenAccount != null) {
			resultVo.setRcvAccount(bankOpenAccount.getAccount());
		}

		UsersInfo usersInfo = this.appRechargeService.getUsersInfoByUserId(userId);
		if (usersInfo != null) {
			resultVo.setRcvAccountName(usersInfo.getTruename());
		}
		// 用户信息
		Users users = this.appRechargeService.getUsers(userId);
		// 用户类型
		Integer userType = users.getUserType();
		// 如果是企业用户
		if (userType == 1) {
			// 根据用户ID查询企业用户账户信息
			CorpOpenAccountRecord record = this.appRechargeService.getCorpOpenAccountRecord(userId);
			resultVo.setRcvAccountName(record.getBusiName());
		}

	}
}
