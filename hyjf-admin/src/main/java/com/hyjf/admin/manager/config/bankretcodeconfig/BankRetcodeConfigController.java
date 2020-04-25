package com.hyjf.admin.manager.config.bankretcodeconfig;

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

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BankReturnCodeConfig;

/**
 * 返回码配置
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = BankRetcodeConfigDefine.REQUEST_MAPPING)
public class BankRetcodeConfigController extends BaseController {
	/**
	 * 类名
	 */
	private static final String THIS_CLASS = BankRetcodeConfigController.class.getName();

	@Autowired
	private BankRetcodeConfigService bankRetcodeConfigService;

	/**
	 * 返回码维护画面初始化
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankRetcodeConfigDefine.INIT)
	@RequiresPermissions(BankRetcodeConfigDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(BankRetcodeConfigDefine.CONFIGBANK_FORM) BankRetcodeConfigBean form) {
		LogUtil.startLog(THIS_CLASS, BankRetcodeConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BankRetcodeConfigDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, BankRetcodeConfigDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面查询
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankRetcodeConfigDefine.SEARCH_ACTION)
	@RequiresPermissions(BankRetcodeConfigDefine.PERMISSIONS_VIEW)
	public ModelAndView search(HttpServletRequest request, BankRetcodeConfigBean form) {
		LogUtil.startLog(THIS_CLASS, BankRetcodeConfigDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(BankRetcodeConfigDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, BankRetcodeConfigDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建返回码维护分页机能
	 *
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BankRetcodeConfigBean form) {
		// 获取列表总数
		int count = this.bankRetcodeConfigService.countRecord(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			List<BankReturnCodeConfig> recordList = this.bankRetcodeConfigService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(BankRetcodeConfigDefine.CONFIGBANK_FORM, form);
		}
	}

	/**
	 * 迁移到返回码维护详细画面
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = BankRetcodeConfigDefine.INFO_ACTION)
	@RequiresPermissions(value = { BankRetcodeConfigDefine.PERMISSIONS_ADD, BankRetcodeConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView moveToInfoAction(HttpServletRequest request, BankRetcodeConfigBean form) {
		LogUtil.startLog(THIS_CLASS, BankRetcodeConfigDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(BankRetcodeConfigDefine.INFO_PATH);
		BankReturnCodeConfig record = new BankReturnCodeConfig();
		record.setId(form.getId());
		if (record.getId() != null) {
			// 根据条件判断该条数据在数据库中是否存在
			boolean isExists = this.bankRetcodeConfigService.isExistsRecord(record);

			// 没有添加权限 同时 也没能检索出数据的时候异常
			if (!isExists) {
				Subject currentUser = SecurityUtils.getSubject();
				currentUser.checkPermission(BankRetcodeConfigDefine.PERMISSIONS_ADD);
			}
			// 根据主键检索数据
			record = this.bankRetcodeConfigService.getRecord(record);
		}

		modelAndView.addObject(BankRetcodeConfigDefine.CONFIGBANK_FORM, record);
		LogUtil.endLog(THIS_CLASS, BankRetcodeConfigDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 添加返回码维护信息
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = BankRetcodeConfigDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = { BankRetcodeConfigDefine.PERMISSIONS_ADD })
	public ModelAndView insertAction(HttpServletRequest request, BankRetcodeConfigBean form) {
		LogUtil.startLog(THIS_CLASS, BankRetcodeConfigDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(BankRetcodeConfigDefine.INFO_PATH);
		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		// 返回码功能按钮是否重复
		if (this.bankRetcodeConfigService.isExistsReturnCode(form)) {
			String message = ValidatorFieldCheckUtil.getErrorMessage("retCode.repeat", "返回码");
			ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "retCode", form.getRetCode(), message);
		}
		// 数据检索
		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.setViewName(BankRetcodeConfigDefine.INFO_PATH);
			modelAndView.addObject(BankRetcodeConfigDefine.CONFIGBANK_FORM, form);
			return modelAndView;
		}
		// 插入数据
		if (StringUtils.isNotEmpty(form.getRetCode())) {
			BankReturnCodeConfig returnCodeConfig = new BankReturnCodeConfig();
			BeanUtils.copyProperties(form, returnCodeConfig);
			this.bankRetcodeConfigService.insertRecord(returnCodeConfig);
		}
		// 创建分页
		this.createPage(request, modelAndView, form);

		modelAndView.addObject(BankRetcodeConfigDefine.SUCCESS, BankRetcodeConfigDefine.SUCCESS);
		LogUtil.endLog(THIS_CLASS, BankRetcodeConfigDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改权限维护信息
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = BankRetcodeConfigDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = { BankRetcodeConfigDefine.PERMISSIONS_MODIFY })
	public ModelAndView updateAction(HttpServletRequest request, BankRetcodeConfigBean form) {
		LogUtil.startLog(THIS_CLASS, BankRetcodeConfigDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(BankRetcodeConfigDefine.INFO_PATH);

		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.setViewName(BankRetcodeConfigDefine.INFO_PATH);
			modelAndView.addObject(BankRetcodeConfigDefine.CONFIGBANK_FORM, form);
			return modelAndView;
		}
		// 更新数据
		if (Validator.isNotNull(form.getRetCode())) {
			BankReturnCodeConfig returnCodeConfig = new BankReturnCodeConfig();
			BeanUtils.copyProperties(form, returnCodeConfig);
			this.bankRetcodeConfigService.updateRecord(returnCodeConfig);
		}

		// 创建分页
		this.createPage(request, modelAndView, form);

		modelAndView.addObject(BankRetcodeConfigDefine.SUCCESS, BankRetcodeConfigDefine.SUCCESS);
		LogUtil.endLog(THIS_CLASS, BankRetcodeConfigDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 画面校验
	 *
	 * @param modelAndView
	 * @param form
	 */
	private void validatorFieldCheck(ModelAndView modelAndView, BankRetcodeConfigBean form) {
		// 权限检查用字段的校验
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "txCode", form.getTxCode(), 20, true);
		// 权限名字
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "retCode", form.getRetCode(), 20, true);
		
	}

	/**
	 * 删除权限维护
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankRetcodeConfigDefine.DELETE_ACTION)
	@RequiresPermissions(BankRetcodeConfigDefine.PERMISSIONS_DELETE)
	public String deleteRecordAction(HttpServletRequest request, RedirectAttributes attr, BankRetcodeConfigBean form) {
		LogUtil.startLog(THIS_CLASS, BankRetcodeConfigDefine.DELETE_ACTION);
//		List<Integer> recordList = JSONArray.parseArray(form.getId(), String.class);
//		this.bankRetcodeConfigService.deleteRecord(recordList);

		attr.addFlashAttribute(BankRetcodeConfigDefine.CONFIGBANK_FORM, form);
		LogUtil.endLog(THIS_CLASS, BankRetcodeConfigDefine.DELETE_ACTION);
		return "redirect:" + BankRetcodeConfigDefine.REQUEST_MAPPING + "/" + BankRetcodeConfigDefine.INIT;
	}

}
