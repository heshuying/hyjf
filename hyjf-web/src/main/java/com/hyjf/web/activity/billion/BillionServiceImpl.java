package com.hyjf.web.activity.billion;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ActivityBillionOne;
import com.hyjf.mybatis.model.auto.ActivityBillionOneExample;
import com.hyjf.mybatis.model.auto.ActivityBillionSecond;
import com.hyjf.mybatis.model.auto.ActivityBillionSecondExample;
import com.hyjf.mybatis.model.auto.ActivityBillionSecondTime;
import com.hyjf.mybatis.model.auto.ActivityBillionSecondTimeExample;
import com.hyjf.mybatis.model.auto.ActivityBillionThird;
import com.hyjf.mybatis.model.auto.ActivityBillionThirdConfig;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.mybatis.model.auto.CalculateInvestInterestExample;
import com.hyjf.mybatis.model.auto.CouponUserExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.billion.ActivityBillionThirdCustomize;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.web.BaseServiceImpl;

@Service
public class BillionServiceImpl extends BaseServiceImpl implements BillionService {
    public static JedisPool pool = RedisUtils.getConnection();

	/**
	 * 获取出借总额
	 * @return
	 * @author Michael
	 */
		
	@Override
	public BigDecimal getTendSum() {
		List<CalculateInvestInterest> list = this.calculateInvestInterestMapper.selectByExample(new CalculateInvestInterestExample());
		if(list != null && list.size() > 0){
			return list.get(0).getTenderSum();
		}
		return null;
	}

