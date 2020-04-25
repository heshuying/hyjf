package com.hyjf.batch.coupon.repaystatistic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.CouponRepayMonitor;
import com.hyjf.mybatis.model.auto.CouponRepayMonitorExample;
import com.hyjf.mybatis.model.auto.HolidaysConfig;
import com.hyjf.mybatis.model.auto.HolidaysConfigExample;

/**
 * 
 * 自动生成加息券每日收益统计数据
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月14日
 * @see 下午4:58:24
 */
@Service
public class CouponRepayStatisticServiceImpl extends BaseServiceImpl implements CouponRepayStatisticService{

    public static final String THIS_CLASS = CouponRepayStatisticServiceImpl.class.getName();
    
    public static final String DATE_FORMAT = "yyyy年MM月dd日";
    
    public static final String DATE_FORMAT_2 = "yyyy-MM-dd";
    
    /**
     * 
     * 自动生成加息券每日收益统计数据
     * @author hsy
     * @return
     * @see com.hyjf.batch.coupon.repaystatistic.CouponRepayStatisticService#updateOrSaveRecord()
     */
    @Override
    public int updateOrSaveCouponStatistic() {
        LogUtil.startLog(THIS_CLASS, "updateOrSaveCouponStatistic", "自动生成加息券每日收益统计数据");
        int result = 0;
        
        //计算开始时间
        Calendar calendarStart = this.getStatisticStartTime();
        Long timeStart = calendarStart.getTime().getTime()/1000;
        
        //计算结束时间
        Calendar calendarEnd = Calendar.getInstance(); 
        calendarEnd.setTime(calendarStart.getTime());
        calendarEnd.add(Calendar.DAY_OF_MONTH, 3);
        this.getStatisticEndTime(calendarEnd);
        long timeEnd = calendarEnd.getTime().getTime()/1000;
        
        //统计待收收益
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("timeStart", timeStart);
        paraMap.put("timeEnd", timeEnd);
        
        List<Map<String, Object>> interestWaitList = couponRecoverCustomizeMapper.selectCouponInterestWaitToday(paraMap);
        interestWaitList = processInterestWaitList(interestWaitList, calendarEnd);
        
        //统计已收收益
        BigDecimal interestReceived = couponRecoverCustomizeMapper.selectCouponInterestReceivedToday(paraMap);
        if(interestReceived == null){
            interestReceived = BigDecimal.ZERO;
        }
        
        List<BigDecimal> interestWaitFinal = getInterestWaitListFinal(interestWaitList);
        
        for(int i=0; i<interestWaitFinal.size(); i++) {
            Calendar cc = this.getStatisticStartTime();
            cc.add(Calendar.DAY_OF_MONTH, i);
            String nowDay = GetDate.formatDate(cc.getTime());
            String nowWeek = GetDate.getWeekOfDate(cc.getTime());
            
            if(i > 0){
                interestReceived = BigDecimal.ZERO;
            }
            
            CouponRepayMonitorExample example = new CouponRepayMonitorExample();
            CouponRepayMonitorExample.Criteria criteria = example.createCriteria();
            criteria.andDayEqualTo(nowDay);
            List<CouponRepayMonitor> monitors = couponRepayMonitorMapper.selectByExample(example);
            
            if(monitors == null || monitors.isEmpty()){ 
                //新增
                CouponRepayMonitor monitor = new CouponRepayMonitor();
                monitor.setDay(nowDay);
                monitor.setWeek(nowWeek);
                monitor.setInterestWaitTotal(interestWaitFinal.get(i));
                monitor.setInterestYesTotal(interestReceived);
                monitor.setAddTime(GetDate.getNowTime10());
                monitor.setAddUser(CustomConstants.USERID_ADMIN);
                monitor.setUpdateTime(GetDate.getNowTime10());
                monitor.setUpdateUser(CustomConstants.USERID_ADMIN);
                monitor.setDelFlg(0);
                result = couponRepayMonitorMapper.insertSelective(monitor);
            }else { 
                //更新
                CouponRepayMonitor monitor = monitors.get(0);
                monitor.setInterestWaitTotal(interestWaitFinal.get(i));
                monitor.setInterestYesTotal(interestReceived);
                monitor.setUpdateTime(GetDate.getNowTime10());
                monitor.setUpdateUser(CustomConstants.USERID_ADMIN);
                
                result = couponRepayMonitorMapper.updateByPrimaryKeySelective(monitor);
            }
        }
        
        LogUtil.endLog(THIS_CLASS, "updateOrSaveCouponStatistic", "自动生成加息券每日收益统计数据");
        return result;
    }
    
