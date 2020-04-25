package com.hyjf.web.api.aems.bind;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.ApiSignUtil;
import com.hyjf.common.validator.CheckUtil;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.web.api.base.ApiBaseController;
import com.hyjf.web.api.common.ApiCommonService;
import com.hyjf.web.api.user.ApiUserPostBean;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.login.LoginBean;
import com.hyjf.web.user.login.LoginController;
import com.hyjf.web.user.login.LoginService;
import com.hyjf.web.util.WebUtils;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * Aems用戶绑定
 * @author Zha Daojian
 * @date 2018/9/17 17:08
 * @param
 * @return
 **/
@Controller
@RequestMapping(value = AemsApiUserBindDefine.REQUEST_MAPPING)
public class AemsApiUserBindServer extends ApiBaseController {
	private Logger logger = LoggerFactory.getLogger(AemsApiUserBindServer.class);
	@Autowired
	private ApiCommonService apiCommonService;
	@Autowired
	private AemsApiUserBindService apiUserBindService;
	@Autowired
	private LoginController loginController;
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
	 * @param apiUserPostBean
	 * @return
	 */
    @RequestMapping(value = AemsApiUserBindDefine.BIND_API_MAPPING)
    public ModelAndView bindApi(HttpServletRequest request, HttpServletResponse response, ApiUserPostBean apiUserPostBean){
    	// 设置接口结果页的信息（返回Url）
    	this.initCheckUtil(apiUserPostBean);
    	
    	ModelAndView modelAndView = new ModelAndView(AemsApiUserBindDefine.FAST_AUTH_LOGIN_PTAH);
		// 验证
    	apiCommonService.checkPostBeanOfWeb(apiUserPostBean);
		logger.info("验签开始....");
		// 验签
    	apiCommonService.checkSign(apiUserPostBean);
		logger.info("解密开始....apiUserPostBean is : {}", JSONObject.toJSONString(apiUserPostBean));
    	// 解密
        Long bindUniqueId = apiCommonService.decrypt(apiUserPostBean);
		logger.info("解密结果....bindUniqueId is : {}", bindUniqueId);
		modelAndView.addObject("instcode", apiUserPostBean.getPid());
        // 已授权验证
        Integer userid = apiUserBindService.getUserIdByBind(bindUniqueId, apiUserPostBean.getPid());
        logger.info("apiUserPostBean is :{}", JSONObject.toJSONString(apiUserPostBean));
        if(userid == null){
        	// 跳转登陆授权画面
            modelAndView.addObject("apiForm",new BeanMap(apiUserPostBean));
        }else{
        	// 登陆
            WebViewUser webUser = loginService.getWebViewUserByUserId(userid);
            WebUtils.sessionLogin(request, response, webUser);

            //无记录时，未绑定汇盈金服
            CheckUtil.check(false, "user.repeatbound");
        }
        String idCard = apiUserPostBean.getIdCard();
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
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("readonly", readonly);
		return modelAndView;
    }
    
