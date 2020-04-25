package com.hyjf.batch.statistics.tzjutm;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.mapper.auto.StatisticsTzjUtmMapper;

/**
 * 
 * 投之家渠道统计报表任务
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年3月23日
 * @see 下午3:21:02
 */
public class TZJStatisticsUTMReportTask {

    Logger _log = LoggerFactory.getLogger(TZJStatisticsUTMReportTask.class);
    
    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static Boolean isOver = true;

    @Autowired
    TZJStatisticsUTMReportService tzjStatisticsUTMReportService;

    @Autowired
    protected StatisticsTzjUtmMapper statisticsTzjUtmMapper;

    public void run() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (isOver) {
            try {
                isOver = false;
                
                statisticsDay();
            } finally {
                isOver = true;
            }
        }
    }
    
    /**
     * 统计报表
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     */
    private void statisticsDay() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        _log.info("投之家渠道统计报表开始...");
//      String[] statisticsDay = {"2017-12-05", "2017-12-06", "2017-12-07", "2017-12-08"};
//      StatisticsTzjUtmExample example = new StatisticsTzjUtmExample();
//      example.createCriteria().andDayEqualTo("2017-12-06");
//      example.or(example.createCriteria().andDayEqualTo("2017-12-07"));
//      List<StatisticsTzjUtm> statisticsList = statisticsTzjUtmMapper.selectByExample(example);
//      if(statisticsList == null || statisticsList.isEmpty()){
//          for(String day : statisticsDay){
//          	tzjStatisticsUTMReportService.updateStatistics(day, tzjStatisticsUTMReportService.getStatistics(day));
//          }
//      }
      
      tzjStatisticsUTMReportService.updateStatistics(GetDate.formatDate(), tzjStatisticsUTMReportService.getStatistics(GetDate.formatDate()));
      _log.info("投之家渠道统计报表结束。。。");
    }
    
}
