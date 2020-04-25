package com.hyjf.mybatis.mapper.customize.admin.statistics;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.StatisticsTzj;
import com.hyjf.mybatis.model.auto.StatisticsTzjHour;

public interface StatisticsTZJCustomizeMapper {
    
    List<StatisticsTzj> selectRecordList(Map<String, Object> paraMap);
    
    int countRecordTotal(Map<String, Object> paraMap);
    
    List<StatisticsTzj> selectDayList(Map<String, Object> paraMap);
    
    List<StatisticsTzjHour> selectHourList(Map<String, Object> paraMap);
    
    String selectDayUpdateTime();
    
    String selectHourUpdateTime();
    
    int getTenderCountTotal(Map<String, Object> paraMap);
    
    double getTenderMoneyTotal(Map<String, Object> paraMap);
    
}
