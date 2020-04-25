/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: lb
 * @version: 1.0
 * Created at: 2017年9月15日 上午9:43:49
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.web.api.common;

import com.alibaba.fastjson.JSON;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.security.utils.SignUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.CheckUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BindUsers;
import com.hyjf.mybatis.model.auto.BindUsersExample;
import com.hyjf.web.api.base.ApiBaseServiceImpl;
import com.hyjf.web.api.user.ApiUserPostBean;
import com.hyjf.web.api.user.NmcfPostBean;
import com.hyjf.web.api.util.RSA_Hjs;
import com.hyjf.web.common.HyjfSession;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.interceptor.InterceptorDefine;
import com.hyjf.web.util.WebUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author liubin
 */

@Service("ApiCommonService")
public class ApiCommonServiceImpl extends ApiBaseServiceImpl implements ApiCommonService {
	public static final String DEFAULT_ACCESSKEY = "aop.interface.accesskey";
	
	/**
	 * 传入信息验证,汇晶社自动登录、绑定
	 * @param bean
	 */
	@Override
	public void checkPostBeanOfWeb(ApiUserPostBean bean) {
    	//传入信息验证 
		CheckUtil.check(Validator.isNotNull(bean.getBindUniqueIdScy()), "Object.required", "bindUniqueIdScy");
		CheckUtil.check(Validator.isNotNull(bean.getChkValue()), "Object.required", "chkValue");
		CheckUtil.check(Validator.isNotNull(bean.getPid()), "Object.required", "pid");
	    CheckUtil.check(Validator.isNotNull(bean.getRetUrl()), "Object.required", "retUrl");
	    CheckUtil.check(Validator.isNotNull(bean.getTimestamp()), "Object.required", "timestamp");
		CheckUtil.check(Validator.isNotNull(bean.getChkValue()), "Object.required", "chkValue");
	}

    /**
     * 传入信息验证,纳觅财富自动登录、绑定
     * @param bean
     */
    @Override
    public void checkNmcfPostBean(NmcfPostBean bean) {
        //传入信息验证
        CheckUtil.check(Validator.isNotNull(bean.getTimestamp()), "Object.required", "timestamp");
        CheckUtil.check(Validator.isNotNull(bean.getInstCode()), "Object.required", "instCode");
        CheckUtil.check(Validator.isNotNull(bean.getUserId()), "Object.required", "userId");
        CheckUtil.check(Validator.isNotNull(bean.getChkValue()), "Object.required", "chkValue");
    }

    /**
     * 传入信息验证,汇晶社自动登录、绑定
     * @param bean
     */
    @Override
    public void checkPostBeanOfInfo(ApiUserPostBean bean) {
        //传入信息验证 
        CheckUtil.check(Validator.isNotNull(bean.getBindUniqueIdScy()), "Object.required", "bindUniqueIdScy");
        CheckUtil.check(Validator.isNotNull(bean.getChkValue()), "Object.required", "chkValue");
        CheckUtil.check(Validator.isNotNull(bean.getPid()), "Object.required", "pid");
	    CheckUtil.check(Validator.isNotNull(bean.getTimestamp()), "Object.required", "timestamp");
        CheckUtil.check(Validator.isNotNull(bean.getChkValue()), "Object.required", "chkValue");
    }
	

	/**
	 * 验签
	 * @param bean
	 */
	@Override
	public void checkSign(ApiUserPostBean bean) {
    	CheckUtil.check(SignUtil.checkSignDefaultKey(bean.getChkValue(), bean.getBindUniqueIdScy(),bean.getPid(),bean.getRetUrl(), bean.getTimestamp()), "sign");
	}

	/**
	 * 解密
	 * @param bean
	 * @return
	 */
	@Override
	public Long decrypt(ApiUserPostBean bean) {
		// RAC解密
		String str = decrypt(bean.getBindUniqueIdScy());
		// 解密结果数字验证
		CheckUtil.check(Validator.isDigit(str), "Object.digit", "bindUniqueIdScy");
		// 返回
		return Long.parseLong(str);	
	}

	public String decrypt(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		try {
		    return RSA_Hjs.decrypt(str,"utf-8");
        } catch (Exception e) {
            CheckUtil.check(false, "decrypt.error");
        }
		return null;
		
	}

