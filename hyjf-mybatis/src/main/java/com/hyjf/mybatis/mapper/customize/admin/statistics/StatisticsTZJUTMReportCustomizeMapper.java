package com.hyjf.mybatis.mapper.customize.admin.statistics;

import java.util.List;
import java.util.Map;

public interface StatisticsTZJUTMReportCustomizeMapper {
    List<Map<String, Object>> getRegistCount(Map<String, Object> paraMap);
    
    List<Map<String, Object>> getOpenCount(Map<String, Object> paraMap);
    
    List<Map<String, Object>> getCardBindCount(Map<String, Object> paraMap);
    
    List<Map<String, Object>> getRechargeCount(Map<String, Object> paraMap);
    
    List<Map<String, Object>> getRechargeNewCount(Map<String, Object> paraMap);
    
    List<Map<String, Object>> getTenderMoney(Map<String, Object> paraMap);
    
    List<Map<String, Object>> getTenderFirstMoney(Map<String, Object> paraMap);
    
    List<Map<String, Object>> getTenderCount(Map<String, Object> paraMap);
    
    List<Map<String, Object>> getTenderNewCount(Map<String, Object> paraMap);
    
    List<Map<String, Object>> getTenderFirstCount(Map<String, Object> paraMap);
    
    List<Map<String, Object>> getTenderAgainCount(Map<String, Object> paraMap);
}
