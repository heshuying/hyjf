/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.hgdatareport.nifa;

import com.hyjf.batch.nifa.upload.NifaUploadFileService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mongo.hgdatareport.dao.NifaHistoryDataDao;
import com.hyjf.mongo.hgdatareport.entity.NifaHistoryDataEntity;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;

/**
 * @author PC-LIUSHOUYI
 * @version NifaHistoryDataDualTask, v0.1 2018/12/12 14:01
 */
public class NifaHistoryDataDualTask {

    Logger _log = LoggerFactory.getLogger(NifaHistoryDataDualTask.class);

    /**
     * 运行状态
     */
    private static int isRun = 0;

    @Autowired
    NifaHistoryDataDualService nifaHistoryDataDualService;

    @Autowired
    NifaUploadFileService nifaUploadFileService;

    @Autowired
    NifaHistoryDataDao nifaHistoryDataDao;

    public void run() {
        if (isRun == 0) {
            isRun = 1;
            try {
                // 未设置开始时间不执行
                String historyData = RedisUtils.get(RedisConstants.NIFA_START_DATE);
                if (StringUtils.isBlank(historyData)) {
                    return;
                }
                // 未设值结束时间不执行
                String endDate = RedisUtils.get(RedisConstants.NIFA_END_DATE);
                if (StringUtils.isBlank(endDate)) {
                    return;
                }
                // 未设置循环天数不执行
                String countStr = RedisUtils.get(RedisConstants.NIFA_DUAL_DAYS);
                if (StringUtils.isBlank(countStr) || "0".equals(countStr)) {
                    return;
                }

                _log.info("--------【互金历史数据】处理开始------");
                for (int i = 0; i < 1000; ) {
                    // 最大跑1000、防止redis设值不规范导致死循环
                    if (countStr.equals(i + "")) {
                        break;
                    }
                    // 日期执行到设定的结束日期时退出执行
                    if (historyData.equals(endDate)) {
                        break;
                    }
                    // 时间格式设定
                    SimpleDateFormat date_sdf = new SimpleDateFormat("yyyy-MM-dd");
                    // 已经处理成功的日期不再处理
                    if (historyData.equals("2016-10-07") || historyData.equals("2017-01-31") || historyData.equals("2017-02-03") || historyData.equals("2017-03-07") || historyData.equals("2019-01-03")) {
                        historyData = date_sdf.format(GetDate.countDate(historyData + " 00:00:00", 5, 1));
                        continue;
                    }
                    // 当天report表有数据continue不计入循环次数
                    // 查询mongo是否有当天的数据、没有发送mq重新生成
                    NifaHistoryDataEntity nifaHistoryDataEntity = nifaHistoryDataDualService.selectHistoryData(historyData);
                    // 查不到数据
                    if (null == nifaHistoryDataEntity) {
                        _log.info("【互金历史数据】开始处理当天放款还款债转数据{}！！！！！！！！！！", historyData);
                        // 处理当天放款还款债转数据
                        nifaHistoryDataDualService.selectDualData(historyData);
                        _log.info("【互金历史数据】结束处理当天放款还款债转数据:{}！！！！！！！！！！", historyData);
                        // 时间加一天
                        historyData = date_sdf.format(GetDate.countDate(historyData + " 00:00:00", 5, 1));
                        i = i + 1;
                    }
                    // 能查到数据判断数据状态
                    if (null != nifaHistoryDataEntity) {
                        if (StringUtils.isNotBlank(nifaHistoryDataEntity.getIsDualFile()) && "1".equals(nifaHistoryDataEntity.getIsDualFile())) {
                            // 时间加一天
                            historyData = date_sdf.format(GetDate.countDate(historyData + " 00:00:00", 5, 1));
                            // 已生成文件处理下一个日期
                            continue;
                        } else if (StringUtils.isNotBlank(nifaHistoryDataEntity.getIsDualData()) && "1".equals(nifaHistoryDataEntity.getIsDualData())) {
                            _log.info("【互金历史数据】开始处理当天文件做成开始:{}！！！！！！！！！！", historyData);
                            // 未生成文件已处理数据
                            // 根据数据生成当天文件
                            // 统计系统 24 互联网债权类融资
                            String businessZhaiqFileName = CustomConstants.COM_SOCIAL_CREDIT_CODE.concat(historyData.replaceAll("-", "")).concat("24001");
                            // 26 互联网金融产品及收益权转让融资
                            String businessJinrFileName = CustomConstants.COM_SOCIAL_CREDIT_CODE.concat(historyData.replaceAll("-", "")).concat("26001");
                            // 统计系统拉取数据生成文件并更新数据库、根据日期生成数据
                            boolean fileMakeJinrResult = false;
                            boolean fileMakeZhaiResult = nifaUploadFileService.insertMonitorMakeZhaiFileReportLog(businessZhaiqFileName, historyData);
                            if (!fileMakeZhaiResult) {
                                _log.error("【互金历史数据】统计债权系统文件作成失败！");
                                nifaHistoryDataDualService.updateNifaMongoStatus(historyData, 1, 0, "统计债权系统文件作成失败!");
                            } else {
                                fileMakeJinrResult = nifaUploadFileService.insertMonitorMakeJinrFileReportLog(businessJinrFileName, historyData);
                                if (!fileMakeJinrResult) {
                                    _log.error("【互金历史数据】统计转让系统文件作成失败！");
                                    nifaHistoryDataDualService.updateNifaMongoStatus(historyData, 1, 0, "统计转让系统文件作成失败!");
                                }
                            }
                            // 两种文件都完成后更新数据库
                            if (fileMakeJinrResult) {
                                // 更新mongo状态
                                nifaHistoryDataDualService.updateNifaMongoStatus(historyData, 1, 1, "");
                            }
                            _log.info("【互金历史数据】开始处理当天文件做成结束{}！！！！！！！！！！", historyData);
                            // 时间加一天
                            historyData = date_sdf.format(GetDate.countDate(historyData + " 00:00:00", 5, 1));
                            i = i + 1;
                        } else {
                            _log.info("【互金历史数据】开始再次处理当天放款还款债转数据{}！！！！！！！！！！", historyData);
                            // 处理当天放款还款债转数据
                            boolean result = nifaHistoryDataDualService.selectDualData(historyData);
                            // 更新mongo状态
                            if (result) {
                                nifaHistoryDataDualService.updateNifaMongoStatus(historyData, 1, 0, "");
                            } else {
                                nifaHistoryDataDualService.updateNifaMongoStatus(historyData, 0, 0, "拉取当天数据失败或mq发送失败！");
                            }
                            _log.info("【互金历史数据】结束再次处理当天放款还款债转数据{}！！！！！！！！！！", historyData);
                            // 时间加一天
                            historyData = date_sdf.format(GetDate.countDate(historyData + " 00:00:00", 5, 1));
                            i = i + 1;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 测试用代码：防止batch循环跑
                RedisUtils.set(RedisConstants.NIFA_DUAL_DAYS, "0");
                _log.info("------【互金历史数据】处理结束------");
                isRun = 0;
            }
        }
    }
}
