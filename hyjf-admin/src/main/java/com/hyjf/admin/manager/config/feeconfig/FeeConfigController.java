package com.hyjf.admin.manager.config.feeconfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.config.bankconfig.BankConfigService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.FeeConfig;

/**
 * 手续费配置
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = FeeConfigDefine.REQUEST_MAPPING)
public class FeeConfigController extends BaseController {

	@Autowired
	private FeeConfigService feeConfigService;
	@Autowired
	private BankConfigService bankConfigService;

	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(FeeConfigDefine.INIT)
	@RequiresPermissions(FeeConfigDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(FeeConfigDefine.CONFIGFEE_FORM) FeeConfigBean form) {
		LogUtil.startLog(FeeConfigController.class.toString(), FeeConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(FeeConfigDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(FeeConfigController.class.toString(), FeeConfigDefine.INIT);
		return modelAndView;
	}

	/**
	 * 文章列表维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, FeeConfigBean form) {
		List<FeeConfig> recordList = this.feeConfigService.getRecordList(new FeeConfig(), -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.feeConfigService.getRecordList(new FeeConfig(), paginator.getOffset(),
					paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(FeeConfigDefine.CONFIGFEE_FORM, form);
		}
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(FeeConfigDefine.INFO_ACTION)
	@RequiresPermissions(value = { FeeConfigDefine.PERMISSIONS_INFO, FeeConfigDefine.PERMISSIONS_ADD,
			FeeConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(FeeConfigDefine.CONFIGFEE_FORM) FeeConfigBean form) {
		LogUtil.startLog(FeeConfigController.class.toString(), FeeConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(FeeConfigDefine.INFO_PATH);

		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			FeeConfig record = this.feeConfigService.getRecord(id);
			modelAndView.addObject(FeeConfigDefine.CONFIGFEE_FORM, record);
		}
		// 设置银行列表
		modelAndView.addObject("bankConfig", feeConfigService.getBankConfig(new BankConfig()));
		LogUtil.endLog(FeeConfigController.class.toString(), FeeConfigDefine.INIT);
		return modelAndView;
	}

	/**
	 * 添加活动信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = FeeConfigDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(FeeConfigDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, FeeConfigBean form) {
		LogUtil.startLog(FeeConfigController.class.toString(), FeeConfigDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(FeeConfigDefine.INFO_PATH);

		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}

		BankConfig bk = new BankConfig();
		// 设置银行列表
		form.setBankConfigList(feeConfigService.getBankConfig(bk));
		bk.setName(form.getName());
		List<BankConfig> banks = bankConfigService.getRecordList(bk, -1, -1);
		form.setBankCode(banks.get(0).getCode());
		// 数据插入
		this.feeConfigService.insertRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(FeeConfigDefine.SUCCESS, FeeConfigDefine.SUCCESS);
		LogUtil.endLog(FeeConfigController.class.toString(), FeeConfigDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改活动维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = FeeConfigDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(FeeConfigDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, FeeConfigBean form) {
		LogUtil.startLog(FeeConfigController.class.toString(), FeeConfigDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(FeeConfigDefine.INFO_PATH);

		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// // 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}

		BankConfig bk = new BankConfig();
		// 设置银行列表
		form.setBankConfigList(feeConfigService.getBankConfig(bk));
		bk.setName(form.getName());
		List<BankConfig> banks = bankConfigService.getRecordList(bk, -1, -1);
		form.setBankCode(banks.get(0).getCode());
		// 更新
		this.feeConfigService.updateRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(FeeConfigDefine.SUCCESS, FeeConfigDefine.SUCCESS);
		LogUtil.endLog(FeeConfigController.class.toString(), FeeConfigDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 删除配置信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(FeeConfigDefine.DELETE_ACTION)
	@RequiresPermissions(FeeConfigDefine.PERMISSIONS_DELETE)
	public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
		LogUtil.startLog(FeeConfigController.class.toString(), FeeConfigDefine.DELETE_ACTION);

		ModelAndView modelAndView = new ModelAndView(FeeConfigDefine.RE_LIST_PATH);
		// 解析json字符串
		List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
		this.feeConfigService.deleteRecord(recordList);
		LogUtil.endLog(FeeConfigController.class.toString(), FeeConfigDefine.DELETE_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, FeeConfig form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "name", form.getName())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "name", form.getName(), 50, true)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "personalCredit", form.getPersonalCredit())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "personalCredit", form.getPersonalCredit(), 10,
				true)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "enterpriseCredit", form.getEnterpriseCredit())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "enterpriseCredit", form.getEnterpriseCredit(), 10,
				true)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "quickPayment", form.getQuickPayment())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "quickPayment", form.getQuickPayment(), 10,
				true)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "directTakeout", form.getDirectTakeout())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "directTakeout", form.getDirectTakeout(), 10,
				true)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "quickTakeout", form.getQuickTakeout())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "quickTakeout", form.getQuickTakeout(), 10,
				true)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "normalTakeout", form.getNormalTakeout())) {
			return modelAndView;
		}

		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "normalTakeout", form.getNormalTakeout(), 10,
				true)) {
			return modelAndView;
		}

		return null;
	}

	@ResponseBody
	@RequestMapping(value = FeeConfigDefine.VALIDATEBEFORE)
	@RequiresPermissions(FeeConfigDefine.PERMISSIONS_VIEW)
	public Map<String, Object> validateBeforeAction(HttpServletRequest request, FeeConfigBean form) {
		LogUtil.startLog(FeeConfigController.class.toString(), FeeConfigDefine.VALIDATEBEFORE);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<FeeConfig> list = feeConfigService.getRecordList(form, -1, -1);
		if (list != null && list.size() != 0) {
			if (form.getId() != null) {
				Boolean hasnot = true;
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getId() == form.getId()) {
						hasnot = false;
						break;
					}
				}
				if (hasnot) {
					resultMap.put("success", false);
					resultMap.put("msg", "银行名称或银行代码不可重复添加");
				} else {
					resultMap.put("success", true);
				}
			} else {
				resultMap.put("success", false);
				resultMap.put("msg", "银行名称或银行代码不可重复添加");
			}
		} else {
			resultMap.put("success", true);
		}
		LogUtil.endLog(FeeConfigController.class.toString(), FeeConfigDefine.VALIDATEBEFORE);
		return resultMap;
	}
}
