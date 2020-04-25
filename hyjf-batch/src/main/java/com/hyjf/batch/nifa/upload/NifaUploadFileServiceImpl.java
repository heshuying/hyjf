/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.nifa.upload;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.mongo.hgdatareport.dao.*;
import com.hyjf.mongo.hgdatareport.entity.*;
import com.hyjf.mybatis.model.auto.FddTemplet;
import com.hyjf.mybatis.model.auto.FddTempletExample;
import com.hyjf.mybatis.model.auto.NifaReportLog;
import com.hyjf.mybatis.model.auto.NifaReportLogExample;
import com.hyjf.mybatis.model.customize.nifa.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version NifaUploadFileServiceImpl, v0.1 2018/7/5 17:43
 */
@Service
public class NifaUploadFileServiceImpl extends BaseServiceImpl implements NifaUploadFileService {

    Logger _log = LoggerFactory.getLogger(NifaUploadFileService.class);

    @Autowired
    NifaBorrowInfoDao nifaBorrowInfoDao;

    @Autowired
    NifaTenderInfoDao nifaTenderInfoDao;

    @Autowired
    NifaBorrowerInfoDao nifaBorrowerInfoDao;

    @Autowired
    NifaCreditInfoDao nifaCreditInfoDao;

    @Autowired
    NifaCreditTransferDao nifaCreditTransferDao;

    /**
     * 合同要素信息 CSV文件名
     */
    private static String INV_CONTRACT = "inv_contract";
    /**
     * 合同模板约定条款 CSV文件名
     */
    private static String GENERAL_CONTRACT = "general_contract";
    /**
     * 借款人项目还款记录 CSV文件名
     */
    private static String PRO_REPAY_RECORD = "pro_repay_record";
    /**
     * 合同状态变更 CSV文件名
     */
    private static String CONTRACT_STATE = "contract_state";
    /**
     * 出借人回款记录 CSV文件名
     */
    private static String INV_RETURN_RECORD = "inv_return_record";
    /**
     * 合同模板 PDF文件名
     */
    private static String TEMPLE_NAME = "合同模板";

