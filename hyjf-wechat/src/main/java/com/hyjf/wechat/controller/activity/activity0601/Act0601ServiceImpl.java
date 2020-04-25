/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.wechat.controller.activity.activity0601;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.security.utils.MD5Utils;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.mapper.customize.coupon.CouponConfigCustomizeMapper;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.ActivityListExample;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.auto.CouponConfigExample;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.wechat.base.BaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 六一活动
 *
 * @author fuqiang
 * @version Act0601ServiceImpl, v0.1 2018/5/25 11:20
 */
@Service
public class Act0601ServiceImpl extends BaseServiceImpl implements Act0601Service {

    private Logger logger = LoggerFactory.getLogger(Act0601ServiceImpl.class);

    @Autowired
    private CouponConfigCustomizeMapper couponConfigCustomizeMapper;


    @Override
    public JSONObject selectCommand(String command, Integer userId, String couponId, String activityId) {
        JSONObject jsonObject = new JSONObject();
        String formatDate = GetDate.formatDate();
        String commondValue = generatorExchageCode(formatDate);
        if (command != null && !command.equals(commondValue)) {
            jsonObject.put("status", "11");
            jsonObject.put("statusDesc", "兑奖口令错误!");
        }

        //  判断活动是否过期
        ActivityListExample activityListExample  = new ActivityListExample();
        activityListExample.createCriteria().andIdEqualTo(Integer.valueOf(activityId));
        List<ActivityList> list = activityListMapper.selectByExample(activityListExample);
        if (CollectionUtils.isEmpty(list)) {
            jsonObject.put("status", "11");
            jsonObject.put("statusDesc", "活动已过期!");
            return jsonObject;
        } else {
            ActivityList activity = list.get(0);
            Integer timeEnd = activity.getTimeEnd();
            Date date = new Date();
            if (date.getTime()/1000 > timeEnd) {
                jsonObject.put("status", "11");
                jsonObject.put("statusDesc", "活动已过期!");
                return jsonObject;
            }
        }
        //  判断是否到兑奖时间
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if (hour < 10) {
            jsonObject.put("status", "11");
            jsonObject.put("statusDesc", "未到兑奖时间, 请耐心等待。");
            return jsonObject;
        }

        //  判断奖品是否已经被抢完
        CouponConfigExample example = new CouponConfigExample();
        example.createCriteria().andCouponCodeEqualTo(couponId);
        List<CouponConfig> couponConfigList = couponConfigMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(couponConfigList)) {
            CouponConfig couponConfig = couponConfigList.get(0);
            int num = couponConfigCustomizeMapper.selectCouponPublishedCount(couponId); // 已发张数
            int quantity = couponConfig.getCouponQuantity();// 发行张数
            if (num >= quantity) {
                jsonObject.put("status", "11");
                jsonObject.put("statusDesc", "奖品都被抢完了, 请明天再来!");
                return jsonObject;
            }
        }

        return jsonObject;
    }

    @Override
    public JSONObject sendCoupon(Integer userId, String couponCode, String activityId) {
        JSONObject jsonObject = new JSONObject();
        //  redis锁  防止重复兑奖
        boolean reslut = RedisUtils.tranactionSet(Act0601Define.COMMOND_USER_KEY + userId, getTomorrowZeroSeconds());
        if (!reslut) {
            jsonObject.put("status", "11");
            jsonObject.put("statusDesc", "兑奖口令已失效!");
            return jsonObject;
        }

        CommonParamBean paramBean = new CommonParamBean();
        paramBean.setUserId(String.valueOf(userId));
        paramBean.setCouponSource(2);// 活动
        paramBean.setCouponCode(couponCode);
        paramBean.setSendCount(1);// 数量
        paramBean.setActivityId(Integer.valueOf(activityId));
        paramBean.setRemark("六一活动发放优惠券");
        paramBean.setSendFlg(0);

        logger.info("---------------开始调用发券接口（六一活动发送优惠券），用户id：" + userId + " couponCode: " + couponCode);
        String jsonStr = "";
        try {
    	   // 调用发放优惠券接口
           jsonStr = CommonSoaUtils.sendUserCoupon(paramBean);
		} catch (Exception e) {
			logger.error("---------------调用发券接口（六一活动发送优惠券），用户id：" + userId + " couponCode: " + couponCode+"发送失败！",e);
			//优惠券发送失败，删除Redis记录
			RedisUtils.del(Act0601Define.COMMOND_USER_KEY + userId);
			jsonObject.put("status", "11");
	        jsonObject.put("statusDesc", "优惠券发送失败!");
	        jsonStr = jsonObject.toString();
		}
        logger.info("--------------调用发券接口结束（六一活动发送优惠券），用户id：" + userId + " couponCode: " + couponCode);
        return JSONObject.parseObject(jsonStr);
    }

    /**
     * 生成兑奖码
     * @param initCode
     * @return
     */
    public String generatorExchageCode(String initCode){
        String cd5Code = MD5Utils.MD5(initCode);
        String exchageCode = cd5Code.substring(0, 6);
        return exchageCode;
    }

    /**
     * 获取0点前秒数,每天可兑换一次
     * @return
     */
    public int getTomorrowZeroSeconds() {
        long current = System.currentTimeMillis();// 当前时间毫秒数
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long tomorrowzero = calendar.getTimeInMillis();
        int tomorrowzeroSeconds = (int) ((tomorrowzero- current) / 1000);
        return tomorrowzeroSeconds;
    }

}
