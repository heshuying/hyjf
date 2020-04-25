/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.hgdatareport.entity;

import com.hyjf.mongo.hgdatareport.base.BaseHgDataReportEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author PC-LIUSHOUYI
 * @version NifaHistoryDataEntity, v0.1 2018/12/12 15:40
 */
@Document(collection = "ht_nifa_history_data")
public class NifaHistoryDataEntity extends BaseHgDataReportEntity implements Serializable {

    /**
     * 当前日期数据是否处理 0：未处理 1：已处理
     */
    private String isDualData;
    /**
     * 当前日期数据是否生成文件 0：未生成 1：已生成
     */
    private String isDualFile;
    /**
     * 记录一些错误信息
     */
    private String errMsg;

    public String getIsDualData() {
        return isDualData;
    }

    public void setIsDualData(String isDualData) {
        this.isDualData = isDualData;
    }

    public String getIsDualFile() {
        return isDualFile;
    }

    public void setIsDualFile(String isDualFile) {
        this.isDualFile = isDualFile;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