    /**
     * 生成产品登记业务数据zip合同模板文件zip
     *
     * @param businessDataFileName
     * @param contractTemplateFileName
     * @return
     */
    @Override
    public boolean insertMakeFileReportLog(String businessDataFileName, String contractTemplateFileName) {

        // 获取前一天日期yyyyMMdd
        String beforDay = GetDate.yyyyMMdd.format(GetDate.countDate(new Date(), 5, -1));

        boolean result = true;

        // 上传文件生成地址
        String uploadURL = PropUtils.getSystem("hyjf.nifa.upload.path");
        if (!uploadURL.endsWith("/")) {
            uploadURL = uploadURL.concat("/").concat(beforDay).concat("/");
        } else {
            uploadURL = uploadURL.concat(beforDay).concat("/");
        }

        // 需要打压缩zip的文件集合
        StringBuffer sb = new StringBuffer();
        // 打压缩zip合同模板
        StringBuffer sbTemplate = new StringBuffer();
        // 生成csv文件名中文集合
        StringBuffer fileName = new StringBuffer();

        // true说明文件已存在
        if (!this.selectNifaReportLogByFileName(contractTemplateFileName)) {
            // 合同模板约定条款
            List<NifaContractTemplateCustomize> nifaContractTemplate = nifaContractTemplateCustomizeMapper.selectNifaContractTemplate();
            if (null != nifaContractTemplate && nifaContractTemplate.size() > 0) {
                // 合同数据变更时压缩最新合同模板
                FddTemplet fddTemplet = this.selectFddTemplet(nifaContractTemplate.get(0).getTempletNid());
                result = downLoadFromUrl(fddTemplet.getFileUrl(), nifaContractTemplate.get(0).getTempletNid() + ".pdf", uploadURL);
                if (result) {
                    sbTemplate.append(uploadURL).append(nifaContractTemplate.get(0).getTempletNid()).append(".pdf,");
                    if (!writeZip(sbTemplate, uploadURL + contractTemplateFileName)) {
                        result = false;
                        _log.info("【互金上传文件】:合同数据变更时压缩最新合同模板失败！");
                    }
                }
                // 更新数据库插入一条数据
                // 上传日志增加记录
                // 插入上传日志
                NifaReportLog nifaReportLogTemplate = new NifaReportLog();
                // 处理历史数据日期
                nifaReportLogTemplate.setHistoryData(beforDay);
                // 文件包信息
                nifaReportLogTemplate.setPackageInformation(TEMPLE_NAME);
                // 上传时间：初始
                nifaReportLogTemplate.setUploadIme(0);
                // 上传状态：初始
                nifaReportLogTemplate.setFileUploadStatus(0);
                // 反馈文件解析状态：初始
                nifaReportLogTemplate.setFeedbackResult(0);
                // 上传文件包名
                nifaReportLogTemplate.setUploadName(contractTemplateFileName);
                // 上传文件路径
                nifaReportLogTemplate.setUploadPath(uploadURL);
                nifaReportLogTemplate.setCreateTime(new Date());
                nifaReportLogTemplate.setCreateUserId(3);
                boolean flag = nifaReportLogMapper.insertSelective(nifaReportLogTemplate) > 0 ? true : false;
                if (!flag) {
                    result = false;
                    _log.info("【互金上传文件】:合同模板约定条款、上传日志增加记录失败！");
                }
                // list类型转换
                List<Object> listNCT = toObject(nifaContractTemplate);
                // 导出csv文件
                _log.info("【互金上传文件】合同模板约定条款记录生成！！");
                if (createCSVFile(listNCT, null, uploadURL, GENERAL_CONTRACT, StringPool.ASCII_TABLE[01])) {
                    if (StringUtils.isNotBlank(fileName)) {
                        fileName.append(",");
                    }
                    sb.append(uploadURL).append(GENERAL_CONTRACT).append(".csv,");
                    fileName.append("合同模板约定条款");
                } else {
                    result = false;
                    _log.info("【互金上传文件】:合同模板约定条款导出CSV失败！");
                }
            }
        }

        if (!this.selectNifaReportLogByFileName(businessDataFileName)) {
            // 合同要素信息(记录出借信息)
            List<NifaContractEssenceCustomize> nifaContractEssenceList = nifaContractEssenceCustomizeMapper.selectNifaContractEssenceList();
            if (null != nifaContractEssenceList && nifaContractEssenceList.size() > 0) {
                // list类型转换
                List<Object> listNCE = toObject(nifaContractEssenceList);
                // 导出csv文件
                _log.info("【互金上传文件】合同要素信息记录生成！！");
                if (createCSVFile(listNCE, null, uploadURL, INV_CONTRACT, StringPool.ASCII_TABLE[01])) {
                    if (StringUtils.isNotBlank(fileName)) {
                        fileName.append(",");
                    }
                    sb.append(uploadURL).append(INV_CONTRACT).append(".csv,");
                    fileName.append("合同要素信息");
                } else {
                    result = false;
                    _log.info("【互金上传文件】:合同要素信息导出CSV失败！");
                }
            }

            // 借款人项目还款记录
            List<NifaRepayInfoCustomize> nifaRepayInfoList = nifaRepayInfoCustomizeMapper.selectNifaRepayInfoList();
            if (null != nifaRepayInfoList && nifaRepayInfoList.size() > 0) {
                // list类型转换
                List<Object> listNRI = toObject(nifaRepayInfoList);
                // 导出csv文件
                _log.info("【互金上传文件】借款人项目还款记录生成！！");
                if (createCSVFile(listNRI, null, uploadURL, PRO_REPAY_RECORD, StringPool.ASCII_TABLE[01])) {
                    if (StringUtils.isNotBlank(fileName)) {
                        fileName.append(",");
                    }
                    sb.append(uploadURL).append(PRO_REPAY_RECORD).append(".csv,");
                    fileName.append("借款人项目还款记录");
                } else {
                    result = false;
                    _log.info("【互金上传文件】:借款人项目还款记录导出CSV失败！");
                }
            }

            // 合同状态变更
            List<NifaContractStatusCustomize> nifaContractStatus = nifaContractStatusCustomizeMapper.selectNifaContractStatus();
            if (null != nifaContractStatus && nifaContractStatus.size() > 0) {
                // list类型转换
                List<Object> listNCS = toObject(nifaContractStatus);
                // 导出csv文件
                _log.info("【互金上传文件】合同状态变更记录生成！！");
                if (createCSVFile(listNCS, null, uploadURL, CONTRACT_STATE, StringPool.ASCII_TABLE[01])) {
                    if (StringUtils.isNotBlank(fileName)) {
                        fileName.append(",");
                    }
                    sb.append(uploadURL).append(CONTRACT_STATE).append(".csv,");
                    fileName.append("合同状态变更");
                } else {
                    result = false;
                    _log.info("【互金上传文件】:合同状态变更导出CSV失败！");
                }
            }

            // 出借人回款记录
            List<NifaReceivedPaymentsCustomize> nifaReceivedPaymentsList = nifaReceivedPaymentsCustomizeMapper.selectNifaReceivedPaymentsList();
            if (null != nifaReceivedPaymentsList && nifaReceivedPaymentsList.size() > 0) {
                // list类型转换
                List<Object> listNRP = toObject(nifaReceivedPaymentsList);
                // 导出csv文件
                _log.info("【互金上传文件】出借人回款记录生成！！");
                if (createCSVFile(listNRP, null, uploadURL, INV_RETURN_RECORD, StringPool.ASCII_TABLE[01])) {
                    if (StringUtils.isNotBlank(fileName)) {
                        fileName.append(",");
                    }
                    sb.append(uploadURL).append(INV_RETURN_RECORD).append(".csv,");
                    fileName.append("出借人回款记录");
                } else {
                    result = false;
                    _log.info("【互金上传文件】:出借人回款记录导出CSV失败！");
                }
            }
            // 上传日志增加记录
            if (result && StringUtils.isNotBlank(fileName)) {
                // 插入上传日志
                NifaReportLog nifaReportLog = new NifaReportLog();
                // 处理历史数据日期
                nifaReportLog.setHistoryData(beforDay);
                // 文件包信息
                nifaReportLog.setPackageInformation(fileName.toString());
                // 上传时间：初始
                nifaReportLog.setUploadIme(0);
                // 上传状态：初始
                nifaReportLog.setFileUploadStatus(0);
                // 反馈文件解析状态：初始
                nifaReportLog.setFeedbackResult(0);
                // 上传文件包名
                nifaReportLog.setUploadName(businessDataFileName);
                // 上传文件路径
                nifaReportLog.setUploadPath(uploadURL);
                nifaReportLog.setCreateTime(new Date());
                nifaReportLog.setCreateUserId(3);
                boolean flag = nifaReportLogMapper.insertSelective(nifaReportLog) > 0 ? true : false;
                if (!flag) {
                    result = false;
                    _log.info("【互金上传文件】:上传日志增加记录失败！");
                }
            }
            // zip打压缩包
            if (result && StringUtils.isNotBlank(sb)) {
                _log.info("【互金上传文件】压缩包生成！！");
                if (!writeZip(sb, uploadURL + businessDataFileName)) {
                    result = false;
                    _log.info("【互金上传文件】压缩包生成失败！！");
                }
            } else {
                _log.info("【互金上传文件】数据文件未生成！！");
                result = false;
            }
        }
        return result;
    }

