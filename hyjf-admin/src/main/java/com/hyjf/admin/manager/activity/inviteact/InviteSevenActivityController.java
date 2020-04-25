package com.hyjf.admin.manager.activity.inviteact;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.ActivityInviteSeven;

/**
 * 
 * 七月推荐好友活动
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月12日
 * @see 下午4:58:59
 */
@Controller
@RequestMapping(value = InviteSevenActivityDefine.REQUEST_MAPPING)
public class InviteSevenActivityController extends BaseController {

	@Autowired
	private InviteSevenActivityService inviteSevenActivityService;

	 /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(InviteSevenActivityDefine.INIT)
    @RequiresPermissions(InviteSevenActivityDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, InviteSevenActivityBean form) {
        LogUtil.startLog(InviteSevenActivityController.class.toString(), InviteSevenActivityDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(InviteSevenActivityDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(InviteSevenActivityController.class.toString(), InviteSevenActivityDefine.INIT);
        return modelAndView;
    }

    /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(InviteSevenActivityDefine.SEARCH_ACTION)
    @RequiresPermissions(InviteSevenActivityDefine.PERMISSIONS_SEARCH)
    public ModelAndView search(HttpServletRequest request, InviteSevenActivityBean form) {
        LogUtil.startLog(InviteSevenActivityController.class.toString(), InviteSevenActivityDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(InviteSevenActivityDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(InviteSevenActivityController.class.toString(), InviteSevenActivityDefine.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 创建分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, InviteSevenActivityBean form) {

        Integer count = this.inviteSevenActivityService.selectRecordCount(form);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            List<ActivityInviteSeven> recordList = this.inviteSevenActivityService.selectRecordList(form, paginator);
            form.setPaginator(paginator);
            
            modelAndView.addObject("recordList", recordList);
        }

        modelAndView.addObject(InviteSevenActivityDefine.INVITESEVEN_FORM, form);
    }

    
}
