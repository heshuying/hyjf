package com.hyjf.admin.promotion.utmadmin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.maintenance.admin.AdminDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.AdminUtmReadPermissions;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.admin.AdminUtmReadPermissionsCustomize;

/**
 * 渠道账户管理
 * @author Michael
 */
@Controller
@RequestMapping(value = AdminUtmReadPermissionsDefine.REQUEST_MAPPING)
public class AdminUtmReadPermissionsController extends BaseController {

	@Autowired
	private AdminUtmReadPermissionsService adminUtmReadPermissionsService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AdminUtmReadPermissionsDefine.INIT)
	@RequiresPermissions(AdminUtmReadPermissionsDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(AdminUtmReadPermissionsDefine.FORM) AdminUtmReadPermissionsBean form) {
		LogUtil.startLog(AdminUtmReadPermissionsController.class.toString(), AdminUtmReadPermissionsDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(AdminUtmReadPermissionsDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(AdminUtmReadPermissionsController.class.toString(), AdminUtmReadPermissionsDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AdminUtmReadPermissionsDefine.SEARCH_ACTION)
	@RequiresPermissions(AdminUtmReadPermissionsDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, AdminUtmReadPermissionsBean form) {
		LogUtil.startLog(AdminUtmReadPermissionsController.class.toString(), AdminUtmReadPermissionsDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(AdminUtmReadPermissionsDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(AdminUtmReadPermissionsController.class.toString(), AdminUtmReadPermissionsDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, AdminUtmReadPermissionsBean form) {

		AdminUtmReadPermissionsCustomize adminUtmReadPermissionsCustomize = new AdminUtmReadPermissionsCustomize();
		if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
			adminUtmReadPermissionsCustomize.setTimeStartSrch(GetDate.getDayStart(form.getTimeStartSrch()));
		}
		if(StringUtils.isNotEmpty(form.getTimeEndSrch())){
			adminUtmReadPermissionsCustomize.setTimeEndSrch(GetDate.getDayEnd(form.getTimeEndSrch()));
		}
		if(StringUtils.isNotEmpty(form.getAdminTrueNameSrch())){
			adminUtmReadPermissionsCustomize.setAdminTrueName(form.getAdminTrueNameSrch());
		}
		if(StringUtils.isNotEmpty(form.getKeyCodeSrch())){
			adminUtmReadPermissionsCustomize.setKeyCode(form.getKeyCodeSrch());
		}
		if(StringUtils.isNotEmpty(form.getAdminUserNameSrch())){
			adminUtmReadPermissionsCustomize.setAdminUserName(form.getAdminUserNameSrch());
		}
		Integer count = this.adminUtmReadPermissionsService.countAdminUtmReadPermissionsRecord(adminUtmReadPermissionsCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			adminUtmReadPermissionsCustomize.setLimitStart(paginator.getOffset());
			adminUtmReadPermissionsCustomize.setLimitEnd(paginator.getLimit());
			List<AdminUtmReadPermissionsCustomize> recordList = this.adminUtmReadPermissionsService.selectAdminUtmReadPermissionsRecord(adminUtmReadPermissionsCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		prepareDatas(modelAndView);
		modelAndView.addObject(AdminUtmReadPermissionsDefine.FORM, form);
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AdminUtmReadPermissionsDefine.INFO_ACTION)
	@RequiresPermissions(value = { AdminUtmReadPermissionsDefine.PERMISSIONS_ADD, AdminUtmReadPermissionsDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView infoAction(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(AdminUtmReadPermissionsDefine.FORM) AdminUtmReadPermissionsBean form) {
		LogUtil.startLog(AdminUtmReadPermissionsController.class.toString(), AdminUtmReadPermissionsDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(AdminUtmReadPermissionsDefine.INFO_PATH);
		prepareDatas(modelAndView);
		if (StringUtils.isNotEmpty(form.getId())) {
			AdminUtmReadPermissions record = adminUtmReadPermissionsService.getRecord(Integer.valueOf(form.getId()));
			modelAndView.addObject(AdminUtmReadPermissionsDefine.FORM, record);
			return modelAndView;
		}
		LogUtil.endLog(AdminUtmReadPermissionsController.class.toString(), AdminUtmReadPermissionsDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 添加信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = AdminUtmReadPermissionsDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(AdminUtmReadPermissionsDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, AdminUtmReadPermissionsBean form) {
		LogUtil.startLog(AdminUtmReadPermissionsController.class.toString(), AdminUtmReadPermissionsDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(AdminUtmReadPermissionsDefine.INFO_PATH);

		if (StringUtils.isNotEmpty(form.getId())) {
			AdminUtmReadPermissions record = adminUtmReadPermissionsService.getRecord(Integer.valueOf(form.getId()));
			if (record != null) {
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "adminUserName", "repeat");
			}
		}
		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject(AdminUtmReadPermissionsDefine.FORM, form);
			return modelAndView;
		}
		// 数据插入
		this.adminUtmReadPermissionsService.insertRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(AdminUtmReadPermissionsDefine.SUCCESS, AdminUtmReadPermissionsDefine.SUCCESS);
		LogUtil.endLog(AdminUtmReadPermissionsController.class.toString(), AdminUtmReadPermissionsDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = AdminUtmReadPermissionsDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(AdminUtmReadPermissionsDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, AdminUtmReadPermissionsBean form) {
		LogUtil.startLog(AdminUtmReadPermissionsController.class.toString(), AdminUtmReadPermissionsDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(AdminUtmReadPermissionsDefine.INFO_PATH);
		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject(AdminUtmReadPermissionsDefine.FORM, form);
			return modelAndView;
		}
		// 更新
		this.adminUtmReadPermissionsService.updateRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(AdminUtmReadPermissionsDefine.SUCCESS, AdminUtmReadPermissionsDefine.SUCCESS);
		LogUtil.endLog(AdminUtmReadPermissionsController.class.toString(), AdminUtmReadPermissionsDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 准备枚举
	 * 
	 * @param modelAndView
	 */
	private void prepareDatas(ModelAndView modelAndView) {
		//渠道
		List<UtmPlat> utmPlatList = this.adminUtmReadPermissionsService.getUtmPlatList();
		modelAndView.addObject("utmPlatList", utmPlatList);
	}

	
	/**
	 * 画面校验
	 * 
	 * @param modelAndView
	 * @param form
	 */
	private void validatorFieldCheck(ModelAndView modelAndView, AdminUtmReadPermissionsBean form) {
		// 用户名
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "adminUserName", form.getAdminUserName());
		// 关键字
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "keyCode", form.getKeyCode(), 20, true);
		// 渠道
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "utmIds", form.getUtmIds(), 200, true);
	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AdminUtmReadPermissionsDefine.DELETE_ACTION)
	@RequiresPermissions(AdminUtmReadPermissionsDefine.PERMISSIONS_DELETE)
	public String deleteRecordAction(HttpServletRequest request, RedirectAttributes attr, AdminUtmReadPermissionsBean form) {
		LogUtil.startLog(AdminUtmReadPermissionsController.class.toString(), AdminUtmReadPermissionsDefine.DELETE_ACTION);
		if(StringUtils.isNotEmpty(form.getId())){
			this.adminUtmReadPermissionsService.deleteRecord(Integer.valueOf(form.getId()));
		}
		attr.addFlashAttribute(AdminUtmReadPermissionsDefine.FORM, form);
		LogUtil.endLog(AdminUtmReadPermissionsController.class.toString(), AdminUtmReadPermissionsDefine.DELETE_ACTION);
		return "redirect:" + AdminUtmReadPermissionsDefine.REQUEST_MAPPING + "/" + AdminUtmReadPermissionsDefine.INIT;
	}

	/**
	 * 检查编号唯一性
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AdminUtmReadPermissionsDefine.CHECK_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = { AdminUtmReadPermissionsDefine.PERMISSIONS_ADD, AdminUtmReadPermissionsDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String checkAction(HttpServletRequest request) {
		LogUtil.startLog(AdminUtmReadPermissionsController.class.toString(), AdminUtmReadPermissionsDefine.CHECK_ACTION);

		String userName = request.getParameter("param");
		JSONObject ret = new JSONObject();
		//返回消息
		String message = "";
		int flag = adminUtmReadPermissionsService.isExistsAdminUser(userName);
		if (flag == 1) {
			message = ValidatorFieldCheckUtil.getErrorMessage("admin.username.not.exists", "");
			message = message.replace("{label}", "用户名");
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		}else if(flag == 2){
			message = ValidatorFieldCheckUtil.getErrorMessage("admin.username.not.use","");
			message = message.replace("{label}", "用户名");
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		}else if(flag == 3){
			message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
			message = message.replace("{label}", "用户名");
			ret.put(AdminUtmReadPermissionsDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		}
		// 没有错误时,返回y
		if (!ret.containsKey(AdminUtmReadPermissionsDefine.JSON_VALID_INFO_KEY)) {
			ret.put(AdminUtmReadPermissionsDefine.JSON_VALID_STATUS_KEY, AdminUtmReadPermissionsDefine.JSON_VALID_STATUS_OK);
		}
		LogUtil.endLog(AdminUtmReadPermissionsController.class.toString(), AdminUtmReadPermissionsDefine.CHECK_ACTION);
		return ret.toString();
	}
	
}
