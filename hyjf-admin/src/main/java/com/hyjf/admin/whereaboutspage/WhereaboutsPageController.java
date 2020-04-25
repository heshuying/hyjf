package com.hyjf.admin.whereaboutspage;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonController;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonDefine;
import com.hyjf.admin.manager.user.manageruser.ManageUsersService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.whereaboutspage.WhereaboutsPageConfigCustomize;

/**
 * 优惠券发行
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = WhereaboutsPageDefine.REQUEST_MAPPING)
public class WhereaboutsPageController extends BaseController {

	@Autowired
	private WhereaboutsPageService whereaboutsPageService;

	@Resource
    private ManageUsersService usersService;
	/**
	 * 画面初始化
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(WhereaboutsPageDefine.INIT)
	@RequiresPermissions(WhereaboutsPageDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(WhereaboutsPageDefine.FORM) WhereaboutsPageBean form) {
		LogUtil.startLog(WhereaboutsPageController.class.toString(), WhereaboutsPageDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(WhereaboutsPageDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(WhereaboutsPageController.class.toString(), WhereaboutsPageDefine.INIT);
		return modelAndView;
	}

	/**
	 * 查询
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(WhereaboutsPageDefine.SEARCH_ACTION)
	@RequiresPermissions(WhereaboutsPageDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(WhereaboutsPageDefine.FORM) WhereaboutsPageBean form) {
		LogUtil.startLog(WhereaboutsPageController.class.toString(), WhereaboutsPageDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(WhereaboutsPageDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(WhereaboutsPageController.class.toString(), WhereaboutsPageDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 分页机能
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, WhereaboutsPageBean form) {
        WhereaboutsPageConfigCustomize whereaboutsPageConfigCustomize=new WhereaboutsPageConfigCustomize();
        whereaboutsPageConfigCustomize.setReferrerName(form.getReferrerName());
        whereaboutsPageConfigCustomize.setTitleName(form.getTitleName());
        whereaboutsPageConfigCustomize.setUtmName(form.getUtmName());
        Integer count = this.whereaboutsPageService.countRecord(whereaboutsPageConfigCustomize);

		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			whereaboutsPageConfigCustomize.setLimitStart(paginator.getOffset());
			whereaboutsPageConfigCustomize.setLimitEnd(paginator.getLimit());
			List<WhereaboutsPageConfigCustomize>  recordList = this.whereaboutsPageService.getRecordList(whereaboutsPageConfigCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList",recordList);
		}
		modelAndView.addObject(WhereaboutsPageDefine.FORM, form);
	}
	
	

    /**
     * 删除
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(WhereaboutsPageDefine.DELETE_ACTION)
    @RequiresPermissions(WhereaboutsPageDefine.PERMISSIONS_DELETE)
    public String deleteAction(HttpServletRequest request,
            RedirectAttributes attr, WhereaboutsPageBean form) {
        LogUtil.startLog(WhereaboutsPageDefine.class.toString(),
                WhereaboutsPageDefine.DELETE_ACTION);
        this.whereaboutsPageService.deleteRecord(String.valueOf(form.getId()));
        attr.addFlashAttribute(WhereaboutsPageDefine.FORM, form);
        LogUtil.endLog(WhereaboutsPageDefine.class.toString(),
                WhereaboutsPageDefine.DELETE_ACTION);
        return "redirect:" + WhereaboutsPageDefine.REQUEST_MAPPING + "/"
                + WhereaboutsPageDefine.INIT;
    }

	
    
    /**
     * 迁移到详细画面
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = WhereaboutsPageDefine.INFO_ACTION)
    public ModelAndView infoAction(HttpServletRequest request, WhereaboutsPageBean form) {
        LogUtil.startLog(WhereaboutsPageController.class.toString(), WhereaboutsPageDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(WhereaboutsPageDefine.INFO_PATH);
        //样式
        List<ParamName> pageStyles = this.whereaboutsPageService.getParamNameList("WHEREABOUTS_STYLE");
		modelAndView.addObject("pageStyles", pageStyles);
        modelAndView.addObject(WhereaboutsPageDefine.FORM, form);
        LogUtil.endLog(WhereaboutsPageController.class.toString(), WhereaboutsPageDefine.INFO_ACTION);
        return modelAndView;
    }
    
    
    
    /**
     * 迁移到更新画面
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = WhereaboutsPageDefine.UPDATE_INFO_ACTION)
    public ModelAndView updateInfoAction(HttpServletRequest request, WhereaboutsPageBean form) {
        LogUtil.startLog(WhereaboutsPageController.class.toString(), WhereaboutsPageDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(WhereaboutsPageDefine.INFO_PATH);
        //样式
        List<ParamName> pageStyles = this.whereaboutsPageService.getParamNameList("WHEREABOUTS_STYLE");
		modelAndView.addObject("pageStyles", pageStyles);
        if(form.getId()!=null&&form.getId()!=0){
            whereaboutsPageService.getWhereaboutsPageConfigById(form);
        }
        modelAndView.addObject(WhereaboutsPageDefine.FORM, form);
        LogUtil.endLog(WhereaboutsPageController.class.toString(), WhereaboutsPageDefine.INFO_ACTION);
        return modelAndView;
    }
    
   
    /**
     * 添加着落页操作
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = WhereaboutsPageDefine.INSERT_ACTION)
    public ModelAndView insertAction(HttpServletRequest request, WhereaboutsPageBean form) {
        LogUtil.startLog(WhereaboutsPageController.class.toString(), WhereaboutsPageDefine.INSERT_ACTION);
        ModelAndView modelAndView = new ModelAndView(WhereaboutsPageDefine.LIST_PATH);
        if(form.getId()!=null&&form.getId()!=0){
            whereaboutsPageService.updateAction(form);
        }else{
            whereaboutsPageService.insertAction(form);
        }
       
        LogUtil.endLog(WhereaboutsPageController.class.toString(), WhereaboutsPageDefine.INSERT_ACTION);
        modelAndView = new ModelAndView("redirect:"+WhereaboutsPageDefine.REQUEST_MAPPING+"/"+WhereaboutsPageDefine.INIT);
        return modelAndView;
    }
    

    /**
     * 状态更新操作
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = WhereaboutsPageDefine.STATUS_ACTION)
    public ModelAndView statusAction(HttpServletRequest request, WhereaboutsPageBean form) {
        LogUtil.startLog(WhereaboutsPageController.class.toString(), WhereaboutsPageDefine.STATUS_ACTION);
        ModelAndView modelAndView = new ModelAndView("redirect:"+WhereaboutsPageDefine.REQUEST_MAPPING+"/"+WhereaboutsPageDefine.INIT);
        if(form.getStatusOn() == null || form.getId() == null){
            return modelAndView;
        }
        whereaboutsPageService.statusAction(form.getStatusOn(), form.getId());
       
        LogUtil.endLog(WhereaboutsPageController.class.toString(), WhereaboutsPageDefine.STATUS_ACTION);
        return modelAndView;
    }
    
	
    /**
     * 检查渠道
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = WhereaboutsPageDefine.CHECK_UTMID_ACTION)
    @ResponseBody
    public String checkUtmId(HttpServletRequest request, WhereaboutsPageBean form) {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.GETBORROWPRENID_ACTION);
      
        String msg = this.whereaboutsPageService.checkUtmId(request.getParameter("param"));
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.GETBORROWPRENID_ACTION);
        return msg;
    }
    
    /**
     * 检查推广人
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = WhereaboutsPageDefine.CHECK_REFERRER_ACTION)
    @ResponseBody
    public String checkReferrer(HttpServletRequest request, WhereaboutsPageBean form) {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.GETBORROWPRENID_ACTION);
        String msg = this.whereaboutsPageService.checkReferrer(request.getParameter("param"));
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.GETBORROWPRENID_ACTION);
        return msg;
    }
    
   
    /**
     * 资料上传
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = WhereaboutsPageDefine.UPLOAD_FILE, method = RequestMethod.POST)
    @ResponseBody
    public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogUtil.startLog(WhereaboutsPageController.class.toString(), WhereaboutsPageDefine.UPLOAD_FILE);
        String files = this.whereaboutsPageService.uploadFile(request, response);
        LogUtil.endLog(WhereaboutsPageController.class.toString(), WhereaboutsPageDefine.UPLOAD_FILE);
        return files;
    }
}
