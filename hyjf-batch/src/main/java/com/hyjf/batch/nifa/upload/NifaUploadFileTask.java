/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.nifa.upload;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.http.HttpDeal;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.NifaReportLog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version NifaUploadFileJobTask, v0.1 2018/7/4 15:40
 */
public class NifaUploadFileTask {

    /**
     * 运行状态
     */
    private static int isRun = 0;

    Logger _log = LoggerFactory.getLogger(NifaUploadFileTask.class);

    @Autowired
    NifaUploadFileService nifaUploadFileService;

    public void run() {
        if (isRun == 0) {
            isRun = 1;
            try {
                _log.info("------【互金上传文件】上传开始------");

                // 各种时间格式
                SimpleDateFormat date_sdf = new SimpleDateFormat("yyyy-MM-dd");
                // 各种时间格式
                SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");

                // 获取上传地址前缀
                String uploadURL = PropUtils.getSystem("hyjf.nifa.upload.url");
                // 获取18位社会信用代码
                String comSocialCreditCode = PropUtils.getSystem("hyjf.com.social.credit.code");

                // 获取实时反馈文件地址(信息获取之后删除文件)
                String feedBackPath = PropUtils.getSystem("hyjf.nifa.feedback.path");
                if (!feedBackPath.endsWith("/")) {
                    // 地址末尾撇拼接反斜杠
                    feedBackPath = feedBackPath.concat("/");
                }

                // 获取前一天日期yyyyMMdd
                String beforDay = yyyyMMdd.format(GetDate.countDate(new Date(), 5, -1));
                String beforeSdfDay = date_sdf.format(GetDate.countDate(new Date(), 5, -1));
                // 登记系统业务数据文件名
                String businessDataFileName = comSocialCreditCode.concat(beforDay).concat("33001");
                // 登记系统合同模板文件
                String contractTemplateFileName = comSocialCreditCode.concat(beforDay).concat("34001");
                // 统计系统 24 互联网债权类融资
                String businessZhaiqFileName = comSocialCreditCode.concat(beforDay).concat("24001");
                // 26 互联网金融产品及收益权转让融资
                String businessJinrFileName = comSocialCreditCode.concat(beforDay).concat("26001");

                // 登记系统拉取数据生成文件并更新数据库
                boolean fileMakeResult = nifaUploadFileService.insertMakeFileReportLog(businessDataFileName, contractTemplateFileName);
                if (!fileMakeResult) {
                    _log.error("【互金上传文件】登记系统文件作成失败！");
                }

                // 统计系统拉取数据生成文件并更新数据库、根据前一天日期生成数据
                fileMakeResult = nifaUploadFileService.insertMonitorMakeZhaiFileReportLog(businessZhaiqFileName, beforeSdfDay);
                if (!fileMakeResult) {
                    _log.error("【互金上传文件】统计债权系统文件作成失败！");
                }
                fileMakeResult = nifaUploadFileService.insertMonitorMakeJinrFileReportLog(businessJinrFileName, beforeSdfDay);
                if (!fileMakeResult) {
                    _log.error("【互金上传文件】统计转让系统文件作成失败！");
                }

                // 拉取未成功上传的文件名集合（可能会包含之前上传未成功的数据）
                List<NifaReportLog> nifaReportLogList = nifaUploadFileService.selectNifaReportLogList();
                if (nifaReportLogList == null || nifaReportLogList.size() == 0) {
                    _log.info("【互金上传文件】未获取到需要上传的数据！");
                    return;
                }

                // 循环文件名集合
                for (NifaReportLog nifaReportLog : nifaReportLogList) {
                    // 记录更新时间
                    nifaReportLog.setUpdateTime(new Date());

                    // 判断该条数据对应上传文件类型（后面有时间在数据库增加类型字段）
                    String uploadType = nifaReportLog.getUploadName().substring(26, 28);
                    String requestURL = "";
                    // 登记系统拼接请求地址
                    if ("33".equals(uploadType) || "34".equals(uploadType)) {
                        requestURL = uploadURL.concat("?systemid=2&stype=")
                                .concat(uploadType).concat("&sourcePath=")
                                .concat(nifaReportLog.getUploadPath())
                                .concat(nifaReportLog.getUploadName())
                                .concat(".zip");
                        requestURL.replace(":", "%3A");
                        requestURL.replace("/", "%2F");
                    }
                    // 统计系统拼接请求地址
                    if ("24".equals(uploadType) || "26".equals(uploadType)) {
                        requestURL = uploadURL.concat("?systemid=1&stype=")
                                .concat(uploadType).concat("&sourcePath=")
                                .concat(nifaReportLog.getUploadPath())
                                .concat(nifaReportLog.getUploadName())
                                .concat(".zip");
                        requestURL.replace(":", "%3A");
                        requestURL.replace("/", "%2F");
                    }
                    if(StringUtils.isBlank(requestURL)){
                        _log.error("【互金上传文件】文件上传地址拼接错误，文件id：" + nifaReportLog.getId());
                        continue;
                    }
                    // 文件上传请求
                    String uploadResult = HttpDeal.get(requestURL);
                    // 上传结果解析
                    JSONObject jsonObject = JSONObject.parseObject(uploadResult);
                    if (!"true".equals(jsonObject.get("success"))) {
                        _log.error("【互金上传文件】上传错误，返回错误信息：" + jsonObject + "文件id：" + nifaReportLog.getId());
                        // 更新上传结果
                        nifaReportLog.setFileUploadStatus(2);
                        nifaUploadFileService.updateNifaReportLog(nifaReportLog);
                        continue;
                    }

                    // 读取上传异步回调文件
                    // 异步回传文件地址与前置服务配置匹配写到环境变量
                    String filePathName = feedBackPath.concat(nifaReportLog.getUploadName()).concat(".txt");
                    String fileReadResult = nifaUploadFileService.UploadResultFileRead(filePathName);
                    if (StringUtils.isBlank(fileReadResult)) {
                        _log.error("【互金上传文件】互金上传文件反馈信息为空");
                        nifaReportLog.setFileUploadStatus(2);
                    } else if ("ERROR".equals(fileReadResult)) {
                        _log.error("【互金上传文件】上传反馈文件解析失败");
                        nifaReportLog.setFileUploadStatus(2);
                    } else {
                        _log.info("【互金上传文件】上传状态：" + fileReadResult);
                        nifaReportLog.setFileUploadStatus(1);
                        // 删除filePathName文件
                        File file = new File(filePathName);
                        if (file.exists() && file.isFile()) {
                            file.delete();
                        }
                    }
                    // 更新上传结果
                    nifaReportLog.setUploadIme(GetDate.getNowTime10());
                    nifaUploadFileService.updateNifaReportLog(nifaReportLog);
                }
            } catch (Exception e) {
                e.printStackTrace();
                _log.error("------【互金上传文件】上传失败------");
            } finally {
                _log.info("------【互金上传文件】上传结束------");
                isRun = 0;
            }
        }
    }
}