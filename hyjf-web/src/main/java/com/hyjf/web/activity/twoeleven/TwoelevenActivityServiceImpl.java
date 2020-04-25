/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.web.activity.twoeleven;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.enums.utils.TwoElevenAwardsNameEnum;
import com.hyjf.common.util.ActivityDateUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.TwoelevenReward;
import com.hyjf.mybatis.model.customize.activity.ActivityAwardsVO;
import com.hyjf.mybatis.model.customize.activity.ActivityOtherDataVO;
import com.hyjf.mybatis.model.customize.activity.ActivityTimeGapVO;
import com.hyjf.mybatis.model.customize.activity.TwoElevenActivityVO;
import com.hyjf.mybatis.model.customize.admin.TwoelevenCustomize;
import com.hyjf.web.BaseServiceImpl;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author yinhui
 * @version TwoelevenActivityServiceImpl, v0.1 2018/10/11 10:01
 */
@Service("webTwoelevenActivityServiceImpl")
public class TwoelevenActivityServiceImpl  extends BaseServiceImpl implements TwoelevenActivityService {

    private Logger logger = LoggerFactory.getLogger(TwoelevenActivityServiceImpl.class);

    //70万
    private static BigDecimal SEVENBIG = new BigDecimal(700000);
    //90万
    private static BigDecimal EIGHTBIG = new BigDecimal(900000);
    //100万
    private static BigDecimal TENBIG = new BigDecimal(1000000);

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    /**
     * 查询活动是否开始
     * @param activityId
     * @return
     */
    @Override
    public ActivityList checkActivityIfAvailable(String activityId) {
        if (activityId == null) {
            return new ActivityList();
        }
        ActivityList activityList = activityListMapper.selectByPrimaryKey(new Integer(activityId));
        if (activityList == null) {
            return new ActivityList();
        }
        return activityList;
    }

    /**
     * 获得活动状态
     * @return 活动状态 0未开始 1进行中 2已结束
     */
    public String getActivityStatus(Long timeStart, Long timeEnd){

        if (timeStart > GetDate.getMillis()) {
            return "0";
        }
        if (timeEnd < GetDate.getMillis()) {
            return "2";
        }
        return "1";
    }

    /**
     * 加息先行 活动
     * @return
     */
    @Override
    public TwoElevenActivityVO getActivityJX(ActivityList activityList){
        List<ActivityTimeGapVO> gapVOList = new ArrayList<>();

        List<ParamName> listParamName = getParamNameList("TWOELEVEN");
        //时段一 11:11 ~ 13:11
        List<ActivityAwardsVO> listActivityAwardsVO = getListActivityAwardsVO
                (ActivityDateUtil.TWOELEVEN_ACTIVITY_JX_ONE,ActivityDateUtil.TWOELEVEN_ACTIVITY_JX_TWO,ActivityDateUtil.TWOELEVEN_ACTIVITY_JX_THREE);

        ActivityTimeGapVO timeGapVO = new ActivityTimeGapVO();
        timeGapVO.setStartTime(Long.valueOf(listParamName.get(0).getOther1()));
        timeGapVO.setEndTime(Long.valueOf(listParamName.get(0).getOther2()));
        timeGapVO.setStatus(getTimeGapStatus(timeGapVO.getStartTime(),timeGapVO.getEndTime(),listActivityAwardsVO));
        timeGapVO.setListAwards(listActivityAwardsVO);
        gapVOList.add(timeGapVO);

        //时段二 13:11 ~ 15:11
        List<ActivityAwardsVO> listActivityAwardsTwoVO = getListActivityAwardsVO
                (ActivityDateUtil.TWOELEVEN_ACTIVITY_JX_FOUR,ActivityDateUtil.TWOELEVEN_ACTIVITY_JX_FIVE,ActivityDateUtil.TWOELEVEN_ACTIVITY_JX_SIX);

        ActivityTimeGapVO timeGapTwoVO = new ActivityTimeGapVO();
        timeGapTwoVO.setStartTime(Long.valueOf(listParamName.get(1).getOther1()));
        timeGapTwoVO.setEndTime(Long.valueOf(listParamName.get(1).getOther2()));
        timeGapTwoVO.setStatus(getTimeGapStatus(timeGapTwoVO.getStartTime(),timeGapTwoVO.getEndTime(),listActivityAwardsTwoVO));
        timeGapTwoVO.setListAwards(listActivityAwardsTwoVO);
        gapVOList.add(timeGapTwoVO);

        //时段三 17:11 ~ 19:11
        List<ActivityAwardsVO> listActivityAwardsThreeVO = getListActivityAwardsVO
                (ActivityDateUtil.TWOELEVEN_ACTIVITY_JX_SEVEN,ActivityDateUtil.TWOELEVEN_ACTIVITY_JX_EIGHT,ActivityDateUtil.TWOELEVEN_ACTIVITY_JX_NINE);

        ActivityTimeGapVO timeGapThreeVO = new ActivityTimeGapVO();
        timeGapThreeVO.setStartTime(Long.valueOf(listParamName.get(2).getOther1()));
        timeGapThreeVO.setEndTime(Long.valueOf(listParamName.get(2).getOther2()));
        timeGapThreeVO.setStatus(getTimeGapStatus(timeGapThreeVO.getStartTime(),timeGapThreeVO.getEndTime(),listActivityAwardsThreeVO));
        timeGapThreeVO.setListAwards(listActivityAwardsThreeVO);
        gapVOList.add(timeGapThreeVO);

        TwoElevenActivityVO elevenActivityVO = new TwoElevenActivityVO();

        //活动状态 0未开始 1进行中 2已结束
        elevenActivityVO.setStatus(getActivityStatus(timeGapVO.getStartTime(),timeGapThreeVO.getEndTime()));
        //活动名称
        elevenActivityVO.setName("加息先行");
        // 活动开始日期 毫秒级时间戳
        elevenActivityVO.setStartDate(Long.valueOf(new StringBuffer(String.valueOf(activityList.getTimeStart())).append("000").toString()));
        // 时段数据
        elevenActivityVO.setData(gapVOList);

        return elevenActivityVO;
    }

