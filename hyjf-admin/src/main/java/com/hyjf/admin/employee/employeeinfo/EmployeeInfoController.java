package com.hyjf.admin.employee.employeeinfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.annotate.Token;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.customize.EmployeeDimissionCustomize;
import com.hyjf.mybatis.model.customize.EmployeeInfoCustomize;

@Controller
@RequestMapping(EmployeeInfoDefine.REQUEST_MAPPING)
public class EmployeeInfoController extends BaseController{
	@Autowired
	private EmployeeInfoService employeeInfoService;
	
    private static final String THIS_CLASS = EmployeeInfoController.class.getName();

	/**
	 * 分页
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, EmployeeInfoBean form){
		EmployeeInfoCustomize employeeInfoCustomize = new EmployeeInfoCustomize();
        BeanUtils.copyProperties(form, employeeInfoCustomize);
		List<EmployeeInfoCustomize> recordList = this.employeeInfoService.getRecordList(employeeInfoCustomize); 
		if(recordList != null){
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			employeeInfoCustomize.setLimitStart(paginator.getOffset());
			employeeInfoCustomize.setLimitEnd(paginator.getLimit());
			
			recordList = this.employeeInfoService.getRecordList(employeeInfoCustomize);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modeAndView.addObject(EmployeeInfoDefine.EMPLOYEEINFO_FORM, form);
		}

	}
	
	/**
     * 账户设置画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(EmployeeInfoDefine.LIST)
    public ModelAndView init(HttpServletRequest request, @ModelAttribute("delete_error") String error, @ModelAttribute EmployeeInfoBean form) {
        LogUtil.startLog(THIS_CLASS, EmployeeInfoDefine.LIST);
    	ModelAndView modelAndView = new ModelAndView(EmployeeInfoDefine.LIST_PATH);

        if (Validator.isNotNull(error)) {
            modelAndView.addObject("delete_error", error);
        }
        
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, EmployeeInfoDefine.LIST);
        return modelAndView;
    }

    /**
     * 画面查询
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(EmployeeInfoDefine.SEARCH_ACTION)
    public ModelAndView search(HttpServletRequest request,@ModelAttribute("delete_error") String error, EmployeeInfoBean form) {
        LogUtil.startLog(THIS_CLASS, EmployeeInfoDefine.SEARCH_ACTION);

    	ModelAndView modelAndView = new ModelAndView(EmployeeInfoDefine.LIST_PATH);
        
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, EmployeeInfoDefine.SEARCH_ACTION);

        return modelAndView;
    }


    /**
     * 迁移到账户设置详细画面
     * 
     * @param request
     * @param form
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping(value = EmployeeInfoDefine.INFO_ACTION)
    @Token(save = true)
    @RequiresPermissions(value = { EmployeeInfoDefine.PERMISSIONS_ADD, EmployeeInfoDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
    public ModelAndView moveToInfoAction(HttpServletRequest request, EmployeeInfoBean form, EmployeeInfoCustomize employeeInfoCustomize) throws Exception {
        LogUtil.startLog(THIS_CLASS, EmployeeInfoDefine.INFO_ACTION);
    	ModelAndView modelAndView = new ModelAndView(EmployeeInfoDefine.EDIT_PATH);

//        String ids = form.getIds();
    	String ids = request.getParameter("ids");
//    	String ids2= form.getIds();
        EmployeeInfoBean bean = new EmployeeInfoBean();
        
        if (Validator.isNotNull(ids)&& NumberUtils.isNumber(ids)) {
        	Integer id = Integer.valueOf(ids);
        	bean.setId(id);
     	
        	EmployeeInfoCustomize record = this.employeeInfoService.getRecord(id);
        	BeanUtils.copyProperties(record, bean);
        }
        
//        bean.setRecordList(this.employeeInfoService.getRecordList(employeeInfoCustomize));
        
        modelAndView.addObject(EmployeeInfoDefine.EMPLOYEEINFO_FORM, bean);
        LogUtil.endLog(THIS_CLASS, EmployeeInfoDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     * 添加账户设置信息
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = EmployeeInfoDefine.INSERT_ACTION, method = RequestMethod.POST)
    @Token(check = true, forward = EmployeeInfoDefine.TOKEN_INIT_PATH)
    public ModelAndView insertAction(HttpServletRequest request, EmployeeInfoBean form) {
        LogUtil.startLog(THIS_CLASS, EmployeeInfoDefine.INSERT_ACTION);
        ModelAndView modelAndView = new ModelAndView(EmployeeInfoDefine.EDIT_PATH);
        
//        // 画面验证
//        this.validatorFieldCheck(modelAndView, form);
        
        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
        	form.setRecordList(this.employeeInfoService.getRecordList(form));
        	
        	modelAndView.addObject(EmployeeInfoDefine.EMPLOYEEINFO_FORM, form);
        	LogUtil.errorLog(THIS_CLASS, EmployeeInfoDefine.INSERT_ACTION, "输入内容验证失败", null);
        
        }
        
        form.setAcc_province("null");
        form.setAcc_city("null");
        form.setIspart("N");
        form.setEnterdate("null");
        form.setSchool("null");
        form.setLast_login_ip("null");
        form.setLast_login_time("null");
        form.setCreate_time("null");
        form.setUser_status("E1");
        form.setHyd_id(0);
        
//        String departmentid = request.getParameter("department");
//        String level = request.getParameter("level");
//        String positionid = request.getParameter("position");
//        String payroll = request.getParameter("payroll");
        String hyd_name = request.getParameter("hydname");
        String user_realname = request.getParameter("realname");
//        String idcard = request.getParameter("idcard");
//        String sex = request.getParameter("sex");
//        String age = request.getParameter("age");
//        String mobile = request.getParameter("mobile");
//        String acc_address = request.getParameter("acc_address");
//        String temporary = request.getParameter("temporary");
//        String bank_address = request.getParameter("bank_address");
//        String bank_user = request.getParameter("bank_user");
//        String bank_num = request.getParameter("bank_num");
//        String user_email = request.getParameter("user_email");
//        String reference = request.getParameter("reference");
//        String education = request.getParameter("education");
//        String specialty = request.getParameter("specialty");
//        
//        form.setDepartmentid(Integer.parseInt(departmentid));
//        form.setLevel(Integer.parseInt(level));
//        form.setPositionid(Integer.parseInt(positionid));
//        form.setPayroll(Integer.parseInt(payroll));
        form.setHyd_name(hyd_name);
        form.setUser_realname(user_realname);
//        form.setIdcard(idcard);
//        form.setSex(Integer.parseInt(sex));
//        form.setAge(Integer.parseInt(age));
//        form.setMobile(mobile);
//        form.setAcc_address(acc_address);
//        form.setTemporary(Integer.parseInt(temporary));
//        form.setBank_address(bank_address);
//        form.setBank_user(bank_user);
//        form.setBank_num(bank_num);
//        form.setUser_email(user_email);
//        form.setReference(reference);
//        form.setEducation(Integer.parseInt(education));
//        form.setSpecialty(specialty);

        this.employeeInfoService.insertRecord(form);;
        
//        this.createPage(request, modelAndView, form);

        modelAndView.addObject(EmployeeInfoDefine.SUCCESS, EmployeeInfoDefine.SUCCESS);
        LogUtil.endLog(THIS_CLASS, EmployeeInfoDefine.INSERT_ACTION);
        return modelAndView;
    }

    /**
     * 修改账户设置信息
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = EmployeeInfoDefine.UPDATE_ACTION, method = RequestMethod.POST)
    @Token(check = true, forward = EmployeeInfoDefine.TOKEN_INIT_PATH)
    public ModelAndView updateAction(HttpServletRequest request, EmployeeInfoBean form) {
        LogUtil.startLog(THIS_CLASS, EmployeeInfoDefine.UPDATE_ACTION);
        ModelAndView modelAndView = new ModelAndView(EmployeeInfoDefine.EDIT_PATH);

        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
        	form.setRecordList(this.employeeInfoService.getRecordList(form));
        	modelAndView.addObject(EmployeeInfoDefine.EMPLOYEEINFO_FORM, form);
        	LogUtil.errorLog(THIS_CLASS, EmployeeInfoDefine.INSERT_ACTION, "输入内容验证失败", null);
            return modelAndView;
        }
        
        this.employeeInfoService.updateRecord(form);
        // 创建分页
        this.createPage(request, modelAndView, form);

        modelAndView.addObject(EmployeeInfoDefine.SUCCESS, EmployeeInfoDefine.SUCCESS);
        LogUtil.endLog(THIS_CLASS, EmployeeInfoDefine.UPDATE_ACTION);
        return modelAndView;
    }

    
    /**
	 * 员工明细
	 * @param modelMap
	 * @param id
	 * @return
	 */
	@RequestMapping(EmployeeInfoDefine.DETAIL)
	public ModelAndView selectDetailStaff(ModelMap modelMap, int id){
		ModelAndView modeAndView = new ModelAndView(EmployeeInfoDefine.DETAIL_PATH);
	    List<EmployeeInfoCustomize> list = employeeInfoService.selectDetailStaff(id);		 
	    modelMap.put("employeedetail", list);
	    return modeAndView;
	}
	
	
	@RequestMapping(EmployeeInfoDefine.RESETPASSWORD)
	public ModelAndView resetPassword(ModelMap modelMap,int id){
		employeeInfoService.resetPassword(id);
		return new ModelAndView(EmployeeInfoDefine.RESETPASSWORD_PATH);
	}

