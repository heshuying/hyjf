package com.hyjf.admin.manager.config.sitesetting;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.SiteSetting;

/**
 * 网站邮件设置页面
 * 
 * @author qingbing
 *
 */
@Controller
@RequestMapping(value = EmailSettingDefine.REQUEST_MAPPING)
public class EmailSettingController extends BaseController {

	@Autowired
	private SiteSettingService siteSettingService;

	/**
	 * 网站邮件设置画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(EmailSettingDefine.INIT)
	@RequiresPermissions(EmailSettingDefine.PERMISSIONS_VIEW)
	public ModelAndView init() {
		// 日志开始
		LogUtil.startLog(EmailSettingController.class.toString(), EmailSettingDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(EmailSettingDefine.LIST_PATH);

		List<SiteSetting> recordList = this.siteSettingService.getRecordList(new SiteSetting(), -1, -1);
		if (recordList != null) {
			SiteSetting siteSetting = recordList.get(0);
			modelAndView.addObject(EmailSettingDefine.SITESETTING_FORM, siteSetting);
			return modelAndView;
		}
		// 日志结束
		LogUtil.endLog(EmailSettingController.class.toString(), EmailSettingDefine.INIT);
		return modelAndView;
	}

	/**
	 * 数据添加
	 * 
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(EmailSettingDefine.INSERT_ACTION)
	@RequiresPermissions(EmailSettingDefine.PERMISSIONS_ADD)
	public ModelAndView add(SiteSetting form) {
		// 日志开始
		LogUtil.startLog(EmailSettingController.class.toString(), EmailSettingDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(EmailSettingDefine.RE_LIST_PATH);
		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 成功插入
		siteSettingService.insertRecord(form);
		List<SiteSetting> recordList = this.siteSettingService.getRecordList(new SiteSetting(), -1, -1);
		if (recordList != null) {
			modelAndView.addObject(EmailSettingDefine.SITESETTING_FORM, recordList);
		}
		// 日志结束
		LogUtil.endLog(EmailSettingController.class.toString(), EmailSettingDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 数据修改
	 * 
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(EmailSettingDefine.UPDATE_ACTION)
	@RequiresPermissions(EmailSettingDefine.PERMISSIONS_MODIFY)
	public ModelAndView update(SiteSetting form) {
		// 日志开始
		LogUtil.startLog(EmailSettingController.class.toString(), EmailSettingDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(EmailSettingDefine.RE_LIST_PATH);
		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 成功修改
		siteSettingService.updateRecord(form);
		List<SiteSetting> recordList = this.siteSettingService.getRecordList(new SiteSetting(), -1, -1);
		if (recordList != null) {
			modelAndView.addObject(EmailSettingDefine.SITESETTING_FORM, recordList);
		}
		// 日志结束
		LogUtil.endLog(EmailSettingController.class.toString(), EmailSettingDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, SiteSetting form) {
		// 字段校验公司
		if (form.getCompany() != null
				&& !ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "company", form.getCompany(), 100, true)) {
			return modelAndView;
		}
		// 网站标题
		if (form.getSiteName() != null
				&& !ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "code", form.getSiteName(), 50, true)) {
			return modelAndView;
		}
		// 网站描述
		if (form.getSiteDescription() != null && !ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "value",
				form.getSiteDescription(), 200, true)) {
			return modelAndView;
		}
		// 网站关键字
		if (form.getSiteKeyword() != null
				&& !ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "unit", form.getSiteKeyword(), 200, true)) {
			return modelAndView;
		}
		// 网站备案
		if (form.getSiteIcp() != null
				&& !ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "unit", form.getSiteIcp(), 50, true)) {
			return modelAndView;
		}
		// 网站模板路径
		if (form.getSiteThemePath() != null && !ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "unit",
				form.getSiteThemePath(), 100, true)) {
			return modelAndView;
		}
		// 网站声明
		if (form.getSiteStats() != null
				&& !ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "unit", form.getSiteStats(), 200, true)) {
			return modelAndView;
		}
		return null;
	}
}
