package com.hyjf.web.api.aems.login;

import com.hyjf.common.enums.utils.MsgEnum;
import com.hyjf.common.util.ApiSignUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.CheckUtil;
import com.hyjf.web.api.base.ApiBaseController;
import com.hyjf.web.api.common.ApiCommonService;
import com.hyjf.web.api.skip.ApiSkipFormBean;
import com.hyjf.web.api.user.ApiUserPostBean;
import com.hyjf.web.api.user.NmcfPostBean;
import com.hyjf.web.bank.web.borrow.BorrowDefine;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.login.LoginDefine;
import com.hyjf.web.user.login.LoginService;
import com.hyjf.web.user.pandect.PandectDefine;
import com.hyjf.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Aems用戶自动登录
 * @author Zha Daojian
 * @date 2018/9/17 17:08
 * @param
 * @return
 **/
@Controller
@RequestMapping(value = AemsApiUserLoginDefine.REQUEST_MAPPING)
public class AemsApiUserLoginServer extends ApiBaseController {
	/** 跳转的jsp页面 */
	private static final String SEND_JSP = "/bank/bank_send";

	@Autowired
	private ApiCommonService apiCommonService;
	@Autowired
	private LoginService loginService;

    @RequestMapping(value = AemsApiUserLoginDefine.LOGIN_THIRDAPI)
    public ModelAndView thirdLoginApi(ApiUserPostBean bean){
    	// 设置接口结果页的信息（返回Url）
    	this.initCheckUtil(bean);

		// 验证
        apiCommonService.checkPostBeanOfWeb(bean);

		ModelAndView modelAndView = new ModelAndView(SEND_JSP);
		ApiSkipFormBean apiSkipFormBean = new ApiSkipFormBean();
		String returl = CustomConstants.HOST + AemsApiUserLoginDefine.REQUEST_MAPPING + AemsApiUserLoginDefine.LOGIN_THIRDAPI_NOTIFY + ".do";

		apiSkipFormBean.setAction(returl);
		apiSkipFormBean.set("bindUniqueIdScy", bean.getBindUniqueIdScy());
		apiSkipFormBean.set("pid", bean.getPid()+"");
		apiSkipFormBean.set("retUrl", bean.getRetUrl());
		apiSkipFormBean.set("timestamp", String.valueOf(bean.getTimestamp()));
		apiSkipFormBean.set("chkValue", bean.getChkValue());
		modelAndView.addObject("bankForm", apiSkipFormBean);
		return modelAndView;
    }

	 /**
     * 第三方登录
     *
     * @param request
     * @return
     */
    @RequestMapping(value = AemsApiUserLoginDefine.LOGIN_THIRDAPI_NOTIFY)
    public ModelAndView thirdLogin(HttpServletRequest request,HttpServletResponse response, @ModelAttribute ApiUserPostBean bean){
		// 验证
        apiCommonService.checkPostBeanOfWeb(bean);
		// 验签
		apiCommonService.checkSign(bean);
		// 解密
		Long bindUniqueId = apiCommonService.decrypt(bean);
		// 查询Userid
		Integer userId = apiCommonService.getUserIdByBind(bindUniqueId, bean.getPid());

		// 登陆
        WebViewUser webUser = loginService.getWebViewUserByUserId(userId);
        WebUtils.sessionLogin(request, response, webUser);
        
        // 返回
        return new ModelAndView("redirect:" + CustomConstants.HOST
                + PandectDefine.REQUEST_MAPPING + PandectDefine.PANDECT_ACTION + ".do");
    }


}
