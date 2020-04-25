package com.hyjf.admin.manager.config.bailconfiglog;

import com.hyjf.admin.BaseController;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.customize.admin.HjhBailConfigLogCustomize;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 现金贷资产方配置页面
 *
 * @author liushouyi
 */
@Controller
@RequestMapping(value = BailConfigLogDefine.REQUEST_MAPPING)
public class BailConfigLogController extends BaseController {

    @Autowired
    private BailConfigLogService bailConfigLogService;

    /**
     * 现保证金配置修改日志画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BailConfigLogDefine.INIT)
    @RequiresPermissions(BailConfigLogDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, @ModelAttribute(BailConfigLogDefine.BAILCONFIG_FORM) BailConfigLogBean form) {
        ModelAndView modelAndView = new ModelAndView(BailConfigLogDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        return modelAndView;
    }

    /**
     * 查找
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(BailConfigLogDefine.SEARCH_ACTION)
    @RequiresPermissions(BailConfigLogDefine.PERMISSIONS_SEARCH)
    public ModelAndView searchAction(HttpServletRequest request, @ModelAttribute(BailConfigLogDefine.BAILCONFIG_FORM) BailConfigLogBean form) {
        ModelAndView modelAndView = new ModelAndView(BailConfigLogDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        return modelAndView;
    }

    /**
     * 创建分页机能
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, BailConfigLogBean form) {

        // 资金来源
        List<HjhInstConfig> hjhInstConfigList = this.bailConfigLogService.hjhInstConfigList("");
        modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);

        HjhBailConfigLogCustomize hjhBailConfigLogCustomize = new HjhBailConfigLogCustomize();
        hjhBailConfigLogCustomize.setInstCodeSrch(form.getInstCodeSrch());
        hjhBailConfigLogCustomize.setModifyColumnSrch(form.getModifyColumnSrch());
        hjhBailConfigLogCustomize.setStartDate(form.getStartDate());
        hjhBailConfigLogCustomize.setEndDate(form.getEndDate());
        hjhBailConfigLogCustomize.setCreateUserNameSrch(form.getCreateUserNameSrch());

        int count = this.bailConfigLogService.countBailConfig(hjhBailConfigLogCustomize);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            hjhBailConfigLogCustomize.setLimitStart(paginator.getOffset());
            hjhBailConfigLogCustomize.setLimitEnd(paginator.getLimit());
            List<HjhBailConfigLogCustomize> record = bailConfigLogService.getRecordList(hjhBailConfigLogCustomize);
            form.setPaginator(paginator);
            form.setRecordList(record);
        }
        modelAndView.addObject(BailConfigLogDefine.BAILCONFIG_FORM, form);
    }
}
