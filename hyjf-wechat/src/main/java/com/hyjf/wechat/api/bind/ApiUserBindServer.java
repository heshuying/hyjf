package com.hyjf.wechat.api.bind;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.RSAJSPUtil;
import com.hyjf.common.util.CookieUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.WrbCommonDateUtil;
import com.hyjf.common.util.WrbParseParamUtil;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.wechat.BaseResultBeanFrontEnd;
import com.hyjf.wechat.api.base.ApiBaseController;
import com.hyjf.wechat.api.common.ApiCommonService;
import com.hyjf.wechat.api.user.ApiUserLoginDefine;
import com.hyjf.wechat.base.BaseMapBean;
import com.hyjf.wechat.controller.login.LoginController;
import com.hyjf.wechat.controller.login.LoginDefine;
import com.hyjf.wechat.controller.login.LoginResultBean;
import com.hyjf.wechat.service.login.LoginService;
import com.hyjf.wechat.util.ResultEnum;
import com.hyjf.wechat.util.SecretUtil;

/**
 * @author limeng
 */
@Controller
@RequestMapping(value = ApiUserBindDefine.REQUEST_MAPPING)
public class ApiUserBindServer extends ApiBaseController {

	private Logger logger = LoggerFactory.getLogger(ApiUserBindServer.class);
	
	/** 汇盈金服调用风车理财接口通知注册绑定成功 **/
	public static final String HYJF_REQ_PUB_KEY_PATH = PropUtils.getSystem("wrb.callback.bind.url");
	
	@Autowired
	private ApiCommonService apiCommonService;
	@Autowired
	private ApiUserBindService apiUserBindService;
	@Autowired
	private LoginService loginService;
	
	@InitBinder("apiUserPostBean")
    public void initBinderApiUserPostBean(WebDataBinder binder) {  
		binder.setFieldDefaultPrefix("apiUserPostBean_");
    }
	@InitBinder("loginBean")
    public void initBinderLoginBean(WebDataBinder binder) {  
		binder.setFieldDefaultPrefix("loginBean_");  
    }
	
