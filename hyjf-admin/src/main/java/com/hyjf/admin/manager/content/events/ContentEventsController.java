package com.hyjf.admin.manager.content.events;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.maintenance.config.ConfigController;
import com.hyjf.admin.maintenance.config.ConfigDefine;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.Events;

/**
 * 内容管理-公司记事
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = ContentEventsDefine.REQUEST_MAPPING)
public class ContentEventsController extends BaseController {

	@Autowired
	private ContentEventsService contentEventsService;
	@Autowired
	private BorrowCommonService borrowCommonService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentEventsDefine.INIT)
	@RequiresPermissions(ContentEventsDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(ContentEventsDefine.CONTENTEVENTS_FORM) ContentEventsBean form) {
		LogUtil.startLog(ContentEventsController.class.toString(), ContentEventsDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ContentEventsDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ContentEventsController.class.toString(), ContentEventsDefine.INIT);
		return modelAndView;
	}


	/**
	 * 查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentEventsDefine.SEARCH_ACTION)
	@RequiresPermissions(ContentEventsDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(ContentEventsDefine.CONTENTEVENTS_FORM) ContentEventsBean form) {
		LogUtil.startLog(ContentEventsController.class.toString(), ContentEventsDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentEventsDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ContentEventsController.class.toString(), ContentEventsDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ContentEventsBean form) {
		List<Events> recordList = this.contentEventsService.selectRecordList(form, -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.contentEventsService.selectRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(ContentEventsDefine.CONTENTEVENTS_FORM, form);
		}
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ContentEventsDefine.INFO_ACTION)
	@RequiresPermissions(value = { ContentEventsDefine.PERMISSIONS_INFO, ContentEventsDefine.PERMISSIONS_ADD,
			ContentEventsDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,@ModelAttribute(ContentEventsDefine.CONTENTEVENTS_FORM) ContentEventsBean form) {
		LogUtil.startLog(ContentEventsController.class.toString(), ContentEventsDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ContentEventsDefine.INFO_PATH);

		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			Events record = this.contentEventsService.getRecord(id);
			modelAndView.addObject("actTimeStr", GetDate.getDateMyTimeInMillis(record.getActTime()));
			modelAndView.addObject(ContentEventsDefine.CONTENTEVENTS_FORM, record);
		}
		LogUtil.endLog(ContentEventsController.class.toString(), ContentEventsDefine.INIT);
		return modelAndView;
	}

	
	
	/**
	 * 添加
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ContentEventsDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ContentEventsDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, Events form, @RequestParam("acTime") String acTime) {
		LogUtil.startLog(ContentEventsController.class.toString(), ContentEventsDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentEventsDefine.RE_LIST_PATH);

		form.setActTime(Integer.valueOf(GetDate.get10Time(acTime)));
		String[] split = acTime.split("-");
		form.setEventYear(Integer.valueOf(split[0]));
//		form.setEventTime(split[1] + "月" + split[2] + "日");
		form.setEventTime(acTime);

		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 数据插入
		this.contentEventsService.insertRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(ContentEventsDefine.SUCCESS, ContentEventsDefine.SUCCESS);
		LogUtil.endLog(ContentEventsController.class.toString(), ContentEventsDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ContentEventsDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ContentEventsDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, Events form, @RequestParam("acTime") String acTime) {
		LogUtil.startLog(ContentEventsController.class.toString(), ContentEventsDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentEventsDefine.RE_LIST_PATH);
		form.setActTime(Integer.valueOf(GetDate.get10Time(acTime)));
		String[] split = acTime.split("-");
		form.setEventYear(Integer.valueOf(split[0]));
//		form.setEventTime(split[1] + "月" + split[2] + "日");
		form.setEventTime(acTime);
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
		this.contentEventsService.updateRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(ContentEventsDefine.SUCCESS, ContentEventsDefine.SUCCESS);
		LogUtil.endLog(ContentEventsController.class.toString(), ContentEventsDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 *  更新纪事状态
	 * @param request
	 * @param attr
	 * @param form
	 * @return
	 * @Author : huanghui
	 */
	@RequestMapping(value = ContentEventsDefine.STATUS_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ContentEventsDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateStatus(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(ContentEventsDefine.CONTENTEVENTS_FORM) ContentEventsBean form){


		LogUtil.startLog(ContentEventsController.class.toString(), ContentEventsDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(ContentEventsDefine.RE_LIST_PATH);

		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			Events record = this.contentEventsService.getRecord(id);
			if (record.getStatus() == 1){
				record.setStatus(0);
			}else {
				record.setStatus(1);
			}
			this.contentEventsService.updateRecord(record);
		}
		// 更新
		attr.addFlashAttribute(ContentEventsDefine.CONTENTEVENTS_FORM, form);
		LogUtil.endLog(ConfigController.class.toString(), ContentEventsDefine.STATUS_ACTION);
		return modelAndView;
	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param ids
	 * @return
	 */
	@RequestMapping(ConfigDefine.DELETE_ACTION)
	@RequiresPermissions(ContentEventsDefine.PERMISSIONS_DELETE)
	public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
		LogUtil.startLog(ConfigController.class.toString(), ContentEventsDefine.DELETE_ACTION);

		ModelAndView modelAndView = new ModelAndView(ContentEventsDefine.RE_LIST_PATH);
		// 解析json字符串
		List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
		this.contentEventsService.deleteRecord(recordList);
		LogUtil.endLog(ConfigController.class.toString(), ContentEventsDefine.DELETE_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, Events form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "eventTime", form.getEventTime())) {
			return modelAndView;
		}
		;
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "eventTime", form.getEventTime(), 30, true)) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "title", form.getTitle())) {
			return modelAndView;
		}
		;
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "title", form.getTitle(), 255, true)) {
			return modelAndView;
		}

		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "content", form.getContent())){
			return modelAndView;
		}

		return null;
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = ContentEventsDefine.UPLOAD_FILE, method = RequestMethod.POST)
	@ResponseBody
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(ContentEventsController.class.toString(), ContentEventsDefine.UPLOAD_FILE);
		String files = borrowCommonService.uploadFile(request, response);
		LogUtil.endLog(ContentEventsController.class.toString(), ContentEventsDefine.UPLOAD_FILE);
		return files;
	}
}
