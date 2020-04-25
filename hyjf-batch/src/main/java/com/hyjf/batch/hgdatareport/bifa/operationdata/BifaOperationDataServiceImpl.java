/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.hgdatareport.bifa.operationdata;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.hgdatareport.BaseHgDateReportServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.hgdatareport.dao.BifaOperationDataDao;
import com.hyjf.mongo.hgdatareport.entity.BifaOperationDataEntity;
import com.hyjf.mongo.operationreport.dao.OperationMongDao;
import com.hyjf.mongo.operationreport.dao.TotalInvestAndInterestMongoDao;
import com.hyjf.mongo.operationreport.entity.OperationReportEntity;
import com.hyjf.mongo.operationreport.entity.TotalInvestAndInterestEntity;
import com.hyjf.mybatis.mapper.customize.OperationReportCustomizeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 运营数据上报北互金
 * @author jun
 * @version BifaOperationDataService, v0.1 2018/11/30 9:34
 */
@Service
public class BifaOperationDataServiceImpl extends BaseHgDateReportServiceImpl implements BifaOperationDataService {

    Logger _log = LoggerFactory.getLogger(BifaOperationDataServiceImpl.class);

    @Autowired
    private OperationReportCustomizeMapper operationReportCustomizeMapper;

    @Autowired
    private OperationMongDao operationMongDao;

    @Autowired
    private TotalInvestAndInterestMongoDao totalInvestAndInterestMongoDao;

    @Autowired
    private BifaOperationDataDao bifaOperationDataDao;

