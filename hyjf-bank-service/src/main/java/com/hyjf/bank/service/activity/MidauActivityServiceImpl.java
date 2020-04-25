/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.activity;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.ActivityDateUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.ActivityMidauInfo;
import com.hyjf.mybatis.model.auto.ActivityMidauInfoExample;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 中秋国庆活动
 * @author yinhui
 * @version MidauActivityServiceImpl, v0.1 2018/9/8 14:40
 */
@Service("bankMidauActivityServiceImpl")
public class MidauActivityServiceImpl extends BaseServiceImpl implements MidauActivityService{

    Logger _log = LoggerFactory.getLogger(MidauActivityServiceImpl.class);

    /**
     * 发放用户优惠券
     * @param userId  用户ID
     * @param orderId  订单ID
     * @param productType 来源,1=新手标，2=散标，3=汇计划
     * @param investMoney 出借金额
     */
    @Override
    public boolean sendUserAward(Integer userId,String orderId,int productType,BigDecimal investMoney){
        ActivityMidauInfo activityMidauInfo = new ActivityMidauInfo();

        //查询用户是否有在中秋活动中登记，没登记不下面逻辑
        String activityType = RedisUtils.get(RedisConstants.MIDAU_ACTIVITY_USER_+userId);
        if(StringUtils.isEmpty(activityType)){
            _log.debug("MidauActivityServiceImpl 中秋活动 userId is not active ,userId:"+userId);
            return false;
        }

        //订单号相同就不发放
        ActivityMidauInfoExample example = new ActivityMidauInfoExample();
        ActivityMidauInfoExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andOrderIdEqualTo(orderId);

        List<ActivityMidauInfo> list = activityMidauInfoMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(list)){
            _log.debug("MidauActivityServiceImpl 用户已经获得奖励 ,userId:"+userId+",orderId:"+orderId);
            return false;
        }

        List<ActivityMidauInfo> tenderList = new ArrayList<>();

        //1=新手标，2=散标，3=汇计划
        if(productType == 3){
            //查询加入汇计划的出借信息
            tenderList = activityMidauInfoCustomizeMapper.queryPlanList(orderId,userId);
            if(CollectionUtils.isEmpty(tenderList)){
                _log.error("MidauActivityServiceImpl用户加入汇计划信息为空，orderId:"+orderId+",userId:"+userId);
                return true;
            }
            activityMidauInfo = tenderList.get(0);
            activityMidauInfo.setProductType("汇计划");

        }else{
            //查询加出借散标的出借信息
            tenderList = activityMidauInfoCustomizeMapper.queryTenderList(orderId,userId);
            if(CollectionUtils.isEmpty(tenderList)){
                _log.error("MidauActivityServiceImpl用户出借的散标信息为空，orderId:"+orderId+",userId:"+userId);
                return true;
            }

            activityMidauInfo = tenderList.get(0);
            String productName = productType == 1 ?"新手标":"散标";
            //产品类型(汇计划、散标、新手标)
            activityMidauInfo.setProductType(productName);
        }

        //activityType  1=加息券,2=代金券,3=实物
        int type = Integer.valueOf(activityType);
        Map<String,String> mapAward = new HashMap<>();
        if(type == 3){

            if(!activityMidauInfo.getProductStyle().contains("个月")){
                return false;
            }

            String number = StringUtils.substringBefore(activityMidauInfo.getProductStyle(),"个月");
            if(StringUtils.isEmpty(number)){
                return false;
            }

            mapAward = byInvestMoneyGetAwardSW(investMoney,type,Integer.valueOf(number));
        }else{
            //通过出借金额获得奖品信息
            mapAward = byInvestMoneyGetAward(investMoney,type);
        }


        //奖励名称没有就是不满足条件就不走下去
        if(StringUtils.isEmpty(mapAward.get("rewardName"))){
            _log.debug("MidauActivityServiceImpl userId invest no satisfy ,userId:"+userId+",investMoney:"+investMoney);
            return false;
        }

        _log.debug("MidauActivityServiceImpl userId:"+userId+",investMoney:"+investMoney
                +",rewardName"+mapAward.get("rewardName")
                +",rewardId"+mapAward.get("rewardId")
                +",rewardType"+mapAward.get("rewardType"));


