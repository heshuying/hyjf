package com.hyjf.admin.datacenter.nifareportlog;

import java.io.Serializable;

public class NifaReportLogResponseBean implements Serializable {
    private Integer id;

    private String packageInformation;

    private Integer uploadIme;

    private Integer fileUploadStatus;

    private Integer feedbackResult;

    private String uploadName;

    private String feedbackName;

    private String uploadPath;

    private String feedbackPath;

    private String strUpdateTime;

    private String historyData;

    private static final long serialVersionUID = 1L;

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
        this.packageInformation = packageInformation == null ? null : packageInformation.trim();
    }

    public Integer getUploadIme() {
        return uploadIme;
    }

    public void setUploadIme(Integer uploadIme) {
        this.uploadIme = uploadIme;
    }

    public Integer getFileUploadStatus() {
        return fileUploadStatus;
    }

    public void setFileUploadStatus(Integer fileUploadStatus) {
        this.fileUploadStatus = fileUploadStatus;
    }

    public Integer getFeedbackResult() {
        return feedbackResult;
    }

    public void setFeedbackResult(Integer feedbackResult) {
        this.feedbackResult = feedbackResult;
    }

    public String getUploadName() {
        return uploadName;
    }

    public void setUploadName(String uploadName) {
        this.uploadName = uploadName == null ? null : uploadName.trim();
    }

    public String getFeedbackName() {
        return feedbackName;
    }

    public void setFeedbackName(String feedbackName) {
        this.feedbackName = feedbackName == null ? null : feedbackName.trim();
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath == null ? null : uploadPath.trim();
    }

    public String getFeedbackPath() {
        return feedbackPath;
    }

    public void setFeedbackPath(String feedbackPath) {
        this.feedbackPath = feedbackPath == null ? null : feedbackPath.trim();
    }

    public String getStrUpdateTime() {
        return strUpdateTime;
    }

    public void setStrUpdateTime(String strUpdateTime) {
        this.strUpdateTime = strUpdateTime;
    }

    public String getHistoryData() {
        return historyData;
    }

    public void setHistoryData(String historyData) {
        this.historyData = historyData;
    }
}