    /**
     * 
     * 统计当前三天代收收益列表
     * @author hsy
     * @param interestWaitListProcessed
     * @return
     */
    public List<BigDecimal> getInterestWaitListFinal(List<Map<String, Object>> interestWaitListProcessed){
        List<BigDecimal> result = new ArrayList<BigDecimal>();
        BigDecimal interestWaitHoliday =  getInterestWaitHolidayTotal(interestWaitListProcessed);
        if(interestWaitHoliday.compareTo(BigDecimal.ZERO) == 0){
            for(int i=0; i<3; i++){
                result.add((BigDecimal)interestWaitListProcessed.get(i).get("interest_sum"));
            }
        }else {
            for(int i=0; i<3; i++){
                String recoverTime = (String)interestWaitListProcessed.get(i).get("recover_time");
                Calendar recoverTimeC = Calendar.getInstance();
                recoverTimeC.setTime(GetDate.stringToDate3(recoverTime, DATE_FORMAT_2));
                if(this.checkIsHoliday(recoverTimeC)){
                    result.add(BigDecimal.ZERO);
                    continue;
                }else{
                    BigDecimal sumToday = (BigDecimal)interestWaitListProcessed.get(i).get("interest_sum");
                    recoverTimeC.add(Calendar.DAY_OF_MONTH, 1);
                    if(this.checkIsHoliday(recoverTimeC)){
                        BigDecimal sumHoliday = this.getInterestWaitHoliday(interestWaitListProcessed, recoverTimeC);
                        sumToday = sumToday.add(sumHoliday);
                    }
                    result.add(sumToday);
                }
            }
        }
        
        return result;
    }
    
