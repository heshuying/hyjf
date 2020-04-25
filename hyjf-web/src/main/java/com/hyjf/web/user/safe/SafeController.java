package com.hyjf.web.user.safe;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.common.util.*;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.model.customize.admin.AdminUserDetailCustomize;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.autoup.AutoPlusService;
import com.hyjf.bank.service.user.pandect.PandectService;
import com.hyjf.bank.service.user.recharge.RechargeService;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.MD5Utils;
import com.hyjf.common.security.utils.RSAJSPUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.UserBindEmailLog;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersContract;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.web.BaseController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.financialadvisor.FinancialAdvisorDefine;
import com.hyjf.web.user.financialadvisor.FinancialAdvisorService;
import com.hyjf.web.user.login.LoginService;
import com.hyjf.web.user.regist.UserRegistDefine;
import com.hyjf.web.user.regist.UserRegistService;
import com.hyjf.web.util.WebUtils;

import sun.misc.BASE64Decoder;

/**
 * <p>
 * SafeController
 * </p>
 *
 * @author gogtz
 * @version 1.0.0
 */
@SuppressWarnings("restriction")
@Controller
@RequestMapping(SafeDefine.CONTROLLOR_REQUEST_MAPPING)
public class SafeController extends BaseController {

	Logger _log = LoggerFactory.getLogger(SafeController.class);

	@Autowired
	SafeService safeService;
	@Autowired
	PandectService pandectService;
	@Autowired
	LoginService loginService;
	@Autowired
	private UserRegistService registService;
//	@Autowired
//	private DriecTransService driecTransService;
	@Autowired
	private FinancialAdvisorService financialAdvisorService;
	
	@Autowired
    private AutoPlusService autoPlusService;
	@Autowired
	private RechargeService userRechargeService;