    /**
     * 红包到达战场 活动
     * @return
     */
    @Override
    public TwoElevenActivityVO getActivityDJ(ActivityList activityList){
        List<ActivityTimeGapVO> gapVOList = new ArrayList<>();

        List<ParamName> listParamName = getParamNameList("TWOELEVEN");

        //时段一 11:11 ~ 13:11
        List<ActivityAwardsVO> listActivityAwardsVO = getListActivityAwardsVO
                (ActivityDateUtil.TWOELEVEN_ACTIVITY_DJ_ONE,ActivityDateUtil.TWOELEVEN_ACTIVITY_DJ_TWO,ActivityDateUtil.TWOELEVEN_ACTIVITY_DJ_THREE);

        ActivityTimeGapVO timeGapVO = new ActivityTimeGapVO();
        timeGapVO.setStartTime(Long.valueOf(listParamName.get(3).getOther1()));
        timeGapVO.setEndTime(Long.valueOf(listParamName.get(3).getOther2()));
        timeGapVO.setStatus(getTimeGapStatus(timeGapVO.getStartTime(),timeGapVO.getEndTime(),listActivityAwardsVO));
        timeGapVO.setListAwards(listActivityAwardsVO);
        gapVOList.add(timeGapVO);

        //时段二 13:11 ~ 15:11
        List<ActivityAwardsVO> listActivityAwardsTwoVO = getListActivityAwardsVO
                (ActivityDateUtil.TWOELEVEN_ACTIVITY_DJ_FOUR,ActivityDateUtil.TWOELEVEN_ACTIVITY_DJ_FIVE,ActivityDateUtil.TWOELEVEN_ACTIVITY_DJ_SIX);

        ActivityTimeGapVO timeGapTwoVO = new ActivityTimeGapVO();
        timeGapTwoVO.setStartTime(Long.valueOf(listParamName.get(4).getOther1()));
        timeGapTwoVO.setEndTime(Long.valueOf(listParamName.get(4).getOther2()));
        timeGapTwoVO.setStatus(getTimeGapStatus(timeGapTwoVO.getStartTime(),timeGapTwoVO.getEndTime(),listActivityAwardsTwoVO));
        timeGapTwoVO.setListAwards(listActivityAwardsTwoVO);
        gapVOList.add(timeGapTwoVO);

        //时段三 17:11 ~ 19:11
        List<ActivityAwardsVO> listActivityAwardsThreeVO = getListActivityAwardsVO
                (ActivityDateUtil.TWOELEVEN_ACTIVITY_DJ_SEVEN,ActivityDateUtil.TWOELEVEN_ACTIVITY_DJ_EIGHT,ActivityDateUtil.TWOELEVEN_ACTIVITY_DJ_NINE);

        ActivityTimeGapVO timeGapThreeVO = new ActivityTimeGapVO();
        timeGapThreeVO.setStartTime(Long.valueOf(listParamName.get(5).getOther1()));
        timeGapThreeVO.setEndTime(Long.valueOf(listParamName.get(5).getOther2()));
        timeGapThreeVO.setStatus(getTimeGapStatus(timeGapThreeVO.getStartTime(),timeGapThreeVO.getEndTime(),listActivityAwardsThreeVO));
        timeGapThreeVO.setListAwards(listActivityAwardsThreeVO);
        gapVOList.add(timeGapThreeVO);

        TwoElevenActivityVO elevenActivityVO = new TwoElevenActivityVO();

        //活动状态 0未开始 1进行中 2已结束
        elevenActivityVO.setStatus(getActivityStatus(timeGapVO.getStartTime(),timeGapThreeVO.getEndTime()));
        //活动名称
        elevenActivityVO.setName("红包到达战场");
        // 活动开始日期 毫秒级时间戳
        elevenActivityVO.setStartDate(Long.valueOf(new StringBuffer(String.valueOf(
                GetDate.countDate(activityList.getTimeStart(),5,1) )).append("000").toString()));
        // 时段数据
        elevenActivityVO.setData(gapVOList);

        return elevenActivityVO;
    }

