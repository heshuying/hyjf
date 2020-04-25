package com.hyjf.batch.statistics.tzjutm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.StatisticsTzjUtm;
import com.hyjf.mybatis.model.auto.StatisticsTzjUtmExample;

/**
 * 
 * 此处为类说明
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年3月23日
 * @see 下午3:21:36
 */
@Service(value="TZJStatisticsUTMReportService")
public class TZJStatisticsUTMReportServiceImpl extends BaseServiceImpl implements TZJStatisticsUTMReportService{
    // 统计开始时间 2017-03-27 00:00:00
    public static final String timeStart = "1490544000";
    // 统计结束时间
    public static final String timeEnd = String.valueOf(GetDate.getDayEnd10(new Date()));
 
   /* @Override
	public void doStatistics() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
    	String[] statisticsDay = {"2017-12-05", "2017-12-06", "2017-12-07", "2017-12-08", "2017-12-09", "2017-12-10"};
        StatisticsTzjUtmExample example = new StatisticsTzjUtmExample();
        example.createCriteria().andDayEqualTo("2017-12-06");
        example.or(example.createCriteria().andDayEqualTo("2017-12-07"));
        List<StatisticsTzjUtm> statisticsList = statisticsTzjUtmMapper.selectByExample(example);
        if(statisticsList == null || statisticsList.isEmpty()){
            for(String day : statisticsDay){
                updateStatistics(day, getStatistics(day));
            }
        }
        
        updateStatistics(GetDate.formatDate(), getStatistics(GetDate.formatDate()));
    }*/
    
    @Override
	public Map<String,List<Map<String, Object>>> getStatistics(String statisticDay){
    	Map<String, List<Map<String, Object>>> resultMap = new HashMap<String, List<Map<String, Object>>>();
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("statisticDay", statisticDay);
        
        // 统计数据
        List<Map<String, Object>> tenderCount = statisticsTZJUTMReportCustomizeMapper.getTenderCount(paraMap);
        List<Map<String, Object>> tenderNewCount = statisticsTZJUTMReportCustomizeMapper.getTenderNewCount(paraMap);
        List<Map<String, Object>> tenderFirstCount = statisticsTZJUTMReportCustomizeMapper.getTenderFirstCount(paraMap);
        List<Map<String, Object>> tenderAgainCount = statisticsTZJUTMReportCustomizeMapper.getTenderAgainCount(paraMap);
        
        List<Map<String, Object>> registCount = statisticsTZJUTMReportCustomizeMapper.getRegistCount(paraMap);
        List<Map<String, Object>> openCount = statisticsTZJUTMReportCustomizeMapper.getOpenCount(paraMap);
        List<Map<String, Object>> cardbindCount = statisticsTZJUTMReportCustomizeMapper.getCardBindCount(paraMap);
        List<Map<String, Object>> rechargeCount = statisticsTZJUTMReportCustomizeMapper.getRechargeCount(paraMap);
        List<Map<String, Object>> rechargenewCount = statisticsTZJUTMReportCustomizeMapper.getRechargeNewCount(paraMap);
        List<Map<String, Object>> tenderMoney = statisticsTZJUTMReportCustomizeMapper.getTenderMoney(paraMap);
        List<Map<String, Object>> tenderfirstMoney = statisticsTZJUTMReportCustomizeMapper.getTenderFirstMoney(paraMap);
        
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
    
    /**
     * 
     * 统计实现
     * @author hsy
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @see com.hyjf.batch.statistics.tzjutm.TZJStatisticsUTMReportService#doStatistics()
     */
    @Override
	public synchronized void updateStatistics(String statisticDay, Map<String,List<Map<String, Object>>> data) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("statisticDay", statisticDay);
        
        // 统计数据
        List<Map<String, Object>> tenderCount = data.get("tenderCount");
        List<Map<String, Object>> tenderNewCount = data.get("tenderNewCount");
        List<Map<String, Object>> tenderFirstCount = data.get("tenderFirstCount");
        List<Map<String, Object>> tenderAgainCount = data.get("tenderAgainCount");
        
        List<Map<String, Object>> registCount = data.get("registCount");
        List<Map<String, Object>> openCount = data.get("openCount");
        List<Map<String, Object>> cardbindCount = data.get("cardbindCount");
        List<Map<String, Object>> rechargeCount = data.get("rechargeCount");
        List<Map<String, Object>> rechargenewCount = data.get("rechargenewCount");
        List<Map<String, Object>> tenderMoney = data.get("tenderMoney");
        List<Map<String, Object>> tenderfirstMoney = data.get("tenderfirstMoney");
        
        StatisticsTzjUtmExample example = new StatisticsTzjUtmExample();
        example.createCriteria().andDayEqualTo(statisticDay);
        List<StatisticsTzjUtm> statisticsList = statisticsTzjUtmMapper.selectByExample(example);
        
        if(!statisticsList.isEmpty()){
            statisticsTzjUtmMapper.deleteByExample(example);
        }
        
        // 逐个保存
        save(registCount, "setRegistCount", "int");
        save(openCount, "setOpenCount", "int");
        save(cardbindCount, "setCardbindCount", "int");
        save(rechargeCount, "setRechargeCount", "int");
        save(rechargenewCount, "setRechargenewCount", "int");
        save(tenderMoney, "setTenderMoney", "decimal");
        save(tenderfirstMoney, "setTenderfirstMoney", "decimal");
        save(tenderCount, "setTenderCount", "int");
        save(tenderNewCount, "setTendernewCount", "int");
        save(tenderFirstCount, "setTenderfirstCount", "int");
        save(tenderAgainCount, "setTenderAgainCount", "int");
        
        // 计算各种比率
//        StatisticsTzjUtmExample example = new StatisticsTzjUtmExample();
//        List<StatisticsTzjUtm> statisticsList = statisticsTzjUtmMapper.selectByExample(example);
//        
//        for(StatisticsTzjUtm item : statisticsList){
//            // 开户率
//            BigDecimal openRate = BigDecimal.ZERO;
//            // 绑卡率
//            BigDecimal cardbindRate = BigDecimal.ZERO;
//            // 新投比
//            BigDecimal tenderNewRate = BigDecimal.ZERO;
//            // 复投比
//            BigDecimal tenderAgainRate = BigDecimal.ZERO;
//            
//            BigDecimal value100 = new BigDecimal(100);
//            
//            int registCountInt = item.getRegistCount();
//            int openCountInt = item.getOpenCount();
//            int cardbindCountInt = item.getCardbindCount();
//            int tenderNewCountInt = item.getTendernewCount();
//            int tenderCountInt = item.getTenderCount();
//            int tenderAgainCountInt = item.getTenderAgainCount();
//            
//            if(registCountInt != 0){
//                openRate = new BigDecimal(openCountInt).divide(new BigDecimal(registCountInt), 2, BigDecimal.ROUND_HALF_UP).multiply(value100);
//                cardbindRate = new BigDecimal(cardbindCountInt).divide(new BigDecimal(registCountInt) , 2, BigDecimal.ROUND_HALF_UP).multiply(value100);
//                tenderNewRate = new BigDecimal(tenderNewCountInt).divide(new BigDecimal(registCountInt) , 2, BigDecimal.ROUND_HALF_UP).multiply(value100);
//            }
//            
//            if(tenderCountInt != 0){
//                tenderAgainRate = new BigDecimal(tenderAgainCountInt).divide(new BigDecimal(tenderCountInt), 2, BigDecimal.ROUND_HALF_UP).multiply(value100);
//            }
//            
//            
//            item.setOpenRate(openRate);
//            item.setCardbindRate(cardbindRate);
//            item.setTendernewRate(tenderNewRate);
//            item.setTenderAgainRate(tenderAgainRate);
//
//            statisticsTzjUtmMapper.updateByPrimaryKeySelective(item);
//        }
        
    }
    
