package com.hyjf.wechat.controller.regist;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.http.HttpDeal;
import com.hyjf.common.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.security.utils.RSAJSPUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.SmsConfig;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.app.AppAdsCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.wechat.BaseResultBeanFrontEnd;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseMapBean;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.controller.login.LoginResultBean;
import com.hyjf.wechat.service.coupon.util.CouponCheckUtil;
import com.hyjf.wechat.service.login.LoginService;
import com.hyjf.wechat.service.regist.UserService;
import com.hyjf.wechat.util.ResultEnum;
import com.hyjf.wechat.util.SecretUtil;

/**
 * 微信用户注册controller
 * @author jijun
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月27日
 * @see 上午09：11
 */
@Controller
@RequestMapping(value = UserRegistDefine.REQUEST_MAPPING)
public class UserRegistController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(UserRegistController.class);

	public static final String SOA_COUPON_KEY = PropUtils.getSystem("release.coupon.accesskey");

	@Autowired
	private UserService userService;

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;//指定用smsProcesser类来初始化

	@Autowired
	private CouponCheckUtil couponCheckUtil;

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private LoginService loginService;

	/** 图片地址 */
	private static String HOST_URL = PropUtils.getSystem("file.domain.url");

	/**
	 * 注册
	 * update by jijun 2018/02/22
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = UserRegistDefine.REGIST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResultBean registAction(HttpServletRequest request, HttpServletResponse response) {

		UserRegistResultVo vo = new UserRegistResultVo();

		LogUtil.startLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.REGIST_ACTION);

		vo.setRequest(UserRegistDefine.REGIST_REQUEST);

		// 手机号
		String mobile = request.getParameter("mobile");
		// 验证码
		String verificationCode = request.getParameter("verificationCode");
		// 登录密码
		String password = request.getParameter("password");
		// 推荐人
		String reffer = request.getParameter("reffer");

		// 合规改造 add by huanghui 20181120 start
		/**
		 * 当前注册用的类型
		 * 1:普通用户;
		 * 2:企业用户;
		 * 根据前端传值来判定, 如果不传或者传值其他值 默认为普通用户
		 */
		// 前端拼接的 用户类型
		String urlUserType = request.getParameter("userType");

		Integer userType = 0;
		if ("2".equals(urlUserType)){
			userType = 1;
		}else {
			userType = 0;
		}
		// 合规改造 add by huanghui 20181120 end

		// 神策数据统计追加 add by liuyang 20180725 start
		// 神策数据统计的预置属性
		String presetProps = getStringFromStream(request);
		// 神策数据统计追加 add by liuyang 20180725 end
		logger.info("当前注册手机号: {}", mobile);
		//密码解密
		password = RSAJSPUtil.rsaToPassword(password);
		{
			if (!Validator.isNull(reffer)) {
				int count = userService.countUserByRecommendName(reffer);
				if (count == 0) {
					vo.setEnum(ResultEnum.ERROR_009);
					vo.setSuccessUrl("");
					return vo;
				}
			}
		}
		// 检查参数正确性
		if (Validator.isNull(mobile) || Validator.isNull(verificationCode)) {
			vo.setEnum(ResultEnum.PARAM);
			vo.setSuccessUrl("");
			return vo;
		}

		// 业务逻辑
		try {

			logger.info("当前注册手机号: {}", mobile);
			if (Validator.isNull(mobile)) {
				vo.setEnum(ResultEnum.ERROR_010);
				vo.setSuccessUrl("");
				return vo;
			}
			if (Validator.isNull(verificationCode)) {
				vo.setEnum(ResultEnum.ERROR_011);
				vo.setSuccessUrl("");
				return vo;
			}
			if (Validator.isNull(password)) {
				vo.setEnum(ResultEnum.ERROR_012);
				vo.setSuccessUrl("");
				return vo;
			}

			if (password.length() < 6 || password.length() > 16) {
				vo.setEnum(ResultEnum.ERROR_013);
				vo.setSuccessUrl("");
				return vo;
			}

			boolean hasNumber = false;

			for (int i = 0; i < password.length(); i++) {
				if (Validator.isNumber(password.substring(i, i + 1))) {
					hasNumber = true;
					break;
				}
			}
			if (!hasNumber) {
				vo.setEnum(ResultEnum.ERROR_014);
				vo.setSuccessUrl("");
				return vo;
			}

			String regEx = "^[a-zA-Z0-9]+$";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(password);
			if (!m.matches()) {
				vo.setEnum(ResultEnum.ERROR_015);
				vo.setSuccessUrl("");
				return vo;
			}
			if (!Validator.isMobile(mobile)) {
				vo.setEnum(ResultEnum.ERROR_016);
				vo.setSuccessUrl("");
				return vo;
			}
			{
				int cnt = userService.countUserByMobile(mobile);
				if (cnt > 0) {
					vo.setEnum(ResultEnum.ERROR_017);
					vo.setSuccessUrl("");
					return vo;
				}
			}
			{
				String verificationType = UserRegistDefine.PARAM_TPL_ZHUCE;
				int cnt = userService.updateCheckMobileCode(mobile, verificationCode, verificationType, UserRegistDefine.CKCODE_YIYAN, UserRegistDefine.CKCODE_USED);
				if (cnt == 0) {
					vo.setEnum(ResultEnum.ERROR_018);
					vo.setSuccessUrl("");
					return vo;
				}
			}
			// 注册
			Users user = new Users();
			String loginIp = CustomUtil.getIpAddr(request);
			int userId = userService.insertUserAction(mobile, password, verificationCode, reffer, loginIp, request,user, userType);

			if (userId != 0) {
				
				// 发送Ip同步信息
				sendIpInfo(userId, loginIp);
				
			    user.setUserId(userId);
				//完成注册同时完成登录返回sign值 add by jijun 2018/03/30
				LoginResultBean resultOfLogin=(LoginResultBean) this.proceedLoginAction(request,mobile,password);
				if(Validator.isNull(resultOfLogin.getSign())) {
					//sign为空时代表登录失败
					vo.setStatus(resultOfLogin.getStatus());
					vo.setStatusDesc(resultOfLogin.getStatusDesc());
					vo.setSuccessUrl("");
					return vo;
				}else {
					vo.setSign(resultOfLogin.getSign());
				}
				// 神策数据统计 add by liuyang 20180725 start
				// 注册成功后将用户ID返给前端
				vo.setUserId(String.valueOf(userId));
				if (StringUtils.isNotBlank(presetProps)) {
					logger.info("注册时预置属性:presetProps" + presetProps);
					try {
						SensorsDataBean sensorsDataBean = new SensorsDataBean();
						// 将json串转换成Bean
						Map<String, Object> sensorsDataMap = JSONObject.parseObject(presetProps, new TypeReference<Map<String, Object>>() {
						});
						sensorsDataBean.setPresetProps(sensorsDataMap);
						sensorsDataBean.setUserId(userId);
						// 发送神策数据统计MQ
						this.userService.sendSensorsDataMQ(sensorsDataBean);
						// add by liuyang 神策数据统计追加 登录成功后 将用户ID返回前端 20180717 end
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// 神策数据统计 add by liuyang 20180725 end
				String statusDesc = "注册成功";
				// 投之家用户注册送券活动
				String activityIdTzj = CustomConstants.REGIST_TZJ_ACTIVITY_ID;
//				System.out.println("APP注册送加息券，活动id：" + activityIdTzj);
				// 活动有效期校验
				String resultActivityTzj = couponCheckUtil.checkActivityIfAvailable(activityIdTzj);
				if (StringUtils.isEmpty(resultActivityTzj)) {
					// 投之家用户额外发两张加息券
					if(StringUtils.isNotEmpty(user.getReferrerUserName()) && user.getReferrerUserName().equals("touzhijia")){
						CommonParamBean paramBean = new CommonParamBean();
						paramBean.setUserId(String.valueOf(userId));
						paramBean.setCouponSource(2);
						paramBean.setCouponCode("PJ2958703");
						paramBean.setSendCount(2);
						paramBean.setActivityId(Integer.parseInt(activityIdTzj));
						paramBean.setRemark("投之家用户注册送加息券");
						paramBean.setSendFlg(0);
						// 发放两张加息券
						CommonSoaUtils.sendUserCouponNoRet(paramBean);

					}

				}

				// add by zhangjinpeng 注册送888元新手红包 start
				String activityId = CustomConstants.REGIST_888_ACTIVITY_ID;
				logger.info("送优惠券活动id :{}",activityId);
				// 活动有效期校验
				String resultActivity = couponCheckUtil.checkActivityIfAvailable(activityId);
				logger.info("活动有效期限：{}",resultActivity);
				if(StringUtils.isEmpty(resultActivity)){
//                    CommonParamBean paramBean = new CommonParamBean();
//                    paramBean.setUserId(user.getUserId().toString());
//                    paramBean.setSendFlg(11);
//                    // 发放888元新手红包
//                    CommonSoaUtils.sendUserCouponNoRet(paramBean);

					// 发放注册888红包
					try {
						sendCoupon(user);
					} catch (Exception e) {
						LogUtil.errorLog(this.getClass().getName(), "regist", "注册发放888红包失败", e);
					}

					Map<String, Object> paraMap = new HashMap<String, Object>();
					paraMap.put("limitStart", 0);
					paraMap.put("limitEnd", 1);
					paraMap.put("host", HOST_URL);
					paraMap.put("code", "registpop");
					List<AppAdsCustomize> recordList = userService.searchBannerList(paraMap);
					if(recordList != null && !recordList.isEmpty()){
						// 注册成功发券提示
						AppAdsCustomize record = recordList.get(0);
						String operationUrl = "://jumpCouponsList/?"; //record.getUrl() + "?couponStatus=0&sign=" + sign + "&platform" + platform;
//                        ret.put("imageUrl", record.getImage());
//                        ret.put("imageUrlOperation", operationUrl);
						BaseMapBean baseMapBean=new BaseMapBean();
						baseMapBean.set("imageUrl", record.getImage());
						baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
						baseMapBean.set(CustomConstants.APP_STATUS_DESC, URLEncoder.encode(statusDesc, "UTF-8"));
						baseMapBean.set("imageUrlOperation", operationUrl);
						baseMapBean.setCallBackAction(CustomConstants.HOST+UserRegistDefine.REGIST_RESULT_SUCCESS);

						vo.setEnum(ResultEnum.SUCCESS1);
						vo.setSuccessUrl(baseMapBean.getUrl());
						return vo;
					}

					// 发券成功
					// 发送短信通知
					user.setMobile(mobile);
					sendSmsCoupon(user);
				}else {
//                    ret.put("imageUrl", "");
//                    ret.put("imageUrlOperation", "");
					BaseMapBean baseMapBean=new BaseMapBean();
					baseMapBean.set("imageUrl", "");
					baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
					baseMapBean.set(CustomConstants.APP_STATUS_DESC, URLEncoder.encode(statusDesc, "UTF-8"));
					baseMapBean.set("imageUrlOperation", "");
					baseMapBean.setCallBackAction(CustomConstants.HOST+UserRegistDefine.REGIST_RESULT_SUCCESS);

					vo.setEnum(ResultEnum.SUCCESS1);
					vo.setSuccessUrl(baseMapBean.getUrl());
					return vo;

				}
				// add by zhangjinpeng 注册送888元新手红包 end
				BaseMapBean baseMapBean=new BaseMapBean();
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
				baseMapBean.set(CustomConstants.APP_STATUS_DESC, URLEncoder.encode(statusDesc, "UTF-8"));
				baseMapBean.setCallBackAction(CustomConstants.HOST+UserRegistDefine.REGIST_RESULT_SUCCESS);
				vo.setEnum(ResultEnum.SUCCESS1);
				vo.setSuccessUrl(baseMapBean.getUrl());
				return vo;

			} else {
				vo.setEnum(ResultEnum.ERROR_019);
				vo.setSuccessUrl("");
				return vo;
			}
		} catch (Exception e) {
			vo.setEnum(ResultEnum.ERROR_020);
			vo.setSuccessUrl("");
		}

		LogUtil.endLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.REGIST_ACTION);
		return vo;
	}



	/**
	 * 完成登录操作
	 *  add by jijun 2018/03/30
	 */
	private BaseResultBean proceedLoginAction(HttpServletRequest request, String userName, String password) {
		logger.info("执行登录操作开始,手机号为：【"+userName+"】");
		LoginResultBean result = new LoginResultBean();
		if(StringUtils.isBlank(userName)||StringUtils.isBlank(password)){
			return result.setEnum(ResultEnum.PARAM);
		}
		int userId = loginService.updateLoginInAction(userName, password, CustomUtil.getIpAddr(request));
		switch (userId) {
			case -1:
				result.setEnum(ResultEnum.ERROR_001);
				break;
			case -2:
				result.setEnum(ResultEnum.ERROR_002);
				break;
			case -3:
				result.setEnum(ResultEnum.ERROR_003);
				break;
			default:
				Users users = loginService.getUsers(userId);

				BankOpenAccount account = loginService.getBankOpenAccount(userId);
				String accountId = null;
				if(account!=null&&StringUtils.isNoneBlank(account.getAccount())){
					accountId = account.getAccount();
					/*********** 登录时自动同步线下充值记录 start ***********/
					if (users.getBankOpenAccount() == 1) {
						CommonSoaUtils.synBalance(users.getUserId());
					}
					/*********** 登录时自动同步线下充值记录 end ***********/
				}
				String sign = SecretUtil.createToken(userId, users.getUsername(), accountId);

				try {
					StringBuffer url = request.getRequestURL();
					String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
					String host = CustomConstants.WECHAT_HOST;
					logger.info("登录url为===="+tempContextUrl);
					logger.info("获取登录配置为==="+host);
					logger.info("登录配置和url是否相等==="+(tempContextUrl.equals(host)));
					String str[] = tempContextUrl.split(":");
					String str1[] = host.split(":");
					if(str.length > 1 && str1.length > 1){
						if (str[1].equals(str1[1])) {
							RedisUtils.del("loginFrom"+userId);
							RedisUtils.set("loginFrom"+userId, "2", 1800);
						}
					}
				} catch (Exception e) {
					logger.error("处理登陆来源异常！");
				}
				// 登录完成返回值
				result.setEnum(ResultEnum.SUCCESS);
				result.setSign(sign);
				break;
		}

		logger.info("执行登录操作结束,手机号为：【"+userName+"】");
		return result;
	}

	/**
	 * 注册888红包发放
	 * @param user
	 */
	private void sendCoupon(Users user) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("mqMsgId", GetCode.getRandomCode(10));
		params.put("userId", user.getUserId().toString());
		params.put("sendFlg", "11");

		String signValue = StringUtils.lowerCase(MD5.toMD5Code(SOA_COUPON_KEY + user.getUserId().toString() + 11 + SOA_COUPON_KEY));
		params.put("sign", signValue);

		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_COUPON_SEND, JSONObject.toJSONString(params));
	}
	
	/**
	 * 发送成功消息队列
	 * @param userid
	 * @param ip
	 */
	private void sendIpInfo(int userid, String ip) {

		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("mqMsgId", GetCode.getRandomCode(10));
			params.put("userId", String.valueOf(userid));
			params.put("regIp", ip);

			rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME,
					RabbitMQConstants.ROUTINGKEY_SYNC_USER_IP_USER, JSONObject.toJSONString(params));

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}


	/**
	 * 点击下一步验证
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = UserRegistDefine.NEXT_STEP_ACTION, method = RequestMethod.POST)
	public JSONObject nextStepAction(HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = new JSONObject();

		ret.put("status", "000");
		ret.put("statusDesc", "手机号校验通过");

		// 手机号
		String mobile = request.getParameter("mobile");
		if (Validator.isNull(mobile)) {
			ret.put("status", "99");
			ret.put("statusDesc", "手机号不能为空");
			return ret;
		}
		int cnt = userService.countUserByMobile(mobile);
		if (cnt > 0) {
			ret.put("status", "99");
			ret.put("statusDesc", "该手机号已经注册");
			return ret;
		}

		return ret;

	}

	/**
	 * 获取密码加密的公钥
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = UserRegistDefine.FIND_PUBLIC_KEY_ACTION, method = RequestMethod.POST)
	public JSONObject findPublicKeys(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pubexponent", "10001");
		jsonObject.put("pubmodules", RSAJSPUtil.getPunlicKeys());
		return jsonObject;
	}

	/**
	 * 发送验证码
	 * 	再发送短信验证码前增加无感知图形验证码验证.
	 * 	验证码验证通过再给用户发送短信验证码
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = UserRegistDefine.SEND_VERIFICATIONCODE_ACTION, method = RequestMethod.POST)
	public JSONObject sendVerificationCodeAction(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.SEND_VERIFICATIONCODE_ACTION);
		JSONObject ret = new JSONObject();
		ret.put("request", UserRegistDefine.SEND_VERIFICATIONCODE_REQUEST);

		// 无感知图形验证码验证开始  Add By huanghui 20181220
		// 注册页面类型(register : 注册页, 使用图形验证码; landingpage : 着陆页,使用无感知图形验证码.)
		String pageType = request.getParameter("pageType");

		// 手机号
		String mobile = request.getParameter("mobile");

		// 验证码类型
		String verificationType = request.getParameter("verificationType");
		logger.info("Wechat注册短信验证码:pageType:" + pageType + ";mobile:" + mobile + ";verificationType:" + verificationType);
		// 解密
		if (Validator.isNull(verificationType)) {
			ret.put("status", "99");
			ret.put("statusDesc", "验证码类型不能为空");
			return ret;
		}
		if (Validator.isNull(mobile)) {
			ret.put("status", "99");
			ret.put("statusDesc", "手机号不能为空");
			return ret;
		}
		if (!Validator.isMobile(mobile)) {
			ret.put("status", "99");
			ret.put("statusDesc", "请输入您的真实手机号码");
			return ret;
		}

		if (!(verificationType.equals(UserRegistDefine.PARAM_TPL_ZHUCE))) {
			// 仅 TPL_ZHUCE
			ret.put("status", "99");
			ret.put("statusDesc", "无效的验证码类型");
			return ret;
		}
		{
			int cnt = userService.countUserByMobile(mobile);
			if (verificationType.equals(UserRegistDefine.PARAM_TPL_ZHUCE)) {
				if (cnt > 0) {
					ret.put("status", "99");
					ret.put("statusDesc", "该手机号已经注册");
					return ret;
				}
			}
		}

		// 获取短信配置
		SmsConfig smsConfig = userService.getSmsConfig();

		try {
			// 短信加固
			validateSmsTemplate(mobile, smsConfig, request);
		}catch (Exception e){
			ret.put("status", "99");
			ret.put("statusDesc", e.getMessage());
			return ret;
		}

		// 发送短信 标识
		boolean success = false;
		if ("register".equals(pageType)){
			// 普通注册,使用普通验证码

			success = sendVerificationCodeAction(mobile, smsConfig, verificationType, request);
		}else if ("landingpage".equals(pageType)){
			// 着陆页注册, 使用无感知验证码

			// 拼装所需参数
			String aid = "2019172208";
			String AppSecretKey = "0XP-6snx24gPoyAuGz5_Now**";
			String ticket = request.getParameter("ticket");
			String randstr = request.getParameter("randstr");
			String userIp = GetCilentIP.getIpAddr(request);
			String verifyUrl = "https://ssl.captcha.qq.com/ticket/verify?aid=" + aid + "&AppSecretKey=" + AppSecretKey + "&Ticket=" + ticket + "&Randstr=" + randstr + "&UserIP=" + userIp;
			logger.info(mobile + "=>无感知验证码请求地址:" + verifyUrl);
			String backRes = HttpDeal.get(verifyUrl);
			logger.info(mobile + "=>无感知验证码请求返回:" + backRes);
			// 解析返回的JSON串
			JSONObject jsonObject = JSON.parseObject(backRes);
			String backResponse = (String) jsonObject.get("response");
			String backErrMsg = (String) jsonObject.get("err_msg");

			if ("1".equals(backResponse)){
				// 无感知图形验证码验证通过
				success = sendVerificationCodeAction(mobile, smsConfig, verificationType, request);
			}else {
				String getBackErrMsg = this.backErrMsg(backErrMsg);
				ret.put("status", "99");
				ret.put("statusDesc", "图形验证码:" + getBackErrMsg);
				return ret;
			}
		}

		if (success) {
			ret.put("status", "000");
			ret.put("statusDesc", "发送验证码成功");
		} else {
			ret.put("status", "99");
			ret.put("statusDesc", "发送验证码失败");
		}

		LogUtil.endLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.SEND_VERIFICATIONCODE_ACTION);
		return ret;

	}

	/**
	 * 匹配返回的错误信息
	 * @param errCode
	 * @return
	 */
	private String backErrMsg(String errCode){
		String backMsg = "";
		switch (errCode){
			case "OK":
				backMsg = "验证通过";
				break;
			case "user code len error":
				backMsg = "验证码长度不匹配";
				break;
			case "captcha no match":
				backMsg = "验证码答案不匹配/Randstr参数不匹配";
				break;
			case "verify timeout":
				backMsg = "验证码签名超时";
				break;
			case "Sequnce repeat":
				backMsg = "验证码签名重放";
				break;
			case "Sequnce invalid":
				backMsg = "验证码签名序列";
				break;
			case "Cookie invalid":
				backMsg = "验证码cookie信息不合法";
				break;
			case "verify ip no match":
				backMsg = "ip不匹配";
				break;
			case "decrypt fail":
				backMsg = "验证码签名解密失败";
				break;
			case "appid no match":
				backMsg = "验证码强校验appid错误";
				break;
			case "param err":
				backMsg = "AppSecretKey参数校验错误";
				break;
			case "cmd no match":
				backMsg = "验证码系统命令号不匹配";
				break;
			case "uin no match":
				backMsg = "号码不匹配";
				break;
			case "seq redirect":
				backMsg = "重定向验证";
				break;
			case "opt no vcode":
				backMsg = "操作使用pt免验证码校验错误";
				break;
			case "diff":
				backMsg = "差别，验证错误";
				break;
			case "captcha type not match":
				backMsg = "验证码类型与拉取时不一致";
				break;
			case "verify type error":
				backMsg = "验证类型错误";
				break;
			case "invalid pkg":
				backMsg = "非法请求包";
				break;
			case "bad visitor":
				backMsg = "策略拦截";
				break;
			case "system busy":
				backMsg = "系统内部错误";
				break;
			default:
				backMsg = "验证错误";
				break;
		}
		return backMsg;
	}

	/**
	 * 验证验证码
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = UserRegistDefine.VALIDATE_VERIFICATIONCODE_ACTION, method = RequestMethod.POST)
	public JSONObject validateVerificationCodeAction(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.VALIDATE_VERIFICATIONCODE_ACTION);
		JSONObject ret = new JSONObject();
		ret.put("request", UserRegistDefine.VALIDATE_VERIFICATIONCODE_REQUEST);

		// 验证方式
		String verificationType = request.getParameter("verificationType");
		// 验证码
		String verificationCode = request.getParameter("verificationCode");
		// 手机号
		String mobile = request.getParameter("mobile");

		if (Validator.isNull(verificationType)) {
			ret.put("status", "99");
			ret.put("statusDesc", "验证码类型不能为空");
			return ret;
		}
		if (Validator.isNull(verificationCode)) {
			ret.put("status", "99");
			ret.put("statusDesc", "验证码不能为空");
			return ret;
		}
		if (!(verificationType.equals(UserRegistDefine.PARAM_TPL_ZHUCE) || verificationType.equals(UserRegistDefine.PARAM_TPL_ZHAOHUIMIMA) || verificationType.equals(UserRegistDefine.PARAM_TPL_BDYSJH) || verificationType.equals(UserRegistDefine.PARAM_TPL_YZYSJH))) {
			ret.put("status", "99");
			ret.put("statusDesc", "无效的验证码类型");
			return ret;
		}

		// 业务逻辑
		try {
			if (Validator.isNull(mobile)) {
				ret.put("status", "99");
				ret.put("statusDesc", "手机号不能为空");
				return ret;
			}
			if (!Validator.isMobile(mobile)) {
				ret.put("status", "99");
				ret.put("statusDesc", "请输入您的真实手机号码");
				return ret;
			}

			int cnt = userService.updateCheckMobileCode(mobile, verificationCode, verificationType, UserRegistDefine.CKCODE_NEW, UserRegistDefine.CKCODE_YIYAN);

			if (cnt > 0) {
				ret.put("status", "000");
				ret.put("statusDesc", "验证验证码成功");
			} else {
				ret.put("status", "99");
				ret.put("statusDesc", "验证码输入错误");
			}

		} catch (Exception e) {
			ret.put("status", "99");
			ret.put("statusDesc", "验证验证码发生错误");
		}

		LogUtil.endLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.VALIDATE_VERIFICATIONCODE_ACTION);
		return ret;
	}

	/**
	 * 绑定新手机
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = UserRegistDefine.BIND_NEWPHONE_ACTION, method = RequestMethod.POST)
	public JSONObject bindNewPhoneAction(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.BIND_NEWPHONE_ACTION);
		JSONObject ret = new JSONObject();
		ret.put("request", UserRegistDefine.BIND_NEWPHONE_REQUEST);

		// 唯一标识
		String sign = request.getParameter("sign");

		// 江西银行业务码
		String bankCode = request.getParameter("bankCode");
		logger.info("江西银行业务码bankCode :{}", bankCode);

		if (Validator.isNull(bankCode)) {
			ret.put("status", "99");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		String failReturnUrl = CustomConstants.HOST + UserRegistDefine.SET_MOBILE_FAIL;
		String successReturnUrl = CustomConstants.HOST + UserRegistDefine.SET_MOBILE_SUCCESS;

		// 验证码
		String verificationCode = request.getParameter("newVerificationCode");
		// 手机号
		String mobile = request.getParameter("newMobile");
		logger.info("绑定新手机获取mobile :{}", mobile);
		// 取得加密用的Key
		String key = SecretUtil.getKey(sign);
		logger.info("取得加密用的key :{}", key);
		if (Validator.isNull(key)) {
			ret.put("status", "99");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		// 业务逻辑
		try {
			// 取得用户ID
			Integer userId = SecretUtil.getUserId(sign).getUserId();

			if (userId != null) {
				// 取得验证码
				logger.info("des解密后得到的mobile :{}", mobile);
				if (Validator.isNull(mobile)) {
					ret.put("status", "99");
					ret.put("statusDesc", "手机号不能为空");
					return ret;
				}
				if (Validator.isNull(verificationCode)) {
					ret.put("status", "99");
					ret.put("statusDesc", "验证码不能为空");
					return ret;
				}
				if (!Validator.isMobile(mobile)) {
					ret.put("status", "99");
					ret.put("statusDesc", "请输入您的真实手机号码");
					return ret;
				}
				{
					int cnt = userService.countUserByMobile(mobile);
					Users users = userService.getUserByUserId(userId);
					if (cnt > 0 && !users.getMobile().equals(mobile)) {
						ret.put("status", "99");
						ret.put("statusDesc", "该手机号已经注册");
						return ret;
					}
				}
				if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(bankCode) || StringUtils.isEmpty(verificationCode)) {
					ret.put("status", "99");
					ret.put("statusDesc", "请求参数非法");
					return ret;
				}

				BankOpenAccount bankOpenAccount = userService.getBankOpenAccount(userId);
				if (bankOpenAccount == null) {
					ret.put("status", "99");
					ret.put("statusDesc", "用户未开户");
					return ret;
				}

				// 调用电子账号手机号修改增强
				BankCallBean bean = new BankCallBean();
				bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
				bean.setTxCode(BankCallConstant.TXCODE_MOBILE_MODIFY_PLUS);// 消息类型 修改手机号增强 mobileModifyPlus
				bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
				bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
				bean.setTxDate(GetOrderIdUtils.getTxDate());
				bean.setTxTime(GetOrderIdUtils.getTxTime());
				bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
				bean.setChannel(BankCallConstant.CHANNEL_APP);
				bean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));//电子账号
				bean.setOption(BankCallConstant.OPTION_1);//修改
				bean.setMobile(mobile);// 新手机号
				bean.setLastSrvAuthCode(bankCode);//业务授权码
				bean.setSmsCode(verificationCode);//短信验证码
				// 商户私有域，存放开户平台,用户userId
				LogAcqResBean acqRes = new LogAcqResBean();
				acqRes.setUserId(userId);
				bean.setLogAcqResBean(acqRes);
				// 操作者ID
				bean.setLogUserId(String.valueOf(userId));
				bean.setLogOrderId(GetOrderIdUtils.getUsrId(userId));
				//返回参数
				BankCallBean retBean = null;
				try {
					//调用接口
					retBean = BankCallUtils.callApiBg(bean);
					LogUtil.startLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.BIND_NEWPHONE_ACTION);
				} catch (Exception e) {
					LogUtil.errorLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.BIND_NEWPHONE_ACTION, e);
					ret.put("status", "99");
					ret.put("statusDesc", "调用银行接口异常！");
					ret.put("successUrl", failReturnUrl + "?status=99&statusDesc=调用银行接口异常！");
					return ret;
				}
				if (retBean == null) {
					ret.put("status", "99");
					ret.put("statusDesc", "修改手机号失败，系统异常");
					ret.put("successUrl", failReturnUrl + "?status=99&statusDesc=修改手机号失败，系统异常！");
					return ret;
				}
				//返回失败
				if (!BankCallConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode())) {
					String errorMsg = retBean.getRestMsg();
					if(StringUtils.isBlank(errorMsg)){
						errorMsg = this.userService.getBankRetMsg(retBean.getRetCode());
					}
					if(StringUtils.isBlank(errorMsg)){
						errorMsg = "修改手机号失败...";
					}
					ret.put("status", "99");
					ret.put("statusDesc", errorMsg);
					ret.put("successUrl", failReturnUrl + "?status=99&statusDesc=" + errorMsg);
					return ret;
				}
				//修改手机号
				int result = this.userService.updateNewPhoneAction(userId, mobile);
				switch (result) {
					case 0:
						ret.put("status", "000");
						ret.put("statusDesc", "修改手机号成功");
						ret.put("mobile", mobile);
						ret.put("successUrl", successReturnUrl + "?status=000&statusDesc=" + "您已绑定手机号" + mobile.substring(0, 3).concat("****").concat(mobile.substring(7)));
						break;
					default:
						break;
				}
			} else {
				ret.put("status", "99");
				ret.put("statusDesc", "用户信息不存在");
				ret.put("successUrl", failReturnUrl + "?status=99&statusDesc=用户信息不存在!");
			}
		} catch (Exception e) {
			logger.error("绑定新手机发生错误...", e);
			ret.put("status", "99");
			ret.put("statusDesc", "绑定新手机发生错误");
			ret.put("successUrl", failReturnUrl + "?status=99&statusDesc=绑定新手机发生错误");
		}
//		}
		LogUtil.endLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.BIND_NEWPHONE_ACTION);
		return ret;
	}

	/**
	 * 用户退出登录
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = UserRegistDefine.LOGIN_OUT_ACTION, method = RequestMethod.POST)
	public JSONObject loginOutAction(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.LOGIN_OUT_ACTION);
		JSONObject ret = new JSONObject();
		ret.put("request", UserRegistDefine.LOGIN_OUT_REQUEST);

		// 版本号
		String version = request.getParameter("version");
		// 网络状态
		String netStatus = request.getParameter("netStatus");
		// 平台
		String platform = request.getParameter("platform");
		// 唯一标识
		String sign = request.getParameter("sign");
		// token
		String token = request.getParameter("token");
		// 随机字符串
		String randomString = request.getParameter("randomString");
		// Order
		String order = request.getParameter("order");

		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(sign) || Validator.isNull(token) || Validator.isNull(randomString) || Validator.isNull(order)) {
			ret.put("status", "99");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 取得加密用的Key
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			ret.put("status", "99");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 业务逻辑
		try {
			// 取得用户ID
			Integer userId = SecretUtil.getUserId(sign).getUserId();

			if (userId != null) {
				clearMobileCode(userId,sign);

				// 移除sign
				SecretUtil.clearToken(sign);


				ret.put("status", "000");
				ret.put("statusDesc", "退出登录成功");
			} else {
				ret.put("status", "99");
				ret.put("statusDesc", "用户信息不存在");
			}

		} catch (Exception e) {
			ret.put("status", "99");
			ret.put("statusDesc", "退出登录发生错误");
		}

		LogUtil.endLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.LOGIN_OUT_ACTION);
		return ret;
	}

	private void clearMobileCode(Integer userId, String sign) {
		userService.clearMobileCode(userId,sign);

	}

	// =======================验证方法===========================
	/**
	 * 短信发送时的加固验证
	 *
	 * @param request
	 * @param result
	 */
	private void validateSmsTemplate(String mobile, SmsConfig smsConfig, HttpServletRequest request) throws Exception {
		String ip = CustomUtil.getIpAddr(request);
		String ipCount = RedisUtils.get(ip + ":MaxIpCount");
		if (StringUtils.isBlank(ipCount)) {
			ipCount = "0";
			RedisUtils.set(ip + ":MaxIpCount", "0");
		}
//		System.out.println(mobile + "------ip---" + ip + "----------MaxIpCount-----------" + ipCount);
		if (Integer.valueOf(ipCount) >= smsConfig.getMaxIpCount()) {
			if (Integer.valueOf(ipCount).equals(smsConfig.getMaxIpCount())) {
				try {
					userService.sendSms(mobile, "IP访问次数超限:" + ip);
				} catch (Exception e) {
				}
				RedisUtils.set(ip + ":MaxIpCount", (Integer.valueOf(ipCount) + 1) + "", 24 * 60 * 60);
			}
			try {
				userService.sendEmail(mobile, "IP访问次数超限" + ip);
			} catch (Exception e) {
			}
			throw new Exception("IP访问次数超限");
		}

		// 判断最大发送数max_phone_count
		String count = RedisUtils.get(mobile + ":MaxPhoneCount");
		if (StringUtils.isBlank(count)) {
			count = "0";
			RedisUtils.set(mobile + ":MaxPhoneCount", "0");
		}
		int maxPhoneCount = smsConfig.getMaxPhoneCount();
		;// 测试时临时是30,正式上线改为smsConfig.getMaxPhoneCount();
		if (Integer.valueOf(count) >= maxPhoneCount) {
			if (Integer.valueOf(count) == maxPhoneCount) {
				try {
					userService.sendSms(mobile, "手机发送次数超限");
				} catch (Exception e) {
				}
				RedisUtils.set(mobile + ":MaxPhoneCount", (Integer.valueOf(count) + 1) + "", 24 * 60 * 60);
			}
			try {
				userService.sendEmail(mobile, "手机发送次数超限");
			} catch (Exception e) {
			}
			throw new Exception("手机发送次数超限");
		}
		// 判断发送间隔时间
		String intervalTime = RedisUtils.get(mobile + ":IntervalTime");
		if (StringUtils.isNotBlank(intervalTime)) {
			throw new Exception("验证码发送过于频繁");
		}
	}


	/**
	 * 发送短信验证码 update by jijun 2018/02/26
	 * @return
	 */
	private Boolean sendVerificationCodeAction(String mobile, SmsConfig smsConfig, String verificationType, HttpServletRequest request) {
		// 生成验证码
		String checkCode = GetCode.getRandomSMSCode(6);
		Map<String, String> param = new HashMap<String, String>();
		param.put("val_code", checkCode);
		// 发送短信验证码
		SmsMessage smsMessage =
				new SmsMessage(null, param, mobile, null, MessageDefine.SMSSENDFORMOBILE, null,
						verificationType, CustomConstants.CHANNEL_TYPE_NORMAL);
		Integer result = (smsProcesser.gather(smsMessage) >= 1) ? 0 : 1;


		// checkCode过期时间，默认120秒
		RedisUtils.set(mobile + ":MaxValidTime", checkCode, smsConfig.getMaxValidTime() == null ? 120 : smsConfig.getMaxValidTime() * 60);

		// 发送checkCode最大时间间隔，默认60秒
		RedisUtils.set(mobile + ":IntervalTime", mobile, smsConfig.getMaxIntervalTime() == null ? 60 : smsConfig.getMaxIntervalTime());

		// 短信发送成功后处理 临时注释掉
		if (result != null && result == 0) {
			// 累加IP次数
			String ip = CustomUtil.getIpAddr(request);
			String currentMaxIpCount = RedisUtils.get(ip + ":MaxIpCount");
			if (StringUtils.isBlank(currentMaxIpCount)) {
				currentMaxIpCount = "0";
			}
			// 累加手机次数
			String currentMaxPhoneCount = RedisUtils.get(mobile + ":MaxPhoneCount");
			if (StringUtils.isBlank(currentMaxPhoneCount)) {
				currentMaxPhoneCount = "0";
			}
			RedisUtils.set(ip + ":MaxIpCount", (Integer.valueOf(currentMaxIpCount) + 1) + "", 24 * 60 * 60);
			RedisUtils.set(mobile + ":MaxPhoneCount", (Integer.valueOf(currentMaxPhoneCount) + 1) + "", 24 * 60 * 60);
		}

		// 保存短信验证码
		userService.saveSmsCode(mobile, checkCode, verificationType, result);
		return true;
	}

	/**
	 * 发送短信(注册送188元新手红包)
	 *
	 * @param userId
	 */
	private void sendSmsCoupon(Users users) {
		if (users == null || Validator.isNull(users.getUserId())) {
			return;
		}
		SmsMessage smsMessage = new SmsMessage(users.getUserId(), null, users.getMobile(), null, MessageDefine.SMSSENDFORMOBILE, null,
				CustomConstants.PARAM_TPL_TZJ_188HB, CustomConstants.CHANNEL_TYPE_NORMAL);
		smsProcesser.gather(smsMessage);

	}

	/**
	 * 初始化 注册-着陆页
	 * add by jijun 2018/03/28
	 * @param args
	 */
	@ResponseBody
	@RequestMapping(value = UserRegistDefine.INIT_REGIST_LANDING_PAGE_ACTION, method = RequestMethod.GET)
	public JSONObject initRegistLandingPageAction(HttpServletRequest request, HttpServletResponse response,UserParameters form) {
		LogUtil.startLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.INIT_REGIST_LANDING_PAGE_ACTION);

		JSONObject ret = new JSONObject();
		ret.put("request", UserRegistDefine.LOGIN_OUT_REQUEST);
		//校验渠道号
		if(Validator.isNull(form.getPage_id())){
			ret.put("status", "99");
			ret.put("statusDesc", "渠道号获取失败");
			return ret;
		}
		//获取着陆页数据
		JSONObject resultJson=userService.getRegistLandingPageData(form);

		LogUtil.endLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.INIT_REGIST_LANDING_PAGE_ACTION);

		return resultJson;
	}


	/**
	 * 生成图片验证码 add by jijun 20180402
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = UserRegistDefine.REGIST_RANDOMCODE, method = RequestMethod.GET)
	public void randomCode(HttpServletRequest request, HttpServletResponse response) {
		RandomValidateCode randomValidateCode = new RandomValidateCode();
		try {
			randomValidateCode.getRandcode(request, response);// 输出图片方法
		} catch (Exception e) {
			LogUtil.errorLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.INIT_REGIST_ACTION, e);
		}
	}



	/**
	 * 检查图片验证码,返回json验证结果 add by jijun 2018/04/02
	 * @param request
	 * @param response
	 */
	public JSONObject checkcaptchajson(HttpServletRequest request, HttpServletResponse response) {
		RandomValidateCode randomValidateCode = new RandomValidateCode();
		JSONObject ret = new JSONObject();
		if(Validator.isNull(request.getParameter("newRegVerifyCode"))) {
			ret.put("status", "99");
			ret.put("statusDesc", "图形验证码为空!");
			return ret;
		}
		if (randomValidateCode.checkRandomCode(request, request.getParameter("newRegVerifyCode"))) {
			ret.put("status", "000");
			ret.put("statusDesc", "图形验证码验证成功");
			return ret;
		} else {
			ret.put("status", "99");
			ret.put("statusDesc", "图形验证码不正确!");
			return ret;
		}
	}


	/**
	 * 注册着陆页提交 add by jijun 2018/04/02
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = UserRegistDefine.REGIST_LANDING_PAGE_COMMIT_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public JSONObject registLandingPageCommitAction(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.REGIST_ACTION);

		JSONObject ret = new JSONObject();


		// 神策数据统计追加 add by liuyang 20181105 start
		// 神策数据统计的预置属性
		String presetProps = getStringFromStream(request);
		// 神策数据统计追加 add by liuyang 20181105 end

		// 手机号
		String mobile = request.getParameter("mobile");
		logger.info("当前注册手机号: {}", mobile);
		if (Validator.isNull(mobile)) {
			ret.put("status", "99");
			ret.put("statusDesc", "手机号为空!");
			return ret;
		}
		if (!Validator.isMobile(mobile)) {
			ret.put("status", "99");
			ret.put("statusDesc", "手机号格式不正确!");
			return ret;
		}
		//查看当前手机号是否已经注册
		int countOfMobile = userService.countUserByMobile(mobile);
		if (countOfMobile > 0) {
			ret.put("status", "99");
			ret.put("statusDesc", "该手机号已经注册!");
			return ret;
		}

		// 登录密码校验
		String password = request.getParameter("password");
		//密码解密
		password = RSAJSPUtil.rsaToPassword(password);
		if (Validator.isNull(password)) {
			//密码不能为空
			ret.put("status", "99");
			ret.put("statusDesc", "密码不能为空!");
			return ret;
		}

		if (password.length() < 6 || password.length() > 16) {
			ret.put("status", "99");
			ret.put("statusDesc", "密码长度6-16位!");
			return ret;
		}

		boolean hasNumber = false;

		for (int i = 0; i < password.length(); i++) {
			if (Validator.isNumber(password.substring(i, i + 1))) {
				hasNumber = true;
				break;
			}
		}
		if (!hasNumber) {
			ret.put("status", "99");
			ret.put("statusDesc", "密码必须包含数字!");
			return ret;
		}

		String regEx = "^[a-zA-Z0-9]+$";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(password);
		if (!m.matches()) {
			//密码必须由数字和字母组成，如abc123
			ret.put("status", "99");
			ret.put("statusDesc", "密码必须由数字和字母组成，如abc123!");
			return ret;
		}

		//验证图片验证码
//		JSONObject captchaCheckResult =this.checkcaptchajson(request, response);
//		if(!"000".equals(captchaCheckResult.get("status"))) {
//			//不等于000 代表校验不通过
//			return captchaCheckResult;
//		}
		//验证短信验证码
		JSONObject verificationCodeCheckResult=this.validateVerificationCodeAction(request, response);
		if(!"000".equals(verificationCodeCheckResult.get("status"))) {
			//不等于000 代表校验不通过
			return verificationCodeCheckResult;
		}

		String verificationType = UserRegistDefine.PARAM_TPL_ZHUCE;
		int cnt = userService.updateCheckMobileCode(mobile, request.getParameter("verificationCode"), verificationType, UserRegistDefine.CKCODE_YIYAN, UserRegistDefine.CKCODE_USED);
		if (cnt == 0) {
			ret.put("status", "99");
			ret.put("statusDesc", "验证码无效!");
			return ret;
		}
		//推荐人userId
		String refferUserId=request.getParameter("refferUserId");
		Users user = new Users();
		logger.info("utmId: {}", request.getParameter("utmId"));
		logger.info("refferUserId: {}", request.getParameter("refferUserId"));
		logger.info("utmSource: {}", request.getParameter("utmSource"));
		logger.info("verificationCode: {}", request.getParameter("verificationCode"));
		
		String loginIp = CustomUtil.getIpAddr(request);
//		int userId = userService.insertUserAction(mobile, password, , refferUserId, CustomUtil.getIpAddr(request), request,user);
		int userId = userService.insertUserActionUtm(mobile, password,request.getParameter("verificationCode"), refferUserId, loginIp, 
				CustomConstants.CLIENT_WECHAT,request.getParameter("utmId"),request.getParameter("utmSource"), request, response,user);
		
		try {
			if (userId != 0) {
				
				// 发送Ip同步信息
				sendIpInfo(userId, loginIp);
				
			    user.setUserId(userId);
				//完成注册同时完成登录返回sign值 add by jijun 2018/03/30
				LoginResultBean resultOfLogin=(LoginResultBean) this.proceedLoginAction(request,mobile,password);
				if(Validator.isNull(resultOfLogin.getSign())) {
					//sign为空时代表登录失败
					ret.put("status", resultOfLogin.getStatus());
					ret.put("statusDesc", resultOfLogin.getStatusDesc());
					return ret;
				}else {
					ret.put("sign",resultOfLogin.getSign());
				}

				String statusDesc = "注册成功";

				// 神策数据统计 add by liuyang 20180725 start
				// 注册成功后将用户ID返给前端
				ret.put("userId",String.valueOf(userId));
				if (StringUtils.isNotBlank(presetProps)) {
					logger.info("注册时预置属性:presetProps" + presetProps);
					try {
						SensorsDataBean sensorsDataBean = new SensorsDataBean();
						// 将json串转换成Bean
						Map<String, Object> sensorsDataMap = JSONObject.parseObject(presetProps, new TypeReference<Map<String, Object>>() {
						});
						sensorsDataBean.setPresetProps(sensorsDataMap);
						sensorsDataBean.setUserId(userId);
						// 发送神策数据统计MQ E
						this.userService.sendSensorsDataMQ(sensorsDataBean);
						// add by liuyang 神策数据统计追加 登录成功后 将用户ID返回前端 20180717 end
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// 神策数据统计 add by liuyang 20180725 end

				// 投之家用户注册送券活动
				String activityIdTzj = CustomConstants.REGIST_TZJ_ACTIVITY_ID;
//				System.out.println("APP注册送加息券，活动id：" + activityIdTzj);
				// 活动有效期校验
				String resultActivityTzj = couponCheckUtil.checkActivityIfAvailable(activityIdTzj);
				if (StringUtils.isEmpty(resultActivityTzj)) {
					// 投之家用户额外发两张加息券
					if(StringUtils.isNotEmpty(user.getReferrerUserName()) && user.getReferrerUserName().equals("touzhijia")){
						CommonParamBean paramBean = new CommonParamBean();
						paramBean.setUserId(String.valueOf(userId));
						paramBean.setCouponSource(2);
						paramBean.setCouponCode("PJ2958703");
						paramBean.setSendCount(2);
						paramBean.setActivityId(Integer.parseInt(activityIdTzj));
						paramBean.setRemark("投之家用户注册送加息券");
						paramBean.setSendFlg(0);
						// 发放两张加息券
						CommonSoaUtils.sendUserCouponNoRet(paramBean);

					}

				}

				// add by zhangjinpeng 注册送888元新手红包 start
				String activityId = CustomConstants.REGIST_888_ACTIVITY_ID;
				// 活动有效期校验
				String resultActivity = couponCheckUtil.checkActivityIfAvailable(activityId);
				if(StringUtils.isEmpty(resultActivity)){
//                CommonParamBean paramBean = new CommonParamBean();
//                paramBean.setUserId(user.getUserId().toString());
//                paramBean.setSendFlg(11);
//                // 发放888元新手红包
//                CommonSoaUtils.sendUserCouponNoRet(paramBean);

					// 发放注册888红包
					try {
						sendCoupon(user);
					} catch (Exception e) {
						LogUtil.errorLog(this.getClass().getName(), "regist", "注册发放888红包失败", e);
					}

					Map<String, Object> paraMap = new HashMap<String, Object>();
					paraMap.put("limitStart", 0);
					paraMap.put("limitEnd", 1);
					paraMap.put("host", HOST_URL);
					paraMap.put("code", "registpop");
					List<AppAdsCustomize> recordList = userService.searchBannerList(paraMap);
					if(recordList != null && !recordList.isEmpty()){
						// 注册成功发券提示
						AppAdsCustomize record = recordList.get(0);
						String operationUrl = "://jumpCouponsList/?"; //record.getUrl() + "?couponStatus=0&sign=" + sign + "&platform" + platform;
//                    ret.put("imageUrl", record.getImage());
//                    ret.put("imageUrlOperation", operationUrl);
						BaseMapBean baseMapBean=new BaseMapBean();
						baseMapBean.set("imageUrl", record.getImage());
						baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
						baseMapBean.set(CustomConstants.APP_STATUS_DESC, URLEncoder.encode(statusDesc, "UTF-8"));
						baseMapBean.set("imageUrlOperation", operationUrl);
						baseMapBean.setCallBackAction(CustomConstants.HOST+UserRegistDefine.REGIST_RESULT_SUCCESS);

						ret.put("status", "000");
						ret.put("statusDesc", "注册成功");
						ret.put("successUrl", baseMapBean.getUrl());
						return ret;
					}

					// 发券成功
					// 发送短信通知
					user.setMobile(mobile);
					sendSmsCoupon(user);
				}else {
//                ret.put("imageUrl", "");
//                ret.put("imageUrlOperation", "");
					BaseMapBean baseMapBean=new BaseMapBean();
					baseMapBean.set("imageUrl", "");
					baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
					baseMapBean.set(CustomConstants.APP_STATUS_DESC, URLEncoder.encode(statusDesc, "UTF-8"));
					baseMapBean.set("imageUrlOperation", "");
					baseMapBean.setCallBackAction(CustomConstants.HOST+UserRegistDefine.REGIST_RESULT_SUCCESS);

					ret.put("status", "000");
					ret.put("statusDesc", "注册成功");
					ret.put("successUrl",baseMapBean.getUrl());
					return ret;
				}
				// add by zhangjinpeng 注册送888元新手红包 end
				BaseMapBean baseMapBean=new BaseMapBean();
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
				baseMapBean.set(CustomConstants.APP_STATUS_DESC, URLEncoder.encode(statusDesc, "UTF-8"));
				baseMapBean.setCallBackAction(CustomConstants.HOST+UserRegistDefine.REGIST_RESULT_SUCCESS);
				ret.put("status", "000");
				ret.put("statusDesc", "注册成功");
				ret.put("successUrl",baseMapBean.getUrl());
				return ret;
			} else {
				//注册失败,参数异常
				ret.put("status", "99");
				ret.put("statusDesc", "注册失败,参数异常");
				ret.put("successUrl","");
				return ret;
			}
		} catch (Exception e) {
		    e.printStackTrace();
			ret.put("status", "99");
			ret.put("statusDesc", "注册发生错误,参数异常");
			ret.put("successUrl", "");
		}

		LogUtil.endLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.REGIST_ACTION);
		return ret;

	}

	/**
	 * 从payload里面取神策预置属性,为解决从request里面取乱码的问题
	 *
	 * @param req
	 * @return
	 */
	private String getStringFromStream(HttpServletRequest req) {
		ServletInputStream is;
		try {
			is = req.getInputStream();
			int nRead = 1;
			int nTotalRead = 0;
			byte[] bytes = new byte[10240];
			while (nRead > 0) {
				nRead = is.read(bytes, nTotalRead, bytes.length - nTotalRead);
				if (nRead > 0)
					nTotalRead = nTotalRead + nRead;
			}
			String str = new String(bytes, 0, nTotalRead, "utf-8");
			return str;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	/*public static void main(String[] args) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
		m = p.matcher("17090000000");
		b = m.matches();
		System.out.println(b);
		m = p.matcher("13990000000");
		b = m.matches();
		System.out.println(b);
	}*/
}
