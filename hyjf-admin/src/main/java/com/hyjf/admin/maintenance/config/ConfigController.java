/**
 * Description:配置管理
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:01:57
 * Modification History:
 * Modified by : 
 * */
package com.hyjf.admin.maintenance.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
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
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.Config;

@Controller
@RequestMapping(value = ConfigDefine.REQUEST_MAPPING)
public class ConfigController extends BaseController {

	@Autowired
	private ConfigService configService;

	/**
	 * 配置维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ConfigDefine.INIT)
	// @RequiresPermissions(ConfigDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(ConfigDefine.CONFIGFORM_FORM) ConfigBean form) {
		LogUtil.startLog(ConfigController.class.toString(), ConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ConfigDefine.LIST_PATH);

		System.out.println(form.getId());

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ConfigController.class.toString(), ConfigDefine.INIT);
		return modelAndView;
	}

	/**
	 * 配置维护分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ConfigBean form) {
		List<Config> recordList = this.configService.getRecordList(new Config(), -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.configService.getRecordList(new Config(), paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(ConfigDefine.CONFIGFORM_FORM, form);
		}
	}

	/**
	 * 配置详细画面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ConfigDefine.INFO_ACTION)
	@Token(save = true)
	@RequiresPermissions(value = { ConfigDefine.PERMISSIONS_ADD, ConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView moveToInfoAction(HttpServletRequest request, ConfigBean form) {
		LogUtil.startLog(ConfigController.class.toString(), ConfigDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(ConfigDefine.INFO_PATH);
		Config record = new Config();
		record.setId(form.getId());
		if (Validator.isNotNull(record.getId())) {
			// 根据主键判断该条数据在数据库中是否存在
			boolean isExists = this.configService.isExistsRecord(record);

			// 没有添加权限 同时 也没能检索出数据的时候异常
			if (!isExists) {
				Subject currentUser = SecurityUtils.getSubject();
				currentUser.checkPermission(ConfigDefine.PERMISSIONS_ADD);
			}

			// 根据主键检索数据
			record = this.configService.getRecord(record);
		}

		modelAndView.addObject(ConfigDefine.CONFIGFORM_FORM, record);
		LogUtil.endLog(ConfigController.class.toString(), ConfigDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 添加配置维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ConfigDefine.INSERT_ACTION, method = RequestMethod.POST)
	@Token(check = true, forward = ConfigDefine.TOKEN_INIT_PATH)
	@RequiresPermissions(value = { ConfigDefine.PERMISSIONS_ADD })
	public ModelAndView insertAction(HttpServletRequest request, ConfigBean form) {
		LogUtil.startLog(ConfigController.class.toString(), ConfigDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(ConfigDefine.LIST_PATH);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.setViewName(ConfigDefine.INFO_PATH);
			modelAndView.addObject(ConfigDefine.CONFIGFORM_FORM, form);
			return modelAndView;
		}

		Config config = new Config();
		config.setRemark(form.getRemark());

		if (Validator.isNotNull(form.getId())) {
			this.configService.insertRecord(config);
		}

		LogUtil.endLog(ConfigController.class.toString(), ConfigDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改配置维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ConfigDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@Token(check = true, forward = ConfigDefine.TOKEN_INIT_PATH)
	@RequiresPermissions(value = { ConfigDefine.PERMISSIONS_MODIFY })
	public ModelAndView updateAction(HttpServletRequest request, ConfigBean form) {
		LogUtil.startLog(ConfigController.class.toString(), ConfigDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(ConfigDefine.INFO_PATH);

		Subject currentUser = SecurityUtils.getSubject();
		currentUser.checkPermission(ConfigDefine.PERMISSIONS_MODIFY);

		if (Validator.isNotNull(form.getId())) {
			Config permission = new Config();

			this.configService.updateRecord(permission);
		}

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ConfigController.class.toString(), ConfigDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 删除配置信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ConfigDefine.DELETE_ACTION)
	@RequiresPermissions(ConfigDefine.PERMISSIONS_DELETE)
	public String deleteRecordAction(HttpServletRequest request, RedirectAttributes attr, ConfigBean form) {
		LogUtil.startLog(ConfigController.class.toString(), ConfigDefine.DELETE_ACTION);
		List<String> recordList = JSONArray.parseArray(form.getId().toString(), String.class);
		this.configService.deleteRecord(recordList);
		attr.addFlashAttribute(ConfigDefine.CONFIGFORM_FORM, form);
		LogUtil.endLog(ConfigController.class.toString(), ConfigDefine.DELETE_ACTION);
		return "redirect:" + ConfigDefine.REQUEST_MAPPING + "/" + ConfigDefine.INIT;
	}
}
