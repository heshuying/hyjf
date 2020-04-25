package com.hyjf.web.user.regist;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.enums.utils.ProtocolEnum;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.security.utils.RSAJSPUtil;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.ProtocolTemplate;
import com.hyjf.mybatis.model.auto.SmsConfig;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.activity.activity68.Activity68Controller;
import com.hyjf.web.agreement.AgreementService;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.coupon.util.CouponCheckUtil;
import com.hyjf.web.user.login.LoginDefine;
import com.hyjf.web.user.login.LoginService;
import com.hyjf.web.util.TreeDESUtils;
import com.hyjf.web.util.WebUtils;
import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller("userRegistController")
@RequestMapping(value = UserRegistDefine.REQUEST_MAPPING)
public class UserRegistController extends BaseController {
	
	public static final String SOA_COUPON_KEY = PropUtils.getSystem("release.coupon.accesskey");
	public static final boolean ENV_TEST = "true".equals(PropUtils.getSystem("hyjf.env.test")) ? true : false;
	public static final Logger _log = LoggerFactory.getLogger(UserRegistController.class);

	@Autowired
	private UserRegistService registService;

	@Autowired
	private LoginService loginService;

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	private Activity68Controller activity68Controller;

	@Autowired
	private CouponCheckUtil couponCheckUtil;
	
	@Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

	@Autowired
	private AgreementService agreementService;

