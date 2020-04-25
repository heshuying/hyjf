package com.hyjf.admin.maintenance.log;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.MaintenanceLogCustomize;

@Controller
@RequestMapping(MaintenanceLogDefine.REQUEST_MAPPING)
public class MaintenanceLogController {
	@Autowired
	private MaintenanceLogService maintenanceLogService;
	
	private static final String THIS_CLASS = MaintenanceLogController.class.toString();
	
	/**
	 * 分页
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, MaintenanceLogBean form){
		MaintenanceLogCustomize maintenanceLogCustomize = new MaintenanceLogCustomize();
		BeanUtils.copyProperties(form, maintenanceLogCustomize);
		List<MaintenanceLogCustomize> recordList = this.maintenanceLogService.queryLogList(maintenanceLogCustomize);
		
		if(recordList != null){
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			maintenanceLogCustomize.setLimitStart(paginator.getOffset());
			maintenanceLogCustomize.setLimitEnd(paginator.getLimit());
			
			recordList = this.maintenanceLogService.queryLogList(maintenanceLogCustomize);
			form.setPaginator(paginator);;
			form.setRecordList(recordList);
			modelAndView.addObject(MaintenanceLogDefine.MAINTENANCELOG_FORM, form);
		}
	}
	
	/**
	 * 账户设置画面初始化
	 * 即查询日志列表
	 * @param request
	 * @param error
	 * @param form
	 * @return
	 */
	@RequestMapping(MaintenanceLogDefine.INIT)
	public ModelAndView init(HttpServletRequest request,  @ModelAttribute("delete_error") String error, @ModelAttribute MaintenanceLogBean form){
		LogUtil.startLog(THIS_CLASS, MaintenanceLogDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(MaintenanceLogDefine.LIST_PATH);
		
		if(Validator.isNotNull(error)){
            modelAndView.addObject("delete_error", error);
		}
		
		//创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, MaintenanceLogDefine.INIT);
		
		return modelAndView;
	}
	
	/**
	 * 画面查询
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MaintenanceLogDefine.SEARCH_ACTION)
	public ModelAndView search(HttpServletRequest request, MaintenanceLogBean form) {
		LogUtil.startLog(THIS_CLASS, MaintenanceLogDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(MaintenanceLogDefine.LIST_PATH);
		
		//创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, MaintenanceLogDefine.SEARCH_ACTION);
		
		return modelAndView;
		
	}
	
	
	
}
