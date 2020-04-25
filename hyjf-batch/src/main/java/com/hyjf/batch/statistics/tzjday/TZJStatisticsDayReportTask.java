package com.hyjf.batch.statistics.tzjday;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.mapper.auto.StatisticsTzjMapper;

/**
 * 
 * 投之家日统计报表任务
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年3月23日
 * @see 下午3:21:02
 */
public class TZJStatisticsDayReportTask {

    Logger _log = LoggerFactory.getLogger(TZJStatisticsDayReportTask.class);
    
    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static Boolean isOver = true;

    @Autowired
    TZJStatisticsDayReportService tzjStatisticsDayReportService;
    
	@Autowired
	protected StatisticsTzjMapper statisticsTzjMapper; 

    public void run() {
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
     */
    private void statisticsDay(){
        _log.info("投之家日统计报表开始...");
//      String[] statisticsDay = {"2017-12-05", "2017-12-06", "2017-12-07", "2017-12-08", "2017-12-09", "2017-12-10"};
//      StatisticsTzjExample example = new StatisticsTzjExample();
//      example.createCriteria().andDayEqualTo("2017-12-07");
//      List<StatisticsTzj> statisticsList = statisticsTzjMapper.selectByExample(example);
//      if(statisticsList == null || statisticsList.isEmpty()){
//          for(String day : statisticsDay){
//          	tzjStatisticsDayReportService.updateStatistics(day, tzjStatisticsDayReportService.getStatistics(day));
//          }
//      }
      
      tzjStatisticsDayReportService.updateStatistics(GetDate.formatDate(), tzjStatisticsDayReportService.getStatistics(GetDate.formatDate()));
      
      _log.info("投之家日统计报表结束。。。");
    }
    
}