    /**
     * 获取上传失败的日志
     *
     * @return
     */
    @Override
    public List<NifaReportLog> selectNifaReportLogList() {
        NifaReportLogExample example = new NifaReportLogExample();
        NifaReportLogExample.Criteria cra = example.createCriteria();
        // 获取状态不是成功的数据
        cra.andFileUploadStatusNotEqualTo(1);
        List<NifaReportLog> nifaReportLogList = nifaReportLogMapper.selectByExample(example);
        if (null != nifaReportLogList && nifaReportLogList.size() > 0) {
            return nifaReportLogList;
        }
        return null;
    }

    @Override
    public String UploadResultFileRead(String filePathName) {
        try {
            File file = new File(filePathName);
            StringBuilder localStrBulider = new StringBuilder();
            if (file.isFile() && file.exists()) {
                try {
                    FileInputStream inputStreamReader = new FileInputStream(file);
                    BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStreamReader, "UTF-8"));

                    String lineStr = null;
                    try {
                        while ((lineStr = bufferReader.readLine()) != null) {
                            System.out.println(lineStr);
                            localStrBulider.append(lineStr);
                        }
                        bufferReader.close();
                        inputStreamReader.close();
                        _log.info("【互金上传文件】上传状态异步返回文件解析成功，文件内容：" + localStrBulider.toString());
                        return localStrBulider.toString();
                    } catch (IOException e) {
                        _log.error("【互金上传文件】上传状态异步返回文件读取失败!");
                        e.printStackTrace();
                        return "ERROR";
                    }
                } catch (UnsupportedEncodingException e) {
                    _log.error("【互金上传文件】上传状态异步返回文件不支持编码格式!");
                    e.printStackTrace();
                    return "ERROR";
                }
            } else {
                _log.error("【互金上传文件】上传状态异步返回文件不存在!");
                return "ERROR";
            }
        } catch (Exception e) {
            _log.error("【互金上传文件】上传状态异步返回文件解析错误！");
            e.printStackTrace();
            return "ERROR";
        }
    }

    @Override
    public boolean updateNifaReportLog(NifaReportLog nifaReportLog) {
        boolean result = nifaReportLogMapper.updateByPrimaryKeySelective(nifaReportLog) > 0 ? true : false;
        if (!result) {
            _log.info("【互金上传文件】更新上传日志表失败！Id:" + nifaReportLog.getId());
        }
        return result;
    }

    @Override
    public boolean insertMonitorMakeZhaiFileReportLog(String businessZhaiqFileName, String beforeSdfDay) {

        // --> 生成标的信息文件
        Query query = new Query();
        Criteria criteria = Criteria.where("reportStatus").is("0").and("historyData").is(beforeSdfDay);
        query.addCriteria(criteria);
        List<NifaBorrowInfoEntity> nifaBorrowInfoEntities = nifaBorrowInfoDao.find(query);
        if (null == nifaBorrowInfoEntities || nifaBorrowInfoEntities.size() <= 0) {
            _log.info("【互金上传文件】:统计二期未查询到互联网债权类融资项目信息！");
            return true;
        }

        // 获取前一天日期yyyyMMdd
        String beforDay = beforeSdfDay.replaceAll("-", "");
        // 生成csv文件名中文集合
        String fileName = "互联网债权类融资项目信息，互联网债权类融资出借人信息，互联网债权类融资借款人信息";

        // 判定当前文件是否已生成
        businessZhaiqFileName = selectMaxFileName(businessZhaiqFileName, beforDay, fileName);
        if (StringUtils.isBlank(businessZhaiqFileName)) {
            _log.error("【互金上传文件】:统计二期互联网债权类融资项目信息文件名处理错误！");
            return false;
        }

        boolean result = true;
        // 上传文件生成地址
        String uploadURL = PropUtils.getSystem("hyjf.nifa.upload.path");
        if (!uploadURL.endsWith("/")) {
            uploadURL = uploadURL.concat("/").concat(beforDay).concat("/");
        } else {
            uploadURL = uploadURL.concat(beforDay).concat("/");
        }

        // 需要打压缩zip的文件集合
        StringBuffer sb = new StringBuffer();

        // list类型转换
        List<Object> listBIE = toObject(nifaBorrowInfoEntities);
        // 导出txt文件
        _log.info("【互金上传文件】统计二期标的信息生成txt！！");
        boolean re;
        try {
            re = createTxtFile(listBIE, null, uploadURL, CustomConstants.NIFA_BORROW_INFO_TYPE, "|");
            if (re) {
                sb.append(uploadURL).append(CustomConstants.NIFA_BORROW_INFO_TYPE).append(".txt,");
            } else {
                _log.error("【互金上传文件】:互联网债权类融资项目信息导出TXT失败！");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            _log.error("【互金上传文件】统计二期标的信息生成txt抛出异常！！");
            return false;
        }

        // 处理借款人和出借人数据
        for (NifaBorrowInfoEntity nifaBorrowInfoEntity : nifaBorrowInfoEntities) {
            // --> 获取出借人信息
            Query queryWait = new Query();
            Criteria criteriaWait = Criteria.where("message").is(nifaBorrowInfoEntity.getMessage()).and("reportStatus").is("0");
            queryWait.addCriteria(criteriaWait);
            // 更新需要导出文件的出借人数据为2：待处理
            List<NifaTenderInfoEntity> nifaTenderInfoEntities = this.nifaTenderInfoDao.find(queryWait);
            if (null != nifaTenderInfoEntities && nifaTenderInfoEntities.size() >= 0) {
                Update update = new Update();
                update.set("reportStatus", "2").set("updateTime", new Date());
                nifaTenderInfoDao.updateAll(queryWait, update);
            } else {
                _log.error("【互金上传文件】统计二期标获取标的对应的出借人信息失败！！message:" + nifaBorrowInfoEntity.getMessage());
            }
            // 更新需要导出文件的借款人数据为2：待处理
            List<NifaBorrowerInfoEntity> nifaBorrowerInfoEntities = this.nifaBorrowerInfoDao.find(queryWait);
            if (null != nifaBorrowerInfoEntities && nifaBorrowerInfoEntities.size() >= 0) {
                Update update = new Update();
                update.set("reportStatus", "2").set("updateTime", new Date());
                nifaBorrowerInfoDao.updateAll(queryWait, update);
            } else {
                _log.error("【互金上传文件】统计二期标获取标的对应的借款人信息失败！！message:" + nifaBorrowInfoEntity.getMessage());
            }
        }

        Query queryTender = new Query();
        Criteria criteriaTender = Criteria.where("reportStatus").is("2");
        queryTender.addCriteria(criteriaTender);
        List<NifaTenderInfoEntity> nifaTenderInfoEntities = this.nifaTenderInfoDao.find(queryTender);
        if (null == nifaTenderInfoEntities || nifaTenderInfoEntities.size() <= 0) {
            _log.error("【互金上传文件】统计二期标获取出借人信息失败！！");
            result = false;
        }
        // list类型转换
        List<Object> listTIE = toObject(nifaTenderInfoEntities);
        // 导出csv文件
        _log.info("【互金上传文件】统计二期标的信息生成txt！！");
        try {
            re = createTxtFile(listTIE, null, uploadURL, CustomConstants.NIFA_LENDER_INFO_TYPE, "|");
            if (re) {
                sb.append(uploadURL).append(CustomConstants.NIFA_LENDER_INFO_TYPE).append(".txt,");
                // 导出到文件后处理数据库数据为1:处理成功
                Update update = new Update();
                update.set("reportStatus", "1").set("updateTime", new Date());
                nifaTenderInfoDao.updateAll(queryTender, update);
            } else {
                result = false;
                _log.info("【互金上传文件】:互联网债权类融资出借人信息导出TXT失败！");
            }
        } catch (IOException e) {
            result = false;
            e.printStackTrace();
            _log.error("【互金上传文件】统计二期标的信息生成txt失败！！");
        }

        // --> 获取借款人信息
        Query queryBorrower = new Query();
        Criteria criteriaBorrower = Criteria.where("reportStatus").is("2");
        queryBorrower.addCriteria(criteriaBorrower);
        List<NifaBorrowerInfoEntity> nifaBorrowerInfoEntities = this.nifaBorrowerInfoDao.find(queryBorrower);
        if (null != nifaBorrowerInfoEntities && nifaBorrowerInfoEntities.size() > 0) {
            // list类型转换
            List<Object> listBerIE = toObject(nifaBorrowerInfoEntities);
            // 导出txt文件
            _log.info("【互金上传文件】统计二期标的信息生成txt！！");
            try {
                re = createTxtFile(listBerIE, null, uploadURL, CustomConstants.NIFA_BORROWER_INFO_TYPE, "|");
                if (re) {
                    sb.append(uploadURL).append(CustomConstants.NIFA_BORROWER_INFO_TYPE).append(".txt,");
                    Update update = new Update();
                    update.set("reportStatus", "1").set("updateTime", new Date());
                    // 导出到文件后处理数据库数据为1:处理成功
                    nifaBorrowerInfoDao.updateAll(queryBorrower, update);
                } else {
                    result = false;
                    _log.info("【互金上传文件】:互联网债权类融资借款人信息导出TXT失败！");
                }
            } catch (IOException e) {
                result = false;
                e.printStackTrace();
                _log.error("【互金上传文件】统计二期标的信息生成txt失败！！");
            }
        }

        // 上传日志增加记录
        if (result && StringUtils.isNotBlank(fileName)) {
            // 插入上传日志
            NifaReportLog nifaReportLog = new NifaReportLog();
            // 处理历史数据日期
            nifaReportLog.setHistoryData(beforDay);
            // 文件包信息
            nifaReportLog.setPackageInformation(fileName);
            // 上传时间：初始
            nifaReportLog.setUploadIme(0);
            // 上传状态：初始
            nifaReportLog.setFileUploadStatus(0);
            // 反馈文件解析状态：初始
            nifaReportLog.setFeedbackResult(0);
            // 上传文件包名
            nifaReportLog.setUploadName(businessZhaiqFileName);
            // 上传文件路径
            nifaReportLog.setUploadPath(uploadURL);
            nifaReportLog.setCreateTime(new Date());
            nifaReportLog.setCreateUserId(3);
            boolean flag = nifaReportLogMapper.insertSelective(nifaReportLog) > 0 ? true : false;
            if (!flag) {
                result = false;
                _log.info("【互金上传文件】:上传日志增加记录失败！");
            }
        }
        // zip打压缩包
        if (result && StringUtils.isNotBlank(sb)) {
            _log.info("【互金上传文件】统计二期压缩包生成！！");
            if (writeZip(sb, uploadURL + businessZhaiqFileName)) {
                Update update = new Update();
                update.set("reportStatus", "1").set("updateTime", new Date());
                nifaBorrowInfoDao.updateAll(query, update);
            } else {
                result = false;
                _log.info("【互金上传文件】统计二期压缩包生成失败！！");
            }
        } else {
            _log.info("【互金上传文件】统计二期数据文件未生成！！");
            result = false;
        }
        return result;
    }

    @Override
    public boolean insertMonitorMakeJinrFileReportLog(String businessJinrFileName, String beforeSdfDay) {

        // --> 生成债转标的信息文件
        Query query = new Query();
        Criteria criteria = Criteria.where("reportStatus").is("0").and("historyData").is(beforeSdfDay);
        query.addCriteria(criteria);
        List<NifaCreditInfoEntity> nifaCreditInfoEntities = nifaCreditInfoDao.find(query);
        if (null == nifaCreditInfoEntities || nifaCreditInfoEntities.size() <= 0) {
            _log.info("【互金上传文件】统计二期未查询到债转标的信息！！");
            return true;
        }

        // 获取处理日期yyyyMMdd
        String beforDay = beforeSdfDay.replaceAll("-", "");
        // 生成csv文件名中文集合
        String fileNameCredit = "互联网金融产品及收益权转让融资项目信息，互联网金融产品及收益权转让融资受让人信息";

        // 判定当前文件是否已生成
        businessJinrFileName = selectMaxFileName(businessJinrFileName, beforDay, fileNameCredit);
        if (StringUtils.isBlank(businessJinrFileName)) {
            _log.info("【互金上传文件】:统计二期债转标的信息文件名处理错误！");
            return false;
        }

        boolean result = true;
        // 上传文件生成地址
        String uploadURL = PropUtils.getSystem("hyjf.nifa.upload.path");
        if (!uploadURL.endsWith("/")) {
            uploadURL = uploadURL.concat("/").concat(beforDay).concat("/");
        } else {
            uploadURL = uploadURL.concat(beforDay).concat("/");
        }
        // 需要打压缩zip的文件集合
        StringBuffer sbCredit = new StringBuffer();

        // list类型转换
        List<Object> listCIE = toObject(nifaCreditInfoEntities);
        // 导出csv文件
        _log.info("【互金上传文件】统计二期债转标的信息生成txt！！");
        boolean re = false;
        try {
            re = createTxtFile(listCIE, null, uploadURL, CustomConstants.NIFA_CREDIT_INFO_TYPE, "|");
            if (re) {
                sbCredit.append(uploadURL).append(CustomConstants.NIFA_CREDIT_INFO_TYPE).append(".txt,");
            } else {
                result = false;
                _log.info("【互金上传文件】:互联网金融产品及收益权转让融资项目信息导出TXT失败！");
            }
        } catch (IOException e) {
            result = false;
            e.printStackTrace();
            _log.error("【互金上传文件】统计二期债转标的信息生成txt抛出异常！！");
        }

        // --> 生成债转标的信息文件
        for (NifaCreditInfoEntity nifaCreditInfoEntity : nifaCreditInfoEntities) {
            Query queryWait = new Query();
            Criteria criteriaWait = Criteria.where("message").is(nifaCreditInfoEntity.getMessage()).and("reportStatus").is("0");
            queryWait.addCriteria(criteriaWait);
            List<NifaCreditTransferEntity> nifaCreditTransferEntities = nifaCreditTransferDao.find(queryWait);
            if (null != nifaCreditTransferEntities && nifaCreditTransferEntities.size() > 0) {
                Update update = new Update();
                update.set("reportStatus", "2").set("updateTime", new Date());
                nifaCreditTransferDao.updateAll(queryWait, update);
            } else {
                _log.error("【互金上传文件】统计二期债转标的信息为查询到相对应的承接人信息！！message：" + nifaCreditInfoEntity.getMessage());
            }
        }

        Query queryCrediter = new Query();
        Criteria criteriaCrediter = Criteria.where("reportStatus").is("2");
        queryCrediter.addCriteria(criteriaCrediter);
        List<NifaCreditTransferEntity> nifaCreditTransferEntities = nifaCreditTransferDao.find(queryCrediter);
        if (null != nifaCreditTransferEntities && nifaCreditTransferEntities.size() > 0) {
            // list类型转换
            List<Object> listCerIE = toObject(nifaCreditTransferEntities);
            // 导出csv文件
            _log.info("【互金上传文件】统计二期债转标的信息生成txt！！");
            try {
                re = createTxtFile(listCerIE, null, uploadURL, CustomConstants.NIFA_CREDITER_INFO_TYPE, "|");
                if (re) {
                    sbCredit.append(uploadURL).append(CustomConstants.NIFA_CREDITER_INFO_TYPE).append(".txt,");
                    Update update = new Update();
                    update.set("reportStatus", "1").set("updateTime", new Date());
                    nifaCreditTransferDao.updateAll(queryCrediter, update);
                } else {
                    result = false;
                    _log.info("【互金上传文件】:审计二期债转债转标的承接人信息导出TXT失败！");
                }
            } catch (IOException e) {
                e.printStackTrace();
                _log.error("【互金上传文件】统计二期债转标的承接人信息生成txt抛出异常！！");
            }
        }

        // 上传日志增加记录
        if (result && StringUtils.isNotBlank(fileNameCredit)) {
            // 插入上传日志
            NifaReportLog nifaReportLog = new NifaReportLog();
            // 处理历史数据日期
            nifaReportLog.setHistoryData(beforDay);
            // 文件包信息
            nifaReportLog.setPackageInformation(fileNameCredit);
            // 上传时间：初始
            nifaReportLog.setUploadIme(0);
            // 上传状态：初始
            nifaReportLog.setFileUploadStatus(0);
            // 反馈文件解析状态：初始
            nifaReportLog.setFeedbackResult(0);
            // 上传文件包名
            nifaReportLog.setUploadName(businessJinrFileName);
            // 上传文件路径
            nifaReportLog.setUploadPath(uploadURL);
            nifaReportLog.setCreateTime(new Date());
            nifaReportLog.setCreateUserId(3);
            boolean flag = nifaReportLogMapper.insertSelective(nifaReportLog) > 0 ? true : false;
            if (!flag) {
                result = false;
                _log.info("【互金上传文件】:审计二期债转信息上传日志增加记录失败！");
            }
        }
        // zip打压缩包
        if (result && StringUtils.isNotBlank(sbCredit)) {
            _log.info("【互金上传文件】审计二期债转信息压缩包生成！！");
            if (writeZip(sbCredit, uploadURL + businessJinrFileName)) {
                Update update = new Update();
                update.set("reportStatus", "1").set("updateTime", new Date());
                nifaCreditInfoDao.updateAll(query, update);
            } else {
                result = false;
                _log.info("【互金上传文件】审计二期债转信息压缩包生成失败！！");
            }
        } else {
            _log.info("【互金上传文件】审计二期债转信息数据文件未生成！！");
            result = false;
        }
        return result;
    }

    /**
     * CSV文件生成方法
     *
     * @param dataList
     * @param head       标题、暂时不用
     * @param outPutPath
     * @param filename
     * @param col        分隔符
     * @return
     */
    private boolean createCSVFile(List<Object> dataList, List<Object> head, String outPutPath, String filename, String col) {

        boolean result = true;
        File csvFile = null;
        BufferedWriter csvWtriter = null;
        try {
            // 建立空csv文件
            csvFile = new File(outPutPath + File.separator + filename + ".csv");
            File parent = csvFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            csvFile.createNewFile();

            // 如果生产文件乱码，windows下用GBK，linux用UTF-8
            csvWtriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                    csvFile), "GBK"), 1024);
            // 写入文件内容
            for (Object data : dataList) {
                csvWtriter.write(objectToStr(data, col));
            }
