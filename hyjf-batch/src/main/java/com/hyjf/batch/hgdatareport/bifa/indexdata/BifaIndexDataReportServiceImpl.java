/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.hgdatareport.bifa.indexdata;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.hgdatareport.BaseHgDateReportServiceImpl;
import com.hyjf.batch.hgdatareport.bifa.BifaCommonConstants;
import com.hyjf.batch.hgdatareport.bifa.exceptiondata.BifaExceptionDataServiceImpl;
import com.hyjf.common.file.FileUtil;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.hgdatareport.dao.BifaBorrowUserInfoIndexDao;
import com.hyjf.mongo.hgdatareport.entity.BifaIndexUserInfoEntity;
import com.hyjf.mongo.hgdatareport.entity.UserInfoSHA256Entity;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import com.hyjf.mybatis.model.customize.bifa.BifaIndexUserInfoBean;
import com.hyjf.mybatis.model.customize.bifa.UserIdAccountSumBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.record.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 北互金索引数据上报
 *
 * @author jun
 */
@Service
public class BifaIndexDataReportServiceImpl extends BaseHgDateReportServiceImpl implements BifaIndexDataReportService {

    public Logger _log = LoggerFactory.getLogger(BifaExceptionDataServiceImpl.class);

    private static final String thisMessName = "定时任务上报索引数据";
    public static final String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_BIFA + " " + thisMessName + "】";

    @Autowired
    BifaBorrowUserInfoIndexDao bifaBorrowUserInfoIndexDao;

    //保存待上报的文件
    static List<Map<String, String>> reportFiles = new ArrayList<Map<String, String>>();
    //上报文件最大行数
    final int reportFileMaxRows = 1000;

    @Override
    public void executeIndexDataReport(Integer startDate, Integer endDate) throws Exception {
        reportFiles.clear();
        // 执行已开户用户索引数据上报
        this.prepareBankOpenedAccountReportData(startDate, endDate);
        // 执行出借大于0的用户索引数据上报
        this.prepareLenderGTZeroReportData(startDate, endDate);
        // 执行出借等于0的用户索引数据上报
        this.prepareLenderZeroReportData(startDate, endDate);
        // 执行借贷用户(已放款)索引数据上报
        this.prepareBorrowedUserReportData(startDate, endDate);
        // 执行数据上报操作
        this.executeDataReport();
    }

    /**
     * 用户散标出借成功(放款成功)
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<UserIdAccountSumBean> getBorrowTenderAccountSum(Integer startDate, Integer endDate) {
        return this.borrowTenderInfoCustomizeMapper.getBorrowTenderAccountSum(startDate, endDate);
    }

    /**
     * 获取北互金索引上报信息
     *
     * @param userId
     * @return
     */
    @Override
    public BifaIndexUserInfoBean selectUserInfoByUserId(Integer userId) {
        return this.usersCustomizeMapper.selectUserCorpInfoByUserId(userId);
    }

    /**
     * 执行数据上报
     *
     * @throws Exception
     */
    private void executeDataReport() throws Exception {
        for (Map<String, String> fileMap : reportFiles) {
            JSONObject result = this.report(fileMap.get("filePath"), fileMap.get("dataType"));
            int resultCode= (int) result.get("result");
            if (resultCode == 0){
                _log.info(logHeader + result.get("message") +",文件:"+fileMap.get("filePath"));
            }else {
                _log.error(logHeader + result.get("message") +",文件:"+fileMap.get("filePath"));
            }
        }
    }

