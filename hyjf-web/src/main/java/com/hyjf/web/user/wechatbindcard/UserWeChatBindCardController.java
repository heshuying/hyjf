package com.hyjf.web.user.wechatbindcard;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogWithBLOBs;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.web.BaseController;
import com.hyjf.web.chinapnr.ChinapnrService;
import com.hyjf.web.user.bindcard.UserBindCardController;
import com.hyjf.web.user.bindcard.UserBindCardDefine;
import com.hyjf.web.user.withdraw.UserWithdrawDefine;

@Controller
@RequestMapping(value = UserWeChatBindCardDefine.REQUEST_MAPPING)
public class UserWeChatBindCardController extends BaseController{

	@Autowired
	private UserWeChatBindCardService weChatBindCardService;
	
	@Autowired
	private ChinapnrService chinapnrService;
	
	/** THIS_CLASS */
	private static final String THIS_CLASS = UserWeChatBindCardController.class.getName();
	/**
	 * 用户绑卡
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(UserWeChatBindCardDefine.REQUEST_BINDCARD_MAPPING)
	public ModelAndView bindCard(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(UserBindCardController.class.toString(), UserWeChatBindCardDefine.REQUEST_MAPPING);
		ModelAndView modelAndView = new ModelAndView(UserWeChatBindCardDefine.JSP_CHINAPNR_SEND);
		
		if (StringUtils.isBlank(request.getParameter("callback")) ) {
			modelAndView = new ModelAndView(UserWeChatBindCardDefine.JSP_BINDCARD_FALSE);
			modelAndView.addObject("error", "绑卡异常!");
			return modelAndView;
		}
		String callbackUrl = request.getParameter("callback");
		
		if (StringUtils.isBlank(request.getParameter("userId")) ) {
			return returnModelView("1", "userId为空!", callbackUrl, null);
		}
		
		Integer userId = Integer.parseInt(request.getParameter("userId"));
		
		try {
			// 取得用户在汇付天下的客户号
			AccountChinapnr accountChinapnrTender = weChatBindCardService.getAccountChinapnr(userId);
			if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getChinapnrUsrcustid())) {
				LogUtil.endLog(THIS_CLASS, UserWeChatBindCardDefine.REQUEST_MAPPING, "[用户未开户]");
				return returnModelView("1", "用户未开户，请开户后再进行绑卡。", callbackUrl, null);
			}

			Long chinapnrUsrcustidTender = accountChinapnrTender.getChinapnrUsrcustid();
			
			// 调用汇付接口(4.2.2 用户绑卡接口)
			ChinapnrBean bean = new ChinapnrBean();
			bean.setVersion(ChinaPnrConstant.VERSION_10);// 接口版本号
			bean.setCmdId(ChinaPnrConstant.CMDID_USER_BIND_CARD); // 每一种消息类型代表一种交易，绑卡为UserBindCard
			bean.setUsrCustId(String.valueOf(chinapnrUsrcustidTender));// 用户客户号
			bean.setBgRetUrl(PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) + UserWeChatBindCardDefine.REQUEST_MAPPING + UserWeChatBindCardDefine.RETURN_MAPPING + ".do?returnUrl="+callbackUrl);// 商户后台应答地址(必须)
			System.out.println("绑卡回调函数：\n" + bean.getBgRetUrl());
			
			// 跳转到汇付天下画面
			try {
				modelAndView = ChinapnrUtil.callApi(bean);
			} catch (Exception e) {
				e.printStackTrace();
				return returnModelView("1", "挑转汇付天下画面失败!", callbackUrl, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return returnModelView("1", e.getMessage(), callbackUrl, null);
		}
		LogUtil.endLog(UserBindCardController.class.toString(), UserWeChatBindCardDefine.REQUEST_MAPPING);
		return modelAndView;
	}

	/**
	 * 用户绑卡后处理
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(UserWeChatBindCardDefine.RETURN_MAPPING)
	public ModelAndView bindCardReturn(HttpServletRequest request,HttpServletResponse response, @ModelAttribute ChinapnrBean bean) {
		LogUtil.startLog(THIS_CLASS, UserWeChatBindCardDefine.RETURN_MAPPING, "[绑卡完成后,回调开始]");
		bean.convert();
		LogUtil.debugLog(THIS_CLASS, UserBindCardDefine.RETURN_MAPPING, "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
		System.out.println("WeChatBindCard返回值处理：" + "参数: " + bean == null ? "无" : bean.getAllParams() + "]");
		// 返回Code
		String respCode = null;
		boolean updateFlag = false;
		String requestUrl  =request.getParameter("returnUrl");
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
			LogUtil.endLog(THIS_CLASS, UserWithdrawDefine.RETURN_MAPPING, "[交易正在处理中或者已经完成,本次回调结束]");
			return returnModelView("1", "绑卡失败!", requestUrl, null);
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
				this.weChatBindCardService.updateAfterBindCard(bean, params);

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
		
		if (ChinaPnrConstant.STATUS_SUCCESS.equals(status)) {//成功
			return returnModelView("0", bean.get(ChinaPnrConstant.PARAM_RESPDESC), requestUrl, null);
		}else{
			return returnModelView("1", bean.get(ChinaPnrConstant.PARAM_RESPDESC), requestUrl, null);
		}
	}


	
	/**
	 * 拼接返回结果
	 * 
	 * @param error
	 * @param data
	 * @param callback
	 * @param pageFrom
	 * @return
	 */
	public ModelAndView returnModelView(String error, String data, String callback, String pageFrom) {
		// RespCode:000 成功；否则，失败
		Map<String, String> map = new HashMap<String, String>();
		map.put("error", error);
		map.put("message", data);
		data = JSON.toJSONString(map);
		try {
			data = URLEncoder.encode(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LogUtil.errorLog(UserWeChatBindCardController.class.getName(), "returnModelView", e);
			e.printStackTrace();
		}
		StringBuffer url = new StringBuffer();
		url.append(callback).append("backinfo/").append(data);
		if (StringUtils.isNotEmpty(pageFrom)) {
			url.append("&pageFrom=" + pageFrom);
		}
		System.out.println("==================cwyang url: " + url.toString());
		return new ModelAndView("redirect:" + url.toString());
	}
}
