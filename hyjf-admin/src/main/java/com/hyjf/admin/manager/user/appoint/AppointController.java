/**
 * Description:管理
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:01:57
 * Modification History:
 * Modified by : 
 * */
package com.hyjf.admin.manager.user.appoint;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.AppointmentAuthLogCustomize;
import com.hyjf.mybatis.model.customize.AppointmentRecodLogCustomize;

@Controller
@RequestMapping(value = AppointDefine.REQUEST_MAPPING)
public class AppointController extends BaseController {

	@Autowired
	private AppointService appointService;

	/**
	 * 维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppointDefine.INIT)
	@RequiresPermissions(AppointDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(AppointDefine.FORM) AppointBean form) {
		LogUtil.startLog(AppointController.class.toString(), AppointDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(AppointDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(AppointController.class.toString(), AppointDefine.INIT);
		return modelAndView;
	}

	/**
	 * 维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppointDefine.SEARCH_ACTION)
	@RequiresPermissions(AppointDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(AppointDefine.FORM) AppointBean form) {
		LogUtil.startLog(AppointController.class.toString(), AppointDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(AppointDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(AppointController.class.toString(), AppointDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 维护分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, AppointBean form) {
		
		
		int recordTotal = this.appointService.countAppointRecordTotalNum(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, 10);
			List<AppointmentAuthLogCustomize> recordList = this.appointService.getAppointRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(AppointDefine.FORM, form);
	}
	
	/**
	 * 明细详情页面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppointDefine.AUTHLIST)
	@RequiresPermissions(AppointDefine.PERMISSIONS_VIEW)
	public ModelAndView authlist(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(AppointDefine.FORM) AppointBean form) {
		LogUtil.startLog(AppointController.class.toString(), AppointDefine.AUTHLIST);
		ModelAndView modelAndView = new ModelAndView(AppointDefine.AUTH_LIST_PATH);
		// 创建分页
		this.createPageAuth(request, modelAndView, form);
		LogUtil.endLog(AppointController.class.toString(), AppointDefine.AUTHLIST);
		return modelAndView;
	}
	
	/**
	 * 维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppointDefine.AUTH_SEARCH_ACTION)
	@RequiresPermissions(AppointDefine.PERMISSIONS_SEARCH)
	public ModelAndView authlistSearchAction(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(AppointDefine.FORM) AppointBean form) {
		LogUtil.startLog(AppointController.class.toString(), AppointDefine.AUTH_SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(AppointDefine.AUTH_LIST_PATH);
		// 创建分页
		this.createPageAuth(request, modelAndView, form);
		LogUtil.endLog(AppointController.class.toString(), AppointDefine.AUTH_SEARCH_ACTION);
		return modelAndView;
	}
	
	/**
	 * 维护分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPageAuth(HttpServletRequest request, ModelAndView modelAndView, AppointBean form) {
		int recordTotal = this.appointService.countAuthAppointRecordTotalNum(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, 10);
			List<AppointmentAuthLogCustomize> recordList = this.appointService.getAuthAppointRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(AppointDefine.FORM, form);
		modelAndView.addObject("userId", form.getUserId());
	}
	
	/**
	 * 违约分值
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppointDefine.APPOINTLISTRECORDMAIN)
	@RequiresPermissions(AppointDefine.PERMISSIONS_VIEW)
	public ModelAndView appointlist(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(AppointDefine.FORM) AppointBean form) {
		LogUtil.startLog(AppointController.class.toString(), AppointDefine.APPOINTLISTRECORDMAIN);
		ModelAndView modelAndView = new ModelAndView(AppointDefine.APPOINT_LIST_MAIN_PATH);
		// 创建分页
		this.createPageappointlist(request, modelAndView, form);
		LogUtil.endLog(AppointController.class.toString(), AppointDefine.APPOINTLISTRECORDMAIN);
		return modelAndView;
	}
	
	/**
	 * 维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppointDefine.APPOINT_MAIN_SEARCH_ACTION)
	@RequiresPermissions(AppointDefine.PERMISSIONS_SEARCH)
	public ModelAndView appointlistSearchAction(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(AppointDefine.FORM) AppointBean form) {
		LogUtil.startLog(AppointController.class.toString(), AppointDefine.APPOINT_MAIN_SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(AppointDefine.APPOINT_LIST_MAIN_PATH);
		// 创建分页
		this.createPageappointlist(request, modelAndView, form);
		LogUtil.endLog(AppointController.class.toString(), AppointDefine.APPOINT_MAIN_SEARCH_ACTION);
		return modelAndView;
	}
	
	/**
	 * 维护分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPageappointlist(HttpServletRequest request, ModelAndView modelAndView, AppointBean form) {
		int recordTotal = this.appointService.countAppointRecordTotalNumMain(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, 10);
			List<AppointmentAuthLogCustomize> recordList = this.appointService.getAppointRecordListMain(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(AppointDefine.FORM, form);
		modelAndView.addObject("userId", form.getUserId());
	}

	/**
	 * 违约分值明细记录
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppointDefine.APPOINTLISTRECORD)
	@RequiresPermissions(AppointDefine.PERMISSIONS_VIEW)
	public ModelAndView appointlistrecord(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(AppointDefine.FORM) AppointBean form) {
		LogUtil.startLog(AppointController.class.toString(), AppointDefine.APPOINTLISTRECORD);
		ModelAndView modelAndView = new ModelAndView(AppointDefine.APPOINT_LIST_INFO_PATH);
		// 创建分页
		this.createPageappointlistrecord(request, modelAndView, form);
		LogUtil.endLog(AppointController.class.toString(), AppointDefine.APPOINTLISTRECORD);
		return modelAndView;
	}
	
	/**
	 * 维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppointDefine.APPOINT_RECORD_SEARCH_ACTION)
	@RequiresPermissions(AppointDefine.PERMISSIONS_SEARCH)
	public ModelAndView appointlistrecordSearchAction(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(AppointDefine.FORM) AppointBean form) {
		LogUtil.startLog(AppointController.class.toString(), AppointDefine.APPOINT_RECORD_SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(AppointDefine.APPOINT_LIST_INFO_PATH);
		// 创建分页
		this.createPageappointlistrecord(request, modelAndView, form);
		LogUtil.endLog(AppointController.class.toString(), AppointDefine.APPOINT_RECORD_SEARCH_ACTION);
		return modelAndView;
	}
	
	/**
	 * 维护分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPageappointlistrecord(HttpServletRequest request, ModelAndView modelAndView, AppointBean form) {
		int recordTotal = this.appointService.countAppointRecordTotalRecordNumMain(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, 10);
			List<AppointmentRecodLogCustomize> recordList = this.appointService.getAppointRecordListRecordMain(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(AppointDefine.FORM, form);
		modelAndView.addObject("userId", form.getUserId());
	}


}