	@RequestMapping("/leaveStaff")
	public ModelAndView leaveStaff(ModelMap modelMap,int id,HttpServletRequest request,EmployeeInfoBean form){
		ModelAndView modelAndView = new ModelAndView("employee/employeeinfo/employeeinfo_leave");
		this.createPage(request, modelAndView, form);
		return modelAndView;
	}
	@RequestMapping(value="/leaveStaffOK",method = RequestMethod.GET)
	public ModelAndView leaveStaffOk(HttpServletRequest request,int id){
		employeeInfoService.leaveStaff(id);
		
		EmployeeDimissionCustomize employeeDimissionCustomize = new EmployeeDimissionCustomize();
//    	String ids = request.getParameter("ids");

//		String remark = request.getParameter("leaveType");
		String leave_time = request.getParameter("leaveDay");
		String end_time = request.getParameter("leaveSalaryDay");
//		String userid = request.getParameter("id");
		int remark = Integer.parseInt(request.getParameter("leaveType"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd "); 
		Date leavetime1 = null;
		Date endtime1 = null;
		try {
			leavetime1 = sdf.parse(leave_time);
			endtime1 = sdf.parse(end_time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int userid = Integer.parseInt(request.getParameter("id"));
		
		employeeDimissionCustomize.setRemark(remark);
		employeeDimissionCustomize.setLeave_time(leavetime1);
		employeeDimissionCustomize.setEnd_time(endtime1);
		employeeDimissionCustomize.setUserid(userid);
		
		employeeInfoService.insertLeaveStaff(employeeDimissionCustomize);
		return new ModelAndView(EmployeeInfoDefine.RESETPASSWORD_PATH);

	}
	
	
	 /**
     * 根据业务需求导出相应的表格 此处暂时为可用情况 缺陷： 1.无法指定相应的列的顺序， 2.无法配置，excel文件名，excel sheet名称
     * 3.目前只能导出一个sheet 4.列的宽度的自适应，中文存在一定问题
     * 5.根据导出的业务需求最好可以在导出的时候输入起止页码，因为在大数据量的情况下容易造成卡顿
     *
     * 导出账户列表
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(EmployeeInfoDefine.EXPORT_ACCOUNTMANAGE_ACTION)
    public void exportEmployeeInfoExcel(HttpServletRequest request, HttpServletResponse response, EmployeeInfoBean form) throws Exception {
        // 从session中获取相应的登录用户名
        String userName = ShiroUtil.getLoginUsername();
        // 表格sheet名称
        String sheetName = "员工数据";
        // 设置相应的列名
        Map<String, String> headerTitles = new LinkedHashMap<String, String>();

        headerTitles.put("id", "员工编号");
        headerTitles.put("seconddepart", "二级分部");
        headerTitles.put("cityManager", "城市经理");
        headerTitles.put("thirddepart", "团队名称");
        headerTitles.put("user_realname", "姓名");
        headerTitles.put("level", "角色");
        headerTitles.put("pname", "岗位名称");
        headerTitles.put("mobile", "手机");
        headerTitles.put("entrydate", "入职日期");
        headerTitles.put("temporary", "是否兼职");
        headerTitles.put("hyd_name", "汇盈贷账号");

        // 取得数据
        form.setLimitStart(-1);
        form.setLimitEnd(-1);
        List<EmployeeInfoCustomize> recordList = this.employeeInfoService.getRecordList(form);

        ServletOutputStream out = null;
        try {
            String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + ".xls";
            response.setContentType("application/msexcel;utf-8");
            // 文件名的中文显示
            response.setHeader("Content-Disposition", new String(("attachment;filename=" + fileName).getBytes("UTF-8"), "ISO8859-1"));
            out = response.getOutputStream();

            ExportExcel<EmployeeInfoCustomize> ex = new ExportExcel<EmployeeInfoCustomize>();
            ex.exportExcel(sheetName, userName, headerTitles, recordList, out, "yyyy-MM-dd HH:mm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }


}