    /**
     * iPhone疯狂抢 活动
     * @return
     */
    @Override
    public Map<String,Object> getActivitySW(Integer userId,ActivityList activityList ){
        Map<String,Object> map = new HashedMap();
        BigDecimal total = BigDecimal.ZERO;

        map.put("name","iPhone疯狂抢");
        //已经中奖奖品 0=没中奖，1=70-90万，2=90-100万，3=100以上
        String award = "0";
        if(userId != null){
            total = twoelevenCustomizeMapper.getYearAmount(userId);
            if (activityList.getTimeEnd() < GetDate.getNowTime10()) {
                if(total.compareTo(SEVENBIG) < 0){
                    award = "0";
                }else if(total.compareTo(SEVENBIG) >= 0 && total.compareTo(EIGHTBIG) < 0){
                    award = "1";
                }else if(total.compareTo(EIGHTBIG) >= 0 && total.compareTo(TENBIG) < 0){
                    award = "2";
                }else if(total.compareTo(TENBIG) >= 0){
                    award = "3";
                }
            }
        }
        map.put("award",award);
        map.put("total",total.toString());
        map.put("startDate",Long.valueOf(new StringBuffer(String.valueOf(activityList.getTimeStart())).append("000").toString()));
        map.put("endDate",Long.valueOf(new StringBuffer(String.valueOf(activityList.getTimeEnd())).append("000").toString()));
        return map;
    }

    /**
     * 辅助数据
     * @return
     */
    @Override
    public ActivityOtherDataVO getActivityOtherDataVO(Integer userId,ActivityList activityList ){
        ActivityOtherDataVO otherDataVO = new ActivityOtherDataVO();

        int restTimes = 2;
        if(userId != null){
            //一天两次秒杀机会
            int awardTime = twoelevenCustomizeMapper.getUserAwardTime(userId);
            restTimes = 2-awardTime;
            if(restTimes < 0 ){
                restTimes = 0;
            }
        }
        otherDataVO.setRestTimes(restTimes);
        otherDataVO.setNow(GetDate.getMillis());
        String token = RedisUtils.get(RedisConstants.TWO_ELEVEN_ACTIVITY_TOKEN);
        if(StringUtils.isEmpty(token)){
            token = "";
        }
        otherDataVO.setActivityToken(token);
        return otherDataVO;
    }

