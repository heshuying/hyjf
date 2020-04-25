package com.hyjf.admin.datacenter.adminstockinfo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminStockInfoCustomize;
/**
 * 查询
 * @author Albert
 *
 */
@Controller
@RequestMapping(value = AdminStockInfoDefine.REQUEST_MAPPING)
public class AdminStockInfoController extends BaseController  {

	@Autowired
	private AdminStockInfoService adminStockInfoService;

    /**
     * 查询列表
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AdminStockInfoDefine.ADMINSTOCKINFO_LIST)
    @RequiresPermissions(AdminStockInfoDefine.PERMISSIONS_VIEW)
    public ModelAndView queryadminStockInfo(HttpServletRequest request, AdminStockInfoBean form) {
        LogUtil.startLog(AdminStockInfoController.class.toString(), AdminStockInfoDefine.ADMINSTOCKINFO_LIST);
        ModelAndView modeAndView = new ModelAndView(AdminStockInfoDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modeAndView, form);
        LogUtil.endLog(AdminStockInfoController.class.toString(), AdminStockInfoDefine.ADMINSTOCKINFO_LIST);
        return modeAndView;
    }

    /**
     * 分页
     *
     * @param request
     * @param modeAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modeAndView, AdminStockInfoBean form) {
    	AdminStockInfoCustomize adminStockInfoCustomize = new AdminStockInfoCustomize();
        BeanUtils.copyProperties(form, adminStockInfoCustomize);
        Integer count = this.adminStockInfoService.getAdminStockInfocount(adminStockInfoCustomize);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            adminStockInfoCustomize.setLimitStart(paginator.getOffset());
            adminStockInfoCustomize.setLimitEnd(paginator.getLimit());

            List<AdminStockInfoCustomize> customers = this.adminStockInfoService.getAdminStockInfoList(adminStockInfoCustomize);
            form.setPaginator(paginator);
            form.setRecordList(customers);
        }
        modeAndView.addObject(AdminStockInfoDefine.ADMINSTOCKINFO_FORM, form);

    }
    
    /**
     * 跳转新增修改页面 
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AdminStockInfoDefine.ADMINSTOCKINFO_INFO_LIST)
    public ModelAndView AdminStockInfoPageAction(HttpServletRequest request) {
        LogUtil.startLog(AdminStockInfoController.class.toString(), AdminStockInfoDefine.ADMINSTOCKINFO_INFO_LIST);
        ModelAndView modelAndView = new ModelAndView(AdminStockInfoDefine.INFO_PATH);
        String userId = request.getParameter("id"); 
        if(userId!=null&&userId!=""){
            AdminStockInfoCustomize adminStockInfo = this.adminStockInfoService.getAdminStockInfoById(Integer.valueOf(userId));
            modelAndView.addObject(AdminStockInfoDefine.ADMINSTOCKINFO_FORM , adminStockInfo);
        }
        LogUtil.startLog(AdminStockInfoController.class.toString(), AdminStockInfoDefine.ADMINSTOCKINFO_INFO_LIST);
        return modelAndView;
    }

    /**
     * 新增修改信息
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AdminStockInfoDefine.ADMINSTOCKINFO_ADD_OR_SAVE)
    public ModelAndView addOrSaveAdminStockInfoAction(HttpServletRequest request,@ModelAttribute AdminStockInfoBean form) {
        ModelAndView modeAndView = new ModelAndView(AdminStockInfoDefine.INFO_PATH);
        LogUtil.startLog(AdminStockInfoController.class.toString(), AdminStockInfoDefine.ADMINSTOCKINFO_ADD_OR_SAVE);
        if(form.getId()!=null){
            this.adminStockInfoService.updateAdminStockInfo(form);
        }else{ 
            this.adminStockInfoService.insertAdminStockInfo(form);
        }
        modeAndView.addObject(AdminStockInfoDefine.SUCCESS, AdminStockInfoDefine.SUCCESS);
        LogUtil.startLog(AdminStockInfoController.class.toString(), AdminStockInfoDefine.ADMINSTOCKINFO_ADD_OR_SAVE);
        return modeAndView;
    }
    
    /**
     * 删除信息
     * 
     * @param request
     * @return
     */
    @RequestMapping(AdminStockInfoDefine.ADMINSTOCKINFO_DELETE)
    public String adminStockInfoDeleteAction(HttpServletRequest request) {
        LogUtil.startLog(AdminStockInfoController.class.toString(), AdminStockInfoDefine.ADMINSTOCKINFO_DELETE);
        String userId = request.getParameter("id");
        if(userId!=null&&userId!=""){
            this.adminStockInfoService.deleteAdminStockInfo(Integer.valueOf(userId));
        }
        LogUtil.startLog(AdminStockInfoController.class.toString(), AdminStockInfoDefine.ADMINSTOCKINFO_DELETE);
        return "redirect:" + AdminStockInfoDefine.REQUEST_MAPPING + "/" + AdminStockInfoDefine.ADMINSTOCKINFO_LIST ;
    }
}