	/**
	 * 获取中奖记录
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<ActivityBillionOne> getBillionOneRecords() {
		return this.activityBillionOneMapper.selectByExample(new ActivityBillionOneExample());
	}

    @Override
    public List<ActivityBillionThirdCustomize> getBillionThirdActivityList() {
        List<ActivityBillionThirdCustomize> list=billionCustomizeMapper.getBillionThirdActivityList();
        for (ActivityBillionThirdCustomize activityBillionThirdCustomize : list) {
            String remainingNum = RedisUtils.get("billionThird"+activityBillionThirdCustomize.getId());
            activityBillionThirdCustomize.setRemainingNum(remainingNum);
            
        }
        return billionCustomizeMapper.getBillionThirdActivityList();
    }

    @Override
    public synchronized  String updateUserPartakeBillionThirdActivity(String killId, Integer userId) {
        Jedis jedis=pool.getResource();
        ActivityBillionThirdConfig activityBillionThirdConfig=activityBillionThirdConfigMapper.selectByPrimaryKey(new Integer(killId));
        
        if(activityBillionThirdConfig==null||activityBillionThirdConfig.getId()==0){
            return "商品不存在";
        }
        
        if(activityBillionThirdConfig.getStatus()==1){
            return "商品已下架";
        }
        String userPartakeMapJSON = RedisUtils.get("userPartakeMap");
        Gson gson = new Gson();
        Map<String, String> userPartakeMap = gson.fromJson(userPartakeMapJSON, new TypeToken<Map<String, String>>(){}.getType());
        
        if(userPartakeMap!=null&&StringUtils.isNotBlank(userPartakeMap.get(activityBillionThirdConfig.getCouponCode()+userId+killId))){
            return "您已经抢过该券了，不要太贪心哦！";
        }
        
        List<Object> result=null;
        int count=1;
        do {
            if("OK".equals(jedis.watch("billionThird"+killId))){
                Transaction tx = jedis.multi();
                String billionThirdcount = RedisUtils.get("billionThird"+killId);
                if(billionThirdcount==null||new Integer(billionThirdcount)<=0){
                    return "活动奖品已发放完毕请等待下一个时段";
                }
                tx.set("billionThird"+killId, new Integer(billionThirdcount)-1+"");
                result = tx.exec();
                if (result == null || result.isEmpty()) {
                    jedis.unwatch();
                }
            }
            count++;
        } while (result == null || result.isEmpty()&&count!=3);
        
        
        CommonParamBean couponParamBean=new CommonParamBean();
        couponParamBean.setSendFlg(9);
        couponParamBean.setUserId(userId+"");
        couponParamBean.setPrizeGroupCode(activityBillionThirdConfig.getCouponCode());
        String jsonStr=CommonSoaUtils.sendUserCoupon(couponParamBean);
        JSONObject sendResult = JSONObject.parseObject(jsonStr);
        // 发放是否成功状态
        int sendStatus = sendResult.getIntValue("status");
        // 发放优惠券的数量
        int sendCount = sendResult.getIntValue("couponCount");
        @SuppressWarnings("unchecked")
        List<String> couponUserCodeList = (List<String>)sendResult.get("retCouponUserCodes");
        ActivityBillionThird activityBillionThird=new ActivityBillionThird();
        if(sendStatus == 0 && sendCount > 0){
            //修改活动优惠券剩余数量
            activityBillionThirdConfig.setSecKillNum(activityBillionThirdConfig.getCouponNum()-new Integer(RedisUtils.get("billionThird"+killId)));
            activityBillionThirdConfigMapper.updateByPrimaryKeySelective(activityBillionThirdConfig);
            activityBillionThird=insertActivityBillionThird(activityBillionThirdConfig,userId,couponUserCodeList);
        }
        if(userPartakeMap==null){
           userPartakeMap=new HashMap<String, String>();
        }
        // 从redis中重新取得用户抢券名单
        String userPartakeMapJSON2 = RedisUtils.get("userPartakeMap");
        Gson gson2 = new Gson();
        userPartakeMap = gson2.fromJson(userPartakeMapJSON2, new TypeToken<Map<String, String>>(){}.getType());
        String key = activityBillionThirdConfig.getCouponCode()+userId+killId;
        // 判断用户是否已经抢过
        if(!userPartakeMap.containsKey(key)){
        	userPartakeMap.put(activityBillionThirdConfig.getCouponCode()+userId+killId,activityBillionThird.getCouponId()+GetCode.getRandomCode(5) );
        	Transaction tx = jedis.multi();
            tx.set("userPartakeMap", JSONArray.toJSONString(userPartakeMap));
            tx.exec();
        }else{
        	// 已经抢过优惠券
        	// 百亿活动抢券数据回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            CouponUserExample example = new CouponUserExample();
            example.createCriteria().andCouponUserCodeEqualTo(couponUserCodeList.get(0));
            // 删除已发放的优惠券
            this.couponUserMapper.deleteByExample(example);
            List<Object> resultRollback=null;
            // 修改redis中剩余优惠券数量（+1）
            do {
            	if("OK".equals(jedis.watch("billionThird"+killId))){
                	Transaction txRollback = jedis.multi();
                	txRollback.set("billionThird"+killId, new Integer(RedisUtils.get("billionThird"+killId))+1+"");
                	resultRollback = txRollback.exec();
                	if(resultRollback == null || resultRollback.isEmpty()){
                		jedis.unwatch();
                	}
                }
            } while (resultRollback == null || resultRollback.isEmpty());
            return "您已经抢过该券了，不要太贪心哦！";
        }
        return "";
    }

    private ActivityBillionThird insertActivityBillionThird(ActivityBillionThirdConfig activityBillionThirdConfig, Integer userId, List<String> couponUserCodeList) {
        ActivityBillionThird activityBillionThird=new ActivityBillionThird();
        Users users = this.getUsersByUserId(userId);
        UsersInfo usersInfo = this.getUsersInfoByUserId(userId);
        activityBillionThird.setUserId(userId);
        activityBillionThird.setUserName(users.getUsername());
        activityBillionThird.setTrueName(usersInfo.getTruename());
        activityBillionThird.setMobile(users.getMobile());
        activityBillionThird.setPrizeName(activityBillionThirdConfig.getPrizeName());
        activityBillionThird.setCouponId(couponUserCodeList.get(0));
        activityBillionThird.setCouponCode(activityBillionThirdConfig.getCouponCode());
        activityBillionThird.setCreateTime(GetDate.getNowTime10());
        activityBillionThird.setConfigId(activityBillionThirdConfig.getId());
        
        activityBillionThirdMapper.insertSelective(activityBillionThird);
        return activityBillionThird;
    }
    
    /**
     * 根据用户ID取得用户信息
     *
     * @param userId
     * @return
     */
    public Users getUsersByUserId(Integer userId) {
        if (userId != null) {
            UsersExample example = new UsersExample();
            example.createCriteria().andUserIdEqualTo(userId);
            List<Users> usersList = this.usersMapper.selectByExample(example);
            if (usersList != null && usersList.size() > 0) {
                return usersList.get(0);
            }
        }
        return null;
    }

	/**
	 * 读取活动二时间点记录
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<ActivityBillionSecondTime> getBillionSecondTimeRecords() {
		return this.activityBillionSecondTimeMapper.selectByExample(new ActivityBillionSecondTimeExample());
	}

	/**
	 * 活动二获奖记录
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<ActivityBillionSecond> getBillionSecondRecords() {
		return this.activityBillionSecondMapper.selectByExample(new ActivityBillionSecondExample());
	}

}




