package com.hyjf.admin.manager.holidaysconfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.HolidaysConfig;
import com.hyjf.mybatis.model.auto.HolidaysConfigExample;

@Service
public class HolidaysConfigServiceImpl extends BaseServiceImpl implements HolidaysConfigService {
   

    /**
     * 获取列表列表
     * 
     * @return
     */
    public List<HolidaysConfig> getRecordList(HolidaysConfig h, int limitStart, int limitEnd) {
        
        HolidaysConfigExample example=new HolidaysConfigExample();

        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        example.createCriteria();
        return holidaysConfig.selectByExample(example);
    }

    /**
     * 获取单个维护
     * 
     * @return
     */
    public HolidaysConfig getRecord(Integer record) {
        HolidaysConfig hConfig= holidaysConfig.selectByPrimaryKey(record);
        hConfig.setStatrTime(returnDateFormat(hConfig.getStatrTime()));
        hConfig.setEndTime(returnDateFormat(hConfig.getEndTime()));
        return hConfig;
    }



    /**
     * 维护插入
     * 
     * @param record
     */
    public void insertRecord(HolidaysConfigBean record) {
        record.setCreatetime(GetDate.getDate());
        record.setUpdatetime(GetDate.getDate());
        record.setStatrTime(requestDateFormat(record.getStatrTime()));
        record.setEndTime(requestDateFormat(record.getEndTime()));
        record.setYear(record.getStatrTime().substring(0,4));
        holidaysConfig.insertSelective(record);
    }
    /**
     * 维护更新
     * 
     * @param record
     */
    public void updateRecord(HolidaysConfigBean record) {
        record.setUpdatetime(GetDate.getDate());
        record.setStatrTime(requestDateFormat(record.getStatrTime()));
        record.setEndTime(requestDateFormat(record.getEndTime()));
        record.setYear(record.getStatrTime().substring(0,4));
        holidaysConfig.updateByPrimaryKeySelective(record);
    }


    private  String requestDateFormat(String dateString) {
        SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat rsim=new SimpleDateFormat("yyyy年MM月dd日");
        Date date=new Date();
        try {
            date = sim.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rsim.format(date);
    }
    
    
    private  String returnDateFormat(String dateString) {
        SimpleDateFormat rsim=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sim=new SimpleDateFormat("yyyy年MM月dd日");
        Date date=new Date();
        try {
            date = sim.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rsim.format(date);
    }
    
    
    
}
