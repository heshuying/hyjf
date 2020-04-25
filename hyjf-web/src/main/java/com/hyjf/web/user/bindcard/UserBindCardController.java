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
package com.hyjf.web.user.bindcard;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
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
import com.hyjf.web.BaseController;
import com.hyjf.web.chinapnr.ChinapnrService;
import com.hyjf.web.user.withdraw.UserWithdrawDefine;
import com.hyjf.web.util.WebUtils;

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
	 * 检查参数
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(UserBindCardDefine.CHECK_MAPPING)
	public String check(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(UserBindCardController.class.toString(), UserBindCardDefine.CHECK_MAPPING);
		Integer userId = WebUtils.getUserId(request);
		// 检查参数
		JSONObject checkResult = checkParam(request, userId);
		if (checkResult != null) {
			return checkResult.toString();
		}
		checkResult = new JSONObject();
		checkResult.put("url", UserBindCardDefine.CALLURL);
		LogUtil.endLog(UserBindCardController.class.toString(), UserBindCardDefine.CHECK_MAPPING);
		return checkResult.toString();
	}

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
		Integer userId = WebUtils.getUserId(request);
		String userName = WebUtils.getUser(request).getUsername(); // 用户名
		// 检查参数
		if (checkParam(request, userId) != null) {
			modelAndView = new ModelAndView(UserBindCardDefine.JSP_BINDCARD_FALSE);
			return modelAndView;
		}

		// 取得用户在汇付天下的客户号
		AccountChinapnr accountChinapnrTender = userBindCardService.getAccountChinapnr(userId);
		if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getChinapnrUsrcustid())) {
			modelAndView = new ModelAndView(UserBindCardDefine.JSP_BINDCARD_FALSE);
			LogUtil.endLog(THIS_CLASS, UserBindCardDefine.REQUEST_MAPPING, "[用户未开户]");
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
		bean.setBgRetUrl(PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) + request.getContextPath() + UserBindCardDefine.REQUEST_MAPPING + UserBindCardDefine.RETURN_MAPPING + ".do");// 商户后台应答地址(必须)
		System.out.println("绑卡回调函数：\n" + bean.getBgRetUrl());
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
		System.out.println("UserBindCard返回值处理：" + "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
		// 返回Code
		String respCode = null;
		boolean updateFlag = false;
		// 取得更新用UUID
		String uuid = request.getParameter("uuid");
		System.out.println("---------uuid:" + uuid);
		if (Validator.isNotNull(uuid)) {// 如果uuid不为空
			// 取得检证数据
			ChinapnrExclusiveLogWithBLOBs record = chinapnrService.selectChinapnrExclusiveLog(Long.parseLong(uuid));
			// 如果检证数据状态为未发送
			if (record != null) {
				respCode = record.getRespcode();
				System.out.println("---ChinapnrExclusiveLog表获取：respCode:" + respCode + ";status:" + record.getStatus());
				// 检证通过时
				if (ChinaPnrConstant.STATUS_VERTIFY_OK.equals(record.getStatus())) {
					// 将状态更新成[2:处理中]
					record.setId(Long.parseLong(uuid));
					record.setResult(bean.getAllParams().toString());
					record.setStatus(ChinaPnrConstant.STATUS_RUNNING);
					int cnt = this.chinapnrService.updateChinapnrExclusiveLog(record);
					System.out.println("修改ChinapnrExclusiveLog表结果：" + cnt);
					// 防止重复提交
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
			ModelAndView modelAndView = new ModelAndView(UserBindCardDefine.JSP_BINDCARD_FALSE);
			LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易正在处理中或者已经完成,本次回调结束]");
			return modelAndView;
		}

		// 发送状态
		String status = ChinaPnrConstant.STATUS_VERTIFY_OK;
		// 成功
		if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
			try {
				Long chinaprnUsrcustid = Long.valueOf(bean.getUsrCustId());
				Integer userId = this.chinapnrService.selectUserIdByUsrcustid(chinaprnUsrcustid); // 用户ID
				System.out.println("****绑卡：客户号：" + chinaprnUsrcustid + "; 用户ID：" + userId);

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
			LogUtil.debugLog(THIS_CLASS, UserBindCardDefine.RETURN_MAPPING, "绑卡失败：" + bean.get(ChinaPnrConstant.PARAM_RESPDESC));
		}

		// 更新状态记录
		if (updateFlag && Validator.isNotNull(uuid)) {
			this.chinapnrService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), status);
		}
		ModelAndView modelAndView = new ModelAndView(UserBindCardDefine.JSP_BINDCARD_FALSE);
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
	private JSONObject checkParam(HttpServletRequest request, Integer userId) {
		// 检查用户是否登录
		if (Validator.isNull(WebUtils.getUserId(request))) {
			return jsonMessage("您没有登录，请登录后再进行出借。", "1");
		}
		// 取得用户在汇付天下的客户号
		AccountChinapnr accountChinapnrTender = userBindCardService.getAccountChinapnr(userId);
		if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getChinapnrUsrcustid())) {
			return jsonMessage("用户未开户，请开户后再充值。", "1");
		}
		return null;
	}

}
