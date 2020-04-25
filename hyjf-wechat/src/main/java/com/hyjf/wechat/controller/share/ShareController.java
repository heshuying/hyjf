package com.hyjf.wechat.controller.share;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;
import com.hyjf.wechat.base.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author xiasq
 * @version ShareController, v0.1 2018/5/10 9:11
 */
@Controller
@RequestMapping("/wx/share")
public class ShareController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(ShareController.class);

	@RequestMapping(value = "/doShare", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Map<String, Object> doShare(HttpServletRequest request, @RequestBody Map<String, Object> params) {
		// String url = "http://" + request.getServerName() + request.getContextPath();
		String url = (String) params.get("url");

		// 获取用户id
		Integer userId = requestUtil.getRequestUserId(request);
		logger.info("当前登录用户: {}", userId);
		return WxConfigUtil.getSignature(url, PropUtils.getSystem(CustomConstants.WECHAT_APP_APPID),
				PropUtils.getSystem(CustomConstants.WECHAT_APP_SECRET), userId);
	}
}
