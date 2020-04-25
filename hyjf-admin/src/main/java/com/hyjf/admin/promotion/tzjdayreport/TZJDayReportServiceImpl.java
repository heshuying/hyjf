package com.hyjf.admin.promotion.tzjdayreport;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.StatisticsTzj;
import com.hyjf.mybatis.model.auto.StatisticsTzjHour;

@Service
public class TZJDayReportServiceImpl extends BaseServiceImpl implements TZJDayReportService {

    @Override
    public List<StatisticsTzj> selectRecordList(Map<String, Object> paraMap){
        return statisticsTZJCustomizeMapper.selectRecordList(paraMap);
    }
    
    @Override
    public int countRecordTotal(Map<String, Object> paraMap){
        return statisticsTZJCustomizeMapper.countRecordTotal(paraMap);
    }
    
    @Override
    public List<StatisticsTzj> selectDayList(Map<String, Object> paraMap){
        return statisticsTZJCustomizeMapper.selectDayList(paraMap);
    }

    @Override
    public List<StatisticsTzjHour> selectHourList(Map<String, Object> paraMap){
        return statisticsTZJCustomizeMapper.selectHourList(paraMap);
    }
    
    @Override
    public String selectDayUpdateTime(){
        return statisticsTZJCustomizeMapper.selectDayUpdateTime();
    }
    
    @Override
    public String selectHourUpdateTime(){
        return statisticsTZJCustomizeMapper.selectHourUpdateTime();
    }
    
    @Override
    public int getTenderCountTotal(Map<String, Object> paraMap){
        return statisticsTZJCustomizeMapper.getTenderCountTotal(paraMap);
    }
    
    @Override
    public double getTenderMoneyTotal(Map<String, Object> paraMap){
        return statisticsTZJCustomizeMapper.getTenderMoneyTotal(paraMap);
    }

}
