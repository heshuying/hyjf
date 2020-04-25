/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.hgdatareport.nifa;

import com.hyjf.batch.BaseService;
import com.hyjf.mongo.hgdatareport.entity.NifaHistoryDataEntity;

/**
 * @author PC-LIUSHOUYI
 * @version NifaHistoryDataDualService, v0.1 2018/12/12 16:33
 */
public interface NifaHistoryDataDualService extends BaseService {

    /**
     * 初始化当天数据处理和生成文件信息
     *
     * @param historyData
     * @param isDualData
     * @param isDualFile
     * @param errMsg
     */
    void saveNifaMongoStatus(String historyData, int isDualData, int isDualFile, String errMsg);

    /**
     * 更新当天数据处理和生成文件信息
     *
     * @param historyData
     * @param isDualData
     * @param isDualFile
     * @param errMsg
     */
    void updateNifaMongoStatus(String historyData, int isDualData, int isDualFile, String errMsg);

    /**
     * 处理当天放款还款债转数据
     *
     * @param historyData
     * @return
     */
    boolean selectDualData(String historyData);

    /**
     * 获取该天处理状态
     *
     * @param historyData
     * @return
     */
    NifaHistoryDataEntity selectHistoryData(String historyData);
}