	/**
	 * 初期化,跳转到注册页面
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = UserRegistDefine.INIT, method = RequestMethod.GET)
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(UserRegistDefine.class.getName(), UserRegistDefine.INIT);

		if (StringUtils.isNotBlank(request.getParameter("from"))){
			// 带有推荐人信息的url, 需从pc的注册页重定向到pc的着陆页
			response.sendRedirect(PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+"/landingpage/init.do?refferUserId="+request.getParameter("from"));
			// 浏览器第一次打开造成的 IllegalStateException 异常
			return null;
		}

		ModelAndView modelAndView = new ModelAndView(UserRegistDefine.INIT_PATH);

		if (RedisUtils.get(UserRegistDefine.CONTROLLOR_CLASS_NAME + "_registvalidCode_" + request.getSession().getId()) == null) {
			RedisUtils.set(UserRegistDefine.CONTROLLOR_CLASS_NAME + "_registvalidCode_" + request.getSession().getId(), "1", 10 * 60);
			// 不显示验证码
			modelAndView.addObject("hasValid", false);
		} else {
			// 显示验证码
			modelAndView.addObject("hasValid", true);
		}
		String referer = request.getHeader("Referer");
		if (null != referer && referer.contains("activity/activity68/getUserStatus")) {
			modelAndView.addObject("activity68", "1");
		}

		// 合规审批 add by huanghui 20181122 start
		String urlUserType = request.getParameter("userType");
		if (StringUtils.isEmpty(urlUserType)){
			modelAndView.addObject("userType", "1");
		}else {
			modelAndView.addObject("userType", urlUserType);
		}
		// 合规审批 add by huanghui 20181122 end
		// 后台渠道统计 删除 liuyang 20170602 start
//		// 渠道 网贷之家
//		if (null != request.getSession().getAttribute("utm_id")) {
//			String utmId = request.getSession().getAttribute("utm_id").toString();
//			if ("2122".equals(utmId)) {
//				modeAndView.addObject("utm_id", utmId);
//				modeAndView.addObject("utm_source", "网贷之家");
//			}
//		}
		// 后台渠道统计 删除 liuyang 20170602 end
		// 如果有推荐人，把推荐人带过去。 着陆页跳转。。。hbz 20161010
		String reff = request.getParameter("referer");
		if (StringUtils.isEmpty(reff)) {
			Object rr = request.getSession().getAttribute("from_id");
			if (rr != null) {
				reff = rr.toString();
			}
		}
		Object tim0 = request.getSession().getAttribute("inittime");
		String time0 = null;
		if (tim0 != null) {
			time0 = tim0.toString();
		}
		if (StringUtils.isNotEmpty(time0)) {
			long inittime = Long.valueOf(time0);
			long nowtime = GetDate.getMillis();
			// 如果页面超过30分钟没点击，reff制空。
			long res = nowtime - inittime;
			if (res > 30 * 60 * 1000) {
				reff = null;
			}
		}

		if (reff != null && !"".equals(reff.trim())) {
			modelAndView.addObject("newRegReferree", reff);
		} else {// 这是原有逻辑
			modelAndView.addObject("newRegReferree", request.getParameter("from"));
		}

//		KeyPair kp = RSAJSPUtil.generateKeyPair();
//        RSAPublicKey pubk = (RSAPublicKey) kp.getPublic();// 生成公钥
//        RSAPrivateKey prik = (RSAPrivateKey) kp.getPrivate();// 生成私钥
//
//        String publicKeyExponent = pubk.getPublicExponent().toString(16);// 16进制
//        String publicKeyModulus = pubk.getModulus().toString(16);// 16进制
//        request.getSession().setAttribute("prik", prik);
//        modeAndView.addObject("pubexponent", publicKeyExponent);
//        modeAndView.addObject("pubmodules", publicKeyModulus);
		modelAndView.addObject("pubexponent", "10001");
		modelAndView.addObject("pubmodules", RSAJSPUtil.getPunlicKeys());
		//协议名称 动态获得
		List<ProtocolTemplate> list = agreementService.getdisplayNameDynamic();
		if(CollectionUtils.isNotEmpty(list)){
			//是否在枚举中有定义
			for (ProtocolTemplate p : list) {
				String protocolType = p.getProtocolType();
				String alia = ProtocolEnum.getAlias(protocolType);
				if (alia != null){
					modelAndView.addObject(alia, p.getDisplayName());
				}
			}
		}

		LogUtil.endLog(UserRegistController.class.getName(), UserRegistDefine.INIT);
		return modelAndView;
	}


	/**
	 * 跳转到注册成功页面
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = UserRegistDefine.TO_REGIST_SUCCESS_ACTION, method = RequestMethod.GET)
	public ModelAndView to_regist_success(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(UserRegistController.class.getName(), UserRegistDefine.TO_REGIST_SUCCESS_ACTION);
		ModelAndView modeAndView = null;
		String activity68 = request.getParameter("activity68");
		
		String couponSendCount = request.getParameter("couponSendCount");
		
		
		if (activity68 != null && activity68.equals("1")) {
			modeAndView = activity68Controller.getUserStatus(request, response);
		} else {
			modeAndView = new ModelAndView(UserRegistDefine.REGIST_SUCCESS_PATH);
		}
		modeAndView.addObject("userid", request.getParameter("userid"));
		// 关联注册送券活动  couponSendCount > 0 发送优惠券
		modeAndView.addObject("couponSendCount", couponSendCount);
		LogUtil.endLog(UserRegistController.class.getName(), UserRegistDefine.TO_REGIST_SUCCESS_ACTION);
		return modeAndView;
	}

	/**
	 * 跳转到注册成功优惠券页面
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = UserRegistDefine.TO_REGIST_SUCCESS_COUPON_ACTION, method = RequestMethod.GET)
	public ModelAndView to_regist_success_coupon(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(UserRegistController.class.getName(), UserRegistDefine.TO_REGIST_SUCCESS_COUPON_ACTION);
		ModelAndView modeAndView = null;
		String activity68 = request.getParameter("activity68");
		if (activity68 != null && activity68.equals("1")) {
			modeAndView = activity68Controller.getUserStatus(request, response);
		} else {
			modeAndView = new ModelAndView(UserRegistDefine.REGIST_SUCCESS_COUPON_PATH);
		}
		modeAndView.addObject("userid", request.getParameter("userid"));

		LogUtil.endLog(UserRegistController.class.getName(), UserRegistDefine.TO_REGIST_SUCCESS_COUPON_ACTION);
		return modeAndView;
	}

	/**
	 * 企业用户注册成功跳转画面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = UserRegistDefine.TO_BUSINESS_USERS_REGISTER_SUCCESS, method = RequestMethod.GET)
	public ModelAndView businessUsersRegisterSuccess(HttpServletRequest request, HttpServletResponse response){
		LogUtil.startLog(UserRegistDefine.class.getName(), UserRegistDefine.TO_BUSINESS_USERS_REGISTER_SUCCESS);

		ModelAndView modelAndView = new ModelAndView(UserRegistDefine.BUSINESS_USERS_REGISTER_SUCCESS_PAGE);

		return modelAndView;
	}

	/**
	 * 企业用户开户指南画面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = UserRegistDefine.TO_BUSINESS_USERS_GUIDE, method = RequestMethod.GET)
	public ModelAndView businessUsersGuide(HttpServletRequest request, HttpServletResponse response){
		ModelAndView modelAndView = new ModelAndView(UserRegistDefine.BUSINESS_USERS_GUIDE_PAGE);

		modelAndView.addObject("webUrl", PropUtils.getSystem(CustomConstants.HYJF_WEB_URL));
		return modelAndView;
	}

	/**
	 * 判断推荐人是否存在 如果存在返回true，如果不存在返回false;
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = UserRegistDefine.REGIST_CHECK_RECOMMEND_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public boolean checkRecommend(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.REGIST_CHECK_RECOMMEND_ACTION);
		String recommend = request.getParameter("newRegReferree");
		if (registService.countUserByRecommendName(recommend) <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * 生成图片验证码
	 *
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
	 * 检查图片验证码
	 *
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value = UserRegistDefine.REGIST_CHECK_RANDOMCODE, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public boolean checkcaptcha(HttpServletRequest request, HttpServletResponse response) {
		RandomValidateCode randomValidateCode = new RandomValidateCode();
		return randomValidateCode.checkRandomCode(request, request.getParameter("newRegVerifyCode"));
	}

	/**
	 * 检查图片验证码,返回json验证结果
	 *
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value = UserRegistDefine.REGIST_CHECK_RANDOMCODE_JSON, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public JSONObject checkcaptchajson(HttpServletRequest request, HttpServletResponse response) {
		RandomValidateCode randomValidateCode = new RandomValidateCode();
		JSONObject ret = new JSONObject();
		if (randomValidateCode.checkRandomCode(request, request.getParameter("newRegVerifyCode"))) {
			ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_TRUE);
			ret.put(UserRegistDefine.INFO, "验证成功");
			return ret;
		} else {
			ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
			ret.put(UserRegistDefine.ERROR, "图形验证码不正确!");
			return ret;
		}
	}

	/**
	 * 发送短信验证码（ajax请求） 短信验证码数据保存
	 */
	@ResponseBody
	@RequestMapping(value = UserRegistDefine.REGIST_SEND_CODE_ACTION, produces = "application/json; charset=utf-8")
	public JSONObject sendCode(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.REGIST_SEND_CODE_ACTION);
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();

