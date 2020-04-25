package com.hyjf.mybatis.mapper.customize.admin.statistics;

import java.util.Map;

public interface AdminTZJStatisticsDayReportCustomizeMapper {
    int getRegistCount(Map<String, Object> paraMap);
    
    int getOpenCount(Map<String, Object> paraMap);
    
    int getCardBindCount(Map<String, Object> paraMap);
    
    int getRechargeCount(Map<String, Object> paraMap);
    
    int getRechargeNewCount(Map<String, Object> paraMap);
    
    double getTenderMoney(Map<String, Object> paraMap);
    
    double getTenderFirstMoney(Map<String, Object> paraMap);
    
    int getTenderCount(Map<String, Object> paraMap);
    
    int getTenderNewCount(Map<String, Object> paraMap);
    
    int getTenderFirstCount(Map<String, Object> paraMap);
    
    int getTenderAgainCount(Map<String, Object> paraMap);
}
