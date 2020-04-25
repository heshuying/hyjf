package com.hyjf.batch.statistics.tzjday;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.StatisticsTzj;
import com.hyjf.mybatis.model.auto.StatisticsTzjExample;
import com.hyjf.mybatis.model.auto.StatisticsTzjHour;
import com.hyjf.mybatis.model.auto.StatisticsTzjHourExample;

/**
 * 
 * 此处为类说明
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年3月23日
 * @see 下午3:21:36
 */
@Service(value="TZJStatisticsDayReportService")
public class TZJStatisticsDayReportServiceImpl extends BaseServiceImpl implements TZJStatisticsDayReportService{
    
   /* @Override
	public void doStatistics(){
        String[] statisticsDay = {"2017-12-05", "2017-12-06", "2017-12-07", "2017-12-08", "2017-12-09", "2017-12-10"};
        StatisticsTzjExample example = new StatisticsTzjExample();
        example.createCriteria().andDayEqualTo("2017-12-07");
        List<StatisticsTzj> statisticsList = statisticsTzjMapper.selectByExample(example);
        if(statisticsList == null || statisticsList.isEmpty()){
            for(String day : statisticsDay){
            	updateStatistics(day, getStatistics(day));
            }
        }
        
        updateStatistics(GetDate.formatDate(), getStatistics(GetDate.formatDate()));
    }*/
    
    @Override
	public Map<String,Object> getStatistics(String statisticDay){
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("statisticDay", statisticDay);
        
        // 查询统计数据
        int tenderCount = tzjStatisticsDayReportCustomizeMapper.getTenderCount(paraMap);
        int tenderNewCount = tzjStatisticsDayReportCustomizeMapper.getTenderNewCount(paraMap);
        int tenderFirstCount = tzjStatisticsDayReportCustomizeMapper.getTenderFirstCount(paraMap);
        int tenderAgainCount = tzjStatisticsDayReportCustomizeMapper.getTenderAgainCount(paraMap);
        
        int registCount = tzjStatisticsDayReportCustomizeMapper.getRegistCount(paraMap);
        int openCount = tzjStatisticsDayReportCustomizeMapper.getOpenCount(paraMap);
        int cardbindCount = tzjStatisticsDayReportCustomizeMapper.getCardBindCount(paraMap);
        int rechargeCount = tzjStatisticsDayReportCustomizeMapper.getRechargeCount(paraMap);
        int rechargenewCount = tzjStatisticsDayReportCustomizeMapper.getRechargeNewCount(paraMap);
        double tenderMoney = tzjStatisticsDayReportCustomizeMapper.getTenderMoney(paraMap);
        double tenderfirstMoney = tzjStatisticsDayReportCustomizeMapper.getTenderFirstMoney(paraMap);
        
        resultMap.put("tenderCount", tenderCount);
        resultMap.put("tenderNewCount", tenderNewCount);
        resultMap.put("tenderFirstCount", tenderFirstCount);
        resultMap.put("tenderAgainCount", tenderAgainCount);
        resultMap.put("registCount", registCount);
        resultMap.put("openCount", openCount);
        resultMap.put("cardbindCount", cardbindCount);
        resultMap.put("rechargeCount", rechargeCount);
        resultMap.put("rechargenewCount", rechargenewCount);
        resultMap.put("tenderMoney", tenderMoney);
        resultMap.put("tenderfirstMoney", tenderfirstMoney);
        
    	return resultMap;
    }
    