	/**
	 * 跳转登陆授权页面
	 * @param form
	 * @return
	 */
    @RequestMapping(value = ApiUserBindDefine.BIND_API_MAPPING)
    public ModelAndView bindApi(HttpServletRequest request, HttpServletResponse response, @RequestParam String param,
			 @RequestParam(value = "sign", required = false) String sign){
    	 LogUtil.startLog(ApiUserBindDefine.REQUEST_MAPPING, ApiUserBindDefine.BIND_API_MAPPING);
    	 logger.info("风车理财登陆授权, param is :{}, sign is :{}", param, sign);
         Map<String, String> paramMap = WrbParseParamUtil.parseParam(param);
         ModelAndView modelAndView = new ModelAndView(ApiUserLoginDefine.JUMP_HTML);
		 // 回调url（h5错误页面）
         BaseMapBean baseMapBean=new BaseMapBean();
         
         
         ApiUserPostBean apiUserPostBean = null;
	 		try {
	 			apiUserPostBean = WrbParseParamUtil.mapToBean(paramMap, ApiUserPostBean.class);
	 			
	 		// 验证
	 	      apiCommonService.checkPostBeanOfWeb(apiUserPostBean);
	 	     if(!(StringUtils.isNotBlank(apiUserPostBean.getFrom()) && "wrb".equals(apiUserPostBean.getFrom()))){
	 	    	baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.FAIL);
	 	        baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户授权平台异常！");
	 	        baseMapBean.setCallBackAction(CustomConstants.WECHAT_HOST+ApiUserLoginDefine.JUMP_FAILE_HTML);
	 	        modelAndView.addObject("callBackForm", baseMapBean);
	 	        return modelAndView;
	         }
	 	     if (StringUtils.isNotBlank(apiUserPostBean.getWrb_user_id())) {
	 	    	modelAndView.addObject("instcode", WrbCommonDateUtil.FCLC_INSTCODE);
		 	    baseMapBean.set("instcode", WrbCommonDateUtil.FCLC_INSTCODE+"");
		         // 已授权验证
		         Integer userid = apiUserBindService.getUserIdByBind(apiUserPostBean.getWrb_user_id(), WrbCommonDateUtil.FCLC_INSTCODE);
		         logger.info("apiUserPostBean is :{}", apiUserPostBean);
		         if(userid == null){
		         	// 跳转登陆授权画面
		        	 baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
			 	     baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户授权！");
			 	     baseMapBean.setCallBackAction(CustomConstants.WECHAT_HOST+ApiUserLoginDefine.JUMP_BIND_HTML);
			 	     baseMapBean.setAll(paramMap);
		         }else{
		        	 Users users = apiCommonService.getUsers(userid);
			            
			            BankOpenAccount account = apiCommonService.getBankOpenAccount(userid);
			            String accountId = null;
			            if(account!=null&&StringUtils.isNoneBlank(account.getAccount())){
			                accountId = account.getAccount();
			                /*********** 登录时自动同步线下充值记录 start ***********/
			                if (users.getBankOpenAccount() == 1) {
			                    CommonSoaUtils.synBalance(users.getUserId());
			                }
			                /*********** 登录时自动同步线下充值记录 end ***********/
			            }
			            
		        	 String loginsign = SecretUtil.createToken(userid, users.getUsername(), accountId);
		        	//登录成功之后风车理财的特殊标记，供后续出借使用
					CookieUtils.addCookie(request, response, CustomConstants.TENDER_FROM_TAG,
								CustomConstants.WRB_CHANNEL_CODE);
					RedisUtils.del("loginFrom"+userid);
					RedisUtils.set("loginFrom"+userid, "2", 1800);
					baseMapBean.setCallBackAction(PropUtils.getSystem("hyjf.wechat.web.host"));
		        	baseMapBean.set("sign", loginsign); 
		         }
		         
		         String idCard = apiUserPostBean.getId_no();
		         String phone = apiUserPostBean.getMobile();
		         String mobile = apiUserPostBean.getMobile()==null?"":phone;
		         String readonly = "";
		         if (!StringUtils.isEmpty(idCard)) {
		 			String hyjfMobole = apiUserBindService.getByIdCard(idCard);
		 			if(hyjfMobole != null){
		 				mobile = hyjfMobole;
		 				readonly = "readonly";
		 			}
		 		}else {
		 			if (!StringUtils.isEmpty(phone)) {
		 				readonly = "readonly";
		 			}
		 		}
		         
		         baseMapBean.set("mobile", mobile);
		         baseMapBean.set("readonly", readonly);
			}else{
				
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.FAIL);
				baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户授权参数参数异常！");
				baseMapBean.setCallBackAction(CustomConstants.WECHAT_HOST+ApiUserLoginDefine.JUMP_FAILE_HTML);
			}
	 	      
	 		} catch (Exception e) {
	 			logger.error("参数解析失败, paramMap is :"+ paramMap, e);
	 			 baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.FAIL);
	 	         baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户授权参数解析失败！");
	 	         baseMapBean.setCallBackAction(CustomConstants.WECHAT_HOST+ApiUserLoginDefine.JUMP_FAILE_HTML);
	 	        
	 		} 
	 		 modelAndView.addObject("callBackForm", baseMapBean);
 	         return modelAndView;
    }
    
    /**
     * 授权操作
     * @param request
     * @param response
     * @param apiUserPostBean
     * @param loginBean
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = ApiUserBindDefine.BIND_MAPPING, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public JSONObject bind(HttpServletRequest request, HttpServletResponse response, 
    			@ModelAttribute("apiUserPostBean") ApiUserPostBean apiUserPostBean, @ModelAttribute("loginBean") LoginBean loginBean) throws Exception{
    	// 返回对象
    	JSONObject jsonObj = new JSONObject();
    	
		// 第三方用户ID
		String bindUniqueId = apiUserPostBean.getWrb_user_id();
		logger.info("bindUniqueId is :{}", bindUniqueId);
		//用户Id
		Integer userId = null;
		//用户名
		String userName = null;
		
		
        // 用户接受协议验证
		if(!loginBean.getReadAgreement()){
			jsonObj = new JSONObject();
		    jsonObj.put("status", BaseResultBeanFrontEnd.FAIL);
		    jsonObj.put("statusCode", BaseResultBeanFrontEnd.FAIL);
		    jsonObj.put("statusDesc", "授权失败，请仔细阅读并同意《汇盈金服授权协议》");
            return jsonObj;
		}
		// 用户手机号码验证
		if(!StringUtils.isNotBlank(loginBean.getLoginUserName())){
			jsonObj = new JSONObject();
			jsonObj.put("status", BaseResultBeanFrontEnd.FAIL);
			jsonObj.put("statusCode", BaseResultBeanFrontEnd.FAIL);
			jsonObj.put("statusDesc", "授权失败，请输入正确的手机号码");
			return jsonObj;
		}
		
    	
		//根据登陆账户名取得用户ID和用户名
		Users users = apiUserBindService.getUserInfoByLogin(loginBean);
		logger.info("users is :{}", users);
		// 未获取的验证在下面登陆时 验证
		if (users != null) {
			//用户Id
			userId = users.getUserId();
			logger.info("userId is :{}", userId);
			//用户名
			userName = users.getUsername();
			
	        // 第三方用户已授权验证
	        Integer userid = apiUserBindService.getUserIdByBind(bindUniqueId, WrbCommonDateUtil.FCLC_INSTCODE);
	        logger.info("userid is:{}", userid);
	        if(userid != null){
                    JSONObject jsonResult = new JSONObject();
                    jsonResult.put("status", BaseResultBeanFrontEnd.FAIL);
                    jsonResult.put("statusCode", BaseResultBeanFrontEnd.FAIL);
                    jsonResult.put("statusDesc", "该用户已绑定汇盈金服，不能重复绑定");

                    jsonResult.put("hyjfUserName",userName );
                    jsonResult.put("userId",users.getUserId() );
                    return jsonResult;
	        }
	        
	        // 汇盈金服账号已绑定其他用户验证
	        String fcUserId = apiUserBindService.getBindUniqueIdByUserId(userId, WrbCommonDateUtil.FCLC_INSTCODE);
	        if(fcUserId != null){
	        	jsonObj = new JSONObject();
	        	jsonObj.put("status", BaseResultBeanFrontEnd.FAIL);
	        	jsonObj.put("statusCode", BaseResultBeanFrontEnd.FAIL);
	        	jsonObj.put("statusDesc", "该汇盈账号以绑定其他用户，不能重复绑定");

	        	jsonObj.put("hyjfUserName",userName );
	        	jsonObj.put("userId",users.getUserId() );
	            return jsonObj;
	        }
		}else{
			jsonObj = new JSONObject();
        	jsonObj.put("status", BaseResultBeanFrontEnd.FAIL);
        	jsonObj.put("statusCode", BaseResultBeanFrontEnd.FAIL);
        	jsonObj.put("statusDesc", "请先注册汇盈金服账号！");
            return jsonObj;
		}
		
         
		// 登陆
		LoginResultBean baseResultBean = this.doLogin(request, response, loginBean.getLoginUserName(),loginBean.getLoginPassword());
        if (!baseResultBean.getStatus().equals("000")) {
            // 登陆失败，返回失败信息
        	jsonObj.put("status", baseResultBean.getStatus());
        	jsonObj.put("statusCode", BaseResultBeanFrontEnd.FAIL);
        	jsonObj.put("statusDesc", baseResultBean.getStatusDesc());
            return jsonObj;
        }
        String sign = baseResultBean.getSign();
        //登录成功之后风车理财的特殊标记，供后续出借使用
		CookieUtils.addCookie(request, response, CustomConstants.TENDER_FROM_TAG,
				CustomConstants.WRB_CHANNEL_CODE);
        // 授权
        Boolean result = apiUserBindService.bindThirdUser(userId, bindUniqueId, WrbCommonDateUtil.FCLC_INSTCODE);
        if(!result){
        	jsonObj = new JSONObject();
            jsonObj.put("status", BaseResultBeanFrontEnd.FAIL);
            jsonObj.put("statusCode", BaseResultBeanFrontEnd.FAIL);
            jsonObj.put("statusDesc", "授权失败，请联系汇盈金服客服。");
            return jsonObj;
        }
        
        // 返回第三方页面
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("status", BaseResultBeanFrontEnd.SUCCESS);
        jsonResult.put("statusCode", BaseResultBeanFrontEnd.SUCCESS);
        jsonResult.put("statusDesc", "授权成功"); 
        if (StringUtils.isNoneBlank(apiUserPostBean.getTarget_url())) {
        	jsonResult.put(ApiUserBindDefine.RETURL, apiUserPostBean.getTarget_url());
		}else {
			jsonResult.put(ApiUserBindDefine.RETURL, CustomConstants.WECHAT_HOST+"?sign=" + sign);
		}
        jsonResult.put("hyjfUserName",userName ); 
        jsonResult.put("userId",users.getUserId() ); 
        jsonResult.put("sign",sign ); 
		
        //回调风车理财绑定用户
        Map<String, String> params = new HashMap<>();
        params.put("from", "hyjf");
        params.put("wrb_user_id", apiUserPostBean.getWrb_user_id());
        params.put("pf_user_id", users.getUserId()+"");
        params.put("pf_user_name", users.getUsername());
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(users.getRegTime());
        Date date = new Date(lt);
        
        params.put("reg_time", simpleDateFormat.format(date));
  		WrbParseParamUtil.wrbCallback(HYJF_REQ_PUB_KEY_PATH, params);
  		
		return jsonResult;
    }
    
    /**
     * 登陆
     * @param request
     * @param response
     * @param userName 用户名
     * @param password 密码
     * @return LoginResultBean
     */
    @ResponseBody
    @RequestMapping(value = LoginDefine.DOLOGIN_MAPPING, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public LoginResultBean doLogin(HttpServletRequest request, HttpServletResponse response, @RequestParam String userName , @RequestParam String password) {
        LogUtil.startLog(LoginController.class.getName(), LoginDefine.DOLOGIN_MAPPING);
        logger.info("请求登录接口,手机号为：【"+userName+"】");
        LoginResultBean result = new LoginResultBean();
        if(StringUtils.isBlank(userName)||StringUtils.isBlank(password)){
        	result.setEnum(ResultEnum.PARAM);
            return result;
        }
        //密码解密
        password = RSAJSPUtil.rsaToPassword(password);
        
        int userId = loginService.updateLoginInAction(userName, password, CustomUtil.getIpAddr(request));

        switch (userId) {
        case -1:
            result.setEnum(ResultEnum.ERROR_001);
            break;
        case -2:
            result.setEnum(ResultEnum.ERROR_002);
            break;
        case -3:
            result.setEnum(ResultEnum.ERROR_003);
            break;
        default:
            Users users = loginService.getUsers(userId);
            
            BankOpenAccount account = loginService.getBankOpenAccount(userId);
            String accountId = null;
            if(account!=null&&StringUtils.isNoneBlank(account.getAccount())){
                accountId = account.getAccount();
                /*********** 登录时自动同步线下充值记录 start ***********/
                if (users.getBankOpenAccount() == 1) {
                    CommonSoaUtils.synBalance(users.getUserId());
                }
                /*********** 登录时自动同步线下充值记录 end ***********/
            }
            String sign = SecretUtil.createToken(userId, users.getUsername(), accountId);
            // 登录完成返回值
            result.setEnum(ResultEnum.SUCCESS);
            result.setSign(sign);
            RedisUtils.del("loginFrom"+userId);
            RedisUtils.set("loginFrom"+userId, "2", 1800);
            break;
        }
        
        LogUtil.endLog(LoginController.class.getName(), LoginDefine.DOLOGIN_MAPPING);
        return result;
    }
}