    /**
     * 执行借贷用户(已放款)索引数据上报 最近7天的
     * 情况特殊 需要每次上报 其他的都是只上报一次
     * @param startDate
     * @param endDate
     * @return
     */
    private void prepareBorrowedUserReportData(Integer startDate, Integer endDate) throws Exception {
        try {
            String dataType = BifaCommonConstants.DATATYPE_1000;
            List<BifaIndexUserInfoBean> bifaIndexUserInfoBeans = this.getBorrowUserInfo(startDate, endDate);
            //判空
            if (null == bifaIndexUserInfoBeans) {
                throw new Exception(logHeader + "获取已放款的借贷用户失败!!!");
            }

            _log.info(logHeader + dataType + " DB检出记录数：" + bifaIndexUserInfoBeans.size());
            // 输出记录数合计
            int total = 0;
            // 单次输出数
            int count = 0;
            // 文件数
            int filsNum = 0;

            StringBuffer sb = new StringBuffer();
            List<BifaIndexUserInfoEntity> list = new ArrayList<BifaIndexUserInfoEntity>();
            for (int i = 0; i < bifaIndexUserInfoBeans.size(); i++) {
                BifaIndexUserInfoBean bean = bifaIndexUserInfoBeans.get(i);
                UserInfoSHA256Entity sha256Entity = this.selectUserIdToSHA256(bean.getUserId(), bean.getTrueName(), bean.getIdCard());
                BifaIndexUserInfoEntity resultFromMongo = this.getBorrowUserInfoFromMongo(bean.getBorrowNid());
                if (resultFromMongo == null) {
                    //已开户的用户没有上报时 才执行上报操作
                    sb.append(sha256Entity.getSha256() + "," + bean.getBorrowBeginDate() + "," + bean.getBorrowEndDate() + "\r\n");
                    BifaIndexUserInfoEntity entity = CommonUtils.convertBean(bean, BifaIndexUserInfoEntity.class);
                    list.add(entity);
                    count++;
                    total++;
                }

                // 每reportFileMaxRows条存1次数据 及最后一次有记录
                if (count >= reportFileMaxRows || (i == bifaIndexUserInfoBeans.size() - 1 && count > 0 && count < reportFileMaxRows)) {
                    //往文件里面写
                    filsNum++;
                    this.handleReportIndexData(sb, dataType, filsNum, endDate);
                    _log.info(logHeader + dataType + "_" + filsNum + " 文件生成。生成记录数：" + count);
                    //上报完成后 将借贷用户信息保存至本地mongo
                    this.insertReportData(list);
                    _log.info(logHeader + dataType + "_" + filsNum + " MongoDB保存。" + list.size());
                    sb.setLength(0); // 释放StringBuffer
                    list.clear(); // 释放List
                    count = 0; // 初始单次输出数
                }
            }
            _log.info(logHeader + dataType + " 文件生成。总生成记录数：" + total);
        } catch (Exception e) {
            _log.error(logHeader + "定时任务处理失败！！", e);
        }
    }

    /**
     * 从mongo中获取借贷用户索引信息
     *
     * @param borrowNid
     * @return
     */
    private BifaIndexUserInfoEntity getBorrowUserInfoFromMongo(String borrowNid) {
        return bifaBorrowUserInfoIndexDao.findOne(new Query(Criteria.where("borrowNid").is(borrowNid)));
    }