	/**
	 * 授权按钮
	 * @param loginBean
	 * @return
	 * @throws Exception 
	 */
    @ResponseBody
    @RequestMapping(value = AemsApiUserBindDefine.BIND_MAPPING, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public JSONObject bind(HttpServletRequest request, HttpServletResponse response, 
    			@ModelAttribute("apiUserPostBean") ApiUserPostBean apiUserPostBean, @ModelAttribute("loginBean") LoginBean loginBean) throws Exception{
    	// 设置接口结果页的信息（返回Url）
    	this.initCheckUtil(apiUserPostBean);
    	// 返回对象
    	JSONObject jsonObj = new JSONObject();
    	
    	// 验证
    	apiCommonService.checkPostBeanOfWeb(apiUserPostBean);
		// 验签
    	apiCommonService.checkSign(apiUserPostBean);
		// 解密
		Long bindUniqueId = apiCommonService.decrypt(apiUserPostBean);
		logger.info("bindUniqueId is :{}", bindUniqueId);
		//用户Id
		Integer userId = null;
		//用户名
		String userName = null;
		
		
        // 用户接受协议验证
		if(!loginBean.getReadAgreement()){
			jsonObj = new JSONObject();
		    jsonObj.put(AemsApiUserBindDefine.STATUS, AemsApiUserBindDefine.STATUS_FALSE);
		    jsonObj.put(AemsApiUserBindDefine.ERROR, "授权失败，请仔细阅读并同意《汇盈金服授权协议》");
            return jsonObj;
		}
		// 用户手机号码验证
		if(!StringUtils.isNotBlank(loginBean.getLoginUserName())){
			jsonObj = new JSONObject();
			jsonObj.put(AemsApiUserBindDefine.STATUS, AemsApiUserBindDefine.STATUS_FALSE);
			jsonObj.put(AemsApiUserBindDefine.ERROR, "授权失败，请输入正确的手机号码");
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
	        Integer userid = apiUserBindService.getUserIdByBind(bindUniqueId, apiUserPostBean.getPid());
	        logger.info("userid is:{}", userid);
	        if(userid != null){
                // 纳觅财富
                if (AemsApiUserBindDefine.NMCF_PID.equals(apiUserPostBean.getPid())) {
                    JSONObject jsonResult = new JSONObject();
                    jsonResult.put(AemsApiUserBindDefine.STATUS, AemsApiUserBindDefine.STATUS_TRUE);
                    jsonResult.put("statusCode", AemsApiUserBindDefine.ERROR_SUCCESS);
                    jsonResult.put("statusDesc", "该用户已绑定汇盈金服，不能重复绑定");

                    jsonResult.put("bindUniqueIdScy", apiUserPostBean.getBindUniqueIdScy());
                    jsonResult.put("hyjfUserName",userName );
                    jsonResult.put("userId",users.getUserId() );
                    Long timestamp = System.currentTimeMillis();
                    jsonResult.put("timestamp",timestamp);
                    jsonResult.put("chkValue",ApiSignUtil.encryptByRSA(apiUserPostBean.getPid()+timestamp+""));
					String str = jsonResult.toJSONString();
					jsonResult.put(AemsApiUserBindDefine.RETURL, apiUserPostBean.getRetUrl() + "?datajson=" + URLEncoder.encode(str, "UTF-8"));
                    return jsonResult;
                }
	        	jsonObj = new JSONObject();
	            jsonObj.put(AemsApiUserBindDefine.STATUS, AemsApiUserBindDefine.STATUS_FALSE);
	            jsonObj.put(AemsApiUserBindDefine.ERROR, ValidatorCheckUtil.getErrorMessage("user.repeatbound"));
	            return jsonObj;
	        }
	        
	        // 汇盈金服账号已绑定其他用户验证
	        String binduniqueid = apiUserBindService.getBindUniqueIdByUserId(userId, apiUserPostBean.getPid());
	        if(binduniqueid != null){
	        	jsonObj = new JSONObject();
	            jsonObj.put(AemsApiUserBindDefine.STATUS, AemsApiUserBindDefine.STATUS_FALSE);
	            jsonObj.put(AemsApiUserBindDefine.ERROR, ValidatorCheckUtil.getErrorMessage("user.useridbound"));
	            return jsonObj;
	        }
		}
		
		// 登陆
        jsonObj = loginController.login(request, response, loginBean);
        if (jsonObj.get(AemsApiUserBindDefine.STATUS).equals(AemsApiUserBindDefine.STATUS_FALSE)) {
            // 登陆失败，返回失败信息
            return jsonObj;
        }

        // 授权
        Boolean result = apiUserBindService.bindThirdUser(userId, bindUniqueId+"", apiUserPostBean.getPid());
        if(!result){
        	jsonObj = new JSONObject();
            jsonObj.put(AemsApiUserBindDefine.STATUS, AemsApiUserBindDefine.STATUS_FALSE);
            jsonObj.put(AemsApiUserBindDefine.ERROR, "授权失败，请联系汇盈金服客服。");
            return jsonObj;
        }
        
        // 返回第三方页面
        JSONObject jsonResult = new JSONObject();
        jsonResult.put(AemsApiUserBindDefine.STATUS, AemsApiUserBindDefine.STATUS_TRUE);
        jsonResult.put("statusCode", AemsApiUserBindDefine.ERROR_SUCCESS);
        jsonResult.put("statusDesc", "授权成功"); 
        jsonResult.put(AemsApiUserBindDefine.RETURL, apiUserPostBean.getRetUrl());
        jsonResult.put("bindUniqueIdScy", apiUserPostBean.getBindUniqueIdScy());
        jsonResult.put("hyjfUserName",userName ); 
        jsonResult.put("userId",users.getUserId() ); 
        Long timestamp = System.currentTimeMillis();
        jsonResult.put("timestamp",timestamp); 
        jsonResult.put("chkValue",ApiSignUtil.encryptByRSA(apiUserPostBean.getPid()+timestamp+"")); 
        logger.info("chkValue:"+ApiSignUtil.encryptByRSA(apiUserPostBean.getPid()+timestamp+""));
		
		return jsonResult;
    }
}
