/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.hgdatareport.nifa;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.mq.MqService;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mongo.hgdatareport.dao.NifaHistoryDataDao;
import com.hyjf.mongo.hgdatareport.entity.NifaHistoryDataEntity;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author PC-LIUSHOUYI
 * @version NifaHistoryDataDualServiceImpl, v0.1 2018/12/12 16:33
 */
@Service
public class NifaHistoryDataDualServiceImpl extends BaseServiceImpl implements NifaHistoryDataDualService {

    Logger _log = LoggerFactory.getLogger(NifaHistoryDataDualServiceImpl.class);

    @Autowired
    NifaHistoryDataDao nifaHistoryDataDao;

    @Autowired
    MqService mqService;

    /**
     * 初始化当天数据处理和生成文件信息
     *
     * @param historyData
     * @param isDualData
     * @param isDualFile
     * @param errMsg
     */
    @Override
    public void saveNifaMongoStatus(String historyData, int isDualData, int isDualFile, String errMsg) {
        NifaHistoryDataEntity nifaHistoryDataEntity = new NifaHistoryDataEntity();
        nifaHistoryDataEntity.setHistoryData(historyData);
        nifaHistoryDataEntity.setIsDualData("" + isDualData);
        nifaHistoryDataEntity.setIsDualFile("" + isDualFile);
        nifaHistoryDataEntity.setErrMsg(errMsg);
        this.nifaHistoryDataDao.save(nifaHistoryDataEntity);
    }

    /**
     * 更新当天数据处理和生成文件信息
     *
     * @param historyData
     * @param isDualData
     * @param isDualFile
     * @param errMsg
     */
    @Override
    public void updateNifaMongoStatus(String historyData, int isDualData, int isDualFile, String errMsg) {
        Query query = new Query();
        Criteria criteria = Criteria.where("historyData").is(historyData);
        query.addCriteria(criteria);
        Update update = new Update();
        update.set("isDualData", "" + isDualData).set("isDualFile", "" + isDualFile).set("errMsg", errMsg);
        nifaHistoryDataDao.updateAll(query, update);
    }