    /**
     * 保存至mongo
     *
     * @param list
     */
    public void insertReportData(List<BifaIndexUserInfoEntity> list) {
        try {
            for (BifaIndexUserInfoEntity entity : list) {
                BifaIndexUserInfoEntity mongoResult = this.getBorrowUserInfoFromMongo(entity.getBorrowNid());
                if (null == mongoResult) {
                    // 等于空的时候再添加
                    bifaBorrowUserInfoIndexDao.insert(entity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取借款人信息 最近7天的
     *
     * @param startDate
     * @param endDate
     * @return
     */
    private List<BifaIndexUserInfoBean> getBorrowUserInfo(Integer startDate, Integer endDate) {
        List<BifaIndexUserInfoBean> result = new ArrayList<BifaIndexUserInfoBean>();
        //获取近七天添加的标的信息
        List<BorrowCustomize> borrowCustomizes = this.borrowCustomizeMapper.selectBorrowUserInfo(startDate, endDate);
        for (BorrowCustomize bean : borrowCustomizes) {
            if ("1".equals(bean.getCompanyOrPersonal()) && StringUtils.isEmpty(bean.getIdCard())) {
                //借款主体为公司时,公司的统一社会信用代码不能为空
                _log.info(logHeader + "索引数据错误！！" + JSONObject.toJSONString(bean));
                continue;
            } else if ("2".equals(bean.getCompanyOrPersonal())
                    && (StringUtils.isEmpty(bean.getTruename()) || StringUtils.isEmpty(bean.getIdCard()))) {
                //借款主体为个人时,个人的真实名称和身份证号不能为空
                _log.info(logHeader + "索引数据错误！！" + JSONObject.toJSONString(bean));
                continue;
            }

            BifaIndexUserInfoBean biuib = this.buildBifaIndexUserInfoBean(bean);
            result.add(biuib);
        }

        return result;

    }

    private BifaIndexUserInfoBean buildBifaIndexUserInfoBean(BorrowCustomize bean) {
        BifaIndexUserInfoBean result = new BifaIndexUserInfoBean();
        result.setUserId(bean.getUserId());
        result.setTrueName(bean.getTruename());
        result.setIdCard(bean.getIdCard());
        result.setBorrowBeginDate(bean.getRecoverLastTime());
        result.setBorrowNid(bean.getBorrowNid());
        result.setBorrowEndDate(GetDate.times10toStrYYYYMMDD(bean.getRepayLastTime()));
        return result;
    }

    /**
     * 分期还款
     *
     * @param borrowNid
     * @return
     */
    private List<BorrowRepayPlan> getBorrowRepayPlan(String borrowNid) {
        BorrowRepayPlanExample example = new BorrowRepayPlanExample();
        BorrowRepayPlanExample.Criteria criteria = example.createCriteria();
        // 标的编号
        criteria.andBorrowNidEqualTo(borrowNid);
        // 按照期数升序排列
        example.setOrderByClause("repay_period");
        return this.borrowRepayPlanMapper.selectByExample(example);
    }


    private BorrowRepay getBorrowRepay(String borrowNid) {
        BorrowRepayExample example = new BorrowRepayExample();
        BorrowRepayExample.Criteria criteria = example.createCriteria();
        // 标的编号
        criteria.andBorrowNidEqualTo(borrowNid);
        List<BorrowRepay> repayList = this.borrowRepayMapper.selectByExample(example);
        if (null != repayList && repayList.size() > 0) {
            return repayList.get(0);
        }
        return null;

    }

    /**
     * 执行已开户用户索引数据上报
     */
    private void prepareBankOpenedAccountReportData(Integer startDate, Integer endDate) throws Exception {
        try {
            String dataType = BifaCommonConstants.DATATYPE_1005;
            //获取最近七天开户的用户
            List<BifaIndexUserInfoBean> bifaIndexUserInfoBeans = this.getBankOpenedAccountUsers(startDate, endDate);
            _log.info(bifaIndexUserInfoBeans.size()+"");
            if (null == bifaIndexUserInfoBeans) {
                throw new Exception(logHeader + "获取开户用户失败!!!");
            }
            _log.info(logHeader + dataType + " DB检出记录数：" + bifaIndexUserInfoBeans.size());
            // 输出记录数合计
            int total = 0;
            // 单次输出数
            int count = 0;
            // 文件数
            int filsNum = 0;

            StringBuffer sb = new StringBuffer();
            List<Integer> userList = new ArrayList<>();
            for (int i = 0; i < bifaIndexUserInfoBeans.size(); i++) {
                BifaIndexUserInfoBean bean = bifaIndexUserInfoBeans.get(i);
                UserInfoSHA256Entity sha256Entity = this.selectUserIdToSHA256(bean.getUserId(), bean.getTrueName(), bean.getIdCard());
                if ("0".equals(sha256Entity.getIsOpenUp())) {
                    //已开户的用户没有上报时 才执行上报操作
                    sb.append(sha256Entity.getSha256() + "," + bean.getRegDate() + "\r\n");
                    userList.add(bean.getUserId());
                    count++;
                    total++;
                }

                // 每reportFileMaxRows条存1次数据 及最后一次有记录
                if (count >= reportFileMaxRows || (i == bifaIndexUserInfoBeans.size() - 1 && count > 0 && count < reportFileMaxRows)) {
                    //往文件里面写
                    filsNum++;
                    this.handleReportIndexData(sb, dataType, filsNum, endDate);
                    _log.info(logHeader + dataType + "_" + filsNum + " 文件生成。生成记录数：" + count);
                    //上报完成后 更新sha256表中用户对应状态
                    this.updateUserIndexReportStatus(userList, dataType);
                    _log.info(logHeader + dataType + "_" + filsNum + " MongoDB保存。" + JSONObject.toJSONString(userList));
                    sb.setLength(0); // 释放StringBuffer
                    userList.clear(); // 释放List
                    count = 0; // 初始单次输出数
                }
            }
            _log.info(logHeader + dataType + " 文件生成。总生成记录数：" + total);
        } catch (Exception e) {
            _log.error(logHeader + "定时任务处理失败！！", e);
        }
    }

    /**
     * 获取最近七天开户的用户
     */
    private List<BifaIndexUserInfoBean> getBankOpenedAccountUsers(Integer startDate, Integer endDate) {
        return this.usersCustomizeMapper.getBankOpenedAccountUsers(startDate, endDate);
    }


    /**
     * 执行出借大于0或者等于的用户索引数据上报
     */
    private void prepareLenderGTZeroReportData(Integer startDate, Integer endDate) throws Exception {
        try {
            String dataType = BifaCommonConstants.DATATYPE_1002;
            //拉取已开户且出借>0的用户
            List<UserIdAccountSumBean> borrowTenders = this.getBorrowTenderAccountSum(startDate, endDate);
            //判空
            if (null == borrowTenders) {
                throw new Exception(logHeader + "获取已开户且出借>0的用户失败!!!");
            }
            _log.info(logHeader + dataType + " DB检出记录数：" + borrowTenders.size());
            // 输出记录数合计
            int total = 0;
            // 单次输出数
            int count = 0;
            // 文件数
            int filsNum = 0;
            StringBuffer sb = new StringBuffer();
            List<Integer> userList = new ArrayList<>();
            for (int i = 0; i < borrowTenders.size(); i++) {
                UserIdAccountSumBean bean = borrowTenders.get(i);
                //获取用户的名称,身份证号,注册时间
                BifaIndexUserInfoBean bifaIndexUserInfoBean = this.selectUserInfoByUserId(bean.getUserId());
                //获取sha256加密
                UserInfoSHA256Entity userInfoSHA256Entity = this.selectUserIdToSHA256(bean.getUserId(), bifaIndexUserInfoBean.getTrueName(), bifaIndexUserInfoBean.getIdCard());
                //出借大于0的用户
                //判断借款大于0的用户是否上报
                if ("0".equals(userInfoSHA256Entity.getIsLenderOneUp())) {
                    //没有上报
                    sb.append(userInfoSHA256Entity.getSha256() + "," + bifaIndexUserInfoBean.getRegDate() + "\r\n");
                    userList.add(bean.getUserId());
                    count++;
                    total++;
                }

                // 每reportFileMaxRows条存1次数据 及最后一次有记录
                if (count >= reportFileMaxRows || (i == borrowTenders.size() - 1 && count > 0 && count < reportFileMaxRows)) {
                    //往文件里面写
                    filsNum++;
                    this.handleReportIndexData(sb, dataType, filsNum, endDate);
                    _log.info(logHeader + dataType + "_" + filsNum + " 文件生成。生成记录数：" + count);
                    //上报完成后 更新sha256表中用户对应状态
                    this.updateUserIndexReportStatus(userList, dataType);
                    _log.info(logHeader + dataType + "_" + filsNum + " MongoDB保存。" + JSONObject.toJSONString(userList));
                    sb.setLength(0); // 释放StringBuffer
                    userList.clear(); // 释放List
                    count = 0; // 初始单次输出数
                }
            }
            _log.info(logHeader + dataType + " 文件生成。总生成记录数：" + total);
        } catch (Exception e) {
            _log.error(logHeader + "定时任务处理失败！！", e);
        }
    }

    /**
     * 执行出借大于0或者等于的用户索引数据上报
     */
    private void prepareLenderZeroReportData(Integer startDate, Integer endDate) throws Exception {
        try {
            String dataType = BifaCommonConstants.DATATYPE_1003;
            //拉取已开户且出借等于0的用户
            Query query = new Query();
            Criteria criteria = Criteria.where("isOpenUp").is("1").and("isLenderZeroUp").is("0").and("isLenderOneUp").is("0");
            query.addCriteria(criteria);
            List<UserInfoSHA256Entity> userInfoSHA256EntityList = this.userInfoSHA256Dao.find(query);
            String currDate = GetDate.formatDate();

            if (userInfoSHA256EntityList == null) {
                _log.info(logHeader + "未获取已开户且出借等于0的用户!!!" + userInfoSHA256EntityList);
            }

            _log.info(logHeader + dataType + " DB检出记录数：" + userInfoSHA256EntityList.size());
            // 输出记录数合计
            int total = 0;
            // 单次输出数
            int count = 0;
            // 文件数
            int filsNum = 0;
            StringBuffer sb = new StringBuffer();
            List<Integer> userList = new ArrayList<>();

            for (int i = 0; i < userInfoSHA256EntityList.size(); i++) {
                UserInfoSHA256Entity entity = userInfoSHA256EntityList.get(i);
                //出借等于0的用户的注册时间
                Users users = this.getUsersByUserId(entity.getUserId());
                //没有上报
                sb.append(entity.getSha256() + "," + GetDate.times10toStrYYYYMMDD(users.getRegTime()) + "," + currDate + "\r\n");
                userList.add(entity.getUserId());
                count++;
                total++;

                // 每reportFileMaxRows条存1次数据 及最后一次有记录
                if (count >= reportFileMaxRows || (i == userInfoSHA256EntityList.size() - 1 && count > 0 && count < reportFileMaxRows)) {
                    //往文件里面写
                    filsNum++;
                    this.handleReportIndexData(sb, dataType, filsNum, endDate);
                    _log.info(logHeader + dataType + "_" + filsNum + " 文件生成。生成记录数：" + count);
                    //上报完成后 更新sha256表中用户对应状态
                    this.updateUserIndexReportStatus(userList, dataType);
                    _log.info(logHeader + dataType + "_" + filsNum + " MongoDB保存。" + JSONObject.toJSONString(userList));
                    sb.setLength(0); // 释放StringBuffer
                    userList.clear(); // 释放List
                    count = 0; // 初始单次输出数
                }
            }
            _log.info(logHeader + dataType + " 文件生成。总生成记录数：" + total);
        } catch (Exception e) {
            _log.error(logHeader + "定时任务处理失败！！", e);
        }
    }

    /**
     * 更新sha256表中用户对应状态
     *
     * @param userIdDataTypeJsonList
     */
    private void updateUserIndexReportStatus(List<Integer> userIdList, String dataType) {
        for (Integer userId : userIdList) {
            Query query = new Query();
            Criteria criteria = Criteria.where("userId").is(userId);
            query.addCriteria(criteria);
            Update update = new Update();

            if (BifaCommonConstants.DATATYPE_1003.equals(dataType)) {
                //出借等于0的用户
                update.set("isLenderZeroUp", "1");
            } else if (BifaCommonConstants.DATATYPE_1002.equals(dataType)) {
                //出借大于0的用户
                update.set("isLenderOneUp", "1");
            } else if (BifaCommonConstants.DATATYPE_1005.equals(dataType)) {
                //已开户的用户
                update.set("isOpenUp", "1");
            }
            update.set("updateTime", GetDate.getDate());
            this.userInfoSHA256Dao.findAndModify(query, update, BifaCommonConstants.HT_USERINFOSHA256_COLLECTIONNAME);
        }
    }

    /**
     * 处理上报数据
     * @param sb
     * @param dataType
     * @param fileNum
     * @throws Exception
     */
    private void handleReportIndexData(StringBuffer sb, String dataType, int fileNum) throws Exception {

        String fileSuffix = "_" + GetDate.getNowTimeYYYYMMDDHHMMSS() + "_" + new DecimalFormat("000").format(fileNum) + ".txt";
        if (StringUtils.isEmpty(sb) || StringUtils.isEmpty(dataType)) {
            _log.info(logHeader + "获取待上报数据或者数据类型失败!!!");
        } else {
            //处理出借等于0的用户
            if (StringUtils.isNotEmpty(sb) && BifaCommonConstants.DATATYPE_1003.equals(dataType)) {
                String fileName = BifaCommonConstants.DATATYPE_1003 + fileSuffix;
                String filePath = "";
                if (!HYJF_BIFA_FILES_INDEXFILE.endsWith(File.separator)) {
                    filePath = HYJF_BIFA_FILES_INDEXFILE + File.separator + fileName;
                } else {
                    filePath = HYJF_BIFA_FILES_INDEXFILE + fileName;
                }

                FileUtil.writeFile(filePath, sb.toString().getBytes(), false);
                Map<String, String> filesMap = new HashMap<String, String>();
                filesMap.put("filePath", filePath);
                filesMap.put("dataType", dataType);
                reportFiles.add(filesMap);
            }
            //处理出借大于0的用户
            if (StringUtils.isNotEmpty(sb) && BifaCommonConstants.DATATYPE_1002.equals(dataType)) {
                String fileName = BifaCommonConstants.DATATYPE_1002 + fileSuffix;
                String filePath = "";
                if (!HYJF_BIFA_FILES_INDEXFILE.endsWith(File.separator)) {
                    filePath = HYJF_BIFA_FILES_INDEXFILE + File.separator + fileName;
                } else {
                    filePath = HYJF_BIFA_FILES_INDEXFILE + fileName;
                }

                FileUtil.writeFile(filePath, sb.toString().getBytes(), false);
                Map<String, String> filesMap = new HashMap<String, String>();
                filesMap.put("filePath", filePath);
                filesMap.put("dataType", dataType);
                reportFiles.add(filesMap);
            }
            //处理开户用户
            if (StringUtils.isNotEmpty(sb) && BifaCommonConstants.DATATYPE_1005.equals(dataType)) {
                String fileName = BifaCommonConstants.DATATYPE_1005 + fileSuffix;
                String filePath = "";
                if (!HYJF_BIFA_FILES_INDEXFILE.endsWith(File.separator)) {
                    filePath = HYJF_BIFA_FILES_INDEXFILE + File.separator + fileName;
                } else {
                    filePath = HYJF_BIFA_FILES_INDEXFILE + fileName;
                }

                FileUtil.writeFile(filePath, sb.toString().getBytes(), false);
                Map<String, String> filesMap = new HashMap<String, String>();
                filesMap.put("filePath", filePath);
                filesMap.put("dataType", dataType);
                reportFiles.add(filesMap);
            }
            //处理借贷用户
            if (StringUtils.isNotEmpty(sb) && BifaCommonConstants.DATATYPE_1000.equals(dataType)) {
                String fileName = BifaCommonConstants.DATATYPE_1000 + fileSuffix;
                String filePath = "";
                if (!HYJF_BIFA_FILES_INDEXFILE.endsWith(File.separator)) {
                    filePath = HYJF_BIFA_FILES_INDEXFILE + File.separator + fileName;
                } else {
                    filePath = HYJF_BIFA_FILES_INDEXFILE + fileName;
                }

                FileUtil.writeFile(filePath, sb.toString().getBytes(), false);
                Map<String, String> filesMap = new HashMap<String, String>();
                filesMap.put("filePath", filePath);
                filesMap.put("dataType", dataType);
                reportFiles.add(filesMap);
            }
        }

    }

    /**
     * 处理上报数据
     * @param sb
     * @param dataType
     * @param fileNum
     * @throws Exception
     */
    private void handleReportIndexData(StringBuffer sb, String dataType, int fileNum, Integer endDate) throws Exception {
        String fileSuffix = "_" + GetDate.getNowTimeYYYYMMDDHHMMSS() + "_" + new DecimalFormat("000").format(fileNum) + ".txt";

        // 历史数据的文件名
        if (endDate.compareTo(GetDate.getDayStart10(new Date())-1) < 0){
            fileSuffix = "_" + GetDate.timestamptoNUMStrYYYYMMDDHHMMSS2(endDate) + "_" + new DecimalFormat("000").format(fileNum) + ".txt";
        }

        if (StringUtils.isEmpty(sb) || StringUtils.isEmpty(dataType)) {
            _log.info(logHeader + "获取待上报数据或者数据类型失败!!!");
        } else {
            //处理出借等于0的用户
            if (StringUtils.isNotEmpty(sb) && BifaCommonConstants.DATATYPE_1003.equals(dataType)) {
                String fileName = BifaCommonConstants.DATATYPE_1003 + fileSuffix;
                String filePath = "";
                if (!HYJF_BIFA_FILES_INDEXFILE.endsWith(File.separator)) {
                    filePath = HYJF_BIFA_FILES_INDEXFILE + File.separator + fileName;
                } else {
                    filePath = HYJF_BIFA_FILES_INDEXFILE + fileName;
                }

                FileUtil.writeFile(filePath, sb.toString().getBytes(), false);
                Map<String, String> filesMap = new HashMap<String, String>();
                filesMap.put("filePath", filePath);
                filesMap.put("dataType", dataType);
                reportFiles.add(filesMap);
            }
            //处理出借大于0的用户
            if (StringUtils.isNotEmpty(sb) && BifaCommonConstants.DATATYPE_1002.equals(dataType)) {
                String fileName = BifaCommonConstants.DATATYPE_1002 + fileSuffix;
                String filePath = "";
                if (!HYJF_BIFA_FILES_INDEXFILE.endsWith(File.separator)) {
                    filePath = HYJF_BIFA_FILES_INDEXFILE + File.separator + fileName;
                } else {
                    filePath = HYJF_BIFA_FILES_INDEXFILE + fileName;
                }

                FileUtil.writeFile(filePath, sb.toString().getBytes(), false);
                Map<String, String> filesMap = new HashMap<String, String>();
                filesMap.put("filePath", filePath);
                filesMap.put("dataType", dataType);
                reportFiles.add(filesMap);
            }
            //处理开户用户
            if (StringUtils.isNotEmpty(sb) && BifaCommonConstants.DATATYPE_1005.equals(dataType)) {
                String fileName = BifaCommonConstants.DATATYPE_1005 + fileSuffix;
                String filePath = "";
                if (!HYJF_BIFA_FILES_INDEXFILE.endsWith(File.separator)) {
                    filePath = HYJF_BIFA_FILES_INDEXFILE + File.separator + fileName;
                } else {
                    filePath = HYJF_BIFA_FILES_INDEXFILE + fileName;
                }

                FileUtil.writeFile(filePath, sb.toString().getBytes(), false);
                Map<String, String> filesMap = new HashMap<String, String>();
                filesMap.put("filePath", filePath);
                filesMap.put("dataType", dataType);
                reportFiles.add(filesMap);
            }
            //处理借贷用户
            if (StringUtils.isNotEmpty(sb) && BifaCommonConstants.DATATYPE_1000.equals(dataType)) {
                String fileName = BifaCommonConstants.DATATYPE_1000 + fileSuffix;
                String filePath = "";
                if (!HYJF_BIFA_FILES_INDEXFILE.endsWith(File.separator)) {
                    filePath = HYJF_BIFA_FILES_INDEXFILE + File.separator + fileName;
                } else {
                    filePath = HYJF_BIFA_FILES_INDEXFILE + fileName;
                }

                FileUtil.writeFile(filePath, sb.toString().getBytes(), false);
                Map<String, String> filesMap = new HashMap<String, String>();
                filesMap.put("filePath", filePath);
                filesMap.put("dataType", dataType);
                reportFiles.add(filesMap);
            }
        }

    }

    /**
     * 上报索引文件
     */
    private JSONObject report(String filePath, String dataType) throws IOException {
        File file = new File(filePath);
        FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
        StringBody stringBody = new StringBody(dataType, ContentType.MULTIPART_FORM_DATA);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addPart("file", fileBody);
        builder.addPart("dataType", stringBody);
        HttpEntity entity = builder.build();
        HttpPost post = new HttpPost(HYJF_BIFA_INDEXDATA_REPORT_URL);
        post.setEntity(entity);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(post);
        String result=EntityUtils.toString(response.getEntity());
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject;
    }

    public static void main(String[] args) {
        try {
            String filePath = "D:\\hyjfdata\\data\\bifaindexfile\\1000_20190104040530_001.txt";
            String url = "http://bits.hyjf.com/analyze/uploadApi";
            File file = new File(filePath);
            FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
            StringBody stringBody1 = new StringBody("1000", ContentType.MULTIPART_FORM_DATA);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addPart("file", fileBody);
            builder.addPart("dataType", stringBody1);
            HttpEntity entity = builder.build();
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            CloseableHttpResponse response = httpclient.execute(post);
            response.getEntity().writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
