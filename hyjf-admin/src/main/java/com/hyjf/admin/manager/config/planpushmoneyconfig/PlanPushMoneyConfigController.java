package com.hyjf.admin.manager.config.planpushmoneyconfig;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.DebtCommissionConfig;

@Controller
@RequestMapping(PlanPushMoneyConfigDefine.REQUEST_MAPPING)
public class PlanPushMoneyConfigController extends BaseController {

	@Autowired
	private PlanPushMoneyConfigService planPushMoneyConfigService;
	/**
	 * 类名
	 */
	private static final String THIS_CLASS = PlanPushMoneyConfigController.class.toString();

	/**
	 * 权限维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanPushMoneyConfigDefine.INIT)
	@RequiresPermissions(PlanPushMoneyConfigDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, PlanPushMoneyConfigBean form) {
		LogUtil.startLog(THIS_CLASS, PlanPushMoneyConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(PlanPushMoneyConfigDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, PlanPushMoneyConfigDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面检索
	 * 
	 * @Title search
	 * @param request
	 * @param attr
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanPushMoneyConfigDefine.SEARCH_ACTION)
	@RequiresPermissions(PlanPushMoneyConfigDefine.PERMISSIONS_VIEW)
	public ModelAndView search(HttpServletRequest request, RedirectAttributes attr, PlanPushMoneyConfigBean form) {
		LogUtil.startLog(THIS_CLASS, PlanPushMoneyConfigDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanPushMoneyConfigDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, PlanPushMoneyConfigDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, PlanPushMoneyConfigBean form) {
		int count = planPushMoneyConfigService.countCommissionList(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			List<DebtCommissionConfig> recordList = this.planPushMoneyConfigService.selectCommissionList(form);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}
		modelAndView.addObject(PlanPushMoneyConfigDefine.PUSHMONEYCONFIG_FORM, form);
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanPushMoneyConfigDefine.INFO_ACTION)
	@RequiresPermissions(value = { PlanPushMoneyConfigDefine.PERMISSIONS_ADD, PlanPushMoneyConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, @ModelAttribute(PlanPushMoneyConfigDefine.PUSHMONEYCONFIG_FORM) PlanPushMoneyConfigBean form) {
		LogUtil.startLog(THIS_CLASS, PlanPushMoneyConfigDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanPushMoneyConfigDefine.INFO_PATH);
		PlanPushMoneyConfigBean planPushMoneyConfigBean = new PlanPushMoneyConfigBean();
		if (StringUtils.isNotEmpty(form.getIds())) {
			DebtCommissionConfig record = this.planPushMoneyConfigService.getDebtCommissionConfigById(form.getIds());
			BeanUtils.copyProperties(record, planPushMoneyConfigBean);
			planPushMoneyConfigBean.setIds(form.getIds());
		}
		modelAndView.addObject(PlanPushMoneyConfigDefine.PUSHMONEYCONFIG_FORM, planPushMoneyConfigBean);
		LogUtil.endLog(THIS_CLASS, PlanPushMoneyConfigDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 更新操作
	 * 
	 * @Title updateAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanPushMoneyConfigDefine.UPDATE_ACTION)
	@RequiresPermissions(value = PlanPushMoneyConfigDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, PlanPushMoneyConfigBean form) {
		LogUtil.startLog(THIS_CLASS, PlanPushMoneyConfigDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanPushMoneyConfigDefine.INFO_PATH);

		// 画面验证
		this.validatorFieldCheck(modelAndView, form, true);
		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject(PlanPushMoneyConfigDefine.PUSHMONEYCONFIG_FORM, form);
			return modelAndView;
		}

		this.planPushMoneyConfigService.updateRecord(form);
		modelAndView.addObject(PlanPushMoneyConfigDefine.SUCCESS, PlanPushMoneyConfigDefine.SUCCESS);
		LogUtil.endLog(THIS_CLASS, PlanPushMoneyConfigDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 插入操作
	 * 
	 * @Title insertAction
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PlanPushMoneyConfigDefine.INSERT_ACTION)
	@RequiresPermissions(PlanPushMoneyConfigDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, PlanPushMoneyConfigBean form) {
		LogUtil.startLog(THIS_CLASS, PlanPushMoneyConfigDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(PlanPushMoneyConfigDefine.INFO_PATH);

		// 画面验证
		this.validatorFieldCheck(modelAndView, form, false);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject(PlanPushMoneyConfigDefine.PUSHMONEYCONFIG_FORM, form);
			return modelAndView;
		}

		this.planPushMoneyConfigService.insertRecord(form);
		modelAndView.addObject(PlanPushMoneyConfigDefine.SUCCESS, PlanPushMoneyConfigDefine.SUCCESS);

		LogUtil.endLog(THIS_CLASS, PlanPushMoneyConfigDefine.INSERT_ACTION);
		return modelAndView;

	}

	/**
	 * 画面校验
	 * 
	 * @Title validatorFieldCheck
	 * @param modelAndView
	 * @param form
	 * @param isUpdate
	 */
	private void validatorFieldCheck(ModelAndView modelAndView, PlanPushMoneyConfigBean form, boolean isUpdate) {
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "rate", form.getRate());
	}

}
