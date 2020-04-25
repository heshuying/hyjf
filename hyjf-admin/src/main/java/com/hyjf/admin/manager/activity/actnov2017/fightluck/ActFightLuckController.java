
package com.hyjf.admin.manager.activity.actnov2017.fightluck;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.mybatis.model.auto.ActRewardList;

/**
 * 双十一活动   我画你猜
 * @author sunss
 */
@Controller
@RequestMapping(value = ActFightLuckDefine.REQUEST_MAPPING)
public class ActFightLuckController extends BaseController {

	@Autowired
	private ActFightLuckService actQuestionTenService;

	 /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ActFightLuckDefine.INIT)
    @RequiresPermissions(ActFightLuckDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request) {
        LogUtil.startLog(ActFightLuckController.class.toString(), ActFightLuckDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(ActFightLuckDefine.LIST_PATH);
        
        // 创建分页
        this.createPage(request, modelAndView);
        LogUtil.endLog(ActFightLuckController.class.toString(), ActFightLuckDefine.INIT);
        return modelAndView;
    }

    /**
     * 创建分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView) {
        List<ActRewardList> recordList = this.actQuestionTenService.getRecordList();
        modelAndView.addObject("recordList", recordList);
    }

}