    /**
     * 计算活动状态
     * @param startTime
     * @param endTime
     * @param list
     * @return
     */
    public String getTimeGapStatus(Long startTime,Long endTime,List<ActivityAwardsVO> list){
        // 时段秒杀状态 0未开始 1进行中 2已结束 3倒计时 4已抢完且已结束

        //0未开始  3倒计时
        if(startTime > GetDate.getMillis()){
            Long value = startTime-GetDate.getMillis();
            //倒计时 = 秒杀处于距离1小时候开始的时段状态
            if(value/1000 <= 3600){
                return "3";
            }else{
                return "0";
            }

        }

        int sum = 0;
        for(ActivityAwardsVO vo : list){
            sum += vo.getRest();
        }

        //1进行中
        if(startTime < GetDate.getMillis() && endTime > GetDate.getMillis()){
            if(sum == 0){
                return "4";
            }
            return "1";
        }

        //2已结束  4已抢完且已结束
        if(endTime < GetDate.getMillis()){

            if(sum == 0){
                return "4";
            }else{
                return "2";
            }

        }
        return "0";
    }

    /**
     * get每个时间段的奖品
     * @param awardId
     * @return
     */
    public List<ActivityAwardsVO> getListActivityAwardsVO(String... awardId){
        List<ActivityAwardsVO> listActivityAwardsVO = new ArrayList<>();
        Map<String,Integer> coupon = null;
        for(String id : awardId){
            coupon = getCouponData(id);
            //奖品三 1.0%加息券
            ActivityAwardsVO awardsVOThree = new ActivityAwardsVO();
            awardsVOThree.setId(id);
            awardsVOThree.setName(TwoElevenAwardsNameEnum.getValue(id));
            if(coupon != null){
                awardsVOThree.setProgress(coupon.get("progress"));
                awardsVOThree.setRest(coupon.get("rest"));
            }else{
                awardsVOThree.setProgress(100);
                awardsVOThree.setRest(0);
            }

            listActivityAwardsVO.add(awardsVOThree);
        }
        return listActivityAwardsVO;
    }

    /**
     * 获得优惠券的进度和剩余数量
     * @param awardId
     * @return
     */
    @Override
    public Map<String,Integer> getCouponData(String awardId){
        if(StringUtils.isEmpty(awardId)){
            return null;
        }

        Map<String,Integer> mapCouponData = new HashMap<>(16);
        String couponCount = RedisUtils.get(RedisConstants.TWO_ELEVEN_COUPON_NUMBER_+awardId);
        if(StringUtils.isEmpty(couponCount)){
            //数据库获得优惠券编码的数量
            couponCount = String.valueOf(twoelevenCustomizeMapper.getCouponQuantity(awardId));
            RedisUtils.set(RedisConstants.TWO_ELEVEN_COUPON_NUMBER_+awardId,couponCount);
        }
        //双十一 每个优惠券的数量
        Integer quantity = Integer.valueOf(couponCount);

        //双十一 每个优惠券的剩余数量
        String sentNumber = RedisUtils.get(RedisConstants.TWO_ELEVEN_COUPON_SENT_NUMBER_+awardId);
        Integer sent = 0;
        if(StringUtils.isNotEmpty(sentNumber)){
            sent = Integer.valueOf(sentNumber);
        }
        Integer rest = quantity-sent;
        int progress = sent*100/quantity;

        //奖品秒杀进度
        mapCouponData.put("progress",progress);
        //剩余奖品数量
        mapCouponData.put("rest",rest < 0 ? 0 : rest);

        return mapCouponData;
    }

    /**
     * 奖品秒杀
     * @param couponId 优惠券编号
     * @param activity 活动类型 1=第一天活动,2=第二天活动
     * @param userId
     * @return
     */
    @Override
    public JSONObject seckillAward(String couponId,String activity, Integer userId){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "801");
        jsonObject.put("statusDesc", "加油再接再厉");

        //判断活动是否开始
        boolean flag = false;
        List<ParamName> listParamName = getParamNameList("TWOELEVEN");
        for(ParamName vo : listParamName){
            Long startTime = Long.valueOf(vo.getOther1());
            Long endTime = Long.valueOf(vo.getOther2());

            //true进行中
            if(startTime < GetDate.getMillis() && endTime > GetDate.getMillis()){
                flag = true;
            }
        }

        if(!flag){
            jsonObject.put("status", "805");
            jsonObject.put("statusDesc", "活动未开始");
            return jsonObject;
        }

        logger.info("【双十一秒杀活动】PC用户秒杀 userId="+userId+",couponId="+couponId);

