/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.nifa.download;

import com.hyjf.common.calculate.DateUtils;
import com.hyjf.mybatis.model.auto.NifaReportLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version NifaDownloadFileJobTask, v0.1 2018/7/4 15:40
 */
public class NifaDownloadFileTask {

    Logger _log = LoggerFactory.getLogger(NifaDownloadFileTask.class);

    /**
     * 运行状态
     */
    private static int isRun = 0;

    @Autowired
    NifaDownloadFileService nifaDownloadFileService;

    public void run() {
        if (isRun == 0) {
            isRun = 1;
            _log.info("------【互金下载反馈文件】处理开始------");
            try {

                SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");

                // 获取当天日期yyyyMMdd
                String nowDay = yyyyMMdd.format(new Date());
                // 获取未成功下载日志的数据
                List<NifaReportLog> nifaReportLogList = nifaDownloadFileService.selectNifaReportLogDownloadPath();
                if (null == nifaReportLogList || nifaReportLogList.size() <= 0) {
                    _log.info("【互金下载反馈文件】未获取到需要下载反馈文件的数据！");
                    return;
                }

                // 循环文件名集合
                for (NifaReportLog nifaReportLog : nifaReportLogList) {
                    Integer downloadResult = 0;
                    // 记录更新时间
                    nifaReportLog.setUpdateTime(new Date());
                    String feedBackType = nifaReportLog.getUploadName().substring(26,28);
                    String filePathDate = nifaReportLog.getUploadName().substring(18,26);
                    // 登记接口下载
                    if ("33".equals(feedBackType) || "34".equals(feedBackType)){
                        downloadResult = this.nifaDownloadFileService.downloadFiles(filePathDate) ? 1 : 2;
                        // 文件名称
                        nifaReportLog.setFeedbackName(filePathDate);
                    }
                    // 统计二期接口下载
                    if ("24".equals(feedBackType) || "26".equals(feedBackType)){
                        downloadResult = this.nifaDownloadFileService.downloadFilesByUrl(nifaReportLog,feedBackType,filePathDate) ? 1 : 2;
                        // 文件名称
                        nifaReportLog.setFeedbackName(nifaReportLog.getUploadName().concat("1"));
                    }
                    // 更新结果
                    nifaReportLog.setFeedbackResult(downloadResult);
                    // 文件地址
                    nifaReportLog.setFeedbackPath(filePathDate);
                    // 更新下载结果
                    boolean result = nifaDownloadFileService.updateNifaReportLog(nifaReportLog);
                    if (!result) {
                        _log.error("【互金下载反馈文件】更新下载结果失败!");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                _log.info("------【互金下载反馈文件】处理结束------");
                isRun = 0;
            }
        }
    }
}