    /**
     * 处理当天放款还款债转数据
     *
     * @param historyData
     * @return
     */
    @Override
    public boolean selectDualData(String historyData) {
        boolean zeroData = false;
        // 根据日期拉取当天放款信息并发送mq
        List<String> loanBorrowNidList = this.nifaDualHistoryDataCustomizeMapper.selectBorrowByHistoryData(historyData);
        if (null != loanBorrowNidList && loanBorrowNidList.size() > 0) {
            zeroData = true;
            for (String borrowNid : loanBorrowNidList) {
                // 发送mq到hyjf-nifa-tender-info
                Map<String, String> params = new HashMap<String, String>();
                params.put("borrowNid", borrowNid);
                params.put("historyData", historyData);
                this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.HYJF_NIFA_TENDER_INFO, JSONObject.toJSONString(params));
            }
        } else {
            _log.info("【互金历史数据】数据处理日期：{}中未查询到放款信息！！！！！！！！！！", historyData);
        }
        // 根据日期拉取当天还款信息并发送mq
        // 到期还款的数据
        List<String> endRepayBorrowNidList = this.nifaDualHistoryDataCustomizeMapper.selectBorrowRepayByHistoryData(historyData);
        if (null != endRepayBorrowNidList && endRepayBorrowNidList.size() > 0) {
            zeroData = true;
            for (String borrowNid : endRepayBorrowNidList) {
                // 发送mq到hyjf-nifa-repay-info
                Map<String, String> params = new HashMap<String, String>();
                params.put("borrowNid", borrowNid);
                params.put("repayPeriod", "1");
                params.put("historyData", historyData);
                this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.HYJF_NIFA_REPAY_INFO, JSONObject.toJSONString(params));
            }
        } else {
            _log.info("【互金历史数据】数据处理日期：{}中未查询到还款信息！！！！！！！！！！", historyData);
        }

        // 分期还款的数据
        List<BorrowRepayPlan> monthRepayBorrowNidList = this.nifaDualHistoryDataCustomizeMapper.selectBorrowRepayPlanByHistoryData(historyData);
        if (null != monthRepayBorrowNidList && monthRepayBorrowNidList.size() > 0) {
            for (BorrowRepayPlan borrowRepayPlan : monthRepayBorrowNidList) {
                zeroData = true;
                // 发送mq到hyjf-nifa-repay-info
                Map<String, String> params = new HashMap<String, String>();
                params.put("borrowNid", borrowRepayPlan.getBorrowNid());
                params.put("repayPeriod", "" + borrowRepayPlan.getRepayPeriod());
                params.put("historyData", historyData);
                this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.HYJF_NIFA_REPAY_INFO, JSONObject.toJSONString(params));
            }
        } else {
            _log.info("【互金历史数据】数据处理日期：{}中未查询到分期还款信息！！！！！！！！！！", historyData);
        }

        // 根据日期拉取当天散标债转信息并发送mq
        // 时间减3天
        SimpleDateFormat date_sdf = new SimpleDateFormat("yyyy-MM-dd");
        String subtractDate = date_sdf.format(GetDate.countDate(historyData + " 00:00:00", 5, -3));
        List<String> creditNidList = this.nifaDualHistoryDataCustomizeMapper.selectBorrowCreditByHistoryData(subtractDate);
        if (null != creditNidList && creditNidList.size() > 0) {
            for (String creditNid : creditNidList) {
                zeroData = true;
                // 发送mq到hyjf-nifa-creditinfo
                Map<String, String> params = new HashMap<String, String>();
                params.put("creditNid", creditNid);
                //1（散）2（智投）
                params.put("flag", "1");
                //2承接（成功）
                params.put("status", "2");
                params.put("historyData", historyData);
                this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.HYJF_NIFA_CREDITINFO, JSONObject.toJSONString(params));
            }
        } else {
            _log.info("【互金历史数据】数据处理日期：{}中未查询到散标债转信息！！！！！！！！！！", historyData);
        }

        // 根据日期拉取当天计划债转信息并发送mq
        List<String> hjhCreditNidList = this.nifaDualHistoryDataCustomizeMapper.selectHjhDebtCreditByHistoryData(historyData);
        if (null != hjhCreditNidList && hjhCreditNidList.size() > 0) {
            for (String creditNid : hjhCreditNidList) {
                zeroData = true;
                // 发送mq到hyjf-nifa-creditinfo
                Map<String, String> params = new HashMap<String, String>();
                params.put("creditNid", creditNid);
                //1（散）2（智投）
                params.put("flag", "2");
                //2承接（成功）
                params.put("status", "2");
                params.put("historyData", historyData);
                this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.HYJF_NIFA_CREDITINFO, JSONObject.toJSONString(params));
            }
        } else {
            _log.info("【互金历史数据】数据处理日期：{}中未查询到计划债转信息！！！！！！！！！！", historyData);
        }
        if(zeroData){
            saveNifaMongoStatus(historyData,1,0,"");
        } else {
            // 当天无数据更新mongo状态
            saveNifaMongoStatus(historyData,1,1,"当天无数据");
        }
        return true;
    }

    /**
     * 获取该天处理状态
     *
     * @param historyData
     * @return
     */
    @Override
    public NifaHistoryDataEntity selectHistoryData(String historyData) {
        Query query = new Query();
        Criteria criteria = Criteria.where("historyData").is(historyData);
        query.addCriteria(criteria);
        NifaHistoryDataEntity nifaHistoryDataEntity = nifaHistoryDataDao.findOne(query);
        if (null != nifaHistoryDataEntity) {
            return nifaHistoryDataEntity;
        }
        return null;
    }
}
