package com.hyjf.admin.manager.config.pushmoney;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.PushMoney;

/**
 * 提成配置页面
 * 
 * @author qingbing
 *
 */
@Controller
@RequestMapping(value = PushMoneyDefine.REQUEST_MAPPING)
public class PushMoneyController extends BaseController {

	@Autowired
	private PushMoneyService pushMoneyService;

	/**
	 * 提成配置画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PushMoneyDefine.INIT)
	@RequiresPermissions(PushMoneyDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request,
			@ModelAttribute(PushMoneyDefine.PUSHMONEY_FORM) PushMoneyBean form) {
		// 日志开始
		LogUtil.startLog(PushMoneyController.class.toString(), PushMoneyDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(PushMoneyDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		// 日志结束
		LogUtil.endLog(PushMoneyController.class.toString(), PushMoneyDefine.INIT);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, PushMoneyBean form) {
		List<PushMoney> recordList = pushMoneyService.getRecordList(new PushMoney(), -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.pushMoneyService.getRecordList(new PushMoney(), paginator.getOffset(),
					paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(PushMoneyDefine.PUSHMONEY_FORM, form);
		}
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PushMoneyDefine.INFO_ACTION)
	@RequiresPermissions(value = { PushMoneyDefine.PERMISSIONS_INFO, PushMoneyDefine.PERMISSIONS_ADD,
			PushMoneyDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request,
			@ModelAttribute(PushMoneyDefine.PUSHMONEY_FORM) PushMoneyBean form) {
		LogUtil.startLog(PushMoneyController.class.toString(), PushMoneyDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(PushMoneyDefine.INFO_PATH);

		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			PushMoney record = this.pushMoneyService.getRecord(id);
			modelAndView.addObject(PushMoneyDefine.PUSHMONEY_FORM, record);
		}
		LogUtil.endLog(PushMoneyController.class.toString(), PushMoneyDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 数据添加
	 * 
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(PushMoneyDefine.INSERT_ACTION)
	@RequiresPermissions(PushMoneyDefine.PERMISSIONS_ADD)
	public ModelAndView add(PushMoney form) {
		// 日志开始
		LogUtil.startLog(PushMoneyController.class.toString(), PushMoneyDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(PushMoneyDefine.INFO_PATH);
		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 成功插入
		this.pushMoneyService.insertRecord(form);
		modelAndView.addObject(PushMoneyDefine.SUCCESS, PushMoneyDefine.SUCCESS);
		// 日志结束
		LogUtil.endLog(PushMoneyController.class.toString(), PushMoneyDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 数据修改
	 * 
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(PushMoneyDefine.UPDATE_ACTION)
	@RequiresPermissions(PushMoneyDefine.PERMISSIONS_MODIFY)
	public ModelAndView update(PushMoney form) {
		// 日志开始
		LogUtil.startLog(PushMoneyController.class.toString(), PushMoneyDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(PushMoneyDefine.INFO_PATH);
		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 成功修改
		this.pushMoneyService.updateRecord(form);
		modelAndView.addObject(PushMoneyDefine.SUCCESS, PushMoneyDefine.SUCCESS);
		// 日志结束
		LogUtil.endLog(PushMoneyController.class.toString(), PushMoneyDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 删除配置信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(PushMoneyDefine.DELETE_ACTION)
	@RequiresPermissions(PushMoneyDefine.PERMISSIONS_DELETE)
	public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
		LogUtil.startLog(PushMoneyController.class.toString(), PushMoneyDefine.DELETE_ACTION);

		ModelAndView modelAndView = new ModelAndView(PushMoneyDefine.RE_LIST_PATH);
		// 解析json字符串
		List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
		this.pushMoneyService.deleteRecord(recordList);
		LogUtil.endLog(PushMoneyController.class.toString(), PushMoneyDefine.DELETE_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, PushMoney form) {
		// 字段校验
		if (form.getType() != null
				&& !ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "name", form.getType(), 20, true)) {
			return modelAndView;
		}
		if (form.getMonthTender() != null
				&& !ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "value", form.getMonthTender(), 20, true)) {
			return modelAndView;
		}
		return null;
	}
}
