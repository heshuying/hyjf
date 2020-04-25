/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.mapper.customize.nifa;

import com.hyjf.mybatis.model.auto.NifaReportLog;

import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version NifaDownloadFileCustomizeMapper, v0.1 2018/7/7 18:39
 */
public interface NifaReportLogCustomizeMapper {
    /**
     * 拉取当天上传的数据
     * @return
     */
    List<NifaReportLog> selectNowDayUploadDataList();
}