    /**
     * 运营数据转换
     * @return
     */
    @Override
    public boolean convertBifaOperationData(BifaOperationDataEntity bifaOperationDataEntity) {
        // 获取数据的当前时间
        Calendar cal = Calendar.getInstance();

        try {
            DecimalFormat df = new DecimalFormat("#,##0");
            bifaOperationDataEntity.setSource_code(SOURCE_CODE);
            String curDate = GetDate.formatDate();
            bifaOperationDataEntity.setUpload_date(curDate);
            bifaOperationDataEntity.setData_begin_period("2013.12.23");
            bifaOperationDataEntity.setData_end_period(curDate);
            bifaOperationDataEntity.setTotal_loan_money(df.format(this.selectTotalInvest().setScale(0, BigDecimal.ROUND_DOWN)));
            //累计借款笔数(只统计放款之后的标的)
            bifaOperationDataEntity.setTotal_loan_num(this.getLoanNum(cal));
            //累计借贷余额
            bifaOperationDataEntity.setTotal_loan_balance_money(df.format(this.getWillPayMoney(cal)));
            //累计借贷余额笔数
            bifaOperationDataEntity.setTotal_loan_balance_num(this.getTotalLoanBalanceNum(cal));

            // 累计借款人（定义：系统累计到现在进行过发表的底层借款人数量）
            Integer countBorrowUser = borrowUserStatisticCustomizeMapper.countBorrowUser(new HashMap<String,Object>());
            //累計借款人數
            bifaOperationDataEntity.setTotal_borrow_users(String.valueOf(countBorrowUser));
            //累計投資人數
            bifaOperationDataEntity.setTotal_invest_users(this.getTenderCount(cal));
            // 当前借款人（定义：当前有尚未结清债权的底层借款人数量）
            Integer countBorrowUserCurrent = borrowUserStatisticCustomizeMapper.countCurrentBorrowUser(new HashMap<String,Object>());
            bifaOperationDataEntity.setCur_borrow_users(String.valueOf(countBorrowUserCurrent));
            //debug
            List<Integer> userIds = borrowUserStatisticCustomizeMapper.getCurrentTenderUserIds(new HashMap<String,Object>());
            _log.info("运营数据上报之获取当前投资人数的userId:"+JSONObject.toJSONString(userIds));

            // 当前投资人（定义：当前代还金额不为0的用户数量）
            Integer countCurrentTenderUser = borrowUserStatisticCustomizeMapper.countCurrentTenderUser(new HashMap<String,Object>());
            bifaOperationDataEntity.setCur_invest_users(String.valueOf(countCurrentTenderUser));

            //平台前十大融资人融资待还余额占比
            BigDecimal sumBorrowUserMoneyTopTen = borrowUserStatisticCustomizeMapper.sumBorrowUserMoneyTopTen(new HashMap<String,Object>());

            // 要统计前一个月的数据，所以月份要减一
            cal.add(Calendar.MONTH, -1);
            // 代还总金额
            BigDecimal sumBorrowUserMoney = borrowUserStatisticCustomizeMapper.sumBorrowUserMoney(this.getLastDay(cal));

            bifaOperationDataEntity.setTopten_repay_rate(this.getBorrowuserMoneyTopten(sumBorrowUserMoneyTopTen,sumBorrowUserMoney));

            //平台单一融资人最大融资待还余额占比
            BigDecimal sumBorrowUserMoneyTopOne = borrowUserStatisticCustomizeMapper.sumBorrowUserMoneyTopOne(new HashMap<String,Object>());

            bifaOperationDataEntity.setTop_repay_rate(this.getBorrowuserMoneyTopone(sumBorrowUserMoneyTopOne,sumBorrowUserMoney));

            bifaOperationDataEntity.setRelated_loan_money("0");
            bifaOperationDataEntity.setRelated_loan_num("0");
            bifaOperationDataEntity.setOverdue_loan_num("0");
            bifaOperationDataEntity.setOverdue_loan_money("0");
            bifaOperationDataEntity.setOverdue_ninety_loan_num("0");
            bifaOperationDataEntity.setOverdue_ninety_loan_money("0");
            bifaOperationDataEntity.setPayed_risk_money("0");
            bifaOperationDataEntity.setPayed_risk_num("0");
            bifaOperationDataEntity.setTotal_recharge("0");
            bifaOperationDataEntity.setTotal_deposit("1元/笔");
            bifaOperationDataEntity.setIdentity_auth_fee("0");
            bifaOperationDataEntity.setDegree_auth_fee("0");
            bifaOperationDataEntity.setVideo_auth_fee("0");
            bifaOperationDataEntity.setInterest_fee("0");
            bifaOperationDataEntity.setTranser_fee("手续费=成功出让金额*1%");
            bifaOperationDataEntity.setService_fee("年化7%-18%");
            Date currDate =GetDate.getDate();
            bifaOperationDataEntity.setCreateTime(currDate);
            bifaOperationDataEntity.setUpdateTime(currDate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 通过输入的日期，获取这个日期所在月份的最后一天
     * @param cal
     * @return
     */
    private Date getLastDay(Calendar cal) {
        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * 平台单一融资人最大融资待还余额占比
     * @return
     */
    private String getBorrowuserMoneyTopone(BigDecimal borrowuserMoneyTopone,BigDecimal borrowuserMoneyTotal) {
        String result = borrowuserMoneyTopone.divide(borrowuserMoneyTotal, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100")).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_DOWN).toString();
        return result;
    }

    /**
     * 平台前十大融资人融资待还余额占比
     * @return
     */
    private String getBorrowuserMoneyTopten(BigDecimal borrowuserMoneyTopten,BigDecimal borrowuserMoneyTotal) {
      String result =  borrowuserMoneyTopten.divide(borrowuserMoneyTotal, 4,
                BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2,BigDecimal.ROUND_HALF_UP)
                .setScale(2, BigDecimal.ROUND_DOWN).toString();
      return result;
    }

    /**
     * 累計投資人數
     * @param cal
     * @return
     */
    private String getTenderCount(Calendar cal) {
        return String.valueOf(operationReportCustomizeMapper.getTenderCount(cal.getTime()));
    }


    /**
     * 累计借贷余额
     * @param cal
     * @return
     */
    private BigDecimal getWillPayMoney(Calendar cal) {
        return operationReportCustomizeMapper.getRepayTotal(cal.getTime());
    }

    /**
     * 累计借款笔数
     * @param cal
     * @return
     */
    private String getLoanNum(Calendar cal) {
      return String.valueOf(operationReportCustomizeMapper.getLoanNum(cal.getTime()));
    }

    /**
     * 累计借贷余额笔数
     * @param cal
     * @return
     */
    public String getTotalLoanBalanceNum(Calendar cal) {
        return String.valueOf(operationReportCustomizeMapper.getLoanBalanceNum(cal.getTime()));
    }


    private BigDecimal selectTotalInvest() {
        TotalInvestAndInterestEntity entity = this.getTotalInvestAndInterestEntity();
        if (entity != null) {
            return entity.getTotalInvestAmount();
        }
        return BigDecimal.ZERO;
    }

    private TotalInvestAndInterestEntity getTotalInvestAndInterestEntity() {
        return totalInvestAndInterestMongoDao.findOne(new Query());
    }

    @Override
    public OperationReportEntity getOperationDataFromPlat() {
        Query query = new Query();
        query.limit(1);
        query.with(new Sort(Sort.Direction.DESC, "statisticsMonth"));
        return operationMongDao.findOne(query);
    }

    @Override
    public boolean insertReportData(BifaOperationDataEntity data) {
        try {
            bifaOperationDataDao.insert(data);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
