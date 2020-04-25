package com.hyjf.batch.activity.billion.third;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.mybatis.model.auto.ActivityBillionSecondTime;
import com.hyjf.mybatis.model.auto.ActivityBillionSecondTimeExample;
import com.hyjf.mybatis.model.auto.ActivityBillionThirdConfig;
import com.hyjf.mybatis.model.auto.ActivityBillionThirdConfigExample;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.mybatis.model.auto.CalculateInvestInterestExample;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

/**
 * 满心满亿
 * @author Michael
 */
@Service
public class BillionThirdServiceImpl extends BaseServiceImpl implements BillionThirdService {
    public static JedisPool pool = RedisUtils.getConnection();
	
	
    @Override
    public void startHourActivity(String hour) {
        boolean next=false;
        List<ActivityBillionThirdConfig> thirdConfigs=activityBillionThirdConfigMapper.selectByExample(new ActivityBillionThirdConfigExample());
        for (ActivityBillionThirdConfig activityBillionThirdConfig : thirdConfigs) {
            if(next){
                activityBillionThirdConfig.setKillStatus(2);
                activityBillionThirdConfigMapper.updateByPrimaryKeySelective(activityBillionThirdConfig);  
            }
            if(hour.equals(activityBillionThirdConfig.getSecKillTime())){
                activityBillionThirdConfig.setKillStatus(1);
                activityBillionThirdConfigMapper.updateByPrimaryKeySelective(activityBillionThirdConfig);
                next=true;
            }
            
        }
        
    }

    @Override
    public void activityReset() {
        
        Integer activityKillDay=getActivityKillDay();
        List<ActivityBillionThirdConfig> thirdConfigs=activityBillionThirdConfigMapper.selectByExample(new ActivityBillionThirdConfigExample());
        System.out.println("activityKillDay:"+activityKillDay);
        //小于100亿
        if(activityKillDay==0){
            return;
        }
        if(activityKillDay==1){
            for (ActivityBillionThirdConfig activityBillionThirdConfig : thirdConfigs) {
                activityBillionThirdConfig.setStatus(0);
                activityBillionThirdConfig.setKillStatus(0);
                activityBillionThirdConfigMapper.updateByPrimaryKeySelective(activityBillionThirdConfig);
            } 
        }
        
 
        
        if(activityKillDay > 3){
            for (ActivityBillionThirdConfig activityBillionThirdConfig : thirdConfigs) {
                activityBillionThirdConfig.setStatus(1);
                activityBillionThirdConfig.setKillStatus(3);
                activityBillionThirdConfigMapper.updateByPrimaryKeySelective(activityBillionThirdConfig);
            }
            return;
        }
        Jedis jedis=pool.getResource();
        //重置jedis中
        for (ActivityBillionThirdConfig activityBillionThirdConfig : thirdConfigs) {
            List<Object> result=null;
            int count=1;
            do {
                if("OK".equals(jedis.watch("billionThird"+activityBillionThirdConfig.getId()))){
                    Transaction tx = jedis.multi();
                    tx.set("billionThird"+activityBillionThirdConfig.getId(), activityBillionThirdConfig.getCouponNum()+"");
                    result = tx.exec();
                    if (result == null || result.isEmpty()) {
                        jedis.unwatch();
                    }
                }
                count++;
            } while (result == null || result.isEmpty()&&count!=3);
        }
        
        List<Object> result=null;
        int count=1;
        do {
            if("OK".equals(jedis.watch("userPartakeMap"))){
                Transaction tx = jedis.multi();
                tx.set("userPartakeMap", JSONArray.toJSONString(new HashMap<String,String>()));
                result =tx.exec();
                if (result == null || result.isEmpty()) {
                    jedis.unwatch();
                }
            }
            count++;
            
        } while (result == null || result.isEmpty()&&count!=3);
        
        
        for (ActivityBillionThirdConfig activityBillionThirdConfig : thirdConfigs) {
            if("10:00".equals(activityBillionThirdConfig.getSecKillTime())){
                activityBillionThirdConfig.setKillStatus(2);
            }else{
                activityBillionThirdConfig.setKillStatus(0);
            }
            activityBillionThirdConfig.setSecKillNum(0);
            activityBillionThirdConfigMapper.updateByPrimaryKeySelective(activityBillionThirdConfig);
        }
        
        
    }
	
    private Integer getActivityKillDay() {
        List<ActivityBillionSecondTime> list=activityBillionSecondTimeMapper.selectByExample(new ActivityBillionSecondTimeExample());
        if(list.get(0).getAccordTime()==0||list.get(0).getAccordTime()==null){
            return 0;
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        long nd = 24*60*60*1000;//一天的秒数
        long diff =0;
        try {
            diff =sdf.parse(sdf.format(new Date())).getTime()-sdf.parse(sdf.format(new Date(list.get(0).getAccordTime()*1000L))).getTime();

        } catch (ParseException e) {
            return 0;
        }
        long day = diff/nd;//计算差多少天
        return (int) day;
    }
    
    public CalculateInvestInterest getCalculateRecord() {
        List<CalculateInvestInterest> recordList =  calculateInvestInterestMapper.selectByExample(new CalculateInvestInterestExample());
        if(recordList != null && recordList.size() > 0){
            return recordList.get(0);
        }
        return null;
    }

    @Override
    public void startHourlyActivity() {
    	Integer activityKillDay=getActivityKillDay();
    	if(activityKillDay > 3){
            return;
        }
        SimpleDateFormat df = new SimpleDateFormat("HH:00");  
        Date d = new Date();
        String hour=df.format(d);
        boolean next=false;
        List<ActivityBillionThirdConfig> thirdConfigs=activityBillionThirdConfigMapper.selectByExample(new ActivityBillionThirdConfigExample());
        for (ActivityBillionThirdConfig activityBillionThirdConfig : thirdConfigs) {
            if(next){
                activityBillionThirdConfig.setKillStatus(2);
                activityBillionThirdConfigMapper.updateByPrimaryKeySelective(activityBillionThirdConfig); 
                next=false;
            }
            if(hour.equals(activityBillionThirdConfig.getSecKillTime())){
                activityBillionThirdConfig.setKillStatus(1);
                activityBillionThirdConfigMapper.updateByPrimaryKeySelective(activityBillionThirdConfig);
                next=true;
            }
            
        }
        
    }
	
    
}
