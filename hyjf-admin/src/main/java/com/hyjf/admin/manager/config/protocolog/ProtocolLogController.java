package com.hyjf.admin.manager.config.protocolog;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.config.protocol.ProtocolController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.ProtocolLog;
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
 * 协议模板管理日志
 * Created by xiehuili on 2018/6/4.
 */
@Controller
@RequestMapping(value = ProtocolTemplateLogDefine.REQUEST_MAPPING)
public class ProtocolLogController extends BaseController {
    @Autowired
    private ProtocolLogService protocolLogService;
    /**
     * 列表初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ProtocolTemplateLogDefine.INIT)
    @RequiresPermissions(ProtocolTemplateLogDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
                             @ModelAttribute(ProtocolTemplateLogDefine.FORM) ProtocolLogBean form) {
        LogUtil.startLog(ProtocolController.class.toString(), ProtocolTemplateLogDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(ProtocolTemplateLogDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(ProtocolTemplateLogDefine.class.toString(), ProtocolTemplateLogDefine.INIT);
        return modelAndView;
    }

    private void createPage(HttpServletRequest request, ModelAndView modelAndView, ProtocolLogBean form) {
        List<ProtocolLog> recordList = null;
        Integer count = protocolLogService.countRecord(-1, -1);
        if (count.intValue()>0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            recordList = protocolLogService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
            modelAndView.addObject(ProtocolTemplateLogDefine.FORM, form);
        }
    }



}
