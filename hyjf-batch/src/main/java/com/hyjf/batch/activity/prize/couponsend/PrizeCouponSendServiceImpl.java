package com.hyjf.batch.activity.prize.couponsend;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.PropertiesConstants;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.CouponUser;
import com.hyjf.mybatis.model.auto.CouponUserExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;

@Service
public class PrizeCouponSendServiceImpl extends BaseServiceImpl implements PrizeCouponSendService{
    public static final Integer TENDER_AMOUNT_BASE = 1000;
    
    /**
     * 
     * 校验并发代金券
     * @author hsy
     * @see com.hyjf.batch.activity.prize.couponsend.PrizeCouponSendService#updateSendCoupon()
     */
    @Override
    public void updateSendCoupon(){
        String activityId = PropUtils
                .getSystem(PropertiesConstants.TENDER_PRIZE_ACTIVITY_ID);
        // 取得活动
        
        ActivityList activity = this.activityListMapper
                .selectByPrimaryKey(Integer.valueOf(activityId));
        // 活动开始时间
        int startDate = activity.getTimeStart();
        // 活动结束时间
        int endDate = activity.getTimeEnd();
        
        //取得活动期注册的用户列表
        UsersExample example = new UsersExample();
        example.createCriteria().andRegTimeBetween(startDate, endDate);
        List<Users> users = usersMapper.selectByExample(example); 
        
        for(Users user : users){
            //未达到出借金额不发券
            if(this.getTenderAccountSum(user.getUserId(), activityId).compareTo(BigDecimal.valueOf(TENDER_AMOUNT_BASE)) < 0){
                continue;
            }
            //已发券的不在重复发券
            if(this.getCouponSendStatus(user.getUserId(), activityId)){
                continue;
            }
            
            JSONObject sendResult = this.sendCoupon(user.getUserId());
            // 发放是否成功状态
            int sendStatus = sendResult.getIntValue("status");
            // 发放优惠券的数量
            int sendCount = sendResult.getIntValue("couponCount");
            if (sendStatus == 0 && sendCount > 0) {
                LogUtil.infoLog(this.getClass().getName(), "updateSendCoupon", "用户ID为：" + user.getUserId() + " 的用户代金券发送成功，发送数量：" + sendCount+ "张");
            }else{
                LogUtil.errorLog(this.getClass().getName(), "updateSendCoupon", "用户ID为：" + user.getUserId() + " 的用户代金券发送失败", null);
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
    public JSONObject sendCoupon(int userId) {
        CommonParamBean paramBean = new CommonParamBean();
        paramBean.setUserId(String.valueOf(userId));

        paramBean.setSendFlg(3);
        // 调用发放优惠券接口
        String jsonStr = CommonSoaUtils.sendUserCoupon(paramBean);
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
