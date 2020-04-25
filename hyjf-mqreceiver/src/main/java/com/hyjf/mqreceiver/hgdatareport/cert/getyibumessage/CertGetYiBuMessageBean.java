/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.cert.getyibumessage;

/**
 * @author nxl
 * @version CertGetYiBuMessageBean, v0.1 2018/12/26 9:52
 */
public class CertGetYiBuMessageBean {
    // 批次号
    private String batchNum;
    // 执行入库结果：
    // execType=1为入库成功；execType=-1为入库失败。
    private String execType;
    // 如果异常，存储异常的数据和错误描述
    private String errorMsg;
    // 数据类型，返回正式数据或测试数据对应的查询结果
    private String dataType;
    // 执行失败的数量
    private String errorNum;
    // 本批次共执行的条数
    private String execNum;

    public String getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(String batchNum) {
        this.batchNum = batchNum;
    }

    public String getExecType() {
        return execType;
    }

    public void setExecType(String execType) {
        this.execType = execType;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(String errorNum) {
        this.errorNum = errorNum;
    }

    public String getExecNum() {
        return execNum;
    }

    public void setExecNum(String execNum) {
        this.execNum = execNum;
    }
}
