package com.hyjf.web.aboutus.compliance;

import com.hyjf.common.log.LogUtil;
import com.hyjf.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = ComplianceDefine.REQUEST_MAPPING)
public class ComplianceController extends BaseController {

    /**
     * 合规举措详情
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = ComplianceDefine.DETAIL)
    public ModelAndView detail(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(ComplianceController.class.toString(), ComplianceDefine.DETAIL);
        ModelAndView modelAndView = new ModelAndView(ComplianceDefine.DETAIL_PATH);
        LogUtil.endLog(ComplianceController.class.toString(), ComplianceDefine.DETAIL);
        return modelAndView;
    }

    /**
     * 合规举措日历
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = ComplianceDefine.LIST)
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(ComplianceController.class.toString(), ComplianceDefine.LIST);
        ModelAndView modelAndView = new ModelAndView(ComplianceDefine.LIST_PATH);
        LogUtil.endLog(ComplianceController.class.toString(), ComplianceDefine.LIST);
        return modelAndView;
    }
}
