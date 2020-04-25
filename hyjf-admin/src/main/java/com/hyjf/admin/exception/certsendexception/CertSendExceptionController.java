/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.exception.certsendexception;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.CertErrLog;
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
 * 合规数据上报 CERT 应急中心错误日志
 */
@Controller
@RequestMapping(value = CertSendExceptionDefine.REQUEST_MAPPING)
public class CertSendExceptionController {

    @Autowired
    private CertSendExceptionService certReportLogService;
    Logger _log = LoggerFactory.getLogger(CertSendExceptionController.class);

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
    @RequestMapping(CertSendExceptionDefine.INIT)
    @RequiresPermissions(CertSendExceptionDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(CertSendExceptionDefine.REPORTLOG_FORM) CertSendExceptionBean form) {
        LogUtil.startLog(CertSendExceptionController.class.toString(), CertSendExceptionDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(CertSendExceptionDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(CertSendExceptionController.class.toString(), CertSendExceptionDefine.INIT);
        return modelAndView;
    }

    /**
     * 创建分页机能
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, CertSendExceptionBean form) {

        Integer certCount = certReportLogService.selectCertErrLogCount(form);
        if (null!=certCount&&certCount.intValue()>0) {

            Paginator paginator = new Paginator(form.getPaginatorPage(), certCount);
            List<CertErrLog> list = certReportLogService.selectCertErrLog(paginator.getOffset(), paginator.getLimit(),form);
            form.setPaginator(paginator);
            form.setRecordList(list);
            modelAndView.addObject(CertSendExceptionDefine.REPORTLOG_FORM, form);
        }
    }


    /**
     * 重新跑批
     * @return
     */
    @RequestMapping(CertSendExceptionDefine.UPDATECOUNT)
    @RequiresPermissions(CertSendExceptionDefine.PERMISSIONS_MODIFY)
    @ResponseBody
    public JSONObject updateCount(HttpServletRequest request, Integer id) {
        JSONObject result = new JSONObject();
        try{
            certReportLogService.updateErrorCount(id);
            result.put("status","success");
        }catch (Exception e){
            result.put("status","false");
        }
        return result;
    }

    /**
     * MQ发送页面
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(CertSendExceptionDefine.SEND_MQ)
    @RequiresPermissions(CertSendExceptionDefine.PERMISSIONS_VIEW)
    public ModelAndView sendMQPage(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(CertSendExceptionDefine.REPORTLOG_FORM) CertSendExceptionBean form) {
        LogUtil.startLog(CertSendExceptionController.class.toString(), CertSendExceptionDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(CertSendExceptionDefine.SEND_MQ_PATH);
        LogUtil.endLog(CertSendExceptionController.class.toString(), CertSendExceptionDefine.INIT);
        return modelAndView;
    }

    /**
     * 发送MQ
     * @param request
     * @param dataType
     * @return
     */
    @RequestMapping(CertSendExceptionDefine.DO_SEND_MQ)
    @RequiresPermissions(CertSendExceptionDefine.PERMISSIONS_MODIFY)
    @ResponseBody
    public JSONObject doSendMQ(HttpServletRequest request,String dataType) {
        JSONObject result = new JSONObject();
        try {
            String value = request.getParameter("mqValue");
            _log.info("应急中心掉单处理，请求人【"+ShiroUtil.getLoginUserId()+"】，请求类型【"+dataType+"】，请求参数【"+value+"】");
            if("1".equals(dataType)){
                // 用户数据同步
                this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_EXCEPTION_TENDER_USER, value);
            }
            if("2".equals(dataType)){
                // 散标数据同步
                this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_EXCEPTION_BORROW_USER, value);
            }
            if("6".equals(dataType)){
                // 散标状态数据同步
                this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_EXCEPTION_BORROW_STATUS, value);
            }
            if("81".equals(dataType)){
                // 还款计划数据同步
                this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_EXCEPTION_BORROW_REPAYMENTPLAN,value);
            }
            if("82".equals(dataType)){
                // 债权信息数据同步
                this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_EXCEPTION_TENDER_INFO, value);
            }
            if("83".equals(dataType)){
                // 转让项目数据同步
                this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_EXCEPTION_CREDITINFO, value);
            }
            if("84".equals(dataType)){
                // 转让状态数据同步
                this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_EXCEPTION_CREDIT_STATUS, value);
            }
            if("85".equals(dataType)){
                // 承接信息数据同步
                this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_EXCEPTION_CREDITTENDERINFO, value);
            }
            if("4".equals(dataType)){
                // 交易流水数据同步
                this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_EXCEPTION_TRANSACT, value);
            }
            result.put("status","success");
        }catch (Exception e){
            _log.error("应急中心发送MQ出错",e);
            result.put("status","false");
        }
        return result;
    }
}