		String userId = request.getParameter("userId"); //微信端发送验证码需要传userId
		String validCodeType = request.getParameter("validCodeType");
		if (validCodeType == null) {
			ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
			ret.put(UserRegistDefine.ERROR, "无效的验证码类型!");
			return ret;
		}
		if (!validCodeType.equals(UserRegistDefine.PARAM_TPL_ZHUCE) && !validCodeType.equals(UserRegistDefine.PARAM_TPL_ZHAOHUIMIMA) && !validCodeType.equals(UserRegistDefine.PARAM_TPL_YZYSJH)
				&& !validCodeType.equals(UserRegistDefine.PARAM_TPL_BDYSJH) && !validCodeType.equals(UserRegistDefine.PARAM_TPL_SMS_WITHDRAW)) {
			ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
			ret.put(UserRegistDefine.ERROR, "无效的验证码类型!");
			return ret;
		}
		// 手机号码(必须,数字,最大长度)
		String mobile = request.getParameter("newRegPhoneNum");
		if (!ValidatorCheckUtil.validateRequired(info, null, null, mobile)) {
			ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
			ret.put(UserRegistDefine.ERROR, "请填写手机号!");
			return ret;
		}
		if (!Validator.isMobile(mobile)) {
			ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
			ret.put(LoginDefine.ERROR, "手机号格式不正确!");
			return ret;
		}
		if (validCodeType.equals(UserRegistDefine.PARAM_TPL_ZHUCE)) {
			// 注册时要判断不能重复
			if (loginService.existUser(mobile)) {
				ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
				ret.put(UserRegistDefine.ERROR, "手机号已存在!");
				return ret;
			}
		}
		if (validCodeType.equals(UserRegistDefine.PARAM_TPL_YZYSJH)) {
			if (WebUtils.getUser(request) == null || StringUtils.isBlank(WebUtils.getUser(request).getMobile())) {
				ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
				ret.put(UserRegistDefine.ERROR, "不存在用户!");
				return ret;
			}
			if (!WebUtils.getUser(request).getMobile().equals(mobile)) {
				ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
				ret.put(UserRegistDefine.ERROR, "获取验证码手机号与注册手机号不一致!");
				return ret;
			}
		}
		if (validCodeType.equals(UserRegistDefine.PARAM_TPL_BDYSJH)) {
			if (WebUtils.getUser(request) == null) {
				ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
				ret.put(UserRegistDefine.ERROR, "不存在用户!");
				return ret;
			}
			if (WebUtils.getUser(request).getMobile().equals(mobile)) {
				ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
				ret.put(UserRegistDefine.ERROR, "修改手机号与原手机号不能相同!");
				return ret;
			}
			// 不能重复
			if (loginService.existUser(mobile)) {
				ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
				ret.put(UserRegistDefine.ERROR, "手机号已存在!");
				return ret;
			}
		}
		
