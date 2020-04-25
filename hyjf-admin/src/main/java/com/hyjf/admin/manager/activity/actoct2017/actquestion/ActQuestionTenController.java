package com.hyjf.admin.manager.activity.actoct2017.actquestion;

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
import com.hyjf.mybatis.model.auto.ActQuestions;
import com.hyjf.mybatis.model.auto.ActQuestionsAnswerlist;

/**
 * 十月份活动2   答题活动
 * @author sunss
 */
@Controller
@RequestMapping(value = ActQuestionTenDefine.REQUEST_MAPPING)
public class ActQuestionTenController extends BaseController {

	@Autowired
	private ActQuestionTenService actQuestionTenService;

	 /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ActQuestionTenDefine.INIT)
    @RequiresPermissions(ActQuestionTenDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, ActQuestionTenBean form) {
        LogUtil.startLog(ActQuestionTenController.class.toString(), ActQuestionTenDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(ActQuestionTenDefine.LIST_PATH);
        
        // 创建分页
        this.createPage(request, modelAndView, form);
        
        //查询题目列表
        List<ActQuestions> questionsList = actQuestionTenService.getAllQuestions();
        modelAndView.addObject(ActQuestionTenDefine.QUESTION_LIST, questionsList);
        
        LogUtil.endLog(ActQuestionTenController.class.toString(), ActQuestionTenDefine.INIT);
        return modelAndView;
    }

    /**
     * 创建分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, ActQuestionTenBean form) {
        Integer count = this.actQuestionTenService.selectRecordCount(form);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            List<ActQuestionsAnswerlist> recordList = this.actQuestionTenService.getRecordList(form,paginator.getOffset(),paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        modelAndView.addObject(ActQuestionTenDefine.FORM, form);
    }

}