    /**
     * 
     * 将不连续的待收收益列表处理为连续的收益列表
     * @author hsy
     * @param interestWaitList
     * @param calendarEnd
     * @return
     */
    public List<Map<String, Object>> processInterestWaitList(List<Map<String, Object>> interestWaitList, Calendar calendarEnd){
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Calendar calendarNow = this.getStatisticStartTime();  
        
        while(calendarNow.get(Calendar.DAY_OF_YEAR) < calendarEnd.get(Calendar.DAY_OF_YEAR)){
            String dateNowStr = GetDate.formatDate(calendarNow.getTime());
            boolean dataExist = false;
            for(Map<String, Object> interest : interestWaitList){
                if(interest.get("recover_time").equals(dateNowStr)){
                    Map<String, Object> interestMap = new HashMap<String, Object>();
                    interestMap.put("recover_time", dateNowStr);
                    interestMap.put("interest_sum", interest.get("interest_sum"));
                    resultList.add(interestMap);
                    dataExist = true;
                }
            }
            
            if(!dataExist){
                Map<String, Object> interestMap = new HashMap<String, Object>();
                interestMap.put("recover_time", dateNowStr);
                interestMap.put("interest_sum", BigDecimal.ZERO);
                resultList.add(interestMap);
            }
            
            calendarNow.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        return resultList;
    }
    
    /**
     * 
     * 取得节假日代收收益总和
     * @author hsy
     * @param interestWaitListProcessed
     * @return
     */
    public BigDecimal getInterestWaitHolidayTotal(List<Map<String, Object>> interestWaitListProcessed){
        BigDecimal result = BigDecimal.ZERO;
        for(Map<String, Object> interestMap : interestWaitListProcessed){
            String recoverTime = (String)interestMap.get("recover_time");
            Calendar recoverTimeC = Calendar.getInstance();
            recoverTimeC.setTime(GetDate.stringToDate3(recoverTime, DATE_FORMAT_2));
            if(this.checkIsHoliday(recoverTimeC)){
                result = result.add((BigDecimal)interestMap.get("interest_sum"));
            }
        }
        
        return result;
    }
    
    /**
     * 
     * 获取紧邻当前时间的假期的待还总额
     * @author hsy
     * @param interestWaitListProcessed
     * @param calendar
     * @return
     */
    public BigDecimal getInterestWaitHoliday(List<Map<String, Object>> interestWaitListProcessed, Calendar calendar){
        BigDecimal result = BigDecimal.ZERO;
        int startIndex = 0;
        for(int i=0; i<interestWaitListProcessed.size(); i++){
            String dateStr = GetDate.formatDate(calendar.getTime());
            if(interestWaitListProcessed.get(i).get("recover_time").equals(dateStr)){
                startIndex = i;
                break;
            }
        }
        
        for(int i=startIndex; i<interestWaitListProcessed.size(); i++){
            Calendar recoverTimeC = Calendar.getInstance();
            recoverTimeC.setTime(GetDate.stringToDate3((String)interestWaitListProcessed.get(i).get("recover_time"), DATE_FORMAT_2));
            if(this.checkIsHoliday(recoverTimeC)){
                result = result.add((BigDecimal)interestWaitListProcessed.get(i).get("interest_sum"));
                recoverTimeC.add(Calendar.DAY_OF_MONTH, 1);
                if(!this.checkIsHoliday(recoverTimeC)){
                    break;
                }
            }
        }
        
        return result;
    }
    
    /**
     * 
     * 获取节假日列表
     * @author hsy
     * @return
     */
    public List<HolidaysConfig> getHolidayList(){
    	HolidaysConfigExample example=new HolidaysConfigExample();
    	example.setOrderByClause("statr_time asc");
    	return holidaysConfig.selectByExample(example);
//        List<HolidaysConfig> holidays = new ArrayList<HolidaysConfig>();
//        HolidaysConfig holidayconfig = new HolidaysConfig();
//        holidayconfig.setStatrTime("2016年07月26日");
//        holidayconfig.setEndTime("2016年07月29日");
//        holidayconfig.setEventsName("我的假期");
//        holidays.add(holidayconfig);
//        return holidays;
    }
    
    /**
     * 
     * 计算统计开始时间
     * @author hsy
     * @return
     */
    public Calendar getStatisticStartTime(){
        Calendar calendar = Calendar.getInstance();  
//        calendar.set(2016, 6, 23);
        calendar.set(Calendar.HOUR_OF_DAY, 0);  
        calendar.set(Calendar.MINUTE, 0);  
        calendar.set(Calendar.SECOND,0);  
        calendar.set(Calendar.MILLISECOND, 0);  
        return calendar;
    }
    
    /**
     * 
     * 递归计算统计结束时间
     * @author hsy
     * @param calendar
     */
    public void getStatisticEndTime(Calendar calendar){
        calendar.set(Calendar.HOUR_OF_DAY, 0);  
        calendar.set(Calendar.MINUTE, 0);  
        calendar.set(Calendar.SECOND, 0);  
        calendar.set(Calendar.MILLISECOND, 0);  
        String week = GetDate.getWeekOfDate(calendar.getTime());
        if(week.equals("周六") || week.equals("周日")){
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            getStatisticEndTime(calendar);
        }
        
        List<HolidaysConfig> holidays = this.getHolidayList();
        for(HolidaysConfig holiday : holidays){
           Long hstart = GetDate.stringToDate3(holiday.getStatrTime(), DATE_FORMAT).getTime();
           Long hend = GetDate.stringToDate3(holiday.getEndTime(), DATE_FORMAT).getTime();
           if(calendar.getTime().getTime() >= hstart && calendar.getTime().getTime() <= hend){
               calendar.add(Calendar.DAY_OF_MONTH, 1);
               getStatisticEndTime(calendar);
           }
        }
        
        return;
    }
    
    /**
     * 
     * 当前日期是否是节假日判断
     * @author hsy
     * @param calendar
     * @return
     */
    public boolean checkIsHoliday(Calendar calendar){
        calendar.set(Calendar.HOUR_OF_DAY, 0);  
        calendar.set(Calendar.MINUTE, 0);  
        calendar.set(Calendar.SECOND, 0);  
        calendar.set(Calendar.MILLISECOND, 0);  
        String week = GetDate.getWeekOfDate(calendar.getTime());
        if(week.equals("周六") || week.equals("周日")){
            return true;
        }
        
        List<HolidaysConfig> holidays = this.getHolidayList();
        for(HolidaysConfig holiday : holidays){
           Long hstart = GetDate.stringToDate3(holiday.getStatrTime(), DATE_FORMAT).getTime();
           Long hend = GetDate.stringToDate3(holiday.getEndTime(), DATE_FORMAT).getTime();
           if(calendar.getTime().getTime() >= hstart && calendar.getTime().getTime() <= hend){
               return true;
           }
        }
        
        return false;
    }
    
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();  
        calendar.add(Calendar.DAY_OF_MONTH, 2);
//        CouponRepayStatisticServiceImpl service = new CouponRepayStatisticServiceImpl();
//        System.out.println(service.getStatisticStartTime().getTime());
//        service.getStatisticEndTime(calendar);
//        System.out.println(service.getStatisticEndTime(calendar.getTime()));
//        System.out.println(GetDate.formatDateTime(calendar.getTime().getTime()));
//        System.out.println(GetDate.stringToDate3("2016年08月11日", "yyyy年MM月dd日").getTime());
    }

}
