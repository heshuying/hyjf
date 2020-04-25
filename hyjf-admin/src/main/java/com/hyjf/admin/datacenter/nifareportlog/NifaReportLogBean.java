/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.datacenter.nifareportlog;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.NifaFieldDefinition;
import com.hyjf.mybatis.model.auto.NifaReportLog;

import java.io.Serializable;
import java.util.List;

/**
 * @author nxl
 * @version NifaReportLogBean, v0.1 2018/7/6 15:35
 */
public class NifaReportLogBean implements Serializable {

    private List<NifaReportLogResponseBean> recordList;
    //id
    private Integer id;
    //文件包信息
    private String packageInformation;
    //上传时间 开始
    private String uploadImeStart;
    //上传时间 结束
    private String uploadImeEnd;
    // 数据处理日期
    private String historyData;
    //文件上传状态
    private String fileUploadStatus;
    //文件解析反馈
    private String feedbackResult;
    //上传文件包名
    private String uploadName;
    //反馈文件包名
    private String feedbackName;
    //上传文件包路径
    private String uploadPath;
    //反馈文件包路径
    private String feedbackPath;
    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;
    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;

    public int getPaginatorPage() {
        if (paginatorPage == 0) {
            paginatorPage = 1;
        }
        return paginatorPage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPackageInformation() {
        return packageInformation;
    }

    public void setPackageInformation(String packageInformation) {
        this.packageInformation = packageInformation;
    }

    public String getUploadImeStart() {
        return uploadImeStart;
    }

    public void setUploadImeStart(String uploadImeStart) {
        this.uploadImeStart = uploadImeStart;
    }

    public String getUploadImeEnd() {
        return uploadImeEnd;
    }

    public void setUploadImeEnd(String uploadImeEnd) {
        this.uploadImeEnd = uploadImeEnd;
    }

    public String getFileUploadStatus() {
        return fileUploadStatus;
    }

    public void setFileUploadStatus(String fileUploadStatus) {
        this.fileUploadStatus = fileUploadStatus;
    }

    public String getFeedbackResult() {
        return feedbackResult;
    }

    public void setFeedbackResult(String feedbackResult) {
        this.feedbackResult = feedbackResult;
    }

    public String getUploadName() {
        return uploadName;
    }

    public void setUploadName(String uploadName) {
        this.uploadName = uploadName;
    }

    public String getFeedbackName() {
        return feedbackName;
    }

    public void setFeedbackName(String feedbackName) {
        this.feedbackName = feedbackName;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String getFeedbackPath() {
        return feedbackPath;
    }

    public void setFeedbackPath(String feedbackPath) {
        this.feedbackPath = feedbackPath;
    }

    public void setPaginatorPage(int paginatorPage) {
        this.paginatorPage = paginatorPage;
    }

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public List<NifaReportLogResponseBean> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<NifaReportLogResponseBean> recordList) {
        this.recordList = recordList;
    }

    public String getHistoryData() {
        return historyData;
    }

    public void setHistoryData(String historyData) {
        this.historyData = historyData;
    }
}
