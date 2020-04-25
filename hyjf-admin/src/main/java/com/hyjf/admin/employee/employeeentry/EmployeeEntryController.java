package com.hyjf.admin.employee.employeeentry;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.EmployeeEntryCustomize;

@Controller
@RequestMapping(EmployeeEntryDefine.REQUEST_MAPPING)
public class EmployeeEntryController {
	@Autowired
	private EmployeeEntryService employeeEntryService;
	
	private static final String THIS_CLASS = EmployeeEntryController.class.toString();
	
	/**
	 * 分页
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, EmployeeEntryBean form){
		EmployeeEntryCustomize employeeEntryCustomize = new EmployeeEntryCustomize();
        BeanUtils.copyProperties(form, employeeEntryCustomize);
        List<EmployeeEntryCustomize> recordList = this.employeeEntryService.selectEntry(employeeEntryCustomize);
        
        if(recordList != null){
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			employeeEntryCustomize.setLimitStart(paginator.getOffset());
			employeeEntryCustomize.setLimitEnd(paginator.getLimit());
			
			recordList = this.employeeEntryService.selectEntry(employeeEntryCustomize);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(EmployeeEntryDefine.EMPLOYEEENTRY_FORM, form);

        }	
	}
	
	/**
	 * 入职员工列表
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(EmployeeEntryDefine.LIST)
	public ModelAndView selectEntry(HttpServletRequest request,EmployeeEntryBean form){
		ModelAndView modelAndView = new ModelAndView(EmployeeEntryDefine.LIST_PATH);
		//创建分页
		this.createPage(request, modelAndView, form);
		return modelAndView;
	}
	
	/**
     * 画面查询
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(EmployeeEntryDefine.SEARCH_ACTION)
    public ModelAndView search(HttpServletRequest request, EmployeeEntryBean form) {
        LogUtil.startLog(THIS_CLASS, EmployeeEntryDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(EmployeeEntryDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, EmployeeEntryDefine.SEARCH_ACTION);
        return modelAndView;
    }
	
	@RequestMapping(EmployeeEntryDefine.DETAIL)
	public ModelAndView selectEntryDetail(ModelMap modelMap ,int id){
		List<EmployeeEntryCustomize> list = employeeEntryService.selectEntryDetail(id);
		modelMap.put("employeeentry", list);
		return new ModelAndView(EmployeeEntryDefine.DETAIL_PATH);
	}
	
	@RequestMapping(EmployeeEntryDefine.APPROVAL)
	public ModelAndView approvalEntryDetail(ModelMap modelMap ,int id){
		List<EmployeeEntryCustomize> list = employeeEntryService.selectEntryDetail(id);
		modelMap.put("employeeentry1", list);
		return new ModelAndView(EmployeeEntryDefine.APPROVAL_PATH);
	}
	
	@RequestMapping(EmployeeEntryDefine.ENTRYPASS)
	public ModelAndView entrypass(int id){
		employeeEntryService.approvalStaff(id);
		
		return new ModelAndView(EmployeeEntryDefine.ENTRYPASS_PATH);
	}

}
