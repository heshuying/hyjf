/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.config.authconfig;

import com.hyjf.admin.BaseController;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.HjhUserAuthConfig;
import com.hyjf.mybatis.model.auto.HjhUserAuthConfigCustomize;
import com.hyjf.mybatis.model.auto.HjhUserAuthConfigLogCustomize;
import com.hyjf.mybatis.model.customize.AdminSystem;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 配置中心-授权配置
 * @author jun
 * @version AuthConfigController, v0.1 2018/8/16 15:19
 */
@Controller
@RequestMapping("/manager/config/authconfig")
public class AuthConfigController extends BaseController {

    Logger logger = LoggerFactory.getLogger(AuthConfigController.class);

    public static final String THIS_CLASS = AuthConfigController.class.getName();

    @Autowired
    private AuthConfigService authConfigService;

    @RequestMapping("/initAuthConfigAction")
    @RequiresPermissions("authconfig:VIEW")
    public ModelAndView initAuthConfigAction(HttpServletRequest request, @ModelAttribute("authConfigForm") AuthConfigBean form) {
        LogUtil.startLog(THIS_CLASS, "initAuthConfigAction");
        ModelAndView modelAndView = new ModelAndView("manager/config/authconfig/authConfig");
        // 初始化授权配置页
        this.createPage(modelAndView,form);

        LogUtil.endLog(THIS_CLASS, "initAuthConfigAction");
        return modelAndView;
    }


    @RequestMapping("/initAuthConfigLogAction")
    @RequiresPermissions("authconfig:VIEW")
    public ModelAndView initAuthConfigLogAction(HttpServletRequest request, @ModelAttribute("authConfigLogForm") AuthConfigLogBean form){
        LogUtil.startLog(THIS_CLASS, "initAuthConfigLogAction");
        ModelAndView modelAndView = new ModelAndView("/manager/config/authconfig/authConfigLog");
        this.createLogPage(modelAndView,form);
        LogUtil.endLog(THIS_CLASS, "initAuthConfigLogAction");
        return modelAndView;
    }

    /**
     * 初始化授权配置操作记录
     * @param modelAndView
     */
    private void createLogPage(ModelAndView modelAndView,AuthConfigLogBean form) {
        int count=authConfigService.getAuthConfigLogCount();
        if (count>0){
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            List<HjhUserAuthConfigLogCustomize> authConfigLogList = this.authConfigService.getAuthConfigLogList(paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(authConfigLogList);
            modelAndView.addObject("authConfigLogForm", form);
        }
    }

    /**
     * 初始化授权配置页数据
     * @param modelAndView
     * @param form
     */
    private void createPage(ModelAndView modelAndView,AuthConfigBean form) {
        int count=authConfigService.getAuthConfigCount();
        if (count>0){
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            List<HjhUserAuthConfigCustomize> authConfigList = this.authConfigService.getAuthConfigList(paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(authConfigList);
            modelAndView.addObject("authConfigForm", form);
        }
    }

    /**
     * 获取授权配置编辑信息
     * @param request
     * @return 进入授权配置详情页面
     */
    @RequestMapping("/goUpdateAuthConfigAction")
    @RequiresPermissions("authconfig:VIEW")
    public ModelAndView goUpdateAuthConfigAction(HttpServletRequest request,@ModelAttribute("authConfigForm") AuthConfigBean form) {
        LogUtil.startLog(THIS_CLASS, "goUpdateAuthConfigAction");
        ModelAndView modelAndView = new ModelAndView("manager/config/authconfig/authConfigInfo");

        HjhUserAuthConfig authConfig = null;
        com.hyjf.common.util.HjhUserAuthConfig ret = null;
        if (StringUtils.isNotEmpty(form.getIds())) {
            Integer id = Integer.valueOf(form.getIds());
            switch (id) {
                case 1:
                    ret=CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH);
                    authConfig=this.convertAuthConfig(ret,id);
                    break;
                case 2:
                    ret=CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_REPAYMENT_AUTH);
                    authConfig=this.convertAuthConfig(ret,id);
                    break;
                case 3:
                    ret=CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_AUTO_TENDER_AUTH);
                    authConfig=this.convertAuthConfig(ret,id);
                    break;
                case 4:
                    ret=CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_AUTO_CREDIT_AUTH);
                    authConfig=this.convertAuthConfig(ret,id);
                    break;
                default:
                    break;
            }

        }

        modelAndView.addObject("authConfigForm", authConfig);
        LogUtil.endLog(THIS_CLASS, "goUpdateAuthConfigAction");
        return modelAndView;
    }


    /**
     * 先从redis缓存里面取,redis里面没有才从数据库里面取
     * @param ret
     * @return
     */
    private HjhUserAuthConfig convertAuthConfig(com.hyjf.common.util.HjhUserAuthConfig ret,Integer id) {
        HjhUserAuthConfig authConfig = new HjhUserAuthConfig();
        if (Validator.isNull(ret)){
            // 根据授权配置id查询配置详情 redis缓存里面没有从 数据库中取
            authConfig = authConfigService.getAuthConfigById(id);
        }else{
            BeanUtils.copyProperties(ret, authConfig);
        }
        return authConfig;
    }


    @RequestMapping("/updateAuthConfigAction")
    @RequiresPermissions("authconfig:MODIFY")
    public ModelAndView update(HttpServletRequest request,HjhUserAuthConfig form) {
        // 日志开始
        LogUtil.startLog(THIS_CLASS, "updateAuthConfigAction");
        ModelAndView modelAndView = new ModelAndView("manager/config/authconfig/authConfigInfo");

        AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
        form.setUpdateUser(Integer.parseInt(adminSystem.getId()));
        form.setUpdateTime(GetDate.getNowTime10());
        // 成功修改
        int result = this.authConfigService.updateRecord(form,request);
        // 更新redis中的可用余额
        if(result > 0){
            Integer id=form.getId();
            switch (id) {
                case 1:
                    RedisUtils.setObj(CommonUtils.KEY_PAYMENT_AUTH, form);
                    break;
                case 2:
                    RedisUtils.setObj(CommonUtils.KEY_REPAYMENT_AUTH, form);
                    break;
                case 3:
                    RedisUtils.setObj(CommonUtils.KEY_AUTO_TENDER_AUTH, form);
                    break;
                case 4:
                    RedisUtils.setObj(CommonUtils.KEY_AUTO_CREDIT_AUTH, form);
                    break;
                default:
                    break;
            }
        }
        modelAndView.addObject("success", "success");
        // 日志结束
        LogUtil.endLog(THIS_CLASS, "updateAuthConfigAction");
        return modelAndView;
    }



}
