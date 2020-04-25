/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.exception.certsendexception;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.CertErrLog;
import com.hyjf.mybatis.model.auto.CertLog;

import java.io.Serializable;
import java.util.List;


public class CertSendExceptionBean implements Serializable {

    private List<CertErrLog> recordList;
    //id
    private Integer id;
    // 批次号
    private String logOrdId;
    // 请求类型
    private Integer infType;
    private String sendStartTimeStr;
    private String sendEndtTimeStr;
    // 发送状态
    private Integer sendStatus;
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

    public String getLogOrdId() {
        return logOrdId;
    }

    public void setLogOrdId(String logOrdId) {
        this.logOrdId = logOrdId;
    }

    public Integer getInfType() {
        return infType;
    }

    public void setInfType(Integer infType) {
        this.infType = infType;
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
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

    public List<CertErrLog> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<CertErrLog> recordList) {
        this.recordList = recordList;
    }

    public String getSendStartTimeStr() {
        return sendStartTimeStr;
    }

    public void setSendStartTimeStr(String sendStartTimeStr) {
        this.sendStartTimeStr = sendStartTimeStr;
    }

    public String getSendEndtTimeStr() {
        return sendEndtTimeStr;
    }

    public void setSendEndtTimeStr(String sendEndtTimeStr) {
        this.sendEndtTimeStr = sendEndtTimeStr;
    }
}
