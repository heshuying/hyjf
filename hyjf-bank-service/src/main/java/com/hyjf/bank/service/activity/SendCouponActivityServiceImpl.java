/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.activity;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.TwoelevenReward;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 发放活动优惠券
 * @author yinhui
 * @version SendCouponActivityServiceImpl, v0.1 2018/10/16 15:55
 */
@Service
public class SendCouponActivityServiceImpl  extends BaseServiceImpl implements SendCouponActivityService {

    private Logger logger = LoggerFactory.getLogger(SendCouponActivityServiceImpl.class);

    @Override
    public void sendCoupon(Integer userId,String couponId,Integer rewardId){

        // 0:成功，1：失败
        String result = "1";
        try {
            //发送优惠券
            result = sendCoupon(userId,couponId);
            logger.info("【双十一发放优惠券】用户发放优惠券是否成功0:成功，1：失败 result=" + result);
        } catch (Exception e) {
            logger.error("【双十一发放优惠券】发放优惠券失败......", e);
        }

        //发放失败，就更新 TwoelevenReward 的status 发放状态,改为0,之前默认都是1（0：待发放，1：已发放）
        if("1".equals(result)){
            TwoelevenReward twoelevenReward = new TwoelevenReward();
            twoelevenReward.setId(rewardId);
            twoelevenReward.setStatus(0);
            twoelevenRewardMapper.updateByPrimaryKeySelective(twoelevenReward);
        }
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
        couponParamBean.setSendFlg(40);
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
}
