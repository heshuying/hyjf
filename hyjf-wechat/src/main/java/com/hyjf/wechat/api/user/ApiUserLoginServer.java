package com.hyjf.wechat.api.user;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.wechat.BaseResultBeanFrontEnd;
import com.hyjf.wechat.api.common.ApiCommonService;
import com.hyjf.wechat.api.fclc.FclcPostBean;
import com.hyjf.wechat.base.BaseMapBean;
import com.hyjf.wechat.util.SecretUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lm
 */
@Controller
@RequestMapping(value = ApiUserLoginDefine.REQUEST_MAPPING)
public class ApiUserLoginServer {
	private Logger logger = LoggerFactory.getLogger(ApiUserLoginServer.class);
	
	/** 汇盈金服公钥文件地址(请求) **/
	public static final String HYJF_REQ_PUB_KEY_PATH = PropUtils.getSystem("wrb.callback.login.url");

	@Autowired
	private ApiCommonService apiCommonService;
	
	/**
	 * 第三方用户自动登陆接口
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@RequestMapping(value = ApiUserLoginDefine.THIRD_LOGIN)
    public ModelAndView nfcfLogin(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam String param,
                                  @RequestParam(value = "sign", required = false) String sign) {

        FclcPostBean bean = new FclcPostBean();
        try {
            bean = WrbParseParamUtil.mapToBean(WrbParseParamUtil.parseParam(param), FclcPostBean.class);

        } catch (Exception e) {
            logger.error("参数解析失败....", e);
        }
		// 验证
		apiCommonService.checkFclcPostBean(bean);
		Map<String, String> params = new HashMap<>();
		params.put("from", "hyjf");
		params.put("ticket", bean.getTicket());
		ModelAndView modelAndView = new ModelAndView(ApiUserLoginDefine.JUMP_HTML);
        BaseMapBean baseMapBean=new BaseMapBean();
        baseMapBean.setCallBackAction(PropUtils.getSystem("hyjf.wechat.web.host")+"/login");
		//回调风车理财获取用户账号ID
		String returnParams = WrbParseParamUtil.wrbCallback(HYJF_REQ_PUB_KEY_PATH, params);
		if(StringUtils.isNotBlank(returnParams)){
			JSONObject jsonObject = JSONObject.parseObject(returnParams);
			if(jsonObject.getInteger("retcode") == 0){
				String wrbUserId = jsonObject.getString("wrb_user_id");
				String pfUserId = jsonObject.getString("pf_user_id");
				if(!StringUtils.isNoneBlank(pfUserId)){
					modelAndView.addObject("callBackForm", baseMapBean);
	                return modelAndView;
				}
				// 查询userId
				Integer userId = apiCommonService.getUserIdByBind(wrbUserId, WrbCommonDateUtil.FCLC_INSTCODE, pfUserId);
				
				// userid不存在,跳转登录页面
				if (userId != null) {
					Users users = apiCommonService.getUsers(userId);
					BankOpenAccount account = apiCommonService.getBankOpenAccount(userId);
					String accountId = null;
					if (account != null && StringUtils.isNoneBlank(account.getAccount())) {
						accountId = account.getAccount();
						/*********** 登录时自动同步线下充值记录 start ***********/
						if (users.getBankOpenAccount() == 1) {
							CommonSoaUtils.synBalance(users.getUserId());
						}
						/*********** 登录时自动同步线下充值记录 end ***********/
					}
					sign = SecretUtil.createToken(userId, users.getUsername(), accountId);
					baseMapBean.set("sign", sign);
					
					//登录成功之后风车理财的特殊标记，供后续出借使用
					CookieUtils.addCookie(request, response, CustomConstants.TENDER_FROM_TAG,
							CustomConstants.WRB_CHANNEL_CODE);
					RedisUtils.del("loginFrom"+userId);
					RedisUtils.set("loginFrom"+userId, "2", 1800);

					// 回调url（h5错误页面）

					// 跳转url
					if (bean.getTarget_url() != null) {
						baseMapBean.setCallBackAction(bean.getTarget_url());

					} else {
						// 跳转首页（个人主页）
						baseMapBean.setCallBackAction(PropUtils.getSystem("hyjf.wechat.web.host"));
					}

					baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
					baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户登陆成功！");
					modelAndView.addObject("callBackForm", baseMapBean);
					return modelAndView;
				}
				
			}else {
				logger.info("回调风车理财票据："+bean.getTicket() + ",返回数据："+returnParams);
			}
		}
		
		modelAndView.addObject("callBackForm", baseMapBean);
        return modelAndView;
	}
}
