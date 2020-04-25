package com.hyjf.batch.activity.inviteseven.couponsend;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.ActivityInviteSeven;
import com.hyjf.mybatis.model.auto.ActivityInviteSevenExample;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.CouponUser;
import com.hyjf.mybatis.model.auto.CouponUserExample;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;

@Service
public class InviteSevenCouponSendServiceImpl extends BaseServiceImpl implements InviteSevenCouponSendService{
    
    Logger _log = LoggerFactory.getLogger(InviteSevenCouponSendServiceImpl.class);
    
    /**
     * 
     * 校验并发优惠券
     * @author hsy
     */
    @Override
    public void updateSendCoupon(){
        ActivityInviteSevenExample example = new ActivityInviteSevenExample();
        example.createCriteria().andSendFlgEqualTo(0).andRewardTypeNotEqualTo(0);
        List<ActivityInviteSeven> inviteList = activityInviteSevenMapper.selectByExample(example);
        
        for(ActivityInviteSeven invite : inviteList){
            if(invite.getSendFlg() == 1 || invite.getRewardType() == 0){
                continue;
            }
            
            if(StringUtils.isEmpty(invite.getCouponCode())){
                _log.error("优惠券编号不存在");
                continue;
            }
            
            JSONObject sendResult = this.sendCoupon(invite.getUserid(), invite.getCouponCode());
            // 发放是否成功状态
            int sendStatus = sendResult.getIntValue("status");
            // 发放优惠券的数量
            int sendCount = sendResult.getIntValue("couponCount");
            if (sendStatus == 0 && sendCount > 0) {
                invite.setSendFlg(1);
                int result = activityInviteSevenMapper.updateByPrimaryKeySelective(invite);
                if(result <=0){
                    throw new RuntimeException("更新七月份活动优惠券发放状态失败，用户id" + invite.getUserid());
                }
                _log.info("updateSendCoupon", "用户ID为：" + invite.getUserid() + " 的用户优惠期发送成功，发送数量：" + sendCount+ "张");
            }else{
                _log.error("updateSendCoupon", "用户ID为：" + invite.getUserid() + " 的用户优惠券发送失败", null);
            }
        }
    }
    
    /**
     * 
     * 发放优惠券
     * 
     * @author hsy
     * @param userId
     * @return
     */
    public JSONObject sendCoupon(int userId, String couponCode) {
        CommonParamBean paramBean = new CommonParamBean();
        paramBean.setUserId(String.valueOf(userId));
        paramBean.setCouponCode(couponCode);
        
        paramBean.setSendFlg(12);
        _log.info("---------------开始调用发券接口（七月份活动），用户id：" + userId + " couponCode: " + couponCode);
        // 调用发放优惠券接口
        String jsonStr = CommonSoaUtils.sendUserCoupon(paramBean);
        _log.info("--------------调用发券接口结束（七月份活动），用户id：" + userId + " couponCode: " + couponCode);
        JSONObject sendResult = JSONObject.parseObject(jsonStr);

        return sendResult;
    }
    
    /**
     * 
     * 取得活动期内累计出借
     * 
     * @author hsy
     * @param userId
     * @param activityId
     * @return
     */
    public BigDecimal getTenderAccountSum(int userId, String activityId) {
        // 取得活动
        ActivityList activity = this.activityListMapper
                .selectByPrimaryKey(Integer.valueOf(activityId));
        // 活动开始时间
        int startDate = activity.getTimeStart();
        // 活动结束时间
        int endDate = activity.getTimeEnd();
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("userId", userId);
        paraMap.put("status", 1);
        paraMap.put("startDate", startDate);
        paraMap.put("endDate", endDate);
        // 汇消费、尊享汇除外
        paraMap.put("projectType1", 11);
        paraMap.put("projectType2", 8);
        // 取得活动期内出借总额
        BigDecimal accountSum = this.appUserPrizeCodeCustomizeMapper
                .getUserTenderAccountSum(paraMap);
        return accountSum;
    }
    
    /**
     * 
     * 查询该用户是否发送过优惠券
     * 
     * @author hsy
     * @param userId
     * @param activityId
     * @return
     */
    public boolean getCouponSendStatus(int userId, String activityId) {
        CouponUserExample example = new CouponUserExample();
        example.createCriteria().andUserIdEqualTo(userId)
                .andCouponSourceEqualTo(2)
                .andActivityIdEqualTo(Integer.parseInt(activityId));
        List<CouponUser> couponUsers = couponUserMapper
                .selectByExample(example);
        if (couponUsers != null && couponUsers.size() > 0) {
            return true;
        }
        return false;
    }


}
