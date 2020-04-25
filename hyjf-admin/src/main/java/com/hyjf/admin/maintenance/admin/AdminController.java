package com.hyjf.admin.maintenance.admin;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.annotate.Token;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.customize.AdminCustomize;

/**
 * @package com.hyjf.admin.maintenance.Admin
 * @author GOGTZ-T
 * @date 2015/11/29 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = AdminDefine.REQUEST_MAPPING)
public class AdminController extends BaseController {
    /**
     * 类名
     */
    private static final String THIS_CLASS = AdminController.class.getName();

    @Autowired
    private AdminService adminService;

    /**
     * 账户设置画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AdminDefine.INIT)
    @RequiresPermissions(AdminDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, @ModelAttribute("delete_error") String error, @ModelAttribute AdminBean form) {
        LogUtil.startLog(THIS_CLASS, AdminDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(AdminDefine.LIST_PATH);

        if (Validator.isNotNull(error)) {
            modelAndView.addObject("delete_error", error);
        }
        
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, AdminDefine.INIT);
        return modelAndView;
    }

    /**
     * 画面查询
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AdminDefine.SEARCH_ACTION)
    @RequiresPermissions(AdminDefine.PERMISSIONS_VIEW)
    public ModelAndView search(HttpServletRequest request, AdminBean form) {
        LogUtil.startLog(THIS_CLASS, AdminDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(AdminDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, AdminDefine.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 创建账户设置分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, AdminBean form) {
        AdminCustomize adminCustomize = new AdminCustomize();
        BeanUtils.copyProperties(form, adminCustomize);
        List<AdminCustomize> recordList = this.adminService.getRecordList(adminCustomize);
        if (recordList != null) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
            adminCustomize.setLimitStart(paginator.getOffset());
            adminCustomize.setLimitEnd(paginator.getLimit());
            recordList = this.adminService.getRecordList(adminCustomize);
            form.setPaginator(paginator);
            form.setRecordList(recordList);
            modelAndView.addObject(AdminDefine.ADMIN_FORM, form);
        }
    }

    /**
     * 迁移到账户设置详细画面
     * 
     * @param request
     * @param form
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping(value = AdminDefine.INFO_ACTION)
    @Token(save = true)
    @RequiresPermissions(value = { AdminDefine.PERMISSIONS_ADD, AdminDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
    public ModelAndView moveToInfoAction(HttpServletRequest request, AdminBean form) throws Exception {
        LogUtil.startLog(THIS_CLASS, AdminDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(AdminDefine.INFO_PATH);

        String ids = form.getIds();
        AdminBean bean = new AdminBean();

        if (Validator.isNotNull(ids) && NumberUtils.isNumber(ids)) {
            Integer id = Integer.valueOf(ids);
            bean.setId(id);

            // 根据主键判断该条数据在数据库中是否存在
            boolean isExists = this.adminService.isExistsRecord(id);

            // 没有添加权限 同时 也没能检索出数据的时候异常
            if (!isExists) {
                Subject currentUser = SecurityUtils.getSubject();
                currentUser.checkPermission(AdminDefine.PERMISSIONS_ADD);
            }

            // 根据主键检索数据
            AdminCustomize record = this.adminService.getRecord(id);
            BeanUtils.copyProperties(record, bean);
        }

        // 设置部门列表
        bean.setDepartmentList(this.adminService.getDepartmentList());
        // 设置角色列表
        bean.setAdminRoleList(this.adminService.getAdminRoleList());

        modelAndView.addObject(AdminDefine.ADMIN_FORM, bean);
        LogUtil.endLog(THIS_CLASS, AdminDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     * 添加账户设置信息
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = AdminDefine.INSERT_ACTION, method = RequestMethod.POST)
    @Token(check = true, forward = AdminDefine.TOKEN_INIT_PATH)
    @RequiresPermissions(value = { AdminDefine.PERMISSIONS_ADD })
    public ModelAndView insertAction(HttpServletRequest request, AdminBean form) {
        LogUtil.startLog(THIS_CLASS, AdminDefine.INSERT_ACTION);
        ModelAndView modelAndView = new ModelAndView(AdminDefine.INFO_PATH);
        // 画面验证
        this.validatorFieldCheck(modelAndView, form);

        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
            // 设置部门列表
            form.setDepartmentList(this.adminService.getDepartmentList());
            // 设置角色列表
            form.setAdminRoleList(this.adminService.getAdminRoleList());

            modelAndView.addObject(AdminDefine.ADMIN_FORM, form);

            LogUtil.errorLog(THIS_CLASS, AdminDefine.INSERT_ACTION, "输入内容验证失败", null);
            return modelAndView;
        }

        this.adminService.insertRecord(form);
        // 更新权限
        ShiroUtil.updateAuth();
        
        this.createPage(request, modelAndView, form);

        modelAndView.addObject(AdminDefine.SUCCESS, AdminDefine.SUCCESS);
        LogUtil.endLog(THIS_CLASS, AdminDefine.INSERT_ACTION);
        return modelAndView;
    }

    /**
     * 修改账户设置信息
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = AdminDefine.UPDATE_ACTION, method = RequestMethod.POST)
    @Token(check = true, forward = AdminDefine.TOKEN_INIT_PATH)
    @RequiresPermissions(value = { AdminDefine.PERMISSIONS_MODIFY })
    public ModelAndView updateAction(HttpServletRequest request, AdminBean form) {
        LogUtil.startLog(THIS_CLASS, AdminDefine.UPDATE_ACTION);
        ModelAndView modelAndView = new ModelAndView(AdminDefine.INFO_PATH);

        // 画面验证
        this.validatorFieldCheck(modelAndView, form);

        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
            // 设置部门列表
            form.setDepartmentList(this.adminService.getDepartmentList());
            // 设置角色列表
            form.setAdminRoleList(this.adminService.getAdminRoleList());

            modelAndView.addObject(AdminDefine.ADMIN_FORM, form);

            LogUtil.errorLog(THIS_CLASS, AdminDefine.INSERT_ACTION, "输入内容验证失败", null);
            return modelAndView;
        }

        this.adminService.updateRecord(form);
        // 创建分页
        this.createPage(request, modelAndView, form);

        modelAndView.addObject(AdminDefine.SUCCESS, AdminDefine.SUCCESS);
        LogUtil.endLog(THIS_CLASS, AdminDefine.UPDATE_ACTION);
        return modelAndView;
    }

    /**
     * 画面校验
     * 
     * @param modelAndView
     * @param form
     */
    private void validatorFieldCheck(ModelAndView modelAndView, AdminBean form) {
        // 用户名(必须,最大长度)
        ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "username", form.getUsername(), 20, true);
        // 姓名(必须,最大长度,汉字)
        ValidatorFieldCheckUtil.validateChinese(modelAndView, "truename", form.getTruename(), 10, true);
        // 所属部门(必须)
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "departmentId", String.valueOf(form.getDepartmentId()));
        // 邮箱(必须,邮箱,最大长度)
        ValidatorFieldCheckUtil.validateMailAndMaxLength(modelAndView, "email", form.getEmail(), 100, false);
        // 手机号码(必须,数字,最大长度)
        ValidatorFieldCheckUtil.validateNumJustLength(modelAndView, "mobile", form.getMobile(), 11, true);
        // 账户状态
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "state", form.getState());

        if (!ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
            // 检查手机号码唯一性
            int cnt = adminService.countAdminByMobile(form.getId(), form.getMobile());
            if (cnt > 0) {
                ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "mobile", "repeat");
            }
            // 检查用户名唯一性
            cnt = adminService.countAdminByUsername(form.getId(), form.getUsername());
            if (cnt > 0) {
                ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "username", "repeat");
            }
        }
    }

    /**
     * 删除账户
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AdminDefine.DELETE_ACTION)
    @RequiresPermissions(AdminDefine.PERMISSIONS_DELETE)
    public ModelAndView deleteRecordAction(HttpServletRequest request, RedirectAttributes attr, AdminBean form) {
        LogUtil.startLog(THIS_CLASS, AdminDefine.DELETE_ACTION);
        ModelAndView modelAndView = new ModelAndView(AdminDefine.LIST_PATH);
        
        List<Integer> recordList = JSONArray.parseArray(form.getIds(), Integer.class);

        if (recordList.contains(GetterUtil.getInteger(ShiroUtil.getLoginUserId()))) {
            attr.addFlashAttribute("delete_error", "不能删除自己,请重新选择!");
            modelAndView.setViewName("redirect:" + AdminDefine.REQUEST_MAPPING + "/" + AdminDefine.INIT);
            return modelAndView;
        }
        
        this.adminService.deleteRecord(recordList);
        LogUtil.endLog(THIS_CLASS, AdminDefine.DELETE_ACTION);
        modelAndView.setViewName("redirect:" + AdminDefine.REQUEST_MAPPING + "/" + AdminDefine.INIT);
        return modelAndView;
    }
    
    /**
     * 重置密码账户
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AdminDefine.RESETPWD_ACTION)
    @RequiresPermissions(AdminDefine.PERMISSIONS_RESETPWD)
    public ModelAndView resetPwdAction(HttpServletRequest request, RedirectAttributes attr, AdminBean form) {
        LogUtil.startLog(THIS_CLASS, AdminDefine.RESETPWD_ACTION);
        ModelAndView modelAndView = new ModelAndView(AdminDefine.LIST_PATH);
        
        List<Integer> recordList = JSONArray.parseArray(form.getIds(), Integer.class);
        if (recordList.size()==0) {
            attr.addFlashAttribute("delete_error", "当前用户不存在");
            modelAndView.setViewName("redirect:" + AdminDefine.REQUEST_MAPPING + "/" + AdminDefine.INIT);
            return modelAndView;
        }
        this.adminService.resetPwdAction(recordList);
        LogUtil.endLog(THIS_CLASS, AdminDefine.RESETPWD_ACTION);
        modelAndView.setViewName("redirect:" + AdminDefine.REQUEST_MAPPING + "/" + AdminDefine.INIT);
        modelAndView.addObject(AdminDefine.SUCCESS, AdminDefine.SUCCESS);
        return modelAndView;
    }
    

    /**
     * 检查手机号码或用户名唯一性
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AdminDefine.CHECK_ACTION, method = RequestMethod.POST)
    @RequiresPermissions(value = { AdminDefine.PERMISSIONS_ADD, AdminDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
    public String checkAction(HttpServletRequest request) {
        LogUtil.startLog(THIS_CLASS, AdminDefine.CHECK_ACTION);

        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String param = request.getParameter("param");
        
        JSONObject ret = new JSONObject();
        // 检查手机号码唯一性
        if ("mobile".equals(name)) {
            int cnt = adminService.countAdminByMobile(GetterUtil.getInteger(id), param);
            if (cnt > 0) {
                String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
                message = message.replace("{label}", "手机号码");
                ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
            }
        } else if ("username".equals(name)) {
            // 检查用户名唯一性
            int cnt = adminService.countAdminByUsername(GetterUtil.getInteger(id), param);
            if (cnt > 0) {
                String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
                message = message.replace("{label}", "用户名");
                ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
            }
        } else if ("email".equals(name)) {
            // 检查邮箱唯一性
            int cnt = adminService.countAdminByEmail(GetterUtil.getInteger(id), param);
            if (cnt > 0) {
                String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
                message = message.replace("{label}", "邮箱");
                ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
            }
        }
        // 没有错误时,返回y
        if (!ret.containsKey(AdminDefine.JSON_VALID_INFO_KEY)) {
            ret.put(AdminDefine.JSON_VALID_STATUS_KEY, AdminDefine.JSON_VALID_STATUS_OK);
        }

        LogUtil.endLog(THIS_CLASS, AdminDefine.CHECK_ACTION);
        return ret.toString();
    }
}
