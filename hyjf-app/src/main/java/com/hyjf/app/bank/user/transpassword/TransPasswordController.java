/**
 * 个人设置控制器
 */
package com.hyjf.app.bank.user.transpassword;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.app.user.manage.AppUserService;
import com.hyjf.bank.service.user.transpassword.TransPasswordService;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseMapBean;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.bank.user.auto.AutoDefine;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.app.util.SignValue;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.SmsConfig;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

/**
 * @package com.hyjf.web.user.recharge
 * @author GOGTZ-T
 * @date 2015/12/04 14:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = TransPasswordDefine.REQUEST_MAPPING)
public class TransPasswordController extends BaseController {

	@Autowired
	private AppTransPasswordService appTransPasswordService;

	@Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;

	@Autowired
	private TransPasswordService transPasswordService;
	@Autowired
	private AppUserService appUserService;

	/**
     * 设置交易密码同步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(TransPasswordDefine.INIT)
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {

        LogUtil.startLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.SETPASSWORD_ACTION, "[开户同步回调开始]");
        ModelAndView modelAndView = new ModelAndView();
        
        String sign = request.getParameter("sign");
        
        
        // 获取sign缓存
        String value = RedisUtils.get(sign);
        SignValue signValue = JSON.parseObject(value, SignValue.class);
        String token=signValue.getToken();
        if(token==null){
            modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "用户未登录");
            LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.SETPASSWORD_ACTION);
            return modelAndView; 
        }
        //判断用户是否登录
        Integer userId = SecretUtil.getUserId(sign);
        if(userId==null||userId<=0){
            modelAndView = new ModelAndView(TransPasswordDefine.PASSWORD_ERROR_PATH);
            modelAndView.addObject("message", "用户未登录");
            LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.SETPASSWORD_ACTION);
            return modelAndView; 
        }
        Users user= appTransPasswordService.getUsers(userId);
        modelAndView = new ModelAndView(TransPasswordDefine.SET_PASSWORD_PATH);
        modelAndView.addObject("mobile",user.getMobile());
        modelAndView.addObject("sign", sign);
        LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.SETPASSWORD_ACTION, "[交易完成后,回调结束]");
        return modelAndView;
    }
	
	
	
    
	/**
	 * 设置交易密码
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(TransPasswordDefine.SETPASSWORD_ACTION)
	public ModelAndView setPassword(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.SETPASSWORD_ACTION);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
        BaseMapBean baseMapBean=new BaseMapBean();
        String sign = request.getParameter("sign");
		String platform = request.getParameter("platform");
        // 获取sign缓存
        String value = RedisUtils.get(sign);
        SignValue signValue = JSON.parseObject(value, SignValue.class);
        String token=signValue.getToken();
        if(token==null){
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未登录！");
            baseMapBean.setCallBackAction(CustomConstants.HOST+TransPasswordDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            
            LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.INIT);
            return modelAndView; 
        }
        //判断用户是否登录
        Integer userId = SecretUtil.getUserId(sign);
        if(userId==null||userId<=0){
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未登录！");
            baseMapBean.setCallBackAction(CustomConstants.HOST+TransPasswordDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.INIT);
            return modelAndView; 
        }

        
		//判断用户是否开户
        Users user= appTransPasswordService.getUsers(userId);
		if (user.getBankOpenAccount().intValue() != 1) {//未开户
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未开户！");
            baseMapBean.setCallBackAction(CustomConstants.HOST+TransPasswordDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
			return modelAndView;
		} 
		
		//判断用户是否设置过交易密码
		Integer passwordFlag = user.getIsSetPassword();
		if (passwordFlag == 1) {
			//已设置交易密码
			
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "已设置交易密码！");
            baseMapBean.setCallBackAction(CustomConstants.HOST+TransPasswordDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
			return modelAndView;
		} 
		
		UsersInfo usersInfo= appTransPasswordService.getUsersInfoByUserId(userId);


		logger.info("开户第一次设置密码==========================");
		// 用户id
		UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
		userOperationLogEntity.setOperationType(6);
		userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
		if(StringUtils.isEmpty(platform)){
			platform = request.getParameter("realPlatform");
		}
		if(StringUtils.isEmpty(platform)){
			platform = "3";
		}
		userOperationLogEntity.setPlatform(Integer.parseInt(platform));
		userOperationLogEntity.setRemark("");
		userOperationLogEntity.setOperationTime(new Date());
		userOperationLogEntity.setUserName(user.getUsername());
		userOperationLogEntity.setUserRole(String.valueOf(usersInfo.getRoleId()));
		appTransPasswordService.sendUserLogMQ(userOperationLogEntity);
		logger.info("开户第一次设置密码==========================结束");

		BankOpenAccount bankOpenAccount = appTransPasswordService.getBankOpenAccount(userId);
		// 同步调用路径
		String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() +  TransPasswordDefine.REQUEST_MAPPING
				+ TransPasswordDefine.RETURL_SYN_PASSWORD_ACTION + ".do";
		// 异步调用路
		String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() +  TransPasswordDefine.REQUEST_MAPPING
				+ TransPasswordDefine.RETURN_ASY_PASSWORD_ACTION + ".do";
		// 调用设置密码接口
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_PASSWORD_RESET_PAGE);// 消息类型(用户开户)
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
		bean.setTxDate(GetOrderIdUtils.getTxDate());
		bean.setTxTime(GetOrderIdUtils.getTxTime());
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
		bean.setChannel(BankCallConstant.CHANNEL_APP);
		if(user.getUserType() == 1){ //企业用户 传组织机构代码
			CorpOpenAccountRecord record = appTransPasswordService.getCorpOpenAccountRecord(Integer.valueOf(userId));
			bean.setIdType(record.getCardType() != null ? String.valueOf(record.getCardType()) : BankCallConstant.ID_TYPE_COMCODE);// 证件类型 20：其他证件（组织机构代码）25：社会信用号
			bean.setIdNo(record.getBusiCode());
			bean.setName(record.getBusiName());
		}else{
			bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);
			bean.setIdNo(usersInfo.getIdcard());
			bean.setName(usersInfo.getTruename());
		}
		bean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));//电子账号
		bean.setMobile(user.getMobile());
		bean.setRetUrl(retUrl);// 页面同步返回 URL
		bean.setNotifyUrl(bgRetUrl);// 页面异步返回URL(必须)
		// 商户私有域，存放开户平台,用户userId
		LogAcqResBean acqRes = new LogAcqResBean();
		acqRes.setUserId(userId);
		bean.setLogAcqResBean(acqRes);
		// 操作者ID
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_PASSWORDRESETPAGE);
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
		// 跳转到汇付天下画面
		try {
			modelAndView = BankCallUtils.callApi(bean);
			LogUtil.endLog(TransPasswordController.class.toString(), TransPasswordDefine.SETPASSWORD_ACTION);
		} catch (Exception e) {
			logger.error("调用银行接口失败",e);
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "调用银行接口失败！");
            baseMapBean.setCallBackAction(CustomConstants.HOST+TransPasswordDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
			LogUtil.errorLog(TransPasswordController.class.toString(), TransPasswordDefine.SETPASSWORD_ACTION, e);
		}
		return modelAndView;
	}


	/**
	 * 设置交易密码同步回调
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(TransPasswordDefine.RETURL_SYN_PASSWORD_ACTION)
	public ModelAndView passwordReturn(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute BankCallBean bean) {
		LogUtil.startLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.RETURL_SYN_PASSWORD_ACTION, "[开户同步回调开始]");
		ModelAndView modelAndView = new ModelAndView();
		 modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
         BaseMapBean baseMapBean=new BaseMapBean();
		bean.convert();
		LogAcqResBean acqes = bean.getLogAcqResBean();
		int userId = acqes.getUserId();
		Users user = this.appTransPasswordService.getUsers(userId);
		// 返回失败
        if (bean.getRetCode()!=null&&!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
            
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "交易密码设置失败,失败原因：" + appTransPasswordService.getBankRetMsg(bean.getRetCode()));
            baseMapBean.setCallBackAction(CustomConstants.HOST+TransPasswordDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }


		//判断用户是否设置了交易密码
		boolean flag = user.getIsSetPassword() == 1 ? true : false ;
		if(flag){

			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "交易密码设置成功");
            baseMapBean.setCallBackAction(CustomConstants.HOST+TransPasswordDefine.JUMP_HTML_SUCCESS_PATH);
            baseMapBean.setJumpFlag(BaseMapBean.JUMP_FLAG_NO);
            modelAndView.addObject("callBackForm", baseMapBean);
			return modelAndView;
		}
		BankOpenAccount bankOpenAccount = appTransPasswordService.getBankOpenAccount(userId);
		// 调用查询电子账户密码是否设置
        BankCallBean selectbean = new BankCallBean();
        selectbean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        selectbean.setTxCode(BankCallConstant.TXCODE_PASSWORD_SET_QUERY);
        selectbean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        selectbean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
        selectbean.setTxDate(GetOrderIdUtils.getTxDate());
        selectbean.setTxTime(GetOrderIdUtils.getTxTime());
        selectbean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
        selectbean.setChannel(BankCallConstant.CHANNEL_PC);
        selectbean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));// 电子账号

        // 操作者ID
        selectbean.setLogUserId(String.valueOf(userId));
        selectbean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
        selectbean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
        selectbean.setLogClient(0);
        // 返回参数
        BankCallBean retBean = null;
        // 调用接口
        retBean = BankCallUtils.callApiBg(selectbean);
        LogUtil.endLog(TransPasswordController.class.toString(), TransPasswordDefine.RETURL_SYN_PASSWORD_ACTION);
        

        if("1".equals(retBean.getPinFlag())){
            // 是否设置密码中间状态
            this.appTransPasswordService.updateUserIsSetPassword(user, 1);
            
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "交易密码设置成功");
            baseMapBean.setCallBackAction(CustomConstants.HOST+TransPasswordDefine.JUMP_HTML_SUCCESS_PATH);
            baseMapBean.setJumpFlag(BaseMapBean.JUMP_FLAG_NO);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
		
		baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, "交易密码设置失败");
        baseMapBean.setCallBackAction(CustomConstants.HOST+TransPasswordDefine.JUMP_HTML_FAILED_PATH);
        modelAndView.addObject("callBackForm", baseMapBean);
		LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.RETURL_SYN_PASSWORD_ACTION, "[交易完成后,回调结束]");
		return modelAndView;
	}

	/**
	 * 设置交易密码异步回调
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(TransPasswordDefine.RETURN_ASY_PASSWORD_ACTION)
	public String passwordBgreturn(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute BankCallBean bean) {
	    BankCallResult result = new BankCallResult();
		LogUtil.startLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.RETURN_ASY_PASSWORD_ACTION, "[开户异步回调开始]");
		bean.convert();
		LogAcqResBean acqes = bean.getLogAcqResBean();
		int userId = acqes.getUserId();
		// 查询用户开户状态
		Users user = this.appTransPasswordService.getUsers(userId);

		// 成功或审核中
		if (user != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE))) {
			try {
				// 开户后保存相应的数据以及日志
				this.appTransPasswordService.updateUserIsSetPassword(user, 1);
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.errorLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.RETURN_ASY_PASSWORD_ACTION, e);
			}
		} 
		LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.RETURN_ASY_PASSWORD_ACTION, "[交易完成后,回调结束]");
		result.setMessage("交易密码设置成功");
        result.setStatus(true);
        return JSONObject.toJSONString(result, true);
	}
	
	
	
	/**
	 * 重置交易密码
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(TransPasswordDefine.RESETPASSWORD_ACTION)
	public ModelAndView resetPassword(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.RESETPASSWORD_ACTION);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
        BaseMapBean baseMapBean=new BaseMapBean();

		String sign = request.getParameter("sign");
		String platform = request.getParameter("platform");
        // 获取sign缓存
        String value = RedisUtils.get(sign);
        SignValue signValue = JSON.parseObject(value, SignValue.class);
        String token=signValue.getToken();
        if(token==null){
            
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未登录");
            baseMapBean.setCallBackAction(CustomConstants.HOST+TransPasswordDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);

            LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.INIT);
            return modelAndView; 
        }
        //判断用户是否登录
        Integer userId = SecretUtil.getUserId(sign);
//        Integer userId = 22401107;
        if(userId==null||userId<=0){
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未登录");
            baseMapBean.setCallBackAction(CustomConstants.HOST+TransPasswordDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);

            LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.INIT);
            return modelAndView; 
        }
        
        //判断用户是否开户
        Users user= appTransPasswordService.getUsers(userId);
		UsersInfo usersInfo = appUserService.getUsersInfoByUserId(userId);
		UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
		userOperationLogEntity.setOperationType(6);
		userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
		if(StringUtils.isEmpty(platform)){
			platform = request.getParameter("realPlatform");
		}
		if(StringUtils.isEmpty(platform)){
			platform = "3";
		}
		userOperationLogEntity.setPlatform(Integer.parseInt(platform));
		userOperationLogEntity.setRemark("");
		userOperationLogEntity.setOperationTime(new Date());
		userOperationLogEntity.setUserName(user.getUsername());
		userOperationLogEntity.setUserRole(String.valueOf(usersInfo.getRoleId()));
		appUserService.sendUserLogMQ(userOperationLogEntity);
        if (user.getBankOpenAccount().intValue() != 1) {//未开户
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未开户");
            baseMapBean.setCallBackAction(CustomConstants.HOST+TransPasswordDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);

            return modelAndView;
        } 

        //判断用户是否设置过交易密码
        
        Integer passwordFlag = user.getIsSetPassword();
        if (passwordFlag == 0) {//已设置交易密码
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "未设置过交易密码，请先设置交易密码");
            baseMapBean.setCallBackAction(CustomConstants.HOST+TransPasswordDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);

            return modelAndView;
        }
		BankOpenAccount bankOpenAccount = appTransPasswordService.getBankOpenAccount(userId);
		// 同步调用路径
		String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() +  TransPasswordDefine.REQUEST_MAPPING
				+ TransPasswordDefine.RETURL_SYN_RESETPASSWORD_ACTION + ".do";
		// 异步调用路
		String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() +  TransPasswordDefine.REQUEST_MAPPING
				+ TransPasswordDefine.RETURN_ASY_RESETPASSWORD_ACTION + ".do";
		
		// 调用设置密码接口
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_PASSWORD_RESET_PAGE);// 消息类型(用户开户)
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
		bean.setTxDate(GetOrderIdUtils.getTxDate());
		bean.setTxTime(GetOrderIdUtils.getTxTime());
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
		bean.setChannel(BankCallConstant.CHANNEL_APP);
		if(user.getUserType() == 1){ //企业用户 传组织机构代码
			CorpOpenAccountRecord record = appTransPasswordService.getCorpOpenAccountRecord(Integer.valueOf(userId));
			bean.setIdType(record.getCardType() != null ? String.valueOf(record.getCardType()) : BankCallConstant.ID_TYPE_COMCODE);// 证件类型 20：其他证件（组织机构代码）25：社会信用号
			bean.setIdNo(record.getBusiCode());
			bean.setName(record.getBusiName());
		}else{
			bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);
			bean.setIdNo(usersInfo.getIdcard());
			bean.setName(usersInfo.getTruename());
		}
		bean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));//电子账号
		bean.setMobile(user.getMobile());

		bean.setNotifyUrl(bgRetUrl);// 页面异步返回URL(必须)
		// 商户私有域，存放开户平台,用户userId
		LogAcqResBean acqRes = new LogAcqResBean();
		acqRes.setUserId(userId);
		bean.setLogAcqResBean(acqRes);
		// 操作者ID
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_PASSWORDRESETPAGE);
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
		bean.setRetUrl(retUrl+"?isSuccess=1&ordid=" + bean.getLogOrderId());// 页面同步返回 URL
		bean.setSuccessfulUrl(retUrl+"?isSuccess=1" + bean.getLogOrderId());
		// 跳转到汇付天下画面
		try {
			modelAndView = BankCallUtils.callApi(bean);
			LogUtil.endLog(TransPasswordController.class.toString(), TransPasswordDefine.RESETPASSWORD_ACTION);
		} catch (Exception e) {
			logger.error("调用银行接口失败!",e);
			
			baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "调用银行接口失败！");
            baseMapBean.setCallBackAction(CustomConstants.HOST+TransPasswordDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
			LogUtil.errorLog(TransPasswordController.class.toString(), TransPasswordDefine.RESETPASSWORD_ACTION, e);
		}
		return modelAndView;
	}

	/**
	 * 重置交易密码同步回调
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(TransPasswordDefine.RETURL_SYN_RESETPASSWORD_ACTION)
	public ModelAndView resetPasswordReturn(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute BankCallBean bean) {

		LogUtil.startLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.RETURL_SYN_RESETPASSWORD_ACTION, "[重置交易密码同步回调开始]");
		String isSuccess = request.getParameter("isSuccess");
		String ordid = request.getParameter("ordid");



		ModelAndView modelAndView = new ModelAndView();
		modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
        BaseMapBean baseMapBean=new BaseMapBean();

		//add by cwyang 防止同步比异步快
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//add by cwyang 查询银行设置交易密码是否成功
        boolean backIsSuccess = transPasswordService.backLogIsSuccess(ordid);
        logger.info("========================判断修改交易密码异步返回结果，订单号：" + ordid + "，返回结果：" + backIsSuccess);

		// 返回失败
        if (StringUtils.isBlank(isSuccess) || !backIsSuccess) {
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "交易密码修改失败,失败原因：" + appTransPasswordService.getBankRetMsg(bean.getRetCode()));
            baseMapBean.setCallBackAction(CustomConstants.HOST+TransPasswordDefine.JUMP_HTML_FAILED_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }
		baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, "交易密码修改成功");
        baseMapBean.setCallBackAction(CustomConstants.HOST+TransPasswordDefine.JUMP_HTML_SUCCESS_PATH);
        baseMapBean.setJumpFlag(BaseMapBean.JUMP_FLAG_NO);
        modelAndView.addObject("callBackForm", baseMapBean);
		LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.RETURL_SYN_RESETPASSWORD_ACTION, "[重置交易密码同步回调结束]");
		return modelAndView;
	}

	/**
	 * 重置交易密码异步回调
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(TransPasswordDefine.RETURN_ASY_RESETPASSWORD_ACTION)
	public String resetPasswordBgreturn(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute BankCallBean bean) {
	    BankCallResult result = new BankCallResult();
		LogUtil.startLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.RETURN_ASY_RESETPASSWORD_ACTION, "[重置交易密码回调开始]");

		LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.RETURN_ASY_RESETPASSWORD_ACTION, "[重置交易密码回调结束]");
		result.setMessage("交易密码修改成功");
        result.setStatus(true);
        return JSONObject.toJSONString(result, true);
	}
	
	

    /**
     * 发送短信验证码（ajax请求） 短信验证码数据保存
     */
    @ResponseBody
    @RequestMapping(value = TransPasswordDefine.TRANS_PASSWORD_SEND_CODE_ACTION, produces = "application/json; charset=utf-8")
    public JSONObject transPasswordSendCode(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.TRANS_PASSWORD_SEND_CODE_ACTION);
        JSONObject ret = new JSONObject();
        // 手机号码(必须,数字,最大长度)
        String mobile = request.getParameter("mobile");
        if (StringUtils.isEmpty(mobile)) {
            ret.put("status",0); 
            ret.put("message", "请填写手机号!");
            return ret;
        } 
        //短信配置
        SmsConfig smsConfig = appTransPasswordService.getSmsConfig();

        String ip = GetCilentIP.getIpAddr(request);
        String ipCount = RedisUtils.get(ip + ":MaxIpCount");
        if (StringUtils.isBlank(ipCount) || !Validator.isNumber(ipCount)) {
            ipCount = "0";
            RedisUtils.set(ip + ":MaxIpCount", "0");
        }
        if (Integer.valueOf(ipCount) >= smsConfig.getMaxIpCount()) {
            if (Integer.valueOf(ipCount) == smsConfig.getMaxIpCount()) {
                try {
                    appTransPasswordService.sendSms(mobile, "IP访问次数超限:" + ip);
                } catch (Exception e) {
                    LogUtil.errorLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.TRANS_PASSWORD_SEND_CODE_ACTION, e);
                }
                RedisUtils.set(ip + ":MaxIpCount", (Integer.valueOf(ipCount) + 1) + "", 24 * 60 * 60);
            }
            ret.put("status", 0); 
            ret.put("message", "该设备短信请求次数超限，请明日再试");
            LogUtil.errorLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.TRANS_PASSWORD_SEND_CODE_ACTION, "短信验证码发送失败", null);
            return ret;
        }

        // 判断最大发送数max_phone_count
        String count = RedisUtils.get(mobile + ":MaxPhoneCount");
        if (StringUtils.isBlank(count) || !Validator.isNumber(count)) {
            count = "0";
            RedisUtils.set(mobile + ":MaxPhoneCount", "0");
        }

        if (Integer.valueOf(count) >= smsConfig.getMaxPhoneCount()) {
            if (Integer.valueOf(count) == smsConfig.getMaxPhoneCount()) {
                try {
                    appTransPasswordService.sendSms(mobile, "手机验证码发送次数超限");
                } catch (Exception e) {
                    LogUtil.errorLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.TRANS_PASSWORD_SEND_CODE_ACTION, e);
                }

                RedisUtils.set(mobile + ":MaxPhoneCount", (Integer.valueOf(count) + 1) + "", 24 * 60 * 60);
            }
            ret.put("status", 0); 
            ret.put("message", "该手机号短信请求次数超限，请明日再试");
            LogUtil.errorLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.TRANS_PASSWORD_SEND_CODE_ACTION, "短信验证码发送失败", null);
            return ret;
        }

        // 生成验证码
        String checkCode = GetCode.getRandomSMSCode(6);
        Map<String, String> param = new HashMap<String, String>();
        param.put("val_code", checkCode);
        // 发送短信验证码
        SmsMessage smsMessage = new SmsMessage(null, param, mobile, null, MessageDefine.SMSSENDFORMOBILE, null,
                CustomConstants.PARAM_TPL_ZHUCE, CustomConstants.CHANNEL_TYPE_NORMAL);
        Integer result = (smsProcesser.gather(smsMessage) == 1) ? 0 : 1;

        // 短信发送成功后处理
        if (result != null && result == 0) {
            // 累计IP次数
            String currentMaxIpCount = RedisUtils.get(ip + ":MaxIpCount");
            if (StringUtils.isBlank(currentMaxIpCount)) {
                currentMaxIpCount = "0";
            }
            // 累加手机次数
            String currentMaxPhoneCount = RedisUtils.get(mobile + ":MaxPhoneCount");
            if (StringUtils.isBlank(currentMaxPhoneCount)) {
                currentMaxPhoneCount = "0";
            }
            RedisUtils.set(ip + ":MaxIpCount", (Integer.valueOf(currentMaxIpCount) + 1) + "", 24 * 60 * 60);
            RedisUtils.set(mobile + ":MaxPhoneCount", (Integer.valueOf(currentMaxPhoneCount) + 1) + "", 24 * 60 * 60);
        }
        System.out.println(checkCode);
        // 保存短信验证码
        this.appTransPasswordService.saveSmsCode(mobile, checkCode);
        //最大间隔时间
        Integer maxValidTime = smsConfig.getMaxIntervalTime() == null ? 60 : smsConfig.getMaxIntervalTime();
        ret.put("maxValidTime", maxValidTime);
        ret.put("status", 1); 
        ret.put("message", "短信发送成功");
        LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.TRANS_PASSWORD_SEND_CODE_ACTION);
        return ret;

    }

    /**
     * 短信验证码校验
     * 
     * 用户注册数据提交（获取session数据并保存） 1.校验验证码
     * 2.若验证码正确，则获取session数据，并将相应的注册数据写入数据库（三张表），跳转相应的注册成功界面
     */
    @ResponseBody
    @RequestMapping(value = TransPasswordDefine.TRANS_PASSWORD_CHECK_CODE_ACTION, produces = "application/json; charset=utf-8")
    public JSONObject transPasswordCheckCode(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.TRANS_PASSWORD_CHECK_CODE_ACTION);
        JSONObject info = new JSONObject();
        JSONObject ret = new JSONObject();
        // 手机号码(必须,数字,最大长度)
        String mobile = request.getParameter("mobile");
        if (!ValidatorCheckUtil.validateRequired(info, null, null, mobile)) {
            ret.put("status", 0); 
            ret.put("message", "手机号不能为空");
            return ret;
        } else if (!ValidatorCheckUtil.validateMobile(info, null, null, mobile, 11, false)) {
            ret.put("status", 0); 
            ret.put("message", "手机号格式有误");
            return ret;
        }
        // 短信验证码
        String code = request.getParameter("code");
        if (!ValidatorCheckUtil.validateRequired(info, null, null, code)) {
            ret.put("status", 0); 
            ret.put("message", "验证码不能为空");
            return ret;
        }
        int cnt = this.appTransPasswordService.checkMobileCode(mobile, code);
        if (cnt > 0) {
            LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.TRANS_PASSWORD_CHECK_CODE_ACTION);
            ret.put("status", 1); 
            ret.put("message", "校验成功");
            ret.put("url", PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() + TransPasswordDefine.REQUEST_MAPPING
                    + TransPasswordDefine.SETPASSWORD_ACTION + ".do?sign="+request.getParameter("sign")); 
            return ret;
        } else {
            LogUtil.endLog(TransPasswordDefine.THIS_CLASS, TransPasswordDefine.TRANS_PASSWORD_CHECK_CODE_ACTION);
            ret.put("status", 0); 
            ret.put("message", "验证码错误请重新输入");
            return ret;
        }
    }
	
	
	
}
