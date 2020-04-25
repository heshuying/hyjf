package com.hyjf.api.web.regist;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;

/**
 * 用户注册外部用接口
 * 
 * @author zhangjinpeng
 * 
 */
@Controller
@RequestMapping(value = UserRegistDefine.REGIST_REQUEST_MAPPING_CLASS)
public class UserRegistServer extends BaseController {

	private static final String THIS_CLASS = UserRegistServer.class.getName();

	@Autowired
	UserRegistService userRegistService;
	@Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser; 

	/**
	 * 用户注册
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = UserRegistDefine.REGIST_REQUEST_ACTION, method = RequestMethod.POST)
	public JSONObject userRegist(HttpServletRequest request,
			UserRegistBean paramBean) {
		LogUtil.startLog(THIS_CLASS, UserRegistDefine.REGIST_REQUEST_ACTION);
		LogUtil.infoLog(THIS_CLASS, UserRegistDefine.REGIST_REQUEST_ACTION,
				JSONObject.toJSONString(paramBean));
		JSONObject jsonObject = new JSONObject();
		// 画面验证(暂时不用)
		JSONObject info = new JSONObject();

		try {
			// 注册信息校验
			jsonObject = this.validatorFieldCheckRegist(info, paramBean);
			if (jsonObject == null || jsonObject.isEmpty()) {
				String password = userRegistService.insertRegistUser(paramBean);
				// 状态
				jsonObject.put(UserRegistDefine.STATUS,
						UserRegistDefine.SUCCESS_CODE);
				// 第三方平台用户名
				jsonObject.put(UserRegistDefine.USER_NAME,
						paramBean.getUsername());
				// 汇盈平台用户名
				jsonObject.put(UserRegistDefine.USER_NAMEP,
						paramBean.getUsernameSelf());
				// 注册时间戳
				jsonObject.put(UserRegistDefine.REG_TIME,
						paramBean.getRegtime());

				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("val_username", paramBean.getUsernameSelf());
				paramMap.put("val_password", password);
				// 短信通知用户注册成功，同时发送用户名和密码
		        SmsMessage smsMessage =
                        new SmsMessage(null, paramMap, paramBean.getMobile(), null, MessageDefine.SMSSENDFORMOBILE, null,
                        		UserRegistDefine.TPL_SMS_AUTO_REGIST_NAME, CustomConstants.CHANNEL_TYPE_NORMAL);
		        smsProcesser.gather(smsMessage);
			} else {
				jsonObject.put(UserRegistDefine.STATUS,
						UserRegistDefine.FAILED_CODE);
			}
		} catch (Exception e) {
			// 系统异常 通讯失败
			jsonObject.put(UserRegistDefine.STATUS,
					UserRegistDefine.FAILED_CODE);
			jsonObject.put(UserRegistDefine.ERROR_MESSAGE, PropUtils
					.getMessage(UserRegistDefine.ERRORS_EXCEPTION_INFO));
		}
		LogUtil.endLog(THIS_CLASS, UserRegistDefine.REGIST_REQUEST_ACTION);
		return jsonObject;
	}

	/**
	 * 注册信息校验
	 * 
	 * @param info
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	private JSONObject validatorFieldCheckRegist(JSONObject info,
			UserRegistBean paramBean) throws Exception {
		JSONObject jo = new JSONObject();
		// 验证签名
		if (!this.checkSign(paramBean)) {
			// 签名验证失败
			jo.put(UserRegistDefine.ERROR_MESSAGE,
					PropUtils.getMessage(UserRegistDefine.ERRORS_SIGN));
			return jo;
		}
		// 来源渠道校验
		if (!ValidatorCheckUtil.validateRequired(info, null, null,
				paramBean.getFrom())) {
			// 非空
			jo.put(UserRegistDefine.ERROR_MESSAGE,
					PropUtils.getMessage(UserRegistDefine.ERRORS_FROM));
			return jo;
		}

		// 用户名校验
		if (!ValidatorCheckUtil.validateRequired(info, null, null,
				paramBean.getUsername())) {
			// 非空
			jo.put(UserRegistDefine.ERROR_MESSAGE, PropUtils
					.getMessage(UserRegistDefine.ERRORS_USERNAME_REQUIRED));
			return jo;

		} else if (!ValidatorCheckUtil.validateAlphaNumericRange(info, null,
				null, paramBean.getUsername(), 6, 16, false)) {
			// 格式不正确
			jo.put(UserRegistDefine.ERROR_MESSAGE, PropUtils
					.getMessage(UserRegistDefine.ERRORS_USERNAME_OTHER));
			return jo;

		}

		// 手机号
		if (!ValidatorCheckUtil.validateRequired(info, null, null,
				paramBean.getMobile())) {
			jo.put(UserRegistDefine.ERROR_MESSAGE, PropUtils
					.getMessage(UserRegistDefine.ERRORS_MOBILE_REQUIRED));
			return jo;

		} else if (!ValidatorCheckUtil.validateMobile(info, null, null,
				paramBean.getMobile(), 11, false)) {
			jo.put(UserRegistDefine.ERROR_MESSAGE,
					PropUtils.getMessage(UserRegistDefine.ERRORS_MOBILE_OTHER));
			return jo;
		}

		// 用户的注册邮箱
		if (!ValidatorCheckUtil.validateMailAndMaxLength(info, null, null,
				paramBean.getEmail(), 32, false)) {
			jo.put(UserRegistDefine.ERROR_MESSAGE,
					PropUtils.getMessage(UserRegistDefine.ERRORS_EMAIL_OTHER));
			return jo;
		}

		// 请求的时间戳
		if (!ValidatorCheckUtil.validateRequired(info, null, null,
				paramBean.getTimestamp())) {
			jo.put(UserRegistDefine.ERROR_MESSAGE, PropUtils
					.getMessage(UserRegistDefine.ERRORS_TIMESTAMP_REQUIRED));
			return jo;
		}

		if (jo == null || jo.isEmpty()) {

			// 有效渠道合作商校验
			int cnt = userRegistService.countUserPlat(paramBean.getFrom());
			if (cnt == 0) {
				jo.put(UserRegistDefine.ERROR_MESSAGE,
						PropUtils.getMessage(UserRegistDefine.ERRORS_FROM));
				return jo;
			}
			// 校验用户是否已经存在
			if (userRegistService.checkUsernameExists(paramBean)) {
				jo.put(UserRegistDefine.ERROR_MESSAGE, PropUtils
						.getMessage(UserRegistDefine.ERRORS_USERNAME_EXISTS));
				return jo;
			}
			// 检查手机号码唯一性
			cnt = userRegistService.countUserByMobile(paramBean.getMobile());
			if (cnt > 0) {
				jo.put(UserRegistDefine.ERROR_MESSAGE, PropUtils
						.getMessage(UserRegistDefine.ERRORS_MOBILE_EXISTS));
				return jo;
			}
			// 检查email唯一性
			if (StringUtils.isNotEmpty(paramBean.getEmail())) {
				cnt = userRegistService.countUserByEmail(paramBean.getEmail());
				if (cnt > 0) {
					jo.put(UserRegistDefine.ERROR_MESSAGE, PropUtils
							.getMessage(UserRegistDefine.ERRORS_EMAIL_EXISTS));
					return jo;
				}
			}

		}
		return jo;
	}

	/**
	 * 验证签名
	 * 
	 * @param paramBean
	 * @return
	 */
	private boolean checkSign(UserRegistBean paramBean) {

		String from = paramBean.getFrom();
		String username = paramBean.getUsername();
		String mobile = paramBean.getMobile();
		// email可选
		String email = paramBean.getEmail() == null ? StringUtils.EMPTY
				: paramBean.getEmail();
		String timestamp = paramBean.getTimestamp();
		String accessKey = PropUtils
				.getSystem(UserRegistDefine.RELEASE_TANLIULIU_ACCESSKEY);
		String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + email
				+ from + mobile + timestamp + username + accessKey));
		return StringUtils.equals(sign, paramBean.getSign()) ? true : false;

	}
}
