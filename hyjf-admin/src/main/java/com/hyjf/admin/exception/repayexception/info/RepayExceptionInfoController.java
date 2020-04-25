package com.hyjf.admin.exception.repayexception.info;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.exception.repayexception.RepayExceptionDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.RepayExceptionCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-T
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = RepayExceptionInfoDefine.REQUEST_MAPPING)
public class RepayExceptionInfoController extends BaseController {

    @Autowired
    private RepayExceptionInfoService repaymentExceptionInfoService;

    // ****************************************还款掉单详情****************************************
    /**
     * 画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(RepayExceptionInfoDefine.INIT)
    @RequiresPermissions(RepayExceptionInfoDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, @ModelAttribute(RepayExceptionInfoDefine.REPAYMENT_FORM) RepayExceptionInfoBean form) {
        LogUtil.startLog(RepayExceptionInfoController.class.toString(), RepayExceptionInfoDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(RepayExceptionInfoDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(RepayExceptionInfoController.class.toString(), RepayExceptionInfoDefine.INIT);
        return modelAndView;
    }

    /**
     * 画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(RepayExceptionInfoDefine.SEARCH_ACTION)
    @RequiresPermissions(RepayExceptionInfoDefine.PERMISSIONS_SEARCH)
    public ModelAndView search(HttpServletRequest request, RepayExceptionInfoBean form) {
        LogUtil.startLog(RepayExceptionInfoController.class.toString(), RepayExceptionInfoDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(RepayExceptionInfoDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(RepayExceptionInfoController.class.toString(), RepayExceptionInfoDefine.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 创建分页
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, RepayExceptionInfoBean form) {
        RepayExceptionCustomize repayExceptionCustomize = new RepayExceptionCustomize();
        BeanUtils.copyProperties(form, repayExceptionCustomize);

        Long count = this.repaymentExceptionInfoService.countBorrowRepayment(repayExceptionCustomize);
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
            repayExceptionCustomize.setLimitStart(paginator.getOffset());
            repayExceptionCustomize.setLimitEnd(paginator.getLimit());
            List<RepayExceptionCustomize> recordList = this.repaymentExceptionInfoService.selectBorrowRepaymentList(repayExceptionCustomize);
            form.setPaginator(paginator);
            modelAndView.addObject("recordList", recordList);

            RepayExceptionCustomize sumObject = this.repaymentExceptionInfoService.sumBorrowRepaymentInfo(repayExceptionCustomize);
            modelAndView.addObject("sumObject", sumObject);
        }
        modelAndView.addObject(RepayExceptionInfoDefine.REPAYMENT_FORM, form);
    }

    // ****************************************还款详情****************************************
    /**
     * 跳转到详情
     *
     * @param request
     * @param form
     */
    @RequestMapping(RepayExceptionInfoDefine.TO_REPAY_ACTION)
    public ModelAndView toRecoverAction(HttpServletRequest request, RepayExceptionInfoBean form, RedirectAttributes attr) {
        LogUtil.startLog(RepayExceptionInfoController.class.toString(), RepayExceptionInfoDefine.TO_REPAY_ACTION);
        ModelAndView modelAndView = new ModelAndView(RepayExceptionDefine.RE_LIST_PATH);
        // attr.addAttribute("borrowNidParam", form.getBorrowNidHidden());
        // attr.addAttribute("borrowPeriodParam", form.getPeriodNowHidden());
        // 跳转到还款计划
        LogUtil.endLog(RepayExceptionInfoController.class.toString(), RepayExceptionInfoDefine.TO_REPAY_ACTION);
        return modelAndView;
    }

}