//            csvWtriter.newLine();
            csvWtriter.flush();
        } catch (Exception e) {
            _log.error("【互金上传文件】文件生成失败！文件名：" + outPutPath + filename);
            e.printStackTrace();
            result = false;
        } finally {
            try {
                csvWtriter.close();
                return result;
            } catch (IOException e) {
                _log.error("【互金上传文件】文件关闭失败！文件名：" + outPutPath + filename);
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * 生成txt文件并写入数据
     *
     * @param dataList
     * @param head
     * @param outPutPath
     * @param filename
     * @param col        分隔符
     * @return
     */
    private boolean createTxtFile(List<Object> dataList, List<Object> head, String outPutPath, String filename, String col) throws IOException {
        boolean result = true;
        /* 写入Txt文件 */
        // 相对路径，如果没有则要建立一个新的output。txt文件
        File writename = null;
        BufferedWriter out = null;
        try {
            writename = new File(outPutPath + File.separator + filename + ".txt");
            // 父级文件夹不存在时创建一个
            File parent = writename.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            // 创建新文件
            writename.createNewFile();
            out = new BufferedWriter(new FileWriter(writename));
            // 写入文件内容
            for (Object data : dataList) {
                out.write(objectToStr(data, col));
            }
            // 把缓存区内容压入文件
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            // 最后关闭文件
            if (null != out) {
                out.close();
            }
            return result;
        }
    }

    /**
     * 实体类list转List<Object>
     *
     * @param list
     * @param <E>
     * @return
     */
    private <E> List<Object> toObject(List<E> list) {
        List<Object> objlist = new ArrayList<Object>();
        for (Object e : list) {
            Object obj = (Object) e;
            objlist.add(obj);
        }
        return objlist;
    }

    /**
     * 实体类拼接成String字符串
     *
     * @param model
     * @param col   分隔符
     * @return 解析错误返回空字符串
     */
    private String objectToStr(Object model, String col) {
        try {
            String result = "";
            // 获取实体类的所有属性，返回Field数组
            Field[] field = model.getClass().getDeclaredFields();
            // 获取属性的名字
            String[] modelName = new String[field.length];
            String[] modelType = new String[field.length];
            for (int i = 0; i < field.length; i++) {
                // 获取属性的名字
                String name = field[i].getName();
                modelName[i] = name;
                // 获取属性类型
                String type = field[i].getGenericType().toString();
                modelType[i] = type;

                //关键。。。可访问私有变量
                field[i].setAccessible(true);

                // 将属性的首字母大写
                name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1)
                        .toUpperCase());

                // 忽略serialVersionUID字段、保证serialVersionUID在实体类的最后一个
                if ("SerialVersionUID".equals(name)) {
                    continue;
                } else {
                    if (i != 0) {
                        // 拼接分割字符串SOH（ASCII码为01）
                        result = result.concat(col);
                    }
                    // 如果type是类类型，则前面包含"class "，后面跟类名
                    Method m = model.getClass().getMethod("get" + name);
                    // 调用getter方法获取属性值
                    if (null != m.invoke(model)) {
                        // 拼接各个字段的值，空的情况只拼接分隔符
                        result = result.concat(m.invoke(model).toString());
                    }
                }
            }
            // 去掉行数据中多余的回车换行
            result = result.replace(StringPool.ASCII_TABLE[13], "");
            result = result.replace(StringPool.ASCII_TABLE[10], "");
            // 末尾拼接回车换行
            result = result.concat(StringPool.ASCII_TABLE[13]).concat(StringPool.ASCII_TABLE[10]);
            // 拼接成功返回字符串
            return result;
        } catch (NoSuchMethodException e) {
            _log.error("【互金上传文件】拼接数据失败！");
            e.printStackTrace();
            return "";
        } catch (IllegalAccessException e) {
            _log.error("【互金上传文件】拼接数据失败！");
            e.printStackTrace();
            return "";
        } catch (InvocationTargetException e) {
            _log.error("【互金上传文件】拼接数据失败！");
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取居间服务协议模板上传信息
     *
     * @param templetId
     * @return
     */
    private FddTemplet selectFddTemplet(String templetId) {
        FddTempletExample example = new FddTempletExample();
        example.createCriteria().andTempletIdEqualTo(templetId);
        List<FddTemplet> fddTempletList = this.fddTempletMapper.selectByExample(example);
        if (null != fddTempletList && fddTempletList.size() > 0) {
            return fddTempletList.get(0);
        }
        return null;
    }

    private boolean downLoadFromUrl(String urlStr, String fileName, String savePath) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            //得到输入流
            InputStream inputStream = conn.getInputStream();
            //获取自己数组
            byte[] getData = readInputStream(inputStream);

            //文件保存位置
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            File file = new File(saveDir + File.separator + fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getData);
            if (fos != null) {
                fos.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            _log.info("【互金上传文件】info:" + url + " 下载成功。");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            _log.info("【互金上传文件】居间服务协议模板下载失败！");
            return false;
        }
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
     * 判断文件是否生成过
     *
     * @param fileName
     * @return
     */
    private boolean selectNifaReportLogByFileName(String fileName) {
        NifaReportLogExample example = new NifaReportLogExample();
        example.createCriteria().andUploadNameEqualTo(fileName);
        List<NifaReportLog> nifaReportLogList = this.nifaReportLogMapper.selectByExample(example);
        if (null != nifaReportLogList && nifaReportLogList.size() > 0) {
            return true;
        }
        return false;
    }

    private String selectMaxFileName(String fileName, String beforDate, String fileType) {
        // 已存在取最大文件名最后三位加一
        NifaReportLogExample example = new NifaReportLogExample();
        example.createCriteria().andHistoryDataEqualTo(beforDate).andPackageInformationEqualTo(fileType);
        example.setOrderByClause("upload_name desc");
        List<NifaReportLog> nifaReportLogList = this.nifaReportLogMapper.selectByExample(example);
        if (null != nifaReportLogList && nifaReportLogList.size() > 0) {
            fileName = addOne(nifaReportLogList.get(0).getUploadName());
        }
        return fileName;
    }

    /**
     * 字符串+1方法，该方法将其结尾的整数+1,适用于任何以整数结尾的字符串,不限格式，不限分隔符。
     *
     * @param testStr 要+1的字符串
     * @return +1后的字符串
     * @throws NumberFormatException
     * @author liushouyi
     */
    private String addOne(String testStr) {
        //根据不是数字的字符拆分字符串
        String[] strs = testStr.split("[^0-9]");
        //取出最后一组数字
        String numStr = strs[strs.length - 1];
        //如果最后一组没有数字(也就是不以数字结尾)，抛NumberFormatException异常
        if (numStr != null && numStr.length() > 0) {
            //取出字符串的长度
            int n = numStr.length();
            //将该数字加一
            Long num = Long.parseLong(numStr) + 1;
            String added = String.valueOf(num);
            n = Math.min(n, added.length());
            //拼接字符串
            return testStr.subSequence(0, testStr.length() - n) + added;
        } else {
            return "";
        }
    }
}
