package com.hyjf.admin.maintenance.menu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.maintenance.role.AdminRoleDefine;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.customize.AdminSystem;

/**
 * @package com.hyjf.admin.maintenance.Admin
 * @author GOGTZ-T
 * @date 2015/11/29 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = AdminMenuDefine.REQUEST_MAPPING)
public class AdminMenuController extends BaseController {
    /**
     * 类名
     */
    private static final String THIS_CLASS = AdminMenuController.class.getName();

    @Autowired
    private AdminMenuService adminMenuService;

    /**
     * 菜单管理画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AdminMenuDefine.INIT)
    @RequiresPermissions(AdminMenuDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(AdminMenuDefine.ADMIN_MENU_FORM) AdminMenuBean form) {
        LogUtil.startLog(THIS_CLASS, AdminMenuDefine.INIT);

        ModelAndView modelAndView = new ModelAndView(AdminMenuDefine.LIST_PATH);
        modelAndView.addObject(AdminMenuDefine.ADMIN_MENU_FORM, form);

        LogUtil.endLog(THIS_CLASS, AdminMenuDefine.INIT);
        return modelAndView;
    }

    /**
     * 菜单管理画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AdminMenuDefine.INFO_ACTION)
    @ResponseBody
    @RequiresPermissions(AdminMenuDefine.PERMISSIONS_VIEW)
    public String infoAction(@RequestBody AdminMenuBean form) {
        LogUtil.startLog(THIS_CLASS, AdminMenuDefine.INFO_ACTION);

        JSONArray ja = this.adminMenuService.getRecordList(form);
        if (ja != null) {
            return ja.toString();
        }

        LogUtil.endLog(THIS_CLASS, AdminMenuDefine.INFO_ACTION);
        return StringUtils.EMPTY;
    }

    /**
     * 添加菜单管理信息
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = AdminMenuDefine.INSERT_ACTION, method = RequestMethod.POST)
    @RequiresPermissions(value = { AdminMenuDefine.PERMISSIONS_ADD })
    public ModelAndView insertAction(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(AdminMenuDefine.ADMIN_MENU_FORM) AdminMenuBean form) {
        LogUtil.startLog(THIS_CLASS, AdminMenuDefine.INSERT_ACTION);
        ModelAndView modelAndView = new ModelAndView(AdminMenuDefine.LIST_PATH);

        // 设置选中的菜单
        form.setSelectedNode(form.getMenuUuid());

        // 画面验证
        this.validatorFieldCheck(modelAndView, form);

        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
            modelAndView.addObject(AdminMenuDefine.ADMIN_MENU_FORM, form);

            LogUtil.errorLog(THIS_CLASS, AdminMenuDefine.INSERT_ACTION, "输入内容验证失败", null);
            return modelAndView;
        }

        this.adminMenuService.insertRecord(form);

        // 更新权限
        ShiroUtil.updateAuth();

        attr.addFlashAttribute(AdminMenuDefine.ADMIN_MENU_FORM, form);
        modelAndView = new ModelAndView("redirect:" + AdminMenuDefine.REQUEST_MAPPING + "/" + AdminMenuDefine.INIT);
        LogUtil.endLog(THIS_CLASS, AdminMenuDefine.INSERT_ACTION);
        return modelAndView;
    }

    /**
     * 修改菜单管理信息
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = AdminMenuDefine.UPDATE_ACTION, method = RequestMethod.POST)
    @RequiresPermissions(value = { AdminMenuDefine.PERMISSIONS_MODIFY })
    public ModelAndView updateAction(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(AdminMenuDefine.ADMIN_MENU_FORM) AdminMenuBean form) {
        LogUtil.startLog(THIS_CLASS, AdminMenuDefine.UPDATE_ACTION);
        ModelAndView modelAndView = new ModelAndView(AdminMenuDefine.LIST_PATH);

        // 设置选中的菜单
        form.setSelectedNode(form.getMenuUuid());

        // 画面验证
        this.validatorFieldCheck(modelAndView, form);

        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
            modelAndView.addObject(AdminMenuDefine.ADMIN_MENU_FORM, form);

            LogUtil.errorLog(THIS_CLASS, AdminMenuDefine.INSERT_ACTION, "输入内容验证失败", null);
            return modelAndView;
        }
        // 更新菜单
        this.adminMenuService.updateRecord(form);

        // 更新权限
        ShiroUtil.updateAuth();

        attr.addFlashAttribute(AdminMenuDefine.ADMIN_MENU_FORM, form);
        modelAndView = new ModelAndView("redirect:" + AdminMenuDefine.REQUEST_MAPPING + "/" + AdminMenuDefine.INIT);
        LogUtil.endLog(THIS_CLASS, AdminMenuDefine.UPDATE_ACTION);
        return modelAndView;
    }

    /**
     * 画面校验
     *
     * @param modelAndView
     * @param form
     */
    private void validatorFieldCheck(ModelAndView modelAndView, AdminMenuBean form) {
        // 菜单名称
        ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "menuName", form.getMenuName(), 20, true);
        // 权限名称
        ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "menuCtrl", form.getMenuCtrl(), 20, false);
        // URL
        ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "menuUrl", form.getMenuUrl(), 255, false);
        // 图标
        ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "menuIcon", form.getMenuIcon(), 50, false);
        // 排序
        ValidatorFieldCheckUtil.validateNum(modelAndView, "menuSort", GetterUtil.getString(form.getMenuSort()), false);
    }

    /**
     * 删除菜单
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AdminMenuDefine.DELETE_ACTION)
    @RequiresPermissions(AdminMenuDefine.PERMISSIONS_DELETE)
    public String deleteRecordAction(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(AdminMenuDefine.ADMIN_MENU_FORM) AdminMenuBean form) {
        LogUtil.startLog(THIS_CLASS, AdminMenuDefine.DELETE_ACTION);
        List<String> recordList = JSONArray.parseArray(form.getIds(), String.class);

        this.adminMenuService.deleteRecord(recordList);

        // 更新权限
        ShiroUtil.updateAuth();

        // 设置选中的菜单
        form.setSelectedNode(form.getMenuPuuid());
        attr.addFlashAttribute(AdminMenuDefine.ADMIN_MENU_FORM, form);
        LogUtil.endLog(THIS_CLASS, AdminMenuDefine.DELETE_ACTION);
        return "redirect:" + AdminMenuDefine.REQUEST_MAPPING + "/" + AdminMenuDefine.INIT;
    }

    /**
     * 迁移到授权画面
     *
     * @param request
     * @param form
     * @return
     * @throws Exception
     */
    @RequestMapping(value = AdminMenuDefine.SETTING_ACTION)
    public ModelAndView moveToAuthAction(HttpServletRequest request, AdminMenuBean form) throws Exception {
        LogUtil.startLog(THIS_CLASS, AdminMenuDefine.SETTING_ACTION);
        ModelAndView modelAndView = new ModelAndView(AdminMenuDefine.SETTING_PATH);
        String menuUuid = form.getMenuUuid();
        AdminMenuBean bean = new AdminMenuBean();

        if (Validator.isNotNull(menuUuid)) {
            bean.setMenuUuid(menuUuid);
            List<AdminSystem> list = this.adminMenuService.getMenuPermissionsList(menuUuid);
            bean.setRecordList(list);
        } else {
            throw new Exception("菜单不存在");
        }

        modelAndView.addObject(AdminMenuDefine.ADMIN_MENU_FORM, bean);
        LogUtil.endLog(THIS_CLASS, AdminMenuDefine.SETTING_ACTION);
        return modelAndView;
    }

    /**
     * 修改菜单权限管理信息
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = AdminMenuDefine.UPDATE_SETTING_ACTION, method = RequestMethod.POST)
    public ModelAndView updateMenuPermissionsAction(@ModelAttribute(AdminMenuDefine.ADMIN_MENU_FORM) AdminMenuBean form) throws Exception {
        LogUtil.startLog(THIS_CLASS, AdminMenuDefine.UPDATE_SETTING_ACTION);
        ModelAndView modelAndView = new ModelAndView(AdminMenuDefine.SETTING_PATH);

        String menuUuid = form.getMenuUuid();
        AdminMenuBean bean = new AdminMenuBean();
        if (Validator.isNotNull(menuUuid)) {
            // 更新菜单权限
            this.adminMenuService.updateMenuPermissions(form);

            bean.setMenuUuid(menuUuid);
            List<AdminSystem> list = this.adminMenuService.getMenuPermissionsList(menuUuid);
            bean.setRecordList(list);
        } else {
            throw new Exception("菜单不存在");
        }

        modelAndView.addObject(AdminMenuDefine.ADMIN_MENU_FORM, bean);
        modelAndView.addObject(AdminRoleDefine.SUCCESS, AdminRoleDefine.SUCCESS);
        LogUtil.endLog(THIS_CLASS, AdminMenuDefine.UPDATE_SETTING_ACTION);
        return modelAndView;
    }
}
