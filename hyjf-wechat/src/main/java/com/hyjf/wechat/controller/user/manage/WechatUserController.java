package com.hyjf.wechat.controller.user.manage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.service.regist.UserService;

/**
 * 微信用户注册controller
 * 
 * @author jijun
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月27日
 * @see 上午09：11
 */
@Controller
@RequestMapping(value = WechatUserDefine.REQUEST_MAPPING)
public class WechatUserController extends BaseController {

	public static final String SOA_COUPON_KEY = PropUtils.getSystem("release.coupon.accesskey");

	@Autowired
	private UserService userService;

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;// 指定用smsProcesser类来初始化

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;

	/**
	 * 验证验证码
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/validateVerificationCodeAction", method = RequestMethod.POST)
	public JSONObject validateVerificationCodeAction(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(WechatUserDefine.THIS_CLASS, WechatUserDefine.VALIDATE_VERIFICATIONCODE_ACTION);
		JSONObject ret = new JSONObject();
		ret.put("request", WechatUserDefine.VALIDATE_VERIFICATIONCODE_REQUEST);

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
		if (!(verificationType.equals(WechatUserDefine.PARAM_TPL_ZHUCE)
				|| verificationType.equals(WechatUserDefine.PARAM_TPL_ZHAOHUIMIMA)
				|| verificationType.equals(WechatUserDefine.PARAM_TPL_BDYSJH)
				|| verificationType.equals(WechatUserDefine.PARAM_TPL_YZYSJH))) {
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

			int cnt = userService.updateCheckMobileCode(mobile, verificationCode, verificationType,
					WechatUserDefine.CKCODE_NEW, WechatUserDefine.CKCODE_YIYAN);

			if (cnt > 0) {
				ret.put("status", "000");
				ret.put("statusDesc", "验证验证码成功");
			} else {
				ret.put("status", "99");
				ret.put("statusDesc", "验证码无效");
			}

		} catch (Exception e) {
			ret.put("status", "99");
			ret.put("statusDesc", "验证验证码发生错误");
		}

		LogUtil.endLog(WechatUserDefine.THIS_CLASS, WechatUserDefine.VALIDATE_VERIFICATIONCODE_ACTION);
		return ret;
	}

}