	/**
	 * 初期化,跳转到登录页面
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = SafeDefine.INIT, method = RequestMethod.GET)
	public ModelAndView init(HttpServletRequest request ,HttpServletResponse response) throws Exception {
		LogUtil.startLog(SafeController.class.getName(), SafeDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(SafeDefine.INIT_PATH);
		WebViewUser webViewUser = WebUtils.getUser(request);
		modeAndView.addObject("webViewUser", webViewUser);
		if (webViewUser.getTruename() != null && webViewUser.getTruename().length() >= 1) {
			modeAndView.addObject("truename", webViewUser.getTruename().substring(0, 1) + "**");
		}
		if (webViewUser.getIdcard() != null && webViewUser.getIdcard().length() >= 15) {
			modeAndView.addObject("idcard", webViewUser.getIdcard().substring(0, 3) + "***********" + webViewUser.getIdcard().substring(webViewUser.getIdcard().length() - 4));
		}
		if (webViewUser.getMobile() != null && webViewUser.getMobile().length() == 11) {
			modeAndView.addObject("mobile", webViewUser.getMobile().substring(0, 3) + "****" + webViewUser.getMobile().substring(webViewUser.getMobile().length() - 4));
		}
		if (webViewUser.getEmail() != null && webViewUser.getEmail().length() >= 2) {
			String emails[] = webViewUser.getEmail().split("@");
			modeAndView.addObject("email", AsteriskProcessUtil.getAsteriskedValue(emails[0], 2, emails[0].length() -2) + "@" + emails[1]);
		}
		Users user = this.safeService.getUsers(webViewUser.getUserId());
		// 用户角色
		UsersInfo userInfo = this.safeService.getUsersInfoByUserId(webViewUser.getUserId());
		modeAndView.addObject("roleId", userInfo.getRoleId());
		// 是否设置交易密码
		modeAndView.addObject("isSetPassword", user.getIsSetPassword());
		modeAndView.addObject("lastTime", user.getLastTime());
		// 紧急联系人类型
		List<ParamName> paramList = safeService.getParamNameList("USER_RELATION");
		JSONArray result = new JSONArray();
		for (int i = 0; i < paramList.size(); i++) {
			JSONObject json = new JSONObject();
			json.put("name", paramList.get(i).getName());
			json.put("value", paramList.get(i).getNameCd());
			result.add(json);
		}
		modeAndView.addObject("userRelation", result);
		// 根据用户Id查询用户银行卡号 add by tyy 2018-6-27
		BankCard bankCard = this.userRechargeService.selectBankCardByUserId(webViewUser.getUserId());
		if(bankCard!=null) {
			bankCard.setCardNo(BankCardUtil.getCardNo(bankCard.getCardNo()));
			modeAndView.addObject("bankname", org.apache.commons.lang3.StringUtils.isNotEmpty(bankCard.getBank()) ? bankCard.getBank() : "");
		}
		modeAndView.addObject("bankCard", bankCard);
		BankOpenAccount bankOpenAccount = loginService.getBankOpenAccount(webViewUser.getUserId());
		AccountChinapnr chinapnr = safeService.getAccountChinapnr(webViewUser.getUserId());
		modeAndView.addObject("bankOpenAccount", bankOpenAccount);
		modeAndView.addObject("chinapnr", chinapnr);
		UserEvalationResultCustomize userEvalationResultCustomize = financialAdvisorService.selectUserEvalationResultByUserId(webViewUser.getUserId());
		if (userEvalationResultCustomize != null && userEvalationResultCustomize.getId() != 0) {
			//获取评测时间加一年的毫秒数18.2.2评测 19.2.2
			Long lCreate = user.getEvaluationExpiredTime().getTime();
			//获取当前时间加一天的毫秒数 19.2.1以后需要再评测19.2.2
			Long lNow = System.currentTimeMillis();
			if (lCreate <= lNow) {
				//已过期需要重新评测 2是已过期
				modeAndView.addObject(FinancialAdvisorDefine.JSON_IF_EVALUATION_KEY, 2);
			} else {
				// ifEvaluation是否已经调查表示 1是已调查，0是未调查
				modeAndView.addObject(FinancialAdvisorDefine.JSON_IF_EVALUATION_KEY, 1);
				// userEvalationResult 测评结果
				modeAndView.addObject(FinancialAdvisorDefine.JSON_USER_EVALATION_RESULT_KEY, createUserEvalationResult(userEvalationResultCustomize));
			}
			// 测评到期时间
			modeAndView.addObject("evaluationExpireDate",GetDate.dateToString2(user.getEvaluationExpiredTime()));
		} else {
			modeAndView.addObject(FinancialAdvisorDefine.JSON_IF_EVALUATION_KEY, 0);
		}
		
		HjhUserAuth hjhUserAuth=safeService.getHjhUserAuthByUserId(webViewUser.getUserId());
		hjhUserAuth = autoPlusService.getUserAuthState(hjhUserAuth);
		if(hjhUserAuth.getAutoBidEndTime()!=null&&!hjhUserAuth.getAutoBidEndTime().equals("")&&!hjhUserAuth.getAutoBidEndTime().equals("0")){
		    hjhUserAuth.setAutoBidEndTime(GetDate.dateToString2(GetDate.str2Date(hjhUserAuth.getAutoBidEndTime(),GetDate.yyyyMMdd)));
		}
		//设置时间显示格式:add by nxl 合规四期修改
		hjhUserAuth.setAutoRepayEndTime(setDateFormat(hjhUserAuth.getAutoRepayEndTime()));
		hjhUserAuth.setAutoCreditEndTime(setDateFormat(hjhUserAuth.getAutoCreditEndTime()));
		hjhUserAuth.setAutoPaymentEndTime(setDateFormat(hjhUserAuth.getAutoPaymentEndTime()));
		modeAndView.addObject("hjhUserAuth",hjhUserAuth );
		
		// 是否开启服务费授权 0未开启  1已开启
		modeAndView.addObject("paymentAuthOn", CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus());
		// 是否开启还款授权 0未开启  1已开启
        modeAndView.addObject("repayAuthOn", CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_REPAYMENT_AUTH).getEnabledStatus());
		
		// 获得是否授权
		// 获取用户上传头像
		String imghost = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.head.url"));
		imghost = imghost.substring(0, imghost.length() - 1);// http://cdn.huiyingdai.com/
		// 实际物理路径前缀2
		String fileUploadTempPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.head.path"));
		if(org.apache.commons.lang3.StringUtils.isNotEmpty( user.getIconurl())){
		    modeAndView.addObject("iconUrl", imghost + fileUploadTempPath + user.getIconurl());
		}

		// 通过当前用户ID 查询用户所在一级分部,从而关联用户所属渠道
		// 合规自查添加
		// 20181205 产品需求, 屏蔽渠道,只保留用户ID
//		AdminUserDetailCustomize userUtmInfo = safeService.selectUserUtmInfo(webViewUser.getUserId());
//		String utmId = null;
//		String utmSource = null;
//		if (userUtmInfo != null) {
//			utmId = userUtmInfo.getSourceId().toString();
//			utmSource = userUtmInfo.getSourceName();
//            modeAndView.addObject("inviteLink", PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+"/landingpage/init.do.do?refferUserId="+webViewUser.getUserId() + "&utmId=" + utmId + "&utmSource=" + utmSource);
//		}else {
			// 已确认未关联渠道的用户
            modeAndView.addObject("inviteLink", PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+"/landingpage/init.do.do?refferUserId="+webViewUser.getUserId());
//		}

		// 获取用户的汇付信息
		LogUtil.endLog(SafeController.class.getName(), SafeDefine.INIT);
		return modeAndView;
	}

	private UserEvalationResultCustomize createUserEvalationResult(UserEvalationResultCustomize userEvalationResult) {
		UserEvalationResultCustomize userEvalationResultCustomize = new UserEvalationResultCustomize();
		userEvalationResultCustomize.setType(userEvalationResult.getType());
		userEvalationResultCustomize.setSummary(userEvalationResult.getSummary());
		userEvalationResultCustomize.setScoreCount(userEvalationResult.getScoreCount());
		return userEvalationResultCustomize;
	}

	/**
	 * 检查密码格式
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = SafeDefine.CHECKPARAM, method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public boolean checkParam(HttpServletRequest request) throws Exception {
		String name = request.getParameter("name");
		if (StringUtils.isNotBlank(name)) {
			if (name.equals("oldpass")) {
				String passwrod = request.getParameter("oldpass");
				// if (!validPassword(passwrod)) {
				// return false;
				// } else {
				return safeService.validPassword(WebUtils.getUserId(request), passwrod);
				// }
			} else if (name.equals("password")) {
				String passwrod = request.getParameter("password");
				if (!validPassword(passwrod)) {
					return false;
				} else {
					return true;
				}
			} else if (name.equals("repassword")) {
				String passwrod = request.getParameter("password");
				String passwrod2 = request.getParameter("repassword");
				if (!passwrod.equals(passwrod2) || !validPassword(passwrod2)) {
					return false;
				} else {
					return true;
				}
			} else if (name.equals("rlName")) {
				String rlName = request.getParameter("rlName");
				if (rlName.length() < 2 || rlName.length() > 4) {
					return false;
				} else {
					return true;
				}
			} else if (name.equals("rlPhone")) {
				String rlPhone = request.getParameter("rlPhone");
				if (rlPhone.length() != 11 || !Validator.isMobile(rlPhone)) {
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
	 * 验证密码格式
	 * 
	 * @param password
	 * @return
	 */
	private boolean validPassword(String password) {
		if (password.length() < 6 || password.length() > 16) {
			return false;
		}
		if (Validator.isNumber(password.substring(0, 1))) {
			return false;
		}
		boolean hasNumber = false;

		for (int i = 0; i < password.length(); i++) {
			if (Validator.isNumber(password.substring(i, i + 1))) {
				hasNumber = true;
				break;
			}
		}
		if (!hasNumber) {
			return false;
		}
		String regEx = "^[a-zA-Z0-9]+$";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(password);
		if (!m.matches()) {
			return false;
		}
		return true;
	}

	
	/**
	 * 修改登录密码
	 *
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = SafeDefine.UPDATEPASSWORD, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public JSONObject updatePassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(SafeController.class.getName(), SafeDefine.UPDATEPASSWORD);
		JSONObject resultJson = new JSONObject();
		String oldpass = request.getParameter("oldPassword");
		String password = request.getParameter("newPw");
		String password2 = request.getParameter("pwSure");

		Integer userId = WebUtils.getUserId(request);
		if (StringUtils.isBlank(oldpass)) {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "原始登录密码不能为空!");
			return resultJson;
		}
		if (StringUtils.isBlank(password) || StringUtils.isBlank(password2)) {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "新密码不能为空!");
			return resultJson;
		}
		//密码解密
		//RSAPrivateKey prik = (RSAPrivateKey) request.getSession().getAttribute("prik");

        oldpass = RSAJSPUtil.rsaToPassword(oldpass);
        password = RSAJSPUtil.rsaToPassword(password);
        password2 = RSAJSPUtil.rsaToPassword(password2);
        
		/*if (!validPassword(password)) {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "新密码不符合格式要求:密码6~16 位，必须由数字和字母组成，区分大小写,且首位必须为字母");
			return resultJson;
		} */
		if (!password.equals(password2)) {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "两次密码不一致");
			return resultJson;
		}
		if (!safeService.validPassword(userId, oldpass)) {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "原始登录密码错误!");
			return resultJson;
		}
		if (oldpass.equals(password)) {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "新密码不能与原密码相同!");
			return resultJson;
		}

		Boolean result = safeService.updatePassword(userId, password);
		if (result) {
			/**
			 * 调用重新登录接口
			 */
			WebViewUser webUser = loginService.getWebViewUserByUserId(userId);
			WebViewUser user = WebUtils.getUser(request);
			UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
			userOperationLogEntity.setOperationType(7);
			userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
			userOperationLogEntity.setPlatform(0);
			userOperationLogEntity.setRemark("");
			userOperationLogEntity.setOperationTime(new Date());
			userOperationLogEntity.setUserName(user.getUsername());
			userOperationLogEntity.setUserRole(user.getRoleId());
			loginService.sendUserLogMQ(userOperationLogEntity);
			WebUtils.sessionLogin(request, response, webUser);
			//如果修改密码成功就将登陆密码错误次数的key删除
			RedisUtils.del(RedisConstants.PASSWORD_ERR_COUNT_ALL+userId);
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_TRUE);
			resultJson.put(SafeDefine.ERROR, "修改密码成功");
			return resultJson;
		} else {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "修改密码失败,未知错误");
			return resultJson;
		}
	}

	
	
	
	/**
	 * 修改昵称
	 *
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = SafeDefine.UPDATENICKNAME, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public JSONObject updatenickname(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(SafeController.class.getName(), SafeDefine.UPDATENICKNAME);
		JSONObject resultJson = new JSONObject();
		String nickname = request.getParameter("nickname");
		Integer userId = WebUtils.getUserId(request);
		if (StringUtils.isBlank(nickname)) {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "昵称不能为空!");
			return resultJson;
		}
		if (nickname.length() < 2 || nickname.length() > 16) {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "昵称2~16 位");
			return resultJson;
		}
		Boolean result = safeService.updatenickname(userId, nickname);
		if (result) {
			/**
			 * 调用重新登录接口
			 */
			WebViewUser webUser = loginService.getWebViewUserByUserId(userId);
			WebUtils.sessionLogin(request, response, webUser);
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_TRUE);
			resultJson.put(SafeDefine.ERROR, "修改昵称成功");
			return resultJson;
		} else {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "修改昵称失败,未知错误");
			return resultJson;
		}
	}


	/**
	 * 修改手机号
	 *
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = SafeDefine.CHANGEMOBILE, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public JSONObject changeMobile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(SafeController.class.getName(), SafeDefine.CHANGEMOBILE);
		JSONObject resultJson = new JSONObject();
		Integer userId = WebUtils.getUserId(request);
		WebViewUser user = WebUtils.getUser(request);
		String oldCode = request.getParameter("oldCode");
		String oldMobile = request.getParameter("oldMobile");
		String newCode = request.getParameter("newCode");
		String newMobile = request.getParameter("newMobile");

		if (StringUtils.isNotBlank(user.getMobile())) {
			// =======================如果存在原手机号,表示是更改手机号=======================
			// 加入验证
			if (StringUtils.isBlank(oldCode)) {
				resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
				resultJson.put(SafeDefine.ERROR, "旧手机验证码不能为空!");
				return resultJson;
			}
			if (StringUtils.isBlank(oldMobile)) {
				resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
				resultJson.put(SafeDefine.ERROR, "旧手机号不能为空!");
				return resultJson;
			}
			if (StringUtils.isBlank(newCode)) {
				resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
				resultJson.put(SafeDefine.ERROR, "新手机验证码不能为空!");
				return resultJson;
			}
			if (StringUtils.isBlank(newMobile)) {
				resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
				resultJson.put(SafeDefine.ERROR, "新手机号不能为空!");
				return resultJson;
			}
			if (!user.getMobile().equals(oldMobile)) {
				resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
				resultJson.put(SafeDefine.ERROR, "旧手机号不正确!");
				return resultJson;
			}
			if (oldMobile.equals(newMobile)) {
				resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
				resultJson.put(SafeDefine.ERROR, "修改手机号与原手机号不能相同!");
				return resultJson;
			}
			if (!Validator.isMobile(newMobile)) {
				resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
				resultJson.put(SafeDefine.ERROR, "手机号格式不正确!");
				return resultJson;
			}
			if (loginService.existUser(newMobile)) {
				resultJson.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
				resultJson.put(UserRegistDefine.ERROR, "手机号已存在!");
				return resultJson;
			}
			int cntold = this.registService.updateCheckMobileCode(oldMobile, oldCode, UserRegistDefine.PARAM_TPL_YZYSJH, CustomConstants.CLIENT_PC, UserRegistDefine.CKCODE_YIYAN,
					UserRegistDefine.CKCODE_YIYAN);
			if (cntold == 0) {
				resultJson.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
				resultJson.put(UserRegistDefine.ERROR, "旧手机验证码不正确!");
				return resultJson;
			}
			int cntnew = this.registService.updateCheckMobileCode(newMobile, newCode, UserRegistDefine.PARAM_TPL_BDYSJH, CustomConstants.CLIENT_PC, UserRegistDefine.CKCODE_YIYAN,
					UserRegistDefine.CKCODE_YIYAN);
			if (cntnew == 0) {
				resultJson.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
				resultJson.put(UserRegistDefine.ERROR, "新手机验证码不正确!");
				return resultJson;
			}
			try {
				safeService.updateMobile(userId, newMobile);
				this.registService.updateCheckMobileCode(oldMobile, oldCode, UserRegistDefine.PARAM_TPL_YZYSJH, CustomConstants.CLIENT_PC, UserRegistDefine.CKCODE_YIYAN, UserRegistDefine.CKCODE_USED);
				this.registService.updateCheckMobileCode(newMobile, newCode, UserRegistDefine.PARAM_TPL_YZYSJH, CustomConstants.CLIENT_PC, UserRegistDefine.CKCODE_YIYAN, UserRegistDefine.CKCODE_USED);
				resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_TRUE);
				resultJson.put(SafeDefine.ERROR, "绑定新手机号成功");
				/**
				 * 调用重新登录接口
				 */
				WebViewUser webUser = loginService.getWebViewUserByUserId(userId);
				WebUtils.sessionLogin(request, response, webUser);
				return resultJson;
			} catch (Exception e) {
				resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
				resultJson.put(SafeDefine.ERROR, "绑定新手机号失败,未知错误");
				e.printStackTrace();
				return resultJson;
			}
		} else {
			// =======================如果不存在原手机号,表示是绑定新手机号=======================
			// 加入验证
			if (StringUtils.isBlank(newCode)) {
				resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
				resultJson.put(SafeDefine.ERROR, "验证码不能为空!");
				return resultJson;
			}
			if (StringUtils.isBlank(newMobile)) {
				resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
				resultJson.put(SafeDefine.ERROR, "手机号不能为空!");
				return resultJson;
			}
			if (!Validator.isMobile(newMobile)) {
				resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
				resultJson.put(SafeDefine.ERROR, "手机号格式不正确!");
				return resultJson;
			}
			if (loginService.existUser(newMobile)) {
				resultJson.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
				resultJson.put(UserRegistDefine.ERROR, "手机号已存在!");
				return resultJson;
			}
			int cntnew = this.registService.updateCheckMobileCode(newMobile, newCode, UserRegistDefine.PARAM_TPL_BDYSJH, CustomConstants.CLIENT_PC, UserRegistDefine.CKCODE_YIYAN,
					UserRegistDefine.CKCODE_YIYAN);
			if (cntnew == 0) {
				resultJson.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
				resultJson.put(UserRegistDefine.ERROR, "验证码不正确!");
				return resultJson;
			}

			try {
				safeService.updateMobile(userId, newMobile);
				this.registService.updateCheckMobileCode(newMobile, newCode, UserRegistDefine.PARAM_TPL_YZYSJH, CustomConstants.CLIENT_PC, UserRegistDefine.CKCODE_YIYAN, UserRegistDefine.CKCODE_USED);
				resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_TRUE);
				resultJson.put(SafeDefine.ERROR, "绑定手机号成功");
				/**
				 * 调用重新登录接口
				 */
				WebViewUser webUser = loginService.getWebViewUserByUserId(userId);
				WebUtils.sessionLogin(request, response, webUser);
				return resultJson;
			} catch (Exception e) {
				resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
				resultJson.put(SafeDefine.ERROR, "绑定手机号失败,未知错误");
				e.printStackTrace();
				return resultJson;
			}
		}
	}

	/**
	 * 发送邮件给邮箱（点击按键后走的方法并不是鼠标离开时周的方法）
	 *
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = SafeDefine.SENDEMAIL, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public JSONObject sendEmail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(SafeController.class.getName(), SafeDefine.SENDEMAIL);
		JSONObject resultJson = new JSONObject();
		String email = request.getParameter("email");
		
		// 加入验证
		if (StringUtils.isBlank(email)) {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "邮箱不能为空!");
			return resultJson;
		}
		if (!Validator.isEmailAddress(email)) {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "邮箱格式不正确!");
			return resultJson;
		}
		if (loginService.existEmail(email)) {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "邮箱已被使用,请重新选择邮箱!");
			return resultJson;
		}
		try {
			safeService.sendEmailToUser(WebUtils.getUser(request), email);
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_TRUE);
			resultJson.put(SafeDefine.ERROR, "发送邮件成功");//给新注册用户发一封带着链接的邮件
			return resultJson;
		} catch (Exception e) {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "绑定新邮件失败,未知错误");
			e.printStackTrace();
			return resultJson;
		}
	}

	/**
	 * 接收用户的请求,绑定邮箱
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = SafeDefine.BINDEMAIL, method = RequestMethod.GET)
	public ModelAndView bindEmail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(SafeController.class.getName(), SafeDefine.BINDEMAIL);
		ModelAndView modelAndView = new ModelAndView(SafeDefine.BINDEMAILRESULT);
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		String email = request.getParameter("email");
		// 加入验证
		if (StringUtils.isBlank(key) || !Validator.isNumber(key) || StringUtils.isBlank(value) || StringUtils.isBlank(email)) {
			modelAndView.addObject(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			modelAndView.addObject(SafeDefine.ERROR, "无效的参数!");
			return modelAndView;
		}
		UserBindEmailLog log = safeService.getUserBindEmail(Integer.parseInt(key));
		if (log == null) {
			modelAndView.addObject(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			modelAndView.addObject(SafeDefine.ERROR, "邮件已过期!");
			return modelAndView;
		} else {
			if (new Date().after(log.getEmailActiveUrlDeadtime())) {
				modelAndView.addObject(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
				modelAndView.addObject(SafeDefine.ERROR, "邮件已过期!");
				return modelAndView;
			}
			String validValue = MD5Utils.MD5(MD5Utils.MD5(log.getEmailActiveCode()));
			// String validEmail=MD5Utils.MD5(MD5Utils.MD5(log.getUserEmail()));
			if (key.equals(log.getUserId() + "") && value.equals(validValue) && email.equals(log.getUserEmail())) {
				safeService.updateEmail(log.getUserId(), email, log);
				// add by liuyang 20180301 邮箱修改成功后,发CA认证客户信息修改MQ start
				this.safeService.sendCAMQ(log.getUserId());
				// add by liuyang 20180301 邮箱修改成功后,发CA认证客户信息修改MQ end
				modelAndView.addObject(SafeDefine.STATUS, SafeDefine.STATUS_TRUE);
				modelAndView.addObject(SafeDefine.ERROR, "绑定成功!");
				//修改成功之后重新登录
				WebViewUser webUser = loginService.getWebViewUserByUserId(log.getUserId());
				WebUtils.sessionLogin(request, response, webUser);
				return modelAndView;
			} else {
				modelAndView.addObject(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
				modelAndView.addObject(SafeDefine.ERROR, "绑定失败,未知原因!");
				return modelAndView;
			}
		}
	}

	/**
	 * 更新sms配置
	 *
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = SafeDefine.UPDATESMSCONFIG, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public JSONObject updateSmsConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(SafeController.class.getName(), SafeDefine.UPDATESMSCONFIG);
		JSONObject resultJson = new JSONObject();
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		// 加入验证
		if (StringUtils.isBlank(key)) {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "无效的参数!");
			return resultJson;
		}
		if (!key.equals("rechargeSms") && !key.equals("withdrawSms") && !key.equals("investSms") && !key.equals("recieveSms")) {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "无效的参数!");
			return resultJson;
		}
		if (StringUtils.isBlank(value) || (!value.equals("0") && !value.equals("1"))) {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "无效的参数!");
			return resultJson;
		}
		safeService.updateSmsConfig(WebUtils.getUserId(request), key, Integer.parseInt(value));
		/**
		 * 调用重新登录接口
		 */
		WebViewUser webUser = loginService.getWebViewUserByUserId(WebUtils.getUserId(request));
		WebUtils.sessionLogin(request, response, webUser);
		resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_TRUE);
		resultJson.put(SafeDefine.ERROR, "修改成功!");
		return resultJson;
	}

	/**
	 * 上传头像
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = SafeDefine.UPLOAD_AVATAR_ACTION, method = RequestMethod.POST)
	public JSONObject uploadAvatarAction(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(SafeController.class.getName(), SafeDefine.UPLOAD_AVATAR_ACTION);
		JSONObject ret = new JSONObject();
		String image = request.getParameter("image");

		// 用户id

		try {
			WebViewUser user = WebUtils.getUser(request);
			// 取得用户ID
			Integer userId = user.getUserId();

			if (userId != null) {
				BASE64Decoder decoder = new BASE64Decoder();
				byte[] decodedBytes = decoder.decodeBuffer(image); // 将字符串格式的image转为二进制流（biye[])的decodedBytes

				String filePhysicalPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.physical.path"));
				// 实际物理路径前缀2
				String fileUploadTempPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.head.path"));

				// 如果文件夹(前缀+后缀)不存在,则新建文件夹
				String logoRealPathDir = filePhysicalPath + fileUploadTempPath;
				File logoSaveFile = new File(logoRealPathDir);
				if (!logoSaveFile.exists()) {
					logoSaveFile.mkdirs();
				}

				// 生成图片文件名
				String fileRealName = String.valueOf(new Date().getTime());
				fileRealName = "appIconImg_" + userId + fileRealName + ".png";
				String imgFilePath = logoSaveFile + "/" + fileRealName; // 指定图片要存放的位置

				FileOutputStream out = new FileOutputStream(imgFilePath); // 新建一个文件输出器，并为它指定输出位置imgFilePath
				out.write(decodedBytes); // 利用文件输出器将二进制格式decodedBytes输出
				out.close(); // 关闭文件输出器
				// 保存到数据库的路径=上传文件的CDNURL+图片的文件名
				String iconUrl = fileRealName;
				// 保存到数据库
				safeService.updateUserIconImg(userId, iconUrl);
				WebViewUser webUser = loginService.getWebViewUserByUserId(userId);
				WebUtils.sessionLogin(request, response, webUser);
				ret.put("iconUrl", imgFilePath);
				ret.put("status", "0");
				ret.put("statusDesc", "头像上传成功");
			} else {
				ret.put("status", "1");
				ret.put("statusDesc", "用户信息不存在");
			}
		} catch (Exception e) {
			ret.put("err", e.getMessage());
			ret.put("status", "1");
			ret.put("statusDesc", "头像修改失败,请刷新重试！");
		}
		LogUtil.endLog(SafeController.class.getName(), SafeDefine.UPLOAD_AVATAR_ACTION);
		return ret;
	}

	/**
	 * 头像上传页面初始化
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = SafeDefine.UPLOAD_AVATAR_INIT_ACTION, method = RequestMethod.GET)
	public ModelAndView uploadAvatarInitAction(HttpServletRequest request) throws Exception {
		LogUtil.startLog(SafeController.class.getName(), SafeDefine.UPLOAD_AVATAR_INIT_ACTION);
		ModelAndView modeAndView = new ModelAndView(SafeDefine.UPLOAD_AVATAR_INIT_PATH);
		WebViewUser user = WebUtils.getUser(request);
		if(StringUtils.isNotEmpty(user.getIconurl())){
		    modeAndView.addObject("iconUrl", user.getIconurl());
		}
		LogUtil.endLog(SafeController.class.getName(), SafeDefine.UPLOAD_AVATAR_INIT_ACTION);
		return modeAndView;
	}

	/**
	 * 绑定邮箱页面初始化
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(SafeDefine.BINDING_EMAIL_ACTION)
	public ModelAndView initEmail(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(SafeController.class.getName(), SafeDefine.BINDING_EMAIL_ACTION);
		String isUpdate = request.getParameter("isUpdate");
		WebViewUser user = WebUtils.getUser(request);
		UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
		if(StringUtils.isNotEmpty(isUpdate)&&"isUpdate".equals(isUpdate)){
			userOperationLogEntity.setOperationType(9);
		}else {
			userOperationLogEntity.setOperationType(8);
		}
		userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
		userOperationLogEntity.setPlatform(0);
		userOperationLogEntity.setRemark("");
		userOperationLogEntity.setOperationTime(new Date());
		userOperationLogEntity.setUserName(user.getUsername());
		userOperationLogEntity.setUserRole(user.getRoleId());
		loginService.sendUserLogMQ(userOperationLogEntity);
		ModelAndView modelAndView = new ModelAndView(SafeDefine.BINDING_EMAIL_PATH);// 跳转画面
		return modelAndView;
	}

	/**
	 * 修改 登录密码 页面初始化
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(SafeDefine.MODIFY_CODE_ACTION)
	public ModelAndView modifyCode(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(SafeController.class.getName(), SafeDefine.MODIFY_CODE_ACTION);
		ModelAndView modelAndView = new ModelAndView(SafeDefine.MODIFY_CODE_PATH);// 跳转画面
		try {
//			KeyPair kp = RSAJSPUtil.generateKeyPair();
//	        RSAPublicKey pubk = (RSAPublicKey) kp.getPublic();// 生成公钥
//	        RSAPrivateKey prik = (RSAPrivateKey) kp.getPrivate();// 生成私钥
//
//	        String publicKeyExponent = pubk.getPublicExponent().toString(16);// 16进制
//	        String publicKeyModulus = pubk.getModulus().toString(16);// 16进制
//	        request.getSession().setAttribute("prik", prik);
//	        modelAndView.addObject("pubexponent", publicKeyExponent);
//	        modelAndView.addObject("pubmodules", publicKeyModulus);
			modelAndView.addObject("pubexponent", "10001");
			modelAndView.addObject("pubmodules", RSAJSPUtil.getPunlicKeys());
		} catch (Exception e) {
			logger.error("修改密码时，生成密码加密密钥错误",e);
		} 
		return modelAndView;
	}
	
	/**
	 * 修改 登录密码 成功画面
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(SafeDefine.SHOW_MODIFY_SUCCESS)
	public ModelAndView showModifySuccess(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(SafeController.class.getName(), SafeDefine.SHOW_MODIFY_SUCCESS);
		ModelAndView modelAndView = new ModelAndView(SafeDefine.MODIFY_SUCCESS_PATH);// 跳转画面  
		return modelAndView;
	}
	
	/**
	 * 紧急联系人设置页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(SafeDefine.CONTACT_SET_PAGE_ACTION)
	public ModelAndView contactSetPage(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(SafeController.class.getName(), SafeDefine.CONTACT_SET_PAGE_ACTION);
		ModelAndView modelAndView = new ModelAndView(SafeDefine.CONTACT_SET_PAGE_PATH);// 跳转画面
		WebViewUser webViewUser = WebUtils.getUser(request);
		// 紧急联系人类型
		List<ParamName> paramList = safeService.getParamNameList("USER_RELATION");
		if(paramList == null || paramList.size() == 0){
			return modelAndView;
		}
		//取选中联系人关系
		if(webViewUser.getUsersContract() != null){
			for (int i = 0; i < paramList.size(); i++) {
				if(webViewUser.getUsersContract().getRelation() != null 
						&& paramList.get(i).getNameCd().equals(String.valueOf(webViewUser.getUsersContract().getRelation()))){
					modelAndView.addObject("checkRelationName", paramList.get(i).getName());
					modelAndView.addObject("checkRelationId", paramList.get(i).getNameCd());
				}
			}
			//现有联系人
			modelAndView.addObject("usersContract", webViewUser.getUsersContract());
		}else{
			modelAndView.addObject("checkRelationName", "");
			modelAndView.addObject("checkRelationId", "");
			//现有联系人
			modelAndView.addObject("usersContract", new UsersContract());
		}
		//关系列表
		modelAndView.addObject("relationList", paramList);
		return modelAndView;
	}
	

	/**
	 * 修改紧急联系人
	 *
	 * @return
	 * @throws Exception
	 */
	
	@ResponseBody
	@RequestMapping(value = SafeDefine.CONTACT_SET_UPDATE_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public JSONObject updateRelation(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(SafeController.class.getName(), SafeDefine.CONTACT_SET_UPDATE_ACTION);
		//返回对象
		JSONObject resultJson = new JSONObject();
		Integer userId = WebUtils.getUserId(request);
		String relationId = request.getParameter("relationId");
		String rlName = request.getParameter("rlName");
		String rlPhone = request.getParameter("rlPhone");
		if (StringUtils.isBlank(relationId)) {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "紧急联系人关系不能为空!");
			return resultJson;
		}
		if (StringUtils.isBlank(rlName)) {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "紧急联系人姓名不能为空!");
			return resultJson;
		}
		if (StringUtils.isBlank(rlPhone)) {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "紧急联系电话不能为空!");
			return resultJson;
		}
		if (rlName.length() < 2 || rlName.length() > 4) {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "紧急联系人姓名长度2-4位!");
			return resultJson;
		}
		if (rlPhone.length() != 11 || !Validator.isMobile(rlPhone)) {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "紧急联系电话格式不正确!");
			return resultJson;
		}
		// 紧急联系人类型
		List<ParamName> paramList = safeService.getParamNameList("USER_RELATION");
		Boolean validRelationId = false;
		for (ParamName param : paramList) {
			if (relationId.equals(param.getNameCd())) {
				validRelationId = true;
				break;
			}
		}
		if (!validRelationId) {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "无效的紧急联系人关系!");
			return resultJson;
		}
		UsersContract contract = new UsersContract();
		contract.setRelation(Integer.parseInt(relationId));
		contract.setRlName(rlName);
		contract.setRlPhone(rlPhone);
		contract.setUserId(userId);
		contract.setAddtime(GetDate.getNowTime10());
		Boolean result = safeService.updateRelation(userId, contract);
		if (result) {
			/**
			 * 调用重新登录接口
			 */
			WebViewUser webUser = loginService.getWebViewUserByUserId(userId);
			WebUtils.sessionLogin(request, response, webUser);
		} else {
			resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_FALSE);
			resultJson.put(SafeDefine.ERROR, "修改紧急联系人失败,未知错误");
			return resultJson;
		}
		resultJson.put(SafeDefine.STATUS, SafeDefine.STATUS_TRUE);
		resultJson.put(SafeDefine.ERROR, "修改紧急联系人成功");
		return resultJson;
	}

	
	/**
	 * 修改紧急联系人成功页
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = SafeDefine.CONTACT_SET_SUCCESS_ACTION)
	public ModelAndView contactSetSuccess(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(SafeController.class.getName(), SafeDefine.CONTACT_SET_SUCCESS_ACTION);
		ModelAndView modelAndView = new ModelAndView(SafeDefine.CONTACT_SET_SUCCESS_PATH);// 跳转画面 
		LogUtil.endLog(SafeController.class.getName(), SafeDefine.CONTACT_SET_SUCCESS_ACTION);
		return modelAndView;
	}
	

	/**
	 * 检查输入的 原登录密码 是否已存在
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = SafeDefine.CHECK_ORIGIN_PW, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public boolean checkPw(HttpServletRequest request) {
		LogUtil.startLog(SafeController.class.getName(), SafeDefine.CHECK_ORIGIN_PW);

		WebViewUser user = WebUtils.getUser(request);
		Integer userId = user.getUserId();

		String pw = request.getParameter("oldPassword");
		
		if (pw != null && !"".equals(pw.trim())) {
			
			//RSAPrivateKey prik = (RSAPrivateKey) request.getSession().getAttribute("prik");

	        pw = RSAJSPUtil.rsaToPassword(pw);
	        
			if (!safeService.validPassword(userId, pw)) { /*一致返回true*/
				return false;
			} else {
				return true;
			}
		} 
		//画面未输入原密码
		else {
			return false;
		}

	}
	
    /**
     * 消息通知页面初始化
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = SafeDefine.MESSAGE_NOTIFICATION_INIT_ACTION, method = RequestMethod.GET)
    public ModelAndView messageNotificationInitAction(HttpServletRequest request) throws Exception {
        LogUtil.startLog(SafeController.class.getName(), SafeDefine.MESSAGE_NOTIFICATION_INIT_ACTION);
        ModelAndView modeAndView = new ModelAndView(SafeDefine.MESSAGE_NOTIFICATION_INIT_PATH);
        WebViewUser user = WebUtils.getUser(request);

        modeAndView.addObject("user",user);
        LogUtil.endLog(SafeController.class.getName(), SafeDefine.MESSAGE_NOTIFICATION_INIT_ACTION);
        return modeAndView;
    }
    

    /**
     * 消息通知页面初始化
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = SafeDefine.UPDATE_MESSAGE_NOTIFICATION_ACTION, method = RequestMethod.POST)
    public JSONObject updateMessageNotificationAction(HttpServletRequest request,HttpServletResponse response,Users user) {
        LogUtil.startLog(SafeController.class.getName(), SafeDefine.UPDATE_MESSAGE_NOTIFICATION_ACTION);
        JSONObject ret = new JSONObject();
        WebViewUser webViewUser = WebUtils.getUser(request);
        user.setUserId(webViewUser.getUserId());
        int flag=safeService.updateMessageNotificationAction(user);
        if(flag>0){
            WebViewUser webUser = loginService.getWebViewUserByUserId(webViewUser.getUserId());
            WebUtils.sessionLogin(request, response, webUser); 
            ret.put("message","更新成功");  
            ret.put("status", "0");
        }else{
            ret.put("message","更新失败,请刷新再试!");  
            ret.put("status", "1");
        }
        
        LogUtil.endLog(SafeController.class.getName(), SafeDefine.UPDATE_MESSAGE_NOTIFICATION_ACTION);
        return ret;
    }
    //日期格式修改
	private String setDateFormat(String strDate) {
		if (StringUtils.isNotBlank(strDate)) {
			try {
				SimpleDateFormat smp = new SimpleDateFormat("yyyyMMdd");
				Date dateForm = smp.parse(strDate);
				SimpleDateFormat smp1 = new SimpleDateFormat("yyyy-MM-dd");
				String returnDate = smp1.format(dateForm);
				return returnDate;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
