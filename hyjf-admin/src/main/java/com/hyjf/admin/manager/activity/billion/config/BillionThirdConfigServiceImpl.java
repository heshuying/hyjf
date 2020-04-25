package com.hyjf.admin.manager.activity.billion.config;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.mybatis.model.auto.ActivityBillionThirdConfig;
import com.hyjf.mybatis.model.auto.ActivityBillionThirdConfigExample;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.auto.CouponConfigExample;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

@Service
public class BillionThirdConfigServiceImpl extends BaseServiceImpl implements BillionThirdConfigService {
    public static JedisPool pool = RedisUtils.getConnection();
	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param form
	 * @return
	 * @author Michael
	 */
		
	@Override
	public Integer selectRecordCount(BillionThirdConfigBean form) {
		return activityBillionThirdConfigMapper.countByExample(new ActivityBillionThirdConfigExample());
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param form
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<ActivityBillionThirdConfig> getRecordList(BillionThirdConfigBean form, int limitStart, int limitEnd) {
		return activityBillionThirdConfigMapper.selectByExample(new ActivityBillionThirdConfigExample());
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param record
	 * @return
	 * @author Michael
	 */
		
	@Override
	public ActivityBillionThirdConfig getRecord(Integer recordId) {
		ActivityBillionThirdConfig record = activityBillionThirdConfigMapper.selectByPrimaryKey(recordId);
	    return record;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param record
	 * @return
	 * @author Michael
	 */
		
	@Override
	public boolean isExistsRecord(ActivityBillionThirdConfig record) {
		 if (record.getId() == null) {
	            return false;
	        }
		 	ActivityBillionThirdConfigExample example = new ActivityBillionThirdConfigExample();
		 	ActivityBillionThirdConfigExample.Criteria cra = example.createCriteria();
	        cra.andIdEqualTo(record.getId());
	        int cnt = activityBillionThirdConfigMapper.countByExample(example);
	        if (cnt > 0) {
	            return true;
	        }
	        return false;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param record
	 * @author Michael
	 */
		
	@Override
	public void updateRecord(ActivityBillionThirdConfig record) {
//		if(record.getSecKillNum() == null){
//			record.setSecKillNum(0);
//		}
	    
		this.activityBillionThirdConfigMapper.updateByPrimaryKeySelective(record);
		ActivityBillionThirdConfig activityBillionThirdConfig=activityBillionThirdConfigMapper.selectByPrimaryKey(record.getId());
		Jedis jedis=pool.getResource();
		List<Object> result=null;
		int count=1;
        do {
            if("OK".equals(jedis.watch("billionThird"+activityBillionThirdConfig.getId()))){
                Transaction tx = jedis.multi();
                int remainingNum=activityBillionThirdConfig.getCouponNum()-activityBillionThirdConfig.getSecKillNum();
                if(remainingNum<=0){
                    remainingNum=0;
                }
                tx.set("billionThird"+activityBillionThirdConfig.getId(), remainingNum+"");
                result = tx.exec();
                if (result == null || result.isEmpty()) {
                    jedis.unwatch();
                }
            }
            count++;
        } while (result == null || result.isEmpty()&&count!=3);
	}
	
	
	/**
     * 
     * 根据优惠券编码查询优惠券详情
     * @author hsy
     * @param couponCode
     * @return
     */
    @Override
    public CouponConfig selectConfigByCode(String couponCode){
        //根据优惠券编码查询优惠券
        CouponConfigExample example = new CouponConfigExample();
        CouponConfigExample.Criteria caConfig = example.createCriteria();
        caConfig.andCouponCodeEqualTo(couponCode);
        List<CouponConfig> configs = couponConfigMapper.selectByExample(example);
        if(configs == null || configs.isEmpty()){
            return null;
        }
        CouponConfig config =  configs.get(0);
        if(config.getStatus()!=2){
            return null;
        }
        return config;
        
    }

}
