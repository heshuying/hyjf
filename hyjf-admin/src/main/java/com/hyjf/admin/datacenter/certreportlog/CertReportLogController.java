/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.datacenter.certreportlog;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.CertLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 合规数据上报 CERT 应急中心上报记录
 */
@Controller
@RequestMapping(value = CertReportLogDefine.REQUEST_MAPPING)
public class CertReportLogController {

    @Autowired
    private CertReportLogService certReportLogService;
    Logger _log = LoggerFactory.getLogger(CertReportLogController.class);


    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    /**
     * 权限维护画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(CertReportLogDefine.INIT)
    @RequiresPermissions(CertReportLogDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(CertReportLogDefine.REPORTLOG_FORM) CertReportLogBean form) {
        LogUtil.startLog(CertReportLogController.class.toString(), CertReportLogDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(CertReportLogDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(CertReportLogController.class.toString(), CertReportLogDefine.INIT);
        return modelAndView;
    }

    /**
     * 创建分页机能
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, CertReportLogBean form) {

        Integer certCount = certReportLogService.selectCertReportLogCount(form);
        if (null!=certCount&&certCount.intValue()>0) {

            Paginator paginator = new Paginator(form.getPaginatorPage(), certCount);
            List<CertLog> list = certReportLogService.selectCertReportLog(paginator.getOffset(), paginator.getLimit(),form);
            form.setPaginator(paginator);
            form.setRecordList(list);
            modelAndView.addObject(CertReportLogDefine.REPORTLOG_FORM, form);
        }
    }

    /**
     * 数据同步页面
     * @param request
     * @return
     */
    @RequestMapping(CertReportLogDefine.DATA_SYN)
    @RequiresPermissions(CertReportLogDefine.PERMISSIONS_MODIFY)
    public ModelAndView dataSynPage(HttpServletRequest request) {
        LogUtil.startLog(CertReportLogController.class.toString(), CertReportLogDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(CertReportLogDefine.DATA_SYN_PATH);
        LogUtil.endLog(CertReportLogController.class.toString(), CertReportLogDefine.INIT);
        return modelAndView;
    }

    /**
     * 数据同步
     * @param request
     * @param dataType
     * @return
     */
    @RequestMapping(CertReportLogDefine.DO_DATA_SYN)
    @RequiresPermissions(CertReportLogDefine.PERMISSIONS_MODIFY)
    @ResponseBody
    public JSONObject doDdataSyn(HttpServletRequest request,String dataType) {
        JSONObject result = new JSONObject();
        try {
            certReportLogService.doDdataSyn(dataType);
            result.put("status","success");
        }catch (Exception e){
            _log.error("数据同步出错",e);
            result.put("status","false");
        }
        return result;
    }



}
