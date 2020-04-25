package com.hyjf.admin.manager.activity.actnov2017.bargain;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.security.utils.BASE64;
import com.hyjf.mybatis.model.auto.ActJanBargain;
import com.hyjf.mybatis.model.auto.ActJanPrizewinList;

/**
 * 
 * 十月份活动出借返现
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月12日
 * @see 下午4:58:59
 */
@Controller
@RequestMapping(value = ActBargainDefine.REQUEST_MAPPING)
public class ActBargainController extends BaseController {

	@Autowired
	private ActBargainService actBargainService;

	 /**
     * 砍价页面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ActBargainDefine.INIT_BARGAIN)
    @RequiresPermissions(ActBargainDefine.PERMISSIONS_VIEW)
    public ModelAndView initBargain(HttpServletRequest request, ActBargainBean form) {
        LogUtil.startLog(ActBargainController.class.toString(), ActBargainDefine.INIT_BARGAIN);
        ModelAndView modelAndView = new ModelAndView(ActBargainDefine.LIST_PATH_BARGAIN);

        // 创建分页
        this.createPageBargain(request, modelAndView, form);
        LogUtil.endLog(ActBargainController.class.toString(), ActBargainDefine.INIT_BARGAIN);
        return modelAndView;
    }

    /**
     * 砍价画面检索
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ActBargainDefine.SEARCH_BARGAIN_ACTION)
    @RequiresPermissions(ActBargainDefine.PERMISSIONS_SEARCH)
    public ModelAndView searchBargain(HttpServletRequest request, ActBargainBean form) {
        LogUtil.startLog(ActBargainController.class.toString(), ActBargainDefine.SEARCH_BARGAIN_ACTION);
        ModelAndView modelAndView = new ModelAndView(ActBargainDefine.LIST_PATH_BARGAIN);
        // 创建分页
        this.createPageBargain(request, modelAndView, form);
        LogUtil.endLog(ActBargainController.class.toString(), ActBargainDefine.SEARCH_BARGAIN_ACTION);
        return modelAndView;
    }

    /**
     * 创建分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPageBargain(HttpServletRequest request, ModelAndView modelAndView, ActBargainBean form) {
        Integer count = actBargainService.countBargainList(form);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            List<ActJanBargain> recordList = actBargainService.selectBargainList(form, paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            
            for(ActJanBargain record : recordList){
            	if(StringUtils.isNotBlank(record.getWechatNickname())){
            		record.setWechatNickname(new String(BASE64.decode(record.getWechatNickname())));
            	}
            	
            	if(StringUtils.isNotBlank(record.getWechatNicknameHelp())){
            		record.setWechatNicknameHelp(new String(BASE64.decode(record.getWechatNicknameHelp())));
            	}
            }
            modelAndView.addObject("recordList", recordList);
        }

        modelAndView.addObject(ActBargainDefine.ACTNOV_BARGAIN_LIST_FORM, form);
    }

	 /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ActBargainDefine.INIT_PRIZEWIN)
    @RequiresPermissions(ActBargainDefine.PERMISSIONS_VIEW)
    public ModelAndView initPrizeWin(HttpServletRequest request, ActBargainBean form) {
        LogUtil.startLog(ActBargainController.class.toString(), ActBargainDefine.INIT_PRIZEWIN);
        ModelAndView modelAndView = new ModelAndView(ActBargainDefine.LIST_PATH_PRIZEWIN);

        // 创建分页
        this.createPagePrizeWin(request, modelAndView, form);
        LogUtil.endLog(ActBargainController.class.toString(), ActBargainDefine.INIT_PRIZEWIN);
        return modelAndView;
    }

    /**
     * 创建分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPagePrizeWin(HttpServletRequest request, ModelAndView modelAndView, ActBargainBean form) {
        Integer count = this.actBargainService.countPrizeWinList();
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            List<ActJanPrizewinList>  recordList = actBargainService.selectPrizeWinList(paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            
            modelAndView.addObject("recordList", recordList);
        }

        modelAndView.addObject(ActBargainDefine.ACTNOV_BARGAIN_LIST_FORM, form);
    }
    

}