        String token = RedisUtils.getToken();
        String rediskey = RedisConstants.TWO_ELEVEN_COUPON_COMPETE_+couponId;
        try{

            //得到锁
            if (RedisUtils.lockWithTimeout(rediskey,token,6000L)) {

                //用户秒杀剩余次数 ，一天两次
                int awardTime = twoelevenCustomizeMapper.getUserAwardTime(userId);
                if(awardTime>=2){
                    logger.info("【双十一秒杀活动】PC用户剩余秒杀次数不足 userId="+userId+",awardTime="+awardTime);
                    //1=加息先行 ,2=红包到达战场
                    if("1".equals(activity)){
                        jsonObject.put("status", "802");
                        jsonObject.put("statusDesc", "活动机会已用完");
                    }else{
                        jsonObject.put("status", "803");
                        jsonObject.put("statusDesc", "活动机会已用完");
                    }

                    return jsonObject;
                }

                //奖品数量
                Map<String,Integer> mapCoupon = getCouponData(couponId);

                if(mapCoupon == null || mapCoupon.get("rest") == 0){
                    return jsonObject;
                }
                logger.info("【双十一秒杀活动】PC秒杀剩余张数(记得减一) rest="+mapCoupon.get("rest")+",couponId="+couponId +",userId="+userId);

                //发放优惠券
                int value = saveTwoelevenReward(userId, couponId);
                if (value > 0) {
                    Map<String,String> award =  getRewardNameAndType(couponId);
                    jsonObject.put("status", "000");
                    jsonObject.put("statusDesc", "秒杀成功");
                    jsonObject.put("awardVal", award.get("rewardVal"));
                    jsonObject.put("awardType", award.get("rewardType"));

                    //优惠券累计值
                    RedisUtils.incr(RedisConstants.TWO_ELEVEN_COUPON_SENT_NUMBER_ + couponId);
                }
            }
        }catch (Exception e){
            logger.error("【双十一秒杀活动】出现异常",e);
        }finally {
            RedisUtils.release(rediskey,token);
        }

        return jsonObject;

    }

    private Map<String,String> getRewardNameAndType(String couponId){
        Map<String,String> mapReward = new HashMap<>();
        String rewardValue = TwoElevenAwardsNameEnum.getValue(couponId);
        double value = Double.valueOf(rewardValue);
        String rewardName = null;
        String rewardType = null;
        String rewardVal = null;
        StringBuilder stringBuffer = new StringBuilder();
        if(value < 10){
            rewardVal = stringBuffer.append(rewardValue).append("%").toString();
            rewardType = "加息券";
            rewardName = stringBuffer.append(rewardType).toString();
        }else{
            rewardVal = stringBuffer.append(rewardValue).append("元").toString();
            rewardType = "代金券";
            rewardName = stringBuffer.append(rewardType).toString();
        }
        mapReward.put("rewardName",rewardName);
        mapReward.put("rewardType",rewardType);
        mapReward.put("rewardVal",rewardVal);
        return mapReward;
    }

    private int saveTwoelevenReward(Integer userId,String couponId){
        Map<String,String> mapReward = getRewardNameAndType(couponId);
        String rewardName = mapReward.get("rewardName");
        String rewardType = mapReward.get("rewardType");

        TwoelevenCustomize twoelevenCustomize = twoelevenCustomizeMapper.getUserInfos(userId);

        TwoelevenReward twoelevenReward = new TwoelevenReward();
        twoelevenReward.setRewardName(rewardName);
        twoelevenReward.setRewardId(couponId);
        twoelevenReward.setRewardType(rewardType);
        twoelevenReward.setUserId(userId);
        twoelevenReward.setUsername(twoelevenCustomize.getUsername());
        twoelevenReward.setTruename(twoelevenCustomize.getTruename());
        twoelevenReward.setDistributionStatus(0);
        twoelevenReward.setStatus(1);
        twoelevenReward.setSecondsTime(new Date());
        twoelevenReward.setSendTime(new Date());
        int rewardId = twoelevenRewardMapper.insert(twoelevenReward);

        //保存成功，发放优惠券
        if(rewardId > 0){
            sendMQActivity(userId,couponId,twoelevenReward.getId());
        }
        return rewardId;
    }

    /**
     * 发放活动奖励
     * @param userId
     * @param couponCode 优惠券编号
     * @param rewardId 发放奖励表ID
     */
    private void sendMQActivity(Integer userId,String couponCode,Integer rewardId){
        // 加入到消息队列
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("couponCode", couponCode);
        params.put("rewardId", rewardId);
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.SENDCOUPON_ACTIVITY, JSONObject.toJSONString(params));
    }

}
