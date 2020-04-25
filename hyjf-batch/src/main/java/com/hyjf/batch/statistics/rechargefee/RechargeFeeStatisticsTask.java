/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.batch.statistics.rechargefee;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.customize.RechargeFeeStatisticsCustomize;

/**
 * 充值手续费统计
 * @author 李深强
 */
public class RechargeFeeStatisticsTask {
   
	
   @Autowired
   private RechargeFeeStatisticsService rechargeFeeStatisticsService;
   
    /**
     * 
     * 充值手续费统计
     * @author 李深强
     */
    public void insertRechargeFeeStatistics() {
        LogUtil.startLog(RechargeFeeStatisticsTask.class.toString(), "insertRechargeFeeStatistics");
        System.out.println("--------------充值手续费统计start-------------------");
        RechargeFeeStatisticsCustomize rechargeFeeStatisticsCustomize = new RechargeFeeStatisticsCustomize();
        String staDate = GetDate.date2Str(GetDate.date_sdf);
        rechargeFeeStatisticsCustomize.setStartTime(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(staDate)));
        rechargeFeeStatisticsCustomize.setEndTime(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayEnd(staDate)));
        //查询统计信息
        rechargeFeeStatisticsCustomize = rechargeFeeStatisticsService.selectRechargeFeeStatistics(rechargeFeeStatisticsCustomize);
     	//是否存在当前天的记录，存在则更新，不存在则插入
    	int recordId = rechargeFeeStatisticsService.isExistsRecordByDate(staDate);
        if(recordId != 0){
        	rechargeFeeStatisticsService.updateRechargeFeeStatisticsRecord(recordId,rechargeFeeStatisticsCustomize);
        }else{
	        rechargeFeeStatisticsService.insertRechargeFeeStatisticsRecord(staDate,rechargeFeeStatisticsCustomize);
        }
        System.out.println("--------------充值手续费统计end-------------------");
        LogUtil.endLog(RechargeFeeStatisticsTask.class.toString(), "insertRechargeFeeStatistics");
    }
   
   
    /**
     * 
     * 充值手续费统计
     * @author 李深强
     */
    public void insertRechargeFeeStatisticsHistory() {
        LogUtil.startLog(RechargeFeeStatisticsTask.class.toString(), "insertRechargeFeeStatistics");
        System.out.println("--------------充值手续费统计start-------------------");
        RechargeFeeStatisticsCustomize rechargeFeeStatisticsCustomize = new RechargeFeeStatisticsCustomize();
        String staDate = null;
        int days = GetDate.countDate(GetDate.stringToDate("2016-08-11 00:00:00"), GetDate.stringToDate("2016-08-15 23:59:59"));
        for(int i = 0;i <= days;i++){
        	staDate = GetDate.dateToString2(GetDate.countDate("2016-08-11 00:00:00", 5, i), "yyyy-MM-dd");
            rechargeFeeStatisticsCustomize.setStartTime(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(staDate)));
            rechargeFeeStatisticsCustomize.setEndTime(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayEnd(staDate)));
            //查询统计信息
            rechargeFeeStatisticsCustomize = rechargeFeeStatisticsService.selectRechargeFeeStatistics(rechargeFeeStatisticsCustomize);
         	//是否存在当前天的记录，存在则更新，不存在则插入
        	int recordId = rechargeFeeStatisticsService.isExistsRecordByDate(staDate);
            if(recordId != 0){
            	rechargeFeeStatisticsService.updateRechargeFeeStatisticsRecord(recordId,rechargeFeeStatisticsCustomize);
            }else{
    	        rechargeFeeStatisticsService.insertRechargeFeeStatisticsRecord(staDate,rechargeFeeStatisticsCustomize);
            }
        }
        
        System.out.println("--------------充值手续费统计end-------------------");
        LogUtil.endLog(RechargeFeeStatisticsTask.class.toString(), "insertRechargeFeeStatistics");
    }

    
 
}
