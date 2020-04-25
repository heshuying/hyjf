/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.wechat.controller.user.loginpassword;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.security.utils.RSAJSPUtil;
import com.hyjf.common.util.StringUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.service.loginpassword.WxLoginPasswordService;

import com.hyjf.wechat.util.AppUserToken;
import com.hyjf.wechat.util.SecretUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fuqiang
 * @version WxLoginPasswordController, v0.1 2018/3/23 10:39
 */
@RestController
@RequestMapping(WxLoginPasswordDefine.REQUEST_MAPPING)
public class WxLoginPasswordController {

	@Autowired
	private WxLoginPasswordService wxLoginPasswordService;

	private Logger logger = LoggerFactory.getLogger(WxLoginPasswordController.class);

	/**
	 * 修改登录密码
	 * 
	 * @param request
	 * @return
	 */
	@SignValidate
	@RequestMapping(value = WxLoginPasswordDefine.MODIFY_PASSWORD_ACTION)
	public JSONObject modifyPassword(HttpServletRequest request) {
        logger.info("修改密码开始...");
		JSONObject ret = new JSONObject();

//		String userId = (String) request.getAttribute("userId");
        Integer userId = (Integer) request.getAttribute("userId");
        // 新密码
		String newPassword = request.getParameter("newPassword");
		// 旧密码
		String oldPassword = request.getParameter("oldPassword");
		
		//密码解密
		try {
			newPassword = RSAJSPUtil.rsaToPassword(newPassword);
			oldPassword = RSAJSPUtil.rsaToPassword(oldPassword);
		} catch (Exception e) {
			logger.error("用户"+userId+"修改密码失败，密码解密异常！",e);
		}

		// 检查参数正确性
		if (Validator.isNull(userId) || Validator.isNull(newPassword) || Validator.isNull(oldPassword)) {
			ret.put("status", "997");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		try {
			// 验证旧密码
			Integer result = wxLoginPasswordService.queryOldPassword(String.valueOf(userId), oldPassword);
			switch (result) {
			case 0:
				// 验证成功
				break;
			case -1:
				ret.put("status", "99");
				ret.put("statusDesc", "旧密码不正确");
				return ret;
			case -2:
				ret.put("status", "99");
				ret.put("statusDesc", "用户不存在");
				return ret;
			case -3:
				ret.put("status", "99");
				ret.put("statusDesc", "存在多个相同用户");
				return ret;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("校验用户密码失败...");
			ret.put("status", "99");
			ret.put("statusDesc", e.getMessage());
		}

		// 业务逻辑
		try {
			if (modifyPassword(ret, String.valueOf(userId), newPassword))
				return ret;
		} catch (Exception e) {
			logger.info("修改密码发生错误...");
			ret.put("status", "99");
			ret.put("statusDesc", "修改密码发生错误");
		}
        logger.info("修改密码结束...");
		return ret;
	}

	/**
	 * 找回登录密码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = WxLoginPasswordDefine.RESET_PASSWORD_ACTION)
	public JSONObject resetPassword(HttpServletRequest request, SendSmsVo sendSmsVo) {

		JSONObject ret = new JSONObject();

        String mobile = request.getParameter("mobile");

        String userId = wxLoginPasswordService.queryUserIdByMobile(mobile);

        if (StringUtils.isBlank(userId)) {
            ret.put("status", "99");
            ret.put("statusDesc", "用户不存在");
            return ret;
        }

        // 新密码
		String newPassword = request.getParameter("newPassword");
		//密码解密
		newPassword = RSAJSPUtil.rsaToPassword(newPassword);

        // 检查参数正确性
        if (Validator.isNull(userId) || Validator.isNull(newPassword) || Validator.isNull(sendSmsVo.getSmscode())) {
            ret.put("status", "997");
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }

		// 校验验证码
        JSONObject jsonObject = wxLoginPasswordService.validateVerificationCoden(sendSmsVo,true);
        if (!"000".equals(jsonObject.get("status"))) {
            ret.put("status", "018");
            ret.put("statusDesc", "验证码无效");
            return ret;
        }

		// 业务逻辑
		try {
			modifyPassword(ret, userId, newPassword);
		} catch (Exception e) {
			logger.info("修改密码失败...");
			ret.put("status", "99");
			ret.put("statusDesc", "失败");
		}

		return ret;
	}

	/**
	 * @author fengping
	 * 微信端获取短信验证码
	 * @param request
	 * @return
	 */
	@RequestMapping(value = WxLoginPasswordDefine.SEND_VERIFICATIONCODE_ACTION)
	public JSONObject sendVerificationCode(HttpServletRequest request,SendSmsVo sendSmsVo) {
		return wxLoginPasswordService.sendCode(sendSmsVo);
	}

	/**
	 * @author fengping
	 * 微信端验证短信验证码
	 * @param request
	 * @return
	 */
	@SignValidate
	@RequestMapping(value = WxLoginPasswordDefine.VALIDATE_VERIFICATIONCODE_ACTION)
	public JSONObject validateVerificationCoden(HttpServletRequest request,SendSmsVo sendSmsVo) {
		return wxLoginPasswordService.validateVerificationCoden(sendSmsVo,false);
	}

	/**
	 * 根据useId修改用户密码
	 * 
	 * @param ret
	 * @param userId
	 * @param newPassword
	 * @return
	 */
	private boolean modifyPassword(JSONObject ret, String userId, String newPassword) {
		if (newPassword.length() < 6 || newPassword.length() > 16) {
			ret.put("status", "99");
			ret.put("statusDesc", "密码长度6-16位");
			return true;
		}

		boolean hasNumber = false;

		for (int i = 0; i < newPassword.length(); i++) {
			if (Validator.isNumber(newPassword.substring(i, i + 1))) {
				hasNumber = true;
				break;
			}
		}
		if (!hasNumber) {
			ret.put("status", "99");
			ret.put("statusDesc", "密码必须包含数字");
			return true;
		}
		String regEx = "^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]{6,20})$";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(newPassword);
		if (!m.matches()) {
			ret.put("status", "99");
			ret.put("statusDesc", "密码必须由数字和字母组成，如abc123");
			return true;
		}
		boolean success = wxLoginPasswordService.updatePasswordAction(userId, newPassword);

		if (success) {
			logger.info("修改密码成功...");
			ret.put("status", "000");
			ret.put("statusDesc", "修改密码成功");
		} else {
			logger.info("修改密码失败...");
			ret.put("status", "99");
			ret.put("statusDesc", "修改密码失败");
		}
		return false;
	}

	/**
	 * 重置登录密码(手机号显示)
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = WxLoginPasswordDefine.RESET_PASSWORD)
	public JSONObject displayPhone(HttpServletRequest request, SendSmsVo sendSmsVo) {
		JSONObject ret = new JSONObject();
		String sign = sendSmsVo.getSign();
		if (StringUtils.isNotBlank(sign)) {
			// 取得用户ID
			AppUserToken at = SecretUtil.getUserId(sign);
			Integer userId = at.getUserId();
			//根据用户Id查询用户
			Users users = wxLoginPasswordService.queryUserByUserId(userId);
			if (users != null && users.getMobile() != null) {
				String mobile = users.getMobile();
				ret.put("mobile", mobile);
				ret.put("status","000");
				ret.put("statusDesc","成功");
				return ret;
			}
			ret.put("mobile","");
			ret.put("status","99");
			ret.put("statusDesc","获取用户信息失败");
			return ret;
		}
		ret.put("mobile","");
		ret.put("status","000");
		ret.put("statusDesc","用户未登录");
		return ret;
	}
}
