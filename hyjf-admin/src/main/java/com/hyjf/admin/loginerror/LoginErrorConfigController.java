/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.loginerror;

import com.hyjf.admin.loginerror.config.LoginErrorConfig;
import com.hyjf.admin.loginerror.config.LoginErrorConfigManager;
import com.hyjf.common.result.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author cui
 * @version LoginErrorConfigController, v0.1 2018/7/13 10:36
 */

@RequestMapping(value = LoginErrorConfigDefine.REQUEST_MAPPING)
@Controller
public class LoginErrorConfigController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private LoginErrorConfigService loginErrorConfigService;

    @RequestMapping(value = LoginErrorConfigDefine.WEBCONFIG_ACTION, method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<LoginErrorConfig.Config> getWebConfig(HttpServletRequest request) {

        ResultBean<LoginErrorConfig.Config> resultBean = new ResultBean<>(ResultBean.SUCCESS, "成功");

        resultBean.setData(LoginErrorConfigManager.getInstance().getWebConfig());

        return resultBean;

    }

    @RequestMapping(value = LoginErrorConfigDefine.ADMINCONFIG_ACTION, method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<LoginErrorConfig.Config> getAdminConfig(HttpServletRequest request) {

        ResultBean<LoginErrorConfig.Config> resultBean = new ResultBean<>(ResultBean.SUCCESS, "成功");

        resultBean.setData(LoginErrorConfigManager.getInstance().getAdminConfig());

        return resultBean;
    }

    @RequestMapping(value = LoginErrorConfigDefine.WEBCONFIG_ACTION, method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setWebConfig(HttpServletRequest request, @RequestBody LoginErrorConfig.Config webConfig) {

        try {
            loginErrorConfigService.saveWebConfig(webConfig);
        } catch (Exception e) {
            logger.error("保存前台登录失败配置出错",e);
            return new ResultBean(ResultBean.ERROR,"失败");
        }

        return new ResultBean(ResultBean.SUCCESS, "成功");
    }


    @RequestMapping(value = LoginErrorConfigDefine.ADMINCONFIG_ACTION, method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setAdminConfig(HttpServletRequest request, @RequestBody LoginErrorConfig.Config adminConfig) {

        try {
            loginErrorConfigService.saveAdminConfig(adminConfig);
        } catch (Exception e) {
            logger.error("保存后台登录失败配置出错",e);
            return new ResultBean(ResultBean.ERROR,"失败");
        }

        return new ResultBean(ResultBean.SUCCESS, "成功");
    }

}
