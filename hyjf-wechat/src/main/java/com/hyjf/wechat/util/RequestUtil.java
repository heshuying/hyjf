package com.hyjf.wechat.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.wechat.base.BaseServiceImpl;

/**
 * 
 * 请求的工具类
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月2日
 * @see 上午10:40:57
 */
@Component
public class RequestUtil extends BaseServiceImpl{

    /**
     * 
     * 获取用户ID
     * @author sunss
     * @return
     */
    public Integer getRequestUserId(HttpServletRequest request){
        return (Integer)request.getAttribute("userId");
    }
    
    /**
     * 
     * 获取登录用户名
     * @author sunss
     * @param request
     * @return
     */
    public String getRequestUserIdStr(HttpServletRequest request){
        return getRequestUserId(request)+"";
    }
    
    /**
     * 
     * 获取sign值
     * @author sunss
     * @param request
     * @return
     */
    public String getRequestSign(HttpServletRequest request){
        return (String)request.getAttribute("sign");
    }
    
    /**
     * 
     * 获取电子帐户号
     * @author sunss
     * @param request
     * @return
     */
    public String getAccount(HttpServletRequest request){
        String accountId = (String) request.getAttribute("accountId");
        if(StringUtils.isNotBlank(accountId)){
            return accountId;
        }else{
            BankOpenAccount account = getBankOpenAccount(getRequestUserId(request));
            if(account!=null&&StringUtils.isNotBlank(account.getAccount())){
                SecretUtil.refreshAccount((String)request.getAttribute("sign"),account.getAccount());
            }
            return account==null?null:account.getAccount();
        }
    }
}
