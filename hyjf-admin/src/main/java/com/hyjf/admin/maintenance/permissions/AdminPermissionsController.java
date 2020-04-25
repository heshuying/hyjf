package com.hyjf.admin.maintenance.permissions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.annotate.Token;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.AdminPermissions;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = AdminPermissionsDefine.REQUEST_MAPPING)
public class AdminPermissionsController extends BaseController {
	/**
	 * 类名
	 */
	private static final String THIS_CLASS = AdminPermissionsController.class.getName();

	@Autowired
	private AdminPermissionsService adminPermissionsService;

	/**
	 * 权限维护画面初始化
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AdminPermissionsDefine.INIT)
	@RequiresPermissions(AdminPermissionsDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(AdminPermissionsDefine.PERMISSIONS_FORM) AdminPermissionsBean form) {
		LogUtil.startLog(THIS_CLASS, AdminPermissionsDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(AdminPermissionsDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, AdminPermissionsDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面查询
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AdminPermissionsDefine.SEARCH_ACTION)
	@RequiresPermissions(AdminPermissionsDefine.PERMISSIONS_VIEW)
	public ModelAndView search(HttpServletRequest request, AdminPermissionsBean form) {
		LogUtil.startLog(THIS_CLASS, AdminPermissionsDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(AdminPermissionsDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, AdminPermissionsDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建权限维护分页机能
	 *
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, AdminPermissionsBean form) {
		List<AdminPermissions> recordList = this.adminPermissionsService.getRecordList(form, -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.adminPermissionsService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(AdminPermissionsDefine.PERMISSIONS_FORM, form);
		}
	}

	/**
	 * 迁移到权限维护详细画面
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = AdminPermissionsDefine.INFO_ACTION)
	@Token(save = true)
	@RequiresPermissions(value = { AdminPermissionsDefine.PERMISSIONS_ADD, AdminPermissionsDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView moveToInfoAction(HttpServletRequest request, AdminPermissionsBean form) {
		LogUtil.startLog(THIS_CLASS, AdminPermissionsDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(AdminPermissionsDefine.INFO_PATH);
		AdminPermissions record = new AdminPermissions();
		record.setPermissionUuid(form.getPermissionUuid());
		if (StringUtils.isNotEmpty(record.getPermissionUuid())) {
			// 根据主键判断该条数据在数据库中是否存在
			boolean isExists = this.adminPermissionsService.isExistsRecord(record);

			// 没有添加权限 同时 也没能检索出数据的时候异常
			if (!isExists) {
				Subject currentUser = SecurityUtils.getSubject();
				currentUser.checkPermission(AdminPermissionsDefine.PERMISSIONS_ADD);
			}

			// 根据主键检索数据
			record = this.adminPermissionsService.getRecord(record);
		}

		modelAndView.addObject(AdminPermissionsDefine.PERMISSIONS_FORM, record);
		LogUtil.endLog(THIS_CLASS, AdminPermissionsDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 添加权限维护信息
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = AdminPermissionsDefine.INSERT_ACTION, method = RequestMethod.POST)
	@Token(check = true, forward = AdminPermissionsDefine.TOKEN_INIT_PATH)
	@RequiresPermissions(value = { AdminPermissionsDefine.PERMISSIONS_ADD })
	public ModelAndView insertAction(HttpServletRequest request, AdminPermissionsBean form) {
		LogUtil.startLog(THIS_CLASS, AdminPermissionsDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(AdminPermissionsDefine.INFO_PATH);
		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		// 权限功能按钮是否重复
		if (this.adminPermissionsService.isExistsPermission(form)) {
			String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "权限");
			ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "permission", form.getPermission(), message);
		}

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.setViewName(AdminPermissionsDefine.INFO_PATH);
			modelAndView.addObject(AdminPermissionsDefine.PERMISSIONS_FORM, form);
			return modelAndView;
		}

		if (StringUtils.isEmpty(form.getPermissionUuid())) {
			AdminPermissions permission = new AdminPermissions();
			BeanUtils.copyProperties(form, permission);
			this.adminPermissionsService.insertRecord(permission);

			// 更新权限
			ShiroUtil.updateAuth();
		}

		this.createPage(request, modelAndView, form);

		modelAndView.addObject(AdminPermissionsDefine.SUCCESS, AdminPermissionsDefine.SUCCESS);
		LogUtil.endLog(THIS_CLASS, AdminPermissionsDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改权限维护信息
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = AdminPermissionsDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@Token(check = true, forward = AdminPermissionsDefine.TOKEN_INIT_PATH)
	@RequiresPermissions(value = { AdminPermissionsDefine.PERMISSIONS_MODIFY })
	public ModelAndView updateAction(HttpServletRequest request, AdminPermissionsBean form) {
		LogUtil.startLog(THIS_CLASS, AdminPermissionsDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(AdminPermissionsDefine.INFO_PATH);

		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.setViewName(AdminPermissionsDefine.INFO_PATH);
			modelAndView.addObject(AdminPermissionsDefine.PERMISSIONS_FORM, form);
			return modelAndView;
		}

		if (StringUtils.isNotEmpty(form.getPermissionUuid())) {
			AdminPermissions permission = new AdminPermissions();
			BeanUtils.copyProperties(form, permission);
			this.adminPermissionsService.updateRecord(permission);

			// 更新权限
			ShiroUtil.updateAuth();
		}

		// 创建分页
		this.createPage(request, modelAndView, form);

		modelAndView.addObject(AdminPermissionsDefine.SUCCESS, AdminPermissionsDefine.SUCCESS);
		LogUtil.endLog(THIS_CLASS, AdminPermissionsDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 画面校验
	 *
	 * @param modelAndView
	 * @param form
	 */
	private void validatorFieldCheck(ModelAndView modelAndView, AdminPermissionsBean form) {
		// 权限检查用字段的校验
		ValidatorFieldCheckUtil.validateAlphaAndMaxLength(modelAndView, "permission", form.getPermission(), 20, true);
		// 权限名字
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "permissionName", form.getPermissionName(), 20, true);
		// 说明
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "description", form.getDescription(), 255, false);
	}

	/**
	 * 删除权限维护
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AdminPermissionsDefine.DELETE_ACTION)
	@RequiresPermissions(AdminPermissionsDefine.PERMISSIONS_DELETE)
	public String deleteRecordAction(HttpServletRequest request, RedirectAttributes attr, AdminPermissionsBean form) {
		LogUtil.startLog(THIS_CLASS, AdminPermissionsDefine.DELETE_ACTION);
		List<String> recordList = JSONArray.parseArray(form.getPermissionUuid(), String.class);
		this.adminPermissionsService.deleteRecord(recordList);

		// 更新权限
		ShiroUtil.updateAuth();

		attr.addFlashAttribute(AdminPermissionsDefine.PERMISSIONS_FORM, form);
		LogUtil.endLog(THIS_CLASS, AdminPermissionsDefine.DELETE_ACTION);
		return "redirect:" + AdminPermissionsDefine.REQUEST_MAPPING + "/" + AdminPermissionsDefine.INIT;
	}
}
