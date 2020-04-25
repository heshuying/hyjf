package com.hyjf.admin.maintenance.role;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.annotate.Token;
import com.hyjf.admin.finance.returncash.ReturncashDefine;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.AdminRole;
import com.hyjf.mybatis.model.auto.AdminRoleMenuPermissions;
import com.hyjf.mybatis.model.customize.AdminRoleCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminRole
 * @author GOGTZ-T
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = AdminRoleDefine.REQUEST_MAPPING)
public class AdminRoleController extends BaseController {
    /**
     * 类名
     */
    private static final String THIS_CLASS = AdminRoleController.class.getName();

    @Autowired
    private AdminRoleService adminRoleService;

    /**
     * 权限维护画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AdminRoleDefine.INIT)
    @RequiresPermissions(AdminRoleDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, @ModelAttribute("delete_error") String error, @ModelAttribute AdminRoleBean form) {
        LogUtil.startLog(THIS_CLASS, AdminRoleDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(AdminRoleDefine.LIST_PATH);

        if (Validator.isNotNull(error)) {
            modelAndView.addObject("delete_error", error);
        }
        
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, AdminRoleDefine.INIT);
        return modelAndView;
    }

    /**
     * 画面查询
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AdminRoleDefine.SEARCH_ACTION)
    @RequiresPermissions(AdminRoleDefine.PERMISSIONS_VIEW)
    public ModelAndView search(HttpServletRequest request, AdminRoleBean form) {
        LogUtil.startLog(THIS_CLASS, AdminRoleDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(AdminRoleDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, AdminRoleDefine.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 创建权限维护分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, AdminRoleBean form) {
        long cnt = this.adminRoleService.getRecordCount(form);
        if (cnt > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), (int) cnt);
            List<AdminRole> recordList = this.adminRoleService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
            modelAndView.addObject(AdminRoleDefine.ROLE_FORM, form);
        }
    }

    /**
     * 迁移到权限维护详细画面
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = AdminRoleDefine.INFO_ACTION)
    @Token(save = true)
    @RequiresPermissions(value = { AdminRoleDefine.PERMISSIONS_ADD, AdminRoleDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
    public ModelAndView moveToInfoAction(HttpServletRequest request, AdminRoleBean form) {
        LogUtil.startLog(THIS_CLASS, AdminRoleDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(AdminRoleDefine.INFO_PATH);
        String ids = form.getIds();
        AdminRoleBean bean = new AdminRoleBean();

        if (Validator.isNotNull(ids) && NumberUtils.isNumber(ids)) {
            Integer id = Integer.valueOf(ids);
            bean.setId(id);

            // 根据主键判断该条数据在数据库中是否存在
            boolean isExists = this.adminRoleService.isExistsRecord(id);

            // 没有添加权限 同时 也没能检索出数据的时候异常
            if (!isExists) {
                Subject currentUser = SecurityUtils.getSubject();
                currentUser.checkPermission(AdminRoleDefine.PERMISSIONS_ADD);
            }

            // 根据主键检索数据
            AdminRole record = this.adminRoleService.getRecord(id);
            BeanUtils.copyProperties(record, bean);
        }

        modelAndView.addObject(AdminRoleDefine.ROLE_FORM, bean);
        LogUtil.endLog(THIS_CLASS, AdminRoleDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     * 添加权限维护信息
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = AdminRoleDefine.INSERT_ACTION, method = RequestMethod.POST)
    @Token(check = true, forward = AdminRoleDefine.TOKEN_INIT_PATH)
    @RequiresPermissions(value = { AdminRoleDefine.PERMISSIONS_ADD })
    public ModelAndView insertAction(HttpServletRequest request, AdminRoleBean form) {
        LogUtil.startLog(THIS_CLASS, AdminRoleDefine.INSERT_ACTION);
        ModelAndView modelAndView = new ModelAndView(AdminRoleDefine.INFO_PATH);
        // 画面验证
        this.validatorFieldCheck(modelAndView, form);

        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
            modelAndView.addObject(AdminRoleDefine.ROLE_FORM, form);

            LogUtil.errorLog(THIS_CLASS, AdminRoleDefine.INSERT_ACTION, "输入内容验证失败", null);
            return modelAndView;
        }

        AdminRole adminRole = new AdminRole();
        try {
            BeanUtils.copyProperties(form, adminRole);
        } catch (Exception e) {
            LogUtil.errorLog(THIS_CLASS, AdminRoleDefine.INSERT_ACTION, e);
        }
        this.adminRoleService.insertRecord(adminRole);
        // 创建分页
        this.createPage(request, modelAndView, form);

        modelAndView.addObject(AdminRoleDefine.SUCCESS, AdminRoleDefine.SUCCESS);
        LogUtil.endLog(THIS_CLASS, AdminRoleDefine.INSERT_ACTION);
        return modelAndView;
    }

    /**
     * 修改权限维护信息
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AdminRoleDefine.UPDATE_ACTION)
    @Token(check = true, forward = AdminRoleDefine.TOKEN_INIT_PATH)
    @RequiresPermissions(value = { AdminRoleDefine.PERMISSIONS_MODIFY })
    public ModelAndView updateAction(HttpServletRequest request, AdminRoleBean form) {
        LogUtil.startLog(THIS_CLASS, AdminRoleDefine.UPDATE_ACTION);
        ModelAndView modelAndView = new ModelAndView(AdminRoleDefine.INFO_PATH);
        // 画面验证
        this.validatorFieldCheck(modelAndView, form);

        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
            modelAndView.addObject(AdminRoleDefine.ROLE_FORM, form);

            LogUtil.errorLog(THIS_CLASS, AdminRoleDefine.UPDATE_ACTION, "输入内容验证失败", null);
            return modelAndView;
        }

        // 维护角色是当前用户角色时,不能禁用
        if (form.getId() == ShiroUtil.getLoginUserRoleId() && String.valueOf(CustomConstants.FLAG_STATUS_DISABLE).equals(form.getState())) {
            modelAndView.addObject(AdminRoleDefine.ROLE_FORM, form);
            modelAndView.addObject("state_error", "该角色正在被当前用户使用中,不能禁用,请重新操作!");
            return modelAndView;
        }

        AdminRole adminRole = new AdminRole();
        try {
            BeanUtils.copyProperties(form, adminRole);
        } catch (Exception e) {
            LogUtil.errorLog(THIS_CLASS, AdminRoleDefine.UPDATE_ACTION, e);
        }
        this.adminRoleService.updateRecord(adminRole);
        // 创建分页
        this.createPage(request, modelAndView, form);

        modelAndView.addObject(AdminRoleDefine.SUCCESS, AdminRoleDefine.SUCCESS);
        LogUtil.endLog(THIS_CLASS, AdminRoleDefine.UPDATE_ACTION);
        return modelAndView;
    }

    /**
     * 画面校验
     * 
     * @param modelAndView
     * @param form
     */
    private void validatorFieldCheck(ModelAndView modelAndView, AdminRoleBean form) {
        // 角色名字(必须,最大长度)
        ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "roleName", form.getRoleName(), 20, true);
        // 排序(最大长度)
        ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "sort", String.valueOf(form.getSort()), 5, false);
        // 角色状态(必须)
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "state", form.getState());

        if (Validator.isNull(form.getId()) && !ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
            // 检查角色名字唯一性
            int cnt = adminRoleService.countRoleByname(form.getId(), form.getRoleName());
            if (cnt > 0) {
                ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "roleName", "repeat");
            }
        }
    }

    /**
     * 删除权限维护
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AdminRoleDefine.DELETE_ACTION)
    @RequiresPermissions(AdminRoleDefine.PERMISSIONS_DELETE)
    public ModelAndView deleteRecordAction(HttpServletRequest request, RedirectAttributes attr, AdminRoleBean form) {
        LogUtil.startLog(THIS_CLASS, AdminRoleDefine.DELETE_ACTION);
        ModelAndView modelAndView = new ModelAndView(AdminRoleDefine.LIST_PATH);

        List<Integer> recordList = JSONArray.parseArray(form.getIds(), Integer.class);

        // 维护角色是当前用户角色时
        if (recordList.contains(ShiroUtil.getLoginUserRoleId())) {
            attr.addFlashAttribute("delete_error", "该角色正在被当前用户使用中,不能删除,请重新操作!");
            modelAndView.setViewName("redirect:" + AdminRoleDefine.REQUEST_MAPPING + "/" + AdminRoleDefine.INIT);
            return modelAndView;
        }
        // 删除角色
        this.adminRoleService.deleteRecord(recordList);
        
        LogUtil.endLog(THIS_CLASS, AdminRoleDefine.DELETE_ACTION);
        modelAndView.setViewName("redirect:" + AdminRoleDefine.REQUEST_MAPPING + "/" + AdminRoleDefine.INIT);
        return modelAndView;
    }

    /**
     * 迁移到授权画面
     * 
     * @param request
     * @param form
     * @return
     * @throws Exception
     */
    @RequestMapping(value = AdminRoleDefine.AUTH_ACTION)
    @Token(save = true)
    @RequiresPermissions(value = { AdminRoleDefine.PERMISSIONS_AUTH }, logical = Logical.OR)
    public ModelAndView moveToAuthAction(HttpServletRequest request, AdminRoleBean form) throws Exception {
        LogUtil.startLog(THIS_CLASS, AdminRoleDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(AdminRoleDefine.AUTH_PATH);
        String ids = form.getIds();
        AdminRoleBean bean = new AdminRoleBean();

        if (Validator.isNotNull(ids) && NumberUtils.isNumber(ids)) {
            Integer id = Integer.valueOf(ids);
            bean.setId(id);
        } else {
            throw new Exception("角色不存在");
        }

        modelAndView.addObject(AdminRoleDefine.ROLE_FORM, bean);
        LogUtil.endLog(THIS_CLASS, AdminRoleDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     * 菜单管理画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @ResponseBody
    @RequestMapping(AdminRoleDefine.MENU_INFO_ACTION + "/{roleId}")
    @RequiresPermissions(AdminRoleDefine.PERMISSIONS_AUTH)
    public String getAdminRoleMenu(@PathVariable String roleId) {
        LogUtil.startLog(THIS_CLASS, AdminRoleDefine.MENU_INFO_ACTION);

        AdminRoleCustomize adminRoleCustomize = new AdminRoleCustomize();
        if (Validator.isNotNull(roleId)) {
            // 角色ID
            adminRoleCustomize.setRoleId(roleId);
        }
        // 取得角色菜单权限表数据
        JSONArray ja = this.adminRoleService.getAdminRoleMenu(adminRoleCustomize);
        if (ja != null) {
            return ja.toString();
        }

        LogUtil.endLog(THIS_CLASS, AdminRoleDefine.MENU_INFO_ACTION);
        return null;
    }

    /**
     * 插入或更新[角色菜单权限表]数据
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AdminRoleDefine.MODIFY_PERMISSION_ACTION, method = RequestMethod.POST)
    @RequiresPermissions(value = { AdminRoleDefine.PERMISSIONS_AUTH })
    public String modifyPermissionAction(@RequestBody AdminRoleBean bean) {
        LogUtil.startLog(THIS_CLASS, AdminRoleDefine.MODIFY_PERMISSION_ACTION);

        JSONObject ret = new JSONObject();
        int cnt = -1;
        try {
            Integer roleId = GetterUtil.getInteger(bean.getRoleId());
            List<AdminRoleMenuPermissions> list = bean.getPermList();
            // 维护角色是当前用户角色时,不能禁用
            if (roleId == ShiroUtil.getLoginUserRoleId() && (list == null || list.size() == 0)) {
                ret.put(ReturncashDefine.JSON_STATUS_KEY, ReturncashDefine.JSON_STATUS_NG);
                ret.put(ReturncashDefine.JSON_RESULT_KEY, "该角色正在被当前用户使用中,不能删除所有的权限,请重新操作!");
                return ret.toString();
            }
            
            if (Validator.isNotNull(roleId)) {
                cnt = this.adminRoleService.updatePermission(roleId, list);
            }
        } catch (Exception e) {
            LogUtil.errorLog(THIS_CLASS, AdminRoleDefine.MODIFY_PERMISSION_ACTION, e);
        }
        
        // 操作成功
        if(cnt > -1) {
            ret.put(ReturncashDefine.JSON_STATUS_KEY, ReturncashDefine.JSON_STATUS_OK);
            ret.put(ReturncashDefine.JSON_RESULT_KEY, "角色权限修改成功!");
        } else {
            ret.put(ReturncashDefine.JSON_STATUS_KEY, ReturncashDefine.JSON_STATUS_NG);
            ret.put(ReturncashDefine.JSON_RESULT_KEY, "角色权限修改时发生错误,请重新操作!");
        }

        LogUtil.endLog(THIS_CLASS, AdminRoleDefine.MODIFY_PERMISSION_ACTION);
        return ret.toString();
    }

    /**
     * 检查角色名称唯一性
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AdminRoleDefine.CHECK_ACTION, method = RequestMethod.POST)
    @RequiresPermissions(value = { AdminRoleDefine.PERMISSIONS_ADD, AdminRoleDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
    public String checkAction(HttpServletRequest request) {
        LogUtil.startLog(THIS_CLASS, AdminRoleDefine.CHECK_ACTION);

        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String param = request.getParameter("param");

        JSONObject ret = new JSONObject();
        // 检查角色名称唯一性
        if ("roleName".equals(name)) {
            int cnt = adminRoleService.countRoleByname(GetterUtil.getInteger(id), param);
            if (cnt > 0) {
                String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
                message = message.replace("{label}", "角色名称");
                ret.put(AdminRoleDefine.JSON_VALID_INFO_KEY, message);
            }
        }
        // 没有错误时,返回y
        if (!ret.containsKey(AdminRoleDefine.JSON_VALID_INFO_KEY)) {
            ret.put(AdminRoleDefine.JSON_VALID_STATUS_KEY, AdminRoleDefine.JSON_VALID_STATUS_OK);
        }

        LogUtil.endLog(THIS_CLASS, AdminRoleDefine.CHECK_ACTION);
        return ret.toString();
    }
}
