package com.hyjf.wechat.controller.user.auth.mergeauth;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.auth.AuthBean;
import com.hyjf.bank.service.user.auth.AuthService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.wechat.BaseResultBeanFrontEnd;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseMapBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 合并授权
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = MergeAuthPagePlusDefine.REQUEST_MAPPING)
public class MergeAuthPagePlusController extends BaseController {

	Logger _log = LoggerFactory.getLogger(MergeAuthPagePlusController.class);

	@Autowired
	private AuthService authService;

	/**
     * 当前controller名称
     */
    public static final String THIS_CLASS = MergeAuthPagePlusController.class.getName();

	/**
	 * 
	 * 合并授权
	 * 
	 * @author pcc
	 * @param request
	 * @param response
	 * @return
	 */
    @SignValidate
	@RequestMapping(MergeAuthPagePlusDefine.MERGE_AUTH_ACTION)
	public ModelAndView userAuthInves(HttpServletRequest request,
			HttpServletResponse response) {

		LogUtil.startLog(MergeAuthPagePlusDefine.THIS_CLASS,
				MergeAuthPagePlusDefine.MERGE_AUTH_ACTION);
		ModelAndView modelAndView = new ModelAndView();
		BaseMapBean baseMapBean=new BaseMapBean();
        // 获取sign缓存
		String sign = request.getParameter("sign");
        //判断用户是否登录
        Integer userId = requestUtil.getRequestUserId(request);
        if(userId==null||userId<=0){
            return getErrorMV("失败原因：用户未登录！",baseMapBean,sign);
        }
        BankOpenAccount account = this.authService.getBankOpenAccount(userId);
        if (account == null) {
            return getErrorMV("失败原因：用户未开户！",baseMapBean,sign);
        }
        Users user = this.authService.getUsers(userId);
        if (user.getIsSetPassword() == 0) {// 未设置交易密码
            return getErrorMV("失败原因：用户未设置交易密码！",baseMapBean,sign);
        }
        // 判断是否授权过  
        if(authService.checkIsAuth(user.getUserId(),AuthBean.AUTH_TYPE_MERGE_AUTH)){
            return getErrorMV("失败原因：用户已授权,无需重复授权！",baseMapBean,sign);
        }

		// 拼装参数 调用江西银行
		// 同步调用路径
		String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+ request.getContextPath()
				+ MergeAuthPagePlusDefine.REQUEST_MAPPING 
				+ MergeAuthPagePlusDefine.RETURL_SYN_ACTION + ".page?authType="+AuthBean.AUTH_TYPE_MERGE_AUTH+"&sign="+sign;
		// 异步调用路
		String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+ request.getContextPath()
				+ MergeAuthPagePlusDefine.REQUEST_MAPPING
				+ MergeAuthPagePlusDefine.RETURL_ASY_ACTION + ".do";

		UsersInfo usersInfo =authService.getUsersInfoByUserId(user.getUserId());

		// 用户ID
		AuthBean authBean = new AuthBean();
		authBean.setUserId(user.getUserId());
		authBean.setIp(CustomUtil.getIpAddr(request));
		authBean.setAccountId(account.getAccount());
		// 同步 异步
		authBean.setRetUrl(retUrl);
		authBean.setNotifyUrl(bgRetUrl);
		// 0：PC 1：微官网 2：Android 3：iOS 4：其他
		authBean.setPlatform(CustomConstants.CLIENT_WECHAT);
		authBean.setAuthType(AuthBean.AUTH_TYPE_MERGE_AUTH);
		authBean.setChannel(BankCallConstant.CHANNEL_WEI);
		authBean.setForgotPwdUrl(CustomConstants.FORGET_PASSWORD_URL + "?sign=" + sign);
		authBean.setName(usersInfo.getTruename());
		authBean.setIdNo(usersInfo.getIdcard());
		authBean.setIdentity(usersInfo.getRoleId()+"");
		authBean.setUserType(user.getUserType());
		// 跳转到汇付天下画面
		try {
			String orderId = GetOrderIdUtils.getOrderId2(authBean.getUserId());
			authBean.setOrderId(orderId);
			modelAndView = authService.getCallbankMV(authBean);
			if(authBean.getAutoBidStatus()&&authBean.getAutoCreditStatus()&&authBean.getPaymentAuthStatus()){
            	//开通自动出借、自动债转、缴费授权
            	authService.insertUserAuthLog(authBean.getUserId(), orderId, Integer.parseInt(authBean.getPlatform()), "14");
            }else if(authBean.getAutoBidStatus()&&authBean.getAutoCreditStatus()){
            	//开通自动出借、自动债转
            	authService.insertUserAuthLog(authBean.getUserId(), orderId, Integer.parseInt(authBean.getPlatform()), "11");
            }else if(authBean.getAutoBidStatus()&&authBean.getPaymentAuthStatus()){
            	//开通自动出借、缴费授权
            	authService.insertUserAuthLog(authBean.getUserId(), orderId, Integer.parseInt(authBean.getPlatform()), "12");
            }else if(authBean.getPaymentAuthStatus()&&authBean.getAutoCreditStatus()){
            	//开通自动债转、缴费授权
            	authService.insertUserAuthLog(authBean.getUserId(), orderId, Integer.parseInt(authBean.getPlatform()), "13");
            }else if(authBean.getAutoBidStatus()){
            	//开通自动出借
            	authService.insertUserAuthLog(authBean.getUserId(), orderId, Integer.parseInt(authBean.getPlatform()), "1");
            }else if(authBean.getAutoCreditStatus()){
            	//开通自动债转
            	authService.insertUserAuthLog(authBean.getUserId(), orderId, Integer.parseInt(authBean.getPlatform()), "4");
            }else if(authBean.getPaymentAuthStatus()){
            	//开通缴费授权
            	authService.insertUserAuthLog(authBean.getUserId(), orderId, Integer.parseInt(authBean.getPlatform()), "5");
            }else{
            	//开通自动出借、自动债转、缴费授权
            	authService.insertUserAuthLog(authBean.getUserId(), orderId, Integer.parseInt(authBean.getPlatform()), "14");
            }
			LogUtil.endLog(MergeAuthPagePlusController.class.toString(),
					MergeAuthPagePlusDefine.MERGE_AUTH_ACTION);
		} catch (Exception e) {
			e.printStackTrace();
			modelAndView = new ModelAndView(
					MergeAuthPagePlusDefine.USER_AUTH_ERROR_PATH);
			modelAndView.addObject("message", "调用银行接口失败！");
			LogUtil.errorLog(MergeAuthPagePlusController.class.toString(),
					MergeAuthPagePlusDefine.MERGE_AUTH_ACTION, e);
		}

		return modelAndView;
	}
	