        //奖励批号
        activityMidauInfo.setRewardId(mapAward.get("rewardId"));
        //奖励名称
        activityMidauInfo.setRewardName(mapAward.get("rewardName"));
        //奖励类型
        activityMidauInfo.setRewardType(mapAward.get("rewardType"));
        //发放方式 0:代金券 加息券-系统发放, 1:实物-手动发放
        activityMidauInfo.setDistributionStatus(type == 3 ? 1:0);
        //状态 0:实物-待发放, 1:代金券 加息券-已发放
        activityMidauInfo.setRewardStatus(0);

        if(type != 3){
            // 请求路径
            try {
                // 0:成功，1：失败
                String result = sendCoupon(userId,mapAward.get("rewardId"));
                _log.info("【中秋活动】发放优惠券是否成功：" + result);
                if("0".equals(result)){
                    activityMidauInfo.setRewardStatus(1);
                }
            } catch (Exception e) {
                activityMidauInfo.setRewardStatus(0);
                _log.error("发放优惠券失败......", e);
            }

        }

        _log.info("【中秋活动】保存用户信息orderId：" + orderId);
        activityMidauInfo.setUpdateTime(new Date());
        activityMidauInfo.setCreateTime(new Date());
        activityMidauInfoMapper.insert(activityMidauInfo);
        return  false;
    }

    /**
     * 发放优惠券
     * @param userId
     */
    private String sendCoupon(int userId,String couponCode){
        // 0:成功，1：失败
        String value = "1";
        CommonParamBean couponParamBean = new CommonParamBean();
        // 用户编号
        couponParamBean.setUserId(String.valueOf(userId));
        // 评测送加息券
        couponParamBean.setSendFlg(39);
        couponParamBean.setCouponCode(couponCode);
        // 发放优惠券（1张加息券）
        String result = CommonSoaUtils.sendUserCoupon(couponParamBean);
        JSONObject sendResult = JSONObject.parseObject(result);
        // 发放是否成功状态
        int sendStatus = sendResult.getIntValue("status");
        // 发放优惠券的数量
        int sendCount = sendResult.getIntValue("couponCount");
        if(sendStatus == 0 && sendCount>0){
            value = "0";
        }
        return value;
    }

    /**
     * 通过出借金额获得奖品信息(实物单独判断)
     * @param investMoney  出借金额
     * @param type 奖励类型
      @param investTime 出借时长
     * @return
     */
    public  Map<String,String> byInvestMoneyGetAwardSW(BigDecimal investMoney,int type,int investTime ){
        Map<String,String> mapInfo = new HashMap<>();
        //单笔出借金额大于等于1万小于2万,出借期限满3个月
        if(investMoney.compareTo(ONE) >=0 &&  investMoney.compareTo(TWO) <0 && investTime >= 3) {
            mapInfo = getAwardInfo(type,1);

        }
        //单笔出借金额大于等于2万小于3万
        else if(investMoney.compareTo(TWO) >=0 &&  investMoney.compareTo(THREE) <0 && investTime >= 3){
            mapInfo = getAwardInfo(type,2);
        }
        //单笔出借金额大于等于3万小于5万
        else if(investMoney.compareTo(THREE) >=0 &&  investMoney.compareTo(FIVE) <0 && investTime >= 3){
            mapInfo = getAwardInfo(type,3);
        }
        //单笔出借金额大于等于5万小于10万,出借期限满6个月
        else if(investMoney.compareTo(FIVE) >=0 &&  investMoney.compareTo(TEN) <0 && investTime >= 6){
            mapInfo = getAwardInfo(type,4);
        }
        //单笔出借金额大于等于10万小于15万
        else if(investMoney.compareTo(TEN) >=0 &&  investMoney.compareTo(FIFTEEN) <0 && investTime >= 6){
            mapInfo = getAwardInfo(type,5);
        }
        //单笔出借金额大于等于15万小于20万
        else if(investMoney.compareTo(FIFTEEN) >=0 &&  investMoney.compareTo(TWENTY) <0 && investTime >= 6){
            mapInfo = getAwardInfo(type,6);
        }
        //单笔出借金额大于等于20万
        else if(investMoney.compareTo(TWENTY) >=0 && investTime >= 6){
            mapInfo = getAwardInfo(type,7);
        }
        return mapInfo;
    }

    /**
     * 通过出借金额获得奖品信息
     * @param investMoney  出借金额
     * @param type 奖励类型
     * @return
     */
    public  Map<String,String> byInvestMoneyGetAward(BigDecimal investMoney,int type){
        Map<String,String> mapInfo = new HashMap<>();
        //单笔出借金额大于等于1万小于2万
        if(investMoney.compareTo(ONE) >=0 &&  investMoney.compareTo(TWO) <0) {
            mapInfo = getAwardInfo(type,1);

        }
        //单笔出借金额大于等于2万小于3万
        else if(investMoney.compareTo(TWO) >=0 &&  investMoney.compareTo(THREE) <0){
            mapInfo = getAwardInfo(type,2);
        }
        //单笔出借金额大于等于3万小于5万
        else if(investMoney.compareTo(THREE) >=0 &&  investMoney.compareTo(FIVE) <0){
            mapInfo = getAwardInfo(type,3);
        }
        //单笔出借金额大于等于5万小于10万
        else if(investMoney.compareTo(FIVE) >=0 &&  investMoney.compareTo(TEN) <0){
            mapInfo = getAwardInfo(type,4);
        }
        //单笔出借金额大于等于10万小于15万
        else if(investMoney.compareTo(TEN) >=0 &&  investMoney.compareTo(FIFTEEN) <0){
            mapInfo = getAwardInfo(type,5);
        }
        //单笔出借金额大于等于15万小于20万
        else if(investMoney.compareTo(FIFTEEN) >=0 &&  investMoney.compareTo(TWENTY) <0){
            mapInfo = getAwardInfo(type,6);
        }
        //单笔出借金额大于等于20万
        else if(investMoney.compareTo(TWENTY) >=0 ){
            mapInfo = getAwardInfo(type,7);
        }
        return mapInfo;
    }

    /**
     * 通过奖励类型获得详细的奖励信息
     * @param type  1=加息券,2=代金券,3=实物
     * @param level 区域
     * @return
     */
    public Map<String,String>  getAwardInfo(int type,int level){
        Map<String,String> mapInfo = new HashMap<>();
        //1=加息券,2=代金券,3=实物
        switch (type){
            case 1:
                mapInfo = getAwardJX(level);
                break;
            case 2:
                mapInfo = getAwardDJ(level);
                break;
            case 3:
                mapInfo = getAwardSW(level);
                break;
            default:
                break;

        }
        return mapInfo;
    }

    /**
     * 获得加息券的奖品
     * @param level 区域
     * @return
     */
    public Map<String,String> getAwardJX(int level){
        //rewardName 奖励名称 ;rewardId 奖励批号 加息券/代金券的优惠券编号 ; rewardType 奖励类型 实物、加息券、代金券
        Map<String,String> map = new HashMap<>();
        map.put("rewardType","加息券");
        switch (level){
            case 1:
                map.put("rewardName","0.9%加息券");
                map.put("rewardId",ActivityDateUtil.MIDAU_ACTIVITY_JX_POINT_NINE);
                break;
            case 2:
                map.put("rewardName","1.0%加息券");
                map.put("rewardId",ActivityDateUtil.MIDAU_ACTIVITY_JX_POINT_ZERO);
                break;
            case 3:
                map.put("rewardName","1.1%加息券");
                map.put("rewardId",ActivityDateUtil.MIDAU_ACTIVITY_JX_POINT_ONE);
                break;
            case 4:
                map.put("rewardName","1.2%加息券");
                map.put("rewardId",ActivityDateUtil.MIDAU_ACTIVITY_JX_POINT_TWO);
                break;
            case 5:
                map.put("rewardName","1.3%加息券");
                map.put("rewardId",ActivityDateUtil.MIDAU_ACTIVITY_JX_POINT_THREE);
                break;
            case 6:
                map.put("rewardName","1.4%加息券");
                map.put("rewardId",ActivityDateUtil.MIDAU_ACTIVITY_JX_POINT_FOUR);
                break;
            case 7:
                map.put("rewardName","1.5%加息券");
                map.put("rewardId",ActivityDateUtil.MIDAU_ACTIVITY_JX_POINT_FIVE);
                break;
            default:
                break;

        }
        return map;
    }

    /**
     * 获得实物的奖品
     * @param level
     * @return
     */
    public Map<String,String> getAwardSW(int level){
        //rewardName 奖励名称 ;rewardId 奖励批号 加息券/代金券的优惠券编号 ; rewardType 奖励类型 实物、加息券、代金券
        Map<String,String> map = new HashMap<>();
        map.put("rewardType","实物");
        switch (level){
            case 1:
                map.put("rewardName","旅行四件套(40元)");
                map.put("rewardId","");
                break;
            case 2:
                map.put("rewardName","小米充电宝(80元)");
                map.put("rewardId","");
                break;
            case 3:
                map.put("rewardName","大闸蟹礼券(118元)");
                map.put("rewardId","");
                break;
            case 4:
                map.put("rewardName","360行车记录仪(400元)");
                map.put("rewardId","");
                break;
            case 5:
                map.put("rewardName","美菱除湿机(800元)");
                map.put("rewardId","");
                break;
            case 6:
                map.put("rewardName","中石化加油卡(1200元)");
                map.put("rewardId","");
                break;
            case 7:
                map.put("rewardName","驴妈妈礼品卡(1600元)");
                map.put("rewardId","");
                break;
            default:
                break;

        }
        return map;
    }

    /**
     * 获得代金券的奖品
     * @param level
     * @return
     */
    public Map<String,String> getAwardDJ(int level){
        //rewardName 奖励名称 ;rewardId 奖励批号 加息券/代金券的优惠券编号 ; rewardType 奖励类型 实物、加息券、代金券
        Map<String,String> map = new HashMap<>();
        map.put("rewardType","代金券");
        switch (level){
            case 1:
                map.put("rewardName","40元代金券");
                map.put("rewardId", ActivityDateUtil.MIDAU_ACTIVITY_DJ_FOUR);
                break;
            case 2:
                map.put("rewardName","80元代金券");
                map.put("rewardId",ActivityDateUtil.MIDAU_ACTIVITY_DJ_EIGHT);
                break;
            case 3:
                map.put("rewardName","120元代金券");
                map.put("rewardId",ActivityDateUtil.MIDAU_ACTIVITY_DJ_TWELVE);
                break;
            case 4:
                map.put("rewardName","400元代金券");
                map.put("rewardId",ActivityDateUtil.MIDAU_ACTIVITY_DJ_FORTY);
                break;
            case 5:
                map.put("rewardName","800元代金券");
                map.put("rewardId",ActivityDateUtil.MIDAU_ACTIVITY_DJ_EIGHTY);
                break;
            case 6:
                map.put("rewardName","1200元代金券");
                map.put("rewardId",ActivityDateUtil.MIDAU_ACTIVITY_DJ_TWELVEHUNDRED);
                break;
            case 7:
                map.put("rewardName","1600元代金券");
                map.put("rewardId",ActivityDateUtil.MIDAU_ACTIVITY_DJ_SIXTEENHUNDRED);
                break;
            default:
                break;

        }
        return map;
    }

    /**
     * 查询活动是否开始
     * @param activityId
     * @return
     */
    @Override
    public String checkActivityIfAvailable(String activityId) {
        if (activityId == null) {
            return "103";
        }
        ActivityList activityList = activityListMapper.selectByPrimaryKey(new Integer(activityId));
        if (activityList == null) {
            return "104";
        }
        if (activityList.getTimeStart() > GetDate.getNowTime10()) {
            return "101";
        }
        if (activityList.getTimeEnd() < GetDate.getNowTime10()) {
            return "102";
        }
        return "000";
    }

    //1万
    private static final BigDecimal ONE = new BigDecimal(10000);
    //2万
    private static final BigDecimal TWO = new BigDecimal(20000);
    //3万
    private static final BigDecimal THREE = new BigDecimal(30000);
    //5万
    private static final BigDecimal FIVE = new BigDecimal(50000);
    //10万
    private static final BigDecimal TEN = new BigDecimal(100000);
    //15万
    private static final BigDecimal FIFTEEN = new BigDecimal(150000);
    //20万
    private static final BigDecimal TWENTY = new BigDecimal(200000);

}
