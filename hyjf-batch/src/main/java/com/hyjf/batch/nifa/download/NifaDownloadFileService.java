/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.nifa.download;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.NifaReportLog;

import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version NifaDownloadFileService, v0.1 2018/7/7 18:33
 */
public interface NifaDownloadFileService extends BaseService {

    /**
     * 更新上传日志信息
     *
     * @param nifaReportLog
     * @return
     */
    boolean updateNifaReportLog(NifaReportLog nifaReportLog);

    /**
     * 获取未成功下载反馈文件的数据
     *
     * @return
     */
    List<NifaReportLog> selectNifaReportLogDownloadPath();

    /**
     * SFTP下载文件
     *
     * @param filePathDate
     * @return
     */
    Boolean downloadFiles(String filePathDate);

    /**
     * 通过前置程序下载反馈文件
     *
     * @param nifaReportLog
     * @param feedBackType
     * @param filePathDate
     * @return
     */
    boolean downloadFilesByUrl(NifaReportLog nifaReportLog, String feedBackType, String filePathDate);
}
