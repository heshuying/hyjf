/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.nifa.upload;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.NifaReportLog;

import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version NifaUploadFileService, v0.1 2018/7/5 17:43
 */
public interface NifaUploadFileService extends BaseService {

    /**
     * 登记系统生成zip文件
     *
     * @param businessDataFileName
     * @param contractTemplateFileName
     * @return
     */
    boolean insertMakeFileReportLog(String businessDataFileName, String contractTemplateFileName);

    /**
     * 获取上传失败日志
     *
     * @return
     */
    List<NifaReportLog> selectNifaReportLogList();

    /**
     * 解析实时反馈文件
     *
     * @param filePathName
     * @return
     */
    String UploadResultFileRead(String filePathName);

    /**
     * 更新上传日志信息
     *
     * @param nifaReportLog
     * @return
     */
    boolean updateNifaReportLog(NifaReportLog nifaReportLog);

    /**
     * 统计系统生成zip文件
     *
     * @param businessZhaiqFileName
     * @param beforeSdfDay
     * @return
     */
    boolean insertMonitorMakeZhaiFileReportLog(String businessZhaiqFileName, String beforeSdfDay);

    /**
     * 统计系统生成zip文件
     *
     * @param businessJinrFileName
     * @param beforeSdfDay
     * @return
     */
    boolean insertMonitorMakeJinrFileReportLog(String businessJinrFileName, String beforeSdfDay);
}