	private ModelAndView getErrorMV(String msg, BaseMapBean baseMapBean,String sign) {
        ModelAndView modelAndView = new ModelAndView(MergeAuthPagePlusDefine.JUMP_HTML);
        baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.FAIL);
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, msg);
        baseMapBean.set("authType", MergeAuthPagePlusDefine.AUTH_TYPE);
		baseMapBean.set("sign", sign);
        if("失败原因:授权期限过短或额度过低，<br>请重新授权！".equals(msg)){
        	baseMapBean.setCallBackAction(CustomConstants.HOST+MergeAuthPagePlusDefine.AUTH_TENDER_AGAIN_ERROR_PATH);
        }else{
        	baseMapBean.setCallBackAction(CustomConstants.HOST+MergeAuthPagePlusDefine.USER_AUTH_ERROR_PATH);
        }
        modelAndView.addObject("callBackForm", baseMapBean);
        LogUtil.endLog(THIS_CLASS, MergeAuthPagePlusDefine.MERGE_AUTH_ACTION);
        return modelAndView;
    }
    
    private ModelAndView getSuccessMV(String msg, BaseMapBean baseMapBean) {
        ModelAndView modelAndView = new ModelAndView(MergeAuthPagePlusDefine.JUMP_HTML);
        baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, msg);
        baseMapBean.set("authType", MergeAuthPagePlusDefine.AUTH_TYPE);
        baseMapBean.setCallBackAction(CustomConstants.HOST+MergeAuthPagePlusDefine.USER_AUTH_SUCCESS_PATH);
        modelAndView.addObject("callBackForm", baseMapBean);
        LogUtil.endLog(THIS_CLASS, MergeAuthPagePlusDefine.MERGE_AUTH_ACTION);
        return modelAndView;
    }

	/**
	 * 
	 * 合并授权
	 * 
	 * @author pcc
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@RequestMapping(MergeAuthPagePlusDefine.RETURL_SYN_ACTION)
	public ModelAndView mergeAuthReturn(HttpServletRequest request,
			HttpServletResponse response, @ModelAttribute BankCallBean bean) {

		LogUtil.startLog(THIS_CLASS, MergeAuthPagePlusDefine.RETURL_SYN_ACTION, "[合并授权同步回调开始]");
        BaseMapBean baseMapBean=new BaseMapBean();
        bean.convert();
        String sign = request.getParameter("sign");
        String frontParams = request.getParameter("frontParams");
        if(StringUtils.isBlank(bean.getRetCode())&&StringUtils.isNotBlank(frontParams)){
            JSONObject jsonParm = JSONObject.parseObject(frontParams);
            if(jsonParm.containsKey("RETCODE")){
                bean.setRetCode(jsonParm.getString("RETCODE"));
            }
        }
        String isSuccess = request.getParameter("isSuccess");
        if (!"1".equals(isSuccess)) {
        	return getErrorMV("失败原因:"+authService.getBankRetMsg(bean.getRetCode()),baseMapBean,sign);
        }
        String retCode = bean.getRetCode();
        if(StringUtils.isNotBlank(retCode)&& !BankCallStatusConstant.RESPCODE_SUCCESS.equals(retCode)){
            return getErrorMV("失败原因:"+authService.getBankRetMsg(bean.getRetCode()),baseMapBean,sign);
        }
        // 出借人签约状态查询
        logger.info("合并授权同步回调调用查询接口查询状态");
        BankCallBean retBean=authService.getTermsAuthQuery(Integer.parseInt(bean.getLogUserId()),BankCallConstant.CHANNEL_WEI);
        
//        bean=retBean;
        // 返回失败
        if (retBean!=null&& BankCallStatusConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode())) {
            logger.info("合并授权同步回调调用查询接口查询状态结束  结果为:" + retBean.getRetCode() + "授权状态为：" +retBean.getPaymentAuth());
            // 成功
            if("1".equals(retBean.getPaymentAuth())){
            	try {
            		if(authService.checkDefaultConfig(retBean,AuthBean.AUTH_TYPE_MERGE_AUTH)){
                        return getErrorMV("失败原因:授权期限过短或额度过低，<br>请重新授权！",baseMapBean,sign);
                    }
    				retBean.setOrderId(bean.getLogOrderId());
    				// 更新签约状态和日志表
    				this.authService.updateUserAuth(
    						Integer.parseInt(bean.getLogUserId()), retBean,AuthBean.AUTH_TYPE_MERGE_AUTH);
    			} catch (Exception e) {
    				e.printStackTrace();
    				LogUtil.errorLog(MergeAuthPagePlusDefine.THIS_CLASS,
    						MergeAuthPagePlusDefine.RETURL_ASY_ACTION, e);
    			}
                return getSuccessMV("合并授权成功！",baseMapBean); 
            }else{
                return getErrorMV("失败原因:"+authService.getBankRetMsg(retCode),baseMapBean,sign);
            }
        } else {
            logger.info("合并授权同步回调调用查询接口查询失败");
            return getErrorMV("失败原因:"+authService.getBankRetMsg(retCode),baseMapBean,sign);
        }
	}

	/**
	 * 合并授权异步回调
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(MergeAuthPagePlusDefine.RETURL_ASY_ACTION)
	public BankCallResult mergeAuthBgreturn(HttpServletRequest request,
			HttpServletResponse response, @ModelAttribute BankCallBean bean) {
		BankCallResult result = new BankCallResult();
		_log.info("[合并授权异步回调开始]");
		LogUtil.startLog(MergeAuthPagePlusDefine.THIS_CLASS,
				MergeAuthPagePlusDefine.RETURL_ASY_ACTION, "[合并授权异步回调开始]");
		bean.convert();
		Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
		// 查询用户开户状态
		Users user = this.authService.getUsers(userId);
		if(authService.checkDefaultConfig(bean,AuthBean.AUTH_TYPE_MERGE_AUTH)){
	       	 _log.info("[用户合并授权完成后,回调结束]");
	         result.setMessage("合并授权成功");
	         result.setStatus(true);
	         return result;
	    }
		// 成功
		if (user != null
				&& bean != null
				&& (BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE)))) {
			try {
				bean.setOrderId(bean.getLogOrderId());
				// 更新签约状态和日志表
				this.authService.updateUserAuth(
						Integer.parseInt(bean.getLogUserId()), bean,AuthBean.AUTH_TYPE_MERGE_AUTH);
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.errorLog(MergeAuthPagePlusDefine.THIS_CLASS,
						MergeAuthPagePlusDefine.RETURL_ASY_ACTION, e);
			}
		}
		LogUtil.endLog(MergeAuthPagePlusDefine.THIS_CLASS,
				MergeAuthPagePlusDefine.RETURL_ASY_ACTION,
				"[用户合并授权完成后,回调结束]");
		_log.info("[用户合并授权完成后,回调结束]");
		result.setMessage("合并授权成功");
		result.setStatus(true);
		return result;
	}
}
