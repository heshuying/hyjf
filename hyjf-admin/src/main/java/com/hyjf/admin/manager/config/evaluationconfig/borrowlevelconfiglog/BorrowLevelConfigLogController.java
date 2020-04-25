/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.config.evaluationconfig.borrowlevelconfiglog;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.config.evaluationconfig.evaluationchecklog.EvaluationCheckLogDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.EvaluationConfigLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 风险测评信用评级配置logController
 *
 * @author liuyang
 * @version BorrowLevelConfigLogController, v0.1 2018/11/29 10:00
 */
@Controller
@RequestMapping(BorrowLevelConfigLogDefine.REQUEST_MAPPING)
public class BorrowLevelConfigLogController extends BaseController {

    @Autowired
    private BorrowLevelConfigLogService borrowLevelConfigLogService;

    /**
     * 初始化页面
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BorrowLevelConfigLogDefine.INIT)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(BorrowLevelConfigLogDefine.BORROW_LEVEL_CONFIG_LOG_FORM) BorrowLevelConfigLogBean form) {
        LogUtil.startLog(BorrowLevelConfigLogController.class.toString(), BorrowLevelConfigLogDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(BorrowLevelConfigLogDefine.LIST_PATH);
        // 创建分页
        this.createPage(modelAndView, form);
        LogUtil.endLog(BorrowLevelConfigLogController.class.toString(), BorrowLevelConfigLogDefine.INIT);
        return modelAndView;
    }

    /**
     * 分页机能 页面初始化
     *
     * @param modelAndView
     * @param form
     */
    private void createPage(ModelAndView modelAndView, BorrowLevelConfigLogBean form) {
        Integer result = this.borrowLevelConfigLogService.countRecordList(form, -1, -1);
        if (result != 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), result);
            List<EvaluationConfigLog> recordList = this.borrowLevelConfigLogService.getRecordList(form, paginator.getOffset(),
                    paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
            modelAndView.addObject(BorrowLevelConfigLogDefine.BORROW_LEVEL_CONFIG_LOG_FORM, form);
        }
    }


    /**
     * 画面检索面板-检索按键画面请求
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BorrowLevelConfigLogDefine.SEARCH_ACTION)
    @RequiresPermissions(BorrowLevelConfigLogDefine.PERMISSIONS_SEARCH)
    public ModelAndView search(HttpServletRequest request, BorrowLevelConfigLogBean form) {
        LogUtil.startLog(BorrowLevelConfigLogController.class.toString(), BorrowLevelConfigLogDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(BorrowLevelConfigLogDefine.LIST_PATH);
        // 创建分页
        this.createPage(modelAndView, form);
        LogUtil.endLog(BorrowLevelConfigLogController.class.toString(), BorrowLevelConfigLogDefine.SEARCH_ACTION);
        return modelAndView;
    }
}
