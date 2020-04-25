package com.hyjf.web.user.pandect;

import com.hyjf.bank.service.user.assetmanage.AssetManageService;
import com.hyjf.bank.service.user.pandect.PandectService;
import com.hyjf.bank.service.user.recharge.RechargeService;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.web.RecentPaymentListCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.web.BaseController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.invest.InvestService;
import com.hyjf.web.util.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 账户总览
 *
 * @author HBZ
 */
@Controller
@RequestMapping(value = PandectDefine.REQUEST_MAPPING)
public class PandectController extends BaseController {
	@Autowired
	PandectService pandectService;

	@Autowired
	private InvestService investService;
	@Autowired
	private AssetManageService assetManageService;
	@Autowired
	private RechargeService userRechargeService;

	private static String HOST = PropUtils.getSystem("hyjf.web.host").trim();


	/**
	 * 账户总览
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(PandectDefine.PANDECT_ACTION)
	public ModelAndView pandect(HttpServletRequest request) throws ParseException {
		LogUtil.startLog(PandectController.class.toString(), PandectDefine.PANDECT_ACTION);
		ModelAndView modelAndView = new ModelAndView(PandectDefine.PANDECT_PATH);
		WebViewUser wuser = WebUtils.getUser(request);
		if (wuser == null) {
			modelAndView.addObject("message", "用户信息失效，请您重新登录。");
			return new ModelAndView("error/systemerror");
		}
		Integer userId = wuser.getUserId();
		Users user = pandectService.getUsers(userId);
		
		String imghost = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.head.url"));
        imghost = imghost.substring(0, imghost.length() - 1);// http://cdn.huiyingdai.com/
        // 实际物理路径前缀2
        String fileUploadTempPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.head.path"));
        if(StringUtils.isNotEmpty(user.getIconurl())){
            user.setIconurl(imghost + fileUploadTempPath + user.getIconurl());
        }
		
		modelAndView.addObject("user", user);
		//上次登录时间
		if (user.getLastTime() != null) {
			modelAndView.addObject("lastlogintime", GetDate.formatTime(user.getLastTime() * 1000L));
		}
		modelAndView.addObject("webViewUser", wuser);
		modelAndView.addObject("auth", user.getAuthStatus());
		// 企业用户
		if (user.getUserType() == 1) {
			// 判断企业用户是否已开户
			int isAccount = this.pandectService.isCompAccount(userId);
			modelAndView.addObject("isAccount", isAccount);
			// 是否已绑定用户
			int isBindUser = this.pandectService.isCompBindUser(userId);
			modelAndView.addObject("isBindUser", isBindUser);
		}
		//用户名
		UsersInfo usersinfo = pandectService.getUsersInfoByUserId(userId);
		modelAndView.addObject("usersinfo", usersinfo);
		String username = "";
		if(StringUtils.isNotEmpty(usersinfo.getTruename())){
            username = usersinfo.getTruename().substring(0, 1);
            if(usersinfo.getSex() == 2){ //女
                username = username + "女士";
            }else{
                username = username + "先生";
            }
        }else{
        	username = user.getUsername();
    		int len = username.length();
    		if(isChineseChar(username)){
    			if(len > 4){
    				username = username.substring(0,4)+"...";
    			}
    		}else{
    			if(len > 8){
    				username = username.substring(0,8)+"...";
    			}
    		}
        }
		modelAndView.addObject("username", username);

        // del by liuyang 用户是否测评修改 start
		// JSONObject jsonObject = CommonSoaUtils.getUserEvalationResultByUserId(userId + "");
		// Integer userEvaluationResultFlag = (Integer) jsonObject.get("userEvaluationResultFlag");
		// del by liuyang 用户是否测评修改 end
//		modelAndView.addObject("userEvaluationResultFlag", user.getIsEvaluationFlag());
        try {
			if(user.getIsEvaluationFlag()==1 && null != user.getEvaluationExpiredTime()){
				//测评到期日
				Long lCreate = user.getEvaluationExpiredTime().getTime();
				//当前日期
				Long lNow = System.currentTimeMillis();
				if (lCreate <= lNow) {
					//已过期需要重新评测
					modelAndView.addObject("userEvaluationResultFlag", "2");
				} else {
					//未到一年有效期
					modelAndView.addObject("userEvaluationResultFlag", "1");
				}
			}else{
				modelAndView.addObject("userEvaluationResultFlag", "0");
			}
			// modify by liuyang 20180411 用户是否完成风险测评标识 end
		} catch (Exception e) {
			logger.error("查询用户是否完成风险测评标识出错....", e);
			modelAndView.addObject("userEvaluationResultFlag", "0");
		}
		modelAndView.addObject("chinapnrHide", "0");
		Account account = pandectService.getAccount(userId);
		String closeTimeStr = pandectService.getParamName("CLOSE_WITHDRAWAL","1");
		if(StringUtils.isNotBlank(closeTimeStr)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date closeDate = sdf.parse(closeTimeStr);
                Date now = new Date();
				String nowStr = sdf.format(now);
				//关闭时间往后延3个月90天
				long threeMonth = 7776000000L;
                //汇付提将于2018年12月30号停止提现服务 showEdition 0旧  1新
                if (now.getTime() < closeDate.getTime()) {
                    modelAndView.addObject("chinapnrHide", "0");
                }else if(closeDate.getTime()+threeMonth>now.getTime()||closeTimeStr.equals(nowStr)){
					modelAndView.addObject("chinapnrHide", "1");
				}else if(closeDate.getTime()+threeMonth<now.getTime()){
					modelAndView.addObject("chinapnrHide", "2");
				}
			}catch (Exception e){
				modelAndView.addObject("chinapnrHide", "1");
			}

		}
		modelAndView.addObject("account", account);
		// 获取用户的汇付信息
		AccountChinapnr accountChinapnr = pandectService.getAccountChinapnr(userId);
		if (accountChinapnr != null) {
			modelAndView.addObject("accountChinapnr", accountChinapnr);
		}
		
		// 获取用户的银行电子账户信息
        BankOpenAccount bankOpenAccount = pandectService.getBankOpenAccount(userId);
        if (bankOpenAccount != null) {
            modelAndView.addObject("bankOpenAccount", bankOpenAccount);
        }
		// 根据用户Id查询用户银行卡号 add by tyy 2018-6-27
		BankCard bankCard = this.userRechargeService.selectBankCardByUserId(userId);
		modelAndView.addObject("bankCard", bankCard);

		Map<String, Object> paraMap=new HashMap<String, Object>();
	    paraMap.put("userId", userId);
	    paraMap.put("limitStart", 0);
	    paraMap.put("limitEnd", 4);
		List<RecentPaymentListCustomize> recoverLatestList = assetManageService.selectRecentPaymentList(paraMap);
		modelAndView.addObject("recoverLatestList", recoverLatestList);
		LogUtil.endLog(PandectController.class.toString(), PandectDefine.PANDECT_ACTION);
		return modelAndView;
	}
	public static boolean isChineseChar(String str){
        boolean temp = false;
        Pattern p=Pattern.compile("[\u4e00-\u9fa5]"); 
        Matcher m=p.matcher(str); 
        if(m.find()){ 
            temp =  true;
        }
        return temp;
    }

	/**
	 * 
	 * @method: appointment
	 * @description: 预约授权
	 * @param request
	 * @param modelMap
	 * @param authType
	 * @param money
	 * @return
	 * @return: ModelAndView
	 * @mender: zhouxiaoshuai
	 * @date: 2016年7月26日 下午1:28:11
	 */
	@RequestMapping(PandectDefine.APPOINTMENT_ACTION)
	public ModelAndView appointment(HttpServletRequest request, ModelMap modelMap, String appointment, String authType,
			String money)  {
		LogUtil.startLog(PandectController.class.toString(), PandectDefine.APPOINTMENT_ACTION);
		Integer userId = WebUtils.getUserId(request);
		if (appointment == null || (!appointment.equals("1") && !appointment.equals("0"))) {
			modelMap.put("message", "请求参数错误");
			return new ModelAndView(PandectDefine.APPOINTMENT_FAIL_PATH);
		}
		modelMap.put("appointment", appointment);
		AccountChinapnr accountChinapnrTender = investService.getAccountChinapnr(userId);
		// 用户未在平台开户
		if (accountChinapnrTender == null) {
			modelMap.put("message", "用户开户信息不存在");
			return new ModelAndView(PandectDefine.APPOINTMENT_FAIL_PATH);
		} else {
			// 判断借款人开户信息是否存在
			if (accountChinapnrTender.getChinapnrUsrcustid() == null) {
				modelMap.put("message", "用户汇付客户号不存在");
				return new ModelAndView(PandectDefine.APPOINTMENT_FAIL_PATH);
			} else {
				// 预约接口查询
				Map<String, Object> appointmentMap = pandectService.checkAppointmentStatus(userId, appointment);
				boolean appointmentFlag = (boolean) appointmentMap.get("appointmentFlag");
				String isError = appointmentMap.get("isError") + "";
				if (appointmentFlag) {
					// 回调路径
					String returl = HOST + PandectDefine.REQUEST_MAPPING + PandectDefine.RETURL_SYN_ACTION
							+ ".do?userId=" + userId + "&appointment=" + appointment;
					if (authType == null) {
						authType = "W";// 预留部分授权 P
					}
					Long tenderUsrcustid = accountChinapnrTender.getChinapnrUsrcustid();
					ChinapnrBean chinapnrBean = new ChinapnrBean();
					// 接口版本号
					chinapnrBean.setVersion(ChinaPnrConstant.VERSION_10);// 2.0
					if (appointment.equals("1")) {
						// 消息类型(自动投标计划)
						chinapnrBean.setCmdId(ChinaPnrConstant.CMDID_AUTO_TENDER_PLAN);
						chinapnrBean.setTenderPlanType(authType);
					} else {
						// 消息类型(自动投标计划关闭)
						chinapnrBean.setCmdId(ChinaPnrConstant.CMDID_AUTO_TENDER_PLAN_CLOSE);
					}
					// 商户客户号
					chinapnrBean.setMerCustId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID));
					// 用户客户号
					chinapnrBean.setUsrCustId(tenderUsrcustid + "");
					if (authType != null && authType.equals("P")) {
						// 判断用户出借金额是否为空
						if (StringUtils.isEmpty(money)) {
							modelMap.put("message", "请输入保留金额");
							return new ModelAndView(PandectDefine.APPOINTMENT_FAIL_PATH);
						} else {
							// 金额必须是整数
							Long accountInt = Long.parseLong(money);
							if (accountInt == 0) {
								modelMap.put("message", "保留金额不能为0元");
								return new ModelAndView(PandectDefine.APPOINTMENT_FAIL_PATH);
							} else {
								if (accountInt < 0) {
									modelMap.put("message", "保留金额不能为负数");
									return new ModelAndView(PandectDefine.APPOINTMENT_FAIL_PATH);
								} else {
									// 交易金额(必须)
									chinapnrBean.setTransAmt(CustomUtil.formatAmount(money));
								}
							}

						}
					}
					chinapnrBean.setRetUrl(returl); // 页面返回
					try {
						return ChinapnrUtil.callApi(chinapnrBean);
					} catch (Exception e) {
						e.printStackTrace();
						modelMap.put("message", "自动出借授权操作失败!");
						return new ModelAndView(PandectDefine.APPOINTMENT_FAIL_PATH);
					}
				} else {
					Users user = pandectService.getUsers(userId);
					Integer AuthStatus = 0;
					String AuthType = "W";
					if (user.getAuthType() != null && user.getAuthType() == 1) {
						AuthType = "P";
					}
					if (user.getAuthStatus() != null) {
						AuthStatus = user.getAuthStatus();
					}
					if (isError.equals("0")) {
						if (appointment.equals("1") && AuthStatus == 0) {
							// 开启授权操作
							pandectService.updateUserAuthStatus(AuthType, appointment, userId + "");
							modelMap.put("message", "恭喜你自动出借授权成功!");
						} else if (appointment.equals("1") && AuthStatus == 1) {
							// 已经开启授权
							modelMap.put("message", "恭喜你自动出借授权成功!");
						} else if (appointment.equals("0") && AuthStatus == 1) {
							// 关闭授权操作
							pandectService.updateUserAuthStatus(AuthType, appointment, userId + "");
							modelMap.put("message", "您的自动出借授权已取消!");
						} else if (appointment.equals("0") && AuthStatus == 0) {
							// 已经授权
							modelMap.put("message", "您的自动出借授权已取消!");
						}
						return new ModelAndView(PandectDefine.APPOINTMENT_SUCCESS_PATH);
					} else if (isError.equals("2")) {
						// 用户还有汇添金计划
						modelMap.put("message", "关闭自动出借授权失败!");
						modelMap.put("error", "您当地有申购中/锁定中的汇添金计划，暂时不能取消授权");
						return new ModelAndView(PandectDefine.APPOINTMENT_FAIL_PATH);
					} else {

						if (appointment.equals("1")) {
							modelMap.put("message", "自动出借授权失败!");
						} else {
							modelMap.put("message", "关闭自动出借授权失败!");
						}
						modelMap.put("error", "调用自动出借授权查询接口失败");
						return new ModelAndView(PandectDefine.APPOINTMENT_FAIL_PATH);
					}
				}

			}

		}

	}

	/**
	 * 
	 * @method: appointmentRetUrl
	 * @description: 预约授权同步回调
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 * @return: ModelAndView
	 * @mender: zhouxiaoshuai
	 * @date: 2016年7月26日 下午1:35:42
	 */
	// pay模块回调此方法
	@RequestMapping(PandectDefine.RETURL_SYN_ACTION)
	public ModelAndView appointmentRetUrl(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute ChinapnrBean bean, String userId, String appointment, ModelMap modelMap) {

		System.out.println("开始调用自动出借授权同步方法");
		if (userId == null || appointment == null) {
			modelMap.put("message", "自动出借授权失败!");
			modelMap.put("error", "回调的参数为null");
			return new ModelAndView(PandectDefine.APPOINTMENT_FAIL_PATH);
		} else {
			modelMap.put("appointment", appointment);
			bean.convert();
			modelMap.put("userId", userId);
			if (appointment.equals("1") || appointment.equals("0")) {
				// 授权
				boolean authFlag = pandectService.updateUserAuthStatus(bean.getTenderPlanType(), appointment, userId);
				if (authFlag) {
					if (appointment.equals("1")) {
						modelMap.put("message", "恭喜你自动出借授权成功!");
					} else {
						modelMap.put("message", "您的自动出借授权已取消!");
					}
					modelMap.put("error", "自动出借授权操作成功");
					return new ModelAndView(PandectDefine.APPOINTMENT_SUCCESS_PATH);
				} else {
					if (appointment.equals("1")) {
						modelMap.put("message", "自动出借授权失败!");
					} else {
						modelMap.put("message", "关闭自动出借授权失败!");
					}
					modelMap.put("error", "更新自动出借授权数据失败");
					return new ModelAndView(PandectDefine.APPOINTMENT_FAIL_PATH);
				}

			} else {
				if (appointment.equals("1")) {
					modelMap.put("message", "自动出借授权失败!");
				} else {
					modelMap.put("message", "关闭自动出借授权失败!");
				}
				modelMap.put("error", "系统异常");
				return new ModelAndView(PandectDefine.APPOINTMENT_FAIL_PATH);
			}
		}

	}

}
