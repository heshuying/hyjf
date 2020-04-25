package com.hyjf.app.user.bindCard;
/**
 * Description:出借控制器
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 * Created at: 2015年12月4日 下午2:32:36
 * Modification History:
 * Modified by :
 */

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.chinapnr.ChinapnrService;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogWithBLOBs;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

/**
 * 用户绑卡
 *
 * @author HBZ
 */
@Controller
@RequestMapping(value = UserBindCardDefine.REQUEST_MAPPING)
public class UserBindCardController extends BaseController {

	/** THIS_CLASS */
	private static final String THIS_CLASS = UserBindCardController.class.getName();

	@Autowired
	private ChinapnrService chinapnrService;

	@Autowired
	private UserBindCardService userBindCardService;

	/**
	 * 用户绑卡
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(UserBindCardDefine.REQUEST_MAPPING)
	// @RequiresRoles(ShiroConstants.ROLE_NORMAL_USER)
	public ModelAndView bindCard(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(UserBindCardController.class.toString(), UserBindCardDefine.REQUEST_MAPPING);
		ModelAndView modelAndView = new ModelAndView(UserBindCardDefine.JSP_CHINAPNR_SEND);

		// 检查参数
		JSONObject checkResult = checkParam(request);
		if (checkResult != null) {
			// TODO
			modelAndView = new ModelAndView(UserBindCardDefine.BINDCARD_ERROR_PATH);
			modelAndView.addObject("message", checkResult.get(CustomConstants.APP_STATUS_DESC));
			return modelAndView;
		}

		// 唯一标识
		String sign = request.getParameter("sign");
		Integer userId = SecretUtil.getUserId(sign);
		String userName = SecretUtil.getUserName(sign);
		// 取得用户在汇付天下的客户号
		AccountChinapnr accountChinapnrTender = userBindCardService.getAccountChinapnr(userId);
		if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getChinapnrUsrcustid())) {
			// TODO
			modelAndView = new ModelAndView(UserBindCardDefine.BINDCARD_ERROR_PATH);
			modelAndView.addObject("message", "用户未开户");
			return modelAndView;
		}

		Long chinapnrUsrcustidTender = accountChinapnrTender.getChinapnrUsrcustid();

		// 调用汇付接口(4.2.2 用户绑卡接口)
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10);// 接口版本号
		bean.setCmdId(ChinaPnrConstant.CMDID_USER_BIND_CARD); // 每一种消息类型代表一种交易，绑卡为
																// UserBindCard
		bean.setUsrCustId(String.valueOf(chinapnrUsrcustidTender));// 用户客户号
		// bean.setBgRetUrl(ChinapnrUtil.getBgRetUrl()); // 商户后台应答地址(必须)
		bean.setBgRetUrl(PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + UserBindCardDefine.REQUEST_MAPPING + UserBindCardDefine.RETURN_MAPPING);// 商户后台应答地址(必须)
        bean.setPageType("1");//app 应用风格页面（无标题）
		// 插值用参数
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", String.valueOf(userId));
		params.put("userName", userName);
		params.put("ip", GetCilentIP.getIpAddr(request));

		// 跳转到汇付天下画面
		try {
			modelAndView = ChinapnrUtil.callApi(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}

		LogUtil.endLog(UserBindCardController.class.toString(), UserBindCardDefine.REQUEST_MAPPING);
		return modelAndView;
	}

	/**
	 * 用户绑卡后处理
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(UserBindCardDefine.RETURN_MAPPING)
	public ModelAndView bindCardReturn(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) {
		LogUtil.startLog(THIS_CLASS, UserBindCardDefine.RETURN_MAPPING, "[绑卡完成后,回调开始]");
		bean.convert();
		LogUtil.debugLog(THIS_CLASS, UserBindCardDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");

		boolean updateFlag = false;
		// 取得更新用UUID
		String uuid = request.getParameter("uuid");
		if (Validator.isNotNull(uuid)) {
			// 取得检证数据
			ChinapnrExclusiveLogWithBLOBs record = chinapnrService.selectChinapnrExclusiveLog(Long.parseLong(uuid));
			// 如果检证数据状态为未发送
			if (record != null) {
				// 检证通过时
				if (ChinaPnrConstant.STATUS_VERTIFY_OK.equals(record.getStatus())) {
					// 将状态更新成[2:处理中]
					record.setId(Long.parseLong(uuid));
					record.setResult(bean.getAllParams().toString());
					record.setStatus(ChinaPnrConstant.STATUS_RUNNING);
					int cnt = this.chinapnrService.updateChinapnrExclusiveLog(record);
					// 
					if (cnt > 0) {
						updateFlag = true;
					}
				}
			}

		} else {
			updateFlag = true;
		}
		// 其他程序正在处理中,或者返回值错误
		if (!updateFlag) {
			ModelAndView modelAndView = new ModelAndView(UserBindCardDefine.BINDCARD_ERROR_PATH);
			modelAndView.addObject("message", "用户已经绑卡");
			return modelAndView;
		}
		// 发送状态
		String status = ChinaPnrConstant.STATUS_VERTIFY_OK;
		// 成功
		if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
			try {
				Long chinaprnUsrcustid = Long.valueOf(bean.getUsrCustId());
				Integer userId = this.chinapnrService.selectUserIdByUsrcustid(chinaprnUsrcustid); // 用户ID

				// 插值用参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("userId", String.valueOf(userId));
				params.put("ip", CustomUtil.getIpAddr(request));

				// 执行绑卡后处理
				this.userBindCardService.updateAfterBindCard(bean, params);

			} catch (Exception e) {
				// 执行结果(失败)
				status = ChinaPnrConstant.STATUS_FAIL;
				LogUtil.errorLog(THIS_CLASS, UserBindCardDefine.RETURN_MAPPING, e);
			}
			// 执行结果(成功)
			status = ChinaPnrConstant.STATUS_SUCCESS;
			LogUtil.debugLog(THIS_CLASS, UserBindCardDefine.RETURN_MAPPING, "成功");
		} else {
			// 执行结果(失败)
			status = ChinaPnrConstant.STATUS_FAIL;
			LogUtil.debugLog(THIS_CLASS, UserBindCardDefine.RETURN_MAPPING, "失败");
		}

		// 更新状态记录
		if (updateFlag && Validator.isNotNull(uuid)) {
			this.chinapnrService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
		}

		ModelAndView modelAndView = new ModelAndView(UserBindCardDefine.BINDCARD_SUCCESS_PATH);
		LogUtil.endLog(THIS_CLASS, UserBindCardDefine.RETURN_MAPPING, "[绑卡完成后,回调结束]");

		return modelAndView;
	}

	/**
	 * 检查参数的正确性
	 *
	 * @param transAmt
	 * @param openBankId
	 * @param rechargeType
	 * @return
	 */
	private JSONObject checkParam(HttpServletRequest request) {

		String version = request.getParameter("version");
		// 网络状态
		String netStatus = request.getParameter("netStatus");
		// 平台
		String platform = request.getParameter("platform");
		// token
		String token = request.getParameter("token");
		// 唯一标识
		String sign = request.getParameter("sign");
		// 随机字符串
		String randomString = request.getParameter("randomString");
		// Order
		String order = request.getParameter("order");

		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(token) || Validator.isNull(sign) || Validator.isNull(randomString) || Validator.isNull(order)) {
			return jsonMessage("请求参数非法", "1");
		}

		// 取得加密用的Key
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			return jsonMessage("请求参数非法", "1");
		}
		Integer userId = SecretUtil.getUserId(sign);
		if (userId == null) {
			return jsonMessage("用户信息不存在", "1");
		}
		return null;
	}
}
