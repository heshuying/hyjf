/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.datacenter.nifareportlog;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.manager.config.nifaconfig.FieldDefinitionBean;
import com.hyjf.admin.manager.config.nifaconfig.NifaConfigDefine;
import com.hyjf.common.file.FileUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.CouponCheck;
import com.hyjf.mybatis.model.auto.NifaReportLog;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author nixiaoling
 * @version NifaReportLogController, v0.1 2018/7/4 11:46
 */
@Controller
@RequestMapping(value = NifaReportLogDefine.REQUEST_MAPPING)
public class NifaReportLogController {

    @Autowired
    private NifaReportLogService nifaReportLogService;
    Logger _log = LoggerFactory.getLogger(NifaReportLogController.class);


    /**
     * 权限维护画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    //互金协会报送日志列表显示
    @RequestMapping(NifaReportLogDefine.INIT)
    @RequiresPermissions(NifaReportLogDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(NifaReportLogDefine.REPORTLOG_FORM) NifaReportLogBean form) {
        LogUtil.startLog(NifaReportLogController.class.toString(), NifaReportLogDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(NifaReportLogDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(NifaReportLogController.class.toString(), NifaReportLogDefine.INIT);
        return modelAndView;
    }

    /**
     * 创建分页机能(互金协会报送日志)
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, NifaReportLogBean form) {
        String strartUploadTime = null;
        String endUploadTime = null;
        if(StringUtils.isNotBlank(form.getUploadImeStart())){
            strartUploadTime = form.getUploadImeStart()+" 00:00:00";
        }
        if(StringUtils.isNotBlank(form.getUploadImeEnd())){
            endUploadTime = form.getUploadImeEnd()+" 23:59:59";
        }
        NifaReportLogBean nifaReportLogBeanSearcha = new NifaReportLogBean();
        nifaReportLogBeanSearcha.setFileUploadStatus(form.getFileUploadStatus());
        nifaReportLogBeanSearcha.setUploadImeStart(strartUploadTime);
        nifaReportLogBeanSearcha.setUploadImeEnd(endUploadTime);
        nifaReportLogBeanSearcha.setFeedbackResult(form.getFeedbackResult());
        nifaReportLogBeanSearcha.setHistoryData(StringUtils.isNotBlank(form.getHistoryData())?form.getHistoryData():"");
        List<NifaReportLog> nifaReportLogList = nifaReportLogService.selectNifaReportLog(-1,-1,nifaReportLogBeanSearcha);
        List<NifaReportLogResponseBean> nifaReportLogResponseBeanList = new ArrayList<NifaReportLogResponseBean>();
        SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null!=nifaReportLogList&&nifaReportLogList.size()>0) {

            Paginator paginator = new Paginator(form.getPaginatorPage(), nifaReportLogList.size());
            nifaReportLogList = nifaReportLogService.selectNifaReportLog(paginator.getOffset(), paginator.getLimit(),nifaReportLogBeanSearcha);
            for(NifaReportLog nifaReportLog:nifaReportLogList){
                NifaReportLogResponseBean nifaReportLogResponseBean = new NifaReportLogResponseBean();
                BeanUtils.copyProperties(nifaReportLog,nifaReportLogResponseBean);
                String strUpdTime = smp.format(nifaReportLog.getCreateTime());
                nifaReportLogResponseBean.setStrUpdateTime(strUpdTime);
                nifaReportLogResponseBeanList.add(nifaReportLogResponseBean);
            }
            form.setPaginator(paginator);
            form.setRecordList(nifaReportLogResponseBeanList);
            modelAndView.addObject(NifaReportLogDefine.REPORTLOG_FORM, form);
        }
    }
    /**
     * 下载文件包
     *
     * @param request
     * @param request
     * @return
     */
    @RequestMapping(value = NifaReportLogDefine.DOWNLOADFILE)
    public void downLoadFile(HttpServletRequest request,HttpServletResponse response,@ModelAttribute(NifaConfigDefine.HZRCONFIG_FORM) FieldDefinitionBean form) {
        String strFileId = form.getId();
        NifaReportLog nifaReportLog = new NifaReportLog();
        if(StringUtils.isNotBlank(strFileId)){
            nifaReportLog = nifaReportLogService.getNifaReportLogById(Integer.parseInt(strFileId));
        }
        _log.info("==============文件开始下载==============");
        if(null!=nifaReportLog){
            //文件上传状态为成功并且上传地址不为空
            if(StringUtils.isNotBlank(nifaReportLog.getUploadPath())){
                String fileName = nifaReportLog.getUploadName()+".zip";
                String downLoadPath = nifaReportLog.getUploadPath();
                if(!downLoadPath.endsWith("/")){
                    downLoadPath = downLoadPath+"/";
                }
                String donwFile = downLoadPath+fileName;
                _log.info("==============文件id:"+nifaReportLog.getId());
                _log.info("==============文件包信息:"+nifaReportLog.getPackageInformation());
                _log.info("==============文件名:"+fileName);
                _log.info("==============文件下载地址:"+donwFile);
                download(donwFile,fileName,response);
            }else{
                _log.error("==============下载文件不存在==============");
            }
        }
    }
    /**
     * 下载反馈文件
     *
     * @param request
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = NifaReportLogDefine.DOWNLOADFEEDBACKFILE,produces = "text/html;charset=UTF-8")
    public void downloadFeedbackFile(HttpServletRequest request,HttpServletResponse response,@ModelAttribute(NifaConfigDefine.HZRCONFIG_FORM) FieldDefinitionBean form) {
        String strFileId = form.getId();
        NifaReportLog nifaReportLog = new NifaReportLog();
        if(StringUtils.isNotBlank(strFileId)){
            nifaReportLog = nifaReportLogService.getNifaReportLogById(Integer.parseInt(strFileId));
        }
        if(null!=nifaReportLog){
            //文件上传状态为成功并且上传地址不为空
            if(StringUtils.isNotBlank(nifaReportLog.getFeedbackPath())){
                String fileName = nifaReportLog.getFeedbackName()+".zip";
                String downLoadPath = nifaReportLog.getFeedbackPath();
                if(!downLoadPath.endsWith("/")){
                    downLoadPath = downLoadPath+"/";
                }
                String donwFile = downLoadPath+fileName;
                _log.info("==============文件id:"+nifaReportLog.getId());
                _log.info("==============文件包信息:"+nifaReportLog.getPackageInformation());
                _log.info("==============反馈文件名:"+fileName);
                _log.info("==============反馈文件下载地址:"+donwFile);
                download(donwFile,fileName,response);
            }else{
                _log.error("==============反馈文件不存在==============");
            }
        }
    }

    /**
     * 下载
     * @param filePath
     * @param fileName
     * @param response
     */
    public void download(String filePath,String fileName, HttpServletResponse response) {
        try {
            response.setHeader("content-disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            response.setContentType("multipart/form-data");
            FileInputStream in = new FileInputStream(filePath);
            // 创建输出流
            OutputStream out = response.getOutputStream();
            // 创建缓冲区
            byte buffer[] = new byte[1024];
            int len = 0;
            // 循环将输入流中的内容读取到缓冲区中
            while ((len = in.read(buffer)) > 0) {
                // 输出缓冲区内容到浏览器，实现文件下载
                out.write(buffer, 0, len);
            }
            // 关闭文件流
            in.close();
            // 关闭输出流
            out.close();
        } catch (Exception e) {
            LogUtil.errorLog(NifaReportLogController.class.getName(), "NifaReportLogDown", e);
        }
    }
}