    /**
     * RSA解密
     * @param bean
     * @return
     */
    @Override
    public Long RSAdecrypt(NmcfPostBean bean) {
        // RAC解密
        String str = decrypt(bean.getUserId());
        // 解密结果数字验证
        CheckUtil.check(Validator.isDigit(str), "Object.digit", "userId");
        // 返回
        return Long.parseLong(str);
    }

    /**
     * 加密
     * @param bean
     * @return
     */
    @Override
    public String encode(ApiUserPostBean bean) {
        // RAC加密
        String str = encode(bean.getBindUniqueIdScy());
        // 加密结果数字验证
        CheckUtil.check(Validator.isDigit(str), "Object.digit", "bindUniqueIdScy");
        // 返回
        return str;   
    }

    public String encode(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        try {
            return RSA_Hjs.encode(str);
        } catch (Exception e) {
            CheckUtil.check(Validator.isDigit(str), "errors.encode.error");
        }
        return null;
    }
	
	/**
	 * 根据绑定信息取得用户id
	 * @param bindUniqueId
	 * @return
	 */
	@Override
	public Integer getUserIdByBind(Long bindUniqueId, int bindPlatformId) {
		//检索条件
		BindUsersExample example = new BindUsersExample();
		BindUsersExample.Criteria cra = example.createCriteria();
		cra.andBindUniqueIdEqualTo(bindUniqueId+"");
		cra.andBindPlatformIdEqualTo(bindPlatformId);
		cra.andDelFlgEqualTo(0);//未删除
		//检索
		List<BindUsers> list = bindUsersMapper.selectByExample(example);
		//无记录时，未绑定汇盈金服
		CheckUtil.check(list != null && list.size() > 0, "user.unbound");
		return list.get(0).getUserId();
	}
	
	
	
	 /**
     * 当前登录用户
     * @param HttpServletRequest
     * @return WebViewUser当前登录用户对象
     */
    @Override
    public boolean checkLoginUser(HttpServletRequest request,HttpServletResponse response) {
        // 判断用户是否已经登录
        String sessionId = request.getParameter("sessionId");
        if (StringUtils.isNotBlank(sessionId)) {
            
            String result = RedisUtils.get(sessionId);// 使用sessionId从缓存中获取session对象
            if (StringUtils.isNotBlank(result)) {// 如果session对象不为空
                HyjfSession session = JSON.parseObject(result, HyjfSession.class);
                WebViewUser user = session.getUser();
                if (null == user) {// 说明用户没有登录
                    return false;
                } else {// 说明用户已经登录
                    RedisUtils.expire(sessionId, CustomConstants.SESSION_EXPIRE);
                    String hyjfUsername = user.getUsername();
                    String iconurl = user.getIconurl();
                    String sex = user.getSex().toString();
                    String roleId = user.getRoleId();
                    WebUtils.addCookie(request, response, "hyjfUsername", hyjfUsername, CustomConstants.SESSION_EXPIRE,
                            InterceptorDefine.HYJF_WEB_DOMAIN_NAMES_LIST.get(0));
                    WebUtils.addCookie(request, response, "sex", sex, CustomConstants.SESSION_EXPIRE,
                            InterceptorDefine.HYJF_WEB_DOMAIN_NAMES_LIST.get(0));
                    if (StringUtils.isNotBlank(iconurl)) {
                        WebUtils.addCookie(request, response, "iconurl", iconurl, CustomConstants.SESSION_EXPIRE,
                                InterceptorDefine.HYJF_WEB_DOMAIN_NAMES_LIST.get(0));
                    }
                    if (StringUtils.isNotBlank(roleId)) {
                        WebUtils.addCookie(request, response, "roleId", roleId, CustomConstants.SESSION_EXPIRE,
                                InterceptorDefine.HYJF_WEB_DOMAIN_NAMES_LIST.get(0));
                    }
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 当前登录用户退出
     * @param HttpServletRequest
     * @return WebViewUser当前登录用户对象
     */
    @Override
    public boolean removeCookie(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        if (StringUtils.isNotBlank(sessionId)) {
            // 删除session
            RedisUtils.del(sessionId);
         // 清除cookie
            WebUtils.removeCookie(request, response, "sessionId");
            WebUtils.removeCookie(request, response, "hyjfUsername");
            WebUtils.removeCookie(request, response, "sex");
            WebUtils.removeCookie(request, response, "iconurl");
            WebUtils.removeCookie(request, response, "roleId");
            // 推广渠道追加  liuyang 20170605 start
            WebUtils.removeCookie(request, response, "utm_id");
        }else{
            return false;
        }
        return true;
    }
}
	