package com.hyjf.admin.employee.employeedimission;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.EmployeeDimissionCustomize;

@Controller
@RequestMapping(EmployeeDimissionDefine.REQUEST_MAPPING)
public class EmployeeDimissionController {
	@Autowired
	private EmployeeDimissionService employeeDimissionService;
	
	private static final String THIS_CLASS = EmployeeDimissionController.class.toString();
	/**
	 * 分页
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, EmployeeDimissionBean form){
		EmployeeDimissionCustomize employeeDimissionCustomize = new EmployeeDimissionCustomize();
		BeanUtils.copyProperties(form, employeeDimissionCustomize);
		List<EmployeeDimissionCustomize> recordList = this.employeeDimissionService.selectDimission(employeeDimissionCustomize);
		if(recordList != null){
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			employeeDimissionCustomize.setLimitStart(paginator.getOffset());
			employeeDimissionCustomize.setLimitEnd(paginator.getLimit());
			
			recordList = this.employeeDimissionService.selectDimission(employeeDimissionCustomize);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modeAndView.addObject(EmployeeDimissionDefine.EMPLOYEEDIMISSION_FORM, form);
		}		
	}
	
	/**
     * 账户设置画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(EmployeeDimissionDefine.LIST)
    public ModelAndView init(HttpServletRequest request, @ModelAttribute("delete_error") String error, @ModelAttribute EmployeeDimissionBean form) {
        LogUtil.startLog(THIS_CLASS, EmployeeDimissionDefine.LIST);
        ModelAndView modelAndView = new ModelAndView(EmployeeDimissionDefine.LIST_PATH);

        if (Validator.isNotNull(error)) {
            modelAndView.addObject("delete_error", error);
        }
        
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, EmployeeDimissionDefine.LIST);
        return modelAndView;
    }

    /**
     * 画面查询
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(EmployeeDimissionDefine.SEARCH_ACTION)
    public ModelAndView search(HttpServletRequest request, EmployeeDimissionBean form) {
        LogUtil.startLog(THIS_CLASS, EmployeeDimissionDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(EmployeeDimissionDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, EmployeeDimissionDefine.SEARCH_ACTION);
        return modelAndView;
    }
    
    @RequestMapping(EmployeeDimissionDefine.DETAIL)
	public ModelAndView selectDimissionDetail(ModelMap modelMap ,int id){
		List<EmployeeDimissionCustomize> list = employeeDimissionService.selectDimissionDetail(id);
		modelMap.put("employeedetail", list);
		return new ModelAndView(EmployeeDimissionDefine.DETAIL_PATH);
	}
	
	@RequestMapping(EmployeeDimissionDefine.APPROVAL)
	public ModelAndView approvalDimissionDetail(ModelMap modelMap ,int id){
		List<EmployeeDimissionCustomize> list = employeeDimissionService.selectDimissionDetail(id);	
		modelMap.put("employeedetail1", list);
		return new ModelAndView(EmployeeDimissionDefine.APPROVAL_PATH);
	}
	
	@RequestMapping(EmployeeDimissionDefine.DIMISSIONPASS)
	public ModelAndView dimissionpass(int id){
		employeeDimissionService.approvalStaff(id);
		return new ModelAndView(EmployeeDimissionDefine.DIMISSIONPASSL_PATH);
	}
    


}