    @Override
	public synchronized void updateStatistics(String statisticDay, Map<String,Object> data){
        // 查询统计数据
        int tenderCount = (Integer)data.get("tenderCount");
        int tenderNewCount = (Integer)data.get("tenderNewCount");
        int tenderFirstCount = (Integer)data.get("tenderFirstCount");
        int tenderAgainCount = (Integer)data.get("tenderAgainCount");
        
        int registCount = (Integer)data.get("registCount");
        int openCount = (Integer)data.get("openCount");
        int cardbindCount = (Integer)data.get("cardbindCount");
        int rechargeCount = (Integer)data.get("rechargeCount");
        int rechargenewCount = (Integer)data.get("rechargenewCount");
        double tenderMoney = (Double)data.get("tenderMoney");
        double tenderfirstMoney = (Double)data.get("tenderfirstMoney");
        
        // 开户率
        BigDecimal openRate = BigDecimal.ZERO;
        // 绑卡率
        BigDecimal cardbindRate = BigDecimal.ZERO;
        // 新投比
        BigDecimal tenderNewRate = BigDecimal.ZERO;
        // 复投比
        BigDecimal tenderAgainRate = BigDecimal.ZERO;
        
        BigDecimal value100 = new BigDecimal(100);
        
        if(registCount != 0){
            openRate = new BigDecimal(openCount).divide(new BigDecimal(registCount), 2, BigDecimal.ROUND_HALF_UP).multiply(value100);
            cardbindRate = new BigDecimal(cardbindCount).divide(new BigDecimal(registCount) , 2, BigDecimal.ROUND_HALF_UP).multiply(value100);
            tenderNewRate = new BigDecimal(tenderNewCount).divide(new BigDecimal(registCount) , 2, BigDecimal.ROUND_HALF_UP).multiply(value100);
        }
        
        if(tenderCount != 0){
            tenderAgainRate = new BigDecimal(tenderAgainCount).divide(new BigDecimal(tenderCount), 2, BigDecimal.ROUND_HALF_UP).multiply(value100);
        }
        
        StatisticsTzjExample example = new StatisticsTzjExample();
        example.createCriteria().andDayEqualTo(statisticDay);
        List<StatisticsTzj> statisticsList = statisticsTzjMapper.selectByExample(example);
        
        // 先删除统计历史数据
        if(!statisticsList.isEmpty()){
            statisticsTzjMapper.deleteByExample(example);
        }
        
        
        StatisticsTzjHourExample hourExample = new StatisticsTzjHourExample();
        hourExample.createCriteria().andDayEqualTo(statisticDay).andHourEqualTo(String.valueOf(GetDate.getHour(new Date())));
        List<StatisticsTzjHour> hourList = statisticsTzjHourMapper.selectByExample(hourExample);
        
        // 先删除统计历史数据
        if(hourList == null || hourList.isEmpty()){
            statisticsTzjHourMapper.deleteByExample(hourExample);
        }
        
        // 小时统计数据
        if(hourList.isEmpty()){
            if(statisticsList.isEmpty()){
                // 新增
                StatisticsTzjHour hour = new StatisticsTzjHour();
                hour.setDay(statisticDay);
                hour.setHour(String.valueOf(GetDate.getHour(new Date())));
                hour.setRegistCount(registCount);
                hour.setTenderfirstCount(tenderFirstCount);
                hour.setAddTime(GetDate.getNowTime10());
                hour.setUpdateTime(GetDate.getNowTime10());
                statisticsTzjHourMapper.insertSelective(hour);
                
            }else {
                // 更新
                StatisticsTzj statistics = statisticsList.get(0);
                StatisticsTzjHour hour = new StatisticsTzjHour();
                hour.setDay(statisticDay);
                hour.setHour(String.valueOf(GetDate.getHour(new Date())));
                hour.setRegistCount(registCount - statistics.getRegistCount());
                hour.setTenderfirstCount(tenderFirstCount - statistics.getTenderfirstCount());
                hour.setAddTime(GetDate.getNowTime10());
                hour.setUpdateTime(GetDate.getNowTime10());
                statisticsTzjHourMapper.insertSelective(hour);
            }
            
        }
        
        // 日统计数据新增
        StatisticsTzj record = new StatisticsTzj();
        record.setAddTime(GetDate.getNowTime10());
        record.setCardbindCount(cardbindCount);
        record.setCardbindRate(cardbindRate);
        record.setDay(statisticDay);
        record.setOpenCount(openCount);
        record.setOpenRate(openRate);
        record.setRechargeCount(rechargeCount);
        record.setRechargenewCount(rechargenewCount);
        record.setRegistCount(registCount);
        record.setTenderAgainCount(tenderAgainCount);
        record.setTenderAgainRate(tenderAgainRate);
        record.setTenderCount(tenderCount);
        record.setTenderfirstCount(tenderFirstCount);
        record.setTenderMoney(new BigDecimal(tenderMoney));
        record.setTenderfirstMoney(new BigDecimal(tenderfirstMoney));
        record.setTendernewCount(tenderNewCount);
        record.setTendernewRate(tenderNewRate);
        record.setUpdateTime(GetDate.getNowTime10());
        
        statisticsTzjMapper.insertSelective(record);
        
       
        
    }
    
}
