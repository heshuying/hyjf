package com.hyjf.wechat.controller.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.service.login.LoginService;
import com.hyjf.wechat.util.ResultEnum;
import com.hyjf.wechat.util.SecretUtil;

/**
 * 
 * 单点登录功能
 * @author pcc
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月1日
 * @see 上午10:20:44
 */
@Controller(LoginDefine.SSO_CONTROLLER_NAME)
@RequestMapping(value = LoginDefine.REQUEST_MAPPING)
public class SSOLoginController extends BaseController {

    @Autowired
    private LoginService loginService;
    
    Logger _log = LoggerFactory.getLogger(SSOLoginController.class);

    
    @ResponseBody
    @RequestMapping(value = LoginDefine.SSO_DOLOGIN_MAPPING, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public BaseResultBean doLogin(HttpServletRequest request, HttpServletResponse response, @RequestParam String userId, @RequestParam String chkValue,@RequestParam String timestamp) {
        LogUtil.startLog(SSOLoginController.class.getName(), LoginDefine.SSO_DOLOGIN_MAPPING);
        LoginResultBean result = new LoginResultBean();
        
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(chkValue)|| StringUtils.isBlank(timestamp)) {
        	return result.setEnum(ResultEnum.ERROR_037);
        }
        if (!StringUtils.isNumeric(userId) ) {
        	return result.setEnum(ResultEnum.ERROR_037);
        }
        
        //验签
        if(!this.verifyRequestSign(userId,chkValue,timestamp)){
        	return result.setEnum(ResultEnum.ERROR_044);
        }
        
        int uId = loginService.updateSSOLoginInAction(userId, CustomUtil.getIpAddr(request));

        switch (uId) {
            case -1:
                result.setEnum(ResultEnum.ERROR_001);
                break;
            case -2:
                result.setEnum(ResultEnum.ERROR_002);
                break;
            case -3:
                result.setEnum(ResultEnum.ERROR_003);
                break;
            case -4:
                result.setEnum(ResultEnum.ERROR_043);
                break;
            default:
                Users users = loginService.getUsers(uId);

                BankOpenAccount account = loginService.getBankOpenAccount(uId);
                String accountId = null;
                if (account != null && StringUtils.isNoneBlank(account.getAccount())) {
                    accountId = account.getAccount();
                    /*********** 登录时自动同步线下充值记录 start ***********/
                    if (users.getBankOpenAccount() == 1) {
                        CommonSoaUtils.synBalance(users.getUserId());
                    }
                    /*********** 登录时自动同步线下充值记录 end ***********/
                }
                String sign = SecretUtil.createToken(uId, users.getUsername(), accountId);
                
                // 输出登录来源
        		_log.info("loginFrom:"+RedisUtils.get("loginFrom"+userId));
                // 登录完成返回值
                result.setStatus(ResultEnum.SUCCESS.getStatus());
                result.setStatusDesc("登录成功");
                result.setSign(sign);
                break;
        }

        LogUtil.endLog(SSOLoginController.class.getName(), LoginDefine.SSO_DOLOGIN_MAPPING);
        return result;
    }


	private boolean verifyRequestSign(String userId, String chkValue,
			String timestamp) {
		String accessKey ="SSODoLogin";
		System.out.println(StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + timestamp + accessKey)));
		if(StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + timestamp + accessKey)).equals(chkValue)){
			return true;
		}
		return false;
	}
    
    
}
