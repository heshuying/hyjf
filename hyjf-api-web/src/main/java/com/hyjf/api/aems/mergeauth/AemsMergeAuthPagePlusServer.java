package com.hyjf.api.aems.mergeauth;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.aems.util.AemsErrorCodeConstant;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.user.auth.AuthBean;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.vip.apply.ApplyDefine;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * AEMS系统:多合一授权
 */
@Controller
@RequestMapping(value = AemsMergeAuthPagePlusDefine.REQUEST_MAPPING)
public class AemsMergeAuthPagePlusServer extends BaseController {

	Logger _log = LoggerFactory.getLogger(AemsMergeAuthPagePlusServer.class);

	@Autowired
	private AemsMergeAuthService authService;

	/**
	 * 
	 * 多合一授权
	 * 
	 * @author pcc
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(AemsMergeAuthPagePlusDefine.MERGE_AUTH_ACTION)
	public ModelAndView page(HttpServletRequest request, HttpServletResponse response,
							 @RequestBody AemsMergeAuthPagePlusRequestBean requestBean) {

		LogUtil.startLog(AemsMergeAuthPagePlusDefine.THIS_CLASS, AemsMergeAuthPagePlusDefine.MERGE_AUTH_ACTION);
        ModelAndView modelAndView = new ModelAndView(AemsMergeAuthPagePlusDefine.PATH_AUTH_PAGE_ERROR);
        _log.info("多合一授权第三方请求参数：" + JSONObject.toJSONString(requestBean));
        try {
	        // 验证请求参数
	        // 机构编号
	        if (Validator.isNull(requestBean.getInstCode())) {
	            getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_CE000001, "请求参数异常");
	            _log.info("请求参数异常[" + JSONObject.toJSONString(requestBean, true) + "]");
	            return modelAndView;
	        }
	        if (Validator.isNull(requestBean.getRetUrl())) {
	            getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_CE000001, "同步地址不能为空");
	            _log.info("同步地址不能为空[" + JSONObject.toJSONString(requestBean, true) + "]");
	            return modelAndView;
	        }
	        if (Validator.isNull(requestBean.getNotifyUrl())) {
	            getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_CE000001, "异步地址不能为空");
	            _log.info("异步地址不能为空[" + JSONObject.toJSONString(requestBean, true) + "]");
	            return modelAndView;
	        }
	        // 渠道
	        if (Validator.isNull(requestBean.getChannel())) {
	            getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_ZC000006, "渠道号不能为空");
	            _log.info("渠道号不能为空[" + JSONObject.toJSONString(requestBean, true) + "]");
	            return modelAndView;
	        }
	        if (Validator.isNull(requestBean.getAuthType())) {
	            getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_SQ000001, "授权类型不能为空");
	            _log.info("授权类型不能为空[" + JSONObject.toJSONString(requestBean, true) + "]");
	            return modelAndView;
	        }
	        if (!Arrays.asList(AuthBean.AUTH_TYPE_AUTO_CREDIT,
	        		AuthBean.AUTH_TYPE_AUTO_BID,
	        		AuthBean.AUTH_TYPE_PAYMENT_AUTH,
	        		AuthBean.AUTH_TYPE_REPAY_AUTH).contains(requestBean.getAuthType())) {
	            getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_SQ000002, "授权类型不是指定类型");
	            _log.info("授权类型不是指定类型");
	            return modelAndView;
	        }
	        
	        // 验签
	        // 机构编号  accountID  时间戳
	        if (!this.AEMSVerifyRequestSign(requestBean, AemsMergeAuthPagePlusDefine.REQUEST_MAPPING + AemsMergeAuthPagePlusDefine.MERGE_AUTH_ACTION)) {
	            _log.info("----验签失败----");
	            getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_CE000002, "验签失败");
	            _log.info("验签失败[" + JSONObject.toJSONString(requestBean, true) + "]");
	            return modelAndView;
	        }
	         
	        // 根据电子账户号查询用户ID
	        BankOpenAccount bankOpenAccount = this.authService.getBankOpenAccount(requestBean.getAccountId());
	        if(bankOpenAccount == null){
	            getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_CE000004, "没有根据电子银行卡找到用户");
	            _log.info("没有根据电子银行卡找到用户[" + JSONObject.toJSONString(requestBean, true) + "]");
	            return modelAndView;
	        }
	        
	        // 检查用户是否存在
	        Users user = authService.getUsers(bankOpenAccount.getUserId());//用户ID
	        if (user == null) {
	            getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_CE000007, "用户不存在汇盈金服账户");
	            _log.info("用户不存在汇盈金服账户[" + JSONObject.toJSONString(requestBean, true) + "]");
	            return modelAndView;
	        }
	        
	        if (user.getBankOpenAccount().intValue() != 1) {
	        	// 未开户
	            getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_CE000006, "用户未开户！");
	            _log.info("用户未开户！[" + JSONObject.toJSONString(requestBean, true) + "]");
	            return modelAndView;
	        }
	        
	        // 检查是否设置交易密码
	        Integer passwordFlag = user.getIsSetPassword();
	        if (passwordFlag != 1) {// 未设置交易密码
	            getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_TP000002, "未设置交易密码！");
	            return modelAndView;
	        }
	        
	        // 查询是否已经授权过
	        boolean isAuth = authService.checkIsAuth(user.getUserId(),requestBean.getAuthType());
	        if(isAuth){
	            getErrorMV(requestBean, modelAndView, AemsErrorCodeConstant.STATUS_CE000009, "已授权,请勿重复授权！");
	            return modelAndView;
	        }
	        // 拼装参数 调用江西银行
	        // 同步调用路径
	        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
	                + AemsMergeAuthPagePlusDefine.REQUEST_MAPPING + AemsMergeAuthPagePlusDefine.RETURL_SYN_ACTION + ".do?acqRes="
	                + requestBean.getAcqRes()  + "&authType="
	    	        + requestBean.getAuthType()+ "&callback="
	    	    	+ requestBean.getRetUrl().replace("#", "*-*-*");
	        // 异步调用路
	        String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
	                + AemsMergeAuthPagePlusDefine.REQUEST_MAPPING + AemsMergeAuthPagePlusDefine.RETURL_ASY_ACTION + ".do?acqRes="
	                + requestBean.getAcqRes() + "&authType="
	    	    	+ requestBean.getAuthType()+ "&callback=" 
	                + requestBean.getNotifyUrl().replace("#", "*-*-*");
	
	
			UsersInfo usersInfo =authService.getUsersInfoByUserId(user.getUserId());
			// 用户ID
			AemsMergeAuthBean authBean = new AemsMergeAuthBean();
			authBean.setUserType(0);
			authBean.setUserId(user.getUserId());
			authBean.setIp(CustomUtil.getIpAddr(request));
			authBean.setAccountId(bankOpenAccount.getAccount());
			// 同步 异步
			authBean.setRetUrl(retUrl);
			authBean.setNotifyUrl(bgRetUrl);
			// 0：PC 1：微官网 2：Android 3：iOS 4：其他
			authBean.setPlatform(requestBean.getPlatform());
			authBean.setAuthType(requestBean.getAuthType());
			authBean.setChannel(requestBean.getChannel());
			authBean.setForgotPwdUrl(requestBean.getForgotPwdUrl());
			authBean.setName(usersInfo.getTruename());
			authBean.setIdNo(usersInfo.getIdcard());
			authBean.setIdentity(usersInfo.getRoleId()+"");
			String orderId = GetOrderIdUtils.getOrderId2(authBean.getUserId());
			authBean.setOrderId(orderId);
			modelAndView = authService.getCallbankMV(authBean);
			String type="0";
			if(AuthBean.AUTH_TYPE_AUTO_BID.equals(requestBean.getAuthType())){
				type="1";
			}else if(AuthBean.AUTH_TYPE_AUTO_CREDIT.equals(requestBean.getAuthType())){
				type="4";
			}else if(AuthBean.AUTH_TYPE_PAYMENT_AUTH.equals(requestBean.getAuthType())){
				type="5";
			}else if(AuthBean.AUTH_TYPE_REPAY_AUTH.equals(requestBean.getAuthType())){
				type="6";
			}
			authService.insertUserAuthLog(authBean.getUserId(), orderId,
					Integer.parseInt(authBean.getPlatform()), type);
			LogUtil.endLog(AemsMergeAuthPagePlusServer.class.toString(),
					AemsMergeAuthPagePlusDefine.MERGE_AUTH_ACTION);
		} catch (Exception e) {
            e.printStackTrace();
            _log.info("多合一授权页面异常,异常信息:[" + e.toString() + "]");
            return null;
        }

		return modelAndView;
	}
	 private ModelAndView getErrorMV(AemsMergeAuthPagePlusRequestBean userOpenAccountRequestBean, ModelAndView modelAndView,
									 String status, String des) {
		AemsMergeAuthPagePlusResultBean repwdResult = new AemsMergeAuthPagePlusResultBean();
        BaseResultBean resultBean = new BaseResultBean();
        resultBean.setStatusForResponse(status);
        repwdResult.setCallBackAction(userOpenAccountRequestBean.getRetUrl());
        repwdResult.set("chkValue", resultBean.getChkValue());
        repwdResult.set("status", resultBean.getStatus());
        repwdResult.setAcqRes(userOpenAccountRequestBean.getAcqRes());
        modelAndView.addObject("callBackForm", repwdResult);
        return modelAndView;
    }
	/**
	 * 
	 * 多合一授权
	 * 
	 * @author pcc
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	 @RequestMapping(AemsMergeAuthPagePlusDefine.RETURL_SYN_ACTION)
    public ModelAndView mergeauthReturn(HttpServletRequest request, HttpServletResponse response,
         BankCallBean bean) {
        _log.info("多合一授权同步回调start,请求参数为：【"+JSONObject.toJSONString(bean, true)+"】");
        ModelAndView modelAndView = new ModelAndView(ApplyDefine.CALL_BACK_TRUSTEEPAY_VIEW);
        AemsMergeAuthPagePlusRetBean repwdResult = new AemsMergeAuthPagePlusRetBean();
        repwdResult.setCallBackAction(request.getParameter("callback").replace("*-*-*","#"));
        bean.convert();
        repwdResult.set("accountId", bean.getAccountId());
        String authType=request.getParameter("authType");
        
        String frontParams = request.getParameter("frontParams");
		
		if (StringUtils.isBlank(bean.getRetCode())
				&& StringUtils.isNotBlank(frontParams)) {
			JSONObject jsonParm = JSONObject.parseObject(frontParams);
			if (jsonParm.containsKey("RETCODE")) {
				bean.setRetCode(jsonParm.getString("RETCODE"));
			}
		}
		String retCode = bean.getRetCode();
		if (StringUtils.isBlank(retCode)
				|| !BankCallStatusConstant.RESPCODE_SUCCESS.equals(retCode)) {
			// 失败
            modelAndView.addObject("statusDesc", "多合一授权申请失败,失败原因：" + authService.getBankRetMsg(bean.getRetCode()));
            BaseResultBean resultBean = new BaseResultBean();
            resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_CE999999);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
            modelAndView.addObject("callBackForm", repwdResult);
			return modelAndView;
		}
        
		// 投资人签约状态查询
		_log.info("多合一授权同步回调调用查询接口查询状态,userId:" + bean.getLogUserId());
		BankCallBean retBean = authService.getTermsAuthQuery(
				Integer.parseInt(bean.getLogUserId()),
				BankCallConstant.CHANNEL_PC);
		if(authService.checkDefaultConfig(retBean,authType)){
			// 失败
            modelAndView.addObject("statusDesc", "多合一授权申请失败,失败原因：授权期限过短或额度过低，<br>请重新授权！");
            BaseResultBean resultBean = new BaseResultBean();
            resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_CE999999);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
            modelAndView.addObject("callBackForm", repwdResult);
			return modelAndView;
        }
		_log.info("多合一授权同步回调调用查询接口查询状态结束  结果为:"
				+ (retBean == null ? "空" : retBean.getRetCode()));
        if (retBean != null
				&& BankCallStatusConstant.RESPCODE_SUCCESS.equals(retBean
						.getRetCode())) {
        	
        	try {
				retBean.setOrderId(bean.getLogOrderId());
				// 更新签约状态和日志表
				this.authService.updateUserAuth(Integer.parseInt(retBean.getLogUserId()), retBean,authType);
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.errorLog(AemsMergeAuthPagePlusDefine.THIS_CLASS,
						AemsMergeAuthPagePlusDefine.RETURL_ASY_ACTION, e);
			}
            modelAndView.addObject("statusDesc", "多合一授权申请成功！");
            BaseResultBean resultBean = new BaseResultBean();
            resultBean.setStatusForResponse(AemsErrorCodeConstant.SUCCESS);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
            repwdResult.set("deadline", bean.getDeadline());
        } else {
            // 失败
            modelAndView.addObject("statusDesc", "多合一授权申请失败,失败原因：" + authService.getBankRetMsg(bean.getRetCode()));
            BaseResultBean resultBean = new BaseResultBean();
            resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_CE999999);
            repwdResult.set("chkValue", resultBean.getChkValue());
            repwdResult.set("status", resultBean.getStatus());
        }
        
        //------------------------------------------------
        repwdResult.setAcqRes(request.getParameter("acqRes"));
        _log.info("多合一授权同步第三方返回参数："+JSONObject.toJSONString(repwdResult));
        modelAndView.addObject("callBackForm", repwdResult);
        _log.info("多合一授权申请同步回调end");
        return modelAndView;
    }

	/**
	 * 多合一授权异步回调
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(AemsMergeAuthPagePlusDefine.RETURL_ASY_ACTION)
	public BankCallResult mergeauthBgreturn(HttpServletRequest request, HttpServletResponse response,
                                            @ModelAttribute BankCallBean bean) {
		_log.info("多合一授权异步回调start");
        BankCallResult result = new BankCallResult();
        BaseResultBean resultBean = new BaseResultBean();
        Map<String, String> params = new HashMap<String, String>();
        String message = "";
        String status = "";
        if (bean == null) {
            _log.info("调用江西银行多合一授权接口,银行异步返回空");
            params.put("status", BaseResultBean.STATUS_FAIL);
            resultBean.setStatusForResponse(BaseResultBean.STATUS_FAIL);
            params.put("statusDesc", "多合一授权失败,调用银行接口失败");
            result.setMessage("多合一授权失败");
            params.put("chkValue", resultBean.getChkValue());
            params.put("acqRes", request.getParameter("acqRes"));
            result.setStatus(false);
            CommonSoaUtils.noRetPostThree(request.getParameter("callback").replace("*-*-*", "#"), params);
            return result;
        }

        bean.convert();
		int userId = Integer.parseInt(bean.getLogUserId());
		String authType = request.getParameter("authType");
		if (authService.checkDefaultConfig(bean, authType)) {
			_log.info("[用户合并授权完成后,回调结束]");
			params.put("status", BaseResultBean.STATUS_FAIL);
			resultBean.setStatusForResponse(BaseResultBean.STATUS_FAIL);
			params.put("statusDesc", "多合一授权失败,调用银行接口失败");
			result.setMessage("合并授权成功");
			params.put("chkValue", resultBean.getChkValue());
			params.put("acqRes", request.getParameter("acqRes"));
			result.setStatus(true);
			CommonSoaUtils.noRetPostThree(request.getParameter("callback").replace("*-*-*", "#"), params);
			return result;
		}
     	
        // 更新签约状态和日志表
		try {
			if (BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE))) {
				bean.setOrderId(bean.getLogOrderId());
				this.authService.updateUserAuth(userId, bean, authType);
				message = "多合一授权成功";
				status = AemsErrorCodeConstant.SUCCESS;
			} else {
				// 失败
				message = "多合一授权失败,失败原因：" + authService.getBankRetMsg(bean.getRetCode());
				status = AemsErrorCodeConstant.STATUS_CE999999;
			}
		} catch (Exception e) {
			_log.error("多合一授权出错,userId:【" + userId + "】错误原因：" + e.getMessage(), e);
			message = "多合一授权失败";
			status = AemsErrorCodeConstant.STATUS_CE999999;
		}
        // 返回值
        params.put("accountId", bean.getAccountId());
        params.put("status", status);
        params.put("statusDesc",message);

        params.put("paymentAuth", bean.getPaymentAuth());
        params.put("paymentDeadline", bean.getPaymentDeadline());
        params.put("paymentMaxAmt", bean.getPaymentMaxAmt());

		params.put("repayAuth", bean.getRepayAuth());
		params.put("repayDeadline", bean.getRepayDeadline());
		params.put("repayMaxAmt", bean.getRepayMaxAmt());


        resultBean.setStatusForResponse(status);
        params.put("chkValue", resultBean.getChkValue());
        params.put("acqRes",request.getParameter("acqRes"));
        _log.info("多合一授权第三方返回参数："+JSONObject.toJSONString(params));
        CommonSoaUtils.noRetPostThree(request.getParameter("callback").replace("*-*-*","#"), params);
        _log.info("多合一授权异步回调end");
        result.setMessage("多合一授权权成功");
        result.setStatus(true);
		return result;
	}
}
