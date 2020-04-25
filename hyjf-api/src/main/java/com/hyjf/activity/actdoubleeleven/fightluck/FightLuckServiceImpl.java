package com.hyjf.activity.actdoubleeleven.fightluck;

import java.util.List;

import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.ActRewardList;
import com.hyjf.mybatis.model.auto.ActRewardListExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;

@Service("fightLuckService")
public class FightLuckServiceImpl extends BaseServiceImpl implements FightLuckService {
    public static JedisPool pool = RedisUtils.getConnection();
    @Override
    public synchronized String grabCoupons(Integer userId) {
        
        Integer fightLuckTime = Integer.parseInt(PropUtils.getSystem("hyjf.act.nov.2017.fight.luck.time"));
        Integer fightLuckEndTime = Integer.parseInt(PropUtils.getSystem("hyjf.act.nov.2017.fight.luck.endtime"));
        String fightLuckCoupon = PropUtils.getSystem("hyjf.act.nov.2017.fight.luck.coupon");
        
        if("".equals(userId)||userId==null){
            return "用户未登录,请先登录";
        }
        if(GetDate.getNowTime10()<fightLuckTime){
            return "活动尚未开始";
        }
        if(GetDate.getNowTime10()>=(fightLuckEndTime)){
            return "活动已经结束";
        }
        Users user=this.getUsers(userId);
        if(user==null){
            return "用户不存在";
        }
        UsersInfo usersInfo=this.getUsersInfoByUserId(user.getUserId());
        if(usersInfo!=null&&(usersInfo.getAttribute()==2||usersInfo.getAttribute()==3)){
            return "公司员工不可参与抢券活动";
        }
        Jedis jedis=pool.getResource();
        boolean reslut = RedisUtils.tranactionSet("fightLuck:"+userId,30*60);
        if(!reslut){
            return "您已经抢过该券了，不要太贪心哦！";
        }
        List<Object> result=null;
        int count=1;
        do {
            if("OK".equals(jedis.watch("fightLuckCouponCount"))){
                Transaction tx = jedis.multi();
                String fightLuckCouponCount = RedisUtils.get("fightLuckCouponCount");
                if(fightLuckCouponCount==null||new Integer(fightLuckCouponCount)<=0){
                    return "啊哦～晚了一步";
                }
                tx.set("fightLuckCouponCount", new Integer(fightLuckCouponCount)-1+"");
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
        couponParamBean.setPrizeGroupCode(fightLuckCoupon);
        String jsonStr=CommonSoaUtils.sendUserCoupon(couponParamBean);
        JSONObject sendResult = JSONObject.parseObject(jsonStr);
        // 发放是否成功状态
        int sendStatus = sendResult.getIntValue("status");
        // 发放优惠券的数量
        int sendCount = sendResult.getIntValue("couponCount");
        
        if(sendStatus == 0 && sendCount > 0){
            ActRewardList actRewardList=new ActRewardList();
            actRewardList.setUserId(user.getUserId());
            actRewardList.setUserName(user.getUsername());
            actRewardList.setTruename(usersInfo.getTruename());
            actRewardList.setMobile(user.getMobile());
            actRewardList.setActType(4);
            actRewardList.setAct1RewardType(4);
            actRewardList.setCouponCode(fightLuckCoupon);
            actRewardList.setSendFlg(1);
            actRewardList.setRemark("双十一拼手气");
            actRewardList.setCreateTime(GetDate.getNowTime10());
            actRewardListMapper.insertSelective(actRewardList);
            
        }else{
            return "啊哦～晚了一步";
        }
        return "";
    }

    @Override
    public List<ActRewardList> getFightLuckWinnersList() {
        ActRewardListExample example=new ActRewardListExample();
        example.createCriteria().andAct1RewardTypeEqualTo(4).andActTypeEqualTo(4);
        return actRewardListMapper.selectByExample(example);
    }

    @Override
    public List<ActRewardList> getFightLuckWinnersListByUserId(Integer userId) {
        ActRewardListExample example=new ActRewardListExample();
        example.createCriteria().andAct1RewardTypeEqualTo(4).andActTypeEqualTo(4).andUserIdEqualTo(userId);
        return actRewardListMapper.selectByExample(example);
    }

   
}