    /**
     * 
     * 保存到数据库
     * @author hsy
     * @param dataList
     * @param methodName
     * @param paraType
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public void save(List<Map<String, Object>> dataList, String methodName, String paraType) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        
        for(Map<String, Object> item : dataList){
            if(item == null || item.get("source_name") == null){
                continue;
            }
            
            String day = String.valueOf(item.get("day"));
            String channelName = String.valueOf(item.get("source_name"));
            String channelId = String.valueOf(item.get("source_id"));
            Object count = item.get("total_num");
            
            StatisticsTzjUtmExample example = new StatisticsTzjUtmExample();
            example.createCriteria().andChannelNameEqualTo(channelName).andDayEqualTo(day);
            List<StatisticsTzjUtm> statisticsList = statisticsTzjUtmMapper.selectByExample(example);
            
            if(statisticsList.isEmpty()){
                StatisticsTzjUtm record = new StatisticsTzjUtm();
                record.setAddTime(GetDate.getNowTime10());
                record.setUpdateTime(GetDate.getNowTime10());
                record.setChannelName(channelName);
                record.setChannelFlg(channelId == null ? null : Integer.parseInt(channelId));
                record.setDay(day);
                
                if(paraType.equals("int")){
                    Method method = record.getClass().getMethod(methodName, Integer.class);
                    method.invoke(record, ((Long)count).intValue());
                    
                }else if(paraType.equals("decimal")){
                    Method method = record.getClass().getMethod(methodName,BigDecimal.class);
                    method.invoke(record, (BigDecimal)count);
                    
                }
                
                statisticsTzjUtmMapper.insertSelective(record);
            }else {
                
                StatisticsTzjUtm record = statisticsList.get(0);
                record.setUpdateTime(GetDate.getNowTime10());
                record.setChannelName(channelName);
                record.setChannelFlg(channelId == null ? null : Integer.parseInt(channelId));
                record.setDay(day);
                
                if(paraType.equals("int")){
                    Method method = record.getClass().getMethod(methodName, Integer.class);
                    method.invoke(record, ((Long)count).intValue());
                    
                }else if(paraType.equals("decimal")){
                    Method method = record.getClass().getMethod(methodName,BigDecimal.class);
                    method.invoke(record, (BigDecimal)count);
                    
                }
                
                statisticsTzjUtmMapper.updateByPrimaryKeySelective(record);
            }
        }
        
    }
    
    public static void main(String[] args) {
        System.out.println(GetDate.getHour(new Date()));
    }
    
}
