
package com.hyjf.admin.manager.activity.actnov2017.draw;

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
import com.hyjf.mybatis.model.auto.ActJanAnswerList;
import com.hyjf.mybatis.model.auto.ActJanQuestions;

/**
 * 双十一活动   我画你猜
 * @author sunss
 */
@Controller
@RequestMapping(value = ActDrawGuessDefine.REQUEST_MAPPING)
public class ActDrawGuessController extends BaseController {

	@Autowired
	private ActDrawGuessService actQuestionTenService;

	 /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ActDrawGuessDefine.INIT)
    @RequiresPermissions(ActDrawGuessDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, ActDrawGuessBean form) {
        LogUtil.startLog(ActDrawGuessController.class.toString(), ActDrawGuessDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(ActDrawGuessDefine.LIST_PATH);
        
        // 创建分页
        this.createPage(request, modelAndView, form);
        
        //查询题目列表
        List<ActJanQuestions> questionsList = actQuestionTenService.getAllQuestions();
        modelAndView.addObject(ActDrawGuessDefine.QUESTION_LIST, questionsList);
        
        LogUtil.endLog(ActDrawGuessController.class.toString(), ActDrawGuessDefine.INIT);
        return modelAndView;
    }

    /**
     * 创建分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, ActDrawGuessBean form) {
        Integer count = this.actQuestionTenService.selectRecordCount(form);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            List<ActJanAnswerList> recordList = this.actQuestionTenService.getRecordList(form,paginator.getOffset(),paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        modelAndView.addObject(ActDrawGuessDefine.FORM, form);
    }

}
