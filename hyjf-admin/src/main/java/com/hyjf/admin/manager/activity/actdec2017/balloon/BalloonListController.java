package com.hyjf.admin.manager.activity.actdec2017.balloon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.activity.actoct2017.acttender.TenderRewardActivityBean;
import com.hyjf.admin.manager.activity.actoct2017.acttender.TenderRewardActivityController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.ActdecTenderBalloon;

/**
 * 
 * 双十二气球活动
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月12日
 * @see 下午4:58:59
 */
@Controller
@RequestMapping(value = BalloonListDefine.REQUEST_MAPPING)
public class BalloonListController extends BaseController {

	@Autowired
	private BalloonListService balloonListService;

	 /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BalloonListDefine.INIT)
    @RequiresPermissions(BalloonListDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, BalloonListBean form) {
        LogUtil.startLog(BalloonListController.class.toString(), BalloonListDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(BalloonListDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(BalloonListController.class.toString(), BalloonListDefine.INIT);
        return modelAndView;
    }

    /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BalloonListDefine.SEARCH_ACTION)
    @RequiresPermissions(BalloonListDefine.PERMISSIONS_SEARCH)
    public ModelAndView search(HttpServletRequest request, BalloonListBean form) {
        LogUtil.startLog(BalloonListController.class.toString(), BalloonListDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(BalloonListDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(BalloonListController.class.toString(), BalloonListDefine.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 创建分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, BalloonListBean form) {
    	Map<String, Object> paraMap = new HashMap<String, Object>();
    	 if(StringUtils.isNotEmpty(form.getUsernameSrch())){
             paraMap.put("usernameSrch", form.getUsernameSrch());
         }
         if(StringUtils.isNotEmpty(form.getMobileSrch())){
             paraMap.put("mobileSrch", form.getMobileSrch());
         }
         if(StringUtils.isNotEmpty(form.getTruenameSrch())){
             paraMap.put("truenameSrch", form.getTruenameSrch());
         }
         
        Integer count = balloonListService.countBalloonList(paraMap);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            List<ActdecTenderBalloon> recordList = balloonListService.selectRecordList(paraMap);
            form.setPaginator(paginator);
            
            modelAndView.addObject("recordList", recordList);
        }

        modelAndView.addObject(BalloonListDefine.ACTDEC_BALLOON_FORM, form);
    }
    
	 /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BalloonListDefine.INIT_DETAIL)
    @RequiresPermissions(BalloonListDefine.PERMISSIONS_VIEW)
    public ModelAndView initDetail(HttpServletRequest request, TenderRewardActivityBean form) {
        LogUtil.startLog(TenderRewardActivityController.class.toString(), BalloonListDefine.INIT_DETAIL);
        ModelAndView modelAndView = new ModelAndView(BalloonListDefine.LIST_PATH_DETAIL);

        // 创建分页
        this.createPageDetail(request, modelAndView, form);
        LogUtil.endLog(TenderRewardActivityController.class.toString(), BalloonListDefine.INIT_DETAIL);
        return modelAndView;
    }

    /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BalloonListDefine.SEARCH_ACTION_DETAIL)
    @RequiresPermissions(BalloonListDefine.PERMISSIONS_SEARCH)
    public ModelAndView searchDetail(HttpServletRequest request, TenderRewardActivityBean form) {
        LogUtil.startLog(TenderRewardActivityController.class.toString(), BalloonListDefine.SEARCH_ACTION_DETAIL);
        ModelAndView modelAndView = new ModelAndView(BalloonListDefine.LIST_PATH_DETAIL);
        // 创建分页
        this.createPageDetail(request, modelAndView, form);
        LogUtil.endLog(TenderRewardActivityController.class.toString(), BalloonListDefine.SEARCH_ACTION_DETAIL);
        return modelAndView;
    }

    /**
     * 创建分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPageDetail(HttpServletRequest request, ModelAndView modelAndView, TenderRewardActivityBean form) {
    	Integer userId = form.getUserId();
    	
        Integer count = balloonListService.countRecordListDetail(userId);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            List<ActdecTenderBalloon> recordList = balloonListService.selectRecordListDetail(userId,paginator.getOffset(),paginator.getLimit());
            form.setPaginator(paginator);
            
            modelAndView.addObject("recordList", recordList);
        }

        modelAndView.addObject(BalloonListDefine.ACTDEC_BALLOON_FORM, form);

    }



}
