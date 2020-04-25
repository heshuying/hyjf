/**
 * Description:用户开户
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年12月4日 下午2:32:36
 * Modification History:
 * Modified by :
 */
package com.hyjf.api.surong.user.open;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.client.autoinvestsys.InvestSysUtils;
import com.hyjf.common.chinapnr.MerPriv;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.http.HttpClientUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.coupon.util.CouponCheckUtil;
import com.hyjf.financialadvisor.FinancialAdvisorService;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

@Controller
@RequestMapping(value = OpenAccountDefine.REQUEST_MAPPING)
public class OpenAccountServer {

	@Autowired
	private SuRongOpenAccountService suRongOpenAccountService;
	@Autowired
    private CouponCheckUtil couponCheckUtil;
	@Autowired
	private FinancialAdvisorService financialAdvisorService;
	
	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;
	/**
	 * 主机地址
	 */
	private static String HOST = PropUtils.getSystem("hyjf.web.host").trim();

	/**
	 * 用户开户
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(OpenAccountDefine.OPENACCOUNT_MAPPING)
	public ModelAndView initOpenAccount(HttpServletRequest request, HttpServletResponse response) {
	    ModelAndView modelAndView = new ModelAndView();
	    String message = "开户失败，请更新新版本APP";
        modelAndView = new ModelAndView(OpenAccountDefine.OPEN_ERROR_PATH);
        modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
        return modelAndView;
        //2017.9.18针对老版本融东风app开户操作调用汇付天下接口     注释start,方法开始部分为新增代码
		/*LogUtil.startLog(OpenAccountServer.class.toString(), KclOpenAccountDefine.OPENACCOUNT_MAPPING);
		ModelAndView modelAndView = new ModelAndView(KclOpenAccountDefine.JSP_CHINAPNR_SEND);
		String message = "开户失败";
		// 用户id

		Integer userId = null;
		if(!Validator.isNull(request.getParameter("userId"))){
			userId = Integer.valueOf(request.getParameter("userId"));
		}
		if (userId == null) {
			modelAndView = new ModelAndView(KclOpenAccountDefine.OPEN_ERROR_PATH);
			modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
			return modelAndView;
		}
		String mobile = request.getParameter("mobile");// 用户注册手机号
		String client = request.getParameter("platform");// 客户端
		String pageType = "1"; // 页面类型
		String version = request.getParameter("version");//版本
		System.out.println("appOpenAccount UserId:" + userId );
		// 取得用户在汇付天下的客户号
		AccountChinapnr accountChinapnr = suRongOpenAccountService.getAccountChinapnr(userId);
		if (accountChinapnr != null && accountChinapnr.getId() != null) {
			System.out.println("initOpenAccount accountChinapnr----------------------:" + JSON.toJSONString(accountChinapnr));
			message = "用户已开户";
			LogUtil.endLog(OpenAccountServer.class.getName(), KclOpenAccountDefine.OPENACCOUNT_MAPPING, "[用户已开户]");
			// 回调url（h5错误页面）
			modelAndView = new ModelAndView(KclOpenAccountDefine.OPEN_ERROR_PATH);
			modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
			return modelAndView;
		}
		// 同步调用路径
		String retUrl = HOST + KclOpenAccountDefine.SURONG_REQUEST_HOME + KclOpenAccountDefine.REQUEST_MAPPING
				+ KclOpenAccountDefine.RETURL_SYN_MAPPING;
		// 异步调用路
		String bgRetUrl = HOST + KclOpenAccountDefine.SURONG_REQUEST_HOME + KclOpenAccountDefine.REQUEST_MAPPING
				+ KclOpenAccountDefine.RETURN_ASY_MAPPING;
		// 调用开户接口
		if (StringUtils.contains(version, CustomConstants.APP_VERSION_NUM)) {

		} else {
			String gesture = request.getParameter("gesture");
			if(StringUtils.isNotBlank(gesture)){
				// 同步调用路径
				retUrl = retUrl + "&gesture=" + gesture;
				// 异步调用路
				bgRetUrl = bgRetUrl + "&gesture=" + gesture;
			}
		}
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
		if(StringUtils.isNotBlank(mobile)){
			bean.setUsrMp(mobile);
		}
		// 商户私有域，存放开户平台,用户userId
		MerPriv merPriv = new MerPriv();
		merPriv.setUserId(userId);
		merPriv.setClient(client);
		bean.setMerPrivPo(merPriv);
		// 页面类型
		bean.setPageType(pageType);
		// 拼装用户的汇付客户号
		String usrId = GetOrderIdUtils.getUsrId(userId);
		bean.setUsrId(usrId);
		// 写log用参数
		// 操作者ID
		bean.setLogUserId(userId);
		// 备注
		bean.setLogRemark("用户开户");
		// 账户开通平台 0pc 1微信 2安卓 3IOS 4其他
		bean.setLogClient(client);
		// IP地址
		bean.setLogIp(CustomUtil.getIpAddr(request));
		// 跳转到汇付天下画面
		try {
			//modelAndView = ChinapnrUtil.callApi(bean);
			System.out.println("发送开户请求！");
			LogUtil.endLog(OpenAccountServer.class.toString(), KclOpenAccountDefine.OPENACCOUNT_MAPPING);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.errorLog(OpenAccountServer.class.toString(), KclOpenAccountDefine.OPENACCOUNT_MAPPING, e);
			message = "调用汇付接口失败";
			modelAndView = new ModelAndView(KclOpenAccountDefine.OPEN_ERROR_PATH);
			modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
		}
		return modelAndView;
		*/
	}

	/**
	 * 用户开户同步处理
	 * 
	 * @param request
	 * @param bean
	 * @return
	 */
	@SuppressWarnings("static-access")
    @RequestMapping(OpenAccountDefine.RETURL_SYN_MAPPING)
	public ModelAndView openAccountSynReturn(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) {

		LogUtil.startLog(OpenAccountServer.class.getName(), OpenAccountDefine.RETURL_SYN_MAPPING, "[开户同步回调开始]");
	    String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host")) + BaseDefine.SURONG_REQUEST_HOME + "/";
	    webhost = webhost.substring(0, webhost.length() - 1);// http://new.hyjf.com/hyjf-app/
		bean.convert();
		MerPriv merPriv = bean.getMerPrivPo();
		System.out.println("[开户同步回调开始]openAccountReturn bean:----------" + JSON.toJSONString(bean));
		System.out.println("requestUrl: " + request.getRequestURL().toString());
	    System.out.println("paramMap: " + request.getParameterMap());

		String platform = merPriv.getClient();
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
			ModelAndView modelAndView = new ModelAndView(OpenAccountDefine.OPEN_ERROR_PATH);
			modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
			return modelAndView;
		}

		// 取得用户开户状态
		boolean updateFlag = false;
		Users user = this.suRongOpenAccountService.selectUserById(userId);
		if (user == null) {
			message = "用户未注册，请先注册！";
			ModelAndView modelAndView = new ModelAndView(OpenAccountDefine.OPEN_ERROR_PATH);
			modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
			return modelAndView;
		} else {
			int openAccountFlag = user.getOpenAccount();
			if (openAccountFlag == 0) {
				updateFlag = true;
			}
		}
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
					flag = this.suRongOpenAccountService.userOpenAccount(bean);
					if (!flag) {
						Users user2 = suRongOpenAccountService.selectUserById(userId);
						if (user2.getOpenAccount().intValue()==1) {
							// 执行结果(成功)
							status = ChinaPnrConstant.STATUS_SUCCESS;
							LogUtil.debugLog(OpenAccountServer.class.getName(), OpenAccountDefine.RETURL_SYN_MAPPING, "成功");
						}else {
							// 执行结果(失败)
							status = ChinaPnrConstant.STATUS_FAIL;
							LogUtil.endLog(OpenAccountServer.class.getName(), OpenAccountDefine.RETURL_SYN_MAPPING,
									"[开户完成后,更新用户信息失败]");
						}
					} else {
						// 执行结果(成功)
						status = ChinaPnrConstant.STATUS_SUCCESS;
						LogUtil.debugLog(OpenAccountServer.class.getName(), OpenAccountDefine.RETURL_SYN_MAPPING, "成功");
						String mobile =bean.getUsrMp();
						JSONObject params= this.suRongOpenAccountService.selectUserByMobile(userId,mobile);
						if(params!=null){
							Map<String, String> replaceMap = new HashMap<String, String>();
							replaceMap.put("val_name_new", params.getString("newUserName"));
							replaceMap.put("val_name_old", params.getString("oldUserName"));
							replaceMap.put("val_mobile", mobile);
							String[] email = { "sunjijin@hyjf.com","gaohonggang@hyjf.com","liudandan@hyjf.com"  };
							MailMessage messageMail = new MailMessage(null, replaceMap, "用户开户",null,null, email,CustomConstants.EMAILPARAM_TPL_REPEATMOBILE, MessageDefine.MAILSENDFORMAILINGADDRESS);
				          //  mailMessageProcesser.gather(messageMail);
						}
					}
				} catch (Exception e) {
					// 执行结果(失败)
					status = ChinaPnrConstant.STATUS_FAIL;
					LogUtil.errorLog(OpenAccountServer.class.getName(), OpenAccountDefine.RETURL_SYN_MAPPING, e);
				}
			} else {
				// 执行结果(失败)
				status = ChinaPnrConstant.STATUS_FAIL;
				LogUtil.debugLog(OpenAccountServer.class.getName(), OpenAccountDefine.RETURL_SYN_MAPPING, "失败");
			}

			if (ChinaPnrConstant.STATUS_SUCCESS.equals(status)) {
				message = "开户成功";
				ModelAndView modelAndView = new ModelAndView(OpenAccountDefine.OPEN_SUCCESS_PATH);
				LogUtil.endLog(OpenAccountServer.class.getName(), OpenAccountDefine.RETURL_SYN_MAPPING, "[交易完成后,回调结束]");
				modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
				if(StringUtils.isNotBlank(gesture)){
					modelAndView.addObject("gesture", gesture);
				}

                //速溶会写开户数据
				UsersInfo userinfo = suRongOpenAccountService.selectUserInfoById(user.getUserId());
                this.callBack(user.getUserId().toString(), 
                        suRongOpenAccountService.selectAccountChinapnrById(user.getUserId()).getChinapnrUsrid(),
                        userinfo.getTruename(),userinfo.getIdcard()
                        );
				return modelAndView;
			} else {
				message = "开户失败";
				ModelAndView modelAndView = new ModelAndView(OpenAccountDefine.OPEN_ERROR_PATH);
				modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
				return modelAndView;
			}

		} else {
			message = "用户已开户";
			ModelAndView modelAndView = new ModelAndView(OpenAccountDefine.OPEN_SUCCESS_PATH);
			modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
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
	@SuppressWarnings("static-access")
    @RequestMapping(OpenAccountDefine.RETURN_ASY_MAPPING)
	public ModelAndView openAccountAsyReturn(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) {

		LogUtil.startLog(OpenAccountServer.class.getName(), OpenAccountDefine.RETURN_ASY_MAPPING, "[开户异步回调开始]");
        String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host")) + BaseDefine.SURONG_REQUEST_HOME + "/";
        webhost = webhost.substring(0, webhost.length() - 1);// http://new.hyjf.com/hyjf-app/
		bean.convert();
		MerPriv merPriv = bean.getMerPrivPo();
		System.out.println("[开户异步回调开始]openAccountReturn bean:----------" + JSON.toJSONString(bean));
		String platform = merPriv.getClient();
		// 校验userId
		int userId = merPriv.getUserId();
		String gesture = request.getParameter("gesture");
		String message = "开户失败";
		System.out.println("用户开户同步调用:openAccountReturn------------------------------" + userId);
		String resCode = bean.getRespCode();
		System.out.println("resCode:" + resCode);
		if ("508".equals(resCode)) {
			message = "用户已开户";
			ModelAndView modelAndView = new ModelAndView(OpenAccountDefine.OPEN_ERROR_PATH);
			modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
			return modelAndView;
		}
		// 查询用户开户状态
		boolean updateFlag = false;
		Users user = this.suRongOpenAccountService.selectUserById(userId);
		if (user == null) {
			message = "用户未注册，请先注册！";
			ModelAndView modelAndView = new ModelAndView(OpenAccountDefine.OPEN_ERROR_PATH);
			modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
			return modelAndView;
		} else {
			int openAccountFlag = user.getOpenAccount();
			if (openAccountFlag == 0) {
				updateFlag = true;
			}
		}
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
					boolean flag = this.suRongOpenAccountService.userOpenAccount(bean);
					if (!flag) {
						Users userNew = this.suRongOpenAccountService.selectUserById(userId);
						if(userNew.getOpenAccount() ==1){
							// 执行结果(失败)
							status = ChinaPnrConstant.STATUS_SUCCESS;
							LogUtil.debugLog(OpenAccountServer.class.getName(), OpenAccountDefine.RETURN_ASY_MAPPING, "成功");
						}else{
							// 执行结果(失败)
							status = ChinaPnrConstant.STATUS_FAIL;
							LogUtil.endLog(OpenAccountServer.class.getName(), OpenAccountDefine.RETURN_ASY_MAPPING,
									"[开户完成后,更新用户信息失败]");
						}
					} else {
						// 执行结果(成功)
						status = ChinaPnrConstant.STATUS_SUCCESS;
						LogUtil.debugLog(OpenAccountServer.class.getName(), OpenAccountDefine.RETURN_ASY_MAPPING, "成功");
						String mobile =bean.getUsrMp();
						JSONObject params= this.suRongOpenAccountService.selectUserByMobile(userId,mobile);
						if(params!=null){
							Map<String, String> replaceMap = new HashMap<String, String>();
							replaceMap.put("val_name_new", params.getString("newUserName"));
							replaceMap.put("val_name_old", params.getString("oldUserName"));
							replaceMap.put("val_mobile", mobile);
							String[] email = { "sunjijin@hyjf.com","gaohonggang@hyjf.com","liudandan@hyjf.com"  };
							MailMessage messageMail = new MailMessage(null, replaceMap, "用户开户",null,null, email,CustomConstants.EMAILPARAM_TPL_REPEATMOBILE, MessageDefine.MAILSENDFORMAILINGADDRESS);
				          //  mailMessageProcesser.gather(messageMail);
						}
					}
				} catch (Exception e) {
					// 执行结果(失败)
					status = ChinaPnrConstant.STATUS_FAIL;
					LogUtil.errorLog(OpenAccountServer.class.getName(), OpenAccountDefine.RETURN_ASY_MAPPING, e);
				}

			} else {
				// 执行结果(失败)
				status = ChinaPnrConstant.STATUS_FAIL;
				LogUtil.debugLog(OpenAccountServer.class.getName(), OpenAccountDefine.RETURN_ASY_MAPPING, "失败");
			}

			if (ChinaPnrConstant.STATUS_SUCCESS.equals(status)) {
				message = "开户成功";
				ModelAndView modelAndView = new ModelAndView(OpenAccountDefine.OPEN_SUCCESS_PATH);
				LogUtil.endLog(OpenAccountServer.class.getName(), OpenAccountDefine.RETURL_SYN_MAPPING, "[交易完成后,回调结束]");
				modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
				if(StringUtils.isNotBlank(gesture)){
					modelAndView.addObject("gesture", gesture);
				}
				
				//速溶会写开户数据
				UsersInfo userinfo = suRongOpenAccountService.selectUserInfoById(user.getUserId());
                this.callBack(user.getUserId().toString(), 
                        suRongOpenAccountService.selectAccountChinapnrById(user.getUserId()).getChinapnrUsrid(),
                        userinfo.getTruename(),userinfo.getIdcard()
                        );               
                
                return modelAndView;
			} else {
				message = "开户失败";
				ModelAndView modelAndView = new ModelAndView(OpenAccountDefine.OPEN_ERROR_PATH);
				modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
				return modelAndView;
			}
		} else {
			message = "用户已开户";
			ModelAndView modelAndView = new ModelAndView(OpenAccountDefine.OPEN_SUCCESS_PATH);
			modelAndView.addObject(CustomConstants.APP_STATUS_DESC, message);
			if(StringUtils.isNotBlank(gesture)){
				modelAndView.addObject("gesture", gesture);
			}
			return modelAndView;

		}

	}
	
	/**
     * 融东风回调方法
     */
	public static void callBack(String userId, String chinapnrUsrid,String truename,String idCard){
	    Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userId);
        params.put("chinapnrUsrid", chinapnrUsrid);
        params.put("truename", truename);
        params.put("idCard", idCard);
        // 请求路径
        String requestUrl = PropUtils.getSystem("wcsr.open");
        InvestSysUtils.noRetPost(requestUrl,params);
	}
	

    /**
     * 无需等待返回的http请求
     * @param requestUrl
     * @param paramsMap
     */
    public static void noRetPost(String requestUrl, Map<String,String> paramsMap) {
            ExecutorService exec = Executors.newFixedThreadPool(15);
            NoRetTask task = new NoRetTask(requestUrl,paramsMap);
            exec.execute(task);
        }
    }

    /**
     * 无需等待返回的http请求类
     * @author zhangjinpeng
     *
     */
    class NoRetTask implements Runnable {
        String requestUrl = StringUtils.EMPTY;
        private Map<String, String> params;
        
        public NoRetTask(String requestUrl, Map<String, String> params){
            this.requestUrl = requestUrl;
            this.params = params;
        }
    
        @Override
        public void run() {
            // 调用服务接口
            HttpClientUtils.post(requestUrl, params);
        }

}