		SmsConfig smsConfig = registService.getSmsConfig();
		_log.info("smsConfig---------------------------------------" + JSON.toJSONString(smsConfig));
		// smsConfig.getMaxIpCount();

		// 判断发送间隔时间
		String intervalTime = RedisUtils.get(mobile + ":" + validCodeType + ":IntervalTime");
		_log.info(mobile + ":" + validCodeType + "----------IntervalTime-----------" + intervalTime);
		if (StringUtils.isNotBlank(intervalTime)) {
			ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
			ret.put(UserRegistDefine.ERROR, "验证码发送过于频繁");
			LogUtil.errorLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.REGIST_SEND_CODE_ACTION, "验证码发送过于频繁，短信验证码发送失败", null);
			return ret;
		}

		String ip = GetCilentIP.getIpAddr(request);
		String ipCount = RedisUtils.get(ip + ":MaxIpCount");
		if (StringUtils.isBlank(ipCount) || !Validator.isNumber(ipCount)) {
			ipCount = "0";
			RedisUtils.set(ip + ":MaxIpCount", "0");
		}
		_log.info(mobile + "------ip---" + ip + "----------MaxIpCount-----------" + ipCount);
		if (Integer.valueOf(ipCount) >= smsConfig.getMaxIpCount()) {
			if (Integer.valueOf(ipCount) == smsConfig.getMaxIpCount()) {
				try {
					registService.sendSms(mobile, "IP访问次数超限:" + ip);
				} catch (Exception e) {
					LogUtil.errorLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.REGIST_SEND_CODE_ACTION, e);
				}

				RedisUtils.set(ip + ":MaxIpCount", (Integer.valueOf(ipCount) + 1) + "", 24 * 60 * 60);

			}
			// try {
			// registService.sendEmail(mobile, "IP访问次数超限" + ip);
			// } catch (Exception e) {
			// LogUtil.errorLog(UserRegistDefine.THIS_CLASS,
			// UserRegistDefine.REGIST_SEND_CODE_ACTION, e);
			// }
			ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
			ret.put(UserRegistDefine.ERROR, "该设备短信请求次数超限，请明日再试");
			LogUtil.errorLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.REGIST_SEND_CODE_ACTION, "该设备短信请求次数超限，请明日再试", null);
			return ret;
		}

		// 判断最大发送数max_phone_count
		String count = RedisUtils.get(mobile + ":MaxPhoneCount");
		if (StringUtils.isBlank(count) || !Validator.isNumber(count)) {
			count = "0";
			RedisUtils.set(mobile + ":MaxPhoneCount", "0");
		}
		_log.info(mobile + "----------MaxPhoneCount-----------" + count);
		if (Integer.valueOf(count) >= smsConfig.getMaxPhoneCount()) {
			if (Integer.valueOf(count) == smsConfig.getMaxPhoneCount()) {
				try {
					registService.sendSms(mobile, "手机验证码发送次数超限");
				} catch (Exception e) {
					LogUtil.errorLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.REGIST_SEND_CODE_ACTION, e);
				}

				RedisUtils.set(mobile + ":MaxPhoneCount", (Integer.valueOf(count) + 1) + "", 24 * 60 * 60);
			}
			// try {
			// registService.sendEmail(mobile, "手机验证码发送次数超限");
			// } catch (Exception e) {
			// LogUtil.errorLog(UserRegistDefine.THIS_CLASS,
			// UserRegistDefine.REGIST_SEND_CODE_ACTION, e);
			// }
			ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
			ret.put(UserRegistDefine.ERROR, "该手机号短信请求次数超限，请明日再试");
			LogUtil.errorLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.REGIST_SEND_CODE_ACTION, "该手机号短信请求次数超限，短信验证码发送失败", null);
			return ret;
		}

		WebViewUser user = WebUtils.getUser(request);
		UsersInfo usersInfo = null;
		if(StringUtils.isNotBlank(userId)){
			usersInfo = registService.getUsersInfoByUserId(Integer.parseInt(userId));
		}
		
		String valName = "";
		String valSex = "";
		if(user != null){
			if(StringUtils.isNotBlank(user.getTruename())) {
				valName = user.getTruename().substring(0, 1);				
			}
			valSex = user.getSex() == 2 ? "女士" : "先生";
		}else if(usersInfo != null){
			if(StringUtils.isNotBlank(usersInfo.getTruename())) {
				valName = usersInfo.getTruename().substring(0, 1);				
			}
			valSex = usersInfo.getSex() == 2 ? "女士" : "先生";
		}
		// 生成验证码
		String checkCode = GetCode.getRandomSMSCode(6);

		//测试环境统一验证码
		if(ENV_TEST){
			checkCode = "111111";
		}

		Map<String, String> param = new HashMap<String, String>();
		param.put("val_code", checkCode);
		if(validCodeType.equals(UserRegistDefine.PARAM_TPL_SMS_WITHDRAW)){
			param.put("val_name", valName);
			param.put("val_sex", valSex);
			param.put("val_amount", request.getParameter("withdrawmoney"));
		}
		// 发送短信验证码
		SmsMessage smsMessage = new SmsMessage(null, param, mobile, null, MessageDefine.SMSSENDFORMOBILE, null, validCodeType, CustomConstants.CHANNEL_TYPE_NORMAL);
		Integer result = (smsProcesser.gather(smsMessage) == 1) ? 0 : 1;

		// 短信发送成功后处理
		if (result != null && result == 0) {
			// 累计IP次数
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
		_log.info(checkCode);
		// 保存短信验证码
		this.registService.saveSmsCode(mobile, checkCode, validCodeType, UserRegistDefine.CKCODE_NEW, CustomConstants.CLIENT_PC);
		// 发送checkCode最大时间间隔，默认60秒
		RedisUtils.set(mobile + ":" + validCodeType + ":IntervalTime", mobile, smsConfig.getMaxIntervalTime() == null ? 60 : smsConfig.getMaxIntervalTime());
		ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_TRUE);
		ret.put(UserRegistDefine.INFO, "短信发送成功");
		LogUtil.endLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.REGIST_SEND_CODE_ACTION);
		return ret;

	}

	/**
	 * 短信验证码校验
	 * 
	 * 用户注册数据提交（获取session数据并保存） 1.校验验证码
	 * 2.若验证码正确，则获取session数据，并将相应的注册数据写入数据库（三张表），跳转相应的注册成功界面
	 */
	@ResponseBody
	@RequestMapping(value = UserRegistDefine.REGIST_CHECK_CODE_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public boolean checkcode(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.REGIST_CHECK_CODE_ACTION);
		// JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		// JSONObject jo = new JSONObject();

		String validCodeType = request.getParameter("validCodeType");
		if (StringUtils.isBlank(validCodeType)) {
			return false;
		}
		if (!validCodeType.equals(UserRegistDefine.PARAM_TPL_ZHUCE) && !validCodeType.equals(UserRegistDefine.PARAM_TPL_ZHAOHUIMIMA) && !validCodeType.equals(UserRegistDefine.PARAM_TPL_YZYSJH)
				&& !validCodeType.equals(UserRegistDefine.PARAM_TPL_BDYSJH)) {
			return false;
		}
		if (validCodeType.equals(UserRegistDefine.PARAM_TPL_ZHUCE) || validCodeType.equals(UserRegistDefine.PARAM_TPL_ZHAOHUIMIMA)) {
/*			if (!checkcaptcha(request, response)) {
				return false;
			}*/
		}
		// 手机号码(必须,数字,最大长度)
		String mobile = request.getParameter("newRegPhoneNum");
		if (!ValidatorCheckUtil.validateRequired(info, null, null, mobile)) {
			return false;
		} else if (!ValidatorCheckUtil.validateMobile(info, null, null, mobile, 11, false)) {
			return false;
		}

		// 短信验证码
		String code = request.getParameter("newRegVerify");
		if (!ValidatorCheckUtil.validateRequired(info, null, null, code)) {
			return false;
		}

		int cnt = this.registService.updateCheckMobileCode(mobile, code, validCodeType, CustomConstants.CLIENT_PC, UserRegistDefine.CKCODE_YIYAN, UserRegistDefine.CKCODE_YIYAN);
		if (cnt > 0) {
			LogUtil.endLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.REGIST_CHECK_CODE_ACTION);
			return true;
		} else {
			LogUtil.endLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.REGIST_CHECK_CODE_ACTION);
			return false;
		}
	}

	/**
	 * 用户注册初始化画面数据保存（保存到session）
	 */
	@ResponseBody
	@RequestMapping(value = UserRegistDefine.USER_REGIST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public JSONObject regist(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.INIT_REGIST_ACTION);

		RedisUtils.set(UserRegistDefine.CONTROLLOR_CLASS_NAME + "_registvalidCode_" + request.getSession().getId(), "1", 10 * 60);
		// 用户名
		// String userName = request.getParameter("userName");
		// 邮箱(待定)
		// String email = request.getParameter("email");
		// 密码
		String password = request.getParameter("newRegPsw");

		//_log.info("前台传来密码【{}】",password);

		//RSAPrivateKey prik = (RSAPrivateKey) request.getSession().getAttribute("prik");
		password = RSAJSPUtil.rsaToPassword(password);
		//_log.info("解密后为【{}】",password);

		// 密码确认
		// String passwordConfirm = request.getParameter("passwordConfirm");
		// 手机号
		String mobile = request.getParameter("newRegPhoneNum");
		// 短信验证码
		String verificationCode = request.getParameter("newRegVerify");
		// add by liuyang 神策数据埋点追加 20180716 start
		// 神策数据统计预置属性
		String presetProps = request.getParameter("presetProps");
		// add by liuyang 神策数据埋点追加 20180716 end

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

		// 推荐人
		String reffer = request.getParameter("newRegReferree");
		if (StringUtils.isEmpty(reffer)) {
			Object rr = request.getSession().getAttribute("from_id");
			if (rr != null) {
				reffer = rr.toString();
			}
		}
		Object tim0 = request.getSession().getAttribute("inittime");
		String time0 = null;
		if (tim0 != null) {
			time0 = tim0.toString();
		}
		if (StringUtils.isNotEmpty(time0)) {
			long inittime = Long.valueOf(time0);
			long nowtime = GetDate.getMillis();
			// 如果页面超过30分钟没点击，reffer制空。
			long res = nowtime - inittime;
			if (res > 30 * 60 * 1000) {
				reffer = null;
			}
		}

		// 是否是活动页过来的
		// 活动结束 注释掉
		// String activity68 = request.getParameter("activity68");
		// 登录IP
		String loginIp = GetCilentIP.getIpAddr(request);
		// ------以下是通过着陆页注册 补充字段-------------
		String utm_id = request.getParameter("utm_id");// 链接唯一id
		if (StringUtils.isNotEmpty(utm_id)) {
			request.getSession().setAttribute("utm_id", utm_id);
		}
		String utm_source = request.getParameter("utm_source");// 推广渠道
		String utm_medium = request.getParameter("utm_medium");// 推广方式
		String utm_content = request.getParameter("utm_content");// 推广单元

		JSONObject ret = new JSONObject();

		/*
		 * if (!checkcaptcha(request, response)) {
		 * ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
		 * ret.put(UserRegistDefine.ERROR, "图形验证码不正确!"); return ret; }
		 */

		if (Validator.isNull(mobile)) {
			ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
			ret.put(UserRegistDefine.ERROR, "请填写手机号");
			return ret;
		}
		if (Validator.isNull(verificationCode)) {
			ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
			ret.put(UserRegistDefine.ERROR, "验证码不能为空");
			return ret;
		}
		if (Validator.isNull(password)) {
			ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
			ret.put(UserRegistDefine.ERROR, "密码不能为空");
			return ret;
		}
		if (!Validator.isMobile(mobile)) {
			ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
			ret.put(UserRegistDefine.ERROR, "请填写您的真实手机号码");
			return ret;
		} else {
			if (loginService.existUser(mobile)) {
				ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
				ret.put(UserRegistDefine.ERROR, "手机号已存在");
				return ret;
			}
		}
		if (password.length() < 8 || password.length() > 16) {
			ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
			ret.put(UserRegistDefine.ERROR, "密码长度6-16位");
			return ret;
		}
		/*if (Validator.isNumber(password.substring(0, 1))) {
			ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
			ret.put(UserRegistDefine.ERROR, "密码首位必须为字母");
			return ret;
		}*/
		boolean hasNumber = false;

		for (int i = 0; i < password.length(); i++) {
			if (Validator.isNumber(password.substring(i, i + 1))) {
				hasNumber = true;
				break;
			}
		}
		String regEx = "^(?![\\d]+$)(?![a-zA-Z]+$)(?![^\\da-zA-Z]+$).{8,16}$";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(password);
		if (!m.matches()) {
			ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
			ret.put(UserRegistDefine.ERROR, "密码必须由8~16位数字、字母或者符号组合");
			return ret;
		}
		
		//推荐人校验通过之后再消费验证码
		if (StringUtils.isNotBlank(reffer) && registService.countUserByRecommendName(reffer) <= 0) {
			ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
			ret.put(UserRegistDefine.ERROR, "推荐人无效");
			return ret;
		}
		
		//消费短信验证码
		String verificationType = UserRegistDefine.PARAM_TPL_ZHUCE;
		int cnt = registService.updateCheckMobileCode(mobile, verificationCode, verificationType, CustomConstants.CLIENT_PC, UserRegistDefine.CKCODE_YIYAN, UserRegistDefine.CKCODE_USED);
		if (cnt == 0) {
			ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
			ret.put(UserRegistDefine.ERROR, "验证码无效");
			return ret;
		}
		
		
		// 活动结束 注释掉
		/*
		 * if (StringUtils.isNotBlank(activity68) && activity68.equals("1")) {
		 * ret.put("activity68", activity68); }
		 */
		// 注册
		int userid = registService.insertUserActionNew(mobile, password, verificationCode, reffer, loginIp, CustomConstants.CLIENT_PC, utm_id, utm_source, utm_medium, utm_content, request, response, userType);

		if (userid != 0) {	
			
			// 发送Ip同步信息
			sendIpInfo(userid, loginIp);
			
			int timestamp = GetDate.getMyTimeInMillis();
			String useridStr = TreeDESUtils.getEncrypt(String.valueOf(timestamp), String.valueOf(userid));
			ret.put("connection", useridStr);
			ret.put("timestamp", timestamp);
			ret.put("userid", userid);
			ret.put("userType", (userType + 1));	// 迎合前端传值
			ret.put("couponSendCount", 0);
			ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_TRUE);
			ret.put(UserRegistDefine.INFO, "注册成功");
			try {
				Thread.sleep(500);
				WebViewUser webUser = loginService.getWebViewUserByUserId(userid);
				WebUtils.sessionLogin(request, response, webUser);
			} catch (Exception e) {
				LogUtil.errorLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.INIT_REGIST_ACTION, "用户不存在，有可能读写数据库不同步", e);
			}

			// 投之家用户注册送券活动
			String activityIdTzj = CustomConstants.REGIST_TZJ_ACTIVITY_ID;
			// 活动有效期校验
			String resultActivityTzj = couponCheckUtil.checkActivityIfAvailable(activityIdTzj);
			if (StringUtils.isEmpty(resultActivityTzj)) {
				Users user = loginService.getUsers(userid);
				// 投之家用户额外发两张加息券
				if(StringUtils.isNotEmpty(user.getReferrerUserName()) && user.getReferrerUserName().equals("touzhijia")){
					CommonParamBean paramBean = new CommonParamBean();
					paramBean.setUserId(String.valueOf(userid));
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
			
			// add by zhangjinpeng 注册送188元新手红包 start
			// 发券成功
			// 发送短信通知
			String activityId = CustomConstants.REGIST_888_ACTIVITY_ID;
			// 活动有效期校验
			String resultActivity = couponCheckUtil.checkActivityIfAvailable(activityId);
			if (StringUtils.isEmpty(resultActivity)) {

                /*CommonParamBean paramBean = new CommonParamBean();
                paramBean.setUserId(String.valueOf(userid));
                paramBean.setSendFlg(11);
//                 发放888元新手红包
                CommonSoaUtils.sendUserCouponNoRet(paramBean);*/
                
                try {
					sendCoupon(userid);
				} catch (Exception e) {
					LogUtil.errorLog(this.getClass().getName(), "regist", "注册发放888红包失败", e);
				}
                
                // 发送短信通知
                sendSmsCoupon(userid,mobile);
                ret.put("couponSendCount", 8);

			}
			// add by zhangjinpeng 注册送188元新手红包 end

			// add by liuyang 注册成功后,发送神策数据统计MQ 20180716 start
			if (StringUtils.isNotBlank(presetProps)) {
				try {
					SensorsDataBean sensorsDataBean = new SensorsDataBean();
					// 将json串转换成Bean
					Map<String, Object> sensorsDataMap = JSONObject.parseObject(presetProps, new TypeReference<Map<String, Object>>() {
					});
					sensorsDataBean.setPresetProps(sensorsDataMap);
					sensorsDataBean.setUserId(userid);
					// 发送神策数据统计MQ
					this.registService.sendSensorsDataMQ(sensorsDataBean);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// add by liuyang 注册成功后,发送神策数据统计MQ 20180716 end
			return ret;
		} else {
			ret.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
			ret.put(UserRegistDefine.ERROR, "注册失败,参数异常");
			return ret;
		}
	}

	private void sendCoupon(int userid) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("mqMsgId", GetCode.getRandomCode(10));
		params.put("userId", String.valueOf(userid));
		params.put("sendFlg", "11");
		
		String sign = StringUtils.lowerCase(MD5.toMD5Code(SOA_COUPON_KEY + String.valueOf(userid) + 11 + SOA_COUPON_KEY));
		params.put("sign", sign);
		
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
	 * 检查手机号码或用户名唯一性
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = UserRegistDefine.REGIST_CHECK_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public boolean checkAction(HttpServletRequest request) {
		LogUtil.startLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.REGIST_CHECK_ACTION);
		String name = request.getParameter("name");
		String param = request.getParameter("param");

		if ("userName".equals(name)) {
			if (loginService.existUser(param)) {
				// 存在用户,返回false
				return false;
			} else {
				JSONObject info = new JSONObject();
				return ValidatorCheckUtil.validateMobile(info, null, null, param, 11, false);
			}
		} else {
			return false;
		}

	}

	/**
	 * 检查手机号是否已存在
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = UserRegistDefine.REGIST_CHECK_PHONE, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public boolean checkPhone(HttpServletRequest request) {
		LogUtil.startLog(UserRegistDefine.THIS_CLASS, UserRegistDefine.REGIST_CHECK_PHONE);

		String mobile = request.getParameter("newRegPhoneNum");

		if (mobile != null && !"".equals(mobile.trim())) {

			if (Validator.isMobile(mobile)) {

				if (loginService.existUser(mobile)) {
					// 存在用户,返回false
					return false;
				} else {
					return true;
				}

			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	/**
	 * 跳转注册协议
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = UserRegistDefine.REGIST_DETAIL)
	public ModelAndView goDetail(HttpServletRequest request) {
		LogUtil.startLog(UserRegistDefine.class.getName(), UserRegistDefine.INIT);
		ModelAndView modeAndView = null;
		String pageType = request.getParameter("type");
		if (pageType.equals("register")) {
			// 网站注册协议
			modeAndView = new ModelAndView("user/regist/web_register");
		} else if (pageType.equals("secret")) {
			// 隐私保护规则
			modeAndView = new ModelAndView("user/regist/web_secret");
		} else if (pageType.equals("service")) {
			// 出借咨询与管理服务协议
			modeAndView = new ModelAndView("user/regist/web_service");
		} else if (pageType.equals("invest")) {
			// 平台居间服务协议
			modeAndView = new ModelAndView("user/regist/web_invest");
		} else if (pageType.equals("creditcontract")) {
			// 债权转让协议
			modeAndView = new ModelAndView("user/regist/web_creditcontract");
		} else if (pageType.equals("contract")) {
			// 散标风险揭示书
			modeAndView = new ModelAndView("user/regist/web_contract");
		} else if (pageType.equals("app_invest")) {
			// app平台居间服务协议H5
			modeAndView = new ModelAndView("user/regist/app_invest");
		} else if (pageType.equals("app_contract")) {
			// app散标风险揭示书H5
			modeAndView = new ModelAndView("user/regist/app_contract");
		} else if (pageType.equals("rtb_contract")) {
			// 融通宝系列说明书
			modeAndView = new ModelAndView("user/regist/rtb_contract");
		} else if (pageType.equals("rtb_instructions")) {
			// 温州金融资产交易中心股份有限公司融通宝系列说明书
			modeAndView = new ModelAndView("user/regist/rtb_instructions");
		} else if (pageType.equals("wzjr_personal_contract")) {
			// 温州金融资产交易中心股份有限公司个人会员服务协议
			modeAndView = new ModelAndView("user/regist/wzjr_personal_contract");
		} else if (pageType.equals("web_contract-rtb")) {
			// 融通宝散标风险揭示书
			modeAndView = new ModelAndView("user/regist/web_contract-rtb");
		} else if ("bankOpen".equals(pageType)) {// 银行开户协议
			modeAndView = new ModelAndView("bank/user/bankopen/jxbank-contract");
		}
		LogUtil.endLog(UserRegistController.class.getName(), UserRegistDefine.INIT);
		return modeAndView;
	}

	/**
	 * 发送短信(注册送188元新手红包)
	 * 
	 * @param userId
	 */
	private void sendSmsCoupon(Integer userId, String mobile) {

		SmsMessage smsMessage = new SmsMessage(userId, null, mobile, null, MessageDefine.SMSSENDFORMOBILE, null, CustomConstants.PARAM_TPL_TZJ_188HB, CustomConstants.CHANNEL_TYPE_NORMAL);
		smsProcesser.gather(smsMessage);

	}
}
