package com.hyjf.admin.manager.config.hzrconfig;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import com.hyjf.admin.maintenance.config.ConfigController;
import com.hyjf.admin.manager.config.borrow.sendtype.SendTypeController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.HzrConfig;

/**
 * 汇转让设置
 *
 * @author yangx
 *
 */
@Controller
@RequestMapping(value = HzrConfigDefine.REQUEST_MAPPING)
public class HzrConfigController extends BaseController {

	@Autowired
	private HzrConfigService hzrConfigService;

	/**
	 * 权限维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HzrConfigDefine.INIT)
	@RequiresPermissions(HzrConfigDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(HzrConfigDefine.HZRCONFIG_FORM) HzrConfigBean form) {
		LogUtil.startLog(HzrConfigController.class.toString(), HzrConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(HzrConfigDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(HzrConfigController.class.toString(), HzrConfigDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, HzrConfigBean form) {
		List<HzrConfig> recordList = hzrConfigService.getRecordList(form, -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.hzrConfigService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(HzrConfigDefine.HZRCONFIG_FORM, form);
		}
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HzrConfigDefine.INFO_ACTION)
	@RequiresPermissions(value = { HzrConfigDefine.PERMISSIONS_ADD,
			HzrConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, @ModelAttribute(HzrConfigDefine.HZRCONFIG_FORM) HzrConfigBean form) {
		LogUtil.startLog(HzrConfigController.class.toString(), HzrConfigDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(HzrConfigDefine.INFO_PATH);
		HzrConfig record = null;
		if (StringUtils.isNotEmpty(form.getId())) {
			record = this.hzrConfigService.getRecord(Integer.valueOf(form.getId()));
		}
		modelAndView.addObject(HzrConfigDefine.HZRCONFIG_FORM, record);
		LogUtil.endLog(HzrConfigController.class.toString(), HzrConfigDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 汇转让设置-新增
	 */
	@RequestMapping(HzrConfigDefine.INSERT_ACTION)
	@RequiresPermissions(HzrConfigDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, HzrConfigBean form) {
		// 日志开始
		LogUtil.startLog(HzrConfigController.class.toString(), HzrConfigDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(HzrConfigDefine.INFO_PATH);

		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject(HzrConfigDefine.HZRCONFIG_FORM, form);
			return modelAndView;
		}

		hzrConfigService.insertRecord(form);
		modelAndView.addObject(HzrConfigDefine.SUCCESS, HzrConfigDefine.SUCCESS);
		// 日志结束
		LogUtil.endLog(HzrConfigController.class.toString(), HzrConfigDefine.INIT);
		return modelAndView;
	}

	/**
	 * 汇转让设置-修改
	 */
	@RequestMapping(HzrConfigDefine.UPDATE_ACTION)
	@RequiresPermissions(HzrConfigDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, HzrConfigBean form) {
		// 日志开始
		LogUtil.startLog(HzrConfigController.class.toString(), HzrConfigDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(HzrConfigDefine.INFO_PATH);

		// 画面验证
		this.validatorFieldCheck(modelAndView, form);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject(HzrConfigDefine.HZRCONFIG_FORM, form);
			return modelAndView;
		}

		this.hzrConfigService.updateRecord(form);
		modelAndView.addObject(HzrConfigDefine.SUCCESS, HzrConfigDefine.SUCCESS);
		// 日志结束
		LogUtil.endLog(HzrConfigController.class.toString(), HzrConfigDefine.INIT);
		return modelAndView;
	}

	/**
	 * 删除配置信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(HzrConfigDefine.DELETE_ACTION)
	@RequiresPermissions(HzrConfigDefine.PERMISSIONS_DELETE)
	public ModelAndView deleteRecordAction(HttpServletRequest request, HzrConfigBean form) {
		LogUtil.startLog(ConfigController.class.toString(), HzrConfigDefine.DELETE_ACTION);

		ModelAndView modelAndView = new ModelAndView(HzrConfigDefine.RE_LIST_PATH);
		// 解析json字符串
		List<Integer> recordList = new ArrayList<Integer>();
		if (StringUtils.isNotEmpty(form.getId())) {
			recordList.add(Integer.valueOf(form.getId()));
		}

		this.hzrConfigService.deleteRecord(recordList);
		LogUtil.endLog(ConfigController.class.toString(), HzrConfigDefine.DELETE_ACTION);
		return modelAndView;
	}

	/**
	 * 检查编号唯一性
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = HzrConfigDefine.CHECK_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(HzrConfigDefine.PERMISSIONS_VIEW)
	public String checkAction(HttpServletRequest request) {
		LogUtil.startLog(SendTypeController.class.toString(), HzrConfigDefine.CHECK_ACTION);

		String id = request.getParameter("id");
		String param = request.getParameter("param");

		JSONObject ret = new JSONObject();
		HzrConfigBean form = new HzrConfigBean();
		form.setId(id);
		form.setCode(param);

		boolean flag = this.hzrConfigService.isExistsCode(form);
		if (flag) {
			String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
			message = message.replace("{label}", "编号");
			ret.put(HzrConfigDefine.JSON_VALID_INFO_KEY, message);
		}

		// 没有错误时,返回y
		if (!ret.containsKey(HzrConfigDefine.JSON_VALID_INFO_KEY)) {
			ret.put(HzrConfigDefine.JSON_VALID_STATUS_KEY, HzrConfigDefine.JSON_VALID_STATUS_OK);
		}
		LogUtil.endLog(SendTypeController.class.toString(), HzrConfigDefine.CHECK_ACTION);
		return ret.toString();
	}

	/**
	 * 调用校验表单方法
	 */
	private void validatorFieldCheck(ModelAndView modelAndView, HzrConfigBean form) {
		// 编号
		boolean codeFlag = ValidatorFieldCheckUtil.validateAlphaNumericAndMaxLength(modelAndView, "code", form.getCode(), 20, true);
		// 名称
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "name", form.getName(), 20, true);
		// 数值
//		ValidatorFieldCheckUtil.validateSignlessNum(modelAndView, "value", form.getValue(), 20, true);
		//2015年12月23日17:37:09 BUG203 允许输入%
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "value", form.getValue(), 20, true);
		// 单位
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "unit", form.getUnit(), 20, false);

		if (codeFlag && StringUtils.isEmpty(form.getId())) {
			HzrConfigBean record = new HzrConfigBean();
			record.setId(form.getId());
			record.setCode(form.getCode());
			boolean flag = this.hzrConfigService.isExistsCode(record);
			if (flag) {
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "code", "repeat");
			}
		}

	}
}
