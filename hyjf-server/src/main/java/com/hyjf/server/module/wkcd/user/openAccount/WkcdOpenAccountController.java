package com.hyjf.server.module.wkcd.user.openAccount;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.cache.SerializeUtils;
import com.hyjf.common.chinapnr.MerPriv;
import com.hyjf.common.http.HttpRequest;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.DesECBUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.ServerApplication;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.server.BaseController;
import com.hyjf.server.module.wkcd.user.regist.WkcdRegistService;

@Controller
@RequestMapping(value = WkcdOpenAccountDefine.REQUEST_MAPPING)
public class WkcdOpenAccountController extends BaseController {
	@Autowired
	private WkcdOpenAccountService wkcdOpenAccountService;
	@Autowired
	private WkcdRegistService wkcdRegistService;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;
	
	/**
	 * 主机地址
	 */
	private static String HOST = PropUtils.getSystem("hyjf.server.host").trim();
	private static String HOST_HTTP = PropUtils.getSystem("http.hyjf.server.host").trim();
	
    /**
     * 开户
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value=WkcdOpenAccountDefine.OPEN_ACCOUNT_ACTION)
	public ModelAndView openAccountAction(HttpServletRequest request,HttpServletResponse response){
		LogUtil.startLog("WkcdOpenAccountController","registAction");
		ModelAndView modelAndView = new ModelAndView(WkcdOpenAccountDefine.JSP_CHINAPNR_SEND);
		//获取明文数据
		String requestObjectMingwen = request.getAttribute("requestObject").toString();
		if(StringUtils.isEmpty(requestObjectMingwen)){
			modelAndView = new ModelAndView(WkcdOpenAccountDefine.OPEN_ERROR_PATH);
			modelAndView.addObject("statusDesc", "接口调用失败,参数为空");
	        return modelAndView;
		} 
        Map<String, String> map = parseRequestJson(requestObjectMingwen);
        String userId= map.get("userId");
        String platform= map.get("platform");
        Object secretKey = request.getAttribute("secretKey");
        String appId = request.getParameter("appId");
        if (Validator.isNull(userId)|| Validator.isNull(platform)) {
        	modelAndView = new ModelAndView(WkcdOpenAccountDefine.OPEN_ERROR_PATH);
			modelAndView.addObject("statusDesc", "接口调用失败,参数不完整");
	        return modelAndView;
        }
        Users users= wkcdOpenAccountService.getUsers(Integer.valueOf(userId));
        if(users==null){
        	modelAndView = new ModelAndView(WkcdOpenAccountDefine.OPEN_ERROR_PATH);
			modelAndView.addObject("statusDesc", "接口调用失败,用户不存在【userId="+userId+"】");
	        return modelAndView;
        }
        // 取得用户在汇付天下的客户号
 		AccountChinapnr accountChinapnr = wkcdOpenAccountService.getAccountChinapnr(Integer.valueOf(userId));
 		if (accountChinapnr != null && accountChinapnr.getId() != null) {
 			System.out.println("initOpenAccount accountChinapnr----------------------:" + JSON.toJSONString(accountChinapnr));
 			LogUtil.endLog("WkcdOpenAccountController","registAction", "[用户已开户]");
 			// 回调url（h5错误页面）
 			modelAndView = new ModelAndView(WkcdOpenAccountDefine.OPEN_ERROR_PATH);
 			modelAndView.addObject("statusDesc", "用户已开户");
 			return modelAndView;
 		}

 		// 同步调用路径
 		String retUrl = HOST + WkcdOpenAccountDefine.REQUEST_MAPPING        // WkcdOpenAccountDefine.REQUEST_HOME
 				+ WkcdOpenAccountDefine.RETURL_SYN_MAPPING + "?sign=" + request.getParameter("uniqueSign")+"&appId="+appId+"&secretKey="+String.valueOf(secretKey);
 		// 异步调用路
		String bgRetUrl = HOST_HTTP +WkcdOpenAccountDefine.REQUEST_MAPPING
 				+ WkcdOpenAccountDefine.RETURN_ASY_MAPPING + "?sign=" + request.getParameter("uniqueSign")+"&appId="+appId+"&secretKey="+String.valueOf(secretKey);
 		
		//****  调用开户接口    ****
		ChinapnrBean bean = new ChinapnrBean();
		// 接口版本号
		bean.setVersion(ChinaPnrConstant.VERSION_10);
		// 消息类型(用户开户)
		bean.setCmdId(ChinaPnrConstant.CMDID_USER_REGISTER);
		// 页面同步返回 URL
		bean.setRetUrl(retUrl);
		// 页面异步返回URL(必须)
		bean.setBgRetUrl(bgRetUrl);
		// 用户手机
		bean.setUsrMp(users.getMobile());
		// 商户私有域，存放开户平台,用户userId
		MerPriv merPriv = new MerPriv();
		merPriv.setUserId(Integer.valueOf(userId));
		merPriv.setClient("5");//微可
		bean.setMerPrivPo(merPriv);
		// 页面类型
		bean.setPageType("1");//PageType 为空：即自适应风格页面		1：app 应用风格页面（无标题）	2：app 应用风格页面（有标题）
		// 拼装用户的汇付客户号
		String usrId = GetOrderIdUtils.getUsrId(Integer.valueOf(userId));
		bean.setUsrId(usrId);
		// 写log用参数
		// 操作者ID
		bean.setLogUserId(Integer.valueOf(userId));
		// 备注
		bean.setLogRemark("用户开户");
		// 账户开通平台 0pc 1微信 2安卓 3IOS 4其他  5微可
		bean.setLogClient("5");
		// IP地址
		bean.setLogIp(CustomUtil.getIpAddr(request));
		// 跳转到汇付天下画面
		try {
			modelAndView = ChinapnrUtil.callApi(bean);
			System.out.println("发送开户请求！");
			LogUtil.endLog("WkcdOpenAccountController","registAction");
		}catch(Exception e){
			LogUtil.errorLog("WkcdOpenAccountController","registAction", e);
			// 回调url（h5错误页面）
 			modelAndView = new ModelAndView(WkcdOpenAccountDefine.OPEN_ERROR_PATH);
 			modelAndView.addObject("statusDesc", "调用汇付接口失败");
		}
        LogUtil.endLog("WkcdOpenAccountController","registAction");
        return modelAndView;
	} 
	

	/**
	 * 用户开户同步处理
	 * 
	 * @param request
	 * @param bean
	 * @return
	 */
	@RequestMapping(WkcdOpenAccountDefine.RETURL_SYN_MAPPING)
	public ModelAndView openAccountSynReturn(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) {

		LogUtil.startLog(WkcdOpenAccountDefine.THIS_CLASS, WkcdOpenAccountDefine.RETURL_SYN_MAPPING, "[开户同步回调开始]");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(WkcdOpenAccountDefine.OPENACCOUNT_JSP);
        modelAndView.addObject("action", PropUtils.getSystem("wkcd.sync.callback"));
        
	    String webhost = PropUtils.getSystem("hyjf.server.host") + "/";
	    webhost = webhost.substring(0, webhost.length() - 1);// http://new.hyjf.com/hyjf-server/
		bean.convert();
		MerPriv merPriv = bean.getMerPrivPo();
		System.out.println("[开户同步回调开始]openAccountReturn bean:----------" + JSON.toJSONString(bean));
		System.out.println("requestUrl: " + request.getRequestURL().toString());
	    System.out.println("paramMap: " + request.getParameterMap());

		String platform = merPriv.getClient();

		String appId = request.getParameter("appId");
        String secretKey = request.getParameter("secretKey");
        byte[] applicationByte = RedisUtils.get(("Third-Party-Application:"+appId).getBytes());
        ServerApplication serverApplication = (ServerApplication)SerializeUtils.unserialize(applicationByte);
        //得到appKey
        String appKey = serverApplication.getAppkey();
        modelAndView.addObject("secretKey", DesECBUtil.encrypt(secretKey, appKey));
		// 校验userId
		int userId = merPriv.getUserId();
		System.out.println("platform: " + platform);
		String gesture = request.getParameter("gesture");
		String message = "开户失败";
		System.out.println("用户开户同步调用:openAccountReturn------------------------------" + userId);
		String resCode = bean.getRespCode();
		System.out.println("resCode:" + resCode);
		if ("508".equals(resCode)) {
			message = "用户已开户";
//			ModelAndView modelAndView = new ModelAndView(WkcdOpenAccountDefine.OPEN_ERROR_PATH);
			modelAndView.addObject("status","1");
			modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
			return modelAndView;
		}

		// 取得用户开户状态
		boolean updateFlag = false;
		Users user = this.wkcdOpenAccountService.getUsers(userId);
		if (user == null) {
			message = "用户未注册，请先注册！";
//			ModelAndView modelAndView = new ModelAndView(WkcdOpenAccountDefine.OPEN_ERROR_PATH);
			modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
			modelAndView.addObject("status","1");
			return modelAndView;
		} else {
			int openAccountFlag = user.getOpenAccount();
			if (openAccountFlag == 0) {
				updateFlag = true;
			}
		}
//		UserEvalationResult ueResult = financialAdvisorService.selectUserEvalationResultByUserId(userId);
		// 用户未开户
		if (updateFlag) {
			// 发送状态
			String status = ChinaPnrConstant.STATUS_VERTIFY_OK;
			// 成功或审核中
			if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))
					|| ChinaPnrConstant.RESPCODE_CHECK.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
				try {
					// 插值用参数
					bean.setLogUserId(userId);
					bean.setLogIp(CustomUtil.getIpAddr(request));
					bean.setLogClient(platform);
					// 开户后保存相应的数据以及日志
					boolean flag = false;
					flag = this.wkcdOpenAccountService.userOpenAccount(bean);
					if (!flag) {
						Users user2 = wkcdOpenAccountService.getUsers(userId);
						if (user2.getOpenAccount().intValue()==1) {
							// 执行结果(成功)
							status = ChinaPnrConstant.STATUS_SUCCESS;
							LogUtil.debugLog(WkcdOpenAccountDefine.THIS_CLASS, WkcdOpenAccountDefine.RETURL_SYN_MAPPING, "成功");
						}else {
							// 执行结果(失败)
							status = ChinaPnrConstant.STATUS_FAIL;
							LogUtil.endLog(WkcdOpenAccountDefine.THIS_CLASS, WkcdOpenAccountDefine.RETURL_SYN_MAPPING,
									"[开户完成后,更新用户信息失败]");
						}
					} else {
						// 执行结果(成功)
						status = ChinaPnrConstant.STATUS_SUCCESS;
						LogUtil.debugLog(WkcdOpenAccountDefine.THIS_CLASS, WkcdOpenAccountDefine.RETURL_SYN_MAPPING, "成功");
						String mobile =bean.getUsrMp();
						JSONObject params= this.wkcdOpenAccountService.selectUserByMobile(userId,mobile);
						if(params!=null){
							Map<String, String> replaceMap = new HashMap<String, String>();
							replaceMap.put("val_name_new", params.getString("newUserName"));
							replaceMap.put("val_name_old", params.getString("oldUserName"));
							replaceMap.put("val_mobile", mobile);
							String[] email = { "gaohonggang@hyjf.com","liudandan@hyjf.com"  };
							MailMessage messageMail = new MailMessage(null, replaceMap, "用户开户",null,null, email,CustomConstants.EMAILPARAM_TPL_REPEATMOBILE, MessageDefine.MAILSENDFORMAILINGADDRESS);
				            mailMessageProcesser.gather(messageMail);
						}
						//发放代金券开始ZXS
						//是否可以发放注册开户68代金券校验
//						boolean isSend=wkcdOpenAccountService.checkIfSendCoupon(user);
//						if (isSend) {
//							CouponParamBean couponParamBean=new CouponParamBean();
//							couponParamBean.setSendFlg(4);
//							couponParamBean.setUserId(userId+"");
//							CouponUtils.sendUserCoupon(couponParamBean);
//						}
						//发放代金券结束
					}
				} catch (Exception e) {
					// 执行结果(失败)
					status = ChinaPnrConstant.STATUS_FAIL;
					LogUtil.errorLog(WkcdOpenAccountDefine.THIS_CLASS, WkcdOpenAccountDefine.RETURL_SYN_MAPPING, e);
				}
			} else {
				// 执行结果(失败)
				status = ChinaPnrConstant.STATUS_FAIL;
				LogUtil.debugLog(WkcdOpenAccountDefine.THIS_CLASS, WkcdOpenAccountDefine.RETURL_SYN_MAPPING, "失败");
			}

			if (ChinaPnrConstant.STATUS_SUCCESS.equals(status)) {
				message = "开户成功";
//				ModelAndView modelAndView = new ModelAndView(WkcdOpenAccountDefine.OPEN_SUCCESS_PATH);
				LogUtil.endLog(WkcdOpenAccountDefine.THIS_CLASS, WkcdOpenAccountDefine.RETURL_SYN_MAPPING, "[交易完成后,回调结束]");
				modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
				modelAndView.addObject("status","0");
				if(StringUtils.isNotBlank(gesture)){
					modelAndView.addObject("gesture", gesture);
				}
				JSONObject ret = new JSONObject();
				ret.put("userId", userId);
				ret.put("UsrCustId", bean.getUsrCustId());
				ret.put("IdType", bean.getIdType());
				ret.put("IdNo", bean.getIdNo());
				ret.put("UsrName", bean.getUsrName());
				ret.put("sn", "OpenAccount");
				modelAndView.addObject("responseObject", DesECBUtil.encrypt(ret.toString(), secretKey));
				
				// add by zhangjp 优惠券相关  start
//				String resultActivity = couponCheckUtil.checkActivityIfAvailable(CustomConstants.ACTIVITY_ID);
//				if(StringUtils.isEmpty(resultActivity) && ueResult == null){
//					modelAndView.addObject("activityFlg", true);
//					modelAndView.addObject("activityMsg", "限时活动：参与风险测评调查，即可获得1张加息券");
//				}
				// add by zhangjp 优惠券相关  end
				
//				String financialUrl = webhost+FinancialAdvisorDefine.REQUEST_MAPPING+FinancialAdvisorDefine.INIT_ACTION+"?sign="+request.getParameter("sign")+"&platform="+platform;
//				modelAndView.addObject("financialUrl", financialUrl);
//				LogUtil.infoLog(this.getClass().getName(), "openAccountSynReturn", "测评地址financialUrl：" + financialUrl);
				return modelAndView;
			} else {
				message = "开户失败";
//				ModelAndView modelAndView = new ModelAndView(WkcdOpenAccountDefine.OPEN_ERROR_PATH);
				modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
				modelAndView.addObject("status","1");
				return modelAndView;
			}

		} else {
			message = "用户已开户!";
//			ModelAndView modelAndView = new ModelAndView(WkcdOpenAccountDefine.OPEN_SUCCESS_PATH);
			modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
			// add by zhangjp 优惠券相关  start
//			String resultActivity = couponCheckUtil.checkActivityIfAvailable(CustomConstants.ACTIVITY_ID);
//			if(StringUtils.isEmpty(resultActivity) && ueResult == null){
//				modelAndView.addObject("activityFlg", true);
//				modelAndView.addObject("activityMsg", "限时活动：参与风险测评调查，即可获得1张加息券");
//			}
//			String financialUrl = webhost+FinancialAdvisorDefine.REQUEST_MAPPING+FinancialAdvisorDefine.INIT_ACTION+"?sign="+request.getParameter("sign")+"&platform="+platform;
//			modelAndView.addObject("financialUrl", financialUrl);
			// add by zhangjp 优惠券相关  end
			if(StringUtils.isNotBlank(gesture)){
				modelAndView.addObject("gesture", gesture);
			}
			return modelAndView;
		}

	}

	/**
	 * 用户开户异步处理
	 * 
	 * @param request
	 * @param bean
	 * @return
	 */
	@RequestMapping(WkcdOpenAccountDefine.RETURN_ASY_MAPPING)
	public ModelAndView openAccountAsyReturn(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) throws Exception {
		LogUtil.startLog(WkcdOpenAccountDefine.THIS_CLASS, WkcdOpenAccountDefine.RETURN_ASY_MAPPING, "[开户异步回调开始]");
		ModelAndView modelAndView = new ModelAndView();
		Map<String, Object> params = new HashMap<String, Object>();
        String webhost = PropUtils.getSystem("hyjf.server.host")  + "/";
        webhost = webhost.substring(0, webhost.length() - 1);// http://new.hyjf.com/hyjf-app/
		bean.convert();
		MerPriv merPriv = bean.getMerPrivPo();
		System.out.println("[开户异步回调开始]openAccountReturn bean:----------" + JSON.toJSONString(bean));
		String platform = merPriv.getClient();
		
		String appId = request.getParameter("appId");
        String secretKey = request.getParameter("secretKey");
        byte[] applicationByte = RedisUtils.get(("Third-Party-Application:"+appId).getBytes());
        ServerApplication serverApplication = (ServerApplication)SerializeUtils.unserialize(applicationByte);
        //得到appKey
        String appKey = serverApplication.getAppkey();
        modelAndView.addObject("secretKey", DesECBUtil.encrypt(secretKey, appKey));
		// 校验userId
		int userId = merPriv.getUserId();
		String gesture = request.getParameter("gesture");
		String message = "开户失败";
		System.out.println("用户开户同步调用:openAccountReturn------------------------------" + userId);
		String resCode = bean.getRespCode();
		System.out.println("resCode:" + resCode);
		if ("508".equals(resCode)) {
			message = "用户已开户";
//			ModelAndView modelAndView = new ModelAndView(WkcdOpenAccountDefine.OPEN_ERROR_PATH);
			modelAndView.addObject(CustomConstants.APP_STATUS, 1);
			modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
			return modelAndView;
		}
		// 查询用户开户状态
		boolean updateFlag = false;
		Users user = this.wkcdOpenAccountService.getUsers(userId);
		if (user == null) {
			message = "用户未注册，请先注册！";
//			ModelAndView modelAndView = new ModelAndView(WkcdOpenAccountDefine.OPEN_ERROR_PATH);
			modelAndView.addObject(CustomConstants.APP_STATUS, 1);
			modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
			return modelAndView;
		} else {
			int openAccountFlag = user.getOpenAccount();
			if (openAccountFlag == 0) {
				updateFlag = true;
			}
		}
//		UserEvalationResult ueResult = financialAdvisorService.selectUserEvalationResultByUserId(userId);
		// 用户未开户
		if (updateFlag) {
			// 发送状态
			String status = ChinaPnrConstant.STATUS_VERTIFY_OK;
			// 成功或审核中
			if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))
					|| ChinaPnrConstant.RESPCODE_CHECK.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
				try {
					// 插值用参数
					bean.setLogUserId(userId);
					bean.setLogIp(CustomUtil.getIpAddr(request));
					bean.setLogClient(platform);
					// 开户后保存相应的数据以及日志
					boolean flag = this.wkcdOpenAccountService.userOpenAccount(bean);
					if (!flag) {
						Users userNew = this.wkcdOpenAccountService.getUsers(userId);
						if(userNew.getOpenAccount() ==1){
							// 执行结果(失败)
							status = ChinaPnrConstant.STATUS_SUCCESS;
							LogUtil.debugLog(WkcdOpenAccountDefine.THIS_CLASS, WkcdOpenAccountDefine.RETURN_ASY_MAPPING, "成功");
						}else{
							// 执行结果(失败)
							status = ChinaPnrConstant.STATUS_FAIL;
							LogUtil.endLog(WkcdOpenAccountDefine.THIS_CLASS, WkcdOpenAccountDefine.RETURN_ASY_MAPPING,
									"[开户完成后,更新用户信息失败]");
						}
					} else {
						// 执行结果(成功)
						status = ChinaPnrConstant.STATUS_SUCCESS;
						LogUtil.debugLog(WkcdOpenAccountDefine.THIS_CLASS, WkcdOpenAccountDefine.RETURN_ASY_MAPPING, "成功");
						String mobile =bean.getUsrMp();
						JSONObject params22= this.wkcdOpenAccountService.selectUserByMobile(userId,mobile);
						if(params22!=null){
							Map<String, String> replaceMap = new HashMap<String, String>();
							replaceMap.put("val_name_new", params22.getString("newUserName"));
							replaceMap.put("val_name_old", params22.getString("oldUserName"));
							replaceMap.put("val_mobile", mobile);
							String[] email = { "gaohonggang@hyjf.com","liudandan@hyjf.com"  };
							MailMessage messageMail = new MailMessage(null, replaceMap, "用户开户",null,null, email,CustomConstants.EMAILPARAM_TPL_REPEATMOBILE, MessageDefine.MAILSENDFORMAILINGADDRESS);
				            mailMessageProcesser.gather(messageMail);
						}
						//发放代金券开始ZXS
						//是否可以发放注册开户68代金券校验
//						boolean isSend=wkcdOpenAccountService.checkIfSendCoupon(user);
//						if (isSend) {
//							CouponParamBean couponParamBean=new CouponParamBean();
//							couponParamBean.setSendFlg(4);
//							couponParamBean.setUserId(userId+"");
//							CouponUtils.sendUserCoupon(couponParamBean);
//						}
						//发放代金券结束
					}
				} catch (Exception e) {
					// 执行结果(失败)
					status = ChinaPnrConstant.STATUS_FAIL;
					LogUtil.errorLog(WkcdOpenAccountDefine.THIS_CLASS, WkcdOpenAccountDefine.RETURN_ASY_MAPPING, e);
				}

			} else {
				// 执行结果(失败)
				status = ChinaPnrConstant.STATUS_FAIL;
				LogUtil.debugLog(WkcdOpenAccountDefine.THIS_CLASS, WkcdOpenAccountDefine.RETURN_ASY_MAPPING, "失败");
			}

			if (ChinaPnrConstant.STATUS_SUCCESS.equals(status)) {
				message = "开户成功";
//				ModelAndView modelAndView = new ModelAndView(WkcdOpenAccountDefine.OPEN_SUCCESS_PATH);
				LogUtil.endLog(WkcdOpenAccountDefine.THIS_CLASS, WkcdOpenAccountDefine.RETURL_SYN_MAPPING, "[交易完成后,回调结束]");
				modelAndView.addObject(CustomConstants.APP_STATUS, 0);
				modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
				if(StringUtils.isNotBlank(gesture)){
					modelAndView.addObject("gesture", gesture);
				}
				JSONObject ret = new JSONObject();
				ret.put("userId", userId);
				ret.put("UsrCustId", bean.getUsrCustId());
				ret.put("IdType", bean.getIdType());
				ret.put("IdNo", bean.getIdNo());
				ret.put("UsrName", bean.getUsrName());
				ret.put("sn", "OpenAccount");
				modelAndView.addObject("responseObject", DesECBUtil.encrypt(ret.toString(), secretKey));
				
				params.put("status", modelAndView.getModel().get("status")!=null?URLEncoder.encode(String.valueOf(modelAndView.getModel().get("status")), "UTF-8"):"");
		        params.put("secretKey", modelAndView.getModel().get("secretKey")!=null?URLEncoder.encode(String.valueOf(modelAndView.getModel().get("secretKey")), "UTF-8"):"");
		        params.put("statusDesc", modelAndView.getModel().get("statusDesc")!=null?URLEncoder.encode(String.valueOf(modelAndView.getModel().get("statusDesc")), "UTF-8"):"");
		        params.put("responseObject", modelAndView.getModel().get("responseObject")!=null?URLEncoder.encode(String.valueOf(modelAndView.getModel().get("responseObject")), "UTF-8"):"");
		        String result = HttpRequest.sendPost(PropUtils.getSystem("wkcd.async.callback"), params);
			        
				// add by zhangjp 优惠券相关  start
//				String resultActivity = couponCheckUtil.checkActivityIfAvailable(CustomConstants.ACTIVITY_ID);
//				if(StringUtils.isEmpty(resultActivity) && ueResult == null){
//					modelAndView.addObject("activityFlg", true);
//					modelAndView.addObject("activityMsg", "限时活动：参与出借调查，即可获得1张加息券");
//				}
				// add by zhangjp 优惠券相关  end
//                String financialUrl = webhost+FinancialAdvisorDefine.REQUEST_MAPPING+FinancialAdvisorDefine.INIT_ACTION+"?sign="+request.getParameter("sign")+"&platform="+platform;
//                modelAndView.addObject("financialUrl", financialUrl);
//                LogUtil.infoLog(this.getClass().getName(), "openAccountSynReturn", "测评地址financialUrl：" + financialUrl);
                return modelAndView;
			} else {
				message = "开户失败";
//				ModelAndView modelAndView = new ModelAndView(WkcdOpenAccountDefine.OPEN_ERROR_PATH);
				modelAndView.addObject(CustomConstants.APP_STATUS, 1);
				modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
				return modelAndView;
			}
		} else {
			message = "用户已开户";
//			ModelAndView modelAndView = new ModelAndView(WkcdOpenAccountDefine.OPEN_SUCCESS_PATH);
			modelAndView.addObject(CustomConstants.APP_STATUS, 1);
			modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
			// add by zhangjp 优惠券相关  start
//			String resultActivity = couponCheckUtil.checkActivityIfAvailable(CustomConstants.ACTIVITY_ID);
//			if(StringUtils.isEmpty(resultActivity) && ueResult == null){
//				modelAndView.addObject("activityFlg", true);
//				modelAndView.addObject("activityMsg", "限时活动：参与风险测评调查，即可获得1张加息券");
//			}
//			String financialUrl = webhost+FinancialAdvisorDefine.REQUEST_MAPPING+FinancialAdvisorDefine.INIT_ACTION+"?sign="+request.getParameter("sign")+"&platform="+platform;
//			modelAndView.addObject("financialUrl", financialUrl);
			// add by zhangjp 优惠券相关  end
			if(StringUtils.isNotBlank(gesture)){
				modelAndView.addObject("gesture", gesture);
			}
			return modelAndView;

		}

	}

	/**
	 * 判断是否已开户
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value=WkcdOpenAccountDefine.IF_OPENED_ACCOUNT_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public JSONObject ifOpenedAction(HttpServletRequest request,HttpServletResponse response){
		JSONObject ret = new JSONObject();
		//获取明文数据
		String requestObjectMingwen = request.getAttribute("requestObject").toString();
		if(StringUtils.isEmpty(requestObjectMingwen)){
			ret.put("status", "1");
	        ret.put("statusDesc", "接口调用失败,参数为空");
	        return ret;
		}
        Map<String, String> map = parseRequestJson(requestObjectMingwen);
        String mobile= map.get("mobile");
        if (Validator.isNull(mobile)) {
        	ret.put("status", "1");
	        ret.put("statusDesc", "手机号码为空");
	        return ret;
        }
        Users u= wkcdRegistService.getUserByMobile(mobile);
        if(u!=null){
	        Integer userid= u.getUserId();
	        // 取得用户在汇付天下的客户号
	 		AccountChinapnr accountChinapnr = wkcdOpenAccountService.getAccountChinapnr(userid);
	 		if (accountChinapnr != null && accountChinapnr.getId() != null) {
	 			System.out.println("initOpenAccount accountChinapnr----------------------:" + JSON.toJSONString(accountChinapnr));
	 			LogUtil.endLog("WkcdOpenAccountController","registAction", "[用户已开户]");

	        	ret.put("status", "2");
		        ret.put("statusDesc", "用户已开户");
		        return ret;
	 		}else{
	        	ret.put("status", "3");
		        ret.put("statusDesc", "用户尚未开户。");
		        return ret;
	 		}
        }else{
        	ret.put("status", "1");
	        ret.put("statusDesc", "手机号"+mobile+"未注册。");
	        return ret;
        }
        
		
		
	}

}
