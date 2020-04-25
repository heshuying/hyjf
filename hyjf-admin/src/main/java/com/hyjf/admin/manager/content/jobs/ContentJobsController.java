package com.hyjf.admin.manager.content.jobs;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.maintenance.config.ConfigController;
import com.hyjf.admin.manager.content.ads.ContentAdsController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.Jobs;

/**
 * 招贤纳士
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = ContentJobsDefine.REQUEST_MAPPING)
public class ContentJobsController extends BaseController {

	@Autowired
	private ContentJobsService contentJobsService;

	/**
	 * 文章列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentJobsDefine.INIT)
	@RequiresPermissions(ContentJobsDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(ContentJobsDefine.CONTENTJOBS_FORM) ContentJobsBean form) {
		LogUtil.startLog(ContentJobsController.class.toString(), ContentJobsDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ContentJobsDefine.LIST_PATH);

		// 创建分页
		this.createPageBy(request, modelAndView, form);
		LogUtil.endLog(ContentJobsController.class.toString(), ContentJobsDefine.INIT);
		return modelAndView;
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentJobsDefine.INFO_ACTION)
	@RequiresPermissions(value = { ContentJobsDefine.PERMISSIONS_INFO, ContentJobsDefine.PERMISSIONS_ADD,
			ContentJobsDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(ContentJobsDefine.CONTENTJOBS_FORM) ContentJobsBean form) {
		LogUtil.startLog(ContentJobsController.class.toString(), ContentJobsDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ContentJobsDefine.INFO_PATH);

		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			Jobs record = this.contentJobsService.getRecord(id);
			modelAndView.addObject(ContentJobsDefine.CONTENTJOBS_FORM, record);
		}
		LogUtil.endLog(ContentJobsController.class.toString(), ContentJobsDefine.INIT);
		return modelAndView;
	}

	/**
	 * 根据条件查询所需要数据
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentJobsDefine.SEARCH_ACTION)
	@RequiresPermissions(ContentJobsDefine.PERMISSIONS_SEARCH)
	public ModelAndView selectContentTeam(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(ContentJobsDefine.CONTENTJOBS_FORM) ContentJobsBean form) {
		LogUtil.startLog(ContentJobsController.class.toString(), ContentJobsDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentJobsDefine.LIST_PATH);
		// 参数校验
		// if (form.getStartCreate() != null
		// && form.getEndCreate() != null
		// && GetDate.getMillis(GetDate.str2Timestamp(form.getStartCreate())) >
		// GetDate
		// .getMillis(GetDate.str2Timestamp(form.getEndCreate()))) {
		// return modelAndView.addObject("errorMsg", "活动创建开始日期不能小于结束日期");
		// }
		// 创建分页
		this.createPageBy(request, modelAndView, form);
		LogUtil.endLog(ContentJobsController.class.toString(), ContentJobsDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 文章列表维护分页机能 根据条件
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPageBy(HttpServletRequest request, ModelAndView modelAndView, ContentJobsBean form) {
		List<Jobs> recordList = this.contentJobsService.selectRecordList(form, -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.contentJobsService.selectRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(ContentJobsDefine.CONTENTJOBS_FORM, form);
		}
	}

	/**
	 * 添加活动信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ContentJobsDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ContentJobsDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, Jobs form) {
		LogUtil.startLog(ContentJobsController.class.toString(), ContentJobsDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentJobsDefine.INFO_PATH);

		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 数据插入
		this.contentJobsService.insertRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(ContentJobsDefine.SUCCESS, ContentJobsDefine.SUCCESS);
		LogUtil.endLog(ContentJobsController.class.toString(), ContentJobsDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改活动维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ContentJobsDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ContentJobsDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, Jobs form) {
		LogUtil.startLog(ContentJobsController.class.toString(), ContentJobsDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentJobsDefine.INFO_PATH);

		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}
		// 更新
		this.contentJobsService.updateRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(ContentJobsDefine.SUCCESS, ContentJobsDefine.SUCCESS);
		LogUtil.endLog(ContentJobsController.class.toString(), ContentJobsDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 修改状态
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentJobsDefine.STATUS_ACTION)
	@RequiresPermissions(ContentJobsDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateStatus(HttpServletRequest request,  RedirectAttributes attr,ContentJobsBean form) {
		LogUtil.startLog(ContentAdsController.class.toString(), ContentJobsDefine.STATUS_ACTION);

		ModelAndView modelAndView = new ModelAndView(ContentJobsDefine.RE_LIST_PATH);
		// 修改状态
		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			Jobs record = this.contentJobsService.getRecord(id);
			if (record.getStatus() == 1) {
				record.setStatus(0);
			} else {
				record.setStatus(1);
			}
			this.contentJobsService.updateRecord(record);
		}
		attr.addFlashAttribute(ContentJobsDefine.CONTENTJOBS_FORM, form);
		LogUtil.endLog(ContentAdsController.class.toString(), ContentJobsDefine.STATUS_ACTION);
		return modelAndView;
	}

	/**
	 * 删除配置信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentJobsDefine.DELETE_ACTION)
	@RequiresPermissions(ContentJobsDefine.PERMISSIONS_DELETE)
	public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
		LogUtil.startLog(ConfigController.class.toString(), ContentJobsDefine.DELETE_ACTION);

		ModelAndView modelAndView = new ModelAndView(ContentJobsDefine.RE_LIST_PATH);
		// 解析json字符串
		List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
		this.contentJobsService.deleteRecord(recordList);
		LogUtil.endLog(ConfigController.class.toString(), ContentJobsDefine.DELETE_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, Jobs form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "officeName", form.getOfficeName())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "officeName", form.getOfficeName(), 60, true)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "status", form.getStatus().toString())) {
			return modelAndView;
		}
		;
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "status", form.getStatus().toString(), 2, true)) {
			return modelAndView;
		}

		return null;
	}

}
